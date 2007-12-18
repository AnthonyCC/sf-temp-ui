package com.freshdirect.webapp.taglib.crm;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmDepotMembershipControllerTag extends AbstractControllerTag {
	
	private FDUserI user;
	
	public void setUser(FDUserI user){
		this.user = user;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if("updateDepotMembership".equalsIgnoreCase(this.getActionName())){
			try{
				String depotCode = NVL.apply(request.getParameter("depotCode"), "").trim();
				if (!depotCode.equals(NVL.apply(this.user.getDepotCode(), "").trim())){
					if(!"".equals(depotCode)){
						String accessCode = NVL.apply(request.getParameter("accessCode"), "").trim();
						actionResult.addError("".equals(accessCode), "accessCode", "required");
						if(actionResult.isSuccess()){
							actionResult.addError(!FDDepotManager.getInstance().checkAccessCode(depotCode, accessCode), "accessCode", "Access Code is incorrect");
						}
					}
					if (actionResult.isSuccess()) {
						if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
							if("".equals(depotCode)){
								this.user.setDepotCode(null);
								FDCustomerManager.setDepotCode(this.user.getIdentity(), null);
								if (user.getZipCode() == null) {
									for (Iterator i = FDCustomerManager.getShipToAddresses(this.user.getIdentity()).iterator(); i.hasNext(); ) {
										ErpAddressModel a = (ErpAddressModel) i.next();
										user.setZipCode(a.getZipCode());
										break;
									}
								}
							}else{
								this.user.setDepotCode(depotCode);
								FDCustomerManager.setDepotCode(this.user.getIdentity(), depotCode);
							}
							FDCustomerManager.storeUser(((FDSessionUser)this.user).getUser());
							HttpSession session = request.getSession();
							session.setAttribute(SessionName.USER, user);
						}
					}
				}
			}catch(FDResourceException ex){
				throw new JspException(ex);
			}
		}
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI{
		//default implementation
	}

}
