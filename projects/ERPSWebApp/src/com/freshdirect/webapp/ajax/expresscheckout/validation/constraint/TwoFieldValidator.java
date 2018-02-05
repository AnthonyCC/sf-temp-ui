package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

/**
 * Generally used to validate, not a single field, but two fields together
 * @author dheller
 *
 */
public class TwoFieldValidator {
	
	
	
	public boolean isValid(String f1, String f2){
	 if (f2==null || f2.isEmpty()) return true;
	 if (f1==null || f1.isEmpty()) return true;
	 return f1.equalsIgnoreCase(f2);
		
	}
 public String getErrorMessage(){
	 return "Account Numbers should match." +
		   		"<!--  TwoFieldValidator -->";
 }
}
