package com.freshdirect.cms.validator;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.application.service.xml.CmsNodeHandler;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.ContentIndex;
import com.freshdirect.cms.search.SearchTestUtils;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.PrimaryHomeValidator;

import junit.framework.TestCase;

public class PrimaryHomeValidatorTest extends TestCase {
	ContentServiceI contentService;
	ContextService	contextService;
	DraftContext    draftContext = DraftContext.MAIN;
	
	@Deprecated
	ContentValidationDelegate delegate;
	
	CmsRequestI request;

	CmsManager mgr;

	
	@Override
    protected void setUp() throws Exception {
		/****
		FDRegistry.setDefaultRegistry("classpath:/com/freshdirect/PrimaryHomeValidatorTestConfig.registry");
		// The addConfiguration() call has the side-effect that FDRegistry.getInstance()
		// will be null-d out. this is required for the services to be reset before
		// each test - otherwise they would be shared between tests.
		FDRegistry.addConfiguration("classpath:/com/freshdirect/PrimaryHomeValidatorTestConfig.registry");
		***/

		List<ContentTypeServiceI> typeServices = new ArrayList<ContentTypeServiceI>();
		typeServices.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

		// CompositeTypeService typeService = new CompositeTypeService(typeServices);
		ContentTypeServiceI typeService = new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml");

		final XmlContentService xmlService = createService(typeService);
		((AbstractContentService) xmlService).setName("com.freshdirect.cms.StoreDef");
		this.contentService = xmlService;

		// this.content = (ContentServiceI) FDRegistry.getInstance().getService("com.freshdirect.test.CloneContentService", ContentServiceI.class);
		// ((AbstractContentService) content).setName("whatever");
		
		this.contextService = new ContextService(this.contentService);


		CmsManager.setInstance(new CmsManager(contentService, SearchTestUtils.createSearchService(new ArrayList<ContentIndex>(), SearchTestUtils.createTempDir(this.getClass().getCanonicalName(), (new Date()).toString()))));
		this.mgr = CmsManager.getInstance();
		
    	delegate = null /* new ContentValidationDelegate() */;
    	request = new CmsRequest(new CmsUser("vubul"), CmsRequestI.Source.ELSE, draftContext );
	}

    protected XmlContentService createService(ContentTypeServiceI typeService) {
        return new MyXmlContentService(typeService,
				new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/validator/PrimaryHomeTest.xml");
    }


    /**
     * Test against well configured product
     * has parent cats in both stores, has two primary homes --> PASSED
     */
    public void testWellConfiguredProduct() {
    	ContentKey prdKey = ContentKey.getContentKey(FDContentTypes.PRODUCT, "Prd1");
    	
    	PrimaryHomeValidator v = new PrimaryHomeValidator();
    	
    	ContentNodeI node = mgr.getContentNode(prdKey);
    	
    	v.validate(delegate, contentService, draftContext, node, null, null);
    	
    	assertTrue("Nothing should be changed", request.getNodes().isEmpty());
    }
    
    /**
     * Prd2 has parent categories in both stores but misses one primary homes
     */
    public void testPartiallyGoodProduct() {
    	ContentKey prdKey = ContentKey.getContentKey(FDContentTypes.PRODUCT, "Prd2");
    	
    	PrimaryHomeValidator v = new PrimaryHomeValidator();
    	
    	ContentNodeI node = mgr.getContentNode(prdKey);
    	
    	v.validate(delegate, contentService, draftContext, node, request, null);
    	
    	assertTrue("Product must be fixed!", !request.getNodes().isEmpty());
    	
    	// see fixed primary homes
    	ContentNodeI fixedNode = request.getNodes().iterator().next();

    	List<ContentKey> homes = (List<ContentKey>) fixedNode.getAttributeValue("PRIMARY_HOME");
    	assertEquals("Primary homes must contain two members", 2, homes.size());

    	assertTrue(homes.contains( ContentKey.getContentKey(FDContentTypes.CATEGORY, "Cat11") ));
    	assertTrue(homes.contains( ContentKey.getContentKey(FDContentTypes.CATEGORY, "Cat22") ));
    }
    
    
    public void testProductWithNoPrimaryHomes() {
    	ContentKey prdKey = ContentKey.getContentKey(FDContentTypes.PRODUCT, "Prd3");
    	PrimaryHomeValidator v = new PrimaryHomeValidator();

    	ContentNodeI node = mgr.getContentNode(prdKey);

    	v.validate(delegate, contentService, draftContext, node, request, null);
    	
    	assertTrue("Product must be fixed by the validator!", !request.getNodes().isEmpty());

    	ContentNodeI fixedNode = request.getNodes().iterator().next();

    	List<ContentKey> homes = (List<ContentKey>) fixedNode.getAttributeValue("PRIMARY_HOME");
    	assertEquals("Primary homes must contain two members", 2, homes.size());

    	assertTrue(homes.contains( ContentKey.getContentKey(FDContentTypes.CATEGORY, "Cat12") ));
    	assertTrue(homes.contains( ContentKey.getContentKey(FDContentTypes.CATEGORY, "Cat21") ));
    }
    
    
    public void testHalfOrphanProduct() {
    	ContentKey prdKey = ContentKey.getContentKey(FDContentTypes.PRODUCT, "HalfOrphanPrd");
    	PrimaryHomeValidator v = new PrimaryHomeValidator();

    	ContentNodeI node = mgr.getContentNode(prdKey);

    	assertNotNull("Object must exist in the test catalog", node);
    	
    	Set<ContentKey> parentKeys = mgr.getParentKeys(prdKey);
    	assertEquals("Product must have two parents", 2, parentKeys.size());
    	assertTrue("Product must have an orphan parent cat", parentKeys.contains(ContentKey.getContentKey(FDContentTypes.CATEGORY, "OrphanCat") ));
    	final Set<ContentKey> _parKeys = mgr.getParentKeys(ContentKey.getContentKey(FDContentTypes.CATEGORY, "OrphanCat"));
		assertEquals("Orphan category should not be a member of any parent node", 0, _parKeys.size());
    	
    	v.validate(delegate, contentService, draftContext, node, request, null);

    	assertTrue("Product must be fixed by the validator!", !request.getNodes().isEmpty());

    	ContentNodeI fixedNode = request.getNodes().iterator().next();

    	List<ContentKey> homes = (List<ContentKey>) fixedNode.getAttributeValue("PRIMARY_HOME");
    	assertEquals("Primary homes must contain exactly one primary home", 1, homes.size());

    	assertTrue(homes.contains( ContentKey.getContentKey(FDContentTypes.CATEGORY, "Cat11") ));
    }
}



class MyXmlContentService extends XmlContentService {
	public MyXmlContentService(ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles) {
	    super(typeService, nodeHandler, resourceFiles);
	}

	@Override
	protected Object writeReplace() throws ObjectStreamException {
		return null;
	}
}

