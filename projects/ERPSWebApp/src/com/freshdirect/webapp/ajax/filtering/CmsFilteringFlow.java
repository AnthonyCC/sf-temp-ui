package com.freshdirect.webapp.ajax.filtering;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.content.nutrition.EnumKosherSymbolValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.NutritionValueEnum;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.AbstractProductItemFilter;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.ProductModel;
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
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.PagerData;
import com.freshdirect.webapp.ajax.browse.paging.BrowseDataPagerHelper;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringServlet.BrowseEvent;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;

public class CmsFilteringFlow {
	
	private static final Logger LOG = LoggerFactory.getInstance( CmsFilteringFlow.class );
	
	private static String FALLBACK_URL = "/";
	private static String RECIPE_DEPARTMENT_URL_FS = "/recipe_dept.jsp?deptId=%s";
	private static String SPECIAL_LAYOUT_URL_FS = "/browse_special.jsp?id=%s";
	private static String ONE_CATEGORY_REDIRECT_URL = "/browse.jsp?id=%s";
	private static String SUPER_DEPARTMENT_WITHOUT_GLOBALNAV_URL = "/index.jsp";
	
	@SuppressWarnings("deprecation")
	public CmsFilteringFlowResult doFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		BrowseData browseData = null;
		BrowseDataContext browseDataContext = getBrowseDataContextFromCacheForPagingOrSorting(nav, user);
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
				if (pagerData.isAll() || pagerData.getActivePage() == pagerData.getPageCount()){
					boolean disableCategoryYmalRecommender = browseDataContext.getCurrentContainer() instanceof ProductContainer && ((ProductContainer) browseDataContext.getCurrentContainer()).isDisableCategoryYmalRecommender();
					BrowseDataBuilderFactory.getInstance().appendCarousels(browseData, user, shownProductKeysForRecommender, disableCategoryYmalRecommender);
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

		} else if (nav.getPageType() != FilteringFlowType.BROWSE) {
			
			if (browseDataContext == null) {
				browseDataContext = doSearchLikeFlow(nav, user);
			}
			EhCacheUtil.putObjectToCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey(), browseDataContext);				
			
			BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav, user);			
				
			browseData = browseDataContext.extractBrowseDataPrototype(user, nav);
			
				
			// -- PAGING --
			if (!FilteringFlowType.PRES_PICKS.equals(nav.getPageType()) || (FilteringFlowType.PRES_PICKS.equals(nav.getPageType()) && FDStoreProperties.isPresidentPicksPagingEnabled())) {
				BrowseDataPagerHelper.createPagerContext(browseData, nav);
			}
	
			//Update product hit counts
