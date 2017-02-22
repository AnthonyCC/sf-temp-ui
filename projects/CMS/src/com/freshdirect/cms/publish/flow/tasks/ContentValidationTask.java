package com.freshdirect.cms.publish.flow.tasks;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.config.ContentValidatorConfiguration;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.flow.TransformerTask;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.cms.validation.ContentValidatorI;

public class ContentValidationTask extends TransformerTask<ContentServiceI, List<ContentValidationMessage>> {

    private static final Logger LOGGER = Logger.getLogger(ContentValidationTask.class);

    private final List<ContentValidatorI> validators;

    public ContentValidationTask(String publishId, Phase phase, ContentServiceI input) {
        super(publishId, phase, input);

        this.validators = ContentValidatorConfiguration.getValidatorList();
    }

    @Override
    public List<ContentValidationMessage> call() throws Exception {

        Boolean result = Boolean.TRUE;
        
        final ContentServiceI contentService = input;

        final DraftContext draftContext = DraftContext.MAIN;
        final Set<ContentKey> keys = contentService.getContentKeys(draftContext);
        final Collection<ContentNodeI> nodes = contentService.getContentNodes(keys, draftContext).values();
        final ContentValidationDelegate delegate = new ContentValidationDelegate();

        ExecutorService service = Executors.newFixedThreadPool(validators.size());

        final CountDownLatch latch = new CountDownLatch(nodes.size() * validators.size());
        LOGGER.debug("Enqueueing " + latch.getCount() + " validation tasks");

        for (final ContentNodeI node : nodes) {
            for (final ContentValidatorI validator : validators) {
                service.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            validator.validate(delegate, contentService, draftContext, node, null, null);
                        } catch (Exception e) {
                            LOGGER.warn("Exception in validator", e);
                            delegate.record(node.getKey(), e.getMessage());
                        } finally {
                            latch.countDown();
                        }
                    }
                });
            }
        }

        LOGGER.debug("Validation tasks enqueued");

        try {
            while (!latch.await(1, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            LOGGER.debug("Validation interrupted");

            result = false;
        }

        service.shutdownNow();

        if (!delegate.getValidationMessages().isEmpty()) {
            LOGGER.error("Validation failed");
        } else {
            LOGGER.debug("Validation completed");
        }
        
        return delegate.getValidationMessages();
    }

    @Override
    public String getName() {
        return "Validate content nodes";
    }

}
