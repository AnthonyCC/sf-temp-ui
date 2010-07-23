package com.freshdirect.fdstore.promotion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class Promotion extends ModelSupport implements PromotionI {

	private static final long	serialVersionUID	= -4069961539775362219L;

	private final EnumPromotionType promotionType;

	private final String promotionCode;

	private final String description;

	private final String name;

	//private boolean maxUsagePerCust;

	//private int rollingExpDays;
	
	//private boolean audienceBased;

	private final List<PromotionStrategyI> strategies = new ArrayList<PromotionStrategyI>();
	
	//private final List lineItemStrategies= new ArrayList();

	private PromotionApplicatorI applicator;
	
	//private PromotionApplicatorI lineItemApplicator;

	private Timestamp lastModified;
		
	
	private int priority;
	
	private boolean combineOffer=false;
	
	private boolean recommendedItemsOnly=false;
	
	private Set<String> excludeSkusFromSubTotal;
	
	private EnumOfferType offerType;
	
	private final static Comparator<PromotionStrategyI> PRECEDENCE_COMPARATOR = new Comparator<PromotionStrategyI>() {
		public int compare(PromotionStrategyI o1, PromotionStrategyI o2) {			
			int p1 = o1.getPrecedence();
			int p2 = o2.getPrecedence();
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
	
		
	private Set<String> convertToSkus(String value){
		StringTokenizer tokens = new StringTokenizer(value);
		Set<String> returnSet = new HashSet<String>();
		
		while(tokens.hasMoreTokens()){
			returnSet.add(tokens.nextToken());
		}
		return returnSet;
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
		else if(this.isExtendDeliveryPass())
			//Extend Delivery Pass Promo
			setPriority(30);	
		else if((this.isHeaderDiscount() || this.isLineItemDiscount()) && this.isCombineOffer())
			//Combine offer promotions are guaranteed to apply. 
			setPriority(35);
		else if(this.isSignupDiscount())
			//Signup promo
			setPriority(40);
		else if(this.isRedemption())
			//Redemption promo
			setPriority(50);
		else if(this.isLineItemDiscount())
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

		for (Iterator<PromotionStrategyI> i = this.strategies.iterator(); i.hasNext();) {
			PromotionStrategyI strategy = i.next();
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

	public Collection<PromotionStrategyI> getStrategies() {
		return Collections.unmodifiableCollection(this.strategies);
	}
	
	public PromotionStrategyI getStrategy(Class<?> strategyClass) {
		for (Iterator<PromotionStrategyI> i = this.strategies.iterator(); i.hasNext();) {
			PromotionStrategyI strategy = i.next();
			if (strategyClass.isAssignableFrom(strategy.getClass())) {
				return strategy;
			}
		}
		return null;
	}

	public Date getExpirationDate() {
		for (Iterator<PromotionStrategyI> i = getStrategies().iterator(); i.hasNext();) {
			Object obj = i.next();
			if (obj instanceof DateRangeStrategy) {
				return ((DateRangeStrategy) obj).getExpirationDate();
			}
		}
		return null;
	}

	//FIXME List of what? Types are mixed up here (HeaderDiscountRule <--> DCPDiscountRule <--> SignupDiscountRule), 
	// this will cause ClassCastException-s ( getHeaderDiscountTotal() will try to cast to HeaderDiscountRule for example)
	public List getHeaderDiscountRules() {
		if (this.applicator instanceof SignupDiscountApplicator) {
			return ((SignupDiscountApplicator) this.applicator).getDiscountRules();
		} else if (this.applicator instanceof HeaderDiscountApplicator) {
			HeaderDiscountRule rule = ((HeaderDiscountApplicator) this.applicator).getDiscountRule();
			return Arrays.asList(new HeaderDiscountRule[] { rule });
		}/* else if (this.applicator instanceof DCPDiscountApplicator) {
			DCPDiscountRule rule = ((DCPDiscountApplicator) this.applicator).getDiscountRule();
			return Arrays.asList(new DCPDiscountRule[] { rule });
		}*/
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

	public double getLineItemDiscountPercentage() {
		if(applicator instanceof LineItemDiscountApplicator){
			return ((LineItemDiscountApplicator)applicator).getPercentOff();
		}

		return 0.0;
	}
	
	public boolean isSampleItem() {
		return this.applicator instanceof SampleLineApplicator;
	}

	public boolean isWaiveCharge() {
		return this.applicator instanceof WaiveChargeApplicator;
	}
	
	public boolean isExtendDeliveryPass() {
		return this.applicator instanceof ExtendDeliveryPassApplicator;
	}
	
	public boolean isHeaderDiscount() {
		return (this.applicator instanceof HeaderDiscountApplicator || 
				this.applicator instanceof PercentOffApplicator); 
		
	}
	
	public boolean isDollarValueDiscount() {
		return this.isHeaderDiscount() || this.isLineItemDiscount();
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
		for (Iterator<PromotionStrategyI> i = getStrategies().iterator(); i.hasNext();) {
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
		for (Iterator<PromotionStrategyI> i = this.strategies.iterator(); i.hasNext();) {
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
	public boolean isCombineOffer() {
		return combineOffer;
	}

	public void setCombineOffer(boolean combineOffer) {
		this.combineOffer = combineOffer;
	}

	public void setRecommendedItemsOnly(boolean recommendedItemsOnly) {
		this.recommendedItemsOnly = recommendedItemsOnly;
	}
	 public boolean isFavoritesOnly(){
		 if(applicator instanceof LineItemDiscountApplicator){
			 LineItemDiscountApplicator app = (LineItemDiscountApplicator) applicator;
			 return app.isFavoritesOnly();
		 }
		 return false;
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
			for (Iterator<PromotionStrategyI> i = getStrategies().iterator(); i.hasNext();) {
				Object obj = i.next();
				if (obj instanceof FraudStrategy) {
					//Fruad Check is required for this promo.
					value = true;
					break;
				}
			}
			return value;		 
	 }

	public Set<String> getExcludeSkusFromSubTotal() {
		return excludeSkusFromSubTotal;
	}

	public void setExcludeSkusFromSubTotal(String excludeSkusFromSubTotal) {
		this.excludeSkusFromSubTotal = convertToSkus(excludeSkusFromSubTotal);
	}


	public EnumOfferType getOfferType() {
		return offerType;
	}


	public void setOfferType(EnumOfferType offerType) {
		this.offerType = offerType;
	}
	 

}
