/*
 * AttributeRow.java
 *
 * Created on September 18, 2001, 8:03 PM
 */

package com.freshdirect.dataloader.attributes;

/**
 *
 * @author  mrose
 * @version
 */
public class AttributeRow {
    
    public String id;
    public String rootId;
    public String child1Id;
    public String child2Id;
    public String atrType;
    public String atrName;
    public String atrValue;
    
    /** Creates new AttributeRow */
    public AttributeRow() {
        super();
    }
    
    @Override
    public String toString() {
        String out = rootId + " : ";
        out += child1Id + " : ";
        out += child2Id + " : ";
        out += atrType + " : ";
        out += atrName + " : ";
        out += atrValue;
        return out;
    }
    
}
