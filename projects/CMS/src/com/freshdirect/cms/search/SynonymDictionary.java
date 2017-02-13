/**
 * 
 */
package com.freshdirect.cms.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.search.term.IdentityConvFactory;
import com.freshdirect.cms.search.term.Synonym;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;
import com.freshdirect.cms.search.term.TermCoderFactory;
import com.freshdirect.framework.conf.ResourceUtil;

/**
 * @author zsombor
 * @author csongor
 */
public class SynonymDictionary {
	public static final String SYNONYM_LIST_KEY = "FDFolder:synonymList";

	public static final String SPELLING_SYNONYM_LIST_KEY = "FDFolder:spellingSynonymList";

	private static String[] NUMBER_TEXT = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" };

	private final Map<String, List<Synonym>> wordMap;

	protected SynonymDictionary() {
		wordMap = new HashMap<String, List<Synonym>>();
	}

	// DO NOT CALL DIRECTLY
	private void addSynonym(Term term, Collection<Term> alternates) {
		synchronized (wordMap) {
			String firstWord = term.getTokens().get(0);
			List<Synonym> result = wordMap.get(firstWord);
			if (result == null) {
				result = new ArrayList<Synonym>();
				result.add(new Synonym(term, alternates));
				wordMap.put(firstWord, result);
			} else {
				for (Synonym s : result) {
					if (term.equals(s.getTerm())) {
						s.merge(alternates);
						return;
					}
				}
				result.add(new Synonym(term, alternates));
				Collections.sort(result, new Comparator<Synonym>() {
					@Override
					public int compare(Synonym s1, Synonym s2) {
						// reverse order by word count
						int i = -(s1.getWordCount() - s2.getWordCount());
						if (i == 0)
							return s1.getTermAsString().compareTo(s2.getTermAsString());
						else
							return i;
					}
				});
			}
		}
	}

	public void addSynonyms(Set<?> synonyms, TermCoderFactory factory) {
		synchronized (wordMap) {
			Set<Term> normalized = new HashSet<Term>();
			for (Object synonym : synonyms) {
				TermCoder filter = factory.create(synonym instanceof Term ? (Term) synonym : new Term(synonym.toString()));
				normalized.addAll(filter.getTerms());
			}
			if (normalized.size() > 1)
				for (Term synonym : normalized) {
					Set<Term> exclude = new HashSet<Term>(normalized);
					exclude.remove(synonym);
					addSynonym(synonym, exclude);
				}
		}
	}

	public void addSynonyms(String[] from, String[] to, TermCoderFactory factory) {
		Set<Term> fromTerms = new HashSet<Term>();
		for (String s : from) {
			TermCoder filter = factory.create(new Term(s));
			fromTerms.addAll(filter.getTerms());
		}
		Set<Term> toTerms = new HashSet<Term>();
		for (String s : to) {
			TermCoder filter = factory.create(new Term(s));
			toTerms.addAll(filter.getTerms());
		}
		for (Term synonym : fromTerms)
			addSynonym(synonym, toTerms);
	}
	
	public void addSynonyms(SynonymDictionary dictionary) {
		synchronized (dictionary.wordMap) {
			for (List<Synonym> synonyms : dictionary.wordMap.values())
				for (Synonym synonym : synonyms) {
					addSynonym(synonym.getTerm(), synonym.getSynonyms());
				}
		}
	}

	public Synonym[] getSynonymsForPrefix(String firstWord) {
		synchronized (wordMap) {
			List<Synonym> synonyms = wordMap.get(firstWord);
			if (synonyms == null)
				return new Synonym[0];
			Collections.sort(synonyms, new Comparator<Synonym>() {
				@Override
				public int compare(Synonym o1, Synonym o2) {
					int d = o2.getWordCount() - o1.getWordCount();
					if (d != 0)
						return d;
					return o1.getTermAsString().compareTo(o2.getTermAsString());
				}
			});
			return synonyms.toArray(new Synonym[0]);
		}
	}

	public Synonym getSynonymForWord(String word) {
		synchronized (wordMap) {
			List<Synonym> synonyms = wordMap.get(word);
			if (synonyms != null)					
				for (Synonym synonym : synonyms)
					if (synonym.getWordCount() == 1)
						return synonym;
			return null;
		}
	}
	
	public Map<String, List<Synonym>> getSynonyms() {
		return Collections.unmodifiableMap(wordMap);
	}

	public void parseSynonymes(String resource) throws IOException {
		parseSynonymes(ResourceUtil.openResource(resource));
	}

	public void parseSynonymes(InputStream input) throws IOException {
		parseSynonymes(new BufferedReader(new InputStreamReader(input)));
	}

	public void parseSynonymes(URL url) throws IOException {
		parseSynonymes(new BufferedReader(new InputStreamReader(url.openStream())));
	}

	public void parseSynonymes(File file) throws IOException {
		parseSynonymes(new BufferedReader(new FileReader(file)));
	}

