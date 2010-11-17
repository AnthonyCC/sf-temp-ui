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
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpReturnLineI;
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
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.aspects.BaseAspect;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDCompositeAvailability;
import com.freshdirect.fdstore.atp.FDStatusAvailability;
import com.freshdirect.fdstore.atp.FDStockAvailability;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.SaleStatisticsI;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSessionBean.ProcessActionResult;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.giftcard.ErpGiftCardModel;

public class ServiceTest extends TestCase {
	public ServiceTest(String name) {
		super(name);
	}


	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	StandingOrdersServiceHome sosHome;
	StandingOrdersServiceSessionBeanMockup bean;

	// AddressModel address;
	ErpPaymentMethodI paymentMethod;
	DlvZoneInfoModel zoneInfo;
	
	Date	expiration;
	
	@Override
	protected void setUp() throws Exception {
		// super.setUp();

		// final Context context = TestUtils.createContext();
        
        // TestUtils.createMockContainer(context);
        // TestUtils.createTransaction(context);


		// sosHome =  (StandingOrdersServiceHome)context.lookup( StandingOrdersServiceHome.JNDI_HOME );
		// bean = sosHome.create();
        bean = new StandingOrdersServiceSessionBeanMockup();

        // AspectSystem aspectSystem = TestUtils.createAspectSystem();
        // aspectSystem.add(new OrderLineUtilAspect());

		// address = getCorporateAddress();
		
		
		paymentMethod = new ErpGiftCardModel();
		
		
		zoneInfo = new DlvZoneInfoModel("CODE1", "ZONE1", "REGION1", EnumZipCheckResponses.DELIVER, true, true);
		
		expiration = new Date(System.currentTimeMillis() + 1000);

		// configure rules engine
		FDRegistry.setDefaultRegistry("classpath:/com/freshdirect/TestSOS.registry");
		FDRegistry.addConfiguration("classpath:/com/freshdirect/TestSOS.xml");
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
				TimeOfDay.NEXT_MIDNIGHT));
			res.add(new RecurringRestriction("34343455",
				EnumDlvRestrictionCriterion.DELIVERY,
				EnumDlvRestrictionReason.KOSHER,
				"Kosher Saturday",
				"Saturday",
				Calendar.SATURDAY,
				TimeOfDay.MIDNIGHT,
				TimeOfDay.NEXT_MIDNIGHT));
			res.add(new RecurringRestriction("2343556667",
				EnumDlvRestrictionCriterion.DELIVERY,
				EnumDlvRestrictionReason.ALCOHOL,
				"Alcohol",
				"Alcohol",
				Calendar.SUNDAY,
				new TimeOfDay("12:00 AM"),
				new TimeOfDay("12:00 PM")));

			DlvRestrictionsList restrictionList = new DlvRestrictionsList(res);

			FDRestrictedAvailability ri = new FDRestrictedAvailability(NullAvailability.AVAILABLE, restrictionList);
			
			assertFalse( doATPCheck(identity, createCartLinesSimple(), ri));
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
							TimeOfDay.NEXT_MIDNIGHT));
			
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



	public void testUpdateDeliverySurchages() {
		final FDIdentity identity = new FDIdentity("erp1", "fdu1");

		{
			final AddressModel address = getCorporateAddress();

			FDCartModel cart = new FDTransientCartModel();
			{
				Collection<FDCartLineI> ols = createCartLinesSimple(1);
				SimpleCartLine cl = (SimpleCartLine) ols.iterator().next();
		
				cart.setDeliveryReservation(getFDReservation(identity
						.getErpCustomerPK(), address.getId()));
				cart.addOrderLines(ols);
			}
			cart.setDeliveryAddress(new ErpAddressModel(address));
			
			ProfileModel pm = new ProfileModel();
			MockRuleContext ctx = new MockRuleContext(cart, pm);
			ctx.setSelectedServiceType(EnumServiceType.CORPORATE);
			
			bean.updateDeliverySurcharges(cart, ctx);

			assertEquals(14.95, cart.getChargeAmount(EnumChargeType.DELIVERY), 0);
			// assertEquals(0.97, cart.getChargeAmount(EnumChargeType.MISCELLANEOUS), 0);
			
		}
		{
			final AddressModel address = getPrivateAddress();
	
			FDCartModel cart = new FDTransientCartModel();
			{
				Collection<FDCartLineI> ols = createCartLinesSimple(1);
				SimpleCartLine cl = (SimpleCartLine) ols.iterator().next();
		
				cart.setDeliveryReservation(getFDReservation(identity
						.getErpCustomerPK(), address.getId()));
				cart.addOrderLines(ols);
			}
			cart.setDeliveryAddress(new ErpAddressModel(address));
			
			ProfileModel pm = new ProfileModel();
			MockRuleContext ctx = new MockRuleContext(cart, pm);
			ctx.setSelectedServiceType(EnumServiceType.CORPORATE);
			
			bean.updateDeliverySurcharges(cart, ctx);

			assertEquals(4.95, cart.getChargeAmount(EnumChargeType.DELIVERY), 0);
			// assertEquals(0.97, cart.getChargeAmount(EnumChargeType.MISCELLANEOUS), 0);
		}
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







/**
 * Mock cartline class
 * 
 * @author segabor
 *
 */
class SimpleCartLine implements FDCartLineI {
	private static final long serialVersionUID = 1L;

	private static Random RND = new Random();

	
	private int randomId = RND.nextInt(1000);
	private String cartLineId = Integer.toString(RND.nextInt(16));
	private FDConfigurableI configuration = new FDConfiguration(1.0, "ea");
	private FDSku sku = new FDSku("SKU"+RND.nextInt(1000), 1);
	private String productName;

	// restrictions
	
	private boolean alcohol;
	private boolean kosher;

	private double price = 10.0;


	@Override
	public ErpOrderLineModel buildErpOrderLines(int baseLineNumber)
			throws FDResourceException, FDInvalidConfigurationException {
		return null;
	}

	@Override
	public FDCartLineI createCopy() {
		return null;
	}

	@Override
	public double getActualPrice() {

		return 0;
	}

	@Override
	public Set<EnumDlvRestrictionReason> getApplicableRestrictions() {

		return null;
	}

	@Override
	public String getCartlineId() {
		return cartLineId;
	}

	public void setCartLineId(String cartLineId) {
		this.cartLineId = cartLineId;
	}

	@Override
	public String getCartonNumber() {
		return null;
	}

	@Override
	public String getDeliveredQuantity() {
		return null;
	}

	@Override
	public double getDepositValue() {
		return 0;
	}

	@Override
	public Discount getDiscount() {
		return null;
	}

	@Override
	public double getDiscountAmount() {
		return 0;
	}

	@Override
	public int getErpOrderLineSize() {
		return 0;
	}

	@Override
	public ErpInvoiceLineI getInvoiceLine() {
		return null;
	}

	@Override
	public String getMaterialNumber() {
		return null;
	}

	@Override
	public String getOrderLineId() {
		return null;
	}

	@Override
	public String getOrderLineNumber() {
		return null;
	}

	@Override
	public String getOrderedQuantity() {
		return null;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public PricingContext getPricingContext() {
		return null;
	}

	@Override
	public double getPromotionValue() {
		return 0;
	}

	
	@Override
	public int getRandomId() {
		return randomId;
	}

	public void setRandomId(int randomId) {
		this.randomId = randomId;
	}
	
	@Override
	public String getReturnDisplayQuantity() {
		return null;
	}

	@Override
	public ErpReturnLineI getReturnLine() {
		return null;
	}

	@Override
	public String getReturnedQuantity() {
		return null;
	}

	@Override
	public String getSavingsId() {
		return null;
	}

	@Override
	public EnumEventSource getSource() {
		return null;
	}

	@Override
	public double getTaxRate() {
		return 0;
	}

	@Override
	public double getTaxValue() {
		return 0;
	}

	@Override
	public String getUnitsOfMeasure() {
		return null;
	}

	@Override
	public String getVariantId() {
		return null;
	}

	@Override
	public boolean hasAdvanceOrderFlag() {
		return false;
	}

	@Override
	public boolean hasDepositValue() {
		return false;
	}

	@Override
	public boolean hasDiscount(String promoCode) {
		return false;
	}

	@Override
	public boolean hasInvoiceLine() {
		return false;
	}

	@Override
	public boolean hasRestockingFee() {
		return false;
	}

	@Override
	public boolean hasReturnLine() {
		return false;
	}

	@Override
	public boolean hasScaledPricing() {
		return false;
	}

	@Override
	public boolean hasTax() {
		return false;
	}

	@Override
	public boolean isDiscountApplied() {
		return false;
	}

	@Override
	public boolean isDiscountFlag() {
		return false;
	}

	@Override
	public boolean isEstimatedPrice() {
		return false;
	}

	@Override
	public boolean isSample() {
		return false;
	}

	@Override
	public void removeLineItemDiscount() {
	}

	@Override
	public void setCartonNumber(String no) {
	}

	@Override
	public void setDepositValue(double depositRate) {
	}

	@Override
	public void setDiscount(Discount d) {
	}

	@Override
	public void setDiscountFlag(boolean b) {
	}

	@Override
	public void setOrderLineId(String orderLineId) {
	}

	@Override
	public void setSavingsId(String savingsId) {
	}

	@Override
	public void setSource(EnumEventSource source) {
	}

	@Override
	public void setTaxRate(double taxRate) {
	}

	@Override
	public ErpAffiliate getAffiliate() {
		return null;
	}

	@Override
	public String getCategoryName() {
		return null;
	}

	@Override
	public List<ErpClientCode> getClientCodes() {
		return null;
	}

	@Override
	public FDConfigurableI getConfiguration() {
		return configuration;
	}

	@Override
	public String getConfigurationDesc() {
		return null;
	}

	@Override
	public String getCustomerListLineId() {
		return null;
	}

	@Override
	public String getDepartmentDesc() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDisplayQuantity() {
		return null;
	}

	@Override
	public double getFixedPrice() {
		return 0;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public String getOriginatingProductId() {
		return null;
	}

	@Override
	public EnumOrderLineRating getProduceRating() {
		return null;
	}

	@Override
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public ProductReference getProductRef() {
		return null;
	}

	@Override
	public String getRecipeSourceId() {
		return null;
	}

	@Override
	public String getSalesUnitDescription() {
		return null;
	}

	@Override
	public FDSku getSku() {
		return this.sku;
	}

	@Override
	public String getSkuCode() {
		return this.sku.getSkuCode();
	}

	@Override
	public SaleStatisticsI getStatistics() {
		return null;
	}

	@Override
	public String getUnitPrice() {
		return null;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public String getYmalCategoryId() {
		return null;
	}

	@Override
	public String getYmalSetId() {
		return null;
	}

	@Override
	public boolean hasBrandName(Set<String> brandNames) {
		return false;
	}

	@Override
	public boolean isAlcohol() {
		return this.alcohol;
	}

	public void setAlcohol(boolean alcohol) {
		this.alcohol = alcohol;
	}
	
	@Override
	public boolean isInvalidConfig() {
		return false;
	}

	@Override
	public boolean isKosher() {
		return this.kosher;
	}

	public void setKosher(boolean kosher) {
		this.kosher = kosher;
	}
	
	@Override
	public boolean isPerishable() {
		return false;
	}

	@Override
	public boolean isPlatter() {
		return false;
	}

	@Override
	public boolean isPricedByLb() {
		return false;
	}

	@Override
	public boolean isRequestNotification() {
		return false;
	}

	@Override
	public boolean isSoldByLb() {
		return false;
	}

	@Override
	public boolean isSoldBySalesUnits() {
		return false;
	}

	@Override
	public FDProduct lookupFDProduct() {
		return null;
	}

	@Override
	public FDProductInfo lookupFDProductInfo() {
		return null;
	}

	@Override
	public ProductModel lookupProduct() {
		return null;
	}

	@Override
	public void refreshConfiguration() throws FDResourceException,
			FDInvalidConfigurationException {
	}

	@Override
	public void setConfiguration(FDConfigurableI configuration) {
		this.configuration = configuration;
	}

	@Override
	public void setConfigurationDesc(String configDesc) {
	}

	@Override
	public void setCustomerListLineId(String id) {
	}

	@Override
	public void setDepartmentDesc(String deptDesc) {
	}

	@Override
	public void setDescription(String desc) {
	}

	@Override
	public void setFixedPrice(double price) {
	}

	@Override
	public void setOptions(Map<String, String> options) {
	}

	@Override
	public void setOriginatingProductId(String originatingProductId) {
	}

	@Override
	public void setPricingContext(PricingContext pCtx) {
	}

	@Override
	public void setQuantity(double quantity) {
		this.configuration = new FDConfiguration(quantity, this.configuration.getSalesUnit());
	}

	@Override
	public void setRecipeSourceId(String recipeSourceId) {
	}

	@Override
	public void setRequestNotification(boolean requestNotification) {
	}

	@Override
	public void setSalesUnit(String salesUnit) {
	}

	@Override
	public void setSku(FDSku sku) {
		this.sku  = sku;
	}

	@Override
	public void setStatistics(SaleStatisticsI statistics) {
	}

	@Override
	public void setYmalCategoryId(String ymalCategoryId) {
	}

	@Override
	public void setYmalSetId(String ymalSetId) {
	}

	@Override
	public Map<String, String> getOptions() {
		return null;
	}

	@Override
	public double getQuantity() {
		return configuration.getQuantity();
	}

	@Override
	public String getSalesUnit() {
		return configuration.getSalesUnit();
	}
}



class MockRuleContext implements FDRuleContextI {
	// FDUserI	user;
	FDCartI cart;
	ProfileModel pm;
	EnumServiceType selectedServiceType = null;

	boolean isChefsTable = false;
	
	

	public MockRuleContext(FDCartI cart, ProfileModel pm) {
		this.cart = cart;
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
		// return this.user.getShoppingCart().getSubTotal();
		return cart.getSubTotal();
	}

	@Override
	public EnumServiceType getServiceType() {
		AddressModel address = this.cart.getDeliveryAddress();
		return address != null  ? address.getServiceType() : this.selectedServiceType ;
	}

	@Override
	public FDUserI getUser() {
		return null;
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
