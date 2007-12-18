package com.freshdirect.webapp.taglib.fdstore.depot;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CorpLoginControllerTag extends DepotLoginControllerTag {

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {

		try {
			if ("checkAccessCode".equalsIgnoreCase(this.getActionName())) {

				this.accessCode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode()), "");

				if ("".equals(accessCode)) {
					actionResult.addError(true, EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode(), SystemMessageList.MSG_REQUIRED);
					return true;
				}

				this.depotCode = FDDepotManager.getInstance().checkCorpAccessCode(accessCode);
				if (depotCode == null) {
					Object[] params = new Object[] { this.accessCode };
					actionResult.addError(
						true,
						EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode(),
						MessageFormat.format(SystemMessageList.MSG_INVALID_ACCESS_CODE, params));
					return true;
				}

				this.loginUser();

			}
		} catch (FDResourceException e) {
			actionResult.addError(true, EnumUserInfoName.TECHNICAL_DIFFICULTY.getCode(), SystemMessageList.MSG_TECHNICAL_ERROR);
		}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// Empty 
	}

}
