package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.customer.FDUserI;

public class BrowseDataContext extends BrowseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3281468863959645884L;
	
	@JsonIgnore
	private List<SectionContext> sectionContexts;
	
	@JsonIgnore
	private List<FilteringProductItem> unfilteredItems = new ArrayList<FilteringProductItem>();
	
	@JsonIgnore
	private ProductContainer currentContainer;
	
	@JsonIgnore
	private NavigationModel navigationModel;
	
	public BrowseData extractBrowseDataPrototype(FDUserI user){
		
		List<SectionData> sections = new ArrayList<SectionData>();
		for(SectionContext context : sectionContexts){
			sections.add(context.extractDataFromContext(user));
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

	public ProductContainer getCurrentContainer() {
		return currentContainer;
	}

	public void setCurrentContainer(ProductContainer currentContainer) {
		this.currentContainer = currentContainer;
	}

	public NavigationModel getNavigationModel() {
		return navigationModel;
	}

	public void setNavigationModel(NavigationModel navigationModel) {
		this.navigationModel = navigationModel;
	}

}
