package com.luga_online.util;

import com.luga_online.service.AuthService;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VkCustomFilter extends AbstractAuthenticationProcessingFilter {

    private final OAuth2RestOperations restTemplate;

    private final AuthService authService;

    public VkCustomFilter(AuthService authService, OAuth2RestTemplate restTemplate) {
        super("/login");
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
        try {
            return authService.setAuthorized(accessToken, request);
        } catch (InvalidTokenException e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain user details from token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
    }

    private void publish(ApplicationEvent event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }
}
