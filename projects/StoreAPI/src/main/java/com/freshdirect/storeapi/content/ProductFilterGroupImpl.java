package com.freshdirect.storeapi.content;

import java.util.List;

public class ProductFilterGroupImpl implements ProductFilterGroupI {
	
	private String id;
	private String name;
	private ProductFilterGroupType type;
	private String allSelectedLabel;
	private List<ProductItemFilterI> productFilters;
	private boolean displayOnCategoryListingPage;
	private boolean multiGroupModel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ProductFilterGroupType getType() {
		return type;
	}
	
	public void setType(ProductFilterGroupType type) {
		this.type = type;
	}

	public void setType(String type) {
		setType(ProductFilterGroupType.toEnum(type));
	}
	
	public String getAllSelectedLabel() {
		return allSelectedLabel;
	}
	
	public void setAllSelectedLabel(String allSelectedLabel) {
		this.allSelectedLabel = allSelectedLabel;
	}
	
	public List<ProductItemFilterI> getProductFilters() {
		return productFilters;
	}
	
	public void setProductFilters(List<ProductItemFilterI> productFilters) {
		this.productFilters = productFilters;
	}

	public boolean isDisplayOnCategoryListingPage() {
		return displayOnCategoryListingPage;
	}

	public void setDisplayOnCategoryListingPage(boolean displayOnCategoryListingPage) {
		this.displayOnCategoryListingPage = displayOnCategoryListingPage;
	}

	public boolean isMultiGroupModel() {
		return multiGroupModel;
	}

	public void setMultiGroupModel(boolean multiGroupModel) {
		this.multiGroupModel = multiGroupModel;
	}

}
