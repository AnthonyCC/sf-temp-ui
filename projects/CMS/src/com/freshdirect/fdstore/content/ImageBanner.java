package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class ImageBanner extends ContentNodeModelImpl {

    private static final long serialVersionUID = 6496621551873294978L;


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
	
	
	public ContentNodeModel getTarget() {	
		return FDAttributeFactory.lookup(this, "Target", null);
	}

}
