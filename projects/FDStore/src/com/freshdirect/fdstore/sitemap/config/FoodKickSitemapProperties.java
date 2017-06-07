package com.freshdirect.fdstore.sitemap.config;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FoodKickSitemapProperties {

    private static final Category LOGGER = LoggerFactory.getInstance(FoodKickSitemapProperties.class);

    private static final long REFRESH_PERIOD = 5 * 60 * 1000;
    private static final Properties DEFAULTS = new Properties();
    private static final String PROPERTIES_FILE_NAME = "sitemap_fdx.properties";
    private static final String MAIN_CONTEXT_PATHS = "sitemap.main.context.paths";
    private static final String PRODUCT_CONTEXT_PATH_TEMPLATE = "sitemap.product.context.path";
    private static final String BROWSE_CONTEXT_PATH_TEMPLATE = "sitemap.browse.context.path";
    private static final String DEPARTMENT_CONTEXT_PATH_TEMPLATE = "sitemap.department.context.path";
    private static final String BASE_PATH = "sitemap.base.path";
    private static final String BASE_PATH_POSTFIX = "sitemap.base.path.postfix";
    private static final String CONTEXT_PATH = "sitemap.context.path";
    private static final String DIRECTORY_PATH = "sitemap.directory.path";

    private static final FoodKickSitemapProperties INSTANCE = new FoodKickSitemapProperties();

    private long lastRefresh = 0;
    private Properties config;

    static {
        DEFAULTS.put(MAIN_CONTEXT_PATHS,
                "/,/help,/help/aboutus/,/help/faq/,/help/faq/iphonegeneral,/help/faq/iphonemanageaccount,/help/faq/iphonebilling,/help/faq/iphonedelivery,/help/legal/,/help/legal/agreement,/help/legal/privacy,/help/legal/platformterms,/help/smstermsofuse/,/help/foodSafety/,/help/foodSafety/prodRecall,/help/foodSafety/cookStorage,/help/foodSafety/foodSafety");
        DEFAULTS.put(PRODUCT_CONTEXT_PATH_TEMPLATE, "/product/{0}");
        DEFAULTS.put(BROWSE_CONTEXT_PATH_TEMPLATE, "/browse/{0}");
        DEFAULTS.put(DEPARTMENT_CONTEXT_PATH_TEMPLATE, "/department/{0}");
        DEFAULTS.put(BASE_PATH, "https://www.foodkick.com");
        DEFAULTS.put(BASE_PATH_POSTFIX, "/#!");
        DEFAULTS.put(CONTEXT_PATH, "/sitemap");
        DEFAULTS.put(DIRECTORY_PATH, "/var/tmp/sitemap");
    }

    public static FoodKickSitemapProperties getInstance() {
        return INSTANCE;
    }

    private FoodKickSitemapProperties() {
    }

    private void refresh() {
        refresh(false);
    }

    private synchronized void refresh(boolean force) {
        long t = System.currentTimeMillis();
        if (force || (t - lastRefresh > REFRESH_PERIOD)) {
            config = ConfigHelper.getPropertiesFromClassLoader(PROPERTIES_FILE_NAME, DEFAULTS);
            lastRefresh = t;
            LOGGER.info(MessageFormat.format("Loaded configuration from {0}: {1}", PROPERTIES_FILE_NAME, config));
        }
    }

    private String get(String key) {
        refresh();
        return config.getProperty(key);
    }

    public List<String> getMainContextPaths() {
        return Arrays.asList(get(MAIN_CONTEXT_PATHS).split(","));
    }

    public String getProductContextPathTemplate() {
        return get(PRODUCT_CONTEXT_PATH_TEMPLATE);
    }

    public String getBrowseContextPathTemplate() {
        return get(BROWSE_CONTEXT_PATH_TEMPLATE);
    }

    public String getDepartmentContextPathTemplate() {
        return get(DEPARTMENT_CONTEXT_PATH_TEMPLATE);
    }

    public String getBasePath() {
        return get(BASE_PATH);
    }

    public String getBasePathPostfix() {
        return get(BASE_PATH_POSTFIX);
    }

    public String getContextPath() {
        return get(CONTEXT_PATH);
    }

    public String getDirectoryPath() {
        return get(DIRECTORY_PATH);
    }

}
