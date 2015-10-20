package com.freshdirect.webapp.taglib.fdstore;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


@SuppressWarnings("rawtypes")
public class GetConnectedProvidersTag extends AbstractGetterTag implements SessionName  {

	private static final long serialVersionUID = -5167645289765829021L;
	private static Category LOGGER = LoggerFactory.getInstance(GetConnectedProvidersTag.class);

	@SuppressWarnings("unchecked")
	protected Object getResult() throws Exception {
		String userId="";
		
		HttpSession session = pageContext.getSession();
		
		HashMap<String, String> socialUser = (HashMap<String, String>) session.getAttribute(SessionName.SOCIAL_USER);
		
		List<String> connectedProviders = null;
		
		if(socialUser != null)
		{
			userId = socialUser.get("email");
			
			String result="";
			
			if(userId != null)
			{
				connectedProviders =  ExternalAccountManager.getConnectedProvidersByUserId(userId, EnumExternalLoginSource.SOCIAL);
				
				for(String provider : connectedProviders)
		  		{
					result += "'"+provider+"',";
		  		}
		  	
		  		if(result.length() > 5)
		  			result = result.substring(0, result.length()-1);
		  		
		  		session.setAttribute("providerStr", result);
		  		LOGGER.info("providers:"+result);
		
			}
			
	  		return result;
			
			
		}
		return null;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.ArrayList";
		}

	}
	

}

