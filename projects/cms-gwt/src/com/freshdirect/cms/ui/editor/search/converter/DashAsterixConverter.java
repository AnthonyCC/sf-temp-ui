package com.freshdirect.cms.ui.editor.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DashAsterixConverter {

    public String convert(String term) {
        List<String> words = new ArrayList<String>();
        String[] terms = StringUtils.split(term, "-*");
        for (int i = 0; i < terms.length; i++) {
            String t = terms[i];
            if (!t.isEmpty()) {
                words.add(t);
            }
        }

        return StringUtils.join(words, "");
    }
}
