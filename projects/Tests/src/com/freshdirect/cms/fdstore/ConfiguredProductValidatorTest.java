package com.freshdirect.cms.fdstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.search.ContentIndex;
import com.freshdirect.cms.search.SearchTestUtils;
import com.freshdirect.cms.validation.ConfiguredProductValidator;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidationMessage;

import junit.framework.TestCase;

/**
 * Test case for the RecipeChildNodeValidator class.
 */
public class ConfiguredProductValidatorTest extends TestCase {

	ContentServiceI service;

	ConfiguredProductValidator validator;

	ContentValidationDelegate delegate;

	@Override
    public void setUp() {

		List<XmlTypeService> list = new ArrayList<XmlTypeService>();
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));
		list.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/fdstore/ErpDef.xml"));
		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/ConfiguredProducts.xml");

		try {
            CmsManager.setInstance(new CmsManager(service, SearchTestUtils.createSearchService(new ArrayList<ContentIndex>(), SearchTestUtils.createTempDir(this.getClass().getCanonicalName(), (new Date()).toString()))));
        } catch (IOException e) {
            e.printStackTrace();
        }

		validator = new ConfiguredProductValidator();

		delegate = new ContentValidationDelegate();
	}

	public void testOk() {
		validator.validate(delegate, service, DraftContext.MAIN, findProduct("ok"), null, null);
		assertEquals(0, delegate.getValidationMessages().size());
	}

	public void testBadSku() {
		validator.validate(delegate, service, DraftContext.MAIN, findProduct("bad_sku"), null, null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0, "Sku:XYZ not found");
	}

	public void testBadSalesUnit() {
		validator.validate(delegate, service, DraftContext.MAIN, findProduct("bad_salesunit"),
				null, null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0,
				"Invalid sales unit 'XYZ' specified, valid units are [C01, C03]");
	}

	public void testBadOption() {
		validator.validate(delegate, service, DraftContext.MAIN, findProduct("bad_option"), null, null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(
				0,
				"Invalid characteristic value XY for characteristic C_MT_BF_PAK. Valid options are [ST, VP]");
	}

	public void testMissingOption() {
		validator.validate(delegate, service, DraftContext.MAIN, findProduct("missing_option"),
				null, null);
		assertEquals(1, delegate.getValidationMessages().size());
		assertValidationMessage(0, "No value for characteristic C_MT_BF_PAK");
	}

	private void assertValidationMessage(int index, String message) {
		ContentValidationMessage msg = delegate
				.getValidationMessages().get(index);
		assertEquals(message, msg.getMessage());
	}

	private ContentNodeI findProduct(String id) {
		return service.getContentNode(ContentKey.getContentKey(FDContentTypes.CONFIGURED_PRODUCT, id), DraftContext.MAIN);
	}

}
