package com.freshdirect.fdstore;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class SmsOverlayHelper {
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL,FDStoreProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	public static boolean getSmsOverlayFlag(){
		boolean smsOverlayFlag=false;
		Context ctx=null;
		try {
			ctx = getInitialContext();
			String flag= (String)ctx.lookup(FDStoreProperties.getSMSOverlayFlag());
			smsOverlayFlag=Boolean.parseBoolean(flag);
		} catch (NamingException e) {
			
			e.printStackTrace();
		}
		finally{
			
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
				
			} catch (NamingException ne) {
				
			}
		}
		return smsOverlayFlag;
		
	}

}
