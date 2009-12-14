package com.freshdirect.mobileapi.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.RecipeDepartment;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.WebOrderViewI;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.DateFormat;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.SalesUnit;
import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;
import com.freshdirect.mobileapi.controller.data.request.MultipleRequest;
import com.freshdirect.mobileapi.controller.data.request.SimpleRequest;
import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.CreditCard;
import com.freshdirect.mobileapi.controller.data.response.DepotLocation;
import com.freshdirect.mobileapi.controller.data.response.ElectronicCheck;
import com.freshdirect.mobileapi.controller.data.response.ModifyCartDetail;
import com.freshdirect.mobileapi.controller.data.response.Order;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.AffiliateCartDetail;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.Group;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.ProductLineItem;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.SummaryLineCharge;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.CartLineItem.CartLineItemType;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount.DiscountType;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.RedemptionPromotion;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.RedemptionPromotion.RedemptionPromotionType;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.tagwrapper.FDShoppingCartControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RedemptionCodeControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RequestParamName;
import com.freshdirect.mobileapi.service.ProductServiceImpl;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.util.RestrictionUtil;

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
        Date modificationCutoffTime = null;

        if (cart.getDeliveryReservation() != null) {
            modificationCutoffTime = cart.getDeliveryReservation().getCutoffTime();
        }

        return modificationCutoffTime;
    }

    //    public void addItemToCart(CartLineItem lineItem) {
    //        try {
    //            ((FDCartModel) cart).addOrderLine(lineItem.getFdCartline());
    //        } catch (OrderLineLimitExceededException limitExceeded) {
    //            //TODO: Exception handling strategy
    //            throw new RuntimeException(limitExceeded); //This need to be checked.
    //        }
    //    }

    public void setZoneInfo(DlvZoneInfoModel zoneInfo) {
        ((FDCartModel) cart).setZoneInfo(zoneInfo);
    }

    public void setDeliveryAddress(ShipToAddress shippingAddress) {
        ((FDCartModel) cart).setDeliveryAddress(shippingAddress.getAddress());
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
        this.cart = FDCustomerManager.checkAvailability(user.getFDSessionUser().getIdentity(), ((FDCartModel) cart), 30000);
        // recalculate promotions
        user.getFDSessionUser().updateUserState();
        boolean isAvailable = ((FDCartModel) cart).isFullyAvailable();
        return isAvailable;
    }

    //Friendly
    FDCartModel getCart() {
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
        for (Iterator i = ((FDCartModel) cart).getApplicableRestrictions().iterator(); i.hasNext();) {
            EnumDlvRestrictionReason reason = (EnumDlvRestrictionReason) i.next();
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
    public ResultBundle removeItemFromCart(String cartLineId, RequestData requestData, SessionUser user) throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);
        ResultBundle result = wrapper.removeItemFromCart(cartLineId, cartEvent);//

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this);

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
    public ResultBundle addItemToCart(AddItemToCart addItemToCart, RequestData requestData, SessionUser user) throws FDException,
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
            Product product = Product.getProduct(productConfiguration.getProductId(), productConfiguration.getCategoryId(), user);
            productConfiguration.setSalesUnit(new SalesUnit(product.getAutoConfiguredSalesUnit()));
        }

        ResultBundle result = wrapper.addItemToCart(addItemToCart, cartEvent);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this);

        List<FDCartLineI> recentItems = ((FDCartModel) cart).getRecentOrderLines();
        List<String> recentItemIds = new ArrayList<String>();
        for (FDCartLineI item : recentItems) {
            recentItemIds.add(Integer.toString(item.getRandomId()));
        }
        result.addExtraData(RECENT_ITEMS, recentItemIds);
        return result;
    }

    public ResultBundle addMultipleItemsToCart(AddMultipleItemsToCart multipleItemsToCart, RequestData requestData, SessionUser user)
            throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);

        // Check if this will be needed
        CartEvent cartEvent = new CartEvent(CartEvent.FD_ADD_TO_CART_EVENT);
        cartEvent.setRequestData(requestData);
        cartEvent.setTrackingCode(multipleItemsToCart.getTrackingCode());
        cartEvent.setTrackingCodeEx(multipleItemsToCart.getTrackingCodeEx());
        cartEvent.setImpressionId(multipleItemsToCart.getImpressionId());
        ResultBundle result = wrapper.addMultipleItemsToCart(multipleItemsToCart, cartEvent);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this);

        List<FDCartLineI> recentItems = ((FDCartModel) cart).getRecentOrderLines();
        List<String> recentItemIds = new ArrayList<String>();
        for (FDCartLineI item : recentItems) {
            recentItemIds.add(Integer.toString(item.getRandomId()));
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
    public ResultBundle updateItemInCart(UpdateItemInCart updateItemInCart, RequestData requestData, SessionUser user) throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_MODIFY_CART_EVENT);
        cartEvent.setRequestData(requestData);
        ResultBundle result = wrapper.updateItemInCart(updateItemInCart, cartEvent, getOrderLineById(Integer.parseInt(updateItemInCart
                .getCartLineId())));

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this);
        return result;
    }

    public ResultBundle removeRedemptionCode(String id, SessionUser user) throws FDException {
        RedemptionCodeControllerTagWrapper wrapper = new RedemptionCodeControllerTagWrapper(user);
        ResultBundle result = wrapper.removeRedemptionCode(id);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this);
        return result;
    }

    public ResultBundle applyRedemptionCode(String id, SessionUser user) throws FDException {
        RedemptionCodeControllerTagWrapper wrapper = new RedemptionCodeControllerTagWrapper(user);
        ResultBundle result = wrapper.applyRedemptionCode(id);

        //Updating internal wrapped cart explicitly even though it should already be set since we're passing around user object reference
        user.updateShoppingCart(this);
        return result;
    }

    public ResultBundle removeMultipleItemsFromCart(MultipleRequest removeItemRequest, RequestData requestData, SessionUser user)
            throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);

        ResultBundle result = wrapper.removeMultipleItemsFromCart(removeItemRequest.getIds(), cartEvent);
        return result;
    }

    public Map getUnavailabilityMap() {
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

    public double getSubTotal() {
        return cart.getSubTotal();
    }

    public double getTotal() {
        return cart.getTotal();
    }

    public Order getOrderDetails(SessionUser user) throws FDException {
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
                DlvDepotModel dm = FDDepotManager.getInstance().getDepotByLocationId(locationId);
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
            } else if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {
                checkoutDetail.setPaymentMethod(new CreditCard(PaymentMethod.wrap(paymentMethod)));
            } else {
                throw new IllegalArgumentException("Unrecongized payment type. paymentMethod.getPaymentMethodType="
                        + paymentMethod.getPaymentMethodType());
            }
        }

        //Cart detail here...
        checkoutDetail.setCartDetail(getCartDetail(user));
        return checkoutDetail;
    }

    public CartDetail getCartDetail(SessionUser user) throws FDException {
        return getCartDetail(user, this.cart);
    }

    /**
     * TODO: this shouldn't return an object that's of response package.
     * @param user
     * @return
     * @throws FDException 
     * @throws ModelException 
     */
    private CartDetail getCartDetail(SessionUser user, FDCartI cart) throws FDException {
        FDShoppingCartControllerTagWrapper wrapper = new FDShoppingCartControllerTagWrapper(user);
        wrapper.refreshDeliveryPass();
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

        } else {
            cartDetail = new CartDetail();
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
                        String lastDeptImgName = cartLine.getProductRef().lookupCategory().getDepartment().getContentName() + "_cart.gif";

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
                            String deptId = ContentFactory.getInstance().getProductByName(cartLine.getCategoryName(),
                                    cartLine.getProductName()).getDepartment().getContentName();
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
                    Product productData = Product.wrap(productNode, user.getFDSessionUser().getUser());

                    //Automatically add "agree to terms". In order to end up in cart or prev order, user must have agreed already. Website does 
                    //something similar with hidden input fields.
                    if (productData.hasTerms()) {
                        productConfiguration.addPassbackParam(RequestParamName.REQ_PARAM_AGREE_TO_TERMS, "yes");
                    }

                    Sku sku = productData.getSkyByCode(cartLine.getSkuCode());
                    if (sku == null) {
                        LOG.warn("sku=" + cartLine.getSkuCode() + "::product desc=" + cartLine.getDescription() + " was null");
                        if (cartLine.getSkuCode() != null) {
                            LOG
                                    .debug("cartLine.getSkuCode() was not null. setting skucode only at config level and not prod. letting product default.");
                            productConfiguration.populateProductWithModel(productData, cartLine.getSkuCode());
                        } else {
                            LOG.debug("cartLine.getSkuCode() was null. should we skip this one?");
                        }

                    } else {
                        productConfiguration.populateProductWithModel(productData, com.freshdirect.mobileapi.controller.data.Sku.wrap(sku));
                    }
                } catch (ModelException e) {
                    throw new FDResourceException(e);
                }

                productLineItem.setProductConfiguration(productConfiguration);
                productLineItem.setHasKosherRestriction(cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.KOSHER));

                String earliestAvailability = productNode.getSku(cartLine.getSkuCode()).getEarliestAvailabilityMessage();
                boolean displayShortTermUnavailability = fdProduct.getMaterial().getBlockedDays().isEmpty();
                productLineItem.setPlatter(productNode.isPlatter());

                productLineItem.setKosherProduction(fdProduct.getKosherInfo().isKosherProduction());
                productLineItem.setModifiedLineItem((cart instanceof FDModifyCartModel) && !(cartLine instanceof FDModifyCartLineI));
                productLineItem.setHasPlatterRestriction(cartLine.getApplicableRestrictions().contains(EnumDlvRestrictionReason.PLATTER));
                productLineItem
                        .setEarliestAvailability((displayShortTermUnavailability && earliestAvailability != null && !(cartLine instanceof FDModifyCartLineI)) ? earliestAvailability
                                : null);
                productLineItem.setUnitPrice(cartLine.getUnitPrice());
                productLineItem.setPrice(cartLine.getPrice());
                productLineItem.setHasDepositValue(cartLine.hasDepositValue());
                productLineItem.setHasScaledPricing(cartLine.hasScaledPricing());
                productLineItem.setEstimatedPrice(cartLine.isEstimatedPrice());
                productLineItem.setHasTax(cartLine.hasTax());
                productLineItem.setCartLineId(Integer.toString(cartLine.getRandomId()));

                //Slightly altering condition logic.  do only once and add to cart level later. 
                if ((platterCutoffTime == null) && productLineItem.hasPlatterRestriction()) {
                    TimeOfDay platterTime = RestrictionUtil.getPlatterRestrictionStartTime();
                    if (platterTime != null) {
                        platterCutoffTime = platterTime.getAsDate();

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
        //Charge Lines
        cartDetail.setEstimatedTotal(cart.getTotal());
        cartDetail.setSubtotal(cart.getSubTotal());

        cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getTaxValue(), false, false, false, "Total Tax"));
        //cartDetail.setTax(cart.getTaxValue());

        if (cart.getDepositValue() > 0) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getDepositValue(), false, false, false, "Bottle Deposit & Fee"));
            //cartDetail.setDepositValue(cart.getDepositValue());
        }

        if (cart instanceof FDOrderI) {
            cartDetail.setIsDlvPassApplied(((FDOrderI) cart).isDlvPassApplied());
        } else if (cart instanceof FDCartModel) {
            cartDetail.setIsDlvPassApplied(((FDCartModel) cart).isDlvPassApplied());
        }

        //Delivery Charge 
        if (cartDetail.isDlvPassApplied()) {

            cartDetail.addSummaryLineCharge(new SummaryLineCharge(0, false, false, false, "Delivery Charge", false, DeliveryPassUtil
                    .getDlvPassAppliedMessage(user.getFDSessionUser())));
        } else {
            double deliveyCharge = cart.getDeliverySurcharge();
            if (cart.isDeliveryChargeWaived()) {
                deliveyCharge = 0;
            }
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(deliveyCharge, cart.isDeliveryChargeTaxable(), cart
                    .isDeliveryChargeWaived(), false, "Delivery Charge"));
        }

        //Misc Charge
        //        if (cart.isMiscellaneousChargeWaived()) {
        //            //If waived, may need to show it w/ (waived labeling) 
        //            cartDetail.addSummaryLineCharge(new SummaryLineCharge(0, cart.isMiscellaneousChargeTaxable(), true, false, MobileApiProperties
        //                    .getMiscChargeLabel()));
        //        } else {
        //If not waived, show only if the value is greater than zero
        if (cart.getMiscellaneousCharge() > 0) {
            cartDetail.addSummaryLineCharge(new SummaryLineCharge(cart.getMiscellaneousCharge(), cart.isMiscellaneousChargeTaxable(),
                    false, false, MobileApiProperties.getMiscChargeLabel()));
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
        //double maxPromotion = user.getMaxSignupPromotion();
        PromotionI redemptionPromo = user.getRedeemedPromotion();
        boolean isRedemptionApplied = (redemptionPromo != null && user.getPromotionEligibility().isApplied(
                redemptionPromo.getPromotionCode()));

        List discounts = cart.getDiscounts();
        for (Iterator iter = discounts.iterator(); iter.hasNext();) {
            ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
            Discount discount = discountLine.getDiscount();
            if (user.isEligibleForSignupPromotion() && cart.getTotalDiscountValue() >= 0.01) {
                cartDetail.addDiscount(new com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount(discount
                        .getPromotionCode(), DiscountType.SIGNUP, discount.getAmount(), true));
                //                        %>
                //                        <tr valign="top" class="orderSummary">
                //                                <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=signup','large')">FREE FOOD</a></b>:</td>
                //                                <td colspan="1" align="right">-<%=CCFormatter.formatCurrency(discount.getAmount())%></td>
                //                                <td colspan="3"></td>      
                //                        </tr>      
                //                        <%
            } else if (isRedemptionApplied && redemptionPromo.getPromotionCode().equalsIgnoreCase(discount.getPromotionCode())) {

                cartDetail.addDiscount(new com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount(discount
                        .getPromotionCode(), DiscountType.PROMO, discount.getAmount(), false, redemptionPromo.getDescription()));
                //                        %>
                //                        <tr valign="top" class="orderSummary">
                //                                <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= redemptionPromo.getPromotionCode()%>','small')"><%= redemptionPromo.getDescription()%></a></b>:</td>
                //                                <td colspan="1" align="right">
                //                                        <%="-" + CCFormatter.formatCurrency(discount.getAmount()) %>
                //                                </td>
                //                                <td colspan="1"></td>
                //                                <td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
                //                        </tr>      
                //        <%
            } else { //Its a automatic header discount
                PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
                cartDetail.addDiscount(new com.freshdirect.mobileapi.controller.data.response.CartDetail.Discount(promotion
                        .getPromotionCode(), DiscountType.PROMO, discount.getAmount(), true, promotion.getDescription()));
                //                        %>
                //                        <tr valign="top" class="orderSummary">
                //                                <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= promotion.getPromotionCode()%>','small')"><%= promotion.getDescription()%></a></b>:</td>
                //                                <td colspan="1" align="right">
                //                                        <%="-" + CCFormatter.formatCurrency(discount.getAmount()) %>
                //                                </td>
                //                                <td colspan="3"></td>
                //                        </tr>      
                //                        
                //    <%          
            }
        }

        if (isRedemptionApplied) {
            if (redemptionPromo.isSampleItem()) {
                cartDetail.addRedemptionPromotion(new RedemptionPromotion(redemptionPromo.getPromotionCode(),
                        RedemptionPromotionType.SAMPLE, redemptionPromo.getDescription(), false));
                //        %>
                //        <tr valign="top" class="orderSummary">
                //            <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= redemptionPromo.getPromotionCode()%>','small')"><%= redemptionPromo.getDescription()%></a></b>:</td>
                //            <td colspan="1" align="right"><b>FREE!</b></td>
                //            <td colspan="1"></td>
                //            <td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
                //        </tr>
                //        <%
            } else if (redemptionPromo.isWaiveCharge()) {
                cartDetail.addRedemptionPromotion(new RedemptionPromotion(redemptionPromo.getPromotionCode(),
                        RedemptionPromotionType.WAIVE_CHARGE, redemptionPromo.getDescription(), false));
                //        %>
                //            <tr valign="top" class="orderSummary">
                //                <td colspan="3" align="right"><b><a href="javascript:popup('/shared/promotion_popup.jsp?promoCode=<%= redemptionPromo.getPromotionCode()%>','small')"><%= redemptionPromo.getDescription()%></a></b>:</td>
                //                <td colspan="1" align="right"><b>$0.00</b></td>
                //                <td colspan="1"></td>
                //                <td colspan="2">&nbsp;<a href="<%= request.getRequestURI() %>?action=removeCode" class="note">Remove</a></td>
                //            </tr>
                //        <%
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
            cartDetail.setPlatterCutoffTime(new SimpleDateFormat(DateFormat.STANDARDIZED_DATE_FORMAT).format(platterCutoffTime));
        }
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
}
