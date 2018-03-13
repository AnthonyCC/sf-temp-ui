package com.freshdirect.webapp.ajax.reorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.pricing.SkuModelPricingAdapter;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ConfiguredProduct;
import com.freshdirect.storeapi.content.ConfiguredProductGroup;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.EnumQuickShopFilteringValue;
import com.freshdirect.storeapi.content.FDFolder;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.FilteringValue;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.content.StarterList;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.quickshop.QuickShopYmalServlet;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.ajax.reorder.data.EnumQuickShopTab;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class QuickShopHelper {

	private static final int TIME_LIMIT = -13;
	private final static Logger LOG = LoggerFactory.getInstance(QuickShopHelper.class);

	//create filter
	private static final Set<FilteringValue> filters = new HashSet<FilteringValue>();
	static {
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_ALL);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_LAST);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_30);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_60);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_90);
		filters.add(EnumQuickShopFilteringValue.TIME_FRAME_180);
		filters.add(EnumQuickShopFilteringValue.ORDERS_BY_DATE);
		filters.add(EnumQuickShopFilteringValue.DEPT);
		filters.add(EnumQuickShopFilteringValue.GLUTEN_FREE);
		filters.add(EnumQuickShopFilteringValue.KOSHER);
		filters.add(EnumQuickShopFilteringValue.LOCAL);
		filters.add(EnumQuickShopFilteringValue.ORGANIC);
		filters.add(EnumQuickShopFilteringValue.ON_SALE);
	}

	public static QuickShopLineItemWrapper createItemCore(FDProductSelectionI productSelection, FDCustomerList list, StarterList starterList, FDUserI user, EnumQuickShopTab tab)
			throws FDResourceException {
		String compositeId = getCompositeIdForTab(productSelection, starterList, tab);

		// Create quickshop line item (unpopulated)
		QuickShopLineItem item = new QuickShopLineItem();
		item.setItemId(compositeId);

		// Refresh configuration TODO: invalid config scenario ?!
		try {
			productSelection.refreshConfiguration();
		} catch (Exception e) {
			LOG.error("Refresh configuration failed with configissue2: " + e);
			return null;
		}

		// Get CMS product model
		ProductModel productModel = productSelection.lookupProduct();
		if (productModel == null) {
			return null;
		}

		// Skip products without having primary home
		CategoryModel primaryHome = productModel.getPrimaryHome();
		if (primaryHome == null) {
		    return null;
		}

		// is hidden in quickshop?
		if (primaryHome.getDepartment().isHidddenInQuickshop()) {
			return null;
		}

		if (productModel instanceof ProductModelPricingAdapter) {
			// strip down, get the naked product model
			productModel = ((ProductModelPricingAdapter) productModel).getRealProduct();

			// create valid pricing adapter with valid pricing context
			productModel = ProductPricingFactory.getInstance().getPricingAdapter(productModel, user.getPricingContext());
		}

		// Selected sku code
		String skuCode = productSelection.getSkuCode();

		// Get CMS sku model
		SkuModel skuModel = productModel.getSku(skuCode);
		if (skuModel == null) {
			return null;
		}
		if (skuModel instanceof SkuModelPricingAdapter) {
			// strip down, get the naked sku model, we don't need an adapter for
			// the sku ???
			skuModel = ((SkuModelPricingAdapter) skuModel).getRealSku();
		}

		// Get ERPS FDProduct - latest version
		FDProduct latestFdProduct;
		try {
			latestFdProduct = skuModel.getProduct();
		} catch (FDSkuNotFoundException e1) {
			LOG.warn("Sku not found: " + skuCode);
			return null;
		}
		if (latestFdProduct == null) {
			return null;
		}

		// Get ERPS FDProductInfo - latest version
		FDProductInfo latestFdProductInfo;
		try {
			latestFdProductInfo = skuModel.getProductInfo();
		} catch (FDSkuNotFoundException e) {
			LOG.error("Cannot collect subtotal calculation data with " + productModel + ", e:" + e.getMessage(), e);
			return null;
		}
		if (latestFdProductInfo == null) {
			return null;
		}

		// create a valid price calculator
		PriceCalculator priceCalculator = productModel.getPriceCalculator(skuModel); // TODO
		// verify
		// pricing
		// context
		if (priceCalculator == null) {
			return null;
		}

		// Wrap the resulting item - adds filtering info
		QuickShopLineItemWrapper wrapper = new QuickShopLineItemWrapper(item, productModel);
		if (list != null) {
			wrapper.setCclId(list.getId());
			item.setListId(list.getId());
			wrapper.setListName(list.getName());
			wrapper.setRecipeName(list.getRecipeName());
			wrapper.setRecipeId(list.getRecipeId());

			// check if recipe is still alive
			if (list.getRecipeId() != null) {
				wrapper.setRecipeAlive(true);
				Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(ContentType.Recipe, list.getRecipeId()));
				if (recipe.isOrphan() || !recipe.isAvailable()) {
					wrapper.setRecipeAlive(false);
				}
			}
		}

		// === POPULATE ===

		ProductDetailPopulator.populateBasicProductData(item, user, productModel);
		//APPDEV-4178 : "Your Fave" Bursts should not display in Reorder"

		//ProductDetailPopulator.populateProductData(item, user, productModel, skuModel, latestFdProduct, priceCalculator, productSelection, true, true);
		ProductDetailPopulator.populateProductData(item, user, productModel, skuModel, latestFdProduct, priceCalculator, productSelection, false, true);

		ProductDetailPopulator.populatePricing(item, latestFdProduct, latestFdProductInfo, priceCalculator, user);

		populateOrderLineData(item, productSelection);

		try {
			ProductDetailPopulator.populateSkuData(item, user, productModel, skuModel, latestFdProduct);
		} catch (FDSkuNotFoundException e) {
			LOG.error("Failed to populate sku data", e);
		} catch (HttpErrorResponse e) {
			LOG.error("Failed to populate sku data", e);
		}

		ProductDetailPopulator.postProcessPopulate(user, item, item.getSkuCode());

		// User specific data - scores
		if (user != null) {
			FDIdentity identity = user.getIdentity();
			if (identity != null) {
				Float score = ScoreProvider.getInstance().getUserProductScore(identity.getErpCustomerPK(), productModel.getContentKey());
				if (score != null) {
					wrapper.setUserScore(score);
				}
			}
		}

		return wrapper;
	}

	public static QuickShopLineItem createItemFromProduct(ProductModel productModel, SkuModel skuModel, FDUserI user, boolean useFavBurst) throws FDResourceException {

		QuickShopLineItem item = new QuickShopLineItem();

		if (!(productModel instanceof ProductModelPricingAdapter)) {
			// wrap it into a pricing adapter if naked
			productModel = ProductPricingFactory.getInstance().getPricingAdapter(productModel, user.getPricingContext());
		}

		if (skuModel == null) {
			skuModel = productModel.getDefaultSku();
		}
		String skuCode = skuModel.getSkuCode();

		try {
			FDProductInfo productInfo = skuModel.getProductInfo();
			FDProduct fdProduct = skuModel.getProduct();

			PriceCalculator priceCalculator = productModel.getPriceCalculator();

			ProductDetailPopulator.populateBasicProductData(item, user, productModel);
			ProductDetailPopulator.populateProductData(item, user, productModel, skuModel, fdProduct, priceCalculator, null, useFavBurst, true);
			ProductDetailPopulator.populatePricing(item, fdProduct, productInfo, priceCalculator, user);

			try {
				ProductDetailPopulator.populateSkuData(item, user, productModel, skuModel, fdProduct);
			} catch (FDSkuNotFoundException e) {
				LOG.error("Failed to populate sku data", e);
			} catch (HttpErrorResponse e) {
				LOG.error("Failed to populate sku data", e);
			}

			ProductDetailPopulator.postProcessPopulate(user, item, item.getSkuCode());

			return item;

		} catch (FDSkuNotFoundException e) {
			LOG.warn("Sku not found: " + skuCode, e);
			return null;
		} /*
		 * catch ( HttpErrorResponse e ) { LOG.warn(
		 * "Error creating item: "+skuCode, e ); return null; }
		 */
	}

	@SuppressWarnings("unchecked")
	public static List<String> getActiveReplacements(HttpSession session) {
		return (List<String>) session.getAttribute(SessionName.SESSION_QS_REPLACEMENT);
	}

	public static int getListCount(FDUserI user) throws FDResourceException {
		List<FDCustomerCreatedList> customerCreatedLists = FDListManager.getCustomerCreatedLists(user);
		return customerCreatedLists.size();
	}

	public static int getOrderCount(FDUserI user) throws FDResourceException {
		EnumQuickShopTab tab = EnumQuickShopTab.PAST_ORDERS;
		List<QuickShopLineItemWrapper> items = getWrappedOrderHistoryUsingCache(user, tab, tab.cacheName);
		return countOrders(items);
	}

	public static List<FDOrderInfoI> getOrderHistoryInfo(FDUserI user, boolean regularOnly) throws FDResourceException {

		FDOrderHistory history = (FDOrderHistory) user.getOrderHistory();
		List<FDOrderInfoI> orderHistoryInfo = new ArrayList<FDOrderInfoI>(history.getFDOrderInfos(EnumSaleType.REGULAR));

		if (!regularOnly) {
			// Add gift cards orders too.
			orderHistoryInfo.addAll(history.getFDOrderInfos(EnumSaleType.GIFTCARD));

			// ADD Donation Orders too-for Robin Hood.
			orderHistoryInfo.addAll(history.getFDOrderInfos(EnumSaleType.DONATION));
		}

		return orderHistoryInfo;
	}

	public static List<String> getRecentOrderHistoryInfoIds(FDUserI user) throws FDResourceException {

		List<FDOrderInfoI> orderHistoryInfo = getOrderHistoryInfo(user, true);

		Calendar timeLimit = Calendar.getInstance();
		timeLimit.add(Calendar.MONTH, TIME_LIMIT);
		List<String> ids = new ArrayList<String>();
		for (FDOrderInfoI info : orderHistoryInfo) {
			if (info.getRequestedDate().after(timeLimit.getTime())) {
				ids.add(info.getErpSalesId());
			}
		}

		return ids;
	}

	public static List<StarterList> getStarterLists() {
		List<StarterList> result = new ArrayList<StarterList>();
		FDFolder folder = (FDFolder) ContentFactory.getInstance().getContentNode("starterLists");
		if (folder == null) {
			return result;
		}
		getStarterLists(folder.getContentKey(), result, true, true);
		return result;
	}

	public static int getTopItemsCount(FDUserI user) throws FDResourceException {
		EnumQuickShopTab tab = EnumQuickShopTab.TOP_ITEMS;
		List<QuickShopLineItemWrapper> items = getWrappedOrderHistoryUsingCache(user, tab, tab.cacheName);
		return items.size();
	}

	public static List<QuickShopLineItemWrapper> getWrappedCustomerCreatedLists(FDUserI user, EnumQuickShopTab tab) throws FDResourceException {

		List<QuickShopLineItemWrapper> result = new ArrayList<QuickShopLineItemWrapper>();

		for (FDCustomerCreatedList list : FDListManager.getCustomerCreatedLists(user)) {
			List<FDCustomerListItem> cclItems = list.getLineItems();
			if (cclItems != null) {
				for (FDProductSelectionI productSelection : OrderLineUtil.getValidProductSelectionsFromCCLItems(cclItems)) {

					QuickShopLineItemWrapper fsi = createItemCore(productSelection, list, null, user, tab);
					if (fsi == null) {
						continue;
					}

					result.add(fsi);

				}
			}
		}

		return result;

	}

	/**
	 * Load past orders or top items based on tab (cache used for performance
	 * improvement).
	 *
	 * @param user
	 * @param tab
	 * @param cacheName
	 * @return
	 * @throws FDResourceException
	 */
	public static List<QuickShopLineItemWrapper> getWrappedOrderHistoryUsingCache(FDUserI user, EnumQuickShopTab tab, String cacheName) throws FDResourceException {
        List<QuickShopLineItemWrapper> result = null;
        if(user!=null&&user.getIdentity()!=null){
	        result = CmsServiceLocator.ehCacheUtil().getListFromCache(cacheName, user.getIdentity().getErpCustomerPK());
			if(result!=null
					&& !result.isEmpty()
					&& result.get(0).getProduct()!=null
					&& result.get(0).getProduct().getUserContext()!=null
					&& result.get(0).getProduct().getUserContext().getFulfillmentContext()!=null
					&& result.get(0).getProduct().getUserContext().getFulfillmentContext().getPlantId()!=null
					&& user!=null && user.getUserContext()!=null && user.getUserContext().getFulfillmentContext()!= null && user.getIdentity()!=null
					&& !result.get(0).getProduct().getUserContext().getFulfillmentContext().getPlantId().equals(user.getUserContext().getFulfillmentContext().getPlantId())){
			    CmsServiceLocator.ehCacheUtil().removeFromCache(cacheName, user.getIdentity().getErpCustomerPK());
				result = CmsServiceLocator.ehCacheUtil().getListFromCache(cacheName, user.getIdentity().getErpCustomerPK());
			}
			if (result == null) {
				LOG.info("Wrapping products");
				result = QuickShopHelper.getWrappedOrderHistory(user, tab);
				if (EnumQuickShopTab.TOP_ITEMS.equals(tab)) {
					Collections.sort(result, QuickShopLineItemWrapper.FREQUENCY_COMPARATOR);
					List<FilteringSortingItem<QuickShopLineItemWrapper>> wrappedItems = QuickShopServlet.prepareForFiltering(result);
					if(!FDStoreProperties.isQSTopItemsPerfOptimizationEnabled()){
						removeSkuDuplicates(wrappedItems);
					}
					result = FilteringSortingItem.unwrap(wrappedItems);
					final int topItemMaxCount = FDStoreProperties.getQuickShopResultMaxLimit();
					if (result.size() > topItemMaxCount) {
						result = result.subList(0, topItemMaxCount);
					}
				}
				if (!result.isEmpty() && user!= null && user.getIdentity()!=null) {
	                CmsServiceLocator.ehCacheUtil().putListToCache(cacheName, user.getIdentity().getErpCustomerPK(), new ArrayList<QuickShopLineItemWrapper>(result));
				}
			} else {
				LOG.info("Fetching items from cache");
				result = new ArrayList<QuickShopLineItemWrapper>(result);
			}
		}
		return result;
	}

	public static List<QuickShopLineItemWrapper> getWrappedProductFromStarterList(FDUserI user, List<StarterList> starterLists, List<String> activeReplacements) throws FDResourceException {
		List<QuickShopLineItemWrapper> result = new ArrayList<QuickShopLineItemWrapper>();

		if (starterLists == null) {
			starterLists = getStarterLists();
		}
		if (starterLists == null) {
			return result;
		}
		for (StarterList sl : starterLists) {
			StarterList starterList = (StarterList) ContentFactory.getInstance().getContentNode(sl.getContentKey().getId());
			if (starterList == null) {
				return result;
			}

			for (ProductModel p : starterList.getListContents()) {
				if (p == null || !(p instanceof ConfiguredProduct))
					continue;

				ConfiguredProduct product = (ConfiguredProduct) p;

				if (product instanceof ConfiguredProductGroup)
					product = (ConfiguredProduct) product.getProduct();

				if (product == null)
					continue;

				try {
					FDProductInfo prodInfo = FDCachedFactory.getProductInfo(product.getSkuCode());
					FDProductSelection productSelection = new FDProductSelection(prodInfo, product, product.getConfiguration(), user.getUserContext());
					productSelection.refreshConfiguration();
					OrderLineUtil.describe(productSelection);

					QuickShopLineItemWrapper wrapper = QuickShopHelper.createItemCore(productSelection, null, starterList, user, EnumQuickShopTab.FD_LISTS);
					if (wrapper == null)
						continue;

					wrapper.setStarterList(starterList);

					result.add(wrapper);

				} catch (FDSkuNotFoundException e) {
					LOG.warn("SKU NOT FOUND: " + e);
					throw new FDResourceException(e);
				} catch (FDInvalidConfigurationException e) {
					LOG.warn("INVALID CONFIGURATION " + e);
					throw new FDResourceException(e);
				} catch (FDRuntimeException e) {
					LOG.error("CANNOT REFRESH PRODUCT CONFIGURATION. e: ", e);
					continue;
				}
			}
		}
		return result;
	}

	public static void emptyQuickShopCaches(String userId) {
        CmsServiceLocator.ehCacheUtil().removeFromCache(EnumQuickShopTab.TOP_ITEMS.cacheName, userId);
        CmsServiceLocator.ehCacheUtil().removeFromCache(EnumQuickShopTab.PAST_ORDERS.cacheName, userId);
        CmsServiceLocator.ehCacheUtil().removeFromCache(EnumQuickShopTab.CUSTOMER_LISTS.cacheName, userId);
	}

	public static boolean hasYourFavoritesRecommendation(FDUserI user) {
		try {
			List<QuickShopLineItem> items = QuickShopYmalServlet.doRecommend(user, null, EnumSiteFeature.DYF, 10, null, null);
			return !items.isEmpty();
		} catch (FDResourceException e) {
			LOG.warn("FDResourceException while checking favorites recommender", e);
			return false;
		}
	}

	/**
	 * Populates quickshop specific replacement items
	 *
	 * @param item
	 * @param originalProduct
	 * @param user
	 */
	public static void populateReplacements(QuickShopLineItem item, ProductModel originalProduct, FDUserI user) {
		// look up a recommended replacement
		List<ContentNodeModel> alternatives = originalProduct.getRecommendedAlternatives();

		for (ContentNodeModel node : alternatives) {
			ProductModel altProd = null;
			SkuModel altSku = null;

			// this attribute can have multiple non-compatible types, sort this
			// out ...
			if (node instanceof ProductModel) {
				altProd = (ProductModel) node;
				altSku = altProd.getDefaultSku();
			} else if (node instanceof SkuModel) {
				altSku = (SkuModel) node;
				altProd = altSku.getProductModel();
			} else {
				// unknown type, skip
				continue;
			}
			if (altProd == null || altSku == null || altSku.isUnavailable()) {
				// unavailable, skip
				continue;
			}

			// now we have an available product alternative to recommend
			// create the quickshop line item, and add it to the original item
			try {
				QuickShopLineItem altItem = createItemFromProduct(altProd, altSku, user, true);
				altItem.setItemId("REPLACEMENT_" + item.getItemId());
				item.setReplacement(altItem);

				// populate props from the original lineItem
				altItem.setListId(item.getListId());
				altItem.setOriginalLineId(item.getOriginalLineId());
			} catch (FDResourceException e) {
				LOG.warn("Failed to add replacements: ", e);
				continue;
			}
			// we need only the first one available, so no more iterations
			break;
		}
	}

	public static void postProcessPopulate(FDUserI user, FilteringFlowResult<QuickShopLineItemWrapper> result, HttpSession session) {

		for (FilteringSortingItem<QuickShopLineItemWrapper> fsItem : result.getItems()) {
			QuickShopLineItem qsItem = fsItem.getNode().getItem();

			ProductDetailPopulator.postProcessPopulate(user, qsItem, qsItem.getSkuCode());

			// check if we have temporary configuration for this item in session
			@SuppressWarnings("unchecked")
			Map<String, QuickShopLineItem> tempConfigs = (Map<String, QuickShopLineItem>) session.getAttribute(SessionName.SESSION_QS_CONFIG_REPLACEMENTS);

			if (tempConfigs != null) {
				if (tempConfigs.get(qsItem.getItemId()) != null) {
					qsItem.setTempConfig(tempConfigs.get(qsItem.getItemId()));
				}

				// check if the replacement item has a temp config
				if (qsItem.getReplacement() != null) {
					QuickShopLineItem replacement = qsItem.getReplacement();
					if (tempConfigs.get(replacement.getItemId()) != null) {
						replacement.setTempConfig(tempConfigs.get(replacement.getItemId()));
					}
				}
			}
		}
	}

	public static void removeSkuDuplicates(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		Set<String> skus = new HashSet<String>();
		Iterator<FilteringSortingItem<QuickShopLineItemWrapper>> it = items.iterator();
		while (it.hasNext()) {
			FilteringSortingItem<QuickShopLineItemWrapper> item = it.next();
			if (skus.contains(item.getNode().getItem().getSkuCode())) {
				it.remove();
				continue;
			}
			skus.add(item.getNode().getItem().getSkuCode());
		}
	}

	public static FDUserI getUserFromSession(HttpSession session) {
		return (FDUserI) session.getAttribute(SessionName.USER);
	}

	private static int countOrders(List<QuickShopLineItemWrapper> items) {
		Set<String> orderIds = new HashSet<String>();
		for (QuickShopLineItemWrapper item : items) {
			orderIds.add(item.getOrderId());
		}
		return orderIds.size();
	}

	private static String getCompositeIdForTab(FDProductSelectionI productSelection, StarterList starterList, EnumQuickShopTab tab) {
		String idSuffix = null;
		String compositeId = null;
		if (tab != null) {
			switch (tab) {
			case TOP_ITEMS:
				//$FALL-THROUGH$
			case PAST_ORDERS: {
				idSuffix = productSelection.getOrderLineId();
				compositeId = tab + "_" + idSuffix;
				break;
			}
			case CUSTOMER_LISTS: {
				idSuffix = productSelection.getCustomerListLineId();
				compositeId = tab + "_" + idSuffix;
				break;
			}
			case FD_LISTS: {
				idSuffix = starterList.getContentKey().getId();
				compositeId = tab + "_" + productSelection.getSkuCode() + "_" + idSuffix;
			}
			}
		}
		if (compositeId == null) {
			compositeId = tab + "_" + productSelection.getSkuCode() + "_" + idSuffix;
		}
		return compositeId;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private static void getStarterLists(ContentKey key, List<StarterList> starterLists, boolean active, boolean recursive) {
		ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		List children = null;

		children = (List) node.getAttributeValue("children");
		if (children == null) {
			return;
		}

		for (Object object : children) {
			ContentKey childKey = (ContentKey) object;
			if (childKey.getType().equals(FDContentTypes.STARTER_LIST)) {
				StarterList starterList = (StarterList) ContentFactory.getInstance().getContentNodeByKey(childKey);
				if (!active || starterList.isAvailable()) {
					starterLists.add(starterList);
				}
			} else if (recursive) {
				if (childKey.getType().equals(FDContentTypes.FDFOLDER)) {
					getStarterLists(childKey, starterLists, active, true);
				}
			}
		}
	}

	private static List<QuickShopLineItemWrapper> getWrappedOrderHistory(FDUserI user, EnumQuickShopTab tab) throws FDResourceException {

		List<QuickShopLineItemWrapper> result = new ArrayList<QuickShopLineItemWrapper>();
		List<FDProductSelectionI> items = null;

		//APPDEV-4179 - Item quantities should NOT be honored in "Your Top Items" - START

		if (EnumQuickShopTab.TOP_ITEMS.equals(tab)) {
			items = FDListManager.getQsSpecificEveryItemEverOrderedListTopItems(user.getIdentity(),user.getUserContext().getStoreContext());
		} else {
			items = FDListManager.getQsSpecificEveryItemEverOrderedList(user.getIdentity(), user.getUserContext().getStoreContext());
		}
		//APPDEV-4179 - Item quantities should NOT be honored in "Your Top Items" - END
		if (items == null || items.isEmpty()) {
			return result;
		}

		List<Date> orderDates = new ArrayList<Date>();

		for (FDProductSelectionI item : items) {

			QuickShopLineItemWrapper wrapper = createItemCore(item, null, null, user, tab);
			if (wrapper == null) {
				continue;
			}

			wrapper.setDeliveryDate(item.getDeliveryStartDate());
			wrapper.setOrderId(item.getOrderId());
			wrapper.setOrderStatus(item.getSaleStatus().getDisplayName());

			result.add(wrapper);

			if (!orderDates.contains(item.getDeliveryStartDate())) {
				orderDates.add(item.getDeliveryStartDate());
			}
		}

		Collections.sort(orderDates);
		Collections.reverse(orderDates);
		setLastOrderFlag(result, orderDates.get(0));

		return result;
	}

	private static void populateOrderLineData(QuickShopLineItem item, FDProductSelectionI orderLine) {

		// Orderline data

		item.setConfigInvalid(orderLine.isInvalidConfig());

		if (orderLine.getConfiguration().getOptions() != null && !orderLine.getConfiguration().getOptions().isEmpty()) {
			item.setConfiguration(orderLine.getConfiguration().getOptions());
		}

		item.setConfigDescr(orderLine.getConfigurationDesc());

		item.setDescription(orderLine.getDescription());

		item.setDepartmentDesc(orderLine.getDepartmentDesc());

		if (orderLine.getCustomerListLineId() != null) {
			item.setOriginalLineId(orderLine.getCustomerListLineId());
		} else {
			item.setOriginalLineId(orderLine.getOrderLineId());
		}

		item.setConfiguredPrice(orderLine.getConfiguredPrice());

	}

	private static void setLastOrderFlag(List<QuickShopLineItemWrapper> items, Date last) {

		String lastOrderId = null;

		for (QuickShopLineItemWrapper item : items) {
			if (item.getDeliveryDate().equals(last)) {
				if (lastOrderId == null) {
					lastOrderId = item.getOrderId();
				}
				if (item.getOrderId().equals(lastOrderId)) {
					item.setInLastOrder(true);
				}
			}
		}
	}

    public static FilteringSortingItem<QuickShopLineItemWrapper> createQuickShopFilteringItemWrapper(ProductModel model, FDUserI user) throws FDResourceException {
        QuickShopLineItem aggregatedLineItem = QuickShopHelper.createItemFromProduct(model, model.getDefaultSku(), user, true);
        QuickShopLineItemWrapper wrapper = new QuickShopLineItemWrapper(aggregatedLineItem, model);
        return new FilteringSortingItem<QuickShopLineItemWrapper>(wrapper);
    }

}
