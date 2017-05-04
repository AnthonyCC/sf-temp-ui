package com.freshdirect.fdstore.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import com.freshdirect.fdstore.sitemap.SitemapGenerator;
import com.freshdirect.fdstore.sitemap.SitemapService;
import com.freshdirect.fdstore.sitemap.SitemapTypeEnum;
import com.freshdirect.fdstore.sitemap.config.SitemapConfigStrategy;
import com.freshdirect.fdstore.sitemap.config.SitemapConfiguration;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class SitemapGeneratorTest {

    private static final SitemapTypeEnum SITEMAP_TYPE_MAIN = SitemapTypeEnum.MAIN;

    @Mock
    private SitemapService service;
    @Mock
    private SitemapConfigStrategy configStrategy;

    private SitemapGenerator generator;

    @Before
    public void setUp() {
        generator = new SitemapGenerator(service, configStrategy);
    }

    @Test
    public void generateNoSitemap() throws MalformedURLException {
        List<File> mockedSitemaps = Collections.emptyList();
        SitemapConfiguration mockedSitemapConfig = new SitemapConfiguration();
        Mockito.when(configStrategy.getConfig(SITEMAP_TYPE_MAIN)).thenReturn(mockedSitemapConfig);
        Mockito.when(service.generateSitemap(mockedSitemapConfig)).thenReturn(mockedSitemaps);

        List<File> sitemaps = generator.generate(SITEMAP_TYPE_MAIN);

        Assert.assertEquals(mockedSitemaps, sitemaps);
    }

    @Test
    public void generateAndNoValidateSitemap() throws MalformedURLException, SAXException {
        List<File> mockedSitemaps = Arrays.asList(new File(""));
        SitemapConfiguration mockedSitemapConfig = new SitemapConfiguration();
        Mockito.when(configStrategy.getConfig(SITEMAP_TYPE_MAIN)).thenReturn(mockedSitemapConfig);
        Mockito.when(service.generateSitemap(mockedSitemapConfig)).thenReturn(mockedSitemaps);

        List<File> sitemaps = generator.generate(SITEMAP_TYPE_MAIN);

        Assert.assertEquals(mockedSitemaps, sitemaps);
        Mockito.verify(service, Mockito.times(1)).validateSitemap(mockedSitemaps.get(0));
        Mockito.verify(service, Mockito.times(0)).validateSitemapIndex(mockedSitemapConfig);
    }

    @Test
    public void generateAndValidateSitemap() throws MalformedURLException, SAXException {
        List<File> mockedSitemaps = Arrays.asList(new File(""));
        SitemapConfiguration mockedSitemapConfig = new SitemapConfiguration().setIndexEnabled(true);
        Mockito.when(configStrategy.getConfig(SITEMAP_TYPE_MAIN)).thenReturn(mockedSitemapConfig);
        Mockito.when(service.generateSitemap(mockedSitemapConfig)).thenReturn(mockedSitemaps);

        List<File> sitemaps = generator.generate(SITEMAP_TYPE_MAIN);

        Assert.assertEquals(mockedSitemaps, sitemaps);
        Mockito.verify(service, Mockito.times(1)).validateSitemap(mockedSitemaps.get(0));
        Mockito.verify(service, Mockito.times(1)).validateSitemapIndex(mockedSitemapConfig);
    }

    @Test
    public void generateSitemapThrowException() throws MalformedURLException {
        SitemapConfiguration mockedSitemapConfig = new SitemapConfiguration();
        Mockito.when(configStrategy.getConfig(SITEMAP_TYPE_MAIN)).thenReturn(mockedSitemapConfig);
        Mockito.when(service.generateSitemap(mockedSitemapConfig)).thenThrow(new MalformedURLException());

        List<File> sitemaps = generator.generate(SITEMAP_TYPE_MAIN);
        Assert.assertTrue(sitemaps.isEmpty());
    }

    @Test
    public void generatedAndValidateSitemapThrowException() throws MalformedURLException, SAXException {
        List<File> mockedSitemaps = Arrays.asList(new File("test1"), new File("test2"));
        SitemapConfiguration mockedSitemapConfig = new SitemapConfiguration();
        Mockito.when(configStrategy.getConfig(SITEMAP_TYPE_MAIN)).thenReturn(mockedSitemapConfig);
        Mockito.when(service.generateSitemap(mockedSitemapConfig)).thenReturn(mockedSitemaps);
        Mockito.doThrow(new SAXException()).when(service).validateSitemap(mockedSitemaps.get(0));

        List<File> sitemaps = generator.generate(SITEMAP_TYPE_MAIN);
        Assert.assertEquals(1, sitemaps.size());
        Assert.assertEquals("test2", sitemaps.get(0).getName());
    }

    @Test
    public void generateAllSitemaps() throws MalformedURLException {
        SitemapConfiguration mockedSitemapConfig = new SitemapConfiguration();
        Mockito.when(configStrategy.getConfig(SitemapTypeEnum.MAIN)).thenReturn(mockedSitemapConfig);
        Mockito.when(configStrategy.getConfig(SitemapTypeEnum.CATEGORY)).thenReturn(mockedSitemapConfig);
        Mockito.when(configStrategy.getConfig(SitemapTypeEnum.PRODUCT)).thenReturn(mockedSitemapConfig);
        
        generator.generateAllTypes();

        Mockito.verify(configStrategy).getConfig(SitemapTypeEnum.MAIN);
        Mockito.verify(configStrategy).getConfig(SitemapTypeEnum.CATEGORY);
        Mockito.verify(configStrategy).getConfig(SitemapTypeEnum.PRODUCT);
    }

}
