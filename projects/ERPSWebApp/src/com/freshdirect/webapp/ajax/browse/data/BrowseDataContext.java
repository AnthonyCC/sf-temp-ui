package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;

public class BrowseDataContext extends BrowseData {

	private static final long serialVersionUID = 3281468863959645884L;

	@JsonIgnore
    private List<SectionContext> sectionContexts = new ArrayList<SectionContext>();

	@JsonIgnore
	private List<FilteringProductItem> unfilteredItems = new ArrayList<FilteringProductItem>();

	@JsonIgnore
	private ContentNodeModel currentContainer;

	@JsonIgnore
	private NavigationModel navigationModel;

	@JsonIgnore
	private FilteringFlowType pageType;

	@JsonIgnore
	private Map<String, List<String>> requestFilterParams;

	@JsonIgnore
	private Map<String, Boolean> dataFilterParams;

    public BrowseData extractBrowseDataPrototype(FDUserI user, CmsFilteringNavigator nav) {
		List<SectionData> sections = new ArrayList<SectionData>();
		for(SectionContext context : sectionContexts){
            sections.add(context.extractDataFromContext(nav));
		}
		this.getSections().setSections(sections);
		this.getSections().setIfSingleUL((FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.productCard2018, user)));
		
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

    public Map<String, List<String>> getRequestFilterParams() {
        return requestFilterParams;
    }

    public void setRequestFilterParams(Map<String, List<String>> requestFilterParams) {
        this.requestFilterParams = requestFilterParams;
    }

    public Map<String, Boolean> getDataFilterParams() {
        return dataFilterParams;
    }

    public void setDataFilterParams(Map<String, Boolean> dataFilterParams) {
        this.dataFilterParams = dataFilterParams;
    }
}
