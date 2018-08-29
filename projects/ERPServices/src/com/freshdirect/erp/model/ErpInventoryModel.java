/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.common.date.SimpleDateDeserializer;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpInventory model class.
 *
 * @version $Revision$
 * @author $Author$
 * @stereotype fd-model
 */
public class ErpInventoryModel extends ModelSupport {
	private static final long serialVersionUID = -2646485627750337690L;

	/** SAP unique material number */
	private String sapId;

	/** Date of last inventory update */
	private Date lastUpdated;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date inventoryStartDate;
	EnumEStoreId enumStoreId = EnumEStoreId.FD;

	public Date getInventoryStartDate() {
		return inventoryStartDate;
	}

	/**
	 * Inventory entries
	 * 
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpInventoryEntryModel}>
	 */
	// private List<ErpInventoryEntryModel> entries;

	private Map<String, Map<java.util.Date, Double>> inventoryEntryMap = new HashMap<String, Map<java.util.Date, Double>>();

	/**
	 * Constructor with all properties.
	 *
	 * @param sapId
	 *            SAP unique material number
	 * @param entries
	 *            collection of ErpInventoryEntryModel objects
	 */
	public ErpInventoryModel(@JsonProperty("sapId") String sapId, @JsonProperty("lastUpdated") Date lastUpdated,
			@JsonProperty("entries") List<ErpInventoryEntryModel> entries) {
		this.sapId = sapId;
		this.lastUpdated = lastUpdated;
		// this.entries = new ArrayList<ErpInventoryEntryModel>( entries );
		inventoryEntryMap = convertListToMap(entries);// dh
		if (entries.size() > 0) {
			Collections.<ErpInventoryEntryModel> sort(entries);
			this.inventoryStartDate = entries.get(0).getStartDate();
		} else {
			this.inventoryStartDate = new Date();
		}
		// if(this.entries.size() > 0)
		// this.inventoryStartDate = this.entries.get(0).getStartDate();
		// else
		// this.inventoryStartDate = new Date();
		// this.entries = Collections.unmodifiableList( this.entries );
	}

	/**
	 * Get SAP Material ID.
	 *
	 * @return SAP ID
	 */
	public String getSapId() {
		return this.sapId;
	}

	/**
	 * Get the date the inventory was last refreshed on.
	 *
	 * @return date of last inventory update
	 */
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	/**
	 * Get all inventory entries.
	 *
	 * @return collection of ErpInventoryEntryModel objects
	 */
	public List<ErpInventoryEntryModel> getEntries() {
		return convertMapToList(this.inventoryEntryMap);
		// return this.entries;
	}

	/**
	 * returns the map of date/qty entries for a given plant.
	 * 
	 * @param plantId,
	 *            if blank or null defaults to 1000 (NY)
	 * @return List<ErpInventoryEntryModel> NOTE! if plant is not found, this
	 *         will return empty list.
	 */
	public List<ErpInventoryEntryModel> getInventoryPositionsforPlantid(String plantId) {
		if ((null == plantId) || plantId.isEmpty()) {

			plantId = FDStoreProperties.getDefaultFdPlantID();
			// TODO need to determine FreshDirect of FK to get better results
			// (FK requires 1300 not 1000)
		} // default to NY if blank or null.

		List<ErpInventoryEntryModel> entryModelLst = new ArrayList<ErpInventoryEntryModel>();
		if (this.inventoryEntryMap.containsKey(plantId)) {
			Iterator<Entry<Date, Double>> dateQtyIter = this.inventoryEntryMap.get(plantId).entrySet().iterator();
			while (dateQtyIter.hasNext()) {
				Map.Entry<Date, Double> dateQtyPair = dateQtyIter.next();
				entryModelLst.add(new ErpInventoryEntryModel(dateQtyPair.getKey(), dateQtyPair.getValue(), plantId));
			}
			Collections.<ErpInventoryEntryModel> sort(entryModelLst);
		}
		return entryModelLst;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("ErpInventoryModel[");
		buf.append(sapId);
		for (ErpInventoryEntryModel e : this.getEntries()) {
			buf.append("\n\t").append(e);
		}
		buf.append("\n]");
		return buf.toString();
	}

