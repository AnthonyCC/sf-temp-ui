package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;

public class FDRestrictedAvailabilityTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public FDRestrictedAvailabilityTestCase(String arg0) {
		super(arg0);
	}
	
	private FDRestrictedAvailability ri;

	protected void setUp() throws Exception {
		super.setUp();
		// setup
		List res = new ArrayList();
		res.add(new RecurringRestriction("12234342",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Friday",
			"Friday",
			Calendar.FRIDAY,
			TimeOfDay.MIDNIGHT,
			TimeOfDay.NEXT_MIDNIGHT));
		res.add(new RecurringRestriction("34343455",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Saturday",
			"Saturday",
			Calendar.SATURDAY,
			TimeOfDay.MIDNIGHT,
			TimeOfDay.NEXT_MIDNIGHT));
		res.add(new RecurringRestriction("2343556667",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"Alcohol",
			"Alcohol",
			Calendar.SUNDAY,
			new TimeOfDay("12:00 AM"),
			new TimeOfDay("12:00 PM")));

		DlvRestrictionsList restrictionList = new DlvRestrictionsList(res);

		List erpEntries = new ArrayList();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-19 00:00:00.0"), 0));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-21 00:00:00.0"), 900000));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDStockAvailability inventory = new FDStockAvailability(erpInv, 30, 1, 1);

		this.ri = new FDRestrictedAvailability(inventory, restrictionList);
	}
	
	public void testCheckAvailability() throws Exception {

		// monday 0:00 to tuesday 0:00 -> unavailable due to stock
		FDAvailabilityInfo info = ri.availableCompletely(range("2004-01-19 00:00:00.0", "2004-01-20 00:00:00.0"));
		assertEquals(false, info.isAvailable());
		assertTrue(info instanceof FDStockAvailabilityInfo);

		// wednesday 0:00 to friday 0:00 -> available
		info = ri.availableCompletely(range("2004-01-21 00:00:00.0", "2004-01-23 00:00:00.0"));
		assertEquals(true, info.isAvailable());

		// friday 15:00 to friday 16:00 -> unavailable due to restriction
		info = ri.availableCompletely(range("2004-01-23 15:00:00.0", "2004-01-23 16:00:00.0"));
		assertEquals(false, info.isAvailable());
		assertTrue(info instanceof FDRestrictedAvailabilityInfo);

		// ... even with SomeTime
		info = ri.availableSomeTime(range("2004-01-23 15:00:00.0", "2004-01-23 16:00:00.0"));
		assertEquals(false, info.isAvailable());
		assertTrue(info instanceof FDRestrictedAvailabilityInfo);

		// monday 0:00 to saturday 0:00 -> unavailable due to restriction
		info = ri.availableCompletely(range("2004-01-19 00:00:00.0", "2004-01-24 00:00:00.0"));
		assertEquals(false, info.isAvailable());
		assertTrue(info instanceof FDRestrictedAvailabilityInfo);

		// ... but available some time
		info = ri.availableSomeTime(range("2004-01-19 00:00:00.0", "2004-01-24 00:00:00.0"));
		assertEquals(true, info.isAvailable());

		// sunday 10:00 to sunday 11:00 -> unavailable due to restriction
		info = ri.availableCompletely(range("2004-01-25 10:00:00.0", "2004-01-25 11:00:00.0"));
		assertEquals(false, info.isAvailable());
		assertTrue(info instanceof FDRestrictedAvailabilityInfo);

		// ... not even for a minute
		info = ri.availableSomeTime(range("2004-01-25 10:00:00.0", "2004-01-25 11:00:00.0"));
		assertEquals(false, info.isAvailable());
		assertTrue(info instanceof FDRestrictedAvailabilityInfo);

	}
	
	private DateRange range(String start, String end) throws ParseException {
		return new DateRange(DF.parse(start), DF.parse(end));
	}

	/*
	 * 
	 * 
	 * This would require proper unification of distinct ranges
	 * potential algorithm:
	 * - retain all ranges that overlap with requested range
	 * - sort them by start date
	 * - unify adjacent/overlapping ranges (min(start1,start2), max(end1,end2)
	 * - if anything left, ranges are non-contigous -> available some time in requested range

	 public void testCheckAvailability2() throws Exception {
	 // setup
	 List res = new ArrayList();
	 res.add(createDayRestriction(Calendar.SUNDAY));
	 res.add(createDayRestriction(Calendar.MONDAY));
	 res.add(createDayRestriction(Calendar.TUESDAY));
	 res.add(createDayRestriction(Calendar.WEDNESDAY));
	 res.add(createDayRestriction(Calendar.THURSDAY));
	 res.add(createDayRestriction(Calendar.FRIDAY));
	 res.add(createDayRestriction(Calendar.SATURDAY));

	 DlvRestrictionsList restrictionList = new DlvRestrictionsList(res);

	 // execute & verify
	 FDRestrictedAvailability ri = new FDRestrictedAvailability(NullAvailability.AVAILABLE, restrictionList);

	 FDAvailabilityInfo info = ri.availableSomeTime(range("2004-08-05 00:00:00.0", "2004-08-13 00:00:00.0"));
	 assertEquals(false, info.isAvailable());
	 }
	 

	 private final static EnumDlvRestrictionReason[] DAY_REASONS = {
	 EnumDlvRestrictionReason.BLOCK_SUNDAY,
	 EnumDlvRestrictionReason.BLOCK_MONDAY,
	 EnumDlvRestrictionReason.BLOCK_TUESDAY,
	 EnumDlvRestrictionReason.BLOCK_WEDNESDAY,
	 EnumDlvRestrictionReason.BLOCK_THURSDAY,
	 EnumDlvRestrictionReason.BLOCK_FRIDAY,
	 EnumDlvRestrictionReason.BLOCK_SATURDAY};

	 private RecurringRestriction createDayRestriction(int dayOfWeek) {
	 return new RecurringRestriction(
	 DAY_REASONS[dayOfWeek - 1],
	 "Day " + dayOfWeek,
	 "Day " + dayOfWeek,
	 dayOfWeek,
	 TimeOfDay.MIDNIGHT,
	 TimeOfDay.NEXT_MIDNIGHT);
	 }
	 */

}