package com.freshdirect.mobileapi.controller.data;

import com.freshdirect.mobileapi.util.MobileApiProperties;

/**
 * Media file for images, related to product, logos, maps, etc. The width and height may be not available for every image
 * @author fgarcia
 *
 */
public class Image {

    public enum ImageSizeType {
        THUMB, MEDIUM, LARGE
    }

    public Image() {
    }

    public Image(String source, int height, int width, ImageSizeType type) {
        this(source, height, width);
        setType(type);
    }

    public Image(String source, int height, int width) {
        setSource(source);
        setHeight(height);
        setWidth(width);
    }

    private ImageSizeType type;

    public ImageSizeType getType() {
        return type;
    }

    public void setType(ImageSizeType type) {
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
