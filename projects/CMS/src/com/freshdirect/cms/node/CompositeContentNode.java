package com.freshdirect.cms.node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.ContentServiceI;

/**
 * Composite content node that merges the attributes of multiple nodes.
 * <p>
 * The origin of these nodes is maintained as the key set of the Map
 * provided in the constructor. The composited nodes can be retrieved
 * with {@link #getNodes()}. 
 * <p>
 * If the same attribute is present in multiple content nodes, the last one
 * takes precedence based on the ordering of nodes in the Map. Thus for
 * deterministic results, a {@link java.util.SortedMap} is recommended.
 * 
 * @see com.freshdirect.cms.application.service.CompositeContentService
 */
public class CompositeContentNode implements ContentNodeI {

	private final ContentServiceI contentService;

	private final ContentKey key;

	/** Map of String (originating service name) -> {@link ContentNodeI} */
	private final Map nodes;

	/** Map of String (attribute name) -> {@link AttributeI} */ 
	private final Map attributes = new HashMap();

	/**
	 * 
	 * @param contentService
	 * @param key content key of the composited node
	 * @param nodes Map of String (originating service name) -> {@link ContentNodeI}
	 */
	public CompositeContentNode(ContentServiceI contentService, ContentKey key, Map nodes) {
		this.contentService = contentService;
		this.key = key;
		this.nodes = Collections.unmodifiableMap(nodes);
		initializeAttributes();
	}

	//
	// compositing
	//

	private void initializeAttributes() {
		for (Iterator i = nodes.values().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			for (Iterator j = node.getAttributes().values().iterator(); j.hasNext();) {
				AttributeI a = (AttributeI) j.next();
				AttributeI ca = a instanceof RelationshipI ? new CompositeRelationship(a) : new CompositeAttribute(a);
				attributes.put(ca.getName(), ca);
			}
		}
	}

	private class CompositeAttribute implements AttributeI {

		private final AttributeI attribute;

		public CompositeAttribute(AttributeI attribute) {
			this.attribute = attribute;
		}

		public Object getValue() {
			return attribute.getValue();
		}

		public void setValue(Object o) {
			attribute.setValue(o);
		}

		public ContentNodeI getContentNode() {
			return CompositeContentNode.this;
		}

		public AttributeDefI getDefinition() {
			return attribute.getDefinition();
		}

		public String getName() {
			return attribute.getName();
		}

		public String toString() {
			return "CompositeAttribute[" + attribute + "]";
		}
	}

	private class CompositeRelationship extends CompositeAttribute implements RelationshipI {

		public CompositeRelationship(AttributeI attribute) {
			super(attribute);
		}

	}

	/**
	 * Get the composited nodes.
	 * 
	 * @return Map of String (originating service name) -> {@link ContentNodeI}
	 */
	public Map getNodes() {
		return nodes;
	}

	//
	// attributes
	//

	public ContentKey getKey() {
		return key;
	}

	public AttributeI getAttribute(String name) {
		return (AttributeI) attributes.get(name);
	}

	public Map getAttributes() {
		return attributes;
	}

	//
	// convenience
	//

	public ContentTypeDefI getDefinition() {
		return contentService.getTypeService().getContentTypeDefinition(key.getType());
	}

	public Set getChildKeys() {
		return ContentNodeUtil.getChildKeys(this);
	}

	public String getLabel() {
		return ContentNodeUtil.getLabel(this);
	}

	//
	// infrastructure
	//

	private boolean delete = true;

	public void setDelete(boolean b) {
		delete = b;
	}

	public boolean isDelete() {
		return delete;
	}

	public ContentNodeI copy() {
	    Map newNodes = new HashMap();
	    for (Iterator keyIter = nodes.keySet().iterator();keyIter.hasNext();) {
	        Object key = keyIter.next();
	        ContentNodeI node = (ContentNodeI) nodes.get(key);
	        newNodes.put(key, node.copy());
	    }
	    return new CompositeContentNode(contentService,key, newNodes);
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ContentNodeI) {
			ContentNodeI node = (ContentNodeI) obj;
			return key.equals(node.getKey());
		}
		return false;
	}

	public int hashCode() {
		return this.key.hashCode();
	}

	public String toString() {
		return "CompositeContentNode[" + this.key + " -> " + nodes + "]";
	}

}