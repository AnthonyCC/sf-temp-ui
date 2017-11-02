package com.freshdirect.webapp.ajax.expresscheckout.timeslot.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.logistics.delivery.model.TimeslotContext;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
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
	            		StandingOrderHelper.clearSO3ErrorDetails(user.getCurrentStandingOrder(), new String[]{"TIMESLOT","RELEASE_TIMESLOT"});
	            		StandingOrderHelper.clearSO3ErrorDetails(user.getCurrentStandingOrder(), new String[] {"ADDRESS","NO_ADDRESS"});
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
			} catch (ParseException e) {
                validationResult.getErrors().add(new ValidationError("Could not reserve timeslot due to technical difficulty."));
			}
			if (validationResult.getErrors().isEmpty()) {
				try {
					Map<String, Object> checkoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, PageAction.SELECT_DELIVERY_TIMESLOT, validationResult);
					responseData.getSubmitForm().setResult(checkoutData);
				} catch (FDResourceException e) {
                    validationResult.getErrors().add(new ValidationError("Could not load checkout data due to technical difficulty."));
				} catch (JspException e) {
                    validationResult.getErrors().add(new ValidationError("Could not load checkout data due to technical difficulty."));
				} catch (RedirectToPage e) {
                    validationResult.getErrors().add(new ValidationError("Could not load checkout data due to technical difficulty."));
				}
			}
			responseData.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
			writeResponseData(response, responseData);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Learn More HTML media for restricion message.", e);
		}
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        ValidationResult validationResult = new ValidationResult();

        try {
            String timeSlotId = NVL.apply(request.getParameter(TIMESLOT_ID), "");
            TimeslotContext timeSlotContext = TimeslotContext.valueOf(NVL.apply(request.getParameter(TIMESLOT_CONTEXT), TimeslotContext.CHECKOUT_TIMESLOTS.toString()));
            boolean forceOrder = Boolean.getBoolean(request.getParameter(FORCE_ORDER));
            boolean deliveryInfo = Boolean.getBoolean(request.getParameter(DELIVERY_INFO));
            boolean returnSameDaySlots = Boolean.getBoolean(request.getParameter(SAME_DAY_SLOTS));

            Result result = TimeslotService.defaultService().getTimeslot(request.getSession(), user.getShoppingCart().getDeliveryAddress(), user, timeSlotContext, true, timeSlotId,
                    forceOrder, deliveryInfo, returnSameDaySlots);

            for (ActionError error : result.getErrors()) {
                validationResult.addError(new ValidationError(error.getType(), error.getDescription()));
            }

            validationResult.setResult(SoyTemplateEngine.convertToMap(TimeslotService.defaultService().loadTimeslotsData(user, result.getDeliveryTimeslotModel()),
                    DateUtil.getStandardizedDateFormatter()));
        } catch (ReservationException e) {
            validationResult.getErrors().add(new ValidationError("Could not fetch timeslots due to technical difficulty."));
        } catch (FDResourceException e) {
            validationResult.getErrors().add(new ValidationError("Could not fetch timeslots due to technical difficulty."));
        }

        Map<String, ValidationResult> result = new HashMap<String, ValidationResult>();
        result.put(TIMESLOT_DATA, validationResult);
        writeResponseData(response, result);
    }

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
