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
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;

/**
 * @author csongor
 */
public class SmartYMALRecommendationService extends AbstractRecommendationService {
	private static final Category LOGGER = LoggerFactory.getInstance(SmartYMALRecommendationService.class);
	
	Map recommenderCache;
	
	public SmartYMALRecommendationService(Variant variant) {
		super(variant);
		recommenderCache = new HashMap(); 
	}

	/**
	 * Recommends products for the current node
	 */
	public List recommendNodes(Trigger trigger, SessionInput input) {
		
		List prodList = new ArrayList();
		final YmalSource ymalSource = input.getYmalSource();
		final ProductModel selectedProduct = (ProductModel) input.getCurrentNode();
		int availSlots = trigger.getMaxRecommendations();

		clearConfiguredProductCache();

		// related products
		List relatedProducts = ymalSource != null ?
				ymalSource.getRelatedProducts() : 
				selectedProduct != null ?
						selectedProduct.getRelatedProducts() : 
						new ArrayList();
		for (int i = 0; i < relatedProducts.size(); i++) {
			ProductModel pm = (ProductModel) relatedProducts.get(i);
			if (pm.isDisplayable()) {
				ProductModel q = addConfiguredProductToCache(pm);
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
			
			SessionInput smartInput = new SessionInput(input.getCustomerId());
			smartInput.setYmalSource(ymalSource);
			smartInput.setCurrentNode(selectedProduct);
			smartInput.setCartContents(addContentKeys(new HashSet(), prodList));
			if (selectedProduct != null)
				smartInput.getCartContents().add(selectedProduct.getContentKey());
			smartInput.setNoShuffle(input.isNoShuffle());
			
			List[] recommendations = new List[recommenders.size()];
			
			for (int i = 0; i < recommenders.size(); i++) {
			
				Recommender rec = (Recommender) recommenders.get(i);
				RecommenderStrategy strategy = rec.getStrategy();
				List scope = rec.getScope();
				
				RecommendationService rs = getScriptRecommendationService(strategy);
				if (rs == null) {
					recommendations[i] = Collections.EMPTY_LIST;
					continue;
				}
				smartInput.setExplicitList(scope);
				List recNodes = rs.recommendNodes(trigger, smartInput);
				
				addContentKeys(smartInput.getCartContents(), recNodes);
				recommendations[i] = recNodes;
			
			}
			
			if (recommenders.size() == 1) {
				
				prodList.addAll(addConfiguredProductToCache(recommendations[0]));
				
			} else if (recommenders.size() > 1) {
				
				int i = 0;
				
				while (availSlots > 0 && hasAnyItem(recommendations)) {
					ProductModel next;
					ProductModel p;
					do {
						next = recommendations[i].size() > 0 ? 
								(ProductModel) recommendations[i].remove(0) : null;
						p = addConfiguredProductToCache(next);
					} while (next != null && (!next.isDisplayable() || prodList.contains(p)));
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
			for (ListIterator it = ymalProducts.listIterator(); it.hasNext(); ) {
				ProductModel pm = (ProductModel) it.next();
				ProductModel q = addConfiguredProductToCache((ProductModel) pm);
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

	private synchronized RecommendationService getScriptRecommendationService(RecommenderStrategy strategy) {
		if (!recommenderCache.containsKey(strategy)) {
			try {
				RecommendationServiceConfig config = new RecommendationServiceConfig(strategy.getContentName(),
						RecommendationServiceType.SCRIPTED);
				config.set(CKEY_SAMPLING_STRATEGY, strategy.getSampling());
				config.set(CKEY_TOP_N, Integer.toString(strategy.getTopN()));
				config.set(CKEY_TOP_PERC, Double.toString(strategy.getTopPercent()));
				config.set(CKEY_EXPONENT, Double.toString(strategy.getExponent()));
				
				Variant v = new Variant(strategy.getContentName(), EnumSiteFeature.YMAL, config);
				
				RecommendationService rs = new ScriptedRecommendationService(v, strategy.getGenerator(), strategy.getScoring());
				recommenderCache.put(strategy, rs);
				return rs;
			} catch (CompileException e) {
				LOGGER.error("compile error when creating recommender with strategy '" + strategy + "'", e);
				return null;
			}
		} else
			return (RecommendationService) recommenderCache.get(strategy);
	}
	
	private static Set addContentKeys(Set keys, Collection nodes) {
		Iterator it = nodes.iterator();
		while (it.hasNext()) {
			ContentNodeModel node = (ContentNodeModel) it.next();
			keys.add(node.getContentKey());
		}
		
		return keys;
	}
}
