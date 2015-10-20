package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;

public class SocialAccountService implements AccountService {

	private static Category LOGGER = LoggerFactory.getInstance(SocialAccountService.class);
	
	private String signUpUnrecognized = "/social/DeliveryAddress.jsp";
	//private String socialLoginMergePage ="/social/social_login_merge.jsp";
	private String socialLoginRecognized ="/social/social_login_recognized.jsp";
	private String socialLoginAccountLinked ="/social/social_login_account_linked.jsp";
	private String signinUnmatched = "/social/social_login_not_recognized.jsp";   
	private String socialAccountAlreadyConnected = "/social/social_login_social_account_already_connected.jsp";   	
	private String socialCustomMessage ="/social/social_custom_message.jsp";
	String updatedSuccessPage ="/index.jsp";
	
		
	@Override
	public String login (HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		// Retrieve sessionUser
		final FDSessionUser fdSessionUser = (FDSessionUser) session.getAttribute(SessionName.USER);					
		String sessionUserId = "";	
		int sessionUserLevel = 0;
		
		if(fdSessionUser != null) {
			sessionUserId = fdSessionUser.getUserId();	
			sessionUserLevel = fdSessionUser.getLevel();
		}		
		
		// social user profile from SocialProvider
		HashMap<String,String> socialUserProfile = null;
		// connection token for web
		String connectionToken  = (request.getParameter("connection_token") == null) ? "" : (String) request.getParameter("connection_token");
		String userToken = (request.getParameter("userToken") == null) ? "" : (String) request.getParameter("userToken");
		String providerName = (request.getParameter("provider") == null) ? "" : (String)request.getParameter("provider");
				
		// Get Social Provider		
		SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");				
		if(socialProvider != null)
		{	
			//Retrieve social user's profile
			if(connectionToken.length() > 0)
				socialUserProfile = socialProvider.getSocialUserProfile(connectionToken);
			else if(userToken.length() > 0 && providerName.length() > 0)
				socialUserProfile = socialProvider.getSocialUserProfileByUserToken(userToken, providerName);
		}
			
			
			
		HashMap<String, String> userLinkAccPendingProp = null;
		
		String socialUserId="";
	    
	    if(socialUserProfile != null)
	    {
	    	//Set up the socialUserProfile in session so that you can track the user
	    	session.setAttribute(SessionName.SOCIAL_USER,socialUserProfile);
	    	
	    	userToken = socialUserProfile.get("userToken");
			socialUserId = socialUserProfile.get("email"); 
			
			if(socialUserId == null || socialUserId.length() == 0)
			{
				// User email not found. User will not be able to create account with this social.
				// The Social Provider did not return user email or user email missing.
				return socialCustomMessage; 				
			}
	    	

			// check if user token exist in FD external account database.
			if (isUserIdExistForUserToken(userToken)) {
				
					/* the just authenticated social email has been linked with one FD account
					 */
				
				
					/*  on Account Setting page
					 *  Check if the just authenticated social email has been associated with the current logged in FD user
					 */
					if( sessionUserId !=null && !socialUserId.equalsIgnoreCase(sessionUserId) &&
						(sessionUserLevel == FDUserI.SIGNED_IN)){
						
						session.setAttribute("AlreadyConnectedSocialAccount", socialUserId);
						return socialAccountAlreadyConnected;
					}					
					
							    
					/*
					 * The socialUserId has been linked with FD account. 
					 * Auto Login.
					 */
				    String updatedSuccessPage = UserUtil.loginUser(session, request, response,null, socialUserId, null, "", this.updatedSuccessPage, true);
							    
				    if(updatedSuccessPage != null) {
				       //redirect to successpage
				    	try {							
							response.sendRedirect("/social/success.jsp?successPage="+updatedSuccessPage.substring(1,this.updatedSuccessPage.length()));
							LOGGER.info("successPage:"+updatedSuccessPage.substring(1,this.updatedSuccessPage.length()));
						} catch (IOException e) {
							LOGGER.error(e.getMessage());
						}
				    }
					
					
			} else {
				
					/* the just authenticated social email is not linked with FD account
					 */
						
				
				
					/*
					 * the just authenticated social email doesn't match the recognized user, who is session times out
					 */
					if( sessionUserId !=null && !socialUserId.equalsIgnoreCase(sessionUserId) &&
						(sessionUserLevel == FDUserI.RECOGNIZED)){						
							return signinUnmatched;
					}						
						
					
					/* check if there exist a matching FD account
					 *  if exist, 
					 *  	1) link the just authenticated social email with the FD account, 
					 *  	2) auto login
					 *  if not, 
					 */


					if (isUserEmailAlreadyExist(socialUserId)) {
						//Existing FD User
									    		
			    		try {
			    			
			    			String customerId = FDCustomerManager.getCustomerId(socialUserId).getId();
			    			
							ExternalAccountManager.linkUserTokenToUserId( customerId,
																		  socialUserProfile.get("email"),
																		  socialUserProfile.get("userToken"),
																		  socialUserProfile.get("identityToken"),
																		  socialUserProfile.get("provider"),
																		  socialUserProfile.get("displayName"),
																		  socialUserProfile.get("preferredUsername"),
																		  socialUserProfile.get("email"),
																		  socialUserProfile.get("emailVerified"));
							
							LOGGER.info("Account: "+socialUserProfile.get("email")+" provider: "+ socialUserProfile.get("provider")+" linked");														
							
						} catch (FDResourceException e1) {
							LOGGER.error("error in merging account:" + e1.getMessage());
						}						
						
			    		
			    		if(sessionUserLevel == FDUserI.SIGNED_IN){							
			    			// Account Preference page
			    			socialLoginAccountLinked = socialLoginAccountLinked + "?socialnetwork=" + socialUserProfile.get("provider");	
							return socialLoginAccountLinked;							
						}else{							
				    		// Auto login
				    		UserUtil.loginUser(session, request, response,null, socialUserId, null, "", this.updatedSuccessPage, true);
				    		
				    		socialLoginRecognized = socialLoginRecognized + "?socialnetwork=" + socialUserProfile.get("provider");				    		
							return socialLoginRecognized;
						}
			    		

							
					} else {
						//NOT Existed in FD
						
						return signUpUnrecognized;
					}
				}

				
	    }
		return null;
	    	
	}

