package com.freshdirect.payment.gateway.impl;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.PayPalData;
import com.freshdirect.payment.gateway.Merchant;

/**
 * @author Aniwesh Vatsal
 *
 */
final class PayPalConstants implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5812981767127041034L;
	public static final String CURRENCY_EXPONENT = "2";
	
	private PayPalConstants() {
	}

	
	 
	 static enum MerchantID implements java.io.Serializable {
			FRESHDIRECT(Merchant.FRESHDIRECT,PayPalData.getMerchantFRESHDIRECT()),
			FDW(Merchant.FDW,PayPalData.getMerchantFDW());
			private Merchant id;
		    private String value;
		    private static Map<Merchant, MerchantID> merchantIDs;
		    static {
		    	init();
		    }
		    private static void init() {
		    	merchantIDs = new HashMap<Merchant, MerchantID>();
		        for (MerchantID s : values()) {
		        	merchantIDs.put(s.id, s);
		        }
		    }
		    
		    private MerchantID(Merchant  id, String value) {
		        this.id = id;
		        this.value = value;
		    }
		     public String toString() {
		        final StringBuilder sb = new StringBuilder();
		        sb.append(this.getClass().getName());
		        sb.append("{id=").append(id.name());
		        sb.append(", value='").append(value).append('\'');
		        sb.append('}');
		        return sb.toString();
		    }
		     Merchant getID() {
		        return id;
		    }
		 
		     String getValue() {
		        return value;
		    }
		     public static MerchantID get(Merchant  id) {
		        
		     	return merchantIDs.get(id);
		     }
		} 
	
	
	
	
}
