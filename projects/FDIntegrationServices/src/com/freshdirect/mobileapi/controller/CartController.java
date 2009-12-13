package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;
import com.freshdirect.mobileapi.controller.data.request.MultipleRequest;
import com.freshdirect.mobileapi.controller.data.request.SimpleRequest;
import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;

public class CartController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(CartController.class);

    private static final String PARAM_PROMO_ID = "promoId";

    private static final String PARAM_CART_LINE_ID = "cartLineId";

    private static final String ACTION_REMOVE_ALCOHOL = "removealcoholfromcart";

    private static final String ACTION_REMOVE_PROMO = "removepromo";

    private static final String ACTION_APPLY_PROMO = "applypromo";

    private static final String ACTION_REMOVE_ALL_ITEMS_FROM_CART = "removeallitems";

    private static final String ACTION_REMOVE_ITEM_FROM_CART = "removeitem";

    private static final String ACTION_UPDATE_ITEM_IN_CART = "updateitem";

    private static final String ACTION_ADD_ITEM_TO_CART = "additem";

    private static final String ACTION_GET_CART_DETAIL = "getcartdetail";

    private static final String ACTION_ADD_MULTIPLE_ITEMS_TO_CART = "addmultipleitems";

    private static final String ACTION_REMOVE_MULTIPLE_ITEMS_TO_CART = "removemultipleitems";

    protected boolean validateCart() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_ADD_ITEM_TO_CART.equals(action)) {
            AddItemToCart reqestMessage = parseRequestObject(request, response, AddItemToCart.class);
            model = addItemInCart(model, user, reqestMessage, request);
        } else if (ACTION_REMOVE_ITEM_FROM_CART.equals(action) || ACTION_REMOVE_ALL_ITEMS_FROM_CART.equals(action)) {
            SimpleRequest reqestMessage = null;
            String cartLineId = null;
            boolean isRemoveAll = false;
            if (ACTION_REMOVE_ALL_ITEMS_FROM_CART.equals(action)) {
                reqestMessage = new SimpleRequest();
                isRemoveAll = true;
            } else {
                //Simplify this logic to not have simple request object when fully converted.
                cartLineId = request.getParameter(PARAM_CART_LINE_ID);
                if ((cartLineId == null) || (cartLineId.isEmpty())) {
                    cartLineId = parseRequestObject(request, response, SimpleRequest.class).getId();
                }
                isRemoveAll = false;
            }
            model = removeItemInCart(model, user, cartLineId, isRemoveAll, request);
        } else if (ACTION_UPDATE_ITEM_IN_CART.equals(action)) {
            UpdateItemInCart reqestMessage = parseRequestObject(request, response, UpdateItemInCart.class);
            model = updateItemInCart(model, user, reqestMessage, request);
        } else if (ACTION_GET_CART_DETAIL.equals(action)) {
            model = getCartDetail(model, user, request);
        } else if (ACTION_APPLY_PROMO.equals(action)) {
            String promoId = request.getParameter(PARAM_PROMO_ID);
            if (promoId != null) {
                model = applyPromoCode(model, user, promoId, request);
            } else {
                SimpleRequest reqestMessage = parseRequestObject(request, response, SimpleRequest.class);
                model = applyPromoCode(model, user, reqestMessage, request);
            }
        } else if (ACTION_REMOVE_PROMO.equals(action)) {
            String promoId = request.getParameter(PARAM_PROMO_ID);
            if (promoId != null) {
                model = removePromoCode(model, user, promoId, request);
            } else {
                SimpleRequest reqestMessage = parseRequestObject(request, response, SimpleRequest.class);
                model = removePromoCode(model, user, reqestMessage, request);
            }
        } else if (ACTION_REMOVE_ALCOHOL.equals(action)) {
            model = removeAlcohol(model, user, request);
        } else if (ACTION_ADD_MULTIPLE_ITEMS_TO_CART.equals(action)) {
            AddMultipleItemsToCart reqestMessage = parseRequestObject(request, response, AddMultipleItemsToCart.class);
            model = addMultipleItemsInCart(model, user, reqestMessage, request);
        } else if (ACTION_REMOVE_MULTIPLE_ITEMS_TO_CART.equals(action)) {
            MultipleRequest reqestMessage = parseRequestObject(request, response, MultipleRequest.class);
            model = removeMultipleItemsInCart(model, user, reqestMessage, request);
        }

        return model;
    }

    /**
     * @param model
     * @param user
     * @param request
     * @return
     * @throws JsonException
     * @throws FDException 
     */
    private ModelAndView getCartDetail(ModelAndView model, SessionUser user, HttpServletRequest request) throws JsonException, FDException {
        Cart cart = user.getShoppingCart();
        CartDetail cartDetail = cart.getCartDetail(user);
        com.freshdirect.mobileapi.controller.data.response.Cart responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        responseMessage.setSuccessMessage("Cart detail has been retrieved successfully.");
        responseMessage.setCartDetail(cartDetail);
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView removeAlcohol(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
        Message responseMessage = null;
        Cart cart = user.getShoppingCart();
        cart.removeAllAlcohol(user);

        CartDetail cartDetail = cart.getCartDetail(user);
        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
        responseMessage.setSuccessMessage("Alcoholic items have been removed from the cart successfully.");
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws ServiceException
     * @throws JsonException
     */
    private ModelAndView addItemInCart(ModelAndView model, SessionUser user, AddItemToCart reqestMessage, HttpServletRequest request)
            throws FDException, ServiceException, JsonException {
        Message responseMessage = null;

        Cart cart = user.getShoppingCart();
        Product product = Product.getProduct(reqestMessage.getProductConfiguration().getProductId(), reqestMessage
                .getProductConfiguration().getCategoryId(), user);
        if (!user.isHealthWarningAcknowledged() && product.isAlcoholProduct()) {
            responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage.addErrorMessage(ERR_HEALTH_WARNING, MobileApiProperties.getMediaPath() + MobileApiProperties.getAlcoholHealthWarningMediaPath());
        } else {
            ResultBundle resultBundle = cart.addItemToCart(reqestMessage, qetRequestData(request), user);
            ActionResult result = resultBundle.getActionResult();
            propogateSetSessionValues(request.getSession(), resultBundle);

            if (result.isSuccess()) {
                List<String> recentItems = (List<String>) resultBundle.getExtraData(Cart.RECENT_ITEMS);

                CartDetail cartDetail = cart.getCartDetail(user);
                responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
                ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
                ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setRecentlyAddedItems(recentItems);
                responseMessage.setSuccessMessage("Item has been added to cart successfully.");
            } else {
                responseMessage = getErrorMessage(result, request);
            }
            responseMessage.addWarningMessages(result.getWarnings());

        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param isRemoveAll
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView removeItemInCart(ModelAndView model, SessionUser user, String cartLineId, boolean isRemoveAll,
            HttpServletRequest request) throws FDException, JsonException {
        //Only difference between "remove an item" vs. "remove all" is the parameter of "Cart Line ID" being passed in.
        //If no "Cart Line ID" is passed in, then it's executed as "remove all" request.
        String successMessage = null;
        if (isRemoveAll) {
            successMessage = "All items have been removed from cart successfully.";
        } else {
            successMessage = "Item has been removed from cart successfully.";
        }
        Message responseMessage = null;

        Cart cart = user.getShoppingCart();
        ResultBundle resultBundle = cart.removeItemFromCart(cartLineId, qetRequestData(request), user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            CartDetail cartDetail = cart.getCartDetail(user);
            responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
            responseMessage.setSuccessMessage(successMessage);
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
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView updateItemInCart(ModelAndView model, SessionUser user, UpdateItemInCart reqestMessage, HttpServletRequest request)
            throws FDException, JsonException {
        Message responseMessage = null;
        Cart cart = user.getShoppingCart();
        ResultBundle resultBundle = cart.updateItemInCart(reqestMessage, qetRequestData(request), user);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            CartDetail cartDetail = cart.getCartDetail(user);
            responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
            responseMessage.setSuccessMessage("Cart line item has been updated successfully.");
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
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView removePromoCode(ModelAndView model, SessionUser user, SimpleRequest reqestMessage, HttpServletRequest request)
            throws FDException, JsonException {
        return removePromoCode(model, user, reqestMessage.getId(), request);
    }

    /**
     * @param model
     * @param user
     * @param promoId
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView removePromoCode(ModelAndView model, SessionUser user, String promoId, HttpServletRequest request)
            throws FDException, JsonException {
        Cart cart = user.getShoppingCart();
        ResultBundle resultBundle = cart.removeRedemptionCode(promoId, user);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Promo code has been removed successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView applyPromoCode(ModelAndView model, SessionUser user, SimpleRequest reqestMessage, HttpServletRequest request)
            throws FDException, JsonException {
        return applyPromoCode(model, user, reqestMessage.getId(), request);
    }

    /**
     * @param model
     * @param user
     * @param promoId
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView applyPromoCode(ModelAndView model, SessionUser user, String promoId, HttpServletRequest request)
            throws FDException, JsonException {
        Cart cart = user.getShoppingCart();
        ResultBundle resultBundle = cart.applyRedemptionCode(promoId, user);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Promo code has been applied successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView addMultipleItemsInCart(ModelAndView model, SessionUser user, AddMultipleItemsToCart reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Message responseMessage = null;

        Cart cart = user.getShoppingCart();

        ResultBundle resultBundle = cart.addMultipleItemsToCart(reqestMessage, qetRequestData(request), user);
        ActionResult result = resultBundle.getActionResult();

        if (result.isSuccess()) {
            List<String> recentItems = (List<String>) resultBundle.getExtraData(Cart.RECENT_ITEMS);

            CartDetail cartDetail = cart.getCartDetail(user);
            responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setRecentlyAddedItems(recentItems);
            responseMessage.setSuccessMessage("Items has been added to cart successfully.");

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
     * @param requestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView removeMultipleItemsInCart(ModelAndView model, SessionUser user, MultipleRequest requestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Message responseMessage = null;

        Cart cart = user.getShoppingCart();

        ResultBundle resultBundle = cart.removeMultipleItemsFromCart(requestMessage, qetRequestData(request), user);
        ActionResult result = resultBundle.getActionResult();

        CartDetail cartDetail = cart.getCartDetail(user);
        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);

        if (result.isSuccess()) {
            responseMessage.setSuccessMessage("Items has been removed from cart successfully.");

        } else {
            responseMessage = getErrorMessage(result, request);
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

}
