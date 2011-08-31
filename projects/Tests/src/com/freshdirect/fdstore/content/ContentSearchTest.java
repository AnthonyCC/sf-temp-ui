/**
 * 
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.AutoComplete;
import com.freshdirect.cms.search.ContentIndex;
import com.freshdirect.cms.search.LuceneSearchService;
import com.freshdirect.cms.search.LuceneSearchServiceTest;
import com.freshdirect.cms.search.SynonymDictionary;
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

	public void setUp() throws Exception {
		super.setUp();

		List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();
		list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

		CompositeTypeService typeService = new CompositeTypeService(list);

		service = new XmlContentService(typeService, new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");

		List<ContentIndex> indexes = new ArrayList<ContentIndex>();
		ContentIndex index = new ContentIndex();
		index.setContentType("Product");
		indexes.add(index);
		index = new AttributeIndex();
		index.setContentType("Product");
		((AttributeIndex) index).setAttributeName("FULL_NAME");
		((AttributeIndex) index).setSpelled(true);
		indexes.add(index);

		searchService = LuceneSearchServiceTest.createSearchService(indexes, LuceneSearchServiceTest.createTempDir(
				SmartSearchTest.class.getName(), "tmp"));
		searchService.setSynonyms(Collections.<SynonymDictionary>emptyList());
		searchService.setSpellingSynonyms(Collections.<SynonymDictionary>emptyList());

		Map<ContentKey, ContentNodeI> contentNodes = service.getContentNodes(service.getContentKeys());
		searchService.index(contentNodes.values(), true);
		searchService.indexSpelling(contentNodes.values());
		searchService.optimize();

		CmsManager.setInstance(new CmsManager(service, searchService));

		aspectSystem.add(new FDFactoryProductInfoAspect().addAvailableSku("DAI0008813", 2.0).addAvailableSku("DAI0008812", 3.0)
				.addAvailableSku("CAN0062899", 4.0).addAvailableSku("DAI0059088", 5.0));

		AutoComplete.setDisableAutocompleter(true);
	}

	/**
	 * Test method for
	 * {@link com.freshdirect.fdstore.content.ContentSearch#searchProducts(java.lang.String, com.freshdirect.fdstore.content.SearchQueryStemmer)}
	 * .
	 */
	public void testSearchProducts() {

		SearchResults sr = ContentSearch.getInstance().searchProducts("milk");

		assertNotNull("searchResults", sr);
		assertEquals("relevant products length", 4, sr.getProducts().size());

		SmartSearchTest.assertContentNodeModel("filtered product 0", "Organic Valley 2% Milk", sr.getProducts().get(0).getModel());
		SmartSearchTest.assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", sr.getProducts().get(1).getModel());
		SmartSearchTest.assertContentNodeModel("filtered product 2", "Organic Valley Ultra Pasteurized Whole Milk", sr
				.getProducts().get(2).getModel());
		SmartSearchTest.assertContentNodeModel("filtered product 3", "Asher's Milk Chocolate Pecan Caramel Pattie", sr
				.getProducts().get(3).getModel());

		sr = ContentSearch.getInstance().searchProducts("milk organic");

		assertNotNull("searchResults", sr);
		assertEquals("relevant products length for 'milk organic'", 3, sr.getProducts().size());

		sr = ContentSearch.getInstance().searchProducts("caramel");

		assertNotNull("searchResults", sr);
		assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
		SmartSearchTest.assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", sr
				.getProducts().get(0).getModel());

		sr = ContentSearch.getInstance().searchProducts("chocolate caramel");

		assertNotNull("searchResults", sr);
		assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
		SmartSearchTest.assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", sr
				.getProducts().get(0).getModel());

		sr = ContentSearch.getInstance().searchProducts("asher");

		assertNotNull("searchResults", sr);
		assertEquals("relevant products length for 'caramel'", 1, sr.getProducts().size());
		SmartSearchTest.assertContentNodeModel("filtered product 0", "Asher's Milk Chocolate Pecan Caramel Pattie", sr
				.getProducts().get(0).getModel());
		
		sr = ContentSearch.getInstance().searchProducts("funny milk");
		assertNotNull("searchResults", sr); // should give all milks as approximate
		assertEquals("relevant products length for 'funny milk'", 4, sr.getProducts().size());
	}

	/**
	 * Test method for {@link com.freshdirect.fdstore.content.ContentSearch#searchProducts(java.lang.String)}.
	 */
	public void testSearch() {
		SearchResults sr = ContentSearch.getInstance().searchProducts("milk");
		assertNotNull("searchResults", sr);
		assertEquals("relevant products length", 4, sr.getProducts().size());

		SmartSearchTest.assertContentNodeModel("filtered product 0", "Organic Valley 2% Milk", sr.getProducts().get(0).getModel());
		SmartSearchTest.assertContentNodeModel("filtered product 1", "Organic Valley 1% Milk", sr.getProducts().get(1).getModel());
		SmartSearchTest.assertContentNodeModel("filtered product 2", "Organic Valley Ultra Pasteurized Whole Milk", sr
				.getProducts().get(2).getModel());
		SmartSearchTest.assertContentNodeModel("filtered product 3", "Asher's Milk Chocolate Pecan Caramel Pattie", sr
				.getProducts().get(3).getModel());

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
		assertEquals("relevant products length", 0, sr.getProducts().size());
		assertEquals("has spelling suggestion", 3, sr.getSpellingSuggestions().size());
		List<String> suggestions = new ArrayList<String>(sr.getSpellingSuggestions());
		assertEquals("spelling suggestion", "organic valley", suggestions.get(0));
		assertEquals("spelling suggestion", "organic valley 1%", suggestions.get(1));
		assertEquals("spelling suggestion", "organic valley 2%", suggestions.get(2));
	}

	protected String[] getAffectedTables() {
		return null;
	}

	protected String getSchema() {
		return null;
	}
}
