package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.cms.search.term.AmpersandConv;
import com.freshdirect.cms.search.term.AmpersandPermuter;
import com.freshdirect.cms.search.term.AmpersandSpellingPermuter;
import com.freshdirect.cms.search.term.ApostropheConv;
import com.freshdirect.cms.search.term.ApostrophePermuter;
import com.freshdirect.cms.search.term.ApproximationsPermuter;
import com.freshdirect.cms.search.term.CommaPermuter;
import com.freshdirect.cms.search.term.DashAsteriskPermuter;
import com.freshdirect.cms.search.term.DashAsteriskSplitPermuter;
import com.freshdirect.cms.search.term.DiacriticsPermuter;
import com.freshdirect.cms.search.term.DiacriticsRemoval;
import com.freshdirect.cms.search.term.DictionaryPermuter;
import com.freshdirect.cms.search.term.InitialismPermuter;
import com.freshdirect.cms.search.term.IsoLatin1Filter;
import com.freshdirect.cms.search.term.NumberPermuter;
import com.freshdirect.cms.search.term.PercentConv;
import com.freshdirect.cms.search.term.PlusSignConv;
import com.freshdirect.cms.search.term.PunctuationCoder;
import com.freshdirect.cms.search.term.QuotationConv;
import com.freshdirect.cms.search.term.QuotationPermuter;
import com.freshdirect.cms.search.term.SearchTermNormalizer;
import com.freshdirect.cms.search.term.SlashConv;
import com.freshdirect.cms.search.term.SpellingInitialismPermuter;
import com.freshdirect.cms.search.term.SpellingPunctuationCoder;
import com.freshdirect.cms.search.term.SpellingTermNormalizer;
import com.freshdirect.cms.search.term.Synonym;
import com.freshdirect.cms.search.term.SynonymPermuter;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;

public class TermCoderTests extends TestCase {
	static class MySynonymDictionary extends SynonymDictionary {

	}

	public void testAmpersandConv() {
		TermCoder filter = new AmpersandConv(new Term("a&w"));
		assertEquals("a and w", filter.toString());
	}

	public void testAmpersandConv2() {
		TermCoder filter = new AmpersandConv(new Term("a & b homestyle gefilte fish"));
		assertEquals("a and b homestyle gefilte fish", filter.toString());
	}

	public void testAmpersandConv3() {
		TermCoder filter = new AmpersandConv(Arrays.asList(new Term[] { new Term("a& b homestyle"), new Term("gefilte fish") }));
		assertEquals("a and b homestyle gefilte fish", filter.toString());
	}

	public void testApostropheConv() {
		TermCoder filter = new ApostropheConv(new Term("amy's (not just for) kid'ss meals, baked ziti"));
		assertEquals("amy (not just for) kidss meals, baked ziti", filter.toString());
	}

	public void testApostropheConv2() {
		TermCoder filter = new ApostropheConv(new Term(
				"annie's homegrown p'sghetti pasta w/ soy meatballs in tomato & cheese sauce"));
		assertEquals("annie homegrown psghetti pasta w/ soy meatballs in tomato & cheese sauce", filter.toString());
	}

	public void testApostropheConv3() {
		TermCoder filter = new ApostropheConv(new Term("bachman thin n' right baked pretzels"));
		assertEquals("bachman thin n right baked pretzels", filter.toString());
	}

	public void testApostropheConv4() {
		TermCoder filter = new ApostropheConv(new Term("chik'n patties"));
		assertEquals("chikn patties", filter.toString());
	}

	public void testApostropheConv5() {
		TermCoder filter = new ApostropheConv(new Term("clif builder's cookies 'n cream bar"));
		assertEquals("clif builder cookies and cream bar", filter.toString());
	}

	public void testApostropheConv6() {
		TermCoder filter = new ApostropheConv(new Term("damascus honey'n grain pita"));
		assertEquals("damascus honeyn grain pita", filter.toString());
	}

