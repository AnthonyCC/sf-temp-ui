package com.freshdirect.webapp.ajax.filtering;

import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public interface BrowseDataBuilderI {
	
	BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav);

}
