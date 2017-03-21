package com.freshdirect.cms.search.spell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.freshdirect.cms.index.IndexingConstants;
import com.freshdirect.cms.index.LuceneManager;

public class SpellChecker {

    private static final SpellChecker INSTANCE = new SpellChecker();

    public static SpellChecker getInstance(){
        return INSTANCE;
    }
	/**
	 * Boost value for start and end grams
	 */
	private float bStart = 2.0f;
	private float bEnd = 1.0f;

	private StringDistance sd;
	private LuceneManager luceneManager;

	private SpellChecker() {
	    luceneManager = LuceneManager.getInstance();
	    sd = new CsongorDistance();
	}

	/**
	 * Sets the {@link StringDistance} implementation for this {@link SpellChecker} instance.
	 * 
	 * @param sd
	 *            the {@link StringDistance} implementation for this {@link SpellChecker} instance
	 */
	public void setStringDistance(StringDistance sd) {
		this.sd = sd;
	}

	/**
	 * Returns the {@link StringDistance} instance used by this {@link SpellChecker} instance.
	 * 
	 * @return the {@link StringDistance} instance used by this {@link SpellChecker} instance.
	 */
	public StringDistance getStringDistance() {
		return sd;
	}

	/**
	 * Suggest similar words.
	 * 
	 * <p>
	 * As the Lucene similarity that is used to fetch the most relevant n-grammed terms is not the same as the edit distance
	 * strategy used to calculate the best matching spell-checked word from the hits that Lucene found, one usually has to retrieve
	 * a couple of numSug's in order to get the true best match.
	 * 
	 * <p>
	 * I.e. if numSug == 1, don't count on that suggestion being the best one. Thus, you should set this value to <b>at least</b> 5
	 * for a good suggestion.
	 * 
	 * @param indexPath
	 * 
	 * @param phrase
	 *            the word you want a spell check done on
	 * @param numSug
	 *            the number of suggested words
	 * @throws IOException
	 *             if the underlying index throws an {@link IOException}
	 * @return SpellingHit[]
	 */
	public List<SpellingHit> suggestSimilar(String indexPath, String phrase, int numSug) throws IOException {
		return suggestSimilar(indexPath, phrase, numSug,  null);
	}

