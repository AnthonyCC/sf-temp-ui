package com.freshdirect.cms.ui.editor.index.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SpellingInitialismConverter {

    public String convert(String term) {
        List<String> inits = new ArrayList<String>();
        boolean initialism = true;
        for (int i = 0; i < term.length(); i++) {
            char ch = term.charAt(i);
            if (i % 2 == 0) {
                if (Character.isLetterOrDigit(ch)) {
                    inits.add(new String(new char[] { ch }));
                } else {
                    initialism = false;
                    break;
                }
            } else {
                if (ch != '.') {
                    initialism = false;
                    break;
                }
            }
        }
        if (initialism && inits.size() > 1) {
            String initsToAppend = StringUtils.join(inits, "");
            return StringUtils.join(Arrays.asList(term, initsToAppend), "");
        } else
            return term;
    }

}
