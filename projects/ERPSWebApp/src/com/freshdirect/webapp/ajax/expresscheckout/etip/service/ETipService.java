package com.freshdirect.webapp.ajax.expresscheckout.etip.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class ETipService {
	
	private static final ETipService INSTANCE = new ETipService();
	
	public static final String ETIP_FIELD_CODE_ID = "tipAmount";
	
	public static final String decimalRegex = "^[0-9]+(\\.[0-9]+)?$";

	private ETipService() {		
	}
	
	public static ETipService defaultService() {
		return INSTANCE;
	}
	
	public List<ValidationError> applyETip(FormDataRequest eTipRequestData, FDSessionUser user, HttpSession session) throws FDResourceException {
		List<ValidationError> result = new ArrayList<ValidationError>();
		
		String tipAmount = FormDataService.defaultService().get(eTipRequestData, ETIP_FIELD_CODE_ID);
		boolean isCustomTip = false;
		if(tipAmount == null || tipAmount.equalsIgnoreCase("Other Amount")) {
			tipAmount = FormDataService.defaultService().get(eTipRequestData, "otherTipAmount");
			isCustomTip = true;
		}
		
		if(tipAmount.startsWith("$")) {
			tipAmount = tipAmount.substring(1);
		}
		
		if(tipAmount.startsWith(".")) { //allow change without leading zero
			tipAmount = "0"+tipAmount;
		}
		if(tipAmount.endsWith(".")) { //allow no change like "3."
			tipAmount += "0";
		}
		
		if( tipAmount.length() < 1 ){
			tipAmount += "0.00";
		}
		
		double subTotal = user.getShoppingCart().getSubTotal();
		double maximumTipAllowed = subTotal * 32/100;
		
		//result.add(new ValidationError(ETIP_FIELD_CODE_ID, "Not a valid tip amount, " + tipAmount));
		
		if(!tipAmount.matches(decimalRegex)) {
			result.add(new ValidationError(ETIP_FIELD_CODE_ID, "Not a valid tip amount , " + tipAmount.length() ));
		} else {		
		   CheckoutService.defaultService().applyETip(user, tipAmount, isCustomTip);		   
		}
		
		
		return result;
	}

}
