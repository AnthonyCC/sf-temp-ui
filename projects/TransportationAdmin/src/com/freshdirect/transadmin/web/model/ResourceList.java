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
	
	public boolean add(ResourceInfoI o)  {
		return super.add(o);
	}
	
	public void add(int index, ResourceInfoI element) {
		 super.add(index, element);
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
    		Object obj=it.next();
    		if(obj instanceof ResourceInfoI) {
    			resourceInfo=(ResourceInfoI)obj;  
    		}
    		if(resourceInfo!=null && (resourceInfo.getLastName()!=null || resourceInfo.getFirstName()!=null)) 
    		{
    			String star="";
    			if(resourceInfo.getAdjustmentTime()!=null) star="#";
    			buf.append(star+resourceInfo.getLastName()+" "+resourceInfo.getFirstName()+" "+resourceInfo.getNextelNo());    			    		
    		}
    	}	    	
    	return buf.toString();
		
	}

	public int compareTo(Object o) {
		ResourceList other=(ResourceList)o;
		return this.toString().compareTo(other.toString());
	} 
	
	

}
