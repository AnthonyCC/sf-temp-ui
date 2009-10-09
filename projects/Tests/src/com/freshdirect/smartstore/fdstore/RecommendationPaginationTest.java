package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;

public class RecommendationPaginationTest extends TestCase {
	final static Category LOGGER = LoggerFactory.getInstance(RecommendationPaginationTest.class);

	private XmlContentService service;

    Variant variant;
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();

        variant = new Variant("dummy", EnumSiteFeature.DYF, null);

        List<XmlTypeService> list = new ArrayList<XmlTypeService>();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        // This file is generated by com.freshdirect.cms.core.ContentFilterTool
        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore.xml");

        CmsManager.setInstance(new CmsManager(service, null));
    }


	/**
	 * Load products by their names.
	 * 
	 * @param prdNames
	 * @return
	 */
	protected List<ContentNodeModel> loadProducts(List<String[]> prdNames) {
		List<ContentNodeModel> l = new ArrayList<ContentNodeModel>();
		for (String[] namez : prdNames) {
			for (String pname : namez) {
				l.add(ContentNodeModelUtil.constructModel(ContentKey.decode("Product:"+pname), false));
			}
		}
		return l;
	}

	

	public void testOneItem() throws InvalidContentKeyException {
		List<String[]> prdNames = new ArrayList<String[]>();
		prdNames.add(
			new String[] {
					"gro_parmalat_white_2_01"
		});
		Recommendations r = new Recommendations(variant, loadProducts(prdNames));
		
		completeTest(r, prdNames);
	}

	public void testThreeItems() throws InvalidContentKeyException {
		List<String[]> prdNames = new ArrayList<String[]>();
		prdNames.add(
			new String[] {
					"gro_parmalat_white_2_01", "gro_parmalat_white_2_02", "dai_total_honyog"
		});
		Recommendations r = new Recommendations(variant, loadProducts(prdNames));
		
		completeTest(r, prdNames);
	}


	public void testFiveItems() throws InvalidContentKeyException {
		List<String[]> prdNames = new ArrayList<String[]>();
		prdNames.add(
			new String[] {
					"gro_parmalat_white_2_01", "gro_parmalat_white_2_02", "dai_total_honyog", "hba_phillips_mom_02", "hba_phillips_mom_01"
		});
		Recommendations r = new Recommendations(variant, loadProducts(prdNames));
		
		completeTest(r, prdNames);
	}



	public void testEightItems() throws InvalidContentKeyException {
		List<String[]> prdNames = new ArrayList<String[]>();
		prdNames.add(
			new String[] {
					"gro_parmalat_white_2_01", "gro_parmalat_white_2_02", "dai_total_honyog", "hba_phillips_mom_02", "hba_phillips_mom_01"
		});
		prdNames.add(
			new String[] {
					"spe_ritter_mchaz", "gro_carnat_evaporat_02", "spe_feod_mlkalm_01"
		});
		Recommendations r = new Recommendations(variant, loadProducts(prdNames));
		
		completeTest(r, prdNames);
	}



	public void testArbitraryNumberOfItems() {
		final List<ContentKey> keyz = new ArrayList<ContentKey>(service.getContentKeysByType(FDContentTypes.PRODUCT));

		// test it 10 times
		for (int m=0; m<10; m++) {
		
			final int p = 1 + (int) Math.round( 100*Math.random() );
			
			List<String[]> prdNames = new ArrayList<String[]>();
			
			int remaining = p;
			final int wsize = Recommendations.MAX_PRODS;
			for (int k=0; k<p; k++) {
				if (remaining == 0)
					break;
				
				String names[] = new String[ remaining >= wsize ? wsize : remaining ];
				for (int j=0; j<names.length; j++) {
					names[j] = keyz.get( (int) ( ((double)keyz.size()) * Math.random()) ).getId();
					// System.err.println("["+k+"]["+j+"] ->" + names[j]);
				}
				prdNames.add(names);
				
				remaining -= names.length;
			}

			Recommendations r = new Recommendations(variant, loadProducts(prdNames));

			// System.err.println("Test recommendation pagination with " + p + " products / " + prdNames.size() + " pages ..." );
			completeTest(r, prdNames);
		}
	}




	protected void completeTest(Recommendations r, List<String[]> prdNames) {
		final int pages = prdNames.size();

		assertTrue(r.getNumberOfPages() == pages);

		int touched[] = new int[pages];
		for (int k=0; k<pages; k++)
			touched[k] = 0;
		
		// test back and forth three times
		for (int z=0; z<3; z++) {
			for (int j=0; j<pages; j++) {
				checkPageAt(r, prdNames, j);
				assertEquals(0 < touched[j]++, r.isLogged());
				r.pageForward();
			}
			checkPageAt(r, prdNames, pages-1);
			assertEquals(0 < touched[pages-1]++, r.isLogged());
			
			for (int j=pages-1; j>0; j--) {
				checkPageAt(r, prdNames, j);
				assertEquals(0 < touched[j]++, r.isLogged());
				r.pageBackward();
			}
			checkPageAt(r, prdNames, 0);
			assertEquals(0 < touched[pages-1]++, r.isLogged());
		}
	}


	/**
	 * Verifies a particular page
	 * 
	 * @param r
	 * @param prdNames
	 * @param pos
	 */
	protected void checkPageAt(Recommendations r, List<String[]> prdNames,
			int pos) {
		final int pages = prdNames.size();
		// first page test
		if (pos==0) {
			assertTrue( r.isFirstPage());
		}

		// last page test
		if (pos==pages-1) {
			if (!r.isLastPage()) {
				System.err.println("@@@@ pos=" + pos + "; offset="+r.getOffset() + "; pages="+r.getNumberOfPages());
			}
			assertTrue( r.isLastPage());
		}

		if (0 > pos && pos < pages -1) {
			assertFalse( r.isFirstPage());
			assertFalse(r.isLastPage());
		}
		
		// on the correct page
		assertTrue( r.getOffset() == pos);
		List<ProductModel> prds = r.getProducts();
		String[] prdNames1 = prdNames.get(r.getOffset());
		
		// test products window
		assertTrue(prds.size() == prdNames1.length);
		for (int k=0; k<prds.size(); k++) {
			assertTrue( prdNames1[k].equalsIgnoreCase(prds.get(k).getContentName()) );
		}
	}
}
