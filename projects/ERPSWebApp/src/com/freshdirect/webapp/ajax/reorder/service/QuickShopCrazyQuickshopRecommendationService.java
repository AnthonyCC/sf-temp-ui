package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentKeyFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.CarouselItemType;
import com.freshdirect.smartstore.fdstore.FactorUtil;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.scoring.HelperFunctions;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.RecommenderServlet;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.taglib.fdstore.GetPeakProduceTag;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class QuickShopCrazyQuickshopRecommendationService {

	private final static Logger LOG = LoggerFactory.getInstance(QuickShopCrazyQuickshopRecommendationService.class);

	private static final QuickShopCrazyQuickshopRecommendationService INSTANCE = new QuickShopCrazyQuickshopRecommendationService();

	public static final String DEPT_FRUIT = "fru";

	public static final String DEPT_VEG = "veg";

	public static final String DEPT_MEAT = "mea";

	// WARNING: Following parts are pure craziness, never do this at home.
	//
	// I am truly sorry about all this.
	// This never should have happened.
	// I told them several times.
	// They did not listen.
	// They made me do this.

	public static final String DEPT_SEAFOOD = "sea";
	public static final String DEPT_DELI = "del";
	public static final String DEPT_CHEESE = "che";
	public static final String DEPT_DAIRY = "dai";
	public static final String DEPT_GROCERY = "gro";
	public static final String DEPT_FROZEN = "fro";
	public static final String DEPT_4MM = "fdi";
	public static final String DEPT_RTC = "rtc";
	public static final String DEPT_HEAT = "hmr";
	public static final String DEPT_BAKERY = "bak";
	public static final String DEPT_CATERING = "cat";
	public static final String DEPT_FLOWERS = "flo";
	public static final String DEPT_PET = "pet";
	public static final String DEPT_PASTA = "pas";
	public static final String DEPT_COFFEE = "cof";
	public static final String DEPT_HBA = "hba";
	public static final String DEPT_BUYBIG = "big";
	private QuickShopCrazyQuickshopRecommendationService() {
	}
	public static QuickShopCrazyQuickshopRecommendationService defaultService() {
		return INSTANCE;
	}
	private static List<QuickShopLineItem> convertToQuickshopItems(FDUserI user, int maxItems, Collection<? extends Object> skus) throws FDResourceException {
		List<QuickShopLineItem> items = new ArrayList<QuickShopLineItem>();

		if (skus != null && skus.size() > 0) {
			for (Object skuObj : skus) {
				SkuModel sku;
				ProductModel product;

				if (skuObj instanceof SkuModel) {
					// We got a SkuModel
					sku = (SkuModel) skuObj;
					product = sku.getProductModel();
				} else if (skuObj instanceof ProductModelPromotionAdapter) {
					// We got a ProductModelPromotionAdapter
					// Warning: ProductModelPromotionAdapter is broken!
					// It can return an unavailable, non-default sku as the
					// default sku!
					// This is dangerous, so we will just get the embedded
					// ProductModel...
					product = ((ProductModelPromotionAdapter) skuObj).getProductModel();
					sku = product.getDefaultSku();
				} else if (skuObj instanceof ProductModel) {
					// We got a ProductModel
					product = (ProductModel) skuObj;
					sku = product.getDefaultSku();
				} else if (skuObj instanceof String) {
					// We got a String, most probably a skuCode
					sku = (SkuModel) ContentFactory.getInstance().getContentNode(ContentType.get("Sku"), (String) skuObj);
					product = sku.getProductModel();
				} else {
					// No more ideas, just skip this item
					continue;
				}

				QuickShopLineItem item = QuickShopHelper.createItemFromProduct(sku.getProductModel(), sku, user, true);

				// Availability check - do not let anything unavailable beyond
				// this point
				if (!item.isAvailable()) {
					continue;
				}

				// Add item to result
				items.add(item);
				if (--maxItems <= 0) {
					break;
				}

			}
		}
		return items;
	}

	private static List<QuickShopLineItem> convertToQuickshopItems(FDUserI user, int maxItems, Recommendations results, boolean useFavBurst) throws FDResourceException {
		List<QuickShopLineItem> items = new ArrayList<QuickShopLineItem>();

		if (results.getAllProducts() != null && results.getAllProducts().size() > 0) {
			Iterator<ProductModel> it = results.getAllProducts().iterator();
			while (maxItems != 0 && it.hasNext()) {
				ProductModel product = it.next();
				QuickShopLineItem item = QuickShopHelper.createItemFromProduct(product, null, user, useFavBurst);

				// Availability check - do not let anything unavailable beyond
				// this point
				if (!item.isAvailable()) {
					continue;
				}

				items.add(item);
				--maxItems;
			}
		}
		return items;
	}

	private static List<QuickShopLineItem> doRecommend(FDUserI user, HttpSession session, EnumSiteFeature siteFeat, int maxItems, Set<ContentKey> listContent, ContentNodeModel currentNode)
			throws FDResourceException {
		Recommendations results = ProductRecommenderUtil.doRecommend(user, session, siteFeat, maxItems, listContent, currentNode);
		List<QuickShopLineItem> items = convertToQuickshopItems(user, maxItems, results, !siteFeat.equals(EnumSiteFeature.DYF));
		return items;
	}

	@SuppressWarnings("unchecked")
	private static List<QuickShopLineItem> doTheCrazyQuickshopRecommendations(FDUserI user, HttpSession session, String deptId, int maxItems, Set<ContentKey> listContent) throws FDResourceException {

		if (deptId != null && !deptId.trim().isEmpty()) {

			try {
				DepartmentModel department = (DepartmentModel) ContentFactory.getInstance().getContentNode(ContentType.get("Department"), deptId);
				if (department != null) {

					if (DEPT_FRUIT.equals(deptId) || DEPT_VEG.equals(deptId) || DEPT_SEAFOOD.equals(deptId)) {
						// For fruit, vegetables and seafood we should show
						// 'great right now' stuff, whatever that is

						// create a 'peak produce tag'
						// it turns out that this is behind the so called 'Great
						// Right Now'
						// don't be misguided by other codes that are labeled
						// 'great right now'
						// they are most probably dead for a while now
						// the 'peak produce tag' is a tragic mess of a code
						// any reasons for it's existence are unknown
						// it would be much much easier to do this in a normal
						// way
						// for example just use smart-store recommenders
						// because that's what they are for in the first
						// place...
						// but we have to recreate the same mess here
						// so that's exactly what we'll do

						// anyway the peak produce tag seems to return SkuModel
						// objects most of the time,
						// but technically they could return anything ...

						GetPeakProduceTag tag = new GetPeakProduceTag();
						tag.setDeptId(deptId);
						tag.setUseMinCount(false);

						Collection<Object> skus = tag.getPeakProduce(department, maxItems,user.getUserContext());
						return convertToQuickshopItems(user, maxItems, skus);

					} else if (DEPT_DELI.equals(deptId) || DEPT_CHEESE.equals(deptId) || DEPT_4MM.equals(deptId) || DEPT_RTC.equals(deptId) || DEPT_HEAT.equals(deptId) || DEPT_BAKERY.equals(deptId)
							|| DEPT_CATERING.equals(deptId) || DEPT_FLOWERS.equals(deptId) || DEPT_PET.equals(deptId) || DEPT_PASTA.equals(deptId) || DEPT_COFFEE.equals(deptId)
							|| DEPT_HBA.equals(deptId) || DEPT_BUYBIG.equals(deptId)) {
						// So called 'customer favorites department level'
						// recommendations

						// siteFeature: SideCart Featured Items (SCR_FEAT_ITEMS)
						// + dept as currentNode

						EnumSiteFeature siteFeature = getSiteFeature("SCR_FEAT_ITEMS");
						return doRecommend(user, session, siteFeature, maxItems, listContent, department);

					} else if (DEPT_DAIRY.equals(deptId) || DEPT_GROCERY.equals(deptId) || DEPT_FROZEN.equals(deptId)) {
						// 'Brand Name Deals' recommendations, + dept as
						// currentNode

						EnumSiteFeature siteFeature = getSiteFeature("BRAND_NAME_DEALS");
						return doRecommend(user, session, siteFeature, maxItems, listContent, department);

					} else if (DEPT_MEAT.equals(deptId)) {
						// For meat we should display 'meat deals'
						// which is actually a manually maintained category,
						// also called as "This week's best deals on meat" in
						// "what's good" department

						CategoryModel meatDeals = (CategoryModel) ContentFactory.getInstance().getContentNode(ContentType.get("Category"), "wgd_butchers");
						if (meatDeals != null) {

							return convertToQuickshopItems(user, maxItems, meatDeals.getAllChildProductsAsList());
						}
					}

					// There are no rules for all other departments, just use
					// the default

				}
			} catch (Exception e) {
				LOG.error("Something failed while serving the crazy quickshop recommendations. Returning default 'customer favorites' instead.", e);
			}
		}

		CategoryModel rootNode = null; 
		
		try {
			rootNode = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey( ContentKeyFactory.getIntance().getPresidentsPicksCategoryKey() );
			if(rootNode==null)
				return Collections.EMPTY_LIST;
		} catch (InvalidContentKeyException e) {
			LOG.error("Missing root node picks_love", e);
			return Collections.EMPTY_LIST;
		}

		@SuppressWarnings({ "rawtypes" })
		List<ContentNodeModel> prespicks = (List) rootNode.getProducts(); // DDPP
		// content!

		List<ProductModel> result = HelperFunctions.getTopN(prespicks, FactorUtil.GLOBAL_POPULARITY_8W_COLUMN, maxItems,
				ProductRecommenderUtil.createSessionInput(session, user, maxItems, rootNode, listContent), ScoreProvider.getInstance());
		return convertToQuickshopItems(user, maxItems, result);
	}

	private static EnumSiteFeature getSiteFeature(String sfName) throws HttpErrorResponse {
		EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(sfName);
		if (siteFeat == null) {
			BaseJsonServlet.returnHttpError(400, "Missing or invalid site feature ID " + sfName);
		}
		return siteFeat;
	}

	public String getTheCrazyQuickshopTitle(String deptId) {
		if (deptId != null && !deptId.trim().isEmpty()) {
			if (DEPT_FRUIT.equals(deptId) || DEPT_VEG.equals(deptId) || DEPT_SEAFOOD.equals(deptId)) {
				return "Great Right Now";
			} else if (DEPT_MEAT.equals(deptId)) {
				return "This Week's Best Deals on Meat";
			} else if (DEPT_DAIRY.equals(deptId)) {
				return "Brand Name Deals in Dairy";
			} else if (DEPT_GROCERY.equals(deptId)) {
				return "Brand Name Deals in Grocery";
			} else if (DEPT_FROZEN.equals(deptId)) {
				return "Brand Name Deals in Frozen";
			}
		}

		// There are no rules for all other departments, just use the default
		return "Customer Favorites";
	}

	public Map<String, Object> populateCrazyQuickshopRecommendation(HttpSession session, RecommendationRequestObject requestData) throws HttpErrorResponse {
		if (requestData == null) {
			requestData = new RecommendationRequestObject();
		}

		// check if we have listId. If yes then recommend product for the given
		// list content
		Set<ContentKey> listContent = null;
		FDUserI user = QuickShopHelper.getUserFromSession(session);
		if (requestData.getListId() != null) {
			// Get the list
			FDCustomerList list = null;
			try {
				list = FDListManager.getCustomerListById(user.getIdentity(), EnumCustomerListType.CC_LIST, requestData.getListId());
				List<FDCustomerListItem> cclItems = list.getLineItems();
				if (cclItems != null) {
					listContent = new HashSet<ContentKey>();
					for (FDProductSelectionI productSelection : OrderLineUtil.getValidProductSelectionsFromCCLItems(cclItems)) {
						ProductModel product = productSelection.lookupProduct();
						if (product == null) {
							continue;
						}
						if (listContent.size() < ProductRecommenderUtil.MAX_LIST_CONTENT_SIZE) {
							listContent.add(product.getContentKey());
						}
					}
				}
			} catch (FDResourceException e) {
				LOG.error("Could not collect list items with listId: " + requestData.getListId() + " e: ", e);
				listContent = null;
			}
		}

        Map<String, Object> result = null;
		try {
			int maxItems = requestData.getNumberOfItems(); // TODO: scripted
			// recommender
			// doesn't care
			// about
			// maxRecommendations??
			// talk with Gabor
			// about that

			String siteFeature = requestData.getFeature();
			List<QuickShopLineItem> items; // this will be the list of resulting
			// items
			String title = null; // optional title

			// Fetch deptId (quickshop department filter)
			String deptId = requestData.getDeptId();
			// Do the crazy dance
			LOG.info("CRAZY_QUICKSHOP recommendations for: deptId=" + deptId);
			items = doTheCrazyQuickshopRecommendations(user, session, deptId, maxItems, listContent);
			title = getTheCrazyQuickshopTitle(deptId);

            if (items.isEmpty()) {
                result = RecommenderServlet.createRecommendationResult(Collections.<String, Object> emptyMap());
            } else {
                result = RecommenderServlet.createRecommenderResult(siteFeature, CarouselItemType.GRID.getType(), title, items, EnumEventSource.REORDER.getName());
            }
		} catch (FDResourceException e) {
			BaseJsonServlet.returnHttpError(500, "Cannot collect recommendations. e: " + e);
		}
		return result;
	}
}
