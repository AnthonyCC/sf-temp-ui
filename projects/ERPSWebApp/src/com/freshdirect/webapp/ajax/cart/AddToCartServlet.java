package com.freshdirect.webapp.ajax.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.coremetrics.CmContextUtility;
import com.freshdirect.fdstore.coremetrics.builder.ConversionEventTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.ConversionEventTagModel;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.AddToCartRequestData;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseData;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseDataItem;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseDataItem.Status;
import com.freshdirect.webapp.ajax.cart.data.PendingPopupData;
import com.freshdirect.webapp.ajax.cart.data.PendingPopupOrderInfo;
import com.freshdirect.webapp.ajax.quickshop.QuickShopHelper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.RequestUtil;

public class AddToCartServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 4376343201345823580L;

    private static final Logger LOG = LoggerFactory.getInstance(AddToCartServlet.class);
    private static final String SIMPLE_PENDING_ATC_ITEM_GROUP = "_simple_";
    private static final Pattern ONLY_OLD_VIEW_CART_PAGES_PATTERN = Pattern.compile("https?://[^/]+(/checkout)*/view_cart.jsp");

    /**
     * A simple tuple implementation of the following triple external agency, external group, external source
     * 
     * @author segabor
     *
     */
    private static class ExtSource {

        ExternalAgency agency;
        String externalGroup;
        String externalSource;

        public ExtSource(ExternalAgency agency, String externalGroup, String externalSource) {
            this.agency = agency;
            this.externalGroup = externalGroup;
            this.externalSource = externalSource;
        }

        public ExtSource(FDCartLineI item) {
            this(item.getExternalAgency(), item.getExternalGroup(), item.getExternalSource());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((agency == null) ? 0 : agency.hashCode());
            result = prime * result + ((externalGroup == null) ? 0 : externalGroup.hashCode());
            result = prime * result + ((externalSource == null) ? 0 : externalSource.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ExtSource other = (ExtSource) obj;
            if (agency != other.agency)
                return false;
            if (externalGroup == null) {
                if (other.externalGroup != null)
                    return false;
            } else if (!externalGroup.equals(other.externalGroup))
                return false;
            if (externalSource == null) {
                if (other.externalSource != null)
                    return false;
            } else if (!externalSource.equals(other.externalSource))
                return false;
            return true;
        }
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false; // synchronization is done on cart
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

    	boolean isOAuthRequest = isOAuthEnabled() && isOAuthTokenInHeader(request);
    	if (isOAuthRequest){
    		try {
				user = FDCustomerManager.recognize(user.getIdentity(), false);
			} catch (FDAuthenticationException e) {
				returnHttpError(500);
	            return;
			} catch (FDResourceException e) {
				returnHttpError(500);
	            return;
			}
    	}
        FDCartModel cart = user.getShoppingCart();
        if (cart == null) {
            // user doesn't have a cart, this is a bug, as login or site_access should put it there
            returnHttpError(500, "No cart found for user " + user.getUserId()); // 500 Internal Server Error
        }

        // Parse request data
        AddToCartRequestData reqData = parseRequestData(request, AddToCartRequestData.class);
        // [APPDEV-5353] Fill in required data
        reqData.setCookies(request.getCookies());
        reqData.setRequestUrl(RequestUtil.getFullRequestUrl(request));

        // Get event source
        EnumEventSource evtSrc = EnumEventSource.UNKNOWN;
        try {
            if (reqData.getEventSource() != null) {
                evtSrc = EnumEventSource.valueOf(reqData.getEventSource());
            }
        } catch (Exception ignore) {
            LOG.warn("Invalid event source, ignoring", ignore);
        }

        // clear pending failures if in finalizing pending popup
        if (EnumEventSource.FinalizingExternal.equals(evtSrc) && user instanceof FDSessionUser) {
            ((FDSessionUser) user).setPendingExternalAtcItems(null);
        }

        List<AddToCartItem> items = reqData.getItems();
        if (items == null) {
            if (EnumEventSource.FinalizingExternal.equals(evtSrc)) {
                return; // 200 OK - finalizing pending popup may send an empty atc just to delete pending items
            } else {
                returnHttpError(400, "Bad JSON - items is missing"); // 400 Bad Request
            }
        }

        // validate items coming from external call - failures must be stored so they can be finalized
        if (EnumEventSource.ExternalPage.equals(evtSrc)) {
            AddToCartResponseData responseData = new AddToCartResponseData();
            for (AddToCartItem item : items) {
                FDProduct product = null;
                try {
                    product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(item.getSkuCode()));
                } catch (FDResourceException fdre) {
                    LOG.error(fdre);
                    continue;
                } catch (FDSkuNotFoundException fdsnfe) {
                    LOG.error(fdsnfe);
                    continue;
                }

                boolean simpleAtcItem = CartOperations.validateConfiguration(product, item) == null;

                AddToCartResponseDataItem responseItem = processPendingExternAtcItem(user, item, evtSrc, simpleAtcItem);
                if (responseItem.getStatus() != AddToCartResponseDataItem.Status.SUCCESS) { // only return errors
                    responseData.getAtcResult().add(responseItem);
                }
            }
            writeResponseData(response, responseData);
            return;
        }

        try {
			if (!EnumEventSource.ExternalPage.equals(evtSrc) && !EnumEventSource.FinalizingExternal.equals(evtSrc)
					&& user.getShowPendingOrderOverlay() && user.hasPendingOrder() && reqData.getOrderId() == null
					&& !reqData.isNewOrder()) {
				// user has pending orders, show the popup first

                returnWithModifyPopup(response, user, cart, items, reqData.getEventSource());
                return;
            }

        } catch (FDResourceException e) {
            LOG.error("Error while collecting pending orders for user: " + user.getUserId(), e);
        }

        if (reqData.getOrderId() != null) {
            // Modify order mode, load order lines into cart

            cart = modifyOrder(request, user, cart, reqData);
        }

        LOG.debug("add to cart for user: " + user.getUserId() + ", items:" + items);

        // set user as having seen the overlay and used it
        user.setSuspendShowPendingOrderOverlay(true);

        try {
            // Create response data object
            AddToCartResponseData responseData = new AddToCartResponseData();
            boolean result = false;

            synchronized (cart) {
                result = CartOperations.addToCart(user, cart, items, request.getServerName(), reqData, responseData, request.getSession(), evtSrc, isOAuthRequest);
            }

            if (!result) {
                // Global failure
                returnHttpError(500, "Error while adding items to cart for user " + user.getUserId());
            }
            String referer = request.getHeader("Referer");
            if ("pdp_main".equals(reqData.getEventSource())) {
                // redirect to cart-confirm page only for PDP page main ATC button
                for (AddToCartResponseDataItem item : responseData.getAtcResult()) {
                    // get the first successful item for cart-confirm
                    if (item.getStatus() == Status.SUCCESS) {
                    	if (!reqData.isIgnoreRedirect()) {
                    		responseData.setRedirectUrl("/cart_confirm_pdp.jsp?catId=" + item.getCategoryId() + "&productId=" + item.getProductId() + "&cartlineId="
                                + item.getCartlineId());
                    	}
                        break;
                    }
                }
            } else if (EnumEventSource.FinalizingExternal.toString().equals(reqData.getEventSource())) {

                // as a bonus, append conversion event

                // collect all possible external sources of recipes
                Set<ExtSource> extSources = new HashSet<ExtSource>();
                synchronized (cart) {
                    for (FDCartLineI item : cart.getOrderLines()) {
                        if (item.getExternalAgency() != null) {
                            ExtSource esrc = new ExtSource(item);
                            extSources.add(esrc);
                        }
                    }
                }

                for (ExtSource esrc : extSources) {
                    // ConversionEventTagModel model = new ConversionEventTagModel();

                    ConversionEventTagModelBuilder builder = new ConversionEventTagModelBuilder();
                    builder.setEventId(esrc.agency.name());
                    builder.setFirstPhase(false); // --> it makes action type have set 2
                    builder.setCategoryId("Recipe");

                    // setup model
                    ConversionEventTagModel model = builder.buildTagModel();
                    model.getAttributesMaps().put(5, esrc.externalGroup);
                    model.getAttributesMaps().put(8, esrc.externalSource);

                    // add to response data
                    
    				// [APPDEV-4558]
    				if (CmContextUtility.isCoremetricsAvailable(user)) {
                    	responseData.addCoremetrics(model.toStringList());
                    }
                }

                if (!reqData.isIgnoreRedirect()) {
                    responseData.setRedirectUrl("/view_cart.jsp");
                }
            } else if (
            		(
	            		"ps_caraousal".equals(reqData.getEventSource()) || 
	            		"ps_carousel_view_cart".equals(reqData.getEventSource()) || 
	            		"view_cart".equals(reqData.getEventSource()) || 
	            		"TRY".equals(reqData.getEventSource())
            		)
                    && ONLY_OLD_VIEW_CART_PAGES_PATTERN.matcher(referer).matches()) {
                if (!reqData.isIgnoreRedirect()) {
                    responseData.setRedirectUrl(referer);
                }
            }
            writeResponseData( response, responseData );
			
    	} catch (Exception e) {
        	returnHttpError( 500, "Error while adding items to cart for user " + user.getUserId(), e );	// 500 Internal Server Error
		}
	}
            

	public static void returnWithModifyPopup(HttpServletResponse response, FDUserI user, FDCartModel cart, List<AddToCartItem> items, String eventSource) throws FDResourceException, HttpErrorResponse {
		
		// Create response data object
		AddToCartResponseData responseData = new AddToCartResponseData();
		PendingPopupData pendingData = new PendingPopupData();
		
		//create order infos
		pendingData.setOrderInfos(getPendingOrderInfos(user));
		
		//event source
		pendingData.setEventSource(eventSource);
		
		//convert pending items
		List<QuickShopLineItem> pendingItems = new ArrayList<QuickShopLineItem>();
		for(AddToCartItem item : items){
			try {
				//prepare productSelection in order to reuse QuickShopHelper's createItemCore method
	        	ProductModel pm = null;
	        	FDProductInfo prodInfo = null;
	    		if(item.getSkuCode()!=null){
	    			pm = ContentFactory.getInstance().getProduct(item.getSkuCode());
	    			prodInfo = FDCachedFactory.getProductInfo(item.getSkuCode());
	    		}else{
	    			throw new FDResourceException("Cannot create item");
	    		}
	    		FDConfiguration config = new FDConfiguration(Double.parseDouble(item.getQuantity()), item.getSalesUnit(), item.getConfiguration());
	    		
	        	FDProductSelection mockSelection = new FDProductSelection(new FDSku(prodInfo.getSkuCode(), prodInfo.getVersion()), pm, config, ContentFactory.getInstance().getCurrentUserContext());
	        	mockSelection.setCustomerListLineId(item.getLineId());
	        	
	        	QuickShopLineItemWrapper result = QuickShopHelper.createItemCore(mockSelection, null, null, user, null);
	        	
	        	if(result==null){
	        		continue;
	        	}
	        	
	        	QuickShopLineItem quickShopLineItem = result.getItem();
	        	quickShopLineItem.setItemId(item.getAtcItemId());
	        	quickShopLineItem.setExternalAgency(item.getExternalAgency());
	        	quickShopLineItem.setExternalGroup(item.getExternalGroup());
	        	quickShopLineItem.setExternalSource(item.getExternalSource());
	        	
				pendingItems.add(quickShopLineItem);
			} catch (FDSkuNotFoundException e) {
				continue; //skip item for now
			} catch (FDResourceException e){
				continue; //skip item for now
			}
		}
		pendingData.setPendingItems(pendingItems);
		
		//convert cart items
		List<QuickShopLineItem> cartItems = new ArrayList<QuickShopLineItem>();
		for(FDCartLineI cartLine : cart.getOrderLines()){
			QuickShopLineItemWrapper wrapper = QuickShopHelper.createItemCore(cartLine, null, null, user, null); //mock it with past orders enum
			if(wrapper!=null){
	        	QuickShopLineItem quickShopLineItem = wrapper.getItem();
	        	if (cartLine.getExternalAgency()!=null){
	        		quickShopLineItem.setExternalAgency(cartLine.getExternalAgency().toString());
	        	}
	        	quickShopLineItem.setExternalGroup(cartLine.getExternalGroup());
	        	quickShopLineItem.setExternalSource(cartLine.getExternalSource());
				cartItems.add(quickShopLineItem);
			}
		}
		pendingData.setCartData(cartItems);
		
		responseData.setPendingPopupData(pendingData);
		writeResponseData( response, responseData );
	}
	
    public static FDCartModel modifyOrder(HttpServletRequest request, FDUserI user, FDCartModel cart, AddToCartRequestData reqData) throws HttpErrorResponse {

        FDSessionUser sessionUser = null;
        if (user instanceof FDSessionUser) {
            sessionUser = (FDSessionUser) user;
        } else {
            // should not happen
            returnHttpError(500, "User is not a session user");
        }

        // save cart first
        sessionUser.saveCart();

        FDOrderAdapter order;
        try {
            order = (FDOrderAdapter) FDCustomerManager.getOrder(user.getIdentity(), reqData.getOrderId());
            
            ModifyOrderHelper.handleModificationCutoff(order, sessionUser, request.getSession(), new ActionResult());
    		
            FDModifyCartModel modifycart = new FDModifyCartModel(order);

            // this is added because the delivery pass is false when you modify the order though original order has delivery pass applied. This will fix any rules that use
            // dlvpassapplied flag for applying charge
            ModifyOrderHelper.handleDlvPass(modifycart, user);

            // Check if this order has a extend delivery pass promotion. If so get the no. of extended days.
            ModifyOrderHelper.handleDeliveryPassPromotion(sessionUser, null, EnumCheckoutMode.NORMAL, order, modifycart);

            // Reload gift card balance.
            ModifyOrderHelper.loadGiftCardsIntoCart(sessionUser, order);

            // resolve timeslot id based on delivery reservation id
            ModifyOrderHelper.handleReservation(order, modifycart);

            HttpSession session = request.getSession();

            // resolve the redemption promotions
            ModifyOrderHelper.handleRedemptionPromotions(sessionUser, order);

            // Refresh customer's coupon wallet.
            ModifyOrderHelper.handleCoupons(session);

            ModifyOrderHelper.updateSession(sessionUser, session);

            // update reference to the new modify cart
            cart = user.getShoppingCart();

            // set user as having seen the overlay and used it
            sessionUser.setShowPendingOrderOverlay(false);

        } catch (FDException e) {
            LOG.error("Cannot load order data into cart. Exception: " + e);
            returnHttpError(500, "Cannot load order data into cart. Exception: " + e);
        }
        return cart;
    }

    /*public static void returnWithModifyPopup(HttpServletResponse response, FDUserI user, FDCartModel cart, List<AddToCartItem> items, String eventSource)
            throws FDResourceException, HttpErrorResponse {

        // Create response data object
        AddToCartResponseData responseData = new AddToCartResponseData();
        PendingPopupData pendingData = new PendingPopupData();

        // create order infos
        pendingData.setOrderInfos(getPendingOrderInfos(user));

        // event source
        pendingData.setEventSource(eventSource);

        // convert pending items
        List<QuickShopLineItem> pendingItems = new ArrayList<QuickShopLineItem>();
        for (AddToCartItem item : items) {
            try {
                // prepare productSelection in order to reuse QuickShopHelper's createItemCore method
                ProductModel pm = null;
                FDProductInfo prodInfo = null;
                if (item.getSkuCode() != null) {
                    pm = ContentFactory.getInstance().getProduct(item.getSkuCode());
                    prodInfo = FDCachedFactory.getProductInfo(item.getSkuCode());
                } else {
                    throw new FDResourceException("Cannot create item");
                }
                FDConfiguration config = new FDConfiguration(Double.parseDouble(item.getQuantity()), item.getSalesUnit(), item.getConfiguration());

                FDProductSelection mockSelection = new FDProductSelection(new FDSku(prodInfo.getSkuCode(), prodInfo.getVersion()), pm, config, pm.getPricingContext().getZoneId());
                mockSelection.setCustomerListLineId(item.getLineId());

                QuickShopLineItemWrapper result = QuickShopHelper.createItemCore(mockSelection, null, null, user, null);

                if (result == null) {
                    continue;
                }

                QuickShopLineItem quickShopLineItem = result.getItem();
                quickShopLineItem.setItemId(item.getAtcItemId());
                quickShopLineItem.setExternalAgency(item.getExternalAgency());
                quickShopLineItem.setExternalGroup(item.getExternalGroup());
                quickShopLineItem.setExternalSource(item.getExternalSource());

                pendingItems.add(quickShopLineItem);
            } catch (FDSkuNotFoundException e) {
                continue; // skip item for now
            } catch (FDResourceException e) {
                continue; // skip item for now
            }
        }
        pendingData.setPendingItems(pendingItems);

        // convert cart items
        List<QuickShopLineItem> cartItems = new ArrayList<QuickShopLineItem>();
        for (FDCartLineI cartLine : cart.getOrderLines()) {
            QuickShopLineItemWrapper wrapper = QuickShopHelper.createItemCore(cartLine, null, null, user, null); // mock it with past orders enum
            if (wrapper != null) {
                QuickShopLineItem quickShopLineItem = wrapper.getItem();
                if (cartLine.getExternalAgency() != null) {
                    quickShopLineItem.setExternalAgency(cartLine.getExternalAgency().toString());
                }
                quickShopLineItem.setExternalGroup(cartLine.getExternalGroup());
                quickShopLineItem.setExternalSource(cartLine.getExternalSource());
                cartItems.add(quickShopLineItem);
            }
        }
        pendingData.setCartData(cartItems);

        responseData.setPendingPopupData(pendingData);
        writeResponseData(response, responseData);
    }*/

    private static List<PendingPopupOrderInfo> getPendingOrderInfos(FDUserI user) throws FDResourceException {

        List<PendingPopupOrderInfo> result = new ArrayList<PendingPopupOrderInfo>();

        for (FDOrderInfoI source : user.getPendingOrders()) {
            PendingPopupOrderInfo info = new PendingPopupOrderInfo();

            info.setErpSalesId(source.getErpSalesId());
            info.setRequestedDate(source.getRequestedDate());
            info.setDeliveryStartTime(source.getDeliveryStartTime());
            info.setDeliveryEndTime(source.getDeliveryEndTime());
            info.setDeliveryCutoffTime(source.getDeliveryCutoffTime());

            result.add(info);
        }

        return result;
    }

    private static AddToCartResponseDataItem processPendingExternAtcItem(FDUserI user, AddToCartItem item, EnumEventSource evtSrc, boolean simplePendingItem) {

        // validation
        AddToCartResponseDataItem responseItem = new AddToCartResponseDataItem();
        responseItem.setCategoryId(item.getCategoryId());
        responseItem.setProductId(item.getProductId());

        if (CartOperations.getProductModelFromAddToCartItem(item, responseItem) == null || CartOperations.getFDProductFromAddToCartItem(item, responseItem) == null) {
            return responseItem;
        }

        if (user instanceof FDSessionUser) {
            FDSessionUser sessionUser = (FDSessionUser) user;

            Map<String, List<AddToCartItem>> pendingExternalAtcItems = sessionUser.getPendingExternalAtcItems();
            if (pendingExternalAtcItems == null) {
                pendingExternalAtcItems = new HashMap<String, List<AddToCartItem>>();
                sessionUser.setPendingExternalAtcItems(pendingExternalAtcItems);
            }

            String externalGroupLabel = item.getExternalGroup();
            if (simplePendingItem) {
                externalGroupLabel = SIMPLE_PENDING_ATC_ITEM_GROUP;
            } else if (externalGroupLabel == null) {
                externalGroupLabel = "";
            } else {
                externalGroupLabel = OrderLineUtil.createExternalDescription(externalGroupLabel, item.getExternalSource());
            }

            List<AddToCartItem> pendingAtcGroup = pendingExternalAtcItems.get(externalGroupLabel);
            if (pendingAtcGroup == null) {
                pendingAtcGroup = new ArrayList<AddToCartItem>();
                pendingExternalAtcItems.put(externalGroupLabel, pendingAtcGroup);
            }

            pendingAtcGroup.add(item);
        }

        responseItem.setStatus(AddToCartResponseDataItem.Status.SUCCESS);
        return responseItem; // validation is OK
    }
    
    @Override
	protected boolean isOAuthEnabled() {
		return true;
	}
}
