package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.fdlogistics.services.ICommerceService;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Product statistics provider.
 * 
 * @author istvan
 */
public class ProductStatisticsProvider {

	private static final Category LOGGER = LoggerFactory.getInstance(ProductStatisticsProvider.class);

	private static ProductStatisticsProvider instance;

	/**
	 * Service locator.
	 */
	protected ServiceLocator serviceLocator;

	/**
	 * RESTFUL SERVICE
	 */

	ICommerceService service = null;

	/**
	 * Global product frequency map, Map<@link {@link ContentKey},Float>.
	 */
	protected Map<ContentKey, Float> globalProductScores = new HashMap<ContentKey, Float>();

	/**
	 * Get provider instance.
	 * 
	 * @return instance (may throw a runtime exception if the instance cannot be
	 *         created, and it was not cached)
	 */
	public synchronized static ProductStatisticsProvider getInstance() {
		if (instance == null) {
			try {
				instance = new ProductStatisticsProvider();
			} catch (NamingException e) {
				LOGGER.error("could not initialize service", e);
				throw new RuntimeException("could not initialize service", e);
			} catch (RemoteException e) {
				LOGGER.error("could not cache global product scores", e);
			} catch (CreateException e) {
				LOGGER.error("could not create SS session bean", e);
			}
		}
		return instance;
	}

	/**
	 * Private constructor.
	 * 
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws CreateException
	 */
	private ProductStatisticsProvider() throws NamingException, RemoteException, CreateException {

		service = LogisticsServiceLocator.getInstance().getCommerceService();
		try {
			Map<String, Float> intermediaryMap = service.getDYFModelGlobalProductscores();
			globalProductScores = convertSimpleMapToContentKeyMap(intermediaryMap);
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);

		}

	}

	/**
	 * Get global product score.
	 * 
	 * @param key
	 *            product content key
	 * @return global popularity, -1
	 */
	public float getGlobalProductScore(ContentKey key) {
		Float score = globalProductScores.get(key);
		return score == null ? -1 : score.floatValue();
	}

	/**
	 * Products which the user has ever bought.
	 * 
	 * @param customerID
	 * @return Set<ContentKey>
	 */
	public Set getProducts(String customerID) {

		try {
			Set<String> intermediary = service.getDYFModelProducts(customerID);
			
			return convertStringSetToContentKey(intermediary);
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);

		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}

	}
	
	
	 static Map<ContentKey, Float> convertSimpleMapToContentKeyMap(Map<String, Float> intermediarymap) {
		Map<ContentKey, Float> mapToReturn = new HashMap<ContentKey, Float>();

		Set<String> keys = intermediarymap.keySet();
		for (String individualkey : keys) {
			mapToReturn.put(ContentKeyFactory.get(com.freshdirect.storeapi.fdstore.FDContentTypes.PRODUCT, individualkey),
					intermediarymap.get(individualkey));
		}
		return mapToReturn;

	}
	
	 static Set<ContentKey> convertStringSetToContentKey(Set <String> setofconentkeys){
		
		Set<ContentKey> retset = new HashSet<ContentKey>();
		for (String contentkeyProduct : setofconentkeys) {

			retset.add(ContentKeyFactory.get(com.freshdirect.storeapi.fdstore.FDContentTypes.PRODUCT,
					contentkeyProduct));
		}
		return retset;
	}
}
