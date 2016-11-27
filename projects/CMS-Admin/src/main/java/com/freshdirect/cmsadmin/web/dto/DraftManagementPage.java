package com.freshdirect.cmsadmin.web.dto;

import java.util.List;

import com.freshdirect.cmsadmin.domain.Draft;

public class DraftManagementPage extends BasicPage {

    private List<Draft> drafts;

    public List<Draft> getDrafts() {
        return drafts;
    }

    public void setDrafts(List<Draft> drafts) {
        this.drafts = drafts;
    }

}
