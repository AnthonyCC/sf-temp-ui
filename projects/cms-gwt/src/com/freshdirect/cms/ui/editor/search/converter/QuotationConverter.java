package com.freshdirect.cms.ui.editor.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class QuotationConverter {

    public String convert(String term) {
        boolean inch = false;
        if (term.equals("\"")) {
            return "";
        }
        if (term.endsWith("\"") && Character.isDigit(term.charAt(term.length() - 2))) {
            inch = true;
        }
        List<String> result = new ArrayList<String>();
        String[] terms = StringUtils.split(term, '"');
        for (String t : terms) {
            if (!t.isEmpty()) {
                result.add(t);
            }
        }
        if (inch) {
            result.add("inch");
        }
        return StringUtils.join(result, " ");
    }
}
