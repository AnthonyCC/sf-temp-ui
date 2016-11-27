/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import java.util.Date;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiSalesOrderSimulate extends BapiOrder {

	public static interface OrderHeaderIn {
		public String getSalesOrg();
		public String getDistrChan();
		public String getDivision();
		public String getDocumentType();

		public String getPurchaseOrderNumber();

		public Date getRequestedDate();
		public Date getPricingDate();
		public Date getCurrentDate();
	}

	public static interface OrderItemOut {
		public int getItemNumber();
		public String getPOItemNo();
		public String getMaterial();
		public int getHighLevelItem();
	}
	
	public static interface OrderScheduleEx {
		public int getItemNumber();
		public double getConfirmedQty();
		public Date getReqDate();
	}	

	public void setOrderHeaderIn(OrderHeaderIn header);


	public int getOrderItemOutSize();
	public OrderItemOut getOrderItemOut(int index);

	public int getOrderScheduleExSize();
	public OrderScheduleEx getOrderScheduleEx(int index);

}
