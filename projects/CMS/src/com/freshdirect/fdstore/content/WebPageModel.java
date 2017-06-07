package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public class WebPageModel extends ContentNodeModelImpl {
    private static final long serialVersionUID = 5290889160361822204L;

    public WebPageModel(ContentKey key) {
		super(key);
	}

	public String getPageTitle() {
		return getAttribute("PAGE_TITLE", "Online grocer providing high quality fresh foods and popular grocery and household items at incredible prices delivered to the New York area.");
	}

    public String getFdxPageTitle() {
        return getAttribute("PAGE_TITLE_FDX", "");
    }

	public String getSEOMetaDescription(){
		return getAttribute("SEO_META_DESC", "FreshDirect is the leading online grocery shopping service. We provide fast grocery delivery to your home and office. Order today for delivery tomorrow!");
	}

    public String getFdxSEOMetaDescription(){
        return getAttribute("SEO_META_DESC_FDX", "");
    }
}