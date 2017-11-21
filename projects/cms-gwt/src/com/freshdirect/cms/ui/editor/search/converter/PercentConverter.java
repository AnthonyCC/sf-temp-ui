package com.freshdirect.cms.ui.editor.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PercentConverter {

    public String convert(String term) {
        List<String> result = new ArrayList<String>();
        String[] terms = StringUtils.split(term, '%');
        boolean latch = false;
        for (String t : terms) {
            if (latch) {
                result.add("percent");
            } else {
                latch = true;
            }
            if (!t.isEmpty())
                result.add(t);
        }
        return StringUtils.join(result, " ");
    }
}
