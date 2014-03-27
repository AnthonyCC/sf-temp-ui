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
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.RecipeDepartment;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.paging.BrowseDataPagerHelper;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringServlet.BrowseEvent;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class CmsFilteringFlow {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getInstance( CmsFilteringFlow.class );
	
	private static String FALLBACK_URL = "/";
	private static String RECIPE_DEPARTMENT_URL_FS = "/recipe_dept.jsp?deptId=%s";
	private static String SPECIAL_LAYOUT_URL_FS = "/browse_special.jsp?id=%s";
	
	@SuppressWarnings("deprecation")
	public CmsFilteringFlowResult doFlow(CmsFilteringNavigator nav, FDSessionUser user) throws InvalidFilteringArgumentException{
		
		BrowseDataContext browseDataContext = null;
		
		BrowseEvent event = nav.getBrowseEvent()!=null ? BrowseEvent.valueOf(nav.getBrowseEvent().toUpperCase()) : BrowseEvent.NOEVENT;

		// invalidate cache entry in every other case than paging or sorting
		if(event!=BrowseEvent.PAGE && event!=BrowseEvent.SORT) {		
			EhCacheUtil.removeFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey());
		} else {
			browseDataContext = EhCacheUtil.getObjectFromCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey());							
		}
		
		if(browseDataContext==null){
			
			String id = nav.getId();
			ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);
			
			// validation
			validateNode(nav, contentNodeModel, id, user);
			
			ProductContainer productContainer = (ProductContainer) contentNodeModel;
			
			//override show all
			if (productContainer.isTopLevelCategory() && productContainer.isShowAllByDefault()){
				nav.setAll(true);
			}
			
			// create filters and building menu
			NavigationModel navigationModel = NavigationUtil.createNavigationModel(productContainer, nav, user);
			
			// filtering and grouping
			browseDataContext = BrowseDataBuilderFactory.createBuilder(navigationModel.getNavDepth()).buildBrowseData(navigationModel, user, nav);
			// inject references
			browseDataContext.setNavigationModel(navigationModel);
			browseDataContext.setCurrentContainer(productContainer);
			
			// menu availability check
			MenuBuilderFactory.getInstance().checkMenuStatus(browseDataContext, navigationModel, user);
			
			// populate browseData with the menu
			browseDataContext.getMenuBoxes().setMenuBoxes(navigationModel.getLeftNav());
			
			// -- POPULATE EXTRA DATA --
			
			// populate browseData with breadcrumbs
			BrowseDataBuilderFactory.getInstance().populateWithBreadCrumbAndDesciptiveContent(browseDataContext, navigationModel);
			
			// populate browseData with filterLabels
			BrowseDataBuilderFactory.getInstance().populateWithFilterLabels(browseDataContext, navigationModel);
			
			if(!nav.isPdp()){
				EhCacheUtil.putObjectToCache(EhCacheUtil.BR_USER_REFINEMENT_CACHE_NAME, user.getUser().getPrimaryKey(), browseDataContext);				
			}
		}
		
		// -- SORTING --
		if(!nav.isPdp()){
			// process sort options
			BrowseDataBuilderFactory.getInstance().processSorting(browseDataContext, nav);			
		}
			
		// -- REMOVE EMPTY SECTIONS --
		
		BrowseData browseData = browseDataContext.extractBrowseDataPrototype(user);
		if (!FDStoreProperties.getPreviewMode()) {
			BrowseDataBuilderFactory.getInstance().removeEmptySections(browseData.getSections().getSections(), browseDataContext.getMenuBoxes().getMenuBoxes());
		}
		
		// -- REMOVE MENU BOXES WITH NULL SELECTION --
		MenuBuilderFactory.getInstance().checkNullSelection(browseDataContext.getMenuBoxes().getMenuBoxes());
		
			
		if(!nav.isPdp()){
			
			// -- PAGING --
			BrowseDataPagerHelper.createPagerContext(browseData, nav);
			BrowseDataBuilderFactory.getInstance().appendCarousels(browseData, user);
			
			browseData.getDescriptiveContent().setUrl(URLEncoder.encode(nav.assembleQueryString()));
			browseData.getSortOptions().setCurrentOrderAsc(nav.isOrderAscending());		
			
			// -- CALCULATE SECTION MAX LEVEL --
			BrowseDataBuilderFactory.getInstance().calculateMaxSectionLevel(browseData.getSections(), browseData.getSections().getSections(), 0);
		}
		
			
		return new CmsFilteringFlowResult(browseData, browseDataContext.getNavigationModel());
		
	}

	
	private void validateNode(CmsFilteringNavigator nav, ContentNodeModel contentNodeModel, String id, FDSessionUser user) throws InvalidFilteringArgumentException {

		if (contentNodeModel instanceof RecipeDepartment) {
			throw new InvalidFilteringArgumentException("Node is recipe department: "+id, InvalidFilteringArgumentException.Type.NODE_IS_RECIPE_DEPARTMENT, String.format(RECIPE_DEPARTMENT_URL_FS, id));
		}
				
		if (! (contentNodeModel instanceof ProductContainer)) { //applies for null as well
			throw new InvalidFilteringArgumentException("Node is not a product container or null: "+id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, String.format(FALLBACK_URL, id));
		}

		if (contentNodeModel instanceof CategoryModel && (NavigationUtil.isCategoryHiddenInContext(user, (CategoryModel)contentNodeModel)) && !nav.isPdp()) {
			throw new InvalidFilteringArgumentException("Category is hidden: "+id, InvalidFilteringArgumentException.Type.CANNOT_DISPLAY_NODE, String.format(FALLBACK_URL, id));				
		}
		
		// throw exception if we have special layout and we are not on the browse_special.jsp or pdp.jsp
		if(!nav.isPdp() && !nav.isSpecialPage() && contentNodeModel instanceof CategoryModel && ((CategoryModel)contentNodeModel).getSpecialLayout()!=null){
			throw new InvalidFilteringArgumentException("Node has special layout: "+id, InvalidFilteringArgumentException.Type.SPECIAL_LAYOUT, String.format(SPECIAL_LAYOUT_URL_FS, id));
		}
		
		// handle redirect url
		String redirectUrl = ((ProductContainer) contentNodeModel).getRedirectUrlClean();
		if(redirectUrl!=null){
			throw new InvalidFilteringArgumentException("Node has redirect URL: "+id, InvalidFilteringArgumentException.Type.NODE_HAS_REDIRECT_URL, redirectUrl);				
		}
		
		// special categories where leftnav is not needed
		if(!nav.isPdp() && contentNodeModel instanceof CategoryModel && ((CategoryModel)contentNodeModel).getFullWidthLayout()!=null){
			throw new InvalidFilteringArgumentException("Category has a full width layout: "+id+". No left nav needed.", InvalidFilteringArgumentException.Type.TERMINATE);
		}		
		
	}
	

}
