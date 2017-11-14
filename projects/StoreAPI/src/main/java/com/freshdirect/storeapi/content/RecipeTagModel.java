package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class RecipeTagModel extends ContentNodeModelImpl {

    public RecipeTagModel(ContentKey cKey) {
        super(cKey);
    }

	public Image getTabletImage() {
		return FDAttributeFactory.constructImage(this, "tabletImage");
	}

	public String getTagId() {
		return getAttribute("tagId", "");
	}

}