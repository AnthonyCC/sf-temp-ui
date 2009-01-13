package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class CmsContentNodeAdapter implements Serializable, Cloneable, PrioritizedI {
	
	private final static Category LOGGER = LoggerFactory.getInstance(CmsContentNodeAdapter.class);

	private final ContentKey key;

	private boolean fresh = true;
	private ContentNodeModel parentNode;
	private int priority = 1;
    private final String contentType;

	protected CmsContentNodeAdapter(ContentKey key) {
		if (key == null) {
			throw new IllegalArgumentException("ContentKey must not be null");
		}
		this.key = key;
		this.contentType = (String) ContentNodeModelUtil.CONTENT_TO_TYPE_MAP.get(key.getType().getName());
	}

	//
	// core
	//

	protected ContentNodeI getCMSNode() {
		return CmsManager.getInstance().getContentNode(key);
	}

	/** @deprecated use getContentName() */
	public PrimaryKey getPK() {
		// FIXME this should be key.encoded
		return new PrimaryKey(key.getId());
	}

	public ContentKey getContentKey() {
		return key;
	}

	public String getContentName() {
		return this.getContentKey().getId();
	}

	public String getContentType() {
		return this.contentType;
	}

	//
	// attributes
	//

	/**
	 * Get an AttributeI that has a non-null value, with inheritance applied.
	 * 
	 * @return null if the attribute is not defined, or its value is null (or empty list)
	 */
	public AttributeI getCmsAttribute(String name) {
		ContentNodeI node = this.getCMSNode();
		if (node == null) return null;
		AttributeI attr = node.getAttribute(name);
		if (attr == null) return null;

		Object value = attr.getValue();
		if (value != null && !(value instanceof List && ((List) value).isEmpty())) {
			return attr;
		}

		AttributeDefI def = attr.getDefinition();
		if (def == null || !def.isInheritable()) {
			return null;
		}

		ContentNodeModel parent = getParentNode();
		return parent == null ? null : parent.getCmsAttribute(name);
	}

	/**
	 * @deprecated
	 */
	public Attribute getAttribute(String key) {
		AttributeI cmsAttr = getCmsAttribute(key);
		return cmsAttr == null ? null : FDAttributeFactory.getAttribute(cmsAttr);
	}

	/**
	 * @deprecated
	 */
	public boolean hasAttribute(String key) {
		return getCmsAttribute(key) != null;
	}

	public Object getAttribute(String key, Object defaultValue) {
		AttributeI cmsAttr = getCmsAttribute(key);
		if (cmsAttr == null) {
			return defaultValue;
		}
		Attribute a = FDAttributeFactory.getAttribute(cmsAttr);
		Object o = a.getValue();
		return o == null ? defaultValue : o;
	}

	public String getAttribute(String key, String defaultValue) {
		AttributeI attr = this.getCmsAttribute(key);
		return (attr != null) ? (String) attr.getValue() : defaultValue;
	}

	public int getAttribute(String key, int defaultValue) {
		AttributeI attr = this.getCmsAttribute(key);
		return (attr != null) ? ((Integer) attr.getValue()).intValue() : defaultValue;
	}

	public boolean getAttribute(String key, boolean defaultValue) {
		AttributeI attr = this.getCmsAttribute(key);
		return (attr != null) ? ((Boolean) attr.getValue()).booleanValue() : defaultValue;
	}

	public double getAttribute(String key, double defaultValue) {
		AttributeI attr = this.getCmsAttribute(key);
		return (attr != null) ? ((Double) attr.getValue()).doubleValue() : defaultValue;
	}

	//
	// contextual information
	//

	/**
	 * @return Returns the priority.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority The priority to set.
	 */
	protected void setPriority(int priority) {
		this.priority = priority;
	}

	protected void setParentNode(ContentNodeModel parentNode) {
		if (!fresh && this.parentNode != parentNode) {
			throw new IllegalStateException("Cannot reparent node " + key
					+ " from " + this.parentNode + " to " + parentNode
					+ ". Object has to be reconstructed or cloned.");
		}
		this.parentNode = parentNode;
		fresh = false;
	}

	public ContentNodeModel getParentNode() {
		if (parentNode == null) {
			parentNode = ContentNodeModelUtil.findDefaultParent(getContentKey());
			fresh = false;
		}
		return parentNode;
	}

	public Collection getParentKeys() {
            return CmsManager.getInstance().getParentKeys(key);
	}
	
	/**
	 * Recursively find the first parent node of specified type.
	 *
	 * @return null if not found
	 */
//	protected ContentNodeModel getParentNode(String contentType) {
//		ContentNodeModel p = this.getParentNode();
//		return p == null ? null : (p.getContentType().equals(contentType) ? p : p.getParentNode(contentType));
//	}

	public boolean hasParentWithName(String[] contentNames) {
		ContentNodeModel p = this.getParentNode();
		if (p == null) {
			return false;
		}
		final String parentName = p.getContentName();
		for (int i = contentNames.length; --i >= 0;) {
			if (parentName.equals(contentNames[i])) {
				return true;
			}
		}
		return p.hasParentWithName(contentNames);
	}

	//
	// infrastructure
	//

	public Object clone() {
		try {			
			CmsContentNodeAdapter clone = (CmsContentNodeAdapter) super.clone();
			clone.fresh = true;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException();
		}
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof ContentNodeModel) {
			return key.equals(((ContentNodeModel) o).getContentKey());
		}
		return false;
	}

	public int hashCode() {
		return key.hashCode();
	}

    public AttributeI getNotInheritedAttribute(String name) {
        ContentNodeI node = this.getCMSNode();
        if (node == null) {
            return null;
        }
        return node.getAttribute(name);
    }

	
	/**
	 * Very conveniently returns contentName.
	 */
	public String toString() {
		return this.getContentName();
	}

}
