package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

//import weblogic.auddi.util.Logger;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.RegistrationController;
import com.freshdirect.mobileapi.controller.data.UserSocialProfile;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLinkRequest;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLogin;
import com.freshdirect.mobileapi.controller.data.response.SocialLoginResponse;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SiteAccessControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.ExternalAccountControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.SocialProviderOneAll;


public class ExternalAccountControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName, MessageCodes{
	
	public static final String EXTERNAL_PROVIDERS = "EXTERNAL_PROVIDERS";
	public static final String EXTERNAL_USERPROFILE = "EXTERNAL_USERPROFILE";
	private String REDIRECT_URL_SIGN_UP_UNRECOGNIZED = "/social/signup_lite_social.jsp";
	private String REDIRECT_URL_MERGE_PAGE ="/social/social_login_merge.jsp";
	private String REDIRECT_URL_CUSTOM_MESSAGE_PAGE ="/social/social_custom_message.jsp";
	private String REDIRECT_URL_SUCCESS ="/index.jsp";
	
	private static Category LOGGER = LoggerFactory
			.getInstance(RegistrationController.class);
	
	
	public ExternalAccountControllerTagWrapper(SessionUser user){
		super(new ExternalAccountControllerTag(), user);
	}
	@Override
	protected void setResult() {
        ((ExternalAccountControllerTag) wrapTarget).setResult(ACTION_RESULT);  
	}

	@Override
	protected Object getResult() throws FDException {
		return getActionResult();
	}
	
