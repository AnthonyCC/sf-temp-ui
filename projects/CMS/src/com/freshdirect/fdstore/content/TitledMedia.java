/*
 * TitledMedia.java
 *
 * Created on April 12, 2002, 1:26 PM
 */

package com.freshdirect.fdstore.content;

import java.io.Serializable;

/**
 *
 * @author  rgayle
 * @version 
 */
public class TitledMedia extends Html implements MediaI, Serializable {

    private MediaModel mediaObject;
    private String mediaTitle;
    private String popupSize;
    private String mediaType;

    public TitledMedia(MediaModel media ,String title,String sizeName) {
       this(media, title, sizeName, MediaI.TYPE_TITLEDMEDIA);
    }
   
    protected TitledMedia(MediaModel media, String title, String sizeName, String typeOfMedia) {
        if ( media==null || typeOfMedia==null || (title==null && sizeName==null) ) {
            throw new IllegalArgumentException("Media object,type or title is null.");
        }
        
        this.mediaObject=media;
        this.mediaTitle=title;
        this.popupSize=sizeName;
        this.mediaType = typeOfMedia;
    }
    
   
   //----: Start required methods :----
    public String getPath(){
        return this.mediaObject.getPath(); // return path of the media object..
    }

    public String getMediaType() {
        return this.mediaType;
    }
    
    public int getWidth() {
            return -1;
    }
    public int getHeight() {
            return -1;
    }
    //----: End required Methods :----
    
    public MediaModel getMedia() {
        return this.mediaObject;
    }

    public String getMediaTitle() {
        return this.mediaTitle;
    }
    
    public String getPopupSize() {
        return this.popupSize;
    }

    
    public String getMediaObjectType() {
        return this.mediaObject.getMediaType();
    }
    
   
    public String toString() {
        return "TitledMedia: ["+this.mediaTitle+", "+this.popupSize+", "+mediaObject.toString()+" ]";
    }
   
   	public void setMedia(MediaModel media) {
   		this.mediaObject = media;
   	}
   
   	public void setPopupSize(String sizeName) {
   		this.popupSize = sizeName;
   	}
   	
   	public void setTitle(String title) {
   		this.mediaTitle = title;
   	}
}

