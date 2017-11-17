package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class BannerModel extends ContentNodeModelImpl {

    public BannerModel(ContentKey key) {
        super(key);
	}

	public String getLocation(){
		return this.getAttribute("location", "EMPTY");
	}

	public Image getImage() {
		return FDAttributeFactory.constructImage(this, "image");
	}

	public ContentNodeModel getLink() {
		return FDAttributeFactory.lookup(this, "link", null);
	}
}
