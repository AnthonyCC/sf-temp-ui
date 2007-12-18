/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSalesOrderChange;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderChange extends JcoBapiOrder implements BapiSalesOrderChange {

	private JCO.Table orderItemInX;
	private JCO.Table schedulesInX;

	public JcoBapiSalesOrderChange() {
		super("Z_BAPI_SALESORDER_CHANGE");

		// X (change) structures
		this.function.getImportParameterList().getStructure("ORDER_HEADER_INX").setValue("U", "UPDATEFLAG");
		this.orderItemInX = this.function.getTableParameterList().getTable("ORDER_ITEM_INX");
		this.schedulesInX = this.function.getTableParameterList().getTable("SCHEDULE_LINESX");
	}

	public void setOrderHeaderIn(OrderHeaderIn hdr) {
		JCO.Structure orderHeaderIn = this.function.getImportParameterList().getStructure("ORDER_HEADER_IN");
		JCO.Structure orderHeaderInX = this.function.getImportParameterList().getStructure("ORDER_HEADER_INX");

		// Collective number (Delivery zone/depot number)
		orderHeaderIn.setValue(hdr.getCollectiveNo(), "COLLECT_NO");
		orderHeaderInX.setValue("X", "COLLECT_NO");

		// Customer group 1 (Delivery Model)
		orderHeaderIn.setValue(hdr.getCustGrp1(), "CUST_GRP1");
		orderHeaderInX.setValue("X", "CUST_GRP1");

		// Customer's or vendor's internal reference
		orderHeaderIn.setValue(hdr.getRef1(), "REF_1");
		orderHeaderInX.setValue("X", "REF_1");
		
		orderHeaderInX.setValue("X", "REF_1_S");
		orderHeaderIn.setValue(hdr.getRef1S(), "REF_1_S");

		orderHeaderIn.setValue(hdr.getPoSupplement(), "PO_SUPPLEM");
		orderHeaderInX.setValue("X", "PO_SUPPLEM");
		orderHeaderIn.setValue(hdr.getDunDate(), "DUN_DATE");
		orderHeaderInX.setValue("X", "DUN_DATE");
		orderHeaderIn.setValue(hdr.getName(), "NAME");
		orderHeaderInX.setValue("X", "NAME");
		orderHeaderIn.setValue(hdr.getPurchNoS(), "PURCH_NO_S");
		orderHeaderInX.setValue("X", "PURCH_NO_S");

		// requested date
		orderHeaderIn.setValue(hdr.getRequestedDate(), "REQ_DATE_H");
		orderHeaderInX.setValue("X", "REQ_DATE_H");
		orderHeaderIn.setValue(hdr.getRequestedDate(), "PO_DAT_S");
		orderHeaderInX.setValue("X", "PO_DAT_S");

		//customer segmentation/rating
		orderHeaderIn.setValue( hdr.getCustGrp2(), "CUST_GRP2");
		orderHeaderInX.setValue("X", "CUST_GRP2");
		orderHeaderIn.setValue( hdr.getCustGrp3() ,"CUST_GRP3");
		orderHeaderInX.setValue("X", "CUST_GRP3");
		orderHeaderIn.setValue( hdr.getTelephone(),"TELEPHONE");
		orderHeaderInX.setValue("X", "TELEPHONE");
		
		orderHeaderIn.setValue( hdr.getOrdReason(), "ORD_REASON");
		orderHeaderInX.setValue("X", "ORD_REASON");

	}

	public void setSalesDocumentNumber(String salesDocumentNumber) {
		this.function.getImportParameterList().setValue(salesDocumentNumber, "SALESDOCUMENT");
	}

	public void addOrderItemIn(OrderItemIn item) {
		super.addOrderItemIn(item);
		this.orderItemIn.setValue(item.getPriceDate(), "PRICE_DATE");
		this.orderItemIn.setValue(item.getMaxPartialDlv(), "MAX_PL_DLV");
	}

	public void addOrderItemInX(String itmNumber) {
		this.orderItemInX.appendRow();
		this.orderItemInX.setValue(itmNumber, "ITM_NUMBER");
		this.orderItemInX.setValue("I", "UPDATEFLAG"); // insert
		this.orderItemInX.setValue("X", "PO_ITM_NO");
		this.orderItemInX.setValue("X", "POITM_NO_S");
		this.orderItemInX.setValue("X", "MATERIAL");
		this.orderItemInX.setValue("X", "PURCH_NO_S");
		this.orderItemInX.setValue("X", "PO_DAT_S");
		this.orderItemInX.setValue("X", "PRICE_DATE");
		this.orderItemInX.setValue("X", "SALES_UNIT");
		this.orderItemInX.setValue("X", "CUST_MAT35");
	}

	public void addOrderScheduleInX(int itmNumber) {
		this.schedulesInX.appendRow();
		this.schedulesInX.setValue("I", "UPDATEFLAG"); // insert
		this.schedulesInX.setValue(itmNumber, "ITM_NUMBER");	// association key with order_items_in
		this.schedulesInX.setValue("X", "REQ_QTY");
		this.schedulesInX.setValue("X", "REQ_DATE");
	}

	protected String getOrderItemInName() {
		return "ORDER_ITEM_IN";
	}

	protected String getOrderScheduleInName() {
		return "SCHEDULE_LINES";
	}

	protected String getOrderPartnersName() {
		return "PARTNERS";
	}

	protected String getOrderConditionsInName() {
		return "CONDITIONS_IN";
	}

}
