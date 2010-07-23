package com.freshdirect.fdstore.customer;

import java.util.Collection;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDReservation;

public interface FDCartI extends java.io.Serializable {

	public int numberOfOrderLines();

	public FDCartLineI getOrderLine(int index);

	public List<FDCartLineI> getOrderLines();
	
	public List<FDCartLineI> getSampleLines();
	
	public FDReservation getDeliveryReservation();
	
	public String getDeliveryZone();

	public ErpAddressModel getDeliveryAddress();

	public ErpPaymentMethodI getPaymentMethod();

	public double getDeliverySurcharge();
	public boolean isDeliveryChargeWaived();
	public boolean isDeliveryChargeTaxable();
	
	public boolean isPhoneChargeWaived();
	public boolean isPhoneChargeTaxable();

	public boolean isChargeWaived(EnumChargeType chargeType);

	public double getCustomerCreditsValue();

	/**
	 * @return collection of ErpChargeLineModel objects
	 */
	public Collection<ErpChargeLineModel> getCharges();

	public double getPhoneCharge();

	public double getMiscellaneousCharge();
	public boolean isMiscellaneousChargeWaived();
	public boolean isMiscellaneousChargeTaxable();

	public double getCCDeclinedCharge();

	public boolean isEstimatedPrice();
	
	/**
	 * @return total price of orderlines in USD, with taxes, charges without discounts applied
	 */
    	
	public double getPreDeductionTotal();
	
	/**
	 * @return total price of orderlines in USD, with no taxes and promotions applied
	 */
	public double getSubTotal();
	
	/**
	 * @return amount of tax USD
	 */
	public double getTaxValue();
	
	public double getDepositValue();

	/**
	 * @return total price of order in USD, with promotions, taxes, etc
	 */
	public double getTotal();

	public String getCustomerServiceMessage();

	public String getMarketingMessage();
	
	public String getDeliveryInstructions();

	//
	// order views (by affiliate)
	//
	
	/** @return List of WebOrderViewI */
	public List<WebOrderViewI> getOrderViews();

	public WebOrderViewI getOrderView(ErpAffiliate affiliate);

	public List<ErpDiscountLineModel> getDiscounts();
	
	public double getTotalDiscountValue();
	
	public String getRedeemedSampleDescription();
	
	public int getLineItemDiscountCount(String promoCode);
	
	public double getTotalLineItemsDiscountAmount();
	
	public double getBufferAmt();
	
	public double getTotalAppliedGCAmount();
	
	public double getCCPaymentAmount();

	public boolean isDiscountInCart(String promoCode);
	
	public double getLineItemDiscountAmount(String promoCode);
	
	public double getDiscountValue(String promoCode);
	
}
