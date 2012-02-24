package com.freshdirect.cms.search;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.search.term.Synonym;
import com.freshdirect.cms.search.term.SynonymPermuter;
import com.freshdirect.cms.search.term.SynonymSearchTermNormalizerFactory;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;
import com.freshdirect.cms.search.term.TermCoderFactory;

public class SynonymTest extends TestCase {
	public static class MySynDict extends SynonymDictionary {
		public MySynDict() {
		}
	}
	
	public void testSynonyms() {
		SynonymDictionary dictionary = new MySynDict();
		Set<Term> synonyms = new HashSet<Term>();
		TermCoderFactory factory = new SynonymSearchTermNormalizerFactory();

		dictionary.addSynonyms(new String[] { " seltzer "}, new String[] { " water  "}, factory);
		dictionary.addSynonyms(new String[] { "puppy", "kitten" }, new String[] { "pet" }, factory);

		synonyms.add(new Term("orange juice"));
		synonyms.add(new Term("oj"));
		dictionary.addSynonyms(synonyms, factory);
		synonyms.clear();
		synonyms.add(new Term("orange    county choppers           "));
		synonyms.add(new Term("occ"));
		dictionary.addSynonyms(synonyms, factory);
		synonyms.clear();
		synonyms.add(new Term("orange       county"));
		synonyms.add(new Term("oc"));
		dictionary.addSynonyms(synonyms, factory);
		synonyms.clear();
		synonyms.add(new Term("      orange"));
		synonyms.add(new Term("portocala   "));
		synonyms.add(new Term("  aranciata "));
		dictionary.addSynonyms(synonyms, factory);
		synonyms.clear();
		synonyms.add(new Term("omega3"));
		synonyms.add(new Term("omega-3"));
		synonyms.add(new Term("omega 3"));
		synonyms.add(new Term("omaga 3"));
		dictionary.addSynonyms(synonyms, factory);
		synonyms.clear();
		
		Synonym[] syns = dictionary.getSynonymsForPrefix("oc");
		assertEquals(1, syns.length);
		assertEquals("oc", syns[0].getFirstWord());
		assertEquals("oc", syns[0].getTermAsString());
		assertEquals("orange county", syns[0].getSynonyms().iterator().next().toString());
		
		syns = dictionary.getSynonymsForPrefix("omega");
		assertEquals(1, syns.length);
		Set<Term> actual = new LinkedHashSet<Term>();
		actual.add(new Term("omega3"));
		actual.add(new Term("omaga 3"));
		assertEquals(syns[0].getSynonyms(), actual);

		syns = dictionary.getSynonymsForPrefix("portocala");
		assertEquals(1, syns.length);
		assertEquals("portocala", syns[0].getFirstWord());
		assertEquals("portocala", syns[0].getTermAsString());
		actual = new HashSet<Term>();
		for (Term syn : syns[0].getSynonyms())
			actual.add(syn);
		synonyms.add(new Term("orange"));
		synonyms.add(new Term("aranciata"));
		assertEquals(synonyms, actual);
		synonyms.clear();
		actual.clear();
		
		syns = dictionary.getSynonymsForPrefix("water");
		assertEquals(0, syns.length);

		syns = dictionary.getSynonymsForPrefix("seltzer");
		assertEquals(1, syns.length);
		assertEquals("seltzer", syns[0].getFirstWord());
		assertEquals("seltzer", syns[0].getTermAsString());
		assertEquals(1, syns[0].getSynonyms().size());
		assertEquals("water", syns[0].getSynonyms().iterator().next().toString());
		
		syns = dictionary.getSynonymsForPrefix("orange county");
		assertEquals(0, syns.length);

		syns = dictionary.getSynonymsForPrefix("orange");
		assertEquals(4, syns.length);
		assertEquals("orange", syns[0].getFirstWord());
		assertEquals("orange county choppers", syns[0].getTermAsString());
		assertEquals("orange county", syns[1].getTermAsString());
		assertEquals("orange juice", syns[2].getTermAsString());
		assertEquals("orange", syns[3].getTermAsString());
		
		TermCoder filter = new SynonymPermuter(dictionary, new Term("orange orange county terminators orange county choppers oj"));
		List<Term> terms = filter.getTerms();
		assertEquals(24, terms.size());
		synonyms.add(new Term("orange orange county terminators orange county choppers oj"));
		synonyms.add(new Term("portocala orange county terminators orange county choppers oj"));
		synonyms.add(new Term("aranciata orange county terminators orange county choppers oj"));
		synonyms.add(new Term("orange oc terminators orange county choppers oj"));
		synonyms.add(new Term("portocala oc terminators orange county choppers oj"));
		synonyms.add(new Term("aranciata oc terminators orange county choppers oj"));
		synonyms.add(new Term("orange orange county terminators occ oj"));
		synonyms.add(new Term("portocala orange county terminators occ oj"));
		synonyms.add(new Term("aranciata orange county terminators occ oj"));
		synonyms.add(new Term("orange oc terminators occ oj"));
		synonyms.add(new Term("portocala oc terminators occ oj"));
		synonyms.add(new Term("aranciata oc terminators occ oj"));
		synonyms.add(new Term("orange orange county terminators orange county choppers orange juice"));
		synonyms.add(new Term("portocala orange county terminators orange county choppers orange juice"));
		synonyms.add(new Term("aranciata orange county terminators orange county choppers orange juice"));
		synonyms.add(new Term("orange oc terminators orange county choppers orange juice"));
		synonyms.add(new Term("portocala oc terminators orange county choppers orange juice"));
		synonyms.add(new Term("aranciata oc terminators orange county choppers orange juice"));
		synonyms.add(new Term("orange orange county terminators occ orange juice"));
		synonyms.add(new Term("portocala orange county terminators occ orange juice"));
		synonyms.add(new Term("aranciata orange county terminators occ orange juice"));
		synonyms.add(new Term("orange oc terminators occ orange juice"));
		synonyms.add(new Term("portocala oc terminators occ orange juice"));
		synonyms.add(new Term("aranciata oc terminators occ orange juice"));
		for (Term term : terms)
			actual.add(term);
		assertEquals(synonyms, actual);

		filter = new SynonymPermuter(dictionary, new Term("seltzer"));
		assertEquals(Arrays.asList(new Term[] { new Term("seltzer"), new Term("water") }), filter.getTerms());

		filter = new SynonymPermuter(dictionary, new Term("water"));
		assertEquals(Arrays.asList(new Term[] { new Term("water") }), filter.getTerms());

		filter = new SynonymPermuter(dictionary, new Term("puppy kitten"));
		assertEquals(Arrays.asList(new Term[] { new Term("puppy kitten"), new Term("pet kitten"), new Term("puppy pet"), new Term("pet pet") }), filter.getTerms());

		filter = new SynonymPermuter(dictionary, new Term("pet"));
		assertEquals(Arrays.asList(new Term[] { new Term("pet") }), filter.getTerms());

		filter = new SynonymPermuter(dictionary, new Term("orange county orange-juice"));
		assertEquals(Arrays.asList(new Term[] { new Term("orange county orange-juice"), new Term("oc orange-juice"),
				new Term("orange county portocala-juice"), new Term("oc portocala-juice"),
				new Term("orange county aranciata-juice"), new Term("oc aranciata-juice") }), filter.getTerms());
	}
}
