package com.freshdirect.mobileapi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.location.LocationHandlerTag;

public class UserController extends BaseController {

	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessController.class);

	public static final String ACTION_UPDATE_USER = "updateUser";
	public static final String ACTION_UPDATE_USER_ADDRESS = "updateUserAddress";
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
			
		} else if (ACTION_UPDATE_USER_ADDRESS.equals(action)) {
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
