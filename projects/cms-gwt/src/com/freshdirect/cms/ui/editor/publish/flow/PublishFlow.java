package com.freshdirect.cms.ui.editor.publish.flow;

import static com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity.WARNING;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishStatus;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Input;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Phase;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.ContentLoaderTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.ContentValidationTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.MediaDeltaPublisherTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.ParallelStorePublishTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.RunScriptTask;
import com.freshdirect.cms.ui.editor.publish.service.StorePublishService;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.base.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class PublishFlow {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishFlow.class);

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private ContentLoaderTask contentLoaderTask;

    @Autowired
    private ContentValidationTask validationTask;

    @Autowired
    private StorePublishService storePublishService;

    @Autowired
    private MediaDeltaPublisherTask mediaDeltaPublisherTask;

    @Autowired
    private ParallelStorePublishTask parallelStorePublishTask;

    @Autowired
    private RunScriptTask runScriptTask;

    private boolean publishFlowCrashed = false;

    /**
     * Start the publish process here.
     *
     * @param publishId
     *            Publish ID
     * @param basePath
     *            Target path of publish materials
     * @param mediaRepositoryURL
     *            Media Repository URL
     * @param runScriptPath
     *            Path to script to be run after successful publish
     *
     * @throws Exception
     */
    public void doPublish(Long publishId, String basePath, String mediaRepositoryURL, String runScriptPath) throws Exception {

        LOGGER.info("*** STARTING PUBLISH (id: " + publishId + ") ***");
        LOGGER.info("  Publish Base Path: " + basePath);
        LOGGER.info("  Media Repository URL: " + mediaRepositoryURL);

        publishMessageLogger.enableLogging();
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Starting publish", PublishFlow.class.getSimpleName()));

        // INIT PHASE
        Input inputData = executeInitPublish(publishId);
        if (publishFlowCrashed) {
            return;
        }

        // VALIDATION PHASE
        executeValidateContent(publishId, inputData);
        if (publishFlowCrashed) {
            return;
        }

        // PUBLISH PHASE
        executePublishMediaDelta(publishId, basePath, mediaRepositoryURL, inputData);
        if (publishFlowCrashed) {
            return;
        }

        boolean storePublishResult = executeStorePublish(publishId, basePath, inputData);
        if (publishFlowCrashed) {
            return;
        }

        if (storePublishResult) {
            // Run Script
            if (runScriptPath != null) {
                LOGGER.info("Execute Script: " + runScriptPath);
                executeRunScript(publishId, basePath, runScriptPath);
            }

            finishPublish(publishId, true, "Publish finished");
        } else {
            finishPublish(publishId, false, "Publish failed");
        }
    }

    private boolean executeStorePublish(Long publishId, String basePath, Input inputData) {
        boolean storePublishResult = false;
        try {
            updatePublishFlowProgress(Phase.STORE_PUBLISH, "Store Publish phase started");

            // ParallelStorePublishTask ppt = new ParallelStorePublishTask(publishId, Phase.STORE_PUBLISH, inputData, basePath);
            parallelStorePublishTask.setPhase(Phase.STORE_PUBLISH);
            parallelStorePublishTask.setPublishId(publishId);
            parallelStorePublishTask.setPublishBasePath(basePath);
            parallelStorePublishTask.setInput(inputData);

            storePublishResult = parallelStorePublishTask.call().booleanValue();

            if (storePublishResult) {
                updatePublishFlowProgress(Phase.STORE_PUBLISH, "Store Publish phase completed");
            } else {
                updatePublishFlowProgress(Phase.STORE_PUBLISH, "Store Publish phase failed");
            }
        } catch (Exception e) {
            handleException(publishId, e, Phase.STORE_PUBLISH, "Store Publish failed");
            throw new RuntimeException(e);
        }
        return storePublishResult;
    }

    private void executeRunScript(Long publishId, String basePath, String scriptPath) {
        try {
            // BasePath harcoded to /FDX to handle the "# begin awkward hack so that CMS code doesn't run this script twice" in postPublish.sh
            // RunScriptTask runScriptTask = new RunScriptTask(publishId, Phase.POST_OP, basePath + "/FDX" , runScriptPath);
            runScriptTask.setPhase(Phase.POST_OP);
            runScriptTask.setPublishId(publishId);
            runScriptTask.setPublishPath(basePath + "/FDX");
            runScriptTask.setScriptPath(scriptPath);

            runScriptTask.run();
        } catch (Exception exc) {
            String errorMessage = exc.getMessage();

            updatePublishFlowProgress(Phase.POST_OP, errorMessage);
            publishMessageLogger.log(publishId, new StorePublishMessage(WARNING, errorMessage, RunScriptTask.class.getSimpleName()));
        }
    }

    private void executePublishMediaDelta(Long publishId, String basePath, String mediaRepositoryURL, Input inputData) {
        try {
            updatePublishFlowProgress(Phase.WRITE_OUT, "Media delta downloading started");

            mediaDeltaPublisherTask.setPhase(Phase.WRITE_OUT);
            mediaDeltaPublisherTask.setPublishId(publishId);
            mediaDeltaPublisherTask.setInput(inputData.getMediaUris());

            mediaDeltaPublisherTask.setTargetPath(basePath);
            mediaDeltaPublisherTask.setRepositoryUrl(mediaRepositoryURL);

            mediaDeltaPublisherTask.run();

            updatePublishFlowProgress(Phase.WRITE_OUT, "Media delta downloading completed");
        } catch (Exception e) {
            handleException(publishId, e, Phase.WRITE_OUT, "Media delta downloading failed");
            throw new RuntimeException(e);
        }
    }

    private void executeValidateContent(Long publishId, Input inputData) {
        try {
            validationTask.setPhase(Phase.VALIDATION);
            validationTask.setPublishId(publishId);
            validationTask.setInput(inputData);

            ValidationResults result = validationTask.call();

            List<ValidationResult> messages = result.getValidationResults();
            if (!messages.isEmpty()) {
                for (ValidationResult validationResult : messages) {
                    StorePublishMessageSeverity severity = StorePublishMessageSeverity.UNKNOWN;
                    switch (validationResult.getFailureLevel()) {
                        case ERROR:
                            severity = StorePublishMessageSeverity.ERROR;
                            break;
                        case WARNING:
                            severity = StorePublishMessageSeverity.WARNING;
                            break;
                        case INFO:
                            severity = StorePublishMessageSeverity.INFO;
                            break;
                    }

                    publishMessageLogger.log(publishId,
                            new StorePublishMessage(severity, validationResult.getMessage(), validationResult.getValidatedObject(), validationTask.getClass().getSimpleName()));
                }
            }

        } catch (Exception exc) {
            handleException(publishId, exc, Phase.VALIDATION, "Content Validation Failed");
            throw new RuntimeException(exc);
        }
    }

    private Input executeInitPublish(Long publishId) throws RuntimeException {
        Input inputData = null;
        try {
            updatePublishFlowProgress(Phase.INIT, "Init phase started");

            // ProducerTask<Input> initTask = new ContentLoaderTask(publishId, Phase.INIT);
            contentLoaderTask.setPhase(Phase.INIT);
            contentLoaderTask.setPublishId(publishId);

            inputData = contentLoaderTask.call();

            updatePublishFlowProgress(Phase.INIT, "Init phase completed");
        } catch (Exception e) {
            handleException(publishId, e, Phase.INIT, "Data Load failed");
            throw new RuntimeException(e);
        }
        return inputData;
    }

    private void updatePublishFlowProgress(Phase phase, String message) {
        if (message != null) {
            LOGGER.info("Progress update: " + phase + "; " + message);
        } else {
            LOGGER.info("Progress update: " + phase);
        }
    }

    private void handleException(Long publishId, Exception exc, Phase phase, String errorMessage) {
        if (publishFlowCrashed == false) {
            publishFlowCrashed = true;

            LOGGER.error("Publish failed due to error in phase " + phase, exc);

            // persist failed state
            updatePublishStatus(publishId, StorePublishStatus.FAILED, StorePublishMessageSeverity.ERROR, errorMessage);
        }
    }

    private void finishPublish(Long publishId, boolean success, String message) {
        if (publishFlowCrashed) {
            LOGGER.error("*** PUBLISH CRASHED ***");
            return;
        }

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

        StorePublishMessageSeverity severity = success ? StorePublishMessageSeverity.INFO : StorePublishMessageSeverity.FAILURE;

        // persist status
        updatePublishStatus(publishId, (success ? StorePublishStatus.COMPLETE : StorePublishStatus.FAILED), severity, message);
    }

    /**
     * Persist publish state with additional message
     *
     * @param status
     *            Value of {@link StorePublishStatus}. Optional.
     * @param severity
     *            Message severity.
     * @param message
     *            Optional publish message.
     */
    private void updatePublishStatus(Long publishId, StorePublishStatus status, StorePublishMessageSeverity severity, String message) {
        final Optional<StorePublish> optionalPublish = storePublishService.findPublish(publishId);

        if (optionalPublish.isPresent()) {
            StorePublish publish = optionalPublish.get();

            publish.setLastModified(new Date());
            if (status != null) {
                publish.setStatus(status);
                // statusDelegate.setStatus(message);
            }
            if (message != null) {
                publishMessageLogger.log(publishId, new StorePublishMessage(severity, message, PublishFlow.class.getSimpleName()));
            }

            if (StorePublishStatus.COMPLETE == status || StorePublishStatus.FAILED == status) {
                publishMessageLogger.disableLogging();
            }

            storePublishService.update(publish);
        }

    }
}
