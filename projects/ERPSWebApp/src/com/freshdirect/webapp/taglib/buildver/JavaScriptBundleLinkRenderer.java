package com.freshdirect.webapp.taglib.buildver;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.Buildver;

import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.JavascriptHTMLBundleLinkRenderer;


public class JavaScriptBundleLinkRenderer extends JavascriptHTMLBundleLinkRenderer   {

	private static final long serialVersionUID = 7468990075345372486L;
	private String onload;
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
		
		if (this.onload != null && !this.onload.isEmpty()) {
			return link.replace("></script>", " onload=\"" + onload + "\" ></script>");
		}
		return link;
		
	}

	public void init(ResourceBundlesHandler rsHandler, Boolean useRandomParam, Boolean asyncFlag, Boolean deferFlag,
			String onload) {
		super.init(rsHandler, useRandomParam, asyncFlag, deferFlag);
		this.onload = onload;
		
	}
}
