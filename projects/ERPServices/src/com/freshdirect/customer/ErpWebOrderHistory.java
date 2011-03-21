package com.freshdirect.customer;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author skrishnasamy
 * @version 1.0
 * @created 19-Dec-2007 2:58:50 PM
 */
public class ErpWebOrderHistory implements OrderHistoryI {

	private Map orderHistoryInfo = new HashMap();
	
	private static final String DELIVERED_ORDER_COUNT = "deliveredOrderCount";
	private static final String FIRST_ORDER_DATE = "firstOrderDate";
	private static final String FIRST_NON_PICKUP_ORDER_DATE = "firstNonPickupOrderDate";
	private static final String LAST_ORDER_CREATE_DATE = "lastOrderCreateDate";
	private static final String LAST_ORDER_DELIVERY_DATE = "lastOrderDlvDate";
	private static final String LAST_ORDER_ID = "lastOrderId";
	private static final String LAST_ORDER_TYPE = "lastOrderType";
	private static final String LAST_ORDER_ZONE = "lastOrderZone";
	private static final String PHONE_ORDER_COUNT = "phoneOrderCount";
	private static final String RETURNED_ORDER_COUNT = "returnedOrderCount";
	private static final String SECOND_TO_LAST_ORDER_ID = "secondToLastSaleId";
	private static final String TOTAL_ORDER_COUNT = "totalOrderCount";
	private static final String VALID_ECHECK_ORDER_COUNT = "validECheckOrderCount";
	private static final String VALID_ORDER_COUNT = "validOrderCount";
	private static final String VALID_PHONE_ORDER_COUNT = "validPhoneOrderCount";
	private static final String SETTLED_ORDER_COUNT = "settledOrderCount";
	/**
	 * 
	 * @param erpSaleInfos
	 */
	public ErpWebOrderHistory(Collection erpSaleInfos){
		//Populate the orderHistoryInfo using erpSaleInfos Collection.
		orderHistoryInfo.put(DELIVERED_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getDeliveredOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(FIRST_ORDER_DATE, ErpOrderHistoryUtil.getFirstOrderDate(erpSaleInfos));
		orderHistoryInfo.put(FIRST_NON_PICKUP_ORDER_DATE, ErpOrderHistoryUtil.getFirstNonPickupOrderDate(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_CREATE_DATE, ErpOrderHistoryUtil.getLastOrderCreateDate(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_DELIVERY_DATE, ErpOrderHistoryUtil.getLastOrderDlvDate(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_ID, ErpOrderHistoryUtil.getLastOrderId(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_TYPE, ErpOrderHistoryUtil.getLastOrderType(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_ZONE, ErpOrderHistoryUtil.getLastOrderZone(erpSaleInfos));
		orderHistoryInfo.put(PHONE_ORDER_COUNT,  new Integer(ErpOrderHistoryUtil.getPhoneOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(RETURNED_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getReturnOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(SECOND_TO_LAST_ORDER_ID, ErpOrderHistoryUtil.getSecondToLastSaleId(erpSaleInfos));
		orderHistoryInfo.put(TOTAL_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getTotalOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(VALID_ECHECK_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getValidECheckOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(VALID_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(VALID_PHONE_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getValidPhoneOrderCount(erpSaleInfos)));
		orderHistoryInfo.put(SETTLED_ORDER_COUNT, new Integer(ErpOrderHistoryUtil.getSettledOrderCount(erpSaleInfos)));
	}

	public int getDeliveredOrderCount(){
		return ((Integer)orderHistoryInfo.get(DELIVERED_ORDER_COUNT)).intValue();
	}

	public Date getFirstOrderDate(){
		return ((Date)orderHistoryInfo.get(FIRST_ORDER_DATE));
	}
	
	public Date getFirstNonPickupOrderDate(){
		return ((Date)orderHistoryInfo.get(FIRST_NON_PICKUP_ORDER_DATE));
	}

	public Date getLastOrderCreateDate(){
		return ((Date)orderHistoryInfo.get(LAST_ORDER_CREATE_DATE));
	}

	public Date getLastOrderDlvDate(){
		return ((Date)orderHistoryInfo.get(LAST_ORDER_DELIVERY_DATE));
	}

	public String getLastOrderId(){
		return ((String)orderHistoryInfo.get(LAST_ORDER_ID));
	}

	public EnumDeliveryType getLastOrderType(){
		return ((EnumDeliveryType)orderHistoryInfo.get(LAST_ORDER_TYPE));
	}

	public String getLastOrderZone(){
		return ((String)orderHistoryInfo.get(LAST_ORDER_ZONE));
	}

	public int getPhoneOrderCount(){
		return ((Integer)orderHistoryInfo.get(PHONE_ORDER_COUNT)).intValue();
	}

	public int getReturnOrderCount(){
		return ((Integer)orderHistoryInfo.get(RETURNED_ORDER_COUNT)).intValue();
	}

	public String getSecondToLastSaleId(){
		return ((String)orderHistoryInfo.get(SECOND_TO_LAST_ORDER_ID));
	}

	public int getTotalOrderCount(){
		return ((Integer)orderHistoryInfo.get(TOTAL_ORDER_COUNT)).intValue();
	}

	public int getValidECheckOrderCount(){
		return ((Integer)orderHistoryInfo.get(VALID_ECHECK_ORDER_COUNT)).intValue();
	}

	public int getValidOrderCount(){
		return ((Integer)orderHistoryInfo.get(VALID_ORDER_COUNT)).intValue();
	}

	public int getValidPhoneOrderCount(){
		return ((Integer)orderHistoryInfo.get(VALID_PHONE_ORDER_COUNT)).intValue();
	}

	public int getSettledOrderCount() {
		return ((Integer)orderHistoryInfo.get(SETTLED_ORDER_COUNT)).intValue();
	}
	
	public double getOrderSubTotalForChefsTableEligibility() {
		return 0.0;
	}
	
	public int getTotalRegularOrderCount(){
		return 0;
	}
	
	public int getSettledECheckOrderCount() {
		return 0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("ErpWebOrderHistory version $$$$$$$$$$$$$$$$$$$"+"\n");
		buf.append("ValidOrderCount "+getValidOrderCount()+"\n");
		buf.append("LastOrderId "+getLastOrderId()+"\n");
		buf.append("ValidPhoneOrderCount "+getValidPhoneOrderCount()+"\n");
		buf.append("TotalOrderCount "+getTotalOrderCount()+"\n");
		buf.append("LastOrderCreateDate "+getLastOrderCreateDate()+"\n");
		buf.append("SecondToLastSaleId "+getSecondToLastSaleId()+"\n");
		buf.append("FirstOrderDate "+getFirstOrderDate()+"\n");
		buf.append("FirstNonPickupOrderDate "+getFirstNonPickupOrderDate()+"\n");
		buf.append("lastOrderType "+getLastOrderType()+"\n");
		buf.append("lastOrderDlvDate "+getLastOrderDlvDate());
		buf.append("ValidECheckOrderCount "+getValidECheckOrderCount()+"\n");
		buf.append("PhoneOrderCount "+getPhoneOrderCount()+"\n");
		buf.append("ReturnOrderCount "+getReturnOrderCount()+"\n");
		buf.append("DeliveredOrderCount "+getDeliveredOrderCount()+"\n");
		buf.append("SettledOrderCount "+getSettledOrderCount()+"\n");
		return buf.toString();
	}
}