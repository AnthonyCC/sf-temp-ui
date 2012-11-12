package com.freshdirect.fdstore.coremetrics.builder;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.coremetrics.tagmodel.ElementTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.smartstore.service.VariantRegistry;

public class ElementTagModelBuilder {
	
	public static final String CAT_CAROUSEL = "carousel";
	public static final String CAT_TIMESLOT = "timeslot_chooser";

	public static final String CAT_CNTABS = "cntabs";
	private static final String ATTR_DELIMITER = "|";
	private static final String ID_TYPE_DELIMITER = "_";
	private static final String ID_TAB_SHOW_TEMPLATE = "tab_show";
	private static final String ID_TAB_CLICK_TEMPLATE = "tab_click";
	
	public static final String CAT_SEARCH_FILTER = "search_filter";
	public static final String CAT_SEARCH_SORT = "search_sort";
	public static final String CAT_SEARCH_VIEW = "search_view";
	public static final String CAT_NEW_PRODUCTS_FILTER = "new_products_filter";
	public static final String CAT_NEW_PRODUCTS_SORT = "new_products_sort";
	
	public static final String CAT_PRESIDENT_SORT = "president_picks_sort";
	public static final String CAT_PRESIDENT_VIEW = "president_picks_view";
	
	public static final String CAT_VIDEO = "video";
	
	private ElementTagModel model = new ElementTagModel();
	private String elementId;
	private String elementCategory;
	private String carouselId;
	private String siteFeature;
	private String variant;
	private FDUserI user;
	private FDTimeslot timeSlot;
	private boolean soType;

	private TabRecommendation tabRecommendation;
	private Integer tabNumber;

	private FilteringNavigator searchNavigator;
	QueryParameterCollection queryParamCollection;


	public ElementTagModel buildTagModel()  throws SkipTagException {

		setDefaultModelAttributes();

		if (CAT_CAROUSEL.equals(elementCategory)){
			processCarousel();
		} else if (CAT_TIMESLOT.equals(elementCategory)){
			processTimeslot();
		} else if (CAT_CNTABS.equals(elementCategory)){
			processCntabs();
		} else if (CAT_SEARCH_FILTER.equals(elementCategory) || CAT_NEW_PRODUCTS_FILTER.equals(elementCategory)){
			processSearchFilter();
		} else if (CAT_SEARCH_SORT.equals(elementCategory) || CAT_NEW_PRODUCTS_SORT.equals(elementCategory)){
			processSearchSort();
		} else if (CAT_SEARCH_VIEW.equals(elementCategory)){
			processSearchView();
		} else if (CAT_VIDEO.equals(elementCategory)){
			processVideoEvent();
		} else if (CAT_PRESIDENT_SORT.equals(elementCategory)){
			processPresPicksSort();
		} else if (CAT_PRESIDENT_VIEW.equals(elementCategory)){
			processPresPicksView();
		}
			
		return model;
	}

	private void setDefaultModelAttributes(){
		model.setElementId(elementId);
		model.setElementCategory(elementCategory);
	}
	
	private void processCarousel(){
		if (carouselId == null){
			if (variant == null){
				variant = VariantSelectorFactory.getSelector(EnumSiteFeature.getEnum(siteFeature)).select(user, false).getId();
			
			} else {
				siteFeature = VariantRegistry.getInstance().getService(variant).getSiteFeature().getName();
			}

			String cohort = user.getCohortName();
			carouselId = siteFeature + ATTR_DELIMITER + cohort + ATTR_DELIMITER + variant;
			setSsAttributes(siteFeature, variant, cohort);
		}

		elementId = carouselId + ID_TYPE_DELIMITER + "page";
		model.setElementId(elementId);
	}
	

	private void setSsAttributes(String feature, String variant, String cohort){
		Map<Integer, String> attributesMap = model.getAttributesMaps();
		attributesMap.put(4, siteFeature);
		attributesMap.put(5, variant);
		attributesMap.put(6, cohort);
	}
	
	private void processCntabs() throws SkipTagException{
		if (tabRecommendation==null || tabNumber==null){
			throw new SkipTagException("tabRecommendation or tabNumber is null");
		}

		if (ID_TAB_CLICK_TEMPLATE.equals(elementId) || ID_TAB_SHOW_TEMPLATE.equals(elementId)){
			String variant = tabRecommendation.get(tabNumber).getId();
			String siteFeature = tabRecommendation.get(tabNumber).getSiteFeature().getName();
			String tabStrategy = tabRecommendation.getTabVariant().getId();
			String cohort = user.getCohortName();
			elementId= tabNumber +ATTR_DELIMITER+ cohort +ATTR_DELIMITER+ tabStrategy +ATTR_DELIMITER+ variant +ID_TYPE_DELIMITER+ elementId;

			model.setElementId(elementId);
			setSsAttributes(siteFeature+ATTR_DELIMITER+tabStrategy, variant, cohort);
		}
	}