	public void parseSynonymes(BufferedReader reader) throws IOException {
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();
			if (line.length() > 0 && line.charAt(0) != '#') {
				parseSynonymLine(line);
			}
		}
	}

	private void parseSynonymLine(String line) {
		String[] keyValue = StringUtils.split(line, ':');
		if (keyValue.length >= 2) {
			String keys = keyValue[0];
			String values = keyValue[1];
			String[] keyArray = StringUtils.split(keys, ',');
			String[] valueArray = StringUtils.split(values, ',');
			Set<String> synonyms = new HashSet<String>(keyArray.length + valueArray.length);
			synonyms.addAll(Arrays.asList(keyArray));
			synonyms.addAll(Arrays.asList(valueArray));
			addSynonyms(synonyms, new IdentityConvFactory());
		}
	}
	
	public static SynonymDictionary createNumberSynonyms() {
		SynonymDictionary dict = new SynonymDictionary();
		IdentityConvFactory factory = new IdentityConvFactory();
		{
			Set<String> synonyms = new HashSet<String>();
			synonyms.add("0");
			synonyms.add("null");
			dict.addSynonyms(synonyms, factory);
		}
		for (int i = 0; i < NUMBER_TEXT.length; i++) {
			String text = NUMBER_TEXT[i];
			Set<String> synonyms = new HashSet<String>();
			synonyms.add(Integer.toString(i));
			synonyms.add(text);
			dict.addSynonyms(synonyms, factory);
		}
		return dict;
	}

	public static SynonymDictionary createFromCms(TermCoderFactory factory) {
		SynonymDictionary dict = new SynonymDictionary();
		CmsManager instance = CmsManager.getInstance();
		ContentNodeI synRootNode = instance.getContentNode(ContentKey.getContentKey(SynonymDictionary.SYNONYM_LIST_KEY));
		if (synRootNode == null)
			return dict;
		Set<ContentKey> synonymKeys = ContentNodeUtil.collectReachableKeys(synRootNode, FDContentTypes.SYNONYM, instance, DraftContext.MAIN);
		Map<ContentKey, ContentNodeI> synonymNodes = instance.getContentNodes(synonymKeys);
		for (Iterator<ContentNodeI> contentNodeIterator = synonymNodes.values().iterator(); contentNodeIterator.hasNext();) {
			ContentNodeI node = contentNodeIterator.next();
			String[] from = getSynonymFromValues(node);
			String[] to = getSynonymToValues(node);
			if (from != null && to != null && to.length > 0) {
				boolean bidirectional = Boolean.TRUE.equals(node.getAttributeValue("bidirectional"));
				if (bidirectional) {
					Set<String> synonyms = new HashSet<String>(to.length + 1);
					for (String word : from) {
						synonyms.add(word);
					}
					for (String word : to) {
						synonyms.add(word);
					}
					dict.addSynonyms(synonyms, factory);
				} else {
					dict.addSynonyms(from, to, factory);
				}
			}
		}
		return dict;
	}

	public static SynonymDictionary createSpellingFromCms(TermCoderFactory factory) {
		SynonymDictionary dict = new SynonymDictionary();
		CmsManager instance = CmsManager.getInstance();

		ContentNodeI synRootNode = instance.getContentNode(ContentKey.getContentKey(SynonymDictionary.SPELLING_SYNONYM_LIST_KEY));
		if (synRootNode == null)
			return dict;
		Set<ContentKey> synonymKeys = ContentNodeUtil.collectReachableKeys(synRootNode, FDContentTypes.SPELLING_SYNONYM, instance, DraftContext.MAIN);
		Map<ContentKey, ContentNodeI> synonymNodes = instance.getContentNodes(synonymKeys);
		for (Iterator<ContentNodeI> contentNodeIterator = synonymNodes.values().iterator(); contentNodeIterator.hasNext();) {
			ContentNodeI node = contentNodeIterator.next();
			String[] from = getSynonymFromValues(node);
			String[] to = getSynonymToValues(node);
			if (from != null && to != null && to.length > 0) {
				boolean bidirectional = Boolean.TRUE.equals(node.getAttributeValue("bidirectional"));
				if (bidirectional) {
					Set<String> synonyms = new HashSet<String>(to.length + 1);
					for (String word : from) {
						synonyms.add(word);
					}
					for (String word : to) {
						synonyms.add(word);
					}
					dict.addSynonyms(synonyms, factory);
				} else {
					dict.addSynonyms(from, to, factory);
				}
			}
		}
		return dict;
	}

    /**
     * @param node
     * @return
     */
    public static String[] getSynonymToValues(ContentNodeI node) {
        return trimAndLowerCase(ContentNodeUtil.getStringAttribute(node, "synonymValue").split(","));
    }

    /**
     * @param node
     * @return
     */
    public static String[] getSynonymFromValues(ContentNodeI node) {
        return trimAndLowerCase(ContentNodeUtil.getStringAttribute(node, "word").split(","));
    }

    /**
     * @param ret
     */
    static String[] trimAndLowerCase(String[] ret) {
        for (int i = 0;i<ret.length;i++) {
            ret[i] = ret[i].toLowerCase().trim();
        }
        return ret;
    }
}
