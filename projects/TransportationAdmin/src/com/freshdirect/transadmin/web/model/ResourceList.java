package com.freshdirect.transadmin.web.model;

import java.util.Collection;
import java.util.List;

public class ResourceList extends java.util.ArrayList {
	
	private ResourceReq resourceReq;
	
	
	public ResourceList(int size) {
		super(size);
	}
	
	public ResourceList(Collection data) {
		super(data);
	}
	public ResourceList() {
		super();
	}

	/**
	 * @return the resourceReq
	 */
	public ResourceReq getResourceReq() {
		return resourceReq;
	}

	/**
	 * @param resourceReq the resourceReq to set
	 */
	public void setResourceReq(ResourceReq resourceReq) {
		this.resourceReq = resourceReq;
	}
	
	public void clear() {
		
		super.clear();
		if(resourceReq!=null) {
			resourceReq.setMax(new Integer(0));
			resourceReq.setReq(new Integer(0));
		}
	}
	
	

}
