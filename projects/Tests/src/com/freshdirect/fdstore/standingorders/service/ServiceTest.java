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
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
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
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.SaleStatisticsI;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSessionBean.ProcessActionResult;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.giftcard.ErpGiftCardModel;

public class ServiceTest extends TestCase {
	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	StandingOrdersServiceHome sosHome;
	StandingOrdersServiceSessionBeanMockup bean;

	AddressModel address;
	ErpPaymentMethodI paymentMethod;
	DlvZoneInfoModel zoneInfo;
	
	@Override
	protected void setUp() throws Exception {
        // final Context context = TestUtils.createContext();
        
        // TestUtils.createMockContainer(context);
        // TestUtils.createTransaction(context);


		// sosHome =  (StandingOrdersServiceHome)context.lookup( StandingOrdersServiceHome.JNDI_HOME );
		// bean = sosHome.create();
        bean = new StandingOrdersServiceSessionBeanMockup();

        // AspectSystem aspectSystem = TestUtils.createAspectSystem();
        // aspectSystem.add(new OrderLineUtilAspect());

		address = new AddressModel();
		address.setId("addr1");
		address.setAddress1("900 Main ST");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10044");
		AddressInfo info = new AddressInfo();
		info.setAddressType(EnumAddressType.FIRM);
		info.setScrubbedStreet("900 MAIN ST");
		address.setAddressInfo(info);
		
		
		
		paymentMethod = new ErpGiftCardModel();
		
		
		zoneInfo = new DlvZoneInfoModel("CODE1", "ZONE1", "REGION1", EnumZipCheckResponses.DELIVER, true, true);
		
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

	

	private FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
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
		
		
		/* RESTRICTIONS TEST */
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

			List<ErpInventoryEntryModel> erpEntries = new ArrayList<ErpInventoryEntryModel>();
			erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-19 00:00:00.0"), 0));
			erpEntries.add(new ErpInventoryEntryModel(DF.parse("2004-01-21 00:00:00.0"), 900000));

			/** ErpInventoryModel erpInv = new ErpInventoryModel("000000000100200300", new Date(), erpEntries);
			FDAvailabilityI inventory = new FDStockAvailability(erpInv, 30, 1, 1); **/
			
			FDAvailabilityI inventory = NullAvailability.AVAILABLE;

			FDRestrictedAvailability ri = new FDRestrictedAvailability(inventory, restrictionList);
			
			assertFalse( doATPCheck(identity, ri));
		}
		
		// Unavailable by status
		assertTrue( doATPCheckWithStatus(identity, EnumAvailabilityStatus.DISCONTINUED) );
		assertTrue( doATPCheckWithStatus(identity, EnumAvailabilityStatus.OUT_OF_SEASON) );
		assertTrue( doATPCheckWithStatus(identity, EnumAvailabilityStatus.TEMP_UNAV) );
		assertFalse( doATPCheckWithStatus(identity, EnumAvailabilityStatus.AVAILABLE) );
	}



	/**
	 * @param identity
	 * @throws FDResourceException
	 */
	private boolean doATPCheckWithStatus(final FDIdentity identity, EnumAvailabilityStatus status)
			throws FDResourceException {
		// PREPARE CART
		/*** FDCartModel cart = new FDTransientCartModel();
		cart.setDeliveryReservation(getFDReservation(identity.getErpCustomerPK(), address.getId()));
		
		Collection<FDCartLineI> cartlines = new ArrayList<FDCartLineI>();		
		SimpleCartLine orderLine = null;

		orderLine = new SimpleCartLine();
		orderLine.setProductName("prd1");
		cartlines.add(orderLine);

		cart.addOrderLines(cartlines);
		
		Map<Object,FDAvailabilityI> map1 = new HashMap<Object, FDAvailabilityI>();
		map1.put(
				Integer.toString(orderLine.getRandomId()),
				new FDStatusAvailability( status, NullAvailability.AVAILABLE )
		);
		cart.setAvailability(new FDCompositeAvailability(map1));

		ProcessActionResult vr = new ProcessActionResult();
		bean.doATPCheck(identity, cart, vr);

		return vr.hasInvalidItems(); **/
		return doATPCheck(identity, new FDStatusAvailability( status, NullAvailability.AVAILABLE ));
	}


	private boolean doATPCheck(final FDIdentity identity,
			FDAvailabilityI availability) throws FDResourceException {
		// PREPARE CART
		FDCartModel cart = new FDTransientCartModel();
		cart.setDeliveryReservation(getFDReservation(identity
				.getErpCustomerPK(), address.getId()));

		Collection<FDCartLineI> cartlines = new ArrayList<FDCartLineI>();
		SimpleCartLine orderLine = null;

		orderLine = new SimpleCartLine();
		orderLine.setProductName("prd1");
		cartlines.add(orderLine);

		cart.addOrderLines(cartlines);

		Map<Object, FDAvailabilityI> map1 = new HashMap<Object, FDAvailabilityI>();
		map1.put(Integer.toString(orderLine.getRandomId()),
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
		
		/**
		Map<String,FDAvailabilityInfo> map1 = new HashMap<String, FDAvailabilityInfo>();
		
		FDCompositeAvailability a = new FDCompositeAvailability(map1);
		cart.setAvailability(a);
		
		FDCartLineI ol = new SimpleCartLine();
		cart.addOrderLine(ol);
		**/
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








class SimpleCartLine implements FDCartLineI {
	private static final long serialVersionUID = 1L;

	private static Random RND = new Random();

	
	private int randomId = RND.nextInt(1000);
	private String cartLineId = Integer.toString(RND.nextInt(16));
	private FDConfigurableI configuration = new FDConfiguration(1.0, "ea");
	private FDSku sku = new FDSku("SKU"+RND.nextInt(1000), 1);
	private String productName;

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
		return 0;
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
		return false;
	}

	@Override
	public boolean isInvalidConfig() {
		return false;
	}

	@Override
	public boolean isKosher() {
		return false;
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
