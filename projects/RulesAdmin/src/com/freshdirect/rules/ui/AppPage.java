package com.freshdirect.rules.ui;

import org.apache.tapestry.html.BasePage;

import com.freshdirect.framework.conf.FDRegistry;

public class AppPage extends BasePage {

	/*public void attach(IEngine value) {
		super.attach(value);
		RulesSessionManager.open();
	}
	*/

	public void detach() {
		super.detach();
		FDRegistry.getInstance().cleanupThread();
	}

}
