package com.freshdirect.fdstore.sitemap.config;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.fdstore.sitemap.SitemapTypeEnum;

@RunWith(MockitoJUnitRunner.class)
public class FoodKickSitemapConfigStrategyTest {

    @Mock
    private SitemapCmsPopulator populator;
    private SitemapConfigStrategy strategy;

    @Before
    public void setUp() {
        strategy = new FoodKickSitemapConfigStrategy(populator);
    }

    @Test
    public void getMainSitemapConfig() {
        SitemapConfiguration config = strategy.getConfig(SitemapTypeEnum.MAIN);
        assertEquals("https://www.foodkick.com", config.getBasePath());
        assertEquals("/#!", config.getBasePathPostfix());
        assertEquals("/var/tmp/sitemap", config.getDirectoryPath());
        assertEquals("sitemap", config.getNamePrefix());
        assertEquals("FDX", config.getStoreName());
        assertEquals(1, config.getUrlConfigs().size());
    }

    @Test
    public void getCategoryFoodKickSitemapConfig() {
        SitemapConfiguration config = strategy.getConfig(SitemapTypeEnum.CATEGORY);

        assertEquals("https://www.foodkick.com", config.getBasePath());
        assertEquals("/#!", config.getBasePathPostfix());
        assertEquals("/var/tmp/sitemap", config.getDirectoryPath());
        assertEquals("sitemap_category", config.getNamePrefix());
        assertEquals("FDX", config.getStoreName());
        assertEquals(0, config.getUrlConfigs().size());

        Mockito.verify(populator).getCategoriesByDepartment();
    }

    @Test
    public void getProductFoodKickSitemapConfig() {
        SitemapConfiguration config = strategy.getConfig(SitemapTypeEnum.PRODUCT);

        assertEquals("https://www.foodkick.com", config.getBasePath());
        assertEquals("/#!", config.getBasePathPostfix());
        assertEquals("/var/tmp/sitemap", config.getDirectoryPath());
        assertEquals("sitemap_product", config.getNamePrefix());
        assertEquals("FDX", config.getStoreName());
        assertEquals(0, config.getUrlConfigs().size());

        Mockito.verify(populator).getProductsByDepartment();
    }
}