	public void testApostropheConv7() {
		TermCoder filter = new ApostropheConv(new Term("agnesi capelli d'angelo"));
		assertEquals("agnesi capelli angelo", filter.toString());
	}

	public void testDashAsteriskPermuter() {
		TermCoder filter = new DashAsteriskPermuter(new Term("my*t*fine chocolate pudding mix"));
		assertEquals("mytfine chocolate pudding mix", filter.toString());
	}

	public void testDashAsteriskPermuter2() {
		TermCoder filter = new DashAsteriskPermuter(new Term("3-cheese calzone"));
		assertEquals("3cheese calzone", filter.toString());
	}

	public void testDashAsteriskPermuter3() {
		TermCoder filter = new DashAsteriskPermuter(new Term("12-14lb turkey dinner"));
		assertEquals("12 14lb turkey dinner", filter.toString());
	}

	public void testDashAsteriskPermuter4() {
		TermCoder filter = new DashAsteriskPermuter(new Term("airborne effervescent health formula - on-the-go packets"));
		assertEquals("airborne effervescent health formula onthego packets", filter.toString());
	}

	public void testDiactricsRemoval() {
		TermCoder filter = new DiacriticsRemoval(new Term("vermont crème fraîche"));
		assertEquals("vermont creme fraiche", filter.toString());
	}

	public void testDiactricsRemoval2() {
		TermCoder filter = new DiacriticsRemoval(new Term("von hövel oberemmler hütte riesling spätlese"));
		assertEquals("von hovel oberemmler hutte riesling spatlese", filter.toString());
	}

	public void testDiactricsRemoval3() {
		TermCoder filter = new DiacriticsRemoval(new Term("ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ"));
		assertEquals("AAAAAAAeCEEEEIIIIDNOOOOO×OUUUUYThssaaaaaaaeceeeeiiiidnooooo÷ouuuuythy", filter.toString());
	}

	public void testIsoLatin1Filter() {
		TermCoder filter = new IsoLatin1Filter(new Term("one two­fdsadsf"));
		assertEquals("one two-fdsadsf", filter.toString());
	}

	public void testNumberPermuter() {
		SynonymDictionary dictionary = new MySynonymDictionary();
		TermCoder filter = new NumberPermuter(dictionary, new Term("4 c kosher bread crumbs"));
		assertEquals("4 c kosher bread crumbs", filter.toString());
		Synonym[] synonyms = dictionary.getSynonymsForPrefix("4c");
		assertNotNull(synonyms);
		assertEquals(0, synonyms.length);
	}

	public void testNumberPermuter2() {
		SynonymDictionary dictionary = new MySynonymDictionary();
		TermCoder filter = new NumberPermuter(dictionary, new Term("4c kosher bread crumbs omega3"));
		assertEquals("4c kosher bread crumbs omega3", filter.toString());
		Synonym[] synonyms = dictionary.getSynonymsForPrefix("4c");
		assertNotNull(synonyms);
		assertEquals(1, synonyms.length);
	}
	
	public void testNumberPermuter3() {
		SynonymDictionary dictionary = SynonymDictionary.createNumberSynonyms();
		TermCoder filter = new SynonymPermuter(dictionary, new Term("1"));
		assertEquals("1 one", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("2"));
		assertEquals("2 two", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("3"));
		assertEquals("3 three", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("4"));
		assertEquals("4 four", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("5"));
		assertEquals("5 five", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("6"));
		assertEquals("6 six", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("7"));
		assertEquals("7 seven", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("8"));
		assertEquals("8 eight", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("9"));
		assertEquals("9 nine", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("10"));
		assertEquals("10 ten", filter.toString());
		filter = new SynonymPermuter(dictionary, new Term("0"));
		assertEquals("0 null zero", filter.toString());
		assertEquals("0 null zero", filter.toString());
		SynonymDictionary mydict = new MySynonymDictionary();
		mydict.addSynonyms(dictionary);
		filter = new SearchTermNormalizer(mydict, new Term("4c"));
		filter = new SynonymPermuter(mydict, filter);
		assertEquals("4c 4 c fourc four c", filter.toString());
		filter = new SearchTermNormalizer(mydict, new Term("4-c"));
		filter = new SynonymPermuter(mydict, filter);
		assertEquals("4c 4 c fourc four c", filter.toString());
		filter = new SearchTermNormalizer(mydict, new Term("four-c"));
		filter = new SynonymPermuter(mydict, filter);
		assertEquals("fourc 4 c four c 4c", filter.toString());
	}

