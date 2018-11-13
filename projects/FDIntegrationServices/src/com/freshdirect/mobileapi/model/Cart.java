package com.freshdirect.mobileapi.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHeaders;
import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.WebOrderViewI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.PromotionErrorType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.SalesUnit;
import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;
import com.freshdirect.mobileapi.controller.data.request.MultipleRequest;
import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.AffiliateCartDetail;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.CartLineItem.CartLineItemType;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount.DiscountType;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.Group;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.ProductLineItem;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.RedemptionPromotion;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.RedemptionPromotion.RedemptionPromotionType;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.SummaryLineCharge;
import com.freshdirect.mobileapi.controller.data.response.CreditCard;
import com.freshdirect.mobileapi.controller.data.response.DepotLocation;
import com.freshdirect.mobileapi.controller.data.response.EBTCard;
import com.freshdirect.mobileapi.controller.data.response.ElectronicCheck;
import com.freshdirect.mobileapi.controller.data.response.Ewallet;
import com.freshdirect.mobileapi.controller.data.response.ModifyCartDetail;
import com.freshdirect.mobileapi.controller.data.response.Order;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.data.Unavailability;
import com.freshdirect.mobileapi.model.tagwrapper.FDShoppingCartControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RedemptionCodeControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RequestParamName;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;
import com.freshdirect.webapp.cos.util.CosFeatureUtil;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.service.EventLoggerService;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;
import com.freshdirect.webapp.util.RequestUtil;
import com.freshdirect.webapp.util.RestrictionUtil;
import com.freshdirect.webapp.util.ShoppingCartUtil;

/**
 * Mobile API wrapper for FDCart
 * @author Rob
 *
 */
public class Cart {

    private static Category LOG = LoggerFactory.getInstance(Cart.class);

    public static final String AFFILIATE_USQ_WINES_CODE = "USQ";

    public static final String RECENT_ITEMS = "RECENT_ITEMS";

    public static Cart wrap(FDCartModel cart) {
        Cart newInstance = new Cart();
        newInstance.cart = cart;
        return newInstance;
    }

    public static Cart cloneCart(Cart cart) {
        Cart newInstance = new Cart();
        newInstance.cart = new FDCartModel(cart.getCart());
        return newInstance;
    }

    private Cart() {
    }

    public Cart(FDCartI cart) {
        this.cart = cart;
    }

    private FDCartI cart;

    public void updateWrappedCart(FDCartI cart) {
        this.cart = cart;
    }

    public boolean isSet() {
        return (cart != null);
    }

    /**
     * @return
     */
    public Date getModificationCutoffTime() {
       return ((FDCartModel) cart).getModificationCutoffTime();
    }

    //    public void addItemToCart(CartLineItem lineItem) {
    //        try {
    //            ((FDCartModel) cart).addOrderLine(lineItem.getFdCartline());
    //        } catch (OrderLineLimitExceededException limitExceeded) {
    //            //TODO: Exception handling strategy
    //            throw new RuntimeException(limitExceeded); //This need to be checked.
    //        }
    //    }

    public void setZoneInfo(FDDeliveryZoneInfo zoneInfo) {
        ((FDCartModel) cart).setZoneInfo(zoneInfo);
    }

    public void setDeliveryAddress(ShipToAddress shippingAddress) {
    	// FDX-1873 - Show timeslots for anonymous address
    	if(shippingAddress != null) {
    		((FDCartModel) cart).setDeliveryAddress(shippingAddress.getAddress());
    	} else {
    		((FDCartModel) cart).setDeliveryAddress(null);
    		((FDCartModel) cart).setDeliveryReservation(null);
    	}
    }

    public void setUnavailablePasses(List<DlvPassAvailabilityInfo> unavailablePasses) {
        ((FDCartModel) cart).setUnavailablePasses(unavailablePasses);
    }
    /**
     * Sets availability flag and returns results
     *
     * @param user
     * @return
     * @throws FDResourceException
     */
    public boolean isCartFullyAvailable(SessionUser user) throws FDResourceException {
        /*
         * DUP: FDWebSite/docroot/checkout/step_2_check.jsp
         * LAST UPDATED ON: 10/07/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code performs availability check of items in user's cart.
         */
        this.cart = FDCustomerManager.checkAvailability(user.getFDSessionUser().getIdentity(), ((FDCartModel) cart), 30000, user.isFromLogin());
        // recalculate promotions
        user.getFDSessionUser().updateUserState();
        boolean isAvailable = ((FDCartModel) cart).isFullyAvailable();
        return isAvailable;
    }

    //Friendly
    public FDCartModel getCart() {
        return ((FDCartModel) cart);
    }

    public static final String THANKSGIVING_RESTRICTION = "thxgivingRestriction";

    public static final String THANKSGIVING_MEAL_RESTRICTION = "thxgivingMealRestriction";

    public static final String EASTER_RESTRICTION = "easterRestriction";

    public static final String EASTER_MEAL_RESTRICTION = "easterMealRestriction";

    public static final String VALENTINE_RESTRICTION = "valentineRestriction";

    public static final String KOSHER_RESTRICTION = "kosherRestriction";

    public static final String ALCOHOL_RESTRICTION = "alcoholRestriction";

    /**
     * @see "/checkout/step_2_select.jsp"
     *
     * @return
     */
    public Map<String, Boolean> getCartRestriction() {
        Map<String, Boolean> cartRestrictions = new HashMap<String, Boolean>();
        for ( EnumDlvRestrictionReason reason : ((FDCartModel) cart).getApplicableRestrictions() ) {
            if (EnumDlvRestrictionReason.THANKSGIVING.equals(reason)) {
                cartRestrictions.put(THANKSGIVING_RESTRICTION, Boolean.TRUE);
                continue;
            }
            if (EnumDlvRestrictionReason.THANKSGIVING_MEALS.equals(reason)) {
                cartRestrictions.put(THANKSGIVING_MEAL_RESTRICTION, Boolean.TRUE);
                continue;
            }
            //easter
            if (EnumDlvRestrictionReason.EASTER.equals(reason)) {
                cartRestrictions.put(EASTER_RESTRICTION, Boolean.TRUE);
                continue;
            }
            //easter meals
            if (EnumDlvRestrictionReason.EASTER_MEALS.equals(reason)) {
                cartRestrictions.put(EASTER_MEAL_RESTRICTION, Boolean.TRUE);
                continue;
            }
            if (EnumDlvRestrictionReason.ALCOHOL.equals(reason)) {
                cartRestrictions.put(ALCOHOL_RESTRICTION, Boolean.TRUE);
                continue;
            }
            if (EnumDlvRestrictionReason.KOSHER.equals(reason)) {
                cartRestrictions.put(KOSHER_RESTRICTION, Boolean.TRUE);
                continue;
            }
            if (EnumDlvRestrictionReason.VALENTINES.equals(reason)) {
                cartRestrictions.put(VALENTINE_RESTRICTION, Boolean.TRUE);
                continue;
            }
        }
        return cartRestrictions;
    }

