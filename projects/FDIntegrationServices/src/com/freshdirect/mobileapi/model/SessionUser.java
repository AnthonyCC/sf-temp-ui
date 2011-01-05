package com.freshdirect.mobileapi.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.util.FDTimeslotUtil;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.FDCustomerCreatedListTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.GetCutoffInfoTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.GetOrderTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.HealthWarningControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.OrderHistoryInfoTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.QuickShopControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ReserveTimeslotControllerTagWrapper;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.fdstore.CutoffInfo;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.Result;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.ConfigurationContext;

public class SessionUser {


    private FDSessionUser sessionUser;

    private SessionUser() {
    }

    public String getApplication() {
        String app = sessionUser.getApplication().getCode();
        return app;
    }

    public static SessionUser wrap(Object sessionUser) {
        return wrap((FDSessionUser) sessionUser);
    }

    public static SessionUser wrap(FDSessionUser sessionUser) {
        SessionUser newInstance = new SessionUser();
        newInstance.sessionUser = sessionUser;

        return newInstance;
    }

    public List<CustomerCreatedList> getCustomerCreatedList() throws FDException {
        FDCustomerCreatedListTagWrapper tagWrapper = new FDCustomerCreatedListTagWrapper(this);
        return tagWrapper.getCustomerCreatedList();
    }

    public List getPaymentMethods() {
        Collection paymentMethods = sessionUser.getPaymentMethods();
        return new ArrayList(paymentMethods);
    }

    public List<PaymentMethod> getCreditCards(List paymentMethods) {
        List<PaymentMethod> electronicChecks = new ArrayList<PaymentMethod>();
        for (ErpPaymentMethodI paymentMethod : (List<ErpPaymentMethodI>) paymentMethods) {
            if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {
                electronicChecks.add(PaymentMethod.wrap(paymentMethod));
            }
        }
        return electronicChecks;
    }

