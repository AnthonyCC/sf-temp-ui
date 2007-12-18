/*
 * DomainRef.java
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
public class DomainRef extends ContentRef {

	public DomainRef(String refName) throws NullPointerException {
		super(EnumAttributeType.DOMAINREF.getName());
		if (refName == null) {
			throw new NullPointerException("Domain Name cannot be null");
		}
		this.refName = refName;
	}

	public String getDomainName() {
		return this.refName;
	}

	public Domain getDomain() {
		return ContentFactory.getInstance().getDomainById(this.refName);
	}

	public boolean equals(Object o) {
		if (!(o instanceof DomainRef)) {
			return false;
		}
		DomainRef dr = (DomainRef) o;
		return this.type.equals(dr.type) && this.refName.equals(dr.refName);
	}

}
