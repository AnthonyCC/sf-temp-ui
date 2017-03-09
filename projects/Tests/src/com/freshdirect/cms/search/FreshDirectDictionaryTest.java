package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.search.spell.Dictionary;
import com.freshdirect.cms.search.spell.DictionaryItem;
import com.freshdirect.cms.search.spell.FreshDirectDictionary;
import com.freshdirect.cms.search.term.SynonymSearchTermNormalizerFactory;
import com.freshdirect.cms.search.term.TermCoderFactory;

import junit.framework.TestCase;

public class FreshDirectDictionaryTest extends TestCase {
	private ContentServiceI initContent(String string) {
		List<ContentTypeServiceI> ts = new ArrayList<ContentTypeServiceI>();
		ts.add(new XmlTypeService("classpath:/com/freshdirect/cms/search/TestDef2.xml"));

		CompositeTypeService typeService = new CompositeTypeService(ts);

		return new XmlContentService(typeService, new FlexContentHandler(), string);
	}

	public void testIndexingWithSynonyms() throws IOException {
		Iterator<DictionaryItem> it = buildDictionary(false);
		// foo1
		if (it.hasNext())
			assertEquals("foo", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("one", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("foo one", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("goose", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");

		// foo2
		if (it.hasNext())
			assertEquals("foo", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("two", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("foo two", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("goose", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("liver", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("goose liver", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");

		// foo3
		if (it.hasNext())
			assertEquals("stemming", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("words", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("stemming words", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");

		// foo4
		if (it.hasNext())
			assertEquals("stir-fry", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("milk", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("chocolate", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("milk chocolate", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");

		// foo5
		if (it.hasNext())
			assertEquals("j.k.'s", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("j", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("k", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("j k", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("scotch", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("whiskey", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("whisky", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("scotch whiskey", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("scotch whisky", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");

		// must be over
		if (it.hasNext())
			fail("expecting no more dictionary word");
	}

	public void testIndexingWithoutSynonyms() throws IOException {
		Iterator<DictionaryItem> it = buildDictionary(true);
		
		while (it.hasNext()) {
			DictionaryItem next = it.next();
			if ("scotch".equals(next.getSpellingTerm()))
				break;
		}
		if (it.hasNext())
			assertEquals("whiskey", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");
		if (it.hasNext())
			assertEquals("scotch whiskey", it.next().getSpellingTerm());
		else
			fail("expected dictionary word missing");

		// must be over
		if (it.hasNext())
			fail("expecting no more dictionary word");
	}
	
	private Iterator<DictionaryItem> buildDictionary(boolean skipSynonyms) {
		ContentServiceI content = initContent("classpath:/com/freshdirect/cms/search/TestContent1.xml");

		Map<ContentType, List<AttributeIndex>> indexes = new HashMap<ContentType, List<AttributeIndex>>();
		ContentType type = ContentType.get("Foo");
		indexes.put(type, new ArrayList<AttributeIndex>());
		AttributeIndex index = new AttributeIndex("Foo", "name");
		index.setSpelled(true);
		indexes.get(type).add(index);
		index = new AttributeIndex("Foo", "value");
		index.setSpelled(true);
		indexes.get(type).add(index);

		SynonymDictionary synonyms = new SynonymDictionary();
		TermCoderFactory factory = new SynonymSearchTermNormalizerFactory();
		if (!skipSynonyms) {
			Set<String> whiskey = new HashSet<String>();
			whiskey.add("whisky");
			whiskey.add("whiskey");
			synonyms.addSynonyms(whiskey, factory);
		}

		List<ContentNodeI> nodes = new ArrayList<ContentNodeI>(content.getContentNodes(content.getContentKeys(DraftContext.MAIN), DraftContext.MAIN).values());
		Collections.sort(nodes, new Comparator<ContentNodeI>() {
			@Override
			public int compare(ContentNodeI o1, ContentNodeI o2) {
				return o1.getKey().getEncoded().compareTo(o2.getKey().getEncoded());
			}
		});
		Dictionary dictionary = new FreshDirectDictionary(nodes, indexes, Collections.singletonList(synonyms),
				Collections.<SynonymDictionary>emptyList(), false, CmsManager.getInstance());
		Iterator<DictionaryItem> it = dictionary.getWordsIterator();
		return it;
	}
}
