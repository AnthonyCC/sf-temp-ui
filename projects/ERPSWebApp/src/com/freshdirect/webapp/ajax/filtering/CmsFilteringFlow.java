package com.freshdirect.webapp.ajax.filtering;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.WineUtil;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManager;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.AbstractProductItemFilter;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductModelBrandAdsAdapter;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeDepartment;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.content.browse.filter.BrandFilter;
import com.freshdirect.fdstore.content.browse.filter.ContentNodeFilter;
import com.freshdirect.fdstore.content.util.SmartSearchUtils;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.SearchPageType;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.CarouselDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContextService;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.PagerData;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;
import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.browse.paging.BrowseDataPagerHelper;
import com.freshdirect.webapp.ajax.browse.service.ProductService;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringServlet.BrowseEvent;
import com.freshdirect.webapp.ajax.holidaymealbundle.service.HolidayMealBundleService;
import com.freshdirect.webapp.ajax.mealkit.service.MealkitService;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;

public class CmsFilteringFlow {
	
    private static final Logger LOG = LoggerFactory.getInstance( CmsFilteringFlow.class );
	
	private static String FALLBACK_URL = "/";
	private static String RECIPE_DEPARTMENT_URL_FS = "/recipe_dept.jsp?deptId=%s";
	private static String SPECIAL_LAYOUT_URL_FS = "/browse_special.jsp?id=%s";
    private static String HOLIDAY_MEAL_BUNDLE_LAYOUT_URL_FS = "/hmb/category.jsp?id=%s";
    private static String RECIPE_MEALKIT_LAYOUT_URL_FS = "/handpick/category.jsp?id=%s";
	private static String ONE_CATEGORY_REDIRECT_URL = "/browse.jsp?id=%s";
	private static String SUPER_DEPARTMENT_WITHOUT_GLOBALNAV_URL = "/index.jsp";
	
	@SuppressWarnings("deprecation")
		BrowseData browseData = null;
	public CmsFilteringFlowResult doFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException, FDResourceException {
		BrowseDataContext browseDataContext = getBrowseDataContextFromCacheForPaging(nav, user);
		if (nav.getPageType() == FilteringFlowType.BROWSE) {
			
			if (browseDataContext == null) {
				browseDataContext = doBrowseFlow(nav, user);
			}
			
			if(!nav.isPdp()){
				EhCacheUtil.putObjectToCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey(), browseDataContext);				
			}
			
			// -- SORTING -- (not on pdp or super department page)
			if(!nav.isPdp() && !(browseDataContext.getCurrentContainer() instanceof SuperDepartmentModel)){
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
			
			if(!nav.isPdp()){

				/* loop through sectionContexts, and insert from HL list */
				List <SectionData> tempList = browseData.getSections().getSections().get(0).getSections();
				if (tempList == null || tempList.size() == 0) { /* single cat */
					SectionData section = browseData.getSections().getSections().get(0);
			        insertHookLogicProductsIntoBrowseData(
			        		section.getProducts(), 
		            		browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId()), 
		            		user,
		            		FDStoreProperties.getHookLogicAllowOwnRows()
		            );
				} else { /* multiple sub cats */
					//skip out if not on show all or first page
					if (nav.getActivePage() <= 1) {
						int runningTotal = 0;
				        Iterator<SectionData> iterator = browseData.getSections().getSections().get(0).getSections().iterator();
				        while (iterator.hasNext()) {
							SectionData section = iterator.next();
							
							int curSectionSize = section.getProducts().size();
							
							int itemsPerRow = (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn5_0, user)) ? 5 :
					        	(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn4_0, user)) ? 4 : 5;
							
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
					            		browseData.getAdProducts().getHlSelectionOfProductList().get(section.getCatId()), 
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
				
				// -- PAGING --
				BrowseDataPagerHelper.createPagerContext(browseData, nav);
				Set<ContentKey> shownProductKeysForRecommender = new HashSet<ContentKey>();
				BrowseDataBuilderFactory.getInstance().collectAllProductKeysForRecommender(browseData.getSections().getSections(), shownProductKeysForRecommender);
				// -- SET NO PRODUCTS MESSAGE --
				if(shownProductKeysForRecommender.size()==0 && browseDataContext.getNavigationModel().isProductListing()){
					browseData.getSections().setAllSectionsEmpty(true);
				}

				//only display recommenders if on last page
				PagerData pagerData = browseData.getPager();
				if (pagerData.isAll() || isCaroulelsTopAndOnFirstPage(browseData, pagerData) || isCarouselsBottomAndOnLastPage(browseData, pagerData)) {
					boolean disableCategoryYmalRecommender = browseDataContext.getCurrentContainer() instanceof ProductContainer && ((ProductContainer) browseDataContext.getCurrentContainer()).isDisableCategoryYmalRecommender();
					BrowseDataBuilderFactory.getInstance().appendCarousels(browseData, browseDataContext, user, shownProductKeysForRecommender, disableCategoryYmalRecommender, nav.isPdp());
				} else { //remove if already added
					CarouselDataCointainer carousels = browseData.getCarousels();
					carousels.setCarousel1(null);
					carousels.setCarousel2(null);
				}
				
				browseData.getDescriptiveContent().setUrl(URLEncoder.encode(nav.assembleQueryString()));
				browseData.getDescriptiveContent().setNavDepth(browseDataContext.getNavigationModel().getNavDepth().name());
				browseData.getDescriptiveContent().setContentId(nav.getId());
				browseData.getSortOptions().setCurrentOrderAsc(nav.isOrderAscending());		
				
				// -- CALCULATE SECTION MAX LEVEL --
				BrowseDataBuilderFactory.getInstance().calculateMaxSectionLevel(browseData.getSections(), browseData.getSections().getSections(), 0);
			}
		} else if (nav.getPageType().isSearchLike()) {
			
			if (browseDataContext == null) {
				browseDataContext = doSearchLikeFlow(nav, user);
			}
			EhCacheUtil.putObjectToCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey(), browseDataContext);				
			
			BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav, user);			
				
