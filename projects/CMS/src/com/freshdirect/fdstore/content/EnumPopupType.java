/*
 * EnumPopupSize.java
 *
 * Created on May 3, 2002, 4:48 PM
 */

package com.freshdirect.fdstore.content;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author  rgayle
 * @version 
 */
public class EnumPopupType implements Serializable {
    private String Type = null;
    private int width = 0;
    private int height = 0;

    // popups without menus/toolbars/location window
    public final static EnumPopupType SMALL = new EnumPopupType("small",335,375);
    public final static EnumPopupType LARGE = new EnumPopupType("large",400,585);

    private static List types = null;
    
    static {
        ArrayList t = new ArrayList();
        t.add(SMALL);
        t.add(LARGE);
        types = Collections.unmodifiableList(t);
    }
    
    public static List gePopupTypes() {
        return types;
    }
    
    public static EnumPopupType getPopupType(String puName) {
        for (int i=0;i < types.size();i++) {
            EnumPopupType pt = (EnumPopupType) types.get(i);
            if (pt.getType().equalsIgnoreCase(puName))
                return pt;
        }
        return null;
    }


    /** Creates new EnumPopupSize */
    public EnumPopupType(String sizeType, int sizeHeight, int sizeWidth) {
        this.Type = sizeType;
        this.width = sizeWidth;
        this.height = sizeHeight;
    }
    
    public int getWidth(){
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    public String getType() {
        return this.Type;
    }
  
    public boolean equals(Object o) {
        if (o instanceof EnumPopupType) {
            return this.getType().equals(((EnumPopupType)o).getType());
        } else return false;
    }
    
}
