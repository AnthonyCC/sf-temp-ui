package com.freshdirect.customer;

import java.util.Collection;
import java.util.Date;

/**
 * @author skrishnasamy
 * @version 1.0
 * @created 19-Dec-2007 2:58:50 PM
 */
public class ErpOrderHistory implements OrderHistoryI {
	private static final long serialVersionUID = 4888213033162047918L;


	private Collection erpSaleInfos;
	private Collection erpRegSaleInfos;
	/**
	 * 
	 * @param erpsaleInfos
	 */
	public ErpOrderHistory(Collection erpsaleInfos){
		this.erpSaleInfos = erpsaleInfos;
		erpRegSaleInfos=ErpOrderHistoryUtil.filterOrders(this.erpSaleInfos,EnumSaleType.REGULAR);
	}

	public Collection getErpSaleInfos() {
		return this.erpSaleInfos;
	}
	
	public int getDeliveredOrderCount(){
		return ErpOrderHistoryUtil.getDeliveredOrderCount(erpRegSaleInfos);
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

	public int getValidPhoneOrderCount(){
		return ErpOrderHistoryUtil.getValidPhoneOrderCount(erpRegSaleInfos);
	}
	
	public int getSettledOrderCount() {
		return ErpOrderHistoryUtil.getSettledOrderCount(erpRegSaleInfos);
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