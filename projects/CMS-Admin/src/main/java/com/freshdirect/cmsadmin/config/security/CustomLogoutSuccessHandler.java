package com.freshdirect.cmsadmin.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.domain.MenuItem;
import com.freshdirect.cmsadmin.web.dto.DefaultPage;

/**
 * Handles successful logout and send the custom data object.
 *
 * @author pkovacs
 *
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultPage defaultPage = new DefaultPage();
        List<MenuItem> menuItems = new ArrayList<MenuItem>(1);
        menuItems.add(MenuItem.DEFAULT_PAGE);
        defaultPage.setMenuItems(menuItems);
        converter.getObjectMapper().writeValue(response.getOutputStream(), defaultPage);
    }

}