    /**
     * @param removeItemRequest
     * @param requestData
     * @param user
     * @return
     * @throws FDException
     */
    public ResultBundle removeItemFromCart(String cartLineId, RequestData requestData, SessionUser user, boolean dlvPassCart) throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);
        ResultBundle result = wrapper.removeItemFromCart(cartLineId, cartEvent, dlvPassCart);//

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);

        return result;
    }

    /**
     * @param addItemToCart
     * @param requestData
     * @param user
     * @return
     * @throws FDException
     * @throws ServiceException
     */
    public ResultBundle addItemToCart(AddItemToCart addItemToCart, RequestData requestData, SessionUser user, HttpServletRequest request, boolean dlvPassCart) throws FDException,
            ServiceException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_ADD_TO_CART_EVENT);
        cartEvent.setRequestData(requestData);
        cartEvent.setTrackingCode(addItemToCart.getTrackingCode());
        cartEvent.setTrackingCodeEx(addItemToCart.getTrackingCodeEx());
        cartEvent.setImpressionId(addItemToCart.getImpressionId());

        //Check for auto-configurable items that are added without explicit sales unit.
        ProductConfiguration productConfiguration = addItemToCart.getProductConfiguration();
        if (productConfiguration.getSalesUnit() == null) {
            //ProductServiceImpl productService = new ProductServiceImpl();
            //Product product = productService.getProduct(productConfiguration.getCategoryId(), productConfiguration.getProductId());
            Product product = Product.getProduct(productConfiguration.getProductId(), productConfiguration.getCategoryId(), null, user);
            productConfiguration.setSalesUnit(new SalesUnit(product.getAutoConfiguredSalesUnit()));
        }

        ResultBundle result = wrapper.addItemToCart(addItemToCart, cartEvent, dlvPassCart);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);

        List<FDCartLineI> recentItems = ((FDCartModel) cart).getRecentOrderLines();
        List<String> recentItemIds = new ArrayList<String>();
        for (FDCartLineI item : recentItems) {
            recentItemIds.add(Integer.toString(item.getRandomId()));
            createAndSendUnbxdAnalyticsEvent(user.getFDSessionUser(), request, item);
        }
        result.addExtraData(RECENT_ITEMS, recentItemIds);

        return result;
    }

    public ResultBundle addMultipleItemsToCart(AddMultipleItemsToCart multipleItemsToCart, RequestData requestData, SessionUser user, HttpServletRequest request, boolean dlvPassCart)
            throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);

        // Check if this will be needed
        CartEvent cartEvent = new CartEvent(CartEvent.FD_ADD_TO_CART_EVENT);
        cartEvent.setRequestData(requestData);
        cartEvent.setTrackingCode(multipleItemsToCart.getTrackingCode());
        cartEvent.setTrackingCodeEx(multipleItemsToCart.getTrackingCodeEx());
        cartEvent.setImpressionId(multipleItemsToCart.getImpressionId());
        ResultBundle result = wrapper.addMultipleItemsToCart(multipleItemsToCart, cartEvent, dlvPassCart);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);

        List<FDCartLineI> recentItems = ((FDCartModel) cart).getRecentOrderLines();
        List<String> recentItemIds = new ArrayList<String>();
        for (FDCartLineI item : recentItems) {
            recentItemIds.add(Integer.toString(item.getRandomId()));
            createAndSendUnbxdAnalyticsEvent(user.getFDSessionUser(), request, item);
        }
        result.addExtraData(RECENT_ITEMS, recentItemIds);
        return result;
    }

    /**
     * @param updateItemInCart
     * @param requestData
     * @param user
     * @return
     * @throws FDException
     */
    public ResultBundle updateItemInCart(UpdateItemInCart updateItemInCart, RequestData requestData, SessionUser user, boolean dlvPassCart) throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_MODIFY_CART_EVENT);
        cartEvent.setRequestData(requestData);
        ResultBundle result = wrapper.updateItemInCart(updateItemInCart, cartEvent, getOrderLineById(Integer.parseInt(updateItemInCart
                .getCartLineId())), dlvPassCart);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);
        return result;
    }

    public ResultBundle removeRedemptionCode(String id, SessionUser user, boolean dlvPassCart) throws FDException {
        RedemptionCodeControllerTagWrapper wrapper = new RedemptionCodeControllerTagWrapper(user);
        ResultBundle result = wrapper.removeRedemptionCode(id, dlvPassCart);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);
        return result;
    }

    public ResultBundle applyRedemptionCode(String id, SessionUser user, boolean dlvPassCart) throws FDException {
        RedemptionCodeControllerTagWrapper wrapper = new RedemptionCodeControllerTagWrapper(user);
        ResultBundle result = wrapper.applyRedemptionCode(id, dlvPassCart);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);
        return result;
    }

    public ResultBundle applycode(String id, SessionUser user, boolean dlvPassCart) throws FDException {
        RedemptionCodeControllerTagWrapper wrapper = new RedemptionCodeControllerTagWrapper(user);
        ResultBundle result = wrapper.applyCode(id, dlvPassCart);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);
        return result;
    }

    public ResultBundle removeMultipleItemsFromCart(MultipleRequest removeItemRequest, RequestData requestData, SessionUser user, boolean dlvPassCart)
            throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);

        ResultBundle result = wrapper.removeMultipleItemsFromCart(removeItemRequest.getIds(), cartEvent, dlvPassCart);
        return result;
    }

    public Map<String,FDAvailabilityInfo> getUnavailabilityMap() {
        return ((FDCartModel) cart).getUnavailabilityMap();
    }

    public FDCartLineI getOrderLineById(int intValue) {
        return ((FDCartModel) cart).getOrderLineById(intValue);
    }

    public List<FDCartLineI> getOrderLines() {
        return ((FDCartModel) cart).getOrderLines();
    }

    public Date getFirstAvailableDate() {
        return ((FDCartModel) cart).getFirstAvailableDate();
    }

    public List<DlvPassAvailabilityInfo> getUnavailablePasses() {
        return ((FDCartModel) cart).getUnavailablePasses();
    }

    public ErpAddressModel getDeliveryAddress() {
        return ((FDCartModel) cart).getDeliveryAddress();
    }

    public FDReservation getDeliveryReservation() {
        return ((FDCartModel) cart).getDeliveryReservation();
    }

    public FDReservation getOriginalReservation() {
    	if(cart instanceof FDModifyCartModel){
    		return ((FDModifyCartModel) cart).getOriginalOrder().getDeliveryReservation();
    	}
    	return null;
    }

	public FDAvailabilityI getAvailability() {
		return ((FDCartModel) cart).getAvailability() == null ? NullAvailability.AVAILABLE : ((FDCartModel) cart).getAvailability();
	}

	public void setAvailability(FDAvailabilityI availability) {
		((FDCartModel) cart).setAvailability(availability);
	}

    /**
     * @param qetRequestData
     * @param user
     */
    public void removeAllAlcohol(SessionUser user) {
        /*
         * DUP: FDWebSite/docroot/checkout/no_alcohol_address.jsp
         * LAST UPDATED ON: 10/12/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code removes all alcoholic beverages from user's cart.
         */
        ((FDCartModel) cart).removeAlcoholicLines();
    }

    /**
     * @return
     */
    public boolean containsAlcohol() {
        return ((FDCartModel) cart).containsAlcohol();
    }

    public double getDeliverySurcharge() {
        return cart.getDeliverySurcharge();
    }

    public double getDeliveryCharge() {
        return cart.getDeliveryCharge();
    }

    public double getSubTotal() {
        return cart.getSubTotal();
    }

    public double getSubTotalATPCheck() {
        return ShoppingCartUtil.getSubTotal(cart);
    }

    public double getTotal() {
        return cart.getTotal();
    }

    public Order getCurrentOrderDetails(SessionUser user, EnumCouponContext ctx, boolean dlvPassCart) throws FDException {
        /*
         * DUP: FDWebSite/docroot/checkout/includes/i_checkout_receipt.jspf
         * LAST UPDATED ON: 10/01/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code populates a DTO with order details (timeslot, cart, payment, delivery, etc.)
         *   It isn't an exact duplication but we want to achieve the same as order confirmation page
         *   and order receipt pages.
         */
        Order checkoutDetail = new Order();

        if (cart instanceof FDModifyCartModel) {
            checkoutDetail.setModificationCutoffTime(getModificationCutoffTime());
        }

        FDReservation reservation = cart.getDeliveryReservation();
        ErpAddressModel dlvAddress = cart.getDeliveryAddress();
        ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
        //        ErpCustomerInfoModel customerModel = FDCustomerFactory.getErpCustomerInfo(identity);
        //        FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(identity);

        //Set Reservation Time
        if ((null != reservation) && (null != reservation.getStartTime())) {
            checkoutDetail.setReservationDateTime(reservation.getStartTime());
            checkoutDetail.setReservationTimeRange(FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime()));
            checkoutDetail.setDeliveryZone(reservation.getZoneCode());
        }

        //Delivery Address
        //If missing, just don't display it.
        if (null != dlvAddress) {
            boolean pickupOrder = cart instanceof FDOrderI ? EnumDeliveryType.PICKUP.equals(((FDOrderI) cart).getDeliveryType())
                    : dlvAddress instanceof ErpDepotAddressModel && ((ErpDepotAddressModel) dlvAddress).isPickup();
            boolean isHomeOrder = cart instanceof FDOrderI ? EnumDeliveryType.HOME.equals(((FDOrderI) cart).getDeliveryType())
                    : dlvAddress instanceof ErpAddressModel;
            boolean isCorporateOrder = cart instanceof FDOrderI ? EnumDeliveryType.CORPORATE.equals(((FDOrderI) cart).getDeliveryType())
                    : dlvAddress instanceof ErpAddressModel;

            if (pickupOrder) {
                String locationId = ((ErpDepotAddressModel) dlvAddress).getLocationId();
                FDDeliveryDepotModel dm = FDDeliveryManager.getInstance().getDepotByLocationId(locationId);
                Depot depot = Depot.wrap(dm);
                DepotLocation location = new DepotLocation(depot.getDepotLocation(locationId));
                checkoutDetail.setDeliveryAddress(location);
            } else {
                if (isHomeOrder || isCorporateOrder) {
                    checkoutDetail.setDeliveryAddress(new com.freshdirect.mobileapi.controller.data.response.ShipToAddress(ShipToAddress
                            .wrap(dlvAddress)));
                } else {
                    throw new IllegalArgumentException("Unrecongized delivery type. dlvAddress.getId=" + dlvAddress.getId());
                }
            }
        }

        //Payment Details
        //If missing, just don't display it.
        if (null != paymentMethod) {
            if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
                checkoutDetail.setPaymentMethod(new ElectronicCheck(PaymentMethod.wrap(paymentMethod)));
            } else if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType()) || EnumPaymentMethodType.PAYPAL.equals(paymentMethod.getPaymentMethodType())) {
            	if(paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals("2")){
            		checkoutDetail.setPaymentMethod(new Ewallet(PaymentMethod.wrap(paymentMethod)));
            	}else{
            		checkoutDetail.setPaymentMethod(new CreditCard(PaymentMethod.wrap(paymentMethod)));
            	}
            } else if (EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())) {
            	checkoutDetail.setPaymentMethod(new EBTCard(PaymentMethod.wrap(paymentMethod)));
            }else {
                throw new IllegalArgumentException("Unrecongized payment type. paymentMethod.getPaymentMethodType="
                        + paymentMethod.getPaymentMethodType());
            }
        }

        //Cart detail here...
        checkoutDetail.setCartDetail(getCartDetail(user, ctx, false, dlvPassCart));
        return checkoutDetail;
    }

    public CartDetail getCartDetail(SessionUser user, EnumCouponContext ctx) throws FDException {

        return getCartDetail(user, this.cart, ctx, false, false);
    }


    public CartDetail getCartDetail(SessionUser user, EnumCouponContext ctx, boolean isQuickBuy, boolean dlvPassCart) throws FDException {

        return getCartDetail(user, this.cart, ctx, isQuickBuy, dlvPassCart);
    }
    
    /**
     * TODO: this shouldn't return an object that's of response package.
     * @param user
     * @return
     * @throws FDException
     * @throws ModelException
     */
    private CartDetail getCartDetail(SessionUser user, FDCartI cart, EnumCouponContext ctx, boolean isQuickBuy, boolean dlvPassCart) throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        wrapper.addRequestValue(SessionName.PARAM_EVALUATE_COUPONS, true);
        
        if(user.getFDSessionUser()!=null && user.getFDSessionUser().isDlvPassPending()){
            user.getFDSessionUser().updateDlvPassInfo();
        }
        
        if(dlvPassCart){
        	cart = UserUtil.getCart(user.getFDSessionUser(), "", true);
        }
        
        if(EnumCouponContext.CHECKOUT.equals(ctx)){
        	((FDShoppingCartControllerTag)wrapper.getWrapTarget()).setFilterCoupons(true);
        }
        wrapper.refreshDeliveryPass(dlvPassCart);
        /*
         * DUP: FDWebSite/docroot/shared/includes/chk_acct/i_step_4_cart_details.jspf
         * LAST UPDATED ON: 10/1/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: This is not an exact duplication but goal is the same.
         *      List items in cart separating them into "affiliate" buckets so that it can be displayed
         *      on similar to order confirm and order receipt pages
         */
        List<WebOrderViewI> views = cart.getOrderViews();

        CartDetail cartDetail = null;
        if (cart instanceof FDModifyCartModel) {
            cartDetail = new ModifyCartDetail();
            ((ModifyCartDetail) cartDetail).setOrderNumber(((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId());
            ((ModifyCartDetail) cartDetail).setReservationCutoff(((FDModifyCartModel) cart).getOriginalOrder().getDeliveryReservation()
                    .getCutoffTime());

            /*
             * DUP: FDWebSite/docroot/includes/i_modifyorder.jspf
             * LAST UPDATED ON: 10/20/2009
             * LAST UPDATED WITH SVN#: 5951
             * WHY: The following logic was duplicate because it was specified in a JSP file.
             * WHAT: The following logic determine a price lock cutoff date. Basically, this date is calculated
             *     and then messaged to user as the date at which previously locked prices will be recalculated.
             */
            Date originalOrderDate = ((FDModifyCartModel) cart).getOriginalOrder().getDatePlaced();
            Calendar cal = Calendar.getInstance();
            cal.setTime(originalOrderDate);
            cal.add(Calendar.DAY_OF_MONTH, 8);
            Date weekFromOrderDate = cal.getTime();

            ((ModifyCartDetail) cartDetail).setPriceLockCutoff(weekFromOrderDate);
            if(ctx == null) {
            	ctx = EnumCouponContext.VIEWCART;
            }
        } else {
            cartDetail = new CartDetail();
            if(ctx == null) {
        		ctx = EnumCouponContext.VIEWCART;
            }
        }

        Date platterCutoffTime = null;

        for (WebOrderViewI view : views) {
            AffiliateCartDetail affiliateCartDetail = new AffiliateCartDetail();

            //Set Affiliate specific fields here.
            affiliateCartDetail.setName(view.getAffiliate().getName().toUpperCase());
            affiliateCartDetail.setIsEstimatedPrice(view.isEstimatedPrice());
            affiliateCartDetail.setSubtotal(view.getSubtotal());
            affiliateCartDetail.setTax(view.getTax());
            affiliateCartDetail.setDepositValue(view.getDepositValue());

            cartDetail.addAffiliates(affiliateCartDetail);

            List<FDCartLineI> orderLines = view.getOrderLines();

            String lastDept = null;
            //boolean firstRecipe = true;
            //Group recipeDeptGroup = null;
            Group currentBucketGroup = null; //Could be department or recipe group

            for (FDCartLineI cartLine : orderLines) {

                ProductModel productNode = cartLine.lookupProduct();
                FDProduct fdProduct = cartLine.lookupFDProduct();

                //Do some confusing logic to convert flag structure of cart line items into nested structure.
                //CartDetail -> AffiliateDetail -> (Department Group->Product Item) or (DepartmentGroup->Recipe->Product Item)
                if (lastDept == null || !lastDept.equalsIgnoreCase(cartLine.getDepartmentDesc())) {
                    lastDept = cartLine.getDepartmentDesc();
                    // Bypass idDiaplayDepartment for USQ Affiliate
                    if (view.isDisplayDepartment() || AFFILIATE_USQ_WINES_CODE.equalsIgnoreCase(view.getAffiliate().getCode())) {

                        //This gets the first item's department, does not apply to Recipe
                        String lastDeptImgName = cartLine!=null&&cartLine.lookupProduct()!=null&&cartLine.lookupProduct().getDepartment()!=null?
                        							cartLine.lookupProduct().getDepartment().getContentName() + "_cart.gif":null;

                        if (lastDept.startsWith("Recipe: ")) {
                            //                            if (null == recipeDeptGroup) {
                            //                                recipeDeptGroup = new Group(CartLineItemType.DEPT);
                            //                                recipeDeptGroup.setName("RECIPES");
                            //                                recipeDeptGroup.setImageUrl("/media_stat/images/layout/department_headers/rec_cart.gif");
                            //                                recipeDeptGroup.setId(RecipeDepartment.getDefault().getContentName());
                            //
                            //                                //Add RecipeDepartment to affiliate
                            //                                affiliateCartDetail.addLineItems(recipeDeptGroup);
                            //                            }
                            //                            if (!lastDept.equals(currentBucketGroup.getName())) {
                            currentBucketGroup = new Group(CartLineItemType.DEPT);
                            currentBucketGroup.setName(lastDept);
                            currentBucketGroup.setId(cartLine.getRecipeSourceId());
                            affiliateCartDetail.addLineItems(currentBucketGroup);
                            //                            }
                            //                            currentBucketGroup = new Group(CartLineItemType.DEPT);
                            //                            currentBucketGroup.setId(lastDept);
                            //                            currentBucketGroup.setName(lastDept);
                            //                            currentBucketGroup.setImageUrl(lastDeptImgName);
                            //                            affiliateCartDetail.addLineItems(currentBucketGroup);

                        } else {
                        	ProductModel prodMdl =ContentFactory.getInstance().getProductByName(cartLine.getCategoryName(),
                                    cartLine.getProductName());
                            String deptId = (null !=prodMdl ? prodMdl.getDepartment().getContentName():"");
                            currentBucketGroup = new Group(CartLineItemType.DEPT);
                            currentBucketGroup.setId(deptId);
                            currentBucketGroup.setName(lastDept);
                            currentBucketGroup.setImageUrl(lastDeptImgName);
                            affiliateCartDetail.addLineItems(currentBucketGroup);
                        }
                    }
                }

                ProductLineItem productLineItem = new ProductLineItem();
                //Set flag to indicate that currently items is a new item in a modified cart.
                if ((cart instanceof FDModifyCartModel)) {
                    productLineItem.setNewItem(!(cartLine instanceof FDModifyCartLineI));
                }
            	ProductConfiguration productConfiguration = new ProductConfiguration();
                productConfiguration.setFromProductSelection(ProductSelection.wrap(cartLine));

                try {
                    Product productData = Product.wrap(productNode, user.getFDSessionUser().getUser(), null, cartLine, ctx, isQuickBuy);

                    //Automatically add "agree to terms". In order to end up in cart or prev order, user must have agreed already. Website does
                    //something similar with hidden input fields.
                    if (productData!=null&&productData.hasTerms()) {
                        productConfiguration.addPassbackParam(RequestParamName.REQ_PARAM_AGREE_TO_TERMS, "yes");
                    }

                    Sku sku = productData!=null?productData.getSkyByCode(cartLine.getSkuCode()):null;
                    if (sku == null) {
                        LOG.warn("sku=" + cartLine.getSkuCode() + "::product desc=" + cartLine.getDescription() + " was null");
                        if (cartLine.getSkuCode() != null) {
                            LOG
                                    .debug("cartLine.getSkuCode() was not null. setting skucode only at config level and not prod. letting product default.");
                            if(productData!=null){
                            	productConfiguration.populateProductWithModel(productData, cartLine.getSkuCode());
                            }else {
                                LOG.debug("productData is null");
                            }
                        } else {
                            LOG.debug("cartLine.getSkuCode() was null. should we skip this one?");
                        }

                    } else {
                        productConfiguration.populateProductWithModel(productData, com.freshdirect.mobileapi.controller.data.Sku.wrap(sku));
                    }
                } catch (ModelException e) {
                    throw new FDResourceException(e);
                }

            	if(cartLine.getDiscount() != null) {
            		Discount discount = cartLine.getDiscount();
            		PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
            		productLineItem.setDiscountMsg(promotion.getDescription());
            		productLineItem.setDiscountSavings(cartLine.getDiscountAmount());
            	}
            	//Adding external page details if present.
            	productConfiguration.setExternalAgency(cartLine.getExternalAgency()!=null?cartLine.getExternalAgency():null);
            	productConfiguration.setExternalGroup(cartLine.getExternalGroup()!=null&&!cartLine.getExternalGroup().isEmpty()?cartLine.getExternalGroup():"");
            	productConfiguration.setExternalSource(cartLine.getExternalSource()!=null&&!cartLine.getExternalSource().isEmpty()?cartLine.getExternalSource():"");
                productConfiguration.setAddedFrom(cartLine.getAddedFrom() != null ? cartLine.getAddedFrom().getName() : "");
            	//productConfiguration.setAddedFromSearch(null);
            	productConfiguration.setVariantId("");
            	productConfiguration.setSavingsId(cartLine.getSavingsId()!=null && !cartLine.getSavingsId().isEmpty()?cartLine.getSavingsId():"");

            	productLineItem.setProductConfiguration(productConfiguration);
                productLineItem.setHasKosherRestriction(cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.KOSHER));
                     
				// Changes for FDC Substitution
				if (cartLine != null && cartLine.getInvoiceLine() != null) {
					productLineItem.setSubSkuStatus(null != cartLine.getInvoiceLine().getSubSkuStatus() ? cartLine.getInvoiceLine().getSubSkuStatus() : "");
					productLineItem.setSubstitutedSkuCode(null != cartLine.getInvoiceLine().getSubstitutedSkuCode() ? cartLine.getInvoiceLine().getSubstitutedSkuCode(): "");
					productLineItem.setSubstituteProductName(null != cartLine.getInvoiceLine().getSubstituteProductName() ? cartLine.getInvoiceLine().getSubstituteProductName() : "");
					productLineItem.setSusbtituteProductDefaultPrice(null != cartLine.getInvoiceLine().getSubstituteProductDefaultPrice() ? cartLine.getInvoiceLine().getSubstituteProductDefaultPrice() : "");
					productLineItem.setSubstituteProduct(null != cartLine.getInvoiceLine().getSubstituteProductId() ? cartLine.getInvoiceLine().getSubstituteProductId() : "");	
					if(cartLine != null && cartLine.getInvoiceLine() != null && cartLine.getInvoiceLine().getSubstitutedSkuCode()!=null){
					productLineItem.setSubstituteSkuQuantity(cartLine.getInvoiceLine().getQuantity());
					productLineItem.setSubstituteProductImageURL(null != productConfiguration.getProduct() && null !=productConfiguration.getProduct().getProductData() && null !=productConfiguration.getProduct().getProductData().getProductImage()?productConfiguration.getProduct().getProductData().getProductImage() : "");
					}
				}
                
                String earliestAvailability = productNode.getSku(cartLine.getSkuCode()).getEarliestAvailabilityMessage();
                //String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
                String plantID=ProductInfoUtil.getPickingPlantId(productNode.getSku(cartLine.getSkuCode()).getProductInfo());
                boolean displayShortTermUnavailability = fdProduct.getMaterial().getBlockedDays(plantID).isEmpty();
                productLineItem.setPlatter(productNode.isPlatter());
                productLineItem.setKosherProduction(fdProduct.getKosherInfo(plantID).isKosherProduction());
                productLineItem.setModifiedLineItem((cart instanceof FDModifyCartModel) && !(cartLine instanceof FDModifyCartLineI));
                productLineItem.setHasPlatterRestriction(cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.PLATTER));
                productLineItem
                        .setEarliestAvailability((displayShortTermUnavailability && earliestAvailability != null && !(cartLine instanceof FDModifyCartLineI)) ? earliestAvailability
                                : null);
                productLineItem.setUnitPrice(cartLine.getUnitPrice());
                if (cartLine != null && cartLine.getInvoiceLine() != null) {
                productLineItem.setPrice(cartLine.getInvoiceLine().getPrice());
                } else {
                productLineItem.setPrice(cartLine.getPrice());
                }
                productLineItem.setHasDepositValue(cartLine.hasDepositValue());
                productLineItem.setHasScaledPricing(cartLine.hasScaledPricing());
                productLineItem.setEstimatedPrice(cartLine.isEstimatedPrice());
                productLineItem.setHasTax(cartLine.hasTax());
                productLineItem.setCartLineId(Integer.toString(cartLine.getRandomId()));
                
                if(cart instanceof FDOrderAdapter){ 
                	long cartlineId = Long.valueOf(cartLine.getCartlineId());
                   	productLineItem.setNewItem(user.getFDSessionUser().getRecentCartlineIdsSet(((FDOrderI) cart).getErpSalesId()).contains(cartlineId)?true:false);
                }
                
                productLineItem.setGroupScaleSavings(cartLine.getGroupScaleSavings());
                productLineItem.setDlvPassProduct(cartLine.lookupFDProduct().isDeliveryPass()?true:false);
                if(cartLine.lookupFDProduct().isDeliveryPass()){
                	if (cartLine != null && cartLine.getInvoiceLine() != null) {
                			cartDetail.setDlvPassCharge(cartLine.getInvoiceLine().getPrice());
                        } else {
                        	cartDetail.setDlvPassCharge(cartLine.getPrice());
                        }
                	cartDetail.setDlvPassId(productLineItem.getCartLineId());
                }

                //Slightly altering condition logic.  do only once and add to cart level later.
                if ((platterCutoffTime == null) && productLineItem.hasPlatterRestriction()) {
                	try{
                		TimeOfDay platterTime = RestrictionUtil.getPlatterRestrictionStartTime();
                		if (platterTime != null) {
                			platterCutoffTime = platterTime.getAsDate();
                		}
                	}catch (Exception e) {
                        LOG.error("FDPlatterException : Exception while trying to get platter cutoff. no need to throw exception. log and go forward." , e);
                    }
                }
                //Add item to bucket
                if (null != currentBucketGroup) {
                    currentBucketGroup.addLineItem(productLineItem);
                } else {
                    //If bucket is null, this affiliate group doesn't breakdown by dept (wine)
                    //Add directly to affiliate cart details.
                    affiliateCartDetail.addLineItems(productLineItem);
                }
            }
        }
        
        if(user!=null && user.getFDSessionUser()!=null) {
        	user.getFDSessionUser().updateUserState();
        }
        
        //Charge Lines
        //changes as part of APPDEV-6838
        if (cart instanceof FDOrderI && ((FDOrderI) cart).hasInvoice()) {
        	cartDetail.setEstimatedTotal(((FDOrderI) cart).getInvoicedTotal());
            cartDetail.setSubtotal((user.getUserContext().getStoreContext().getEStoreId().getContentId().equals(EnumEStoreId.FDX.getContentId()) && !dlvPassCart) ?
            						((FDOrderI) cart).getInvoicedSubTotal()-cartDetail.getDlvPassCharge():((FDOrderI) cart).getInvoicedSubTotal());
            
        } else {
        	cartDetail.setEstimatedTotal(cart.getTotal());
            cartDetail.setSubtotal((user.getUserContext().getStoreContext().getEStoreId().getContentId().equals(EnumEStoreId.FDX.getContentId()) && !dlvPassCart) ?
            						cart.getSubTotal()-cartDetail.getDlvPassCharge():cart.getSubTotal());
        }

        double tip1 = cart.getTip();
        
        cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getTaxValue(), false, false, false, "Total Tax"));
     // APPDEV-4417 : Add tip details only if Cart has Tips added
        if(cart.getTip()>0.0)
        cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getTip(), false, false, false, "Tip"));
        //cartDetail.setTax(cart.getTaxValue());

        if (cart.getDepositValue() > 0) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getDepositValue(), false, false, false, "Bottle Deposit"));
            //cartDetail.setDepositValue(cart.getDepositValue());
        }

        if (cart instanceof FDOrderI) {
            cartDetail.setIsDlvPassApplied(((FDOrderI) cart).isDlvPassApplied());
            cartDetail.setEbtPurchaseAmount(((FDOrderI) cart).getEbtPurchaseAmount());
		} else if (cart instanceof FDCartModel) {
			cartDetail.setIsDlvPassApplied(((FDCartModel) cart).isDlvPassApplied());

			if (user.getSelectedServiceType() == EnumServiceType.HOME && (((FDCartModel) cart).isDlvPassApplicableByCartLines()
					|| user.getFDSessionUser().isDlvPassActive() || user.getFDSessionUser().applyFreeTrailOptinBasedDP())){
				cartDetail.setIsDlvPassApplied(true);
			}
			
			cartDetail.setisDpFreeTrialEligible(user.isDPFreeTrialOptInEligible());
		}
        //Changes as part of standalone deliverypass purchase for web and mobile api (DP17-122)
        // Based on the this flag value - UI will switch between 2 checkout views ( regular checkout vs DP only checkout view)
        if (!(cart instanceof FDModifyCartModel) && (FDStoreProperties.isDlvPassStandAloneCheckoutEnabled() && cart.containsDlvPassOnly())){
        	cartDetail.setDeliveryPassCartOnly(true);
        }
        
        //Added for Mid-Week DeliveryPass implementation. Based on this flag - UI will determine about showing the ( OR FREE hyperlink)
        if (user.isDlvPassTimeslotNotMatched()){
        	cartDetail.setDlvPassTimeslotNotMatched(true);
        }
        
        ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
        boolean isEBTPayment = (null!=paymentMethod && EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType()));
       	if(!isEBTPayment){
	        //Delivery Charge
            if (cartDetail.isDlvPassApplied()) {

                cartDetail.addSummaryLineCharge(new SummaryLineCharge(0, false, false, false, "Delivery Charge", false, DeliveryPassUtil
                        .getDlvPassAppliedMessage(user.getFDSessionUser())));
            } else {
                double deliveyCharge = cart.getChargeAmount(EnumChargeType.DELIVERY);
                if (cart.isChargeWaived(EnumChargeType.DELIVERY)) {
                    deliveyCharge = 0;
                }
                cartDetail.addSummaryLineCharge(new SummaryLineCharge(deliveyCharge, cart.isChargeTaxable(EnumChargeType.DELIVERY),
                		cart.isChargeWaived(EnumChargeType.DELIVERY), false, "Delivery Charge"));
            }
            if((int)cart.getChargeAmount(EnumChargeType.DLVPREMIUM) > 0){
                double deliveyPremium = cart.getChargeAmount(EnumChargeType.DLVPREMIUM);
                if (cart.isChargeWaived(EnumChargeType.DLVPREMIUM)) {
                	deliveyPremium = 0;
                }
                cartDetail.addSummaryLineCharge(new SummaryLineCharge(deliveyPremium, cart.isChargeTaxable(EnumChargeType.DLVPREMIUM),
                		cart.isChargeWaived(EnumChargeType.DLVPREMIUM), false, "Delivery Premium (Hamptons)"));
            }

	        //Misc Charge
	        //        if (cart.isMiscellaneousChargeWaived()) {
	        //            //If waived, may need to show it w/ (waived labeling)
	        //            cartDetail.addSummaryLineCharge(new SummaryLineCharge(0, cart.isMiscellaneousChargeTaxable(), true, false, MobileApiProperties
	        //                    .getMiscChargeLabel()));
	        //        } else {
	        //If not waived, show only if the value is greater than zero
	        if (cart.getMiscellaneousCharge() > 0) {
	        	double deliveySurCharge = cart.getMiscellaneousCharge();
	            if (cart.isDeliverySurChargeWaived()) {
	            	deliveySurCharge = 0;
	            }

	            cartDetail.addSummaryLineCharge(new SummaryLineCharge(deliveySurCharge, cart.isMiscellaneousChargeTaxable(),
	                    false, false, MobileApiProperties.getMiscChargeLabel()));
	        }
        }else{
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(0.0, cart.isDeliveryChargeTaxable(), cart
                    .isDeliveryChargeWaived(), false, "Delivery Charge (waived)"));
        	cartDetail.addSummaryLineCharge(new SummaryLineCharge(0.0, cart.isMiscellaneousChargeTaxable(),
                    false, false, MobileApiProperties.getMiscChargeLabel()+" (waived)"));
        }
        //        }

        /*
         * DUP: FDWebSite/docroot/shared/includes/i_viewcart.jspf
         * LAST UPDATED ON: 10/05/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code determines various promotions, discounts, customer credit applied to cart
         */

        //Promotions
        PromotionI redemptionPromo = user.getRedeemedPromotion();
        String redemptionCode = redemptionPromo != null ? redemptionPromo.getRedemptionCode() : "";
        boolean isRedemptionApplied = (redemptionPromo != null && user.getPromotionEligibility().isApplied(redemptionPromo.getPromotionCode()));

        for (ErpDiscountLineModel discountLine : cart.getDiscounts()) {
            Discount discount = discountLine.getDiscount();
            if (user.isEligibleForSignupPromotion() && cart.getTotalDiscountValue() >= 0.01) {
                cartDetail.addDiscount(new com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount(discount
                        .getPromotionCode(), redemptionCode, DiscountType.SIGNUP, discount.getAmount(), true));
            } else if (isRedemptionApplied && redemptionPromo.getPromotionCode().equalsIgnoreCase(discount.getPromotionCode())) {
                cartDetail.addDiscount(new com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount(discount
                        .getPromotionCode(), redemptionCode, DiscountType.PROMO, discount.getAmount(), false, redemptionPromo.getDescription()));
            } else { //Its a automatic header discount
                PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
//                if(!cart.containsDlvPassOnly()){
                cartDetail.addDiscount(new com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount(promotion
                        .getPromotionCode(), promotion.getRedemptionCode(), DiscountType.PROMO, discount.getAmount(), true, promotion.getDescription()));
//                }
            }
        }

      //Redemption code was entered by user but it is still not applied because of missing eligibility criteria.
      // Below section is currently commented out because we dont have requirements yet on how to show it in mobile app.
     String warningMessage = null;
     if(redemptionPromo != null) {//Redemption code was entered by user but it is still not applied because of missing eligibility criteria.
      	double redemptionAmt = 0;

  		if (cart.getSubTotal() < redemptionPromo.getMinSubtotal()) {
              redemptionAmt = redemptionPromo.getHeaderDiscountTotal();
              String esid = (null !=user && null != user.getUserContext() ? user.getUserContext().getStoreContext().getEStoreId().getContentId() : null);
  			  if(esid != null && "FDX".equals(esid)){
  				  warningMessage = MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET_FDX
  														, new Object[] { new Double(redemptionPromo.getMinSubtotal()) } );
  			  }
  			  else
  			  {
  				warningMessage = MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET
							, new Object[] { new Double(redemptionPromo.getMinSubtotal()) } );
  			  }

  		} else if (redemptionPromo.isLineItemDiscount()) {
              int errorCode = user.getPromoErrorCode(redemptionPromo.getPromotionCode());
              if(errorCode == PromotionErrorType.NO_ELIGIBLE_CART_LINES.getErrorCode()) {
  			    warningMessage = SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES;
              }
  		} else if (redemptionPromo.isSampleItem()) {
  			warningMessage = SystemMessageList.MSG_REDEMPTION_PRODUCT_UNAVAILABLE;
  		} else if (null != redemptionPromo.getOfferType() && redemptionPromo.getOfferType().equals(EnumOfferType.WINDOW_STEERING)) {
              warningMessage = SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_TIMESLOT;
        }

        //if (isRedemptionApplied) { // Commented Out to show remove redemption promo feature in IPhone regardless of applied or not

            if (redemptionPromo.isSampleItem()) {
                cartDetail.addRedemptionPromotion(new RedemptionPromotion(redemptionPromo.getPromotionCode(), redemptionPromo.getRedemptionCode(),
                        RedemptionPromotionType.SAMPLE, redemptionPromo.getDescription(), false, isRedemptionApplied, warningMessage));
                //        %>
                //        <tr valign="top" class="orderSummary">
                //            <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= redemptionPromo.getPromotionCode()%>','small')"><%= redemptionPromo.getDescription()%></a></b>:</td>
                //            <td colspan="1" align="right"><b>FREE!</b></td>
                //            <td colspan="1"></td>
                //            <td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
                //        </tr>
                //        <%
            } else if (redemptionPromo.isWaiveCharge()) {
                cartDetail.addRedemptionPromotion(new RedemptionPromotion(redemptionPromo.getPromotionCode(), redemptionPromo.getRedemptionCode(),
                        RedemptionPromotionType.WAIVE_CHARGE, redemptionPromo.getDescription(), false, isRedemptionApplied, warningMessage));
                //        %>
                //            <tr valign="top" class="orderSummary">
                //                <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= redemptionPromo.getPromotionCode()%>','small')"><%= redemptionPromo.getDescription()%></a></b>:</td>
                //                <td colspan="1" align="right"><b>$0.00</b></td>
                //                <td colspan="1"></td>
                //                <td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
                //            </tr>
                //        <%
            } else if(redemptionPromo.isExtendDeliveryPass()) {
            	cartDetail.addRedemptionPromotion(new RedemptionPromotion(redemptionPromo.getPromotionCode(), redemptionPromo.getRedemptionCode(),
                        RedemptionPromotionType.EXTEND_DELIVERY_PASS, redemptionPromo.getDescription(), false, isRedemptionApplied, warningMessage, false, "Pass Extended"));
            }
            else {
            	if (!isRedemptionApplied){
            	cartDetail.addRedemptionPromotion(new RedemptionPromotion(redemptionPromo.getPromotionCode(), redemptionPromo.getRedemptionCode(),
                        RedemptionPromotionType.DOLLAR_VALUE_DISCOUNT, redemptionPromo.getDescription(), false, isRedemptionApplied, warningMessage));
            	}
            }
        }

        //Customer Credit
        if (cart.getCustomerCreditsValue() > 0) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getCustomerCreditsValue(), false, false, true, "Credit Applied"));
            //            %>
            //                <tr valign="top" class="orderSummary">
            //                        <td colspan="3" align="right">Credit Applied:</td>
            //                        <td colspan="1" align="right">-<%=CCFormatter.formatCurrency(cart.getCustomerCreditsValue())%></td>
            //                        <td colspan="3"></td>
            //                </tr>
            //        <%
        }
        //        %>

        //Sub Total
        cartDetail.setIsEstimatedPrice(cart.isEstimatedPrice());
        //        <tr valign="top" class="SSorderTotal">
        //                <td colspan="3" align="right"><b><% if (cart.isEstimatedPrice()) { %>ESTIMATED TOTAL<% } else { %>ORDER TOTAL<%}%></b>:</td>
        //                <td colspan="1" align="right"><b><%= CCFormatter.formatCurrency(cart.getTotal()) %></b></td>
        //                <td colspan="1"><% if (cart.isEstimatedPrice()) { %>*<% } %></td>
        //                <td colspan="2"></td>
        //        </tr>

        //Giftcard Balance
        if (cart instanceof FDOrderI) {
            if (cart.getTotalAppliedGCAmount() > 0) {
                cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getTotalAppliedGCAmount(), false, false, true,
                        "Gift Card Amount to Be Applied"));
            }
        } else {
            if (user.getGiftcardBalance() > 0) {
                cartDetail.addSummaryLineCharge(new SummaryLineCharge(user.getGiftcardBalance(), false, false, true, "Gift Card Balance"));
            }
        }

        //Other charages (phone handling and restocking charge)
        if (cart.isChargeWaived(EnumChargeType.PHONE)) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(0, false, true, false, "Phone Handling Charge"));
        } else if (cart.getPhoneCharge() > 0) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getPhoneCharge(), false, false, false, "Phone Handling Charge"));
        }

        double restockingFee = 0.0;
        if (cart instanceof FDOrderI) {
            if (((FDOrderI) cart).hasSettledReturn()) {
                restockingFee = ((FDOrderI) cart).getRestockingCharges();
                if (restockingFee > 0) {
                    cartDetail.addSummaryLineCharge(new SummaryLineCharge(restockingFee, false, false, false, "Restocking Fees"));
                }
            }
        }

        if (cart.isChargeWaived(EnumChargeType.PHONE)) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(0, false, true, false, "Phone Handling Charge"));
        } else if (cart.getPhoneCharge() > 0) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getPhoneCharge(), false, false, false, "Phone Handling Charge"));
        }

        //
        //        cartDetail.setGiftcardBalance(user.getGiftcardBalance());
        //TODO: If we fully support GF, we'll have to support "remove" giftcard as below

        //    <% if (user.getGiftcardBalance() > 0) { %>
        //    <tr valign="top" style="background-color: #FF9933;line-height: 20px;">
        //        <td colspan="3" align="right" style="color: white;font-size: 13px;"><b>Gift Card Balance</b>:</td>
        //        <td colspan="1" align="right" style="color: white;font-size: 13px;"><b><%= CCFormatter.formatCurrency(user.getGiftcardBalance()) %></b></td>
        //        <td colspan="1"></td>
        //        <td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeGiftCard" class="note">Remove</a></td>
        //        </tr>
        //    <%}%>
        //        <%

        // platterCutoffTime if set.
        if (null != platterCutoffTime) {
            cartDetail.setPlatterCutoffTime(platterCutoffTime);
        }
        if(cart instanceof FDCartModel){
        	cartDetail.setExpCouponDeliveryDate(((FDCartModel)cart).getExpCouponDeliveryDate());
        }

        // Unavailability Data
        Unavailability unavailability = Unavailability.collectData(user);
    	cartDetail.setUnavailability(unavailability);

    	if(cart instanceof FDCartModel){
        	cartDetail.setTotalSavedAmount(((FDCartModel)cart).getSaveAmount(true));
        }
