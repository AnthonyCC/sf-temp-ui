/*
 * $Workfile:FDCartLineModel.java$
 *
 * $Date:6/30/2003 5:28:13 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.sap.PosexUtil;

/**
 *
 * @version    $Revision:16$
 * @author     $Author:Kashif Nadeem$
 * @stereotype fd-model
 */
public class FDCartLineModel extends AbstractCartLine {

	private EnumEventSource source;
	//private Set eligiblePromos = new HashSet();
	//private String recommendedPromoCode;
	
	public FDCartLineModel(ErpOrderLineModel orderLine) {
		this(orderLine, null, null, null);
	}

	public FDCartLineModel(
		ErpOrderLineModel orderLine,
		ErpInvoiceLineI firstInvoiceLine,
		ErpInvoiceLineI lastInvoiceLine,
		ErpReturnLineModel returnLine) {
		super(orderLine, firstInvoiceLine, lastInvoiceLine, returnLine);
	}

	public FDCartLineModel(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String variantId, String pZoneId) {
		super(sku, productRef, configuration, variantId, pZoneId);
		this.orderLine.setCartlineId(ID_GENERATOR.getNextId());
	}
	
	public FDCartLineModel(FDSku sku, ProductModel productRef, FDConfigurableI configuration, String cartlineId, String recipeSourceId, boolean requestNotification, String variantId, String pZoneId) {
		super(sku, productRef, configuration, variantId, pZoneId);
		this.orderLine.setCartlineId(cartlineId);
		this.orderLine.setRecipeSourceId(recipeSourceId);
		this.orderLine.setRequestNotification(requestNotification);
		this.orderLine.setVariantId(variantId);
	}

	public List buildErpOrderLines(int baseLineNumber) throws FDResourceException, FDInvalidConfigurationException {
		this.refreshConfiguration();
		ErpOrderLineModel ol = (ErpOrderLineModel) this.orderLine.deepCopy();
      
		try {
			if(ol.getSku()!=null){

				FDProductInfo productInfo = FDCachedFactory.getProductInfo(ol.getSku().getSkuCode());
				EnumOrderLineRating rating=EnumOrderLineRating.getEnumByStatusCode(productInfo.getRating());
				ol.setProduceRating(rating);
				ol.setBasePrice(productInfo.getZonePriceInfo(getPricingContext().getZoneId()).getSellingPrice());
				ol.setBasePriceUnit(productInfo.getDefaultPriceUnit());	
				//ol.setUserZoneId(getPricingContext().getZoneId());				
				ol.setPricingZoneId(productInfo.getZonePriceInfo(getPricingContext().getZoneId()).getSapZoneId());
//				Promotion p= (Promotion) PromotionFactory.getInstance().getPromotion("SORI_TEST");
//				PercentOffApplicator app = (PercentOffApplicator)p.getApplicator();
//				double discUnitPrice =  (ol.getPrice()/ol.getQuantity()) * app.getPercentOff();
//				Discount dis=new Discount("SORI_TEST",EnumDiscountType.DOLLAR_OFF,discUnitPrice );
//				
//				System.out.println("discUnitPrice111 : "+discUnitPrice);
				
				//ol.setDiscount(dis);
				
			}			
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FDSkuNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ol.setOrderLineNumber(PosexUtil.getPosex(baseLineNumber));
      
		List ols = new ArrayList(1);
		ols.add(ol);
		return ols;
	}

	public int getErpOrderLineSize() {
		return 1;
	}

	public FDCartLineI createCopy() {
		FDCartLineModel newLine = new FDCartLineModel(this.getSku(), this
				.getProductRef().lookupProductModel(), this.getConfiguration(), this.getVariantId(), this.getPricingContext().getZoneId());
		newLine.setRecipeSourceId(this.getRecipeSourceId());
		newLine.setRequestNotification(this.isRequestNotification());
		newLine.setSource(this.source);
		return newLine;
	}
	public void setOrderLineId(String orderLineId){
		this.orderLine.setOrderLineId(orderLineId);
	}

	/**
	 *  Set the source of the event.
	 *  
	 *  @param source the part of the site this event was generated from.
	 */
	public void setSource(EnumEventSource source) {
		this.source = source;
	}
	
	/**
	 *  Get the source of the event.
	 *  
	 *  @return the part of the site this event was generated from.
	 */
	public EnumEventSource getSource() {
		return source;
	}
/*
	public Set getEligiblePromoCodes() {
		return eligiblePromos;
	}

	public void setEligiblePromoCode(String promoCode, boolean eligible) {
		if (eligible) {
			this.eligiblePromos.add(promoCode);
		} else {
			this.eligiblePromos.remove(promoCode);
		}
	}

	public void setRecommendedPromoCode(String promoCode) {
		// TODO Auto-generated method stub
		this.recommendedPromoCode=promoCode;
	}

	public String getRecommendedPromoCode() {
		// TODO Auto-generated method stub
		return this.recommendedPromoCode;
	}

	*/
	public void setSavingsId(String savingsId){
		this.orderLine.setSavingsId(savingsId);
	}
	
	public String getSavingsId(){
		return this.orderLine.getSavingsId();
	}
	
	public void removeLineItemDiscount(){
		this.setDiscountAmount(0.0);
		this.setDiscount(null);
		//this.setDiscountApplied(false);	
		//this.setSavingsId(null);
	}

	public boolean hasDiscount(String promoCode) {
		if(this.getDiscount() != null && this.getDiscount().getPromotionCode().equals(promoCode)) {
			return true;	
		}
		return false;
	}

 
	
}
