package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

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