	public void testNumberPermuter4() {
		SynonymDictionary dictionary = SynonymDictionary.createNumberSynonyms();
		TermCoder filter = new SearchTermNormalizer(dictionary, new Term("2-4lbs"));
		assertEquals("2 4lbs", filter.toString());
	}

	public void testPercentConv() {
		TermCoder filter = new PercentConv(new Term("100% apple juice"));
		assertEquals("100 percent apple juice", filter.toString());
	}

	public void testPercentConv2() {
		TermCoder filter = new PercentConv(new Term("100 % apple juice %"));
		assertEquals("100 percent apple juice percent", filter.toString());
	}

	public void testPlusSignConv() {
		TermCoder filter = new PlusSignConv(Arrays.asList(new Term[] { new Term("a + d diaper ointment"),
				new Term("citracal+d caplets"), new Term("huggies little swimmers disposable swimpants size l, 32lb+") }));
		assertEquals("a plus d diaper ointment a and d diaper ointment citracal plus d caplets citracal and d caplets "
				+ "huggies little swimmers disposable swimpants size l, 32lb plus", filter.toString());
	}

	public void testPunctuationCoder() {
		TermCoder filter = new PunctuationCoder(Arrays.asList(new Term[] {
				new Term(" whole t-bone & porterhouse (cut into 0.5\" thick steaks)"), new Term("(a)muse me!"),
				new Term("(bad)neutrogena norwegian hand cream"), new Term("a perfect pour(cieux)!   	"),
				new Term("earth's best organic baby food vegetable variety pack: sw. potatoes, peas/rice, carrots, squash") }));
		assertEquals(Term.join(Arrays.asList("whole t-bone & porterhouse cut into 0.5\" thick steaks", "amuse me",
				"badneutrogena norwegian hand cream", "a perfect pourcieux",
				"earth's best organic baby food vegetable variety pack sw potatoes peas/rice carrots squash"), " "), filter
				.toString());
	}

	public void testSlashConv() {
		TermCoder filter = new SlashConv(Arrays.asList(new Term[] { new Term("I can't live w/or w/o you"),
				new Term("spaghetti w/oregano/basil"), new Term("peas/rice") }));
		assertEquals("I can't live with or without you spaghetti with oregano basil peas rice", filter.toString());
	}

	public void testSearchTermNormalizer() {
		TermCoder filter = new SearchTermNormalizer(new Term("\""));
		assertEquals("", filter.toString());
		filter = new SearchTermNormalizer(new Term("''''"));
		assertEquals("", filter.toString());
	}

	public void testInitialisms() {
		TermCoder filter = new InitialismPermuter(new Term("a.c. milan"));
		assertEquals("a c milan ac milan", filter.toString());
		filter = new InitialismPermuter(new Term("a.c milan"));
		assertEquals("a c milan ac milan", filter.toString());
		filter = new InitialismPermuter(new Term("i.b.m."));
		assertEquals("i b m ibm", filter.toString());
		filter = new InitialismPermuter(new Term("i. b. m."));
		assertEquals("i. b. m.", filter.toString());
	}

	public void testCommaPermuter() {
		TermCoder filter = new CommaPermuter(new Term("Is it (a)muse me!, , my lo,ve,t1,0ta? 100, big"));
		filter = new PunctuationCoder(filter);
		assertEquals(Arrays.asList(new Term[] { new Term("Is it amuse me"), new Term("my lo"), new Term("ve"),
				new Term("t10ta 100"), new Term("big") }), filter.getTerms());
	}

