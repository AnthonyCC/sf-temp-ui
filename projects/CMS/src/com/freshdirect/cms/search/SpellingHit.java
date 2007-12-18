package com.freshdirect.cms.search;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;

/** 
 * Spelling suggestion information.
 * 
 * @author istvan
 *
 */
public abstract class SpellingHit implements Serializable, Comparable {
	
	// suggestion to give
	protected String suggestion; 
	
	/**
	 * Get suggestion.
	 * @return suggestion.
	 */
	public String getSuggestion() {
		return suggestion;
	}
	
	// edit distance
	protected int distance = -1;
	
	// term count in this match
	protected int matchTermCount = -1;
	
	// term count in original query
	protected int queryTermCount = -1;

	/**
	 * Get edit distance.
	 * @return edit distance.
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * Get match term count.
	 * @return number of terms matched by this suggestion.
	 */
	public int getMatchTermCount() {
		return matchTermCount;
	}
	
	/**
	 * Get query term count.
	 * @return number of original terms in query
	 */
	public int getQueryTermCount() {
		return queryTermCount;
	}
	
	
	/**
	 * String rep.
	 * @return {@link #getSuggestion()}
	 */
	public String toString() {
		return getSuggestion();
	}
	
	/** Constructor.
	 * 
	 * @param suggestion
	 */
	protected SpellingHit(String suggestion, int queryTermCount) {
		this.suggestion = suggestion;
		this.queryTermCount = queryTermCount;
	}
	
	/**
	 * Comparable interface method.
	 * @param o object to compare to
	 */
	public int compareTo(Object o) {
		SpellingHit oh = (SpellingHit)o;
		
		int diff1 = getQueryTermCount() - getMatchTermCount();
		int diff2 = oh.getQueryTermCount() - oh.getMatchTermCount();
		
		if (diff1 < diff2) return -1;
		else if (diff1 > diff2) return +1;
		
		if (getDistance() < oh.getDistance()) return -1;
		else if (getDistance() > oh.getDistance()) return +1;
		
		return 0;
	}
	
	/**
	 * Match produced by being close grammatically to query.
	 * @author istvan
	 */
	public static class GrammaticalMatch extends SpellingHit {
		
		private static final long serialVersionUID = 780050330234300079L;
				
		/**
		 * Constructor.
		 * @param suggestion
		 * @param distance
		 * @param matchTermCount number of terms matched by this suggestion
		 * @param queryTermCount  number of terms in original query
		 */
		public GrammaticalMatch(String suggestion, int distance, int matchTermCount, int queryTermCount) {
			super(suggestion,queryTermCount);
			this.distance = distance;
			this.matchTermCount = matchTermCount;
			
		}
		
		public int hashCode() {
			return getSuggestion().hashCode();
		}
	}
	
	/**
	 * Match created as the result of finding a popular content key.
	 * 
	 * A popular content key is one to which more than one of the original query terms and its suggested
	 * spellings can be associated with.
	 * 
	 * @author istvan
	 *
	 */
	public static class ContentKeyMatch extends SpellingHit {
		
		private static final long serialVersionUID = 7684512676306622756L;
		
		private ContentKey key;

		
		// by default, assume 3 pointers, extend as (if) needed
		private int[] termDistances;
		private String[] nameComponents;
		private String[] queryComponents;
		
		/** Ensure we have enough capacity */
		private void ensureCapacity(int index) {
			/*
			if (termDistances.length <= index) {
				int nT[] = new int[index+1];
				String nC[] = new String[index+1];
				System.arraycopy(termDistances, 0, nT, 0, termDistances.length);
				System.arraycopy(nameComponents, 0, nC, 0, nameComponents.length);
				for(int i = termDistances.length; i<= index; ++i) nT[i] = -1;
				for(int i = nameComponents.length; i<= index; ++i) nC[i] = "";
				termDistances = nT;
				nameComponents = nC;
			}
			*/
		}
		
		/**
		 * Constructor.
		 * @param key content key.
		 * @param queryTermCount number of terms in original query
		 */
		public ContentKeyMatch(ContentKey key, int queryTermCount) {
			super(null,queryTermCount);
			this.key = key;
			termDistances = new int[getQueryTermCount()];
			for(int i=0; i< getQueryTermCount(); ++i) termDistances[i] = -1;
			nameComponents = new String[getQueryTermCount()];
			for(int i=0; i< getQueryTermCount(); ++i) nameComponents[i] = "";
			queryComponents = new String[getQueryTermCount()];
			for(int i=0; i< getQueryTermCount(); ++i) queryComponents[i] = "";
			
		}
		
		/**
		 * Add a matching term.
		 * @param termIndex index of term in the original query
		 * @param word suggested replacement (maybe from the original query if exists in index)
		 * @param queryWord original queryWord
		 * @param d edit distance
		 */
		public void addMatchTerm(int termIndex, String word, String queryWord, int d) {
			ensureCapacity(termIndex);
			
			if (termDistances[termIndex] == -1 || termDistances[termIndex] < d) {
				distance = -1;
				suggestion = null;
				termDistances[termIndex] = d;
				nameComponents[termIndex] = word;
				queryComponents[termIndex] = queryWord;
			}
		}

		// lazy: calculate suggestion, distance and termcount 
		private void calculate() {
			if (distance == -1 || matchTermCount == -1 || suggestion == null) {
				matchTermCount = 0;
				distance = 0;
				StringBuffer buff = new StringBuffer();
				for(int i=0; i< termDistances.length; ++i) {
					if (termDistances[i] != -1) {
						++matchTermCount;
						distance += termDistances[i];
						if (buff.length() > 0) buff.append(' ');
						buff.append(nameComponents[i]);
					} else {
						distance += queryComponents[i].length();
					}
				}
				suggestion = buff.toString();
			}
		}
		
		/**
		 * Get suggested query.
		 * 
		 * Suggestion is composed of the suggested words appearing in order as they would in the corrected query
		 * @return suggested query
		 */
		public String getSuggestion() {
			calculate();
			return suggestion;
		}
		
		/**
		 * Get edit distance.
		 * Edit distance is calculated as the sum of the individual edit distances, so a matches with the same
		 * term count are comparable by distance.
		 * @return edit distance
		 */
		public int getDistance() {
			calculate();
			return distance;
		}
		
		/**
		 * Get query term count.
		 * @return the number of elements in the query to which suggestions were given
		 */
		public int getMatchTermCount() {
			calculate();
			return matchTermCount;
			
		}
		
		/**
		 * Get content key.
		 * @return content key
		 */
		public ContentKey getContentKey() {
			return key;
		}
		
		public int hashCode() {
			return key.hashCode();
		}
	}	
}
