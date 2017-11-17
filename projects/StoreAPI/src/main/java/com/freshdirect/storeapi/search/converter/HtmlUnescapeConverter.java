package com.freshdirect.storeapi.search.converter;

import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlUnescapeConverter {

    public String convert(String term) {
        return StringEscapeUtils.unescapeHtml4(term);
    }

}
