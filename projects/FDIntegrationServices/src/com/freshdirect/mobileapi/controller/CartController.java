package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;
import com.freshdirect.mobileapi.controller.data.request.MultipleRequest;
import com.freshdirect.mobileapi.controller.data.request.DlvPassRequest;
import com.freshdirect.mobileapi.controller.data.request.RemoveItemInCart;
import com.freshdirect.mobileapi.controller.data.request.SimpleRequest;
import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

import freemarker.template.TemplateException;

public class CartController extends BaseController {

    private static final String PARAM_PROMO_ID = "promoId";
    private static final String PARAM_COUPON_ID = "couponId";
    private static final String PARAM_CART_LINE_ID = "cartLineId";
    private static final String PARAM_TIP_AMOUNT = "tipAmount";
    private static final String ACTION_REMOVE_ALCOHOL = "removealcoholfromcart";
    private static final String ACTION_REMOVE_PROMO = "removepromo";
    private static final String ACTION_APPLY_PROMO = "applypromo";
    private static final String ACTION_APPLY_CODE = "applycode";
    private static final String ACTION_REMOVE_ALL_ITEMS_FROM_CART = "removeallitems";
    private static final String ACTION_REMOVE_ITEM_FROM_CART = "removeitem";
    private static final String ACTION_UPDATE_ITEM_IN_CART = "updateitem";
    private static final String ACTION_ADD_ITEM_TO_CART = "additem";
    private static final String ACTION_GET_CART_DETAIL = "getcartdetail";
    private static final String ACTION_ADD_MULTIPLE_ITEMS_TO_CART = "addmultipleitems";
    private static final String ACTION_REMOVE_MULTIPLE_ITEMS_TO_CART = "removemultipleitems";
    private static final String ACTION_COUPON_CLIP = "clipcoupon";
    private static final String ACTION_VIEW_CARTLINE = "viewitem";
    private static final String ACTION_SET_TIP = "settip";
    private static final String ACTION_SAVE_CART = "save";
    private final static String DLV_PASS_CART = "dlvPassCart";
    
