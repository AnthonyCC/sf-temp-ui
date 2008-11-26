package com.freshdirect.cms.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.NodeWalker;
import com.freshdirect.cms.context.PHSWalker;
import com.freshdirect.cms.fdstore.FDContentTypes;

/**
 * ContextWalkerTest tests all ContextWalker subclasses
 * 
 * NodeWalker - sort out orphaned contexts
 * PHSWalker - returns visible contexts (<-> not hidden)
 * HCWalker - (not tested)
 * 
 * @author segabor
 *
 */
public class ContextWalkerTest extends TestCase {
	ContentServiceI content;
	ContextService	svc;
	
	public void setUp() {
		List typeServices = new ArrayList();
		typeServices.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

		CompositeTypeService typeService = new CompositeTypeService(typeServices);

		this.content = createService(typeService);
		
		this.svc = new ContextService(this.content);
	}


    protected XmlContentService createService(CompositeTypeService typeService) {
        return new XmlContentService(typeService,
				new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/content/WalkerStore.xml");
    }


	protected boolean assertContexts(List ctxs, String[] keys) {
		assertNotNull(ctxs);
		assertNotNull(keys);
		assertEquals(ctxs.size(), keys.length);
		
		for(int i=0; i< keys.length; i++) {
			assertEquals(keys[i], ((Context) ctxs.get(i)).getParentContext().getContentKey().getId());
		}
		
		return true;
	}
	
	protected boolean assertContentKeys(List cKeys, String[] keys) {
		assertNotNull(cKeys);
		assertNotNull(keys);
		assertEquals(cKeys.size(), keys.length);
		
		for(int i=0; i< keys.length; i++) {
			assertEquals(keys[i], ((ContentKey) cKeys.get(i)).getId());
		}

		return true;
	}
	
	/**
	 * Test the case when product has a context pointing to a sub-tree of categories
	 * where root IS NOT Department (ie. orphaned)
     *
     * 'cat_1' is orphaned (it is not bound to a department).
     *
	 * @throws InvalidContentKeyException 
	 */
	public void testOrphanedCategory() throws InvalidContentKeyException {
		ContentNodeI prd1 = content.getContentNode(ContentKey.create(FDContentTypes.PRODUCT, "prd_1"));

		List ctxs = new ArrayList(svc.getAllContextsOf(prd1.getKey()));
		assertContexts(ctxs, new String[]{"cat_1","cat_2"});
		
		// sort out orphaned categories.
		NodeWalker.filterOrphanedParents(ctxs.iterator(), svc);

		assertContexts(ctxs, new String[]{"cat_2"});
	}



	/**
	 * @throws InvalidContentKeyException
	 */
	public void testNewProduct() throws InvalidContentKeyException {
		ContentNodeI prd1 = content.getContentNode(ContentKey.create(FDContentTypes.PRODUCT, "new_prd"));
		assertNotNull(prd1);

		List ctxs = new ArrayList(this.svc.getAllContextsOf(prd1.getKey()));
		/*
		 * All contexts of a product without parents should be a set with just one root context
		 * (context without parent context)
		 */
		assertNotNull(ctxs);
		assertEquals(1, ctxs.size());
		assertTrue( ((Context)ctxs.get(0) ).isRoot());
		assertNull( ((Context)ctxs.get(0)).getParentContext() );

		// NOTE: will crash if unfixed (MNT-380)
		List parentKeys = PHSWalker.getVisibleParents(ctxs.iterator(), ContextService.getInstance());

		assertEquals(parentKeys.size(), 0);
		
		System.err.println(parentKeys);
	}
	
	
	public void testInvalidPrimaryHome() throws InvalidContentKeyException {
		ContentNodeI prd = content.getContentNode(ContentKey.create(FDContentTypes.PRODUCT, "prd_2"));
		assertNotNull(prd);

		List ctxs = new ArrayList(this.svc.getAllContextsOf(prd.getKey()));
		assertContexts(ctxs, new String[]{"cat_2","cat_hdn"});

		List possibleParents = NodeWalker.getVisibleParents(ctxs.iterator(), svc);
		assertContexts(possibleParents, new String[]{"cat_2"});
	}

	public void testInvalidPrimaryHome2() throws InvalidContentKeyException {
		ContentNodeI prd = content.getContentNode(ContentKey.create(FDContentTypes.PRODUCT, "prd_3"));
		assertNotNull(prd);

		List ctxs = new ArrayList(this.svc.getAllContextsOf(prd.getKey()));
		assertContexts(ctxs, new String[]{"cat_nosearch","cat_2"});

		List possibleParents = NodeWalker.getVisibleParents(ctxs.iterator(), svc);
		assertContexts(possibleParents, new String[]{"cat_2"});
	}

	public void testInvalidPrimaryHome3() throws InvalidContentKeyException {
		ContentNodeI prd = content.getContentNode(ContentKey.create(FDContentTypes.PRODUCT, "prd_4"));
		assertNotNull(prd);

		List ctxs = new ArrayList(this.svc.getAllContextsOf(prd.getKey()));
		assertContexts(ctxs, new String[]{"cat_hdn_nosearch","cat_2"});

		List possibleParents = NodeWalker.getVisibleParents(ctxs.iterator(), svc);
		// negative test - 'not searchable' flag itself does not affect visibility
		assertContexts(possibleParents, new String[]{"cat_hdn_nosearch","cat_2"});
	}



	/**
	 * Test context ranking
	 * 
	 */
	public void testRanking() throws InvalidContentKeyException {
		ContentNodeI prd = content.getContentNode(ContentKey.create(FDContentTypes.PRODUCT, "prd_r"));
		assertNotNull(prd);
		
		List ctxs = new ArrayList(this.svc.getAllContextsOf(prd.getKey()));
		assertContexts(ctxs, new String[]{"cat_g_invalid","cat_g_valid","cat_a_valid","cat_a_invalid"});

		List deptKeys = new ArrayList(content.getContentKeysByType(FDContentTypes.DEPARTMENT));
		

		// order entries by their rank
		Collections.sort(ctxs, NodeWalker.getRankedComparator(svc, deptKeys));
		assertContexts(ctxs, new String[]{"cat_a_valid","cat_g_valid","cat_a_invalid","cat_g_invalid"});
	}
}
