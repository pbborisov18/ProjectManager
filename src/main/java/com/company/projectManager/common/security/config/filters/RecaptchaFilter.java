package com.company.projectManager.common.security.config.filters;

import com.company.projectManager.common.dto.RecaptchaAnswerDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RecaptchaFilter extends OncePerRequestFilter {

    @Value("${recaptcha.secretKey}")
    private String secretKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //Skip the filter when no secret key is set
        if (secretKey == null || secretKey.isEmpty()){
            return true;
        }

        String path = request.getRequestURI();
        //Send only during login. If you send more than 1 request from the same page you get the timeout-or-duplicate error
        //Since I'm sending register and login request right afterwards I'm using the same token from the same page.
        //So I'll check only for login
        return !(path.equals("/login") /*|| path.equals("/register")*/);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String recaptchaResponse = request.getHeader("Recaptcha-Token");

        boolean isValid = validateRecaptcha(recaptchaResponse);

        if (!isValid) {
            // Respond with an error and stop the filter chain
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Recaptcha failed!");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateRecaptcha(String token) {
        RestClient customClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory())
                .baseUrl("https://www.google.com")
                .build();

        RecaptchaAnswerDTO answer = customClient.post()
                .uri("/recaptcha/api/siteverify?secret="+secretKey+"&response="+token) //Yes I know this is stupid
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(0)
                .retrieve()
                .body(RecaptchaAnswerDTO.class);

        return answer != null && answer.success();
    }



}
