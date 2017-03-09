package com.freshdirect.cms.publish.flow.tasks;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.Input;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.flow.ProducerTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadContentNodesTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadMaterialConfigurationsTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadMediaNodesTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadMediaURIsTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadSalesUnitsTask;
import com.freshdirect.cms.publish.flow.tasks.loader.LoadSkuMaterialAssociationsTask;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

/**
 * This task is responsible to load all data required by the subsequent tasks
 * 
 * @author segabor
 *
 */
public final class ContentLoaderTask extends ProducerTask<Input> {

    private static final Logger LOGGER = Logger.getLogger(ContentLoaderTask.class);
    private final PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    private ExecutorService pool = Executors.newFixedThreadPool(6);

    public ContentLoaderTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

    @Override
    public Input call() throws Exception {

        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading publish materials", ContentLoaderTask.class.getSimpleName()));
        // Fetch CMS nodes
        final Future<Collection<ContentNodeI>> allNodez = pool.submit(new LoadContentNodesTask(publishId, Phase.INIT));

        // Fetch Media nodes
        final Future<Collection<ContentNodeI>> allMediaNodez = pool.submit(new LoadMediaNodesTask(publishId, Phase.INIT));

        // Fetch Media delta
        final Future<List<String>> mediaDelta = pool.submit(new LoadMediaURIsTask(publishId, Phase.INIT));

        // SKU material mapping
        final Future<Map<String, String>> skuMaterialAssignment = pool.submit(new LoadSkuMaterialAssociationsTask(publishId, Phase.INIT));

        // Material configuration options
        final Future<Map<String, Map<String, Set<String>>>> materialConfigurations = pool.submit(new LoadMaterialConfigurationsTask(publishId, Phase.INIT));

        // Material sales units
        final Future<Map<String, Set<String>>> materialSalesUnits = pool.submit(new LoadSalesUnitsTask(publishId, Phase.INIT));

        // Collect results
        Input loadResult = null;
        try {
            loadResult = new Input();
            loadResult.setContentNodes(allNodez.get());
            loadResult.setMediaNodes(allMediaNodez.get());
            loadResult.setMediaUris(mediaDelta.get());
            loadResult.setSkuMaterialAssignment(skuMaterialAssignment.get());
            loadResult.setMaterialConfiguration(materialConfigurations.get());
            loadResult.setMaterialSalesUnits(materialSalesUnits.get());
        } catch (InterruptedException e) {
            LOGGER.error("Loading Publish Data was interrupted", e);
        } catch (ExecutionException e) {
            LOGGER.error("Loading Publish Data was interrupted", e);
        } catch (CancellationException e) {
            LOGGER.error("Loading Publish Data was cancelled", e);
        } finally {
            pool.shutdown();
        }
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Loading publish materials completed", ContentLoaderTask.class.getSimpleName()));
        return loadResult;
    }

    @Override
    public String getName() {
        return "Load Store and Media Content";
    }
}
