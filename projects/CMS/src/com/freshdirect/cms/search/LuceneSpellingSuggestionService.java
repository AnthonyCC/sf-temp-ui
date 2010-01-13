package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Give spelling suggestions for query terms.
 * 
 * @author istvan
 *
 */
public class LuceneSpellingSuggestionService {
	
	final static Logger LOGGER = LoggerFactory.getInstance(LuceneSpellingSuggestionService.class);
	
	// Do not try to search for these
	private final static Set<String> stopWords = new HashSet(Arrays.asList(new String[] {"with","and","of","or"}));
	
	// spell checker instance, created at construction
	private SpellChecker checker;
	
	// searcher instance, created at construction
	private Searcher searcher;
	
	// directory of indexes, created at construction
	private Directory directory;
	
	// indexes to search
	private List indexes;
	
	/**
	 * Interface to decide whether to accept a suggestion for a term.
	 * @author istvan
	 *
	 */
	public interface AcceptableEditDistanceFilter {
		/**
		 * Accept suggestion for term.
		 * @param q query term
		 * @param s suggestion
		 * @param d edit distance
		 * @return whether to accept s for q
		 */
		public boolean accept(String q, String s, int d);
	}
	
	/**
	 * Do not reject suggestion by edit distance.
	 */
	public static AcceptableEditDistanceFilter all = new AcceptableEditDistanceFilter() {
		
		/**
		 * Return true.
		 * @return true
		 */
		public boolean accept(String q, String s, int d) { return true; }
	};
	
	/**
	 * Constructor.
	 * 
	 * @param indexLocation directory location of indices
	 * @param indexes index fields to use
	 * @param reader index reader
	 * @throws IOException
	 */
	public LuceneSpellingSuggestionService(String indexLocation, List indexes, IndexReader reader) throws IOException {
		
		directory = FSDirectory.open(new File(indexLocation));
		
		this.indexes = indexes;
		
		if (!IndexReader.indexExists(directory)) {
			try {
				LOGGER.warn(
				"Directory index does not exist, the the spell checker will try to obtain a write lock to create the index");
				indexWords(reader);
			} catch (Exception e) {
				LOGGER.error("Spellcheker indexing of words have failed.",e);
			}
		} else {
			LOGGER.debug(
					"Directory index exists, OK the spell checker will just use it");
		}
		this.checker = new SpellChecker(directory, new EditDistanceCalculator.ModifiedLevenshtein());
		
		searcher = new IndexSearcher(directory);
	}
	
