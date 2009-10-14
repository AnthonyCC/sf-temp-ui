/**
 * 
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.LuceneSearchService;
import com.freshdirect.cms.search.LuceneSearchServiceTest;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;

/**
 * @author zsombor
 *
 */
public class ContentSearchTest extends FDCustomerManagerTestSupport {

    private XmlContentService service;
    private LuceneSearchService searchService;

    public ContentSearchTest(String name) {
        super(name);
    }

    
    
    

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        List list = new ArrayList();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");

        List indexes = new ArrayList();
        AttributeIndex index = new AttributeIndex();
        index.setContentType("Product");
        index.setAttributeName("FULL_NAME");
        indexes.add(index);

        searchService = LuceneSearchServiceTest.createSearchService(indexes, LuceneSearchServiceTest.createTempDir(SmartSearchTest.class.getName(), "tmp"));

        Map contentNodes = service.getContentNodes(service.getContentKeys());
        searchService.index(contentNodes.values());
        searchService.optimize();

        CmsManager.setInstance(new CmsManager(service, searchService));

        aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("DAI0008813", 2.0).addAvailableSku("DAI0008812", 3.0).addAvailableSku("CAN0062899", 4.0)
                .addAvailableSku("DAI0059088", 5.0));

        ContentSearch.getInstance().setDisableAutocompleter(true);
    }


    /**
     * Test method for {@link com.freshdirect.fdstore.content.ContentSearch#simpleSearch(java.lang.String, com.freshdirect.fdstore.content.SearchQueryStemmer)}.
     */
    public void testSimpleSearch() {
        
        SearchResults sr = ContentSearch.getInstance().simpleSearch("milk", SearchQueryStemmer.Porter);
        
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 4, sr.getProducts().size());

        SmartSearchTest.assertContentNodeModel("filtered product 0","Organic Valley 2% Milk", sr.getProducts().get(0));
        SmartSearchTest.assertContentNodeModel("filtered product 1","Organic Valley 1% Milk", sr.getProducts().get(1));
        SmartSearchTest.assertContentNodeModel("filtered product 2","Organic Valley Ultra Pasteurized Whole Milk", sr.getProducts().get(2));
        SmartSearchTest.assertContentNodeModel("filtered product 3","Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts().get(3));

        
        sr = ContentSearch.getInstance().simpleSearch("milk organic", SearchQueryStemmer.Porter);
        
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'milk organic'", 3, sr.getProducts().size());
        

        sr = ContentSearch.getInstance().simpleSearch("caramel", SearchQueryStemmer.Porter);
        
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
        SmartSearchTest.assertContentNodeModel("filtered product 0","Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts().get(0));


        
        sr = ContentSearch.getInstance().simpleSearch("funny milk", SearchQueryStemmer.Porter);
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'funny milk'", 4, sr.getProducts().size());

        
        sr = ContentSearch.getInstance().simpleSearch("milc", SearchQueryStemmer.Porter);
        assertNotNull("searchResults", sr);
        assertEquals("no products for 'milc'", 0, sr.getProducts().size());
        
    }

    /**
     * Test method for {@link com.freshdirect.fdstore.content.ContentSearch#search(java.lang.String)}.
     */
    public void testSearch() {
        SearchResults sr = ContentSearch.getInstance().search("milk");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 4, sr.getProducts().size());
        assertTrue("product relevant", sr.isProductsRelevant());
        assertFalse("spelling suggestion is not relevant", sr.isSuggestionMoreRelevant());

        SmartSearchTest.assertContentNodeModel("filtered product 0","Organic Valley 2% Milk", sr.getProducts().get(0));
        SmartSearchTest.assertContentNodeModel("filtered product 1","Organic Valley 1% Milk", sr.getProducts().get(1));
        SmartSearchTest.assertContentNodeModel("filtered product 2","Organic Valley Ultra Pasteurized Whole Milk", sr.getProducts().get(2));
        SmartSearchTest.assertContentNodeModel("filtered product 3","Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts().get(3));

        sr = ContentSearch.getInstance().search("milc");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 0, sr.getProducts().size());
        assertFalse("product not relevant", sr.isProductsRelevant());
        assertEquals("has spelling suggestion", "milk", sr.getSpellingSuggestion());
        assertTrue("spelling suggestion is relevant", sr.isSuggestionMoreRelevant());
        
    }

    protected String[] getAffectedTables() {
        // TODO Auto-generated method stub
        return null;
    }

    protected String getSchema() {
        // TODO Auto-generated method stub
        return null;
    }

}
