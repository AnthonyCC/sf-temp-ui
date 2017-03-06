package com.freshdirect.cms.publish.flow;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.tasks.ContentLoaderTask;
import com.freshdirect.cms.publish.flow.tasks.ContentValidationTask;
import com.freshdirect.cms.publish.flow.tasks.IndexingTask;
import com.freshdirect.cms.publish.flow.tasks.MediaDeltaPublisherTask;
import com.freshdirect.cms.publish.flow.tasks.ParallelStorePublishTask;
import com.freshdirect.cms.publish.flow.tasks.RunScriptTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadContentNodesFromXmlTask;
import com.freshdirect.cms.publish.service.ERPSConfigurationDataCache;
import com.freshdirect.cms.publish.service.PublishStatusService;
import com.freshdirect.cms.publish.service.impl.BasicPublishStatusService;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;
import com.freshdirect.cms.search.BackgroundStatus;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.fdstore.EnumEStoreId;

public final class PublishFlow {

    private static final Logger LOGGER = Logger.getLogger(PublishFlow.class);

    private final PublishStatusService publishStatusService = BasicPublishStatusService.defaultService();

    private final PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    private BackgroundStatus statusDelegate;

    /**
     * Publish ID
     */
    private final String publishId;

    /**
     * Target path of publish materials.
     */
    private final String basePath;

    /**
     * Media Repository URL
     */
    private final String mediaRepositoryURL;

    /**
     * Path to script to be run after successful publish
     */
    private final String runScriptPath;

    public PublishFlow(String publishId, String basePath, String mediaRepositoryURL, String runScriptPath) {
        if (publishId == null) {
            throw new CmsRuntimeException("Publish Id is null");
        } else if (basePath == null) {
            throw new CmsRuntimeException("Publish Base Path is null");
        } else if (mediaRepositoryURL == null) {
            throw new CmsRuntimeException("Media repository URL is null");
        }

        this.publishId = publishId;
        this.basePath = basePath;
        this.mediaRepositoryURL = mediaRepositoryURL;
        this.runScriptPath = runScriptPath;
    }

    public void setStatusDelegate(BackgroundStatus statusDelegate) {
        this.statusDelegate = statusDelegate;
    }

    /**
     * Start the publish process here.
     *
     * @throws Exception
     */
    public void doPublish() throws Exception {

        LOGGER.info("*** STARTING PUBLISH (id: " + publishId + ") ***");
        LOGGER.info("  Publish Base Path: " + basePath);
        LOGGER.info("  Media Repository URL: " + mediaRepositoryURL);

        publishMessageLogger.enableLogging();
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Starting publish", PublishFlow.class.getSimpleName()));

        // INIT PHASE

        Input inputData = executeInitPublish();

        ERPSConfigurationDataCache.sharedInstance().updateData(inputData.getSkuMaterialAssignment(), inputData.getMaterialConfiguration(), inputData.getMaterialSalesUnits());

        // VALIDATION PHASE
        executeValidateContent(inputData);

        // PUBLISH PHASE
        executePublishMediaDelta(inputData);

        boolean storePublishResult = executeStorePublish(inputData);

        if (storePublishResult) {
            // Run Script
            if (runScriptPath != null) {
            	LOGGER.info("  Publish About to execute PostScript: " + runScriptPath);
                executeRunScript();
            }

            finishPublish(true, "Publish finished");
        } else {
            finishPublish(false, "Publish failed");
        }
    }

    private boolean executeStorePublish(Input inputData) throws CmsRuntimeException {
        boolean storePublishResult = false;
        try {
            updatePublishFlowProgress(Phase.STORE_PUBLISH, "Store Publish phase started");

            ParallelStorePublishTask ppt = new ParallelStorePublishTask(publishId, Phase.STORE_PUBLISH, inputData, basePath);
            storePublishResult = ppt.call().booleanValue();

            updatePublishFlowProgress(Phase.STORE_PUBLISH, "Store Publish phase completed");
        } catch (Exception e) {
            handleException(e, Phase.STORE_PUBLISH, "Store Publish failed");
            throw new CmsRuntimeException(e);
        }
        return storePublishResult;
    }

    private void executeRunScript() throws CmsRuntimeException {
        try {
        	//BasePath harcoded to /FDX to handle the "# begin awkward hack so that CMS code doesn't run this script twice" in postPublish.sh
            RunScriptTask runScriptTask = new RunScriptTask(publishId, Phase.POST_OP, basePath + "/FDX" , runScriptPath);
            runScriptTask.run();
        } catch (Exception exc) {
            handleException(exc, Phase.POST_OP, "Run Script task crashed");
            throw new CmsRuntimeException(exc);
        }
    }

    private void executePublishMediaDelta(Input inputData) throws CmsRuntimeException {
        try {
            updatePublishFlowProgress(Phase.WRITE_OUT, "Media delta downloading started");

            MediaDeltaPublisherTask mediaDeltaPublisherTask = new MediaDeltaPublisherTask(publishId, Phase.WRITE_OUT, inputData.getMediaUris(), basePath, mediaRepositoryURL);
            mediaDeltaPublisherTask.run();

            updatePublishFlowProgress(Phase.WRITE_OUT, "Media delta downloading completed");
        } catch (Exception e) {
            handleException(e, Phase.WRITE_OUT, "Media delta downloading failed");
            throw new CmsRuntimeException(e);
        }
    }

