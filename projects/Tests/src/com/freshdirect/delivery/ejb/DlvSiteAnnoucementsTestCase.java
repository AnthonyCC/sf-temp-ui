package com.freshdirect.delivery.ejb;

import java.text.SimpleDateFormat;
import java.util.List;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.delivery.announcement.EnumPlacement;
import com.freshdirect.delivery.announcement.EnumUserDeliveryStatus;
import com.freshdirect.delivery.announcement.EnumUserLevel;
import com.freshdirect.delivery.announcement.SiteAnnouncement;

public class DlvSiteAnnoucementsTestCase extends DbTestCaseSupport {

	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public DlvSiteAnnoucementsTestCase(String arg) {
		super(arg);
	}

	protected String getSchema() {
		return "DLV";
	}

	protected String[] getAffectedTables() {
		return new String[] { "DLV.SITE_ANNOUNCEMENTS" };
	}

	public void testSiteAnnouncements() throws Exception {
		this.setUpDataSet("SiteAnnouncements.xml");
		List l = DlvManagerDAO.getSiteAnnouncements(conn);
		assertEquals(2, l.size());

		SiteAnnouncement ann1 = (SiteAnnouncement) l.get(0);
		assertEquals("Headline One", ann1.getHeadline());
		assertEquals("Copy One", ann1.getCopy());

		assertEquals(SF.parse("2003-09-29 00:00:00"), ann1.getStartDate());
		assertEquals(SF.parse("2003-10-05 00:00:00"), ann1.getEndDate());

		assertEquals(2, ann1.getPlacements().size());
		assertTrue(ann1.getPlacements().contains(EnumPlacement.HOME));
		assertTrue(ann1.getPlacements().contains(EnumPlacement.CHECKOUT));

		assertEquals(1, ann1.getUserLevels().size());
		assertTrue(ann1.getUserLevels().contains(EnumUserLevel.RECOGNIZED));

		assertEquals(2, ann1.getDeliveryStatuses().size());
		assertTrue(ann1.getDeliveryStatuses().contains(EnumUserDeliveryStatus.HOME_USER));
		assertTrue(ann1.getDeliveryStatuses().contains(EnumUserDeliveryStatus.DEPOT_USER));

		assertNull(ann1.getLastOrderBefore());

		SiteAnnouncement ann2 = (SiteAnnouncement) l.get(1);
		assertEquals("Headline Two", ann2.getHeadline());
		assertEquals("Copy Two", ann2.getCopy());

		assertEquals(SF.parse("2004-01-01 00:00:00"), ann2.getStartDate());
		assertEquals(SF.parse("2004-02-01 00:00:00"), ann2.getEndDate());

		assertEquals(2, ann2.getPlacements().size());
		assertTrue(ann2.getPlacements().contains(EnumPlacement.YOUR_ACCOUNT));
		assertTrue(ann2.getPlacements().contains(EnumPlacement.DELIVERY_INFO));

		assertEquals(2, ann2.getUserLevels().size());
		assertTrue(ann2.getUserLevels().contains(EnumUserLevel.GUEST));
		assertTrue(ann2.getUserLevels().contains(EnumUserLevel.RECOGNIZED));

		assertEquals(1, ann2.getDeliveryStatuses().size());
		assertTrue(ann2.getDeliveryStatuses().contains(EnumUserDeliveryStatus.PICKUP_ONLY_USER));

		assertEquals(SF.parse("2004-01-15 00:00:00"), ann2.getLastOrderBefore());
	}

}
