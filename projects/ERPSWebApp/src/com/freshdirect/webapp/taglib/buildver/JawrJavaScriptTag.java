package com.freshdirect.webapp.taglib.buildver;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.taglib.JavascriptBundleTag;

public class JawrJavaScriptTag extends JavascriptBundleTag {

	private static final long serialVersionUID = 1285075045931324055L;
	
	private String onload;
	public int doStartTag() throws JspException {
		
		return super.doStartTag();
	}
	@Override
	protected BundleRenderer createRenderer(ResourceBundlesHandler rsHandler, Boolean useRandomParam) {
		Boolean asyncFlag = null;
		if (StringUtils.isNotEmpty(async)) {
			asyncFlag = Boolean.valueOf(async);
		}
		Boolean deferFlag = null;
		if (StringUtils.isNotEmpty(defer)) {
			deferFlag = Boolean.valueOf(defer);
		}
		JavaScriptBundleLinkRenderer renderer = new JavaScriptBundleLinkRenderer();
		renderer.init(rsHandler, useRandomParam, asyncFlag, deferFlag, onload);
		return renderer;
	}
	public String getOnload() {
		return onload;
	}
	public void setOnload(String onload) {
		this.onload = onload;
	}
	
}
