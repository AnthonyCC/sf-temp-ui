/*
 * Created on Jun 27, 2005
 */
package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.fdstore.customer.FDActionInfo;

/**
 * @author jng
 */
public class CrmProfileSettingControllerTag extends AbstractControllerTag {

	private FDUserI user;
	
	public void setUser(FDUserI user){
		this.user = user;
	}

	private String name;
	private String value;
	private String notes;
	private boolean refreshForm;
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		try {
			populate(request);
			
			if (!refreshForm) {
				validate(actionResult);
			} else {
				actionResult.addError(true, "refreshForm", "refreshForm");  // ONLY TEMPORARY SOLUTION
			}
			
			if (actionResult.isSuccess()) {
				if("addProfileAttr".equalsIgnoreCase(this.getActionName())) { 
					String notePrefix = "Add Profile Attribute: " + name + " = " + value + ", Note: ";
					FDActionInfo info =	AccountActivityUtil.getActionInfo(pageContext.getSession(), notePrefix + notes);
					FDCustomerManager.setProfileAttribute(this.user.getIdentity(),name,value,info);
				} else if ("updateProfileAttr".equalsIgnoreCase(this.getActionName())) {
					String notePrefix = "Update Profile Attribute: " + name + " = " + value + ", Note: ";
					FDActionInfo info =	AccountActivityUtil.getActionInfo(pageContext.getSession(), notePrefix + notes);
					FDCustomerManager.setProfileAttribute(this.user.getIdentity(),name,value,info);
				} else if ("removeProfileAttr".equalsIgnoreCase(this.getActionName())) {
					String notePrefix = "Remove Profile Attribute: " + name;
					FDActionInfo info =	AccountActivityUtil.getActionInfo(pageContext.getSession(), notePrefix + notes);
					FDCustomerManager.removeProfileAttribute(this.user.getIdentity(), name, info);				
				}
				user.invalidateCache();
			}
			return true;
		}catch(FDResourceException ex){
			throw new JspException(ex);
		}
	}
	
	private void populate(HttpServletRequest request) {
		refreshForm = NVL.apply(request.getParameter("refresh_form"), "").equalsIgnoreCase("Y");
		name = NVL.apply(request.getParameter("name"), "");
		value = NVL.apply(request.getParameter("value"), "");
		notes = NVL.apply(request.getParameter("notes"), "");		
	}
	
	private void validate(ActionResult actionResult) {
		if ("".equals(name)) {
			actionResult.addError(true, "name", SystemMessageList.MSG_REQUIRED);
		}

		if("addProfileAttr".equalsIgnoreCase(this.getActionName()) || "updateProfileAttr".equalsIgnoreCase(this.getActionName())) { 
			if ("".equals(value)) {
				actionResult.addError(true, "value", SystemMessageList.MSG_REQUIRED);
			}
			if ("".equals(notes)) {
				actionResult.addError(true, "notes", SystemMessageList.MSG_REQUIRED);
			}
		}
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI{
		//default implementation
	}

}