	/**
	 * Index specifically the words for spell-checking of the index files used. 
	 * @param reader index reader
	 * @throws IOException
	 */
	public void indexWords(IndexReader reader) throws IOException {
		LOGGER.info("Started indexing words for spell-checker");
		for(Iterator i = indexes.iterator(); i.hasNext(); ) {
			String index = (String)i.next();
			LOGGER.info("About to index field \"" + index + "\" for spell-checker");
			Dictionary dictionary = new LuceneDictionary(reader,index);
			this.checker.indexDictionary(dictionary);
			LOGGER.info("Indexing of field \"" + index + "\" completed");
		}
		LOGGER.info("Indexing words for spell-checker complete");
	}
	
	
	/**
	 * The work horse method.
	 * 
	 * @param query possibly term query
	 * @param totalTerms total terms in original query 
	 * @param totalChars total number of content characters in original query
	 * @param actualTerms number of terms in query (the parameter)
	 * @param termIndex index of term in query (or -1 if there are more than one terms)
	 * @param maxHits max hits to retrieve by Lucene
	 * @param df distance filter
	 * @param ED edit distance calculator
	 * @param grammarHits data structure to hold grammatical hits
	 * @param contentHits data structure to hold content hits (hits that come about by popular content, only for multi-term queries)
	 * @throws IOException
	 */
	private void collectRawSpellingHits(
		final String query, int totalTerms, int totalChars, int actualTerms, int termIndex, int maxHits, final AcceptableEditDistanceFilter df, 
		final EditDistanceCalculator ED, Map<String,SpellingHit.GrammaticalMatch> grammarHits, Map<ContentKey,SpellingHit.ContentKeyMatch> contentHits) throws IOException {
		
		// get suggestions
		String[] words = checker.suggestSimilar(query,maxHits);
		LOGGER.info("collect raw spelling hits:"+query + " :: " + Arrays.asList(words));
		// if multi-term append the original query as a candidate
		// only if it exists in the index.
		// E.g. query = "dite coke", dite will be rejected, but coke is ok
		if (termIndex != -1) {
			BooleanQuery tquery = new BooleanQuery();
			for(Iterator idx = indexes.iterator(); idx.hasNext(); ) {
				String index = (String)idx.next();
				tquery.add(new TermQuery(new Term(index,query)),BooleanClause.Occur.SHOULD);
			}
			if (searcher.search(tquery, 1).totalHits > 0) {
				String[] newWords = new String[words.length + 1];
				System.arraycopy(words,0, newWords, 0, words.length);
				newWords[words.length] = query;
				words = newWords;
			}
		} else {
		    // if not multi term, try to find it in the autocompleter
		    if (query.length()>2) {
		        String prefix = query.substring(0, 2);
		        List<String> autocompletions = ContentSearch.getInstance().getAutocompletions(prefix, new AutocompleteService.Predicate() {
                            
                            @Override
                            public boolean allow(String s) {
                                int distance = ED.calculate(query,s);
                                return df.accept(query,s,distance);
                            }
                        });
                        LOGGER.info("words from the autocompletions :" + prefix + " -> " + autocompletions);
		        if (autocompletions != null && autocompletions.size() > 0) {
		            for (String word : autocompletions) {
		                String normedWord = word.toLowerCase();
	                        // calculate distance
	                        int distance = ED.calculate(query,normedWord);
	                        
	                        createGrammaticalMatch(grammarHits, query, normedWord, distance, totalTerms, totalChars, actualTerms);
		            }
		        }
		    }
		}
		
		
		
		// for each suggestion
		for(int i = 0; i< words.length; ++i) {
			String normedWord = words[i].toLowerCase();
			
			// calculate distance
			int distance = ED.calculate(query,normedWord);
			
			// reject if too far
			if (!df.accept(query,words[i],distance)) {
			    continue;
			}
			
			createGrammaticalMatch(grammarHits, query, normedWord, distance, totalTerms, totalChars, actualTerms);
			
			// if multi-term
			if (termIndex != -1) {
			
				BooleanQuery tquery = new BooleanQuery();
				for(Iterator idx = indexes.iterator(); idx.hasNext(); ) {
					String index = (String)idx.next();
					tquery.add(new TermQuery(new Term(index,words[i])),BooleanClause.Occur.SHOULD);
				}
				
				// retrieve docs
				TopDocs topDocs = searcher.search(tquery, maxHits);
			
				if (topDocs.totalHits > 0) {
                                    // for each doc (content key)
                                    for (int j = 0; j < topDocs.scoreDocs.length; j++) {
                                        ContentKey key = null;
                
                                        try {
                                            key = ContentKey.decode(searcher.doc(topDocs.scoreDocs[j].doc).get(LuceneSearchService.FIELD_CONTENT_KEY));
                                            if (key != null) {
                                                // produce a content hit
                                                SpellingHit.ContentKeyMatch cm = contentHits.get(key);
                                                if (cm == null) {
                                                    cm = new SpellingHit.ContentKeyMatch(key, totalTerms);
                                                    contentHits.put(key, cm);
                                                }
                                                cm.addMatchTerm(termIndex, words[i], query, distance);
                                            }
                                        } catch (Exception e) {
                                            LOGGER.debug(e);
                                        }
                
                                    }
                                }
				
			}
		}	
	}

    private SpellingHit.GrammaticalMatch createGrammaticalMatch(Map<String, SpellingHit.GrammaticalMatch> grammarHits, String query, String normedWord,
            int distance, int totalTerms, int totalChars, int actualTerms) {
        // Produce a grammatical hit
        SpellingHit.GrammaticalMatch gm = grammarHits.get(normedWord);
        if (gm == null) {
        	gm = new SpellingHit.GrammaticalMatch(normedWord,totalChars - query.length() + distance,actualTerms,totalTerms);
        	grammarHits.put(normedWord, gm);
        } else if (distance < gm.getDistance()) {
        	grammarHits.put(normedWord, gm);
        }
        return gm;
    }
	
