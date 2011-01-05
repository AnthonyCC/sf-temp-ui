package com.freshdirect.fdstore.standingorders.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailability;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.aspects.BaseAspect;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDCompositeAvailability;
import com.freshdirect.fdstore.atp.FDStatusAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSessionBean.ProcessActionResult;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.giftcard.ErpGiftCardModel;

public class ServiceTest extends MockObjectTestCase {
	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	StandingOrdersServiceSessionBeanMockup bean;

	// AddressModel address;
	ErpPaymentMethodI paymentMethod;
	DlvZoneInfoModel zoneInfo;
	
	Date	expiration;
	
	Mockery context;
	
	static {
		// test surcharge updates - configure rules engine
		FDRegistry.setDefaultRegistry("classpath:/com/freshdirect/TestSOS.registry");
		FDRegistry.addConfiguration("classpath:/com/freshdirect/TestSOS.xml");
	}



	@Override
	protected void setUp() throws Exception {
		super.setUp();

        bean = new StandingOrdersServiceSessionBeanMockup();
		
		paymentMethod = new ErpGiftCardModel();
		
		
		zoneInfo = new DlvZoneInfoModel("CODE1", "ZONE1", "REGION1", EnumZipCheckResponses.DELIVER, true, true);
		
		expiration = new Date(System.currentTimeMillis() + 1000);

		context = new Mockery();
	}



	/**
	 * Creates a corporate addess
	 */
	private AddressModel getCorporateAddress() {
		AddressModel address = new AddressModel();
		address.setId("addr1");
		address.setAddress1("900 Main ST");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10044");
		AddressInfo info = new AddressInfo();
		info.setAddressType(EnumAddressType.FIRM);
		info.setScrubbedStreet("900 MAIN ST");
		info.setZoneCode("");
		address.setAddressInfo(info);
		address.setServiceType(EnumServiceType.CORPORATE);

		
		return address;
	}

	private AddressModel getPrivateAddress() {
		AddressModel address = new AddressModel();
		address.setId("addr2");
		address.setAddress1("13911 86TH RD");
		address.setCity("Jamaica");
		address.setState("NY");
		address.setZipCode("11435");
		AddressInfo info = new AddressInfo();
		info.setAddressType(EnumAddressType.STREET);
		info.setScrubbedStreet("13911 86TH RD");
		info.setZoneCode("");
		address.setAddressInfo(info);
		address.setServiceType(EnumServiceType.HOME);

		return address;
	}
	

	static class OrderLineUtilAspect extends BaseAspect {
		public OrderLineUtilAspect() {
			super(new DebugMethodPatternPointCut("StandingOrdersServiceSessionBean\\.isValidCustomerList\\(java.util.List\\)"));
		}

		@Override
		public void intercept(InvocationContext arg0) throws Exception {
			arg0.setReturnObject(Boolean.TRUE);
		}
	}




	private FDTimeslot getFDTimeSlot() {
		Calendar cal = Calendar.getInstance();
		
		
		DlvTimeslotModel dtm = new DlvTimeslotModel();
		
		dtm.setBaseDate(cal.getTime());

		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		dtm.setStartTime(new TimeOfDay(cal.getTime()));

		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		dtm.setEndTime(new TimeOfDay(cal.getTime()));

		return new FDTimeslot(dtm);
	}

	

	private Date getExpiration() {
		return expiration;
	}

