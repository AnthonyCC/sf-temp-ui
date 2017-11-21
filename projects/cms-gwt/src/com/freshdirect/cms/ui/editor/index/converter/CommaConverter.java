package com.freshdirect.cms.ui.editor.index.converter;

public class CommaConverter {

    public String convert(String term) {
        return term.trim().replace(',', ' ');
    }

}
