package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.social.ejb.FDSocialManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.RegistrationController;
import com.freshdirect.mobileapi.controller.data.request.SocialLinkAccountRequest;
import com.freshdirect.mobileapi.controller.data.request.SocialLogin;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialLoginControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;


public class SocialLoginControllerTagWrapper extends NonStandardControllerTagWrapper implements RequestParamName, SessionParamName, MessageCodes{
	
	private String REDIRECT_URL_SIGN_UP_UNRECOGNIZED = "/social/signup_lite_social.jsp";
	private String REDIRECT_URL_MERGE_PAGE ="/social/social_login_merge.jsp";
	private String REDIRECT_URL_CUSTOM_MESSAGE_PAGE ="/social/social_custom_message.jsp";
	private String REDIRECT_URL_SUCCESS ="/index.jsp";
	
	private static Category LOGGER = LoggerFactory
			.getInstance(RegistrationController.class);
	
	
	public SocialLoginControllerTagWrapper(SessionUser user){
		super(new SocialLoginControllerTag(), user);
	}
	@Override
	protected void setResult() {
        ((SocialLoginControllerTag) wrapTarget).setResult(ACTION_RESULT);  
	}

	@Override
	protected Object getResult() throws FDException {
		return getActionResult();
	}
	
	public ResultBundle recognizeAccount(SocialLogin requestMessage) throws FDException{
		addExpectedSessionValues(new String[]{SessionName.SOCIAL_USER, SessionName.PENDING_SOCIAL_ACTIVATION, SessionName.TICK_TIE_CUSTOMER,"fd.application","fd.ss.prevRec","savingsFeatureLookup","prevSavingsVariant","pendingCoremetricsLoginEvent"}, 
				new String[]{SessionName.SOCIAL_USER, SessionName.PENDING_SOCIAL_ACTIVATION, SessionName.TICK_TIE_CUSTOMER,"fd.application","fd.ss.prevRec","savingsFeatureLookup","prevSavingsVariant","pendingCoremetricsLoginEvent"});
		addExpectedRequestValues(new String[]{ REQ_PARAM_USER_TOKEN,REQ_PARAM_PROVIDER,"connection_token","userToken"}, new String[]{REQ_PARAM_USER_TOKEN,REQ_PARAM_PROVIDER,"connection_token","userToken"});
		addRequestValue(REQ_PARAM_USER_TOKEN, requestMessage.getUserToken());
		addRequestValue(REQ_PARAM_PROVIDER, requestMessage.getProvider());
		
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
		 
	
		
		 
		 
		 
		 return resultBundle;
	}
	
	public ResultBundle connectExistingAccounts(SocialLinkAccountRequest requestMessage) throws FDException{
		
		addExpectedSessionValues(new String[]{}, new String[]{});
		addExpectedRequestValues(new String[]{"email","password","existingToken","newToken","provider"}, new String[]{"email","password","existingToken","newToken","provider"});
		SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
		String email = requestMessage.getEmail();
		String password = requestMessage.getPassword();
		String existingToken = requestMessage.getExistingToken();
		String newToken = requestMessage.getNewToken();
		String provider = requestMessage.getProvider();
		FDIdentity userIdentity = null;
		HashMap<String,String> socialUser = null;
		
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
		if(password != null && password.length() > 0)
		{
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
		else // Password null/blank means Social Authentication.
		{
			/* Not required
			if(socialProvider != null &&  existingToken != null)
				socialUser = socialProvider.getSocialUserProfile(existingToken);
			
			if(socialUser == null)
			{
				result.addError(new ActionError(ERR_AUTHENTICATION_BY_SOCIAL_ACCOUNT_FAILED, "Authentication by Social Account Failed"));
				return new ResultBundle(result, this);
			}
			*/
			
			
		}
			
			
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
		
			
		// Actual Link Account Goes Here.	
		try {

			FDSocialManager.mergeSocialAccountWithUser(
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


		
		return new ResultBundle(result, this);
	}
	public ResultBundle unlinkExistingAccounts(
			SocialLogin requestMessage) {
		
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

			FDSocialManager.unlinkSocialAccountWithUser( email, userToken);

		} catch (FDResourceException e1) {
			LOGGER.error("Social unlink Account:" + e1.getMessage());
			result.addError(new ActionError(ERR_LINK_ACCOUNT_FAILED, "unlink Account Failed"));
			return new ResultBundle(result, this);

		}


		
		return new ResultBundle(result, this);
	}
	
}
	
	
