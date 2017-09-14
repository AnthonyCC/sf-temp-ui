package com.freshdirect.fdstore.sitemap.config;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FreshDirectSitemapProperties {

    private static final Category LOGGER = LoggerFactory.getInstance(FreshDirectSitemapProperties.class);

    private static final long REFRESH_PERIOD = 5 * 60 * 1000;
    private static final Properties DEFAULTS = new Properties();
    private static final String PROPERTIES_FILE_NAME = "sitemap_fd.properties";
    private static final String MAIN_CONTEXT_PATHS = "sitemap.main.context.paths";
    private static final String PRODUCT_CONTEXT_PATH_TEMPLATE = "sitemap.product.context.path";
    private static final String BROWSE_CONTEXT_PATH_TEMPLATE = "sitemap.browse.context.path";
    private static final String DEPARTMENT_CONTEXT_PATH_TEMPLATE = "sitemap.department.context.path";
    private static final String BASE_PATH = "sitemap.base.path";
    private static final String BASE_PATH_POSTFIX = "sitemap.base.path.postfix";
    private static final String CONTEXT_PATH = "sitemap.context.path";
    private static final String DIRECTORY_PATH = "sitemap.directory.path";

    private static final FreshDirectSitemapProperties INSTANCE = new FreshDirectSitemapProperties();

    private long lastRefresh = 0;
    private Properties config;

    static {
        DEFAULTS.put(MAIN_CONTEXT_PATHS,
                "/,/index.jsp,/index.jsp?serviceType=HOME,/index.jsp?serviceType=CORPORATE,/srch.jsp?pageType=ecoupon,/srch.jsp?pageType=newproducts,/gift_card/purchase/landing.jsp,/gift_card/purchase/add_giftcard.jsp,/gift_card/purchase/tac.jsp,/about/index.jsp,"
                        + "/help/index.jsp,/help/privacy_policy.jsp,/help/terms_of_service.jsp,/help/platform_agreement.jsp,/help/delivery_info.jsp,/help/delivery_info_check_slots.jsp,/help/delivery_zones.jsp,"
                        + "/help/faq_home.jsp?page=faqHome,/help/faq_home.jsp?page=accessibility,/help/faq_home.jsp?page=acct_info,/help/faq_home.jsp?page=cos,/faq_home.jsp?page=chef_table,/faq_home.jsp?page=delivery_feedback,"
                        + "/help/faq_home.jsp?page=delivery_pass,/help/faq_home.jsp?page=cpns,/help/faq_home.jsp?page=gen_feedback,/help/faq_home.jsp?page=home_delivery,/help/faq_home.jsp?page=inside,/help/faq_home.jsp?page=order_change,"
                        + "/help/faq_home.jsp?page=order_today,/help/faq_home.jsp?page=payment,/help/faq_home.jsp?page=prblem_my_order,/help/faq_home.jsp?page=req_feedback,/help/faq_home.jsp?page=promotion,/help/faq_home.jsp?page=refer_a_friend,"
                        + "/help/faq_home.jsp?page=shopping,/help/faq_home.jsp?page=signing_up,/faq_home.jsp?page=delivery_text_messages,/help/faq_home.jsp?page=sustainable_seafood,/help/faq_home.jsp?page=website_technical,/help/faq_home.jsp?page=what_we_do,"
                        + "/help/contact_fd.jsp,/help/delivery_lic_pickup.jsp,/help/delivery_info_cos.jsp,/help/delivery_jersey_shore.jsp,/help/delivery_hamptons.jsp");
        DEFAULTS.put(PRODUCT_CONTEXT_PATH_TEMPLATE, "/pdp.jsp?productId={0}&catId={1}");
        DEFAULTS.put(BROWSE_CONTEXT_PATH_TEMPLATE, "/browse.jsp?id={0}");
        DEFAULTS.put(DEPARTMENT_CONTEXT_PATH_TEMPLATE, "/browse.jsp?id={0}");
        DEFAULTS.put(BASE_PATH, "https://www.freshdirect.com");
        DEFAULTS.put(BASE_PATH_POSTFIX, "");
        DEFAULTS.put(CONTEXT_PATH, "/sitemap");
        DEFAULTS.put(DIRECTORY_PATH, "/var/tmp/sitemap");
    }

    public static FreshDirectSitemapProperties getInstance() {
        return INSTANCE;
    }

    private FreshDirectSitemapProperties() {
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
