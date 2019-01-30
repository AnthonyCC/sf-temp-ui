package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class DatabaseScoreFactorProvider {

	private static Logger LOGGER = LoggerFactory.getInstance(DatabaseScoreFactorProvider.class);

	private static DatabaseScoreFactorProvider instance = null;

	private String eStoreId = EnumEStoreId.FD.getContentId();

	public static synchronized DatabaseScoreFactorProvider getInstance() {
		if (instance == null) {

			instance = new DatabaseScoreFactorProvider();

		}
		return instance;
	}

	public Map<String, double[]> getPersonalizedFactors(String erpCustomerId, List<String> factors) {

		if (erpCustomerId == null) {
			return Collections.emptyMap();
		}

		return FDECommerceService.getInstance().getPersonalizedFactors(eStoreId, erpCustomerId, factors);

	}

	public Map<String, double[]> getGlobalFactors(List<String> factors) {
		return FDECommerceService.getInstance().getGlobalFactors(eStoreId, factors);

	}

	public Set<String> getGlobalFactorNames() {
		return FDECommerceService.getInstance().getGlobalFactorNames();

	}

	public Set<String> getPersonalizedFactorNames() {
		return FDECommerceService.getInstance().getPersonalizedFactorNames();

	}

	public Set<String> getPersonalizedProducts(String erpCustomerId) {
		return FDECommerceService.getInstance().getPersonalizedProducts(eStoreId, erpCustomerId);

	}

	public Set<String> getGlobalProducts() {
		return FDECommerceService.getInstance().getGlobalProducts(eStoreId);

	}

	/**
	 * Return a list of product recommendation for a given product by a recommender
	 * vendor.
	 *
	 * @param recommender
	 * @param key
	 * @return List<ContentKey>
	 * @throws RemoteException
	 */
	@Deprecated
	public List<ContentKey> getProductRecommendations(String recommender, ContentKey key) {
		List<ContentKey> contentKeys = new ArrayList<ContentKey>();
		List<String> result = FDECommerceService.getInstance().getProductRecommendations(recommender, key.getId());
		for (String productId : result) {
			contentKeys.add(ContentKeyFactory.get(FDContentTypes.PRODUCT, productId));
		}
		return contentKeys;

	}

	/**
	 * Return a list of personal recommendation (ContentKey-s) for a user by a
	 * recommender vendor.
	 *
	 * @param recommender
	 * @param erpCustomerId
	 * @return List<ContentKey>
	 * @throws RemoteException
	 */
	@Deprecated
	public List<ContentKey> getPersonalRecommendations(String recommender, String erpCustomerId) {
		List<ContentKey> contentKeys = new ArrayList<ContentKey>();
		List<String> result = FDECommerceService.getInstance().getPersonalRecommendations(recommender, erpCustomerId);
		for (String productId : result) {
			contentKeys.add(ContentKeyFactory.get(FDContentTypes.PRODUCT, productId));
		}
		return contentKeys;

	}

	private DatabaseScoreFactorProvider() {
		this.eStoreId = CmsManager.getInstance().getEStoreId();

	}

	@Deprecated
	public EnumWinePrice getPreferredWinePrice(String erpCustomerId) {

		String result = FDECommerceService.getInstance().getPreferredWinePrice(erpCustomerId);
		return EnumWinePrice.valueOf(result);

	}

}
