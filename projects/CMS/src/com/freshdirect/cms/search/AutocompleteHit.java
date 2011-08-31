package com.freshdirect.cms.search;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.freshdirect.cms.ContentKey;

class AutocompleteHit implements Serializable {
	private static final long serialVersionUID = 4987366706775989570L;

	private final SortedSet<String> terms;

	// number of content keys related to the given term
	private int number; // updated automatically
	private transient Set<ContentKey> contentKeys;

	public AutocompleteHit(String term, ContentKey contentKey) {
		this.terms = new TreeSet<String>();
		contentKeys = new HashSet<ContentKey>();
		addTerm(term, contentKey);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((terms == null) ? 0 : terms.hashCode());
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
		AutocompleteHit other = (AutocompleteHit) obj;
		if (number != other.number)
			return false;
		if (terms == null) {
			if (other.terms != null)
				return false;
		} else if (!terms.equals(other.terms))
			return false;
		return true;
	}

	public void addTerm(String term, ContentKey contentKey) {
		terms.add(term);
		contentKeys.add(contentKey);
		number++;
	}

	public Set<String> getTerms() {
		return terms;
	}

	public int getNumber() {
		return number;
	}

	public boolean isSameContentKeys(AutocompleteHit o) {
		if (contentKeys.size() != o.contentKeys.size())
			return false;
		else
			return contentKeys.equals(o.contentKeys);
	}

	public void purge() {
		contentKeys = null;
	}

	public String toString() {
		return terms.toString() + '(' + number + ')';
	}
}