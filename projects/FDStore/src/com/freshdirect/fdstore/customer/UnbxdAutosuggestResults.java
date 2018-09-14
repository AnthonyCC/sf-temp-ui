package com.freshdirect.fdstore.customer;

import java.io.Serializable;

public class UnbxdAutosuggestResults implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String autosuggest;
    private String doctype;
    private String uniqueId;
    private int unbxdAutosuggestScore;
    private String internalQuery;

    public String getAutosuggest() {
        return autosuggest;
    }
	public void setAutosuggest(String autosuggest) {
        this.autosuggest = autosuggest;
    }

    @Override
    public String toString() {
        return "[autosuggest: " + autosuggest + "]";
    }

    public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}


	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public int getUnbxdAutosuggestScore() {
		return unbxdAutosuggestScore;
	}
	
	public void setUnbxdAutosuggestScore(int unbxdAutosuggestScore) {
		this.unbxdAutosuggestScore = unbxdAutosuggestScore;
	}
	public String getInternalQuery() {
		return internalQuery;
	}
	public void setInternalQuery(String internalQuery) {
		this.internalQuery = internalQuery;
	}
}