//        if(cart instanceof FDCartModel){
//        	cartDetail.setTip(((FDCartModel)cart).getTip());
//        }
        //APPDEV-4417
    	if(cart.getTip() > 0.0) {
    		cartDetail.setTip(cart.getTip());
    	}

    	// FDX-1873 - Show timeslots for anonymous address
        if(cart.getDeliveryAddress() == null && (user.getAddress() == null || !user.getAddress().isCustomerAnonymousAddress())) {
        	if(user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null) {
        		cartDetail.setDeliveryAddressId(new Checkout(user).getPreselectedDeliveryAddressId());
        	}
        }

        if(cart.getPaymentMethod() == null) {
        	if(user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null) {
        		cartDetail.setPaymentMethodId(new Checkout(user).getPreselectedPaymethodMethodId(Cart.wrap((FDCartModel)cart)));
        	}
        }

        if(cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getPK() == null
        		&& cart.getDeliveryReservation() != null && (user.getAddress() == null || !user.getAddress().isCustomerAnonymousAddress())) {
    		cartDetail.setDeliveryAddressId(cart.getDeliveryReservation().getAddressId());
    	}

    	if(cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getPK() != null) {
    		cartDetail.setDeliveryAddressId(cart.getDeliveryAddress().getId());
    	}
    	if(cart.getPaymentMethod() != null && cart.getPaymentMethod().getPK() != null) {
    		cartDetail.setPaymentMethodId(cart.getPaymentMethod().getPK().getId());
    	}
    	if(cart.getDeliveryReservation() != null && cart.getDeliveryReservation().getTimeslot() != null) {
    		cartDetail.setReservationId(cart.getDeliveryReservation().getId());
    		cartDetail.setTimeslotId(cart.getDeliveryReservation().getTimeslotId());
    	}

		FDIdentity identity  = user.getFDSessionUser().getIdentity();
		if(identity!=null){
			ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
			FDCustomerModel fdcm = FDCustomerFactory.getFDCustomer(identity);
			FDCustomerEStoreModel esm = fdcm != null ? fdcm.getCustomerSmsPreferenceModel() : null;
			String esid = user.getFDSessionUser().getUserContext().getStoreContext().getEStoreId().getContentId();
			if(fdcm != null){
				if(cm != null && esid == null){
					//Default
					cartDetail.setMobileNumber(cm.getMobileNumber() !=null ? cm.getMobileNumber().getPhone() : null);
				} else {
					String mobileNumber = null;

					if(cart instanceof FDCartModel){
						FDCartModel tmp = (FDCartModel)cart;
						mobileNumber = tmp.getOrderMobileNumber() != null ? tmp.getOrderMobileNumber().getPhone() : null;
					} else if(cart instanceof FDOrderAdapter) {
						//setting the order level mobile number on order receipt
						FDOrderAdapter fdOrderAdapter = (FDOrderAdapter)cart;
						if(fdOrderAdapter!=null && fdOrderAdapter.getDeliveryInfo()!=null && fdOrderAdapter.getDeliveryInfo().getOrderMobileNumber()!=null &&  fdOrderAdapter.getDeliveryInfo().getOrderMobileNumber().getPhone()!=null){
							mobileNumber = fdOrderAdapter.getDeliveryInfo().getOrderMobileNumber().getPhone();
						}
					}

					if(mobileNumber == null){
						if("FDX".equals(esid)){
							if(esm.getFdxMobileNumber() != null){
								mobileNumber = esm.getFdxMobileNumber().getPhone();
							} else if(esm.getMobileNumber() != null){
								mobileNumber = esm.getMobileNumber().getPhone();
							}
						} else {
							if(cm.getMobileNumber() != null){
								mobileNumber = cm.getMobileNumber().getPhone();
							} else if (esm.getMobileNumber() != null){
								mobileNumber = esm.getMobileNumber().getPhone();
							}
						}
					}
					cartDetail.setMobileNumber(mobileNumber);
				}


			} else if( cm != null) {
				//Default
				cartDetail.setMobileNumber(cm.getMobileNumber() !=null ? cm.getMobileNumber().getPhone() : null);
			}
		}
		boolean isPurchaseDlvPassEligible = user.getFDSessionUser().isEligibleForDeliveryPass();
        cartDetail.setPurchaseDlvPassEligible(isPurchaseDlvPassEligible);
        return cartDetail;
    }

    /**
     * @return
     */
    public String getOriginalOrderErpSalesId() {
        String originalOrderErpSalesId = null;
        if (isModifyCart() && (((FDModifyCartModel) cart).getOriginalOrder() != null)) {
            originalOrderErpSalesId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
        }
        return originalOrderErpSalesId;
    }

    /**
     * @return
     */
    public boolean isModifyCart() {
        return (this.cart instanceof FDModifyCartModel);
    }

    public boolean isProductInCart(Product product) {
        List<FDCartLineI> lines = getOrderLines();
        boolean inCart = false;
        for (FDCartLineI line : lines) {
            if (product.containsSku(line.getSku().getSkuCode())) {
                inCart = true;
                break;
            }
        }
        return inCart;
    }

    public boolean isAgeVerified() {
        return ((FDCartModel) this.cart).isAgeVerified();
    }

    public boolean isDeliveryChargeWaived() {
        return cart.isDeliveryChargeWaived();
    }

    public void setEbtIneligibleOrderLines(List<FDCartLineI> ebtIneligibleOrderLines) {
    	((FDCartModel) this.cart).setEbtIneligibleOrderLines(ebtIneligibleOrderLines);
    }

    public List<FDCartLineI> getEbtIneligibleOrderLines() {
    	 return ((FDCartModel) this.cart).getEbtIneligibleOrderLines();
    }

    public String getExpCouponDeliveryDate() {
		return ((FDCartModel) this.cart).getExpCouponDeliveryDate();
	}

    public void setTip(double tip) {
    	this.cart.setTip(tip);
    }

	public boolean isValidPromoId(String id, SessionUser user) throws FDResourceException {
		String promoId = FDPromotionNewManager.getRedemptionPromotionId(id);
        if(promoId==null){
        	return false;
        }
        return true;
    }

	public void applycredit(SessionUser user, boolean dlvPassCart, Cart cart) throws FDResourceException {
		if(user.getFDSessionUser()!=null && cart!=null && user.getFDSessionUser().getIdentity()!=null){
		FDCustomerCreditUtil.applyCustomerCredit(cart.getCart(), user.getFDSessionUser().getIdentity());
		}
        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this, dlvPassCart);
	}

	public double getAvalaraTax(AvalaraContext avalaraContext){
		if(FDStoreProperties.getAvalaraTaxEnabled()){
			return this.cart.getAvalaraTaxValue(avalaraContext);
		}
		return 0.0;
	}

	public double getTaxValue(){
		return this.cart.getTaxValue();
	}

    private void createAndSendUnbxdAnalyticsEvent(FDUserI user, HttpServletRequest request, FDCartLineI cartLine) {
        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, request.getCookies(), user)) {
        		final boolean cosAction = CosFeatureUtil.isUnbxdCosAction(user, request.getCookies());
            Visitor visitor = Visitor.withUser(user);
            LocationInfo location = LocationInfo.withUrlAndReferer(RequestUtil.getFullRequestUrl(request), request.getHeader(HttpHeaders.REFERER));
            final AnalyticsEventI event = AnalyticsEventFactory.createEvent(AnalyticsEventType.ATC, visitor, location, null, null, cartLine, cosAction, null);

            EventLoggerService.getInstance().log(event);
        }
    }
}
