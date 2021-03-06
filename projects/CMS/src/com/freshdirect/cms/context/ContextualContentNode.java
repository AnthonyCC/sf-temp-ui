package com.freshdirect.cms.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;

/**
 * Implementation of {@link com.freshdirect.cms.context.ContextualContentNodeI}.
 * Acts as a proxy to a content node and applies contextual acquisition
 * (aka inheritance) of attributes given a parent context node.
 */
class ContextualContentNode implements ContextualContentNodeI {

	private static final long	serialVersionUID	= 6476077866185509385L;
	
	private final ContextualContentNodeI parent;
	private final ContentNodeI node;

	/**
	 * @param context
	 * @param node
	 */
	public ContextualContentNode(ContextualContentNodeI context, ContentNodeI node) {
		this.parent = context;
		this.node = node;
	}

	@Override
	public boolean isRoot() {
		return this.parent == null;
	}

	@Override
	public ContextualContentNodeI getParentNode() {
		return this.parent;
	}

	@Override
	public String getPath() {
		return getPath("");
	}

	private String getPath(String path) {
		path = "/" + this.getKey() + path;
		if (this.isRoot()) {
			return path;
		}
		return ((ContextualContentNode) this.parent).getPath(path);
	}

	@Override
	public AttributeI getAttribute(String name) {
		AttributeI attr = node.getAttribute(name);
		if (attr != null && attr.getValue() == null && !this.isRoot() && attr.getDefinition().isInheritable()) {
			return this.parent.getAttribute(name);
		}
		return attr;
	}
	
        @Override
        public Object getAttributeValue(String name) {
            AttributeI a = getAttribute(name);
            return a != null ? a.getValue() : null;
        }

        @Override
        public boolean setAttributeValue(String name, Object value) {
            AttributeI a = getAttribute(name);
            if (a != null) {
                a.setValue(value);
                return true;
            }
            return false;
        }
        
    @Override
    public ContentKey getKey() {
		return node.getKey();
	}

    @Override
	public Map<String, AttributeI> getAttributes() {
		Map<String, AttributeI> inherited = getInheritedAttributes();
		inherited.putAll(node.getAttributes());
		return inherited;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.ContentNodeI#getDefinition()
	 */
	@Override
	public ContentTypeDefI getDefinition() {
		return node.getDefinition();
	}
	
	@Override
	public Map<String, AttributeI> getParentInheritedAttributes() {
		return parent != null ? parent.getInheritedAttributes() : new HashMap<String, AttributeI>();
	}

	@Override
	public Map<String, AttributeI> getInheritedAttributes() {
		Map<String, AttributeI> inherited = parent != null ? parent.getInheritedAttributes() : new HashMap();
		Map<String, AttributeI> self = node.getAttributes();
		return inherit(inherited, self);
	}

    private Map<String, AttributeI> inherit(Map<String, AttributeI> inherited, Map<String, AttributeI> self) {
        Map<String, AttributeI> inheritable = new HashMap<String, AttributeI>();
        for (Iterator i = self.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();
            AttributeI attr = (AttributeI) e.getValue();
            Object value = attr.getValue();
            AttributeDefI attributeDef = attr.getDefinition();
            boolean inherit = attributeDef.isInheritable();
            if (inherit && value != null) {
                inheritable.put(name, attr);
            }
        }

        inherited.putAll(inheritable);
        return inherited;
    }

    @Override
	public Set<ContentKey> getChildKeys() {
		return node.getChildKeys();
	}

    @Override
	public String getLabel() {
		return node.getLabel();
	}

    @Override
	public ContentNodeI copy() {
		// unmutable
		return this;
	}
	
	@Override
	public String toString() {
	    return "ContextualContentNode["+node.getKey()+','+parent+']';
	}

}