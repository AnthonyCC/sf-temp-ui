/*
 * MediaI.java
 *
 * Created on November 15, 2002, 6:35 PM
 */

package com.freshdirect.fdstore.content;

/**
 *
 * @author  rgayle
 * @version 
 */
public interface MediaI {
    public String getPath();
    public String getMediaType();
    public int getWidth();
    public int getHeight();
    public final static String TYPE_IMAGE = "I";
    public final static String TYPE_HTML = "H";
    public final static String TYPE_TITLEDMEDIA = "T";
    public final static String TYPE_ARTICLEMEDIA = "A";
}