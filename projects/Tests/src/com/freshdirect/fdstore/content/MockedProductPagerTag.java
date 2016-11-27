package com.freshdirect.fdstore.content;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.util.ProductPagerNavigator;
import com.freshdirect.webapp.taglib.fdstore.AbstractProductPagerTag;

public class MockedProductPagerTag extends AbstractProductPagerTag {
	private static final long serialVersionUID = 8543503628694681253L;

	private List<ProductModel> products;

	public MockedProductPagerTag(ProductPagerNavigator nav, String customerId) {
		super(nav, customerId);
	}

	@Override
	protected Comparator<FilteringSortingItem<ProductModel>> getProductSorter(List<FilteringSortingItem<ProductModel>> products,
			SearchSortType sortBy, boolean ascending) {
		return FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR);
	}

	@Override
	protected SearchResults getResults() {
		return new SearchResults(FilteringSortingItem.wrap(products), FilteringSortingItem.<Recipe> emptyList(),
				FilteringSortingItem.<CategoryModel> emptyList(), nav.getSearchTerm(), false);
	}

	@Override
	protected void postProcess(SearchResults results) {
		// null
	}

	public void setProducts(List<ProductModel> products) {
		this.products = products;
	}
}
