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
import com.freshdirect.cms.application.DraftContext;

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
	private static final long serialVersionUID = 8136738845448653594L;
	
	private final ContentServiceI contentService;

	private final ContentKey key;

	/** Map of String (originating service name) -> {@link ContentNodeI} */
	private final Map<String, ContentNodeI> nodes;

	/** Map of String (attribute name) -> {@link AttributeI} */ 
	private final Map<String, AttributeI> attributes = new HashMap<String, AttributeI>();

	/**
	 * CompositeContentNode
	 * 
	 * @param contentService
	 * @param key content key of the composited node
	 * @param nodes Map of String (originating service name) -> {@link ContentNodeI}
	 */
	public CompositeContentNode(ContentServiceI contentService, ContentKey key, Map<String, ContentNodeI> nodes) {
		this.contentService = contentService;
		this.key = key;
		this.nodes = Collections.unmodifiableMap(nodes);
		initializeAttributes();
	}

	//
	// compositing
	//

    private void initializeAttributes() {
        for (ContentNodeI node : nodes.values()) {
            for (AttributeI attribute : node.getAttributes().values()) {
                AttributeI ca = attribute instanceof RelationshipI ? new CompositeRelationship(attribute) : new CompositeAttribute(attribute);
                attributes.put(ca.getName(), ca);
            }
        }
    }

	private class CompositeAttribute implements AttributeI {
		private static final long serialVersionUID = 1240506000120118825L;

		private final AttributeI attribute;

		public CompositeAttribute(AttributeI attribute) {
			this.attribute = attribute;
		}

		@Override
		public Object getValue() {
			return attribute.getValue();
		}

		@Override
		public void setValue(Object o) {
			attribute.setValue(o);
		}

		@Override
		public ContentNodeI getContentNode() {
			return CompositeContentNode.this;
		}

		@Override
		public AttributeDefI getDefinition() {
			return attribute.getDefinition();
		}

		@Override
		public String getName() {
			return attribute.getName();
		}

		@Override
		public String toString() {
			return "CompositeAttribute[" + attribute + "]";
		}
	}

	private class CompositeRelationship extends CompositeAttribute implements RelationshipI {
		private static final long serialVersionUID = 2142686335400764548L;

		public CompositeRelationship(AttributeI attribute) {
			super(attribute);
		}

	}

	/**
	 * Get the composited nodes.
	 * 
	 * @return Map of String (originating service name) -> {@link ContentNodeI}
	 */
	public Map<String, ContentNodeI> getNodes() {
		return nodes;
	}

	//
	// attributes
	//

	@Override
	public ContentKey getKey() {
		return key;
	}

	@Override
	public AttributeI getAttribute(String name) {
		return (AttributeI) attributes.get(name);
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
	public Map<String, AttributeI> getAttributes() {
		return attributes;
	}

	//
	// convenience
	//

	@Override
	public ContentTypeDefI getDefinition() {
		return contentService.getTypeService().getContentTypeDefinition(key.getType());
	}

	@Override
	public Set<ContentKey> getChildKeys() {
		return ContentNodeUtil.getChildKeys(this);
	}

	@Override
	public String getLabel() {
	    // FIXME
		return ContentNodeUtil.getLabel(this, DraftContext.MAIN);
	}

	//
	// infrastructure
	//

	@Override
	public ContentNodeI copy() {
	    Map<String, ContentNodeI> newNodes = new HashMap<String, ContentNodeI>();
	    for (Iterator<String> keyIter = nodes.keySet().iterator();keyIter.hasNext();) {
	        String key = keyIter.next();
	        ContentNodeI node = (ContentNodeI) nodes.get(key);
	        ContentNodeI copiedNode = node.copy();
	        newNodes.put(key, copiedNode);
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