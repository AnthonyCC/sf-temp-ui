package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.EnumResponseAdditional;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.AddProfileRequest;
import com.freshdirect.mobileapi.controller.data.request.AutoRenewDp;
import com.freshdirect.mobileapi.controller.data.request.DlvPassRequest;
import com.freshdirect.mobileapi.controller.data.request.ReserveTimeslot;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.request.Timezone;
import com.freshdirect.mobileapi.controller.data.response.DPInfo;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots;
import com.freshdirect.mobileapi.controller.data.response.DpAllPlans;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order;
import com.freshdirect.mobileapi.controller.data.response.ReservationTimeslots;
import com.freshdirect.mobileapi.controller.data.response.StoreCredits;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.Timeslot;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ListPaginator;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service.DeliveryPassService;
import com.freshdirect.webapp.ajax.storecredits.service.StoreCreditsService;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class AccountController extends BaseController implements Comparator <Order>{

    private static Category LOGGER = LoggerFactory.getInstance(AccountController.class);

    private static final String ACTION_GET_ADDRESSES = "getaddresses";
    private static final String ACTION_GET_TIMESLOTS = "gettimeslots";
    private static final String ACTION_GET_TIMESLOTS_BY_TIMEZONE = "gettimeslotsbytimezone";
    private static final String ACTION_CANCEL_RESERVATION = "cancelreservation";
    private static final String ACTION_RESERVE_TIMESLOT = "reservetimeslot";
    private static final String PARAM_ADDRESS_ID = "addressId";
    private static final String ACTION_GET_ORDER_HISTORY = "getorders";
    private static final String ACTION_GET_CREDITED_ORDER_HISTORY = "getcreditedorders";
    private static final String ACTION_GET_CREDIT_HISTORY = "getcredits";
    private static final String ACTION_ADD_PROFILE = "addProfile";
    private static final String ACTION_DP_FREE_TRIAL="dpFreeTrial";
	private static final String ACTION_DP_GET_INFO="getDpInfo";
    private static final String ACTION_DP_AUTO_RENEW="dpAutoRenew";
    private final static String DLV_PASS_CART = "dlvPassCart";
    private final static String ACTION_GET_ALL_DLV_PASS_PLANS = "getAllDlvPassPlans";

    @Override
    protected boolean validateUser() {
		return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {
    	if(UserExists(user)){
	        if (ACTION_GET_ADDRESSES.equals(action)) {
	            model = getDeliveryAddresses(model, user);
	        } else if (ACTION_GET_TIMESLOTS.equals(action)) {
	            String addressId = request.getParameter(PARAM_ADDRESS_ID);
	            if (addressId == null || addressId.isEmpty()) {
	                model = getReservationTimeslot(model, request, user);
	            } else {
	                model = getReservationTimeslot(model, request, user, addressId);
	            }
	        } else if (ACTION_GET_TIMESLOTS_BY_TIMEZONE.equals(action)) {
	            String addressId = request.getParameter(PARAM_ADDRESS_ID);
	            Timezone requestMessage = parseRequestObject(request, response, Timezone.class);
	            String timezone = requestMessage.getTimezone();
	            boolean excludeaddr = requestMessage.getExcludeaddr();
	            if (addressId == null || addressId.isEmpty()) {
	            	model = getDeliveryTimeslotByTimezone(model, user, timezone,excludeaddr);
	            } else {
	            	model = getDeliveryTimeslotByTimezone(model, user, addressId, timezone,excludeaddr);
	            }
	        } else if (ACTION_CANCEL_RESERVATION.equals(action)) {
	            String addressId = request.getParameter(PARAM_ADDRESS_ID);
	            ReserveTimeslot requestMessage = parseRequestObject(request, response, ReserveTimeslot.class);
	            model = cancelReservation(model, user, addressId, defaultReservationType(requestMessage), request);
	        } else if (ACTION_RESERVE_TIMESLOT.equals(action)) {
	            String addressId = request.getParameter(PARAM_ADDRESS_ID);
	            ReserveTimeslot requestMessage = parseRequestObject(request, response, ReserveTimeslot.class);
	            model = makeReservation(model, user, addressId, defaultReservationType(requestMessage), request);
	        } else if (ACTION_GET_ORDER_HISTORY.equals(action)) {
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		DlvPassRequest requestMessage = parseRequestObject(request, response, DlvPassRequest.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            model = getOrderHistory(model, user, request, response, dlvPassCart);
	        } else if (ACTION_GET_CREDITED_ORDER_HISTORY.equals(action)) {
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		DlvPassRequest requestMessage = parseRequestObject(request, response, DlvPassRequest.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            model = getCreditedOrderHistory(model, user, request, response, dlvPassCart);
	        } else if (ACTION_GET_CREDIT_HISTORY.equals(action)) {
	            model = getCreditHistory(model, user, request, response);
	        } else if(ACTION_ADD_PROFILE.equals(action)){
	        	AddProfileRequest requestMessage = parseRequestObject(request, response, AddProfileRequest.class);
				String notePrefix = "Add Profile Attribute: " + requestMessage.getName() + " = " + requestMessage.getValue() + ", Note: ";
				FDActionInfo info =	AccountActivityUtil.getActionInfo(request.getSession(), notePrefix + requestMessage.getNotes());
				FDCustomerManager.setProfileAttribute(user.getFDSessionUser().getIdentity(),requestMessage.getName(), requestMessage.getValue(), info);
				setResponseMessage(model, Message.createSuccessMessage(MSG_ACCEPT_DP_TERMSANDCONDITIONS), user);
			} else if (ACTION_DP_FREE_TRIAL.equals(action)) {
				if(FDStoreProperties.isDlvPassFreeTrialOptinFeatureEnabled()){
					if (null != user.getFDSessionUser().getIdentity() && !user.getFDSessionUser().getDpFreeTrialOptin()) {
						if(null == user.getFDSessionUser().getDlvPassInfo() || EnumDlvPassStatus.NONE.equals(user.getFDSessionUser().getDlvPassInfo().getStatus())) {
							FDCustomerManager.updateDpFreeTrialOptin(true,
									user.getFDSessionUser().getIdentity().getFDCustomerPK(),AccountActivityUtil.getActionInfo(request.getSession()));
							user.updateDpFreeTrialOptin(true);
							setResponseMessage(model, Message.createSuccessMessage(MSG_DPFREETRIAL_OPTIN_SUCCESS), user);
						}else {
							Message responseMessage = new Message();
				            responseMessage.setStatus(Message.STATUS_FAILED);
				            responseMessage =  getErrorMessage(DPFREETRIAL_OPTIN_FAILED, MSG_DPFREETRIAL_OPTIN_ERROR);
				            setResponseMessage(model, responseMessage, user);
						}
	
					}else {
			    		Message responseMessage = new Message();
			            responseMessage.setStatus(Message.STATUS_FAILED);
			            responseMessage =  getErrorMessage(DPFREETRIAL_OPTIN_FAILED, MSG_DPFREETRIAL_OPTIN_ALREADY);
			            setResponseMessage(model, responseMessage, user);
					}
				} else{
					Message responseMessage = new Message();
		            responseMessage.setStatus(Message.STATUS_FAILED);
		            responseMessage =  getErrorMessage(DPFREETRIAL_OPTIN_FAILED, MSG_DPFREETRIAL_OPTIN_DISABLED_ERROR);
		            setResponseMessage(model, responseMessage, user);
				}
			} else if(ACTION_DP_GET_INFO.equals(action)){
				 model = getDpInfo(model, user, request, response);
			} else if(ACTION_DP_AUTO_RENEW.equals(action)){		
				 AutoRenewDp reqestMessage = parseRequestObject(request, response, AutoRenewDp.class);
				setAutoRenewal(request, response, model, user, reqestMessage);
			}else if(ACTION_GET_ALL_DLV_PASS_PLANS.equals(action)){
				 model = getdlvPassAllPlans(model, user, request, response);
			}
        }else{
    		Message responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage =  getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
            setResponseMessage(model, responseMessage, user);
    	}
        return model;
    }

	/**
	 * @param request
	 * @param model
	 * @param user
	 * @throws JsonException
	 */
	public void setAutoRenewal(HttpServletRequest request,  HttpServletResponse response, ModelAndView model, SessionUser user, AutoRenewDp reqestMessage) throws JsonException {
		if (null !=user.getFDSessionUser().getIdentity()) {
			boolean autoRenewDpON = Boolean.parseBoolean(reqestMessage.getAutoRenewFlag());
			FDSessionUser fdSessionUser = user.getFDSessionUser();
			 String fdPK = fdSessionUser.getIdentity().getFDCustomerPK();
			if (null != fdSessionUser.getDlvPassInfo() && EnumDlvPassStatus.ACTIVE.equals(fdSessionUser.getDlvPassInfo().getStatus())) {
				String dpType = fdSessionUser.getDlvPassInfo().getAutoRenewDPType().getAutoRenewalSKU();
				if (null != dpType) {
					try {
						FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession(), "DeliveryPass auto-renew Opt-in");
						FDCustomerManager.updateDpOptinDetails(autoRenewDpON, fdPK, dpType, info, info.geteStore());
						LOGGER.info("DeliveryPass Optin flag set to: "+autoRenewDpON+", by User: "+fdPK+" , for E-Store: "+info.geteStore().getContentId());
						setResponseMessage(model, Message.createSuccessMessage(MSG_DPFREETRIAL_OPTIN_SUCCESS), user);
					} catch (FDResourceException e) {
						Message responseMessage = new Message();
						responseMessage.setStatus(Message.STATUS_FAILED);
				        responseMessage =  getErrorMessage(DPFREETRIAL_OPTIN_FAILED, "something went wrong");
				        setResponseMessage(model, responseMessage, user);
					}
				}
			}else { // DP is not active
				Message responseMessage = new Message();
				 responseMessage.setStatus(Message.STATUS_FAILED);
		         responseMessage =  getErrorMessage(DPFREETRIAL_OPTIN_FAILED, MSG_DPFREETRIAL_OPTIN_DISABLED_ERROR);
		         setResponseMessage(model, responseMessage, user);
			}
		} 
	}

    public boolean UserExists(SessionUser user){
    	return user!=null ? true:false;
    }

    private ModelAndView getOrderHistory(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response, boolean dlvPassCart) throws FDException, JsonException {

        List<OrderInfo> orderInfos = user.getCompleteOrderHistory();

        OrderHistory responseMessage = new OrderHistory();
        List<Order> orders = OrderHistory.Order.createOrderList(orderInfos, user);

        String postData = getPostData(request, response);
        int page = 1;
        int resultMax = orders != null ? orders.size() : 0;
        responseMessage.setTotalResultCount(resultMax);
        LOGGER.debug("getOrderHistory:: PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
        }
        ListPaginator<Order> paginator = new ListPaginator<Order>(orders, resultMax);

        if(orders != null && !orders.isEmpty()){
        	java.util.Collections.sort(orders, new AccountController());
        	String firstOrder = orders.get(0).getId();
        	com.freshdirect.mobileapi.model.Order order = user.getOrder(firstOrder);
        	final com.freshdirect.mobileapi.controller.data.response.Order orderDetail = order.getOrderDetail(user, dlvPassCart);
            if (isExtraResponseRequested(request)) {
                ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), orderDetail.getCartDetail());
            }
            orders.get(0).setStatus(orderDetail.getStatus());

        }

        responseMessage.setOrders(paginator.getPage(page));

        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView getCreditHistory(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        boolean isSelfCreditFeatureActive = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.backOfficeSelfCredit, request.getCookies(),
                user.getFDSessionUser());
        com.freshdirect.webapp.ajax.storecredits.data.StoreCredits storeCredits = StoreCreditsService.defaultService().collectStoreCredits(user.getFDSessionUser(),
                isSelfCreditFeatureActive);
        StoreCredits responseMessage = new StoreCredits(storeCredits);
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView getDpInfo(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
    	if(user.getFDSessionUser()!=null && user.getFDSessionUser().isDlvPassPending()){
            user.getFDSessionUser().updateDlvPassInfo();
        }
    	FDUserDlvPassInfo dpmodel = user.getFDSessionUser().getDlvPassInfo();
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    	if(dpmodel!=null&&!dpmodel.getStatus().equals(EnumDlvPassStatus.NONE)){
	    	DPInfo responseMessage = new DPInfo();
	    	responseMessage.setDeliveryPassStatus(user.getFDSessionUser().getDeliveryPassStatus());
	    	responseMessage.setName(dpmodel.getTypePurchased().getName());
	    	responseMessage.setShortName(dpmodel.getTypePurchased().getShortName());
	    	responseMessage.setDescription(dpmodel.getDescription());
	    	responseMessage.setPurchaseDate(sdf.format(dpmodel.getPurchaseDate()));
	    	responseMessage.setExpDate(sdf.format(dpmodel.getExpDate()));
	    	responseMessage.setHasAutoRenewDP(user.getFDSessionUser().hasAutoRenewDP().getValue());
	    	responseMessage.setAutoRenewalDate(DeliveryPassUtil.getAutoRenewalDate(user.getFDSessionUser()));
	    	responseMessage.setPricePaid(dpmodel.getAmount());
			responseMessage.setOriginalOrderId(dpmodel.getOriginalOrderId());
	    	responseMessage.setdPSavings(dpmodel.getDPSavings());
	    	responseMessage.setDefaultPaymentMethodPK(user.getFDSessionUser()!=null&&user.getFDSessionUser().getFDCustomer()!=null?
	    												user.getFDSessionUser().getFDCustomer().getDefaultPaymentMethodPK():null);
	    	responseMessage.setIsMidweekPass(user.getFDSessionUser().hasMidWeekDlvPass());
	    	responseMessage.setNumberOfOrders(dpmodel.getUsedCount());
	        setResponseMessage(model, responseMessage, user);
    	}else
    	{
    		Message responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage =  getErrorMessage(ERR_NO_DP_FOUND, "No Delivery Pass found for user");
            setResponseMessage(model, responseMessage, user);
    	}
        return model;
    }
    
	private ModelAndView getdlvPassAllPlans(ModelAndView model, SessionUser user, HttpServletRequest request,
			HttpServletResponse response) throws FDException, JsonException {
		DpAllPlans responseMessage = new DpAllPlans();
		try {
			DeliveryPassData deliveryPassData = DeliveryPassService.defaultService()
					.loadDeliveryPasses(user.getFDSessionUser().getUser());
			responseMessage.setDeliveryPasses(deliveryPassData.getDeliveryPass());
			setResponseMessage(model, responseMessage, user);
		} catch (HttpErrorResponse e) {
			LOGGER.debug("Http Error while fetching all the DlvPassPlans" + e.getMessage());
		} catch (IOException e) {
			LOGGER.debug("IOException while fetching all the DlvPassPlans" + e.getMessage());
		} catch (TemplateException e) {
			LOGGER.debug("TemplateException while fetching all the DlvPassPlans" + e.getMessage());
		}
		return model;
	}

 private ModelAndView getCreditedOrderHistory(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response, boolean dlvPassCart) throws FDException, JsonException {

        List<OrderInfo> orderInfos = user.getCompleteOrderHistory();
        List<OrderInfo> creditedorderInfos = new ArrayList<OrderInfo>();
        for(OrderInfo orderinfo : orderInfos){
        	try {
				if(orderinfo.getPendingCreditAmount() > 0 || orderinfo.getApprovedCreditAmount() > 0){
					creditedorderInfos.add(orderinfo);
				}
			} catch (PricingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
        }
        orderInfos = creditedorderInfos;

        OrderHistory responseMessage = new OrderHistory();
        List<Order> orders = OrderHistory.Order.createOrderList(orderInfos, user);

        String postData = getPostData(request, response);
        int page = 1;
        int resultMax = orders != null ? orders.size() : 0;
        responseMessage.setTotalResultCount(resultMax);
        LOGGER.debug("getCreditedOrderHistory:: PostData received: [" + postData + "]");
        if (StringUtils.isNotEmpty(postData)) {
            SearchQuery requestMessage = parseRequestObject(request, response, SearchQuery.class);
            page = requestMessage.getPage();
            resultMax = requestMessage.getMax();
        }
        ListPaginator<Order> paginator = new ListPaginator<Order>(orders, resultMax);

        if(orders != null && !orders.isEmpty()){
        	java.util.Collections.sort(orders, new AccountController());
        	String firstOrder = orders.get(0).getId();
        	com.freshdirect.mobileapi.model.Order order = user.getOrder(firstOrder);
        	final com.freshdirect.mobileapi.controller.data.response.Order orderDetail = order.getOrderDetail(user, dlvPassCart);
            if (isExtraResponseRequested(request)) {
                ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), orderDetail.getCartDetail());
            }
            orders.get(0).setStatus(orderDetail.getStatus());

        }

        responseMessage.setOrders(paginator.getPage(page));

        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     *
     * @param requestMessage
     */
    private ReserveTimeslot defaultReservationType(ReserveTimeslot requestMessage) {
        if (requestMessage.getReservationType() == null) {
            requestMessage.setReservationType(Timeslot.getTimeslotReservationTypeCode());
        }
        return requestMessage;
    }

    /**
     * @param model
     * @param user
     * @param addressId
     * @param timeslotRequest
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView makeReservation(ModelAndView model, SessionUser user, String addressId, ReserveTimeslot timeslotRequest,
            HttpServletRequest request) throws FDException, JsonException {
        ResultBundle resultBundle = user.makeReservation(timeslotRequest.getAddressId(), timeslotRequest.getDeliveryTimeslotId(), timeslotRequest
                .getReservationType());
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Timeslot has been reserved successfully.");
        	if(user!=null && user.getReservation()!=null && user.getReservation().getExpirationDateTime()!=null ){
        		responseMessage.addNoticeMessage("ReservationEndTime", user.getReservation().getExpirationDateTime().toString());
        	}
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param addressId
     * @param timeslotRequest
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView cancelReservation(ModelAndView model, SessionUser user, String addressId, ReserveTimeslot timeslotRequest,
            HttpServletRequest request) throws FDException, JsonException {
        ResultBundle resultBundle = user.cancelReservation(timeslotRequest.getAddressId(), timeslotRequest.getDeliveryTimeslotId(), timeslotRequest
                .getReservationType());
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Timeslot has been cancelled successfully..");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView getReservationTimeslot(ModelAndView model, HttpServletRequest request, SessionUser user) throws FDException, JsonException, ServiceException {
        String addressId = null;
        //FDX-1873 - Show timeslots for anonymous address
        if((user.getAddress() == null || (user.getAddress() != null && !user.getAddress().isCustomerAnonymousAddress()))) {
        	addressId = user.getReservationAddressId();
        }
        ShipToAddress anonymousAddress = null;
        if (addressId == null) {
        	if(user.getAddress() != null && user.getAddress().getAddress1() != null && user.getAddress().getAddress1().trim().length() > 0
        			&& user.getAddress().isCustomerAnonymousAddress()) {
    			anonymousAddress = ShipToAddress.wrap(user.getAddress());
    		} else {
	        	if(user.getFDSessionUser() != null && user.getFDSessionUser().getIdentity() != null ) {
		            if (user.getDefaultShipToAddress() != null){
		                addressId = user.getDefaultShipToAddress();
		            } else{
		            	if(user.getDeliveryAddresses().size() > 0) {//This can happen when user signed up through Iphone.
		            		addressId =  user.getDeliveryAddresses().get(0).getId();
		            	}
		            }
	        	}
    		}
        }

        if (anonymousAddress != null) {
            DeliveryTimeslots deliveryTimeslots = getDeliveryTimeslots(user, addressId);
            ReservationTimeslots responseMessage = new ReservationTimeslots(new DeliveryAddresses(), deliveryTimeslots, user);
            responseMessage.setSuccessMessage("Delivery timeslots have been retrieved successfully.");
            setResponseMessage(model, responseMessage, user);
            return model;
    	} else if(addressId == null) {
    		// Temp fix for null address ids for iphone apps 2.2 and earlier
    		DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(null, user);//getDeliveryTimeslots(user, addressId);
            ReservationTimeslots responseMessage = new ReservationTimeslots(new DeliveryAddresses(), deliveryTimeslots, user);
            responseMessage.addWarningMessage("No address found for user");
            setResponseMessage(model, responseMessage, user);
            return model;
    	} else {
    		return getReservationTimeslot(model, request, user, addressId);
    	}

    }

    private ModelAndView getDeliveryTimeslotByTimezone(ModelAndView model, SessionUser user, String timezone, boolean excludeaddr) throws FDException, JsonException, ServiceException {
        String addressId = null;

        //FDX-1873 - Show timeslots for anonymous address
        if(user.getAddress() == null || (user.getAddress() != null && !user.getAddress().isCustomerAnonymousAddress())) {
        	addressId = user.getReservationAddressId();
        }
        ShipToAddress anonymousAddress = null;

        if (addressId == null) {
        	if(user.getAddress() != null && user.getAddress().getAddress1() != null && user.getAddress().getAddress1().trim().length() > 0 
        			&& user.getAddress().isCustomerAnonymousAddress()) {
    			anonymousAddress = ShipToAddress.wrap(user.getAddress());
    		} else {
	        	if(user.getFDSessionUser() != null && user.getFDSessionUser().getIdentity() != null ) {
		            if (user.getDefaultShipToAddress() != null){
		                addressId = user.getDefaultShipToAddress();
		            } else{
		            	if(user.getDeliveryAddresses().size() > 0) {//This can happen when user signed up through Iphone.
		            		addressId =  user.getDeliveryAddresses().get(0).getId();
		            	}
		            }
	        	}
    		}
        }
        
        LOGGER.info("getDeliveryTimeslotByTimezone: " + addressId );
        if (anonymousAddress != null) {
        	LOGGER.info("getDeliveryTimeslotByTimezone[anonymousAddress]: " + anonymousAddress.getStreet1() + ":" + anonymousAddress.getPostalCode() );
        	TimeSlotCalculationResult timeSlotResult = anonymousAddress.getDeliveryTimeslot(user, false);
            DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(timeSlotResult, user);
            ReservationTimeslots responseMessage = new ReservationTimeslots(new DeliveryAddresses(), deliveryTimeslots, user);
            responseMessage.setSuccessMessage("Delivery timeslots have been retrieved successfully.");
            setResponseMessage(model, responseMessage, user);
            return model;
    	} else if(addressId == null) {
    		// Temp fix for null address ids for iphone apps 2.2 and earlier
    		DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(null, user);//getDeliveryTimeslots(user, addressId);
            ReservationTimeslots responseMessage = new ReservationTimeslots(new DeliveryAddresses(), deliveryTimeslots, user);
            responseMessage.addWarningMessage("No address found for user");
            setResponseMessage(model, responseMessage, user);
            return model;
        } else {
    		return getDeliveryTimeslotByTimezone(model, user, addressId, timezone, excludeaddr);
    	}

    }

    private TimeSlotCalculationResult getTimeSlotCalculationResult(SessionUser user, String addressId)
            throws FDException, JsonException {
        ShipToAddress shipToAddress = user.getDeliveryAddress(addressId);
        TimeSlotCalculationResult timeSlotResult = shipToAddress!=null?shipToAddress.getDeliveryTimeslot(user, true):null;
        return timeSlotResult;
    }

    private ModelAndView getReservationTimeslot(ModelAndView model, HttpServletRequest request, SessionUser user, String addressId)
            throws FDException, JsonException, ServiceException {
        Message responseMessage = null;
        DeliveryTimeslots deliveryTimeslots = getDeliveryTimeslots(user, addressId);
        if (isResponseAdditionalEnable(request, EnumResponseAdditional.EXCLUDE_ADDRESS)){
            responseMessage = deliveryTimeslots;
        } else {
            DeliveryAddresses deliveryAddresses = getDeliveryAddresses(user);
            deliveryAddresses.setPreSelectedId(addressId);
            responseMessage = new ReservationTimeslots(deliveryAddresses, deliveryTimeslots, user);
        }
        if(user!=null && user.getReservation()!=null && user.getReservation().getExpirationDateTime()!=null ){
    		responseMessage.addNoticeMessage("ReservationEndTime", user.getReservation().getExpirationDateTime().toString());
    	}
        responseMessage.setSuccessMessage("Delivery timeslots have been retrieved successfully.");
        setResponseMessage(model, responseMessage, user);

        return model;
    }

    private DeliveryTimeslots getDeliveryTimeslots(SessionUser user, String addressId) throws FDException, JsonException {
        TimeSlotCalculationResult timeSlotResult = getTimeSlotCalculationResult(user, addressId);
        DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(timeSlotResult, user);

        if ("true".equalsIgnoreCase(FDStoreProperties.get(FDStoreProperties.PROP_EDT_EST_TIMESLOT_CONVERSION_ENABLED))) {// this is for FDX only.
            List<com.freshdirect.mobileapi.controller.data.response.Timeslot> tslist2 = new ArrayList<com.freshdirect.mobileapi.controller.data.response.Timeslot>();
            for (com.freshdirect.mobileapi.controller.data.response.Timeslot ts : deliveryTimeslots.getTimeSlots()) {
                ts.setStart(addHours(ts.getStartDate(), 1));
                ts.setEnd(addHours(ts.getEndDate(), 1));
                ts.setCutoffDate(addHours(ts.getCutoffDateDate(), 1));
                tslist2.add(ts);
            }
            deliveryTimeslots.setTimeSlots(tslist2);
        }
        return deliveryTimeslots;
    }

   private ModelAndView getDeliveryTimeslotByTimezone(ModelAndView model, SessionUser user, String addressId, String timezone, boolean excludeaddr) throws FDException, JsonException,
		   ServiceException {

	    if(user!=null&&user.getFDSessionUser()!=null&&user.getFDSessionUser().getIdentity()!=null){
			DeliveryAddresses deliveryAddresses = null;
			if(excludeaddr!=true){
				deliveryAddresses = getDeliveryAddresses(user);
				deliveryAddresses.setPreSelectedId(addressId);
			}
			TimeSlotCalculationResult timeSlotResult = getTimeSlotCalculationResult(user, addressId);
			DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(timeSlotResult, user);
	
			ReservationTimeslots responseMessage = new ReservationTimeslots(deliveryAddresses, deliveryTimeslots, user);
			if(user!=null && user.getReservation()!=null && user.getReservation().getExpirationDateTime()!=null ){
	    		responseMessage.addNoticeMessage("ReservationEndTime", user.getReservation().getExpirationDateTime().toString());
	    	}
			responseMessage.setSuccessMessage("Delivery timeslots have been retrieved successfully.");
			setResponseMessage(model, responseMessage, user);
		}else{
			DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(null, user);
            ReservationTimeslots responseMessage = new ReservationTimeslots(new DeliveryAddresses(), deliveryTimeslots, user);
            responseMessage.addWarningMessage("No identity found for user");
            setResponseMessage(model, responseMessage, user);
		}

		return model;
	}

    private DeliveryAddresses getDeliveryAddresses(SessionUser user) throws FDException, ServiceException, JsonException {
        List<ShipToAddress> deliveryAddresses = user.getDeliveryAddresses();
        DeliveryAddresses responseMessage = new DeliveryAddresses(null, null, ShipToAddress.filter(deliveryAddresses,
                DeliveryAddressType.RESIDENTIAL), ShipToAddress.filter(deliveryAddresses, DeliveryAddressType.CORP), null);

        responseMessage.setResidentialDeliveryMinimum(user.getMinimumOrderAmount());
        responseMessage.setDepotDeliveryMinimum(user.getMinimumOrderAmount());
        responseMessage.setCorporateDeliveryMinimum(user.getMinCorpOrderAmount());
        return responseMessage;
    }

    /**
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ServiceException
     * @throws JsonException
     */
    private ModelAndView getDeliveryAddresses(ModelAndView model, SessionUser user) throws FDException, ServiceException, JsonException {
        DeliveryAddresses responseMessage = getDeliveryAddresses(user);
        responseMessage.setSuccessMessage("Delivery Addresses have been retrieved successfully.");
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private static Date addHours(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hours);
		return cal.getTime();
	}

    @Override
    public int compare (Order order1, Order order2){
    	return order2.getId().compareTo(order1.getId());
    }
}
