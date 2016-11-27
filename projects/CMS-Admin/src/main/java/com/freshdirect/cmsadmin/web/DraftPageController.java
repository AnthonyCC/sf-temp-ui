package com.freshdirect.cmsadmin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cmsadmin.business.DraftService;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftStatus;
import com.freshdirect.cmsadmin.validation.DraftValidator;
import com.freshdirect.cmsadmin.web.dto.DraftManagementPage;

@RestController
public class DraftPageController {

    @Autowired
    private PageDecorator pageDecorator;

    @Autowired
    private DraftValidator draftValidator;

    @Autowired
    private DraftService draftService;

    public static final String DRAFT_PAGE_PATH = "/api/page/draft";
    public static final String DRAFT_ACTION_PATH = "/api/page/draft/{id}";

    /**
     * Load Draft page.
     *
     * @return draft page
     */
    @RequestMapping(value = DRAFT_PAGE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DraftManagementPage loadDraftManagementPage() {
        return populateDraftManagementPage(draftService.loadAllDrafts());
    }

    /**
     * @param draft
     *            Draft entity
     * @param result
     *            autowired BindingResult
     * @return draft page
     */
    @RequestMapping(value = DRAFT_PAGE_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DraftManagementPage createDraft(@RequestBody Draft draft, BindingResult result) {
        draftValidator.validate(draft, result);
        draftService.createDraft(draft);
        return populateDraftManagementPage(draftService.loadAllDrafts());
    }

    /**
     * @param draft
     *            Draft entity
     * @return draft page
     */
    @RequestMapping(value = DRAFT_ACTION_PATH, method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DraftManagementPage updateDraftStatus(@PathVariable("id") Draft draft) {
        draftService.updateDraftStatus(draft, DraftStatus.DROPPED);
        return populateDraftManagementPage(draftService.loadAllDrafts());
    }

    private DraftManagementPage populateDraftManagementPage(List<Draft> drafts) {
        return pageDecorator.decorateDraftManagementPage(new DraftManagementPage(), drafts);
    }
}
