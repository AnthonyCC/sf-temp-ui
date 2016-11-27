package com.freshdirect.webapp.autosuggest.unbxd.dto;

public class UnbxdAutosuggestResultProduct {

    private String autosuggest;

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
}
