package com.freshdirect.cms.ui.editor.publish.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;
import com.freshdirect.cms.ui.editor.publish.flow.PublishFlow;

@Service
public class AsyncStorePublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncStorePublishService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${cms.publish.basePath}")
    private String cmsPublishBasePath;

    @Value("${cms.publish.repositoryUrl}")
    private String cmsMediaRepositoryURL;

    @Value("${cms.publish.script}")
    private String runScriptPath;

    @Async
    public void startStorePublishFlowAsync(StorePublish publish) throws InterruptedException {
        Assert.notNull(publish);

        final Long publishId = publish.getId();

        LOGGER.info("** Store Publish Flow " + publishId + " started! **");

        PublishFlow publishFlow = applicationContext.getBean(PublishFlow.class);

        try {
            final String publishPath = new File(cmsPublishBasePath, publishId.toString()).toString();

            publishFlow.doPublish(publishId, publishPath, cmsMediaRepositoryURL, runScriptPath);

            LOGGER.info("** Publish flow " + publishId + " successfully ended **");
        } catch (Exception e) {
            LOGGER.error("** Publish flow " + publishId + " stopped due to an error **", e);
        }
    }

}
