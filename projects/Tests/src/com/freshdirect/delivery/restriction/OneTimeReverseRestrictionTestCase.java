package com.freshdirect.delivery.restriction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.freshdirect.framework.util.DateRange;

public class OneTimeReverseRestrictionTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public OneTimeReverseRestrictionTestCase(String name) {
		super(name);
	}

	public void testOneTimeReverseStrategy() throws ParseException {
		RestrictionI s = new OneTimeReverseRestriction("12312553",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			"Thanksgiving",
			"Thanksgiving",
			DF.parse("2003-09-06 00:00:00.0"),
			DF.parse("2003-09-07 00:00:00.0"));

		assertTrue(s.contains(DF.parse("2003-09-05 00:00:00.0")));
		assertTrue(s.contains(DF.parse("2003-09-07 00:00:00.0")));
		assertTrue(s.contains(DF.parse("2003-09-07 13:00:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-06 00:00:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-06 12:00:00.0")));
		assertFalse(s.contains(DF.parse("2003-09-06 23:59:59.0")));

		//
		// isInEffect & isInEffectCompletely scenarios
		//

		// start & end before
		assertEffect(s, "2003-09-04 00:00:00.0", "2003-09-05 00:00:00.0", true, true);

		// start & end after
		assertEffect(s, "2003-09-10 00:00:00.0", "2003-09-11 00:00:00.0", true, true);

		// start & end inside
		assertEffect(s, "2003-09-06 10:00:00.0", "2003-09-06 16:00:00.0", false, false);

		// start before, end on start boundary
		assertEffect(s, "2003-09-05 00:00:00.0", "2003-09-06 00:00:00.0", true, true);

		// start inside, end outside
		assertEffect(s, "2003-09-06 15:00:00.0", "2003-09-10 00:00:00.0", true, false);

		// start on end boundary, end outside
		assertEffect(s, "2003-09-07 00:00:00.0", "2003-09-07 01:00:00.0", true, true);

		// start before, end inside
		assertEffect(s, "2003-09-05 00:00:00.0", "2003-09-06 01:00:00.0", true, false);

		// exact (start on start boundary, end on end boundary)
		assertEffect(s, "2003-09-06 00:00:00.0", "2003-09-07 00:00:00.0", false, false);

		// single moment, on start boundary
		assertEffect(s, "2003-09-06 00:00:00.0", "2003-09-06 00:00:00.0", false, false);

		// single moment, inside
		assertEffect(s, "2003-09-06 23:59:59.0", "2003-09-06 23:59:59.0", false, false);

		// single moment, on end boundary
		assertEffect(s, "2003-09-07 00:00:00.0", "2003-09-07 00:00:00.0", true, true);
	}

	private void assertEffect(RestrictionI r, String start, String end, boolean expectedInEffect, boolean expectedInEffectCompletely)
		throws ParseException {
		DateRange r2 = new DateRange(DF.parse(start), DF.parse(end));
		assertEquals("inEffect", expectedInEffect, r.overlaps(r2));
		assertEquals("inEffectCompletely", expectedInEffectCompletely, r.contains(r2));
	}

}