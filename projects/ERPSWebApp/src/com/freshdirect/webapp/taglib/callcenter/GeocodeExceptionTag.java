package com.freshdirect.webapp.taglib.callcenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.delivery.AddressScrubber;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

/**@author ekracoff on Jul 13, 2004*/
public class GeocodeExceptionTag extends AbstractControllerTag implements SessionName {

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		try {
			if("addGeocodeException".equals(getActionName())){
				ExceptionAddress ex = populateAndValidate(actionResult, request);
				if(actionResult.isSuccess()){
//					CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());
					String agentId = CrmSession.getCurrentAgentStr(request.getSession());
					FDDeliveryManager.getInstance().addGeocodeException(ex, agentId);
				}
			} else if("deleteGeocodeException".equals(getActionName())){
				ExceptionAddress ex = new ExceptionAddress();
				ex.setScrubbedAddress(NVL.apply(request.getParameter("addressId"), ""));
				ex.setZip(NVL.apply(request.getParameter("zip"), ""));
				if(actionResult.isSuccess()){
					FDDeliveryManager.getInstance().deleteGeocodeException(ex);
				}
			} else {
				setSuccessPage(null);
			}
			
		} catch (FDResourceException e) {
            if (e.getMessage().indexOf("unique constraint") > -1) {
            	actionResult.addError(new ActionError("mainError", "Duplicate entry exists, record not saved"));
            } else {
            	throw new FDRuntimeException(e);
            }
		}
		return true;
	}
	
	private ExceptionAddress populateAndValidate(ActionResult result, HttpServletRequest request) {
		ExceptionAddress ex = new ExceptionAddress();
		ex.setScrubbedAddress(NVL.apply(request.getParameter("streetAddress"), ""));
		result.addError("".equals(ex.getScrubbedAddress()), "streetAddress", SystemMessageList.MSG_REQUIRED);
	
		try {
			ex.setScrubbedAddress(AddressScrubber.standardizeForGeocode(ex.getScrubbedAddress()));
		} catch (InvalidAddressException e) {
			result.addError(true, "streetAddress", e.getMessage());
		}
		
		ex.setZip(NVL.apply(request.getParameter("zip"), ""));
		result.addError("".equals(ex.getZip()), "zip", SystemMessageList.MSG_REQUIRED);
		
		if (!result.hasError("zip")) {
			boolean isNumber = true;
			try {
				Integer.parseInt(ex.getZip());
			} catch(NumberFormatException ne) {
				isNumber = false;
			}
			if ((ex.getZip().length() != 5) || !isNumber) {
				result.addError(new ActionError("zip", SystemMessageList.MSG_ZIP_CODE));
			}
		}
		
		String latitude = NVL.apply(request.getParameter("latitude"),"");
		String longitude = NVL.apply(request.getParameter("longitude"),"");
		
		if("".equals(latitude)){
			result.addError(new ActionError("latitude", SystemMessageList.MSG_REQUIRED));
		} else{
			try{
				ex.setLatitude(new Double(latitude).doubleValue());
			} catch(NumberFormatException e){
				result.addError(new ActionError("latitude", "Please enter valid decimal"));
			}
		}
		
		if("".equals(longitude)){
			result.addError(new ActionError("longitude", SystemMessageList.MSG_REQUIRED));
		} else{
			try{
				ex.setLongitude(new Double(longitude).doubleValue());
			} catch(NumberFormatException e){
				result.addError(new ActionError("longitude", "Please enter valid decimal"));
			}
		}
		
		return ex;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
}