    @Override
    protected boolean validateCart() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException, NoSessionException {
    	if(UserExists(user)){
	        if (ACTION_ADD_ITEM_TO_CART.equals(action)) {
	            AddItemToCart reqestMessage = parseRequestObject(request, response, AddItemToCart.class);
	            model = addItemInCart(model, user, reqestMessage, request);
	        } else if (ACTION_REMOVE_ITEM_FROM_CART.equals(action) || ACTION_REMOVE_ALL_ITEMS_FROM_CART.equals(action)) {
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		RemoveItemInCart requestMessage = parseRequestObject(request, response, RemoveItemInCart.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            String cartLineId = null;
	            boolean isRemoveAll = false;
	            if (ACTION_REMOVE_ALL_ITEMS_FROM_CART.equals(action)) {
	                isRemoveAll = true;
	            } else {
	                //Simplify this logic to not have simple request object when fully converted.
	                cartLineId = request.getParameter(PARAM_CART_LINE_ID);
	                if ((cartLineId == null) || (cartLineId.isEmpty())) {
	                    cartLineId = parseRequestObject(request, response, SimpleRequest.class).getId();
	                }
	                isRemoveAll = false;
	            }
	            model = removeItemInCart(model, user, cartLineId, isRemoveAll, request, dlvPassCart);
	        } else if (ACTION_UPDATE_ITEM_IN_CART.equals(action)) {
	            UpdateItemInCart reqestMessage = parseRequestObject(request, response, UpdateItemInCart.class);
	            model = updateItemInCart(model, user, reqestMessage, request);
	        } else if (ACTION_GET_CART_DETAIL.equals(action)) {
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		DlvPassRequest requestMessage = parseRequestObject(request, response, DlvPassRequest.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            model = getCartDetail(model, user, request, dlvPassCart);
	        } else if (ACTION_APPLY_PROMO.equals(action)) {
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		DlvPassRequest requestMessage = parseRequestObject(request, response, DlvPassRequest.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            String promoId = request.getParameter(PARAM_PROMO_ID);
	            if (promoId != null) {
	                model = applyPromoCode(model, user, promoId, request, dlvPassCart);
	            } else {
	                SimpleRequest reqestMessage = parseRequestObject(request, response, SimpleRequest.class);
	                model = applyPromoCode(model, user, reqestMessage, request, dlvPassCart);
	            }
	        } else if(ACTION_APPLY_CODE.equals(action)){
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		DlvPassRequest requestMessage = parseRequestObject(request, response, DlvPassRequest.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            String promoId = request.getParameter(PARAM_PROMO_ID);
	            if (promoId != null) {
	            	if(isValidPromoId(user, promoId)){
	            		model = applyPromoCode(model, user, promoId, request, dlvPassCart);
	            	} else {
	            		model = applyCode(model, user, promoId, request, dlvPassCart);
	            	}
	                
	            } else {
	                SimpleRequest reqestMessage = parseRequestObject(request, response, SimpleRequest.class);
	                if(isValidPromoId(user, reqestMessage.getId())){
	                	model = applyPromoCode(model, user, reqestMessage, request, dlvPassCart);
	                } else {
	                	model = applyCode(model, user, reqestMessage, request, dlvPassCart);
	                }
	            }      	
	        }else if (ACTION_REMOVE_PROMO.equals(action)) {
	        	boolean dlvPassCart = false;
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DLV_PASS_CART)) {
	        		DlvPassRequest requestMessage = parseRequestObject(request, response, DlvPassRequest.class);
	        		dlvPassCart = requestMessage.isDlvPassCart();
	        	}
	            String promoId = request.getParameter(PARAM_PROMO_ID);
	            if (promoId != null) {
	                model = removePromoCode(model, user, promoId, request, dlvPassCart);
	            } else {
	                SimpleRequest reqestMessage = parseRequestObject(request, response, SimpleRequest.class);
	                model = removePromoCode(model, user, reqestMessage, request, dlvPassCart);
	            }
	        } else if (ACTION_REMOVE_ALCOHOL.equals(action)) {
	            model = removeAlcohol(model, user, request);
	        } else if (ACTION_ADD_MULTIPLE_ITEMS_TO_CART.equals(action)) {
	            AddMultipleItemsToCart reqestMessage = parseRequestObject(request, response, AddMultipleItemsToCart.class);
	            model = addMultipleItemsInCart(model, user, reqestMessage, request);
	        } else if (ACTION_REMOVE_MULTIPLE_ITEMS_TO_CART.equals(action)) {
	            MultipleRequest reqestMessage = parseRequestObject(request, response, MultipleRequest.class);
	            model = removeMultipleItemsInCart(model, user, reqestMessage, request);
	        }  else if (ACTION_COUPON_CLIP.equals(action)) {            
	            model = clipCoupon(model, user, request.getParameter(PARAM_COUPON_ID), request);
	        }   else if (ACTION_VIEW_CARTLINE.equals(action)) {            
	            try {
					model = getCartLine(model, request, response, user);
				} catch (NoSessionException e) {
					throw new ServiceException(e);
				} catch (ModelException e) {
					throw new ServiceException(e);
				}
	        } else if(ACTION_SET_TIP.equals(action)) {
	        	String tipAmount = request.getParameter(PARAM_TIP_AMOUNT);
	        	double _tip = Double.parseDouble(tipAmount);
	        	user.getShoppingCart().setTip(_tip);
	        	Message responseMessage = Message.createSuccessMessage("Tip added successfully.");
				setResponseMessage(model, responseMessage, user);
	        } else if(ACTION_SAVE_CART.equals(action)) {
	        	user.getFDSessionUser().saveCart(true);
	        	Message responseMessage = Message.createSuccessMessage("Cart Saved successfully");
				setResponseMessage(model, responseMessage, user);
	        }
    	}
    	else{
    		Message responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage =  getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
            setResponseMessage(model, responseMessage, user);
    	}
        return model;
    }
    
    public boolean UserExists(SessionUser user){
    	return user!=null ? true:false;
    }
    
    private boolean isValidPromoId(SessionUser user, String promoId) throws FDResourceException {
        Cart cart = user.getShoppingCart();
        boolean isValidPromoId = cart.isValidPromoId(promoId, user);
       return isValidPromoId;
    }

    private ModelAndView getCartLine(ModelAndView model, HttpServletRequest request, HttpServletResponse response, SessionUser user)
            throws ServiceException, FDException, JsonException, NoSessionException, ModelException {

        com.freshdirect.mobileapi.model.Product product = null;
        int cartLineId = Integer.parseInt(request.getParameter("cartLineId"));
        FDCartLineI cartLine = user.getShoppingCart().getOrderLineById(cartLineId);
        
        if (cartLine != null) {
        	String categoryId = cartLine.getCategoryName();
            String productId = cartLine.getProductName();
            if (!(StringUtils.isEmpty(categoryId) && StringUtils.isEmpty(productId))) {            
            	product = com.freshdirect.mobileapi.model.Product.getProduct(productId, categoryId, cartLine, getUserFromSession(request, response));
            }
        }        
        
        Message responseMessage = null;
        // FIXME
        if (product != null) {
        	try {
                responseMessage = new com.freshdirect.mobileapi.controller.data.Product(product);
                if (null != ((com.freshdirect.mobileapi.controller.data.Product) responseMessage).getProductTerms()) {
                    ((com.freshdirect.mobileapi.controller.data.Product) responseMessage)
                    	.setProductTerms(getProductWrappedTerms(((com.freshdirect.mobileapi.controller.data.Product) responseMessage).getProductTerms()));
                }
            } catch (ModelException e) {
                throw new FDException(e);
            } catch (IOException e) {
                throw new FDException(e);
            } catch (TemplateException e) {
                throw new FDException(e);
            }            
        }
        setResponseMessage(model, responseMessage, user);
        return model;

    }

    private ModelAndView getCartDetail(ModelAndView model, SessionUser user, HttpServletRequest request, boolean dlvPassCart) throws FDException, JsonException {
    	TimeslotService.defaultService().applyPreReservedDeliveryTimeslot(request.getSession());
        
    	Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", dlvPassCart));
    	
        if(ContentFactory.getInstance().getCurrentUserContext().getPricingContext() == null && user != null){
        	if(null == user.getUserContext()){
        		user.getFDSessionUser().getUser().resetUserContext();
        	}
        	user.setUserContext();
    	}
        
        if(user.getFDSessionUser()!=null && user.getFDSessionUser().isDlvPassPending()){
            user.getFDSessionUser().updateDlvPassInfo();
        }
       
        CartDetail cartDetail = cart.getCartDetail(user, EnumCouponContext.VIEWCART, false, dlvPassCart);
        com.freshdirect.mobileapi.controller.data.response.Cart responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        responseMessage.setSuccessMessage("Cart detail has been retrieved successfully.");
        responseMessage.setCartDetail(cartDetail);
        if(!user.getFDSessionUser().isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
        }
        
        // populate potatoes
        if (isExtraResponseRequested(request)) {
            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
        }

        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView removeAlcohol(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
        Message responseMessage = null;
        Cart cart = user.getShoppingCart();
        cart.removeAllAlcohol(user);

        CartDetail cartDetail = cart.getCartDetail(user, null);
        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
        
        if (isExtraResponseRequested(request)) {
            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
        }
        
        responseMessage.setSuccessMessage("Alcoholic items have been removed from the cart successfully.");
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView addItemInCart(ModelAndView model, SessionUser user, AddItemToCart reqestMessage, HttpServletRequest request)
            throws FDException, ServiceException, JsonException, NoSessionException {
        Message responseMessage = null;

        if (isZipCheck(user, request)) {
            responseMessage = getErrorMessage(ERR_ZIP_REQUIRED, ERR_ZIP_REQUIRED_MSG);
        } else {
        	
        	Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", reqestMessage.isDlvPassCart()));
        	if(reqestMessage.isDlvPassCart()){
        		// clear delivery pass cart before adding new dp to the cart
        		UserUtil.getCart(user.getFDSessionUser(), "", reqestMessage.isDlvPassCart()).clearOrderLines();
        		cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", reqestMessage.isDlvPassCart()));
        	}
        	
            Product product = Product.getProduct(reqestMessage.getProductConfiguration().getProductId(), reqestMessage.getProductConfiguration().getCategoryId(), null, user);
            if (product != null) {
                if (!user.isHealthWarningAcknowledged() && product.isAlcoholProduct()) {
                    responseMessage = new Message();
                    responseMessage.setStatus(Message.STATUS_FAILED);
                    responseMessage.addErrorMessage(ERR_HEALTH_WARNING, MobileApiProperties.getMediaPath() + MobileApiProperties.getAlcoholHealthWarningMediaPath());
                } else {
                    ResultBundle resultBundle = cart.addItemToCart(reqestMessage, qetRequestData(request), user, request, reqestMessage.isDlvPassCart());
                    ActionResult result = resultBundle.getActionResult();
                    propogateSetSessionValues(request.getSession(), resultBundle);

                    if (result.isSuccess()) {
                        List<String> recentItems = (List<String>) resultBundle.getExtraData(Cart.RECENT_ITEMS);
                        
                        CartDetail cartDetail = cart.getCartDetail(user, null, reqestMessage.isQuickBuy(), reqestMessage.isDlvPassCart());
                        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
                        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
                        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setRecentlyAddedItems(recentItems);

                        if (isExtraResponseRequested(request)) {
                            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
                        }

                        responseMessage.setSuccessMessage("Item has been added to cart successfully.");
                    } else {
                        responseMessage = getErrorMessage(result, request);
                    }
                    responseMessage.addWarningMessages(result.getWarnings());

                }
            } else {
                responseMessage = new Message();
                responseMessage.setStatus(Message.STATUS_FAILED);
                responseMessage = Message.createFailureMessage("Products are not available.");
            }
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private boolean isZipCheck(SessionUser user, HttpServletRequest request) {
        boolean isLoggedOutUserZipCheck = !user.isLoggedIn() && user.getAddress() != null && !user.getAddress().isCustomerAnonymousAddress();
        boolean isLoggedInUserZipCheck = user.isLoggedIn() && user.getShoppingCart() != null && user.getShoppingCart().getDeliveryAddress() == null 
        								  && user.getAddress() != null && !user.getAddress().isCustomerAnonymousAddress();
        return isExtraResponseRequested(request) && (isLoggedOutUserZipCheck || isLoggedInUserZipCheck);
    }

    private ModelAndView removeItemInCart(ModelAndView model, SessionUser user, String cartLineId, boolean isRemoveAll,
            HttpServletRequest request, boolean dlvPassCart) throws FDException, JsonException {
        //Only difference between "remove an item" vs. "remove all" is the parameter of "Cart Line ID" being passed in.
        //If no "Cart Line ID" is passed in, then it's executed as "remove all" request.
        String successMessage = null;
        if (isRemoveAll) {
            successMessage = "All items have been removed from cart successfully.";
        } else {
            successMessage = "Item has been removed from cart successfully.";
        }
        Message responseMessage = null;

        Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", dlvPassCart));
        
        ResultBundle resultBundle = cart.removeItemFromCart(cartLineId, qetRequestData(request), user, dlvPassCart);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            CartDetail cartDetail = cart.getCartDetail(user, null, false, dlvPassCart);
            responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
            
            if (isExtraResponseRequested(request)) {
                ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
            }
            
            responseMessage.setSuccessMessage(successMessage);
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView updateItemInCart(ModelAndView model, SessionUser user, UpdateItemInCart reqestMessage, HttpServletRequest request)
            throws FDException, JsonException {
        Message responseMessage = null;
        
        Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", reqestMessage.isDlvPassCart()));
        
        ResultBundle resultBundle = cart.updateItemInCart(reqestMessage, qetRequestData(request), user, reqestMessage.isDlvPassCart());
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        CartDetail cartDetail = cart.getCartDetail(user, null, false, reqestMessage.isDlvPassCart());
        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
        boolean checkLoginStatusEnable = isExtraResponseRequested(request);
        if (checkLoginStatusEnable) {
            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
        }

        if (result.isSuccess()) {
            responseMessage.setSuccessMessage("Cart line item has been updated successfully.");
        } else {
            if (checkLoginStatusEnable && result.hasError(SYSTEM) && SystemMessageList.MSG_IDENTIFY_CARTLINE.equals(result.getError(SYSTEM).getDescription())) {
                result.removeError(SYSTEM);
                result.addError(true, ERR_IDENTIFY_CARTLINE, SystemMessageList.MSG_IDENTIFY_CARTLINE);
                responseMessage.setStatus(Message.STATUS_FAILED);
                responseMessage.addErrorMessages(result.getErrors(), user);
            } else {
                responseMessage = getErrorMessage(result, request);
            }
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);

        return model;
    }

    private ModelAndView removePromoCode(ModelAndView model, SessionUser user, SimpleRequest reqestMessage, HttpServletRequest request, boolean dlvPassCart)
            throws FDException, JsonException {
        return removePromoCode(model, user, reqestMessage.getId(), request, dlvPassCart);
    }

    private ModelAndView removePromoCode(ModelAndView model, SessionUser user, String promoId, HttpServletRequest request, boolean dlvPassCart)
            throws FDException, JsonException {
    	
    	Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", dlvPassCart));
    	
        ResultBundle resultBundle = cart.removeRedemptionCode(promoId, user, dlvPassCart);

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

    private ModelAndView applyPromoCode(ModelAndView model, SessionUser user, SimpleRequest reqestMessage, HttpServletRequest request, boolean dlvPassCart)
            throws FDException, JsonException {
        return applyPromoCode(model, user, reqestMessage.getId(), request, dlvPassCart);
    }
    
    private ModelAndView applyCode(ModelAndView model, SessionUser user, SimpleRequest reqestMessage, HttpServletRequest request, boolean dlvPassCart)
            throws FDException, JsonException {
        return applyCode(model, user, reqestMessage.getId(), request, dlvPassCart);
    }
    
    private ModelAndView applyPromoCode(ModelAndView model, SessionUser user, String promoId, HttpServletRequest request, boolean dlvPassCart)
            throws FDException, JsonException {
    	
    	Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", dlvPassCart));
    	
        ResultBundle resultBundle = cart.applyRedemptionCode(promoId, user, dlvPassCart);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        double maxPromotion = user.getMaxSignupPromotion();
    	PromotionI redemptionPromo = user.getRedeemedPromotion();
        String promoCode = redemptionPromo != null ? redemptionPromo.getPromotionCode() : "";
        boolean isRedemptionApplied = (redemptionPromo != null && user.getPromotionEligibility().isApplied(redemptionPromo.getPromotionCode()));
        
        if ((redemptionPromo == null && maxPromotion <= 0.0) || (redemptionPromo != null && !user.getPromotionEligibility().isEligible(promoCode) && request.getAttribute("promoError") == null)) {
        	/*Boolean isEligible =(Boolean)request.getAttribute("isEligible");
    		if(isEligible != null && !isEligible){*/
    			responseMessage = getErrorMessage(result, request);
    		/*}else{
    			responseMessage = Message.createSuccessMessage("Promo code has been applied successfully.");
    		}*/
        }
        if (isRedemptionApplied || 
        		(redemptionPromo != null && !user.getPromotionEligibility().isEligible(promoCode) && request.getAttribute("promoError") != null)
        		||(redemptionPromo != null	&& user.getPromotionEligibility().isEligible(promoCode)&&(!user.getPromotionEligibility().isApplied(promoCode)))) {
        	responseMessage = Message.createSuccessMessage("Promo code has been applied successfully.");
        }
        /*if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Promo code has been applied successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }*/
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView applyCode(ModelAndView model, SessionUser user, String givexNum, HttpServletRequest request, boolean dlvPassCart)
            throws FDException, JsonException {
    	
    	Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", dlvPassCart));
    	
        ResultBundle resultBundle = cart.applycode(givexNum, user, dlvPassCart);

        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        
        Message responseMessage = null;
        
        if(result.getError("card_zero_balance")!=null || result.getError("account_locked")!=null ||  result.getError("account_locked")!=null || result.getError("card_on_hold")!=null || result.getError("card_in_use")!=null){
        	responseMessage = getErrorMessage(result, request);
        } else if(result.getErrors().size() == 0){
       	responseMessage = Message.createSuccessMessage("Giftcard has been applied successfully.");
        } else {
        	responseMessage = getErrorMessage(result, request);
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }


    private ModelAndView addMultipleItemsInCart(ModelAndView model, SessionUser user, AddMultipleItemsToCart reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Message responseMessage = null;

        Cart cart = Cart.wrap(UserUtil.getCart(user.getFDSessionUser(), "", reqestMessage.isDlvPassCart()));

        ResultBundle resultBundle = cart.addMultipleItemsToCart(reqestMessage, qetRequestData(request), user, request, reqestMessage.isDlvPassCart());
        ActionResult result = resultBundle.getActionResult();

        if (result.isSuccess()) {
            List<String> recentItems = (List<String>) resultBundle.getExtraData(Cart.RECENT_ITEMS);

            CartDetail cartDetail = cart.getCartDetail(user, null, reqestMessage.isQuickBuy(), reqestMessage.isDlvPassCart());
            responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
            ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setRecentlyAddedItems(recentItems);
            
            if (isExtraResponseRequested(request)) {
                ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
            }

            responseMessage.setSuccessMessage("Items has been added to cart successfully.");

        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private ModelAndView removeMultipleItemsInCart(ModelAndView model, SessionUser user, MultipleRequest requestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Message responseMessage = null;

        Cart cart = user.getShoppingCart();

        ResultBundle resultBundle = cart.removeMultipleItemsFromCart(requestMessage, qetRequestData(request), user, requestMessage.isDlvPassCart());
        ActionResult result = resultBundle.getActionResult();

        CartDetail cartDetail = cart.getCartDetail(user, null, false, requestMessage.isDlvPassCart());
        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
        ((com.freshdirect.mobileapi.controller.data.response.Cart) responseMessage).setCartDetail(cartDetail);
        boolean checkLoginStatusEnable = isExtraResponseRequested(request);
        if (checkLoginStatusEnable) {
            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), cartDetail);
        }

        if (result.isSuccess()) {
            responseMessage.setSuccessMessage("Items has been removed from cart successfully.");
        } else {
            if (checkLoginStatusEnable && result.hasError(SYSTEM) && SystemMessageList.MSG_IDENTIFY_CARTLINE.equals(result.getError(SYSTEM).getDescription())) {
                result.removeError(SYSTEM);
                result.addError(true, ERR_IDENTIFY_CARTLINE, SystemMessageList.MSG_IDENTIFY_CARTLINE);
                responseMessage.setStatus(Message.STATUS_FAILED);
                responseMessage.addErrorMessages(result.getErrors(), user);
            } else {
                responseMessage = getErrorMessage(result, request);
            }
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView clipCoupon(ModelAndView model, SessionUser user, String couponId, HttpServletRequest request)
            throws FDException, JsonException {
       
        Message responseMessage = null;
        boolean result = false;
       
		if(user != null && couponId != null && couponId.trim().length() > 0) {
			result = FDCustomerCouponUtil.clipCoupon(request.getSession(), couponId);
		}
        
        if (result) {
            responseMessage = Message.createSuccessMessage("coupon clipped");
        } else {
            responseMessage = Message.createFailureMessage("Unable to clip coupon.");
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }

}