	private FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = getExpiration();
		FDTimeslot timeSlot = getFDTimeSlot();
		FDReservation reservation = new FDReservation(
			new PrimaryKey("1"), timeSlot, expirationDT,
			EnumReservationType.STANDARD_RESERVATION,
			customerID, addressID,
			false, false, null, false, null, 20);
		return reservation;

	}



	/**
	 * Test ATP check
	 * 
	 * 
	 * @throws FDResourceException
	 * @throws ParseException
	 */
	public void testATPCheck() throws FDResourceException, ParseException {
		final FDIdentity identity = new FDIdentity("erp1", "fdu1");
		final Calendar cal = Calendar.getInstance();
		
		
		/**
		 * [1] restrictions
		 */
		{
			List<RestrictionI> res = new ArrayList<RestrictionI>();
			res.add(new RecurringRestriction("12234342",
				EnumDlvRestrictionCriterion.DELIVERY,
				EnumDlvRestrictionReason.KOSHER,
				"Kosher Friday",
				"Friday",
				Calendar.FRIDAY,
				TimeOfDay.MIDNIGHT,
				TimeOfDay.NEXT_MIDNIGHT,""));
			res.add(new RecurringRestriction("34343455",
				EnumDlvRestrictionCriterion.DELIVERY,
				EnumDlvRestrictionReason.KOSHER,
				"Kosher Saturday",
				"Saturday",
				Calendar.SATURDAY,
				TimeOfDay.MIDNIGHT,
				TimeOfDay.NEXT_MIDNIGHT,""));
			res.add(new RecurringRestriction("2343556667",
				EnumDlvRestrictionCriterion.DELIVERY,
				EnumDlvRestrictionReason.ALCOHOL,
				"Alcohol",
				"Alcohol",
				Calendar.SUNDAY,
				new TimeOfDay("12:00 AM"),
				new TimeOfDay("12:00 PM"),""));

			DlvRestrictionsList restrictionList = new DlvRestrictionsList(res);

			FDRestrictedAvailability ri = new FDRestrictedAvailability(NullAvailability.AVAILABLE, restrictionList);
			
			final boolean atpCheckFailed = doATPCheck(identity, createCartLinesSimple(), ri);

			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				// must fail on Friday violating Kosher Friday restriction
				assertTrue( atpCheckFailed);
			} else {
				// must pass
				assertFalse( atpCheckFailed);
			}
		}
		{
			// KOSHER products are not available today...
			List<RestrictionI> res = new ArrayList<RestrictionI>();
			res.add(new RecurringRestriction("res1",
							EnumDlvRestrictionCriterion.DELIVERY,
							EnumDlvRestrictionReason.KOSHER,
							"Kosher Day",
							"Today",
							cal.get(Calendar.DAY_OF_WEEK), // current day
							TimeOfDay.MIDNIGHT,
							TimeOfDay.NEXT_MIDNIGHT,""));
			
			Collection<FDCartLineI> ols = createCartLinesSimple(1);
			SimpleCartLine cl = (SimpleCartLine) ols.iterator().next();
			cl.setKosher(true); // KOSHER PRODUCT

			assertTrue(
				doATPCheck(
					identity, ols,
					new FDRestrictedAvailability(NullAvailability.AVAILABLE, new DlvRestrictionsList(res))
				)
			);
		}
		

		/**
		 * [2] stock availability
		 */
		{
			// Want to buy 5 pcs but only 1 item available -> modify quantity to one.
			Collection<FDCartLineI> ols = createCartLinesSimple(5.0);
			
			List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
			erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-19 00:00:00.0"), 0));
			erpEntries.add(new ErpInventoryEntryModel(DF.parse("2010-01-01 00:00:00.0"), 1));

			ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);
			FDAvailabilityI inventory = new FDStockAvailability(erpInv, 1, 1, 1);

			assertFalse( doATPCheck(identity, ols, inventory));
		}
		{
			// SKU not available although 5 wanted to buy -> no chance
			Collection<FDCartLineI> ols = createCartLinesSimple(5.0);
			
			List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
			erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-19 00:00:00.0"), 0));

			ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);
			FDAvailabilityI inventory = new FDStockAvailability(erpInv, 1, 1, 1);

			assertTrue( doATPCheck(identity, ols, inventory));
		}


		/**
		 * [3] Composite case
		 */
		{
			Collection<FDCartLineI> ols = createCartLinesSimple();
			final FDCartLineI item = ols.iterator().next();
			final String key = Integer.toString(item.getRandomId());

			FDAvailabilityI availability = NullAvailability.AVAILABLE;
			
			Map<String, FDAvailabilityI> map1 = new HashMap<String, FDAvailabilityI>();
			map1.put(
					key,
					availability );

			assertFalse( doATPCheck(identity, ols, new FDCompositeAvailability(map1)) );
		}
		{
			Collection<FDCartLineI> ols = createCartLinesSimple();
			final FDCartLineI item = ols.iterator().next();
			final String key = Integer.toString(item.getRandomId());

			FDAvailabilityI availability = NullAvailability.UNAVAILABLE;
			
			Map<String, FDAvailabilityI> map1 = new HashMap<String, FDAvailabilityI>();
			map1.put(
					key,
					availability );

			assertTrue( doATPCheck(identity, ols, new FDCompositeAvailability(map1)) );
		}


		/**
		 * [4] (Un)available by status
		 */
		assertTrue( doATPCheckWithStatus(identity, createCartLinesSimple(), EnumAvailabilityStatus.DISCONTINUED) );
		assertTrue( doATPCheckWithStatus(identity, createCartLinesSimple(), EnumAvailabilityStatus.OUT_OF_SEASON) );
		assertTrue( doATPCheckWithStatus(identity, createCartLinesSimple(), EnumAvailabilityStatus.TEMP_UNAV) );
		assertFalse( doATPCheckWithStatus(identity, createCartLinesSimple(), EnumAvailabilityStatus.AVAILABLE) );
	}



	public void testUpdateDeliverySurchagesCorporate() {
		final FDIdentity identity = new FDIdentity("erp1", "fdu1");

		final AddressModel address = getCorporateAddress();

		final FDCartModel cart = new FDTransientCartModel();
		{
			Collection<FDCartLineI> ols = createCartLinesSimple(1);
	
			cart.setDeliveryReservation(getFDReservation(identity
					.getErpCustomerPK(), address.getId()));
			cart.addOrderLines(ols);
		}
		cart.setDeliveryAddress(new ErpAddressModel(address));
		
		final FDUserI user = context.mock(FDUserI.class);
		context.checking(new Expectations() {{
			allowing(user).getShoppingCart(); will(returnValue(cart));
		}});

		
		ProfileModel pm = new ProfileModel();
		MockRuleContext ctx = new MockRuleContext(user, pm);
		ctx.setSelectedServiceType(EnumServiceType.CORPORATE);

		
		
		bean.updateDeliverySurcharges(cart, ctx);

		Calendar cal = Calendar.getInstance();
		int dno = cal.get(Calendar.DAY_OF_WEEK);
		assertEquals(dno == Calendar.MONDAY ? 14.99 : 9.99, cart.getChargeAmount(EnumChargeType.DELIVERY), 0);
	}



	public void testUpdateDeliverySurchagesHome() {
		final FDIdentity identity = new FDIdentity("erp1", "fdu1");

		final AddressModel address = getPrivateAddress();

		final FDCartModel cart = new FDTransientCartModel();
		{
			Collection<FDCartLineI> ols = createCartLinesSimple(1);
	
			cart.setDeliveryReservation(getFDReservation(identity
					.getErpCustomerPK(), address.getId()));
			cart.addOrderLines(ols);
		}
		cart.setDeliveryAddress(new ErpAddressModel(address));
		
		final FDUserI user = context.mock(FDUserI.class);
		context.checking(new Expectations() {{
			allowing(user).getShoppingCart(); will(returnValue(cart));
		}});

		ProfileModel pm = new ProfileModel();
		MockRuleContext ctx = new MockRuleContext(user, pm);
		ctx.setSelectedServiceType(EnumServiceType.HOME);
		
		bean.updateDeliverySurcharges(cart, ctx);

		// Expected rule is "Default Fee"
		assertEquals(5.79, cart.getChargeAmount(EnumChargeType.DELIVERY), 0);
	}



	/**
	 * Setup a simple cartline list having only one item.
	 * 
	 * @return
	 */
	private Collection<FDCartLineI> createCartLinesSimple(double quantity) {
		Collection<FDCartLineI> basicCL = new ArrayList<FDCartLineI>();

		SimpleCartLine orderLine = new SimpleCartLine();
		orderLine.setProductName("prd1");
		orderLine.setQuantity(quantity >= 1 ? quantity : 1.0);
		basicCL.add(orderLine);

		return basicCL;
	}

	private Collection<FDCartLineI> createCartLinesSimple() {
		return createCartLinesSimple(1.0);
	}

	/**
	 * @param identity
	 * @throws FDResourceException
	 */
	private boolean doATPCheckWithStatus(final FDIdentity identity, Collection<FDCartLineI> cartlines, EnumAvailabilityStatus status)
			throws FDResourceException {
		return doATPCheck(identity, cartlines, new FDStatusAvailability( status, NullAvailability.AVAILABLE ));
	}


	private boolean doATPCheck(final FDIdentity identity, Collection<FDCartLineI> cartlines,
			FDAvailabilityI availability) throws FDResourceException {
		// PREPARE CART
		final AddressModel address = getCorporateAddress();
		FDCartModel cart = new FDTransientCartModel();
		cart.setDeliveryReservation(getFDReservation(identity
				.getErpCustomerPK(), address.getId()));

		cart.addOrderLines(cartlines);

		Map<String, FDAvailabilityI> map1 = new HashMap<String, FDAvailabilityI>();
		map1.put(
				Integer.toString(cartlines.iterator().next().getRandomId()),
				availability );
		cart.setAvailability(new FDCompositeAvailability(map1));

		ProcessActionResult vr = new ProcessActionResult();
		bean.doATPCheck(identity, cart, vr);

		return vr.hasInvalidItems();
	}
}



