package com.freshdirect.delivery.announcement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class SiteAnnouncementTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	private SiteAnnouncement announcement;

	public SiteAnnouncementTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		Set placements = new HashSet();
		placements.add(EnumPlacement.HOME);
		placements.add(EnumPlacement.CART);

		Set userLevels = new HashSet();
		userLevels.add(EnumUserLevel.RECOGNIZED);

		Set deliveryStatuses = new HashSet();
		deliveryStatuses.add(EnumUserDeliveryStatus.HOME_USER);
		deliveryStatuses.add(EnumUserDeliveryStatus.DEPOT_USER);

		this.announcement =
			new SiteAnnouncement(
				"headline",
				"copy",
				DF.parse("2004-01-01"),
				DF.parse("2004-02-01"),
				placements,
				userLevels,
				deliveryStatuses,
				DF.parse("2004-01-15"));
	}

	public void testLastOrderedBefore() throws ParseException {
		assertTrue(
			announcement.isDisplayable(
				DF.parse("2004-01-10"),
				null,
				EnumPlacement.HOME,
				EnumUserDeliveryStatus.HOME_USER,
				EnumUserLevel.RECOGNIZED));
		assertTrue(
			announcement.isDisplayable(
				DF.parse("2004-01-10"),
				DF.parse("2004-01-08"),
				EnumPlacement.HOME,
				EnumUserDeliveryStatus.HOME_USER,
				EnumUserLevel.RECOGNIZED));
		assertFalse(
			announcement.isDisplayable(
				DF.parse("2004-01-10"),
				DF.parse("2004-01-15"),
				EnumPlacement.HOME,
				EnumUserDeliveryStatus.HOME_USER,
				EnumUserLevel.RECOGNIZED));
	}
	
}
