package com.freshdirect.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.payment.EnumPaymentMethodType;


/**
 * @author skrishnasamy
 * @version 1.0
 * @created 19-Dec-2007 2:58:50 PM
 */
public class ErpOrderHistoryUtil {

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getDeliveredOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for(Iterator i = erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (saleInfo.isDelivered()) {
				ret++;
			}
		}
		return ret;
	}

	public static int getSettledOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for(Iterator i = erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (saleInfo.isSettled()) {
				ret++;
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getPhoneOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for (Iterator it = erpSaleInfos.iterator(); it.hasNext(); ) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) it.next();
			if (EnumTransactionSource.CUSTOMER_REP.equals(saleInfo.getSource())) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getReturnOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for (Iterator it = erpSaleInfos.iterator(); it.hasNext(); ) {
			ErpSaleInfo orderInfo = (ErpSaleInfo) it.next();
			if (orderInfo.getStatus().isReturned())
				ret++;
		}
		return ret;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getTotalOrderCount(Collection erpSaleInfos){
		return erpSaleInfos.size();
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getValidECheckOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for(Iterator i = erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled() && EnumPaymentMethodType.ECHECK.equals(saleInfo.getPaymentMethodType())){
				ret++;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getValidOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for(Iterator i = erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled()) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getValidPhoneOrderCount(Collection erpSaleInfos){
		int ret = 0;
		for(Iterator i = erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled() && EnumTransactionSource.CUSTOMER_REP.equals(saleInfo.getSource())) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static Date getFirstNonPickupOrderDate(Collection erpSaleInfos){
		Date date = null;
		for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			if (!saleInfo.getStatus().isCanceled() && !EnumDeliveryType.PICKUP.equals(saleInfo.getDeliveryType()) ) {
				Date createDate = saleInfo.getCreateDate();
				if (date==null || createDate.before(date)) {
					date = createDate;
				}
			}
		}
		return date;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static ErpSaleInfo getLastSale(Collection erpSaleInfos){
		return getLastSaleBefore(erpSaleInfos, null);
	}

	public static String getLastOrderId(Collection erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getSaleId();
	}

	public static Date getLastOrderCreateDate(Collection erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getCreateDate();
	}

	public static Date getLastOrderDlvDate(Collection erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getRequestedDate();
	}
	
	public static EnumDeliveryType getLastOrderType(Collection erpSaleInfos){
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getDeliveryType();
	}
	
	public static String getLastOrderZone(Collection erpSaleInfos){
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getZone();
	}
	
	/**
	 * 
	 * @param erpSaleInfos
	 * @param maxCreateDate
	 */
	private static ErpSaleInfo getLastSaleBefore(Collection erpSaleInfos, Date maxCreateDate){
		ErpSaleInfo lastOrder = null;
		Date date = null;
		for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			Date createDate = saleInfo.getCreateDate();
			if ((date == null || createDate.after(date)) && (maxCreateDate==null || maxCreateDate.after(createDate))) {
				date = createDate;
				lastOrder = saleInfo;
			}
		}
		return lastOrder;
	}

	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static ErpSaleInfo getSecondToLastSale(Collection erpSaleInfos){
		ErpSaleInfo lastSale = getLastSale(erpSaleInfos);
		return lastSale == null ? null : getLastSaleBefore(erpSaleInfos, lastSale.getCreateDate());
	}
	
	public static String getSecondToLastSaleId(Collection erpSaleInfos){
		ErpSaleInfo secondToLastOrder = getSecondToLastSale(erpSaleInfos);
		return secondToLastOrder==null ? null : secondToLastOrder.getSaleId();
	}
	
	/**
	 * 
	 * @param erpSaleInfos
	 */
	public static int getOrderCountForChefsTableEligibility(Collection erpSaleInfos){
		Calendar beginCal = Calendar.getInstance();
		beginCal.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endCal = Calendar.getInstance();
		beginCal.add(Calendar.MONTH, -2);
		int orderCount = 0;
		Date beginDate = beginCal.getTime();
		Date endDate = endCal.getTime();
	
		for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next(); 			
			Date deliveryDate = saleInfo.getRequestedDate();

			if (!saleInfo.isMakeGood()&&deliveryDate.after(beginDate) && deliveryDate.before(endDate) && 
					!saleInfo.getDeliveryType().equals(EnumDeliveryType.CORPORATE) &&
					!saleInfo.getStatus().equals(EnumSaleStatus.CANCELED) &&
					!saleInfo.getSaleType().equals(EnumSaleType.SUBSCRIPTION)) {
				orderCount++;
			}
		}
		return orderCount;
	}
	
	
	public static double getOrderSubTotalForChefsTableEligibility(Collection erpSaleInfos){
		
		double amount=0.0;
		Calendar beginCal = Calendar.getInstance();
		beginCal.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endCal = Calendar.getInstance();
		beginCal.add(Calendar.MONTH, -2);
		int orderCount = 0;
		Date beginDate = beginCal.getTime();
		Date endDate = endCal.getTime();
		for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next(); 			
			Date deliveryDate = saleInfo.getRequestedDate();
			if (!saleInfo.isMakeGood()&& deliveryDate.after(beginDate) && deliveryDate.before(endDate) && 
					!saleInfo.getDeliveryType().equals(EnumDeliveryType.CORPORATE) &&
					!saleInfo.getStatus().equals(EnumSaleStatus.CANCELED) &&
					//!saleInfo.getPaymentType().equals(EnumPaymentType.MAKE_GOOD) &&
					!saleInfo.getSaleType().equals(EnumSaleType.SUBSCRIPTION)) {
				amount=amount+saleInfo.getSubTotal();
			}
		}
		return amount;
	}

	public static Collection filterOrders(Collection erpSaleInfos, EnumSaleType saleType) {
		
		if(saleType==null) {
			return erpSaleInfos;
		}
		List filteredOrders=new ArrayList();
		if(erpSaleInfos!=null) {
			ErpSaleInfo saleInfo =null;
			for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
				saleInfo = (ErpSaleInfo) i.next();
				if(saleType.equals(saleInfo.getSaleType())) {
					filteredOrders.add(saleInfo);
				}
			}
		}
		
		return filteredOrders;
	}

	
	public static int getTotalRegularOrderCount(Collection erpSaleInfos){
		int intCount = 0;
		if(erpSaleInfos != null) {
			for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
				ErpSaleInfo saleInfo = (ErpSaleInfo) i.next(); 			
				//Date deliveryDate = saleInfo.getRequestedDate();
				if (saleInfo.getSaleType().equals(EnumSaleType.REGULAR)) {
					intCount++;
				}
			}
		}
		return intCount;
	}
}
