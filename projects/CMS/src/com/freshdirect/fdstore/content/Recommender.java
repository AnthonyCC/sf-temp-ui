package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;

/**
 * Recommender represents a recommendation method for Smart YMAL Recommenders.<br/>
 * Has a <i>{@linkplain RecommenderStrategy selection strategy}</i>, and an optional <i>scope</i>. 
 * 
 * @author csongor
 * @author treer 
 * 
 * @see RecommenderStrategy
 * @see SmartYMALRecommendationService
 * @see {@link YmalSet}	
 * 
 * @since 0903_R1
 */

public class Recommender extends ContentNodeModelImpl {

	private static final long serialVersionUID = 7406701239902892409L;
	
	private final List scope = new ArrayList();

	public Recommender(ContentKey key) {
		super(key);
	}
	
	public String getDescription() {
		return getFullName();
	}
	
	public RecommenderStrategy getStrategy() {
		AttributeI attrib = getCmsAttribute("strategy");
		if (attrib==null) return null;
		ContentKey key = (ContentKey) attrib.getValue();
		return key == null ? null : (RecommenderStrategy) ContentFactory.getInstance().getContentNode(key.getId());
	}
	
	public List getScope() {
		ContentNodeModelUtil.refreshModels(this, "scope", scope, false);
		return Collections.unmodifiableList(scope);
	}

}