	/**
	 * Calls {@link #getRawSpellingHits getRawSpellingHits} for each term and as a whole.
	 * 
	 * @param query
	 * @param maxHits for Lucene (needs many, since retrieved by different logic and not edit distance)
	 * @param df distance filter
	 * @param D table to use to calculate distance
	 * @param grammarHits grammatical hits
	 * @param contentHits hits generated for popular content keys
	 */
	private void collectSpellingHits(
		String query, int maxHits, AcceptableEditDistanceFilter df, EditDistanceCalculator ED, Map<String,SpellingHit.GrammaticalMatch> grammarHits, Map<ContentKey,SpellingHit.ContentKeyMatch> contentHits)
	throws IOException {
		
		// lower case query
		String q = query.trim().toLowerCase();
		
		// chop into pieces
		StringTokenizer ts = new StringTokenizer(q," \t&,");
		StringBuffer normedQuery = new StringBuffer();
		List<String> terms = new ArrayList<String>();
		int queryChars = 0; // total number of content (useful) chars in query

		while (ts.hasMoreTokens()) {
                    String token = ts.nextToken().trim().toLowerCase();
                    // skip stop words
                    if (!stopWords.contains(token)) {
                        if (normedQuery.length() > 0) {
                            normedQuery.append(' ');
                        }
                        normedQuery.append(token);
                        terms.add(token);
                        queryChars += token.length();
                    }
                }
			
		int c = 0;
		
		// for each term
		// e.g. for "dite" and "coko" in "dite coko"
		for(Iterator<String> i = terms.iterator(); i.hasNext(); ++c) {
		    collectRawSpellingHits(i.next(), terms.size(), queryChars, 1, terms.size() > 1 ? c : -1 , maxHits, df, ED, grammarHits, contentHits);
		}
			
		// for the normed, stop word free query as a whole
		// e.g. "dite coko"
		if (terms.size() > 1) {
		    collectRawSpellingHits(normedQuery.toString(), terms.size(), queryChars, terms.size(), -1, maxHits, df, ED, grammarHits, contentHits);
		}
	}
	
	/**
	 * Get spelling suggestions and return them in order of relevance.
	 * 
	 * Order of relevance: 1. more terms matched, 2. closer edit distance
	 * 
	 * @param query query term
	 * @param maxHits hits to suggest for Lucene (should be at list 5)
	 * @param df distance filter
	 * @return suggested spellings (List<"{@link SpellingHit}>)
	 */
	public List getSpellingHits(String query, int maxHits, AcceptableEditDistanceFilter df) {
		
		Map<String, SpellingHit.GrammaticalMatch> grammarHits = new HashMap<String, SpellingHit.GrammaticalMatch>(); // purely grammatical closeness
		Map<ContentKey, SpellingHit.ContentKeyMatch> contentHits = new HashMap<ContentKey, SpellingHit.ContentKeyMatch>(); // relevant content: ... only triggered by multi-term queries
		
		// Use this edit distance calculator
		EditDistanceCalculator ED = new EditDistanceCalculator.ModifiedLevenshtein();

		try {
			collectSpellingHits(query, maxHits, df,ED,grammarHits,contentHits);
		} catch(IOException e) {
			LOGGER.debug(e);
		}
		
		List<SpellingHit> hits = new ArrayList<SpellingHit>();
		hits.addAll(grammarHits.values());
		
		// weed content key hits, we only care about those that have 
		// at least two term references
		for(Iterator<SpellingHit.ContentKeyMatch> i = contentHits.values().iterator(); i.hasNext(); ) {
			SpellingHit cm = i.next();
			if (cm.getMatchTermCount() < 2) continue;
			hits.add(cm);
		}
		
		// sort hits
		Object[] values = hits.toArray();
		Arrays.sort(
			values,
			new Comparator() {

				public int compare(Object o1, Object o2) {
					SpellingHit h1 = (SpellingHit)o1;
					SpellingHit h2 = (SpellingHit)o2;
					
					// advantage to hits that match the more terms of the original
					int diff1 = h1.getQueryTermCount() - h1.getMatchTermCount();
					int diff2 = h2.getQueryTermCount() - h2.getMatchTermCount();
					
					if (diff1 < diff2) return -1;
					else if (diff1 > diff2) return +1;
					
					// otherwise edit distance
					if (h1.getDistance() < h2.getDistance()) return -1;
					else if (h1.getDistance() > h2.getDistance()) return +1;
					
					return h1.getSuggestion().compareTo(h2.getSuggestion());
				}
			}
		);
		
		
		return Arrays.asList(values);
	}
	
	/**
	 * Get unique searh terms from hits list.
	 * 
	 * The same suggestion may be derived in sevaral ways (e.g. grammar hit and content hit),
	 * but when collated, this should not make a difference.
	 * 
	 * @param hits list of {@link SpellingHit}
	 * @return a list of spelling hits with the suggestions in the order they occur in the hits parameter 
	 */
        static public List<SpellingHit> getUniqueQueries(List<SpellingHit> hits) {
            Set<SpellingHit> seen = new TreeSet<SpellingHit>(new Comparator<SpellingHit>() {
                public int compare(SpellingHit o1, SpellingHit o2) {
                    return o1.getSuggestion().compareToIgnoreCase(o2.getSuggestion());
                }
    
            });
    
            List<SpellingHit> result = new ArrayList<SpellingHit>();
            for (SpellingHit hit : hits) {
                if (seen.contains(hit))
                    continue;
                seen.add(hit);
                result.add(hit);
            }
            return result;
        }

}
