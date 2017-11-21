package com.freshdirect.cms.changecontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cms.changecontrol.domain.ChangePropagationData;
import com.freshdirect.cms.changecontrol.service.ChangePropagatorService;

@RestController
@Profile("database")
public class ChangePropagatorController {

    @Autowired
    private ChangePropagatorService changePropagatorService;

    @RequestMapping(value = "${cms.eventlisten.path:/api/contentchanged/}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void contentChanged(@RequestBody ChangePropagationData changeData) {
        changePropagatorService.receiveContentChangedNotification(changeData.getContentKeys(), changeData.getDraftContext());
    }
}
