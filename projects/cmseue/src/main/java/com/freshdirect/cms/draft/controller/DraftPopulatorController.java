package com.freshdirect.cms.draft.controller;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cms.draft.domain.Draft;
import com.freshdirect.cms.draft.service.DraftService;

@Profile("database")
@RestController
@RequestMapping(value = "/draft")
public class DraftPopulatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftPopulatorController.class);
    private static final String CMS_DRAFT_ID_SESSION_NAME = "draft";
    private static final Random RANDOM = new Random();

    @Autowired
    private DraftService draftService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Draft> getDrafts() {
        List<Draft> drafts = draftService.getDrafts();
        return drafts;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void setupDraftContext(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String draftId = request.getParameter(CMS_DRAFT_ID_SESSION_NAME);
        Long parsedDraftId;
        try {
            parsedDraftId = Long.parseLong(draftId);
            draftService.setupDraftContext(parsedDraftId, request);
        } catch (NumberFormatException e) {
            LOGGER.info("Can not set to draftcontext given draftId: " + draftId, e);
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/cmsgwt.html?rnd=" + RANDOM.nextInt()));
    }
}
