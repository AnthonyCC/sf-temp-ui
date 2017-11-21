package com.freshdirect.storeapi.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class IsoToLatin1Converter {

    public String convert(String term) {
        List<String> result = new ArrayList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) == '\u00ad') // soft-hyphen
                stringBuilder.append('-');
            else if (Character.isISOControl(term.charAt(i)) || term.charAt(i) == '\u00a0') { // non-breaking space (nbsp)
                if (stringBuilder.length() != 0) {
                    result.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
            } else if (term.charAt(i) <= '\u00ff')
                stringBuilder.append(term.charAt(i));
        }
        if (stringBuilder.length() != 0)
            result.add(stringBuilder.toString());
        return StringUtils.join(result, " ");
    }
}
