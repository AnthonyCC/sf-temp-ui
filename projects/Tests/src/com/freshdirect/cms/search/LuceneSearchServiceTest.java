package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
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
		List indexes = new ArrayList();
		AttributeIndex index = new AttributeIndex();
		index.setContentType("Foo");
		index.setAttributeName("name");
		indexes.add(index);

		LuceneSearchService search = createSearchService(indexes, tempDir);

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

		// update
		ContentKey k = new ContentKey(ContentType.get("Foo"), "foo1");
		ContentNodeI foo1 = content.getContentNode(k);
		foo1.getAttribute("name").setValue("mutatis mutandis");
		List nodes = new ArrayList();
		nodes.add(foo1);
		search.index(nodes);
		
		// search
		assertResults(search, 0, "bar");
		assertResults(search, 1, "foo");
		assertResults(search, 0, "one");
		assertResults(search, 1, "two");
		assertResults(search, 1, "mutatis");
		
		
		

		
		
	}


	private void initContent(String string) {
		List ts = new ArrayList();
		ts.add(new XmlTypeService(
				"classpath:/com/freshdirect/cms/search/TestDef1.xml"));

		CompositeTypeService typeService = new CompositeTypeService(ts);

		this.content = new XmlContentService(typeService,
				new FlexContentHandler(),
				string);
	}


	public static LuceneSearchService createSearchService(List indexes, File tempDir) {
		LuceneSearchService search = new LuceneSearchService();
		search.setIndexLocation(tempDir.getAbsolutePath());
		search.setIndexes(indexes);
		search.initialize();
		return search;
	}
	
	
	public void testSynonyms() throws IOException {
		initContent("classpath:/com/freshdirect/cms/search/TestContent2.xml");

		// initialize the search service
		List indexes = new ArrayList();
		AttributeIndex index = new AttributeIndex();
		index.setContentType("SynTest");
		index.setAttributeName("FULL_NAME");
		indexes.add(index);
		index = new AttributeIndex();
		index.setContentType("SynTest");
		index.setAttributeName("keywords");
		indexes.add(index);

		LuceneSearchService search = createSearchService(indexes, tempDir);
		
		SynonymDictionary dictionary = new SynonymDictionary ();
		dictionary.parseSynonymes("classpath:/com/freshdirect/cms/search/synonymlist.txt");
		search.setDictionary(dictionary);

		indexContent(search);

		
		assertResults(search, 3, "spaghetti"); // Zsombor, it needs to find all 3!
		assertResults(search, 0, "macaroni");
		assertResults(search, 3, "something");
		assertResults(search, 1, "orange juice");

		// now, some synonyms:
		assertResults(search, 1, "pasta");
		assertResults(search, 1, "oj");
		assertResults(search, 1, "one");
		
		
		
		
	}


	private void indexContent(LuceneSearchService search) {
		Map contentNodes = content.getContentNodes(content.getContentKeys());
		search.index(contentNodes.values());
	}

	private void assertResults(ContentSearchServiceI search, int resultCount,
			String query) {
		List results = search.search(query, 1000);
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
			//recursiveDelete(tempDir);
		}
	}

	public static File createTempDir(String prefix, String suffix)
			throws IOException {
		File tmp = File.createTempFile(prefix, suffix);
		// String tmpName = tmp.getAbsolutePath();
		//System.out.println("ZOZO: " + tmp.getName());
		tmp.delete();
		tmp.mkdir();
		tmp.deleteOnExit();
		return tmp;
	}

	private static void recursiveDelete(File f) throws IOException {
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				recursiveDelete(fs[i]);
			}
		}
		if (!(f.delete())) {
			throw new IOException("Cannot delete " + f.getPath());
		}
	}

}
