package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.storeapi.content.CMSImageBannerModel;
import com.freshdirect.storeapi.content.CMSWebPageModel;

public class PageMessageResponse extends MessageResponse {

    private static final long serialVersionUID = -8847236863519683899L;

    private CMSWebPageModel page;
    private CMSWebPageModel pick;
    private List<CMSImageBannerModel> welcomeCarouselBanners;

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

    public List<CMSImageBannerModel> getWelcomeCarouselBanners() {
        return welcomeCarouselBanners;
    }

    public void setWelcomeCarouselBanners(List<CMSImageBannerModel> welcomeCarouselBanners) {
        this.welcomeCarouselBanners = welcomeCarouselBanners;
    }
}
