package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.response.CreditCard;
import com.freshdirect.mobileapi.controller.data.response.DepotLocation;
import com.freshdirect.mobileapi.controller.data.response.ElectronicCheck;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.tagwrapper.ModifyOrderControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.QuickShopControllerTagWrapper;
import com.freshdirect.payment.EnumPaymentMethodType;

public class Order {

    private FDOrderI target;

    /**
     * @param target
     * @return
     */
    public static Order wrap(FDOrderI target) {
        Order newInstance = new Order();
        newInstance.target = target;
        return newInstance;
    }

    public FDOrderI getTarget() {
        return target;
    }

    public void setTarget(FDOrderI target) {
        this.target = target;
    }

    public static ResultBundle loadOrderToCartForUpdate(String orderId, SessionUser user) throws FDException {
        ModifyOrderControllerTagWrapper tagWrapper = new ModifyOrderControllerTagWrapper(user);
        return tagWrapper.loadOrderToCartForUpdate(orderId);
    }

    public static ResultBundle cancelModify(SessionUser user) throws FDException {
        ModifyOrderControllerTagWrapper tagWrapper = new ModifyOrderControllerTagWrapper(user);

        Cart cart = user.getShoppingCart();
        String orderId = cart.getOriginalOrderErpSalesId();
        return tagWrapper.cancelModify(orderId);
    }

    public static ResultBundle cancelOrder(String orderId, SessionUser user) throws FDException {
        ModifyOrderControllerTagWrapper tagWrapper = new ModifyOrderControllerTagWrapper(user);
        return tagWrapper.cancelOrder(orderId);
    }

    public com.freshdirect.mobileapi.controller.data.response.Order getOrderDetail(SessionUser user) throws FDException {
        /*
         * DUP: FDWebSite/docroot/your_account/order_details.jsp
         * DUP: FDWebSite/docroot/your_account/i_order_detail_delivery_payment.jspf
         * LAST UPDATED ON: 10/15/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code populates a DTO with order details (timeslot, cart, payment, delivery, etc.)
         *   It isn't an exact duplication but we want to achieve the same as order confirmation page 
         *   and order receipt pages.
         * NOTE: This is extremely similar to the code that lives in Cart class.  However, FD codes structure that we're wrapping
         * does not allow smoother reuse, therefore it's duplicated here.  Issues is related to the similarities and differences between
         * FDOrderI, FDCartI, and FDCartModel.
         */
        com.freshdirect.mobileapi.controller.data.response.Order orderDetail = new com.freshdirect.mobileapi.controller.data.response.Order();
        orderDetail.setStatus(target.getOrderStatus().getDisplayName());
        FDReservation reservation = target.getDeliveryReservation();
        ErpAddressModel dlvAddress = target.getDeliveryAddress();
        ErpPaymentMethodI paymentMethod = target.getPaymentMethod();

        //Set Reservation Time
        if ((null != reservation) && (null != reservation.getStartTime())) {
            orderDetail.setReservationDateTime(reservation.getStartTime());
            orderDetail.setReservationTimeRange(FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime()));

            //Set modification cutoff time
            orderDetail.setModificationCutoffTime(reservation.getCutoffTime());
        }

        //Delivery Address
        //If missing, just don't display it.
        if (null != dlvAddress) {
            boolean pickupOrder = target instanceof FDOrderI ? EnumDeliveryType.PICKUP.equals((target).getDeliveryType())
                    : dlvAddress instanceof ErpDepotAddressModel && ((ErpDepotAddressModel) dlvAddress).isPickup();
            boolean isHomeOrder = target instanceof FDOrderI ? EnumDeliveryType.HOME.equals((target).getDeliveryType())
                    : dlvAddress instanceof ErpAddressModel;
            boolean isCorporateOrder = target instanceof FDOrderI ? EnumDeliveryType.CORPORATE.equals((target).getDeliveryType())
                    : dlvAddress instanceof ErpAddressModel;

            if (pickupOrder) {
                String locationId = ((ErpDepotAddressModel) dlvAddress).getLocationId();
                DlvDepotModel dm = FDDepotManager.getInstance().getDepotByLocationId(locationId);
                Depot depot = Depot.wrap(dm);
                DepotLocation location = new DepotLocation(depot.getDepotLocation(locationId));
                orderDetail.setDeliveryAddress(location);

            } else {
                if (isHomeOrder || isCorporateOrder) {
                    orderDetail.setDeliveryAddress(new com.freshdirect.mobileapi.controller.data.response.ShipToAddress(ShipToAddress
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
                orderDetail.setPaymentMethod(new ElectronicCheck(PaymentMethod.wrap(paymentMethod)));
            } else if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {
                orderDetail.setPaymentMethod(new CreditCard(PaymentMethod.wrap(paymentMethod)));
            } else {
                throw new IllegalArgumentException("Unrecongized payment type. paymentMethod.getPaymentMethodType="
                        + paymentMethod.getPaymentMethodType());
            }
        }

        Cart cart = new Cart(this.target);
        orderDetail.setCartDetail(cart.getCartDetail(user));

        return orderDetail;
    }

    /**
     * 
     * @param orderId
     * @return
     * @throws FDException 
     * @throws ModelException 
     */
    public List<ProductConfiguration> getOrderProducts(String orderId, SessionUser user) throws FDException, ModelException {
        List<ProductConfiguration> result = new ArrayList<ProductConfiguration>();

        QuickShopControllerTagWrapper wrapper = new QuickShopControllerTagWrapper(user);

        ResultBundle resultBundle = wrapper.getQuickCartFromOrder(orderId);
        QuickCart quickCart = (QuickCart) resultBundle.getExtraData(QuickShopControllerTagWrapper.QUICK_CART_ID);

        Iterator<FDProductSelectionI> products = quickCart.getProducts().iterator();

        List<FDCartLineI> cartLines = user.getShoppingCart().getOrderLines();

        while (products.hasNext()) {
            FDProductSelectionI product = products.next();

            ProductConfiguration productConfiguration = new ProductConfiguration();
            try {
                Product productData = Product.wrap(product.getProductRef().lookupProduct(), user.getFDSessionUser().getUser());
                Sku sku = productData.getSkyByCode(product.getSkuCode());
                productConfiguration.populateProductWithModel(productData, com.freshdirect.mobileapi.controller.data.Sku.wrap(sku));
            } catch (ModelException e) {
                throw new FDResourceException(e);
            }
            //            try {
            //                productConfiguration.populateProductWithModel(Product.wrap(product.getProductRef().lookupProduct(), user.getFDSessionUser()
            //                        .getUser()));
            //            } catch (ModelException e) {
            //                throw new FDResourceException(e);
            //            }
            productConfiguration.setFromProductSelection(ProductSelection.wrap(product));

            Iterator<FDCartLineI> cartProducts = cartLines.iterator();
            while (cartProducts.hasNext()) {
                FDCartLineI cartProduct = cartProducts.next();
                if (OrderLineUtil.isSameConfiguration(cartProduct, product)) {
                    productConfiguration.getProduct().setInCart(true);
                }
            }

            result.add(productConfiguration);
        }

        return result;
    }
}
