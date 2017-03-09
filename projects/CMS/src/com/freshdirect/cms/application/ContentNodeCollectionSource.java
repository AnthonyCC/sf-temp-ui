package com.freshdirect.cms.application;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.node.ContentNodeUtil;

/**
 * A very basic store source implementation backed by collection of content nodes
 * 
 * @author segabor
 *
 */
public class ContentNodeCollectionSource implements ContentNodeSource {

    protected final Collection<ContentNodeI> nodes;
    
    protected final Map<ContentKey, ContentNodeI> nodeMap = new HashMap<ContentKey, ContentNodeI>();

    protected final Map<ContentKey, Set<ContentKey>> parentKeyIndex;

    public ContentNodeCollectionSource(Collection<ContentNodeI> nodes) {
        
        this.nodes = nodes;
        // build content key -> content node index
        for (ContentNodeI node : nodes) {
            nodeMap.put(node.getKey(), node);
        }

        // build parent key index
        parentKeyIndex = ContentNodeUtil.getParentIndex(nodes);
    }

    public Collection<ContentNodeI> getContentNodes() {
        return Collections.unmodifiableCollection(nodes);
    }

    @Override
    public ContentNodeI getContentNode(ContentKey contentKey, DraftContext draftContext) {
        return nodeMap.get(contentKey);
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey contentKey, DraftContext draftContext) {
        final Set<ContentKey> parentKeys = parentKeyIndex.get(contentKey);
        return parentKeys != null ? parentKeys : Collections.<ContentKey>emptySet();
    }

    @Override
    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
        Map<ContentKey, ContentNodeI> contentNodes = new HashMap<ContentKey, ContentNodeI>();
        for (ContentKey key : keys) {
            contentNodes.put(key, nodeMap.get(key));
        }
        return contentNodes;
    }
}
