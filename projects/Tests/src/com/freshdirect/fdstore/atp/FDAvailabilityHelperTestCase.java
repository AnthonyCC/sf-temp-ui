package com.freshdirect.fdstore.atp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailability;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;

public class FDAvailabilityHelperTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public FDAvailabilityHelperTestCase(String arg0) {
		super(arg0);
	}

	public void testUnavailable() throws ParseException {
		// setup		
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-23"), 0));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDAvailabilityI av = new FDStockAvailability(erpInv, 1, 1, 1);

		assertNull(firstAv(av, "2004-01-23", 1));
		assertNull(firstAv(av, "2004-01-23", 2));
		assertNull(firstAv(av, "2004-01-23", 8));
	}

	public void testPartial() throws ParseException {
		// setup		
		List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-23"), 0));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-25"), 99999999));

		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		FDAvailabilityI av = new FDStockAvailability(erpInv, 1, 1, 1);

		assertNull(firstAv(av, "2004-01-23", 1));
		assertNull(firstAv(av, "2004-01-23", 2));
		assertEquals(DF.parse("2004-01-25"), firstAv(av, "2004-01-23", 3));
		assertEquals(DF.parse("2004-01-25"), firstAv(av, "2004-01-23", 8));
	}

	public void testRestricted() throws ParseException {
		List<RestrictionI> res = new ArrayList<RestrictionI>();
		res.add(new RecurringRestriction("343345232",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Friday",
			"Friday",
			Calendar.FRIDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM"),""));
		res.add(new RecurringRestriction("343345231",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Saturday",
			"Saturday",
			Calendar.SATURDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM"),""));

		DlvRestrictionsList restrictionList = new DlvRestrictionsList(res);

		FDRestrictedAvailability ri = new FDRestrictedAvailability(NullAvailability.AVAILABLE, restrictionList);

		assertEquals(DF.parse("2004-08-04"), firstAv(ri, "2004-08-03", 8));

		assertTrue(ri.availableSomeTime(new DateRange(DF.parse("2004-08-03"), DF.parse("2004-08-11"))).isAvailable());

	}

	private Date firstAv(FDAvailabilityI av, String startDate, int days) throws ParseException {
		return FDAvailabilityHelper.getFirstAvailableDate(av, DF.parse(startDate), days);
	}
}
