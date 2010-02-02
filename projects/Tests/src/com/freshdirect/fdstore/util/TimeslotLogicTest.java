/**
 * 
 */
package com.freshdirect.fdstore.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.TimeOfDay;

/**
 * @author kocka
 * 
 */
public class TimeslotLogicTest extends TestCase {

	private static final DateFormat DF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public void testGetAvailableCapacity() throws ParseException {

		//
		// before auto-release
		//
		
		assertCapacity(12, TimeslotLogic.PAGE_NORMAL, 30, 10, 8, 4, BEFORE_RELEASE);
		assertCapacity(18, TimeslotLogic.PAGE_CHEFSTABLE, 30, 10, 8, 4, BEFORE_RELEASE);

		
		//
		// after auto-release
		//

		assertCapacity(30, TimeslotLogic.PAGE_NORMAL, 30, 10, 0, 0, AFTER_RELEASE);
		assertCapacity(30, TimeslotLogic.PAGE_CHEFSTABLE, 30, 10, 0, 0, AFTER_RELEASE);

		//
		// extreme values
		//

		//all capacity to chefs table
		assertCapacity(0, TimeslotLogic.PAGE_NORMAL, 30, 30, 0, 10, BEFORE_RELEASE);
		//all capacity to chefs table, with a chefstable user 
		assertCapacity(20, TimeslotLogic.PAGE_CHEFSTABLE, 30, 30, 0, 10, BEFORE_RELEASE);
		//all capacity to chefs table, with a chefstable user
		assertCapacity(30, TimeslotLogic.PAGE_CHEFSTABLE, 30, 30, 0, 0, BEFORE_RELEASE);
		//all capacity to chefs table, with a chefstable user
		assertCapacity(100, TimeslotLogic.PAGE_CHEFSTABLE, 100, 100, 0, 0, BEFORE_RELEASE);
		//none capacity to chefs table
		assertCapacity(20, TimeslotLogic.PAGE_NORMAL, 30, 0, 0, 10, AFTER_RELEASE);
		//zero capacity
		assertCapacity(0, TimeslotLogic.PAGE_NORMAL, 0, 0, 0, 0, AFTER_RELEASE);
		//all allocated to chefs table, from the point of view of ct user
		assertCapacity(0, TimeslotLogic.PAGE_CHEFSTABLE, 30, 30, 0, 30, AFTER_RELEASE);
		//all normal capacity allocated
		assertCapacity(5, TimeslotLogic.PAGE_CHEFSTABLE, 30, 10, 20, 5, BEFORE_RELEASE);
	}
	
	public void testTimingBug() {
		assertEquals(true, TimeslotLogic
				.getAvailableCapacity(createTimeslot(FUTURE, 99, 99, 0, 10),
						PAST, TimeslotLogic.PAGE_CHEFSTABLE, 30));
	}	
	
	private void assertCapacity(int expectedCapacity, int page,
			int totalCapacity, int ctCapacity, int baseAllocation,
			int ctAllocation, Date currentTime) {

		assertEquals(expectedCapacity > 0, TimeslotLogic.getAvailableCapacity(
				createTimeslot(BASE, totalCapacity, ctCapacity, baseAllocation,
						ctAllocation), currentTime, page, 30));
	}

	private final static Date BASE;

	private final static Date BEFORE_RELEASE;

	private final static Date AFTER_RELEASE;
	
	private final static Date FUTURE;

	private final static Date PAST;
	
	static {
		try {
			BASE = DF.parse("2006-11-03 00:00");
			BEFORE_RELEASE = DF.parse("2006-11-02 10:00");
			AFTER_RELEASE = DF.parse("2006-11-02 20:40");
			FUTURE = DF.parse("3000-01-01 00:00");
			PAST = DF.parse("2000-01-01 00:00");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private DlvTimeslotModel createTimeslot(Date base, int totalCapacity, int ctCapacity,
			int baseAllocation, int ctAllocation) {
		return new DlvTimeslotModel(new PrimaryKey(""), "", base,
				new TimeOfDay("09:00 am"), new TimeOfDay("11:00 am"),
				new TimeOfDay("09:00 pm"), EnumTimeslotStatus.PREMIUM,
				totalCapacity, ctCapacity, baseAllocation, ctAllocation, 30, false,"");
	}

}