	public void testQuotationConv() {
		TermCoder filter = new QuotationConv(new Term("\"hello\" \"9\""));
		assertEquals(Arrays.asList(new Term[] { new Term("hello 9 inch") }), filter.getTerms());
	}

	public void testQuotationPermuter() {
		TermCoder filter = new QuotationPermuter(Arrays.asList(new Term[] { new Term("\"hello\" \"9\""),
				new Term("\"bello\" \"9\"\"") }));
		assertEquals(Arrays.asList(new Term[] { new Term("hello 9\""), new Term("hello 9 inch"), new Term("bello 9") }), filter
				.getTerms());
	}

	public void testSpellingPunctuationCoder() {
		TermCoder filter = new SpellingPunctuationCoder(Arrays.asList(new Term[] {
				new Term(" whole t-bone & porterhouse (cut into 0.5\" thick steaks)"), new Term("(a)muse me!"),
				new Term("(bad)neutrogena norwegian hand cream"), new Term("a perfect pour(cieux)!   	"),
				new Term("earth's best organic baby food vegetable variety pack: sw. potatoes, peas/rice, carrots, squash"),
				new Term("a.c. milan") }));
		assertEquals(Arrays.asList(new Term[] { new Term("whole t-bone & porterhouse cut into 0.5\" thick steaks"),
				new Term("amuse me"), new Term("badneutrogena norwegian hand cream"), new Term("a perfect pourcieux"),
				new Term("earth's best organic baby food vegetable variety pack sw. potatoes, peas/rice, carrots, squash"),
				new Term("a.c. milan") }), filter.getTerms());
	}

	public void testAmpersandPermuter() {
		TermCoder filter = new AmpersandPermuter(new Term("apple & pear"));
		assertEquals(Arrays.asList(new Term[] { new Term("apple & pear"), new Term("apple and pear") }), filter.getTerms());
		filter = new AmpersandPermuter(new Term("apple&pear"));
		assertEquals(Arrays.asList(new Term[] { new Term("apple&pear"), new Term("apple and pear") }), filter.getTerms());
		filter = new AmpersandPermuter(new Term("apple &pear"));
		assertEquals(Arrays.asList(new Term[] { new Term("apple &pear"), new Term("apple and pear") }), filter.getTerms());
	}

	public void testAmpersandSpellingPermuter() {
		TermCoder filter = new AmpersandSpellingPermuter(new Term("apple & pear"));
		assertEquals(Arrays.asList(new Term[] { new Term("apple & pear"), new Term("apple and pear") }), filter.getTerms());
		filter = new AmpersandSpellingPermuter(new Term("apple&pear"));
		assertEquals(Arrays.asList(new Term[] { new Term("apple&pear"), new Term("apple and pear"), new Term("apple & pear") }),
				filter.getTerms());
		filter = new AmpersandSpellingPermuter(new Term("apple &pear"));
		assertEquals(Arrays.asList(new Term[] { new Term("apple &pear"), new Term("apple and pear"), new Term("apple & pear") }),
				filter.getTerms());
	}

	public void testSpellingInitialisms() {
		TermCoder filter = new SpellingInitialismPermuter(new Term("a.c. milan"));
		assertEquals(Arrays.asList(new Term[] { new Term("a.c. milan"), new Term("ac milan") }), filter.getTerms());
		filter = new SpellingInitialismPermuter(new Term("a.c milan"));
		assertEquals(Arrays.asList(new Term[] { new Term("a.c milan"), new Term("ac milan") }), filter.getTerms());
		filter = new SpellingInitialismPermuter(new Term("i.b.m."));
		assertEquals(Arrays.asList(new Term[] { new Term("i.b.m."), new Term("ibm") }), filter.getTerms());
		filter = new SpellingInitialismPermuter(new Term("i. b. m."));
		assertEquals("i. b. m.", filter.toString());
	}

