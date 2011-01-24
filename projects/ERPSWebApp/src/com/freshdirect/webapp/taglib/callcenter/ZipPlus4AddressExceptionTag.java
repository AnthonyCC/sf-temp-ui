package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.delivery.AddressScrubber;
import com.freshdirect.delivery.EnumAddressExceptionReason;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

/**@author ekracoff on Jul 13, 2004*/
public class ZipPlus4AddressExceptionTag extends AbstractControllerTag implements SessionName {

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
			if(getActionName().equals("addException")){
				ExceptionAddress ex = populateException(request);
				validateAddress(ex, actionResult);
	
				if (actionResult.isSuccess()) {
					FDDeliveryManager.getInstance().addExceptionAddress(ex);
				}
			
			} else if("deleteException".equals(this.getActionName())){
				String id = NVL.apply(request.getParameter("addressId"), "");
				if(!"".equals(id)) {
					FDDeliveryManager.getInstance().deleteAddressException(id);
				}
			}else {
				setSuccessPage(null);
			}
			return true;
		} catch (FDResourceException e) {
			throw new JspException(e);
		}
	}

	public ExceptionAddress populateException(HttpServletRequest request) throws FDResourceException {
		ExceptionAddress ex = new ExceptionAddress();

		ex.setStreetAddress(request.getParameter("streetAddress"));
		ex.setAptNumLow(request.getParameter("aptNumLow"));
		ex.setAptNumHigh(request.getParameter("aptNumHigh"));
		ex.setCity(request.getParameter("city"));
		ex.setZip(request.getParameter("zip"));
		ex.setAddressType(EnumAddressType.getEnum(request.getParameter("aptType")));
		ex.setReason(EnumAddressExceptionReason.getEnum(request.getParameter("reason")));
		ex.setState(request.getParameter("state"));
//		ex.setUserId(CrmSession.getCurrentAgent(request.getSession()).getUserId());
		ex.setUserId(CrmSession.getCurrentAgentStr(request.getSession()));

		ex.setCounty(FDDeliveryManager.getInstance().getCounty(ex.getCity(), ex.getState()));

		return ex;
	}

	private void validateAddress(ExceptionAddress ex, ActionResult result) throws JspException {
		result.addError(ex.getStreetAddress().equals(""), "streetAddress", SystemMessageList.MSG_REQUIRED);

		try {
			ex.setStreetAddress(AddressScrubber.standardizeForUSPS(ex.getStreetAddress()));
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			result.addError(true, "streetAddress", e.getMessage());
		}

		result.addError(ex.getAddressType() != null
			&& ((ex.getAddressType().equals(EnumAddressType.HIGHRISE) && (ex.getAptNumLow().equals("") || ex
				.getAptNumHigh()
				.equals(""))) || ((ex.getAptNumLow().equals("") && !ex.getAptNumHigh().equals("")) || ((!ex.getAptNumLow().equals(
				"") && ex.getAptNumHigh().equals(""))))), "apt", SystemMessageList.MSG_REQUIRED);
		result.addError(ex.getAptNumLow().compareTo(ex.getAptNumHigh()) > 0, "apt", "Apartment Low is greater then Apartment High");

		result.addError(ex.getZip().equals(""), "zip", SystemMessageList.MSG_REQUIRED);

		if (!result.hasError("zip")) {
			boolean isNumber = true;
			try {
				Integer.parseInt(ex.getZip());
			} catch (NumberFormatException ne) {
				isNumber = false;
			}
			if ((ex.getZip().length() != 5) || !isNumber) {
				result.addError(new ActionError("zip", SystemMessageList.MSG_ZIP_CODE));
			}
		}
		
		if(ex.getCounty() == null){
			result.addError(new ActionError("city", "Could not find corresponding county, either city or state is wrong"));
			result.addError(new ActionError("state", "Could not find corresponding county, either city or state is wrong"));
		}

		result.addError(ex.getAddressType() == null, "aptType", SystemMessageList.MSG_REQUIRED);
		result.addError(ex.getReason() == null, "reason", SystemMessageList.MSG_REQUIRED);

	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),};

		}
	}

}