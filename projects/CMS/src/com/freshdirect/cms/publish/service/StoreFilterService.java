package com.freshdirect.cms.publish.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentNodeSource;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.util.PrimaryHomeUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A service that strips down content nodes to the minimal set needed for a single-store publish
 *
 * @author segabor
 *
 */
public final class StoreFilterService {

    private static final StoreFilterService INSTANCE = new StoreFilterService();
    
    private static final Logger LOGGER = LoggerFactory.getInstance(StoreFilterService.class);

    private static final ContentType[] STORE_TREE_TYPES = {
        // FDContentTypes.SUPER_DEPARTMENT,
        FDContentTypes.DEPARTMENT, FDContentTypes.CATEGORY };

    public static StoreFilterService defaultService() {
        return INSTANCE;
    }
    
    public List<ContentNodeI> filterContentNodes(final String storeId, Collection<ContentNodeI> nodes, ContentNodeSource source, DraftContext draftContext) {
        final ContentKey storeKey = ContentKey.getContentKey(FDContentTypes.STORE + ":" + storeId);
        return filterContentNodes(storeKey, nodes, source, draftContext);
    }

    /**
     * @param storeId
     *            ID of Store node (like "FreshDirect" or "FDX")
     * @param nodes
     *            Initial set of content nodes that will be filtered
     */
    public List<ContentNodeI> filterContentNodes(final ContentKey storeKey, Collection<ContentNodeI> nodes, ContentNodeSource source, DraftContext draftContext) {
        List<ContentNodeI> result = new ArrayList<ContentNodeI>(nodes.size());

        loop: for (final ContentNodeI node : nodes) {
            // ContentNodeI node = it.next();
            ContentKey key = node.getKey();
            ContentType t = key.getType();

            if (FDContentTypes.STORE.equals(t)) {
                // -- Store type --

                // Not the right one, bye ...!
                if (!storeKey.equals(key)) {
                    LOGGER.debug(".. dropping node ('other store') " + key);
                    continue loop;
                }

            } else if (FDContentTypes.PRODUCT.equals(t)) {

                processProduct(node, storeKey, result, source, draftContext);

                continue loop;
            } else {
                // -- Store Tree type --
                // nodes under store tree region

                for (ContentType treeType : STORE_TREE_TYPES) {

                    if (treeType.equals(t)) {
                        // type matched!
                        if (isMemberOfStore(node, storeKey, source, draftContext)) {
                            result.add(node);
                        } else {
                            LOGGER.debug(".. dropping node ('not part of actual store tree') " + key);
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
     * @param node
     *            Product node
     * @param storeKey
     *            Key of actual store node
     * @param out
     *            result set. Add node to it that qualifies.
     * @param svc
     */
    private void processProduct(final ContentNodeI node, final ContentKey storeKey, Collection<ContentNodeI> out, ContentNodeSource svc, DraftContext draftContext) {
        final ContentKey key = node.getKey();

        Set<ContentKey> fixedHomes = PrimaryHomeUtil.fixPrimaryHomes(node, svc, draftContext, storeKey);
        if (fixedHomes == null) {
            LOGGER.error("Error occurred while fixing primary homes of product " + key);

            return;
        } else if (fixedHomes.isEmpty()) {
            LOGGER.debug(".. dropping node: 'not part of store': " + key);
            return;
        }

        @SuppressWarnings("unchecked")
        List<ContentKey> homes = (List<ContentKey>) node.getAttributeValue("PRIMARY_HOME");
        final boolean isHomesChanged = (homes == null) || !fixedHomes.containsAll(homes) || !homes.containsAll(fixedHomes);

        if (isHomesChanged) {
            // make the change
            ContentNodeI clone = node.copy();
            clone.setAttributeValue("PRIMARY_HOME", new ArrayList<ContentKey>(fixedHomes));

            // pass the changed node
            out.add(clone);
        } else {
            // nothing changed, pass the original node
            out.add(node);
        }
    }

    private boolean isMemberOfStore(ContentNodeI node, final ContentKey storeKey, final ContentNodeSource source, DraftContext draftContext) {
        if (node == null || storeKey == null) {
            return false;
        }

        boolean isMember = false;

        if (FDContentTypes.STORE.equals(node.getKey().getType())) {
            isMember = storeKey.equals(node.getKey());
        } else {
            // climb up the tree
            final Set<ContentKey> parentKeys = source.getParentKeys(node.getKey(), draftContext);
            if (parentKeys != null && !parentKeys.isEmpty()) {
                for (ContentKey parentKey : parentKeys) {
                    ContentNodeI pNode = source.getContentNode(parentKey, draftContext);
                    if (isMemberOfStore(pNode, storeKey, source, draftContext)) {
                        isMember = true;
                        break;
                    }
                }
            }
        }

        return isMember;
    }
}
