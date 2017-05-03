package com.freshdirect.fdstore.sitemap.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SitemapUrlConfiguration {

    private String namePrefix;
    private Double priority;
    private Date lastModification;
    private List<String> urlPaths;

    public SitemapUrlConfiguration() {
        this.urlPaths = new ArrayList<String>();
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public SitemapUrlConfiguration setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public SitemapUrlConfiguration setLastModification(Date lastModification) {
        this.lastModification = lastModification;
        return this;
    }

    public List<String> getUrlPaths() {
        return urlPaths;
    }

    public SitemapUrlConfiguration addUrlPath(String urlPath) {
        this.urlPaths.add(urlPath);
        return this;
    }

    public SitemapUrlConfiguration addUrlPaths(List<String> urlPaths) {
        this.urlPaths.addAll(urlPaths);
        return this;
    }

    public Double getPriority() {
        return priority;
    }

    public SitemapUrlConfiguration setPriority(Double priority) {
        this.priority = priority;
        return this;
    }

}
