package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.webapp.crm.util.DeliveryTimeWindowFormatter;

import junit.framework.TestCase;

public class DeliveryTimeWindowFormatterTest extends TestCase {
	public List<TestPair> testWindows;
	
	static class TestPair {
		public final int startHour;
		public final int endHour;
		public final Date startDate;
		public final Date endDate;
		public final String test;
		
		public TestPair(int sh, int eh, String result) {
			this.startHour = sh;
			this.endHour = eh;
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, startHour);
			startDate = c.getTime();
			
			c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, endHour);
			endDate = c.getTime();

			this.test = result;
		}
	}
	
	public void setUp() {
		testWindows = new ArrayList<TestPair>();
		
		testWindows.add(new TestPair(8, 10, "8"+DeliveryTimeWindowFormatter.SEP+"10am"));
		testWindows.add(new TestPair(9, 11, "9"+DeliveryTimeWindowFormatter.SEP+"11am"));
		testWindows.add(new TestPair(13, 15, "1"+DeliveryTimeWindowFormatter.SEP+"3"));
		testWindows.add(new TestPair(8, 10, "8"+DeliveryTimeWindowFormatter.SEP+"10am"));
		testWindows.add(new TestPair(10, 12, "10"+DeliveryTimeWindowFormatter.SEP+"noon"));
		testWindows.add(new TestPair(12, 14, "noon"+DeliveryTimeWindowFormatter.SEP+"2"));
	}
	
	public void testVariousWindows() {
		for (TestPair tp : testWindows) {
			String r = DeliveryTimeWindowFormatter.formatTime(tp.startDate, tp.endDate);
			assertEquals(tp.test, r);
		}
	}
}