            browseData = browseDataContext.extractBrowseDataPrototype(user, nav);
            
            /* insert HL products into the correct spot(s) in the results */
            insertHookLogicProductsIntoBrowseData(
            		browseData.getSections().getSections().get(0).getProducts(), 
            		browseData.getAdProducts().getProducts(), 
            		user,
            		FDStoreProperties.getHookLogicAllowOwnRows()
            );
				
			// -- PAGING --
			if (!FilteringFlowType.PRES_PICKS.equals(nav.getPageType()) || (FilteringFlowType.PRES_PICKS.equals(nav.getPageType()) && FDStoreProperties.isPresidentPicksPagingEnabled())) {
				BrowseDataPagerHelper.createPagerContext(browseData, nav);
			}
	
			//Update product hit counts
//			for (BrowseData.SearchParams.Tab tab : browseData.getSearchParams().getTabs()) {
//				if (SearchPageType.PRODUCT.name.equalsIgnoreCase(nav.getActiveTab())) {
//					int hits = browseData.getPager().getItemCount();
//					tab.setHits(hits);
//					tab.setFilteredHits(filteredHits);
//				}
//			}
			
			BrowseDataBuilderFactory.getInstance().appendSearchLikeCarousel(browseData, user, nav.getPageType(), nav.getActiveTab());
			
			browseData.getDescriptiveContent().setUrl(URLEncoder.encode(nav.assembleQueryString()));
			browseData.getSortOptions().setCurrentOrderAsc(nav.isOrderAscending());
			
