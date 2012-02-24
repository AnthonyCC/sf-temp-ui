/**
 * 
 */
package com.freshdirect.cms.search.term;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Synonym implements Serializable {
	private static final long serialVersionUID = -9115270197519225421L;

	private Term term;
	private String termString;
	private Set<Term> synonyms;

	public Synonym(Term term, Collection<Term> synonyms) {
		this.term = term;
		this.termString = term.toString();
		this.synonyms = new LinkedHashSet<Term>(synonyms);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((term == null) ? 0 : term.hashCode());
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
		Synonym other = (Synonym) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Synonym[term=" + termString + ", synonyms=" + synonyms + "]";
	}

	public String getFirstWord() {
		return term.getTokens().get(0);
	}

	public Term getTerm() {
		return term;
	}

	public String getTermAsString() {
		return termString;
	}

	public int getWordCount() {
		return term.getTokens().size();
	}

	public Collection<Term> getSynonyms() {
		return synonyms;
	}

	public void merge(Collection<Term> synonyms) {
		this.synonyms.addAll(synonyms);
	}
}