	public ResultBundle recognizeAccount(ExternalAccountLogin requestMessage) throws FDException{
		addExpectedSessionValues(new String[]{SessionName.SOCIAL_USER, SessionName.PENDING_SOCIAL_ACTIVATION, SessionName.TICK_TIE_CUSTOMER,"fd.application","fd.ss.prevRec","savingsFeatureLookup","prevSavingsVariant","pendingCoremetricsLoginEvent"}, 
				new String[]{SessionName.SOCIAL_USER, SessionName.PENDING_SOCIAL_ACTIVATION, SessionName.TICK_TIE_CUSTOMER,"fd.application","fd.ss.prevRec","savingsFeatureLookup","prevSavingsVariant","pendingCoremetricsLoginEvent"});
		addExpectedRequestValues(new String[]{ REQ_PARAM_USER_TOKEN,REQ_PARAM_PROVIDER,"connection_token","userToken"}, new String[]{REQ_PARAM_USER_TOKEN,REQ_PARAM_PROVIDER,"connection_token","userToken", "source"});
		addRequestValue(REQ_PARAM_USER_TOKEN, requestMessage.getUserToken());
		addRequestValue(REQ_PARAM_PROVIDER, requestMessage.getProvider());
		addRequestValue("source", requestMessage.getSource());
		addRequestValue("email", requestMessage.getEmail());
		setMethodMode(true);
		
		 ActionResult result = new ActionResult();
		 
		 if(StringUtils.isEmpty(requestMessage.getUserToken())){
			 result.addError(new ActionError(ERR_INVALID_USER_TOKEN, "User Token must not be empty."));
			 return new ResultBundle(result, this);
		 }
		 
		 if(StringUtils.isEmpty(requestMessage.getProvider())){
			 result.addError(new ActionError(ERR_INVALID_PROVIDER, "provider must not be empty."));
			 return new ResultBundle(result, this);
		 }
		 
		 
		 
		 ResultBundle resultBundle = new ResultBundle(executeTagLogic(), this);
		 
		 ActionResult actionResult = resultBundle.getActionResult()==null?result:resultBundle.getActionResult();
		 
		 
		
		 String redirectUrl = ((HttpResponseWrapper) pageContext.getResponse()).getSendRedirectUrl();
		 
		 
		 if(redirectUrl == null){
			 actionResult.addError(new ActionError(ERR_USER_SOCIAL_PROFILE_NOTFOUND, "User profile not found"));
		 
		 }else if(redirectUrl.contains(REDIRECT_URL_SUCCESS)){
			//User Found and has been logged in.
			
		 } else if (redirectUrl.contains(REDIRECT_URL_MERGE_PAGE)){
			 //The user exists but with different Social Login please merge the accounts
			actionResult.addError(new ActionError(ERR_MERGE_PAGE_REDIRECT, "User Already exists with other social Login Please Call linkAccount"));
		 } else if(redirectUrl.contains(REDIRECT_URL_SIGN_UP_UNRECOGNIZED)){
			 //user Not recognized Register FDX Social needs to be called..
			 actionResult.addError(new ActionError(ERR_SOCIAL_USER_UNRECOGNIZED, "User Not recognized Please call registerSocial"));
		 } else if(redirectUrl.contains(REDIRECT_URL_CUSTOM_MESSAGE_PAGE)){
			// The Social Provider did not return user email or user email missing.
			 actionResult.addError(new ActionError(ERR_SOCIAL_USER_EMAIL_MISSING, "The Social Provider did not return user email or user email missing."));
		 }
		 
		
		if(resultBundle.getActionResult().isSuccess()){
		
			String userToken = requestMessage.getUserToken();
			 String providerName = requestMessage.getProvider();
			 HashMap socialUser = null;
				
			 	
			 	UserSocialProfile userSocialProfile = new UserSocialProfile();
				SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
				if(userToken != null)
					socialUser = socialProvider.getSocialUserProfileByUserToken(userToken, providerName);
				if(socialUser != null)
				{	
					
					List<String> providers = ExternalAccountManager.getConnectedProvidersByUserId((String)socialUser.get("email"), EnumExternalLoginSource.SOCIAL);
					resultBundle.addExtraData(EXTERNAL_PROVIDERS, providers);
					
					String email = (String)socialUser.get("email");
		    		String provider = (String)socialUser.get("provider");
		    		String displayName = (String)socialUser.get("displayName"); 
		    		String names[] = displayName.split(" ");
		    		String firstName = (names.length ==0) ? "" : names[0];
		    		String lastName = (names.length <= 1) ? "" : names[names.length -1];
		    		userSocialProfile.setEmail(email);
		    		userSocialProfile.setFirstName(firstName);
		    		userSocialProfile.setLastName(lastName);
		    		resultBundle.addExtraData(EXTERNAL_USERPROFILE, userSocialProfile);
				}
		}
			
			
		
		 
		 
		 
		 return resultBundle;
	}
	
