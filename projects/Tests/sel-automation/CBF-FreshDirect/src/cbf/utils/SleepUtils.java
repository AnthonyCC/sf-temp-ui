/******************************************************************************
$Id : SleepUtils.java 9/8/2014 1:22:41 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
 ******************************************************************************/

package cbf.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cbf.harness.Harness;

/**
 * 
 * Utility to handle sleep with different time slabs.
 * 
 */
public class SleepUtils {

	/**
	 * Gets instance of SleepUtils
	 * 
	 * @return object of SleepUtils
	 */
	public static synchronized SleepUtils getInstance() {
		if (SLEEP_UTILS == null) {
			SLEEP_UTILS = new SleepUtils();
		}
		return SLEEP_UTILS;
	}

	/**
	 * Sleep with level of TimeSlab and returns boolean value depending on level
	 * value
	 * 
	 * @param level
	 *            object of TimeSlab enum
	 * @return boolean result
	 */
	public boolean sleep(TimeSlab level) {
		logger.trace("Sleep:" + level.toString());
		boolean sleep = false;
		int timer = interval(level.getLevel());
		if (timer > 0) {
			try {
				Thread.sleep(timer);
			} catch (InterruptedException e) {
				logger.handleError("Error caught : " + e.getMessage());
			}
			sleep = true;
		}
		logger.trace("Slept:" + level + "," + timer);
		logger.trace("Slept:" + level + "-" + timer);
		return sleep;
	}

	/**
	 * 
	 * Enum to define different sleep levels
	 * 
	 */
	public enum TimeSlab {
		YIELD("YIELD", 0), LOW("LOW", 1), MEDIUM("MEDIUM", 2), HIGH("HIGH", 3);

		private TimeSlab(String n, int lvl) {
			name = n;
			level = lvl;
		}

		/**
		 * Retrieves level of TimeSlab
		 * 
		 * @return TimeSlab level
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * Overridden toString() method of Object class to return name for
		 * specified level
		 */
		public String toString() {
			return name;
		}

		public final int level;
		public final String name;
	}

	/**
	 * Defines interval for sleep
	 * 
	 * @param lvl
	 *            level value for TimeSlab
	 * @return interval value
	 */
	public int interval(int lvl) {
		int timer = 0;
		List<Integer> timerSlabs = getTimerSlabs();
		if (lvl >= 0) {
			if (lvl <= timerSlabs.size())
				timer = timerSlabs.get(lvl);
			else
				timer = timerSlabs.get(timerSlabs.size() - 1)
				* (lvl - timerSlabs.size() + 1);

		}
		if (timer < 0)
			timer = 0;
		return timer;
	}

	/**
	 * Retrieves TimeSlab levels and returns List
	 * 
	 * @return List of levels
	 */
	public List<Integer> getTimerSlabs() {
		if (!this.timerSlabs.isEmpty()) {
			return this.timerSlabs;
		}
		String slabs = (String) Harness.GCONFIG.get("SleepTimerSlabs");
		if (!slabs.equals("") || slabs != null) {
			for (String temp : slabs.trim().split(",")) {
				this.timerSlabs.add(Integer.parseInt(temp));
			}
		}
		if (this.timerSlabs.isEmpty()) {// Not specified; use the default
			this.timerSlabs = Arrays.asList(new Integer[] { 500, 3000, 10000,
					20000 });
		}
		logger
		.trace("TimerSlabs Used="
				+ StringUtils.toString(this.timerSlabs));
		return this.timerSlabs;
	}
	
	/**
	 * Inserts wait
	 * 
	 * @param secs
	 *            Time to sleep(In Seconds)
	 */
	public void sleep(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Overridden toString() method of Object class to return SleepUtils format
	 * string.
	 */
	public String toString() {
		return "SleepUtils()";
	}
	public LogUtils logger = new LogUtils(this);
	private static SleepUtils SLEEP_UTILS;
	private List<Integer> timerSlabs = new ArrayList<Integer>();

}
