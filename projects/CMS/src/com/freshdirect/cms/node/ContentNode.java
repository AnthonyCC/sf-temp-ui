package com.freshdirect.cms.node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.BackReferenceDefI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.reverse.BackReference;
import com.freshdirect.cms.reverse.BidirectionalReference;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;

/**
 * Simple implementation of {@link com.freshdirect.cms.ContentNodeI}.
 * 
 * @see com.freshdirect.cms.node.Attribute
 * @see com.freshdirect.cms.node.Relationship
 */
public class ContentNode implements ContentNodeI {

	private static final long	serialVersionUID	= -2807267115367900617L;

	/** originating content service */
	private final ContentServiceI contentService;
	
	/** */
	private final DraftContext draftContext;

	/** content key of the node */ 
	private final ContentKey key;

	/** Map of String (name) -> Object (value) */
	private final Map<String, AttributeI> attributes = new HashMap<String, AttributeI>();

	/**
	 * @param contentService the {@link ContentServiceI} originating this node (never null)
	 * @param key content key of this node (never null)
	 */
	public ContentNode(ContentServiceI contentService, DraftContext draftContext, ContentKey key) {
		if (key == null) {
			throw new IllegalArgumentException("ContentKey cannot be null");
		}
		this.key = key;
		this.contentService = contentService;
		this.draftContext = draftContext;
		this.initializeAttributes(contentService.getTypeService());
	}

    public ContentNode(ContentServiceI contentService, ContentKey key) {
        this(contentService, DraftContext.MAIN, key);
    }

    private void initializeAttributes(ContentTypeServiceI typeService) {
        if (!ContentKey.NULL_KEY.equals(key)) {
            ContentTypeDefI def = typeService.getContentTypeDefinition(key.getType());
            if (def == null) {
                throw new IllegalArgumentException("No type definition for " + key + " in " + typeService);
            }
            for (String atrName : def.getAttributeNames()) {
                AttributeDefI atrDef = def.getAttributeDef(atrName);
                AttributeI atr;
                if (atrDef instanceof RelationshipDefI) {
                    if (atrDef instanceof BidirectionalRelationshipDefI) {
                        BidirectionalRelationshipDefI br = (BidirectionalRelationshipDefI) atrDef;
                        BidirectionalReferenceHandler handler = typeService.getReferenceHandler(br.getSourceType(), br.getName());
                        atr = new BidirectionalReference(this, handler);
                    } else if (atrDef instanceof BackReferenceDefI) {
                        final RelationshipDefI mainRelationship = ((BackReferenceDefI) atrDef).getMainRelationship();
                        atr = new BackReference(this, typeService.getReferenceHandler(mainRelationship.getSourceType(), mainRelationship.getName()));
                    } else {
                        atr = new Relationship(this, (RelationshipDefI) atrDef);
                    }
                } else {
                    atr = new Attribute(this, atrDef);
                }
                attributes.put(atrName, atr);
            }
        }
    }

	@Override
    public ContentKey getKey() {
		return key;
	}

	@Override
    public ContentTypeDefI getDefinition() {
		return contentService.getTypeService().getContentTypeDefinition(getKey().getType());
	}

	//
	// convenience
	//

	@Override
    public Set<ContentKey> getChildKeys() {
		return ContentNodeUtil.getChildKeys(this);
	}

	@Override
    public String getLabel() {
		return ContentNodeUtil.getLabel(this, draftContext);
	}

	//
	// attributes
	//

	@Override
    public AttributeI getAttribute(String name) {
		return attributes.get(name);
	}
	
    @SuppressWarnings( "deprecation" )
	@Override
    public Object getAttributeValue(String name) {
        AttributeI a = getAttribute(name);
        return a != null ? a.getValue() : null;
    }

    @SuppressWarnings( "deprecation" )
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

	@Override
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

	@Override
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

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	@Override
	public String toString() {
		return "ContentNode[" + this.key + "]";
	}

}
