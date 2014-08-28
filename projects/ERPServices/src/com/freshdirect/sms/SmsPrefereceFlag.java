package com.freshdirect.sms;

public enum SmsPrefereceFlag {
	
	NO_THANKS, VIEWED;
	
	public String getName() {
        return name();
    }
	
	public static SmsPrefereceFlag getSmsPrefereceFlag(String smsPreferece){
		try{
			return smsPreferece!=null?SmsPrefereceFlag.valueOf(smsPreferece):null;
		}catch (IllegalArgumentException e) {
            return null;
        }
	}

}
