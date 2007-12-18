/*
 * Created on Mar 22, 2005
 */
package com.freshdirect.cms.search;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;

/**
 * A scored search hit for a {@link com.freshdirect.cms.ContentKey}.
 * Higher score indicates better match.
 */
public class SearchHit implements Serializable {

	private final ContentKey contentKey;
	private final double score;

	public SearchHit(ContentKey contentKey, double score) {
		this.contentKey = contentKey;
		this.score = score;
	}

	public ContentKey getContentKey() {
		return contentKey;
	}

	public double getScore() {
		return score;
	}

	public String toString() {
		return "SearchHit[" + score + ", " + contentKey + "]";
	}

}