package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDSearchCriteria;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;

public class LineItemDiscountApplicator implements PromotionApplicatorI {
   
	private double minSubTotal=0.0;
	private double percentOff=0.0;
	/*
	 * List of line item strategies to determine the eligibility of a line item before
	 * applying discount.
	 */
	private List lineItemStrategies = new ArrayList();
	
	public LineItemDiscountApplicator(double minAmount,double percentoff) { //, int maxItemCount,boolean applyHeaderDiscount){
		this.minSubTotal=minAmount;
		this.percentOff=percentoff;
	}
	
	public double getMinSubtotal() {
		return this.minSubTotal;
	}
	public void addLineItemStrategy(LineItemStrategyI strategy) {
		this.lineItemStrategies.add(strategy);
		Collections.sort(this.lineItemStrategies, PRECEDENCE_COMPARATOR);
	}
	
	private final static Comparator PRECEDENCE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			
			int p1 = ((LineItemStrategyI) o1).getPrecedence();
			int p2 = ((LineItemStrategyI) o2).getPrecedence();
			return p1 - p2;
		}
	};
	
	public boolean apply(String promotionCode, PromotionContextI context) {
		if (context.getPreDeductionTotal() < this.minSubTotal) {
			return false;
		}
		
		FDCartModel cart= context.getShoppingCart();
		List orderLines=cart.getOrderLines();
        Map recommendedItemMap=new HashMap();
        
		if(orderLines!=null){	
			for(int i=0;i<orderLines.size();i++) {
				  FDCartLineI model=(FDCartLineI)orderLines.get(i);
				  if(model.isDiscountApplied()){
						boolean e = evaluate(model, promotionCode, context);
						if(e) {
							Discount dis=new Discount(promotionCode,EnumDiscountType.PERCENT_OFF,percentOff);
							model.setDiscount(dis);
							String savingsId = model.getSavingsId();
							if(savingsId == null){
								//If SavingsID is null check if the variant id is not null.
								savingsId  = model.getVariantId();
							}
							String productId = model.getProductRef().lookupProduct().getContentKey().getId();							
							recommendedItemMap.put(productId, savingsId);
						}		
				  }
			}
			//Now run through all recently added items.
			for(int i=0;i<orderLines.size();i++) {		
				FDCartLineI model=(FDCartLineI)orderLines.get(i);
				if(!model.isDiscountApplied()) {
					  	boolean e = evaluate(model, promotionCode, context);
						if(e) {
							Discount dis=new Discount(promotionCode,EnumDiscountType.PERCENT_OFF,percentOff);
							model.setDiscount(dis);
							String productId = model.getProductRef().lookupProduct().getContentKey().getId();
							recommendedItemMap.put(productId, model.getVariantId());
							model.setDiscountApplied(true);
						}		
						
				  }
			}
		    // now apply discount to any duplicate sku from the recommended List
						   
			if(orderLines.size()>0){
				for(int i=0;i<orderLines.size();i++){
					FDCartLineI cartModel=(FDCartLineModel)orderLines.get(i);	 
						String productId = cartModel.getProductRef().lookupProduct().getContentKey().getId();
						if(!cartModel.hasDiscount(promotionCode) && recommendedItemMap.containsKey(productId)){								
							Discount dis=new Discount(promotionCode,EnumDiscountType.PERCENT_OFF,percentOff);
							cartModel.setDiscount(dis);
							cartModel.setSavingsId((String)recommendedItemMap.get(productId));
							cartModel.setDiscountApplied(true);
						}
					
				}	
			}
			//Update Pricing after discount application.
			try {
					cart.refreshAll();
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				throw new FDRuntimeException(e);
			}
			catch (FDInvalidConfigurationException e) {
				throw new FDRuntimeException(e);
			}			      						
			return true;
		}						
		return false;
	}		
	
	/**
	 * This method will run through given line item against list of line item strategies
	 * and determine if the line is eligible for promotion passed as a parameter.
	 * @param promoCode
	 * @param context
	 * @param lineItemStrategies
	 * @return
	 */
	protected boolean evaluate(FDCartLineI lineItem, String promoCode, PromotionContextI context) {
		for (Iterator i = this.lineItemStrategies.iterator(); i.hasNext();) {
			LineItemStrategyI strategy = (LineItemStrategyI) i.next();
			int response = strategy.evaluate(lineItem, promoCode, context);

			 //System.out.println("Evaluated " + promoCode + " / " +
			 //strategy.getClass().getName() + " -> " + response);

			switch (response) {

			case PromotionStrategyI.ALLOW:
				// check next rule
				continue;

			case PromotionStrategyI.FORCE:
				// eligible, terminate evaluation
				return true;

			default:
				// not eligible, terminate evaluation
				return false;
			}
		}

		return true;
	}
}
