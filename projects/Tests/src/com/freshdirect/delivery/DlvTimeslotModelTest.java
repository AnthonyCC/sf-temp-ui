package com.freshdirect.delivery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;

import junit.framework.TestCase;

public class DlvTimeslotModelTest extends TestCase {

	private static final DateFormat DF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public void testTimeslotReleased() throws ParseException {
		assertRelease(true, "2006-11-01 18:00", 30, "2006-11-01 17:35");
		assertRelease(true, "2006-11-01 18:00", 30, "2006-11-01 17:31");
		assertRelease(false, "2006-11-01 18:00", 30, "2006-11-01 17:25");
		assertRelease(false, "2006-11-01 18:00", 30, "2006-11-01 17:29");
	}

	private void assertRelease(boolean expected, String cutoffDate,
			int ctReleaseTime, String currentDate) throws ParseException {
		DlvTimeslotModel ts = new DlvTimeslotModel();
		Date c = DF.parse(cutoffDate);
		ts.setBaseDate(DateUtil.addDays(DateUtil.truncate(c), 1));
		ts.setCutoffTime(new TimeOfDay(c));
		ts.setCtReleaseTime(ctReleaseTime);
		ts.setCtActive(true);
		assertEquals(expected, ts.isCTCapacityReleased(DF.parse(currentDate)));
		ts.setCtActive(false);
		assertEquals(false, ts.isCTCapacityReleased(DF.parse(currentDate)));
	}

	public void testCalculations() {
		DlvTimeslotModel ts = new DlvTimeslotModel();
		ts.setCapacity(100);
		ts.setChefsTableCapacity(32);
		ts.setBaseAllocation(40);
		ts.setChefsTableAllocation(16);

		assertEquals(40 + 16, ts.getTotalAllocation());
		assertEquals(100 - 32, ts.getBaseCapacity());
		assertEquals(100 - 40 - 16, ts.getTotalAvailable());
		assertEquals(100 - 32 - 40, ts.getBaseAvailable());
		assertEquals(32 - 16, ts.getChefsTableAvailable());
	}

	public void testDateGetters() throws ParseException {
		DlvTimeslotModel ts = new DlvTimeslotModel();
		ts.setBaseDate(DF.parse("2006-11-12 00:00"));
		ts.setStartTime(new TimeOfDay("10:00 am"));
		ts.setEndTime(new TimeOfDay("02:00 pm"));
		ts.setCutoffTime(new TimeOfDay("09:00 pm"));

		assertEquals(DF.parse("2006-11-12 10:00"), ts.getStartTimeAsDate());
		assertEquals(DF.parse("2006-11-12 14:00"), ts.getEndTimeAsDate());
		assertEquals(DF.parse("2006-11-11 21:00"), ts.getCutoffTimeAsDate());
	}

}
