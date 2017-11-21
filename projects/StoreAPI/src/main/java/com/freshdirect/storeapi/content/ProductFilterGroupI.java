package com.freshdirect.storeapi.content;

import java.util.List;

public interface ProductFilterGroupI {
	
	String getName();
	String getId();
	ProductFilterGroupType getType();
	String getAllSelectedLabel();
	List<ProductItemFilterI> getProductFilters();
	boolean isDisplayOnCategoryListingPage();
	boolean isMultiGroupModel();

}
