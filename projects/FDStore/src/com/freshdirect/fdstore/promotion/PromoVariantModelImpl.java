package com.freshdirect.fdstore.promotion;

/*
 * @author skrishnasamy
 * This model class holds the mapping between variantId and Promotion code
 * tied to it.
 */
import java.io.Serializable;

import com.freshdirect.fdstore.util.EnumSiteFeature;

public class PromoVariantModelImpl implements PromoVariantModel {
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
	public void setPriority(int priority) {
		this.priority = priority;
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
	public void setSiteFeature(EnumSiteFeature siteFeature) {
		this.siteFeature = siteFeature;
	}
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}

}
