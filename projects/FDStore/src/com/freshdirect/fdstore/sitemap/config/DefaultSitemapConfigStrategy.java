package com.freshdirect.fdstore.sitemap.config;

import com.freshdirect.fdstore.sitemap.SitemapTypeEnum;

public class DefaultSitemapConfigStrategy implements SitemapConfigStrategy{

    @Override
    public SitemapConfiguration getConfig(SitemapTypeEnum type) {
        return new SitemapConfiguration();
    }

}
