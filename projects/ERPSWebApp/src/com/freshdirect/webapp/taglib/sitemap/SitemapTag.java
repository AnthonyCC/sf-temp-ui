package com.freshdirect.webapp.taglib.sitemap;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.sitemap.SitemapData;
import com.freshdirect.fdstore.sitemap.SitemapDataFactory;

public class SitemapTag extends SimpleTagSupport {
	
	@Override
	public void doTag() throws JspException, IOException {
		
		PageContext ctx = ((PageContext) getJspContext());
		HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
		String password = request.getParameter("password");

		if (password != null && FDStoreProperties.getSitemapPasswords().contains(password) ){
			SitemapData rootData = SitemapDataFactory.create();
			ctx.setAttribute("siteMapData", rootData);
		}
		
	}
}