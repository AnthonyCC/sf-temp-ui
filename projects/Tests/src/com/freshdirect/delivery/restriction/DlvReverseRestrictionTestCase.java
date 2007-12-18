package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.framework.util.TimeOfDay;

public class DlvReverseRestrictionTestCase extends TestCase {

	private DlvRestrictionsList restrictionList;

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public DlvReverseRestrictionTestCase(String name) {
		super(name);
	}

	public void testIsRestricted() throws ParseException {
		assertTrue(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-09-07 00:00:00.0")));
		assertTrue(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-22 00:00:00.0")));
		assertFalse(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-23 00:00:00.0")));
		assertFalse(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-23 23:59:59.0")));
		assertTrue(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-24 00:00:00.0")));
		assertTrue(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-25 00:00:00.0")));
	}

	protected void setUp() throws Exception {
		List res = new ArrayList();
		res.add(new RecurringRestriction("13233",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"Alcohol",
			"Alcohol Sunday",
			Calendar.SUNDAY,
			new TimeOfDay("12:00 AM"),
			new TimeOfDay("12:00 PM")));
		res.add(new RecurringRestriction("123123",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Friday",
			"Friday",
			Calendar.FRIDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM")));
		res.add(new RecurringRestriction("2123213",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Saturday",
			"Saturday",
			Calendar.SATURDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM")));
		res.add(new OneTimeRestriction("132323",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Holiday One",
			"Holiday One",
			DF.parse("2003-09-06 00:00:00.0"),
			DF.parse("2003-09-06 23:59:59.0")));
		res.add(new OneTimeRestriction("123233",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Holiday Two",
			"Holiday Two",
			DF.parse("2003-09-08 00:00:00.0"),
			DF.parse("2003-09-08 23:59:59.0")));

		res.add(new OneTimeReverseRestriction("312421",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			"Thanksgiving",
			"Thanksgivng",
			DF.parse("2003-10-23 00:00:00.0"),
			DF.parse("2003-10-24 00:00:00.0")));

		this.restrictionList = new DlvRestrictionsList(res);
	}
}
