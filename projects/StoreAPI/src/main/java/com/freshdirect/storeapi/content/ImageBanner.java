package com.freshdirect.storeapi.content;

import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

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

    public String getLinkOneText() {
        return this.getAttribute("linkOneText", "");
    }

    public ContentNodeModel getLinkOneTarget() {
        return FDAttributeFactory.lookup(this, "linkOneTarget", null);
    }

    public String getLinkOneURL() {
        return this.getAttribute("linkOneURL", "");
    }

    public String getLinkOneType() {
        return this.getAttribute("linkOneType", "");
    }

    public String getLinkTwoText() {
        return this.getAttribute("linkTwoText", "");
    }

    public ContentNodeModel getLinkTwoTarget() {
        return FDAttributeFactory.lookup(this, "linkTwoTarget", null);
    }

    public String getLinkTwoURL() {
        return this.getAttribute("linkTwoURL", "");
    }

    public String getLinkTwoType() {
        return this.getAttribute("linkTwoType", "");
    }

    @SuppressWarnings("unchecked")
    public List<Image> getBurstBannerImages() {
        return FDAttributeFactory.constructWrapperList(this, "ImageBannerBurst");
    }

}
