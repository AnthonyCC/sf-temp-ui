package com.freshdirect.webapp.taglib.buildver;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import net.jawr.web.resource.bundle.handler.ResourceBundlesHandler;
import net.jawr.web.resource.bundle.renderer.BundleRenderer;
import net.jawr.web.taglib.el.ELCSSBundleTag;

public class JawrCssTag extends ELCSSBundleTag {

	private static final long serialVersionUID = 1527965316274125975L;
	
	public int doStartTag() throws JspException {
		
		return super.doStartTag();
	}
	@Override
	protected BundleRenderer createRenderer(ResourceBundlesHandler rsHandler, Boolean useRandomParam) {
		CssBundleLinkRenderer renderer = new CssBundleLinkRenderer();
		Boolean alternateFlag = false;
		if (StringUtils.isNotEmpty(getAlternateExpr())) {
			alternateFlag = Boolean.valueOf(getAlternateExpr());
		}
		renderer.init(rsHandler, useRandomParam, getMediaExpr(), alternateFlag, false, getTitleExpr());
		
		return renderer;
	}
	
}
