package com.freshdirect.fdstore.sitemap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.freshdirect.fdstore.sitemap.config.SitemapConfiguration;
import com.freshdirect.fdstore.sitemap.config.SitemapUrlConfiguration;

import junit.framework.Assert;

public class SitemapServiceTest {

    private static final Double HIGH_PRIPORITY = 1.0d;
    private static final Date LAST_MOD_DATE = new Date();
    private static final String STORE_NAME = "FDX";
    private static final String DIRECTORY_PATH = "/var/tmp";
    private static final String SITEMAP_XML = "/sitemap.xml";
    private static final String SITEMAP_XML_GZ = "/sitemap.xml.gz";
    private static final String SITEMAP_INDEX_XML = "/sitemap_index.xml";
    private static final String SITEMAP_PREFIX_XML = "/sitemap_prefix.xml";
    private static final String INDEX_HTML_PATH = "/index.html";
    private static final String SITEMAP_NAME_PREFIX = "sitemap";
    private static final String SITEMAP_BASE_PATH = "http://www.example.com";
    private static final String SITEMAP_CONTEXT_PATH = "/sitemap";

    private SitemapService service;

    @Before
    public void setUp() {
        service = SitemapService.defaultService();
    }

    @After
    public void tearDown() {
        new File(DIRECTORY_PATH + SITEMAP_XML).delete();
        new File(DIRECTORY_PATH + SITEMAP_XML_GZ).delete();
        new File(DIRECTORY_PATH + SITEMAP_INDEX_XML).delete();
        new File(DIRECTORY_PATH + SITEMAP_PREFIX_XML).delete();
    }

    @Test
    public void generateSitemap() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, false, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        List<File> files = service.generateSitemap(sitemapConfig);

        assertEquals(1, files.size());
        assertTrue(new File(DIRECTORY_PATH + SITEMAP_XML).exists());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_XML_GZ).exists());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML).exists());
    }

    @Test
    public void generateSitemapInGzipForm() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        List<File> files = service.generateSitemap(sitemapConfig);

        assertEquals(1, files.size());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_XML).exists());
        assertTrue(new File(DIRECTORY_PATH + SITEMAP_XML_GZ).exists());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML).exists());
    }

    @Test
    public void generateSitemapWithPrefix() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, false, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY, "prefix")));

        List<File> files = service.generateSitemap(sitemapConfig);

        assertEquals(1, files.size());
        assertTrue(new File(DIRECTORY_PATH + SITEMAP_PREFIX_XML).exists());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_XML_GZ).exists());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML).exists());
    }

    @Test(expected = MalformedURLException.class)
    public void generateSitemapThrowException() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, "", SITEMAP_CONTEXT_PATH, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        service.generateSitemap(sitemapConfig);

        Assert.fail();
    }

    @Test
    public void generateSiteIndex() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        service.generateSitemapIndex(sitemapConfig, service.generateSitemap(sitemapConfig));

        assertFalse(new File(DIRECTORY_PATH + SITEMAP_XML).exists());
        assertTrue(new File(DIRECTORY_PATH + SITEMAP_XML_GZ).exists());
        assertTrue(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML).exists());
    }

    @Test
    public void generateNoSiteIndex() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, true, false);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        service.generateSitemapIndex(sitemapConfig, service.generateSitemap(sitemapConfig));

        assertFalse(new File(DIRECTORY_PATH + SITEMAP_XML).exists());
        assertTrue(new File(DIRECTORY_PATH + SITEMAP_XML_GZ).exists());
        assertFalse(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML).exists());
    }

    @Test(expected = MalformedURLException.class)
    public void generateSiteIndexThrowException() throws MalformedURLException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, "", SITEMAP_CONTEXT_PATH, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        service.generateSitemapIndex(sitemapConfig, service.generateSitemap(sitemapConfig));

        Assert.fail();
    }

    @Test
    public void validateSitemap() throws MalformedURLException, SAXException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, false, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));
        List<File> files = service.generateSitemap(sitemapConfig);
        assertEquals(1, files.size());

        service.validateSitemap(files.get(0));
    }

    @Test
    public void validateSitemapInGzipForm() throws MalformedURLException, SAXException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));
        List<File> files = service.generateSitemap(sitemapConfig);
        assertEquals(1, files.size());

        service.validateSitemap(files.get(0));
    }

    @Test
    public void validateSitemapIndex() throws MalformedURLException, SAXException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, SITEMAP_CONTEXT_PATH, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        service.generateSitemapIndex(sitemapConfig, service.generateSitemap(sitemapConfig));
        service.validateSitemapIndex(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML));
    }

    @Test
    public void validateSitemapIndexWithEmptyContextPath() throws MalformedURLException, SAXException {
        SitemapConfiguration sitemapConfig = createSitemapConfiguration(STORE_NAME, DIRECTORY_PATH, SITEMAP_NAME_PREFIX, SITEMAP_BASE_PATH, null, true, true);
        sitemapConfig.addUrlConfigs(Arrays.asList(createSiteUrlConfiguration(Arrays.asList(INDEX_HTML_PATH), LAST_MOD_DATE, HIGH_PRIPORITY)));

        service.generateSitemapIndex(sitemapConfig, service.generateSitemap(sitemapConfig));
        service.validateSitemapIndex(new File(DIRECTORY_PATH + SITEMAP_INDEX_XML));
    }

    private SitemapUrlConfiguration createSiteUrlConfiguration(List<String> urlPaths, Date lastModification, Double priority) {
        return createSiteUrlConfiguration(urlPaths, lastModification, priority, null);
    }

    private SitemapUrlConfiguration createSiteUrlConfiguration(List<String> urlPaths, Date lastModification, Double priority, String namePrefix) {
        return new SitemapUrlConfiguration().addUrlPaths(urlPaths).setLastModification(lastModification).setPriority(priority).setNamePrefix(namePrefix);
    }

    private SitemapConfiguration createSitemapConfiguration(String storeName, String directoryPath, String namePrefix, String basePath, String contextPath, boolean gzip, boolean index) {
        return new SitemapConfiguration().setStoreName(storeName).setDirectoryPath(directoryPath).setNamePrefix(namePrefix).setBasePath(basePath)
                .setContextPath(contextPath).setGzip(gzip).setIndexEnabled(index);
    }

}
