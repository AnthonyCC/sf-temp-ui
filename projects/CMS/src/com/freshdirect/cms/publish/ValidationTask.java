/*
 * Created on Mar 28, 2005
 */
package com.freshdirect.cms.publish;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A PublishTask to perform validation on the nodes to be published.
 */
public class ValidationTask implements PublishTask {

	private final static Category LOGGER = LoggerFactory.getInstance(ValidationTask.class);
	
	private ContentServiceI		contentService;
	
	private List 				validators;
	
	/**
	 * Constructor.
	 * 
	 * @param contentService the content service to perform the validation on.
	 * @param validators a list of ContentValidatorI objects,
	 *        the validators to run the nodes of the publish through
	 */
	public ValidationTask(ContentServiceI	contentService,
		 				  List				validators) {
		this.contentService = contentService;
		this.validators     = validators;
	}

	/**
	 * Run the nodes in the publish element through the validators.
	 * 
	 * @param publish the publish to validate.
	 * @throws ContentValidationException on content validation problems.
	 */
	private void validate(Publish publish) throws ContentValidationException {
				
		Set                         keys     = contentService.getContentKeys();
		Collection                  nodes    = contentService.getContentNodes(keys).values();
		final ContentValidationDelegate delegate = new ContentValidationDelegate();
		
		ExecutorService service = Executors.newFixedThreadPool(validators.size());
		
		final CountDownLatch latch = new CountDownLatch(nodes.size() * validators.size());
		LOGGER.debug("Enqueueing " + latch.getCount() + " validation tasks");
		
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			final ContentNodeI node = (ContentNodeI) i.next();
			
			for (Iterator v = validators.iterator(); v.hasNext();) {
				final ContentValidatorI validator = (ContentValidatorI) v.next();
				service.execute(new Runnable() {
					public void run() {
						try {
							validator.validate(delegate, contentService, node, null);
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
			while (!latch.await(10, TimeUnit.SECONDS)) {
				LOGGER.debug("Validation pending " + latch.getCount());
			}
			LOGGER.debug("Validation completed");
		} catch (InterruptedException e) {
			LOGGER.debug("Validation interrupted");
			// e.printStackTrace();
		}
		
		service.shutdownNow();
	
		// add all validation errors as warnings to the publish, with the current time stamp
		if (!delegate.isEmpty()) {
			for (Iterator it = delegate.getValidationMessages().iterator(); it.hasNext();) {
				ContentValidationMessage message  = (ContentValidationMessage) it.next();
				// the time stamp is going to be the current time
				PublishMessage pmessage = new PublishMessage(PublishMessage.WARNING, message.toString(), message.getContentKey());
			
				publish.getMessages().add(pmessage);
			}
		}	
	}
	
	/**
	 * Execute the task.
	 * 
	 * @param publish the publish to execute the task on.
	 */
	public void execute(Publish publish) {
		try {
			validate(publish);
		} catch (ContentValidationException e) {
			System.out.println("content validation exception: " + e);
			throw new CmsRuntimeException(e);
		}
	}

	public String getComment() {
		return "validating on content service " + contentService.getName();
	}

}