/* Generated by Together */

package com.freshdirect.storeapi.content;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;


abstract public class MediaModel extends ModelSupport implements MediaI{
    
    public MediaModel() {
        super();
    }
    
    public String getPath(){
        return path;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public String getMediaType() {
        return mediaType;
    }
    
    public void setPK(PrimaryKey pk) {
        super.setPK(pk);
    }
   
    public int getWidth() {
            return -1;
    }
    public int getHeight() {
            return -1;
    }
    
    private String path;
    protected String mediaType;
}