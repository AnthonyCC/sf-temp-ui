package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.cms.smartstore.CmsRecommenderService;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recommender;
import com.freshdirect.fdstore.content.RecommenderStrategy;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.service.CmsRecommenderRegistry;

public class CmsRecommenderServiceImpl implements CmsRecommenderService {
	private static final Logger LOG = LoggerFactory
			.getInstance(CmsRecommenderServiceImpl.class);
	private static final long serialVersionUID = 7555105742910594364L;

	@Override
	public List recommendNodes(String recommenderId, String categoryId) {
		ContentNodeModel node = ContentFactory.getInstance().getContentNode(
				recommenderId);
		if (node instanceof Recommender) {
			Recommender recommenderNode = (Recommender) node;
			RecommenderStrategy strategy = recommenderNode.getStrategy();
			if (strategy == null)
				return Collections.emptyList();
			RecommendationService recommender = CmsRecommenderRegistry
					.getInstance().getService(
							recommenderNode.getStrategy().getContentName());
			if (recommender != null) {
				node = ContentFactory.getInstance().getContentNode(categoryId);
				CategoryModel category = null;
				if (node instanceof CategoryModel)
					category = (CategoryModel) node;
				SessionInput input = new SessionInput(null);
				input.setCurrentNode(category);
				input.setExplicitList(recommenderNode.getScope());
				List products = recommender.recommendNodes(input);
				List prodIds = new ArrayList(products.size());
				for (int i = 0; i < products.size(); i++) {
					ProductModel product = (ProductModel) products.get(i);
					prodIds.add(product.getContentName());
				}
				return prodIds;
			} else {
				LOG.debug("recommender " + recommenderId + " is not found");
				return Collections.emptyList();
			}
		} else
			return Collections.emptyList();
	}
}
