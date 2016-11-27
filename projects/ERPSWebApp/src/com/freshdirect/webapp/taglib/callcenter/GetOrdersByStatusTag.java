/*
 * 
 * GetOrderByStatusTag.java
 * Date: Nov 13, 2002 Time: 12:42:09 PM
 */
package com.freshdirect.webapp.taglib.callcenter;

/**
 * 
 * @author knadeem
 */

import com.freshdirect.customer.EnumSaleStatus;

import com.freshdirect.fdstore.*;

import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class GetOrdersByStatusTag extends AbstractGetterTag {
	
	private EnumSaleStatus status = null;
	
	public void setStatus(EnumSaleStatus status){
		this.status = status;
	}
	
	protected Object getResult() throws FDResourceException {

		return CallCenterServices.getOrdersByStatus(this.status);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List";
		}

	}

}
