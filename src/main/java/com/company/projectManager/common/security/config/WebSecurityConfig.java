package com.company.projectManager.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf().disable()
                .cors(Customizer.withDefaults())

                                return configuration;
                            }
                ))

                .formLogin(login -> {
                    login.loginPage("http://localhost:5176/login")
                            .loginProcessingUrl("/login")
                            .successHandler((request, response, authentication) -> {
                                response.setStatus(HttpStatus.OK.value());
                                response.getWriter().flush();
                            })
                            .failureHandler((request, response, exception) -> {
                                response.setStatus(HttpStatus.BAD_REQUEST.value());
                                response.setContentType("text/plain");
                                response.getWriter().write("Incorrect email or password!");
                            })
                            .usernameParameter("email");
                })




                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/", "/login", "/register", "/*.html", "/style/**","/error", "/favicon", "/sign-up").permitAll()
                            .anyRequest().authenticated();
                })

                .exceptionHandling(eh -> {
                    eh.authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("text/plain");
                        response.getWriter().write("Unauthenticated!");
                    });

                })


                .logout(logout -> {
                    logout.logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpStatus.OK.value());
                        response.getWriter().flush();
                    })
                            .logoutSuccessUrl("http://localhost:5176/login")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID");
                })

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
