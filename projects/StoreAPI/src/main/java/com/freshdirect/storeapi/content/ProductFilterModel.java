package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;

public class ProductFilterModel extends ContentNodeModelImpl {

	public static final String TYPE = "type";
	public static final String FILTERS = "filters";
	public static final String NUTRITION_CODE = "nutritionCode";
	public static final String ERPSY_FLAG_CODE = "erpsyFlagCode";
	public static final String TO_VALUE = "toValue";
	public static final String FROM_VALUE = "fromValue";
	public static final String BRAND = "brand";
	public static final String TAG = "tag";
	public static final String DOMAIN_VALUE = "domainValue";
	public static final String NAME = "name";
	public static final String INVERT = "invert";

	private List<ProductFilterModel> filters = new ArrayList<ProductFilterModel>();

    public ProductFilterModel(ContentKey key) {
        super(key);
    }

	public String getName() {
		return (String) getCmsAttributeValue(NAME);
	}

	public DomainValue getDomainValue() {
		return getSingleRelationshipNode(DOMAIN_VALUE);
	}

	public TagModel getTag() {
		return getSingleRelationshipNode(TAG);
	}

	public BrandModel getBrand() {
		return getSingleRelationshipNode(BRAND);
	}

	public Double getFromValue() {
		return (Double) getCmsAttributeValue(FROM_VALUE);
	}

	public Double getToValue() {
		return (Double) getCmsAttributeValue(TO_VALUE);
	}

	public boolean isInvert() {
		return getAttribute(INVERT, false);
	}

	public String getType() {
		return (String) getCmsAttributeValue(TYPE);
	}

	public String getErpsyFlagCode() {
		return (String) getCmsAttributeValue(ERPSY_FLAG_CODE);
	}

	public String getNutritionCode() {
		return (String) getCmsAttributeValue(NUTRITION_CODE);
	}

	public List<ProductFilterModel> getFilters() {
		ContentNodeModelUtil.refreshModels(this, FILTERS, filters, false);
		return new ArrayList<ProductFilterModel>(filters);
	}
}