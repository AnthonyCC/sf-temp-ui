package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDActionNotAllowedException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.EmailCapture;
import com.freshdirect.mobileapi.controller.data.request.UserAccountUpdateRequest;
import com.freshdirect.mobileapi.controller.data.response.MessageResponse;
import com.freshdirect.mobileapi.controller.data.response.UserAccountUpdateResponse;
import com.freshdirect.mobileapi.controller.data.response.UserAccountUpdateResponse.ResultCode;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.LocationHandlerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.location.LocationHandlerTag;

public class UserController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(SiteAccessController.class);

	private static final String ACTION_UPDATE_USER = "updateUser";
	private static final String ACTION_UPDATE_USER_EX = "updateUserEx";
	private static final String ACTION_UPDATE_USER_ACCOUNT = "updateUserAccount"; //FDIP-1062  modified to updateUserAccount from updateUser
	private static final String ACTION_UPDATE_USER_ADDRESS = "updateUserAddress";	//JIRA FD-iPad FDIP-1062
	private static final String ACTION_UPDATE_USER_PAYMENTMETHOD = "updateUserPaymentMethod";
	private static final String ACTION_USER_RESERVE_DELIVERY_SLOT = "reserveDeliveryTimeSlot";
	private static final String ACTION_USER_GET_NAME = "getUserName";
	private static final String ACTION_USER_SET_NAME = "setUserName";
	private static final String USER_ID_AND_PASSWORD_BOTH_EMPTY_ERROR_MESSAGE = "UserId and Password both empty nothing is to change";

	protected boolean validateUser() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see	 org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws FDException, ServiceException, NoSessionException, JsonException, FDResourceException {
		
		LOGGER.debug("Action to use: " + action);
		
		if (ACTION_UPDATE_USER.equals(action)) {
			if( user.isFutureZoneNotificationEmailSentForCurrentAddress() )
			{
				performUserUpdate( user, request );
			}
		} else if (ACTION_UPDATE_USER_EX.equals(action)){
		    EmailCapture requestMessage = parseRequestObject(request, response, EmailCapture.class);
		    LocationHandlerTagWrapper tagWrapper = new LocationHandlerTagWrapper(user);
	        ActionResult result = tagWrapper.setFutureZoneNotificationFdx(requestMessage);
	        Message responseMessage = null;
	        if (result.isSuccess()){
	            responseMessage = Message.createSuccessMessage("User signs up successfully for future zone notification.");
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        setResponseMessage(model, responseMessage, user);
		}
		//JIRA FD-iPad FDIP-1062
        else if (ACTION_UPDATE_USER_ACCOUNT.equals(action)) {
            UserAccountUpdateRequest uau = parseRequestObject(request, response, UserAccountUpdateRequest.class);
            if (isExtraResponseRequested(request)) {
                MessageResponse messageResponse = new MessageResponse();
                if ((uau.getNewPassword() == null || uau.getNewPassword().isEmpty()) && (uau.getNewUserName() == null || uau.getNewUserName().isEmpty())) {
                    messageResponse.setFailureMessage(USER_ID_AND_PASSWORD_BOTH_EMPTY_ERROR_MESSAGE);
                    LOGGER.error(USER_ID_AND_PASSWORD_BOTH_EMPTY_ERROR_MESSAGE);
                } else {
                    if (user.isLoggedIn()) {
                        try {
                            performUserAccountUpdate(request.getSession(), uau, user);
                            request.getSession().removeAttribute(SessionName.USER);
                            user = getUser(request, response);
                        } catch (FDAuthenticationException e) {
                            messageResponse.setFailureMessage(e.getMessage());
                            LOGGER.error(e.getMessage());
                        } catch (ErpInvalidPasswordException e) {
                            messageResponse.setFailureMessage(e.getMessage());
                            LOGGER.error(e.getMessage());
                        } catch (ErpDuplicateUserIdException e) {
                            messageResponse.setFailureMessage(e.getMessage());
                            LOGGER.error(e.getMessage());
                        } catch (FDActionNotAllowedException e) {
                            messageResponse.setFailureMessage(e.getMessage());
                            LOGGER.error(e.getMessage());
                        }
                    } else {
                        messageResponse.setFailureMessage("User is not logged in");
                        LOGGER.error("User is not logged in");
                    }
                }
                populateResponseWithEnabledAdditionsForWebClient(user, messageResponse, request, null);
                setResponseMessage(model, messageResponse, user);
                return model;
            } else {
                UserAccountUpdateResponse userResponse = new UserAccountUpdateResponse();
                // Check if a session exists and user is logged in if yes then proceed
                if (user == null) {
                    userResponse.setResult(ResultCode.USER_NOT_LOGGED_IN);
                } else {
                    // Check if both new userId and new password are empty
                    if ((uau.getNewPassword() == null || uau.getNewPassword().isEmpty()) && (uau.getNewUserName() == null || uau.getNewUserName().isEmpty())) {
                        LOGGER.error(USER_ID_AND_PASSWORD_BOTH_EMPTY_ERROR_MESSAGE);
                        userResponse.setResult(ResultCode.BOTH_USERNAME_PASSWORD_EMPTY);
                    } else {
                        try {
                            performUserAccountUpdate(request.getSession(), uau, user);
                            userResponse.setResult(ResultCode.OK);
                        } catch (ErpInvalidPasswordException e) {
                            userResponse.setResult(ResultCode.INVALID_PASSWORD_ERROR);
                        } catch (ErpDuplicateUserIdException e) {
                            userResponse.setResult(ResultCode.USERIDALREADYTAKEN);
                        } catch (FDActionNotAllowedException e) {
                            userResponse.setResult(ResultCode.INVALID_PASSWORD_ERROR);
                        }
                    }
                }
                setResponseMessage(model, userResponse, user);
            }
        }
		else if (ACTION_UPDATE_USER_ADDRESS.equals(action)) {
//        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
//            model = addDeliveryAddress(model, user, requestMessage, request);
			
			UpdateUserAddressRequest uur = parseRequestObject( request, response, UpdateUserAddressRequest.class );
			performUserAddressUpdate(uur);
			
        }else if (ACTION_UPDATE_USER_PAYMENTMETHOD.equals(action)) {
//        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
//          model = editDeliveryAddress(model, user, requestMessage, request);
			
        	UpdateUserPaymentMethodRequest uur = parseRequestObject( request, response, UpdateUserPaymentMethodRequest.class );
      	performUserPaymentMethodUpdate(uur);
			
      }else if (ACTION_USER_RESERVE_DELIVERY_SLOT.equals(action)) {
//      	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
//        model = editDeliveryAddress(model, user, requestMessage, request);
		
    	  UpdateUserDeliveryTimeSlotrequest uur = parseRequestObject( request, response, UpdateUserDeliveryTimeSlotrequest.class );
    	performUserReserveDeliveryTimeSlot(uur);
		
      } else if (ACTION_USER_GET_NAME.equals(action)){
    	  UserGetNameResponse ugnr = new UserGetNameResponse();
    	  if(user == null){
    		  setResponseMessage(model, Message.createFailureMessage("User not logged in"), user);
    		  return model;
    	  }
    	  ugnr.setFirstName(user.getFirstName());
    	  ugnr.setLastName(user.getLastName());
    	  
    	  setResponseMessage(model, ugnr, user);
    	  
      } else if (ACTION_USER_SET_NAME.equals(action)){
    	  if(user == null){
    		  setResponseMessage(model, Message.createFailureMessage("User not logged in"), user);
    		  return model;
    	  }
    	  UserSetNameRequest usnr = parseRequestObject(request, response, UserSetNameRequest.class);
    	  FDIdentity identity = user.getFDSessionUser().getIdentity();
    	  if(identity != null){    		  
    		  RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
    		  ResultBundle resultBundle = tagWrapper.updateUserContactNames(usnr.getFirstName(), usnr.getLastName());
    		  ActionResult actionResult = resultBundle.getActionResult();
    		  user.getFDSessionUser().updateUserState();
    		  if(actionResult.isFailure()){
    			  setResponseMessage(model, Message.createFailureMessage("Failed updating names"), user);
    		  } else {
    			  user.getFDSessionUser().resetCachedCustomerInfo();
    			  setResponseMessage(model, Message.createSuccessMessage(""), user);
    		  }
    	  }
      }
		return model;
	}

	@Deprecated // Use LocationHandlerTagWrapper class instead
    private void performUserUpdate( SessionUser user, HttpServletRequest request )
    {
    	UserUpdater updater = new UserUpdater();
    	updater.execute();
    }
    
    private void performUserAccountUpdate( HttpSession session, UserAccountUpdateRequest userRequest, SessionUser user )
    		throws FDAuthenticationException, FDResourceException, ErpInvalidPasswordException, ErpDuplicateUserIdException, FDActionNotAllowedException
    {
    	String oldUserID = userRequest.getOldUserName();
    	String newUserID = userRequest.getNewUserName();
    	String oldPW = userRequest.getOldPassword();
    	String newPW = userRequest.getNewPassword();

		//Verify user
    	String sessionUserId = user.getUsername();
    	
		FDIdentity identity = FDCustomerManager.login(oldUserID, oldPW);
		
		if( identity == null || !oldUserID.equalsIgnoreCase(sessionUserId) )	//Not sure if this could happen, its an edge case
		{
			throw new FDAuthenticationException("Login user id is not equal with old user id");
		}
		
		/*
		 * Implementation note: In the event of a fault condition, I'm changing the user's password first.
		 * My thinking is that, if a fault occurs somewhere in the execution of this code, it'll be easier for
		 * the user to remember their most recent email address than their most recent password choice, in the
		 * event that a follow-on call has to be made.
		 */

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDActionInfo aInfo = AccountActivityUtil.getActionInfo(session);

		//Update the user's password
		if(newPW!=null && !newPW.isEmpty()){
			FDCustomerManager.changePassword(aInfo, oldUserID, newPW);
		} else {
			LOGGER.info("Password Not being Changed.");
		}

		//Update the user's email
		if(newUserID!=null && !newUserID.isEmpty() ){
			if(currentUser.isVoucherHolder()){
				throw new FDActionNotAllowedException("This account is not enabled to change username.");
			}
			FDCustomerManager.updateUserId(aInfo, newUserID);
		} else {
			LOGGER.info("UserId not being Changed ");
		}
    }
    
    private void performUserAddressUpdate( UpdateUserAddressRequest uuar )
    {
    	
    }
    
    private void performUserPaymentMethodUpdate( UpdateUserPaymentMethodRequest uumr )
    {
    	
    }
    
    private void performUserReserveDeliveryTimeSlot( UpdateUserDeliveryTimeSlotrequest uupmr )
    {
    	
    }
	    
    private class UpdateUserAddressRequest
    {
    	
    }
    
    private class UpdateUserPaymentMethodRequest
    {
    	
    }
    
    private class UpdateUserDeliveryTimeSlotrequest
    {
    	
    }
    
    public static class UserGetNameResponse extends Message {
    	private String firstName;
    	private String lastName;
    	
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
    	
    }
    
    public static class UserSetNameRequest{
    	private String firstName;
    	private String lastName;
    	
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
    }
    
    private class UserUpdater extends LocationHandlerTag
    {
    	public void execute()
    	{
    		super.setAction("futureZoneNotification");	//set the action to perform
    		super.doTag();	//perform the action
    	}
    }
}
