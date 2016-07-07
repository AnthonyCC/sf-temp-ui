package com.freshdirect.customer;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.fdstore.EnumEStoreId;

/**
 * @author skrishnasamy
 * @version 1.0
 * @created 19-Dec-2007 2:58:50 PM
 */
public class ErpOrderHistory implements OrderHistoryI {
	private static final long serialVersionUID = 4888213033162047918L;


	private Collection<ErpSaleInfo> erpSaleInfos;
	private Collection<ErpSaleInfo> erpRegSaleInfos;

	/**
	 * 
	 * @param erpsaleInfos
	 */
	public ErpOrderHistory(Collection<ErpSaleInfo> erpsaleInfos) {
		this.erpSaleInfos = erpsaleInfos;
		erpRegSaleInfos=ErpOrderHistoryUtil.filterOrders(this.erpSaleInfos,EnumSaleType.REGULAR);
	}

	public Collection<ErpSaleInfo> getErpSaleInfos() {
		return this.erpSaleInfos;
	}
	
	public int getDeliveredOrderCount(){
		return ErpOrderHistoryUtil.getDeliveredOrderCount(erpRegSaleInfos);
	}

	@Override
	public Date getFirstOrderDate() {
		return ErpOrderHistoryUtil.getFirstOrderDate(erpRegSaleInfos);
	}
	
	@Override
	public Date getFirstOrderDateByStore(EnumEStoreId estoreId) {
		return ErpOrderHistoryUtil.getFirstOrderDateByStore(erpRegSaleInfos, estoreId);
	}

	public Date getFirstNonPickupOrderDate(){
		return ErpOrderHistoryUtil.getFirstNonPickupOrderDate(erpRegSaleInfos);
	}

	public Date getLastOrderCreateDate(){
		return ErpOrderHistoryUtil.getLastOrderCreateDate(erpRegSaleInfos);
	}

	public Date getLastOrderDlvDate(){
		return ErpOrderHistoryUtil.getLastOrderDlvDate(erpRegSaleInfos);
	}
	
	public int getOrderCountForChefsTableEligibility(){
		return ErpOrderHistoryUtil.getOrderCountForChefsTableEligibility(erpRegSaleInfos);
	}
	
	public double getOrderSubTotalForChefsTableEligibility(){
		return ErpOrderHistoryUtil.getOrderSubTotalForChefsTableEligibility(erpRegSaleInfos);
	}
	
	public String getLastOrderId(){
		return ErpOrderHistoryUtil.getLastOrderId(erpRegSaleInfos);
	}

	public EnumDeliveryType getLastOrderType(){
		return ErpOrderHistoryUtil.getLastOrderType(erpRegSaleInfos);
	}

	public String getLastOrderZone(){
		return ErpOrderHistoryUtil.getLastOrderZone(erpRegSaleInfos);
	}

	public int getPhoneOrderCount(){
		return ErpOrderHistoryUtil.getPhoneOrderCount(erpRegSaleInfos);
	}

	public int getReturnOrderCount(){
		return ErpOrderHistoryUtil.getReturnOrderCount(erpRegSaleInfos);
	}

	public String getSecondToLastSaleId(){
		return ErpOrderHistoryUtil.getSecondToLastSaleId(erpRegSaleInfos);
	}

	public int getTotalOrderCount(){
		return ErpOrderHistoryUtil.getTotalOrderCount(erpRegSaleInfos);
	}

	public int getValidECheckOrderCount(){
		return ErpOrderHistoryUtil.getValidECheckOrderCount(erpRegSaleInfos);
	}

	public int getValidOrderCount(){
		return ErpOrderHistoryUtil.getValidOrderCount(erpRegSaleInfos);
	}
	
	public int getValidOrderCount(EnumDeliveryType deliveryType){
		return ErpOrderHistoryUtil.getValidOrderCount(erpRegSaleInfos,deliveryType);
	}

	public int getValidPhoneOrderCount(){
		return ErpOrderHistoryUtil.getValidPhoneOrderCount(erpRegSaleInfos);
	}
	
	public int getSettledOrderCount() {
		return ErpOrderHistoryUtil.getSettledOrderCount(erpRegSaleInfos);
	}
	
	public int getTotalRegularOrderCount(){
		return ErpOrderHistoryUtil.getTotalRegularOrderCount(erpRegSaleInfos);
	}
	
	public int getSettledECheckOrderCount() {
		return ErpOrderHistoryUtil.getSettledECheckOrderCount(erpRegSaleInfos);
	}
	
	public int getUnSettledEBTOrderCount(){
		return ErpOrderHistoryUtil.getUnSettledEBTOrderCount(erpRegSaleInfos);
	}
	
	public int getUnSettledEBTOrderCount(String currSaleId){
		return ErpOrderHistoryUtil.getUnSettledEBTOrderCount(erpRegSaleInfos,currSaleId);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("ErpOrderHistory version $$$$$$$$$$$$$$$$$$$"+"\n");
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
		buf.append("UnSettledEBTOrderCount "+getUnSettledEBTOrderCount()+"\n");
		buf.append("OrderCountForChefsTableEligibility "+getOrderCountForChefsTableEligibility()+"\n");
		buf.append("OrderSubTotalForChefsTableEligibility "+getOrderSubTotalForChefsTableEligibility()+"\n");
		return buf.toString();
	}

	@Override
	public int getValidMasterPassOrderCount() {
		return ErpOrderHistoryUtil.getValidMasterPassOrderCount(erpRegSaleInfos);
	}

}
