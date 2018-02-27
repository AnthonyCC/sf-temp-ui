package com.freshdirect.webapp.ajax.filtering;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;

public interface BrowseDataBuilderI {
	
	BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDUserI user, CmsFilteringNavigator nav);

}
