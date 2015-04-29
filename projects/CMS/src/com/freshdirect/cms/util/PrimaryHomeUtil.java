package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.NodeWalker;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PrimaryHomeUtil {
	private static final Category LOGGER = LoggerFactory.getInstance(PrimaryHomeUtil.class);


	public static ContentNodeI findParent(ContentNodeI prd, ContentServiceI svc, String storeId) {
		if (prd == null || svc == null || storeId == null) {
			return null;
		} else if (!FDContentTypes.PRODUCT.equals( prd.getKey().getType())) {
			return null;
		}
		
		Set<ContentKey> parentKeys = svc.getParentKeys(prd.getKey());
		
		for (ContentKey parentKey : parentKeys) {
			final ContentNodeI parentNode = svc.getContentNode(parentKey);
			if (findParentRecursively(parentNode, svc, storeId)) {
				return parentNode;
			}
		}
		
		
		return null;
	}
	
	private static boolean findParentRecursively(ContentNodeI node, ContentServiceI svc, String storeId) {
		while (node != null) {
			if (FDContentTypes.STORE.equals(node.getKey().getType()) && storeId.equals(node.getKey().getId() ) ) {
				return true;
			}

			Set<ContentKey> parentKeys = svc.getParentKeys(node.getKey());
			if (parentKeys == null || parentKeys.size() == 0) {
				node = null;
			} else {
				// pick first parent node (set should always contain exactly one node)
				node = svc.getContentNode(parentKeys.iterator().next());
			}
		}
		return false;
	}

	/**
	 * Setup a map of store key -> parent cats for a product
	 * 
	 * @param prdKey
	 * @param svc
	 * @return
	 */
	public static Map<ContentKey, Set<ContentKey>> collectParentsMap(ContentKey prdKey, ContentServiceI svc) {
		if (prdKey == null || !FDContentTypes.PRODUCT.equals(prdKey.getType())) {
			return Collections.emptyMap();
		}

		Set<ContentKey> _parents = svc.getParentKeys(prdKey);

		// the result map
		Map<ContentKey, Set<ContentKey>> result = new HashMap<ContentKey, Set<ContentKey>>();
		for (ContentKey pKey : _parents) {
			Set<ContentKey> aSet = new HashSet<ContentKey>();
			collectStoreKeys(aSet, pKey, svc);
			if (aSet.size() > 0) {
				// pick store key
				ContentKey storeKey = aSet.iterator().next();
				Set<ContentKey> parCats = result.get(storeKey);
				if (parCats == null) {
					parCats = new HashSet<ContentKey>();
					result.put(storeKey, parCats);
				}
				// store parent cat in store bucket
				parCats.add(pKey);
			}
		}
		
		return result;
	}


	/**
	 * Collect all available store keys for the given content node
	 * 
	 * @param aKey content key of node
	 * @param svc
	 * @return
	 */
	public static Set<ContentKey> getStoreKeys(final ContentKey aKey, final ContentServiceI svc) {
		Set<ContentKey> result = new HashSet<ContentKey>();
		
		if (aKey != null) {
			collectStoreKeys(result, aKey, svc);
		}
		
		return result;
	}


	protected static void collectStoreKeys(final Set<ContentKey> result, final ContentKey aKey, final ContentServiceI svc) {
		if (aKey == null) {
			return;
		}
		
		for (ContentKey k : svc.getParentKeys(aKey)) {
			if (FDContentTypes.STORE.equals(k.getType())) {
				if (result != null) {
					result.add(k);
				}
				break;
			} else {
				collectStoreKeys(result, k, svc);
			}
		}
	}


	/**
	 * Utility method that fixes primary homes of a product node if needed
	 * 
	 * @param node product content node
	 * @param theStoreKey optional
	 * @param service content service
	 * @return
	 */
	public static Set<ContentKey> fixPrimaryHomes(ContentNodeI node, ContentServiceI service, ContentKey theStoreKey) {
		final ContentKey prdKey = node.getKey();
		
		// map of store key -> parent categories
		// Note: it can be partial as not all products are shared among all stores
		final Map<ContentKey, Set<ContentKey>> _s2p_map = PrimaryHomeUtil.collectParentsMap(prdKey, service);

		// current set of primary homes
		@SuppressWarnings("unchecked")
		List<ContentKey> homes = (List<ContentKey>) node.getAttributeValue("PRIMARY_HOME");
		if (homes == null) {
			homes = Collections.emptyList();
		}


		Set<ContentKey> _storeKeySet;
		if (theStoreKey != null) {
			// single-store case: process only the given store key
			_storeKeySet = new HashSet<ContentKey>();
			_storeKeySet.add(theStoreKey);
		} else {
			// multi-store case: run through all keys
			_storeKeySet = _s2p_map.keySet();
		}

		Set<ContentKey> result = new HashSet<ContentKey>(); 


		// MAIN LOOP
		// Go through stores and check if primary home is already set and it is correct
		//   otherwise fix it
		for (ContentKey storeKey : _storeKeySet) {
			
			// parent categories under store
			final Set<ContentKey> _catKeys = _s2p_map.get(storeKey);
			if (_catKeys == null || _catKeys.isEmpty()) {
				// product has no parents under this store, skip
				continue;
			}


			// intersect parent categories with primary homes
			final Set<ContentKey> _test = new HashSet<ContentKey>( _catKeys );
			_test.retainAll(homes);

			if (_test.isEmpty()) {
				// no primary home found for the given store
				// FIX IT!

				ContentKey newHome = assignPrimaryHome(node, storeKey, _catKeys, service );

				if (newHome != null) {
					LOGGER.debug("fixPrimaryHomes(" + prdKey.getId() + ", " + storeKey.getId() + ") ~> " + newHome.getId());

					result.add(newHome);
				} else {
					// no valid home found for the store node, sad story
					LOGGER.error("fixPrimaryHomes(" + prdKey.getId() + ", " + storeKey.getId() + ") NO PH FOUND, ABORT!");
					return null;
				}
			} else {
				// best case: valid parent found, NO FIX NEEDED
				//   just store the existing primary home
				LOGGER.debug("fixPrimaryHomes(" + prdKey.getId() + ", " + storeKey.getId() + ") -> <same PH>");

				result.addAll(_test);
			}
		}
		LOGGER.info(prdKey.getId() + " =PH=> " + result.toString());

		return result;
	}


	/**
	 * Auto assign a primary home if none is assigned yet
	 * 
	 * @param node a product node
	 * @param storeKey actual store root
	 * @param parentKeys parent keys of product
	 * @param service
	 * @return
	 */
	private static ContentKey assignPrimaryHome(ContentNodeI node, ContentKey storeKey, Set<ContentKey> parentKeys, ContentServiceI service) {
		List<ContentKey> deptKeys = new ArrayList<ContentKey>(CmsManager.getInstance().getContentNode(storeKey).getChildKeys());
		
		final ContextService ctxService = new ContextService(service);

		/*
		 * Get all possible paths going from this node to the root.
		 * Context := <this node>->Cat(->Cat)*->Dept
		 */
		List<Context> ctxs = new ArrayList<Context>(ctxService.getAllContextsOf(node.getKey()));

		// Remove orphaned contexts
		NodeWalker.filterOrphanedParents(ctxs.iterator(), ctxService);

		// Remove contexts connected to other store
		Iterator<Context> it = ctxs.iterator();
		while (it.hasNext()) {
			Context c = it.next();
			if ( !storeKey.equals( c.getRootContext().getContentKey() )) {
				it.remove();
			}
		}

		// sort keys by rank
		Collections.sort(ctxs, NodeWalker.getRankedComparator(ctxService, deptKeys));

		// new primary home := parent (category) node of the best scored contextualized node (that is a product)
		return ctxs.isEmpty() ? null : ctxs.get(0).getParentContext().getContentKey();

		/* ContentKey ph = ctxs.size() == 0
				? null :
					ctxService.getContextualizedContentNode( ctxs.get(0) )
						.getParentNode().getKey();
		return ph; */
	}


	/**
	 * Pick the right primary home for the given store from the primary home list
	 *  
	 * @param key product key
	 * @param storeKey store key
	 * @param svc content service
	 * @return
	 */
	public static ContentKey pickPrimaryHomeForStore(ContentKey key, ContentKey storeKey, ContentServiceI svc) {
		if (key == null || storeKey == null || svc == null) {
			return null;
		}

		ContentNodeI prd = svc.getContentNode(key);
		if (prd == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		List<ContentKey> _priHomes = (List<ContentKey>) prd.getAttributeValue("PRIMARY_HOME");

		if (_priHomes != null && !_priHomes.isEmpty()) {
			// store to parent category keys mapping
			Map<ContentKey, Set<ContentKey>> _s2p = PrimaryHomeUtil.collectParentsMap(key, svc);

			if (_s2p.get(storeKey) != null) {
				Set<ContentKey> candidates = new HashSet<ContentKey>(_s2p.get(storeKey));
				candidates.retainAll(_priHomes);

				ContentKey priHome = candidates.iterator().next();
				return priHome;
			}
		}

		return null;
	}
	
	
	// Suitable cms types
	public static final ContentType TYPES[] = { FDContentTypes.CATEGORY,
			FDContentTypes.DEPARTMENT, FDContentTypes.SUPER_DEPARTMENT,
			FDContentTypes.STORE };
}