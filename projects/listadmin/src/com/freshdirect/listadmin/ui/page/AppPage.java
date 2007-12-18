package com.freshdirect.listadmin.ui.page;

/**
 * @author ekracoff
 * Created on Jun 15, 2005
 */
import org.apache.tapestry.html.BasePage;

import com.freshdirect.listadmin.core.ListadminDaoFactory;

public class AppPage extends BasePage {

	public void detach() {
		super.detach();
		System.err.println("AppPage.detach() " + this);
		ListadminDaoFactory.getInstance().getListadminDao().closeSession();
	}

}
