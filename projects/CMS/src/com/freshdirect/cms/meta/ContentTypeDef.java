package com.freshdirect.cms.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Basic implementation of {@link com.freshdirect.cms.ContentTypeDefI}.
 * 
 * @see com.freshdirect.cms.meta.AttributeDef
 * @see com.freshdirect.cms.meta.RelationshipDef
 */
public class ContentTypeDef implements ContentTypeDefI {

    final static Logger LOG = LoggerFactory.getInstance(ContentTypeDef.class);
    
	private final ContentTypeServiceI typeService;

	private final ContentType type;

	private final String label;
	private final Map<String, AttributeDefI> attributeDefs = new HashMap<String, AttributeDefI>();

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

	public Collection<AttributeDefI> getSelfAttributeDefs() {
		return Collections.unmodifiableCollection(this.attributeDefs.values());
	}

	public AttributeDefI getSelfAttributeDef(String name) {
		return this.attributeDefs.get(name);
	}
	
	public AttributeDefI removeSelfAttributeDef(String name) {
	    return this.attributeDefs.remove(name);
	}

        /**
         * Get the definition of a specific attribute, with
         * contextual acquisition (inheritance) applied. 
         * 
         * @param name attribute name
         * @return the attribute definition, or null if no such attribute is defined
         */
        public AttributeDefI getAttributeDef(String name) {
            AttributeDefI def = getSelfAttributeDef(name);
            if (def != null) {
                return def;
            }
            for (ContentType t : ContentTypeUtil.getReachableContentTypes(typeService, this)) {
                ContentTypeDefI typeDef = typeService.getContentTypeDefinition(t);
                if (typeDef != null) {
                    def = typeDef.getSelfAttributeDef(name);
                    if (def != null && def.isInheritable()) {
                        return def;
                    }
                }
            }
            return null;
        }	
	
	public Set<String> getAttributeNames() {
		Set<String> s = new HashSet<String>();
		s.addAll(this.attributeDefs.keySet());
                for (ContentType t : ContentTypeUtil.getReachableContentTypes(typeService, this)) {
                    ContentTypeDef typeDef = (ContentTypeDef) typeService.getContentTypeDefinition(t);
                    s.addAll(typeDef.getInheritableAttributeNames());
                }
		return s;
	}

	public void addAttributeDef(AttributeDefI attrDef) {
		attributeDefs.put(attrDef.getName(), attrDef);
	}

        private Set<String> getInheritableAttributeNames() {
            Set<String> s = new HashSet<String>();
            for (AttributeDefI def : attributeDefs.values()) {
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