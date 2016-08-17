package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.fdstore.content.CMSWebPageModel;

public class PageMessageResponse extends MessageResponse {

    private static final long serialVersionUID = -8847236863519683899L;

    private CMSWebPageModel page;
    private CMSWebPageModel pick;

    public CMSWebPageModel getPage() {
        return page;
    }

    public void setPage(CMSWebPageModel page) {
        this.page = page;
    }

    public CMSWebPageModel getPick() {
        return pick;
    }

    public void setPick(CMSWebPageModel pick) {
        this.pick = pick;
    }

}
