package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.filter.ContentFilter;

public class PrioritizedDataAccess implements DataAccess {
	private List<ContentNodeModel> nodes;
	ContentFilter filter;

	public PrioritizedDataAccess(Collection cartItems, boolean useAlternatives, boolean showTempUnavailable) {
		nodes = new ArrayList<ContentNodeModel>();
		filter = FilterFactory.getInstance().createFilter(cartItems, useAlternatives, showTempUnavailable);
	}

	@Override
	public List getDatasource(SessionInput input, String name) {
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

}
