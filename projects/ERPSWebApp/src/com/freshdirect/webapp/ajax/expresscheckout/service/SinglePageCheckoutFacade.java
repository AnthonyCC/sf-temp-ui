package com.freshdirect.webapp.ajax.expresscheckout.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.content.service.ContentFactoryService;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutSuccessData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service.SinglePageCheckoutHeaderService;
import com.freshdirect.webapp.ajax.expresscheckout.drawer.service.DrawerService;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.FormLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.LocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.data.SuccessPageData;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.service.ReceiptService;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.data.TextMessageAlertData;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.service.TextMessageAlertService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.checkout.PaymentMethodManipulator;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SinglePageCheckoutFacade {

	public static SinglePageCheckoutFacade defaultFacade() {
		return INSTANCE;
	}

	private static final SinglePageCheckoutFacade INSTANCE = new SinglePageCheckoutFacade();
	
	private PaymentService paymentService;
	private ReceiptService receiptService;
	private TimeslotService timeslotService;
	private AvailabilityService availabilityService;
	private ContentFactoryService contentFactoryService;
	private TextMessageAlertService textMessageAlertService;

	private DeliveryAddressService deliveryAddressService;

	private SinglePageCheckoutFacade() {
		paymentService = PaymentService.defaultService();
		receiptService = ReceiptService.defaultService();
		timeslotService = TimeslotService.defaultService();
		availabilityService = AvailabilityService.defaultService();
		contentFactoryService = ContentFactoryService.defaultService();
		textMessageAlertService = TextMessageAlertService.defaultService();
		deliveryAddressService = DeliveryAddressService.defaultService();
	}

	public SinglePageCheckoutData load(final FDUserI user, HttpServletRequest request) throws FDResourceException, IOException, TemplateException, JspException, RedirectToPage {
		SinglePageCheckoutData result = new SinglePageCheckoutData();
		List<ValidationError> modifyCartPreSelectionErrors = handleModifyCartPreSelections(user, request);
		// TODO: set modifyCartPreSelectionErrors into the potato
		Map<String, Object> preCheckOrderResult = CheckoutService.defaultService().preCheckOrder(user);
		result.setHeaderData(SinglePageCheckoutHeaderService.defaultService().populateHeader(user));
		result.setDrawer(DrawerService.defaultService().loadDrawer());
		result.setAddress(loadAddress(user,request.getSession()));
		result.setPayment(loadUserPaymentMethods(user, request));
		result.setAtpFailure(CheckoutService.defaultService().getAtpFailureFromOrderPreCheckResult(preCheckOrderResult));
		result.setFormMetaData(FormMetaDataService.defaultService().populateFormMetaData(user));
		result.setTimeslot(TimeslotService.defaultService().loadCartTimeslot(user.getShoppingCart()));
		result.setRestriction(CheckoutService.defaultService().getRestrictionFromOrderPreCheckResult(preCheckOrderResult));
		result.setRedirectUrl(RedirectService.defaultService().populateRedirectUrl("/expressco/view_cart.jsp", "warning_message", availabilityService.selectWarningType(user)));
		return result;
	}

	public FormLocationData loadAddress(final FDUserI user, HttpSession session) throws FDResourceException, JspException, RedirectToPage {
		List<LocationData> deliveryAddresses = deliveryAddressService.loadDeliveryAddress(user, session);
		FormLocationData formLocation = new FormLocationData();
		formLocation.setAddresses(deliveryAddresses);
		formLocation.setSelected(getSelectedAddressId(deliveryAddresses));
		return formLocation;
	}

	public FormLocationData loadAddressById(final FDUserI user, final String addressId) throws FDResourceException {
		List<LocationData> deliveryAddresses = deliveryAddressService.loadDeliveryAddressById(user, addressId);
		FormLocationData formLocation = new FormLocationData();
		formLocation.setAddresses(deliveryAddresses);
		formLocation.setSelected(getSelectedAddressId(deliveryAddresses));
		return formLocation;
	}

	public SinglePageCheckoutSuccessData loadSuccess(final String requestURI, final FDUserI user, String orderId) throws FDResourceException, IOException, TemplateException {
		SinglePageCheckoutSuccessData result = new SinglePageCheckoutSuccessData();
		FDOrderI order = loadOrder(orderId, user);
		result.setDrawer(DrawerService.defaultService().loadDrawer());
		result.setAddress(loadCartAddress(order, user));
		result.setPayment(loadCartPayment(order, user));
		result.setTimeslot(timeslotService.loadCartTimeslot(order));
		result.setSuccessPageData(loadSuccessPageData(order, requestURI, user));
		result.setTextMessageAlertData(loadTextMessageAlertData(user));
		return result;
	}

	private String getPaymentIdBasedOnModifyCartPaymentAccountNumber(FDUserI user) throws FDResourceException {
		String paymentId = FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity());
		Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods(user.getIdentity());
		for (ErpPaymentMethodI paymentMethod : paymentMethods) {
			if (user.getShoppingCart().getPaymentMethod() != null && paymentMethod.getAccountNumber().equals(user.getShoppingCart().getPaymentMethod().getAccountNumber())) {
				paymentId = paymentMethod.getPK().getId();
				break;
			}
		}
		return paymentId;
	}

	private String getSelectedAddressId(final List<LocationData> deliveryAddresses) {
		String selectedAddressId = null;
		for (LocationData deliveryAddress : deliveryAddresses) {
			if (deliveryAddress.isSelected()) {
				selectedAddressId = deliveryAddress.getId();
				break;
			}
		}
		return selectedAddressId;
	}

	private String getSelectedPaymentId(final List<PaymentData> payments) {
		String selectedPaymentId = null;
		for (PaymentData payment : payments) {
			if (payment.isSelected()) {
				selectedPaymentId = payment.getId();
				break;
			}
		}
		return selectedPaymentId;
	}

	private List<ValidationError> handleModifyCartPreSelections(FDUserI user, HttpServletRequest request) throws FDResourceException, JspException, RedirectToPage {
		FDCartModel cart = user.getShoppingCart();
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		HttpSession session = request.getSession();
		if (cart instanceof FDModifyCartModel) {
			Boolean modifyCartPreSelectionCompleted = (Boolean) session.getAttribute("modifyCartPreSelectionCompleted");
			if (modifyCartPreSelectionCompleted == null) {
				String addressId = cart.getDeliveryReservation().getAddressId();
				ActionResult actionResult = new ActionResult();
				String selectDeliveryAddressActionName = "selectDeliveryAddressMethod";
				DeliveryAddressManipulator.performSetDeliveryAddress(session, user, addressId, null, null, selectDeliveryAddressActionName, true, actionResult, null, null, null, null, null, null);
				String paymentId = getPaymentIdBasedOnModifyCartPaymentAccountNumber(user);
				String selectPaymentMethodActionName = "selectPaymentMethod";
				PaymentMethodManipulator.setPaymentMethod(paymentId, null, request, session, actionResult, selectPaymentMethodActionName);
				for (ActionError error : actionResult.getErrors()) {
					validationErrors.add(new ValidationError(error));
				}
				if (validationErrors.isEmpty()) {
					session.setAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED, true);
				}
			}
		} else {
			session.removeAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
		}
		return validationErrors;
	}

	private FormLocationData loadCartAddress(FDCartI cart, final FDUserI user) throws FDResourceException {
		List<LocationData> cartDeliveryAddresses = deliveryAddressService.loadSuccessLocations(cart, user);

		FormLocationData formLocation = new FormLocationData();
		formLocation.setAddresses(cartDeliveryAddresses);
		formLocation.setSelected(getSelectedAddressId(cartDeliveryAddresses));
		return formLocation;
	}

	private FormPaymentData loadCartPayment(final FDCartI cart, final FDUserI user) throws FDResourceException {
		List<PaymentData> cartPaymentDatas = paymentService.loadCartPayment(cart, user);

		FormPaymentData formPaymentData = new FormPaymentData();
		formPaymentData.setPayments(cartPaymentDatas);
		formPaymentData.setSelected(getSelectedPaymentId(cartPaymentDatas));
		return formPaymentData;
	}

	private FDOrderI loadOrder(final String orderNumber, final FDUserI user) throws FDResourceException {
		FDOrderI order;
		if (user != null && user.getIdentity() != null) {
			order = FDCustomerManager.getOrder(user.getIdentity(), orderNumber);
		} else {
			order = FDCustomerManager.getOrder(orderNumber);
		}
		return order;
	}

	private SuccessPageData loadSuccessPageData(final FDOrderI order, final String requestURI, final FDUserI user) throws FDResourceException {
		SuccessPageData successPageData = new SuccessPageData();
		successPageData.setHeader(contentFactoryService.getExpressCheckoutReceiptHeader(user));
		successPageData.setRightBlock(contentFactoryService.getExpressCheckoutReceiptEditorial(user));
		successPageData.setOrderId(order.getErpSalesId());
		successPageData.setReceipt(receiptService.populateReceiptData(order, requestURI, user));
		return successPageData;
	}

	private TextMessageAlertData loadTextMessageAlertData(final FDUserI user) throws FDResourceException, IOException, TemplateException {
		TextMessageAlertData textMessageAlertData = new TextMessageAlertData();
		textMessageAlertData.setHeader(contentFactoryService.getExpressCheckoutTextMessageAlertHeader(user));
		textMessageAlertData.setShow(textMessageAlertService.showTextMessageAlertPopup(user));
		textMessageAlertData.setMedia(textMessageAlertService.getTermsAndConditionsMedia());
		return textMessageAlertData;
	}

	private FormPaymentData loadUserPaymentMethods(FDUserI user, HttpServletRequest request) throws FDResourceException {
		List<PaymentData> userPaymentMethods = paymentService.loadUserPaymentMethods(user, request);
		FormPaymentData formPaymentData = new FormPaymentData();
		formPaymentData.setPayments(userPaymentMethods);
		for (PaymentData data : userPaymentMethods) {
			if (data.isSelected()) {
				formPaymentData.setSelected(data.getId());
				break;
			}
		}
		return formPaymentData;
	}

}
