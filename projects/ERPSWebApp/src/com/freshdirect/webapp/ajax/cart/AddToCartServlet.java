package com.freshdirect.webapp.ajax.cart;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.AddToCartRequestData;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseData;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseDataItem;
import com.freshdirect.webapp.ajax.cart.data.PendingPopupData;
import com.freshdirect.webapp.ajax.cart.data.PendingPopupOrderInfo;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseDataItem.Status;
import com.freshdirect.webapp.ajax.quickshop.QuickShopHelper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;


public class AddToCartServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= 4376343201345823580L;
	
	private static final Logger LOG = LoggerFactory.getInstance( AddToCartServlet.class );
	
	
	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}	

	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
        
        FDCartModel cart = user.getShoppingCart();        
        if ( cart == null ) {
            // user doesn't have a cart, this is a bug, as login or site_access should put it there
        	returnHttpError( 500, "No cart found for user " + user.getUserId() );		// 500 Internal Server Error
        }      
        
		// Parse request data
        AddToCartRequestData reqData = parseRequestData( request, AddToCartRequestData.class );
        		
		List<AddToCartItem> items = reqData.getItems(); 
		if ( items == null ) {
			returnHttpError( 400, "Bad JSON - items is missing" );	// 400 Bad Request
		}

		try {
			
			if(user.getShowPendingOrderOverlay() && user.hasPendingOrder() && reqData.getOrderId()==null && !reqData.isNewOrder()){
				//user has pending orders, show the popup first
				
				returnWithModifyPopup(response, user, cart, items);
				return;
			}
			
		} catch (FDResourceException e) {
			LOG.error("Error while collecting pending orders for user: " + user.getUserId(), e);
		}

        if(reqData.getOrderId()!=null){
        	//Modify order mode, load order lines into cart
        	       	
        	cart = modifyOrder(request, user, cart, reqData);
        }
		
		LOG.debug( "add to cart for user: " + user.getUserId() + ", items:" + items );

		//set user as having seen the overlay and used it
		user.setSuspendShowPendingOrderOverlay(true);
		
       	try {
           	// Create response data object
			AddToCartResponseData responseData = new AddToCartResponseData();			
       		boolean result = false;
       		
           	synchronized ( cart ) {
        		
				result = CartOperations.addToCart( user, cart, items, request.getServerName(), reqData, responseData, request.getSession() );
				
            }
           	
           	if ( !result ) {
           		// Global failure
           		returnHttpError( 500, "Error while adding items to cart for user " + user.getUserId() );
           	}
           	
           	if ( "pdp_main".equals( reqData.getEventSource() ) ) {
           		// redirect to cart-confirm page only for PDP page main ATC button
           		for ( AddToCartResponseDataItem item : responseData.getAtcResult() ) {
           			// get the first successful item for cart-confirm
           			if ( item.getStatus() == Status.SUCCESS ) {
           				responseData.setRedirectUrl( "/cart_confirm_pdp.jsp?catId="+item.getCategoryId()+"&productId="+item.getProductId()+"&cartlineId="+item.getCartlineId() );
           				break;
           			}
           		}
           	}
           	
			writeResponseData( response, responseData );
			
    	} catch (Exception e) {
        	returnHttpError( 500, "Error while adding items to cart for user " + user.getUserId(), e );	// 500 Internal Server Error
		}
	}

	public static FDCartModel modifyOrder(HttpServletRequest request, FDUserI user, FDCartModel cart, AddToCartRequestData reqData) throws HttpErrorResponse {
		
		FDSessionUser sessionUser = null;
		if(user instanceof FDSessionUser){
			sessionUser = (FDSessionUser)user;        		
		}else{
			//should not happen
			returnHttpError( 500, "User is not a session user" );
		}
		
		//save cart first
		sessionUser.saveCart();
		
		FDOrderAdapter order;
		try {
			order = (FDOrderAdapter) FDCustomerManager.getOrder( user.getIdentity(), reqData.getOrderId() );       		
			FDModifyCartModel modifycart = new FDModifyCartModel(order);
			
			//this is added because the delivery pass is false when you modify the order though original order has delivery pass applied. This will fix any rules that use dlvpassapplied flag for applying charge
			ModifyOrderHelper.handleDlvPass(modifycart, user);
			
			// Check if this order has a extend delivery pass promotion. If so get the no. of extended days.
			ModifyOrderHelper.handleDeliveryPassPromotion(sessionUser, null, EnumCheckoutMode.NORMAL, order, modifycart);
			
			
			//Reload gift card balance.
			ModifyOrderHelper.loadGiftCardsIntoCart(sessionUser, order);
			
			//resolve timeslot id based on delivery reservation id
			ModifyOrderHelper.handleReservation(order, modifycart);
			
			HttpSession session = request.getSession();
			
			//resolve the redemption promotions
			ModifyOrderHelper.handleRedemptionPromotions(sessionUser, order);
			
			//Refresh customer's coupon wallet.
			ModifyOrderHelper.handleCoupons(session);

			ModifyOrderHelper.updateSession(sessionUser, session);
			
			//update reference to the new modify cart
			cart = user.getShoppingCart();
			
			//set user as having seen the overlay and used it
			sessionUser.setShowPendingOrderOverlay(false);
			
		} catch (FDException e) {
			LOG.error("Cannot load order data into cart. Exception: " + e);
			returnHttpError( 500, "Cannot load order data into cart. Exception: " + e );
		}
		return cart;
	}

	public static void returnWithModifyPopup(HttpServletResponse response, FDUserI user, FDCartModel cart, List<AddToCartItem> items) throws FDResourceException, HttpErrorResponse {
		
		// Create response data object
		AddToCartResponseData responseData = new AddToCartResponseData();
		PendingPopupData pendingData = new PendingPopupData();
		
		//create order infos
		pendingData.setOrderInfos(getPendingOrderInfos(user));
		
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
	    		
	        	FDProductSelection mockSelection = new FDProductSelection(new FDSku(prodInfo.getSkuCode(), prodInfo.getVersion()), pm, config, pm.getPricingContext().getZoneId());
	        	mockSelection.setCustomerListLineId(item.getLineId());
	        	
	        	QuickShopLineItemWrapper result = QuickShopHelper.createItemCore(mockSelection, null, null, user, null);
	        	
	        	if(result==null){
	        		continue;
	        	}
	        	
	        	result.getItem().setItemId(item.getAtcItemId());
	        	
				pendingItems.add(result.getItem());
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
				cartItems.add(wrapper.getItem());
			}
		}
		pendingData.setCartData(cartItems);
		
		responseData.setPendingPopupData(pendingData);
		writeResponseData( response, responseData );
	}
	
	private static List<PendingPopupOrderInfo> getPendingOrderInfos(FDUserI user) throws FDResourceException{
		
		List<PendingPopupOrderInfo> result = new ArrayList<PendingPopupOrderInfo>();

		for(FDOrderInfoI source : user.getPendingOrders()){
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

}
