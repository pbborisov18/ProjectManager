package com.company.projectManager.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .csrf().disable()
                .cors(Customizer.withDefaults())

                .formLogin()
                .loginPage("http://localhost:5176/login")
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
                .usernameParameter("email")
                .and()


                .authorizeHttpRequests()
                .requestMatchers("/", "/login", "/register", "/*.html", "/style/**","/error", "/favicon", "/sign-up").permitAll()
                .anyRequest().authenticated()
                .and()


                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                .and()
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType("text/plain");
                        response.getWriter().write("Unauthenticated!");
                })


                .and()
                .logout()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().flush();
                })
                .logoutSuccessUrl("http://localhost:5176/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSION")
                .and()

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Credentials",
                "Access-Control-Expose-Headers",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Origin",
                "content-type"));
        configuration.setExposedHeaders(List.of("Access-Control-Allow-Credentials", "Access-Control-Expose-Headers", "Access-Control-Allow-Headers", "Access-Control-Allow-Methods", "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


//    @Bean
//    static RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = "*:MANAGER > *:EMPLOYEE";
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }

}
