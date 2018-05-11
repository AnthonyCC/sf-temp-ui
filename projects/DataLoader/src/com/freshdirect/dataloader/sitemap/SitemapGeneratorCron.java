package com.freshdirect.dataloader.sitemap;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.sitemap.SitemapHome;
import com.freshdirect.fdstore.sitemap.SitemapSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class SitemapGeneratorCron {

    private static final Logger LOGGER = LoggerFactory.getInstance(SitemapGeneratorCron.class);

    public static void main(String[] args) {
        Context ctx = null;
        try {
            LOGGER.info("SitemapGeneratorCron Started.");
            if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.SitemapSB)){
			FDECommerceService.getInstance().generateSitemap();
            }else{
            ctx = ErpServicesProperties.getInitialContext();
            SitemapHome managerHome = (SitemapHome) ctx.lookup(SitemapHome.JNDI_HOME);
            SitemapSB sb = managerHome.create();
            sb.generateSitemap();
            }
        } catch (Exception e) {
            LOGGER.error("SitemapGeneratorCron failed with Exception...", e);
        } finally {
            try {
                if (ctx != null) {
                    ctx.close();
                }
            } catch (NamingException ne) {
                LOGGER.warn("Cannot close Context while trying to cleanup", ne);
            }
        }

        LOGGER.info("SitemapGeneratorCron Stopped.");
    }

}
