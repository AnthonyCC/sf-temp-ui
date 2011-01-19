package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

public class DateRangeFormatTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public DateRangeFormatTestCase(String name) {
		super(name);
	}

	public void testFromat() throws ParseException {
		RestrictionI s = new OneTimeRestriction("12343445",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Passover",
			"Passover",
			DF.parse("2003-09-06 00:00:00.0"),
			DF.parse("2003-09-07 00:00:00.0"));
		assertEquals("September 6, 2003", s.getDisplayDate());

		s = new OneTimeRestriction("435433545",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Passover",
			"Passover",
			DF.parse("2003-09-06 00:00:00.0"),
			DF.parse("2003-09-08 00:00:00.0"));
		assertEquals("September 6-7, 2003", s.getDisplayDate());

		s = new OneTimeRestriction("32325566",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Passover",
			"Passover",
			DF.parse("2004-07-30 00:00:00.0"),
			DF.parse("2004-08-05 00:00:00.0"));
		assertEquals("July 30-August 5, 2004", s.getDisplayDate());
	}

}