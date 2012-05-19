package com.freshdirect.mail;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.mail.service.TranMailServiceI;
import com.freshdirect.temails.TEmailRuntimeException;

public final class TranMailServiceFactory {

	private static final Map serviceMap  =new HashMap();
	
	static{
		
		try {
			serviceMap.put(EnumTEmailProviderType.CHEETAH.getName(), Class.forName("com.freshdirect.mail.service.CheetahTranMailServiceImpl").newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static TranMailServiceI getTranMailService(String provider){

		
		/*EnumTEmailProviderType type=EnumTEmailProviderType.getEnum(provider);
		if(type==null){
			throw new TEmailRuntimeException("Invalid provider :"+provider);
		}
		*/
				
		return (TranMailServiceI)serviceMap.get(provider);
		
	}
	
}