			populateSearchCarouselProductLimit(nav.getActivePage(), browseDataContext);
		}
		return new CmsFilteringFlowResult(browseData, browseDataContext.getNavigationModel());
	}
	
	/* call this BEFORE BrowseDataPagerHelper.createPagerContext(browseData, nav); */
	/* allowOnlyHLRows will toggle filling in empty rows to display the entire HL list */
	public void insertHookLogicProductsIntoBrowseData(List<ProductData> prodList, List<ProductData> hlProdList, FDSessionUser user, boolean allowOnlyHLRows) {
		/* skip out if hlProds passed in are invalid or empty */
		if (hlProdList == null || hlProdList.size() == 0) { return; }
		
        int itemsPerRow = (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn5_0, user)) ? 5 :
        	(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.gridlayoutcolumn4_0, user)) ? 4 : 5;
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
        
        /* leave the items, so they can be inserted on ajax filtering */
        //browseData.getAdProducts().setProducts(new ArrayList<ProductData>()); //remove list so it's not sent to page
	}

	private boolean isCarouselsBottomAndOnLastPage(BrowseData browseData, PagerData pagerData) {
		return "BOTTOM".equals(browseData.getCarousels().getCarouselPosition()) && pagerData.getActivePage() == pagerData.getPageCount();
	}

	private boolean isCaroulelsTopAndOnFirstPage(BrowseData browseData, PagerData pagerData) {
		return "TOP".equals(browseData.getCarousels().getCarouselPosition()) && pagerData.getActivePage() == 1;
	}

	/**
	 * Set page type value in {@code browseDataContext} for paging and sorting cache.
	 * This value is also available in {@code browseDataContext.searchParams.pageType} but {@code browseDataContext.searchParams} is empty when we are on browse.
	 * @param nav
	 * @param browseDataContext
	 */
	private void savePageTypeForCaching(FilteringFlowType pageType, BrowseDataContext browseDataContext) {
		if (browseDataContext.getPageType() == null) {
			browseDataContext.setPageType(pageType);
		}
	}

	private BrowseDataContext getBrowseDataContextFromCacheForPaging(CmsFilteringNavigator nav, FDSessionUser user) {
		BrowseDataContext browseDataContext = null;
		BrowseEvent event = nav.getBrowseEvent()!=null ? BrowseEvent.valueOf(nav.getBrowseEvent().toUpperCase()) : BrowseEvent.NOEVENT;
		//use userRefinementCache
		String userPrimaryKey = user.getUser().getPrimaryKey();
		if(event != BrowseEvent.PAGE) { // invalidate cache entry in every other case than paging
			browseDataContext = removeBrowseDataContextFromCache(userPrimaryKey);
		} else {
			browseDataContext = EhCacheUtil.getObjectFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey);
			if (!isRequestForTheSamePageType(nav, browseDataContext)) {
				browseDataContext = removeBrowseDataContextFromCache(userPrimaryKey); //if cached has other page type
			} else {
				if (FilteringFlowType.BROWSE.equals(nav.getPageType()) && !isBrowseRequestForTheSameId(nav, browseDataContext)) {
					browseDataContext = removeBrowseDataContextFromCache(userPrimaryKey); //if cached has same page type(browse) but other id
				} else if (FilteringFlowType.SEARCH.equals(nav.getPageType()) && !isRequestForTheSameSearchParams(nav, browseDataContext)) {
					browseDataContext = removeBrowseDataContextFromCache(userPrimaryKey); //if cached has same page type(search) but other search params
				}
			}
		}
		return browseDataContext;
	}

	private boolean isRequestForTheSameSearchParams(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
		return browseDataContext != null && navigator != null && browseDataContext.getSearchParams() != null && browseDataContext.getSearchParams().getSearchParams() != null && browseDataContext.getSearchParams().getSearchParams().equals(navigator.getSearchParams());
	}

	private boolean isRequestForTheSamePageType(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
		return browseDataContext != null && navigator != null && browseDataContext.getPageType() != null && browseDataContext.getPageType().equals(navigator.getPageType());
	}

	private boolean isBrowseRequestForTheSameId(CmsFilteringNavigator navigator, BrowseDataContext browseDataContext) {
		return browseDataContext != null && browseDataContext.getCurrentContainer() != null && browseDataContext.getCurrentContainer().getContentName() != null && browseDataContext.getCurrentContainer().getContentName().equalsIgnoreCase(navigator.getId());
	}

	private BrowseDataContext removeBrowseDataContextFromCache(String userPrimaryKey) {
		EhCacheUtil.removeFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey);
		return null;
	}

	private void populateSearchCarouselProductLimit(int activePage,	BrowseDataContext browseDataContext) {
		int searchCarouselProductLimit;
		if (activePage == 0) {
			searchCarouselProductLimit = 0;
		} else {
			searchCarouselProductLimit = FDStoreProperties.getSearchCarouselProductLimit();
		}
		browseDataContext.getSections().setLimit(searchCarouselProductLimit);
	} 


	public BrowseDataContext doSearchLikeFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		NavigationModel navigationModel = new NavigationModel();
		SearchResults searchResults = null;
		
		switch (nav.getPageType()) {
			case SEARCH:
				String searchParams = nav.getSearchParams() == null ? "" : nav.getSearchParams();
				
				if (user.getMasqueradeContext()!=null){
					List<String> listSearchParamsList = getSearchList(searchParams); //see if search term contains multiple terms
					if (listSearchParamsList.size()>1){
						nav.setListSearchParams(searchParams);
						searchParams = listSearchParamsList.get(0);
						nav.setSearchParams(searchParams);
					}
				}
				searchResults = ContentSearch.getInstance().searchProducts(searchParams);
				if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user)){				//if(FDStoreProperties.isHookLogicEnabled()){
				   SearchResultsUtil.getHLBrandProductAdProducts(searchResults, nav,  user);	
				}
				collectSearchRelevancyScores(searchResults);
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
				LOG.error("Invalid page type: "+ nav.getPageType());
				throw new InvalidFilteringArgumentException("Invalid page type: "+ nav.getPageType(), InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, FALLBACK_URL);
		}
		
		SearchPageType searchPageType = SearchPageType.PRODUCT; //default behavior
		if (SearchPageType.RECIPE.name.equalsIgnoreCase(nav.getActiveTab())) {
			processRecipes(navigationModel, searchResults);
			searchPageType = SearchPageType.RECIPE;
		} else {
			processProducts(nav.getPageType(), navigationModel, searchResults);
			searchPageType = SearchPageType.PRODUCT; //TODO why is this set again?
		}

		setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);

		BrowseDataContext browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), searchPageType).buildBrowseData(navigationModel, user, nav);
		
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
		
		//refresh context sensitive filters
		if (navigationModel.getActiveFilters().size() > 0 && searchPageType == SearchPageType.PRODUCT && browseDataContext.getSectionContexts().size() > 0) {
			refreshResultDependantFilters(nav.getPageType(), navigationModel, browseDataContext.getSectionContexts().get(0).getProductItems());
			setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
		}
		savePageTypeForCaching(nav.getPageType(), browseDataContext);
		browseDataContext.setNavigationModel(navigationModel);
		MenuBuilderI menuBuilder = MenuBuilderFactory.createBuilderByPageType(null, navigationModel.isSuperDepartment(), searchPageType);
		// create menu
		navigationModel.setLeftNav(menuBuilder.buildMenu(navigationModel.getAllFilters(), navigationModel, nav));
		if (!navigationModel.isSuperDepartment()) { //don't do these in case of super department page
			// menu availability check
			MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user);
			reOrderAllMenuItemsByHitCount(navigationModel, browseDataContext);
			reOrderAllMenuItemsByName(navigationModel);
		}
		browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
		BrowseData.DescriptiveDataCointainer descriptiveContent = browseDataContext.getDescriptiveContent();
		switch (nav.getPageType()) { //TODO warning
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
				descriptiveContent.setPageTitle("FreshDirect - President's Picks");
				ContentNodeModel node=ContentFactory.getInstance().getContentNode(nav.getId());
				if(node!=null) 
					descriptiveContent.setOasSitePage(node.getPath());
				Html presidentPicksPageTopMediaBanner = ContentFactory.getInstance().getStore().getPresidentPicksPageTopMediaBanner();
				if (presidentPicksPageTopMediaBanner != null) {
					descriptiveContent.setMedia(MediaUtils.renderHtmlToString(presidentPicksPageTopMediaBanner, user));
					descriptiveContent.setMediaLocation("TOP");
				}
				break;
			case STAFF_PICKS:
				descriptiveContent.setPageTitle("FreshDirect - Staff Picks");
				ContentNodeModel staffPicksNode=ContentFactory.getInstance().getContentNode(nav.getId());
				if(staffPicksNode!=null) {
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
				LOG.error("Invalid page type: "+ nav.getPageType());
				throw new InvalidFilteringArgumentException("Invalid page type: "+ nav.getPageType(), InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, FALLBACK_URL);
		}
		browseDataContext.getSearchParams().setSearchParams(nav.getSearchParams());
		browseDataContext.getSearchParams().setListSearchParams(nav.getListSearchParams());
		browseDataContext.getSearchParams().setListSearchParamsList(getSearchList(nav.getListSearchParams()));
		
		browseDataContext.getSearchParams().setSuggestions((List<String>)searchResults.getSpellingSuggestions());
		browseDataContext.getSearchParams().setSearchTerm(searchResults.getSuggestedTerm());
		buildTabs(browseDataContext, searchResults, nav);
		browseDataContext.getSearchParams().setPageType(nav.getPageType());
		if (FilteringFlowType.NEWPRODUCTS.equals(nav.getPageType())) {
			browseDataContext.getSearchParams().setNumberOfNewProducts(searchResults.getProducts().size());
		}

		//set ddpp products for 'search like' pages
		/* if we need to populate the normal products into the assortProducts browseData
		 * uncomment this section */
		/* UNCOMMENT START */
//		if (FilteringFlowType.STAFF_PICKS.equals(nav.getPageType())) {
//			for (FilteringSortingItem<ProductModel> item : searchResults.getProducts()) {
//				ProductModel prodModel = item.getModel();
//				try {
//					ProductData productData = ProductDetailPopulator.createProductData(user, prodModel);
//					productData.setFeatured(((ProductModelPromotionAdapter)prodModel).isFeatured());
//					productData.setFeaturedHeader(((ProductModelPromotionAdapter)prodModel).getFeaturedHeader());
//					
//					String curCat = ((ProductModelPromotionAdapter) prodModel).getErpCategory();
//					if (curCat == null || curCat.trim() == "") {
//						curCat = "<!-- ERROR -->"; //lump all the bad products together. remember, cat gets displayed by front end
//					}
//					productData.setErpCategory(curCat);
//					productData.setErpCatPosition(((ProductModelPromotionAdapter)prodModel).getErpCatPosition());
//					productData.setPriority(((ProductModelPromotionAdapter)prodModel).getPriority());
//					
//					
//					browseDataContext.getAssortProducts().addProdDataToCat(curCat, productData);
//					//.getProducts(curCat).add(productData);
//					
//				} catch (FDResourceException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (FDSkuNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (HttpErrorResponse e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				
//			}
//		}
		/* UNCOMMENT END */
		for (ProductModel product : searchResults.getDDPPProducts()) {
			try {
				ProductData productData = ProductDetailPopulator.createProductData(user, product);
				productData.setFeatured(((ProductModelPromotionAdapter)product).isFeatured());
				productData.setFeaturedHeader(((ProductModelPromotionAdapter)product).getFeaturedHeader());
				
				String curCat = ((ProductModelPromotionAdapter) product).getErpCategory();
				if (curCat == null || curCat.trim() == "") {
					curCat = "ERROR"; //lump all the bad products together
				}
				productData.setErpCategory(curCat);
				productData.setErpCatPosition(((ProductModelPromotionAdapter)product).getErpCatPosition());
				productData.setPriority(((ProductModelPromotionAdapter)product).getPriority());
				
				if (nav.getPageType()!=null){
					productData.setPageType(nav.getPageType().toString());
				}
				
				browseDataContext.getDDPPProducts().getProducts().add(productData);
			} catch (FDResourceException e) {
				LOG.warn("Getting DDPP products failed!", e);
			} catch (FDSkuNotFoundException e) {
				LOG.warn("Getting DDPP products failed!", e);
			} catch (HttpErrorResponse e) {
				LOG.warn("Getting DDPP products failed!", e);
			}
		}

		//set HookLogic adProducts for 'search like' pages.
		if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user)){	//if(FDStoreProperties.isHookLogicEnabled()){
			  StringBuffer updatedPageBeacon = new StringBuffer("&aShown=");
			try{
			  if(null !=searchResults.getAdProducts() && !searchResults.getAdProducts().isEmpty()){
				  List<FilteringProductItem> items = ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(searchResults.getAdProducts());
				  items = ProductItemFilterUtil.getFilteredProducts(items, navigationModel.getActiveFilters(), true, true);
				  if(null !=items){
						for (FilteringProductItem product : items) {
							
								if(product.getProductModel()!=null && !product.getProductModel().isUnavailable()) {
									ProductData productData = null;
									try {
										productData = ProductDetailPopulator.createProductData(user, product.getProductModel());
									} catch (FDResourceException e) {
										LOG.warn("Exception while populating HookLogic returned product: ", e);
									} catch (FDSkuNotFoundException e) {
										LOG.warn("Exception while populating HookLogic returned product: ", e);
									} catch (HttpErrorResponse e) {
										LOG.warn("Exception while populating HookLogic returned product: ", e);
									}
									catch (Exception e) {
										LOG.warn("Exception while populating HookLogic returned product: ", e);
									}
									if(null != productData && null != productData.getSkuCode()){
										//updatedPageBeacon.append((("&aShown=".equals(updatedPageBeacon.toString()))?productData.getSkuCode():","+productData.getSkuCode()));
										productData.setFeatured(true);
										//productData.setFeaturedHeader(((ProductModelPromotionAdapter)product).getFeaturedHeader());
										productData.setClickBeacon(((ProductModelBrandAdsAdapter)product.getProductModel()).getClickBeacon());
										productData.setImageBeacon(((ProductModelBrandAdsAdapter)product.getProductModel()).getImpBeacon());
										if (nav.getPageType()!=null){
											productData.setPageType(nav.getPageType().toString());
											
										}
										browseDataContext.getAdProducts().getProducts().add(productData);
										if(browseDataContext.getAdProducts().getProducts().size() >=FDStoreProperties.getHlProductsCount()){//DISPLAY ONLY 5 HOOKLOGIC PRODUCTS
											break;
										} 
									}
								}
							
						}
				  }
			
			}else{
				updatedPageBeacon.append("none");
			  }
			if(null!=searchResults.getPageBeacon()){
				StringBuffer PageBeacon=new StringBuffer(searchResults.getPageBeacon());
				browseDataContext.getAdProducts().setPageBeacon(PageBeacon.append(updatedPageBeacon).toString());
				}
		}catch (Exception e) {
			LOG.warn("Exception while populating HookLogic products: ", e);
		}
 }
		
		// -- RELOCATE BRAND FILTER BASED ON CMS SETTING
		if(browseDataContext.getNavigationModel().getBrandFilterLocation()!=null){
			MenuBuilderFactory.getInstance().relocateBrandFilter(browseDataContext.getMenuBoxes().getMenuBoxes(),  browseDataContext.getNavigationModel().getBrandFilterLocation());			
		}
		
		// populate browseData with filterLabels
		BrowseDataBuilderFactory.getInstance().populateWithFilterLabels(browseDataContext, navigationModel);
		
		return browseDataContext;
	}

	/** based on com.freshdirect.webapp.taglib.fdstore.FDParseSearchTermsTag.getSearchList(String) **/
    private List<String> getSearchList(String params) {
		List<String> list = new ArrayList<String>();
		if (params != null && !"".equals(params.trim())) {
			StringTokenizer tokenizer = new StringTokenizer(params, ",\r\n\f\"\'");
			while (tokenizer.hasMoreTokens()) {
				String currentToken = tokenizer.nextToken().trim();
				if ( !"".equals(currentToken) ) {
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

	private void setupAllAndActiveFiltersForNavigationModel(CmsFilteringNavigator nav, FDSessionUser user, NavigationModel navigationModel) {
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
					browseDataContext.getSearchParams().buildTabs(SearchPageType.PRODUCT.label, SearchPageType.PRODUCT.name, results.getProducts().size(), results.getProducts().size(), false);
				}
				browseDataContext.getSearchParams().buildTabs(SearchPageType.RECIPE.label, SearchPageType.RECIPE.name, results.getRecipes().size(), cmsFilteringNavigator.getRecipeHits(), true);
			} else {
				browseDataContext.getSearchParams().buildTabs(SearchPageType.PRODUCT.label, SearchPageType.PRODUCT.name, results.getProducts().size(), Math.min(results.getProducts().size(), cmsFilteringNavigator.getProductHits()), true); //Math.min ~ workaround for product losses because of non existent default sku
				if (!results.getRecipes().isEmpty()) {
					browseDataContext.getSearchParams().buildTabs(SearchPageType.RECIPE.label, SearchPageType.RECIPE.name, results.getRecipes().size(), results.getRecipes().size(), false);
				}
			}
			break;
		default:
			break;
		}
	}

	/**Collecting search results, filtering info and sort options for search results*/
	private void processActiveFilters(NavigationModel navigationModel) {
		BrandFilter selectedBrandFilter = null;
		AbstractProductItemFilter selectedShowMeOnlyFilter = null;
		
		for (ProductItemFilterI productFilter : navigationModel.getActiveFilters()) {
			if (productFilter instanceof BrandFilter) {
				selectedBrandFilter = (BrandFilter)productFilter;
			}
			if (!(productFilter instanceof BrandFilter) && !(productFilter instanceof ContentNodeFilter)) { //aka showMeOnlyFilter
				selectedShowMeOnlyFilter = (AbstractProductItemFilter)productFilter; //TODO what more than one show me only filter is selected?
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
	
	/**based on ProductsFilterImpl.createComparator() and FilteringComparatorUtil.createProductComparator()*/
	private void collectSearchRelevancyScores(SearchResults searchResults){
		String suggestedTerm = NVL.apply(searchResults.getSuggestedTerm(), searchResults.getSearchTerm());
		List<FilteringSortingItem<ProductModel>> products = searchResults.getProducts();
		
		// if there's only one DYM then we display products for that DYM
		// but for those products we have to use the suggested term to produce the following scores
		SmartSearchUtils.collectOriginalTermInfo(products, suggestedTerm);
		SmartSearchUtils.collectRelevancyCategoryScores(products, suggestedTerm);
		SmartSearchUtils.collectTermScores(products, suggestedTerm);
	}
	
	private void processProducts(FilteringFlowType pageType, NavigationModel navigationModel, SearchResults searchResults) {
		navigationModel.setProductListing(true);
				
		Iterator<FilteringSortingItem<ProductModel>> iterator = searchResults.getProducts().iterator();
		while (iterator.hasNext()) {
			FilteringSortingItem<ProductModel> result = iterator.next();
			ProductModel product = result.getModel();
			boolean showMeOnlyNewDisabledOnNewProductsPage = FilteringFlowType.NEWPRODUCTS.equals(pageType);
			try {
				FilterCollector.defaultFilterCollector(showMeOnlyNewDisabledOnNewProductsPage).collectBrandAndShowMeOnlyFilters(navigationModel, product);  //TODO is this necessary when refreshResultDependantFilters() will run as well?
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
	
    public BrowseDataContext doBrowseFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException, FDResourceException {
		
		BrowseDataContext browseDataContext = null;
		
		String id = nav.getId();
		ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);
		
		// validation
		validateNode(nav, contentNodeModel, id, user);
		
		//override show all
		if (contentNodeModel instanceof SuperDepartmentModel || ((ProductContainer) contentNodeModel).isTopLevelCategory() && ((ProductContainer) contentNodeModel).isShowAllByDefault()){
			nav.setAll(true);
		}
		
		// create filters and building menu
		NavigationModel navigationModel = NavigationUtil.createNavigationModel(contentNodeModel, nav, user);
		
		// filtering and grouping
		browseDataContext = BrowseDataBuilderFactory.createBuilder(navigationModel.getNavDepth(), navigationModel.isSuperDepartment(), null).buildBrowseData(navigationModel, user, nav);
		
	
		
		if(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.hooklogic2016, user) && FDStoreProperties.isHookLogicForCategoriesEnabled()){		 //if(FDStoreProperties.isHookLogicEnabled()){	
			if(null != browseDataContext){
				String catId = nav.getId();
				Map<String, List<ProductData>> hlSelectionsofProductsList=new HashMap<String, List<ProductData>>();
				Map<String, String> hlSelectionsofPageBeacons=new HashMap<String, String>();
				List<SectionContext> sectionContexts = browseDataContext.getSectionContexts();
				getAdProductsByCategory(user, navigationModel, catId, hlSelectionsofProductsList, hlSelectionsofPageBeacons, sectionContexts, browseDataContext);
				browseDataContext.getAdProducts().setHlSelectionOfProductList(hlSelectionsofProductsList);
				browseDataContext.getAdProducts().setHlSelectionsPageBeacons(hlSelectionsofPageBeacons);
			}			
		}
    
		// inject references
		browseDataContext.setNavigationModel(navigationModel);
		browseDataContext.setCurrentContainer(contentNodeModel);

        applyCategoryAggregationForGroundAndBurgers(nav, user, browseDataContext, contentNodeModel);

        savePageTypeForCaching(nav.getPageType(), browseDataContext);

		if (!navigationModel.isSuperDepartment()) { //don't do these in case of super department page
			// menu availability check
			MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user);
		}
		
		// populate browseData with the menu
		browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
		final ContentNodeModel departmentorSuperDepartment = getDepartmentOrSuperDepartment(contentNodeModel, navigationModel);
		browseDataContext.getMenuBoxes().setMenuName(departmentorSuperDepartment.getFullName());
		browseDataContext.getMenuBoxes().setMenuId(departmentorSuperDepartment.getContentKey().getId());

		// -- POPULATE EXTRA DATA --
		
		// populate browseData with breadcrumbs
		BrowseDataBuilderFactory.getInstance().populateWithBreadCrumbAndDesciptiveContent(browseDataContext, navigationModel);
		
		// -- RELOCATE BRAND FILTER BASED ON CMS SETTING
		if(browseDataContext.getNavigationModel().getBrandFilterLocation()!=null){
			MenuBuilderFactory.getInstance().relocateBrandFilter(browseDataContext.getMenuBoxes().getMenuBoxes(),  browseDataContext.getNavigationModel().getBrandFilterLocation());			
		}

		// populate browseData with filterLabels
		BrowseDataBuilderFactory.getInstance().populateWithFilterLabels(browseDataContext, navigationModel);
		
		boolean isWineDepartment = checkWineDepartment(navigationModel);
		browseDataContext.getDescriptiveContent().setWineDepartment(isWineDepartment);
		
        browseDataContext.setTopMedia(HolidayMealBundleService.defaultService().populateHolidayMealCategoryMedia(navigationModel));
        browseDataContext.setTopMedia(MealkitService.defaultService().populateMealkitCategoryMedia(navigationModel));

		return browseDataContext;
	}

	private void getAdProductsByCategory(FDSessionUser user, NavigationModel navigationModel, String catId,	Map<String, List<ProductData>> hlSelectionsofProductsList, 
										Map<String, String> hlSelectionsofPageBeacons, List<SectionContext> sectionContexts, BrowseDataContext browseDataContext) throws FDResourceException {
		if(null !=sectionContexts){
			for (Iterator<SectionContext> iterator = sectionContexts.iterator(); iterator.hasNext();) {
				 SectionContext categorySectionsContext  = (SectionContext) iterator.next();
				getAdProductsByCategory(user, navigationModel, categorySectionsContext.getCatId(), hlSelectionsofProductsList, hlSelectionsofPageBeacons,categorySectionsContext.getSectionContexts(), browseDataContext);
			 }
			getAdProductsByCategory(user, navigationModel, catId, hlSelectionsofProductsList, hlSelectionsofPageBeacons, browseDataContext);
		}
	}

	private void getAdProductsByCategory(FDSessionUser user, NavigationModel navigationModel, String catId,	Map<String, List<ProductData>> hlSelectionsofProductsList, 
											Map<String, String> hlSelectionsofPageBeacons, BrowseDataContext browseDataContext)throws FDResourceException {
		
		HLBrandProductAdRequest hLBrandProductAdRequest=new HLBrandProductAdRequest();	
		hLBrandProductAdRequest.setUserId(user.getUser().getPK().getId());
		hLBrandProductAdRequest.setCustomerId(user.getUser().getPK().getId());
		hLBrandProductAdRequest.setCategoryId(catId);
		try {
			HLBrandProductAdResponse hlBrandProductAdResponse = FDBrandProductsAdManager.getHLCategoryProducts(hLBrandProductAdRequest);
			List<HLBrandProductAdInfo> hlBrandAdProductsMeta =hlBrandProductAdResponse.getProductAd();
			List<ProductModel> adPrducts = new ArrayList<ProductModel>();
			
			if(hlBrandAdProductsMeta!=null){
				for (Iterator<HLBrandProductAdInfo> iterator = hlBrandAdProductsMeta.iterator(); iterator.hasNext();) {
					HLBrandProductAdInfo hlBrandProductAdMetaInfo = (HLBrandProductAdInfo) iterator.next();
					hlBrandProductAdMetaInfo.setPageBeacon(hlBrandProductAdResponse.getPageBeacon());
					browseDataContext.getAdProducts().setPageBeacon(hlBrandProductAdResponse.getPageBeacon());
				
					try {
						ProductModel productModel = ContentFactory.getInstance().getProduct(hlBrandProductAdMetaInfo.getProductSKU());
						if(null !=productModel){
							ProductModelBrandAdsAdapter pm = new ProductModelBrandAdsAdapter(productModel, hlBrandProductAdMetaInfo.getClickBeacon(), hlBrandProductAdMetaInfo.getImpBeacon());
								adPrducts.add(pm);
						}
					
					} catch (FDSkuNotFoundException e) {
						LOG.info("FDSkuNotFoundException while populating HookLogic product : ", e);
					}
			}
		  }
			List<FilteringSortingItem<ProductModel>> productResults = new ArrayList<FilteringSortingItem<ProductModel>>();
			if(null !=adPrducts){
				for (ProductModel productModel : adPrducts) {
					FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(productModel);
					productResults.add(item);
				}
			}
			List<ProductData> hlAvailableProductDataList=getProductDataList(user, productResults,navigationModel);			
			hlSelectionsofProductsList.put(catId, hlAvailableProductDataList);
			buildHlCategoriesPageBeacon(hlSelectionsofPageBeacons, browseDataContext, user, productResults, catId);
		} catch (BrandProductAdServiceException e) {
			// TODO Auto-generated catch block
			LOG.warn("Exception while populating HookLogic Product for Categorypages: ", e);
		}
		catch (Exception e) {
			LOG.warn("Exception while populating HookLogic Product for Categorypages: ", e);
		}
	}

    /**
     * Aggregate Ground & Burgers sub-category products into single section and remove sub-category titles
     * 
     * @param nav
     * @param user
     * @param browseDataContext
     * @param contentNodeModel
     */
    private void applyCategoryAggregationForGroundAndBurgers(CmsFilteringNavigator nav, FDSessionUser user, BrowseDataContext browseDataContext, ContentNodeModel contentNodeModel) {
        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.browseaggregatedcategories1_0, nav.getRequestCookies(), user)
                && FDStoreProperties.getBrowseAggregatedCategories().contains(contentNodeModel.getContentKey().getEncoded())) {
            SectionContext mainSectionContext = browseDataContext.getSectionContexts().get(0);
            SectionContext mergedSectionContext = new SectionContext();
            mergedSectionContext.setProductItems(new ArrayList<FilteringProductItem>());
            for (SectionContext subSectionContext : mainSectionContext.getSectionContexts()) {
                mergedSectionContext.getProductItems().addAll(subSectionContext.getProductItems());
            }
            List<SectionContext> subSectionContexts = mainSectionContext.getSectionContexts();
            subSectionContexts.clear();
            subSectionContexts.add(mergedSectionContext);
            ProductService.defaultService().removeSkuDuplicates(mergedSectionContext.getProductItems());
        }
    }

	private boolean checkWineDepartment(NavigationModel navigationModel) {
		boolean result = false;
		if (navigationModel != null && navigationModel.getNavigationHierarchy() != null) {
			ContentNodeModel department = navigationModel.getNavigationHierarchy().get(NavDepth.DEPARTMENT);
			result = department != null && department.getContentKey() != null && WineUtil.getWineAssociateId().equalsIgnoreCase(department.getContentKey().getId());
		}
		return result;
	}

	private ContentNodeModel getDepartmentOrSuperDepartment(ContentNodeModel contentNodeModel,
			NavigationModel navigationModel) {
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

	private void validateNode(CmsFilteringNavigator nav, ContentNodeModel contentNodeModel, String id, FDSessionUser user) throws InvalidFilteringArgumentException {

		if (!FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user) && contentNodeModel instanceof SuperDepartmentModel) {
			throw new InvalidFilteringArgumentException("Following SuperDepartment page is referred without globalNav rolled out: " +id, InvalidFilteringArgumentException.Type.SUPER_DEPARTMENT_WITHOUT_GLOBALNAV, SUPER_DEPARTMENT_WITHOUT_GLOBALNAV_URL);
		}
		
		if (contentNodeModel instanceof RecipeDepartment) {
			throw new InvalidFilteringArgumentException("Node is recipe department: "+id, InvalidFilteringArgumentException.Type.NODE_IS_RECIPE_DEPARTMENT, String.format(RECIPE_DEPARTMENT_URL_FS, id));
		}
				
		if (! (contentNodeModel instanceof ProductContainer || contentNodeModel instanceof SuperDepartmentModel)) { //applies for null as well
			throw new InvalidFilteringArgumentException("Node is not a product container or null: "+id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, String.format(FALLBACK_URL, id));
		}

		if (contentNodeModel instanceof CategoryModel && (NavigationUtil.isCategoryForbiddenInContext(user, (CategoryModel)contentNodeModel)) && !nav.isPdp()) {
			throw new InvalidFilteringArgumentException("Category is forbidden: "+id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, String.format(FALLBACK_URL, id));				
		}
		
		// throw exception if we have special layout and we are not on the browse_special.jsp or pdp.jsp
		if(!nav.isPdp() && !nav.isSpecialPage() && contentNodeModel instanceof CategoryModel && ((CategoryModel)contentNodeModel).getSpecialLayout()!=null){
            if (EnumLayoutType.HOLIDAY_MEAL_BUNDLE_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())) {
                throw new InvalidFilteringArgumentException("Node has holiday meal bundle layout: " + id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT,
                        String.format(HOLIDAY_MEAL_BUNDLE_LAYOUT_URL_FS, id));
            } else if (EnumLayoutType.RECIPE_MEALKIT_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())) {
                throw new InvalidFilteringArgumentException("Node has recipe mealkit layout: " + id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT,
                        String.format(RECIPE_MEALKIT_LAYOUT_URL_FS, id));
            } else {
                throw new InvalidFilteringArgumentException("Node has special layout: " + id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT,
                        String.format(SPECIAL_LAYOUT_URL_FS, id));
            }
        }
		
		// handle redirect url
		if (!(contentNodeModel instanceof SuperDepartmentModel)) {
			String redirectUrl = ((ProductContainer) contentNodeModel).getRedirectUrlClean();
			if(!nav.isPdp() && redirectUrl!=null){
				throw new InvalidFilteringArgumentException("Node has redirect URL: "+id, InvalidFilteringArgumentException.Type.NODE_HAS_REDIRECT_URL, redirectUrl);				
			}
		}
		
		// special categories where leftnav is not needed
		if(!nav.isPdp() && contentNodeModel instanceof CategoryModel && ((CategoryModel)contentNodeModel).getFullWidthLayout()!=null){
			throw new InvalidFilteringArgumentException("Category has a full width layout: "+id+". No left nav needed.", InvalidFilteringArgumentException.Type.TERMINATE);
		}
		
		//special redirect rule - if the department only has one category then redirect to that category
		if(contentNodeModel instanceof DepartmentModel){
			String catId = isSpecialRedirectConditionsApply((DepartmentModel) contentNodeModel, user);
			if(catId!=null){
				throw new InvalidFilteringArgumentException("Node has only one category. Redirect to: "+catId, InvalidFilteringArgumentException.Type.NODE_HAS_REDIRECT_URL, String.format(ONE_CATEGORY_REDIRECT_URL, catId));
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
	private String isSpecialRedirectConditionsApply(DepartmentModel dept, FDSessionUser user){
		
		String theOnlyOne=null;
		
		if(dept.getCategories()!=null){
			
			int categoryCounter = 0;
			
			for(CategoryModel cat : dept.getCategories()){
				
				if(NavigationUtil.isCategoryHiddenInContext(user, cat)){
					continue;
				}
					
				++categoryCounter;
					
				if(categoryCounter>1){
					return null;
				}
					
				theOnlyOne = cat.getContentKey().getId();
			}
			
			if(categoryCounter==1){
				return theOnlyOne;				
			}
			
		}
		
		return null;
	}

 private void buildHlCategoriesPageBeacon(Map<String, String> hlSelectionsofPageBeacon, BrowseDataContext browseDataContext,
		 									FDSessionUser user, List<FilteringSortingItem<ProductModel>> productResults, String catId ){
	try{
		List<FilteringProductItem> items = ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(productResults);
		
		StringBuffer updatedPageBeacon= new StringBuffer("&aShown=");
		  if(null !=items){
				for (FilteringProductItem product : items) {
						if(product.getProductModel()!=null && !product.getProductModel().isUnavailable()) {
							ProductData productData = null;
							try {
								productData = ProductDetailPopulator.createProductData(user, product.getProductModel());
							} catch (FDResourceException e) {
								LOG.warn("Exception while populating HookLogic returned product: ", e);
							} catch (FDSkuNotFoundException e) {
								LOG.warn("Exception while populating HookLogic returned product: ", e);
							} catch (HttpErrorResponse e) {
								LOG.warn("Exception while populating HookLogic returned product: ", e);
							}
							catch (Exception e) {
								LOG.warn("Exception while populating HookLogic returned product: ", e);
							}
							if(null != productData && null != productData.getSkuCode()){
								//updatedPageBeacon.append((("&aShown=".equals(updatedPageBeacon.toString()))?productData.getSkuCode():","+productData.getSkuCode()));
								productData.setFeatured(true);
								productData.setClickBeacon(((ProductModelBrandAdsAdapter)product.getProductModel()).getClickBeacon());
								productData.setImageBeacon(((ProductModelBrandAdsAdapter)product.getProductModel()).getImpBeacon());
									
								} 
							}
						}
					
				}else{
					updatedPageBeacon.append("none");
				  }
				if(null!=browseDataContext.getAdProducts().getPageBeacon()){
					StringBuffer PageBeacon=new StringBuffer(browseDataContext.getAdProducts().getPageBeacon());
					  PageBeacon.append(updatedPageBeacon).toString();
					  hlSelectionsofPageBeacon.put(catId, PageBeacon.toString());
					}
		}catch (Exception e) {
			LOG.warn("Exception while populating HookLogic products: ", e);
		}
 	}
 
private List<ProductData> getProductDataList(FDSessionUser user, List<FilteringSortingItem<ProductModel>> hlItems, NavigationModel navigationModel){
	
	List<FilteringProductItem> items = ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(hlItems);
	ProductData productData = null;
	 	List<ProductData> hlProductDataList = new ArrayList<ProductData>();
	 	items = ProductItemFilterUtil.getFilteredProducts(items, navigationModel.getActiveFilters(), true, true);
	 		for (FilteringProductItem product : items) {
	 			if(product.getProductModel()!=null && !product.getProductModel().isUnavailable()){
	 	      try { 
					productData = ProductDetailPopulator.createProductData(user, product.getProductModel());
					productData.setClickBeacon(((ProductModelBrandAdsAdapter)product.getProductModel()).getClickBeacon());
					productData.setImageBeacon(((ProductModelBrandAdsAdapter)product.getProductModel()).getImpBeacon());
					} catch (FDResourceException e) {
						LOG.warn("Exception while populating HookLogic returned product: ", e);
				e.printStackTrace();
					} catch (FDSkuNotFoundException e) {
						LOG.warn("Exception while populating HookLogic returned product: ", e);
					} catch (HttpErrorResponse e) {
						LOG.warn("Exception while populating HookLogic returned product: ", e);
				e.printStackTrace();
					}
					catch (Exception e) {
						LOG.warn("Exception while populating Hook Logic ProductData: ", e);
					}
	 	     hlProductDataList.add(productData);
			}
	  	
		if(hlProductDataList.size()>=FDStoreProperties.getHlProductsCount())
			break;
		}
	 return hlProductDataList;
 }
}
