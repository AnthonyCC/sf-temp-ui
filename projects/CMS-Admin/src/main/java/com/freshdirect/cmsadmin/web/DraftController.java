package com.freshdirect.cmsadmin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cmsadmin.business.DraftService;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftStatus;

@RestController
public class DraftController {

    public static final String CMS_DRAFT_PATH = "/api/draft";
    public static final String CMS_DRAFT_ACTION_PATH = "/api/draft/{id}";

    @Autowired
    private DraftService draftService;

    /**
     * Load all drafts.
     *
     * @return list of drafts
     */
    @CrossOrigin
    @RequestMapping(value = CMS_DRAFT_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Draft> loadDraftManagementPageForCms() {
        return draftService.loadNotDroppedOrMergedDrafts();
    }

    /**
     * @param draft
     *            Draft entity
     * @return list of remaining drafts
     * 
     */

    @RequestMapping(value = CMS_DRAFT_ACTION_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Draft> updateDraftStatusForCms(@PathVariable("id") Draft draft, @RequestBody DraftStatus draftStatus) {
        draftService.updateDraftStatus(draft, draftStatus);
        return draftService.loadAllDrafts();
    }

}
