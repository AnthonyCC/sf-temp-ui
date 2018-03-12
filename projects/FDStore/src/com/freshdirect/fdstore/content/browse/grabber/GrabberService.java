package com.freshdirect.fdstore.content.browse.grabber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CacheEntryIdentifier;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.browse.filter.ProductItemFilterFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductFilterModel;
import com.freshdirect.storeapi.content.ProductGrabberModel;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.grabber.GrabberServiceI;

/**
 * Product Grabber Service implementation
 *
 * @author segabor
 *
 */
@Service
public class GrabberService implements GrabberServiceI {

	private static final Logger LOGGER = LoggerFactory.getInstance(GrabberService.class);
	private static final ProductItemFilterFactory FILTER_FACTORY = ProductItemFilterFactory.getInstance();
	private static final String PRICING_ZONE_ID_DELIMETER = "#"; //should not be a valid content key char (ContentKey.NAME_PATTERN)

	// limit barriers//
	private static final int MAX_DEPTH = 5;
	private static final int MAX_PRODS = 100000;

	private static Map<String,FilterCacheStrategy> productGrabberToFilterCacheStrategy = new HashMap<String, FilterCacheStrategy>();


	@Override
	public Collection<ProductModel> getProducts(ProductGrabberModel grabberModel) {
		List<ProductModel> filteredProductModels = null;
		CacheEntryIdentifier cacheEntryIdentifier = null;

		boolean isReadOnlyContent = CmsManager.getInstance().isReadOnlyContent(); //mock to true if you want to debug in cms db mode

		if (isReadOnlyContent){ //don't use cache if cms is in db mode
			cacheEntryIdentifier = getCacheEntryIdentifier(grabberModel);
			if (cacheEntryIdentifier != null){ //check if result is cached
                filteredProductModels = CmsServiceLocator.ehCacheUtil().getListFromCache(cacheEntryIdentifier);
			}
		}

		if (filteredProductModels == null) { //not yet - go gather 'em
			filteredProductModels = doProductGrabberFlow(grabberModel, isReadOnlyContent);

			if (isReadOnlyContent && cacheEntryIdentifier != null){
                CmsServiceLocator.ehCacheUtil().putListToCache(cacheEntryIdentifier, filteredProductModels); // store in cache
			}
		}

		return filteredProductModels;
	}


	private List<ProductModel> doProductGrabberFlow(ProductGrabberModel grabberModel, boolean isReadOnlyContent) {

		String grabberKey = grabberModel.getContentName();
		List<ProductModel> filteredProductModels = null;

		Collection<ProductModel> productsInScope = getProductsInScope(grabberModel, isReadOnlyContent);
		if (productsInScope.isEmpty()) {
			LOGGER.debug("Return empty set for "+ grabberKey);
			return Collections.emptyList();
		} else {
			LOGGER.debug("Scope contains " + productsInScope.size() + " products for "+ grabberKey);
		}

		// transform products to filterable items
		final List<FilteringProductItem> itemz = new ArrayList<FilteringProductItem>(productsInScope.size());
		for (final ProductModel prd : productsInScope) {
			itemz.add(new FilteringProductItem(prd));
		}

		// create filter chain
		final List<ProductItemFilterI> filterChain = new ArrayList<ProductItemFilterI>();
		final List<ProductFilterModel> filterModelz = grabberModel.getProductFilterModels();
		if (filterModelz != null && !filterModelz.isEmpty()) {
			for (ProductFilterModel pfm : filterModelz) {
				filterChain.add( FILTER_FACTORY.getProductFilter(pfm, grabberKey, null) );
			}
		}

		// apply filters
		Iterator<ProductItemFilterI> filterIt = filterChain.iterator();
		while (!itemz.isEmpty() && filterIt.hasNext()) {
			ProductItemFilterI f = filterIt.next();

			// apply filter on each filterable items
			ListIterator<FilteringProductItem> it = itemz.listIterator();
			while (!itemz.isEmpty() && it.hasNext()) {
				FilteringProductItem obj = it.next();
				try {
					if (!f.apply(obj)) {
						it.remove();
					}
				} catch (FDResourceException e) {
					LOGGER.debug("Filter " + f + " crashed on " + obj.getProductModel(), e);
				}
			}
		}

		// unwrap product models
		filteredProductModels = new ArrayList<ProductModel>();
		final ContentNodeModel parentCategory = grabberModel.getParentNode();
		for (FilteringProductItem i : itemz) {
			final ProductModel productModel = (ProductModel) i.getProductModel().clone();
			productModel.setParentNode(parentCategory);
            filteredProductModels.add(productModel);
		}

		return filteredProductModels;
	}


