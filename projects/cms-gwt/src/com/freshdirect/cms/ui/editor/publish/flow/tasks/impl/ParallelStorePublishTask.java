package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.EStoreId;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.core.service.NodeCollectionContentProvider;
import com.freshdirect.cms.media.converter.MediaToAttributeConverter;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Input;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Phase;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.TransformerTask;

/**
 * This task is responsible to perform parallel per-store publish.
 *
 * @author segabor
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ParallelStorePublishTask extends TransformerTask<Input, Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelStorePublishTask.class);

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private MediaToAttributeConverter mediaToAttributeConverter;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ApplicationContext applicationContext;

    private String publishBasePath;

    public void setPublishBasePath(String publishBasePath) {
        this.publishBasePath = publishBasePath;
    }

    /**
     * Performs full store publish.
     *
     * @return publishResult false if publish process failed along the way
     */
    @Override
    public Boolean call() throws Exception {
        Boolean publishResult = Boolean.TRUE;

        // Find store nodes (FreshDirect, FDX, ...)
        final Set<ContentKey> storeKeys = findAllStoreNodes();

        final AtomicInteger failureCounter = new AtomicInteger(0);

        final ExecutorService pool = Executors.newFixedThreadPool(storeKeys.size());

        // kick off-per store publish
        for (final ContentKey storeKey : storeKeys) {

            pool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        final String storePublishPath = publishBasePath + "/" + storeKey.id;

                        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Starting publish", storeKey.id, ParallelStorePublishTask.class.getSimpleName()));

                        final Map<ContentKey, Map<Attribute, Object>> singleStoreNodes = getSingleStoreContent(storeKey);

                        // create store publish folder
                        final File storePublishDirectory = new File(storePublishPath);
                        if (!storePublishDirectory.exists()) {
                            storePublishDirectory.mkdirs();
                        }

                        // Write-out phase
                        performPublishStoreData(storeKey, storePublishPath, singleStoreNodes);

                        performPublishMediaData(storeKey, storePublishPath);

                        performContentIndexing(storeKey, storePublishPath, singleStoreNodes);
                        
                    } catch (Exception exc) {
                        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.ERROR, "Parallel Publish task crashed: " + exc.getMessage(), storeKey.id, ParallelStorePublishTask.class.getSimpleName()));

                        LOGGER.error("Parallel Publish task crashed!", exc);
                        failureCounter.incrementAndGet();
                    }
                }
            });

        }

        // wait for termination
        pool.shutdown();
        try {
            while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                if (failureCounter.get() > 0) {
                    LOGGER.error("One of store publish tasks signalled failure, forcing shut down ...");
                    pool.shutdownNow();

                    publishResult = Boolean.FALSE;

                    break;
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error("Store publish task execution failed due to error", e);
            throw e;
        } catch (InterruptedException e) {
            LOGGER.error("Store publish task execution aborted due to error", e);
            throw new RuntimeException(e);
        }

        return publishResult;
    }

    @Override
    public String getName() {
        return "Parallel Store Publisher Task";
    }

    /**
     * Collects all store nodes available in the full content
     *
     * @return Store content nodes
     */
    private Set<ContentKey> findAllStoreNodes() {
        Set<ContentKey> storeNodes = new HashSet<ContentKey>();
        for (ContentKey node : input.getContentNodes().keySet()) {
            if (ContentType.Store.equals(node.type)) {
                storeNodes.add(node);
            }
        }
        return storeNodes;
    }

    /**
     * This particular task executes content indexing
     *
     * @param storeKey
     * @param storePublishPath
     */
    private void performContentIndexing(final ContentKey storeKey, final String storePublishPath, Map<ContentKey, Map<Attribute, Object>> singleStoreNodes) {
        try {
            IndexingTask indexingTask = applicationContext.getBean(IndexingTask.class);
            indexingTask.setPhase(Phase.INDEXING);
            indexingTask.setPublishId(publishId);

            indexingTask.setInput(singleStoreNodes);

            indexingTask.setStoreId(EStoreId.valueOfContentId(storeKey.id));
            indexingTask.setStorePublishPath(storePublishPath);

            indexingTask.run();
        } catch (Exception exc) {
            LOGGER.error("Indexing crashed", exc);
            publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.FAILURE, "Indexing failed", storeKey.id, ParallelStorePublishTask.class.getSimpleName()));
            throw new RuntimeException(exc);
        }
    }

    /**
     * Write out media content into Media.xml
     *
     * @param storeKey
     * @param storePublishPath
     */
    private void performPublishMediaData(final ContentKey storeKey, final String storePublishPath) {
        try {
            MediaPublisherTask mediaPublisherTask = applicationContext.getBean(MediaPublisherTask.class);
            mediaPublisherTask.setPhase(Phase.WRITE_OUT);
            mediaPublisherTask.setPublishId(publishId);

            Map<ContentKey, Map<Attribute, Object>> mediaNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
            for (final Media mediaItem : input.getMediaNodes()) {
                mediaNodes.putAll(mediaToAttributeConverter.convert(mediaItem));
            }

            mediaPublisherTask.setInput(mediaNodes);

            mediaPublisherTask.setStoreId(EStoreId.valueOfContentId(storeKey.id));
            mediaPublisherTask.setTargetPath(storePublishPath);

            mediaPublisherTask.run();
        } catch (Exception exc) {
            LOGGER.error("Media XML generator crashed", exc);
            publishMessageLogger.log(publishId,
                    new StorePublishMessage(StorePublishMessageSeverity.FAILURE, "Media.xml generation failed", storeKey.id, ParallelStorePublishTask.class.getSimpleName()));
            throw new RuntimeException(exc);
        }
    }

    /**
     * Write out store content into Store.xml
     *
     * @param storeKey
     * @param storePublishPath
     * @param singleStoreNodes
     */
    private void performPublishStoreData(final ContentKey storeKey, final String storePublishPath, Map<ContentKey, Map<Attribute, Object>> singleStoreNodes) {
        try {
            StorePublisherTask storePublisherTask = applicationContext.getBean(StorePublisherTask.class); // scope prototype

            storePublisherTask.setPhase(Phase.WRITE_OUT);
            storePublisherTask.setPublishId(publishId);

            storePublisherTask.setInput(singleStoreNodes);

            storePublisherTask.setStoreId(EStoreId.valueOfContentId(storeKey.id));
            storePublisherTask.setTargetPath(storePublishPath);

            storePublisherTask.run();
        } catch (Exception exc) {
            LOGGER.error("Store XML generator crashed", exc);
            publishMessageLogger.log(publishId,
                    new StorePublishMessage(StorePublishMessageSeverity.FAILURE, "Store.xml generation failed", storeKey.id, ParallelStorePublishTask.class.getSimpleName()));
            throw new RuntimeException(exc);
        }
    }

    /**
     * Filter down multi-store content into single store variant This operation also involves primary home filtering and fixing.
     *
     * @param storeKey
     * @return
     */
    private Map<ContentKey, Map<Attribute, Object>> getSingleStoreContent(final ContentKey storeKey) {
        // filter nodes down to single-store
        NodeCollectionContentProvider nodeCollectionContentProviderService =
                new NodeCollectionContentProvider(contentTypeInfoService, contextService, input.getContentNodes());

        return nodeCollectionContentProviderService.filterNodesToStore(storeKey);
    }
}
