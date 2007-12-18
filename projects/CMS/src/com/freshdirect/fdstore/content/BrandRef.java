/*
 * BrandRef.java
 *
 * Created on March 19, 2002, 10:47 AM
 */

package com.freshdirect.fdstore.content;


/**
 *
 * @author  rgayle
 * @version 
 */
public class BrandRef extends ContentRef {

    /** Creates new BrandRef */
    public BrandRef(String refName) throws NullPointerException{
        super(ContentNodeI.TYPE_BRAND);
        if (refName==null) throw new NullPointerException("Brand Id cannot be null");
        this.refName = refName;
    }

    public String getBrandName() {
            return this.refName;
    }
    
    public BrandModel getBrand() {
        return (BrandModel)ContentFactory.getInstance().getContentNodeByName(this.refName);
    }
    
}
