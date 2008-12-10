package com.freshdirect.cms.search;

/**
 * Configuration object to instruct
 * {@link com.freshdirect.cms.search.LuceneSearchService} to index a named
 * attribute of nodes with a particular {@link com.freshdirect.cms.ContentType}.
 */
public class AttributeIndex extends ContentIndex {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String attributeName;

    public AttributeIndex() {
    }

    public AttributeIndex(String contentType, String attributeName) {
        super(contentType);
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String toString() {
        return "AttributeIndex[" + getContentType() + ":" + attributeName + "]";
    }

}