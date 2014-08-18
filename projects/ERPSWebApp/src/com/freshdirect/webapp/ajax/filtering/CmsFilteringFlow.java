package com.freshdirect.webapp.ajax.filtering;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.RecipeDepartment;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.CarouselDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.PagerData;
import com.freshdirect.webapp.ajax.browse.paging.BrowseDataPagerHelper;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringServlet.BrowseEvent;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class CmsFilteringFlow {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance( CmsFilteringFlow.class );
	
	private static String FALLBACK_URL = "/";
	private static String RECIPE_DEPARTMENT_URL_FS = "/recipe_dept.jsp?deptId=%s";
	private static String SPECIAL_LAYOUT_URL_FS = "/browse_special.jsp?id=%s";
	private static String ONE_CATEGORY_REDIRECT_URL = "/browse.jsp?id=%s";
	private static String SUPER_DEPARTMENT_WITHOUT_GLOBALNAV_URL = "/index.jsp";
	
	@SuppressWarnings("deprecation")
	public CmsFilteringFlowResult doFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		
		BrowseDataContext browseDataContext = null;
		
		BrowseEvent event = nav.getBrowseEvent()!=null ? BrowseEvent.valueOf(nav.getBrowseEvent().toUpperCase()) : BrowseEvent.NOEVENT;

		//use userRefinementCache
		String userPrimaryKey = user.getUser().getPrimaryKey();
		if(event!=BrowseEvent.PAGE && event!=BrowseEvent.SORT) { // invalidate cache entry in every other case than paging or sorting		
			EhCacheUtil.removeFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey);
		
		} else {
			browseDataContext = EhCacheUtil.getObjectFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey);
			if (browseDataContext!=null && browseDataContext.getCurrentContainer()!=null && !browseDataContext.getCurrentContainer().getContentName().equalsIgnoreCase(nav.getId())){
				EhCacheUtil.removeFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, userPrimaryKey); //if cached id is not the same as url id 
				browseDataContext = null;
			}
		}
		
		if (browseDataContext == null) {
			if (nav.isBrowseRequest()) {
				browseDataContext = doBrowseFlow(nav, user);
			} else if (nav.isSearchRequest()) {
				browseDataContext = doSearchFlow(nav, user);
			}
		}
		
		if(!nav.isPdp()){
			EhCacheUtil.putObjectToCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey(), browseDataContext);				
		}
		
		// -- SORTING -- (not on pdp or super department page)
		if(!nav.isPdp() && !(browseDataContext.getCurrentContainer() instanceof SuperDepartmentModel)){
			// process sort options
			BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav);			
		}
			
		// -- REMOVE EMPTY SECTIONS --
		
		BrowseData browseData = browseDataContext.extractBrowseDataPrototype(user, nav);
		if (!browseDataContext.getNavigationModel().isSuperDepartment() && !FDStoreProperties.getPreviewMode()) {
			BrowseDataBuilderFactory.getInstance().removeEmptySections(browseData.getSections().getSections(), browseDataContext.getMenuBoxes().getMenuBoxes());
		}
		
		// -- REMOVE MENU BOXES WITH NULL SELECTION --
		MenuBuilderFactory.getInstance().checkNullSelection(browseDataContext.getMenuBoxes().getMenuBoxes());
		
		// -- RELOCATE BRAND FILTER BASED ON CMS SETTING
		if(browseDataContext.getNavigationModel().getBrandFilterLocation()!=null){
			MenuBuilderFactory.getInstance().relocateBrandFilter(browseDataContext.getMenuBoxes().getMenuBoxes(),  browseDataContext.getNavigationModel().getBrandFilterLocation());			
		}
		
		// -- REORDER MENU BOXES, PLACE POPUP TYPE FILTERS BELOW TOP CATEGORIES
		MenuBuilderFactory.getInstance().reorderMenuBoxes(browseDataContext.getMenuBoxes().getMenuBoxes());
		
			
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
				BrowseDataBuilderFactory.getInstance().appendCarousels(browseData, user, shownProductKeysForRecommender);
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
			
		return new CmsFilteringFlowResult(browseData, browseDataContext.getNavigationModel());
	}		

	public BrowseDataContext doSearchFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		
		BrowseDataContext browseDataContext = null;
		
		SearchResults results = ContentSearch.getInstance().searchProducts(nav.getSearchParams());
		
		//TODO: mocked navigation model...
		NavigationModel navigationModel = new NavigationModel();
		navigationModel.setProductListing(true);

		for (FilteringSortingItem<ProductModel> searchResult : results.getProducts()) {
			ProductModel product = searchResult.getModel();
			navigationModel.getSearchResults().add(product);
			navigationModel.getDepartmentsOfSearchResults().put(product.getDepartment().getContentName(), product.getDepartment());
			CategoryModel categoryModel = product.getCategory();
			if (categoryModel.getParentNode() instanceof DepartmentModel) { //Category
				navigationModel.getCategoriesOfSearchResults().put(categoryModel.getContentName(), categoryModel);
			} else {
				if (categoryModel.getParentNode().getParentNode() instanceof DepartmentModel) { //Subcategory
					navigationModel.getSubCategoriesOfSearchResults().put(categoryModel.getContentName(), categoryModel);
					navigationModel.getCategoriesOfSearchResults().put(categoryModel.getParentNode().getContentName(), (CategoryModel)categoryModel.getParentNode());
				} else { //Subsubcategory
					navigationModel.getSubCategoriesOfSearchResults().put(categoryModel.getParentNode().getContentName(), (CategoryModel)categoryModel.getParentNode());
					navigationModel.getCategoriesOfSearchResults().put(categoryModel.getParentNode().getParentNode().getContentName(), (CategoryModel)categoryModel.getParentNode().getParentNode());
				}
			}
		}
		
		navigationModel.setAllFilters(NavigationUtil.createSearchFilterGroups(navigationModel, nav, user));

		// select active filters
		navigationModel.setActiveFilters(NavigationUtil.selectActiveFilters(navigationModel.getAllFilters(), nav));
		
		browseDataContext = BrowseDataBuilderFactory.createBuilder(null, navigationModel.isSuperDepartment(), nav.isSearchRequest()).buildBrowseData(navigationModel, user, nav);

		browseDataContext.setNavigationModel(navigationModel);

		MenuBuilderI menuBuilder = MenuBuilderFactory.createBuilderByPageType(null, navigationModel.isSuperDepartment(), nav.isSearchRequest());

		// create menu
		navigationModel.setLeftNav(menuBuilder.buildMenu(navigationModel.getAllFilters(), navigationModel, nav));

		browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
		
		BrowseData.DescriptiveDataCointainer descriptiveContent = browseDataContext.getDescriptiveContent();
		descriptiveContent.setPageTitle("FreshDirect - Search - " + nav.getSearchParams());

		return browseDataContext;

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
		browseDataContext = BrowseDataBuilderFactory.createBuilder(navigationModel.getNavDepth(), navigationModel.isSuperDepartment(), nav.isSearchRequest()).buildBrowseData(navigationModel, user, nav);

		// inject references
		browseDataContext.setNavigationModel(navigationModel);
		browseDataContext.setCurrentContainer(contentNodeModel);

		if (!navigationModel.isSuperDepartment()) { //don't do these in case of super department page
			// menu availability check
			MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user);
		}
		
		// populate browseData with the menu
		browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
		final String departmentorSuperDepartmentName = getDepartmentOrSuperDepartment(contentNodeModel, navigationModel);
        browseDataContext.getMenuBoxes().setMenuName(departmentorSuperDepartmentName);
		
		// -- POPULATE EXTRA DATA --
		
		// populate browseData with breadcrumbs
		BrowseDataBuilderFactory.getInstance().populateWithBreadCrumbAndDesciptiveContent(browseDataContext, navigationModel);
		
		// populate browseData with filterLabels
		BrowseDataBuilderFactory.getInstance().populateWithFilterLabels(browseDataContext, navigationModel);
		
		return browseDataContext;
	}

	private String getDepartmentOrSuperDepartment(ContentNodeModel contentNodeModel, NavigationModel navigationModel) {
		final String departmentorSuperDepartmentName;
		if (contentNodeModel instanceof SuperDepartmentModel) {
			departmentorSuperDepartmentName = contentNodeModel.getFullName();
		} else if (contentNodeModel instanceof DepartmentModel) {
			SuperDepartmentModel superDepartmentModel = navigationModel.getSuperDepartmentModel();
			if (superDepartmentModel != null) {
				departmentorSuperDepartmentName = superDepartmentModel.getFullName();
			} else {
				departmentorSuperDepartmentName = contentNodeModel.getFullName();
			}
		} else {
			SuperDepartmentModel superDepartmentModel = navigationModel.getSuperDepartmentModel();
			if (superDepartmentModel != null) {
				departmentorSuperDepartmentName = superDepartmentModel.getFullName();
			} else {
				departmentorSuperDepartmentName = navigationModel.getNavigationHierarchy().get(NavDepth.DEPARTMENT).getFullName();
			}
		}
		return departmentorSuperDepartmentName;
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