	public ResultBundle connectExistingAccounts(ExternalAccountLinkRequest requestMessage) throws FDException{
		
		addExpectedSessionValues(new String[]{}, new String[]{});
		addExpectedRequestValues(new String[]{"email","password","existingToken","newToken","provider"}, new String[]{"email","password","existingToken","newToken","provider"});
		String email = requestMessage.getEmail();
		String password = requestMessage.getPassword();
		String existingToken = requestMessage.getExistingToken();
		String newToken = requestMessage.getNewToken();
		String provider = requestMessage.getProvider();
		String source = requestMessage.getSource();
		FDIdentity userIdentity = null;
		
		ActionResult result = new ActionResult();
		
		
		if(email == null || email.length()== 0)
		{
			result.addError(new ActionError(ERR_INVALID_EMAIL_ADDRESS, "Invalid Email."));
			return new ResultBundle(result, this);
			
		}
		
		if(newToken == null || newToken.length()==0)
		{
			result.addError(new ActionError(ERR_INVALID_NEW_TOKEN, "Invalid New Token."));
			return new ResultBundle(result, this);
		}
		
		if(provider == null || provider.length()==0)
		{
			result.addError(new ActionError(ERR_INVALID_PROVIDER, "Invalid Provider."));
			return new ResultBundle(result, this);
		}
		
		///.............
		
		// FD Authentication.
		if(password != null && password.length() > 0){
			try{
				userIdentity = FDCustomerManager.login(requestMessage.getEmail(),requestMessage.getPassword());
			} catch(Exception ex){
				LOGGER.error(ex.getMessage());
			}		
			
			if(userIdentity == null){
				result.addError(new ActionError(ERR_AUTHENTICATION_BY_FD_ACCOUNT_FAILED, "Authentication by FD Account Failed"));
				return new ResultBundle(result, this);
			
			}
			
		}
		
			
		if(StringUtils.isEmpty(source) || source.equals(EnumExternalLoginSource.SOCIAL.value())){
			HashMap<String,String> socialUser = null;
			
			SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
			//Authentication Succeed. Proceed to linkAccount. 
			
			if(newToken != null)
			{
				socialUser = socialProvider.getSocialUserProfileByUserToken(newToken, provider);
			}
			
			if(socialUser == null || socialUser.get("email") == null || socialUser.get("email").length() < 0 )
			{
				result.addError(new ActionError(ERR_USER_SOCIAL_PROFILE_NOTFOUND, "User's New social profile could not be found"));
				return new ResultBundle(result, this);
			}
			
			
			if(!requestMessage.getEmail().equalsIgnoreCase(socialUser.get("email")))
			{
				result.addError(new ActionError(ERR_FD_EMAIL_DONT_MATCH_WITH_SOCIAL_EMAIL, "User's Email don't match with Social Email"));
				return new ResultBundle(result, this);
			}
			try {

				ExternalAccountManager.linkUserTokenToUserId(
						userIdentity.getFDCustomerPK(),
						socialUser.get("email"),
						socialUser.get("userToken"),
						socialUser.get("identityToken"),
						socialUser.get("provider"),
						socialUser.get("displayName"),
						socialUser.get("preferredUsername"),
						socialUser.get("email"), socialUser.get("emailVerified"));

			} catch (FDResourceException e1) {
				LOGGER.error("Social Link Account:" + e1.getMessage());
				result.addError(new ActionError(ERR_LINK_ACCOUNT_FAILED, "Link Account Failed"));
				return new ResultBundle(result, this);

			}
			
		}else{
			try {

				ExternalAccountManager.linkUserTokenToUserId(
						userIdentity.getFDCustomerPK(),
						email,
						newToken,
						newToken,
						provider,
						email,
						email,
						email, "N");

			} catch (FDResourceException e1) {
				LOGGER.error("External Link Account:" + e1.getMessage());
				result.addError(new ActionError(ERR_LINK_ACCOUNT_FAILED, "Link Account Failed"));
				return new ResultBundle(result, this);

			}
			
		}
			
		// Actual Link Account Goes Here.	
		


		
		return new ResultBundle(result, this);
	}
	public ResultBundle unlinkExistingAccounts(
			ExternalAccountLogin requestMessage) {
		
		addExpectedSessionValues(new String[]{}, new String[]{});
		addExpectedRequestValues(new String[]{"email","userToken"}, new String[]{"email", "userToken"});		
		String email = requestMessage.getEmail();
		String userToken = requestMessage.getUserToken();		
		ActionResult result = new ActionResult();
				
		if(email == null || email.length()== 0)
		{
			result.addError(new ActionError(ERR_INVALID_EMAIL_ADDRESS, "Invalid Email."));
			return new ResultBundle(result, this);
			
		}
		
		if(userToken == null || userToken.length()==0)
		{
			result.addError(new ActionError(ERR_INVALID_NEW_TOKEN, "Invalid New Token."));
			return new ResultBundle(result, this);
		}
			
		try {

			ExternalAccountManager.unlinkExternalAccountWithUser( email, userToken, "");
			
		} catch (FDResourceException e1) {
			LOGGER.error("Social unlink Account:" + e1.getMessage());
			result.addError(new ActionError(ERR_LINK_ACCOUNT_FAILED, "unlink Account Failed"));
			return new ResultBundle(result, this);

		}


		
		return new ResultBundle(result, this);
	}
	
}
	
	
