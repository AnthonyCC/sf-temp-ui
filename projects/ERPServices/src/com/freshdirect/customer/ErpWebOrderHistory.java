package com.freshdirect.customer;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.EnumEStoreId;

public class ErpWebOrderHistory implements OrderHistoryI {

    private static final long serialVersionUID = -2545953657947511869L;

    private Map<String, Object> orderHistoryInfo = new HashMap<String, Object>();

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
    private static final String VALID_FD_STORE_ORDER_COUNT = "validFdStoreOrderCount";
    private static final String VALID_FDX_STORE_ORDER_COUNT = "validFdxStoreOrderCount";
	private static final String VALID_PHONE_ORDER_COUNT = "validPhoneOrderCount";
	private static final String SETTLED_ORDER_COUNT = "settledOrderCount";
	private static final String SETTLED_FD_ORDER_COUNT = "settledFDOrderCount";
	private static final String SETTLED_FDX_ORDER_COUNT = "settledFDXOrderCount";
	private static final String UNSETTLED_EBT_ORDER_COUNT = "UnsettledEBTOrderCount";
	private static final String VALID_MASTERPASS_ORDER_COUNT = "validMasterPassOrderCount";
	private static final String FIRST_ORDER_DATE_FOR_FD = "firstOrderDateForFD";
	private static final String FIRST_ORDER_DATE_FOR_FDX = "firstOrderDateForFDX";
	private static final String VALID_ORDER_COUNT_FD_HOME = "validOrderCountForFDHome";
	private static final String VALID_ORDER_COUNT_FD_CORP = "validOrderCountForFDCorp";
	private static final String VALID_ORDER_COUNT_FD_PICKUP = "validOrderCountForFDPickUp";
	private static final String VALID_ORDER_COUNT_FDX = "validOrderCountForFDX";
	private static final String VALID_ORDER_COUNT_FDC_SALESORG = "validOrderCountSalesOrg";
	
	
    public ErpWebOrderHistory(Collection<ErpSaleInfo> erpSaleInfos) {
		//Populate the orderHistoryInfo using erpSaleInfos Collection.
        orderHistoryInfo.put(DELIVERED_ORDER_COUNT, ErpOrderHistoryUtil.getDeliveredOrderCount(erpSaleInfos));
		orderHistoryInfo.put(FIRST_ORDER_DATE, ErpOrderHistoryUtil.getFirstOrderDate(erpSaleInfos));
		orderHistoryInfo.put(FIRST_NON_PICKUP_ORDER_DATE, ErpOrderHistoryUtil.getFirstNonPickupOrderDate(erpSaleInfos));
		orderHistoryInfo.put(FIRST_ORDER_DATE_FOR_FD, ErpOrderHistoryUtil.getFirstOrderDateByStore(erpSaleInfos,EnumEStoreId.FD));
		orderHistoryInfo.put(FIRST_ORDER_DATE_FOR_FDX, ErpOrderHistoryUtil.getFirstOrderDateByStore(erpSaleInfos,EnumEStoreId.FDX));
		orderHistoryInfo.put(LAST_ORDER_CREATE_DATE, ErpOrderHistoryUtil.getLastOrderCreateDate(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_DELIVERY_DATE, ErpOrderHistoryUtil.getLastOrderDlvDate(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_ID, ErpOrderHistoryUtil.getLastOrderId(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_TYPE, ErpOrderHistoryUtil.getLastOrderType(erpSaleInfos));
		orderHistoryInfo.put(LAST_ORDER_ZONE, ErpOrderHistoryUtil.getLastOrderZone(erpSaleInfos));
        orderHistoryInfo.put(PHONE_ORDER_COUNT, ErpOrderHistoryUtil.getPhoneOrderCount(erpSaleInfos));
        orderHistoryInfo.put(RETURNED_ORDER_COUNT, ErpOrderHistoryUtil.getReturnOrderCount(erpSaleInfos));
		orderHistoryInfo.put(SECOND_TO_LAST_ORDER_ID, ErpOrderHistoryUtil.getSecondToLastSaleId(erpSaleInfos));
        orderHistoryInfo.put(TOTAL_ORDER_COUNT, ErpOrderHistoryUtil.getTotalOrderCount(erpSaleInfos));
        orderHistoryInfo.put(VALID_ECHECK_ORDER_COUNT, ErpOrderHistoryUtil.getValidECheckOrderCount(erpSaleInfos));
        orderHistoryInfo.put(VALID_ORDER_COUNT, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos));
        orderHistoryInfo.put(VALID_FD_STORE_ORDER_COUNT, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, EnumEStoreId.FD));
        orderHistoryInfo.put(VALID_FDX_STORE_ORDER_COUNT, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, EnumEStoreId.FDX));
        orderHistoryInfo.put(VALID_PHONE_ORDER_COUNT, ErpOrderHistoryUtil.getValidPhoneOrderCount(erpSaleInfos));
        orderHistoryInfo.put(SETTLED_ORDER_COUNT, ErpOrderHistoryUtil.getSettledOrderCount(erpSaleInfos));
        orderHistoryInfo.put(SETTLED_FD_ORDER_COUNT, ErpOrderHistoryUtil.getSettledOrderCountByStore(erpSaleInfos, EnumEStoreId.FD));
        orderHistoryInfo.put(SETTLED_FDX_ORDER_COUNT, ErpOrderHistoryUtil.getSettledOrderCountByStore(erpSaleInfos, EnumEStoreId.FDX));
        orderHistoryInfo.put(UNSETTLED_EBT_ORDER_COUNT, ErpOrderHistoryUtil.getUnSettledEBTOrderCount(erpSaleInfos));
        orderHistoryInfo.put(VALID_MASTERPASS_ORDER_COUNT, ErpOrderHistoryUtil.getValidMasterPassOrderCount(erpSaleInfos));
        orderHistoryInfo.put(VALID_ORDER_COUNT_FD_HOME, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, EnumDeliveryType.HOME));
        orderHistoryInfo.put(VALID_ORDER_COUNT_FD_CORP, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, EnumDeliveryType.CORPORATE));
        orderHistoryInfo.put(VALID_ORDER_COUNT_FD_PICKUP, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, EnumDeliveryType.PICKUP));
        orderHistoryInfo.put(VALID_ORDER_COUNT_FDX, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, EnumDeliveryType.FDX));
        orderHistoryInfo.put(VALID_ORDER_COUNT_FDC_SALESORG, ErpOrderHistoryUtil.getValidOrderCount(erpSaleInfos, "1400"));
	}

	@Override
    public int getDeliveredOrderCount(){
		return ((Integer)orderHistoryInfo.get(DELIVERED_ORDER_COUNT)).intValue();
	}

	@Override
    public Date getFirstOrderDate(){
		return ((Date)orderHistoryInfo.get(FIRST_ORDER_DATE));
	}
	
	@Override
    public Date getFirstNonPickupOrderDate(){
		return ((Date)orderHistoryInfo.get(FIRST_NON_PICKUP_ORDER_DATE));
	}

	@Override
    public Date getLastOrderCreateDate(){
		return ((Date)orderHistoryInfo.get(LAST_ORDER_CREATE_DATE));
	}

	@Override
    public Date getLastOrderDlvDate(){
		return ((Date)orderHistoryInfo.get(LAST_ORDER_DELIVERY_DATE));
	}

	@Override
    public String getLastOrderId(){
		return ((String)orderHistoryInfo.get(LAST_ORDER_ID));
	}

	@Override
    public EnumDeliveryType getLastOrderType(){
		return ((EnumDeliveryType)orderHistoryInfo.get(LAST_ORDER_TYPE));
	}

	@Override
    public String getLastOrderZone(){
		return ((String)orderHistoryInfo.get(LAST_ORDER_ZONE));
	}

	@Override
    public int getPhoneOrderCount(){
		return ((Integer)orderHistoryInfo.get(PHONE_ORDER_COUNT)).intValue();
	}

	@Override
    public int getReturnOrderCount(){
		return ((Integer)orderHistoryInfo.get(RETURNED_ORDER_COUNT)).intValue();
	}

	@Override
    public String getSecondToLastSaleId(){
		return ((String)orderHistoryInfo.get(SECOND_TO_LAST_ORDER_ID));
	}

	@Override
    public int getTotalOrderCount(){
		return ((Integer)orderHistoryInfo.get(TOTAL_ORDER_COUNT)).intValue();
	}

	@Override
    public int getValidECheckOrderCount(){
		return ((Integer)orderHistoryInfo.get(VALID_ECHECK_ORDER_COUNT)).intValue();
	}

	@Override
    public int getValidOrderCount(){
        return ((Integer) orderHistoryInfo.get(VALID_ORDER_COUNT)).intValue();
	}
	
	@Override
    public int getValidOrderCount(EnumDeliveryType deliveryType){
		if(null != deliveryType){
			if(EnumDeliveryType.HOME.equals(deliveryType)){
				return ((Integer)orderHistoryInfo.get(VALID_ORDER_COUNT_FD_HOME)).intValue();
			} else if(EnumDeliveryType.CORPORATE.equals(deliveryType)){
				return ((Integer)orderHistoryInfo.get(VALID_ORDER_COUNT_FD_CORP)).intValue();
			} else if(EnumDeliveryType.PICKUP.equals(deliveryType)){
				return ((Integer)orderHistoryInfo.get(VALID_ORDER_COUNT_FD_PICKUP)).intValue();
			} else if(EnumDeliveryType.FDX.equals(deliveryType)){
				return ((Integer)orderHistoryInfo.get(VALID_ORDER_COUNT_FDX)).intValue();
			}
		}
        return getValidOrderCount();
	}

    @Override
    public int getValidOrderCount(EnumEStoreId storeId) {
        int count = 0;
        switch (storeId) {
            case FD:
                count = ((Integer) orderHistoryInfo.get(VALID_FD_STORE_ORDER_COUNT)).intValue();
                break;

            case FDX:
                count = ((Integer) orderHistoryInfo.get(VALID_FDX_STORE_ORDER_COUNT)).intValue();
                break;

            default:
                count = getValidOrderCount();
                break;
        }
        return count;
    }

	@Override
    public int getValidPhoneOrderCount(){
		return ((Integer)orderHistoryInfo.get(VALID_PHONE_ORDER_COUNT)).intValue();
	}

	@Override
    public int getSettledOrderCount() {
		return ((Integer)orderHistoryInfo.get(SETTLED_ORDER_COUNT)).intValue();
	}
	
	@Override
    public int getSettledOrderCount(EnumEStoreId estoreId) {
		if(estoreId.equals(EnumEStoreId.FDX)){
			return ((Integer)orderHistoryInfo.get(SETTLED_FDX_ORDER_COUNT)).intValue();
		}else{
			return ((Integer)orderHistoryInfo.get(SETTLED_FD_ORDER_COUNT)).intValue();
		}
	}
	
	@Override
    public double getOrderSubTotalForChefsTableEligibility() {
		return 0.0;
	}
	
	@Override
    public int getTotalRegularOrderCount(){
		return 0;
	}
	
	@Override
    public int getSettledECheckOrderCount() {
		return 0;
	}
	
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("ErpWebOrderHistory version $$$$$$$$$$$$$$$$$$$"+"\n");
		buf.append("ValidOrderCount "+getValidOrderCount()+"\n");
        buf.append("ValidFdStoreOrderCount " + getValidOrderCount(EnumEStoreId.FD) + "\n");
        buf.append("ValidFdxStoreOrderCount " + getValidOrderCount(EnumEStoreId.FDX) + "\n");
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
		buf.append("SettledOrderCountFD "+getSettledOrderCount(EnumEStoreId.FD)+"\n");
		buf.append("SettledOrderCountFDX "+getSettledOrderCount(EnumEStoreId.FDX)+"\n");
		buf.append("UnSettledEBTOrderCount "+getUnSettledEBTOrderCount()+"\n");
		return buf.toString();
	}

	@Override
	public int getOrderCountForChefsTableEligibility() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	@Override
    public int getUnSettledEBTOrderCount(){
		return ((Integer)orderHistoryInfo.get(UNSETTLED_EBT_ORDER_COUNT)).intValue();
	}
	
	@Override
    public int getUnSettledEBTOrderCount(String currSaleId){
		//TODO: Need to fix this, if somebody wants to use it.
		return 0;
	}

	@Override
	public int getValidMasterPassOrderCount() {
		return ((Integer)orderHistoryInfo.get(VALID_MASTERPASS_ORDER_COUNT)).intValue();
	}

	@Override
	public Date getFirstOrderDateByStore(EnumEStoreId eStoreId) {
		if(null !=eStoreId){
			if(EnumEStoreId.FDX.equals(eStoreId))
				return ((Date)orderHistoryInfo.get(FIRST_ORDER_DATE_FOR_FDX));
			else
				return ((Date)orderHistoryInfo.get(FIRST_ORDER_DATE_FOR_FD));
		}
		return ((Date)orderHistoryInfo.get(FIRST_ORDER_DATE));
	}

	@Override
	public boolean hasSettledOrders() {
		return getSettledOrderCount() > 0;
	}
	
	@Override
	public boolean hasSettledOrders(EnumEStoreId estoreId) {
		return getSettledOrderCount(estoreId) > 0;
	}

	@Override
	public int getValidOrderCount(String salesOrg) {
		// TODO Auto-generated method stub
		return 0;
	}

}