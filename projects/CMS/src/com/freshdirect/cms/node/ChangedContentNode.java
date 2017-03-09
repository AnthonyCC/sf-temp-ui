package com.freshdirect.cms.node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.framework.util.NVL;

/**
 * This implementation wraps node to a proxy that also keeps attribute changes in a map.
 * 
 * @see {@link #changes}
 * 
 * @author segabor
 */
public class ChangedContentNode implements ContentNodeI, NodeWrapperI {

    private static final long serialVersionUID = -3448451752290913494L;

    private ContentNodeI originalNode;

    /**
     * Map of changes. Keys are attribute names. Values are the changes. Nulls mean deleted values.
     */
    private final Map<String, Object> changes;

    public ChangedContentNode(ContentNodeI node) {
        this.originalNode = node;
        this.changes = new HashMap<String, Object>();
    }

    public ChangedContentNode(ContentNodeI node, Map<String, Object> changes) {
        this.originalNode = node;
        this.changes = new HashMap<String, Object>(changes);
    }

    @Override
    public ContentNodeI getWrappedNode() {
        return originalNode;
    }

    @Override
    public ContentKey getKey() {
        return originalNode.getKey();
    }

    @Override
    public ContentTypeDefI getDefinition() {
        return originalNode.getDefinition();
    }

    @Override
    public AttributeI getAttribute(String name) {
        return originalNode.getAttribute(name);
    }

    @Override
    public Object getAttributeValue(String name) {
        return originalNode.getAttributeValue(name);
    }

    /**
     * This method record value change after setting value successfully.
     */
    @Override
    public boolean setAttributeValue(String name, Object value) {
        if (NVL.nullEquals(getAttributeValue(name), value)) {
            return false;
        }

        final boolean result = originalNode.setAttributeValue(name, value);
        if (result) {
            // record change upon successful operation
            changes.put(name, value);
        }
        return result;
    }

    @Override
    public Map<String, AttributeI> getAttributes() {
        return originalNode.getAttributes();
    }

    @Override
    public Set<ContentKey> getChildKeys() {
        return originalNode.getChildKeys();
    }

    @Override
    public String getLabel() {
        return originalNode.getLabel();
    }

    @Override
    public ContentNodeI copy() {
        return new ChangedContentNode( originalNode.copy(), changes );
    }

    /**
     * Access changed values
     * 
     * @return
     */
    public Map<String, Object> getChanges() {
        return changes;
    }

    public boolean isChanged() {
        return !changes.isEmpty();
    }

    @Override
    public String toString() {
        return "ChangedContentNode [originalNode=" + originalNode + ", changes=" + changes + "]";
    }
}
