package com.freshdirect.cms.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.meta.AttributeDef;

/**
 * Wraps a ContentNodeI and treats one of it's attributes as
 * a String-encoded hash-map. Exposes the encoded attribute as
 * multiple attributes and the original attribute as read-only.
 * 
 * @FIXME Bit too much magic, but hey whatever.. :) Once we have
 * support for nested types, this should be removed. 
 * 
 * @see com.freshdirect.cms.application.service.query.DbQueryDecorator
 * @see com.freshdirect.cms.fdstore.ConfiguredProductDecorator
 */
public class AttributeMappedNode implements ContentNodeI, NodeWrapperI {

	private final ContentNodeI node;
	private final String mapAttributeName;
	private final Map attributes;
	private final ContentTypeDefI definition;
	private final String separator;

	/**
	 * 
	 * @param node wrapped node
	 * @param mapAttributeName the attribute to expose
	 * @param mapDefinition Map of String (key) -> {@link AttributeDefI}
	 */
	public AttributeMappedNode(ContentNodeI node, String mapAttributeName, Map mapDefinition, String separator) {
		this.node = node;
		this.mapAttributeName = mapAttributeName;
		this.separator = separator;

		Map paramAttributes = new HashMap(mapDefinition.size());
		Map paramDefs = new HashMap(mapDefinition.size());
		for (Iterator i = mapDefinition.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			AttributeDefI def = (AttributeDefI) e.getValue();
			MappedAttribute a = new MappedAttribute(node.getAttribute(mapAttributeName), name, def);
			paramAttributes.put(a.getName(), a);
		}

		for (Iterator i = node.getAttributes().values().iterator(); i.hasNext();) {
			AttributeI a = (AttributeI) i.next();
			AttributeDefI definition = null;
			if (mapAttributeName.equals(a.getName())) {
				AttributeDefI p = a.getDefinition();
				if (!EnumAttributeType.STRING.equals(p.getAttributeType())) {
					throw new IllegalArgumentException("Mapped attribute must be a string");
				}
				// map as a read-only attribute
				definition = new AttributeDef(
					p.getAttributeType(),
					p.getName(),
					p.getLabel(),
					p.isRequired(),
					p.isInheritable(),
					true,
					p.getCardinality());
			}
			paramAttributes.put(a.getName(), a instanceof RelationshipI
				? new ProxyRelationship(a, definition)
				: new ProxyAttribute(a, definition));
		}

		this.attributes = Collections.unmodifiableMap(paramAttributes);

		this.definition = new ParametrizedTypeDef(paramDefs);
	}

	private String getSeparator() {
		return separator;
	}

	public ContentKey getKey() {
		return this.node.getKey();
	}

	public ContentTypeDefI getDefinition() {
		return definition;
	}

	public AttributeI getAttribute(String name) {
		return (AttributeI) this.attributes.get(name);
	}

	public Map getAttributes() {
		return this.attributes;
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
		return this;
	}

	public ContentNodeI getWrappedNode() {
		return this.node;
	}

	private class ParametrizedTypeDef implements ContentTypeDefI, Serializable {

		private final Map parameterDefs;

		public ParametrizedTypeDef(Map parameterDefs) {
			this.parameterDefs = parameterDefs;
		}

		private ContentTypeDefI getBaseDef() {
			return node.getDefinition();
		}

		public ContentType getType() {
			return getBaseDef().getType();
		}

		public String getName() {
			return getBaseDef().getName();
		}

		public String getLabel() {
			return getBaseDef().getLabel();
		}

		public AttributeDefI getSelfAttributeDef(String name) {
			AttributeDefI d = (AttributeDefI) parameterDefs.get(name);
			return d == null ? getBaseDef().getSelfAttributeDef(name) : d;
		}

		public Collection getSelfAttributeDefs() {
			List l = new ArrayList();
			l.addAll(parameterDefs.values());
			for (Iterator i = getBaseDef().getSelfAttributeDefs().iterator(); i.hasNext();) {
				AttributeDefI d = (AttributeDefI) i.next();
				if (mapAttributeName.equals(d.getName())) {
					continue;
				}
				l.add(d);
			}
			return l;
		}

		public AttributeDefI getAttributeDef(String name) {
			if (mapAttributeName.equals(name)) {
				return null;
			}
			AttributeDefI d = (AttributeDefI) parameterDefs.get(name);
			return d == null ? getBaseDef().getAttributeDef(name) : d;
		}

		public Set getAttributeNames() {
			Set s = new HashSet();
			s.addAll(parameterDefs.keySet());
			s.addAll(getBaseDef().getAttributeNames());
			s.remove(mapAttributeName);
			return s;
		}

		public boolean isIdGenerated() {
			// TODO: support generated IDs
			return false;
		}

	}

	private class MappedAttribute implements AttributeI {

		private final AttributeDefI definition;
		private final AttributeI mapAttribute;
		private final String key;

		public MappedAttribute(AttributeI mapAttribute, String key, AttributeDefI definition) {
			this.mapAttribute = mapAttribute;
			this.key = key;
			this.definition = definition;
		}

		public Object getValue() {
			return getMap().get(key);
		}

		private Map getMap() {
			return stringToMap((String) mapAttribute.getValue(), getSeparator());
		}

		private void setMap(Map map) {
			mapAttribute.setValue(mapToString(map, getSeparator()));
		}

		public void setValue(Object o) {
			Map m = getMap();
			m.put(key, o);
			setMap(m);
		}

		public ContentNodeI getContentNode() {
			return AttributeMappedNode.this;
		}

		public AttributeDefI getDefinition() {
			return definition;
		}

		public String getName() {
			return definition.getName();
		}

	}

	public static Map stringToMap(String str, String separator) {
		HashMap ret = new HashMap();
		if (str != null) {
			StringTokenizer st = new StringTokenizer(str, separator);
			while (st.hasMoreTokens()) {
				String token = st.nextToken().trim();
				int idx = token.indexOf("=");
				String key = token.substring(0, idx++);
				String value = token.substring(idx, token.length());
				ret.put(key, value);
			}
		}
		return ret;
	}

	public static String mapToString(Map map, String separator) {
		StringBuffer ret = new StringBuffer();
		for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			String key = (String) e.getKey();
			Object value = e.getValue();
			if (value == null) {
				continue;
			}
			ret.append(key);
			ret.append("=");
			ret.append(value);
			if (i.hasNext()) {
				ret.append(separator);
			}
		}
		return ret.toString();
	}

	private class ProxyAttribute implements AttributeI {

		private final AttributeI attribute;

		/** optional definition to override underlying def */
		private final AttributeDefI definition;

		private ProxyAttribute(AttributeI attribute, AttributeDefI definition) {
			this.attribute = attribute;
			this.definition = definition;
		}

		public Object getValue() {
			return attribute.getValue();
		}

		public void setValue(Object o) {
			attribute.setValue(o);
		}

		public ContentNodeI getContentNode() {
			return AttributeMappedNode.this;
		}

		public AttributeDefI getDefinition() {
			return definition != null ? definition : attribute.getDefinition();
		}

		public String getName() {
			return attribute.getName();
		}

	}

	private class ProxyRelationship extends ProxyAttribute implements RelationshipI {

		ProxyRelationship(AttributeI attribute, AttributeDefI definition) {
			super(attribute, definition);
		}

	}

}
