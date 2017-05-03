package com.freshdirect.fdstore.sitemap.config;

import java.util.ArrayList;
import java.util.List;

public class SitemapConfiguration {

    private String storeName;
    private String directoryPath;
    private String namePrefix;
    private String basePath;
    private String basePathPostfix;
    private boolean gzip;
    private boolean indexEnable;
    private List<SitemapUrlConfiguration> sitemapUrlConfigs;

    public SitemapConfiguration() {
        this.sitemapUrlConfigs = new ArrayList<SitemapUrlConfiguration>();
    }

    public String getStoreName() {
        return storeName;
    }

    public SitemapConfiguration setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public String getBasePath() {
        return basePath;
    }

    public SitemapConfiguration setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }
    
    public String getBasePathPostfix() {
        return basePathPostfix;
    }

    public SitemapConfiguration setBasePathPostfix(String basePathPostfix) {
        this.basePathPostfix = basePathPostfix;
        return this;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public SitemapConfiguration setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public boolean isGzip() {
        return gzip;
    }

    public SitemapConfiguration setGzip(boolean gzip) {
        this.gzip = gzip;
        return this;
    }

    public void addUrlConfig(SitemapUrlConfiguration config) {
        sitemapUrlConfigs.add(config);
    }

    public void addUrlConfigs(List<SitemapUrlConfiguration> configs) {
        sitemapUrlConfigs.addAll(configs);
    }

    public List<SitemapUrlConfiguration> getUrlConfigs() {
        return sitemapUrlConfigs;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public SitemapConfiguration setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    public boolean isIndexEnabled() {
        return indexEnable;
    }

    public SitemapConfiguration setIndexEnabled(boolean indexEnable) {
        this.indexEnable = indexEnable;
        return this;
    }

}