	public java.util.List<ErpInventoryEntryModel> convertMapToList(
			Map<String, Map<java.util.Date, Double>> inventoryEntryMap) {
		java.util.List<ErpInventoryEntryModel> entryModelLst = new ArrayList<ErpInventoryEntryModel>();

		Iterator<Entry<String, Map<Date, Double>>> inventoryEntryMapIter = inventoryEntryMap.entrySet().iterator();

		while (inventoryEntryMapIter.hasNext()) {
			Map.Entry<String, Map<Date, Double>> plantTuplePair = inventoryEntryMapIter.next();
			// String plant = pair.getKey() ;
			// Map <java.util.Date, Double> invByDateMap= pair.getValue();
			Iterator<Entry<Date, Double>> dateQtyIter = plantTuplePair.getValue().entrySet().iterator();
			while (dateQtyIter.hasNext()) {
				Map.Entry<Date, Double> dateQtyPair = dateQtyIter.next();
				// System.out.println( String.format(" plant: %s, date: %s, qty:
				// %s", pair.getKey(), pair2.getKey(), pair2.getValue() )) ;
				entryModelLst.add(new ErpInventoryEntryModel(dateQtyPair.getKey(), dateQtyPair.getValue(),
						plantTuplePair.getKey()));
			}

		}

		Collections.<ErpInventoryEntryModel> sort(entryModelLst);

		return entryModelLst;
	}

	public Map<String, Map<java.util.Date, Double>> convertListToMap(
			java.util.List<ErpInventoryEntryModel> entryModelLst) {

		Map<String, Map<java.util.Date, Double>> inventoryEntryMapFromLst = new HashMap<String, Map<java.util.Date, Double>>();

		for (ErpInventoryEntryModel inventoryEntryModel : entryModelLst) {
			if (inventoryEntryMapFromLst.containsKey(inventoryEntryModel.getPlantId())) {
				Map<Date, Double> dateQuantMap = inventoryEntryMapFromLst.get(inventoryEntryModel.getPlantId());
				dateQuantMap.put(inventoryEntryModel.getStartDate(), inventoryEntryModel.getQuantity());
			} else {
				Map<Date, Double> dateQuantMap = new HashMap<Date, Double>();
				dateQuantMap.put(inventoryEntryModel.getStartDate(), inventoryEntryModel.getQuantity());
				inventoryEntryMapFromLst.put(inventoryEntryModel.getPlantId(), dateQuantMap);

			}

		}
		return inventoryEntryMapFromLst;
	}

	private static double roundQuantity(double quantity, double minimumQuantity, double quantityIncrement) {
		if (quantity < minimumQuantity) {
			return 0.0;
		}
		return Math.floor((quantity - minimumQuantity) / quantityIncrement) * quantityIncrement + minimumQuantity;
	}

	

	boolean areDatesEqual(java.util.Date date1, java.util.Date date2) {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));

	}

	@Override
	public void setId(String id) {
		if (id != null)
			super.setId(id);
	}
	
//do we need this?????
//	public double getAvailabileQtyForDate(java.util.Date targetDate, ErpInventoryModel invModel) {
//		/*
//		 * If the store is Foodkick (FDX) and the available qty is zero for that
//		 * day, we are allowed to look ahead ONE following day for inventory.
//		 */
//		if (!FDStoreProperties.getEnableFDXDistinctAvailability()) {
//			return 0;
//		}
//		double avQty = 0;
//		if (null == targetDate)
//			targetDate = new Date();// set the date to today if null
//		Calendar tomorrow = Calendar.getInstance();
//		tomorrow.setTime(targetDate);
//		tomorrow.add(Calendar.DATE, 1);
//		List<ErpInventoryEntryModel> entries = invModel.getEntries();
//		if (entries.isEmpty()) {
//			return avQty;
//		}
//
//		for (ErpInventoryEntryModel entryModell : entries) {
//			if (areDatesEqual(entryModell.getStartDate(), targetDate)) {
//				avQty += entryModell.getQuantity();
//				if (!this.enumStoreId.equals(EnumEStoreId.FDX) || avQty > 0)
//					break;
//				// if we are here, its fdx and the qty is zero
//
//			}
//
//			if (areDatesEqual(entryModell.getStartDate(), tomorrow.getTime())
//					&& this.enumStoreId.equals(EnumEStoreId.FDX) && avQty == 0) {
//				avQty += entryModell.getQuantity();
//				break;
//
//			}
//
//		}
//		return avQty;
//
//	}

}
