package com.freshdirect.fdstore.sitemap.config;

import com.freshdirect.fdstore.sitemap.SitemapTypeEnum;

public interface SitemapConfigStrategy {

    SitemapConfiguration getConfig(SitemapTypeEnum type);
}
