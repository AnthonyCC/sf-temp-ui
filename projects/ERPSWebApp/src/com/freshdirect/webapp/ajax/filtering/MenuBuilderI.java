package com.freshdirect.webapp.ajax.filtering;

import java.util.List;

import com.freshdirect.fdstore.content.ProductFilterGroupI;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;

public interface MenuBuilderI {
	
	public List<MenuBoxData> buildMenu(List<ProductFilterGroupI> filterGroups, NavigationModel navModel, CmsFilteringNavigator nav);

}
