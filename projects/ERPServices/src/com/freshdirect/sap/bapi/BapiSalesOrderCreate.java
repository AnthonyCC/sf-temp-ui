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
public interface BapiSalesOrderCreate extends BapiOrder {

	public static interface OrderHeaderIn {
		public String getSalesOrg();
		public String getDistrChan();
		public String getDivision();
		public String getDocumentType();

		public String getPurchaseOrderNumber();
		public String getCollectiveNo();
		public String getCustGrp1();
		public String getRef1();
		public String getRef1S();

		public String getPoSupplement();
		public Date getDunDate();
		public String getName();
		public String getPurchNoS();

		public Date getRequestedDate();
		public Date getPricingDate();
		public Date getCurrentDate();
		

		public String getTelephone();
		public String getCustGrp3();
		public String getCustGrp2();
		
		public String getOrdReason();
		
	}

	public void setOrderHeaderIn(OrderHeaderIn header);

	public String getSalesDocument();
	
}
