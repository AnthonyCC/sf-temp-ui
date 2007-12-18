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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.PosexUtil;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapOrderLineI;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiFunctionI;
import com.freshdirect.sap.bapi.BapiMaterialAvailability;
import com.freshdirect.sap.bapi.BapiSalesOrderSimulate;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCheckAvailability implements SapCommandI {

	private final static Category LOGGER = LoggerFactory.getInstance(SapCheckAvailability.class);

	private final static BapiPooledExecutor POOL = new BapiPooledExecutor();

	private final SapOrderI order;
	private final long timeout;

	public SapCheckAvailability(SapOrderI order, long timeout) {
		this.order = order;
		this.timeout = timeout;
	}

	public SapOrderI getOrder() {
		return this.order;
	}

	public void execute() throws SapException {

		AvailabilityBapiBuilder builder = new CompositeAvailabilityBuilder(order);

		for (int i = 0; i < order.numberOfOrderLines(); i++) {
			builder.add(order.getOrderLine(i));
		}

		List bapis = builder.getBapis();

		POOL.execute((BapiFunctionI[]) bapis.toArray(new BapiFunctionI[bapis.size()]), timeout);

		builder.process();

	}

	private static interface AvailabilityBapiBuilder {

		public void add(SapOrderLineI orderLine);

		/**
		 * @return List of BapiFunctionI
		 */
		public List getBapis() throws SapException;

		public void process();

	}

	private class CompositeAvailabilityBuilder implements AvailabilityBapiBuilder {

		/** Map of EnumAtpRule -> BapiBuilder */
		private final Map builders = new HashMap();

		public CompositeAvailabilityBuilder(SapOrderI order) {
			builders.put(EnumATPRule.MATERIAL, new MatAvBapiBuilder());
			builders.put(EnumATPRule.SIMULATE, new SimulateBapiBuilder(false, order));
			builders.put(EnumATPRule.COMPONENT, new SimulateBapiBuilder(true, order));
		}

		public void add(SapOrderLineI orderLine) {
			AvailabilityBapiBuilder builder = (AvailabilityBapiBuilder) builders.get(orderLine.getAtpRule());
			if (builder != null) {
				builder.add(orderLine);
			}
		}

		public List getBapis() throws SapException {
			List bapis = new ArrayList();
			for (Iterator i = builders.values().iterator(); i.hasNext();) {
				AvailabilityBapiBuilder builder = (AvailabilityBapiBuilder) i.next();
				bapis.addAll(builder.getBapis());
			}
			return bapis;
		}

		public void process() {
			for (Iterator i = builders.values().iterator(); i.hasNext();) {
				AvailabilityBapiBuilder builder = (AvailabilityBapiBuilder) i.next();
				builder.process();
			}
		}

	}

	private class MatAvBapiBuilder implements AvailabilityBapiBuilder {

		private final List orderLines = new ArrayList();
		private List helpers;
		private List bapis;

		public void add(SapOrderLineI orderLine) {
			orderLines.add(orderLine);
		}

		public List getBapis() {
			if (orderLines.isEmpty()) {
				return Collections.EMPTY_LIST;
			}

			helpers = new ArrayList(orderLines.size());
			bapis = new ArrayList(orderLines.size());
			for (Iterator i = orderLines.iterator(); i.hasNext();) {
				SapOrderLineI orderLine = (SapOrderLineI) i.next();

				BapiMaterialAvailability bapi = BapiFactory.getInstance().getMaterialAvailabilityBuilder();
				bapis.add(bapi);

				MaterialAvailabilityHelper helper = new MaterialAvailabilityHelper(bapi, order, orderLine);
				helper.build();
				helpers.add(helper);
			}
			return bapis;
		}

		public void process() {
			if (orderLines.isEmpty()) {
				return;
			}
			int idx = 0;
			for (Iterator i = bapis.iterator(); i.hasNext(); idx++) {
				BapiMaterialAvailability bapi = (BapiMaterialAvailability) i.next();
				if (bapi.isFinished()) {
					MaterialAvailabilityHelper helper = (MaterialAvailabilityHelper) helpers.get(idx);
					helper.process(bapi);
				} else {
					LOGGER.debug("ATP " + i + " result skipped, not finished");
				}
			}
		}

	}

	private static class SimulateBapiBuilder implements AvailabilityBapiBuilder {

		private final boolean composite;
		private final SapOrderI order;
		private final List orderLines = new ArrayList();
		private SalesOrderSimulateHelper helper = null;
		private BapiSalesOrderSimulate bapi;

		public SimulateBapiBuilder(boolean composite, SapOrderI order) {
			this.composite = composite;
			this.order = order;
		}

		public void add(SapOrderLineI orderLine) {
			orderLines.add(orderLine);
		}

		public List getBapis() throws SapException {
			if (orderLines.isEmpty()) {
				return Collections.EMPTY_LIST;
			}

			SapOrderLineI[] arr = (SapOrderLineI[]) orderLines.toArray(new SapOrderLineI[orderLines.size()]);

			bapi = composite ? BapiFactory.getInstance().getCompositeSimulateBuilder() : BapiFactory
				.getInstance()
				.getSalesOrderSimulateBuilder();
			helper = new SalesOrderSimulateHelper(bapi, order, arr, composite);
			helper.build();

			List bapis = new ArrayList(1);
			bapis.add(bapi);
			return bapis;
		}

		public void process() {
			if (orderLines.isEmpty()) {
				return;
			}

			if (bapi.isFinished()) {
				Map inventoriesMap = SalesOrderSimulateHelper.parseInventories(bapi);
				LOGGER.debug("Found inventories: " + inventoriesMap);
				processInventories(inventoriesMap);
			}
		}

		protected void processInventories(Map inventoriesMap) {

			for (Iterator i = inventoriesMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				Integer itemNo = (Integer) e.getKey();
				List inventories = (List) e.getValue();

				BapiSalesOrderSimulate.OrderItemOut orderItemOut = SalesOrderSimulateHelper.getOrderItemOut(bapi, itemNo);
				int index = PosexUtil.getIndexFromPosex(orderItemOut.getPOItemNo());

				((SapOrderLineI) orderLines.get(index)).setInventories(inventories);
			}
		}
	}

}