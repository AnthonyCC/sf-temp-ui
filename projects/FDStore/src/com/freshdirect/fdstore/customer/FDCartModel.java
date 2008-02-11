/*
 * $Workfile:FDCartModel.java$
 *
 * $Date:8/23/2003 7:26:19 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.MunicipalityInfoWrapper;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityHelper;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailabilityInfo;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSource;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.MathUtil;

/**
 * FDShoppingCart model class.
 *
 * @version	$Revision:30$
 * @author	 $Author:Viktor Szathmary$
 * @stereotype fd-model
 */
public class FDCartModel extends ModelSupport implements FDCartI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7672203060098085507L;

	private void checkLimitPlus(int delta) {
		if (numberOfOrderLines() + delta >= ErpServicesProperties.getCartOrderLineLimit()) {
			throw new OrderLineLimitExceededException("Adding " + delta + " additional item" + (delta == 1 ? "" : "s") + " will exceed the limit of " +
					ErpServicesProperties.getCartOrderLineLimit());
		}
	}
	
	private void checkLimitTotal(int total) {
		if (total >= ErpServicesProperties.getCartOrderLineLimit()) {
			throw new OrderLineLimitExceededException("Limit of " + ErpServicesProperties.getCartOrderLineLimit() + " reached");
		}
	}
	

	/** Order cartlines by department desc, description, configuration desc, quantity */
	public final static Comparator LINE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
				///order by Department and then by product.
	FDCartLineI cartLine1 = (FDCartLineI) o1;
			FDCartLineI cartLine2 = (FDCartLineI) o2;
			int retValue = cartLine1.getDepartmentDesc().compareTo(cartLine2.getDepartmentDesc());
			if (cartLine1.getDepartmentDesc().compareTo(cartLine2.getDepartmentDesc()) == 0) {
				retValue = cartLine1.getDescription().compareTo(cartLine2.getDescription());
				if (retValue == 0) {
					retValue = cartLine1.getConfigurationDesc().compareTo(cartLine2.getConfigurationDesc());
					if (retValue == 0) { //dept * desc * configDesc matches, check quantity
						if (cartLine1.getQuantity() <= cartLine2.getQuantity()) {
							retValue = -1;
						} else {
							retValue = 1;
						}
					}
				}
			}
			return retValue;
		}
	};

	/**
	 * @associates <{com.freshdirect.fdstore.customer.FDCartLineI}>
	 * @link aggregationByValue
	 * @clientCardinality 1
	 * @supplierCardinality 0..*
	 */
	private final List orderLines = new ArrayList();

	private final List sampleLines = new ArrayList();

	private final List charges = new ArrayList();

	private List customerCredits = new ArrayList();
	
	private FDReservation deliveryReservation = null;

	private ErpAddressModel deliveryAddress;

	private boolean csrWaivedDeliveryCharge = false;

	private ErpPaymentMethodI paymentMethod;

	private DlvZoneInfoModel zoneInfo;

	private String customerServiceMessage;
	private String marketingMessage;

	private boolean ageVerified = false;

	private List discounts = new ArrayList();
	
	//
	// "transient" fields below
	//

	private final List recentOrderLines = new ArrayList();

	private transient FDAvailabilityI availability;
	//This attribute is to hold the count of deliverypasses this cart holds.
	private int deliveryPassCount = 0;

	//This attribute flag is to denote whether a delivery pass was applied to this cart.
	private boolean dlvPassApplied;
	
	//This attribute flag is to denote whether a delivery promotion was applied to this cart.
	private boolean dlvPromotionApplied;
	
	//This attribute contains the list of unavailable delivery passes to the user.
	private List unavailablePasses;
	
	public boolean isDlvPassApplied() {
		return dlvPassApplied;
	}

	public void setDlvPassApplied(boolean dlvPassApplied) {
		this.dlvPassApplied = dlvPassApplied;
	}

	public boolean isDlvPromotionApplied() {
		return dlvPromotionApplied;
	}

	public void setDlvPromotionApplied(boolean dlvPromotionApplied) {
		this.dlvPromotionApplied = dlvPromotionApplied;
	}

	public int getDeliveryPassCount() {
		return this.deliveryPassCount;
	}

	public void setDeliveryPassCount(int dlvPassCount) {
		this.deliveryPassCount = dlvPassCount;
	}
	public FDCartModel() {
	}

	public FDCartModel(FDCartModel cart) {
		setOrderLines(cart.orderLines);
		setCharges(cart.charges);
		setDiscounts(cart.discounts);
		setCustomerCredits(new ArrayList(cart.customerCredits));

		setZoneInfo(cart.zoneInfo);
		setDeliveryReservation(cart.deliveryReservation);
		setDeliveryAddress(cart.deliveryAddress);
		setPaymentMethod(cart.paymentMethod);
	}

	public void addOrderLine(FDCartLineI orderLine) {
		checkLimitPlus(1);
		this.orderLines.add(orderLine);
		this.recentOrderLines.clear();
		this.recentOrderLines.add(orderLine);
		this.clearAvailability();
	}

	public void addOrderLines(Collection cartLines) {
		checkLimitPlus(cartLines.size());
		this.orderLines.addAll(cartLines);
		this.recentOrderLines.clear();
		this.recentOrderLines.addAll(cartLines);
		this.clearAvailability();
	}

	public void mergeCart(FDCartModel cart) {
		this.addOrderLines(cart.getOrderLines());
	}

	public int numberOfOrderLines() {
		return this.orderLines.size();
	}

	public FDCartLineI getOrderLine(int index) {
		return (FDCartLineI) this.orderLines.get(index);
	}

	/**
	 * Find an orderline by it's randomId.
	 *
	 * @return index of orderline, -1 if not found
	 */
	public int getOrderLineIndex(int randomId) {
		int c = 0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext(); c++) {
			if (randomId == ((FDCartLineI) i.next()).getRandomId()) {
				return c;
			}
		}
		return -1;
	}

	public FDCartLineI getOrderLineById(int randomId) {
		int idx = this.getOrderLineIndex(randomId);
		return idx == -1 ? null : this.getOrderLine(idx);
	}

	public void setOrderLines(List lines) {
		checkLimitTotal(lines.size());
		this.orderLines.clear();
		this.orderLines.addAll(lines);
		this.recentOrderLines.clear();
		this.recentOrderLines.addAll(lines);
		this.clearAvailability();
	}

	public void setOrderLine(int index, FDCartLineI orderLine) {
		this.orderLines.set(index, orderLine);
		this.recentOrderLines.clear();
		this.recentOrderLines.add(orderLine);
		this.clearAvailability();
	}

	public void removeOrderLine(int index) {
		this.orderLines.remove(index);
		this.recentOrderLines.clear();
		this.clearAvailability();
	}

	public boolean removeOrderLineById(int randomId) {
		int idx = this.getOrderLineIndex(randomId);
		if (idx == -1) {
			return false;
		}
		this.removeOrderLine(idx);
		return true;
	}

	/**
	 *  Tell if the shopping cart contains any items that were ordered
	 *  by selecting a recipe.
	 *  
	 *  @return true if the shopping cart contains items ordered through
	 *          a recipe, false otherwise
	 */
	public boolean containsRecipeItems() {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			
			if (line.getRecipeSourceId() != null) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 *  Check if the shopping cart contains items ordered through
	 *  some recipes.
	 *
	 *  @param recipeIds a list of key codes (Strings) of recipes.
	 *  @return true if the shopping cart contains items from all the supplied
	 *          recipes IDs, false otherwise.
	 */
	public boolean containsRecipeItems(List recipeIds) {
		Set ids = new HashSet(recipeIds);
		
		for (Iterator i = this.orderLines.iterator(); i.hasNext() && !ids.isEmpty();) {
			FDCartLineI line     = (FDCartLineI) i.next();
			String      recipeId = line.getRecipeSourceId();
			
			if (recipeId != null) {
				ids.remove(recipeId);
			}
		}
		
		return ids.isEmpty();
	}

	/**
	 *  Check if the shopping cart has recipes from a specific recipe book.
	 *
	 *  @param sourceId the ID of the book to check for.
	 *  @return true if the shopping cart contains items from a recipe from
	 *          the specified book, false otherwise.
	 */
	public boolean containsRecipeFromBook(String sourceId) {
		Set recipesIds = new HashSet();
		
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line     = (FDCartLineI) i.next();
			String      recipeId = line.getRecipeSourceId();
			
			if (recipeId == null) {
				continue;
			}
			if (recipesIds.contains(recipeId)) {
				// check each recipe only once
				continue;
			}
			
			recipesIds.add(recipeId);
			
			Recipe       recipe   = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
			RecipeSource source   = recipe.getSource();
			
			if (source != null && source.getContentKey().getId().equals(sourceId)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Remove all orderlines with a specific recipe source ID.
	 * 
	 * @return number of items removed
	 */
	public int removeOrderLinesByRecipe(String recipeId) {
		int count = 0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (recipeId.equals(line.getRecipeSourceId())) {
				i.remove();
				count++;
			}
		}
		this.recentOrderLines.clear();
		this.clearAvailability();
		return count;
	}

	public void clearOrderLines() {
		this.orderLines.clear();
		this.recentOrderLines.clear();
		this.clearAvailability();
	}

	public List getOrderLines() {
		return Collections.unmodifiableList(this.orderLines);
	}

	public List getSampleLines() {
		return Collections.unmodifiableList(this.sampleLines);
	}

	public void addSampleLine(FDCartLineI orderLine) {
		this.sampleLines.add(orderLine);
	}

	public void clearSampleLines() {
		this.sampleLines.clear();
	}

	/**
	 *  Return the recently ordered line items.
	 *  
	 *  @return an unmodifiable list of FDCartLineModel objects.
	 */
	public List getRecentOrderLines() {
		return Collections.unmodifiableList(this.recentOrderLines);
	}

	/**
	 * Sort orderlines with default comparator.
	 */
	public void sortOrderLines() {
		Collections.sort(this.orderLines, LINE_COMPARATOR);
	}

	public boolean isEstimatedPrice() {
		for (Iterator i = orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.isEstimatedPrice()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return total price of orderlines in USD, with no taxes, charges or discounts applied
	 */
	
	public double getSubTotal() {
		double subTotal = 0.0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			subTotal += MathUtil.roundDecimal(((FDCartLineI) i.next()).getPrice());
		}
		return MathUtil.roundDecimal(subTotal);
	}
	
	/**  
	 * @return total price of orderlines in USD, with taxes, charges without discounts applied
	 */
    
	public double getPreDeductionTotal(){	      
	      double preTotal = 0.0;
	        preTotal += MathUtil.roundDecimal(this.getSubTotal());			
	        preTotal += MathUtil.roundDecimal(this.getTaxValue());
	        preTotal += MathUtil.roundDecimal(this.getDepositValue());

			// apply charges
			for (Iterator i = this.charges.iterator(); i.hasNext();) {
				preTotal += MathUtil.roundDecimal(((ErpChargeLineModel) i.next()).getTotalAmount());
			}
           return MathUtil.roundDecimal(preTotal);
	}

	/**
	 * @return amount of tax USD
	 */
	public double getTaxValue() {
		double taxValue = 0.0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			taxValue += ((FDCartLineI) i.next()).getTaxValue();
		}
		
		for(Iterator i = this.charges.iterator(); i.hasNext(); ) {
			ErpChargeLineModel c = (ErpChargeLineModel) i.next();
			taxValue += c.getTotalAmount() * c.getTaxRate();
		}
		
		return taxValue;
	}

	public double getDepositValue() {
		double depositValue = 0.0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			depositValue += ((FDCartLineI) i.next()).getDepositValue();
		}
		return depositValue;
	}

	/**
	 * @return total price of order in USD, with discounts, taxes, etc
	 */
	public double getTotal() {
		double total = 0.0;
		total += MathUtil.roundDecimal(this.getSubTotal());		
		total += MathUtil.roundDecimal(this.getTaxValue());
		total += MathUtil.roundDecimal(this.getDepositValue());
		// apply charges
		for (Iterator i = this.charges.iterator(); i.hasNext();) {
			total += MathUtil.roundDecimal(((ErpChargeLineModel) i.next()).getTotalAmount());
		}
		total -= this.getTotalDiscountValue();
		total -= this.getCustomerCreditsValue();

		return MathUtil.roundDecimal(total);
	}

	public FDReservation getDeliveryReservation() {
		return this.deliveryReservation;
	}

	public void setDeliveryReservation(FDReservation deliveryReservation) {
		this.deliveryReservation = deliveryReservation;
	}
	
	public String getDeliveryZone() {
		return this.zoneInfo.getZoneCode();
	}

	//
	//
	// charge accessor methods
	//
	//

	public Collection getCharges() {
		return Collections.unmodifiableCollection(this.charges);
	}

	protected void setCharges(Collection charges) {
		this.charges.clear();
		this.charges.addAll(charges);
	}

	public ErpChargeLineModel getCharge(EnumChargeType chargeType) {
		ErpChargeLineModel charge = null;
		for (Iterator i = this.charges.iterator(); i.hasNext();) {
			ErpChargeLineModel curr = (ErpChargeLineModel) i.next();
			if (chargeType.equals(curr.getType())) {
				if (charge != null) {
					throw new RuntimeException("Multiple charges present of type " + chargeType);
				}
				charge = curr;
			}
		}
		return charge;
	}
	
	public void clearCharge(EnumChargeType chargeType) {
		for (Iterator i = this.charges.iterator(); i.hasNext();) {
			ErpChargeLineModel curr = (ErpChargeLineModel) i.next();
			if (chargeType.equals(curr.getType())) {
				i.remove();
			}
		}
	}

	public double getChargeAmount(EnumChargeType chargeType) {
		ErpChargeLineModel charge = this.getCharge(chargeType);
		return charge == null ? 0.0 : charge.getAmount();
	}

	public void setChargeAmount(EnumChargeType chargeType, double amount) {
		ErpChargeLineModel charge = this.getCharge(chargeType);
		if (charge == null) {
			// not found, create it
			charge = new ErpChargeLineModel();
			charge.setType(chargeType);
			this.charges.add(charge);
		}
		charge.setAmount(amount);
	}

	public boolean isChargeWaived(EnumChargeType chargeType) {
		ErpChargeLineModel charge = this.getCharge(chargeType);
		return charge == null ? false : charge.getDiscount() != null;
	}

	public boolean isChargeTaxable(EnumChargeType chargeType) {
		ErpChargeLineModel charge = this.getCharge(chargeType);
		return charge == null ? false : charge.getTaxRate() > 0;
	}

	public void setChargeWaived(EnumChargeType chargeType, boolean waived, String promotionCode) {
		ErpChargeLineModel charge = this.getCharge(chargeType);
		if (charge == null) {
			// not found, create it
			charge = new ErpChargeLineModel();
			charge.setType(chargeType);
			this.charges.add(charge);
		}
		charge.setDiscount(waived ? new Discount(promotionCode, EnumDiscountType.PERCENT_OFF, 1.0) : null);

		// MISCELLANEOUS charge waiving is pegged to DELIVERY
		if (EnumChargeType.DELIVERY.equals(chargeType)) {
			setChargeWaived(EnumChargeType.MISCELLANEOUS, waived, promotionCode);
		}
	}

	//
	//
	// simplified charge accessor methods
	//
	//

	public double getDeliverySurcharge() {
		return this.getChargeAmount(EnumChargeType.DELIVERY);
	}

	public boolean isDeliveryChargeWaived() {
		return this.isChargeWaived(EnumChargeType.DELIVERY);
	}
	
	public boolean isDeliveryChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.DELIVERY);
	}
	
	public double getPhoneCharge() {
		return this.getChargeAmount(EnumChargeType.PHONE);
	}
	
	public boolean isPhoneChargeWaived() {
		return this.isChargeWaived(EnumChargeType.PHONE);
	}

	public boolean isPhoneChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.PHONE);
	}

	public double getMiscellaneousCharge() {
		return this.getChargeAmount(EnumChargeType.MISCELLANEOUS);
	}

	public boolean isMiscellaneousChargeWaived() {
		return this.isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	public boolean isMiscellaneousChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.MISCELLANEOUS);
	}

	public double getCCDeclinedCharge() {
		return this.getChargeAmount(EnumChargeType.CC_DECLINED);
	}
	
	//
	//
	//

	public ErpAddressModel getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(ErpAddressModel deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public boolean isCsrWaivedDeliveryCharge() {
		return csrWaivedDeliveryCharge;
	}

	public void setCsrWaivedDeliveryCharge(boolean b) {
		csrWaivedDeliveryCharge = b;
	}

	public boolean isAddressMismatch() {
		if (this.deliveryAddress == null || this.paymentMethod == null) {
			return false;
		}

		if (this.deliveryAddress instanceof ErpDepotAddressModel) {
			return false;
		}

		ErpPaymentMethodI paymentMethod = (ErpPaymentMethodI) this.paymentMethod;
		AddressModel billAddr = paymentMethod.getAddress();

		return !this.deliveryAddress.isSameLocation(billAddr);
	}

	public ErpPaymentMethodI getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(ErpPaymentMethodI paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public FDAvailabilityI getAvailability() {
		return this.availability == null ? NullAvailability.AVAILABLE : this.availability;
	}

	public void setAvailability(FDAvailabilityI availability) {
		this.availability = availability;
	}
	
	private void clearAvailability() {
		this.availability = null;
	}
	
	/**
	 * @return Map of cartLineId -> FDAvailabilityInfo of all unavailable lines for req. date
	 */
	public Map getUnavailabilityMap() {
		if(this.deliveryReservation == null){
			return Collections.EMPTY_MAP;
		}
		
		DateRange requestedRange = new DateRange(deliveryReservation.getStartTime(), deliveryReservation.getEndTime());
		FDAvailabilityInfo info = this.getAvailability().availableCompletely(requestedRange);
		if (info.isAvailable()) {
			return Collections.EMPTY_MAP;
		}
		return ((FDCompositeAvailabilityInfo)info).getComponentInfo();
	}
	
	/**
	 * @return first date within horizon days on which whole cart is available null if no date
	 */
	public Date getFirstAvailableDate() {
		return FDAvailabilityHelper.getFirstAvailableDate(this.getAvailability());
	}

	public Set getRestrictionMessages() {
		Set rMsgs = new HashSet();
		for(Iterator i = this.getUnavailabilityMap().values().iterator(); i.hasNext(); ){
			Object o = i.next();
			if(o instanceof FDRestrictedAvailabilityInfo){
				FDRestrictedAvailabilityInfo info = (FDRestrictedAvailabilityInfo)o;
				rMsgs.add(info.getRestriction().getMessage());
			}
		}
		return rMsgs;
	}

	public boolean hasKosherRestrictions() {
		for(Iterator i = this.getUnavailabilityMap().values().iterator(); i.hasNext(); ){
			Object o = i.next();
			if(o instanceof FDRestrictedAvailabilityInfo){
				FDRestrictedAvailabilityInfo info = (FDRestrictedAvailabilityInfo)o;
				if(EnumDlvRestrictionReason.KOSHER.equals(info.getRestriction().getReason())){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isFullyAvailable() {
		return this.getUnavailabilityMap().isEmpty();
	}

	public void setCustomerCredits(List credits) {
		this.customerCredits = credits;
	}

	public List getCustomerCredits() {
		return this.customerCredits;
	}
	
	public double getCustomerCreditsValue() {
		double creditAmount = 0.0;
		for (Iterator it = this.customerCredits.iterator(); it.hasNext();) {
			ErpAppliedCreditModel creditModel = (ErpAppliedCreditModel) it.next();
			creditAmount += MathUtil.roundDecimal(creditModel.getAmount());
		}
		return MathUtil.roundDecimal(creditAmount);
	}

	public DlvZoneInfoModel getZoneInfo() {
		return this.zoneInfo;
	}

	/**
	 * Set the zone info, update delivery charge from zoneInfo.
	 */
	public void setZoneInfo(DlvZoneInfoModel zoneInfo) {
		this.zoneInfo = zoneInfo;
		//this.setChargeAmount(EnumChargeType.DELIVERY, zoneInfo == null ? 0.0 : zoneInfo.getDeliveryCharges());
	}

	public String getCustomerServiceMessage() {
		return this.customerServiceMessage;
	}
	public void setCustomerServiceMessage(String s) {
		this.customerServiceMessage = s;
	}

	public String getMarketingMessage() {
		return this.marketingMessage;
	}
	public void setMarketingMessage(String s) {
		this.marketingMessage = s;
	}
	
	public String getDeliveryInstructions(){
		if(this.deliveryAddress != null){
			return this.deliveryAddress.getInstructions();
		}
		return "";
	}

	public boolean isAgeVerified() {
		return ageVerified;
	}

	public void setAgeVerified(boolean ageVerified) {
		this.ageVerified = ageVerified;
	}

	public boolean hasItemsFrom(String[] contentNames) throws FDResourceException {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			ProductModel pm = ContentFactory.getInstance().getProductByName(line.getCategoryName(), line.getProductName());
			if (pm.hasParentWithName(contentNames)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasBrandName(String brandName) throws FDResourceException {
		Set brands = new HashSet();
		brands.add(brandName);
		return this.hasBrandName(brands);
	}

	public boolean hasBrandName(Set brandNames) throws FDResourceException {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			ProductModel pm = ContentFactory.getInstance().getProductByName(line.getCategoryName(), line.getProductName());
			List brands = pm.getBrands();
			for (Iterator brandi = brands.iterator(); brandi.hasNext();) {
				BrandModel brand = (BrandModel) brandi.next();
				if (brandNames.contains(brand.getContentName())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsAlcohol() {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.isAlcohol()) {
				return true;
			}
		}
		return false;
	}

	public double getSubTotalWithoutAlcohol() {
		double subTotal = 0.0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (!line.isAlcohol()) {
				subTotal += line.getPrice();
			}
		}

		return subTotal;
	}

	public void removeAlcoholicLines() {
		for (ListIterator i = this.orderLines.listIterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.isAlcohol()) {
				i.remove();
			}
		}
	}

	public boolean containsKosherItems() {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.isKosher()) {
				return true;
			}
		}
		return false;
	}

	public Set getApplicableRestrictions() {
		Set restrictions = new HashSet();
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			restrictions.addAll(line.getApplicableRestrictions());
		}
		return restrictions;
	}

	/**
	 * Get the total quantity of all occurrences of a product in the cart.
	 */
	public double getTotalQuantity(ProductModel product) {
		String categoryName = product.getParentNode().getContentName();
		String productName = product.getContentName();
		double sum = 0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (productName.equals(line.getProductName()) && categoryName.equals(line.getCategoryName())) {
				sum += line.getQuantity();
			}
		}
		return sum;
	}

	/**
	 * Remove stale orderlines, reprice w/ current prices, finally sort the cart.
	 */
	public void doCleanup() throws FDResourceException {
		if (this.numberOfOrderLines() == 0) {
			return;
		}

		List cleanLines = OrderLineUtil.update(this.orderLines,true);

		this.setOrderLines(cleanLines);
		this.sortOrderLines();
	}

	public void refreshAll() throws FDResourceException, FDInvalidConfigurationException {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI) i.next();
			cartLine.refreshConfiguration();
		}
	}

	/*	This method is obsolete. Moved the logic to AdjustAvailabilityTag
	 * *//**
		 * Remove/adjust unavailable orderlines
		 *//*
		public void adjustAvailability() {
			Map unavMap = this.getUnavailabilityMap();
			for (Iterator i = unavMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				Integer key = (Integer) entry.getKey();
				FDAvailabilityInfo info = (FDAvailabilityInfo) entry.getValue();

				if (info instanceof FDStockAvailabilityInfo) {
					FDStockAvailabilityInfo sInfo = (FDStockAvailabilityInfo) info;
					if (sInfo.getQuantity() == 0) {
						// remove
						this.removeOrderLine(this.getOrderLineIndex(key.intValue()));
					} else {
						// adjust quantity
						FDCartLineI cartline = this.getOrderLineById(key.intValue());
						cartline.setQuantity(sInfo.getQuantity());
					}
				} else {
					// remove
					this.removeOrderLine(this.getOrderLineIndex(key.intValue()));
				}
			}
			try {
				this.refreshAll();
			} catch (FDException e) {
				// !!! improve error handling
				throw new FDRuntimeException(e);
			}
		}*/

	//
	// order views
	//
	
	public WebOrderViewI getOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getOrderView(orderLines, affiliate);
	}

	public List getOrderViews() {
		return WebOrderViewFactory.getOrderViews(orderLines);
	}
	
	public void recalculateTaxAndBottleDeposit(String zipcode) throws FDResourceException{
		double taxRate = 0.0;
		double depositRate = 0.0;
		
		FDDeliveryManager fdMan = FDDeliveryManager.getInstance();
		
		MunicipalityInfoWrapper miw = fdMan.getMunicipalityInfos();
		MunicipalityInfo mi = null;
		
		if(deliveryAddress != null){	
			String county = fdMan.getCounty(deliveryAddress);
			mi = miw.getMunicipalityInfo(deliveryAddress.getState(), county, deliveryAddress.getCity());
		} else {
			mi = miw.getMunicipalityInfo(fdMan.lookupStateByZip(zipcode), fdMan.lookupCountyByZip(zipcode), null);
		}
		
		//System.out.println(" MunicipalityInfo :"+mi.getTaxRate());
		if(mi != null){
			taxRate = mi.getTaxRate();
			depositRate = mi.getBottleDeposit();
		}
	
		for(Iterator i = this.orderLines.iterator(); i.hasNext();){
			FDCartLineI cartline = (FDCartLineI) i.next();
			cartline.setTaxRate(taxRate);
			cartline.setDepositValue(depositRate);
		}
	}

	public List getDiscounts() {
		return this.discounts;
	}

	public void setDiscounts(List discounts) {
		this.discounts = discounts;
	}
	
	public void addDiscount(ErpDiscountLineModel discount) {
		this.discounts.add(discount);
	}

	public void addDiscount(Discount discount) {
		this.discounts.add(new ErpDiscountLineModel(discount));
	}

	public double getTotalDiscountValue() {
		double discountValue = 0.0;
		if (this.discounts != null && this.discounts.size() > 0) {
			for (Iterator iter = this.discounts.iterator(); iter.hasNext();) {
				ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
				discountValue += MathUtil.roundDecimal(discountLine.getDiscount().getAmount());
			}
		}
		return MathUtil.roundDecimal(discountValue);
	}
	public void handleDeliveryPass() {
		int count = 0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if(line.lookupFDProduct().isDeliveryPass()){
				count++;
			}
			
		}
		setDeliveryPassCount(count);
		if (this.getDeliverySurcharge() == 0.0) {
			//If there is no applicable delivery charge then return;
			return;
		}//otherwise
		if(getDeliveryPassCount() > 0 ) {
			//Cart contains delivery pass. So Waive delivery fee.
			if(!this.isChargeWaived(EnumChargeType.DELIVERY)){
				//If not already waived. The charge is already waived because
				//of a delivery promotion in which case promotion takes the
				//precedence.
				setChargeWaived(EnumChargeType.DELIVERY, true, DlvPassConstants.PROMO_CODE);	
				this.setDlvPassApplied(true);
			}

		}else{
			//Either Cart contains no delivery pass or the pass was removed. So apply delivery fee
			//only if delivery promotion was not applied.
			if(!this.isDlvPromotionApplied()){
				//If delivery promotion was applied, do not revoke the waiving of dlv charge.
				setChargeWaived(EnumChargeType.DELIVERY, false, DlvPassConstants.PROMO_CODE);
			}
			this.setDlvPassApplied(false);
		}
	}
	
	public boolean containsDlvPassOnly(){
		if(getDeliveryPassCount() > 0 && getDeliveryPassCount() == this.getOrderLines().size()){
			//Cart contains only delivery pass.
			return true;
		}else{
			return false;
		}
	}

	public List getUnavailablePasses() {
		return unavailablePasses;
	}

	public void setUnavailablePasses(List unavailablePasses) {
		this.unavailablePasses = unavailablePasses;
	}
	
	/**
	 * This method returns true or false if the delivery pass was already applied
	 * when the order was created or previously modified. This method is exclusively
	 * return for modify orders and used in handleDeliveryPass method.
	 * FDCartModel class has a default implementation. The actual implementation is
	 * provided by FDModifyCartModel.
	 * @return
	 */
	public boolean isDlvPassAlreadyApplied(){
		return false;
	}
	
	public boolean containsUnlimitedPass(){
		boolean contains = false;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if(line.lookupFDProduct().isDeliveryPass()){
				String skuCode = line.getSkuCode();
				if(DeliveryPassType.getEnum(skuCode).isUnlimited()){
					contains = true;
					break;
				}
			}
		}
		return contains;
	}

	/*
	 * Returns true if an item in the cart has the material attriubte of AOF (Advance Order Flag)
	 */
	
	public boolean hasAdvanceOrderItem() {
		boolean hasAOItem = false;

		for (Iterator i = this.orderLines.iterator(); i.hasNext() && !hasAOItem;) {
			FDCartLineI line = (FDCartLineI) i.next();
			hasAOItem = line.hasAdvanceOrderFlag();
		}
		return hasAOItem;
	}
	
	/*
	 * Check if cart is Empty.
	 */
	public boolean isEmpty() {
		return this.getOrderLines().isEmpty();
	}
	
	/*
	 * Get the product keys of the line items.
	 */
	public Set getProductKeysForLineItems() {
		Set prodKeysInCart = new HashSet();
		for (Iterator i = this.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI) i.next();
			ProductModel model = cartLine.getProductRef().lookupProduct();
			String productId = model.getContentKey().getId();
			prodKeysInCart.add(productId);
		}
		return prodKeysInCart;
	}
	/**
	 * This returns a redeem sample promo desc if any otherwise returns Empty String.
	 * @return java.lang.String.
	 */
	public String getRedeemedSampleDescription() {
		String desc = "NONE";
		//Show any redeemed sample line if any.
		if ( this.sampleLines != null && this.sampleLines.size() > 0) {
			for(Iterator i = this.sampleLines.iterator(); i.hasNext();){
				FDCartLineI cartLine = (FDCartLineI)i.next();
				Discount discount =  cartLine.getDiscount();
				String code = discount.getPromotionCode();
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(code);
				if (promotion != null && promotion.isRedemption()) {
					desc = promotion.getDescription();
				}
			}
		}
		return desc;
	}

}