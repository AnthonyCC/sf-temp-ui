package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Nakkeeran Annamalai
 *
 */
public class WebPageResponse extends Message {

    private CMSWebPageModel page;

    public CMSWebPageModel getPage() {
        return page;
    }

    public void setPage(CMSWebPageModel page) {
        this.page = page;
    }
}