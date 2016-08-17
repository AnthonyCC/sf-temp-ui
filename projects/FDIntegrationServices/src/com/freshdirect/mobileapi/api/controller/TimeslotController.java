package com.freshdirect.mobileapi.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.api.data.request.TimeslotMessageRequest;
import com.freshdirect.mobileapi.api.service.AccountService;
import com.freshdirect.mobileapi.api.service.ConfigurationService;
import com.freshdirect.mobileapi.api.service.TimeslotService;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots;
import com.freshdirect.mobileapi.model.SessionUser;

@RestController
public class TimeslotController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private TimeslotService timeslotService;

    @RequestMapping(value = "/timeslot", method = RequestMethod.GET)
    public Message checkLogin(HttpServletRequest request, HttpServletResponse response, TimeslotMessageRequest timeslotRequest) throws FDException {
        SessionUser user = accountService.getSessionUser(request, response, timeslotRequest.getSource());
        DeliveryTimeslots timeslotResponse = timeslotService.getDeliveryTimeslots(user, timeslotRequest.getAddressId());
        timeslotResponse.setConfiguration(configurationService.getConfiguration(user.getFDSessionUser()));
        timeslotResponse.setStatus(Message.STATUS_SUCCESS);
        return timeslotResponse;
    }
}
