/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapOrderLineI;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.bapi.BapiSalesOrderSimulate;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class SalesOrderSimulateHelper extends SalesOrderHelper {

	private final BapiSalesOrderSimulate bapi;

	private final SapOrderI sapOrder;
	private final SapOrderLineI[] orderLines;
	private final boolean composite;

	public SalesOrderSimulateHelper(BapiSalesOrderSimulate bapi, SapOrderI sapOrder, SapOrderLineI[] orderLines, boolean composite) {
		super(bapi);
		this.sapOrder = sapOrder;
		this.orderLines = orderLines;
		this.bapi = bapi;
		this.composite = composite;
	}

	protected void build() throws SapException {
		this.buildOrderHeader();

		OrderLineHelper olh = new OrderLineHelper(sapOrder.getWebOrderNumber(), sapOrder.getRequestedDate(), this.orderLines);

		olh.buildOrderLines(bapi, composite);

		this.buildPartners(sapOrder);
		// this.buildCreditCard( sapOrder.getCustomer().getCreditCard() );
	}

	protected void buildOrderHeader() {
		final Date currentDate = new Date();

		this.bapi.setOrderHeaderIn(new BapiSalesOrderSimulate.OrderHeaderIn() {
			public String getSalesOrg() {
				return SapProperties.getSalesOrg();
			}

			public String getDistrChan() {
				return SapProperties.getDistrChan();
			}

			public String getDivision() {
				return SapProperties.getDivision();
			}

			public String getDocumentType() {
				return "YOR";
			}

			public String getPurchaseOrderNumber() {
				// dummy number
				return "1";
			}

			public Date getRequestedDate() {
				//return currentDate;
				return sapOrder.getRequestedDate();
			}

			public Date getPricingDate() {
				return currentDate;
			}

			public Date getCurrentDate() {
				return currentDate;
			}

		});
	}

	/**
	 * @return Map of Integer (ITM_NUMBER) -> List of ErpInventoryModel
	 */
	public static Map parseInventories(BapiSalesOrderSimulate bapi) {

		/** Map of Integer (ITM_NUMBER) -> List of ErpInventoryEntryModel */
		Map scheduleMap = new HashMap();
		for (int i = 0; i < bapi.getOrderScheduleExSize(); i++) {
			BapiSalesOrderSimulate.OrderScheduleEx schedule = bapi.getOrderScheduleEx(i);
			Integer itemNum = new Integer(schedule.getItemNumber());

			List schedules = (List) scheduleMap.get(itemNum);
			if (schedules == null) {
				schedules = new ArrayList();
				scheduleMap.put(itemNum, schedules);
			}

			schedules.add(new ErpInventoryEntryModel(DateUtil.truncate(schedule.getReqDate()), schedule.getConfirmedQty()));
		}

		Date now = new Date();

		Map inventoryMap = new HashMap(scheduleMap.size());
		for (Iterator i = scheduleMap.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			Integer itemNo = (Integer) e.getKey();
			List entries = (List) e.getValue();

			BapiSalesOrderSimulate.OrderItemOut orderItemOut = getOrderItemOut(bapi, itemNo);

			if (orderItemOut.getHighLevelItem() != 0) {
				itemNo = new Integer(orderItemOut.getHighLevelItem());
			}

			List inventories = (List) inventoryMap.get(itemNo);
			if (inventories == null) {
				inventories = new ArrayList();
				inventoryMap.put(itemNo, inventories);
			}

			ErpInventoryModel inv = new ErpInventoryModel(orderItemOut.getMaterial(), now, entries);
			inventories.add(inv);
		}

		return inventoryMap;
	}

	/**
	 * @return OrderItemOut by itemNumber, or null
	 */
	public static BapiSalesOrderSimulate.OrderItemOut getOrderItemOut(BapiSalesOrderSimulate bapi, Integer itemNumber) {
		for (int i = 0; i < bapi.getOrderItemOutSize(); i++) {
			BapiSalesOrderSimulate.OrderItemOut orderItemOut = bapi.getOrderItemOut(i);
			if (itemNumber.intValue() == orderItemOut.getItemNumber()) {
				return orderItemOut;
			}
		}
		return null;
	}

}