package com.freshdirect.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class RangeTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public RangeTestCase(String name) {
		super(name);
	}

	public void testConstructor() throws ParseException {
		Date d1 = DF.parse("2004-01-01");
		Date d2 = DF.parse("2004-01-02");

		new DateRange(d1, d2);
		new Range(d1, d1);
		try {
			new Range(d2, d1);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}

	public void testContains() throws ParseException {
		Range r = new Range(DF.parse("2004-01-02"), DF.parse("2004-01-04"));
		assertFalse(r.contains(DF.parse("2004-01-01")));
		assertTrue(r.contains(DF.parse("2004-01-02")));
		assertTrue(r.contains(DF.parse("2004-01-03")));
		assertFalse(r.contains(DF.parse("2004-01-04")));
		assertFalse(r.contains(DF.parse("2004-01-05")));
	}

	public void testRangeOverlapAndContain() throws ParseException {
		Date d1 = DF.parse("2004-01-01");
		Date d2 = DF.parse("2004-01-02");
		Date d3start = DF.parse("2004-01-03");
		Date d4 = DF.parse("2004-01-04");
		Date d5 = DF.parse("2004-01-05");
		Date d6end = DF.parse("2004-01-06");
		Date d7 = DF.parse("2004-01-07");
		Date d8 = DF.parse("2004-01-08");

		Range r = new Range(d3start, d6end);

		// start & end before
		assertOverlapsContains(r, d1, d2, false, false);

		// start & end after
		assertOverlapsContains(r, d7, d8, false, false);

		// start & end inside
		assertOverlapsContains(r, d4, d5, true, true);

		// start before, end on start boundary
		assertOverlapsContains(r, d2, d3start, false, false);

		// start inside, end outside
		assertOverlapsContains(r, d5, d7, true, false);

		// start on end boundary, end outside
		assertOverlapsContains(r, d6end, d7, false, false);

		// start before, end inside
		assertOverlapsContains(r, d2, d4, true, false);

		// exact (start on start boundary, end on end boundary)
		assertOverlapsContains(r, d3start, d6end, true, true);

		// single moment, on start boundary
		assertOverlapsContains(r, d3start, d3start, true, true);

		// single moment, inside
		assertOverlapsContains(r, d5, d5, true, true);

		// single moment, on end boundary
		assertOverlapsContains(r, d6end, d6end, false, false);
	}

	private void assertOverlapsContains(Range r, Date start, Date end, boolean expectOverlaps, boolean expectContains) throws ParseException {
		Range r2 = new Range(start, end);
		assertEquals("overlap", expectOverlaps, r.overlaps(r2));
		assertEquals("overlap reverse", expectOverlaps, r2.overlaps(r));
		assertEquals("contain", expectContains, r.contains(r2));
	}

}