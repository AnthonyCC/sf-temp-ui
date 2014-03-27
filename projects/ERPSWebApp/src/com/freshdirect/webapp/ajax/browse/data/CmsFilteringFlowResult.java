package com.freshdirect.webapp.ajax.browse.data;

public class CmsFilteringFlowResult {
	
	private BrowseData browseDataPrototype;
	private NavigationModel navigationModel;

	public CmsFilteringFlowResult(BrowseData browseDataPrototype, NavigationModel navModel) {
		super();
		this.browseDataPrototype = browseDataPrototype;
		this.navigationModel = navModel;
	}
	

	public BrowseData getBrowseDataPrototype() {
		return browseDataPrototype;
	}

	public void setBrowseDataPrototype(BrowseData browseDataPrototype) {
		this.browseDataPrototype = browseDataPrototype;
	}

	public NavigationModel getNavigationModel() {
		return navigationModel;
	}

	public void setNavigationModel(NavigationModel navigationModel) {
		this.navigationModel = navigationModel;
	}

}
