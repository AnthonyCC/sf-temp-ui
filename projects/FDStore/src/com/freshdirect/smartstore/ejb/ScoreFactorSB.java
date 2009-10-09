package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.cms.ContentKey;


/**
 * 
 * @author istvan
 *
 */
public interface ScoreFactorSB extends EJBObject {
	
	/**
	 * Get the required personalized factors for user.
	 * 
	 * The returned object is a map indexed by product ids and the values
	 * are arrays of doubles containing the factor scores exactly in the order listed
	 * in the <tt>factors</tt> parameter. Null values in the database translate to 
	 * zeros as scores. Integers are converted into doubles.
	 * 
	 * @param erpCustomerId 
	 * @param factors List<FactorName:{@link String}>
	 * @throws RemoteException
	 * @return Map<ProductId:{@link String},double[]>
	 */
	public Map getPersonalizedFactors(String erpCustomerId, List factors) throws RemoteException;
	
	/**
	 * Get the required global factors.
	 * 
	 * The returned object is has the same semantics as that of
	 * {@link #getPersonalizedFactors(String, List)}.
	 * 
	 * @param factors List<FactorName:{@link String}>
	 * @throws RemoteException
	 * @return Map<ProductId:{@link String},double[]>
	 */	
	public Map getGlobalFactors(List factors) throws RemoteException;
	
	/**
	 * Get personalized factor names.
	 * 
	 * @return Set<{@link String}>
	 * @throws RemoteException
	 */
	public Set getPersonalizedFactorNames() throws RemoteException;

	/**
	 * Get the global factor names.
	 * 
	 * @return Set<{@link String>
	 * @throws RemoteException
	 */
	public Set getGlobalFactorNames() throws RemoteException;
	
	/**
	 * Get the product ids that have any scores.
	 * 
	 * @return Set<ProductId:String>
	 * @throws RemoteException
	 */
	public Set getGlobalProducts() throws RemoteException;
	
	/**
	 * Get the product ids for the user that have any scores.
	 * 
	 * @param erpCustomerId 
	 * @return Set<ProductId:String>
	 * @throws RemoteException
	 */
	public Set getPersonalizedProducts(String erpCustomerId) throws RemoteException;
	

	/**
	 * Return a list of product recommendation for a given product by a recommender vendor.
	 * 
	 * @param recommender
	 * @param key
	 * @return List<ContentKey>
	 * @throws RemoteException
	 */
	public List<ContentKey> getProductRecommendations(String recommender, ContentKey key) throws RemoteException;
	
	/**
	 * Return a list of personal recommendation (ContentKey-s) for a user by a recommender vendor.
	 *  
	 * @param recommender
	 * @param erpCustomerId
	 * @return List<ContentKey> 
	 * @throws RemoteException
	 */
	public List<ContentKey> getPersonalRecommendations(String recommender, String erpCustomerId) throws RemoteException;
}
