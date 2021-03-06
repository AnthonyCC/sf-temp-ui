package com.freshdirect.webapp.taglib.standingorder;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.EnumStandingOrderActiveType;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.FDURLUtil;

public class CreateStandingOrderTag extends BodyTagSupport {
	
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


		// store action result in request
		pageContext.setAttribute(resultName, result);
		
		if ("create".equalsIgnoreCase(action)) {
			FDSessionUser u = (FDSessionUser) pageContext.getSession().getAttribute(SessionName.USER);

			String standingOrderName = request.getParameter("soName");
			standingOrderName = standingOrderName != null ? standingOrderName.trim() : "";
			
			if ("".equalsIgnoreCase(standingOrderName)) {
				result.addError(true, "SO_NAME", "Please specify name for standing order!");
			}
			if (standingOrderName.length() > FDCustomerCreatedList.MAX_NAME_LENGTH) {
				result.addError(true, "SO_NAME", "List name too long (max " + FDCustomerCreatedList.MAX_NAME_LENGTH + " characters)!");
			}
			
			// FDListManager.getListName(u.getIdentity(), EnumCustomerListType.SO, standingOrderName);
			try {
				if (FDListManager.isCustomerList(u, EnumCustomerListType.SO, standingOrderName)) {
					result.addError(true, "SO_NAME", "The name '"+standingOrderName+"' is already in use. Please specify another name.");
				}
			} catch (FDResourceException e2) {
				throw new JspException(e2);
			}
			
			int frequency = 0;
			// don't Validate frequency if SO 3.0 is enabled 
			if (!u.isNewSO3Enabled()) {
				String freqValue = request.getParameter("soFreq");
				try {
					frequency = Integer.parseInt(freqValue);
				} catch (NumberFormatException e) {
					result.addError(true, "SO_FREQ", "Please specify delivery frequency!");
				}
			}
			if (!result.isFailure()) {
				
				FDStandingOrder so = new FDStandingOrder();
				so.setCustomerListName(standingOrderName);
				so.setFrequency(frequency);
				

				so.setCustomerId(u.getIdentity().getErpCustomerPK());
                
				if (u.isNewSO3Enabled()) {
					// Need to save the standing order
					try {
						so.setNewSo(true);
						so.setActivate(EnumStandingOrderActiveType.getEnum(1).getName());
						//StandingOrderUtil.createStandingOrder(pageContext.getSession(), u.getSoTemplateCart(), so, null);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						so.setPaymentMethodId(FDCustomerManager.getDefaultPaymentMethodPK(u.getIdentity()));
					} catch (FDResourceException e1) {
					}

					try {
						so.setAddressId(FDCustomerManager.getDefaultShipToAddressPK(u.getIdentity()));
					} catch (FDResourceException e) {
					}
				}

				// What is missing initially
				// - customer list
				// - time zone
				// - alcohol agreement
				
				
				// record new standing order object in session
				// note that this object is not yet complete

				
				u.setCurrentStandingOrder(so);
				u.setCheckoutMode( EnumCheckoutMode.CREATE_SO );

				u.setSuspendShowPendingOrderOverlay(true);
				
				//clear inform ordermodify flag
				u.setShowingInformOrderModify(false);

				try {
					// redirect to main page
					if(u.isNewSO3Enabled()){
						response.sendRedirect(response.encodeRedirectURL("/quickshop/new_standing_order.jsp")) ;
					}else{
                        response.sendRedirect(response.encodeRedirectURL(FDURLUtil.getLandingPageUrl(EnumServiceType.CORPORATE)));
					}

					return SKIP_BODY;
				} catch (IOException ioe) {
					throw new JspException("Error redirecting " + ioe.getMessage());
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
