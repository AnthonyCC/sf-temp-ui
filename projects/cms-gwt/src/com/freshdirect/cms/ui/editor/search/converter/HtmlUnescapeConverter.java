package com.freshdirect.cms.ui.editor.search.converter;

import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlUnescapeConverter {

    public String convert(String term) {
        return StringEscapeUtils.unescapeHtml4(term);
    }

}
