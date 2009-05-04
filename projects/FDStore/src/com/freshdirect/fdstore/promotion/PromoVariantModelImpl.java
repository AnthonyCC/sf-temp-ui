package com.freshdirect.fdstore.promotion;

import com.freshdirect.fdstore.util.EnumSiteFeature;

public class PromoVariantModelImpl implements PromoVariantModel {
	private static final long serialVersionUID = 4213257883052956128L;

	private String variantId;
	private String promoCode;
	private int priority;
	private EnumSiteFeature siteFeature;
	private int featurePriority;
	
	public int getFeaturePriority() {
		return featurePriority;
	}

	public void setFeaturePriority(int featurePriority) {
		this.featurePriority = featurePriority;
	}

	public PromoVariantModelImpl(String variantId, String promoCode,int priority, EnumSiteFeature siteFeature, int featurePriority){
		this.variantId = variantId;
		this.promoCode = promoCode;
		this.priority = priority;
		this.siteFeature = siteFeature;
		this.featurePriority = featurePriority;
	}
	
	public int getPriority() {
		return priority;
	}

	public PromotionI getAssignedPromotion() {
		return PromotionFactory.getInstance().getPromotion(this.promoCode);
	}
	/*
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	*/
	public EnumSiteFeature getSiteFeature() {
		return siteFeature;
	}
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

	public double getPercentageOff() {
		return this.getAssignedPromotion().getLineItemDiscountPercentOff();
	}

}
