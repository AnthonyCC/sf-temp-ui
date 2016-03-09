package com.freshdirect.webapp.ajax.expresscheckout.timeslot.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
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
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class TimeslotServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8229972384125974371L;

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
			        
			        if(null!=user.getCurrentStandingOrder().getAddressId()){
						StandingOrderHelper.populateSO3TimeslotDetails(user, deliveryTimeSlotId, user.getCurrentStandingOrder().getDeliveryAddress(), soFirstDate);
						StandingOrderUtil.createStandingOrder(request.getSession(), user.getSoTemplateCart(), user.getCurrentStandingOrder(), null);
							responseData.setShowSOProduct(StandingOrderHelper.isValidStandingOrder(user));
			        }else{
						validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not reserver timeslot for Standing order 3.0 due to address is not available."));

			        }
				}else{
					List<ValidationError> timeslotReservationErrors = TimeslotService.defaultService().reserveDeliveryTimeSlot(timeslotRequestData, user, request.getSession());
					validationResult.getErrors().addAll(timeslotReservationErrors);
				}
			} catch (FDResourceException e) {
				validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not reserve timeslot due to technical difficulty."));
			} catch (ParseException e) {
				validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not reserve timeslot due to technical difficulty."));
			}
			if (validationResult.getErrors().isEmpty()) {
				try {
					Map<String, Object> checkoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, PageAction.SELECT_DELIVERY_TIMESLOT, validationResult);
					responseData.getSubmitForm().setResult(checkoutData);
				} catch (FDResourceException e) {
					validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not load checkout data due to technical difficulty."));
				} catch (JspException e) {
					validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not load checkout data due to technical difficulty."));
				} catch (RedirectToPage e) {
					validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not load checkout data due to technical difficulty."));
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
	protected boolean synchronizeOnUser() {
		return false;
	}

}
