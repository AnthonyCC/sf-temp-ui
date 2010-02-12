/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.Date;
import java.util.Iterator;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.sap.PosexUtil;
import com.freshdirect.sap.SapOrderLineI;
import com.freshdirect.sap.bapi.BapiOrder;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class OrderLineHelper {
	private final String webOrderNumber;
	private final Date requestedDate;
	private final SapOrderLineI[] orderLines;
	
	public OrderLineHelper(String webOrderNumber, Date requestedDate, SapOrderLineI[] orderLines) {
		this.webOrderNumber = webOrderNumber;
		this.requestedDate = requestedDate;
		this.orderLines = orderLines;
	}
	
	public void buildOrderLines(BapiOrder bapi, final boolean composite) throws SapException {
		
		if (orderLines.length > ErpServicesProperties.getCartOrderLineLimit()) 
			throw new SapException("Order line limit of " + ErpServicesProperties.getCartOrderLineLimit() +
					" exceeded (" + orderLines.length );
		
		final Date currDate = new Date();

		for( int i=0; i < this.orderLines.length; i++) {
			final SapOrderLineI orderline = this.orderLines[i];
			final int pos = i;

			BapiOrder.OrderItemIn item = new BapiOrder.OrderItemIn() {
				public String getMaterialNo() { return orderline.getMaterialNumber(); }
				public String getPurchNo() { return webOrderNumber; }
				public String getPoItemNo() { return PosexUtil.getPosex(pos); }
				public int getItemNumber() { return PosexUtil.getPosexInt(pos); }
				public Date getPurchDate() { return currDate; }
				public Date getPriceDate() { return currDate; }
				public Double getReqQty() { return composite ? new Double(orderline.getQuantity()) : null; }
				public String getSalesUnit() { return orderline.getSalesUnit(); }
				public int getDeliveryGroup() { return 0; }
				public int getPoItemNoS() { return orderline.getDeliveryGroup(); }
				public int getMaxPartialDlv() { return 0; }
				public String getCustMat35() { return orderline.getDepartmentDesc(); }
				public String getSalesDist(){ return orderline.getPricingZoneId();}
			};

			// order item in
			bapi.addOrderItemIn( item );

			BapiOrder.ScheduleIn schedule = new BapiOrder.ScheduleIn() {
				public int getItemNumber() { return PosexUtil.getPosexInt(pos); }
				public double getReqQty() { return getQuantity(orderline); }
				public Date getReqDate() { return requestedDate; }
				public Date getTPDate() { return composite ? requestedDate : null; }
				public Date getMSDate() { return composite ? requestedDate : null; }
				public Date getLoadDate() { return composite ? requestedDate : null; }
				public Date getGIDate() { return composite ? requestedDate : null; }
			};
			
			// schedule
			bapi.addSchedule(schedule);

		
			String suChar = orderline.getSalesUnitCharacteristic();
			String qtyChar = orderline.getQuantityCharacteristic();
		
			if ( orderline.getOptions().size()!=0 ||
				(suChar!=null && suChar.length()!=0) ||
				(qtyChar!=null && qtyChar.length()!=0) ) {

				// it's configured
				bapi.addCfgsRef(
					PosexUtil.getLongPosex(i),
					PosexUtil.getPosex(i),
					PosexUtil.getPosex(i) );

				bapi.addCfgsInst(
					PosexUtil.getPosex(i),
					PosexUtil.getLongPosex(i),
					orderline.getMaterialNumber(),
					orderline.getQuantity() );

				this.buildCfgsValue(bapi, orderline, PosexUtil.getPosex(i), PosexUtil.getLongPosex(i) );

			}
		}
	}
	
	protected double getQuantity(SapOrderLineI orderline) {
		return orderline.getQuantity();
	}

	protected void buildCfgsValue(BapiOrder bapi, SapOrderLineI orderLine, String configID, String instID) {
		for (Iterator i = orderLine.getOptions().keySet().iterator(); i.hasNext(); ) {
			String vcName = (String)i.next();
			String vcValue = (String)orderLine.getOptions().get(vcName);
			bapi.addCfgsValue(vcName, vcValue, configID, instID);
		}
		
		String s = orderLine.getSalesUnitCharacteristic();
		if (s!=null && s.length()!=0) {
			bapi.addCfgsValue(s, orderLine.getSalesUnit(), configID, instID);
		}

		s = orderLine.getQuantityCharacteristic();
		if (s!=null && s.length()!=0) {
			String qty = String.valueOf( orderLine.getQuantity() );
			bapi.addCfgsValue(s, qty, configID, instID);
		}
	}


}