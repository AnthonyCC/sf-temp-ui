/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.cms.ui.editor.search.domain;

import java.io.Serializable;

import com.freshdirect.cms.core.domain.ContentKey;

/**
 * A scored search hit for a {@link com.freshdirect.cms.ContentKey}. Higher score indicates better match.
 */
public class SearchHit implements Serializable {

    private static final long serialVersionUID = 2702004718163852501L;

    private final int approximationLevel;
    private final ContentKey contentKey;
    private final double score;

    public SearchHit(ContentKey contentKey, double score) {
        this.contentKey = contentKey;
        this.score = score;
        this.approximationLevel = 0;
    }

    public SearchHit(ContentKey contentKey, double score, int approximationLevel) {
        this.contentKey = contentKey;
        this.score = score;
        this.approximationLevel = approximationLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchHit other = (SearchHit) obj;
        if (contentKey == null) {
            if (other.contentKey != null)
                return false;
        } else if (!contentKey.equals(other.contentKey))
            return false;
        return true;
    }

    public int getApproximationLevel() {
        return approximationLevel;
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contentKey == null) ? 0 : contentKey.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SearchHit [contentKey=" + contentKey + ", score=" + score + ", approximationLevel=" + approximationLevel + "]";
    }
}
