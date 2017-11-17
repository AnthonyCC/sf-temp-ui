package com.freshdirect.storeapi.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SlashConverter {

    public String convert(String term) {
        if (term.equals("/"))
            return "";
        if (term.equals("w/o"))
            return "without";
        else if (term.equals("w/"))
            return "with";
        else {
            List<String> result = new ArrayList<String>();
            if (term.startsWith("w/")) {
                result.add("with");
                term = term.substring(2);
            }
            String[] terms = StringUtils.split(term, '/');
            for (String t : terms) {
                if (!t.isEmpty()) {
                    result.add(t);
                }
            }
            return StringUtils.join(result, " ");
        }
    }

}