	@Override
	public String register(FDSessionUser user, PageContext pageContext, ActionResult actionResult,
			int registrationType) throws Exception {

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
		HttpSession session = pageContext.getSession();
		String successPage = null;
		
		pageContext.getSession().removeAttribute("LITESIGNUP_COMPLETE");
		/*if(pageContext.getSession().getAttribute("REFERRALNAME") != null ) {
			if(!"done".equals(request.getParameter("submission"))) {
				actionResult.addError(new ActionError("Dummy","Dummy"));
				return true;
			}
		}*/
		RegistrationAction ra = new RegistrationAction(registrationType);

		HttpContext ctx =
			new HttpContext(
					session,
					request,
					response);

		ra.setHttpContext(ctx);
		ra.setResult(actionResult);
		/* APPDEV-1888 Refer a Friend */
		String result = ra.executeEx();
		if((Action.SUCCESS).equals(result)) {
			//if referral information is available, record it.
			if(pageContext.getSession().getAttribute("REFERRALNAME") != null) {
				try {
					//user = (FDSessionUser) session.getAttribute(USER);
					LOGGER.debug(user.getIdentity().getErpCustomerPK());
					LOGGER.debug(user.getUserId());
					LOGGER.debug((String) pageContext.getSession().getAttribute("REFERRALNAME"));
					LOGGER.debug("Adding referral record for CID:" + user.getIdentity().getErpCustomerPK() + "-email:" + user.getUserId() + "-reflink:" + (String) pageContext.getSession().getAttribute("REFERRALNAME"));
					String customerId = user.getIdentity().getErpCustomerPK();
					String referralCustomerId = FDCustomerManager.recordReferral(customerId, (String) pageContext.getSession().getAttribute("REFERRALNAME"), user.getUserId());
					user.setReferralCustomerId(referralCustomerId);
					session.setAttribute(SessionName.USER, user);
					//Record the referee signup in referral activitylog
					ErpActivityRecord rec = new ErpActivityRecord();
					rec.setActivityType(EnumAccountActivityType.REFEREE_SIGNEDUP);
					rec.setSource(EnumTransactionSource.WEBSITE);
					rec.setInitiator("CUSTOMER");
					rec.setCustomerId(referralCustomerId);
					rec.setDate(new Date());
					rec.setNote("<a href=\"/main/summary.jsp?erpCustId=" + customerId + "\">"+user.getUserId() + "</a> <a href=\"/main/summary.jsp?erpCustId=" + customerId + "\">ID #" + customerId + "</a>");
					new ErpLogActivityCommand(FDServiceLocator.getInstance(), rec).execute();
					//this.pageContext.getSession().removeAttribute("EXISTING_CUSTOMERID");
					//this.setSuccessPage("/registration/referee_signup2.jsp");
					successPage = "/registration/referee_signup2.jsp";
					//this.setAjax(true);
					CmRegistrationTag.setPendingRegistrationEvent(session);
				} catch (Exception e) {
					LOGGER.error("Exception when trying to update FDCustomer with referral ID",e);
				}
			} else {
				if("true".equals(pageContext.getRequest().getParameter("LITESIGNUP"))) {
					successPage = "/registration/signup_lite.jsp";
					//this.setAjax(true);
					pageContext.getSession().setAttribute("LITESIGNUP_COMPLETE", "true");
					CmRegistrationTag.setPendingRegistrationEvent(pageContext.getSession());
				}
			}

			user = (FDSessionUser) session.getAttribute(SessionName.USER);
			if (user != null) {
				user.setJustSignedUp(true);
			}
		}

	return successPage;
	}

	private boolean isUserIdExistForUserToken(String userToken) {

		String userId = "";
		
		if (userToken != null && userToken.length() > 0) {
			
			try {
				userId = ExternalAccountManager.getUserIdForUserToken(userToken);
			} catch (FDResourceException e) {
				LOGGER.error(e.getMessage());
			}
		}

		
		if (userId != null && userId.length() > 0) {
			return true;
		}
		
		return false;
		
	}

	private boolean isUserEmailAlreadyExist(String email) {
		try {
			return ExternalAccountManager.isUserEmailAlreadyExist(email);
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
		}

		return false;
	}

}