    public List<PaymentMethod> getElectronicChecks(List paymentMethods) {
        List<PaymentMethod> electronicChecks = new ArrayList<PaymentMethod>();
        for (ErpPaymentMethodI paymentMethod : (List<ErpPaymentMethodI>) paymentMethods) {
            if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
                electronicChecks.add(PaymentMethod.wrap(paymentMethod));
            }
        }
        return electronicChecks;
    }

    public Order getOrder(String saleId) throws FDException {
        GetOrderTagWrapper wrapper = new GetOrderTagWrapper(this);
        return wrapper.getOrder(saleId);
    }

    public boolean isCheckEligible() {
        return this.sessionUser.isCheckEligible();
    }

    public boolean isEcheckRestricted() throws FDResourceException {
        return this.sessionUser.isECheckRestricted();
    }

    /**
     * @return
     * @throws FDResourceException
     */
    public List<ShipToAddress> getDeliveryAddresses() throws FDException {
        return ShipToAddress.wrap(FDCustomerFactory.getErpCustomer(sessionUser.getIdentity()).getShipToAddresses());
    }

    /**
     * @param addressId
     * @return
     * @throws FDResourceException
     */
    public ShipToAddress getDeliveryAddress(String addressId) throws FDException {
        ShipToAddress matchedAddress = null;
        List<ShipToAddress> addresses = getDeliveryAddresses();
        for (ShipToAddress address : addresses) {
            if (address.getId().equals(addressId)) {
                matchedAddress = address;
                break;
            }
        }
        return matchedAddress;
    }

    public Cart getShoppingCart() {
        return Cart.wrap(sessionUser.getShoppingCart());
    }

    /**
     * @return
     */
    public int getItemsInCartCount() {
        int itemsInCart = 0;

        if ((getShoppingCart() != null) && (getShoppingCart().getOrderLines() != null)) {
            itemsInCart = getShoppingCart().getOrderLines().size();
        }
        return itemsInCart;
    }

    public OrderHistory getOrderHistory() throws FDResourceException {
        return OrderHistory.wrap(sessionUser.getOrderHistory());
    }

    public List<OrderInfo> getCompleteOrderHistory() throws FDException {
        OrderHistoryInfoTagWrapper wrapper = new OrderHistoryInfoTagWrapper(this);

        List<FDOrderInfoI> history = (List<FDOrderInfoI>) wrapper.getOrderHistoryInfo();

        List<FDOrderInfoI> filteredHistory = new ArrayList<FDOrderInfoI>();
        
        //Filtering out Giftcart and Donation
        for(FDOrderInfoI historyItem : history) {
            if(!EnumSaleType.GIFTCARD.equals(historyItem.getSaleType()) && !EnumSaleType.DONATION.equals(historyItem.getSaleType())) {
                filteredHistory.add(historyItem);
            }
        }
        
        return OrderInfo.wrap(filteredHistory);
    }

    /**
     * DUP: duplicated code from 
     * Class - com.freshdirect.webapp.taglib.fdstore.UserUtil
     * Method - getCustomerServiceContact
     * Comment - refactored to remove dependencies on HTTPServletRequest
     * 
     * @param request
     * @return
     */
    public String getCustomerServiceContact() {
        if (sessionUser == null) {
            return SystemMessageList.CUSTOMER_SERVICE_CONTACT;
        }
        return sessionUser.getCustomerServiceContact();

    }

    public FDSessionUser getFDSessionUser() {
        return sessionUser;
    }

    public FDReservation getReservation() {
        return sessionUser.getReservation();
    }

    public PromotionI getRedeemedPromotion() {
        return sessionUser.getRedeemedPromotion();
    }

    public FDPromotionEligibility getPromotionEligibility() {
        return sessionUser.getPromotionEligibility();
    }

    public boolean isEligibleForSignupPromotion() {
        return sessionUser.isEligibleForSignupPromotion();
    }

    public double getGiftcardBalance() {
        return sessionUser.getGiftcardBalance();
    }

    public boolean isChefsTable() {
        boolean chefsTable = false;
        try {
            chefsTable = sessionUser.isChefsTable();
        } catch (FDResourceException e) {

        }
        return chefsTable;
    }

    public Timeslot getReservationTimeslot() {
        Timeslot timeslot = null;
        if (sessionUser.getReservation() != null) {
            timeslot = Timeslot.wrap(sessionUser.getReservation().getTimeslot(), isChefsTable());
        }
        return timeslot;

    }

    public void touch() {
        sessionUser.touch();
    }

    public void updateShoppingCart(Cart cart) {
        cart.updateWrappedCart(this.sessionUser.getShoppingCart());
    }

    /**
     * TODO: Refactor with Order.getOrderProducts method. Pretty much identical except few differences.
     * @param customerCreatedListId
     * @return
     * @throws FDException
     * @throws ModelException
     */
    public List<ProductConfiguration> getCustomerCreatedListProducts(String customerCreatedListId) throws FDException, ModelException {
        List<ProductConfiguration> result = new ArrayList<ProductConfiguration>();

        QuickShopControllerTagWrapper wrapper = new QuickShopControllerTagWrapper(this);

        ResultBundle resultBundle = wrapper.getQuickCartFromCustomerCreatedList(customerCreatedListId);
        QuickCart quickCart = (QuickCart) resultBundle.getExtraData(QuickShopControllerTagWrapper.QUICK_CART_ID);

        List<FDProductSelectionI> products = quickCart.getProducts();
        List<FDCartLineI> cartLines = getShoppingCart().getOrderLines();

        for (FDProductSelectionI product : products) {

            ProductConfiguration productConfiguration = new ProductConfiguration();
            try {
                Product productData = Product.wrap(product.getProductRef().lookupProductModel(), this.sessionUser.getUser());
                Sku sku = productData.getSkyByCode(product.getSkuCode());
                if(sku != null) {
                    productConfiguration.populateProductWithModel(productData, com.freshdirect.mobileapi.controller.data.Sku.wrap(sku));
                } else {
                    productConfiguration.populateProductWithModel(productData, product.getSkuCode());
                }
            } catch (ModelException e) {
                throw new FDResourceException(e);
            }
            //            
            //            
            //            ProductConfiguration productConfiguration = new ProductConfiguration();
            //            try {
            //                Product productData = Product.wrap(product.getProductRef().lookupProduct(), this.sessionUser.getUser());
            //                Sku sku = productData.getSkyByCode(product.getSkuCode());
            //                productConfiguration.populateProductWithModel(productData, com.freshdirect.mobileapi.controller.data.Sku.wrap(sku));
            //            } catch (ModelException e) {
            //                throw new FDResourceException(e);
            //            }
            productConfiguration.setFromProductSelection(ProductSelection.wrap(product));

            for (FDCartLineI cartLine : cartLines) {
                if (OrderLineUtil.isSameConfiguration(cartLine, product)) {
                    productConfiguration.getProduct().setInCart(true);
                }
            }
            result.add(productConfiguration);
        }
        return result;
    }

    public void setFDUserOnConfigurationContext(ConfigurationContext confContext) {
        confContext.setFDUser(sessionUser);
    }

    //Package level
    FDCartLineModel getSelectedCartLineFromYmalUtil() {
        return YmalUtil.getSelectedCartLine(sessionUser);
    }

    public String getFirstName() {
        String firstName = "";
        try {
            firstName = sessionUser.getFirstName();
        } catch (FDResourceException e) {
            //Weird. Not critical. Going forward
        }
        return firstName;
    }

    public double getMinimumOrderAmount() {
        return sessionUser.getMinimumOrderAmount();
    }

    /**
     * Returns existing reservation address ID or null, if there are no reservation
     * @return
     */
    public String getReservationAddressId() {
        FDReservation reservation = getReservation();
        return (reservation != null ? reservation.getAddressId() : null);
    }

    /**
     * @param timeslotLists
     * @return
     * @throws FDResourceException
     */
    public void setReservationAndPreselectedTimeslotIds(List<FDTimeslotUtil> timeslotLists,
            TimeSlotCalculationResult timeSlotCalculationResult) throws FDResourceException {
        FDReservation reservation = getReservation();

        /*
        * DUP: FDWebSite/docroot/your_account/reserve_timeslot.jsp
        * LAST UPDATED ON: 11/13/2009
        * LAST UPDATED WITH SVN#: 5951
        * WHY: The following logic was duplicate because it was specified in a JSP file.
        * WHAT: Looping logic to determine timeslot ID of reoccuring reservation
        */
        //        if (rsv == null && hasWeeklyReservation) { //Removed "hasWeeklyReservation" condition because we don't care about it here.
        if (reservation != null) {
            timeSlotCalculationResult.setReservationTimeslotId(reservation.getTimeslotId());
        } else {
            //Specific reservation doesn't exist. try to match by day of week and time range.
            String foundId = "";
            ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(sessionUser.getIdentity());
            boolean hasWeeklyReservation = customerInfo.getRsvAddressId() != null && !"".equals(customerInfo.getRsvAddressId());
            if (hasWeeklyReservation) {
                int maxloop = 8;
                int index = 0;
                int dayOfWeek = customerInfo.getRsvDayOfWeek();
                Calendar cal = Calendar.getInstance();
                if (cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                    cal.add(Calendar.DATE, 1);
                }
                while ((cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) && (index < maxloop)) {
                    cal.add(Calendar.DATE, 1);
                    index++;
                }
                OUTTER: for (FDTimeslotUtil timeslots : timeslotLists) {
                    for (Iterator i = timeslots.getDays().iterator(); i.hasNext();) {
                        Date day = (Date) i.next();
                        for (Iterator j = timeslots.getTimeslotsForDate(day).iterator(); j.hasNext();) {
                            FDTimeslot slot = (FDTimeslot) j.next();
                            if (slot.isMatching(cal.getTime(), customerInfo.getRsvStartTime(), customerInfo.getRsvEndTime())) {
                                foundId = slot.getTimeslotId();
                                break OUTTER;
                            }
                        }
                    }
                }
            }
            if (!"".equals(foundId)) {
                timeSlotCalculationResult.setReservationTimeslotId(foundId);
            }
        }

        //Set timeslot for checkout         
        FDReservation deliveryReservation = this.sessionUser.getShoppingCart().getDeliveryReservation();
        if (deliveryReservation != null) {
            timeSlotCalculationResult.setPreselectedTimeslotId(deliveryReservation.getTimeslotId());
        }
    }

    public String getDefaultShipToAddress() throws FDResourceException {
        String addressId = FDCustomerManager.getDefaultShipToAddressPK(sessionUser.getIdentity());
        return addressId;
    }

    public ResultBundle cancelReservation(String addressId, String timeslotId, String reservationType) throws FDException {
        ReserveTimeslotControllerTagWrapper wrapper = new ReserveTimeslotControllerTagWrapper(this);
        ResultBundle result = wrapper.cancelReservation(addressId, timeslotId, reservationType);
        return result;
    }

    public ResultBundle makeReservation(String addressId, String timeslotId, String reservationType) throws FDException {
        ReserveTimeslotControllerTagWrapper wrapper = new ReserveTimeslotControllerTagWrapper(this);
        ResultBundle result = wrapper.makeReservation(addressId, timeslotId, reservationType);
        return result;
    }

    public boolean isEligibleForPreReservation() throws FDResourceException {
        return sessionUser.isEligibleForPreReservation();
    }

    /**
     * @return
     * @throws FDException
     */
    public String getCutoffInfo() throws FDException {
        GetCutoffInfoTagWrapper wrapper = new GetCutoffInfoTagWrapper(this);
        CutoffInfo cutoffInfo = wrapper.getCutoff();
        String messageString = null;
        /*
         * DUP: FDWebSite/docroot/common/template/includes/i_cutoff_warning.jsp
         * LAST UPDATED ON: 11/17/2009
         * LAST UPDATED WITH SVN#: 6976
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code formats cutoff message
         */
        if (cutoffInfo != null) {

            SimpleDateFormat dayDateFormat = new SimpleDateFormat("EEE. MM/dd");

            EnumServiceType service = sessionUser.getSelectedServiceType();
            boolean isHomeAddr = EnumServiceType.HOME.equals(service);
            boolean isPickupAddr = EnumServiceType.PICKUP.equals(service);
            boolean isCorporateAddr = EnumServiceType.CORPORATE.equals(service);
            boolean isUnknown = !isHomeAddr && !isPickupAddr && !isCorporateAddr;

            StringBuilder message = new StringBuilder();

            if (cutoffInfo.displayWarning()) {
                if (cutoffInfo.hasMultipleCutoff() && !cutoffInfo.getNextCutoff().equals(cutoffInfo.getLastCutoff())) {

                    message.append("The cut off time for evening ");
                    if (isHomeAddr) {
                        message.append("delivery in your area ");
                    } else if (isPickupAddr || isUnknown) {
                        message.append("pickup ");
                    } else if (isCorporateAddr) {
                        message.append("office delivery ");
                    }
                    message.append("tomorrow is ").append(cutoffInfo.getLastCutoff()).append(".");
                    message.append("Please note: orders for earlier ").append((isPickupAddr || isUnknown) ? "pickup" : "deliveries");
                    message.append("(").append(cutoffInfo.getNextEarliestTimeslot()).append("-").append(" - ").append(
                            cutoffInfo.getNextLatestTimeslot()).append(") , must be placed before ").append(cutoffInfo.getNextCutoff())
                            .append(".");
                } else {
                    message.append(" Please note: The cut off time for ");
                    if (isHomeAddr && !isUnknown) {
                        message.append("delivery in your area ");
                    } else if (isPickupAddr || isUnknown) {
                        message.append("pickup ");
                    } else if (isCorporateAddr) {
                        message.append("office delivery ");
                    }
                    message.append("tomorrow is ").append(cutoffInfo.getNextCutoff()).append(".");
                    message.append(isPickupAddr || isUnknown ? "For orders" : "To receive delivery ").append(
                            "tomorrow, please complete checkout within the next ").append(cutoffInfo.getMinsBeforeCutoff()).append(
                            "minutes. ");
                }
            } else if (cutoffInfo.isHourPastCutoff()) {
                message.append("Please note: The cut off time for ").append(isPickupAddr || isUnknown ? "pickup" : "delivery").append(
                        dayDateFormat.format(cutoffInfo.getDeliveryDate())).append(" was ").append(cutoffInfo.getCutoffTime().toString());
            }
            messageString = message.toString();
        }

        return messageString;
    }

    public String getUsername() {
        return sessionUser.getUserId();
    }

    /**
     * @return
     * @throws FDException
     */
    public ResultBundle acknowledgeHealthWarning() throws FDException {
        HealthWarningControllerTagWrapper wrapper = new HealthWarningControllerTagWrapper(this);
        return wrapper.acknowledgeHealthWarning();
    }

    public boolean isHealthWarningAcknowledged() {
        return sessionUser.isHealthWarningAcknowledged();
    }

    public double getMinCorpOrderAmount() {
        return sessionUser.getMinCorpOrderAmount();
    }

    public void setUserPricingContext() {
        ContentFactory.getInstance().setCurrentPricingContext(sessionUser.getPricingContext());
    }
    
    public double getMaxSignupPromotion() {
        return sessionUser.getMaxSignupPromotion();
    }

    
    
}
