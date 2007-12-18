package com.freshdirect.framework.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

public class DayOfWeekSetTestCase extends TestCase {

	public DayOfWeekSetTestCase(String name) {
		super(name);
	}

	public void testFormat() {
		DayOfWeekSet dows = DayOfWeekSet.EMPTY;
		assertEquals("", dows.format(false));

		dows = new DayOfWeekSet(new int[] { Calendar.MONDAY });
		assertEquals("Monday", dows.format(false));
		assertEquals("Mondays", dows.format(true));

		dows = new DayOfWeekSet(new int[] { Calendar.MONDAY, Calendar.SUNDAY, Calendar.TUESDAY });
		assertEquals("Sunday, Monday, Tuesday", dows.format(false));
		assertEquals("Sundays, Mondays, Tuesdays", dows.format(true));
	}

	public void testInvert() {
		DayOfWeekSet dows1 =
			new DayOfWeekSet(
				new int[] { Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY });

		DayOfWeekSet dows2 = new DayOfWeekSet(new int[] { Calendar.SATURDAY, Calendar.SUNDAY });

		assertFalse(dows1.equals(dows2));
		assertEquals(dows1.inverted(), dows2);
		assertEquals(dows2.inverted(), dows1);
	}

	public void testUnion() {
		DayOfWeekSet dows1 = new DayOfWeekSet(new int[] { Calendar.MONDAY });
		DayOfWeekSet dows2 = new DayOfWeekSet(new int[] { Calendar.SUNDAY, Calendar.TUESDAY });
		DayOfWeekSet dowsU = dows1.union(dows2);

		assertEquals(new DayOfWeekSet(new int[] { Calendar.MONDAY, Calendar.SUNDAY, Calendar.TUESDAY }), dowsU);
		assertEquals(dows1, dows1.union(DayOfWeekSet.EMPTY));
		assertEquals(dows1, DayOfWeekSet.EMPTY.union(dows1));
	}

	public void testTranslate() {
		DayOfWeekSet dows = new DayOfWeekSet(new int[] { Calendar.SUNDAY, Calendar.TUESDAY });
		List l = dows.translate(new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" });

		assertEquals(Arrays.asList(new String[] { "Sun", "Tue" }), l);
	}

	public void testEncodeDecode() {
		DayOfWeekSet dows = new DayOfWeekSet(new int[] { Calendar.SUNDAY, Calendar.TUESDAY });
		assertEquals(dows, DayOfWeekSet.decode(dows.encode()));
	}

}
