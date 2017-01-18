package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public class WebPageModel extends ContentNodeModelImpl {
    private static final long serialVersionUID = 5290889160361822204L;

    public WebPageModel(ContentKey key) {
		super(key);
	}

	public String getPageTitle() {
		return getAttribute("PAGE_TITLE", "");
	}

    public String getFdxPageTitle() {
        return getAttribute("PAGE_TITLE_FDX", "");
    }

	public String getSEOMetaDescription(){
		return getAttribute("SEO_META_DESC", "");
	}

    public String getFdxSEOMetaDescription(){
        return getAttribute("SEO_META_DESC_FDX", "");
    }
}