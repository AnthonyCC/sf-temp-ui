package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.TestCase;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;

public class RecurringRestrictionTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public RecurringRestrictionTestCase(String name) {
		super(name);
	}

	public void testContains() throws ParseException {
		RestrictionI s = new RecurringRestriction("123234",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"NY State Sunday block",
			"NY State Sunday block",
			Calendar.SUNDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 AM"),"");

		assertFalse(s.contains(DF.parse("2003-09-05 00:00:00.0")));
		assertTrue(s.contains(DF.parse("2003-09-07 00:00:00.0")));
		assertTrue(s.contains(DF.parse("2003-09-07 09:00:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-07 11:59:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-07 12:00:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-07 16:00:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-08 00:00:00.0")));

	}

	public void testRange() throws ParseException {
		RestrictionI r = new RecurringRestriction("2343444",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"Alcohol",
			"Alcohol",
			Calendar.SUNDAY,
			new TimeOfDay("12:00 AM"),
			new TimeOfDay("12:00 PM"),"");

		assertEffect(r, "2003-09-13 23:00:00.0", "2003-09-14 00:00:00.0", false, false);
		assertEffect(r, "2003-09-14 10:00:00.0", "2003-09-14 12:00:00.0", true, true);
		assertEffect(r, "2003-09-14 10:00:00.0", "2003-09-14 16:00:00.0", true, false);
		assertEffect(r, "2003-09-14 12:00:00.0", "2003-09-14 14:00:00.0", false, false);
		assertEffect(r, "2003-09-14 14:00:00.0", "2003-09-14 16:00:00.0", false, false);
		assertEffect(r, "2003-09-14 12:00:00.0", "2003-09-14 12:00:00.0", false, false);
		assertEffect(r, "2003-09-15 10:00:00.0", "2003-09-15 12:00:00.0", false, false);
	}

	private void assertEffect(RestrictionI r, String start, String end, boolean expectOverlap, boolean expectContain)
		throws ParseException {
		DateRange r2 = new DateRange(DF.parse(start), DF.parse(end));
		assertEquals("overlap", expectOverlap, r.overlaps(r2));
		assertEquals("contain", expectContain, r.contains(r2));
	}

	public void testRecurringBug2() throws ParseException {
		RestrictionI r = new RecurringRestriction("1244445",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher",
			"Kosher",
			Calendar.FRIDAY,
			new TimeOfDay("12:00 AM"),
			new TimeOfDay("11:59 PM"),"");

		assertFalse(r.overlaps(new DateRange(DF.parse("2003-09-16 15:00:00.0"), DF.parse("2003-09-16 17:00:00.0"))));
	}

	public void testRecurringBug3() throws ParseException {
		RestrictionI r = new RecurringRestriction("1243455",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.BLOCK_FRIDAY,
			"Block Friday",
			"Block Friday",
			Calendar.FRIDAY,
			TimeOfDay.MIDNIGHT,
			TimeOfDay.NEXT_MIDNIGHT,"");

		assertTrue(r.contains(new DateRange(DF.parse("2004-08-06 00:00:00.0"), DF.parse("2004-08-07 00:00:00.0"))));
	}

}