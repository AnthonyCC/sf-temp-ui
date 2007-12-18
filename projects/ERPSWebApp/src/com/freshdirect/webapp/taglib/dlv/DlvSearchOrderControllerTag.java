package com.freshdirect.webapp.taglib.dlv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.delivery.DlvPaymentManager;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class DlvSearchOrderControllerTag extends AbstractControllerTag {
	
	private Date date;
	private String address;
	private String zipcode;

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		this.processForm(request);
		this.validateForm(actionResult);
		if(actionResult.isSuccess()){
			try{
				List  orders = DlvPaymentManager.getInstance().getOrdersForDateAndAddress(this.date, this.address, this.zipcode);
				
				pageContext.getSession().setAttribute("SEARCH_RESULTS", orders);
			}catch(DlvResourceException e){
				throw new JspException(e);
			}
		}
		return true;
	}

	private void processForm(HttpServletRequest request) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
		String d = NVL.apply(request.getParameter("date"), "");
		if(!"".equals(d)){
			try{
				this.date = df.parse(d);
			}catch(ParseException e){
				this.date = null;
			}
		}
		this.address = NVL.apply(request.getParameter("address"), "");
		if(!"".equals(address) && address.length() > 2){
			char c = this.address.charAt(0);
			if(Character.isDigit(c)){
				int numBreak = this.address.indexOf(' ');
				this.address = this.address.substring(numBreak+1, this.address.length());
			}
		}
		this.zipcode = NVL.apply(request.getParameter("zipcode"), "");
	}
	
	private void validateForm(ActionResult actionResult) {
		actionResult.addError(this.date == null, "dateProblem", "Enter date as MM/DD/YY");
		actionResult.addError("".equals(this.address), "missingAddress", "required");
		actionResult.addError("".equals(this.zipcode), "missingZipcode", "required");
		if(!"".equals(this.zipcode)){
			boolean isNumber = true;
			try {
				Integer.parseInt(this.zipcode);
			} catch(NumberFormatException ne) {
				isNumber = false;
			}
			actionResult.addError(this.zipcode.length() != 5 || !isNumber, "incorrectZipcode", "zipcode must be a five digit long number");
		}
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	} 
}
