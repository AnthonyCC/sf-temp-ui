package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductFilterGroupI;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;

public class ProductItemFilterUtil {
	
	private static final String COMPOSITE_FILTER_ID_SEPARATOR = "_";
	private static final Logger LOG = LoggerFactory.getInstance( ProductItemFilterUtil.class );
	
	public static boolean hasFilteredProducts(List<FilteringProductItem> items, ProductItemFilterI filter, boolean showUnavProducts, boolean isProductListing) {
		if (items == null) {
			return false;
		}
		
		for (FilteringProductItem productItem : items){
			
			// don't count unavailable items
			if(isProductListing && !showUnavProducts && (productItem.getProductModel() == null || !productItem.getProductModel().isFullyAvailable() || productItem.getProductModel().isOutOfSeason())){
				continue;
			}
			
			try {
				// An item has survived the filtering, return true and break the loop.
				if (filter.apply(productItem)) {
					return true;
				}
			} catch (FDResourceException e) {
				LOG.error("Could not apply filter on product: " + productItem.getProductModel());
			}
		}
		
		return false;
	}
	public static List<FilteringProductItem> getFilteredProducts(List<FilteringProductItem> items, Set<ProductItemFilterI> activeFilters, boolean showUnavProducts, boolean isProductListing) {
		
		if(items==null){
			return null;
		}
		
		List<FilteringProductItem> shownProducts = new ArrayList<FilteringProductItem>();
		
		for (FilteringProductItem productItem : items){
			
			// don't count unavailable items
			if(isProductListing && !showUnavProducts && (productItem.getProductModel() == null || !productItem.getProductModel().isFullyAvailable() || productItem.getProductModel().isOutOfSeason())){
				continue;
			}
			
			boolean passedFilters = true;
			for (ProductItemFilterI filter : activeFilters) {
				try {
					
					
					if (!filter.apply(productItem)) {
						passedFilters = false;
						break;
					}
				} catch (FDResourceException e) {
					LOG.error("Could not apply filter on product: " + productItem.getProductModel());
				}
			}

			if (passedFilters) {
				shownProducts.add(productItem);
			}
		}
		
		return shownProducts;
		
	}
	
	public static int countItemsForFilter(List<FilteringProductItem> items, ProductItemFilterI filter) {
		if (filter == null) {
			return items.size();
		}
		int count = 0;

		for (FilteringProductItem item : items) {
			try {
				if (filter.apply(item)) {
					++count;
				}
			} catch (FDResourceException e) {
				LOG.error("Could not apply filter on product: " + item.getProductModel());
				continue;
			} catch (FDRuntimeException e) {
				LOG.error("Could not apply filter on product: " + item.getProductModel());
				continue;
			}
		}
		return count;
	}
	
	/**
	 * @param parentId
	 * @param filters
	 * @return
	 * 
	 * remove filters belongs to the specified filter group
	 */
	public static Set<ProductItemFilterI> removeFiltersByParentId(String parentId, Map<String, ProductItemFilterI> filters){
		
		Set<ProductItemFilterI> result = new HashSet<ProductItemFilterI>();
		
		for(String key : filters.keySet()){
			
			ProductItemFilterI filter = filters.get(key);
			
			if(!parentId.equals(filter.getParentId())){
				result.add(filter);
			}
		}

		return result;
	}
	
		
	public static List<FilteringProductItem> createFilteringProductItems(List<ProductModel> products){
		List<FilteringProductItem> items = new ArrayList<FilteringProductItem>();
		
		for(ProductModel prod : products){
			if (!prod.isHidden() && !prod.isInvisible()){ //remove those which never should be displayed
				items.add(new FilteringProductItem(prod));
			}
		}
		return items;
	}

	public static List<FilteringProductItem> createFilteringProductItemsFromSearchResults(List<FilteringSortingItem<ProductModel>> searchResults){
		List<FilteringProductItem> items = new ArrayList<FilteringProductItem>();
		
		for(FilteringSortingItem<ProductModel> searchResult : searchResults){
			ProductModel prod = searchResult.getModel();
			if (!prod.isHidden() && !prod.isInvisible()){ //remove those which never should be displayed
				items.add(new FilteringProductItem(searchResult));
			}
		}
		return items;
	}
	
	public static List<FilteringProductItem> createFilteringRecipeItems(List<Recipe> recipes) {
		List<FilteringProductItem> results = new ArrayList<FilteringProductItem>();
		for (Recipe recipe : recipes) {
			results.add(new FilteringProductItem(recipe));
		}
		return results;
	}
	
	/**
	 * @param sections
	 * @param items
	 * collect all product items from section tree
	 */
	public static void collectAllItems(List<SectionContext> sections, List<FilteringProductItem> items, NavigationModel navigationModel){
		if(sections!=null){
			for(SectionContext section : sections){
				if (navigationModel.isRecipeListing()) {
					if(section.getRecipeItems()!=null){
						items.addAll(section.getRecipeItems());
					}
				} else {
					if(section.getProductItems()!=null){
						items.addAll(section.getProductItems());
					}
				}
				collectAllItems(section.getSectionContexts(), items, navigationModel);
			}
		}
	}
	
	
	/**
	 * @param filters
	 * @return
	 * convenience method for transforming filter container
	 */
	public static Map<String, ProductItemFilterI> prepareFilters(List<ProductFilterGroupI> filters){
		
		Map<String, ProductItemFilterI> result = new HashMap<String, ProductItemFilterI>();
		
		for(ProductFilterGroupI group : filters){
			for(ProductItemFilterI filter : group.getProductFilters()){
				result.put(createCompositeId(group.getId(), filter.getId()), filter);
			}
		}
		
		return result;
	}
	
	/**
	 * @param filters
	 * @return
	 * convenience method for transforming filter container
	 */
	public static Map<String, ProductItemFilterI> prepareFilters(Set<ProductItemFilterI> filters){
		
		Map<String, ProductItemFilterI> result = new HashMap<String, ProductItemFilterI>();

		for (ProductItemFilterI filter : filters) {
			result.put(filter.getId(), filter);
		}
		
		return result;
	}
	
	public static String createCompositeId(String groupId, String filterId){
		return groupId + COMPOSITE_FILTER_ID_SEPARATOR + filterId;
	}

}
