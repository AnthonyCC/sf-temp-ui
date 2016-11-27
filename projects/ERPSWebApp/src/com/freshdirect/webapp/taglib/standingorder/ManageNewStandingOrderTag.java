package com.freshdirect.webapp.taglib.standingorder;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class ManageNewStandingOrderTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 1505631948989148320L;
	
	String resultName;	
	
	public void setResult(String result) {
		this.resultName = result;
	}
	
	@Override
	public int doStartTag() throws JspException {
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		ActionResult result = new ActionResult();
		
		String action = request.getParameter("action");
		String soId = request.getParameter("soId");
		String standingOrderName = request.getParameter("soName");

		// store action result in request
		pageContext.setAttribute(resultName, result);
		
		if ("delete".equalsIgnoreCase(action)) {
			FDSessionUser u = (FDSessionUser) pageContext.getSession().getAttribute(SessionName.USER);

			if (soId != null && !"".equals(soId)) {
				FDStandingOrder so;
				try {
					so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
				
				if (!so.isDeleted()) {
					FDActionInfo info = AccountActivityUtil.getActionInfo(pageContext.getSession());
					FDStandingOrdersManager.getInstance().delete(info, so);
				}
				} catch (FDResourceException e) {
					result.addError(true, "SO_NAME", "The name '"+standingOrderName+"' error while deleting.");
				}
			} 

		} else if ("settings".equalsIgnoreCase(action)) {
			FDSessionUser u = (FDSessionUser) pageContext.getSession().getAttribute(SessionName.USER);
			u.setCurrentStandingOrder(null);
			
			if (soId != null && !"".equals(soId)) {
				FDStandingOrder so;
				try {
					so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
					so=FDStandingOrdersManager.getInstance().getStandingOrderDetails(so);
                    u.setCurrentStandingOrder(so);
                    u.setSoTemplateCart(so.getStandingOrderCart());
				} catch (FDResourceException e) {

					result.addError(true, "SO_NAME", "The name '"+standingOrderName+"' error while deleting.");
				}
			} 

		}	

		return EVAL_BODY_BUFFERED;
	}
	

	public static class TagEI extends TagExtraInfo {
        @Override
		public VariableInfo[] getVariableInfo(TagData data) {
        	VariableInfo[] vinfo = new VariableInfo[1];
        	vinfo[0] = new VariableInfo(data.getAttributeString("result"), "com.freshdirect.framework.webapp.ActionResult", true, VariableInfo.NESTED);
        	return vinfo;
        }
	}
}
