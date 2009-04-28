package com.freshdirect.fdstore.promotion;

/*
 * @author skrishnasamy
 * This model class holds the mapping between variantId and Promotion code
 * tied to it.
 */
import java.io.Serializable;

import com.freshdirect.fdstore.util.EnumSiteFeature;

public interface PromoVariantModel extends Serializable {
	
	public int getFeaturePriority();

	public void setFeaturePriority(int featurePriority);

	public int getPriority() ;
	
	public void setPriority(int priority);
	
	public PromotionI getAssignedPromotion();
	
	//public void setPromoCode(String promoCode);
	
	public EnumSiteFeature getSiteFeature();
	
	//public void setSiteFeature(EnumSiteFeature siteFeature);
	
	public String getVariantId();
	
	public void setVariantId(String variantId) ;
	
	public double getPercentageOff();
		
}
