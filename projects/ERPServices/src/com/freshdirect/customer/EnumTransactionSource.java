/* Generated by Together */

package com.freshdirect.customer;

public enum EnumTransactionSource {
	WEBSITE("WEB", "Website"),
	SYSTEM("SYS", "System"),
	CUSTOMER_REP("CSR", "Telephone"),
	ADMINISTRATOR("ADM", "Administrator"),
	TRANSPORTATION("TRA", "Transportation"),
	IPHONE_WEBSITE("IPW", "iPhone"),	
	STANDING_ORDER("STO", "Standing Order"),
	ANDROID_WEBSITE("ANW", "Android")
	;

	EnumTransactionSource(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
        return this.code;
    }

    public String getName() {
		return this.name;
	}

	public static EnumTransactionSource getTransactionSource(int t) {
		switch (t) {
			case 0:
				return WEBSITE;
			case 1:
				return SYSTEM;
			case 2:
				return CUSTOMER_REP;
			case 3:
				return ADMINISTRATOR;
			case 4:
				return TRANSPORTATION;
			case 5:
				return IPHONE_WEBSITE;
			case 6:
				return STANDING_ORDER;
			case 7:
				return ANDROID_WEBSITE;
			default:
				return null;
		}
	}
	public static EnumTransactionSource getTransactionSource(String code) {
		if(WEBSITE.getCode().equalsIgnoreCase(code)){
			return WEBSITE;
		}else if(SYSTEM.getCode().equalsIgnoreCase(code)){
			return SYSTEM;
		}else if(CUSTOMER_REP.getCode().equalsIgnoreCase(code)){
			return CUSTOMER_REP;
		}else if(ADMINISTRATOR.getCode().equalsIgnoreCase(code)){
			return ADMINISTRATOR;
		}else if(TRANSPORTATION.getCode().equalsIgnoreCase(code)){
			return TRANSPORTATION;
		}else if(IPHONE_WEBSITE.getCode().equalsIgnoreCase(code)){
			return IPHONE_WEBSITE;
		}else if(STANDING_ORDER.getCode().equalsIgnoreCase(code)){
			return STANDING_ORDER;
		}else if(ANDROID_WEBSITE.getCode().equalsIgnoreCase(code)){
			return ANDROID_WEBSITE;
		}else{
			try {
				return getTransactionSource( Integer.parseInt(code) );
			} catch (NumberFormatException ex) {
				return null;
			}

		}
	}


	public String toString() {
		return this.code;
	}

    private final String name;
    private final String code;

}
