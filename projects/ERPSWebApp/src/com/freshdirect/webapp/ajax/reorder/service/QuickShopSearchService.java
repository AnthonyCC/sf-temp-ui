package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;

import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.search.SearchService;

public class QuickShopSearchService {

	private static final QuickShopSearchService INSTANCE = new QuickShopSearchService();

	private QuickShopSearchService() {
	}

	public static QuickShopSearchService defaultService() {
		return INSTANCE;
	}

	/**
	 * Merge the original search result with the user's order history.
	 * 
	 * @param searchTerm
	 * @param items
	 *            - to be merged with the search result
	 */
    public void search(String searchTerm, List<QuickShopLineItemWrapper> items, FDUserI user, Cookie[] cookies) {
		List<String> productIds = null;
		if (searchTerm != null) {
            SearchResults results = SearchService.getInstance().searchProducts(searchTerm, cookies, user);
			productIds = new ArrayList<String>();
			for (FilteringSortingItem<ProductModel> product : results.getProducts()) {
				productIds.add(product.getNode().getContentKey().getId());
			}
			Iterator<QuickShopLineItemWrapper> it = items.iterator();
			while (it.hasNext()) {
				if (!productIds.contains(it.next().getProduct().getContentKey().getId())) {
					it.remove();
				}
			}
		}

	}
}
