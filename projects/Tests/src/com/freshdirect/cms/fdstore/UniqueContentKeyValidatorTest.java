package com.freshdirect.cms.fdstore;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidationMessage;

public class UniqueContentKeyValidatorTest extends TestCase {
	ContentServiceI service;

	UniqueContentKeyValidator validator;

	ContentValidationDelegate delegate;

	@Override
	protected void setUp() throws Exception {
		// construct a simple content service
		service = new XmlContentService(
				new XmlTypeService(
						"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"),
				new FlexContentHandler(),
						"classpath:/com/freshdirect/cms/fdstore/same_content_keys.xml");

		CmsManager.setInstance(new CmsManager(service, null));

		validator = new UniqueContentKeyValidator();
		validator.setCmsTypes( service.getTypeService().getContentTypes() );

		delegate = new ContentValidationDelegate();
	}

	public void testKeysWithSameID() throws InvalidContentKeyException {
		ContentNodeI node = CmsManager.getInstance()
				.createPrototypeContentNode(
						ContentKey.create(FDContentTypes.DEPARTMENT, "sameKey1"));
		
		validator.validate(delegate, CmsManager.getInstance(), node, null, null);
		
		assertFalse( delegate.isEmpty() );
		
		// printValidationMessages(delegate);
	}
	
	public void testUniqueKey() throws InvalidContentKeyException {
		ContentNodeI node = CmsManager.getInstance()
				.createPrototypeContentNode(
						ContentKey.create(FDContentTypes.PRODUCT, "otherKey1"));

		validator.validate(delegate, CmsManager.getInstance(), node, null, null);
		// printValidationMessages(delegate);
		
		assertTrue( delegate.isEmpty() );
	}



	@SuppressWarnings("unused")
	private void printValidationMessages(ContentValidationDelegate delegate) {
		for (ContentValidationMessage message : delegate.getValidationMessages()) {
			System.out.println(message.getContentKey() + ": " + message.getMessage());
		}
	}
}
