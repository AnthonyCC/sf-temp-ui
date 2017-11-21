/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.storeapi.search;

import java.io.Serializable;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;

/**
 * A scored search hit for a {@link com.freshdirect.cms.ContentKey}. Higher score indicates better match.
 */
@CmsLegacy
public class SearchHit implements Serializable {
	private static final long serialVersionUID = 2702004718163852501L;

	private final ContentKey contentKey;
	private final double score;
	private ContentNodeModel node;
	private final int approximationLevel;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentKey == null) ? 0 : contentKey.hashCode());
		return result;
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

	public ContentKey getContentKey() {
		return contentKey;
	}

	public double getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "SearchHit [contentKey=" + contentKey + ", score=" + score + ", approximationLevel=" + approximationLevel + "]";
	}

	public ContentNodeModel getNode() {
		if (node == null)
			node = ContentFactory.getInstance().getContentNodeByKey(contentKey);
		return node;
	}

	public int getApproximationLevel() {
		return approximationLevel;
	}
}