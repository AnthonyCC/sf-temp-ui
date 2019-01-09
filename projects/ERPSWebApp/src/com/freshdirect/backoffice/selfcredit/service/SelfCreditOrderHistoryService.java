package com.freshdirect.backoffice.selfcredit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderData;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderHistoryData;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;

public class SelfCreditOrderHistoryService {

    private static final Logger LOGGER = LoggerFactory.getInstance(SelfCreditOrderHistoryService.class);
    private static final SelfCreditOrderHistoryService INSTANCE = new SelfCreditOrderHistoryService();

    private SelfCreditOrderHistoryService() {
    }

    public static SelfCreditOrderHistoryService defaultService() {
        return INSTANCE;
    }

    public SelfCreditOrderHistoryData collectSelfCreditOrders(FDUserI user) throws FDResourceException {

    	List<FDOrderInfoI> orders = QuickShopHelper.getOrderHistoryInfo(user, true);
    	EnumEStoreId store = user.getUserContext().getStoreContext().getEStoreId();
        Date currentDate = new Date();
        
        List<SelfCreditOrderData> ordersToDisplay = new ArrayList<SelfCreditOrderData>();
        for (FDOrderInfoI fdOrderInfo : orders) {
        	final boolean eligibleForSelfCredit = isEligibleForSelfCredit(fdOrderInfo, currentDate, store);
        	if(eligibleForSelfCredit) {
                SelfCreditOrderData order = new SelfCreditOrderData();
                order.setSaleId(fdOrderInfo.getErpSalesId());
                order.setRequestedDate(fdOrderInfo.getRequestedDate());
                order.setDeliveryStart(fdOrderInfo.getDeliveryStartTime());
                order.setDeliveryEnd(fdOrderInfo.getDeliveryEndTime());
                order.setStandingOrderName((null == fdOrderInfo.getStandingOrderId()) ?"" :collectOrderName(fdOrderInfo.getStandingOrderId()));
                ordersToDisplay.add(order);
			}
        }
        
        SelfCreditOrderHistoryData orderHistory = new SelfCreditOrderHistoryData();
        orderHistory.setUserFirstName(user.getFirstName());
        orderHistory.setUserLastName(user.getLastName());
        orderHistory.setOrders(ordersToDisplay.subList(0, Math.min(FDStoreProperties.getOrderComplaintDropdownLimit(), ordersToDisplay.size())));
        return orderHistory;
    }

    private boolean isEligibleForSelfCredit(FDOrderInfoI fdOrderInfo, Date currentDate, EnumEStoreId store) {
      final boolean orderStatusEligible= isOrderStatusEligibleForSelfCredit(fdOrderInfo.getOrderStatus());
      final boolean isRecentOrder = isRecentOrder(fdOrderInfo.getCreateRequestedDate(), currentDate);
      final boolean makeGoodOrder = fdOrderInfo.isMakeGood();
      final boolean orderIsFromStore = store.equals(fdOrderInfo.getEStoreId());
      final boolean giftCardOrder = EnumSaleType.GIFTCARD.equals(fdOrderInfo.getSaleType());
      return orderStatusEligible && isRecentOrder && !makeGoodOrder && orderIsFromStore && !giftCardOrder;
	}

	private boolean isOrderStatusEligibleForSelfCredit(EnumSaleStatus orderStatus) {
		return EnumSaleStatus.SETTLED.equals(orderStatus) ||EnumSaleStatus.PAYMENT_PENDING.equals(orderStatus) ||EnumSaleStatus.ENROUTE.equals(orderStatus) || EnumSaleStatus.CAPTURE_PENDING.equals(orderStatus);
	}

    private boolean isRecentOrder(Date createRequestedDate, Date currentDate) {
        int monthsBetween = DateUtil.monthsBetween(currentDate, createRequestedDate);
        return monthsBetween < 150;//FDStoreProperties.getOrderHistoryFromInMonths();
    }

	private String collectOrderName(String standingOrderId) throws FDResourceException {
		FDStandingOrder so = FDStandingOrdersManager.getInstance().load(
				new PrimaryKey(standingOrderId));
		return (null == so.getCustomerListName()) ?"" :so.getCustomerListName();
	}
}
