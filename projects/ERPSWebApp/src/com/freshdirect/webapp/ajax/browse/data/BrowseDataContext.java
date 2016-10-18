package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;

public class BrowseDataContext extends BrowseData {

	private static final long serialVersionUID = 3281468863959645884L;
	
	@JsonIgnore
	private List<SectionContext> sectionContexts;
	
	@JsonIgnore
	private List<FilteringProductItem> unfilteredItems = new ArrayList<FilteringProductItem>();
	
	@JsonIgnore
	private ContentNodeModel currentContainer;
	
	@JsonIgnore
	private NavigationModel navigationModel;
	
	@JsonIgnore
	private FilteringFlowType pageType;
	
    public BrowseData extractBrowseDataPrototype(FDUserI user, CmsFilteringNavigator nav) {
		List<SectionData> sections = new ArrayList<SectionData>();
		for(SectionContext context : sectionContexts){
            sections.add(context.extractDataFromContext(user, nav));
		}
		this.getSections().setSections(sections);
		return this;
	}

	public List<SectionContext> getSectionContexts() {
		return sectionContexts;
	}

	public void setSectionContexts(List<SectionContext> sectionContexts) {
		this.sectionContexts = sectionContexts;
	}

	public List<FilteringProductItem> getUnfilteredItems() {
		return unfilteredItems;
	}

	public void setUnfilteredItems(List<FilteringProductItem> unfilteredItems) {
		this.unfilteredItems = unfilteredItems;
	}

	public ContentNodeModel getCurrentContainer() {
		return currentContainer;
	}

	public void setCurrentContainer(ContentNodeModel currentContainer) {
		this.currentContainer = currentContainer;
	}

	public NavigationModel getNavigationModel() {
		return navigationModel;
	}

	public void setNavigationModel(NavigationModel navigationModel) {
		this.navigationModel = navigationModel;
	}

	public FilteringFlowType getPageType() {
		return pageType;
	}

	public void setPageType(FilteringFlowType pageType) {
		this.pageType = pageType;
	}

}
