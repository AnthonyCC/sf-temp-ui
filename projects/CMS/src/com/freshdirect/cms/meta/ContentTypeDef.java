package com.freshdirect.cms.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;

/**
 * Basic implementation of {@link com.freshdirect.cms.ContentTypeDefI}.
 * 
 * @see com.freshdirect.cms.meta.AttributeDef
 * @see com.freshdirect.cms.meta.RelationshipDef
 */
public class ContentTypeDef implements ContentTypeDefI {

	private final ContentTypeServiceI typeService;

	private final ContentType type;

	private final String label;
	private final Map attributeDefs = new HashMap();

	private boolean idGenerated = false;

	/**
	 * @param typeService the backing type service
	 * @param type
	 * @param label
	 */
	public ContentTypeDef(ContentTypeServiceI typeService, ContentType type, String label) {
		this.typeService = typeService;
		this.type = type;
		this.label = label;
	}

	public ContentType getType() {
		return type;
	}

	public String getName() {
		return type.getName();
	}

	public String getLabel() {
		return label == null ? getName() : label;
	}

	public Collection getSelfAttributeDefs() {
		return Collections.unmodifiableCollection(this.attributeDefs.values());
	}

	public AttributeDefI getSelfAttributeDef(String name) {
		return (AttributeDefI) this.attributeDefs.get(name);
	}

	public AttributeDefI getAttributeDef(String name) {
		AttributeDefI def = getSelfAttributeDef(name);
		if (def != null) {
			return def;
		}
		for (Iterator i = ContentTypeUtil.getReachableContentTypes(typeService, this).iterator(); i.hasNext();) {
			ContentType t = (ContentType) i.next();
			ContentTypeDefI typeDef = typeService.getContentTypeDefinition(t);
			if (typeDef != null) {
				def = typeDef.getSelfAttributeDef(name);
				if (def != null) {
					return def;
				}
			}
		}
		return null;
	}

	public Set getAttributeNames() {
		Set s = new HashSet();
		s.addAll(this.attributeDefs.keySet());
		for (Iterator i = ContentTypeUtil.getReachableContentTypes(typeService, this).iterator(); i.hasNext();) {
			ContentType t = (ContentType) i.next();
			ContentTypeDef typeDef = (ContentTypeDef) typeService.getContentTypeDefinition(t);
			s.addAll(typeDef.getInheritableAttributeNames());
		}
		return s;
	}

	public void addAttributeDef(AttributeDef attrDef) {
		attributeDefs.put(attrDef.getName(), attrDef);
	}

	private Set getInheritableAttributeNames() {
		Set s = new HashSet();
		for (Iterator i = attributeDefs.values().iterator(); i.hasNext();) {
			AttributeDefI def = (AttributeDefI) i.next();
			if (def.isInheritable()) {
				s.add(def.getName());
			}
		}
		return s;
	}

	public String toString() {
		return "ContentTypeDef[" + type + ", " + attributeDefs + "]";
	}

	public boolean isIdGenerated() {
		return idGenerated;
	}

	public void setIdGenerated(boolean idGenerated) {
		this.idGenerated = idGenerated;
	}

}