/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import java.util.Properties;

import com.freshdirect.framework.util.ConfigHelper;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapProperties {

	private static Category LOGGER = LoggerFactory.getInstance(SapProperties.class);

	private final static String PROP_PLANT = "sap.bapi.plant";
	private final static String PROP_SALESORG = "sap.bapi.salesOrg";
	private final static String PROP_DISTRCHAN = "sap.bapi.distrChan";
	private final static String PROP_DIVISION = "sap.bapi.division";
	private final static String PROP_STGELOC = "sap.bapi.stgeLoc";
	private final static String PROP_REFCUSTOMER = "sap.bapi.refCustomer";
	private final static String PROP_REFCUSTOMER_COS = "sap.bapi.refCustomer.cos";
	private final static String PROP_BLACKHOLE = "sap.blackhole";
	private final static String PROP_FUNCTION_CARTONINFO = "sap.function.cartoninfo";
	
	//Multi plant interface logging properties
	private final static String PROP_MATERIAL_GLOBAL_EXPORT_LOG = "sap.material.global.export.log";
	private final static String PROP_MATERIAL_PLANT_EXPORT_LOG = "sap.material.plant.export.log";
	private final static String PROP_MATERIAL_UOM_EXPORT_LOG = "sap.material.uom.export.log";
	private final static String PROP_MATERIAL_PRICE_EXPORT_LOG = "sap.material.price.export.log";
	private final static String PROP_INVOICE_EXPORT_LOG = "sap.invoice.export.log";
	private final static String PROP_INVENTORY_EXPORT_LOG = "sap.inventory.export.log";
	private final static String PROP_COOL_INFO_EXPORT_LOG = "sap.cool.info.export.log";
	private final static String PROP_GROUP_SCALE_EXPORT_LOG = "sap.group.scale.export.log";
	
	private static Properties config;

	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;

	private final static Properties defaults = new Properties();
	static {

		defaults.put(PROP_PLANT, "1000");
		defaults.put(PROP_SALESORG, "0001");
		defaults.put(PROP_DISTRCHAN, "01");
		defaults.put(PROP_DIVISION, "01");
		defaults.put(PROP_STGELOC, "1000");
		defaults.put(PROP_REFCUSTOMER, "0001000000");
		defaults.put(PROP_REFCUSTOMER_COS, "0001000001");
		defaults.put(PROP_BLACKHOLE, "false");
		defaults.put(PROP_FUNCTION_CARTONINFO, "ZWM_CARTONCOUNT_BYORDERS");
		defaults.put(PROP_MATERIAL_GLOBAL_EXPORT_LOG, "false");
		defaults.put(PROP_MATERIAL_PLANT_EXPORT_LOG, "false");
		defaults.put(PROP_MATERIAL_UOM_EXPORT_LOG, "false");
		defaults.put(PROP_MATERIAL_PRICE_EXPORT_LOG, "false");
		defaults.put(PROP_INVOICE_EXPORT_LOG, "false");
		defaults.put(PROP_INVENTORY_EXPORT_LOG, "true");
		defaults.put(PROP_COOL_INFO_EXPORT_LOG, "false");
		defaults.put(PROP_GROUP_SCALE_EXPORT_LOG, "false");
		
		refresh();
	}

	private SapProperties() {
	}

	private synchronized static void refresh() {
		long t = System.currentTimeMillis();
		if (t - lastRefresh > REFRESH_PERIOD) {
			config = ConfigHelper.getPropertiesFromClassLoader("erpservices.properties", defaults);
			lastRefresh = t;
			LOGGER.info("Loaded configuration from erpservices.properties: " + config);
		}
	}

	private static String get(String key) {
		refresh();
		return config.getProperty(key);
	}

	public static boolean isBlackhole() {
		return Boolean.valueOf(get(PROP_BLACKHOLE)).booleanValue();
	}

	public static String getPlant() {
		return get(PROP_PLANT);
	}

	public static String getSalesOrg() {
		return get(PROP_SALESORG);
	}

	public static String getDistrChan() {
		return get(PROP_DISTRCHAN);
	}

	public static String getDivision() {
		return get(PROP_DIVISION);
	}

	public static String getStgeLoc() {
		return get(PROP_STGELOC);
	}

	public static String getRefCustomer() {
		return get(PROP_REFCUSTOMER);
	}

	public static String getRefCustomerCOS() {
		return get(PROP_REFCUSTOMER_COS);
	}
	
	public static String getCartonInfoFunctionName() {
		return config.getProperty(PROP_FUNCTION_CARTONINFO);
	}
	
	
	public static boolean isMaterialGlobalExportLogEnabled() {
		return Boolean.valueOf(get(PROP_MATERIAL_GLOBAL_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isMaterialPlantExportLogEnabled() {
		return Boolean.valueOf(get(PROP_MATERIAL_PLANT_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isMaterialPriceExportLogEnabled() {
		return Boolean.valueOf(get(PROP_MATERIAL_PRICE_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isMaterialUOMExportLogEnabled() {
		return Boolean.valueOf(get(PROP_MATERIAL_UOM_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isInventoryExportLogEnabled() {
		return Boolean.valueOf(get(PROP_INVENTORY_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isInvoiceExportLogEnabled() {
		return Boolean.valueOf(get(PROP_INVOICE_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isCoolInfoExportLogEnabled() {
		return Boolean.valueOf(get(PROP_COOL_INFO_EXPORT_LOG)).booleanValue();
	}
	
	public static boolean isGroupScaleExportLogEnabled() {
		return Boolean.valueOf(get(PROP_GROUP_SCALE_EXPORT_LOG)).booleanValue();
	}
}
