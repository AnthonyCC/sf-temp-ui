package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Input;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Phase;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader.LoadContentNodesTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader.LoadMediaNodesTask;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader.LoadMediaURIsTask;

/**
 * This task is responsible to load all data required by the subsequent tasks
 *
 * @author segabor
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class ContentLoaderTask extends PublishTask implements Callable<Input> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentLoaderTask.class);

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private LoadContentNodesTask loadContentNodesTask;

    @Autowired
    private LoadMediaNodesTask loadMediaNodesTask;

    @Autowired
    private LoadMediaURIsTask loadMediaURIsTask;

    private ExecutorService pool = Executors.newFixedThreadPool(6);

    @PostConstruct
    private void init() {
        loadContentNodesTask.setPhase(Phase.INIT);
        loadMediaNodesTask.setPhase(Phase.INIT);
        loadMediaURIsTask.setPhase(Phase.INIT);
    }

    @Override
    public void setPublishId(Long publishId) {
        super.setPublishId(publishId);

        loadContentNodesTask.setPublishId(publishId);
        loadMediaNodesTask.setPublishId(publishId);
        loadMediaURIsTask.setPublishId(publishId);
    }

    @Override
    public Input call() throws Exception {

        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading publish materials", ContentLoaderTask.class.getSimpleName()));
        // Fetch CMS nodes
        final Future<Map<ContentKey, Map<Attribute, Object>>> allNodez = pool.submit(loadContentNodesTask);

        // Fetch Media nodes
        final Future<List<Media>> allMediaNodez = pool.submit(loadMediaNodesTask);

        // Fetch Media delta
        final Future<List<String>> mediaDelta = pool.submit(loadMediaURIsTask);

        // Collect results
        Input loadResult = null;
        try {
            loadResult = new Input();
            loadResult.setContentNodes(allNodez.get());
            loadResult.setMediaNodes(allMediaNodez.get());
            loadResult.setMediaUris(mediaDelta.get());
        } catch (InterruptedException e) {
            LOGGER.error("Loading Publish Data was interrupted", e);
        } catch (ExecutionException e) {
            LOGGER.error("Loading Publish Data was interrupted", e);
        } catch (CancellationException e) {
            LOGGER.error("Loading Publish Data was cancelled", e);
        } finally {
            pool.shutdown();
        }
        publishMessageLogger.log(publishId,
                new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading publish materials completed", ContentLoaderTask.class.getSimpleName()));
        return loadResult;
    }

    @Override
    public String getName() {
        return "Load Store and Media Content";
    }
}
