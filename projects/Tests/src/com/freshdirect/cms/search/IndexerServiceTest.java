package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.index.IndexerService;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;

import junit.framework.TestCase;

// TODO mock test cases, please implement them
public class IndexerServiceTest extends TestCase {

	private String tempDir;
	private ContentServiceI content;

	public void testIndexing() throws IOException {
		// load content
		initContent("classpath:/com/freshdirect/cms/search/TestContent1.xml");

		// initialize the search service
		List<ContentIndex> indexes = new ArrayList<ContentIndex>();
		AttributeIndex index2 = new AttributeIndex("Foo", "name");
		indexes.add(index2);

		ContentKey k = ContentKey.getContentKey(ContentType.get("Foo"), "foo1");
		ContentNodeI foo1 = content.getContentNode(k, DraftContext.MAIN);
		foo1.setAttributeValue("name", "mutatis mutandis");
		List<ContentNodeI> nodes = new ArrayList<ContentNodeI>();
		nodes.add(foo1);
		
		IndexerService indexer = SearchTestUtils.createIndexerService(indexes);
		ContentSearchServiceI search = SearchTestUtils.createSearchService(indexes, tempDir);
		StoreContentSource source = SearchTestUtils.createStoreContentSource(nodes, ContentKey.getContentKey("Store:test"));
		IndexerConfiguration indexConfigurator = SearchTestUtils.createIndexConfiguration(source, tempDir);
		
		assertEquals(23, indexer.getIndexedTypes().size());

		indexContent(indexer, indexConfigurator);

		// search
		assertResults(search, 0, "bar");
		
		indexContent(indexer, indexConfigurator);

		// search
		assertResults(search, 0, "bar");
	}

	private void initContent(String string) {
		List<ContentTypeServiceI> ts = new ArrayList<ContentTypeServiceI>();
		ts.add(new XmlTypeService("classpath:/com/freshdirect/cms/search/TestDef1.xml"));

		CompositeTypeService typeService = new CompositeTypeService(ts);

		this.content = new XmlContentService(typeService, new FlexContentHandler(), string);
	}

	public void testSynonyms() throws IOException {
		initContent("classpath:/com/freshdirect/cms/search/TestContent2.xml");

		// initialize the search service
		List<ContentIndex> indexes = new ArrayList<ContentIndex>();
		ContentIndex index = new ContentIndex("SynTest");
		indexes.add(index);
		AttributeIndex index2 = new AttributeIndex("SynTest", "FULL_NAME");
		indexes.add(index2);
		index2 = new AttributeIndex("SynTest", "keywords");
		indexes.add(index2);
		
		ContentKey k = ContentKey.getContentKey(ContentType.get("SynTest"), "foo1");
		ContentNodeI foo1 = content.getContentNode(k, DraftContext.MAIN);
		foo1.setAttributeValue("name", "mutatis mutandis");
		List<ContentNodeI> nodes = new ArrayList<ContentNodeI>();
		nodes.add(foo1);

		IndexerService indexer = SearchTestUtils.createIndexerService(indexes);
		ContentSearchServiceI search = SearchTestUtils.createSearchService(indexes, tempDir);
		StoreContentSource source = SearchTestUtils.createStoreContentSource(nodes, ContentKey.getContentKey("Store:test"));
		IndexerConfiguration indexConfigurator = SearchTestUtils.createIndexConfiguration(source, tempDir);

		SynonymDictionary dictionary = new SynonymDictionary();
		dictionary.parseSynonymes("classpath:/com/freshdirect/cms/search/synonymlist.txt");

		indexContent(indexer, indexConfigurator);

		assertResults(search, 0, "spaghetti");
	}

	private void indexContent(IndexerService indexer, IndexerConfiguration configuration) {
		Map<ContentKey, ContentNodeI> contentNodes = content.getContentNodes(content.getContentKeys(DraftContext.MAIN), DraftContext.MAIN);
		indexer.fullIndex(contentNodes.values(), configuration);
	}

	private void assertResults(ContentSearchServiceI search, int resultCount, String query) {
		Collection<SearchHit> results = search.search(query, true, 1000);
		System.out.println(results.size());
		assertEquals(resultCount, results.size());
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.tempDir = SearchTestUtils.createTempDir(LuceneSearchService.class.getName(), "tmp");
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		if (tempDir != null) {
			// recursiveDelete(tempDir);
		}
	}

}