/**
 * Test Subclass
 */
class StandingOrdersServiceSessionBeanMockup extends StandingOrdersServiceSessionBean {
	private static final long serialVersionUID = 1636173239113263268L;

	@Override
	public boolean isValidCustomerList(List<FDCustomerListItem> lineItems) {
		return true;
	}
	
	@Override
	protected FDCartModel checkAvailability(FDIdentity identity,
			FDCartModel cart, long timeout) throws FDResourceException {
		return cart;
	}


	@Override
	public FDCartModel buildCart(FDCustomerList soList,
			ErpPaymentMethodI paymentMethod, AddressModel deliveryAddressModel,
			List<FDTimeslot> timeslots, DlvZoneInfoModel zoneInfo,
			FDReservation reservation, ProcessActionResult vr)
			throws FDResourceException {
		return super.buildCart(soList, paymentMethod, deliveryAddressModel, timeslots,
				zoneInfo, reservation, vr);
	}
}







class MockRuleContext implements FDRuleContextI {
	FDUserI	user;
	// FDCartI cart;
	ProfileModel pm;
	EnumServiceType selectedServiceType = null;

	boolean isChefsTable = false;
	
	

	public MockRuleContext(FDUserI	user, ProfileModel pm) {
		this.user = user;
		//this.cart = cart;
		this.pm = pm;
	}





