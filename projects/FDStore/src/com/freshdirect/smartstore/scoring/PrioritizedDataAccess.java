package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.filter.ContentFilter;
import com.freshdirect.smartstore.filter.FilterFactory;

public class PrioritizedDataAccess implements DataAccess {
	/**
	 * List of priority nodes.
	 */
	private List<ContentNodeModel> nodes;
	
	/**
	 * List of those nodes
	 */
	private List<ContentNodeModel> posteriorNodes;

	ContentFilter filter;

	public PrioritizedDataAccess(Collection<ContentKey> cartItems, boolean useAlternatives, boolean showTempUnavailable) {
		nodes = new ArrayList<ContentNodeModel>();
		posteriorNodes = new ArrayList<ContentNodeModel>();
		filter = FilterFactory.getInstance().createFilter(cartItems, useAlternatives, showTempUnavailable);
	}

	@Override
	public List<ContentNodeModel> getDatasource(SessionInput input, String name) {
		return ScoreProvider.getInstance().getDatasource(input, name);
	}

	@Override
	public double[] getVariables(String userId, PricingContext pricingContext, ContentNodeModel contentNode,
			String[] variables) {
		return ScoreProvider.getInstance().getVariables(userId, pricingContext, contentNode, variables);
	}

	@Override
	public boolean addPrioritizedNode(ContentNodeModel model) {
		if (model instanceof ProductModel) {
		    ContentNodeModel filteredModel = filter.filter(model);
		    if (filteredModel != null) {
		        nodes.add(filteredModel);
		        return true;
		    }
		} 
		return false;
	}


	@Override
	public List<ContentNodeModel> getPrioritizedNodes() {
		return Collections.unmodifiableList(nodes);
	}

	@Override
	public boolean addPosteriorNode(ContentNodeModel model) {
		if (model instanceof ProductModel) {
		    ContentNodeModel filteredModel = filter.filter(model);
		    if (filteredModel != null) {
		    	posteriorNodes.add(filteredModel);
		        return true;
		    }
		} 
		return false;
	}

	@Override
	public List<ContentNodeModel> getPosteriorNodes() {
		return Collections.unmodifiableList(posteriorNodes);
	}

}
