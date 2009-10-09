package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.filter.ProductFilter;

public class PrioritizedDataAccess implements DataAccess {
	private List<ContentNodeModel> nodes;
	ProductFilter filter;

	public PrioritizedDataAccess(Collection cartItems) {
		nodes = new ArrayList<ContentNodeModel>(10);
		filter = FilterFactory.createStandardFilter(cartItems);
	}

	@Override
	public List getDatasource(SessionInput input, String name) {
		return ScoreProvider.getInstance().getDatasource(input, name);
	}

	@Override
	public double[] getVariables(String userId, ContentNodeModel contentNode,
			String[] variables) {
		return ScoreProvider.getInstance().getVariables(userId, contentNode, variables);
	}

	@Override
	public boolean addPrioritizedNode(ContentNodeModel model) {
		if (model instanceof ProductModel
				&& filter.filter((ProductModel) model) != null) {
			nodes.add(model);
			return true;
		} else
			return false;
	}

	@Override
	public List<ContentNodeModel> getPrioritizedNodes() {
		return Collections.unmodifiableList(nodes);
	}

}
