package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class ImageBanner extends ContentNodeModelImpl {

	public ImageBanner(ContentKey key) {
		super(key);
	}
	
	public String getType(){
		return this.getAttribute("Type", "");
	}
	
	public String getName(){
		return this.getAttribute("Name", "");
	}
	
	public String getDescription(){
		return this.getAttribute("Description", "");
	}
	
	public String getFlagText(){
		return this.getAttribute("FlagText", "");
	}
	
	public String getFlagColor(){
		return this.getAttribute("FlagColor", "");
	}
	
	public String getPrice(){
		return this.getAttribute("Price", "EMPTY");
	}
	
	public Image getImageBannerImage() {
		return FDAttributeFactory.constructImage(this, "ImageBannerImage");
	}
	
	
	public ProductModelImpl getImageBannerProduct() {	
		return FDAttributeFactory.lookup(this, "ImageBannerProduct", null);
	}

}
