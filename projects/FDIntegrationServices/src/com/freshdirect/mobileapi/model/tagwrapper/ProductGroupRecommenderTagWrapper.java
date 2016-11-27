package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.smartstore.ProductGroupRecommenderTag;

public class ProductGroupRecommenderTagWrapper extends GetterTagWrapper {
    
	String departmentId;
	String siteFeature;
	int maxItems;

	public ProductGroupRecommenderTagWrapper(String siteFeature, String departmentId,
			SessionUser user, int maxItems) {
		super(new ProductGroupRecommenderTag(), user);
		this.departmentId = departmentId;
		this.siteFeature = siteFeature;
		this.maxItems = maxItems;
	}

	public Recommendations getFeatureRecommendations() throws FDException {
        ((ProductGroupRecommenderTag) wrapTarget).setCurrentNode(ContentFactory.getInstance().getContentNode(departmentId));
        ((ProductGroupRecommenderTag) wrapTarget).setSiteFeature(siteFeature);
        ((ProductGroupRecommenderTag) wrapTarget).setItemCount(maxItems);
        return (Recommendations) getResult();
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

	public String getSiteFeature() {
		return siteFeature;
	}

	public void setSiteFeature(String siteFeature) {
		this.siteFeature = siteFeature;
	}    

}