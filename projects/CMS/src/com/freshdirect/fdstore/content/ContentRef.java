/*
 * $Workfile$
 *
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.content;

import java.io.Serializable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class ContentRef implements Serializable {
	protected final String type;
	protected  String refName;
	protected  String refName2;
	protected String refName3;

	public ContentRef(String ContentType) {
		this.type = ContentType;
	}
       
	public String toString() {
		return "ContentRef["+this.type+", "+this.refName+(this.refName2!=null?", "+this.refName2:"")+(this.refName3!=null?", "+this.refName3:"")+"]";
	}

	public int hashCode() {
		return this.type.hashCode() ^
				this.refName.hashCode() ^ 
				(this.refName2==null ? 0 : this.refName2.hashCode()) ^
				(this.refName3==null ? 0 : this.refName3.hashCode())    ;
	}
        
	public String getRefName() {
		return this.refName;
	}
        
	public String getRefName2() {
		return this.refName2;
	}
	
	public String getRefName3() {
		return this.refName3;
	}
        
    public String getType() {
    	return this.type;
    }
        
	public boolean equals(Object o) {
		if (!(o instanceof ContentRef)) {
			return false;
		}
		ContentRef cr = (ContentRef)o;
		return
			this.type.equals(cr.type) && 
			this.refName.equals( cr.refName ) &&
			( this.refName2==null ?
				cr.refName2==null :
				this.refName2.equals( cr.refName2 ) 
			)  &&
			( this.refName3==null ?
				cr.refName3==null :
				this.refName3.equals( cr.refName3 ) 
			);
	}

}