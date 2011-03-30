package com.freshdirect.webapp.taglib.fdstore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;

public class DeliveryTimeslotTagTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	private final static DateFormat TF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	private DlvRestrictionsList restrictions;

	public DeliveryTimeslotTagTestCase(String name) {
		super(name);
	}

	public void testGetDateRanges() throws Exception {

		//

		DateRange expectedRange = new DateRange(DF.parse("2003-10-16"), DF.parse("2003-10-22"));
		List ranges = DeliveryTimeSlotTag.getDateRanges(expectedRange, false, restrictions, EnumDlvRestrictionReason.THANKSGIVING);
		assertEquals(1, ranges.size());
		assertEquals(expectedRange, ranges.get(0));

		//

		expectedRange = new DateRange(DF.parse("2003-11-05"), DF.parse("2003-11-11"));
		ranges = DeliveryTimeSlotTag.getDateRanges(expectedRange, true, restrictions, EnumDlvRestrictionReason.THANKSGIVING);
		assertEquals(1, ranges.size());
		assertEquals(expectedRange, ranges.get(0));

		//

		expectedRange = new DateRange(DF.parse("2003-10-23"), DF.parse("2003-10-30"));
		ranges = DeliveryTimeSlotTag.getDateRanges(expectedRange, true, restrictions, EnumDlvRestrictionReason.THANKSGIVING);
		assertEquals(1, ranges.size());

		//

		expectedRange = new DateRange(DF.parse("2003-11-19"), DF.parse("2003-11-25"));
		ranges = DeliveryTimeSlotTag.getDateRanges(expectedRange, true, restrictions, EnumDlvRestrictionReason.THANKSGIVING);
		assertEquals(1, ranges.size());
		assertEquals(expectedRange, ranges.get(0));

		//

		expectedRange = new DateRange(DF.parse("2003-11-23"), DF.parse("2003-11-29"));
		ranges = DeliveryTimeSlotTag.getDateRanges(expectedRange, true, restrictions, EnumDlvRestrictionReason.THANKSGIVING);
		assertEquals(1, ranges.size());
		assertEquals(expectedRange, ranges.get(0));

		//

		expectedRange = new DateRange(DF.parse("2004-01-01"), DF.parse("2004-01-07"));
		ranges = DeliveryTimeSlotTag.getDateRanges(expectedRange, true, restrictions, EnumDlvRestrictionReason.THANKSGIVING);
		assertEquals(1, ranges.size());
		assertEquals(expectedRange, ranges.get(0));
	}

	public void testGetNextHoliday() throws ParseException {
		assertNull(DeliveryTimeSlotTag.getNextHoliday(
			restrictions,
			new DateRange(DF.parse("2003-10-01"), DF.parse("2003-10-09")),
			8));

		assertEquals(EnumDlvRestrictionReason.THANKSGIVING, DeliveryTimeSlotTag.getNextHoliday(restrictions, new DateRange(DF
			.parse("2003-10-01"), DF.parse("2003-10-09")), 20));
	}

	protected void setUp() throws Exception {
		List res = new ArrayList();
		res.add(new RecurringRestriction("234234",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"Alcohol",
			"Alcohol Sunday",
			Calendar.SUNDAY,
			new TimeOfDay("12:00 AM"),
			new TimeOfDay("12:00 PM"),""));
		res.add(new RecurringRestriction("234355",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Friday",
			"Friday",
			Calendar.FRIDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM"),""));
		res.add(new RecurringRestriction("667678",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Saturday",
			"Saturday",
			Calendar.SATURDAY,
			new TimeOfDay("00:00 AM"),
			new TimeOfDay("11:59 PM"),""));
		res.add(new OneTimeRestriction("455677",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Holiday One",
			"Holiday One",
			DF.parse("2003-09-06"),
			TF.parse("2003-09-06 23:59:59.0"),""));
		res.add(new OneTimeRestriction("9273644",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.KOSHER,
			"Kosher Holiday Two",
			"Holiday Two",
			DF.parse("2003-09-08"),
			TF.parse("2003-09-08 23:59:59.0"),""));

		res.add(new OneTimeReverseRestriction("3567899",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.THANKSGIVING,
			"Thanksgiving",
			"Thanksgiving",
			DF.parse("2003-10-28"),
			DF.parse("2003-10-29"),""));

		this.restrictions = new DlvRestrictionsList(res);
	}

}