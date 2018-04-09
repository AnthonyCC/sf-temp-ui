package com.freshdirect.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.cache.EhCacheUtil;
import com.freshdirect.cms.changecontrol.service.ContentChangeControlService;
import com.freshdirect.cms.contentio.xml.FlexContentHandler;
import com.freshdirect.cms.contentio.xml.XmlContentMetadataService;
import com.freshdirect.cms.contentvalidation.service.ContentValidatorService;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.core.service.UniqueContentKeyGeneratorService;
import com.freshdirect.cms.draft.merge.service.DraftMergeService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.draft.validation.service.DraftValidatorService;
import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.lucene.service.LuceneManager;
import com.freshdirect.cms.media.converter.MediaToAttributeConverter;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.properties.service.PropertyResolverService;

@Component
public class CmsServiceLocator implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsServiceLocator.class);

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

        CmsServiceLocator.applicationContext = applicationContext;
    }

    public static ContextualContentProvider contentProviderService() {
        return applicationContext.getBean(ContextualContentProvider.class);
    }

    public static MediaService mediaService() {
        return applicationContext.getBean(MediaService.class);
    }

    public static DraftContextHolder getDraftContextHolder() {
        return applicationContext.getBean(DraftContextHolder.class);
    }

    public static ContentValidatorService getContentValidatorService() {
        return applicationContext.getBean(ContentValidatorService.class);
    }

    public static DraftValidatorService getDraftValidatorService() {
        return applicationContext.getBean(DraftValidatorService.class);
    }

    public static DraftMergeService getDraftMergeService() {
        return applicationContext.getBean(DraftMergeService.class);
    }

    public static ERPSDataService erpsDataService() {
        return applicationContext.getBean(ERPSDataService.class);
    }

    public static ContentTypeInfoService contentTypeInfoService() {
        return applicationContext.getBean(ContentTypeInfoService.class);
    }

    public static PropertyResolverService propertyResolverService() {
        return applicationContext.getBean(PropertyResolverService.class);
    }

    public static ContentChangeControlService contentChangeControlService() {
        return applicationContext.getBean(ContentChangeControlService.class);
    }

    public static LuceneManager luceneManager() {
        return applicationContext.getBean(LuceneManager.class);
    }

    public static ContextService contextService() {
        return applicationContext.getBean(ContextService.class);
    }

    public static IndexerConfiguration indexerConfiguration() {
        return applicationContext.getBean(IndexerConfiguration.class);
    }

    public static FlexContentHandler flexContentHandler() {
        return applicationContext.getBean(FlexContentHandler.class);
    }

    public static UniqueContentKeyGeneratorService uniqueContentKeyGeneratorService() {
        return applicationContext.getBean(UniqueContentKeyGeneratorService.class);
    }

    public static DraftService draftService() {
        return applicationContext.getBean(DraftService.class);
    }

    public static DraftContextHolder draftContextHolder() {
        return applicationContext.getBean(DraftContextHolder.class);
    }

    public static MediaToAttributeConverter mediaToAttributeConverter() {
        return applicationContext.getBean(MediaToAttributeConverter.class);
    }

    /**
     * The returned service CAN be NULL!
     *
     * @return XmlContentMetadataService if the cms's data source is xml, null otherwise
     */
    public static XmlContentMetadataService xmlContentMetadataService() {
        XmlContentMetadataService xmlContentMetadataService = null;
        try {
            xmlContentMetadataService = applicationContext.getBean(XmlContentMetadataService.class);
        } catch (NoSuchBeanDefinitionException e) {
            xmlContentMetadataService = null;
        }
        return xmlContentMetadataService;
    }

    public static CacheManager cacheManager() {
        return applicationContext.getBean(CacheManager.class);
    }

    public static EhCacheUtil ehCacheUtil() {
        return applicationContext.getBean(EhCacheUtil.class);
    }
}
