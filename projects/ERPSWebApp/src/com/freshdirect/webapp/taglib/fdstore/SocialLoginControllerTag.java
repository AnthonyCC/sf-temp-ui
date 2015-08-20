package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.social.ejb.FDSocialManager;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SocialLoginControllerTag extends
		com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static final long serialVersionUID = -7466852553179595103L;
	private static Category LOGGER = LoggerFactory.getInstance(SocialLoginControllerTag.class);
	private String signUpUnrecognized = "/social/signup_lite_social.jsp?is_forwarded=true";
	private String socialLoginMergePage ="/social/social_login_merge.jsp";
	private String socialCustomMessage ="/social/social_custom_message.jsp";
	String updatedSuccessPage ="/index.jsp";
	HttpServletResponse response = null;
	HashMap<String,String> socialUser = null;
	private Object result;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {

		HttpSession session = this.pageContext.getSession();
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		
		
		// connection token for web
		String connectionToken  = (request.getParameter("connection_token") == null) ? "" : (String) request.getParameter("connection_token");
		
		// user token and provider for mobile app
		String userToken = (request.getParameter("userToken") == null) ? "" : (String) request.getParameter("userToken");
		
		String providerName = (request.getParameter("provider") == null) ? "" : (String)request.getParameter("provider");
		
		
		SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
		
		
		if(socialProvider != null)
		{
			if(connectionToken.length() > 0)
				socialUser = socialProvider.getSocialUserProfile(connectionToken);
			else if(userToken.length() > 0 && providerName.length() > 0)
				socialUser = socialProvider.getSocialUserProfileByUserToken(userToken, providerName);
		}
			
			
			
		HashMap<String, String> userLinkAccPendingProp = null;
		
		String userId="",password = "",userIdInDb="";
	    
	    if(socialUser != null)
	    {
	    	//Set up the socialUser in session so that you can track the user
	    	session.setAttribute(SessionName.SOCIAL_USER,socialUser);
	    	
	    	userToken = socialUser.get("userToken");
			userId = socialUser.get("email"); 
				    	
			if(userId == null || userId.length() == 0)
			{
				// User email not found. User will not be able to create account with this social.
				// The Social Provider did not return user email or user email missing.
				doRedirect(socialCustomMessage); 
				
			}
	    	
			// call ejb to check if user token exist in the in fd social database.
			// if exists then return user_id otherwise return null;
			if(userId != null && userId.length() > 0)
				userIdInDb = getUserIdForUserToken(userToken);

			// The social login token exist in db. Take user to home page.
			 //Login the user in the fd system with fd user and fd password	
				
				if (userIdInDb != null && userIdInDb.length() > 0) {
					
					
					//Check if any pending social account exist to merge 
				    Object objLinkAccPendingProp = session.getAttribute(SessionName.PENDING_SOCIAL_ACTIVATION);
				    
				    if(objLinkAccPendingProp != null)
				    {
				    	userLinkAccPendingProp = (HashMap<String, String>)objLinkAccPendingProp;
				    	
				    	if(userLinkAccPendingProp != null)
				    	{
				    		// add this pending account entry to database.
				    		
				    		try {

								FDSocialManager.mergeSocialAccountWithUser(
										userLinkAccPendingProp.get("email"),
										userLinkAccPendingProp.get("userToken"),
										userLinkAccPendingProp.get("identityToken"),
										userLinkAccPendingProp.get("provider"),
										userLinkAccPendingProp.get("displayName"),
										userLinkAccPendingProp.get("preferredUsername"),
										userLinkAccPendingProp.get("email"),
										userLinkAccPendingProp.get("emailVerified"));
								
								LOGGER.info("Account: "+userLinkAccPendingProp.get("email")+" provider: "+ userLinkAccPendingProp.get("provider")+" merged");
								
								session.removeAttribute(SessionName.PENDING_SOCIAL_ACTIVATION); // remove pending link social account.
								
								
							} catch (FDResourceException e1) {
								LOGGER.error("error in merging account:" + e1.getMessage());
							}
				    		
				    		
				    
				    	}
				    }
				 
				    
				    String updatedSuccessPage = UserUtil.loginSocialUser(session, request, response,null, userId,"", this.updatedSuccessPage);
				    
				    
				    
				    
				    
				    if(updatedSuccessPage != null) {
				       //redirect to successpage
				    	try {
							response.sendRedirect("/social/success.jsp?successPage="+updatedSuccessPage.substring(1,this.updatedSuccessPage.length()));
							LOGGER.info("successPage:"+updatedSuccessPage.substring(1,this.updatedSuccessPage.length()));
						} catch (IOException e) {
							LOGGER.error(e.getMessage());
						}
				    	return SKIP_BODY;
				    }
					
					
					} else {
					// check with the email id if an account with this email id exist in
					// our system
					// if exist then take user to merge page. in
					// that cause user has to log
					// in to our site with either fresh direct account or social login.
					// if no such account exist then take to sign up page.

					//If user already a customer of fresh direct.
					if (userId != null && isUserEmailAlreadyExist(userId)) {
						//Recognized User
						doRedirect(socialLoginMergePage);
							
					} 
					else 
					{
						doRedirect(signUpUnrecognized);
					}
				}

				
	    }
	    	
		
	
		return SKIP_BODY;
	}

	private String getUserIdForUserToken(String userToken) {

		try {
			return FDSocialManager.getUserIdForUserToken(userToken);
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
		
	}

	private boolean isUserEmailAlreadyExist(String email) {
		try {
			return FDSocialManager.isUserEmailAlreadyExist(email);
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		}

		return false;
	}
	
	private int doRedirect(String url) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(url));
			JspWriter writer = pageContext.getOut();
			writer.close();
			return SKIP_BODY;
			
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
