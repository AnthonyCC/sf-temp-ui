package com.freshdirect.fdstore.promotion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class Promotion extends ModelSupport implements PromotionI {

	private final EnumPromotionType promotionType;

	private final String promotionCode;

	private final String description;

	private final String name;

	//private boolean maxUsagePerCust;

	//private int rollingExpDays;
	
	//private boolean audienceBased;

	private final List strategies = new ArrayList();
	
	//private final List lineItemStrategies= new ArrayList();

	private PromotionApplicatorI applicator;
	
	//private PromotionApplicatorI lineItemApplicator;

	private Timestamp lastModified;
		
	
	private int priority;
	
	private boolean allowHeaderDiscount=false;
	
	private boolean recommendedItemsOnly=false;
	
	private final static Comparator PRECEDENCE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			
			int p1 = ((PromotionStrategyI) o1).getPrecedence();
			int p2 = ((PromotionStrategyI) o2).getPrecedence();
			return p1 - p2;
		}
	};

	public Promotion(PrimaryKey pk, EnumPromotionType promotionType,
			String promotionCode, String name, String description,
			Timestamp lastModified) {
		this.setPK(pk);
		this.promotionType = promotionType;
		this.promotionCode = promotionCode;
		this.description = description;
		this.name = name;
		//this.maxUsagePerCust = maxUsagePerCust;
		//this.rollingExpDays = rollingExpDays;
		//this.audienceBased = audienceBased;
		this.lastModified = lastModified;
	}

	public void addStrategy(PromotionStrategyI strategy) {
		this.strategies.add(strategy);
		Collections.sort(this.strategies, PRECEDENCE_COMPARATOR);
	}
