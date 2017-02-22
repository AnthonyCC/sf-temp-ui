package com.freshdirect.fdstore.content;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.AutoComplete;
import com.freshdirect.cms.search.ContentIndex;
import com.freshdirect.cms.search.LuceneSearchService;
import com.freshdirect.cms.search.SearchTestUtils;
import com.freshdirect.fdstore.aspects.FDFactoryProductInfoAspect;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;

/**
 * @author zsombor
 * 
 */
public class ContentSearchTest extends FDCustomerManagerTestSupport {

    private XmlContentService service;
    private LuceneSearchService searchService;
    private IndexerService indexerService;

    public ContentSearchTest(String name) {
        super(name);
    }

    @Override
	public void setUp() throws Exception {
		super.setUp();

		List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();
		list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");

		List<ContentIndex> indexes = new ArrayList<ContentIndex>();
		ContentIndex index = new ContentIndex("Product");
		indexes.add(index);
		index = new AttributeIndex("Product","FULL_NAME");
		((AttributeIndex) index).setSpelled(true);
		indexes.add(index);
		
		String tempDir = SearchTestUtils.createTempDir(
                SmartSearchTest.class.getName(), "tmp");
		
		indexerService = SearchTestUtils.createIndexerService(indexes);
		searchService = (LuceneSearchService) SearchTestUtils.createSearchService(indexes, tempDir);
		
//		indexerService.setSynonyms(Collections.<SynonymDictionary>emptyList());
//		indexerService.setSpellingSynonyms(Collections.<SynonymDictionary>emptyList());

		Map<ContentKey, ContentNodeI> contentNodes = service.getContentNodes(service.getContentKeys(DraftContext.MAIN), DraftContext.MAIN);
		indexerService.index(contentNodes.values(), IndexerConfiguration.getDefaultConfiguration());

		CmsManager.setInstance(new CmsManager(service, searchService));

		aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("DAI0008813", 2.0).addAvailableSku("DAI0008812", 3.0)
				.addAvailableSku("CAN0062899", 4.0).addAvailableSku("DAI0059088", 5.0));

		AutoComplete.setDisableAutocompleter(true);
	}

    /**
     * Test method for {@link com.freshdirect.fdstore.content.ContentSearch#searchProducts(java.lang.String, com.freshdirect.fdstore.content.SearchQueryStemmer)} .
     */
    @SuppressWarnings("static-method")
    public void testSearchProducts() {

        SearchResults sr = ContentSearch.getInstance().searchProducts("milk");

        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 4, sr.getProducts().size());

        assertNodeWithNameInCollection("filtered product 0", "Organic Valley 2% Milk", sr.getProducts());
        assertNodeWithNameInCollection("filtered product 1", "Organic Valley 1% Milk", sr.getProducts());
        assertNodeWithNameInCollection("filtered product 2", "Organic Valley Ultra Pasteurized Whole Milk", sr.getProducts());
        assertNodeWithNameInCollection("filtered product 3", "Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts());

        sr = ContentSearch.getInstance().searchProducts("milk organic");

        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'milk organic'", 3, sr.getProducts().size());

        sr = ContentSearch.getInstance().searchProducts("caramel");

        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
        SmartSearchTest.assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts().get(0).getModel());

        sr = ContentSearch.getInstance().searchProducts("chocolate caramel");

        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
        SmartSearchTest.assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts().get(0).getModel());

        sr = ContentSearch.getInstance().searchProducts("asher");

        assertNotNull("searchResults", sr);
        assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
        SmartSearchTest.assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts().get(0).getModel());

        sr = ContentSearch.getInstance().searchProducts("funny milk");
        assertNotNull("searchResults", sr); // should give all milks as approximate
        assertEquals("relevant products length for 'funny milk'", 4, sr.getProducts().size());
    }

    /**
     * Test method for {@link com.freshdirect.fdstore.content.ContentSearch#searchProducts(java.lang.String)}.
     */
    @SuppressWarnings("static-method")
    public void testSearch() {
        SearchResults sr = ContentSearch.getInstance().searchProducts("milk");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 4, sr.getProducts().size());

        assertNodeWithNameInCollection("filtered product 0", "Organic Valley 2% Milk", sr.getProducts());
        assertNodeWithNameInCollection("filtered product 1", "Organic Valley 1% Milk", sr.getProducts());
        assertNodeWithNameInCollection("filtered product 2", "Organic Valley Ultra Pasteurized Whole Milk", sr.getProducts());
        assertNodeWithNameInCollection("filtered product 3", "Asher's Milk Chocolate Pecan Caramel Pattie", sr.getProducts());

        sr = ContentSearch.getInstance().searchProducts("organic valley");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 3, sr.getProducts().size());

        sr = ContentSearch.getInstance().searchProducts("valley organic");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 3, sr.getProducts().size());

        sr = ContentSearch.getInstance().searchProducts("\"valley organic\"");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 3, sr.getProducts().size()); // it'll be approximated to "organic"

        sr = ContentSearch.getInstance().searchProducts("milc");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 4, sr.getProducts().size());
        assertEquals("has spelling suggestion", 1, sr.getSpellingSuggestions().size());
        assertEquals("spelling suggestion", "milk", sr.getSpellingSuggestions().iterator().next());

        sr = ContentSearch.getInstance().searchProducts("ahser's");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 1, sr.getProducts().size());
        assertEquals("has spelling suggestion", 1, sr.getSpellingSuggestions().size());
        assertEquals("spelling suggestion", "asher's", sr.getSpellingSuggestions().iterator().next());

        sr = ContentSearch.getInstance().searchProducts("ahser");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 1, sr.getProducts().size());
        assertEquals("has spelling suggestion", 1, sr.getSpellingSuggestions().size());
        assertEquals("spelling suggestion", "asher's", sr.getSpellingSuggestions().iterator().next());

        sr = ContentSearch.getInstance().searchProducts("pasterized milk");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 1, sr.getProducts().size());
        assertEquals("has spelling suggestion", 1, sr.getSpellingSuggestions().size());
        assertEquals("spelling suggestion", "pasteurized milk", sr.getSpellingSuggestions().iterator().next());

        sr = ContentSearch.getInstance().searchProducts("organicvalley");
        assertNotNull("searchResults", sr);
        assertEquals("relevant products length", 3, sr.getProducts().size());
        assertEquals("has spelling suggestion", 1, sr.getSpellingSuggestions().size());
        List<String> suggestions = new ArrayList<String>(sr.getSpellingSuggestions());
        assertEquals("spelling suggestion", "organic valley", suggestions.get(0));
    }

    private static <T extends ContentNodeModel> void assertNodeWithNameInCollection(String errorMsg, String fullName, Collection<FilteringSortingItem<T>> collection) {
        for (FilteringSortingItem<T> item : collection) {
            if (fullName.equals(item.getModel().getFullName())) {
                return;
            }
        }
        fail("Missing:" + fullName + ", error:" + errorMsg);
    }

    @Override
    protected String[] getAffectedTables() {
        return null;
    }

    @Override
    protected String getSchema() {
        return null;
    }
}
