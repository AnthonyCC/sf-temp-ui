package com.freshdirect.storeapi.spelling.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.freshdirect.storeapi.content.SearchResults;
import com.freshdirect.storeapi.search.SearchHit;

public class SpellingCandidate implements Serializable, Comparable<SpellingCandidate> {
	private static final long serialVersionUID = 4189833530145179270L;

	public static List<SearchResults> extractSearchResults(Collection<SpellingCandidate> candidates) {
		List<SearchResults> searchResultsList = new ArrayList<SearchResults>(candidates.size());
		for (SpellingCandidate candidate : candidates)
			searchResultsList.add(candidate.searchResults);
		return searchResultsList;
	}
	
	private SpellingHit hit;

	private boolean replaceOriginal;

	private Collection<SearchHit> searchHits;

	private SearchResults searchResults;

	public SpellingCandidate(SpellingHit hit) {
		super();
		this.hit = hit;
		this.searchHits = Collections.emptyList();
		this.searchResults = SearchResults.EMPTY_SEARCH_RESULTS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPhrase() == null) ? 0 : getPhrase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpellingCandidate other = (SpellingCandidate) obj;
		if (getPhrase() == null) {
			if (other.getPhrase() != null)
				return false;
		} else if (!getPhrase().equals(other.getPhrase()))
			return false;
		return true;
	}

	@Override
	public int compareTo(SpellingCandidate o) {
		int d = 0;
		if (searchResults != null && o.searchResults != null)
			d = o.searchResults.size() - searchResults.size();
		if (d != 0)
			return d;
		else
			return getPhrase().compareTo(o.getPhrase());
	}

	@Override
	public String toString() {
		return "SpellingCandidate [hit=" + hit + ", replaceOriginal=" + replaceOriginal + "]";
	}

	public SpellingHit getHit() {
		return hit;
	}

	public String getPhrase() {
		return hit.getPhrase();
	}

	public int getDistance() {
		return hit.getDistance();
	}

	public double getScore() {
		return hit.getScore();
	}

	public void setReplaceOriginal(boolean replaceOriginal) {
		this.replaceOriginal = replaceOriginal;
	}

	public boolean isReplaceOriginal() {
		return replaceOriginal;
	}

	public Collection<SearchHit> getSearchHits() {
		return searchHits;
	}

	public void setSearchHits(Collection<SearchHit> searchHits) {
		this.searchHits = searchHits;
	}

	public SearchResults getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(SearchResults searchResults) {
		this.searchResults = searchResults;
	}

	public boolean searchHitsEquals(SpellingCandidate o) {
		if (searchHits.size() == o.searchHits.size()) {
			for (SearchHit hit : searchHits)
				if (!o.searchHits.contains(hit))
					return false;

			return true;
		} else
			return false;
	}
}
