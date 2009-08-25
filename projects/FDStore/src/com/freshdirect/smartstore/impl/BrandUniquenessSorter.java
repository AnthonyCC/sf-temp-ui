package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.WrapperRecommendationService;

public class BrandUniquenessSorter extends WrapperRecommendationService {
	private static final Logger LOGGER = LoggerFactory
			.getInstance(BrandUniquenessSorter.class);

	public BrandUniquenessSorter(RecommendationService internal) {
		super(internal);
	}

	public List recommendNodes(SessionInput input) {
		List nodes = new ArrayList(internal.recommendNodes(input));
		LOGGER.debug("Items before brand uniqueness sorting: " + nodes);
		List newNodes = new ArrayList(nodes.size());
		Set brands = new HashSet(15);
		while (nodes.size() > 0) {
			ListIterator it = nodes.listIterator();
			while (it.hasNext()) {
				ProductModel p = (ProductModel) it.next();
				if (!containsAny(brands, p.getBrands())) {
					newNodes.add(p);
					it.remove();
					brands.addAll(p.getBrands());
				}
			}
			brands.clear();
		}

		LOGGER.debug("Items after brand uniqueness sorting: " + newNodes);
		return newNodes;
	}

	private boolean containsAny(Set set, List elements) {
		for (int i = 0; i < elements.size(); i++)
			if (set.contains(elements.get(i)))
				return true;

		return false;
	}
}
