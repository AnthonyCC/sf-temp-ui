/**
 * @author ekracoff
 * Created on Apr 25, 2005*/

package com.freshdirect.ocf.core;

import java.io.Serializable;

public abstract class Entity implements Serializable {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}