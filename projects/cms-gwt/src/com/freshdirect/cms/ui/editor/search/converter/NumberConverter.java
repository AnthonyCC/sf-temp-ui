package com.freshdirect.cms.ui.editor.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class NumberConverter {

    public String convert(String term) {
        List<String> words = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            buf.append(c);
            if ((Character.isDigit(c) && i + 1 < term.length() && Character.isLetter(term.charAt(i + 1)))
                    || (Character.isLetter(c) && i + 1 < term.length() && Character.isDigit(term.charAt(i + 1)))) {
                if (buf.length() != 0) {
                    words.add(buf.toString());
                    buf = new StringBuilder();
                }
            }
        }
        if (buf.length() != 0) {
            words.add(buf.toString());
        }

        return StringUtils.join(words, "");
    }
}
