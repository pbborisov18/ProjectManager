package com.company.projectManager.common.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Value("${frontend.url}")
    private String frontendUrl;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                /*
                I read too much contradicting info on using httpOnly true cookie
                Some say it's useless cuz to exploit not having httpOnly you need an xss exploit with which you can do worse things
                Others say you need to use it cuz it's more secure
                And since having httpOnly on feels like an impossible scenario to solve (I need it in the frontend but can't access it so I don't have a token to send back)
                soooo I'll just disable httpOnly

                Also turns out the frontend also has protection against csrf. no idea how it works tho as it is the one sending requests to the backend.
                On second thought I don't think it does as I'm only using it as a frontend. dunno
                */
                //I can't figure out how to get the csrf token in the frontend
                //I'm not looking to do the "send a request on x endpoint which exposes the token before the actual request"
                //And I couldn't find a better solution
                .csrf(c -> c.disable())
//                        ( c ->  c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //this puts the csrf token in the cookies
//                                // no idea wtf this does. Can't read the X-XSRF-TOKEN header in the request without it
//                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))

                .cors(c -> c.configurationSource(
                        r -> {
                            CorsConfiguration configuration = new CorsConfiguration();

                            configuration.applyPermitDefaultValues();
                            //Letting the frontend do whatever types of methods it wants
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
                            configuration.setAllowedOriginPatterns(List.of(frontendUrl));
                            configuration.setAllowCredentials(true);
//                                configuration.setAllowedHeaders(List.of("Access-Control-Allow-Credentials", "Access-Control-Expose-Headers", "Access-Control-Allow-Headers", "Access-Control-Allow-Methods", "Access-Control-Allow-Origin", "content-type", "x-xsrf-token"));
//                                configuration.setExposedHeaders(List.of("Access-Control-Allow-Credentials", "Access-Control-Expose-Headers", "Access-Control-Allow-Headers", "Access-Control-Allow-Methods", "Access-Control-Allow-Origin"));

                            return configuration;
                        }
                ))

                .formLogin(login -> login
                        .loginPage(frontendUrl+"/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().flush();
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.BAD_REQUEST.value());
                            response.setContentType("text/plain");
                            response.getWriter().write("Incorrect email or password!");
                        }))

                .rememberMe(me -> me
                        .key("RandomAssPrivateKey")
                        .rememberMeParameter("rememberme")
                        .userDetailsService(userDetailsService)
                )

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/login", "/register", "/*.html", "/style/**", "/error", "/favicon").permitAll()
                        .anyRequest().authenticated())

                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("text/plain");
                            response.getWriter().write("Unauthenticated!");
                        }))


                .logout(logout -> logout.
                        logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().flush();
                        })
                        .logoutSuccessUrl(frontendUrl + "/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))

                .build();
    }

}
