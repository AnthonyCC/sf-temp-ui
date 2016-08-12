package com.freshdirect.payment.ejb;

/**
 * 
 * @author ksriram
 *
 */
public class PayPalSettlementTransactionCodes {

	
	public static enum EnumPPSTLEventCode {

		T0006, T0003;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPSTLEventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPSTLEventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	public static enum EnumPPSTFEventCode {

	    NONE;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPSTFEventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPSTFEventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	public static enum EnumPPCBKEventCode {

		T1106, T1201;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPCBKEventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPCBKEventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	public static enum EnumPPCBREventCode {

		T1202, T1205, T1207, T1208;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPCBREventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPCBREventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	public static enum EnumPPREFEventCode {

		T1107;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPREFEventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPREFEventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	
	public static enum EnumPPMiscFeeEventCode {

		T0100, T0106, T0107, T1108;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPMiscFeeEventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPMiscFeeEventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	public static enum EnumPPIgnoreableEventCode {

		T0400, T0300, T0301;

	    public String getName() {
	        return name();
	    }
	    
	    public static EnumPPIgnoreableEventCode getEnum(String code) {
	        try {
	            return code != null ? EnumPPIgnoreableEventCode.valueOf(code) : null;
	        } catch (IllegalArgumentException e) {
	            return null;
	        }
	    }
	}
	
	
	
	
}
