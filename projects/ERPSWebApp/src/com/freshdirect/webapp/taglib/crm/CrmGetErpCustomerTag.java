package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetErpCustomerTag extends AbstractGetterTag {
	
	private FDUserI user;
	private static final String ERP_CUSTOMER_PAGE_CACHE = "erpCustomer.page.cache";
	
	public void setUser(FDUserI user){
		this.user = user;
	}

	protected Object getResult() throws Exception {
		try{
			if(this.user == null || this.user.getIdentity() == null){
				throw new JspException("No id provided customer");
			}
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			ErpCustomerModel c = (ErpCustomerModel) request.getAttribute(ERP_CUSTOMER_PAGE_CACHE);
			if(c == null || !c.getPK().getId().equals(user.getIdentity().getErpCustomerPK())){
				c = FDCustomerFactory.getErpCustomer(this.user.getIdentity());
				request.setAttribute(ERP_CUSTOMER_PAGE_CACHE, c);
			}
			return c;
		}catch(FDResourceException e){
			throw new JspException(e);
		}
	}
	
	public static  class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType(){
			return "com.freshdirect.customer.ErpCustomerModel";
		}
	}
}
