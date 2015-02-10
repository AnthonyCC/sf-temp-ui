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
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoStructure;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderCreate extends JcoBapiOrder implements BapiSalesOrderCreate {

	public JcoBapiSalesOrderCreate() throws JCoException {
			super("ZBAPI_SALESORDER_CREATEFROMDAT");
	}
	public JcoBapiSalesOrderCreate(String functionName ) throws JCoException {
		super(functionName);
	}

	public void setOrderHeaderIn(OrderHeaderIn hdr) {
		JCoStructure orderHeaderIn = this.function.getImportParameterList().getStructure("ORDER_HEADER_IN");
		
		orderHeaderIn.setValue("SALES_ORG", hdr.getSalesOrg());
		orderHeaderIn.setValue("DISTR_CHAN", hdr.getDistrChan());
		orderHeaderIn.setValue("DIVISION", hdr.getDivision());
		orderHeaderIn.setValue("DOC_TYPE", hdr.getDocumentType());

		orderHeaderIn.setValue("PURCH_NO_C", hdr.getPurchaseOrderNumber());

		// Collective number (Delivery zone/depot number)
		orderHeaderIn.setValue("COLLECT_NO", hdr.getCollectiveNo());

		// Customer group 1 (Delivery Model)
		orderHeaderIn.setValue("CUST_GRP1", hdr.getCustGrp1());

		// Customer's or vendor's internal reference
		orderHeaderIn.setValue("REF_1", hdr.getRef1());
		orderHeaderIn.setValue("REF_1_S", hdr.getRef1S());

		orderHeaderIn.setValue("PO_SUPPLEM", hdr.getPoSupplement());
		orderHeaderIn.setValue("DUN_DATE", hdr.getDunDate());
		orderHeaderIn.setValue("NAME", hdr.getName());
		orderHeaderIn.setValue("PURCH_NO_S", hdr.getPurchNoS());

		// requested date
		orderHeaderIn.setValue("REQ_DATE_H", hdr.getRequestedDate());
		orderHeaderIn.setValue("PO_DAT_S", hdr.getRequestedDate());

		// pricing date
		orderHeaderIn.setValue("PRICE_DATE", hdr.getPricingDate());

		// current date
		orderHeaderIn.setValue("DOC_DATE", hdr.getCurrentDate());
		orderHeaderIn.setValue("PURCH_DATE", hdr.getCurrentDate());
		orderHeaderIn.setValue("SERV_DATE", hdr.getCurrentDate());
		orderHeaderIn.setValue("WAR_DATE", hdr.getCurrentDate());
		
		//customer segmentation/rating
		orderHeaderIn.setValue("CUST_GRP2", hdr.getCustGrp2());
		orderHeaderIn.setValue("CUST_GRP3", hdr.getCustGrp3());
		orderHeaderIn.setValue("TELEPHONE", hdr.getTelephone());
		
		orderHeaderIn.setValue("ORD_REASON", hdr.getOrdReason());
	}

	public void addOrderItemIn(OrderItemIn item) {
		super.addOrderItemIn(item);
		this.orderItemIn.setValue("PRICE_DATE", item.getPriceDate());
		this.orderItemIn.setValue("MAX_PL_DLV", item.getMaxPartialDlv());
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