package com.freshdirect.smartstore.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.FactorRequirer;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.smartstore.sampling.ImpressionSampler;

public class YmalYfRecommendationService extends AbstractRecommendationService implements FactorRequirer {
	private ScriptedRecommendationService popularity;
	private RecommendationService smartYmal = null;
	
	public YmalYfRecommendationService(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems) throws CompileException {
		super(variant, sampler, catAggr, includeCartItems);
		popularity = createPopularityRecommender();
	}

	private ScriptedRecommendationService createPopularityRecommender() throws CompileException {
		RecommendationServiceConfig config = new RecommendationServiceConfig("ymal_yf_popularity",
				RecommendationServiceType.SCRIPTED);
		config.set(SmartStoreServiceConfiguration.CKEY_SAMPLING_STRATEGY, "deterministic");
		config.set(SmartStoreServiceConfiguration.CKEY_TOP_N, Integer.toString(20));
		config.set(SmartStoreServiceConfiguration.CKEY_TOP_PERC, Double.toString(20.0));
		config.set(SmartStoreServiceConfiguration.CKEY_GENERATOR, "PurchaseHistory");
		config.set(SmartStoreServiceConfiguration.CKEY_SCORING, "Popularity_Discretized");
		
		Variant v = new Variant("ymal_yf_popularity", EnumSiteFeature.YMAL, config, new TreeMap());
		
		ScriptedRecommendationService rs = (ScriptedRecommendationService)
				SmartStoreServiceConfiguration.configure(v);
		if (rs == null)
			throw new CompileException("cannot compile popularity recommender, see previous error message");
		return rs;
	}

	public List<ContentNodeModel> doRecommendNodes(SessionInput input) {
		RecommendationService smart = getSmartYmalRecommender();
		if (smart == null)
			return Collections.EMPTY_LIST;
		
		List<ContentNodeModel> favorites = popularity.recommendNodes(input);
		favorites = FDStoreRecommender.getInstance().filterProducts(favorites, input.getCartContents(), false);
		if (favorites.isEmpty())
			return Collections.EMPTY_LIST;
		
		ProductModel currentNode = (ProductModel) favorites.get(0);
		SessionInput i2 = new SessionInput(input.getCustomerId(), input.getCustomerServiceType());
		i2.setMaxRecommendations(input.getMaxRecommendations());
		i2.setCartContents(input.getCartContents());
		i2.setCurrentNode(currentNode);
		i2.setYmalSource(currentNode);
		i2.setNoShuffle(input.isNoShuffle());
		
		List<ContentNodeModel> prods = smart.recommendNodes(i2);
		return prods;
	}

	private synchronized RecommendationService getSmartYmalRecommender() {
		if (smartYmal == null) {
			Map variantMap = SmartStoreServiceConfiguration.getInstance().getServices(EnumSiteFeature.YMAL);
			Iterator it = variantMap.values().iterator();
			while (it.hasNext()) {
				RecommendationService rs = (RecommendationService) it.next();
				if (rs instanceof SmartYMALRecommendationService) {
					return smartYmal = rs;
				}
			}
			return null;
		} else
			return smartYmal;
	}

    public void collectFactors(Collection factors) {
        popularity.collectFactors(factors);
    }
    
    public boolean isRefreshable() {
    	return true;
    }
}
