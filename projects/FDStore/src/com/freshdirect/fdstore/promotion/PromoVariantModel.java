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

	public int getPriority() ;
	
	public PromotionI getAssignedPromotion();
	
	public EnumSiteFeature getSiteFeature();
	
	public String getVariantId();
	
	public double getPercentageOff();
}
