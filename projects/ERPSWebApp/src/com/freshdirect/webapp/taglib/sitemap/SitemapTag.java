package com.freshdirect.webapp.taglib.sitemap;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.sitemap.SitemapData;
import com.freshdirect.fdstore.sitemap.SitemapDataFactory;
import com.freshdirect.fdstore.sitemap.SitemapHome;
import com.freshdirect.fdstore.sitemap.SitemapSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SitemapTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(SitemapTag.class.getName());

    @Override
    public void doTag() throws JspException, IOException {
        PageContext ctx = ((PageContext) getJspContext());
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        String password = request.getParameter("password");
        String type = request.getParameter("type");

        if (password != null && FDStoreProperties.getSitemapPasswords().contains(password)) {
            if ("generate".equals(type)) {
                LOGGER.debug("Generate sitemap");
                Context initialContext = null;
                try {
                    initialContext = ErpServicesProperties.getInitialContext();
                    SitemapHome managerHome = (SitemapHome) initialContext.lookup(SitemapHome.JNDI_HOME);
                    SitemapSB sb = managerHome.create();
                    sb.generateSitemap();
                    ctx.setAttribute("siteMapGenerated", true);
                    LOGGER.debug("Generated sitemap successfully");
                } catch (Exception e) {
                    LOGGER.error("Error occured during sitemap generation", e);
                } finally {
                    try {
                        if (initialContext != null) {
                            initialContext.close();
                        }
                    } catch (NamingException ne) {
                    }
                }
            } else {
                SitemapData rootData = SitemapDataFactory.create();
                ctx.setAttribute("siteMapData", rootData);
            }
        }
    }

}
