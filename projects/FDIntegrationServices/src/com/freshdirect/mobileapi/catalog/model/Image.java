package com.freshdirect.mobileapi.catalog.model;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.mobileapi.util.MobileApiProperties;

public class Image {

	public enum ImageType {
        THUMB, MEDIUM, LARGE, ZOOM;
    }
	/* categoryImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_c.jpg, 80x80]
	 * detailImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_p.jpg, 150x150]
	 * featureImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_f.jpg, 30x30]
	 * prodImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_c.jpg, 80x80]
	 * zoomImage:Image[null, /media/images/product/hba_two/hba_advil_cldsin_z.jpg, 260x260]
	 */
    public Image() {
    }

    public Image(String source, int height, int width, ImageType type) {
        this(source, height, width);
        setType(type);
    }

    public Image(String source, int height, int width) {
        setSource(source);
        setHeight(height);
        setWidth(width);
        switch(getWidth()) {
        	
        	case 80:setType(ImageType.MEDIUM);
        		break;
        	case 150:setType(ImageType.LARGE);
        		break;
        	case 260:setType(ImageType.ZOOM);
    			break;
    		default:setType(ImageType.THUMB);
    		    break;
        }
    }

    public Image(com.freshdirect.storeapi.content.Image image,ImageType type) {
    	this(StringUtil.isEmpty(image.getPathWithPublishId())?image.getPath():image.getPathWithPublishId(), image.getHeight(),image.getWidth(),type);    		
    }
    
    public Image(com.freshdirect.storeapi.content.Image image) {
    	this(StringUtil.isEmpty(image.getPathWithPublishId())?image.getPath():image.getPathWithPublishId(), image.getHeight(),image.getWidth());    		
    }

    public static Image wrap(com.freshdirect.storeapi.content.Image image) {
    	if (image == null) return null;
    	return new Image(image);
    }

    private ImageType type;

    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    /**
     * Relative path were the image is stored
     */
    private String source;

    /**
     * image width
     */
    private int width;

    /**
     * image height
     */
    private int height;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source != null && source.matches("^/media.*$")) {
            source = MobileApiProperties.getMediaPath() + source;
        }
        this.source = source;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