    private void executeValidateContent(Input inputData) throws CmsRuntimeException {
        try {
            XmlTypeService typeService = new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml");
            SimpleContentService simpleContentService = new SimpleContentService(typeService, inputData.getContentNodes());
            ContentValidationTask validationTask = new ContentValidationTask(publishId, Phase.VALIDATION, simpleContentService);

            List<ContentValidationMessage> messages = validationTask.call();

            if (!messages.isEmpty()) {
                for (ContentValidationMessage message : messages) {
                    publishMessageLogger.log(publishId,
                            new PublishMessage(PublishMessage.WARNING, message.toString(), message.getContentKey(), ContentValidationTask.class.getSimpleName()));
                }
            }

        } catch (Exception exc) {
            handleException(exc, Phase.VALIDATION, "Content Validation Failed");
            throw new CmsRuntimeException(exc);
        }
    }

    private Input executeInitPublish() throws CmsRuntimeException {
        Input inputData = null;
        try {
            updatePublishFlowProgress(Phase.INIT, "Init phase started");

            ProducerTask<Input> initTask = new ContentLoaderTask(publishId, Phase.INIT);
            inputData = initTask.call();

            updatePublishFlowProgress(Phase.INIT, "Init phase completed");
        } catch (Exception e) {
            handleException(e, Phase.INIT, "Data Load failed");
            throw new CmsRuntimeException(e);
        }
        return inputData;
    }

    /**
     * Standalone indexing. Takes a Store.xml and generates the index files for it.
     *
     * @param storeDefFilePath
     *            path of the CMSStoreDef.xml file
     * @param storeXmlFile
     *            path of the Store.xml.gz file
     * @param storeName
     *            {@link EnumEStoreId}'s contentId
     * @param destination
     *            where to create the index files
     * @throws Exception
     *             if anything goes wrong
     */
    public void doIndexing(String storeDefFilePath, String storeXmlFile, String storeName, String destination) throws Exception {

        EnumEStoreId storeId = EnumEStoreId.valueOfContentId(storeName);

        LoadContentNodesFromXmlTask loadContentNodesFromXmlTask = new LoadContentNodesFromXmlTask("standalone-indexing", Phase.INIT, storeDefFilePath, storeXmlFile);
        Input input = new Input();

        try {
            Collection<ContentNodeI> allNodes = loadContentNodesFromXmlTask.call();
            input.setContentNodes(allNodes);
        } catch (Exception e) {
            LOGGER.error("Loading contentNodes from Store.xml failed", e);
            throw new CmsRuntimeException(e);
        }
        try {
            IndexingTask indexingTask = new IndexingTask("standalone-indexing", Phase.INDEXING, destination, storeId, input.getContentNodes());
            indexingTask.run();
        } catch (Exception e) {
            LOGGER.error("Standalone indexing crashed", e);
            throw new CmsRuntimeException(e);
        }
    }

    private void updatePublishFlowProgress(Phase phase, String message) {
        if (statusDelegate != null) {
            statusDelegate.setStatus("Publish phase: " + phase);
        }

        if (message != null) {
            LOGGER.info("Progress update: " + phase + "; " + message);
        } else {
            LOGGER.info("Progress update: " + phase);
        }
    }

    private void handleException(Exception exc, Phase phase, String errorMessage) {
        LOGGER.error("Publish failed due to error in phase " + phase, exc);

        // notify runtime about the error
        if (statusDelegate != null) {
            statusDelegate.notifyError(exc);
            statusDelegate.setStatus("Error during publish :" + exc.getMessage());
        }

        // persist failed state
        updatePublishStatus(EnumPublishStatus.FAILED, PublishMessage.ERROR, errorMessage);
    }

    private void finishPublish(boolean success, String message) {
        if (success) {
            LOGGER.info("*** PUBLISH FINISHED ***");
            if (message != null) {
                LOGGER.info(message);
            }
        } else {
            LOGGER.error("*** PUBLISH FAILED ***");
            if (message != null) {
                LOGGER.error(message);
            }
        }

        // update runtime
        if (statusDelegate != null && message != null) {
            statusDelegate.setStatus(message);
        }

        int severity = success ? PublishMessage.INFO : PublishMessage.FAILURE;

        // persist status
        updatePublishStatus((success ? EnumPublishStatus.COMPLETE : EnumPublishStatus.FAILED), severity, message);
    }

    /**
     * Persist publish state with additional message
     *
     * @param status
     *            Value of {@link EnumPublishStatus}. Optional.
     * @param severity
     *            Message severity.
     * @param message
     *            Optional publish message.
     */
    private void updatePublishStatus(EnumPublishStatus status, int severity, String message) {
        final Publish publishState = publishStatusService.fetchPublish(publishId);

        publishState.setLastModified(new Date());
        if (status != null) {
            publishState.setStatus(status);
            statusDelegate.setStatus(message);
        }
        if (message != null) {
            publishMessageLogger.log(publishId, new PublishMessage(severity, message, PublishFlow.class.getSimpleName()));
        }

        if (EnumPublishStatus.COMPLETE == status || EnumPublishStatus.FAILED == status) {
            publishMessageLogger.disableLogging();
        }

        publishStatusService.updatePublishStatus(publishState);

    }
}
