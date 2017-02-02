package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;

public class LuceneSearchServiceTest extends TestCase {

	private File tempDir;

	private ContentServiceI content;

	public void testIndexing() throws IOException {
		// load content
		initContent("classpath:/com/freshdirect/cms/search/TestContent1.xml");

		// initialize the search service
		List<ContentIndex> indexes = new ArrayList<ContentIndex>();
		ContentIndex index = new ContentIndex();
		index.setContentType("Foo");
		AttributeIndex index2 = new AttributeIndex();
		index2.setContentType("Foo");
		index2.setAttributeName("name");
		indexes.add(index2);

		LuceneSearchService search = createSearchService(indexes, tempDir);
		
		search.setSynonyms(Collections.<SynonymDictionary>emptyList());
		search.setSpellingSynonyms(Collections.<SynonymDictionary>emptyList());

		assertEquals(1, search.getIndexedTypes().size());

		indexContent(search);

		// search
		assertResults(search, 0, "bar");
		assertResults(search, 2, "foo");
		assertResults(search, 1, "one");
		assertResults(search, 1, "word");
		assertResults(search, 0, "wor");
		assertResults(search, 1, "stem");
		assertResults(search, 0, "stemmi");
		assertResults(search, 1, "stir");
		assertResults(search, 1, "stirfry");
		assertResults(search, 1, "J.K.");
		assertResults(search, 1, "j k's");
		assertResults(search, 1, "j k's");
		assertResults(search, 1, "j k 's");

		// update
		ContentKey k = ContentKey.getContentKey(ContentType.get("Foo"), "foo1");
		ContentNodeI foo1 = content.getContentNode(k, DraftContext.MAIN);
		foo1.setAttributeValue("name", "mutatis mutandis");
		List<ContentNodeI> nodes = new ArrayList<ContentNodeI>();
		nodes.add(foo1);
		search.index(nodes, false);

		// search
		assertResults(search, 0, "bar");
		assertResults(search, 1, "foo");
		assertResults(search, 0, "one");
		assertResults(search, 1, "two");
		assertResults(search, 1, "mutatis");
	}

	private void initContent(String string) {
		List<ContentTypeServiceI> ts = new ArrayList<ContentTypeServiceI>();
		ts.add(new XmlTypeService("classpath:/com/freshdirect/cms/search/TestDef1.xml"));

		CompositeTypeService typeService = new CompositeTypeService(ts);

		this.content = new XmlContentService(typeService, new FlexContentHandler(), string);
	}

	public static LuceneSearchService createSearchService(List<ContentIndex> indexes, File tempDir) {
		LuceneSearchService search = new LuceneSearchService();
		search.setIndexLocation(tempDir.getAbsolutePath());
		search.setIndexes(indexes);
		search.initialize();
		return search;
	}

	public void testSynonyms() throws IOException {
		initContent("classpath:/com/freshdirect/cms/search/TestContent2.xml");

		// initialize the search service
		List<ContentIndex> indexes = new ArrayList<ContentIndex>();
		ContentIndex index = new ContentIndex();
		index.setContentType("SynTest");
		indexes.add(index);
		AttributeIndex index2 = new AttributeIndex();
		index2.setContentType("SynTest");
		index2.setAttributeName("FULL_NAME");
		indexes.add(index2);
		index2 = new AttributeIndex();
		index2.setContentType("SynTest");
		index2.setAttributeName("keywords");
		indexes.add(index2);

		LuceneSearchService search = createSearchService(indexes, tempDir);

		SynonymDictionary dictionary = new SynonymDictionary();
		dictionary.parseSynonymes("classpath:/com/freshdirect/cms/search/synonymlist.txt");
		search.setSynonyms(Collections.singletonList(dictionary));
		search.setSpellingSynonyms(Collections.<SynonymDictionary>emptyList());

		indexContent(search);

		assertResults(search, 3, "spaghetti");
		assertResults(search, 0, "macaroni");
		assertResults(search, 3, "something");
		assertResults(search, 1, "orange juice");

		// now, some synonyms:
		assertResults(search, 1, "pasta");
		assertResults(search, 1, "oj");
		assertResults(search, 1, "one");

	}

	private void indexContent(ContentSearchServiceI search) {
		Map<ContentKey, ContentNodeI> contentNodes = content.getContentNodes(content.getContentKeys(DraftContext.MAIN), DraftContext.MAIN);
		search.index(contentNodes.values(), true);
	}

	private void assertResults(ContentSearchServiceI search, int resultCount, String query) {
		Collection<SearchHit> results = search.search(query, true, 1000);
		System.out.println(results.size());
		assertEquals(resultCount, results.size());
	}

	protected void setUp() throws Exception {
		super.setUp();
		this.tempDir = createTempDir(LuceneSearchService.class.getName(), "tmp");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (tempDir != null) {
			// recursiveDelete(tempDir);
		}
	}

	public static File createTempDir(String prefix, String suffix) throws IOException {
		File tmp = File.createTempFile(prefix, suffix);
		// String tmpName = tmp.getAbsolutePath();
		// System.out.println("ZOZO: " + tmp.getName());
		tmp.delete();
		tmp.mkdir();
		tmp.deleteOnExit();
		return tmp;
	}
}
