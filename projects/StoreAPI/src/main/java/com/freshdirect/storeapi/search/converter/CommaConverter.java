package com.freshdirect.storeapi.search.converter;

public class CommaConverter {

    public String convert(String term) {
        return term.trim().replace(',', ' ');
    }

}
