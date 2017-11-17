package com.freshdirect.storeapi.search.converter;

public class DotConverter {

    public String convert(String term) {
        return term.replace('.', ' ');
    }

}
