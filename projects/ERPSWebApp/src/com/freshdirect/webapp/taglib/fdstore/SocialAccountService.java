package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.util.AccountUtil;
import com.freshdirect.webapp.util.StoreContextUtil;

public class SocialAccountService implements AccountService {

	private static Category LOGGER = LoggerFactory.getInstance(SocialAccountService.class);
	
	//private String signUpUnrecognized = "/social/DeliveryAddress.jsp";
	private String expressSignUpRelay = "/social/signup_lite_relay.jsp";
	//private String socialLoginMergePage ="/social/social_login_merge.jsp";
	private String socialLoginRecognized ="/social/social_login_recognized.jsp";
	private String termsConditions = "/registration/tcaccept_lite.jsp";
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
		String emailVerified="";
	    
	    if(socialUserProfile != null)
	    {
	    	//Set up the socialUserProfile in session so that you can track the user
	    	session.setAttribute(SessionName.SOCIAL_USER,socialUserProfile);
	    	
	    	userToken = socialUserProfile.get("userToken");
			socialUserId = socialUserProfile.get("email"); 
			//emailVerified = socialUserProfile.get("emailVerified"); 
			providerName  = socialUserProfile.get("provider"); 
			
			// handle not verified email
			if(socialUserId == null || socialUserId.length() == 0 /*|| 
			   emailVerified == null || emailVerified.length()== 0 || emailVerified.equalsIgnoreCase("N")*/)
			{
				 String newURL = request.getScheme() + "://" + request.getServerName() ;
				 if(FDStoreProperties.isLocalDeployment()){
					 newURL = newURL + ":" + request.getServerPort();
				 }
				 
				// User email not found. User will not be able to create account with this social.
				// The Social Provider did not return user email or user email missing.
				return newURL + socialCustomMessage; 				
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
						String markedUserId = socialUserId.substring(0, 2) + "******" + socialUserId.substring(socialUserId.indexOf("@"), socialUserId.length() );
						session.setAttribute("AlreadyConnectedSocialAccount", markedUserId);
						return socialAccountAlreadyConnected;
					}					
					
										
					/*  APPBUG-3955
					 *  on Account Setting page
					 *  Check if the just authenticated social email has been associated with the current logged in FD user
					 */
					if( sessionUserId !=null && socialUserId.equalsIgnoreCase(sessionUserId) &&
						(sessionUserLevel == FDUserI.SIGNED_IN)){
						
						socialLoginAccountLinked = socialLoginAccountLinked + "?NewlyLinkedSocialNetworkProvider=" + socialUserProfile.get("provider");	
						return socialLoginAccountLinked;	
					}	
					
					
							    
					/*
					 * The socialUserId has been linked with FD account. 
					 * Auto Login.
					 */
				    String updatedSuccessPage = UserUtil.loginUser(session, request, response,null, socialUserId, null, "", this.updatedSuccessPage, true);
							    
				    if(updatedSuccessPage != null) {
				       //redirect to successpage
						 String newURL = "";
						 if(FDStoreProperties.isLocalDeployment()){
							 newURL = "http" + "://" + request.getServerName() + ":" + request.getServerPort();
						 }else{
							 newURL = "https" + "://" + request.getServerName();
						 }
						 
				    	try {						
							 // APPDEV-4381 TC Accept.
				    		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				    		
							 if(!user.getTcAcknowledge()){
								//  response.sendRedirect(newURL+termsConditions);
								 	String requestUrl = request.getRequestURL().toString();
								 	
								 	boolean isFromLogin = Boolean.parseBoolean(request.getParameter("isFromLogin")); 
								 	String preSuccessPage = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
								 	
								 	session.setAttribute("fdTcAgree", false);
								 	
									if(preSuccessPage!=null){
									session.removeAttribute(SessionName.PREV_SUCCESS_PAGE);
									session.setAttribute("nextSuccesspage", preSuccessPage );
									// return  newURL + "/social/success.jsp?successPage="+preSuccessPage.substring(1, preSuccessPage.length());
									 response.sendRedirect(newURL+ "/social/success.jsp?successPage="+preSuccessPage.substring(1, preSuccessPage.length()));
									}else{
									session.setAttribute("nextSuccesspage", updatedSuccessPage );
									// return  newURL + "/social/success.jsp?successPage="+updatedSuccessPage.substring(1,this.updatedSuccessPage.length());
									 response.sendRedirect(newURL+ "/social/success.jsp?successPage="+updatedSuccessPage.substring(1,this.updatedSuccessPage.length()));
									}
									
									
								 }else{
									 LOGGER.info("successPage:"+updatedSuccessPage.substring(1,this.updatedSuccessPage.length()));
									 
									 // determine whether socialsignin is trigger from workflow
									 String preSuccessPage = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
									 if( preSuccessPage != null){
										 session.removeAttribute(SessionName.PREV_SUCCESS_PAGE);
										 return  newURL + "/social/success.jsp?successPage="+preSuccessPage.substring(1, preSuccessPage.length());
									 } else {
										 return  newURL + "/social/success.jsp?successPage="+updatedSuccessPage.substring(1,this.updatedSuccessPage.length());
									 }
								 
							 }

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
			    			socialLoginAccountLinked = socialLoginAccountLinked + "?NewlyLinkedSocialNetworkProvider=" + socialUserProfile.get("provider");	
							return socialLoginAccountLinked;							
						}else{							
				    		// Auto login
				    		UserUtil.loginUser(session, request, response,null, socialUserId, null, "", this.updatedSuccessPage, true);
				    		// APPDEV-4381 TC Accept.
				    		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				    		 if(!user.getTcAcknowledge()){
				    			 boolean isFromLogin = Boolean.parseBoolean(request.getParameter("isFromLogin"));
				    			 String requestUrl = request.getRequestURL().toString();
				    			 String newURL = "";
									 if(FDStoreProperties.isLocalDeployment()){
										 newURL = "http" + "://" + request.getServerName() + ":" + request.getServerPort();
									 }else{
										 newURL = "https" + "://" + request.getServerName();
									 }
									 String preSuccessPage = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
									
									if(preSuccessPage!=null){
										session.setAttribute("nextSuccesspage", preSuccessPage +"?socialnetwork="+socialUserProfile.get("provider"));
									}else {
										session.setAttribute("nextSuccesspage", termsConditions +"?socialnetwork="+socialUserProfile.get("provider"));
									}
									session.setAttribute("fdTcAgree", false);

									//termsConditions = termsConditions +"?socialnetwork="+socialUserProfile.get("provider");
									//return newURL + termsConditions;
									socialLoginRecognized = socialLoginRecognized + "?socialnetwork=" + socialUserProfile.get("provider");				    		
									return newURL + socialLoginRecognized;

							
				    			 }else{
									 String newURL = request.getScheme() + "://" + request.getServerName() ;
									 if(FDStoreProperties.isLocalDeployment()){
										 newURL = newURL + ":" + request.getServerPort();
									 }									 
						    		socialLoginRecognized = socialLoginRecognized + "?socialnetwork=" + socialUserProfile.get("provider");				    		
									return newURL + socialLoginRecognized;
				    			 }
							 }
			    									
					} else {
						//NOT Existed in FD
						
						
						// APPBUG-3908
			    		if(sessionUserLevel == FDUserI.SIGNED_IN){	

				    		try {
				    			
				    			String customerId = FDCustomerManager.getCustomerId(sessionUserId).getId();
				    			
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
				    		
			    			// Account Preference page
				    		socialLoginAccountLinked = socialLoginAccountLinked + "?NewlyLinkedSocialNetworkProvider=" + socialUserProfile.get("provider");	
							return socialLoginAccountLinked;							
						}						
						
						
						
						
						
						
						/*
						 * 'SOCIALONLYACCOUNT' 
						 */		
						
						/*
						 * session attributes used in RegistrationAction.java, class AccountInfo { initialize()}
						 */
						session.setAttribute("SOCIALONLYACCOUNT", true);   
						session.setAttribute("SOCIALONLYEMAIL", socialUserId);
						session.setAttribute("SOCIALONLYACCOUNT_SKIP_VALIDATION", true); 
						
						/*
						 * Retrieve firstName/lastName from socialUserProfile. 
						 */
						request.setAttribute("firstName", socialUserProfile.get("firstName"));
						request.setAttribute("lastName", socialUserProfile.get("lastName"));
						
						HttpContext ctx = new HttpContext( session, request, response);
						ActionResult result = new ActionResult();	
						
						/*
						 *  For express registration, 
						 *  	default "user type" to 'Home_USER'
						 *  	default "delivery status" to 'DONOT_DELIVER'
						 *  	default "available services" to 'PICKUP'
						 */
						int regType = AccountUtil.HOME_USER;
									
						EnumDeliveryStatus dlvStatus = EnumDeliveryStatus.DONOT_DELIVER;   
						//Set<EnumServiceType> availableServices = Collections.<EnumServiceType>emptySet(); 
						Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();
						availableServices.add(EnumServiceType.PICKUP);
						
						
						// Set RegistrationAction which will do the major work										
						RegistrationAction ra = new RegistrationAction(regType);
						ra.setHttpContext(ctx);
						ra.setResult(result);
						
															
						// Delegate to RegistrationAction to register the new user						
						try {							
							createUser(EnumServiceType.PICKUP, availableServices, session, response); 
							
							String res = ra.executeEx();
							if((Action.SUCCESS).equals(res)) {
								
								String newURL = request.getScheme() + "://" + request.getServerName() ;
								 if(FDStoreProperties.isLocalDeployment()){
									 newURL = newURL + ":" + request.getServerPort();
								 }								 
								 
								 // determine whether social-login is triggered from workflow or other source
								 String preSuccessPage = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
								 if( preSuccessPage != null){
									 session.removeAttribute(SessionName.PREV_SUCCESS_PAGE);
									 return  newURL + "/social/success.jsp?successPage="+preSuccessPage.substring(1, preSuccessPage.length());
								 } else {
									 return  newURL + "/social/success.jsp?successPage="+updatedSuccessPage.substring(1,this.updatedSuccessPage.length());
								 }								 
							}
						} catch (Exception ex) {
							LOGGER.error("Error performing action expresssignup", ex);
							result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
						}																		
				
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
			if(FDStoreProperties.isExtoleRafEnabled() && pageContext.getSession().getAttribute("CLICKID") != null){
				try {
					//user = (FDSessionUser) session.getAttribute(USER);
					LOGGER.debug(user.getIdentity().getErpCustomerPK());
					LOGGER.debug(user.getUserId());
					String rafPromoCode=(String) pageContext.getSession().getAttribute("COUPONCODE");
					String rafClickId=(String)pageContext.getSession().getAttribute("CLICKID");
					LOGGER.debug(rafClickId);
					LOGGER.debug(rafPromoCode);
					LOGGER.debug("Adding referral record for CID:" + user.getIdentity().getErpCustomerPK() + "-email:" + user.getUserId() + "-reflink:" + (String) pageContext.getSession().getAttribute("CLICKID"));
					String customerId = user.getIdentity().getErpCustomerPK();
				//	String referralCustomerId = FDCustomerManager.recordReferral(customerId, (String) pageContext.getSession().getAttribute("CLICKID"), user.getRafClickId());
			//		user.setReferralCustomerId(referralCustomerId);
					user.setRafPromoCode(rafPromoCode);
					user.setRafClickId(rafClickId);
					user.setRafFriendSignedUp(true);
					session.setAttribute(SessionName.USER, user);
					//Record the referee signup in referral activitylog
					ErpActivityRecord rec = new ErpActivityRecord();
					rec.setActivityType(EnumAccountActivityType.REFEREE_SIGNEDUP);
					rec.setSource(EnumTransactionSource.WEBSITE);
					rec.setInitiator("CUSTOMER");
			//		rec.setCustomerId(referralCustomerId);
					rec.setDate(new Date());
					rec.setNote("<a href=\"/main/summary.jsp?erpCustId=" + customerId + "\">"+user.getUserId() + "</a> <a href=\"/main/summary.jsp?erpCustId=" + customerId + "\">ID #" + customerId + "</a>");
		//			new ErpLogActivityCommand(FDServiceLocator.getInstance(), rec).execute();
					successPage="/registration/invite_signup2.jsp";
					//this.setAjax(true);
					CmRegistrationTag.setPendingRegistrationEvent(session);
				} catch (Exception e) {
					LOGGER.error("Exception when trying to update FDCustomer with referral ID",e);
				}
			} else if(!FDStoreProperties.isExtoleRafEnabled() && pageContext.getSession().getAttribute("REFERRALNAME") != null){
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
	
	/*
	 *  copied from SiteAccessControllerTag.java -createUser(EnumServiceType serviceType, Set availableServices)
	 */
	private void createUser(EnumServiceType serviceType, Set availableServices, HttpSession session, HttpServletResponse response) throws FDResourceException {
		//HttpSession session = pageContext.getSession();
		//HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

		AddressModel address = new AddressModel();   
		// set default address for sociallogin-only user
		address.setAddress1("23-30 borden ave");
		address.setCity("Long Island City");
		address.setState("NY");
		address.setCountry("US");
		address.setZipCode("11101");
		
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);		

			if ((user == null) || ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
				//
				// if there is no user object or a dummy user object created in
				// CallCenter, make a new using this zipcode
				// make sure to hang on to the cart that might be in progress in
				// CallCenter
				//
				FDCartModel oldCart = null;
				if (user != null) {
					oldCart = user.getShoppingCart();
				}
				StoreContext storeContext =StoreContextUtil.getStoreContext(session);				
				//user = new FDSessionUser(FDCustomerManager.createNewUser(this.address, serviceType, storeContext.getEStoreId()), session);
				user = new FDSessionUser(FDCustomerManager.createNewUser(address, serviceType, storeContext.getEStoreId()), session);
				user.setUserCreatedInThisSession(true);
				

				//user.setAddress(this.address);
				user.setAddress(address); 
				user.setSelectedServiceType(serviceType);
				//Added the following line for zone pricing to keep user service type up-to-date.
				user.setZPServiceType(serviceType);
				user.setAvailableServices(availableServices);			
				
				if (oldCart != null) {
					user.setShoppingCart(oldCart);
				}
	
				CookieMonster.storeCookie(user, response);
				session.setAttribute(SessionName.USER, user);
	
			} else {	
				//
				// otherwise, just update the zipcode in their existing object if
				// they haven't yet registered
				//
				if (user.getLevel() < FDUser.RECOGNIZED) {
					//user.setAddress(this.address);
					user.setAddress(address);
					user.setSelectedServiceType(serviceType);
					//Added the following line for zone pricing to keep user service type up-to-date.
					user.setZPServiceType(serviceType);
					user.setAvailableServices(availableServices);
					//Need to reset the pricing context so the pricing context can be recalculated.
					//user.resetPricingContext();
					//user.setP
					CookieMonster.storeCookie(user, response);
					FDCustomerManager.storeUser(user.getUser());
					session.setAttribute(SessionName.USER, user);
				}
							
			}		
			
		//To fetch and set customer's coupons.
		if(user != null){
			FDCustomerCouponUtil.initCustomerCoupons(session);
			user.setNewUserWelcomePageShown(true); //do not redirect to welcome.jsp 
		}
		
        //The previous recommendations of the current session need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
        session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
        session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
		
	}		

}
