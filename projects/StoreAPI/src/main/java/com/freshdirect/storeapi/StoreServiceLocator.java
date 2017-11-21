package com.freshdirect.storeapi;

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

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
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
