package com.freshdirect.fdstore.atp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailability;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateRange;

public class FDCompositeAvailabilityTestCase extends TestCase {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Map of String,FDAvailabilityI pairs
	 * FIXME: String keys should be Integers
	 */
	private Map availabilities = new HashMap();

	public FDCompositeAvailabilityTestCase(String arg0) {
		super(arg0);
	}

	public void testCheckAvailability() throws ParseException {
		FDAvailabilityI av = new FDCompositeAvailability(availabilities);

		FDCompositeAvailabilityInfo i;

		i = (FDCompositeAvailabilityInfo) av.availableSomeTime(range(
				"2004-01-19", "2004-01-20"));
		assertFalse(i.isAvailable());
		assertEquals(1, i.getComponentInfo().size());
		assertTrue(i.getComponentInfo().containsKey("one"));

		i = (FDCompositeAvailabilityInfo) av.availableSomeTime(range(
				"2004-01-22", "2004-01-23"));
		assertFalse(i.isAvailable());

		assertEquals(2, i.getComponentInfo().size());
		assertTrue(i.getComponentInfo().containsKey("one"));
		assertTrue(i.getComponentInfo().containsKey("three"));

		assertTrue(av.availableSomeTime(range("2004-01-25", "2004-01-26"))
				.isAvailable());
		
		assertEquals( DF.parse("2004-01-23"), av.getFirstAvailableDate(range("2004-01-01", "2004-01-25")) );
		assertEquals( DF.parse("2004-01-23"), av.getFirstAvailableDate(range("2004-01-18", "2004-01-25")) );
		assertEquals( DF.parse("2004-01-23"), av.getFirstAvailableDate(range("2004-01-23", "2004-01-25")) );
		assertNull( av.getFirstAvailableDate(range("2004-01-01", "2004-01-10")) );
		assertNull( av.getFirstAvailableDate(range("2004-01-01", "2004-01-23")) );
	}
	
	private DateRange range(String start, String end) throws ParseException {
		return new DateRange(DF.parse(start), DF.parse(end));
	}

	protected void setUp() throws Exception {
		List erpEntries = new ArrayList();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-19"), 0));
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-23"), 900000));
		ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);

		this.availabilities.put("one", new FDStockAvailability(erpInv, 30, 1, 1));

		erpEntries = new ArrayList();
		erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-19"), 900000));
		erpInv = new ErpInventoryModel("000000000100200400", new Date(), erpEntries);

		this.availabilities.put("two", new FDStockAvailability(erpInv, 30, 1, 1));

		List restrictions = new ArrayList();
		restrictions.add(new OneTimeRestriction("12312323",
			EnumDlvRestrictionCriterion.DELIVERY,
			EnumDlvRestrictionReason.ALCOHOL,
			"foo",
			"bar",
			DF.parse("2004-01-22"),
			DF.parse("2004-01-23"),""));

		this.availabilities.put("three", new FDRestrictedAvailability(
			NullAvailability.AVAILABLE,
			new DlvRestrictionsList(restrictions)));
	}

}