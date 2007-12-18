package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmGetFDUserTag extends AbstractGetterTag {
	
	private String erpCustId;
	private String fdCustId;
	private boolean useId;
	
	public void setErpCustId(String erpCustId) {
		this.erpCustId = erpCustId;
	}
	
	public void setFdCustId(String fdCustId) {
		this.fdCustId = fdCustId;
	}
	
	public void setUseId(boolean useId) {
		this.useId = useId;
	}

	protected Object getResult() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = pageContext.getSession();
		
		FDSessionUser user = null;
		//
		// First choice is to retrieve customer id from request
		//
		if(!useId){
			fdCustId  = request.getParameter("fdCustId");
			erpCustId = NVL.apply(request.getParameter("erpCustId"), "");
		}
		//
		// If no customer id in request, check the session
		//
		if(!"".equals(erpCustId)) {
			user = (FDSessionUser) session.getAttribute(SessionName.USER);
			if(user == null || user.getIdentity() == null || !user.getIdentity().getErpCustomerPK().equals(erpCustId)){
				user = new FDSessionUser(FDCustomerManager.recognize(new FDIdentity(erpCustId)), session);
				session.removeAttribute(SessionName.LIST_SEARCH_RAW);
				user.isLoggedIn(true);
			}
		}else {
			user = (FDSessionUser) session.getAttribute(SessionName.USER);
		}
		
		if(user == null){
			throw new JspException("Required Object user not found");
		}
		
		session.setAttribute(SessionName.USER, user);
		
		CrmSession.getSessionStatus(session).setFDUser(user);
		
		return user;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.customer.FDUserI";
		}
	}
}
