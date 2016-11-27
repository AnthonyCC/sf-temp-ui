package com.freshdirect.cmsadmin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cmsadmin.business.DraftChangeService;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;

@RestController
public class DraftChangeController {

    public static final String CMS_DRAFT_CHANGE_PATH = "/api/draftchange";
    public static final String CMS_DRAFT_CHANGE_ACTION_PATH = "/api/draftchange/draft/{id}";

    @Autowired
    private DraftChangeService draftChangeService;

    /**
     * Load all draft changes
     * 
     * @return list of draftChanges
     */
    @RequestMapping(value = CMS_DRAFT_CHANGE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DraftChange> loadDraftChanges() {
        return draftChangeService.loadAllDraftChanges();
    }

    /**
     * Create draftChange
     * 
     * @param draftChange
     *            DraftChange entity
     * @return list of draftChanges
     */
    @RequestMapping(value = CMS_DRAFT_CHANGE_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DraftChange> createDraftChange(@RequestBody List<DraftChange> draftChange) {
        draftChangeService.createDraftChange(draftChange);
        return draftChangeService.loadAllDraftChanges();
    }

    @RequestMapping(value = CMS_DRAFT_CHANGE_ACTION_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DraftChange> loadDraftChangesByDraft(@PathVariable("id") Draft draft) {
        return draftChangeService.loadAllChangesByDraft(draft);
    }


}
