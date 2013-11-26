package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;
import com.freshdirect.cms.ContentKey;

public class ProductFilterModel extends ContentNodeModelImpl {

	private List<ProductFilterModel> filters = new ArrayList<ProductFilterModel>();
	
	public ProductFilterModel(ContentKey key) {
		super(key);
	}

	public String getName() {
		return (String) getCmsAttributeValue("name");
	}
	
	public DomainValue getDomainValue() {
		return getSingleRelationshipNode("domainValue");
	}

	public TagModel getTag() {
		return getSingleRelationshipNode("tag");
	}

	public BrandModel getBrand() {
		return getSingleRelationshipNode("brand");
	}
	
	public Double getFromValue() {
		return (Double) getCmsAttributeValue("fromValue");
	}

	public Double getToValue() {
		return (Double) getCmsAttributeValue("toValue");
	}

	public boolean isInvert() {
		return getAttribute("invert", false);
	}

	public String getType() {
		return (String) getCmsAttributeValue("type");
	}

	public String getErpsyFlagCode() {
		return (String) getCmsAttributeValue("erpsyFlagCode");
	}

	public String getNutritionCode() {
		return (String) getCmsAttributeValue("nutritionCode");
	}
	
	public List<ProductFilterModel> getFilters() {
		ContentNodeModelUtil.refreshModels(this, "filters", filters, false);
		return new ArrayList<ProductFilterModel>(filters);
	}
}

