package com.freshdirect.cmsadmin.config.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.config.security.dto.AuthenticationSuccessData;
import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.MenuItem;

/**
 * Custom authentication success handler.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        AuthenticationSuccessData successData = new AuthenticationSuccessData();
        UserData user = new UserData();
        user.setName(authentication.getName());
        successData.setUser(user);
        successData.setDefaultPath(MenuItem.DEFAULT_PAGE.getPath());
        converter.getObjectMapper().writeValue(response.getOutputStream(), successData);
    }

}
