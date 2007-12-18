/**
 * @author ekracoff
 * Created on May 10, 2005*/

package com.freshdirect.webapp.taglib.callcenter;

import java.util.Collections;

import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class ZipPlus4ExceptionSearchTag extends AbstractGetterTag{
	private String address;
	private String zipcode;
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getZipcode() {
		return zipcode;
	}
	
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	protected Object getResult() throws Exception {
		if("searchAddressExceptions".equals(pageContext.getRequest().getParameter("action"))){
			if(address != null || zipcode != null){
				ExceptionAddress ea = new ExceptionAddress();
				ea.setStreetAddress(address);
				ea.setZip(zipcode);
				return FDDeliveryManager.getInstance().searchExceptionAddresses(ea);
			}
		} else if("searchGeocodeExceptions".equals(pageContext.getRequest().getParameter("action"))){
			if(address !=null || zipcode !=null){
				ExceptionAddress ea = new ExceptionAddress();
				ea.setScrubbedAddress(address);
				ea.setZip(zipcode);
				return FDDeliveryManager.getInstance().searchGeocodeException(ea);
			}
		}
		
		return Collections.EMPTY_LIST;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}

}
