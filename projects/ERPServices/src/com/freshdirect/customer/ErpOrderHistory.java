package com.freshdirect.customer;

import java.util.Collection;
import java.util.Date;

/**
 * @author skrishnasamy
 * @version 1.0
 * @created 19-Dec-2007 2:58:50 PM
 */
public class ErpOrderHistory implements OrderHistoryI {

	private Collection erpSaleInfos;
	/**
	 * 
	 * @param erpsaleInfos
	 */
	public ErpOrderHistory(Collection erpsaleInfos){
		this.erpSaleInfos = erpsaleInfos;
	}

	public Collection getErpSaleInfos() {
		return this.erpSaleInfos;
	}
	
	public int getDeliveredOrderCount(){
		return ErpOrderHistoryUtil.getDeliveredOrderCount(this.erpSaleInfos);
	}

	public Date getFirstNonPickupOrderDate(){
		return ErpOrderHistoryUtil.getFirstNonPickupOrderDate(this.erpSaleInfos);
	}

	public Date getLastOrderCreateDate(){
		return ErpOrderHistoryUtil.getLastOrderCreateDate(this.erpSaleInfos);
	}

	public Date getLastOrderDlvDate(){
		return ErpOrderHistoryUtil.getLastOrderDlvDate(this.erpSaleInfos);
	}

	public String getLastOrderId(){
		return ErpOrderHistoryUtil.getLastOrderId(this.erpSaleInfos);
	}

	public EnumDeliveryType getLastOrderType(){
		return ErpOrderHistoryUtil.getLastOrderType(this.erpSaleInfos);
	}

	public String getLastOrderZone(){
		return ErpOrderHistoryUtil.getLastOrderZone(this.erpSaleInfos);
	}

	public int getPhoneOrderCount(){
		return ErpOrderHistoryUtil.getPhoneOrderCount(this.erpSaleInfos);
	}

	public int getReturnOrderCount(){
		return ErpOrderHistoryUtil.getReturnOrderCount(this.erpSaleInfos);
	}

	public String getSecondToLastSaleId(){
		return ErpOrderHistoryUtil.getSecondToLastSaleId(erpSaleInfos);
	}

	public int getTotalOrderCount(){
		return ErpOrderHistoryUtil.getTotalOrderCount(this.erpSaleInfos);
	}

	public int getValidECheckOrderCount(){
		return ErpOrderHistoryUtil.getValidECheckOrderCount(this.erpSaleInfos);
	}

	public int getValidOrderCount(){
		return ErpOrderHistoryUtil.getValidOrderCount(this.erpSaleInfos);
	}

	public int getValidPhoneOrderCount(){
		return ErpOrderHistoryUtil.getValidPhoneOrderCount(this.erpSaleInfos);
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
		return buf.toString();
	}
}