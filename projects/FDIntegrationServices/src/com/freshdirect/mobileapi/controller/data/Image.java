package com.freshdirect.mobileapi.controller.data;

/**
 * Media file for images, related to product, logos, maps, etc. The width and height may be not available for every image
 * @author fgarcia
 *
 */
public class Image {

    public Image() {        
    }
    
    public Image(String source, int height, int width) {
        this.source = source;
        this.height = height;
        this.width = width;
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
