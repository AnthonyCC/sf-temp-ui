package com.freshdirect.cms.ui.editor.index.domain;

import java.io.Serializable;

public class DictionaryItem implements Serializable {
	private static final long serialVersionUID = 5670208015448211509L;

	private final String searchTerm;

	private final String spellingTerm;
	
	private final boolean synonym;

	public DictionaryItem(String searchTerm, String spellingTerm) {
		super();
		this.searchTerm = searchTerm;
		this.spellingTerm = spellingTerm;
		this.synonym = false;
	}

	public DictionaryItem(String searchTerm, String spellingTerm, boolean synonym) {
		super();
		this.searchTerm = searchTerm;
		this.spellingTerm = spellingTerm;
		this.synonym = synonym;
	}

	public DictionaryItem(String spellingTerm) {
		super();
		this.searchTerm = spellingTerm;
		this.spellingTerm = spellingTerm;
		this.synonym = false;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public String getSpellingTerm() {
		return spellingTerm;
	}
	
	public boolean isSynonym() {
		return synonym;
	}

	@Override
	public String toString() {
		return "DictionaryItem[spellingTerm=" + spellingTerm + ", searchTerm=" + searchTerm + ", synonym=" + synonym + "]";
	}
}
