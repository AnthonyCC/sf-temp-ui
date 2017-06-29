package com.freshdirect.customer;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.fdstore.EnumEStoreId;

public class ErpOrderHistory implements OrderHistoryI {
	private static final long serialVersionUID = 4888213033162047918L;

	private Collection<ErpSaleInfo> erpSaleInfos;
	private Collection<ErpSaleInfo> erpRegSaleInfos;

	public ErpOrderHistory(Collection<ErpSaleInfo> erpsaleInfos) {
		this.erpSaleInfos = erpsaleInfos;
		erpRegSaleInfos=ErpOrderHistoryUtil.filterOrders(this.erpSaleInfos,EnumSaleType.REGULAR);
	}

	public Collection<ErpSaleInfo> getErpSaleInfos() {
		return this.erpSaleInfos;
	}
	
	@Override
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

	@Override
    public Date getFirstNonPickupOrderDate(){
		return ErpOrderHistoryUtil.getFirstNonPickupOrderDate(erpRegSaleInfos);
	}

	@Override
    public Date getLastOrderCreateDate(){
		return ErpOrderHistoryUtil.getLastOrderCreateDate(erpRegSaleInfos);
	}

	@Override
    public Date getLastOrderDlvDate(){
		return ErpOrderHistoryUtil.getLastOrderDlvDate(erpRegSaleInfos);
	}
	
	@Override
    public int getOrderCountForChefsTableEligibility(){
		return ErpOrderHistoryUtil.getOrderCountForChefsTableEligibility(erpRegSaleInfos);
	}
	
	@Override
    public double getOrderSubTotalForChefsTableEligibility(){
		return ErpOrderHistoryUtil.getOrderSubTotalForChefsTableEligibility(erpRegSaleInfos);
	}
	
	@Override
    public String getLastOrderId(){
		return ErpOrderHistoryUtil.getLastOrderId(erpRegSaleInfos);
	}

	@Override
    public EnumDeliveryType getLastOrderType(){
		return ErpOrderHistoryUtil.getLastOrderType(erpRegSaleInfos);
	}

	@Override
    public String getLastOrderZone(){
		return ErpOrderHistoryUtil.getLastOrderZone(erpRegSaleInfos);
	}

	@Override
    public int getPhoneOrderCount(){
		return ErpOrderHistoryUtil.getPhoneOrderCount(erpRegSaleInfos);
	}

	@Override
    public int getReturnOrderCount(){
		return ErpOrderHistoryUtil.getReturnOrderCount(erpRegSaleInfos);
	}

	@Override
    public String getSecondToLastSaleId(){
		return ErpOrderHistoryUtil.getSecondToLastSaleId(erpRegSaleInfos);
	}

	@Override
    public int getTotalOrderCount(){
		return ErpOrderHistoryUtil.getTotalOrderCount(erpRegSaleInfos);
	}

	@Override
    public int getValidECheckOrderCount(){
		return ErpOrderHistoryUtil.getValidECheckOrderCount(erpRegSaleInfos);
	}

	@Override
    public int getValidOrderCount(){
		return ErpOrderHistoryUtil.getValidOrderCount(erpRegSaleInfos);
	}
	
	@Override
    public int getValidOrderCount(EnumDeliveryType deliveryType){
		return ErpOrderHistoryUtil.getValidOrderCount(erpRegSaleInfos,deliveryType);
	}

    @Override
    public int getValidOrderCount(EnumEStoreId storeId) {
        return ErpOrderHistoryUtil.getValidOrderCount(erpRegSaleInfos, storeId);
    }

	@Override
    public int getValidPhoneOrderCount(){
		return ErpOrderHistoryUtil.getValidPhoneOrderCount(erpRegSaleInfos);
	}
	
	@Override
    public int getSettledOrderCount() {
		return ErpOrderHistoryUtil.getSettledOrderCount(erpRegSaleInfos);
	}
	
	@Override
    public int getTotalRegularOrderCount(){
		return ErpOrderHistoryUtil.getTotalRegularOrderCount(erpRegSaleInfos);
	}
	
	@Override
    public int getSettledECheckOrderCount() {
		return ErpOrderHistoryUtil.getSettledECheckOrderCount(erpRegSaleInfos);
	}
	
	@Override
    public int getUnSettledEBTOrderCount(){
		return ErpOrderHistoryUtil.getUnSettledEBTOrderCount(erpRegSaleInfos);
	}
	
	@Override
    public int getUnSettledEBTOrderCount(String currSaleId){
		return ErpOrderHistoryUtil.getUnSettledEBTOrderCount(erpRegSaleInfos,currSaleId);
	}
	
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("ErpOrderHistory version $$$$$$$$$$$$$$$$$$$"+"\n");
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