	private void processTimeslot() throws SkipTagException{
		
		if(timeSlot!=null){
			String elementId = DateUtil.formatDayOfWeek(timeSlot.getBaseDate())+"_"+DateUtil.formatCmTimeslot(timeSlot.getBegTime())+"_"+DateUtil.formatCmTimeslot(timeSlot.getEndTime());
			model.setElementId(elementId);
			model.setElementCategory(elementCategory);	
			model.getAttributesMaps().put(1, DateUtil.formatDayOfWeek(timeSlot.getBaseDate())+"|"+DateUtil.getDiffInMinutes(timeSlot.getEndTime(), timeSlot.getBegTime())/60+" h window");
			model.getAttributesMaps().put(2, DateUtil.formatCmTimeslot(timeSlot.getBegTime())+"|"+DateUtil.formatCmTimeslot(timeSlot.getEndTime()));
			model.getAttributesMaps().put(3, soType ? "so_template" : "regular");			
		} else {
			throw new SkipTagException("No timeslot found! Skipping tag.");
		}
		
	}
	
	private void processSearchFilter() throws SkipTagException{
		
		if(searchNavigator!=null){
			
			StringBuilder idBuilder = new StringBuilder();
			
			for(EnumFilteringValue filter : searchNavigator.getFilterValues().keySet()){
				List<Object> values = searchNavigator.getFilterValues().get(filter);
				if(values!=null){
					idBuilder.append(filter.getName().charAt(0)+"-"+values.get(0)+ATTR_DELIMITER);
				}
			}
			
			if (idBuilder.length()>0){
				model.setElementId(idBuilder.substring(0, idBuilder.lastIndexOf(ATTR_DELIMITER)));
			} else {
				throw new SkipTagException("Filter is empty! Skipping tag.");
			}
			
		} else {
			throw new SkipTagException("No navigator found! Skipping tag.");
		}
		
	}
	
	private void processSearchSort() throws SkipTagException{
		
		if(searchNavigator!=null && !searchNavigator.isSortByDefault()){
			//as the compareTo method of Score class is working in a reverse way (when the navigator says asc it will sort desc in order to keep the most popular things on the top of the page by default), 
			//but the navigator handles asc and desc as excpected, we need to report the opposite order in case of relevancy and popularity order
			if(searchNavigator.getSortBy() == SearchSortType.BY_POPULARITY || searchNavigator.getSortBy() == SearchSortType.BY_RELEVANCY){
				model.setElementId(searchNavigator.getSortBy().getText().toLowerCase()+"_"+(searchNavigator.isOrderAscending() ? "desc" : "asc"));
			}else{
				model.setElementId(searchNavigator.getSortBy().getText().toLowerCase()+"_"+(searchNavigator.isOrderAscending() ? "asc" : "desc"));				
			}
			
		} else {
			throw new SkipTagException("No navigator found or sorted by default! Skipping tag.");
		}
	}
	
	private void processSearchView() throws SkipTagException{
		
		if(searchNavigator!=null){
			model.setElementId(searchNavigator.getViewName());
		} else {
			throw new SkipTagException("No navigator found! Skipping tag.");
		}
	}
	
	private void processPresPicksView() throws SkipTagException{
		
		if(queryParamCollection!=null){
			model.setElementId(queryParamCollection.getParameterValue("view", "grid"));
		} else {
			throw new SkipTagException("No navigator found! Skipping tag.");
		}
	}
	
	private void processPresPicksSort() throws SkipTagException{
		
		if(queryParamCollection!=null){
			model.setElementId(queryParamCollection.getParameterValue("sort"));
		} else {
			throw new SkipTagException("No navigator found! Skipping tag.");
		}
	}
	
	private void processVideoEvent(){
		model.setElementId("video.title");
	}
	
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public void setElementCategory(String elementCategory) {
		this.elementCategory = elementCategory;
	}

	public void setCarouselId(String carouselId) {
		this.carouselId = carouselId;
	}

	public void setSiteFeature(String siteFeature) {
		this.siteFeature = siteFeature;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public void setUser(FDUserI user) {
		this.user = user;
	}

	public void setTimeSlot(FDTimeslot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public void setSoType(boolean soType) {
		this.soType = soType;
	}

	public void setTabRecommendation(TabRecommendation tabRecommendation) {
		this.tabRecommendation = tabRecommendation;
	}

	public void setTabNumber(int tabNumber) {
		this.tabNumber = tabNumber;
	}

	public void setSearchNavigator(FilteringNavigator searchNavigator) {
		this.searchNavigator = searchNavigator;
	}

	public void setQueryParamCollection(QueryParameterCollection queryParamCollection) {
		this.queryParamCollection = queryParamCollection;
	}

}