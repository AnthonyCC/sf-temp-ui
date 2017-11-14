package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.storeapi.content.CMSWebPageModel;

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