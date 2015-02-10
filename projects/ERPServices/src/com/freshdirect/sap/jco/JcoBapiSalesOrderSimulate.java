/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiSalesOrderSimulate;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderSimulate extends JcoBapiOrder implements BapiSalesOrderSimulate {

	private OrderItemOut[] orderItemOut;
	private OrderScheduleEx[] orderScheduleEx;

	public JcoBapiSalesOrderSimulate(boolean composite) throws JCoException
	{
		super(composite ? "Z_BAPI_COMPOSITE_SIMULATE" : "Z_BAPI_SALESORDER_SIMULATE");
	}

	public void setOrderHeaderIn(OrderHeaderIn hdr)
	{
		JCoStructure orderHeaderIn = this.function.getImportParameterList().getStructure("ORDER_HEADER_IN");
		
		orderHeaderIn.setValue("DISTR_CHAN", hdr.getDistrChan());
		orderHeaderIn.setValue("DIVISION", hdr.getDivision());
		orderHeaderIn.setValue("DOC_TYPE", hdr.getDocumentType());
		orderHeaderIn.setValue("SALES_ORG", hdr.getSalesOrg());

		orderHeaderIn.setValue("PURCH_NO_C", hdr.getPurchaseOrderNumber());

		// requested date
		orderHeaderIn.setValue("REQ_DATE_H", hdr.getRequestedDate());
		orderHeaderIn.setValue("PO_DAT_S", hdr.getRequestedDate());

		// pricing date
		orderHeaderIn.setValue("PRICE_DATE", hdr.getPricingDate());

		// current date
		orderHeaderIn.setValue("PURCH_DATE", hdr.getCurrentDate());
	}

	protected String getOrderItemInName() {
		return "ORDER_ITEMS_IN";
	}

	protected String getOrderScheduleInName() {
		return "ORDER_SCHEDULE_IN";
	}

	protected String getOrderPartnersName() {
		return "ORDER_PARTNERS";
	}

	protected String getOrderConditionsInName() {
		return "ORDER_CONDITIONS_IN";
	}

	public void processResponse() throws BapiException {
		super.processResponse();

		JCoTable jcoItemsOut = function.getTableParameterList().getTable("ORDER_ITEMS_OUT");

		this.orderItemOut = new OrderItemOut[jcoItemsOut.getNumRows()];
		jcoItemsOut.firstRow();
		for (int i = 0; i < this.orderItemOut.length; i++) {
			final int itemNumber = jcoItemsOut.getInt("ITM_NUMBER");
			final String poItemNo = jcoItemsOut.getString("PO_ITM_NO");
			final String material = jcoItemsOut.getString("MATERIAL");
			final int hgLvItem = jcoItemsOut.getInt("HG_LV_ITEM");

			this.orderItemOut[i] = new OrderItemOut() {
				public int getItemNumber() {
					return itemNumber;
				}
				public String getPOItemNo() {
					return poItemNo;
				}
				public String getMaterial() {
					return material;
				}
				public int getHighLevelItem() {
					return hgLvItem;
				}
			};
			jcoItemsOut.nextRow();
		}

		JCoTable jcoOrderSchedule = function.getTableParameterList().getTable("ORDER_SCHEDULE_EX");
		this.orderScheduleEx = new OrderScheduleEx[jcoOrderSchedule.getNumRows()];
		jcoOrderSchedule.firstRow();
		for (int i = 0; i < this.orderScheduleEx.length; i++) {

			final int itemNumber = jcoOrderSchedule.getInt("ITM_NUMBER");
			final double confirmedQty = jcoOrderSchedule.getDouble("CONFIR_QTY");
			final Date reqDate = jcoOrderSchedule.getDate("REQ_DATE");

			this.orderScheduleEx[i] = new OrderScheduleEx() {
				public int getItemNumber() {
					return itemNumber;
				}
				public double getConfirmedQty() {
					return confirmedQty;
				}
				public Date getReqDate() {
					return reqDate;
				}
			};

			jcoOrderSchedule.nextRow();
		}
	}
	
	public int getOrderItemOutSize() {
		return this.orderItemOut.length;
	}

	public OrderItemOut getOrderItemOut(int index) {
		return this.orderItemOut[index];
	}

	public int getOrderScheduleExSize() {
		return this.orderScheduleEx.length;
	}

	public OrderScheduleEx getOrderScheduleEx(int index) {
		return this.orderScheduleEx[index];
	}

}
