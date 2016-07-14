package com.freshdirect.cmsadmin.config.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.config.security.dto.AuthenticationFailureData;

/**
 * Custom authentication failure handler.
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        AuthenticationFailureData failureData = new AuthenticationFailureData();
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("general", "Bad Credentials. Try again!");
        failureData.setErrors(errors);
        converter.getObjectMapper().writeValue(response.getOutputStream(), failureData);
    }

}
