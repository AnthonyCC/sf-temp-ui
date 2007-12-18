package com.freshdirect.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class TimeOfDayTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public TimeOfDayTestCase(String name) {
		super(name);
	}

	public void testExtremes() {
		assertTrue(TimeOfDay.MIDNIGHT.before(new TimeOfDay("00:01 AM")));
		assertTrue(TimeOfDay.NEXT_MIDNIGHT.after(new TimeOfDay("11:59 PM")));
		assertTrue(TimeOfDay.MIDNIGHT.before(TimeOfDay.NEXT_MIDNIGHT));
	}

	public void testAsDate() throws ParseException {
		Date base = DF.parse("2004-01-01 00:00:00.0");

		assertEquals(DF.parse("2004-01-01 00:00:00.0"), TimeOfDay.MIDNIGHT.getAsDate(base));
		assertEquals(DF.parse("2004-01-02 00:00:00.0"), TimeOfDay.NEXT_MIDNIGHT.getAsDate(base));
	}

}