	/**
	 * Suggest similar words (optionally restricted to a field of an index).
	 * 
	 * <p>
	 * As the Lucene similarity that is used to fetch the most relevant n-grammed terms is not the same as the edit distance
	 * strategy used to calculate the best matching spell-checked word from the hits that Lucene found, one usually has to retrieve
	 * a couple of numSug's in order to get the true best match.
	 * 
	 * <p>
	 * I.e. if numSug == 1, don't count on that suggestion being the best one. Thus, you should set this value to <b>at least</b> 5
	 * for a good suggestion.
	 * 
	 * @param indexPath
	 * @param phrase
	 *            the word you want a spell check done on
	 * @param numSug
	 *            the number of suggested words
	 * @param field
	 *            the field of the user index: if field is not null, the suggested words are restricted to the words present in this
	 *            field.
	 * @throws IOException
	 *             if the underlying index throws an {@link IOException}
	 * @return String[] the sorted list of the suggest words with these 2 criteria: first criteria: the edit distance, second
	 *         criteria (only if restricted mode): the popularity of the suggest words in the field of the user index
	 */
    public List<SpellingHit> suggestSimilar(String indexPath, String phrase, int numSug, String field) throws IOException {
        phrase = CsongorDistance.purify(phrase).trim();
        final int length = phrase.length();

        BooleanQuery query = new BooleanQuery();
        String[] grams;
        String key;

        for (int ng = getMin(length); ng <= getMax(length); ng++) {

            key = "gram" + ng; // form key

            grams = formGrams(phrase, ng); // form word into ngrams (allow dups too)

            if (grams.length == 0) {
                continue; // hmm
            }

            if (bStart > 0) { // should we boost prefixes?
                add(query, "start" + ng, grams[0], bStart); // matches start of word

            }
            if (bEnd > 0) { // should we boost suffixes
                add(query, "end" + ng, grams[grams.length - 1], bEnd); // matches end of word

            }
            for (int i = 0; i < grams.length; i++) {
                add(query, key, grams[i]);
            }
        }

        int maxHits = 50 * numSug;

        TopDocs hits = luceneManager.search(indexPath, query, maxHits);
        SuggestWordQueue sugQueue = new SuggestWordQueue(maxHits);

        // go thru more than 'maxr' matches in case the distance filter triggers
        SuggestWord sugWord = new SuggestWord();
        List<Document> searchHits = luceneManager.convertSearchHits(indexPath, hits, maxHits);
        for (Document document : searchHits) {
            sugWord.searchTerm = document.get(IndexingConstants.F_SEARCH_TERM);
            sugWord.spellingTerm = document.get(IndexingConstants.F_SPELLING_TERM);

            // edit distance
            sugWord.distance = sd.getDistance(phrase, sugWord.searchTerm);
            if (document.get(IndexingConstants.F_IS_SYNONYM) != null)
                sugWord.distance++; // we say that this is a good candidate but we add a penalty point

            sugQueue.insertWithOverflow(sugWord);
            sugWord = new SuggestWord();
        }

        int minDistance = 0;
        List<SpellingHit> list = new ArrayList<SpellingHit>(numSug);
        for (int i = sugQueue.size() - 1; i >= 0; i--) {
            SuggestWord w = sugQueue.pop();
            if (w.distance != minDistance && list.size() > numSug) {
                break;
            }
            minDistance = w.distance;
            list.add(new SpellingHit(w.spellingTerm, w.searchTerm, w.distance));
        }

        Collections.sort(list, SpellingHit.SORT_BY_DISTANCE);
        return list;
    }

	/**
	 * Add a clause to a boolean query.
	 */
	private static void add(BooleanQuery q, String name, String value, float boost) {
		Query tq = new TermQuery(new Term(name, value));
		tq.setBoost(boost);
		q.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));
	}

	/**
	 * Add a clause to a boolean query.
	 */
	private static void add(BooleanQuery q, String name, String value) {
		q.add(new BooleanClause(new TermQuery(new Term(name, value)), BooleanClause.Occur.SHOULD));
	}

	/**
	 * Form all ngrams for a given word.
	 * 
	 * @param text
	 *            the word to parse
	 * @param ng
	 *            the ngram length e.g. 3
	 * @return an array of all ngrams in the word and note that duplicates are not removed
	 */
	private static String[] formGrams(String text, int ng) {
		int len = text.length();
		String[] res = new String[len - ng + 1];
		for (int i = 0; i < len - ng + 1; i++) {
			res[i] = text.substring(i, i + ng);
		}
		return res;
	}

	/**
	 * Check whether the word exists in the index.
	 * 
	 * @param indexPath
	 * @param word
	 * @throws IOException
	 * 
	 * @return true if the word exists in the index
	 */
	public boolean exist(String indexPath, String word) throws IOException {
		return luceneManager.getWordFrequency(indexPath, word) > 0;
	}

	/**
	 * Close the IndexSearcher used by this SpellChecker
	 * 
	 * @throws IOException
	 *             if the close operation causes an {@link IOException}
	 */
	public void close(String indexPath) throws IOException {
	    luceneManager.closeIndexSearcher(indexPath);
	}

	private int getMin(int l) {
		if (l > 7) {
			return 3;
		}
		if (l > 5) {
			return 2;
		}
		if (l == 5) {
			return 1;
		}
		return 1;
	}

	private int getMax(int l) {
		if (l > 5) {
			return 4;
		}
		if (l == 5) {
			return 3;
		}
		return Math.min(2, l);
	}
}
