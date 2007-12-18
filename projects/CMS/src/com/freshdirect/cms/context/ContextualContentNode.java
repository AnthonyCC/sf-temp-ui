package com.freshdirect.cms.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	private final ContextualContentNodeI context;
	private final ContentNodeI node;

	/**
	 * @param context
	 * @param node
	 */
	public ContextualContentNode(ContextualContentNodeI context, ContentNodeI node) {
		this.context = context;
		this.node = node;
	}

	public boolean isRoot() {
		return this.context == null;
	}

	public ContextualContentNodeI getParentNode() {
		return this.context;
	}

	public String getPath() {
		return getPath("");
	}

	private String getPath(String path) {
		path = "/" + this.getKey() + path;
		if (this.isRoot()) {
			return path;
		}
		return ((ContextualContentNode) this.context).getPath(path);
	}

	public AttributeI getAttribute(String name) {
		AttributeI attr = node.getAttribute(name);
		if (attr != null && attr.getValue() == null && !this.isRoot() && attr.getDefinition().isInheritable()) {
			return this.context.getAttribute(name);
		}
		return attr;
	}

	public ContentKey getKey() {
		return node.getKey();
	}

	public Map getAttributes() {
		Map inherited = getInheritedAttributes();
		inherited.putAll(node.getAttributes());
		return inherited;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.ContentNodeI#getDefinition()
	 */
	public ContentTypeDefI getDefinition() {
		return node.getDefinition();
	}

	public Map getInheritedAttributes() {
		Map inherited = context != null ? context.getInheritedAttributes() : new HashMap();
		Map self = node.getAttributes();
		return inherit(inherited, self);
	}

	private Map inherit(Map inherited, Map self) {
		Map inheritable = new HashMap();
		for (Iterator i = self.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			Object value = e.getValue();
			boolean inherit = getDefinition().getAttributeDef(name).isInheritable();
			if (inherit) {
				inheritable.put(name, value);
			}
		}

		inherited.putAll(inheritable);
		return inherited;
	}

	public Set getChildKeys() {
		return node.getChildKeys();
	}

	public String getLabel() {
		return node.getLabel();
	}

	public void setDelete(boolean b) {
		node.setDelete(b);
	}

	public boolean isDelete() {
		return node.isDelete();
	}

	public ContentNodeI copy() {
		// unmutable
		return this;
	}

}