package com.freshdirect.fdstore.customer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FDProductPromoEligibilityList implements Serializable {
	
	Map info = new HashMap();
	
	public FDProductPromoEligibilityList(){
	}
	
	public void setEligibility(String promoId, boolean eligible){
		info.put(promoId, new Boolean(eligible));
	}
	
	public boolean isEligibleFor(String promoId){
		Boolean value = (Boolean)info.get(promoId);
		return (value != null ? value.booleanValue() : false); 
	}
	
	public boolean isEvaluatedFor(String promoId){
		Boolean value = (Boolean)info.get(promoId);
		return (value != null ? true : false); 
	}
}
