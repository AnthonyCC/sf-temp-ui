package com.freshdirect.cms.publish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ValidationTaskTest extends TestCase {

	private final static int VALIDATION_DELAY = 10;

	private final static int NODE_COUNT = 100;

	private final static int VALIDATORS_COUNT = 10;

	private ContentServiceI service;

	public void setUp() {
		XmlTypeService typeService = new XmlTypeService(
				"classpath:/com/freshdirect/cms/application/service/TestDef1.xml");

		service = new SimpleContentService(typeService);

		// generate some random nodes
		CmsRequest req = new CmsRequest(new CmsUser("test"));
		for (int i = 0; i < NODE_COUNT; i++) {
			ContentKey k = new ContentKey(ContentType.get("Foo"), String
					.valueOf(i));
			req.addNode(service.createPrototypeContentNode(k));
		}
		service.handle(req);
	}

	public void testParallelCompletion() throws InterruptedException {
		Category LOGGER = LoggerFactory.getInstance(ValidationTaskTest.class);
		LOGGER.debug("init");
		
		int startingThreads = Thread.activeCount();
		
		Set expectedKeys = service.getContentKeys();
		assertEquals(NODE_COUNT, expectedKeys.size());

		List validators = new ArrayList(VALIDATORS_COUNT);
		for (int i = 0; i < VALIDATORS_COUNT - 1; i++) {
			validators.add(new MockValidator());
		}
		// throw in a failing validator as well
		validators.add(new FailingValidator());

		ValidationTask t = new ValidationTask(service, validators);

		Publish publish = new Publish();
		t.execute(publish);

		// collect all keys
		Set actualKeys = new HashSet(NODE_COUNT);
		for (Iterator i = validators.iterator(); i.hasNext();) {
			MockValidator v = (MockValidator) i.next();
			actualKeys.addAll(v.keys);
		}

		assertEquals(expectedKeys, actualKeys);

		Thread.sleep(3000);
		assertEquals(startingThreads, Thread.activeCount());
	}

	private class MockValidator implements ContentValidatorI {

		List keys = Collections.synchronizedList(new ArrayList());

		public void validate(ContentValidationDelegate delegate,
				ContentServiceI service, ContentNodeI node, CmsRequestI request) {
			try {
				Thread.sleep(VALIDATION_DELAY);
				this.keys.add(node.getKey());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class FailingValidator extends MockValidator {

		public void validate(ContentValidationDelegate delegate,
				ContentServiceI service, ContentNodeI node, CmsRequestI request) {
			super.validate(delegate, service, node, request);
			throw new RuntimeException("oops");
		}
	}

}
