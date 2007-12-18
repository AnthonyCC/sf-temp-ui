package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidationMessage;

/**
 * Test case for the RecipeChildNodeValidator class.
 */
public class ConfiguredProductValidatorTest extends TestCase {

	ContentServiceI service;

	ConfiguredProductValidator validator;

	ContentValidationDelegate delegate;

	public void setUp() {

		List list = new ArrayList();
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/ConfiguredProducts.xml");

		CmsManager.setInstance(new CmsManager(service, null));

		validator = new ConfiguredProductValidator();

		delegate = new ContentValidationDelegate();
	}

	public void testOk() {
		validator.validate(delegate, service, findProduct("ok"), null);
		assertEquals(0, delegate.getValidationMessages().size());
	}

	public void testBadSku() {
		validator.validate(delegate, service, findProduct("bad_sku"), null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0, "Sku:XYZ not found");
	}

	public void testBadSalesUnit() {
		validator.validate(delegate, service, findProduct("bad_salesunit"),
				null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0,
				"Invalid sales unit 'XYZ' specified, valid units are [C01, C03]");
	}

	public void testBadOption() {
		validator.validate(delegate, service, findProduct("bad_option"), null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(
				0,
				"Invalid characteristic value XY for characteristic C_MT_BF_PAK. Valid options are [VP, ST]");
	}

	public void testMissingOption() {
		validator.validate(delegate, service, findProduct("missing_option"),
				null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0, "No value for characteristic C_MT_BF_PAK");
	}

	public void testExtraOption() {
		validator
				.validate(delegate, service, findProduct("extra_option"), null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0, "Extraneous characteristics [FOO]");
	}

	private void assertValidationMessage(int index, String message) {
		ContentValidationMessage msg = (ContentValidationMessage) delegate
				.getValidationMessages().get(index);
		assertEquals(message, msg.getMessage());
	}

	private ContentNodeI findProduct(String id) {
		return service.getContentNode(new ContentKey(
				FDContentTypes.CONFIGURED_PRODUCT, id));
	}

}
