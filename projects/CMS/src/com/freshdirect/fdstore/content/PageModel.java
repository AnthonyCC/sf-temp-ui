package com.freshdirect.fdstore.content;

import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class PageModel extends ContentNodeModelImpl {

	public PageModel(ContentKey key) {
		super(key);
	}

	public String getTitle() {
		return getAttribute("title", "");
	}
	
	public boolean getShowSideNav() {
		return getAttribute("showSideNav", false);
	}
	
    public List<MediaI> getMediaList() {
        return FDAttributeFactory.constructWrapperList(this, "media");
    }

    public List<PageModel> getSubPages() {
        return FDAttributeFactory.constructWrapperList(this, "subPages");
    }
    
    public String getLink() {
    	return "/page.jsp?pageId="+getContentName();
    }
    
}
