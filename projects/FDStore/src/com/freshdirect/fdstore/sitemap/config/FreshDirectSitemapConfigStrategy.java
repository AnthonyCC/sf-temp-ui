package com.freshdirect.fdstore.sitemap.config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.sitemap.SitemapTypeEnum;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class FreshDirectSitemapConfigStrategy implements SitemapConfigStrategy {

    private static final boolean SITEMAP_GZIP = true;
    private static final String MAIN_NAME_PREFIX = "sitemap";
    private static final String CATEGORY_NAME_PREFIX = "sitemap_category";
    private static final String PRODUCT_NAME_PREFIX = "sitemap_product";
    private static final String STORE_CONTENT_ID = EnumEStoreId.FD.getContentId();

    private SitemapCmsPopulator populator;
    private FreshDirectSitemapProperties properties;

    public FreshDirectSitemapConfigStrategy(SitemapCmsPopulator populator) {
        this.populator = populator;
        this.properties = FreshDirectSitemapProperties.getInstance();
    }

    @Override
    public SitemapConfiguration getConfig(SitemapTypeEnum type) {
        SitemapConfiguration config = null;
        switch (type) {
            case MAIN:
                config = new SitemapConfiguration().setStoreName(STORE_CONTENT_ID).setDirectoryPath(properties.getDirectoryPath()).setNamePrefix(MAIN_NAME_PREFIX)
                        .setBasePath(properties.getBasePath()).setBasePathPostfix(properties.getBasePathPostfix()).setContextPath(properties.getContextPath())
                        .setGzip(SITEMAP_GZIP).setIndexEnabled(false);

                config.addUrlConfig(new SitemapUrlConfiguration().addUrlPaths(properties.getMainContextPaths()).setLastModification(new Date())
                        .setPriority(SitemapPriorityEnum.HIGHEST.getPriority()));

                break;

            case CATEGORY:
                config = new SitemapConfiguration().setStoreName(STORE_CONTENT_ID).setDirectoryPath(properties.getDirectoryPath()).setNamePrefix(CATEGORY_NAME_PREFIX)
                        .setBasePath(properties.getBasePath()).setBasePathPostfix(properties.getBasePathPostfix()).setContextPath(properties.getContextPath())
                        .setGzip(SITEMAP_GZIP).setIndexEnabled(true);

                for (Map.Entry<String, List<ContentKey>> entry : populator.getCategoriesByDepartment().entrySet()) {
                    config.addUrlConfig(new SitemapUrlConfiguration().addUrlPaths(translateNodeToUrl(entry.getValue())).setLastModification(new Date())
                            .setPriority(SitemapPriorityEnum.HIGHEST.getPriority()).setNamePrefix(entry.getKey()));
                }

                break;

            case PRODUCT:
                config = new SitemapConfiguration().setStoreName(STORE_CONTENT_ID).setDirectoryPath(properties.getDirectoryPath()).setNamePrefix(PRODUCT_NAME_PREFIX)
                        .setBasePath(properties.getBasePath()).setBasePathPostfix(properties.getBasePathPostfix()).setContextPath(properties.getContextPath())
                        .setGzip(SITEMAP_GZIP).setIndexEnabled(true);

                for (Map.Entry<String, List<ContentKey>> entry : populator.getProductsByDepartment().entrySet()) {
                    config.addUrlConfig(new SitemapUrlConfiguration().addUrlPaths(translateNodeToUrl(entry.getValue())).setLastModification(new Date())
                            .setPriority(SitemapPriorityEnum.MIDDLE.getPriority()).setNamePrefix(entry.getKey()));
                }

                break;

            default:
                config = new SitemapConfiguration();
                break;
        }

        return config;
    }

    private List<String> translateNodeToUrl(List<ContentKey> contentKeys) {
        List<String> urls = new ArrayList<String>(contentKeys.size());
        for (ContentKey key : contentKeys) {
            urls.add(configureContextPath(key));
        }
        return urls;
    }

    private String configureContextPath(ContentKey key) {
        ContentType type = key.getType();
        String id = key.getId();
        String path = null;
        if ((FDContentTypes.SUPER_DEPARTMENT.equals(type) || FDContentTypes.DEPARTMENT.equals(type))) {
            path = MessageFormat.format(properties.getDepartmentContextPathTemplate(), id);
        } else if (FDContentTypes.CATEGORY.equals(type)) {
            path = MessageFormat.format(properties.getBrowseContextPathTemplate(), id);
        } else {
            ContentKey categoryKey = populator.getPrimaryHomeKey(key);
            path = MessageFormat.format(properties.getProductContextPathTemplate(), id, categoryKey.getId());
        }
        return path;
    }

}
