package com.freshdirect.storeapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.freshdirect.storeapi.content.ContentSearch;
import com.freshdirect.storeapi.rules.RulesPublisher;
import com.freshdirect.storeapi.rules.RulesRegistry;
import com.freshdirect.storeapi.search.service.TermTokenizer;
import com.freshdirect.storeapi.smartstore.CmsRecommenderService;

@Component
public class StoreServiceLocator implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceLocator.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (applicationContext != null) {
            LOGGER.info("== Setting application context ==");
            LOGGER.info("  app name: " + applicationContext.getApplicationName() );
            LOGGER.info("  ID: " + applicationContext.getId() );
            LOGGER.info("  display name: " + applicationContext.getDisplayName() );

            ApplicationContext parentContext = applicationContext.getParent();
            LOGGER.info("  has parent context? " + Boolean.valueOf(parentContext != null) );
            if (parentContext != null) {
                LOGGER.info("  parent app name: " + parentContext.getApplicationName() );
                LOGGER.info("  parent ID: " + parentContext.getId() );
                LOGGER.info("  parent display name: " + parentContext.getDisplayName() );
            }
            LOGGER.info("== == == == == == == == == ==");
        } else {
            LOGGER.error("received null context, expect future crashes!");
        }

        StoreServiceLocator.applicationContext = applicationContext;
    }

    public static RulesRegistry rulesRegistry() {
        return applicationContext.getBean(RulesRegistry.class);
    }

    public static RulesPublisher rulesPublisher() {
        return applicationContext.getBean(RulesPublisher.class);
    }

    public static ContentSearch contentSearch() {
        return applicationContext.getBean(ContentSearch.class);
    }

    public static PreviewLinkProvider previewLinkProvider() {
        return applicationContext.getBean(PreviewLinkProvider.class);
    }

    public static CmsRecommenderService recommenderService() {
        return applicationContext.getBean(CmsRecommenderService.class);
    }

    public static TermTokenizer termTokenizer() {
        return applicationContext.getBean(TermTokenizer.class);
    }
}
