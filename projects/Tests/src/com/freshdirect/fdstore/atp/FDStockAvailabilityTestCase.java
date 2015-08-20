package com.freshdirect.fdstore.atp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateRange;

public class FDStockAvailabilityTestCase extends TestCase {

	public FDStockAvailabilityTestCase(String arg0) {
		super(arg0);
	}

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public void testAvailable() throws ParseException {
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();

		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 10));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-08"), 10));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-11"), 900000));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 30, 1, 1);

		assertTrue(fdInv.availableCompletely(range("2003-01-11", "2003-01-12")).isAvailable());
		assertTrue(fdInv.availableSomeTime(range("2003-01-11", "2003-01-12")).isAvailable());
		assertStock(fdInv.availableCompletely(range("2003-01-05", "2003-01-13")), 10);
		assertTrue(fdInv.availableSomeTime(range("2003-01-05", "2003-01-13")).isAvailable());

		assertStock(fdInv.availableCompletely(range("2003-01-05", "2003-01-06")), 10);
		assertStock(fdInv.availableSomeTime(range("2003-01-05", "2003-01-06")), 10);
		assertStock(fdInv.availableCompletely(range("2003-01-08", "2003-01-09")), 20);
		assertStock(fdInv.availableCompletely(range("2003-01-06", "2003-01-09")), 10);
		assertStock(fdInv.availableSomeTime(range("2003-01-06", "2003-01-09")), 20);
		assertStock(fdInv.availableCompletely(range("2003-01-06", "2003-01-08")), 10);
	}

	public void testStuff() throws ParseException {
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();

		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-01"), 0));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-04"), 900000));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);
		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 1, 1, 1);

		assertTrue(fdInv.availableSomeTime(range("2003-01-01", "2003-01-10")).isAvailable());
		assertFalse(fdInv.availableCompletely(range("2003-01-01", "2003-01-10")).isAvailable());

	}

	public void testFutureExpansion() throws ParseException {
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();

		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 0));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 30, 1, 1);

		assertFalse(fdInv.availableCompletely(range("2003-01-11", "2003-01-12")).isAvailable());
		assertFalse(fdInv.availableCompletely(range("2003-01-05", "2003-01-13")).isAvailable());
		assertFalse(fdInv.availableCompletely(range("2003-02-10", "2003-02-11")).isAvailable());
		assertFalse(fdInv.availableCompletely(range("2003-01-03", "2003-01-04")).isAvailable());
	}

	public void testPastExpansionAlwaysUnavailable() throws ParseException {

		// unavailable on first day
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 0));
		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 30, 1, 1);
		assertFalse(fdInv.availableCompletely(range("2003-01-01", "2003-01-05")).isAvailable());
		assertFalse(fdInv.availableCompletely(range("2003-01-01", "2003-01-10")).isAvailable());

		// available on first day
		erpEntries = new ArrayList<ErpInventoryEntryModel>();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 1000));
		erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		fdInv = new FDStockAvailability(erpInv, 30, 1, 1);
		assertFalse(fdInv.availableCompletely(range("2003-01-01", "2003-01-05")).isAvailable());

	}

	public void testEmptyInventoryAlwaysAvailable() throws ParseException {
		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), new ArrayList<ErpInventoryEntryModel>());

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 30, 1, 1);

		assertTrue(fdInv.availableCompletely(range("2003-01-06", "2003-01-07")).isAvailable());
	}

	public void testRoundToZeroBelowMinimumQuantity() throws ParseException {
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 3.2));
		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 10, 5, 1);

		assertStock(fdInv.availableCompletely(range("2003-01-05", "2003-01-06")), 0);
	}

	public void testRoundToIncrement() throws ParseException {
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 3.2));
		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 10, 1, 1);

		assertStock(fdInv.availableCompletely(range("2003-01-05", "2003-01-06")), 3);
	}
	
	public void testFirstAvailableDate() throws ParseException {
		
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();

		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-05"), 0));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-08"), 10));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2003-01-11"), 900000));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability fdInv = new FDStockAvailability(erpInv, 1, 1, 1);
		
		assertEquals(DF.parse("2003-01-08"), fdInv.getFirstAvailableDate(range("2003-01-01", "2003-02-01")));
		assertEquals(DF.parse("2003-01-08"), fdInv.getFirstAvailableDate(range("2003-01-01", "2003-01-09")));
		assertEquals(DF.parse("2003-01-08"), fdInv.getFirstAvailableDate(range("2003-01-05", "2003-01-09")));
		assertEquals(DF.parse("2003-01-08"), fdInv.getFirstAvailableDate(range("2003-01-06", "2003-01-10")));
		assertEquals(DF.parse("2003-01-08"), fdInv.getFirstAvailableDate(range("2003-01-08", "2003-01-10")));
		assertNull(fdInv.getFirstAvailableDate(range("2003-01-01", "2003-01-02")));
		assertNull(fdInv.getFirstAvailableDate(range("2003-01-01", "2003-01-05")));
		assertNull(fdInv.getFirstAvailableDate(range("2003-01-06", "2003-01-07")));
		assertNull(fdInv.getFirstAvailableDate(range("2003-01-01", "2003-01-08")));
	}

	private DateRange range(String start, String end) throws ParseException {
		return new DateRange(DF.parse(start), DF.parse(end));
	}

	private void assertStock(FDAvailabilityInfo info, double qty) {
		assertFalse(info.isAvailable());
		assertEquals(qty, ((FDStockAvailabilityInfo) info).getQuantity(), 0.01);
	}

}