package com.freshdirect.cms.lucene.domain;

import java.io.Serializable;

import com.freshdirect.cms.core.domain.ContentType;

/**
 * Configuration object to instruct {@link com.freshdirect.cms.search.LuceneSearchService} to index the existence of nodes with a particular
 * {@link com.freshdirect.cms.ContentType}.
 */
public class ContentIndex implements Serializable {

    private static final long serialVersionUID = -4178824828324127700L;

    private ContentType contentType;

    public ContentIndex(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "ContentIndex [contentType=" + contentType + "]";
    }
}
