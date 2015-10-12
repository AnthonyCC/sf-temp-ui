package com.freshdirect.fdstore.content;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.framework.util.DateUtil;

public class SkuModelAvailabilityTest extends TestCase {
	
	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 *  A very simple smoke test.
	 *  
	 *  @throws ParseException on date parsing errors.
	 */
	public void testFirst() throws ParseException {
		Date now = new Date();
		List erpEntries  = new ArrayList();
		String[] materials = {"000000000123"};
		TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
		ErpInventoryModel            inventoryModel;
		FDStockAvailability          availability;
		FDProductInfo                productInfo;
		SkuModel.AvailabilityAdapter adapter;
		
		// a 10000 units available starting 2004-01-23
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-23"), 10000));
		inventoryModel = new ErpInventoryModel("SAP12345", now, erpEntries);
		inventoryCache.addInventory(materials[0], inventoryModel);
		Date[] availDates = new Date[0];
		availability   = new FDStockAvailability(inventoryModel, 1, 1, 1);

		productInfo = FDCustomerManagerTestSupport.createProductInfo("SKU123456", now, materials, inventoryCache);

		adapter = new SkuModel.AvailabilityAdapter(productInfo, availability,"1000","1000","1000");//::FDX::

		assertFalse(adapter.isDiscontinued());
		assertFalse(adapter.isTempUnavailable());
	    assertFalse(adapter.isOutOfSeason());
	    assertFalse(adapter.isUnavailable());
	    assertTrue(adapter.isAvailableWithin(2));
	}

	/**
	 *  Test availability within a few days.
	 *  
	 *  @throws ParseException on date parsing errors.
	 */
	public void testAvailableWithin() throws ParseException {
		Date today = new Date();
		Date dayAfterTomorrow = DateUtil.addDays(today, 2);
		List erpEntries = new ArrayList();
		ErpInventoryModel            inventoryModel;
		String[] materials = {"000000000123"};
		TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
		FDStockAvailability          availability;
		FDProductInfo                productInfo;
		SkuModel.AvailabilityAdapter adapter;
		
		// a 10000 units available starting 2004-01-23, none the day after,
		// then a lot available the following day
		erpEntries.add(new ErpInventoryEntryModel(today, 0));
		erpEntries.add(new ErpInventoryEntryModel(dayAfterTomorrow, 10000));
		inventoryModel   = new ErpInventoryModel("SAP12345", today, erpEntries);
		inventoryCache.addInventory(materials[0], inventoryModel);
		Date[] availDates = new Date[0];
		availability     = new FDStockAvailability(inventoryModel, 1, 1, 1);

                productInfo = FDCustomerManagerTestSupport.createProductInfo("SKU123456", today, materials, inventoryCache);

		adapter = new SkuModel.AvailabilityAdapter(productInfo, availability,"1000","1000","1000");

		assertFalse(adapter.isDiscontinued());
		assertFalse(adapter.isTempUnavailable());
	    assertFalse(adapter.isOutOfSeason());
	    assertFalse(adapter.isUnavailable());
	    // see if available tomorrow
	    assertFalse(adapter.isAvailableWithin(2));
	    // see if available afterwards
	    assertTrue(adapter.isAvailableWithin(3));
	    // see that the earliest availability is the day after tomorrow
	    assertTrue(DateUtil.getDiffInDays(dayAfterTomorrow, adapter.getEarliestAvailability()) == 0);
	}

	/**
	 *  Test availability within a few days, with discontinued items.
	 *  
	 *  @throws ParseException on date parsing errors.
	 */
	public void testAvailableWithinDiscontinued() throws ParseException {
		Date today = new Date();
		Date dayAfterTomorrow = DateUtil.addDays(today, 2);
		List erpEntries = new ArrayList();
		ErpInventoryModel            inventoryModel;
		String[] materials = {"000000000123"};
		TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
		FDStockAvailability          availability;
		FDProductInfo                productInfo;
		SkuModel.AvailabilityAdapter adapter;
		
		// a 10000 units available starting 2004-01-23, none the day after,
		// then a lot available the following day
		erpEntries.add(new ErpInventoryEntryModel(today, 0));
		erpEntries.add(new ErpInventoryEntryModel(dayAfterTomorrow, 10000));
		inventoryModel   = new ErpInventoryModel("SAP12345", today, erpEntries);
		inventoryCache.addInventory(materials[0], inventoryModel);
		Date[] availDates = new Date[0];
		availability     = new FDStockAvailability(inventoryModel, 1, 1, 1);
		
                productInfo = FDCustomerManagerTestSupport.createProductInfo("SKU123456", today, materials, inventoryCache, EnumAvailabilityStatus.DISCONTINUED);
		
		adapter = new SkuModel.AvailabilityAdapter(productInfo, availability,"1000","1000","1000");

		assertTrue(adapter.isDiscontinued());
		assertFalse(adapter.isTempUnavailable());
	    assertFalse(adapter.isOutOfSeason());
	    assertTrue(adapter.isUnavailable());
	    // see if available tomorrow
	    assertFalse(adapter.isAvailableWithin(2));
	    // see if available afterwards
	    assertFalse(adapter.isAvailableWithin(3));
	    // see the earliest availability indication
	    assertNull(adapter.getEarliestAvailability());
	}

}
