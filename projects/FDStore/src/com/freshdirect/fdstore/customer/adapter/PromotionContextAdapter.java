package com.freshdirect.fdstore.customer.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.DCPDPromoProductCache;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.EnumOrderType;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.DCPDiscountRule;
import com.freshdirect.fdstore.promotion.FDPromotionVisitor;
import com.freshdirect.fdstore.promotion.PromotionContextI;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PromotionContextAdapter implements PromotionContextI {

	private final FDUserI user;
	private List rulePromoCodes;
	private Date now;
	private static Category LOGGER = LoggerFactory.getInstance(PromotionContextAdapter.class);


	public PromotionContextAdapter(FDUserI user) {		
		this.user = user;
		now = new Date();
	}
	
	
	/**
	 * @return total price of orderlines in USD, with taxes, charges without discounts applied
	 */
	public double getPreDeductionTotal(){
		return this.user.getShoppingCart().getPreDeductionTotal();
	}

	public double getSubTotal() {
		return this.user.getShoppingCart().getSubTotal();
	}

	public void addSampleLine(FDCartLineI cartLine) {
		this.user.getShoppingCart().addSampleLine(cartLine);
	}

	public boolean isAddressMismatch() {
		return this.user.getShoppingCart().isAddressMismatch();
	}

	public void setPromotionAddressMismatch(boolean b) {
		this.user.setPromotionAddressMismatch(b);
	}

	public void setSignupDiscountRule(SignupDiscountRule discountRule) {
		this.user.setSignupDiscountRule(discountRule);
	}

	public String getModifiedSaleId() {
		FDCartModel cart = user.getShoppingCart();

		if (cart instanceof FDModifyCartModel) {
			return ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
		}
		return null;
	}

	public int getPromotionUsageCount(String promotionCode) {
		String ignoreSaleId = this.getModifiedSaleId();
		try {
			return user.getPromotionHistory().getPromotionUsageCount(promotionCode, ignoreSaleId);
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public String getSubscribedSignupPromotionCode() {
		String ignoreSaleId = this.getModifiedSaleId();
		Set promoCodes = PromotionFactory.getInstance().getPromotionCodesByType(EnumPromotionType.SIGNUP);
		try {

			Set allPromos = user.getPromotionHistory().getUsedPromotionCodes(ignoreSaleId);
			promoCodes.retainAll(allPromos);

			if (promoCodes.isEmpty()) {
				return null;
			}
			return (String) promoCodes.iterator().next();

		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public String getZipCode() {
		ErpAddressModel addr = this.user.getShoppingCart().getDeliveryAddress();
		if (addr != null) {
			if (addr instanceof ErpDepotAddressModel) {
				return null;
			}
			return addr.getZipCode();
		}
		return this.user.getZipCode();
	}

	public String getDepotCode() {
		ErpAddressModel addr = this.user.getShoppingCart().getDeliveryAddress();
		if (addr != null && addr instanceof ErpDepotAddressModel) {
			String locationId = ((ErpDepotAddressModel) addr).getLocationId();
			DlvDepotModel depot;
			try {
				depot = FDDepotManager.getInstance().getDepotByLocationId(locationId);
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}
			return depot.getDepotCode();
		}
		return user.getDepotCode();
	}

	public PromotionI getRedeemedPromotion() {
		return this.user.getRedeemedPromotion();
	}

	public boolean isFraudulent() {
		try {
			return this.user.isFraudulent();
		} catch (FDResourceException e) {
			throw new FDRuntimeException();
		}
	}

	public FDCartModel getShoppingCart() {
		return this.user.getShoppingCart();
	}

	public int getAdjustedValidOrderCount() {
		try {
			return this.user.getAdjustedValidOrderCount();
		} catch (FDResourceException e) {
			throw new FDRuntimeException();
		}
	}

	public double getApplicableSignupAmount(double amount, double maxAmountPerSku) {
		return OrderPromotionHelper.getApplicableSignupAmount(this.user.getShoppingCart(), amount, maxAmountPerSku);
	}

	public EnumOrderType getOrderType() {
		ErpAddressModel address = this.user.getShoppingCart().getDeliveryAddress();
		if (address != null) {
			if (address instanceof ErpDepotAddressModel) {
				if (((ErpDepotAddressModel) address).isPickup()) {
					return EnumOrderType.PICKUP;
				}
				return EnumOrderType.DEPOT;
			}
			if (EnumServiceType.CORPORATE.equals(address.getServiceType())) {
				return EnumOrderType.CORPORATE;
			}

			return EnumOrderType.HOME;
		}

		// no address, work out from user
		if (user.isPickupOnly()) {
			return EnumOrderType.PICKUP;
		}

		if (user.isCorporateUser()) {
			return EnumOrderType.CORPORATE;
		}

		if (user.isDepotUser()) {
			return EnumOrderType.DEPOT;
		}

		return EnumOrderType.HOME;
	}

	public boolean hasProfileAttribute(String attributeName, String desiredValue) {
		try {
			if (user.getIdentity() == null)
				return false;
			ProfileModel pm = user.getFDCustomer().getProfile();
			if (pm == null)
				return false;

			String attribValue = pm.getAttribute(attributeName);
			if (desiredValue == null)
				return attribValue != null;
			return (desiredValue.equalsIgnoreCase(attribValue));
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public FDIdentity getIdentity() {
		return user.getIdentity();
	}

	public void setRulePromoCode(List rulePromoCodes) {
		this.rulePromoCodes = rulePromoCodes;

	}

	public boolean hasRulePromoCode(String promoCode) {
		if(this.rulePromoCodes.isEmpty()) {
			return false;
		}
		return this.rulePromoCodes.contains(promoCode);
	}

	public FDUserI getUser() {
		return user;
	}

	public void addDiscount(Discount discount) {
		this.user.getShoppingCart().addDiscount(discount);
	}

	public Date getCurrentDate() {
		return now;
	}
	public AssignedCustomerParam getAssignedCustomerParam(String promoId){
		return this.user.getAssignedCustomerParam(promoId);
	}

	public List getEligibleLinesForDCPDiscount(String promoId, Set contentKeys){
		if(getShoppingCart().isEmpty()){
			return Collections.EMPTY_LIST;
		}
		List eligibleLines = new ArrayList();

		List orderLines = getShoppingCart().getOrderLines();
		for(Iterator i = orderLines.iterator(); i.hasNext();){
			FDCartLineI cartLine = (FDCartLineI)i.next();
			boolean eligible = false;
			String recipeSourceId = cartLine.getRecipeSourceId();
			if(recipeSourceId != null && recipeSourceId.length() > 0){
				////Check if the line item is eligible for a recipe discount.
				eligible = OrderPromotionHelper.isRecipeEligible(recipeSourceId, contentKeys);
			}
			if(!eligible){
				ProductModel model = cartLine.getProductRef().lookupProduct();
				String productId = model.getContentKey().getId();
				DCPDPromoProductCache dcpdCache = this.user.getDCPDPromoProductCache();
				//Check if the line item product is already evaluated.
				if(dcpdCache.isEvaluated(productId, promoId)){
					eligible = dcpdCache.isEligible(productId, promoId);
				}else{
					//Check if the line item is eligible for a category or department discount.
					eligible = OrderPromotionHelper.evaluateProductForDCPDPromo(model, contentKeys);
					//Set the eligiblity info to user session.
					dcpdCache.setPromoProductInfo(productId, promoId, eligible);
				}
			}
			if(eligible){
				//Cartline is eligible for discount.
				eligibleLines.add(cartLine);
			}
		}
		return eligibleLines;
	}
	
	public boolean applyHeaderDiscount(String promoCode, double promotionAmt, EnumPromotionType type){
		//Poll the promotion context to know if this is the max discount amount.
		if(this.isMaxDiscountAmount(promotionAmt, type)){
			//Clear any existing discount.
			this.clearHeaderDiscounts();
			//Add this discount.
			Discount discount = new Discount(promoCode, EnumDiscountType.DOLLAR_OFF, promotionAmt);
			this.addDiscount(discount);
			return true;
		}
		return false;
	}

	public void clearHeaderDiscounts(){
		//Clear all header discounts.
		this.user.getShoppingCart().setDiscounts(new ArrayList());
	}
	private boolean isMaxDiscountAmount(double promotionAmt, EnumPromotionType type){
		Discount applied = this.getHeaderDiscount();
		if(applied == null) return true;
		boolean flag = false;
		String appliedCode = applied.getPromotionCode();
		PromotionI appliedPromo = PromotionFactory.getInstance().getPromotion(appliedCode);
		EnumPromotionType appliedType = appliedPromo.getPromotionType();
		if((type.getPriority() < appliedType.getPriority()) ||
				(type.getPriority() == appliedType.getPriority() &&
						promotionAmt > applied.getAmount())){
			//The applied promo priority is less than the one that is being applied.
			//or the applied promo amount is less than the one that is being applied.
			flag = true;
		}
		return flag;
	}

	public Discount getHeaderDiscount() {
		// TODO Auto-generated method stub
		List l = this.getShoppingCart().getDiscounts();
		if(l.isEmpty())
			return null;

		Iterator i = l.iterator();
		//Get the applied discount from the cart.
		ErpDiscountLineModel model = (ErpDiscountLineModel)i.next();
		if(model==null) return null;
		Discount applied = model.getDiscount();
		return applied; 
	}
	
	
	public Map getPromoVariantMap() {
		return this.getUser().getPromoVariantMap();
	}
	
	public boolean isPostPromoConflictEnabled(){
		return this.getUser().isPostPromoConflictEnabled();
	}


	public void clearLineItemDiscounts() {
		// TODO Auto-generated method stub
		  FDCartModel cart= this.getUser().getShoppingCart();
		  cart.clearLineItemDiscounts();
		  try {					
				cart.refreshAll();
		  } catch (FDInvalidConfigurationException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new FDRuntimeException(e);		
		} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				throw new FDRuntimeException(e);
		}														
	}


	public double getTotalLineItemDiscount() {
		// TODO Auto-generated method stub
		FDCartModel cart= this.getUser().getShoppingCart();
		return cart.getTotalLineItemsDiscountAmount();
	}
	
}