	private CacheEntryIdentifier getCacheEntryIdentifier(ProductGrabberModel grabberModel){

		String grabberKey = grabberModel.getContentName();
		FilterCacheStrategy filterCacheStrategy = productGrabberToFilterCacheStrategy.get(grabberKey); //try cache

		//determine compound cache strategy for grabber
		if (filterCacheStrategy==null){
			List<ProductFilterModel> filterModelz = grabberModel.getProductFilterModels();
			List<FilterCacheStrategy> filterCacheStrategies = new ArrayList<FilterCacheStrategy>();

			for (ProductFilterModel pfm : filterModelz) {
				filterCacheStrategies.add(FILTER_FACTORY.getProductFilter(pfm, grabberKey, null).getCacheStrategy());
			}

			filterCacheStrategy = FilterCacheStrategy.getCompoundCacheStrategy(filterCacheStrategies);
			productGrabberToFilterCacheStrategy.put(grabberKey, filterCacheStrategy);
		}

		switch (filterCacheStrategy) {
			case CMS_ONLY:
                return new CacheEntryIdentifier(CmsCaches.BR_CMS_ONLY_PRODUCT_GRABBER_CACHE.cacheName, grabberKey);
			case ERPS:
                return new CacheEntryIdentifier(CmsCaches.BR_ERPS_PRODUCT_GRABBER_CACHE.cacheName, grabberKey);
			case ERPS_PRICING_ZONE:
				String zoneId = ContentFactory.getInstance()!=null&&ContentFactory.getInstance().getCurrentUserContext()!=null&&
								ContentFactory.getInstance().getCurrentUserContext().getPricingContext()!=null&&
								ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo()!=null?
								ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().toString():null; //thread local for user
                return new CacheEntryIdentifier(CmsCaches.BR_ERPS_ZONE_PRODUCT_GRABBER_CACHE.cacheName, grabberKey + PRICING_ZONE_ID_DELIMETER + zoneId);
			default: //NO_CACHING:
				return null;
		}
	}

	/**
	 * The main entry point
	 * @param model
	 * @return
	 */
	private Collection<ProductModel> getProductsInScope(ProductGrabberModel model, boolean isReadOnlyContent) {

		Set<ProductModel> resultOfFullScope = new HashSet<ProductModel>();
		for (ProductContainer container : model.getScope()) {
            CacheEntryIdentifier subTreeEntryId = new CacheEntryIdentifier(CmsCaches.BR_STATIC_PRODUCTS_IN_SUB_TREE_CACHE.cacheName, container.getContentKey().getEncoded());

			List<ProductModel> productsForSubTreeList = null;
			if (isReadOnlyContent){
                productsForSubTreeList = CmsServiceLocator.ehCacheUtil().getListFromCache(subTreeEntryId); // try cache
			}

			if (productsForSubTreeList==null) {

				Set<ProductModel> productsForSubTreeSet = new HashSet<ProductModel>();
				getProductsInSubTree(container, productsForSubTreeSet, MAX_DEPTH);

				productsForSubTreeList = new ArrayList<ProductModel>(productsForSubTreeSet);
				if (isReadOnlyContent){
                    CmsServiceLocator.ehCacheUtil().putListToCache(subTreeEntryId, productsForSubTreeList);
				}
			}

			resultOfFullScope.addAll(productsForSubTreeList);
		}

		return resultOfFullScope;
	}



	/**
	 * Collect 'static' products by descending in the {@link ProductContainer} tree.
	 *
	 * @param container the root element of the container tree
	 * @param depth
	 *
	 * @return returns false if a limit was reached during the tree walk
	 */
	private boolean getProductsInSubTree(ProductContainer container, Set<ProductModel> bucket, int level) {

		if (level == 0) { // check level cap
			LOGGER.warn("Maximum depth reached, stop collecting more products");
			return false;
		}
		if (bucket.size() > MAX_PRODS) { // check bucket size
			LOGGER.warn("Bucket limit reached, stop collecting products");
			return false;
		}

		// do the dirty job, pick the products, really
		bucket.addAll(container.getStaticProducts());
		for (CategoryModel subCat : ((container)).getSubcategories()){ // we've got branches to visit
			if (!getProductsInSubTree(subCat, bucket, level-1)) {
				return false; // stop visiting more branches
			}
		}

		return true; // everything went fine, we are happy!
	}
}
