package com.freshdirect.dataloader.sitemap;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class SitemapGeneratorCron {

    private static final Logger LOGGER = LoggerFactory.getInstance(SitemapGeneratorCron.class);

    public static void main(String[] args) {
        try {
            LOGGER.info("SitemapGeneratorCron Started.");
            FDECommerceService.getInstance().generateSitemap();
            
        } catch (Exception e) {
            LOGGER.error("SitemapGeneratorCron failed with Exception...", e);
        }

        LOGGER.info("SitemapGeneratorCron Stopped.");
    }

}
