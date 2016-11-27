package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;

public class DlvRestrictionsListTestCase extends TestCase {

	private DlvRestrictionsList restrictionList;

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public DlvRestrictionsListTestCase(String name) {
		super(name);
	}

	public void testIsRestricted() throws ParseException {
		assertFalse(this.restrictionList.isRestricted(EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.KOSHER, DF
			.parse("2003-09-07 00:00:00.0")));
		assertTrue(this.restrictionList.isRestricted(EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.KOSHER, DF
			.parse("2003-09-06 00:00:00.0")));
		assertTrue(this.restrictionList.isRestricted(EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.KOSHER, DF
			.parse("2003-09-05 00:00:00.0")));
		assertFalse(this.restrictionList.isRestricted(EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.KOSHER, DF
			.parse("2003-09-04 00:00:00.0")));

		assertTrue(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-21 00:00:00.0")));
		assertFalse(this.restrictionList.isRestricted(
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			DF.parse("2003-10-28 00:00:00.0")));
	}

	public void testGetRestrictions() throws ParseException {
		List l = this.restrictionList.getRestrictions(EnumDlvRestrictionReason.KOSHER, new DateRange(DF
			.parse("2003-09-05 00:00:00.0"), DF.parse("2003-09-07 23:59:59.0")));
		assertEquals(3, l.size());

		l = this.restrictionList.getRestrictions(EnumDlvRestrictionReason.KOSHER, new DateRange(
			DF.parse("2003-09-06 10:00:00.0"),
			DF.parse("2003-09-08 10:00:00.0")));
		assertEquals(3, l.size());

		l = this.restrictionList.getRestrictions(EnumDlvRestrictionReason.KOSHER, new DateRange(
			DF.parse("2003-09-06 16:00:00.0"),
			DF.parse("2003-09-09 10:00:00.0")));
		assertEquals(3, l.size());

		l = this.restrictionList.getRestrictions(EnumDlvRestrictionReason.THANKSGIVING, new DateRange(DF
			.parse("2003-10-21 00:00:00.0"), DF.parse("2003-11-11 00:00:00.0")));
		assertEquals(1, l.size());
	}

	protected void setUp() throws Exception {
		List res = new ArrayList();
		res.add(new RecurringRestriction("11323",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"Alcohol",
			"Alcohol Sunday",
			Calendar.SUNDAY,
			new TimeOfDay("12:00 AM"),
			new TimeOfDay("12:00 PM"),""));
		res.add(new RecurringRestriction("21232",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Friday",
			"Friday",
			Calendar.FRIDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM"),""));
		res.add(new RecurringRestriction("123213",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Saturday",
			"Saturday",
			Calendar.SATURDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM"),""));
		res.add(new OneTimeRestriction("234234",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Holiday One",
			"Holiday One",
			DF.parse("2003-09-06 00:00:00.0"),
			DF.parse("2003-09-06 23:59:59.0"),""));
		res.add(new OneTimeRestriction("34344",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Holiday Two",
			"Holiday Two",
			DF.parse("2003-09-08 00:00:00.0"),
			DF.parse("2003-09-08 23:59:59.0"),""));

		res.add(new OneTimeReverseRestriction("123213",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			"Thanksgiving",
			"thanksgiving",
			DF.parse("2003-10-28 00:00:00.0"),
			DF.parse("2003-10-29 23:59:59.0"),""));

		this.restrictionList = new DlvRestrictionsList(res);
	}

}
