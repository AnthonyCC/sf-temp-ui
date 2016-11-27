package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A simple helper class that strips down content nodes
 * to the minimal set needed for a single-store publish
 * 
 * @author segabor
 *
 */
public class SingleStoreFilterHelper {
    final static Logger LOG = LoggerFactory.getInstance(SingleStoreFilterHelper.class);

	private static ContentType[] STORE_TREE_TYPES = {
		// FDContentTypes.SUPER_DEPARTMENT,
		FDContentTypes.DEPARTMENT,
		FDContentTypes.CATEGORY
	};



	/**
	 * @param storeId ID of Store node (like "FreshDirect" or "FDX")
	 * @param nodes Initial set of content nodes that will be filtered
	 */
	public static List<ContentNodeI> filterContentNodes(final String storeId, Collection<ContentNodeI> nodes, ContentServiceI svc, DraftContext draftContext) {
		Iterator<ContentNodeI> it = nodes.iterator();
		
		final ContentKey _storeKey = ContentKey.decode(FDContentTypes.STORE + ":" + storeId);
		
		List<ContentNodeI> result = new ArrayList<ContentNodeI>(nodes.size());

	loop:
		for (final ContentNodeI node : nodes) {
			// ContentNodeI node = it.next();
			ContentKey key = node.getKey();
			ContentType t = key.getType();


			if (FDContentTypes.STORE.equals(t)) {
				// -- Store type --
				
				// Not the right one, bye ...!
				if (!storeId.equals(key.getId())) {
					LOG.debug(".. dropping node ('other store') " + key);
					continue loop;
				}

			} else if (FDContentTypes.PRODUCT.equals(t)) {

				processProduct(node, _storeKey, result, svc, draftContext );

				continue loop;
			} else {
				// -- Store Tree type --
				// nodes under store tree region
				
				for (ContentType treeType : STORE_TREE_TYPES) {

					if (treeType.equals(t)) {
						// type matched!
						if (isMemberOfStore(node, storeId, svc, draftContext)) {
							result.add(node);
						} else {
							LOG.debug(".. dropping node ('not part of actual store tree') " + key);
						}
						continue loop;
					}
				}
			}

			// ok, put node in the result set
			result.add(node);
		}


		// TODO Check relationships
		return result;
	}



	/**
	 * Process a product node.
	 * 
	 * @param node Product node
	 * @param storeKey Key of actual store node
	 * @param out result set. Add node to it that qualifies.
	 * @param svc
	 */
	private static void processProduct(final ContentNodeI node, final ContentKey storeKey, Collection<ContentNodeI> out, ContentServiceI svc, DraftContext draftContext) {
		final ContentKey key = node.getKey();

		Set<ContentKey> fixedHomes = PrimaryHomeUtil.fixPrimaryHomes(node, svc, draftContext, storeKey);
		if (fixedHomes == null) {
			LOG.error("Error occurred while fixing primary homes of product " + key);

			return;
		} else if (fixedHomes.isEmpty()) {
			LOG.debug(".. dropping node: 'not part of store': " + key);
			return;
		}



		@SuppressWarnings("unchecked")
		List<ContentKey> homes = (List<ContentKey>) node.getAttributeValue("PRIMARY_HOME");
		final boolean isHomesChanged = !fixedHomes.containsAll(homes) || !homes.containsAll(fixedHomes);


		if (isHomesChanged) {
			// make the change
			ContentNodeI clone = node.copy();
			clone.setAttributeValue("PRIMARY_HOME", new ArrayList<ContentKey>( fixedHomes ) );

			// pass the changed node
			out.add(clone);
		} else {
			// nothing changed, pass the original node
			out.add(node);
		}
	}
	



	private static boolean isMemberOfStore(ContentNodeI node, final String storeId, final ContentServiceI svc, DraftContext draftContext) {
		if (node == null || storeId == null) {
			return false;
		}
		
		if (FDContentTypes.STORE.equals( node.getKey().getType()) ) {
			return storeId.equals(node.getKey().getId());
		}
		
		// climb up the tree
		final Set<ContentKey> parentKeys = svc.getParentKeys(node.getKey(), draftContext);
		if (parentKeys == null || parentKeys.size() == 0) {
			return false;
		}

		for (ContentKey parentKey : parentKeys) {
			ContentNodeI pNode = svc.getContentNode(parentKey, draftContext);
			if (isMemberOfStore(pNode, storeId, svc, draftContext)) {
				return true;
			}
		}

		return false;
	}
}
