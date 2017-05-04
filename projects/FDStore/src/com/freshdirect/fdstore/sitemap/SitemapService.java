package com.freshdirect.fdstore.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.SAXException;

import com.freshdirect.fdstore.sitemap.config.SitemapConfiguration;
import com.freshdirect.fdstore.sitemap.config.SitemapUrlConfiguration;
import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.SitemapIndexUrl;
import com.redfin.sitemapgenerator.SitemapValidator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

public class SitemapService {

    private static final String URL_TEMPLATE = "{0}/{1}";
    private static final String INDEX_TEMPLATE = "{0}/{1}_index.xml";

    private static final SitemapService INSTANCE = new SitemapService();

    private SitemapService() {
    }

    public static SitemapService defaultService() {
        return INSTANCE;
    }

    public List<File> generateSitemap(SitemapConfiguration config) throws MalformedURLException {
        List<File> files = new ArrayList<File>();
        File baseDir = new File(config.getDirectoryPath());
        if (!baseDir.exists()){
            baseDir.mkdir();
        }
        for (SitemapUrlConfiguration urlConfig : config.getUrlConfigs()) {
            if (!urlConfig.getUrlPaths().isEmpty()) {
                String fullBasePath = decorateBasePath(config.getBasePath(), config.getBasePathPostfix());
                WebSitemapGenerator generator = WebSitemapGenerator.builder(fullBasePath, baseDir).gzip(config.isGzip())
                        .fileNamePrefix(decorateNamePrefix(config.getNamePrefix(), urlConfig.getNamePrefix())).build();
                generator.addUrls(generateSiteUrls(fullBasePath, urlConfig));
                files.addAll(generator.write());
            }
        }
        return files;
    }

    private String decorateBasePath(String basePath, String basePathPostfix) {
        StringBuilder fullBasePath = new StringBuilder(basePath);
        if (basePathPostfix != null){
            fullBasePath.append(basePathPostfix);
        }
        return fullBasePath.toString();
    }
    
    private String decorateNamePrefix(String namePrefix, String urlNamePrefix) {
        StringBuilder fullNamePrefix = new StringBuilder(namePrefix);
        if (urlNamePrefix != null) {
            fullNamePrefix.append("_").append(urlNamePrefix);
        }
        return fullNamePrefix.toString();
    }

    private List<WebSitemapUrl> generateSiteUrls(String basePath, SitemapUrlConfiguration urlConfig) throws MalformedURLException {
        List<WebSitemapUrl> siteUrls = new ArrayList<WebSitemapUrl>();
        for (String urlPath : urlConfig.getUrlPaths()) {
            siteUrls.add(new WebSitemapUrl.Options(basePath + urlPath).lastMod(urlConfig.getLastModification()).priority(urlConfig.getPriority()).build());
        }
        return siteUrls;
    }

    public void generateSitemapIndex(SitemapConfiguration config, List<File> sitemaps) throws MalformedURLException {
        if (!sitemaps.isEmpty() && config.isIndexEnabled()) {
            SitemapIndexGenerator generator = new SitemapIndexGenerator(config.getBasePath(), createIndexFile(config));
            generator.addUrls(generateIndexUrls(config, sitemaps));
            generator.write();
        }
    }

    private File createIndexFile(SitemapConfiguration config) {
        return new File(MessageFormat.format(INDEX_TEMPLATE, config.getDirectoryPath(), config.getNamePrefix()));
    }

    private List<SitemapIndexUrl> generateIndexUrls(SitemapConfiguration config, List<File> sitemaps) throws MalformedURLException {
        List<SitemapIndexUrl> indexUrls = new ArrayList<SitemapIndexUrl>();
        Date now = new Date();
        for (File sitemap : sitemaps) {
            indexUrls.add(new SitemapIndexUrl(MessageFormat.format(URL_TEMPLATE, config.getBasePath(), sitemap.getName()), now));
        }
        return indexUrls;
    }

    public void validateSitemap(File path) throws SAXException {
        SitemapValidator.validateWebSitemap(path);
    }

    public void validateSitemapIndex(File path) throws SAXException {
        SitemapValidator.validateSitemapIndex(path);
    }

    public void validateSitemapIndex(SitemapConfiguration config) throws SAXException {
        validateSitemapIndex(createIndexFile(config));
    }

}
