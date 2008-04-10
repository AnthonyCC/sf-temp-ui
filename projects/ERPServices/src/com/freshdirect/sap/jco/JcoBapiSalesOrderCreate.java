/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSalesOrderCreate;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderCreate extends JcoBapiOrder implements BapiSalesOrderCreate {

	public JcoBapiSalesOrderCreate() {
			super("ZBAPI_SALESORDER_CREATEFROMDAT");
	}
	public JcoBapiSalesOrderCreate(String functionName ) {
		super(functionName);
	}

	public void setOrderHeaderIn(OrderHeaderIn hdr) {
		JCO.Structure orderHeaderIn = this.function.getImportParameterList().getStructure("ORDER_HEADER_IN");
		orderHeaderIn.setValue(hdr.getSalesOrg(), "SALES_ORG");
		orderHeaderIn.setValue(hdr.getDistrChan(), "DISTR_CHAN");
		orderHeaderIn.setValue(hdr.getDivision(), "DIVISION");
		orderHeaderIn.setValue(hdr.getDocumentType(), "DOC_TYPE");

		orderHeaderIn.setValue(hdr.getPurchaseOrderNumber(), "PURCH_NO_C");

		// Collective number (Delivery zone/depot number)
		orderHeaderIn.setValue(hdr.getCollectiveNo(), "COLLECT_NO");

		// Customer group 1 (Delivery Model)
		orderHeaderIn.setValue(hdr.getCustGrp1(), "CUST_GRP1");

		// Customer's or vendor's internal reference
		orderHeaderIn.setValue(hdr.getRef1(), "REF_1");
		orderHeaderIn.setValue(hdr.getRef1S(), "REF_1_S");

		orderHeaderIn.setValue(hdr.getPoSupplement(), "PO_SUPPLEM");
		orderHeaderIn.setValue(hdr.getDunDate(), "DUN_DATE");
		orderHeaderIn.setValue(hdr.getName(), "NAME");
		orderHeaderIn.setValue(hdr.getPurchNoS(), "PURCH_NO_S");

		// requested date
		orderHeaderIn.setValue(hdr.getRequestedDate(), "REQ_DATE_H");
		orderHeaderIn.setValue(hdr.getRequestedDate(), "PO_DAT_S");

		// pricing date
		orderHeaderIn.setValue(hdr.getPricingDate(), "PRICE_DATE");

		// current date
		orderHeaderIn.setValue(hdr.getCurrentDate(), "DOC_DATE");
		orderHeaderIn.setValue(hdr.getCurrentDate(), "PURCH_DATE");
		orderHeaderIn.setValue(hdr.getCurrentDate(), "SERV_DATE");
		orderHeaderIn.setValue(hdr.getCurrentDate(), "WAR_DATE");
		
		//customer segmentation/rating
		orderHeaderIn.setValue( hdr.getCustGrp2(), "CUST_GRP2");
		orderHeaderIn.setValue( hdr.getCustGrp3(), "CUST_GRP3");
		orderHeaderIn.setValue( hdr.getTelephone(), "TELEPHONE");
		
		orderHeaderIn.setValue( hdr.getOrdReason(), "ORD_REASON");
	}

	public void addOrderItemIn(OrderItemIn item) {
		super.addOrderItemIn(item);
		this.orderItemIn.setValue(item.getPriceDate(), "PRICE_DATE");
		this.orderItemIn.setValue(item.getMaxPartialDlv(), "MAX_PL_DLV");
	}

	protected String getOrderItemInName() {
		return "ORDER_ITEMS_IN";
	}

	protected String getOrderScheduleInName() {
		return "ORDER_SCHEDULES_IN";
	}

	protected String getOrderPartnersName() {
		return "ORDER_PARTNERS";
	}

	protected String getOrderConditionsInName() {
		return "ORDER_CONDITIONS_IN";
	}


	public String getSalesDocument() {
		return function.getExportParameterList().getString("SALESDOCUMENT");
	}
	
	public String getInvoiceNumber() {
		return function.getExportParameterList().getString("INVOICE");
	}


}