	public void testDictionaryPermuter() {
		TermCoder filter = new DictionaryPermuter(new Term("huggies little swimmers disposable swimpants"));
		assertEquals(Arrays.asList(new Term[] { new Term("huggies"), new Term("little"), new Term("swimmers"),
				new Term("disposable"), new Term("swimpants"), new Term("huggies little"), new Term("little swimmers"),
				new Term("swimmers disposable"), new Term("disposable swimpants"), new Term("huggies little swimmers"),
				new Term("little swimmers disposable"), new Term("swimmers disposable swimpants") }), filter.getTerms());
		filter = new DictionaryPermuter(Arrays.asList(new Term[] {}));
		assertEquals(Arrays.asList(new Term[] {}), filter.getTerms());
		filter = new DictionaryPermuter(Arrays.asList(new Term[] { new Term("scotch whisky"), new Term("iron maiden"),
				new Term("irish whisky") }));
		assertEquals(Arrays.asList(new Term[] { new Term("scotch"), new Term("whisky"), new Term("iron"), new Term("maiden"),
				new Term("irish"), new Term("scotch whisky"), new Term("iron maiden"), 	new Term("irish whisky") }), filter.getTerms());
	}

	public void testApostrophePermuter() {
		TermCoder filter = new ApostrophePermuter(new Term("amy's burger"));
		assertEquals(Arrays.asList(new Term[] { new Term("amy's burger"), new Term("amy burger") }), filter.getTerms());
	}

	public void testApostrophePermuter2() {
		TermCoder filter = new ApostrophePermuter(new Term("agnesi capelli d'angelo"));
		assertEquals(Arrays.asList(new Term[] { new Term("agnesi capelli d'angelo"), new Term("agnesi capelli angelo") }), filter.getTerms());
	}
	
	public void testDiactricsPermuter() {
		TermCoder filter = new DiacriticsPermuter(new Term("vermont crème fraîche"));
		assertEquals(Arrays.asList(new Term[] { new Term("vermont crème fraîche"), new Term("vermont creme fraiche") }), filter
				.getTerms());
	}

	public void testDashAsteriskSplitPermuter() {
		TermCoder filter = new DashAsteriskSplitPermuter(new Term("airborne effervescent health formula - on-the-go packets"));
		assertEquals(Arrays.asList(new Term[] { new Term("airborne effervescent health formula on-the-go packets"),
				new Term("airborne effervescent health formula on the go packets") }), filter.getTerms());
	}

	public void testDashAsteriskSplitPermuter2() {
		TermCoder filter = new DashAsteriskSplitPermuter(new Term("t-bone"));
		assertEquals(Arrays.asList(new Term[] { new Term("t-bone"), new Term("t bone") }), filter.getTerms());
	}

	public void testSpellingTermNormalizer() {
		TermCoder filter = new SpellingTermNormalizer(new Term("t-bone"));
		assertEquals(Arrays.asList(new Term[] { new Term("t-bone") }), filter.getTerms());
		filter = new SpellingTermNormalizer(new Term("''''"));
		assertEquals(Arrays.asList(new Term[] { new Term("'''") }), filter.getTerms());
	}

	public void testSpellingTermNormalizer2() {
		TermCoder filter = new SpellingTermNormalizer(new Term("3-ply xxx"));
		assertEquals(Arrays.asList(new Term[] { new Term("3-ply xxx"), new Term("three-ply xxx") }), filter.getTerms());
		filter = new SpellingTermNormalizer(new Term("2-4lbs xxx"));
		assertEquals(Arrays.asList(new Term[] { new Term("2-4lbs xxx") }), filter.getTerms());
	}
	
	public void testSearchTermNormalizer2() {
		TermCoder filter = new SearchTermNormalizer(Arrays.asList(new Term[] { new Term("1.5\""), new Term("1.5 inch") }));
		assertEquals(Arrays.asList(new Term[] { new Term("1.5 inch"), new Term("1.5 inch") }), filter.getTerms());
	}

