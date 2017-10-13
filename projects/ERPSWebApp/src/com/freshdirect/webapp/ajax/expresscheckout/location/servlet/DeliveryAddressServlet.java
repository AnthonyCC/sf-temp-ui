package com.freshdirect.webapp.ajax.expresscheckout.location.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.FormLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.LocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationConstants;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class DeliveryAddressServlet extends BaseJsonServlet {
	
	private final static Category LOGGER = LoggerFactory.getInstance(DeliveryAddressServlet.class);

    private static final String ADDRESS_BY_ID_KEY = "address_by_id";

    private static final long serialVersionUID = -7582639712245761241L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            FormDataRequest deliveryAddressRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
            PageAction pageAction = FormDataService.defaultService().getPageAction(deliveryAddressRequest);
            ValidationResult validationResult = new ValidationResult();
            FormDataResponse deliveryAddressResponse = FormDataService.defaultService().prepareFormDataResponse(deliveryAddressRequest, validationResult);
            boolean canBeSaved= true;
            boolean save = true;
            
            if (pageAction != null) {
                switch (pageAction) {
                    case GET_DELIVERY_ADDRESS_METHOD: {
                        String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
                        FormLocationData deliveryAddressForm = SinglePageCheckoutFacade.defaultFacade().loadAddressById(user, deliveryAddressId);
                        LocationData selectedLocationData = SinglePageCheckoutFacade.defaultFacade().selectedLocationData(deliveryAddressForm);
                        ErpAddressModel selectedAddressModel = DeliveryAddressService.defaultService().createErpAddressModel(selectedLocationData);
                        deliveryAddressResponse.getSubmitForm().getResult().put(ADDRESS_BY_ID_KEY, deliveryAddressForm);
                        deliveryAddressResponse.getSubmitForm().getResult().put(DeliveryAddressValidationConstants.UNATTENDED_DELIVERY, DeliveryAddressService.defaultService().checkUnattendedDelivery(selectedAddressModel));
                        break;
                    }
                    case ADD_DELIVERY_ADDRESS_METHOD: {
                    	if (StandingOrderHelper.isSO3StandingOrder(user) && null != user.getCurrentStandingOrder()) {
                    		user.getCurrentStandingOrder().setOldAddressId(user.getCurrentStandingOrder().getAddressId());
                    		user.getCurrentStandingOrder().setNextDeliveryDate(null);
                    		user.getCurrentStandingOrder().setStartTime(null);
                    		user.getCurrentStandingOrder().setEndTime(null);
                    		save = false;
                    	}
                        List<ValidationError> validationErrors = DeliveryAddressService.defaultService().addDeliveryAddressMethod(deliveryAddressRequest, request.getSession(),
                                user);
                        validationResult.getErrors().addAll(validationErrors);
                        break;
                    }
                    case EDIT_DELIVERY_ADDRESS_METHOD: {
                    	List<ValidationError> validationErrors = DeliveryAddressService.defaultService().editDeliveryAddressMethod(deliveryAddressRequest, request.getSession(),
                                user);
                        validationResult.getErrors().addAll(validationErrors);
                        if (StandingOrderHelper.isSO3StandingOrder(user)) {
                        	save = false;
                        	LOGGER.debug("Delivery address edited sucessfully " + user.getCurrentStandingOrder().getAddressId() + " . initial addressID: "+ user.getCurrentStandingOrder().getOldAddressId());
                        }
                        break;
                    }
                    case DELETE_DELIVERY_ADDRESS_METHOD: {
                    	if (StandingOrderHelper.isSO3StandingOrder(user) && null != user.getCurrentStandingOrder()) {
                    		user.getCurrentStandingOrder().setOldAddressId(user.getCurrentStandingOrder().getAddressId());
                    		user.getCurrentStandingOrder().setNextDeliveryDate(null);
                    		save = false;
                    	}
                        DeliveryAddressService.defaultService().deleteDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
                        break;
                    }
                    case SELECT_DELIVERY_ADDRESS_METHOD: {
                        String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
                        if(StandingOrderHelper.isSO3StandingOrder(user)){
                        	if (user.getCurrentStandingOrder() != null && user.getCurrentStandingOrder().getNextDeliveryDate() != null 
                        			|| user.getCurrentStandingOrder().getOldAddressId() == null) {
                        		user.getCurrentStandingOrder().setOldAddressId(user.getCurrentStandingOrder().getAddressId());
                        		LOGGER.debug("user selected addressID from UI: " + deliveryAddressId + " . initial addressID: "+ user.getCurrentStandingOrder().getOldAddressId());
                        	}
                        	if(user != null && user.getCurrentStandingOrder() != null){
                        		canBeSaved = user.getCurrentStandingOrder().getOldAddressId() != null	&& user.getCurrentStandingOrder().getOldAddressId().equals(deliveryAddressId);
                        	}
                        	if(canBeSaved){
								FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(user.getCurrentStandingOrder().getId()));
								user.getCurrentStandingOrder().setNextDeliveryDate(so.getNextDeliveryDate());
								user.getCurrentStandingOrder().setStartTime(so.getStartTime());
								user.getCurrentStandingOrder().setEndTime(so.getEndTime());
								LOGGER.debug("restoring address timeslot for customer:"+user.getIdentity().getErpCustomerPK()+ " StandingOrder ID: "+ user.getCurrentStandingOrder().getId());
								canBeSaved = false;
							}
							else {
								user.getCurrentStandingOrder().setNextDeliveryDate(null);
								user.getCurrentStandingOrder().setStartTime(null);
								user.getCurrentStandingOrder().setEndTime(null);
								canBeSaved = false;
								LOGGER.debug("customer:"+user.getIdentity().getErpCustomerPK()+ " trying to modify address for StandingOrder ID: "+ user.getCurrentStandingOrder().getId());
							}
                        }
						String ebtPaymentRemovalApproved = FormDataService.defaultService().get(deliveryAddressRequest, "ebtPaymentRemovalApproved");
						List<ValidationError> validationErrors = new ArrayList<ValidationError>();
                        ErpAddressModel deliveryAddress = user.getShoppingCart().getDeliveryAddress();
                        if (deliveryAddressId != null) {
                            if (ebtPaymentRemovalApproved == null && deliveryAddress != null && !deliveryAddressId.equals(deliveryAddress.getId())) {
                                validationErrors.addAll(DeliveryAddressService.defaultService().checkEbtAddressPaymentSelectionByAddressId(user, deliveryAddressId));
                            }
                            if (validationErrors.isEmpty()) {
                                String pickupPhone = FormDataService.defaultService().get(deliveryAddressRequest, deliveryAddressId + "_phone");
                                validationErrors = DeliveryAddressService.defaultService().selectDeliveryAddressMethod(deliveryAddressId, pickupPhone, pageAction.actionName,
                                        request.getSession(), user);
                                PaymentService.defaultService().deselectEbtPayment(user, request.getSession());
                            }
                        }
                        validationResult.getErrors().addAll(validationErrors);
                        
                    }
                    default:
                        break;
                }
                if (!PageAction.GET_DELIVERY_ADDRESS_METHOD.equals(pageAction)) {
                    deliveryAddressResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
                }
            }
			if(StandingOrderHelper.isSO3StandingOrder(user)
					&& validationResult.getErrors().isEmpty() && canBeSaved){
				try {
   					StandingOrderHelper.clearSO3ErrorDetails(user.getCurrentStandingOrder(), new String[] {"ADDRESS","NO_ADDRESS"});

 					StandingOrderHelper.populateStandingOrderDetails(user.getCurrentStandingOrder(),deliveryAddressResponse.getSubmitForm().getResult());
                    user.setRefreshSO3(true);
                    if(user.getCurrentStandingOrder()!=null && user.getCurrentStandingOrder().getCustomerListId()!=null) {
                    	user.getCurrentStandingOrder().setDeleteDate(null);
						if(save){
                    		StandingOrderUtil.createStandingOrder(request.getSession(), user.getSoTemplateCart(), user.getCurrentStandingOrder(), null);
                    	}
                    }
					 
				} catch (FDResourceException e) {
					BaseJsonServlet.returnHttpError(500, "Error while selecting delivery address for user " + user.getUserId(), e);  				}
			}
            deliveryAddressResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
            writeResponseData(response, deliveryAddressResponse);
        } catch (FDResourceException e) {
            returnHttpError(500, "Failed to submit delivery address action.", e);
        } catch (IOException e) {
            returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
        } catch (TemplateException e) {
            returnHttpError(500, "Failed to render Learn More HTML media for restriction message.", e);
        } catch (JspException e) {
            returnHttpError(500, "Failed to check delivery pass status.", e);
        } catch (RedirectToPage e) {
            returnHttpError(500, "Failed to load checkout page data due to technical difficulties.", e);
        }
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.SIGNED_IN;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

}
