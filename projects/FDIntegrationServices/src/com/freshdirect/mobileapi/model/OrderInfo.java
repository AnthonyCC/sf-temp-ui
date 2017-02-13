package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.webapp.taglib.fdstore.OrderUtil;

public class OrderInfo {

    private FDOrderInfoI target;

    public static OrderInfo wrap(FDOrderInfoI info) {
        OrderInfo newInstance = new OrderInfo();
        newInstance.target = info;
        return newInstance;
    }

    public static List<OrderInfo> wrap(List<FDOrderInfoI> infos) {
        List<OrderInfo> wrappedInfos = new ArrayList<OrderInfo>();
        for (FDOrderInfoI info : infos) {
            wrappedInfos.add(wrap(info));
        }
        return wrappedInfos;
    }

    public String getId() {
        return target.getErpSalesId();
    }

    public Date getRequestedDate() {
        return target.getRequestedDate();
    }

    public String getOrderStatusName() {

        String status = "";
        if (target.getSaleType().equals(EnumSaleType.GIFTCARD) || target.getSaleType().equals(EnumSaleType.DONATION)) {
            status = target.isPending() ? "In Process" : "Completed";
        } else {
            status = target.getOrderStatus().getDisplayName();
        }
        return status;
    }

    public EnumSaleStatus getOrderStatus() {
        return target.getOrderStatus();
    }

    public double getTotal() throws PricingException {
        return target.getTotal();
    }

    public Date getDeliveryCutoffTime() {
        return target.getDeliveryCutoffTime();
    }

    public Date getDeliveryStartTime() {
        return target.getDeliveryStartTime();
    }

    public Date getDeliveryEndTime() {
        return target.getDeliveryEndTime();
    }

    public static final String GC_CODE_PERSONAL = EnumDeliveryType.GIFT_CARD_PERSONAL.getCode();

    public static final String GC_CODE_CORPORATE = EnumDeliveryType.GIFT_CARD_CORPORATE.getCode();

    public static final String DONATE_PERSONAL = EnumDeliveryType.DONATION_INDIVIDUAL.getCode();

    public static final String DONATE_CORPORATE = EnumDeliveryType.DONATION_BUSINESS.getCode();

    public boolean isPending() {
        return target.isPending();
    }
    
    public boolean isPendingDeliveryOrder() {
        /*
         * DUP: FDWebSite/docroot/index.jsp
         * LAST UPDATED ON: 11/09/2009
         * LAST UPDATED WITH SVN#: 6677
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: Following logic deteremines which order should be listed when user logs in.  Showing only pending order
         * that are not giftcard or donation orders.
         * NOTE: The code has been updated to follow Java Best Practices standards.
         */

        boolean isPendingDeliveryOrder = false;

        String ordDeliveryType = target.getDeliveryType().toString();
        

        if (target.isPending() && target.getOrderStatus() != EnumSaleStatus.REFUSED_ORDER
                && (!GC_CODE_PERSONAL.equals(ordDeliveryType) && !GC_CODE_CORPORATE.equals(ordDeliveryType))
                && (!DONATE_PERSONAL.equals(ordDeliveryType) && !DONATE_CORPORATE.equals(ordDeliveryType))) {
            isPendingDeliveryOrder = true;
        }

        return isPendingDeliveryOrder;
    }

    public boolean isModifiable() {
        return OrderUtil.isModifiable(target.getErpSalesId(), target.getDeliveryCutoffTime());
    }

    public boolean isShoppable() {
        return (!target.isPending() && !target.getSaleType().equals(EnumSaleType.GIFTCARD) && !target.getSaleType().equals(
                EnumSaleType.DONATION));
    }

    public boolean isRefused() {
        return (target.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER);
    }

    public boolean isInPast(Date now) {
        return (now.compareTo(target.getDeliveryEndTime()) > 0);
    }

	public String getDisplayStatus() {
		String status = getOrderStatusName();
		if("Cancelled".equals(status) || "Processing".equals(status)){
			return status;
		}else if("Submitted".equals(status)){
			return "Pending";
		}
		return "";
	}
	
	public EnumDeliveryType getDeliveryType(){
	    return target.getDeliveryType();
	}
}
