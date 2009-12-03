package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.request.SimpleRequest;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.ModifiedOrder;
import com.freshdirect.mobileapi.controller.data.response.QuickShop;
import com.freshdirect.mobileapi.controller.data.response.QuickShopLists;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.Order;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;

/**
 * @author Rob
 *
 */
public class OrderController extends BaseController {
    private static final Category LOGGER = LoggerFactory.getInstance(OrderController.class);

    private static final String PARAM_ORDER_ID = "orderId";

    private static final String ACTION_GET_ORDER = "getorderdetail";

    private static final String ACTION_GET_QUICK_SHOP_ORDER_LIST = "getquickshoporders";

    private static final String ACTION_CANCEL_ORDER = "cancelorder";

    private static final String ACTION_LOAD_ORDER_TO_CART = "modifyorder";

    private static final String ACTION_CANCEL_ORDER_MODIFY = "cancelmodify";

    private static final String ACTION_QUICK_SHOP = "quickshop";

    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_GET_ORDER.equals(action)) {
            String orderId = request.getParameter(PARAM_ORDER_ID);
            if (orderId == null) {
                SimpleRequest requestMessage = parseRequestObject(request, response, SimpleRequest.class);
                orderId = requestMessage.getId();
            }
            model = getOrderDetail(model, user, orderId);
        } else if (ACTION_GET_QUICK_SHOP_ORDER_LIST.equals(action)) {
            model = getQuickshopOrders(model, user);
        } else if (ACTION_CANCEL_ORDER.equals(action)) {
            String orderId = request.getParameter(PARAM_ORDER_ID);
            if (orderId == null) {
                SimpleRequest requestMessage = parseRequestObject(request, response, SimpleRequest.class);
                orderId = requestMessage.getId();
            }
            model = cancelOrder(model, user, orderId, request);
        } else if (ACTION_LOAD_ORDER_TO_CART.equals(action)) {
            String orderId = request.getParameter(PARAM_ORDER_ID);
            if (orderId == null) {
                SimpleRequest requestMessage = parseRequestObject(request, response, SimpleRequest.class);
                orderId = requestMessage.getId();
            }
            model = loadOrder(model, user, orderId, request);
        } else if (ACTION_QUICK_SHOP.equals(action)) {
            String orderId = request.getParameter("orderId");
            model = getProductsFromOrder(model, user, orderId);
        } else if (ACTION_CANCEL_ORDER_MODIFY.equals(action)) {
            model = cancelOrderModify(model, user, request);
        }

        return model;
    }

    /**
     * 
     * @param user
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView cancelOrderModify(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException,
            JsonException {
        ResultBundle resultBundle = Order.cancelModify(user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Order modification was successfully canceled");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView loadOrder(ModelAndView model, SessionUser user, String orderId, HttpServletRequest request) throws FDException,
            JsonException {
        ResultBundle resultBundle = Order.loadOrderToCartForUpdate(orderId, user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            Cart cart = user.getShoppingCart();
            CartDetail cartDetail = cart.getCartDetail(user);
            responseMessage = new ModifiedOrder();
            ((ModifiedOrder) responseMessage).setCartDetail(cartDetail);
            ((ModifiedOrder) responseMessage).setModificationCutoffTime(cart.getModificationCutoffTime());
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView cancelOrder(ModelAndView model, SessionUser user, String orderId, HttpServletRequest request) throws FDException,
            JsonException {
        ResultBundle resultBundle = Order.cancelOrder(orderId, user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Order was successfully canceled");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView getOrderDetail(ModelAndView model, SessionUser user, String orderId) throws FDException, JsonException {
        Order order = user.getOrder(orderId);
        Message responseMessage = order.getOrderDetail(user);
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param user
     * @return
     * @throws FDException
     * @throws JsonException  
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView getQuickshopOrders(ModelAndView model, SessionUser user) throws FDException, JsonException {
        OrderHistory orderHistory = user.getOrderHistory();
        List<OrderInfo> infos = orderHistory.getCompletedOrderInfos();
        Message responseMessage = QuickShopLists.initWithOrder(infos);
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView getProductsFromOrder(ModelAndView model, SessionUser user, String orderId) throws FDException, JsonException {
        Order order = user.getOrder(orderId);

        List<ProductConfiguration> products;
        try {
            products = order.getOrderProducts(orderId, user);
        } catch (ModelException e) {
            throw new FDException(e);
        }
        QuickShop quickShop = new QuickShop();
        quickShop.setProducts(products);
        setResponseMessage(model, quickShop, user);
        return model;
    }

}
