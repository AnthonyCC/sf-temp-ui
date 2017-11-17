package com.freshdirect.storeapi.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PlusSignConverter {

    private boolean retainPunctuation;

    public String convert(String term) {
        if (term.indexOf('+') < 0) {
            return term;
        }
        List<String> result = new ArrayList<String>();
        if (retainPunctuation == true) {
            result.add(term);
        }

        List<String> plusRet = new ArrayList<String>();
        List<String> andRet = new ArrayList<String>();

        String[] terms = StringUtils.split(term, '+');
        boolean latch = false;
        for (String t : terms) {
            if (latch) {
                plusRet.add("plus");
                andRet.add("and");
            } else {
                latch = true;
            }
            if (!t.isEmpty()) {
                plusRet.add(t);
                andRet.add(t);
            }
        }

        result.addAll(plusRet);
        if (term.length() == 1 || term.charAt(term.length() - 1) != '+') {
            result.addAll(andRet);
        }
        return StringUtils.join(result, " ");
    }
}
