package com.freshdirect.cms.search;

import java.io.Serializable;

/**
 * Configuration object to instruct
 * {@link com.freshdirect.cms.search.LuceneSearchService} to index the existence
 * of nodes with a particular {@link com.freshdirect.cms.ContentType}.
 */
public class ContentIndex implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String contentType;

    public ContentIndex() {
    }

    public ContentIndex(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String toString() {
        return "ContentIndex[" + contentType + ']';
    }

}