/*	
	public void addLineItemStrategy(PromotionStrategyI strategy) {
		this.lineItemStrategies.add(strategy);
		Collections.sort(this.lineItemStrategies, PRECEDENCE_COMPARATOR);
	}
*/	

	public void setApplicator(PromotionApplicatorI applicator) {
		this.applicator = applicator;
		if(this.isSampleItem())
			//Sample Promo
			setPriority(10);
		else if(this.isWaiveCharge())
			//Delivery Promo
			setPriority(20);	
		else if(this.isLineItemDiscount()) //&& this.isAllowHeaderDiscount())
			//Line Item Promo
			setPriority(30);					
		else if(this.isSignupDiscount())
			//Signup promo
			setPriority(40);
		else if(this.isRedemption())
			//Redemption promo
			setPriority(50);
		else if(this.isCategoryDiscount())
			//DCPD promotion
			setPriority(60);
		else{
			//Any other automatic percent off or dollar off.
			setPriority(70);
		}
	}

	public PromotionApplicatorI getApplicator() {
		return this.applicator;
	}

	public EnumPromotionType getPromotionType() {
		return this.promotionType;
	}

	public String getPromotionCode() {
		return this.promotionCode;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * @return true if the Promotion is configured properly.
	 */
	public boolean isValid() {
		return this.applicator != null ;
	}

	public boolean evaluate(PromotionContextI context) {

		for (Iterator i = this.strategies.iterator(); i.hasNext();) {
			PromotionStrategyI strategy = (PromotionStrategyI) i.next();
			int response = strategy.evaluate(this.promotionCode, context);

			 //System.out.println("Evaluated " + this.promotionCode + " / " +
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

	public boolean apply(PromotionContextI context) {
		return this.applicator.apply(this.promotionCode, context);
	}
	/*
	public boolean applyLineItem(PromotionContextI context) {
		return this.lineItemApplicator.apply(this.promotionCode, context);
	}
	*/

	public Collection getStrategies() {
		return Collections.unmodifiableCollection(this.strategies);
	}
	
	public PromotionStrategyI getStrategy(Class strategyClass) {
		for (Iterator i = this.strategies.iterator(); i.hasNext();) {
			PromotionStrategyI strategy = (PromotionStrategyI) i.next();
			if (strategyClass.isAssignableFrom(strategy.getClass())) {
				return strategy;
			}
		}
		return null;
	}

	public Date getExpirationDate() {
		for (Iterator i = getStrategies().iterator(); i.hasNext();) {
			Object obj = i.next();
			if (obj instanceof DateRangeStrategy) {
				return ((DateRangeStrategy) obj).getExpirationDate();
			}
		}
		return null;
	}

	public List getHeaderDiscountRules() {
		if (this.applicator instanceof SignupDiscountApplicator) {
			return ((SignupDiscountApplicator) this.applicator)
					.getDiscountRules();

		} else if (this.applicator instanceof HeaderDiscountApplicator) {
			HeaderDiscountRule rule = ((HeaderDiscountApplicator) this.applicator)
					.getDiscountRule();
			return Arrays.asList(new HeaderDiscountRule[] { rule });
		} else if (this.applicator instanceof DCPDiscountApplicator) {
			DCPDiscountRule rule = ((DCPDiscountApplicator) this.applicator)
					.getDiscountRule();
			return Arrays.asList(new DCPDiscountRule[] { rule });
		}
		return null;
	}

	public double getHeaderDiscountTotal() {
		List discountRules = this.getHeaderDiscountRules();
		if (discountRules == null) {
			return 0;
		}
		double sum = 0;
		for (Iterator i = discountRules.iterator(); i.hasNext();) {
			HeaderDiscountRule discountRule = (HeaderDiscountRule) i.next();
			sum += discountRule.getMaxAmount();
		}
		return sum;
	}

	public boolean isSampleItem() {
		return this.applicator instanceof SampleLineApplicator;
	}

	public boolean isWaiveCharge() {
		return this.applicator instanceof WaiveChargeApplicator;
	}
	
	public boolean isHeaderDiscount() {
		return (this.applicator instanceof HeaderDiscountApplicator || 
				this.applicator instanceof PercentOffApplicator || 
				this.applicator instanceof DCPDiscountApplicator); 
		
	}
	
	public boolean isCategoryDiscount() {
		return this.applicator instanceof DCPDiscountApplicator;
	}
	
	
	public boolean isSignupDiscount() {
		return this.applicator instanceof SignupDiscountApplicator;
	}
	
	public double getMinSubtotal() {
		if (this.applicator instanceof SampleLineApplicator) {
			return ((SampleLineApplicator) this.applicator).getMinSubtotal();
		}
		if (this.applicator instanceof PercentOffApplicator) {
			return ((PercentOffApplicator) this.applicator).getMinSubtotal();
		}
		if (this.applicator instanceof WaiveChargeApplicator) {
			return ((WaiveChargeApplicator) this.applicator).getMinSubtotal();
		}
		if (this.applicator instanceof LineItemDiscountApplicator) {
			return ((LineItemDiscountApplicator) this.applicator).getMinSubtotal();
		}

		List discountRules = this.getHeaderDiscountRules();
		if (discountRules == null) {
			return 0;
		}
		return ((HeaderDiscountRule) discountRules.get(0)).getMinSubtotal();
	}
	
	public Timestamp getModifyDate() {
		return lastModified;
	}

	public boolean isRedemption(){
		boolean value = false;
		for (Iterator i = getStrategies().iterator(); i.hasNext();) {
			Object obj = i.next();
			if (obj instanceof RedemptionCodeStrategy) {
				//This is redemption promo.
				value = true;
				break;
			}
		}
		return value;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("Promotion[");
		sb.append(this.promotionType).append(" / ").append(this.promotionCode);
		sb.append(" (").append(this.description).append(")");
		sb.append("\n\tStrategies=[");
		for (Iterator i = this.strategies.iterator(); i.hasNext();) {
			sb.append("\n\t\t");
			sb.append(i.next());
		}
		sb.append("\n\t], Applicator=").append(this.applicator).append("\n]");
		return sb.toString();
	}
/*
	public boolean evaluateLineItemPromo(PromotionContextI context) {
		// TODO Auto-generated method stub
		for (Iterator i = this.lineItemStrategies.iterator(); i.hasNext();) {
			PromotionStrategyI strategy = (PromotionStrategyI) i.next();
			int response = strategy.evaluate(this.promotionCode, context);

			 System.out.println("Evaluated " + this.promotionCode + " / " +
			 strategy.getClass().getName() + " -> " + response);

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
		
*/

	public boolean isLineItemDiscount() {
		// TODO Auto-generated method stub
		return this.applicator instanceof LineItemDiscountApplicator;
	}
	
	
/*
	public Collection getLineItemStrategies() {
			return Collections.unmodifiableCollection(this.lineItemStrategies);
	}
*/
	public boolean isAllowHeaderDiscount() {
		return allowHeaderDiscount;
	}

	public void setAllowHeaderDiscount(boolean applyHeaderDiscount) {
		this.allowHeaderDiscount = applyHeaderDiscount;
	}

	public void setRecommendedItemsOnly(boolean recommendedItemsOnly) {
		this.recommendedItemsOnly = recommendedItemsOnly;
	}
	 public boolean isRecommendedItemsOnly(){
		 return this.recommendedItemsOnly;
	 }

	 
	 public double getLineItemDiscountPercentOff(){
		 double percentOff=0;
		 if(isLineItemDiscount()){
			 LineItemDiscountApplicator app=(LineItemDiscountApplicator)applicator;
			 percentOff=app.getPercentOff();
		 }
		 return percentOff;
	 }
	 
	 public boolean isFraudCheckRequired(){
		 boolean value = false;
			for (Iterator i = getStrategies().iterator(); i.hasNext();) {
				Object obj = i.next();
				if (obj instanceof FraudStrategy) {
					//Fruad Check is required for this promo.
					value = true;
					break;
				}
			}
			return value;		 
	 }
	 

}
