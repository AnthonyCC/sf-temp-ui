/*
 * DomainValueRef.java
 *
 * Created on May 10, 2002, 030:40 PM
 */
package com.freshdirect.fdstore.content;

import com.freshdirect.fdstore.attributes.EnumAttributeType;

/**
 *
 * @author  rgayle
 * @version 
 */
public class DomainValueRef extends ContentRef {

    public DomainValueRef(String refName,String refName2)  throws NullPointerException {
            super(EnumAttributeType.DOMAINVALUEREF.getName());
            if (refName==null) throw new NullPointerException("Domain Name cannot be null");
            if (refName2==null) throw new NullPointerException("DomainValue cannot be null");

            this.refName = refName;
            this.refName2 = refName2;
     }
    
    public String getDomainName() {
            return this.refName;
    }
    public Domain getDomain() {
        return ContentFactory.getInstance().getDomainById(this.refName);
    }
    
    public String toString() {
            return "ContentRef["+this.type+", "+this.refName+(this.refName2!=null?">"+this.refName2:"")+"]";
    }
    
    public DomainValue getDomainValue() {
    	return ContentFactory.getInstance().getDomainValueById(this.refName2);
    }

    public boolean equals(Object o) {
            if (!(o instanceof DomainValueRef)) {
                    return false;
            }
            DomainValueRef dvr = (DomainValueRef)o;
            return
                this.type.equals(dvr.type) && 
                this.refName.equals( dvr.refName ) && 
                this.refName2.equals(dvr.refName2) ;
    }
    
 
}
