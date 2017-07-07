package com.freshdirect.webapp.ajax.analytics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.webapp.ajax.analytics.data.GACheckoutData;
import com.freshdirect.webapp.ajax.analytics.data.GAProductData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.Item;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.Section;
import com.freshdirect.webapp.ajax.expresscheckout.sempixels.service.SemPixelService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GACheckoutDataService {

    private static final GACheckoutDataService INSTANCE = new GACheckoutDataService();

    private GACheckoutDataService() {

    }

    public static GACheckoutDataService defaultService() {
        return INSTANCE;
    }

    public GACheckoutData populateCheckoutSuccessData(GACheckoutData data, CartData cartData, FDOrderI order, HttpSession session) {

        data.setOrderId(order.getErpSalesId());
        data.setPaymentType(order.getPaymentMethod().getPaymentMethodType().getDescription());
        data.setRevenue(Double.toString(order.getTotal()));
        data.setTax(Double.toString(order.getTaxValue()));
        data.setShippingCost(cartData.getDeliveryCharge());
        data.setCouponCode(populateCouponCodes(cartData));
        data.setRedemptionCode(SemPixelService.defaultService().populateRedeemedPromotionCodes(order));
        data.setTipping(Double.toString(order.getTip()));
        data.setNewOrder(getNewOrderStatus(session));
        data.setModifyOrder(Boolean.toString(order.isModifiedOrder()));
        data.setDiscountAmount(Double.toString(order.getTotalDiscountValue()));
        data.setDeliveryType(order.getDeliveryAddress().getServiceType().name());
        data.setSelectedTimeslotValue(order.getDeliveryReservation().getTimeslot());
        data.setUnavailableTimeslotValue(null);

        return data;
    }

    public GACheckoutData populateCheckoutProductData(FDCartI cart) {
        GACheckoutData data = new GACheckoutData();

        data.setProducts(populateProductList(cart.getOrderLines()));

        return data;
    }

    public Map<String, GAProductData> populateProductList(List<FDCartLineI> cartLines) {

        Map<String, GAProductData> products = new HashMap<String, GAProductData>();

        for (FDCartLineI cartLine : cartLines) {
            GAProductData product = products.get(cartLine.getSkuCode());
            if (product == null) {
                products.put(cartLine.getSkuCode(), GAProductDataService.defaultService().populateProductData(cartLine, Double.toString(cartLine.getQuantity())));

            } else {
                product.setQuantity(product.getQuantity() + cartLine.getQuantity());
            }
        }

        return products;
    }

    private List<String> populateCouponCodes(CartData cartData) {
        List<String> couponIds = new ArrayList<String>();
        List<Section> cartSections = cartData.getCartSections();
        for (Section section : cartSections) {
            List<Item> cartLines = section.getCartLines();
            for (Item cartLine : cartLines) {
                FDCustomerCoupon coupon = cartLine.getCoupon();
                if (coupon != null) {
                    couponIds.add(coupon.getCouponId());
                }
            }
        }
        return couponIds;
    }

    public String getNewOrderStatus(HttpSession session) {
        boolean isNewOrder = false;
        Boolean orderSubmittedFlagForSemPixel = (Boolean) session.getAttribute(SessionName.ORDER_SUBMITTED_FLAG_FOR_SEM_PIXEL);
        if (orderSubmittedFlagForSemPixel != null && orderSubmittedFlagForSemPixel) {
            isNewOrder = true;
            session.removeAttribute(SessionName.ORDER_SUBMITTED_FLAG_FOR_SEM_PIXEL);
        }
        return Boolean.toString(isNewOrder);
    }

}
