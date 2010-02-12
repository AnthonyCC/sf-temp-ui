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
public interface BapiOrder extends BapiFunctionI {
	
	public static interface OrderItemIn {
		public String getMaterialNo();
		public String getPurchNo();
		public String getPoItemNo();
		public int getPoItemNoS();
		public int getItemNumber();
		public Date getPurchDate();
		public Date getPriceDate();
		public Double getReqQty();
		public String getSalesUnit();
		public int getDeliveryGroup();
		public int getMaxPartialDlv();
		public String getCustMat35();
		public String getSalesDist();
	}
	
	public static interface ScheduleIn {
		public int getItemNumber();
		public double getReqQty();
		public Date getReqDate();
		public Date getTPDate();
		public Date getMSDate();
		public Date getLoadDate();
		public Date getGIDate();
	}

	//public void addCreditCard(String ccType, String ccNumber, Date ccValidTo, String ccName);

	public void addExtension(String structure, String valuePart1);

	public void addCondition(int posex, String condType, double condValue, String currency);
	public void addCondition(int posex, String condType, double condValue, String currency, double condPerUnit, String condUnit);
	
	public void addPartner(String partnerRole, String partnerNumber);
	public void addPartner(String partnerRole, String partnerNumber, String name, String name2, String street, String city, String zip, String region, String country, String phoneNumber);

	public void addOrderItemIn(OrderItemIn itemIn);

	public void addSchedule(ScheduleIn schedule);
	public void addCfgsValue(String configId, String instId, String vcName, String vcValue);
	public void addCfgsRef(String rootId, String configId, String posex);
	public void addCfgsInst(String configId, String instId, String objKey, double quantity);

	public void addOrderText(int posex, String textId, String text);


}
