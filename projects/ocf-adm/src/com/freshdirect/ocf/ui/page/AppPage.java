/**
 * @author ekracoff
 * Created on Jun 15, 2005*/

package com.freshdirect.ocf.ui.page;

import org.apache.tapestry.html.BasePage;

import com.freshdirect.ocf.core.OcfDaoFactory;

public class AppPage extends BasePage {

	public void detach() {
		super.detach();
		System.err.println("AppPage.detach() " + this);
		OcfDaoFactory.getInstance().getActionDao().closeSession();
	}

}