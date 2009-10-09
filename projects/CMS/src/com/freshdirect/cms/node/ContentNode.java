package com.freshdirect.cms.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;

/**
 * Simple implementation of {@link com.freshdirect.cms.ContentNodeI}.
 * 
 * @see com.freshdirect.cms.node.Attribute
 * @see com.freshdirect.cms.node.Relationship
 */
public class ContentNode implements ContentNodeI {

	/** originating content service */
	private final ContentServiceI contentService;

	/** content key of the node */ 
	private final ContentKey key;

	/** Map of String (name) -> Object (value) */
	private final Map attributes = new HashMap();

	/**
	 * @param contentService the {@link ContentServiceI} originating this node (never null)
	 * @param key content key of this node (never null)
	 */
	public ContentNode(ContentServiceI contentService, ContentKey key) {
		if (key == null) {
			throw new IllegalArgumentException("ContentKey cannot be null");
		}
		this.key = key;
		this.contentService = contentService;
		this.initializeAttributes(contentService.getTypeService());
	}

	private void initializeAttributes(ContentTypeServiceI typeService) {
		ContentTypeDefI def = typeService.getContentTypeDefinition(key.getType());
		if (def == null) {
			throw new IllegalArgumentException("No type definition for " + key + " in " + typeService);
		}
		for (Iterator i = def.getAttributeNames().iterator(); i.hasNext();) {
			String atrName = (String) i.next();
			AttributeDefI atrDef = def.getAttributeDef(atrName);
			Attribute atr;
			if (atrDef instanceof RelationshipDefI) {
				atr = new Relationship(this, (RelationshipDefI) atrDef);
			} else {
				atr = new Attribute(this, atrDef);
			}
			attributes.put(atrName, atr);
		}
	}

	public ContentKey getKey() {
		return key;
	}

	public ContentTypeDefI getDefinition() {
		return contentService.getTypeService().getContentTypeDefinition(getKey().getType());
	}

	//
	// convenience
	//

	public Set getChildKeys() {
		return ContentNodeUtil.getChildKeys(this);
	}

	public String getLabel() {
		return ContentNodeUtil.getLabel(this);
	}

	//
	// attributes
	//

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
        
	public Map getAttributes() {
		return this.attributes;
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
        // do a serialization / de-serialization cycle as a trick
        // against explicit deep cloning

        // serialization
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ObjectOutputStream    oas  = null;
        try {
            oas = new ObjectOutputStream(baos);
            oas.writeObject(this);
            oas.close();
        } catch (IOException e) {
            return null;
        }

        // de-serialization
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream    oin = null;
        try {
            oin = new ObjectInputStream(bais);
            return (ContentNodeI) oin.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
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
		return "ContentNode[" + this.key + "]";
	}

}
