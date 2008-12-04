/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.cms.search;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeI;
import com.freshdirect.fdstore.content.ContentNodeModel;

/**
 * A scored search hit for a {@link com.freshdirect.cms.ContentKey}. Higher
 * score indicates better match.
 */
public class SearchHit implements Serializable {

    private final ContentKey contentKey;
    private final double     score;
    private String           keywords;
    private ContentNodeModel node;

    public SearchHit(ContentKey contentKey, double score) {
        this.contentKey = contentKey;
        this.score = score;
    }

    public SearchHit(ContentKey contentKey, double score, String keywords) {
        this.contentKey = contentKey;
        this.score = score;
        this.keywords = keywords;
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public double getScore() {
        return score;
    }

    public String toString() {
        return "SearchHit[" + score + ", " + contentKey + ',' + keywords + "]";
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }
    
    public void setNode(ContentNodeModel node) {
        this.node = node;
    }

    public ContentNodeModel getNode() {
        return node;
    }
}