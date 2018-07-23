package com.freshdirect.webapp.ajax.filtering;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import com.freshdirect.WineUtil;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManager;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.fdstore.content.browse.filter.BrandFilter;
import com.freshdirect.fdstore.content.browse.filter.ContentNodeFilter;
import com.freshdirect.fdstore.content.util.SmartSearchUtils;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.ProductModelPromotionAdapter;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumBrandFilterLocation;
import com.freshdirect.storeapi.content.EnumLayoutType;
import com.freshdirect.storeapi.content.EnumSortingValue;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.HasRedirectUrl;
import com.freshdirect.storeapi.content.Html;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductModelBrandAdsAdapter;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeDepartment;
import com.freshdirect.storeapi.content.SearchResults;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.analytics.service.GoogleAnalyticsDataService;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.SearchPageType;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.CarouselDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.CategoryKey;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContextService;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.MySaleItemsData;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.PagerData;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;
import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.browse.paging.BrowseDataPagerHelper;
import com.freshdirect.webapp.ajax.browse.service.ProductService;
import com.freshdirect.webapp.ajax.cache.EhCacheUtilWrapper;
import com.freshdirect.webapp.ajax.holidaymealbundle.service.HolidayMealBundleService;
import com.freshdirect.webapp.ajax.mealkit.service.MealkitService;
import com.freshdirect.webapp.ajax.product.CriteoProductsUtil;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.search.SearchService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class CmsFilteringFlow {

    private static final Logger LOG = LoggerFactory.getInstance(CmsFilteringFlow.class);

    private static final String FALLBACK_URL = "/";
    private static final String RECIPE_DEPARTMENT_URL_FS = "/recipe_dept.jsp?deptId=%s";
    private static final String SPECIAL_LAYOUT_URL_FS = "/browse_special.jsp?id=%s";
    private static final String HOLIDAY_MEAL_BUNDLE_LAYOUT_URL_FS = "/hmb/category.jsp?id=%s";
    private static final String RECIPE_MEALKIT_LAYOUT_URL_FS = "/handpick/category.jsp?id=%s";
    private static final String ONE_CATEGORY_REDIRECT_URL = "/browse.jsp?id=%s";
    private static final String SUPER_DEPARTMENT_WITHOUT_GLOBALNAV_URL = "/index.jsp";

    private static final CmsFilteringFlow INSTANCE = new CmsFilteringFlow();

    public static CmsFilteringFlow getInstance() {
        return INSTANCE;
    }

    private CmsFilteringFlow() {
    }

	public BrowseData doBrowseSectionsFlow(CmsFilteringNavigator nav, FDSessionUser user)
			throws InvalidFilteringArgumentException, FDResourceException, FDNotFoundException {
		String plantId = user.getUserContext().getFulfillmentContext().getPlantId();
		String cacheKey = user.getUser().getPrimaryKey() + "," +plantId + "," + nav.getId() + ",sec" + nav.getActivePage() + "_" + nav.getPageSize();
		BrowseData browseData = EhCacheUtilWrapper.getObjectFromCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey);
		if (browseData != null) {
			return browseData;
		}

		BrowseDataContext browseDataContext = doBrowseFlow(nav, user);

		if (!nav.isPdp() && !(browseDataContext.getCurrentContainer() instanceof SuperDepartmentModel)) {
			// process sort options
			BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav, user);
		}

		// -- REMOVE EMPTY SECTIONS --
		browseData = browseDataContext.extractBrowseDataPrototype(user, nav);
		if (!browseDataContext.getNavigationModel().isSuperDepartment() && !FDStoreProperties.getPreviewMode()) {
			BrowseDataBuilderFactory.getInstance().removeEmptySections(browseData.getSections().getSections(), null);
		}

		// pagination
		BrowseDataPagerHelper.createPagerContext(browseData, nav, user);

		if (!nav.isPdp()) {
			browseData.getSortOptions().setCurrentOrderAsc(nav.isOrderAscending());
		}

		// remove unused data
		browseData.setMenuBoxes(null);
		browseData.setAdProducts(null);
		browseData.setAssortProducts(null);
		browseData.setCarousels(null);
		browseData.setDescriptiveContent(null);
		browseData.setSearchParams(null);
		browseData.setPager(null);

		EhCacheUtilWrapper.putObjectToCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey, browseData);
		return browseData;
	}

	public CmsFilteringFlowResult doFlow(CmsFilteringNavigator nav, FDUserI user)
			throws InvalidFilteringArgumentException, FDResourceException, FDNotFoundException {

		if (null == nav || null == nav.getPageType()) {
			return new CmsFilteringFlowResult(null, null);
		} else if (nav.getPageType().isBrowseType()) {
			return doFlowForBrowseType(nav, user);

		} else {
			return doFlowForSearchType(nav, user);
		}
	}

    private CmsFilteringFlowResult doFlowForBrowseType(CmsFilteringNavigator nav, FDUserI user) throws InvalidFilteringArgumentException, FDResourceException, FDNotFoundException {
        BrowseData browseData = null;
        String plantId = user.getUserContext().getFulfillmentContext().getPlantId();
        String cacheKey = user.getPrimaryKey()+ "," +plantId + "," + nav.getId();
        BrowseDataContext browseDataContext = getBrowseDataContextFromCacheForPaging(nav, cacheKey);

    	if (browseDataContext == null) {
            browseDataContext = doBrowseFlow(nav, user);
        }

        if (!nav.isPdp() && !nav.isSpecialPage()) { //TODO quick fix for TKG meals (special layouts)
            EhCacheUtilWrapper.putObjectToCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey, browseDataContext);
        }

        // -- SORTING -- (not on pdp or super department page)
        if (!nav.isPdp() && !(browseDataContext.getCurrentContainer() instanceof SuperDepartmentModel)) {
            // process sort options
            BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav, user);
        }

        // -- REMOVE EMPTY SECTIONS --

        browseData = browseDataContext.extractBrowseDataPrototype(user, nav);
        if (!browseDataContext.getNavigationModel().isSuperDepartment() && !FDStoreProperties.getPreviewMode()) {
            BrowseDataBuilderFactory.getInstance().removeEmptySections(browseData.getSections().getSections(), browseDataContext.getMenuBoxes().getMenuBoxes());
        }

        // -- REMOVE MENU BOXES WITH NULL SELECTION --
        MenuBuilderFactory.getInstance().checkNullSelection(browseDataContext.getMenuBoxes().getMenuBoxes());

        if (!nav.isPdp()) {
            /* loop through sectionContexts, and insert from HL list */
            List <SectionData> tempList = (null !=browseData.getSections() && null !=browseData.getSections().getSections() && !browseData.getSections().getSections().isEmpty())?browseData.getSections().getSections().get(0).getSections():null;
            if (tempList == null || tempList.size() == 0) { /* single cat */
                SectionData section = (null !=browseData.getSections() && null !=browseData.getSections().getSections() && !browseData.getSections().getSections().isEmpty()) ? browseData.getSections().getSections().get(0): null;
                if(null !=section){
                    insertHookLogicProductsIntoBrowseData(
                            section.getProducts(),
                            browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId()),
                            user,
                            FDStoreProperties.getHookLogicAllowOwnRows()
                    );
                }
            } else { /* multiple sub cats */
                //skip out if not on show all or first page
                if (nav.getActivePage() <= 1) {
                    int runningTotal = 0;
                    if(null !=browseData.getSections().getSections() && !browseData.getSections().getSections().isEmpty()){
                        SectionData lSection = browseData.getSections().getSections().get(0);
                        if(null !=lSection && null !=lSection.getSections() && !lSection.getSections().isEmpty()){
                            Iterator<SectionData> iterator = lSection.getSections().iterator();
                            while (iterator.hasNext()) {
                                SectionData section = iterator.next();

                                int curSectionSize = null !=section.getProducts()?section.getProducts().size():0;

                                int itemsPerRow = (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.productCard2018, user))
                                    	? 4
                                    	: (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn5_0, user)) 
                            	    		? 5 
                            	    		: (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn4_0, user)) 
                            	    			? 4 
                            	    			: 5;
                                //calc how many HL will be inserted...
                                double calcd = Math.min(
                                        Math.ceil( ((double)curSectionSize / itemsPerRow)),
                                        null !=browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId())?browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId()).size():0
                                );
                                //...and cover lower-end
                                if (calcd > 0 && curSectionSize < itemsPerRow) {
                                    calcd = 1;
                                }

                                //check if we have room, it's the first cat, or it's show all
                                //nav.isAll() doesn't work here, it's always true for cats that display subcats
                                //pageNum is 0, using that for 'all'
                                if ((nav.getActivePage() == 1 || nav.getActivePage() == 0) && (nav.getActivePage() == 0 || runningTotal == 0 || (runningTotal + (curSectionSize+calcd)) <= nav.getPageSize())) {
                                    insertHookLogicProductsIntoBrowseData(
                                            section.getProducts(),
                                            null !=browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId())?browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId()).subList(0, (int) calcd):null,
                                            user,
                                            FDStoreProperties.getHookLogicAllowOwnRows()
                                    );
                                }
                                runningTotal += curSectionSize+calcd; //incr total
                                //break if we're done inserting
                                if (runningTotal > nav.getPageSize() && nav.getActivePage() != 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // -- PAGING --
            BrowseDataPagerHelper.createPagerContext(browseData, nav, user);

            Set<ContentKey> shownProductKeysForRecommender = new HashSet<ContentKey>();
            BrowseDataBuilderFactory.getInstance().collectAllProductKeysForRecommender(browseData.getSections().getSections(), shownProductKeysForRecommender);
            // -- SET NO PRODUCTS MESSAGE --
            if (shownProductKeysForRecommender.size() == 0 && browseDataContext.getNavigationModel().isProductListing()) {
                browseData.getSections().setAllSectionsEmpty(true);
            }
            EhCacheUtilWrapper.putObjectToCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, user.getPrimaryKey()+ "," +plantId, browseDataContext);

            // only display recommenders if on last page
            PagerData pagerData = browseData.getPager();
            if (pagerData.isAll() || isCaroulelsTopAndOnFirstPage(browseData, pagerData) || isCarouselsBottomAndOnLastPage(browseData, pagerData)) {
                boolean disableCategoryYmalRecommender = browseDataContext.getCurrentContainer() instanceof ProductContainer
                        && ((ProductContainer) browseDataContext.getCurrentContainer()).isDisableCategoryYmalRecommender();
                BrowseDataBuilderFactory.getInstance().appendCarousels(browseData, browseDataContext, user, shownProductKeysForRecommender, disableCategoryYmalRecommender,
                        nav.isPdp());
            } else { // remove if already added
                CarouselDataCointainer carousels = browseData.getCarousels();
                carousels.setCarousel1(null);
                carousels.setCarousel2(null);
            }

            try {
                browseData.getDescriptiveContent().setUrl(URLEncoder.encode(nav.assembleQueryString(), CharEncoding.UTF_8));
            } catch (UnsupportedEncodingException e) {
                LOG.error(e);
                throw new FDResourceException(e);
            }
            browseData.getDescriptiveContent().setNavDepth(browseDataContext.getNavigationModel().getNavDepth().name());
            browseData.getDescriptiveContent().setContentId(nav.getId());
            browseData.getSortOptions().setCurrentOrderAsc(nav.isOrderAscending());

            // -- CALCULATE SECTION MAX LEVEL --
            BrowseDataBuilderFactory.getInstance().calculateMaxSectionLevel(browseData.getSections(), browseData.getSections().getSections(), 0);
        }
        return new CmsFilteringFlowResult(browseData, null !=browseDataContext ? browseDataContext.getNavigationModel() :null);
    }

    private CmsFilteringFlowResult doFlowForSearchType(CmsFilteringNavigator nav, FDUserI user) throws InvalidFilteringArgumentException, FDResourceException, FDNotFoundException {
    	BrowseData browseData = null;
    	BrowseDataContext browseDataContext = null;

    	String plantId = user!=null&&user.getUserContext()!=null&&user.getUserContext().getFulfillmentContext()!=null?user.getUserContext().getFulfillmentContext().getPlantId():null;
    	String cacheKey = user.getPrimaryKey()+ "," +plantId + "," + nav.getPageType() + ",sch_" + nav.getActiveTab();
    	browseDataContext = getBrowseDataContextFromCacheForPaging(nav, cacheKey);
		if (browseDataContext == null) {
			browseDataContext = doSearchLikeFlow(nav, user);
		}
        EhCacheUtilWrapper.putObjectToCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey, browseDataContext);

        BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav, user);

        /* Added this condition as part of APPDEV-5920 staff picks sort bar implementation
         * At this point we now have the the sorted section data object which preserves the sort strategy and order of the products
         * Here we are iterating through this sorted section data and adding them to the assort products map based upon the product id
         * Thus the assort product map and section data sorted products are always in sync
         * */
        if(!browseDataContext.getSectionContexts().isEmpty() && null !=browseDataContext.getSectionContexts().get(0).getProductItems()){

        	for(FilteringProductItem filteredProduct: browseDataContext.getSectionContexts().get(0).getProductItems()){
        		if(null !=browseDataContext.getAssortProducts() & null !=browseDataContext.getAssortProducts().getUnfilteredAssortedProducts()
        				&& !browseDataContext.getAssortProducts().getUnfilteredAssortedProducts().isEmpty()){
            		for(ProductData productData:browseDataContext.getAssortProducts().getUnfilteredAssortedProducts()){
            			if(productData.getProductId().equalsIgnoreCase(filteredProduct.getProductModel().getContentKey().getId())){
                			browseDataContext.getAssortProducts().addProdDataToCat(productData.getErpCategory(), productData);
                			break;
                		}
            		}
        		}

        	}
        }
        browseData = browseDataContext.extractBrowseDataPrototype(user, nav);

        /* insert HL products into the correct spot(s) in the results */
        insertHookLogicProductsIntoBrowseData(
                (null !=browseData.getSections() && null !=browseData.getSections().getSections() && !browseData.getSections().getSections().isEmpty()) ? (null !=browseData.getSections().getSections().get(0) ? browseData.getSections().getSections().get(0).getProducts():null): null,/*browseData.getSections().getSections().get(0).getProducts(),*/
                browseData.getAdProducts().getProducts(),
                user,
                FDStoreProperties.getHookLogicAllowOwnRows()
        );

        // -- PAGING --
        if (!FilteringFlowType.PRES_PICKS.equals(nav.getPageType())
                || (FilteringFlowType.PRES_PICKS.equals(nav.getPageType()) && FDStoreProperties.isPresidentPicksPagingEnabled())) {
            BrowseDataPagerHelper.createPagerContext(browseData, nav, user);
        } else {
         	// Populate product data
            populateProductData(browseData, nav, user);
        }

		if (nav.isDescriptiveContentRequested()) {
			try {
				browseData.getDescriptiveContent()
						.setUrl(URLEncoder.encode(nav.assembleQueryString(), CharEncoding.UTF_8));
			} catch (UnsupportedEncodingException e) {
				LOG.error(e);
				throw new FDResourceException(e);
			}
		} else {
			browseData.setDescriptiveContent(null);

		}

		browseData.getSortOptions().setCurrentOrderAsc(nav.isOrderAscending());

        populateSearchCarouselProductLimit(nav, browseDataContext, user);
        return new CmsFilteringFlowResult(browseData, null !=browseDataContext ? browseDataContext.getNavigationModel() :null);
    }
    /* call this BEFORE BrowseDataPagerHelper.createPagerContext(browseData, nav); */
    /* allowOnlyHLRows will toggle filling in empty rows to display the entire HL list */
    public void insertHookLogicProductsIntoBrowseData(List<ProductData> prodList, List<ProductData> hlProdList, FDUserI user, boolean allowOnlyHLRows) {
        /* skip out if hlProds passed in are invalid or empty */
        if (hlProdList == null || hlProdList.size() == 0 || prodList == null || prodList.size() == 0) { return; }

        int itemsPerRow = (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.productCard2018, user))
        	? 4
        	: (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn5_0, user)) 
	    		? 5 
	    		: (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn4_0, user)) 
	    			? 4 
	    			: 5;
        int hlIndex = itemsPerRow-1;
        ProductData pd = new ProductData();
        pd.setProductId("!_SPACER_!");

        Iterator<ProductData> iterator = hlProdList.iterator();
        while (iterator.hasNext()) {
            ProductData result = iterator.next();
            if (hlIndex > prodList.size()) {
                while (hlIndex > prodList.size()) {
                    prodList.add(pd); //add spacer. These count as a "product" so that count needs modification elsewhere
                }
                prodList.add(result); //add to end

                /* break out if we don't want HL-only rows */
                if (!allowOnlyHLRows) {
                    break;
                }
            } else {
                prodList.add(hlIndex, result); //insert
            }
            hlIndex += itemsPerRow;
        }
    }

    private void populateProductData(BrowseData browseData, CmsFilteringNavigator nav, FDUserI user) {
    	List<SectionData> sections = browseData.getSections().getSections();
    	if (sections != null && !sections.isEmpty()) {
    		for (SectionData section : sections) {
    			populateProductData(section, nav, user);
    		}
    	}
    }
    private void populateProductData(SectionData sectionData, CmsFilteringNavigator nav, FDUserI user) {
    	// if this section has products, populate them
    	List<ProductData> products = sectionData.getProducts();
    	if (products != null && !products.isEmpty()) {
    		Iterator<ProductData> it = products.iterator();
    		while (it.hasNext()) {
    			ProductData product = it.next();
    			try {
    				ProductDetailPopulator.populateBrowseProductData(product, user, nav, (nav.isPdp() || FDStoreProperties.getPreviewMode()));
    			} catch(Exception e) {
    				it.remove();
    				LOG.error("Failed to populate product data for " + product==null || product.getProductModel() == null ? "null": product.getProductModel().getContentName()+ " (" + e.getMessage() + ")");
    			}
    		}
    	}
    	// if this section has sub sections, check them also
    	List<SectionData> sections = sectionData.getSections();
    	if (sections != null && !sections.isEmpty()) {
    		for (SectionData section : sections) {
    			populateProductData(section, nav, user);
    		}
    	}
    }
    private boolean isCarouselsBottomAndOnLastPage(BrowseData browseData, PagerData pagerData) {
        return "BOTTOM".equals(browseData.getCarousels().getCarouselPosition()) && pagerData.getActivePage() == pagerData.getPageCount();
    }

    private boolean isCaroulelsTopAndOnFirstPage(BrowseData browseData, PagerData pagerData) {
        return "TOP".equals(browseData.getCarousels().getCarouselPosition()) && pagerData.getActivePage() == 1;
    }

    /**
     * Set page type value in {@code browseDataContext} for paging and sorting cache. This value is also available in {@code browseDataContext.searchParams.pageType} but
     * {@code browseDataContext.searchParams} is empty when we are on browse.
     *
     * @param nav
     * @param browseDataContext
     */
    private void savePageTypeForCaching(FilteringFlowType pageType, BrowseDataContext browseDataContext) {
        if (browseDataContext.getPageType() == null) {
            browseDataContext.setPageType(pageType);
        }
    }

    private BrowseDataContext getBrowseDataContextFromCacheForPaging(CmsFilteringNavigator nav, String cacheKey) {
        BrowseDataContext browseDataContext = null;
        // use userRefinementCache

        browseDataContext = EhCacheUtilWrapper.getObjectFromCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey);
        if (browseDataContext == null || nav.isPdp()) {
        	return null;
        }
        if (!isRequestForTheSamePageType(nav, browseDataContext) ||
        		(!nav.getPageType().equals(FilteringFlowType.BROWSE) && !nav.getPageType().equals(FilteringFlowType.SEARCH))) {
            EhCacheUtilWrapper.removeFromCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey); // if cached has other page type
            browseDataContext = null;
        } else {
            if (FilteringFlowType.BROWSE.equals(nav.getPageType()) &&
            		(!isBrowseRequestForTheSameId(nav, browseDataContext) || !isBrowseRequestForSameFilter(nav, browseDataContext))) {
                EhCacheUtilWrapper.removeFromCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey); // if cached has same page type(browse) but other id or filter
                browseDataContext = null;
            } else if (FilteringFlowType.SEARCH.equals(nav.getPageType()) &&
                	(!isRequestForTheSameSearchParams(nav, browseDataContext) || !isBrowseRequestForSameFilter(nav, browseDataContext))) {
                EhCacheUtilWrapper.removeFromCache(CmsCaches.BR_USER_REFINEMENT_CACHE.cacheName, cacheKey); // if cached has same page type(search) but other search params or filter
                browseDataContext = null;
            }
        }

        return browseDataContext;
    }

    private boolean isRequestForTheSameSearchParams(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
        return browseDataContext != null && navigator != null && browseDataContext.getSearchParams() != null && browseDataContext.getSearchParams().getSearchParams() != null
                && browseDataContext.getSearchParams().getSearchParams().equals(navigator.getSearchParams());
    }

    private boolean isRequestForTheSamePageType(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
        return browseDataContext != null && navigator != null && browseDataContext.getPageType() != null && browseDataContext.getPageType().equals(navigator.getPageType());
    }

    private boolean isBrowseRequestForTheSameId(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
        return browseDataContext != null && browseDataContext.getCurrentContainer() != null && browseDataContext.getCurrentContainer().getContentName() != null
                && browseDataContext.getCurrentContainer().getContentName().equalsIgnoreCase(navigator.getId());
    }

    private boolean isBrowseRequestForSameFilter(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
        return compareFilters(navigator.getRequestFilterParams(), browseDataContext.getRequestFilterParams()) &&
        		compareFilters(navigator.getDataFilterParams(), browseDataContext.getDataFilterParams());
    }

    private boolean compareFilters(Map<String, ?> filter1, Map<String, ?> filter2){
    	// If filter1 is null && filter2 is empty or vice versa
    	if ( (filter1 == null && filter2 != null && filter2.size() == 0) ||
    			(filter1 != null && filter2 == null && filter1.size() == 0)){
    		return true;
    	}
    	// If filter1 is null && filter2 is not empty or vice versa
    	if ( (filter1 == null && filter2 != null) ||
    			(filter1 != null && filter2 == null)){
    		return false;
    	}
    	// If filter1 and filter2 are null or empty
    	if ( (filter1 == null && filter2 == null) ||
    			(filter1.size() == 0 && filter2.size() == 0)){
    		return true;
    	}

    	if(filter1.size() != filter2.size()){
    		return false;
    	}
        return filter1.equals(filter2);
    }
    private void populateSearchCarouselProductLimit(CmsFilteringNavigator nav, BrowseDataContext browseDataContext, FDUserI user) {
    	int activePage = nav.getActivePage();
    	boolean isProductCard2018 = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.productCard2018, user);
        int searchCarouselProductLimit;
        int noOfAdProducts = (null != browseDataContext.getAdProducts() && null != browseDataContext.getAdProducts().getProducts()) ? browseDataContext.getAdProducts()
                .getProducts().size() : 0;
        if (activePage == 0) {
            searchCarouselProductLimit = 0;
        } else {
            searchCarouselProductLimit = FDStoreProperties.getSearchCarouselProductLimit();
            if (isProductCard2018) {
            	searchCarouselProductLimit = (nav.getPageSize() - 4);
            }
        }
        if (searchCarouselProductLimit > 0 && noOfAdProducts > 0 && searchCarouselProductLimit >= noOfAdProducts) {
            //searchCarouselProductLimit = searchCarouselProductLimit - noOfAdProducts;
        }
        browseDataContext.getSections().setLimit(searchCarouselProductLimit);
    }

    private BrowseDataContext doSearchLikeFlow(CmsFilteringNavigator nav, FDUserI user) throws InvalidFilteringArgumentException {
        NavigationModel navigationModel = new NavigationModel();
        SearchResults searchResults = null;

        switch (nav.getPageType()) {
            case SEARCH:
                String searchParams = nav.getSearchParams() == null ? "" : nav.getSearchParams();
                System.out.println("searchParams==============================="+searchParams);
                if (user.getMasqueradeContext() != null) {
                    List<String> listSearchParamsList = getSearchList(searchParams); // see if search term contains multiple terms
                    if (listSearchParamsList.size() > 1) {
                        nav.setListSearchParams(searchParams);
                        searchParams = listSearchParamsList.get(0);
                        nav.setSearchParams(searchParams);
                    }
                }
                searchResults = SearchService.getInstance().searchProducts(searchParams, nav.getRequestCookies(), user,
					nav.getRequestUrl(), nav.getReferer(), nav.isReceipeRequested());

                if (nav.isAdProductRequested() && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user)) {               //if(FDStoreProperties.isHookLogicEnabled()){
                    SearchResultsUtil.getHLBrandProductAdProducts(searchResults, nav,  user, null, null, null);
                } else {
                	searchResults.setAdProducts(null);
                }
               
                collectSearchRelevancyScores(searchResults, nav.getRequestCookies(), user);
                break;
            case NEWPRODUCTS:
                searchResults = SearchResultsUtil.getNewProducts(nav, user);
                break;
            case ECOUPON:
                searchResults = SearchResultsUtil.getProductsWithCoupons(user);
                break;
            case PRES_PICKS:
                searchResults = SearchResultsUtil.getPresidentsPicksProducts(nav);
                break;
            case STAFF_PICKS:
                searchResults = SearchResultsUtil.getStaffPicksProducts(nav);
                break;
            default:
                LOG.error("Invalid page type: " + nav.getPageType());
                throw new InvalidFilteringArgumentException("Invalid page type: " + nav.getPageType(), InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, FALLBACK_URL);
        }

        SearchPageType searchPageType = SearchPageType.PRODUCT; // default behavior
        if (SearchPageType.RECIPE.name.equalsIgnoreCase(nav.getActiveTab())) {
            processRecipes(navigationModel, searchResults);
            searchPageType = SearchPageType.RECIPE;
        } else {
            processProducts(nav.getPageType(), navigationModel, searchResults);
        }

        if (nav.isMenuBoxAndFilterRequested()) {
        	setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
        }

        BrowseDataContext browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), searchPageType).buildBrowseData(navigationModel,
                user, nav);

        // if we are on recipe tab but there is no search result, it should fallback to products
        if (SearchPageType.RECIPE.name.equalsIgnoreCase(nav.getActiveTab()) && 0 == nav.getRecipeHits()) {
            nav.setActiveTab(SearchPageType.PRODUCT.name);
            navigationModel.setRecipeListing(false);
            navigationModel.getRecipeResults().clear();
            processProducts(nav.getPageType(), navigationModel, searchResults);
            searchPageType = SearchPageType.PRODUCT;
            setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
            browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), searchPageType).buildBrowseData(navigationModel, user, nav);
        } else if (SearchPageType.PRODUCT.name.equalsIgnoreCase(nav.getActiveTab()) && 0 == nav.getProductHits() && !searchResults.getRecipes().isEmpty()) {
            nav.setActiveTab(SearchPageType.RECIPE.name);
            navigationModel.setProductListing(false);
            processRecipes(navigationModel, searchResults);
            searchPageType = SearchPageType.RECIPE;
            setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
            browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), searchPageType).buildBrowseData(navigationModel, user, nav);
        }

        browseDataContext.setRequestFilterParams(nav.getRequestFilterParams());
        browseDataContext.setDataFilterParams(nav.getDataFilterParams());

        savePageTypeForCaching(nav.getPageType(), browseDataContext);

        setupFilterAndMenu(nav, user, navigationModel, searchPageType, browseDataContext);

        setDescriptiveContent(nav, user, browseDataContext);
        browseDataContext.getSearchParams().setSearchParams(nav.getSearchParams());
        browseDataContext.getSearchParams().setListSearchParams(nav.getListSearchParams());
        browseDataContext.getSearchParams().setListSearchParamsList(getSearchList(nav.getListSearchParams()));

        browseDataContext.getSearchParams().setSuggestions((List<String>) searchResults.getSpellingSuggestions());
        browseDataContext.getSearchParams().setSearchTerm(searchResults.getSuggestedTerm());
        buildTabs(browseDataContext, searchResults, nav);
        browseDataContext.getSearchParams().setPageType(nav.getPageType());
        if (FilteringFlowType.NEWPRODUCTS.equals(nav.getPageType())) {
            browseDataContext.getSearchParams().setNumberOfNewProducts(searchResults.getProducts().size());
        }

        for (ProductModel product : searchResults.getDDPPProducts()) {
            try {
                ProductData productData = ProductDetailPopulator.createProductData(user, product);
                productData.setFeatured(((ProductModelPromotionAdapter) product).isFeatured());
                productData.setFeaturedHeader(((ProductModelPromotionAdapter) product).getFeaturedHeader());

                String curCat = ((ProductModelPromotionAdapter) product).getErpCategory();
                if (curCat == null || curCat.trim() == "") {
                    //curCat = "ERROR"; // lump all the bad products together
                    curCat = "";
                }
                productData.setErpCategory(curCat);
                productData.setErpCatPosition(((ProductModelPromotionAdapter) product).getErpCatPosition());
                productData.setPriority(((ProductModelPromotionAdapter) product).getPriority());

                if (nav.getPageType() != null) {
                    productData.setPageType(nav.getPageType().toString());
                }
                browseDataContext.getDDPPProducts().getProducts().add(productData);
            //    browseDataContext.getAssortProducts().addProdDataToCat(curCat, productData)
            } catch (FDResourceException e) {
                LOG.warn("Getting DDPP products failed!", e);
            } catch (FDSkuNotFoundException e) {
                LOG.warn("Getting DDPP products failed!", e);
            } catch (HttpErrorResponse e) {
                LOG.warn("Getting DDPP products failed!", e);
            }
        }

       // for staff picks: group the products based on the erp_category column value in product_promotion_group table
        for (Map.Entry<String, List<ProductModel>> entry : searchResults.getAssortProducts().entrySet()) {
            for(ProductModel product:entry.getValue()){

        	try {
            	CategoryKey catKey= new CategoryKey();
                ProductData productData = ProductDetailPopulator.createProductData(user, product);
                productData.setFeatured(((ProductModelPromotionAdapter) product).isFeatured());
                productData.setFeaturedHeader(((ProductModelPromotionAdapter) product).getFeaturedHeader());

                String curCat = ((ProductModelPromotionAdapter) product).getErpCategory();
                if (curCat == null || curCat.trim() == "") {
                    //curCat = "ERROR"; // lump all the bad products together
                    curCat = "%%STAFFPICKS%%"; // We cannot leave this variable blank. UI also needs a change
                }
                productData.setErpCategory(curCat);
                productData.setErpCatPosition(((ProductModelPromotionAdapter) product).getErpCatPosition());
                productData.setPriority(((ProductModelPromotionAdapter) product).getPriority());

                if (nav.getPageType() != null) {
                    productData.setPageType(nav.getPageType().toString());
                }
                catKey.setCategoryName(productData.getErpCategory());
                catKey.setCategoryPosition(productData.getErpCatPosition());
                browseDataContext.getAssortProducts().getUnfilteredAssortedProducts().add(productData);
                browseDataContext.getAssortProducts().addCategoryKeys(catKey);
               /* if(!browseDataContext.getSectionContexts().isEmpty()){

                	for(FilteringProductItem filteredProduct: browseDataContext.getSectionContexts().get(0).getProductItems()){
                		if(productData.getProductId().equalsIgnoreCase(filteredProduct.getProductModel().getContentKey().getId())){
                			browseDataContext.getAssortProducts().addProdDataToCat(catKey.getCategoryName(), productData);
                		}
                	}
                }*/
             //   browseDataContext.getAssortProducts().addProdDataToCat(catKey.getCategoryName(), productData);

            } catch (FDResourceException e) {
                LOG.warn("Getting DDPP products failed!", e);
            } catch (FDSkuNotFoundException e) {
                LOG.warn("Getting DDPP products failed!", e);
            } catch (HttpErrorResponse e) {
                LOG.warn("Getting DDPP products failed!", e);
            }
        }
        }
        // set HookLogic adProducts for 'search like' pages.
        if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user)){   //if(FDStoreProperties.isHookLogicEnabled()){
            StringBuffer updatedPageBeacon = new StringBuffer("&aShown=");
            try {
                if (null != searchResults.getAdProducts() && !searchResults.getAdProducts().isEmpty()) {
                    List<FilteringProductItem> items = ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(searchResults.getAdProducts());
                    items = ProductItemFilterUtil.getFilteredProducts(items, navigationModel.getActiveFilters(), true, true);
                    if (null != items) {
                        for (FilteringProductItem product : items) {

                            if (product.getProductModel() != null && !product.getProductModel().isUnavailable()) {
                                ProductData productData = null;
                                try {
                                    productData = ProductDetailPopulator.createProductData(user, product.getProductModel());
                                } catch (FDResourceException e) {
                                    LOG.warn("Exception while populating HookLogic returned product: ", e);
                                } catch (FDSkuNotFoundException e) {
                                    LOG.warn("Exception while populating HookLogic returned product: ", e);
                                } catch (HttpErrorResponse e) {
                                    LOG.warn("Exception while populating HookLogic returned product: ", e);
                                } catch (Exception e) {
                                    LOG.warn("Exception while populating HookLogic returned product: ", e);
                                }
                                if (null != productData && null != productData.getSkuCode()) {
                                    // updatedPageBeacon.append((("&aShown=".equals(updatedPageBeacon.toString())) ? productData.getSkuCode() : "," + productData.getSkuCode()));
                                    productData.setFeatured(true);
                                    // productData.setFeaturedHeader(((ProductModelPromotionAdapter)product).getFeaturedHeader());
                                    productData.setClickBeacon(((ProductModelBrandAdsAdapter) product.getProductModel()).getClickBeacon());
                                    productData.setImageBeacon(((ProductModelBrandAdsAdapter) product.getProductModel()).getImpBeacon());
                                    if (nav.getPageType() != null) {
                                        productData.setPageType(nav.getPageType().toString());

                                    }
                                    browseDataContext.getAdProducts().getProducts().add(productData);
                                    if (browseDataContext.getAdProducts().getProducts().size() >= FDStoreProperties.getHlProductsCount()) {// DISPLAY ONLY 5 HOOKLOGIC PRODUCTS
                                        break;
                                    }
                                }
                            }

                        }
                    }

                }
                if(null!=searchResults.getEmptyProductsPageBeacon())
                	browseDataContext.getAdProducts().setHlEmptyProductsPagebeacon(searchResults.getEmptyProductsPageBeacon());

                if (null != searchResults.getPageBeacon()) {
                    StringBuffer PageBeacon = new StringBuffer(searchResults.getPageBeacon());
                    browseDataContext.getAdProducts().setPageBeacon(PageBeacon.append(updatedPageBeacon).toString());
                    browseDataContext.getAdProducts().setHlProductsCount(searchResults.getHlProductsCount());
                }
            } catch (Exception e) {
                LOG.warn("Exception while populating HookLogic products: ", e);
            }
        }

        if (nav.isMenuBoxAndFilterRequested()) {
	        relocateBrandFilterBasedOnCmsSetting(browseDataContext);
	        // populate browseData with filterLabels
	        BrowseDataBuilderFactory.getInstance().populateWithFilterLabels(browseDataContext, navigationModel);
        }

        if (FilteringFlowType.SEARCH.equals(nav.getPageType())) {
            browseDataContext.setGoogleAnalyticsData(GoogleAnalyticsDataService.defaultService().populateSearchGAData(browseDataContext.getSearchParams()));
        }

        return browseDataContext;
    }

	private void setupFilterAndMenu(CmsFilteringNavigator nav, FDUserI user, NavigationModel navigationModel,
			SearchPageType searchPageType, BrowseDataContext browseDataContext) {

		// if menus and filters are not requested, just set the navigation model without populating it.
		if (!nav.isMenuBoxAndFilterRequested()) {
			browseDataContext.setNavigationModel(navigationModel);
			return;
		}
        // refresh context sensitive filters
        if (navigationModel.getActiveFilters().size() > 0 && searchPageType == SearchPageType.PRODUCT && browseDataContext.getSectionContexts().size() > 0) {
            refreshResultDependantFilters(nav.getPageType(), navigationModel, browseDataContext.getSectionContexts().get(0).getProductItems());
            setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
        }

        browseDataContext.setNavigationModel(navigationModel);
        MenuBuilderI menuBuilder = MenuBuilderFactory.createBuilderByPageType(null, navigationModel.isSuperDepartment(), searchPageType);
        // create menu
        navigationModel.setLeftNav(menuBuilder.buildMenu(navigationModel.getAllFilters(), navigationModel, nav));
        if (!navigationModel.isSuperDepartment()) { // don't do these in case of super department page
            // menu availability check
            MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user, true);
            reOrderAllMenuItemsByHitCount(navigationModel, browseDataContext);
            reOrderAllMenuItemsByName(navigationModel);
        }
        browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
	}

	private void setDescriptiveContent(CmsFilteringNavigator nav, FDUserI user, BrowseDataContext browseDataContext)
			throws InvalidFilteringArgumentException {

		if (!nav.isDescriptiveContentRequested()) {
			return;
		}
        BrowseData.DescriptiveDataCointainer descriptiveContent = browseDataContext.getDescriptiveContent();
        switch (nav.getPageType()) {
            case SEARCH:
                String pageTitle = nav.getSearchParams() == null ? "" : " - " + nav.getSearchParams();
                descriptiveContent.setPageTitle("FreshDirect - Search" + pageTitle);
                descriptiveContent.setOasSitePage("www.freshdirect.com/search");
                Html searchPageTopMediaBanner = ContentFactory.getInstance().getStore().getSearchPageTopMediaBanner();
                if (searchPageTopMediaBanner != null) {
                    descriptiveContent.setMedia(MediaUtils.renderHtmlToString(searchPageTopMediaBanner, user));
                    descriptiveContent.setMediaLocation("TOP");
                }
                break;
            case ECOUPON:
                descriptiveContent.setPageTitle("FreshDirect - Coupon Circular Page");
                descriptiveContent.setOasSitePage("www.freshdirect.com/ecoupon");
                Html ecouponsPageTopMediaBanner = ContentFactory.getInstance().getStore().getEcouponsPageTopMediaBanner();
                if (ecouponsPageTopMediaBanner != null) {
                    descriptiveContent.setMedia(MediaUtils.renderHtmlToString(ecouponsPageTopMediaBanner, user));
                    descriptiveContent.setMediaLocation("TOP");
                }
                break;
            case PRES_PICKS:
                descriptiveContent.setPageTitle("FreshDirect - Fresh Deals");
                ContentNodeModel node = ContentFactory.getInstance().getContentNode(nav.getId());
                if (node != null)
                    descriptiveContent.setOasSitePage(node.getPath());
                Html presidentPicksPageTopMediaBanner = ContentFactory.getInstance().getStore().getPresidentPicksPageTopMediaBanner();
                if (presidentPicksPageTopMediaBanner != null) {
                    descriptiveContent.setMedia(MediaUtils.renderHtmlToString(presidentPicksPageTopMediaBanner, user));
                    descriptiveContent.setMediaLocation("TOP");
                }
                break;
            case STAFF_PICKS:
                descriptiveContent.setPageTitle("FreshDirect - Staff Picks");
                ContentNodeModel staffPicksNode = ContentFactory.getInstance().getContentNode(nav.getId());
                if (staffPicksNode != null) {
                    descriptiveContent.setOasSitePage(staffPicksNode.getPath());
                } else {
                    descriptiveContent.setOasSitePage("www.freshdirect.com/staffpicks");
                }
                Html staffPicksPageTopMediaBanner = ContentFactory.getInstance().getStore().getStaffPicksPageTopMediaBanner();
                if (staffPicksPageTopMediaBanner != null) {
                    descriptiveContent.setMedia(MediaUtils.renderHtmlToString(staffPicksPageTopMediaBanner, user));
                    descriptiveContent.setMediaLocation("TOP");
                }
                break;
            case NEWPRODUCTS:
                descriptiveContent.setPageTitle("FreshDirect - New products");
                descriptiveContent.setOasSitePage("www.freshdirect.com/newproducts");
                Html newProductsPageTopMediaBanner = ContentFactory.getInstance().getStore().getNewProductsPageTopMediaBanner();
                if (newProductsPageTopMediaBanner != null) {
                    descriptiveContent.setMedia(MediaUtils.renderHtmlToString(newProductsPageTopMediaBanner, user));
                    descriptiveContent.setMediaLocation("TOP");
                }
                break;
            default:
                LOG.error("Invalid page type: " + nav.getPageType());
                throw new InvalidFilteringArgumentException("Invalid page type: " + nav.getPageType(), InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, FALLBACK_URL);
        }
	}

    private void relocateBrandFilterBasedOnCmsSetting(BrowseDataContext browseDataContext) {
		// If search result doesn't need to show filter/menu,
		// navigation model will be null
		if (browseDataContext.getNavigationModel() == null) {
			return;
		}
        EnumBrandFilterLocation location = browseDataContext.getNavigationModel().getBrandFilterLocation();
        if (location != null && EnumBrandFilterLocation.ORIGINAL != location) {
            MenuBuilderFactory.getInstance().relocateBrandFilter(browseDataContext.getMenuBoxes().getMenuBoxes(), location);
        }
    }

    /** based on com.freshdirect.webapp.taglib.fdstore.FDParseSearchTermsTag.getSearchList(String) **/
    private List<String> getSearchList(String params) {
        List<String> list = new ArrayList<String>();
        if (params != null && !"".equals(params.trim())) {
            StringTokenizer tokenizer = new StringTokenizer(params, ",\r\n\f\"\'");
            while (tokenizer.hasMoreTokens()) {
                String currentToken = tokenizer.nextToken().trim();
                if (!"".equals(currentToken)) {
                    list.add(currentToken);
                }
            }
        }
        return list;
    }

    private void reOrderAllMenuItemsByHitCount(NavigationModel navigationModel, BrowseDataContext browseDataContext) {
        List<MenuBoxData> leftNav = navigationModel.getLeftNav();
        List<FilteringProductItem> items = BrowseDataContextService.getDefaultBrowseDataContextService().collectAllItems(browseDataContext);
        Map<String, ProductItemFilterI> allFilters = ProductItemFilterUtil.prepareFilters(navigationModel.getAllFilters());
        reOrderMenuItemsByHitCountForMenuBox(leftNav, items, allFilters, NavigationUtil.DEPARTMENT_FILTER_GROUP_ID);
        reOrderMenuItemsByHitCountForMenuBox(leftNav, items, allFilters, NavigationUtil.RECIPE_CATEGORY_FILTER_GROUP);
    }

    private void reOrderMenuItemsByHitCountForMenuBox(List<MenuBoxData> leftNav, List<FilteringProductItem> items, Map<String, ProductItemFilterI> allFilters, String menuBoxId) {
        MenuBoxData menuBoxData = MenuBoxDataService.getDefaultMenuBoxDataService().getMenuBoxById(menuBoxId, leftNav);
        reOrderMenuItemsByHitCount(menuBoxData, items, allFilters);
        MenuBoxDataService.getDefaultMenuBoxDataService().removeHitCountFromAllFilters(menuBoxData);
    }

    private void reOrderMenuItemsByHitCount(MenuBoxData menuBoxData, List<FilteringProductItem> items, Map<String, ProductItemFilterI> allFilters) {
        if (menuBoxData != null && menuBoxData.getSelectedLabel() == null) {
            MenuItemSorter.getDefaultMenuItemSorter().sortItemsByHitCount(menuBoxData, items, allFilters);
        }
    }

    private void reOrderAllMenuItemsByName(NavigationModel navigationModel) {
        List<MenuBoxData> leftNav = navigationModel.getLeftNav();
        reOrderMenuItemsByNameForMenuBox(leftNav, NavigationUtil.BRAND_FILTER_GROUP_ID);
    }

    private void reOrderMenuItemsByNameForMenuBox(List<MenuBoxData> leftNav, String menuBoxId) {
        MenuBoxData menuBoxData = MenuBoxDataService.getDefaultMenuBoxDataService().getMenuBoxById(menuBoxId, leftNav);
        reOrderMenuItemsByName(menuBoxData);
    }

    private void reOrderMenuItemsByName(MenuBoxData menuBoxData) {
        if (menuBoxData != null) {
            MenuItemSorter.getDefaultMenuItemSorter().sortItemsByName(menuBoxData);
        }
    }

    private void setupAllAndActiveFiltersForNavigationModel(CmsFilteringNavigator nav, FDUserI user, NavigationModel navigationModel) {
        navigationModel.setAllFilters(NavigationUtil.createSearchFilterGroups(navigationModel, nav, user));
        navigationModel.setActiveFilters(NavigationUtil.selectActiveFilters(navigationModel.getAllFilters(), nav));
    }

    private void processRecipes(NavigationModel navigationModel, SearchResults recipeResults) {
        navigationModel.setRecipeListing(true);
        for (FilteringSortingItem<Recipe> recipe : recipeResults.getRecipes()) {
            Recipe recipeModel = recipe.getModel();
            navigationModel.getRecipeResults().add(recipeModel);
        }
    }

    private void buildTabs(BrowseDataContext browseDataContext, SearchResults results, CmsFilteringNavigator cmsFilteringNavigator) {
        switch (cmsFilteringNavigator.getPageType()) {
            case SEARCH:
                if (SearchPageType.RECIPE.name.equalsIgnoreCase(cmsFilteringNavigator.getActiveTab())) {
                    if (!results.getProducts().isEmpty()) {
                        browseDataContext.getSearchParams().buildTabs(SearchPageType.PRODUCT.label, SearchPageType.PRODUCT.name, results.getProducts().size(),
                                results.getProducts().size(), false);
                    }
                    browseDataContext.getSearchParams().buildTabs(SearchPageType.RECIPE.label, SearchPageType.RECIPE.name, results.getRecipes().size(),
                            cmsFilteringNavigator.getRecipeHits(), true);
                } else {
                    browseDataContext.getSearchParams().buildTabs(SearchPageType.PRODUCT.label, SearchPageType.PRODUCT.name, results.getProducts().size(),
                            Math.min(results.getProducts().size(), cmsFilteringNavigator.getProductHits()), true); // Math.min ~ workaround for product losses because of non
                                                                                                                   // existent default sku
                    if (!results.getRecipes().isEmpty()) {
                        browseDataContext.getSearchParams().buildTabs(SearchPageType.RECIPE.label, SearchPageType.RECIPE.name, results.getRecipes().size(),
                                results.getRecipes().size(), false);
                    }
                }
                break;
            default:
                break;
        }
    }

    /** Collecting search results, filtering info and sort options for search results */
    private void processActiveFilters(NavigationModel navigationModel) {
        BrandFilter selectedBrandFilter = null;
        AbstractProductItemFilter selectedShowMeOnlyFilter = null;

        for (ProductItemFilterI productFilter : navigationModel.getActiveFilters()) {
            if (productFilter instanceof BrandFilter) {
                selectedBrandFilter = (BrandFilter) productFilter;
            }
            if (!(productFilter instanceof BrandFilter) && !(productFilter instanceof ContentNodeFilter)) { // aka showMeOnlyFilter
                selectedShowMeOnlyFilter = (AbstractProductItemFilter) productFilter; // TODO what more than one show me only filter is selected?
            }
        }
        navigationModel.getShowMeOnlyOfSearchResults().clear();
        navigationModel.getBrandsOfSearchResults().clear();
        if (selectedBrandFilter != null) {
            navigationModel.getBrandsOfSearchResults().put(selectedBrandFilter.getBrand().getContentName(), selectedBrandFilter.getBrand());
        }
        if (selectedShowMeOnlyFilter != null) {
            navigationModel.getShowMeOnlyOfSearchResults().add(selectedShowMeOnlyFilter.getId());
        }
    }

    /** based on ProductsFilterImpl.createComparator() and FilteringComparatorUtil.createProductComparator() */
    private void collectSearchRelevancyScores(SearchResults searchResults, Cookie[] cookies, FDUserI user) {
        String suggestedTerm = NVL.apply(searchResults.getSuggestedTerm(), searchResults.getSearchTerm());
        List<FilteringSortingItem<ProductModel>> products = searchResults.getProducts();

        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, cookies, user)) {
            //for unbxd search results the relevancy is the order of in which the products are returned
            for(int i = products.size(); i > 0; i--){
                products.get(products.size() - i).putSortingValue(EnumSortingValue.TERM_SCORE, i);
            }
        } else {
            // if there's only one DYM then we display products for that DYM
            // but for those products we have to use the suggested term to produce the following scores
            SmartSearchUtils.collectOriginalTermInfo(products, suggestedTerm);
            SmartSearchUtils.collectRelevancyCategoryScores(products, suggestedTerm);
            SmartSearchUtils.collectTermScores(products, suggestedTerm);
        }
    }

    private void processProducts(FilteringFlowType pageType, NavigationModel navigationModel, SearchResults searchResults) {
        navigationModel.setProductListing(true);

        Iterator<FilteringSortingItem<ProductModel>> iterator = searchResults.getProducts().iterator();
        while (iterator.hasNext()) {
            FilteringSortingItem<ProductModel> result = iterator.next();
            ProductModel product = result.getModel();
            boolean showMeOnlyNewDisabledOnNewProductsPage = FilteringFlowType.NEWPRODUCTS.equals(pageType);
            try {
                FilterCollector.defaultFilterCollector(showMeOnlyNewDisabledOnNewProductsPage).collectBrandAndShowMeOnlyFilters(navigationModel, product); // TODO is this necessary
                                                                                                                                                           // when
                                                                                                                                                           // refreshResultDependantFilters()
                                                                                                                                                           // will run as well?
            } catch (FDException e) {
                LOG.error("Filtering setup failed: " + e.getMessage());
                iterator.remove();
            }

            navigationModel.getSearchResults().add(result);
            FilterCollector.defaultFilterCollector(showMeOnlyNewDisabledOnNewProductsPage).collectDepartmentAndCategoryFilters(navigationModel, product);
        }
    }

    private void refreshResultDependantFilters(FilteringFlowType pageType, NavigationModel navigationModel, List<FilteringProductItem> results) {
        processActiveFilters(navigationModel);

        Iterator<FilteringProductItem> iterator = results.iterator();
        while (iterator.hasNext()) {
            ProductModel product = iterator.next().getProductModel();

            try {
                boolean showMeOnlyNewDisabledOnNewProductsPage = FilteringFlowType.NEWPRODUCTS.equals(pageType);
                FilterCollector.defaultFilterCollector(showMeOnlyNewDisabledOnNewProductsPage).collectBrandAndShowMeOnlyFilters(navigationModel, product);
            } catch (FDException e) {
                LOG.error("Filtering setup failed: " + e.getMessage());
                iterator.remove();
            }
        }
    }

    private BrowseDataContext doBrowseFlow(CmsFilteringNavigator nav, FDUserI user) throws InvalidFilteringArgumentException, FDResourceException, FDNotFoundException {

        BrowseDataContext browseDataContext = null;

        String id = nav.getId();
        ContentNodeModel contentNodeModel = PopulatorUtil.getContentNode(id);

        // validation
        validateNode(nav, contentNodeModel, id, user);

        // override show all
        if (contentNodeModel instanceof SuperDepartmentModel || ((ProductContainer) contentNodeModel).isTopLevelCategory()
                && ((ProductContainer) contentNodeModel).isShowAllByDefault()) {
            nav.setAll(true);
        }

        // create filters and building menu
        NavigationModel navigationModel = NavigationUtil.createNavigationModel(contentNodeModel, nav, user);

        // filtering and grouping
        browseDataContext = BrowseDataBuilderFactory.createBuilder(navigationModel.getNavDepth(), navigationModel.isSuperDepartment(), null).buildBrowseData(navigationModel, user,
                nav);

        if (!nav.isPdp() &&
        		!nav.populateSectionsOnly() &&
        		null != browseDataContext &&
        		displayHookLogicProducts(nav, user, contentNodeModel)) {         //if(FDStoreProperties.isHookLogicEnabled()){
            String catId = nav.getId();
            Map<String, List<ProductData>> hlSelectionsofProductsList=new HashMap<String, List<ProductData>>();
            Map<String, String> hlSelectionsofPageBeacons=new HashMap<String, String>();
            Map<String, Integer> hlCatProductsCount=new HashMap<String, Integer>();
            Map<String, String> hlCatEmptyProductPageBeacon=new HashMap<String, String>();
            List<SectionContext> sectionContexts = browseDataContext.getSectionContexts();
            getAdProductsByCategory(user, navigationModel, catId, hlSelectionsofProductsList, hlSelectionsofPageBeacons, sectionContexts,
            		browseDataContext, hlCatProductsCount, hlCatEmptyProductPageBeacon, nav.isMobile());
            browseDataContext.getAdProducts().setHlSelectionOfProductList(hlSelectionsofProductsList);
            browseDataContext.getAdProducts().setHlSelectionsPageBeacons(hlSelectionsofPageBeacons);
            browseDataContext.getAdProducts().setHlCatProductsCount(hlCatProductsCount);

        }
		if(nav.isPdp() && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user)) {
				browseDataContext.setProductId(nav.getProductId());
					CriteoProductsUtil.getHlBrandPdpProducts(user, browseDataContext);
		}
        // inject references
        browseDataContext.setNavigationModel(navigationModel);
        browseDataContext.setRequestFilterParams(nav.getRequestFilterParams());
        browseDataContext.setCurrentContainer(contentNodeModel);

        if (isCategoryAggregationApplicable(nav, user, contentNodeModel)) {
            applyCategoryAggregation(nav, browseDataContext.getSectionContexts());
        }

        savePageTypeForCaching(nav.getPageType(), browseDataContext);

        if (!navigationModel.isSuperDepartment() && !nav.populateSectionsOnly()) { // don't do these in case of super department page
            // menu availability check
            MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user);
        }


        if (!nav.populateSectionsOnly()) {
        	// populate browseData with the menu
	        browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
	        final ContentNodeModel departmentorSuperDepartment = getDepartmentOrSuperDepartment(contentNodeModel, navigationModel);
	        browseDataContext.getMenuBoxes().setMenuName(departmentorSuperDepartment.getFullName());
	        browseDataContext.getMenuBoxes().setMenuId(departmentorSuperDepartment.getContentKey().getId());
	        // -- POPULATE EXTRA DATA --

	        // populate browseData with breadcrumbs
        	BrowseDataBuilderFactory.getInstance().populateWithBreadCrumbAndDesciptiveContent(browseDataContext, navigationModel);
        	relocateBrandFilterBasedOnCmsSetting(browseDataContext);


	        // populate browseData with filterLabels
	        BrowseDataBuilderFactory.getInstance().populateWithFilterLabels(browseDataContext, navigationModel);

	        boolean isWineDepartment = checkWineDepartment(navigationModel);
	        browseDataContext.getDescriptiveContent().setWineDepartment(isWineDepartment);

	        if (contentNodeModel instanceof CategoryModel && ((CategoryModel) contentNodeModel).getSpecialLayout() != null) {
	            if (EnumLayoutType.HOLIDAY_MEAL_BUNDLE_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())) {
	                browseDataContext.setTopMedia(HolidayMealBundleService.defaultService().populateHolidayMealCategoryMedia(navigationModel));
	            } else if (EnumLayoutType.RECIPE_MEALKIT_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())) {
	                browseDataContext.setTopMedia(MealkitService.defaultService().populateMealkitCategoryMedia(navigationModel));
	            }
	        }
        }
        return browseDataContext;
    }

	private boolean displayHookLogicProducts(CmsFilteringNavigator nav, FDUserI user,
			ContentNodeModel contentNodeModel) {
		return FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user) && FDStoreProperties.isHookLogicForCategoriesEnabled()
				&& !isExcludedForHookLogicProducts(contentNodeModel, nav);
	}

	private boolean isExcludedForHookLogicProducts(ContentNodeModel contentNodeModel, CmsFilteringNavigator nav){
		boolean isExcluded = false;
		List<String> excludedDeptOrCats = FDStoreProperties.getHookLogicExcludedDepOrCatIds();
		if(null !=excludedDeptOrCats && !excludedDeptOrCats.isEmpty()){
			isExcluded = excludedDeptOrCats.contains(nav.getId());
		}
		if(!isExcluded){
			String parentId = null;
			while ((contentNodeModel != null && !(contentNodeModel instanceof DepartmentModel))) {
				parentId = contentNodeModel.getParentId();
				if(null != parentId ){
					isExcluded = excludedDeptOrCats.contains(parentId.toLowerCase());
					if(isExcluded){
						break;
					}
				}
				contentNodeModel = contentNodeModel.getParentNode();
			}

		}
		return isExcluded;
	}
    private void getAdProductsByCategory(FDUserI user, NavigationModel navigationModel, String catId, Map<String, List<ProductData>> hlSelectionsofProductsList,
            Map<String, String> hlSelectionsofPageBeacons, List<SectionContext> sectionContexts, BrowseDataContext browseDataContext, Map<String, Integer> hlCatProductsCount,
            Map<String, String> hlCatEmptyProductPageBeacon, boolean isMobile) throws FDResourceException {

        if (null != sectionContexts) {
            for (Iterator<SectionContext> iterator = sectionContexts.iterator(); iterator.hasNext();) {
                SectionContext categorySectionsContext = iterator.next();
                getAdProductsByCategory(user, navigationModel, categorySectionsContext.getCatId(), hlSelectionsofProductsList, hlSelectionsofPageBeacons,
                        categorySectionsContext.getSectionContexts(), browseDataContext, hlCatProductsCount, hlCatEmptyProductPageBeacon, isMobile);
            }
            getAdProductsByCategory(user, navigationModel, catId, hlSelectionsofProductsList, hlSelectionsofPageBeacons, browseDataContext, hlCatProductsCount, hlCatEmptyProductPageBeacon, isMobile);
        }
    }

    private void getAdProductsByCategory(FDUserI user, NavigationModel navigationModel, String catId, Map<String, List<ProductData>> hlSelectionsofProductsList,
            Map<String, String> hlSelectionsofPageBeacons, BrowseDataContext browseDataContext,
            Map<String, Integer> hlCatProductsCount, Map<String, String> hlCatEmptyProductPageBeacon, boolean isMobile) throws FDResourceException {

        HLBrandProductAdRequest hLBrandProductAdRequest = new HLBrandProductAdRequest();
        hLBrandProductAdRequest.setUserId(user.getPrimaryKey());
        hLBrandProductAdRequest.setCustomerId(user.getPrimaryKey());
        hLBrandProductAdRequest.setCategoryId(catId);

        if(isMobile)
			hLBrandProductAdRequest.setPlatformSource("mobile");
	 	  else
		 	 hLBrandProductAdRequest.setPlatformSource("web");

        try {
            HLBrandProductAdResponse hlBrandProductAdResponse = FDBrandProductsAdManager.getHLCategoryProducts(hLBrandProductAdRequest);
            List<ProductModel> adPrducts = new ArrayList<ProductModel>();
            if(null !=hlBrandProductAdResponse){
	            List<HLBrandProductAdInfo> hlBrandAdProductsMeta = hlBrandProductAdResponse.getProductAd();


	            if (hlBrandAdProductsMeta != null) {
					hlCatProductsCount.put(catId, hlBrandAdProductsMeta.size());

	                for (Iterator<HLBrandProductAdInfo> iterator = hlBrandAdProductsMeta.iterator(); iterator.hasNext();) {
	                    HLBrandProductAdInfo hlBrandProductAdMetaInfo = iterator.next();
	                    hlBrandProductAdMetaInfo.setPageBeacon(hlBrandProductAdResponse.getPageBeacon());
	                    browseDataContext.getAdProducts().setPageBeacon(hlBrandProductAdResponse.getPageBeacon());

	                    try {
	                        ProductModel productModel = ContentFactory.getInstance().getProduct(hlBrandProductAdMetaInfo.getProductSKU());
	                        if (null != productModel) {
	                            ProductModelBrandAdsAdapter pm = new ProductModelBrandAdsAdapter(productModel, hlBrandProductAdMetaInfo.getClickBeacon(),
	                                    hlBrandProductAdMetaInfo.getImpBeacon());
	                            adPrducts.add(pm);
	                        }

	                    } catch (FDSkuNotFoundException e) {
	                        LOG.info("FDSkuNotFoundException while populating HookLogic product : ", e);
	                    }
	                }
	            }
	            else {
	            	hlCatEmptyProductPageBeacon.put(catId,	hlBrandProductAdResponse.getPageBeacon());
	            }
            }

            List<FilteringSortingItem<ProductModel>> productResults = new ArrayList<FilteringSortingItem<ProductModel>>();
            if (null != adPrducts) {
                for (ProductModel productModel : adPrducts) {
                    FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
                    productResults.add(item);
                }
            }
            List<ProductData> hlAvailableProductDataList = getProductDataList(user, productResults, navigationModel);
            hlSelectionsofProductsList.put(catId, hlAvailableProductDataList);
            buildHlCategoriesPageBeacon(hlSelectionsofPageBeacons, browseDataContext, user, productResults, catId);
            browseDataContext.getAdProducts().setHlSelectionsEmptyProductsPageBeacons(hlCatEmptyProductPageBeacon);
        } catch (BrandProductAdServiceException e) {
            // TODO Auto-generated catch block
            LOG.warn("Exception while populating HookLogic Product for Categorypages: ", e);
        } catch (Exception e) {
            LOG.warn("Exception while populating HookLogic Product for Categorypages: ", e);
        }
    }

    /**
     * Aggregate sub-category products into single section and remove sub-category titles
     *
     * @param sectionContexts
     */
    private void applyCategoryAggregation(CmsFilteringNavigator nav, List<SectionContext> sectionContexts) {
        if (sectionContexts != null && !sectionContexts.isEmpty()) {
            SectionContext mainSectionContext = sectionContexts.get(0);
            SectionContext mergedSectionContext = new SectionContext();
            List<FilteringProductItem> productItems = new ArrayList<FilteringProductItem>();
            mergedSectionContext.setCatId("");
            mergedSectionContext.setProductItems(productItems);
            collectCategoryProducts(nav, mainSectionContext, mergedSectionContext);
            List<SectionContext> subSectionContexts = mainSectionContext.getSectionContexts();
            if (subSectionContexts == null){
                subSectionContexts = new ArrayList<SectionContext>();
                mainSectionContext.setSectionContexts(subSectionContexts);
            }
            if (!productItems.isEmpty()) {
                subSectionContexts.clear();
                subSectionContexts.add(mergedSectionContext);
            }
            ProductService.defaultService().removeContentKeyDuplicates(productItems);
        }
    }

    private void collectCategoryProducts(CmsFilteringNavigator nav, SectionContext actualSectionContext, SectionContext mergedSectionContext) {
        if (actualSectionContext.getProductItems() != null && nav.isAggregateCategories()){
            mergedSectionContext.getProductItems().addAll(actualSectionContext.getProductItems());
            actualSectionContext.getProductItems().clear();
        }
        List<SectionContext> childSectionContexts = actualSectionContext.getSectionContexts();
        if (childSectionContexts != null) {
            for (SectionContext childSectionContext : childSectionContexts) {
                List<FilteringProductItem> productItems = childSectionContext.getProductItems();
                if (productItems != null) {
                    mergedSectionContext.getProductItems().addAll(productItems);
                }
                if (nav.isAggregateCategories()) {
                    collectCategoryProducts(nav, childSectionContext, mergedSectionContext);
                }
            }
        }
    }

    private boolean isCategoryAggregationApplicable(CmsFilteringNavigator nav, FDUserI user, ContentNodeModel contentNodeModel) {
        boolean isAggregationFeatureActive = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.browseaggregatedcategories1_0, nav.getRequestCookies(), user);
        boolean containsCategoryProperties = FDStoreProperties.getBrowseAggregatedCategories().contains(contentNodeModel.getContentKey().getEncoded());
        return (isAggregationFeatureActive && containsCategoryProperties) || nav.isAggregateCategories();
    }

    private boolean checkWineDepartment(NavigationModel navigationModel) {
        boolean result = false;
        if (navigationModel != null && navigationModel.getNavigationHierarchy() != null) {
            ContentNodeModel department = navigationModel.getNavigationHierarchy().get(NavDepth.DEPARTMENT);
            result = department != null && department.getContentKey() != null && WineUtil.getWineAssociateId().equalsIgnoreCase(department.getContentKey().getId());
        }
        return result;
    }

    private ContentNodeModel getDepartmentOrSuperDepartment(ContentNodeModel contentNodeModel, NavigationModel navigationModel) {
        final ContentNodeModel departmentorSuperDepartment;
        if (contentNodeModel instanceof SuperDepartmentModel) {
            departmentorSuperDepartment = contentNodeModel;
        } else if (contentNodeModel instanceof DepartmentModel) {
            SuperDepartmentModel superDepartmentModel = navigationModel.getSuperDepartmentModel();
            if (superDepartmentModel != null) {
                departmentorSuperDepartment = superDepartmentModel;
            } else {
                departmentorSuperDepartment = contentNodeModel;
            }
        } else {
            SuperDepartmentModel superDepartmentModel = navigationModel.getSuperDepartmentModel();
            if (superDepartmentModel != null) {
                departmentorSuperDepartment = superDepartmentModel;
            } else {
                departmentorSuperDepartment = navigationModel.getNavigationHierarchy().get(NavDepth.DEPARTMENT);
            }
        }
        return departmentorSuperDepartment;
    }

    private void validateNode(CmsFilteringNavigator nav, ContentNodeModel contentNodeModel, String id, FDUserI user) throws InvalidFilteringArgumentException {

        if (!FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user) && contentNodeModel instanceof SuperDepartmentModel) {
            throw new InvalidFilteringArgumentException("Following SuperDepartment page is referred without globalNav rolled out: " + id,
                    InvalidFilteringArgumentException.Type.SUPER_DEPARTMENT_WITHOUT_GLOBALNAV, SUPER_DEPARTMENT_WITHOUT_GLOBALNAV_URL);
        }

        if (contentNodeModel instanceof RecipeDepartment) {
            throw new InvalidFilteringArgumentException("Node is recipe department: " + id, InvalidFilteringArgumentException.Type.NODE_IS_RECIPE_DEPARTMENT, String.format(
                    RECIPE_DEPARTMENT_URL_FS, id));
        }

        if (!(contentNodeModel instanceof ProductContainer || contentNodeModel instanceof SuperDepartmentModel)) { // applies for null as well
            throw new InvalidFilteringArgumentException("Node is not a product container or null: " + id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE,
                    String.format(FALLBACK_URL, id));
        }

        if (contentNodeModel instanceof CategoryModel && (NavigationUtil.isCategoryForbiddenInContext(user, (CategoryModel) contentNodeModel)) && !nav.isPdp()) {
            throw new InvalidFilteringArgumentException("Category is forbidden: " + id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, String.format(FALLBACK_URL, id));
        }

        // throw exception if we have special layout and we are not on the browse_special.jsp or pdp.jsp
        if (!nav.isPdp() && !nav.isSpecialPage() && contentNodeModel instanceof CategoryModel && ((CategoryModel) contentNodeModel).getSpecialLayout() != null) {
            if (EnumLayoutType.HOLIDAY_MEAL_BUNDLE_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())) {
                throw new InvalidFilteringArgumentException("Node has holiday meal bundle layout: " + id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT, String.format(
                        HOLIDAY_MEAL_BUNDLE_LAYOUT_URL_FS, id));
            } else if (EnumLayoutType.RECIPE_MEALKIT_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())) {
                throw new InvalidFilteringArgumentException("Node has recipe mealkit layout: " + id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT, String.format(
                        RECIPE_MEALKIT_LAYOUT_URL_FS, id));
            } else {
                throw new InvalidFilteringArgumentException("Node has special layout: " + id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT, String.format(
                        SPECIAL_LAYOUT_URL_FS, id));
            }
        }

        // handle hide url case
        if (!ContentFactory.getInstance().getPreviewMode()) {
            String hideUrl = contentNodeModel.getHideUrl();

            if (!nav.isPdp() && ContentNodeModelUtil.isRedirectUrlValid(hideUrl)) {
                throw new InvalidFilteringArgumentException("Node is hidden on production site; browser will be redirected to: " + id, InvalidFilteringArgumentException.Type.NODE_HAS_REDIRECT_URL, hideUrl);
            }
        }

        // handle redirect url
        if (contentNodeModel instanceof HasRedirectUrl) {
            String redirectUrl = ((HasRedirectUrl) contentNodeModel).getRedirectUrl();
            if (!nav.isPdp() && ContentNodeModelUtil.isRedirectUrlValid(redirectUrl)) {
                throw new InvalidFilteringArgumentException("Node has redirect URL: " + id, InvalidFilteringArgumentException.Type.NODE_HAS_REDIRECT_URL, redirectUrl);
            }
        }

        // special categories where leftnav is not needed
        if (!nav.isPdp() && contentNodeModel instanceof CategoryModel && ((CategoryModel) contentNodeModel).getFullWidthLayout() != null) {
            throw new InvalidFilteringArgumentException("Category has a full width layout: " + id + ". No left nav needed.", InvalidFilteringArgumentException.Type.TERMINATE);
        }

        // special redirect rule - if the department only has one category then redirect to that category
        if (contentNodeModel instanceof DepartmentModel) {
            String catId = isSpecialRedirectConditionsApply((DepartmentModel) contentNodeModel, user);
            if (catId != null) {
                throw new InvalidFilteringArgumentException("Node has only one category. Redirect to: " + catId, InvalidFilteringArgumentException.Type.NODE_HAS_REDIRECT_URL,
                        String.format(ONE_CATEGORY_REDIRECT_URL, catId));
            }
        }

    }

    /**
     * @param dept
     * @param user
     * @return
     *
     * if department has only one usable category then return with that categoryId
     */
    private String isSpecialRedirectConditionsApply(DepartmentModel dept, FDUserI user) {

        String theOnlyOne = null;

        if (dept.getCategories() != null) {

            int categoryCounter = 0;

            for (CategoryModel cat : dept.getCategories()) {

                if (NavigationUtil.isCategoryHiddenInContext(user, cat)) {
                    continue;
                }

                ++categoryCounter;

                if (categoryCounter > 1) {
                    return null;
                }

                theOnlyOne = cat.getContentKey().getId();
            }

            if (categoryCounter == 1) {
                return theOnlyOne;
            }

        }

        return null;
    }

    private void buildHlCategoriesPageBeacon(Map<String, String> hlSelectionsofPageBeacon, BrowseDataContext browseDataContext, FDUserI user,
            List<FilteringSortingItem<ProductModel>> productResults, String catId) {
        try {
            List<FilteringProductItem> items = ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(productResults);

            StringBuffer updatedPageBeacon = new StringBuffer("&aShown=");
            if (null != items) {
                for (FilteringProductItem product : items) {
                    if (product.getProductModel() != null && !product.getProductModel().isUnavailable()) {
                        ProductData productData = null;
                        try {
                            productData = ProductDetailPopulator.createProductData(user, product.getProductModel());
                        } catch (FDResourceException e) {
                            LOG.warn("Exception while populating HookLogic returned product: ", e);
                        } catch (FDSkuNotFoundException e) {
                            LOG.warn("Exception while populating HookLogic returned product: ", e);
                        } catch (HttpErrorResponse e) {
                            LOG.warn("Exception while populating HookLogic returned product: ", e);
                        } catch (Exception e) {
                            LOG.warn("Exception while populating HookLogic returned product: ", e);
                        }
                        if (null != productData && null != productData.getSkuCode()) {
                            // updatedPageBeacon.append((("&aShown=".equals(updatedPageBeacon.toString()))?productData.getSkuCode():","+productData.getSkuCode()));
                            productData.setFeatured(true);
                            productData.setClickBeacon(((ProductModelBrandAdsAdapter) product.getProductModel()).getClickBeacon());
                            productData.setImageBeacon(((ProductModelBrandAdsAdapter) product.getProductModel()).getImpBeacon());

                        }
                    }
                }

            } else {
                updatedPageBeacon.append("none");
            }
            if (null != browseDataContext.getAdProducts().getPageBeacon()) {
                StringBuffer PageBeacon = new StringBuffer(browseDataContext.getAdProducts().getPageBeacon());
                PageBeacon.append(updatedPageBeacon).toString();
                hlSelectionsofPageBeacon.put(catId, PageBeacon.toString());
            }
        } catch (Exception e) {
            LOG.warn("Exception while populating HookLogic products: ", e);
        }
    }

    private List<ProductData> getProductDataList(FDUserI user, List<FilteringSortingItem<ProductModel>> hlItems, NavigationModel navigationModel) {

        List<FilteringProductItem> items = ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(hlItems);
        ProductData productData = null;
        List<ProductData> hlProductDataList = new ArrayList<ProductData>();
        items = ProductItemFilterUtil.getFilteredProducts(items, navigationModel.getActiveFilters(), true, true);
        for (FilteringProductItem product : items) {
            if (product.getProductModel() != null && !product.getProductModel().isUnavailable()) {
                try {
                    productData = ProductDetailPopulator.createProductData(user, product.getProductModel());
                    if (null != productData) {
                        productData.setClickBeacon(((ProductModelBrandAdsAdapter) product.getProductModel()).getClickBeacon());
                        productData.setImageBeacon(((ProductModelBrandAdsAdapter) product.getProductModel()).getImpBeacon());
                        hlProductDataList.add(productData);
                    }
                } catch (Exception e) {
                    LOG.warn("Exception while populating Hook Logic ProductData: ", e);
                }
            }

            if (hlProductDataList.size() >= FDStoreProperties.getHlProductsCount())
                break;
        }
        return hlProductDataList;
    }
    
    public MySaleItemsData getSaleItems(HttpServletRequest request,
			FDUserI user, CmsFilteringNavigator navigator, boolean isMobile) throws Exception {
    	double ratingBaseLine = 4;
		double popularityBaseLine = 10000;
		double dealsBaseLine = 20;
		boolean considerNew = false;
		boolean considerBackInStock = false;
		boolean sortProducts = true;
		int maxNoOfProducts = 50;
    	try {
	    	if(isMobile){
	    		return getSaleItemsMobile(request, user, navigator, ratingBaseLine, dealsBaseLine, popularityBaseLine,
							considerNew, considerBackInStock, sortProducts, maxNoOfProducts); 
	    	} else {
	    		return getSaleItemsWeb(request, user, navigator, ratingBaseLine, dealsBaseLine, popularityBaseLine,
							considerNew, considerBackInStock, sortProducts, maxNoOfProducts);
	    	}
    	} catch(Exception e){
    		throw e;
    	}
    }
    
    public MySaleItemsData getSaleItemsMobile(HttpServletRequest request,
			FDUserI user, CmsFilteringNavigator navigator, double ratingBaseLine, double dealsBaseLine, double popularityBaseLine
			, boolean considerNew, boolean considerBackInStock, boolean sortProducts, int maxNoOfProducts)
			throws InvalidFilteringArgumentException, FDResourceException {
		
		ratingBaseLine = navigator.getRatingBaseLine();
		dealsBaseLine = navigator.getDealsBaseLine();
		popularityBaseLine = navigator.getPopularityBaseLine();
		considerNew = navigator.isConsiderNew();
		considerBackInStock = navigator.isConsiderBackInStock();
		sortProducts = navigator.isSortProducts();
		maxNoOfProducts = navigator.getMaxNoOfProducts();
		
		BrowseData browseData = getWeLoveYouLoveData(user, navigator, ratingBaseLine, dealsBaseLine, popularityBaseLine,
													 	considerNew, considerBackInStock, sortProducts, maxNoOfProducts) ;
		MySaleItemsData mySaleItemsData = new MySaleItemsData();
		mySaleItemsData.setBrowsedata(browseData);
		mySaleItemsData.setRatingBaseLine(ratingBaseLine);
		mySaleItemsData.setDealsBaseLine(dealsBaseLine);
		mySaleItemsData.setConsiderBackInStock(considerBackInStock);
		return mySaleItemsData;
	}
    
    public MySaleItemsData getSaleItemsWeb(HttpServletRequest request,
			FDUserI user, CmsFilteringNavigator navigator, double ratingBaseLine, double dealsBaseLine, double popularityBaseLine
			, boolean considerNew, boolean considerBackInStock, boolean sortProducts, int maxNoOfProducts)
			throws InvalidFilteringArgumentException, FDResourceException {
	
		if(request.getParameter("rbl") != null) {
			ratingBaseLine = Double.parseDouble(request.getParameter("rbl"));
		}
		if(request.getParameter("pbl") != null) {
			popularityBaseLine = Double.parseDouble(request.getParameter("pbl"));
		}
		if(request.getParameter("dbl") != null) {
			dealsBaseLine = Double.parseDouble(request.getParameter("dbl"));
		}
		if(request.getParameter("cn") != null) {
			considerNew = Boolean.parseBoolean(request.getParameter("cn"));
		}
		if(request.getParameter("cbis") != null) {
			considerBackInStock = Boolean.parseBoolean(request.getParameter("cbis"));
		}
		if(request.getParameter("sp") != null) {			
			sortProducts = Boolean.parseBoolean(request.getParameter("sp"));
		}
		if(request.getParameter("mnp") != null) {			
			maxNoOfProducts = Integer.parseInt(request.getParameter("mnp"));
		}
			
		BrowseData browseData = getWeLoveYouLoveData(user, navigator, ratingBaseLine, dealsBaseLine, popularityBaseLine,
														considerNew, considerBackInStock, sortProducts, maxNoOfProducts) ;
		MySaleItemsData mySaleItemsData = new MySaleItemsData();
		mySaleItemsData.setBrowsedata(browseData);
		mySaleItemsData.setRatingBaseLine(ratingBaseLine);
		mySaleItemsData.setDealsBaseLine(dealsBaseLine);
		mySaleItemsData.setConsiderBackInStock(considerBackInStock);
		return mySaleItemsData;
	}

  //Method added as part of FDLabs of Aggregated Customer and Global Favorites page, will be soon integrated with browse.jsp
	public BrowseData getWeLoveYouLoveData(FDUserI user, CmsFilteringNavigator nav, double ratingBaseLine
    		, double dealsBaseLine, double popularityBaseLine
    		, boolean considerNew , boolean considerBackInStock, boolean sortProducts, int maxNoOfProducts) throws FDResourceException {

		List<List<ProductModel>> interestingProductGroups = ProductRecommenderUtil.getYouLoveWeLoveProducts(user, ratingBaseLine, dealsBaseLine
																, popularityBaseLine, considerNew, considerBackInStock, sortProducts, maxNoOfProducts);

		BrowseData browseData = new BrowseData();

		List<SectionData> sections = new ArrayList<SectionData>();
		browseData.getSections().setSections(sections);

		int sectionCount = 0;
		SectionData sectionData = null;
		ProductData productData = null;

		List<ContentKey> uniqueProducts = new ArrayList<ContentKey>();

		if(interestingProductGroups != null) {
			for(List<ProductModel> interestingProducts :  interestingProductGroups) {
				if(interestingProducts !=null && interestingProducts.size() > 0) {

					List<ProductData> productDatas = new ArrayList<ProductData>();

					for(ProductModel product : interestingProducts) {
						if(!uniqueProducts.contains(product.getContentKey())) {
                            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
							productData = new ProductData(product);
							try {
								ProductDetailPopulator.populateBrowseProductData(productData, user, nav, false);
								productDatas.add(productData);
							} catch(Exception e) {
								//System.out.println("Failed to populate product data for " + product==null ? "null": product.getContentName() + " (" + e.getMessage() + ")");
							}
							uniqueProducts.add(product.getContentKey());
						}
					}
					if(productDatas.size() > 0) {
						sectionData = new SectionData();
						if(sectionCount == 0) {
							sectionData.setHeaderText("Your favorites with at least "+ (int)ratingBaseLine + "-star rating or " + (int)dealsBaseLine + "% discount " + (considerBackInStock ? "or is back in stock" : ""));
						} else {
							sectionData.setHeaderText("Our favorites with at least "+ (int)ratingBaseLine + "-star rating or " + (int)dealsBaseLine + "% discount " + (considerBackInStock ? "or is back in stock" : ""));
						}
						sections.add(sectionData);
						sectionData.setProducts(productDatas);
					}
				}
				sectionCount++;
			}
		}
		return browseData;
	}


}
