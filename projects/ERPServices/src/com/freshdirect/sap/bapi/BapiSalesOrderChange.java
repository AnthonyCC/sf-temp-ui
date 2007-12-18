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
public interface BapiSalesOrderChange extends BapiOrder {

	public static interface OrderHeaderIn {
		public String getCollectiveNo();
		public String getCustGrp1();
		public String getRef1();
		public String getRef1S();

		public String getPoSupplement();
		public Date getDunDate();
		public String getName();
		public String getPurchNoS();

		public Date getRequestedDate();
		
		public String getTelephone();
		public String getCustGrp3();
		public String getCustGrp2();
		
		public String getOrdReason();
		
	}

	public void setOrderHeaderIn(OrderHeaderIn hdr);
	
	public void setSalesDocumentNumber(String salesDocumentNumber);

	public void addOrderItemInX( String itmNumber );
	public void addOrderScheduleInX( int itmNumber );


}
