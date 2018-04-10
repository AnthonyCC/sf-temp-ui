package com.freshdirect.webapp.ajax.expresscheckout.timeslot.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotsData;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.Result;
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class TimeslotServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 8229972384125974371L;
    
	private static Category LOGGER = LoggerFactory.getInstance(TimeslotServlet.class);

    private static final String TIMESLOT_DATA = "timeslotData";
    private static final String TIMESLOT_ID = "timeslotId";
    private static final String FORCE_ORDER = "forceOrder";
    private static final String DELIVERY_INFO = "deliveryInfo";
    private static final String SAME_DAY_SLOTS = "sameDaySlots";
    private static final String TIMESLOT_CONTEXT = "timeslotContext";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			FormDataRequest timeslotRequestData = parseRequestData(request, FormDataRequest.class);
			ValidationResult validationResult = new ValidationResult();
			FormDataResponse responseData = FormDataService.defaultService().prepareFormDataResponse(timeslotRequestData, validationResult);
			try {
				  // To Save the selected time slot for Standing Order
				if(StandingOrderHelper.isSO3StandingOrder(user)){
					String deliveryTimeSlotId = FormDataService.defaultService().get(timeslotRequestData, "deliveryTimeslotId");  
			        String soFirstDate = FormDataService.defaultService().get(timeslotRequestData, "soFirstDate");
			        if(deliveryTimeSlotId==null){
			        	validationResult.getErrors().add(new ValidationError("deliveryTime", "You must select a delivery timeslot. Please select one from below or contact Us for help."));

			        } else if(null==user.getCurrentStandingOrder().getAddressId()){
						validationResult.getErrors().add(new ValidationError("deliveryAddress", "You must select a address."));

			        }else{

						StandingOrderHelper.populateSO3TimeslotDetails(user, deliveryTimeSlotId, user.getCurrentStandingOrder().getDeliveryAddress(), soFirstDate);
	            		StandingOrderHelper.clearSO3ErrorDetails(user.getCurrentStandingOrder(), new String[]{"TIMESLOT","RELEASE_TIMESLOT","ADDRESS","NO_ADDRESS"});
                        user.setRefreshSO3(true);
                        if(user.getCurrentStandingOrder()!=null && user.getCurrentStandingOrder().getCustomerListId()!=null) {
                        	user.getCurrentStandingOrder().setDeleteDate(null);
                        	StandingOrderUtil.createStandingOrder(request.getSession(), user.getSoTemplateCart(), user.getCurrentStandingOrder(), null);
                        }
                        	
			        }
				}else{
					List<ValidationError> timeslotReservationErrors = TimeslotService.defaultService().reserveDeliveryTimeSlot(timeslotRequestData, user, request.getSession());
					validationResult.getErrors().addAll(timeslotReservationErrors);
				}
			} catch (FDResourceException e) {
                validationResult.getErrors().add(new ValidationError("Could not reserve timeslot due to technical difficulty."));
                LOGGER.error("Could not reserve timeslot due to technical difficulty. 1" , e);
			} catch (ParseException e) {
                validationResult.getErrors().add(new ValidationError("Could not reserve timeslot due to technical difficulty."));
                LOGGER.error("Could not reserve timeslot due to technical difficulty. 2" , e);
			}
			if (validationResult.getErrors().isEmpty()) {
				try {
					Map<String, Object> checkoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, PageAction.SELECT_DELIVERY_TIMESLOT, validationResult);
					responseData.getSubmitForm().setResult(checkoutData);
				} catch (FDResourceException e) {
                    validationResult.getErrors().add(new ValidationError("Could not load checkout data due to technical difficulty."));
                    LOGGER.error("Could not reserve timeslot due to technical difficulty. 3" , e);
				} catch (JspException e) {
                    validationResult.getErrors().add(new ValidationError("Could not load checkout data due to technical difficulty."));
                    LOGGER.error("Could not reserve timeslot due to technical difficulty. 4" , e);
				} catch (RedirectToPage e) {
                    validationResult.getErrors().add(new ValidationError("Could not load checkout data due to technical difficulty."));
                    LOGGER.error("Could not reserve timeslot due to technical difficulty. 5" , e);
				}
			}
			responseData.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
			writeResponseData(response, responseData);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
            LOGGER.error("Failed to load Learn More media for restriction message." , e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Learn More HTML media for restricion message.", e);
            LOGGER.error("Failed to render Learn More HTML media for restricion message." , e);
		} catch (Exception e) {
            LOGGER.error("Could not reserve timeslot due to technical difficulty. 6" , e);
		}
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        ValidationResult validationResult = new ValidationResult();

        try {
        	if (request.getParameter("action") != null) {
        		doGetAction(request, response, user, request.getParameter("action"));
        		return;
        	}
            String timeSlotId = NVL.apply(request.getParameter(TIMESLOT_ID), "");
            TimeslotContext timeSlotContext = TimeslotContext.valueOf(NVL.apply(request.getParameter(TIMESLOT_CONTEXT), TimeslotContext.CHECKOUT_TIMESLOTS.toString()));
            boolean forceOrder = Boolean.getBoolean(request.getParameter(FORCE_ORDER));
            boolean deliveryInfo = Boolean.getBoolean(request.getParameter(DELIVERY_INFO));
            boolean returnSameDaySlots = Boolean.getBoolean(request.getParameter(SAME_DAY_SLOTS));

            ErpAddressModel deliveryAddress = user.getShoppingCart().getDeliveryAddress();
            if (deliveryAddress != null) {
                Result result = TimeslotService.defaultService().getTimeslot(request.getSession(), deliveryAddress, user, timeSlotContext, true, timeSlotId,
                        forceOrder, deliveryInfo, returnSameDaySlots);

                for (ActionError error : result.getErrors()) {
                    validationResult.addError(new ValidationError(error.getType(), error.getDescription()));
                }
                
                FormTimeslotsData returnData = TimeslotService.defaultService().loadTimeslotsData(user, result.getDeliveryTimeslotModel());
    
                addWarningMessages(returnData, user);
                
                
                if (result.isSuccess()) {
                    validationResult.setResult(SoyTemplateEngine.convertToMap(returnData, DateUtil.getStandardizedDateFormatter()));
                }
            }
        } catch (ReservationException e) {
            validationResult.getErrors().add(new ValidationError("Could not fetch timeslots due to technical difficulty."));
        } catch (FDResourceException e) {
            validationResult.getErrors().add(new ValidationError("Could not fetch timeslots due to technical difficulty."));
        }

        Map<String, ValidationResult> result = new HashMap<String, ValidationResult>();
        result.put(TIMESLOT_DATA, validationResult);
        writeResponseData(response, result);
    }

    private void addWarningMessages(FormTimeslotsData returnData, FDUserI user) {
        /* add dlvpass msg
         * taken from i_delivery_timeslots.jspf -> i_delivery_pass_not_applied.jspf
         * hard-coded for now
         * 20180328 batchley */
        if (user != null && user.getSelectedServiceType() == EnumServiceType.CORPORATE 
        	&& !(EnumCheckoutMode.MODIFY_SO_TMPL == user.getCheckoutMode())
        ) {
        	returnData.addWarningMessage("FreshDirect DeliveryPass is not valid for corporate/office deliveries");
        }
		
	}

	protected void doGetAction(HttpServletRequest request, HttpServletResponse response, FDUserI user, String action) throws HttpErrorResponse, FDResourceException {
    	if (action.equals("getCurrentSelected")) {
    		FormTimeslotData data;
    		
    		TimeslotService.defaultService().applyPreReservedDeliveryTimeslot(request.getSession());
    		
			if (StandingOrderHelper.isSO3StandingOrder(user)) {
				data = TimeslotService.defaultService().loadCartTimeslot(user, user.getSoTemplateCart());
	        } else {
	            data = TimeslotService.defaultService().loadCartTimeslot(user, user.getShoppingCart());
	        }
			writeResponseData(response, data);
    	}
    }
	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
