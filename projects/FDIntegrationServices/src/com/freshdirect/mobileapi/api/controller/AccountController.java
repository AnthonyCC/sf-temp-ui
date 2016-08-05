package com.freshdirect.mobileapi.api.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.mobileapi.api.data.request.PasswordMessageRequest;
import com.freshdirect.mobileapi.api.service.AccountService;
import com.freshdirect.mobileapi.api.service.ConfigurationService;
import com.freshdirect.mobileapi.api.service.PasswordService;
import com.freshdirect.mobileapi.api.validation.PasswordValidator;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.SessionRequest;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

@RestController
public class AccountController {

    private static final String MSG_CHANGE_PASSWORD_TOKEN_EXPIRED = "Sorry, the reset password link has expired, please request a new link.";

    @Autowired
    private AccountService loginService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/checklogin", method = RequestMethod.GET)
    public Message checkLogin(HttpServletRequest request, HttpServletResponse response, SessionRequest sessionRequest) throws FDException {
        SessionUser user = loginService.checkLogin(request, response, sessionRequest.getSource());
        LoggedIn sessionResponse = loginService.createLoginResponseMessage(user);
        sessionResponse.setConfiguration(configurationService.getConfiguration(user.getFDSessionUser()));
        return sessionResponse;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response, SessionRequest sessionRequest) throws IOException {
        loginService.logout(request.getSession(), response);
        response.sendRedirect(getLandingPagePath(request));
    }

    private String getLandingPagePath(HttpServletRequest request) {
        return request.getContextPath() + request.getServletPath() + PageController.HOME_PAGE_PATH + "?" + PageController.REQUESTED_DATE +  "=" + new Date().getTime() / 1000;
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody Message changePassword(HttpServletRequest request, HttpServletResponse response)
            throws FDResourceException, FDAuthenticationException, ErpInvalidPasswordException, JsonParseException, JsonMappingException, IOException {
        PasswordMessageRequest passwordRequest = objectMapper.readValue(request.getParameter("data"), PasswordMessageRequest.class);

        BindingResult passwordErrors = new BeanPropertyBindingResult(passwordRequest, "password", true, 256);
        passwordValidator.validate(passwordRequest, passwordErrors);

        Message passwordResponse = null;
        boolean isTokenExpired = passwordService.isTokenExpired(passwordRequest.getEmail(), passwordRequest.getToken());
        if (isTokenExpired) {
            passwordResponse = Message.createFailureMessage(MSG_CHANGE_PASSWORD_TOKEN_EXPIRED);
        } else {
            passwordService.changeForgotPassword(request.getSession(), passwordRequest.getEmail(), passwordRequest.getPassword());
            FDSessionUser user = loginService.login(request, response, passwordRequest.getEmail(), passwordRequest.getPassword());
            passwordResponse = Message.createSuccessMessage("User changes password successfully.");
            passwordResponse.setConfiguration(configurationService.getConfiguration(user));
        }
        return passwordResponse;
    }
}
