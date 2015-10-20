package com.freshdirect.webapp.taglib.fdstore;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.customer.EnumExternalLoginSource;

public class AccountServiceFactory {

	public static AccountService getService(String source){
		
		if(StringUtils.isEmpty(source) || source.equals(EnumExternalLoginSource.SOCIAL.value())){
			return new SocialAccountService();
		}else{
			return new ExternalAccountService();
		}
	}
}
