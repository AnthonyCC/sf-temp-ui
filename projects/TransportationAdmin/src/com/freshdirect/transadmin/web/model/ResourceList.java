package com.freshdirect.transadmin.web.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.transadmin.model.ResourceInfoI;

public class ResourceList extends java.util.ArrayList implements Comparable {
	
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
	
	public String toString(){
		
	   	StringBuffer buf=new StringBuffer();
		if(resourceReq!=null)
			buf.append(this.resourceReq.getReq()).append("/").append(this.resourceReq.getMax());
	   	
		Iterator it=this.iterator();
    	ResourceInfoI resourceInfo=null;
    	while(it.hasNext()) {
    		resourceInfo=(ResourceInfoI)it.next();    		    		    		
    		if(resourceInfo.getLastName()!=null || resourceInfo.getFirstName()!=null) {
    			buf.append(resourceInfo.getLastName()+" "+resourceInfo.getFirstName());    			    		
    		}
    	}	    	
    	return buf.toString();
		
	}

	public int compareTo(Object o) {
		ResourceList other=(ResourceList)o;
		return this.toString().compareTo(other.toString());
	} 
	
	

}
