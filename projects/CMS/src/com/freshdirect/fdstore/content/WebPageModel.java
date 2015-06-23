package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public class WebPageModel extends ContentNodeModelImpl {
	public WebPageModel(ContentKey key) {
		super(key);
	}

	public String getPageTitle() {
		return getAttribute("PAGE_TITLE", "");
	}
	
	public String getSEOMetaDescription(){
		return getAttribute("SEO_META_DESC", "");
	}
}