//			for (BrowseData.SearchParams.Tab tab : browseData.getSearchParams().getTabs()) {
//				if ("product".equalsIgnoreCase(nav.getActiveTab())) {
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

	private BrowseDataContext getBrowseDataContextFromCacheForPagingOrSorting(CmsFilteringNavigator nav, FDSessionUser user) {
		BrowseDataContext browseDataContext = null;
		BrowseEvent event = nav.getBrowseEvent()!=null ? BrowseEvent.valueOf(nav.getBrowseEvent().toUpperCase()) : BrowseEvent.NOEVENT;
		//use userRefinementCache
		String userPrimaryKey = user.getUser().getPrimaryKey();
		if(event != BrowseEvent.PAGE && event != BrowseEvent.SORT) { // invalidate cache entry in every other case than paging or sorting
			removeBrowseDataContextFromCache(userPrimaryKey);
		} else {
			browseDataContext = EhCacheUtil.getObjectFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey);
			if (FilteringFlowType.BROWSE.equals(nav.getPageType())) {
				if (isBrowseRequestForTheSameId(nav, browseDataContext)){
					removeBrowseDataContextFromCache(userPrimaryKey); //if cached id is not the same as url id 
					browseDataContext = null;
				}
			} else {
				if (browseDataContext!=null && (!isRequestForTheSamePageType(nav, browseDataContext) || (FilteringFlowType.SEARCH.equals(nav.getPageType()) && !isRequestForTheSameSearchParams(nav, browseDataContext)))) {
					removeBrowseDataContextFromCache(userPrimaryKey); //if cached id is not the same as url id 
					browseDataContext = null;
				}
			}
		}
		return browseDataContext;
	}

	private boolean isRequestForTheSameSearchParams(CmsFilteringNavigator nav, BrowseDataContext browseDataContext) {
		return browseDataContext.getSearchParams().getSearchParams().equals(nav.getSearchParams());
	}

	private boolean isRequestForTheSamePageType(CmsFilteringNavigator nav, BrowseDataContext browseDataContext) {
		return browseDataContext.getSearchParams().getPageType().equals(nav.getPageType());
	}

	private boolean isBrowseRequestForTheSameId(CmsFilteringNavigator nav, BrowseDataContext browseDataContext) {
		return browseDataContext != null && browseDataContext.getCurrentContainer() != null && !browseDataContext.getCurrentContainer().getContentName().equalsIgnoreCase(nav.getId());
	}

	private void removeBrowseDataContextFromCache(String userPrimaryKey) {
		EhCacheUtil.removeFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey);
	}

	private void populateSearchCarouselProductLimit(int activePage,
			BrowseDataContext browseDataContext) {
		final int searchCarouselProductLimit;
		if (activePage == 0) {
			searchCarouselProductLimit = 0;
		} else {
			searchCarouselProductLimit = FDStoreProperties.getSearchCarouselProductLimit();
		}
		browseDataContext.getSections().setLimit(searchCarouselProductLimit);
	}		

	public BrowseDataContext doSearchLikeFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		NavigationModel navigationModel = new NavigationModel();
		SearchResults searchResults = ContentSearch.getInstance().searchProducts(nav.getSearchParams() == null ? "" : nav.getSearchParams()); //TODO this shouldn't be done when not on search page

		switch (nav.getPageType()) { //TODO warning
			case SEARCH:
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
		}
		
		SearchPageType searchPageType = SearchPageType.PRODUCT; //default behavior
		if ("recipe".equalsIgnoreCase(nav.getActiveTab())) {
			processRecipes(navigationModel, searchResults);
			searchPageType = SearchPageType.RECIPE;
		} else {
			processProducts(nav.getPageType(), navigationModel, searchResults);
			searchPageType = SearchPageType.PRODUCT; //TODO why is this set again?
		}

		setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);

		BrowseDataContext browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), searchPageType).buildBrowseData(navigationModel, user, nav);
		
		// if we are on recipe tab but there is no search result, it should fallback to products
		if ("recipe".equalsIgnoreCase(nav.getActiveTab()) && 0 == nav.getRecipeHits()) {
			nav.setActiveTab("product");
			navigationModel.setRecipeListing(false);
			navigationModel.getRecipeResults().clear();
			processProducts(nav.getPageType(), navigationModel, searchResults);
			searchPageType = SearchPageType.PRODUCT;
			setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
			browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), searchPageType).buildBrowseData(navigationModel, user, nav);
		}
		
		//refresh context sensitive filters
		if (navigationModel.getActiveFilters().size() > 0 && searchPageType == SearchPageType.PRODUCT && browseDataContext.getSectionContexts().size() > 0) {
			refreshResultDependantFilters(nav.getPageType(), navigationModel, browseDataContext.getSectionContexts().get(0).getProductItems());
			setupAllAndActiveFiltersForNavigationModel(nav, user, navigationModel);
		}
		
		browseDataContext.setNavigationModel(navigationModel);
		MenuBuilderI menuBuilder = MenuBuilderFactory.createBuilderByPageType(null, navigationModel.isSuperDepartment(), searchPageType);
		// create menu
		navigationModel.setLeftNav(menuBuilder.buildMenu(navigationModel.getAllFilters(), navigationModel, nav));
		if (!navigationModel.isSuperDepartment()) { //don't do these in case of super department page
			// menu availability check
			MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user);
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
				descriptiveContent.setOasSitePage(ContentFactory.getInstance().getContentNode(nav.getId()).getPath());
				Html presidentPicksPageTopMediaBanner = ContentFactory.getInstance().getStore().getPresidentPicksPageTopMediaBanner();
				if (presidentPicksPageTopMediaBanner != null) {
					descriptiveContent.setMedia(MediaUtils.renderHtmlToString(presidentPicksPageTopMediaBanner, user));
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
		}
		browseDataContext.getSearchParams().setSearchParams(nav.getSearchParams());
		browseDataContext.getSearchParams().setSuggestions((List<String>)searchResults.getSpellingSuggestions());
		browseDataContext.getSearchParams().setSearchTerm(searchResults.getSuggestedTerm());
		buildTabs(browseDataContext, searchResults, nav);
		browseDataContext.getSearchParams().setPageType(nav.getPageType());
		if (FilteringFlowType.NEWPRODUCTS.equals(nav.getPageType())) {
			browseDataContext.getSearchParams().setNumberOfNewProducts(searchResults.getProducts().size());
		}

		//set ddpp products for 'search like' pages
		for (ProductModel product : searchResults.getDDPPProducts()) {
			try {
				ProductData productData = ProductDetailPopulator.createProductData(user, product);
				productData.setFeatured(((ProductModelPromotionAdapter)product).isFeatured());
				productData.setFeaturedHeader(((ProductModelPromotionAdapter)product).getFeaturedHeader());
				browseDataContext.getDDPPProducts().getProducts().add(productData);
			} catch (FDResourceException e) {
				LOG.warn("Getting DDPP products failed!", e);
			} catch (FDSkuNotFoundException e) {
				LOG.warn("Getting DDPP products failed!", e);
			} catch (HttpErrorResponse e) {
				LOG.warn("Getting DDPP products failed!", e);
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
			if ("recipe".equalsIgnoreCase(cmsFilteringNavigator.getActiveTab())) {
				browseDataContext.getSearchParams().buildTabs("Products", "product", results.getProducts().size(), results.getProducts().size(), false);
				browseDataContext.getSearchParams().buildTabs("Recipes", "recipe", results.getRecipes().size(), cmsFilteringNavigator.getRecipeHits(), true);
			} else {
				browseDataContext.getSearchParams().buildTabs("Products", "product", results.getProducts().size(), Math.min(results.getProducts().size(), cmsFilteringNavigator.getProductHits()), true); //Math.min ~ workaround for product losses because of non existent default sku
				if (0 < results.getRecipes().size()) {
					browseDataContext.getSearchParams().buildTabs("Recipes", "recipe", results.getRecipes().size(), results.getRecipes().size(), false);
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
	
	public BrowseDataContext doBrowseFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		
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

		// inject references
		browseDataContext.setNavigationModel(navigationModel);
		browseDataContext.setCurrentContainer(contentNodeModel);

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
		
		return browseDataContext;
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
			throw new InvalidFilteringArgumentException("Node has special layout: "+id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT, String.format(SPECIAL_LAYOUT_URL_FS, id));
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
	

}
