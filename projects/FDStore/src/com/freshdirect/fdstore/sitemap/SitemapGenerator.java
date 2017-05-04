package com.freshdirect.fdstore.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.freshdirect.fdstore.sitemap.config.SitemapConfigStrategy;
import com.freshdirect.fdstore.sitemap.config.SitemapConfiguration;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SitemapGenerator {

    private static final Logger LOGGER = LoggerFactory.getInstance(SitemapGenerator.class);

    private SitemapService service;
    private SitemapConfigStrategy configStrategy;

    public SitemapGenerator(SitemapService service, SitemapConfigStrategy configStrategy) {
        this.service = service;
        this.configStrategy = configStrategy;
    }

    public void generateAllTypes() {
        for (SitemapTypeEnum type : SitemapTypeEnum.values()) {
            generate(type);
        }
    }

    public List<File> generate(SitemapTypeEnum type) {
        List<File> sitemaps = Collections.emptyList();
        SitemapConfiguration config = configStrategy.getConfig(type);
        try {
            List<File> validSitemaps = validateSitemap(config, service.generateSitemap(config));
            service.generateSitemapIndex(config, validSitemaps);
            if (validateSiteIndex(config)) {
                sitemaps = validSitemaps;
            }
        } catch (MalformedURLException e) {
            LOGGER.error(MessageFormat.format("Failed to generate sitemap {0} for {1} store", config.getDirectoryPath(), config.getStoreName()), e);
        }
        return sitemaps;
    }

    private boolean validateSiteIndex(SitemapConfiguration config) {
        boolean valid = true;
        try {
            if (config.isIndexEnabled()) {
                service.validateSitemapIndex(config);
            }
        } catch (SAXException e) {
            valid = false;
            LOGGER.error(MessageFormat.format("Failed to validate sitemap index {0} for {1} store", config.getDirectoryPath(), config.getStoreName()), e);
        }
        return valid;
    }

    private List<File> validateSitemap(SitemapConfiguration config, List<File> sitemaps) {
        List<File> validSitemaps = new ArrayList<File>();
        for (File sitemap : sitemaps) {
            try {
                service.validateSitemap(sitemap);
                validSitemaps.add(sitemap);
            } catch (SAXException e) {
                LOGGER.error(MessageFormat.format("Failed to validate sitemap {0} for {1} store", config.getDirectoryPath(), config.getStoreName()), e);
            }
        }
        return validSitemaps;
    }

}
