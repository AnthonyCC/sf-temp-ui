package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.webapp.ajax.filtering.ProductItemFilterUtil;

public class BrowseDataContextService {

	private static final BrowseDataContextService INSTANCE = new BrowseDataContextService();

	private BrowseDataContextService() {
	}

	public static BrowseDataContextService getDefaultBrowseDataContextService() {
		return INSTANCE;
	}

	public List<FilteringProductItem> collectAllItems(BrowseDataContext browseDataContext) {
		List<FilteringProductItem> result = new ArrayList<FilteringProductItem>();
		ProductItemFilterUtil.collectAllItems(browseDataContext.getSectionContexts(), result, browseDataContext.getNavigationModel());
		return result;
	}
}
