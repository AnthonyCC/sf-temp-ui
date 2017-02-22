package com.freshdirect.cms.publish.flow.tasks;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.SingleStoreNodeCollectionSource;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Input;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.flow.TransformerTask;
import com.freshdirect.cms.publish.service.StoreFilterService;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;
import com.freshdirect.fdstore.EnumEStoreId;

/**
 * This task is responsible to perform parallel per-store publish.
 *
 * @author segabor
 *
 */
public final class ParallelStorePublishTask extends TransformerTask<Input, Boolean> {

    private static final Logger LOGGER = Logger.getLogger(ParallelStorePublishTask.class);

    private PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    private final String publishBasePath;

    /**
     * Default constructor.
     *
     * @param publishId
     * @param phase
     * @param input
     * @param publishBasePath
     */
    public ParallelStorePublishTask(String publishId, Phase phase, Input input, String publishBasePath) {
        super(publishId, phase, input);

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
        final Set<ContentNodeI> storeNodes = findAllStoreNodes();

        final AtomicInteger failureCounter = new AtomicInteger(0);

        final ExecutorService pool = Executors.newFixedThreadPool(storeNodes.size());

        // kick off-per store publish
        for (final ContentNodeI storeNode : storeNodes) {

            pool.execute(new Runnable() {

                @Override
                public void run() {
                    final ContentKey storeKey = storeNode.getKey();
                    final String storePublishPath = publishBasePath + "/" + storeKey.getId();

                    publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Starting publish", storeKey.getId(), ParallelStorePublishTask.class.getSimpleName()));

                    final Collection<ContentNodeI> singleStoreNodes = getSingleStoreContent(storeKey);

                    // create store publish folder
                    final File storePublishDirectory = new File(storePublishPath);
                    if (!storePublishDirectory.exists()) {
                        storePublishDirectory.mkdirs();
                    }

                    // Write-out phase
                    performPublishStoreData(storeKey, storePublishPath, singleStoreNodes);

                    performPublishMediaData(storeKey, storePublishPath);

                    performContentIndexing(storeKey, storePublishPath);
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
        } catch (CmsRuntimeException e) {
            LOGGER.error("Store publish execution failed due to error", e);
            throw e;
        } catch (InterruptedException e) {
            LOGGER.error("Store publish execution aborted due to error", e);
            throw new CmsRuntimeException(e);
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
    private Set<ContentNodeI> findAllStoreNodes() {
        Set<ContentNodeI> storeNodes = new HashSet<ContentNodeI>();
        for (ContentNodeI node : input.getContentNodes()) {
            if (FDContentTypes.STORE.equals(node.getKey().getType())) {
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
    private void performContentIndexing(final ContentKey storeKey, final String storePublishPath) {
        try {
            IndexingTask indexingTask = new IndexingTask(publishId, Phase.INDEXING, storePublishPath, EnumEStoreId.valueOfContentId(storeKey.getId()), input.getContentNodes());
            indexingTask.run();
        } catch (Exception exc) {
            LOGGER.error("Indexing crashed", exc);
            publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.FAILURE, "Indexing failed", storeKey.getId(), ParallelStorePublishTask.class.getSimpleName()));
            throw new CmsRuntimeException(exc);
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
            MediaPublisherTask mediaPublisherTask = new MediaPublisherTask(publishId, Phase.WRITE_OUT, input.getMediaNodes(), storePublishPath,
                    EnumEStoreId.valueOfContentId(storeKey.getId()));
            mediaPublisherTask.run();
        } catch (Exception exc) {
            LOGGER.error("Media XML generator crashed", exc);
            publishMessageLogger.log(publishId,
                    new PublishMessage(PublishMessage.FAILURE, "Media.xml generation failed", storeKey.getId(), ParallelStorePublishTask.class.getSimpleName()));
            throw new CmsRuntimeException(exc);
        }
    }

    /**
     * Write out store content into Store.xml
     *
     * @param storeKey
     * @param storePublishPath
     * @param singleStoreNodes
     */
    private void performPublishStoreData(final ContentKey storeKey, final String storePublishPath, Collection<ContentNodeI> singleStoreNodes) {
        try {
            StorePublisherTask storePublisherTask = new StorePublisherTask(publishId, Phase.WRITE_OUT, singleStoreNodes, storePublishPath,
                    EnumEStoreId.valueOfContentId(storeKey.getId()));
            storePublisherTask.run();
        } catch (Exception exc) {
            LOGGER.error("Store XML generator crashed", exc);
            publishMessageLogger.log(publishId,
                    new PublishMessage(PublishMessage.FAILURE, "Store.xml generation failed", storeKey.getId(), ParallelStorePublishTask.class.getSimpleName()));
            throw new CmsRuntimeException(exc);
        }
    }

    /**
     * Filter down multi-store content into single store variant This operation also involves primary home filtering and fixing.
     *
     * @param storeKey
     * @return
     *
     * @see StoreFilterService
     */
    private Collection<ContentNodeI> getSingleStoreContent(final ContentKey storeKey) {
        // filter nodes down to single-store
        SingleStoreNodeCollectionSource source = new SingleStoreNodeCollectionSource(input.getContentNodes(), storeKey);

        Collection<ContentNodeI> singleStoreNodes = StoreFilterService.defaultService().filterContentNodes(storeKey, input.getContentNodes(), source, DraftContext.MAIN);
        return singleStoreNodes;
    }
}
