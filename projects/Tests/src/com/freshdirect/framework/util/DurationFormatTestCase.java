package com.freshdirect.framework.util;

import junit.framework.TestCase;

public class DurationFormatTestCase extends TestCase {

	public DurationFormatTestCase(String name) {
		super(name);
	}

	public void testDefaultFormat() {
		DurationFormat df = new DurationFormat();
		assertEquals("", df.format(0));
		assertEquals("1 second", df.format(1000));
		assertEquals("1 second", df.format(1999));
		assertEquals("15 seconds", df.format(15 * 1000));
		assertEquals("1 minute", df.format(60 * 1000));
		assertEquals("1 minute 59 seconds", df.format(119000));
		assertEquals("2 minutes", df.format(2 * 60 * 1000));
		assertEquals("2 minutes 5 seconds", df.format((2 * 60 * 1000) + (5 * 1000)));
		assertEquals("1 hour", df.format(60 * 60 * 1000));
		assertEquals("1 hour 15 minutes", df.format((60 * 60 * 1000) + (15 * 60 * 1000)));
		assertEquals("1 day", df.format(24 * 60 * 60 * 1000));
		assertEquals("1 day 2 hours", df.format(26 * 60 * 60 * 1000));
		assertEquals("1 day 2 hours", df.format((26 * 60 * 60 * 1000) + 1999));
		assertEquals("1 day", df.format((24 * 60 * 60 * 1000) + (15 * 60 * 1000)));
	}

	public void testCustomFormat() {
		DurationFormat df = new DurationFormat(false, DurationFormat.MASK_HOUR | DurationFormat.MASK_MINUTE);
		assertEquals("", df.format(0));
		assertEquals("", df.format(1000));
		assertEquals("", df.format(1999));
		assertEquals("", df.format(15 * 1000));
		assertEquals("1 m", df.format(60 * 1000));
		assertEquals("1 m", df.format(119000));
		assertEquals("2 m", df.format(2 * 60 * 1000));
		assertEquals("2 m", df.format((2 * 60 * 1000) + (5 * 1000)));
		assertEquals("1 h", df.format(60 * 60 * 1000));
		assertEquals("1 h 15 m", df.format((60 * 60 * 1000) + (15 * 60 * 1000)));
		assertEquals("24 h", df.format(24 * 60 * 60 * 1000));
		assertEquals("26 h", df.format(26 * 60 * 60 * 1000));
		assertEquals("26 h", df.format((26 * 60 * 60 * 1000) + 1999));
		assertEquals("24 h 15 m", df.format((24 * 60 * 60 * 1000) + (15 * 60 * 1000)));
	}

}
