package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmGetFDUserTag extends AbstractGetterTag<FDUserI> {
	private static final long serialVersionUID = -8121263158947764285L;

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

	@Override
	protected FDUserI getResult() throws Exception {
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
				user = new FDSessionUser(FDCustomerManager.recognizeForCRM(new FDIdentity(erpCustId), EnumTransactionSource.CUSTOMER_REP, null), session);
				session.removeAttribute(SessionName.LIST_SEARCH_RAW);
				user.isLoggedIn(true);
			}
			session.setAttribute(SessionName.USER, user);
			// FIXME ksriram please fix this
			FDCustomerCouponUtil.initCustomerCoupons(session);
		}else {
			user = (FDSessionUser) session.getAttribute(SessionName.USER);
		}
		
		if(user == null){
			throw new JspException("Required Object user not found");
		}

		session.setAttribute(SessionName.USER, user);
//		FDCustomerCouponUtil.initCustomerCoupons(session);
		CrmSessionStatus sessionStatus =CrmSession.getSessionStatus(session);
		if(null !=sessionStatus){
			sessionStatus.setFDUser(user);
		}else{			
			CrmStatus status = new CrmStatus(CrmSession.getCurrentAgent(session).getPK());			
			sessionStatus = new CrmSessionStatus(status, session);
			sessionStatus.setFDUser(user);
			CrmSession.setSessionStatus(session, sessionStatus);
		}
		ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || user.isEligibleForDDPP());
		return user;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.customer.FDUserI";
		}
	}
}
