package com.freshdirect.webapp.taglib.crm;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetFdCustomerTag extends AbstractGetterTag {
	
	private FDUserI user;
	
	public void setUser(FDUserI user){
		this.user = user;
	}

	protected Object getResult() throws Exception {
		try{
			if(user == null || user.getIdentity().getErpCustomerPK() == null) {
				throw new JspException("No id provided customer");
			}
			return user.getFDCustomer();
		}catch(FDResourceException e){
			throw new JspException(e);
		}
	}
	
	public static  class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType(){
			return "com.freshdirect.fdstore.customer.FDCustomerModel";
		}
	}
}
