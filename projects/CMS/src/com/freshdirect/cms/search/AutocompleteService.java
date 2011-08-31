package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.freshdirect.cms.search.term.AutocompleteTermNormalizer;
import com.freshdirect.cms.search.term.DiacriticsRemoval;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutocompleteService {
	private final static Logger LOGGER = LoggerFactory.getInstance(AutocompleteService.class);

	private final static String punctuation = ",;:";
	private static final String other_punctiation = "&'+-/";
	private final static String[] articles = new String[] { "a", "an", "the" };
	private final static String[] conjunctions = new String[] { "and", "or" };
	private final static String[] prepositions = new String[] { "with", "without" };
	private final static String[] conjunctions_prefix = new String[] { "and ", "or " };
	private final static String[] conjunctions_suffix = new String[] { " and", " or" };

	private boolean permute;
	private SortedMap<String, AutocompleteHit> prefixes;

	public AutocompleteService(List<AutocompleteTerm> acTerms, boolean permute) {
		this.permute = permute;
		setTerms(acTerms);
	}

	public List<String> getAutocompletions(String prefix, int maxResults) {
		Set<AutocompleteHit> result = new LinkedHashSet<AutocompleteHit>(getAutocompletionHits(prefix));

		List<String> terms = new ArrayList<String>();
		int barrier = 0;
		OUTER: while (!result.isEmpty()) {
			Iterator<AutocompleteHit> it = result.iterator();
			Map<String, AutocompleteHit> removes = new HashMap<String, AutocompleteHit>();
			MIDDLE: while (it.hasNext()) {
				AutocompleteHit hit = it.next();
				Iterator<String> ix = terms.iterator();
				for (int i = 0; i < barrier && ix.hasNext(); i++)
					ix.next();
				INNER: while (ix.hasNext()) {
					String p = ix.next();
					for (String t : hit.getTerms()) {
						if (t.startsWith(p))
							continue MIDDLE;
						if (p.startsWith(t)) {
							ix.remove();
							removes.remove(p);
							continue INNER;
						}
					}
				}
				terms.addAll(hit.getTerms());
				for (String term : hit.getTerms())
					removes.put(term, hit);
				if (terms.size() > maxResults)
					break OUTER;
			}
			for (AutocompleteHit remove : new HashSet<AutocompleteHit>(removes.values()))
				result.remove(remove);
			barrier = terms.size();
		}
		if (terms.size() > maxResults)
			terms = new ArrayList<String>(terms.subList(0, maxResults));
		// Collections.sort(terms);
		return terms;
	}

	List<AutocompleteHit> getAutocompletionHits(String prefix) {
		int pos = prefix.length() - 1;
		int wsCount = 0;
		while (pos >= 0 && prefix.charAt(pos) <= ' ') {
		    pos--;
		    wsCount++;
		}
		char[] spaces = new char[wsCount];
		for (int i = 0; i < spaces.length; i++)
			spaces[i] = ' ';
		String suffix = new String(spaces);

		AutocompleteTermNormalizer termConv = new AutocompleteTermNormalizer(new Term(prefix), true);
		prefix = termConv.getTerms().get(0).toString();
		if (prefix.isEmpty())
			return Collections.emptyList();
		prefix = prefix.concat(suffix);
		String start = prefix;
		String end = prefix + '\uffff';

		Set<Map.Entry<String, AutocompleteHit>> items = prefixes.subMap(start, end).entrySet();
		// sort according to the number of occurrences
		List<Map.Entry<String, AutocompleteHit>> sorted = new ArrayList<Map.Entry<String, AutocompleteHit>>(items);
		Collections.sort(sorted, new Comparator<Map.Entry<String, AutocompleteHit>>() {
			@Override
			public int compare(Entry<String, AutocompleteHit> e1, Entry<String, AutocompleteHit> e2) {
				int d = e2.getValue().getNumber() - e1.getValue().getNumber();
				if (d != 0)
					return d;
				else
					return e1.getKey().compareTo(e2.getKey());
			}
		});
		List<AutocompleteHit> results = new ArrayList<AutocompleteHit>();
		for (Map.Entry<String, AutocompleteHit> entry : sorted)
			results.add(entry.getValue());
		return results;
	}

	private void setTerms(List<AutocompleteTerm> acTerms) {
		LOGGER.info("term list size: " + acTerms.size());
		SortedMap<String, AutocompleteHit> prefixes = new TreeMap<String, AutocompleteHit>();

		// build
		for (AutocompleteTerm acTerm : acTerms) {
			List<Term> terms;
			if (permute)
				terms = acTerm.permute();
			else
				terms = Collections.singletonList(acTerm.getTerm());
			for (Term term : terms) {
				String prefix = DiacriticsRemoval.removeDiactrics(term.toString());
				if (punctuation.indexOf(prefix.charAt(prefix.length() - 1)) >= 0)
					continue;
				if (punctuation.indexOf(prefix.charAt(0)) >= 0)
					continue;
				if (other_punctiation.indexOf(prefix.charAt(0)) >= 0)
					continue;
				if (other_punctiation.indexOf(prefix.charAt(prefix.length() - 1)) >= 0)
					continue;
				AutocompleteHit hit;
				hit = prefixes.get(prefix);
				if (hit == null) {
					hit = new AutocompleteHit(term.toString(), acTerm.getContentKey());
					prefixes.put(prefix, hit);
				} else {
					hit.addTerm(term.toString(), acTerm.getContentKey());
				}
			}
		}
		LOGGER.info("prefixes size before compact: " + prefixes.size());

		// compact
		for (String article : articles)
			prefixes.remove(article);
		for (String conjunction : conjunctions)
			prefixes.remove(conjunction);
		for (String preposition: prepositions)
			prefixes.remove(preposition);

		List<String> removed = new ArrayList<String>();
		Iterator<String> it = prefixes.keySet().iterator();
		if (it.hasNext()) {
			String prev = it.next();
			while (it.hasNext()) {
				String prefix = it.next();
				boolean isRemoved = false;
				if (prefix.startsWith(prev) && prefixes.get(prev).isSameContentKeys(prefixes.get(prefix))) {
					removed.add(prev);
					isRemoved = true;
				}
				prev = prefix;
				if (isRemoved)
					continue;
				for (String co_prefix : conjunctions_prefix)
					if (prefix.startsWith(co_prefix)) {
						removed.add(prefix);
						continue;
					}
				for (String co_suffix : conjunctions_suffix)
					if (prefix.endsWith(co_suffix)) {
						removed.add(prefix);
						continue;
					}
			}
		}
		for (String prefix : removed)
			prefixes.remove(prefix);

		LOGGER.info("prefixes size after compact: " + prefixes.size());
		// purge
		for (AutocompleteHit hit : prefixes.values())
			hit.purge();

		this.prefixes = prefixes;
		LOGGER.info("completed processing terms");
	}
	
	public Set<String> getPrefixes() {
		return prefixes.keySet();
	}
}
