
	package com.freshdirect.webapp.taglib.crm;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.jsp.JspException;
	
	import com.freshdirect.fdstore.FDResourceException;
	import com.freshdirect.fdstore.customer.FDCustomerManager;
	import com.freshdirect.fdstore.customer.FDUserI;
	import com.freshdirect.framework.webapp.ActionResult;
	import com.freshdirect.webapp.taglib.AbstractControllerTag;

	public class CrmVIPSettingControllerTag extends AbstractControllerTag {
	
		private FDUserI user;
	
		public void setUser(FDUserI user){
			this.user = user;
		}

		protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
			if("updateVipSettings".equalsIgnoreCase(this.getActionName())){
				try{
					String vipSetting= "true".equalsIgnoreCase(request.getParameter("vipSetting")) ? "true" : "false";
					if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
						FDCustomerManager.setProfileAttribute(this.user.getIdentity(),"VIPCustomer",vipSetting);
						user.invalidateCache();
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