	public void testSearchTermNormalizer3() {
		SynonymDictionary dictionary = new MySynonymDictionary();
		dictionary.addSynonyms(SynonymDictionary.createNumberSynonyms());
		TermCoder filter = new SearchTermNormalizer(dictionary, Arrays.asList(new Term[] {
				new Term("4-c kosher bread crumbs omega-3"), new Term("12-14lb turkey dinner"), new Term("2-4lbs diaper") }));
		assertEquals("4c kosher bread crumbs omega3 12 14lb turkey dinner 2 4lbs diaper", filter.toString());
		Synonym[] synonyms = dictionary.getSynonymsForPrefix("4c");
		assertNotNull(synonyms);
		assertEquals(1, synonyms.length);
		assertEquals(new HashSet<Term>(Arrays.asList(new Term[] { new Term("4 c"), new Term("fourc"), new Term("four c") })), synonyms[0].getSynonyms());
		synonyms = dictionary.getSynonymsForPrefix("14lb");
		assertNotNull(synonyms);
		assertEquals(1, synonyms.length);
		assertEquals(new HashSet<Term>(Arrays.asList(new Term[] { new Term("14 lb") })), synonyms[0].getSynonyms());
		synonyms = dictionary.getSynonymsForPrefix("omega3");
		assertNotNull(synonyms);
		assertEquals(1, synonyms.length);
		assertEquals(new HashSet<Term>(Arrays.asList(new Term[] { new Term("omega 3"), new Term("omegathree"), new Term("omega three") })), synonyms[0].getSynonyms());
		synonyms = dictionary.getSynonymsForPrefix("12");
		assertNotNull(synonyms);
		assertEquals(0, synonyms.length);
		synonyms = dictionary.getSynonymsForPrefix("2");
		assertNotNull(synonyms);
		assertEquals(1, synonyms.length);
		assertEquals(new HashSet<Term>(Arrays.asList(new Term[] { new Term("two") })), synonyms[0].getSynonyms());
	}

	public void testDotPermuter() {
		TermCoder filter = new SpellingTermNormalizer(Arrays.asList(new Term[] { new Term("1.5\""), new Term("a. b. c."),
				new Term("Going...Going...'gogne!") }));
		assertEquals(Arrays.asList(new Term[] { new Term("1.5\""), new Term("1.5 inch"), new Term("a. b. c."), new Term("a b c"),
				new Term("going...going...'gogne"), new Term("going going gogne") }), filter.getTerms());
	}
	
	public void testApproximationsPermuter() {
		ApproximationsPermuter permuter = new ApproximationsPermuter(new Term("one two"));
		assertEquals(Collections.singletonList(Arrays.asList(new Term[] { new Term("two"), new Term("one") })), permuter.permute());

		permuter = new ApproximationsPermuter(new Term("one two three"));
		assertEquals(Collections.singletonList(Arrays.asList(new Term[] { new Term("two three"), new Term("one three"),
				new Term("one two") })), permuter.permute());

		permuter = new ApproximationsPermuter(new Term("one two three four"));
		List<List<Term>> expected = new ArrayList<List<Term>>();
		expected.add(Arrays.asList(new Term[] { new Term("two three four"), new Term("one three four"), new Term("one two four"),
				new Term("one two three") }));
		expected.add(Arrays.asList(new Term[] { new Term("three four"), new Term("two four"), new Term("two three"),
				new Term("one four"), new Term("one three"), new Term("one two") }));
		assertEquals(expected, permuter.permute());

		permuter = new ApproximationsPermuter(new Term("one two three four five"));
		expected = new ArrayList<List<Term>>();
		expected.add(Arrays.asList(new Term[] { new Term("two three four five"), new Term("one three four five"),
				new Term("one two four five"), new Term("one two three five"), new Term("one two three four") }));
		expected.add(Arrays.asList(new Term[] { new Term("three four five"), new Term("two four five"), new Term("two three five"),
				new Term("two three four"), new Term("one four five"), new Term("one three five"), new Term("one three four"),
				new Term("one two five"), new Term("one two four"), new Term("one two three") }));
		assertEquals(expected, permuter.permute());
	}
}
