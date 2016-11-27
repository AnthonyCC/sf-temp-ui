package com.freshdirect.mobileapi.controller.data;

public class ImageBanner {

    public static ImageBanner wrap(com.freshdirect.fdstore.content.ImageBanner source) {
        
        final ImageBanner imgBanner = new ImageBanner();
        
        imgBanner.setText(source.getDescription());
        imgBanner.setFlagColor(source.getFlagColor());
        imgBanner.setFlagText(source.getFlagText());
        imgBanner.setPrice(source.getPrice());
        if (source.getImageBannerImage() != null) {
            imgBanner.setImage(new Image(source
                     .getImageBannerImage()).getSource());
        }
        if (source.getTarget() != null
                && source.getTarget().getContentKey() != null) {
            imgBanner.setTargetTypeId(source
                     .getTarget().getContentKey().getEncoded());
        }
        
        return imgBanner;
    }


	public ImageBanner() {}


    public ImageBanner(String image, String text, String targetTypeId, String flagText, String flagColor, String price) {
       setImage(image);
       setText(text);
       setTargetTypeId(targetTypeId);
       setFlagText(flagText);
       setFlagColor(flagColor);
       setPrice(price);
    }

    private String image; 
    private String text;
    private String targetTypeId; 
    private String flagText;
    private String flagColor;
    private String price;
    
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTargetTypeId() {
		return targetTypeId;
	}

	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	public String getFlagText() {
		return flagText;
	}

	public void setFlagText(String flagText) {
		this.flagText = flagText;
	}

	public String getFlagColor() {
		return flagColor;
	}

	public void setFlagColor(String flagColor) {
		this.flagColor = flagColor;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
    
    
}
