package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.request.UserAccountUpdateRequest;
import com.freshdirect.mobileapi.controller.data.response.UserAccountUpdateResponse;
import com.freshdirect.mobileapi.controller.data.response.UserAccountUpdateResponse.ResultCode;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.location.LocationHandlerTag;

public class UserController extends BaseController {

	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessController.class);

	public static final String ACTION_UPDATE_USER = "updateUser";
	public static final String ACTION_UPDATE_USER_ACCOUNT = "updateUser";
	public static final String ACTION_UPDATE_USER_ADDRESS = "updateUserAddress";	//JIRA FD-iPad FDIP-1062
	public static final String ACTION_UPDATE_USER_PAYMENTMETHOD = "updateUserPaymentMethod";
	public static final String ACTION_USER_RESERVE_DELIVERY_SLOT = "reserveDeliveryTimeSlot";

	

	protected boolean validateUser() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws FDException, ServiceException,
			NoSessionException, JsonException {
		if (ACTION_UPDATE_USER.equals(action)) {
//			RegisterMessage requestMessage = parseRequestObject(request,
//					response, RegisterMessage.class);
//			model = register(model, requestMessage, request, response,user);
			
//			LoggedIn userDataToUpdate = parseRequestObject( request, response, LoggedIn.class );
			if( /*userDataToUpdate.isOnMailingList()*/ user.isFutureZoneNotificationEmailSentForCurrentAddress() )
			{
				performUserUpdate( user, request );
			}
			
		}
		//JIRA FD-iPad FDIP-1062
		else if( ACTION_UPDATE_USER_ACCOUNT.equals(action))
		{
			UserAccountUpdateResponse responseObject = new UserAccountUpdateResponse();
			try
			{
				UserAccountUpdateRequest uau = parseRequestObject( request, response, UserAccountUpdateRequest.class );
				performUserAccountUpdate( request.getSession(), uau);
				responseObject.setResult(ResultCode.OK);
			}
			catch(JsonException je )
			{
				responseObject.setResult(ResultCode.REQUEST_FORMAT_ERROR);
			}
			catch (ErpInvalidPasswordException e)
			{
				responseObject.setResult(ResultCode.INVALID_PASSWORD_ERROR);
			}
			catch (ErpDuplicateUserIdException e)
			{
				responseObject.setResult(ResultCode.USERIDALREADYTAKEN);
			}
			
			ObjectMapper om = new ObjectMapper();
			String jsonresponse;
			try
			{
				jsonresponse = om.writeValueAsString(responseObject);
				PrintWriter pw = response.getWriter();
				pw.write(jsonresponse);
				pw.flush();
			}
			catch (JsonProcessingException e)
			{
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			catch (IOException e)
			{
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
		
    }
		return model;
	}

    protected <T> T parseRequestObject(HttpServletRequest request, HttpServletResponse response, Class<T> valueType) throws JsonException {
        try {
            return getMapper().readValue(getPostData(request, response), valueType);
        } catch (JsonGenerationException e) {
            throw new JsonException(e);
        } catch (JsonMappingException e) {
            throw new JsonException(e);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }
    
    private void performUserUpdate( SessionUser UserObject, HttpServletRequest request )
    {
    	UserUpdater updater = new UserUpdater( UserObject.getFDSessionUser(), request );
    	updater.execute();
    }
    
    private void performUserAccountUpdate( HttpSession Session, UserAccountUpdateRequest UAU )
    		throws FDAuthenticationException, FDResourceException, ErpInvalidPasswordException, ErpDuplicateUserIdException
    {
    	String oldUserID = UAU.getOldUserName();
    	String newUserID = UAU.getNewUserName();
    	String oldPW = UAU.getOldPassword();
    	String newPW = UAU.getNewPassword();

		//Verify user
		FDIdentity FDID = FDCustomerManager.login(oldUserID, oldPW);
		
		if( FDID == null )	//Not sure if this could happen, but I'm checking for it anyways
		{
			throw new FDAuthenticationException();
		}
		
		/*
		 * Implementation note: In the event of a fault condition, I'm changing the user's password first.
		 * My thinking is that, if a fault occurs somewhere in the execution of this code, it'll be easier for
		 * the user to remember their most recent email address than their most recent password choice, in the
		 * event that a follow-on call has to be made.
		 */

		//Adapted from AccountActivityUtil:
		FDSessionUser currentUser = (FDSessionUser) Session.getAttribute(SessionName.USER);
		FDActionInfo info = new FDActionInfo(EnumTransactionSource.IPHONE_WEBSITE, FDID, "CUSTOMER", "", null, (currentUser!=null)?currentUser.getPrimaryKey():null);

		//Update the user's password
		FDCustomerManager.changePassword(info, oldUserID, newPW);

		//Update the user's email
		FDCustomerManager.updateUserId(info, newUserID);
    }
    
    private void performUserAddressUpdate( UpdateUserAddressRequest UUAR )
    {
    	
    }
    
    private void performUserPaymentMethodUpdate( UpdateUserPaymentMethodRequest UUPMR )
    {
    	
    }
    
    private void performUserReserveDeliveryTimeSlot( UpdateUserDeliveryTimeSlotrequest UUPMR )
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
    
    private class UserUpdater extends LocationHandlerTag
    {
    	private FDSessionUser user;
    	private HttpServletRequest request;
    
    	public UserUpdater( FDSessionUser User, HttpServletRequest Request )
    	{
    		user = User;
    		request = Request;
    	}
    	
    	public void execute()
    	{
    		super.setAction("futureZoneNotification");	//set the action to perform
    		super.doTag();	//perform the action
    	}
    }
}
