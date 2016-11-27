package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class RecipeTagModel extends ContentNodeModelImpl {

	public RecipeTagModel(ContentKey key) {
		super(key);
	}

	public Image getTabletImage() {
		return FDAttributeFactory.constructImage(this, "tabletImage");
	}
	
	public String getTagId() {
		return getAttribute("tagId", "");
	}
	
}