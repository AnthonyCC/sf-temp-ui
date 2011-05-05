package com.freshdirect.delivery.restriction.ejb;

import java.util.List;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;

public class DlvRestrictionDAOTestCase extends DbTestCaseSupport {

	public DlvRestrictionDAOTestCase(String arg) {
		super(arg);
	}

	protected String getSchema() {
		return "DLV";
	}

	protected String[] getAffectedTables() {
		return new String[] {"DLV.RESTRICTED_DAYS", "DLV.MUNICIPALITY_INFO", "DLV.MUNICIPALITY_RESTRICTION_DATA"};
	}

	public void testDlvRestrictions() throws Exception {
		this.setUpDataSet("DlvRestrictionsInit.xml");
		List l = DlvRestrictionDAO.getDlvRestrictions(conn);
		assertEquals(2, l.size());

		OneTimeRestriction r1 = (OneTimeRestriction) l.get(0);
		assertEquals("OneTime", r1.getName());
		assertEquals("Msg OneTime", r1.getMessage());
		assertEquals(EnumDlvRestrictionReason.KOSHER, r1.getReason());
		assertEquals(EnumDlvRestrictionType.ONE_TIME_RESTRICTION, r1.getType());

		AlcoholRestriction r2 = (AlcoholRestriction) l.get(1);
		assertEquals("Recurring", r2.getName());
		assertEquals("Msg Recurring", r2.getMessage());
		assertEquals(EnumDlvRestrictionReason.ALCOHOL, r2.getReason());
		assertEquals(EnumDlvRestrictionType.RECURRING_RESTRICTION, r2.getType());

	}

}