	@Override
	public String getCounty() {
		return "US";
	}

	@Override
	public String getDepotCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getOrderTotal() {
		return this.user.getShoppingCart().getSubTotal();
		// return cart.getSubTotal();
	}

	@Override
	public EnumServiceType getServiceType() {
		AddressModel address = this.user.getShoppingCart().getDeliveryAddress();
		return address != null  ? address.getServiceType() : this.selectedServiceType ;
	}

	@Override
	public FDUserI getUser() {
		return this.user;
	}

	@Override
	public boolean hasProfileAttribute(String attributeName, String attributeValue) {
		// This piece of code is dupped from FDRulesContextImpl
		if (pm == null)
			return false;

		String attribValue = pm.getAttribute(attributeName);
		if (attributeValue == null)
			return attribValue != null;
		return (attributeValue.equalsIgnoreCase(attribValue));
	}

	@Override
	public boolean isChefsTable() {
		return this.isChefsTable;
	}

	public void setChefsTable(boolean isChefsTable) {
		this.isChefsTable = isChefsTable;
	}
	
	
	@Override
	public boolean isVip() {
		return this.hasProfileAttribute("VIPCustomer", "true");
	}
	
	
	public void setProfile(ProfileModel pm) {
		this.pm = pm;
	}


	public void setSelectedServiceType(EnumServiceType selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}
	
	public EnumServiceType getSelectedServiceType() {
		return selectedServiceType;
	}
}
