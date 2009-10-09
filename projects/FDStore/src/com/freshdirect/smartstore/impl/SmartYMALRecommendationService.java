package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recommender;
import com.freshdirect.fdstore.content.RecommenderStrategy;
import com.freshdirect.fdstore.content.YmalSet;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FactorRequirer;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.service.CmsRecommenderRegistry;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

/**
 * @author csongor
 */
public class SmartYMALRecommendationService extends
		AbstractRecommendationService implements FactorRequirer {
	private static final Category LOGGER = LoggerFactory
			.getInstance(SmartYMALRecommendationService.class);

	public SmartYMALRecommendationService(Variant variant,
			ImpressionSampler sampler, boolean catAggr, boolean includeCartItems) {
		super(variant, sampler, catAggr, includeCartItems);
	}

	/**
	 * Recommends products for the current node
	 */
	public List doRecommendNodes(SessionInput input) {

		List prodList = new ArrayList();
		final YmalSource ymalSource = input.getYmalSource();
		final ProductModel selectedProduct = (ProductModel) input
				.getCurrentNode();
		int availSlots = input.getMaxRecommendations();

		SmartStoreUtil.clearConfiguredProductCache();

		// related products
		List relatedProducts = ymalSource != null ? ymalSource
				.getRelatedProducts()
				: selectedProduct != null ? selectedProduct
						.getRelatedProducts() : new ArrayList();
		for (int i = 0; i < relatedProducts.size(); i++) {
			ProductModel pm = (ProductModel) relatedProducts.get(i);
			if (pm.isDisplayable()) {
				ProductModel q = SmartStoreUtil.addConfiguredProductToCache(pm);
				ProductModel p = q;
				if (p != null)
					prodList.add(p);
			}
		}
		availSlots -= prodList.size();

		// smart YMAL products
		YmalSet ymalSet = null;
		if (ymalSource != null)
			ymalSet = ymalSource.getActiveYmalSet();
		if (ymalSet == null && selectedProduct != null)
			ymalSet = selectedProduct.getActiveYmalSet();

		// true YMAL products
		if (ymalSet != null && availSlots > 0) {
			// smart YMAL
			List recommenders = ymalSet.getRecommenders();

			SessionInput smartInput = new SessionInput(input.getCustomerId(),
					input.getCustomerServiceType());
			smartInput.setYmalSource(ymalSource);
			smartInput.setCurrentNode(selectedProduct);
			smartInput.setCartContents(addContentKeys(new HashSet(), prodList));
			if (selectedProduct != null)
				smartInput.getCartContents().add(
						selectedProduct.getContentKey());
			smartInput.setNoShuffle(input.isNoShuffle());

			Map<String,String> recServiceAudit = new HashMap<String,String>();
			RECOMMENDER_SERVICE_AUDIT.set(recServiceAudit);
			Map<String,String> recStratServiceAudit = new HashMap<String,String>();
			RECOMMENDER_STRATEGY_SERVICE_AUDIT.set(recStratServiceAudit);

			List[] recommendations = new List[recommenders.size()];

			for (int i = 0; i < recommenders.size(); i++) {

				Recommender rec = (Recommender) recommenders.get(i);
				RecommenderStrategy strategy = rec.getStrategy();
				if (strategy == null) {
					recommendations[i] = Collections.EMPTY_LIST;
					continue;
				}

				List scope = rec.getScope();

				RecommendationService rs = (RecommendationService) CmsRecommenderRegistry
						.getInstance().getService(strategy.getContentName());
				if (rs == null) {
					recommendations[i] = Collections.EMPTY_LIST;
					continue;
				}
				smartInput.setExplicitList(scope);
				List recNodes = rs.recommendNodes(smartInput);

				for (int j = 0; j < recNodes.size(); j++) {
					ContentNodeModel model = (ContentNodeModel) recNodes.get(j);
					recServiceAudit.put(model.getContentKey().getId(), rec
							.getContentKey().getId());
					recStratServiceAudit.put(model.getContentKey().getId(),
							strategy.getContentKey().getId());
				}
				addContentKeys(smartInput.getCartContents(), recNodes);
				recommendations[i] = recNodes;

			}

			if (recommenders.size() == 1) {

				prodList.addAll(SmartStoreUtil
						.addConfiguredProductToCache(recommendations[0]));

			} else if (recommenders.size() > 1) {

				int i = 0;

				while (availSlots > 0 && hasAnyItem(recommendations)) {
					ProductModel next;
					ProductModel p;
					do {
						next = recommendations[i].size() > 0 ? (ProductModel) recommendations[i]
								.remove(0)
								: null;
						p = SmartStoreUtil.addConfiguredProductToCache(next);
					} while (next != null
							&& (!next.isDisplayable() || prodList.contains(p)));
					if (next != null) {
						if (p != null) {
							prodList.add(p);
							availSlots--;
						}
					}
					i = (i + 1) % recommendations.length;
				}

			}
		}

		if (ymalSource != null && availSlots > 0) {
			// classic YMAL set products
			List ymalProducts = new ArrayList(ymalSource.getYmalProducts());
			for (ListIterator it = ymalProducts.listIterator(); it.hasNext();) {
				ProductModel pm = (ProductModel) it.next();
				ProductModel q = SmartStoreUtil
						.addConfiguredProductToCache((ProductModel) pm);
				if (relatedProducts.contains(pm) || q == null)
					it.remove();
				else {
					it.set(q);
				}
			}
			prodList.addAll(ymalProducts);
		} else {
			LOGGER.info("ymal source is null");
		}

		return prodList;
	}

	private boolean hasAnyItem(List[] recommendations) {
		for (int i = 0; i < recommendations.length; i++)
			if (recommendations[i].size() > 0)
				return true;
		return false;
	}

	private static Set addContentKeys(Set keys, Collection nodes) {
		Iterator it = nodes.iterator();
		while (it.hasNext()) {
			ContentNodeModel node = (ContentNodeModel) it.next();
			keys.add(node.getContentKey());
		}

		return keys;
	}

	public void collectFactors(Collection buffer) {
		// XXX currently loaded in the SmartStoreServiceConfiguration
	}

	public boolean isRefreshable() {
		return true;
	}
}
