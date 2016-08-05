package com.freshdirect.mobileapi.api.controller;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.mobileapi.api.data.request.SocialLoginMessageRequest;
import com.freshdirect.mobileapi.api.service.SocialAccountService;
import com.freshdirect.mobileapi.api.validation.SocialRequestValidator;
import com.freshdirect.mobileapi.api.validation.SocialUserProfileValidator;
import com.freshdirect.mobileapi.model.SessionUser;

@RestController
public class SocialAccountController {

    @Autowired
    private SocialAccountService socialAccountService;

    @Autowired
    private SocialRequestValidator socialRequestValidator;

    @Autowired
    private SocialUserProfileValidator socialUserProfileValidator;

    // Callback method of oneall service, connectionToken comes from there 
    @RequestMapping(value = "/sociallogin", method = RequestMethod.POST)
    public ResponseEntity<Void> login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SocialLoginMessageRequest socialRequest) throws Exception {
        BindingResult socialRequestErrors = new BeanPropertyBindingResult(socialRequest, "socialRequest", true, 256);
        socialRequestValidator.validate(socialRequest, socialRequestErrors);
        Map<String, String> socialUserProfile = socialAccountService.getSocialUserProfile(socialRequest.getConnection_token());
        BindingResult socialUserProfileErrors = new BeanPropertyBindingResult(socialUserProfile, "socialUserProfile", true, 256);
        socialUserProfileValidator.validate(socialUserProfile, socialUserProfileErrors);

        SessionUser user = socialAccountService.signInSocialUser(request, response, socialUserProfile);
        if (user == null) {
            user = socialAccountService.registerSocialUser(request, response, socialUserProfile);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI(socialRequest.getRedirect_url()));
        return new ResponseEntity<Void>(null, responseHeaders, HttpStatus.FOUND);
    }

}
