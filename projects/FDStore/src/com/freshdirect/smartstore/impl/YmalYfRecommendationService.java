package com.freshdirect.smartstore.impl;

import java.util.Collection;
import java.util.Collections;
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
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;
import com.freshdirect.smartstore.service.VariantRegistry;

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
		config.set(RecommendationServiceFactory.CKEY_SAMPLING_STRATEGY, "deterministic");
		config.set(RecommendationServiceFactory.CKEY_TOP_N, Integer.toString(20));
		config.set(RecommendationServiceFactory.CKEY_TOP_PERC, Double.toString(20.0));
		config.set(RecommendationServiceFactory.CKEY_GENERATOR, "PurchaseHistory");
		config.set(RecommendationServiceFactory.CKEY_SCORING, "Popularity_Discretized");
		
		Variant v = new Variant("ymal_yf_popularity", EnumSiteFeature.YMAL, config, new TreeMap());
		
		ScriptedRecommendationService rs = (ScriptedRecommendationService)
				RecommendationServiceFactory.configure(v);
		if (rs == null)
			throw new CompileException("cannot compile popularity recommender, see previous error message");
		return rs;
	}

	public List<ContentNodeModel> doRecommendNodes(SessionInput input) {
		RecommendationService smart = getSmartYmalRecommender();
		if (smart == null)
			return Collections.EMPTY_LIST;
		
		List<ContentNodeModel> favorites = popularity.recommendNodes(input);
		favorites = FDStoreRecommender.getInstance().filterProducts(favorites, input.getCartContents(), false, variant.isUseAlternatives());
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
			Map<String, Variant> variantMap = VariantRegistry.getInstance().getServices(EnumSiteFeature.YMAL);
			for (Variant v : variantMap.values()) {
				RecommendationService rs = v.getRecommender();
				if (rs instanceof SmartYMALRecommendationService) {
					return smartYmal = rs;
				}
			}
			return null;
		} else
			return smartYmal;
	}

    public void collectFactors(Collection<String> factors) {
        popularity.collectFactors(factors);
    }
    
    public boolean isRefreshable() {
    	return true;
    }
}
