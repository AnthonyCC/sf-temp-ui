/**
 * 
 */
package com.freshdirect.webapp.ajax.login;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

/**
 * @author ksriram
 *
 */
public class LoginServlet extends HttpServlet {

	private static final String ERROR_CODE_400 = "400";
	private static final String BAD_REQUEST = "Bad request";
	private static final String DATA = "data";
	private static final String ACCOUNT_DISABLED = "Account disabled";
	private static final String ERROR_AUTHENTICATION = "authentication";
	/**
	 * 
	 */
	private static final long serialVersionUID = 133867689987220127L;
	private static final Logger LOGGER = LoggerFactory.getInstance( LoginServlet.class );
	
	
	@Override
	/**
	 * This is an API to do the log-in based on the existing login logic(@LoginControllerTag.java).
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoginResponse loginResponse = new LoginResponse();
		LoginRequest loginRequest = parseRequestData(request, response, LoginRequest.class);
		
		if(null != loginRequest){
			String userId = loginRequest.getUserId();
		    String password = loginRequest.getPassword();
		        
		    if (userId == null ||  userId.length() < 1 ) {
		    	loginResponse.addError(new ActionError(EnumUserInfoName.USER_ID.getCode(), SystemMessageList.MSG_REQUIRED));
		    } else if (!EmailUtil.isValidEmailAddress(userId)) {
		    	loginResponse.addError(new ActionError(EnumUserInfoName.EMAIL_FORMAT.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
		    }
		        
		    if (password==null || password.length() < 1) {
		    	loginResponse.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_REQUIRED));
		    } else if (password.length() < 4) {
		    	loginResponse.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_TOO_SHORT));
		    }
		        
		    if (loginResponse.getErrorMessages()!=null && !loginResponse.getErrorMessages().isEmpty()) {
		    	writeResponse(response, loginResponse);
		    	return;
		    }
		        
		    try {
	        	FDIdentity identity = FDCustomerManager.login(userId,password);
	            LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK() + " : fdId = " + identity.getFDCustomerPK());
	            
	            FDUser loginUser = FDCustomerManager.recognize(identity);           
	            
	            LOGGER.info("FDUser : erpId = " + loginUser.getIdentity().getErpCustomerPK() + " : " + loginUser.getIdentity().getFDCustomerPK());
	            
	            HttpSession session = request.getSession();
	            FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
	            
	            if(session.getAttribute("TICK_TIE_CUSTOMER") != null) {
	            	session.removeAttribute(SessionName.USER);
	            	currentUser = null;
	            }
	            
	//	            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
	            
	            LOGGER.info("loginUser is " + loginUser.getFirstName() + " Level = " + loginUser.getLevel());
	            LOGGER.info("currentUser is " + (currentUser==null?"null":currentUser.getFirstName()+currentUser.getLevel()));
	            String currentUserId=null;
	            if (currentUser == null) {
	                // this is the case right after signout
	                UserUtil.createSessionUser(request, response, loginUser);
	                
	            } else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
	                // current user is different from user who just logged in
	                int currentLines = currentUser.getShoppingCart().numberOfOrderLines();
	                int loginLines = loginUser.getShoppingCart().numberOfOrderLines();
	                
	                //address needs to be set using logged in user's information - in case existing cart is used or cart merge
	                currentUser.getShoppingCart().setDeliveryAddress(loginUser.getShoppingCart().getDeliveryAddress());
	                
	                if ((currentLines > 0) && (loginLines > 0)) {
	                    /*// keep the current cart in the session and send them to the merge cart page
	                    if(null !=this.getSuccessPage() && !this.getSuccessPage().contains("/robin_hood") && !this.getSuccessPage().contains("/gift_card")){
		                    session.setAttribute(SessionName.CURRENT_CART, currentUser.getShoppingCart());
		                    this.setSuccessPage( mergePage + "?successPage=" + URLEncoder.encode( this.getSuccessPage() ) );
	                    }*/
	                    
	                } else if ((currentLines > 0) && (loginLines == 0)) {
	                    // keep current cart                	
	                    loginUser.setShoppingCart(currentUser.getShoppingCart());
	                    loginUser.getShoppingCart().setPricingContextToOrderLines(loginUser.getPricingContext());                                     
	                    
	                }
	                
	                // merge coupons
	                currentUserId= currentUser.getPrimaryKey();
	                
	                // current user has gift card recipients that need to be added to the login user's recipients list
	                if(currentUser.getLevel()==FDUserI.GUEST &&  currentUser.getRecipientList().getRecipients().size() > 0) {
	                	List<RecipientModel> tempList = currentUser.getRecipientList().getRecipients();
	                	ListIterator<RecipientModel> iterator = tempList.listIterator();
	                	//add currentUser's list to login user
	                	while(iterator.hasNext()) {
	                		SavedRecipientModel srm = (SavedRecipientModel)iterator.next();
	                		// reset the FDUserId to the login user
	                		srm.setFdUserId(loginUser.getUserId());
	                		loginUser.getRecipientList().removeRecipients(EnumGiftCardType.DONATION_GIFTCARD);
	                		loginUser.getRecipientList().addRecipient(srm);
	                	}
	
	                	/*Seems like no need to clear the recipients
	                	ListIterator i = currentUser.getRecipentList().getRecipents().listIterator();
	                	int index = 0;
	                	// remove currentUser's list
	                	while(i.hasNext()) {
	                		currentUser.getRecipentList().removeRecipient(index);
	                		index++;
	                	}*/
	                }
	                
	                loginUser.setGiftCardType(currentUser.getGiftCardType());
	                
	                if(currentUser.getDonationTotalQuantity()>0){
	                	loginUser.setDonationTotalQuantity(currentUser.getDonationTotalQuantity());
	                }
	                UserUtil.createSessionUser(request, response, loginUser);
	                //The previous recommendations of the current session need to be removed.
	                session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
	                session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
	                session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
	                
	            } else {
	                // the logged in user was the same as the current user,
	                // that means that they were previously recognized by their cookie before log in
	                // just set their login status and move on
	                currentUser.isLoggedIn(true);
	                session.setAttribute(SessionName.USER, currentUser);
	            }
	//	          loginUser.setEbtAccepted(loginUser.isEbtAccepted()&&(loginUser.getOrderHistory().getUnSettledEBTOrderCount()<=0));  
	          FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	          if(user != null) {
	              user.setEbtAccepted(user.isEbtAccepted()&&(user.getOrderHistory().getUnSettledEBTOrderCount()<1)&&!user.hasEBTAlert());
	              FDCustomerCouponUtil.initCustomerCoupons(session,currentUserId);
	          }
	          
	         /* 
	          if (user!=null && EnumServiceType.CORPORATE.equals(user.getUserServiceType())) {
	        	  if(request.getRequestURI().indexOf("index.jsp")!=-1 || (getSuccessPage()!=null && getSuccessPage().indexOf("/login/index.jsp")!=-1)){        	  
	                 this.setSuccessPage("/department.jsp?deptId=COS");;
	        	  }
	          }
	          */
	          //tick and tie for refer a friend program
	          if(session.getAttribute("TICK_TIE_CUSTOMER") != null) {
	        	  String ticktie = (String) session.getAttribute("TICK_TIE_CUSTOMER");
	        	  String custID = ticktie.substring(0, ticktie.indexOf("|"));
	        	  String refName = ticktie.substring(ticktie.indexOf("|"));
	        	  if(custID.equals(identity.getErpCustomerPK())) {
	        		  //the session is for this user only
	        		  String referralCustomerId = FDCustomerManager.recordReferral(custID, (String) request.getSession().getAttribute("REFERRALNAME"), user.getUserId());
	        		  LOGGER.debug("Tick and tie:" + user.getUserId() + " with:" + referralCustomerId);
	        		  user.setReferralCustomerId(referralCustomerId);
	        		  user.setReferralPromoList();
	        		  session.setAttribute(SessionName.USER, user);
	        	  }
	        	  session.removeAttribute("TICK_TIE_CUSTOMER");
	          }
	
	          CmRegistrationTag.setPendingLoginEvent(session);
	          
	        } catch (FDResourceException fdre) {
	            LOGGER.warn("Resource error during authentication", fdre);
	            loginResponse.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
	            
	        } catch (FDAuthenticationException fdae) {
	        	if(ACCOUNT_DISABLED.equals(fdae.getMessage())) {
	        		loginResponse.addError(new ActionError(ERROR_AUTHENTICATION, 
		            		MessageFormat.format(SystemMessageList.MSG_DEACTIVATED, 
		            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
	        	} else {
	        		loginResponse.addError(new ActionError(ERROR_AUTHENTICATION, 
		            		MessageFormat.format(SystemMessageList.MSG_AUTHENTICATION, 
		            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
	        	}
	        }
		    if(loginResponse.getErrorMessages()==null || loginResponse.getErrorMessages().isEmpty()){
	        	loginResponse.setSuccess(true);
	        }
        }
		else{
			loginResponse.addError(new ActionError(ERROR_CODE_400, BAD_REQUEST)); //400 - Bad request.
		}
        writeResponse(response, loginResponse);
	}
	
	private void writeResponse(HttpServletResponse response,LoginResponse loginResponse) {
		try {
			Writer w =response.getWriter();
			getMapper().writeValue(w, loginResponse);
		} catch (JsonGenerationException e) {
			LOGGER.warn("JsonGenerationException ", e);
		} catch (JsonMappingException e) {
			LOGGER.warn("JsonMappingException ", e);
		} catch (IOException e) {
			LOGGER.warn("IOException ", e);
		}
	}
	
	protected final static <T> T parseRequestData( HttpServletRequest request, HttpServletResponse response, Class<T> typeClass) throws IOException {
		
		String reqJson = request.getParameter( DATA );
		if(reqJson == null){
			reqJson = (String)request.getAttribute( DATA );
			if(reqJson != null){
				reqJson = URLDecoder.decode(reqJson, "UTF-8");				
			}
		}
				
		LOGGER.debug( "Parsing request data: " + reqJson );		
		T reqData = null;
		try {
			reqData = getMapper().readValue(reqJson, typeClass);
		} catch (Exception e) {
			LOGGER.warn("Exception while parsing the request: "+e);
		}
		
		return reqData;
	}
	
	protected static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
        return mapper;
    }
}
