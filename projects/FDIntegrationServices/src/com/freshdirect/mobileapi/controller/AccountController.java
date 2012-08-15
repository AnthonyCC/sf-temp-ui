package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.ReserveTimeslot;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order;
import com.freshdirect.mobileapi.controller.data.response.ReservationTimeslots;
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
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;

public class AccountController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(AccountController.class);

    private static String ACTION_GET_ADDRESSES = "getaddresses";

    private static String ACTION_GET_TIMESLOTS = "gettimeslots";

    private static String ACTION_CANCEL_RESERVATION = "cancelreservation";

    private static String ACTION_RESERVE_TIMESLOT = "reservetimeslot";

    private static String PARAM_ADDRESS_ID = "addressId";
    
    private static final String ACTION_GET_ORDER_HISTORY = "getorders";

    private static final String ACTION_ACCEPT_DP_TERMSANDCONDITIONS = "acceptDeliveryPassTermsAndConditions";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_GET_ADDRESSES.equals(action)) {
            model = getDeliveryAddresses(model, user);
        } else if (ACTION_GET_TIMESLOTS.equals(action)) {
            String addressId = request.getParameter(PARAM_ADDRESS_ID);
            if (addressId == null || addressId.isEmpty()) {
                model = getDeliveryTimeslot(model, user);
            } else {
                model = getDeliveryTimeslot(model, user, addressId);
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
            model = getOrderHistory(model, user, request, response);
        } else if (ACTION_ACCEPT_DP_TERMSANDCONDITIONS.equals(action)) {
           // APPDEV-2567 Logging DP Terms acceptance - mobile API
            FDCustomerManager.storeDPTCAgreeDate(AccountActivityUtil.getActionInfo(request.getSession())
            										, user.getFDSessionUser().getIdentity().getErpCustomerPK(), new Date());
            setResponseMessage(model, Message.createSuccessMessage(MSG_ACCEPT_DP_TERMSANDCONDITIONS), user);
        }
        return model;
    }

    private ModelAndView getOrderHistory(ModelAndView model, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        //ResultBundle resultBundle = Order.cancelModify(user);
        List<OrderInfo> orderInfos = user.getCompleteOrderHistory();
        
        OrderHistory responseMessage = new OrderHistory();
        List<Order> orders = OrderHistory.Order.createOrderList(orderInfos);
        
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
               
        ((OrderHistory) responseMessage).setOrders(paginator.getPage(page));
        
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

    private ModelAndView getDeliveryTimeslot(ModelAndView model, SessionUser user) throws FDException, JsonException, ServiceException {
        String addressId = user.getReservationAddressId();
        if (addressId == null) {
            if (user.getDefaultShipToAddress() != null){
                addressId = user.getDefaultShipToAddress();
            } else{
            	if(user.getDeliveryAddresses().size() == 0) {//This can happen when user signed up through Iphone.
            		Message responseMessage = Message.createSuccessMessage("You need to add a delivery address to perform this action.");
            		setResponseMessage(model, responseMessage, user);
            		return model;
            	}
            		
            		addressId =  user.getDeliveryAddresses().get(0).getId();
            }
        }
        return getDeliveryTimeslot(model, user, addressId);
    }

    private TimeSlotCalculationResult getTimeSlotCalculationResult(ModelAndView model, SessionUser user, String addressId)
            throws FDException, JsonException {
        ShipToAddress shipToAddress = user.getDeliveryAddress(addressId);
        TimeSlotCalculationResult timeSlotResult = shipToAddress.getDeliveryTimeslot(user, true);
        return timeSlotResult;
    }

    private ModelAndView getDeliveryTimeslot(ModelAndView model, SessionUser user, String addressId) throws FDException, JsonException,
            ServiceException {

        DeliveryAddresses deliveryAddresses = getDeliveryAddresses(user);
        deliveryAddresses.setPreSelectedId(addressId);
        TimeSlotCalculationResult timeSlotResult = getTimeSlotCalculationResult(model, user, addressId);
        DeliveryTimeslots deliveryTimeslots = new DeliveryTimeslots(timeSlotResult);

        ReservationTimeslots responseMessage = new ReservationTimeslots(deliveryAddresses, deliveryTimeslots, user);
        responseMessage.setSuccessMessage("Delivery timeslots have been retrieved successfully.");
        setResponseMessage(model, responseMessage, user);

        return model;
    }

    private DeliveryAddresses getDeliveryAddresses(SessionUser user) throws FDException, ServiceException, JsonException {
        List<ShipToAddress> deliveryAddresses = user.getDeliveryAddresses();
        DeliveryAddresses responseMessage = new DeliveryAddresses(null, ShipToAddress.filter(deliveryAddresses,
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

}
