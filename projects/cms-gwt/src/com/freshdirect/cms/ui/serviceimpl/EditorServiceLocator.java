package com.freshdirect.cms.ui.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.editor.bulkloader.service.ContentBulkLoaderService;
import com.freshdirect.cms.ui.editor.permission.service.PermissionService;
import com.freshdirect.cms.ui.editor.permission.service.PersonaService;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishToGwtPublishDataConverter;
import com.freshdirect.cms.ui.editor.publish.feed.service.FeedPublishService;
import com.freshdirect.cms.ui.editor.publish.service.StorePublishService;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;
import com.freshdirect.cms.ui.editor.service.ContentChangesService;
import com.freshdirect.cms.ui.editor.service.ContentLoaderService;
import com.freshdirect.cms.ui.editor.service.ContentUpdateService;
import com.freshdirect.cms.ui.editor.service.IndexingService;
import com.freshdirect.cms.ui.editor.service.LabelProviderService;
import com.freshdirect.cms.ui.editor.service.PreviewLinkProvider;
import com.freshdirect.cms.ui.editor.service.PublishService;
import com.freshdirect.cms.ui.editor.service.SearchService;
import com.freshdirect.cms.ui.editor.service.TreeNodeService;

@Component
public class EditorServiceLocator implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditorServiceLocator.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (applicationContext != null) {
            LOGGER.info("== Setting application context ==");
            LOGGER.info("  app name: " + applicationContext.getApplicationName());
            LOGGER.info("  ID: " + applicationContext.getId());
            LOGGER.info("  display name: " + applicationContext.getDisplayName());

            ApplicationContext parentContext = applicationContext.getParent();
            LOGGER.info("  has parent context? " + Boolean.valueOf(parentContext != null));
            if (parentContext != null) {
                LOGGER.info("  parent app name: " + parentContext.getApplicationName());
                LOGGER.info("  parent ID: " + parentContext.getId());
                LOGGER.info("  parent display name: " + parentContext.getDisplayName());
            }
            LOGGER.info("== == == == == == == == == ==");
        } else {
            LOGGER.error("received null context, expect future crashes!");
        }

        EditorServiceLocator.applicationContext = applicationContext;
    }

    public static PublishService publishService() {
        return applicationContext.getBean(PublishService.class);
    }

    public static StorePublishService storePublishService() {
        return applicationContext.getBean(StorePublishService.class);
    }

    public static ContentLoaderService contentLoaderService() {
        return applicationContext.getBean(ContentLoaderService.class);
    }

    public static ContentChangesService contentChangesService() {
        return applicationContext.getBean(ContentChangesService.class);
    }

    public static ContentUpdateService contentUpdateService() {
        return applicationContext.getBean(ContentUpdateService.class);
    }

    public static PermissionService permissionService() {
        return applicationContext.getBean(PermissionService.class);
    }

    public static ContentBulkLoaderService contentBulkLoaderService() {
        return applicationContext.getBean(ContentBulkLoaderService.class);
    }

    public static TreeNodeService treeNodeService() {
        return applicationContext.getBean(TreeNodeService.class);
    }

    public static SearchService searchService() {
        return applicationContext.getBean(SearchService.class);
    }

    public static ReportingService reportingService() {
        return applicationContext.getBean(ReportingService.class);
    }

    public static LabelProviderService labelProviderService() {
        return applicationContext.getBean(LabelProviderService.class);
    }

    public static IndexingService indexingService() {
        return applicationContext.getBean(IndexingService.class);
    }

    public static FeedPublishService feedPublishService() {
        return applicationContext.getBean(FeedPublishService.class);
    }

    public static FeedPublishToGwtPublishDataConverter feedPublishToGwtPublishDataConverter() {
        return applicationContext.getBean(FeedPublishToGwtPublishDataConverter.class);
    }

    public static PreviewLinkProvider previewLinkProvider() {
        return applicationContext.getBean(PreviewLinkProvider.class);
    }

    public static PropertyResolverService propertyResolverService() {
        return applicationContext.getBean(PropertyResolverService.class);
    }

    public static PersonaService personaService() {
        return applicationContext.getBean(PersonaService.class);
    }
}
