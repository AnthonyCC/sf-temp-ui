package com.freshdirect.webapp.taglib.buildver;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.Buildver;

import net.jawr.web.resource.bundle.renderer.JavascriptHTMLBundleLinkRenderer;


public class JavaScriptBundleLinkRenderer extends JavascriptHTMLBundleLinkRenderer   {

	private static final long serialVersionUID = 7468990075345372486L;

	@Override
	protected String renderLink(String fullPath) {
		if (FDStoreProperties.isBuildverEnabled()) {
			String buildVersion = Buildver.getInstance().getBuildver();
			if (fullPath.indexOf("?") != -1)
				fullPath = fullPath + "&buildver=" + buildVersion;
			else
				fullPath = fullPath + "?buildver=" + buildVersion;
		}
		String link =  super.renderLink(fullPath);
		
		return link;
		
	}
}
