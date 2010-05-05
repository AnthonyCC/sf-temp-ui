package com.freshdirect.cms.application.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.meta.ContentTypeDef;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;

public abstract class AbstractTypeService implements ContentTypeServiceI {

    /** Map of {@link ContentType} -> {@link ContentTypeDefI} */
    private Map<ContentType,ContentTypeDefI> defsByType;

    private Map<ContentType, Map<String, BidirectionalReferenceHandler>> referenceHandlers = new HashMap<ContentType, Map<String, BidirectionalReferenceHandler>>();


    protected void setContentTypes(Map<ContentType, ContentTypeDefI> types) {
        this.defsByType = types;
        referenceHandlers.clear();
        for (Map.Entry<ContentType, ContentTypeDefI> e : defsByType.entrySet()) {
            for (AttributeDefI a : e.getValue().getSelfAttributeDefs()) {
                if (a instanceof BidirectionalRelationshipDefI) {
                    BidirectionalRelationshipDefI b = (BidirectionalRelationshipDefI) a;
                    if (b.isWritableSide()) {
                        ContentTypeDef targetDef = (ContentTypeDef) types.get(b.getOtherSide().getType());
                        if (targetDef == null) {
                            throw new CmsRuntimeException("Unknown destination type : " + b.getOtherSide().getType() + " for " + b.getType() + "."
                                    + b.getName());
                        }
                        if (targetDef.getSelfAttributeDef(b.getOtherSide().getName()) != null) {
                            throw new CmsRuntimeException("Attribute " + b.getOtherSide().getName() + " is already defined for " + b.getOtherSide().getType()
                                    + ", so creating back reference for " + b.getType() + '.' + b.getName() + " is not possible!");
                        }
                        targetDef.addAttributeDef(b.getOtherSide());
                        
                        createReferenceHandler(e.getKey(), a.getName(), b);
                    }
                }
            }
        }
        
    }
    
    @Override
    public final Set<ContentType> getContentTypes() {
        return Collections.unmodifiableSet(defsByType.keySet());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.freshdirect.cms.application.ContentTypeServiceI#getContentTypeDefinitions
     * ()
     */
    @Override
    public final Set<ContentTypeDefI> getContentTypeDefinitions() {
        return new HashSet<ContentTypeDefI>(defsByType.values());
    }

    @Override
    public final ContentTypeDefI getContentTypeDefinition(ContentType type) {
        return (ContentTypeDefI) defsByType.get(type);
    }

    protected void createReferenceHandler(ContentType type, String attribute, BidirectionalRelationshipDefI rel) {
        Map<String, BidirectionalReferenceHandler> map = referenceHandlers.get(type);
        if (map == null) {
            map = new HashMap<String, BidirectionalReferenceHandler>();
            referenceHandlers.put(type, map);
        }
        map.put(attribute, new BidirectionalReferenceHandler(rel));
    }
    
    @Override
    public BidirectionalReferenceHandler getReferenceHandler(ContentType type, String attribute) {
        Map<String, BidirectionalReferenceHandler> map = referenceHandlers.get(type);
        if (map != null) {
            return map.get(attribute);
        }
        return null;
    }

    @Override
    public String generateUniqueId(ContentType type) {
        ContentTypeDefI def = getContentTypeDefinition(type);
        if (def == null || !def.isIdGenerated()) {
            return null;
        }

        // generate a UUID
        UUIDGenerator idGenerator = UUIDGenerator.getInstance();
        UUID id = idGenerator.generateRandomBasedUUID();

        return id.toString();
    }

    @Override
    public ContentKey generateUniqueContentKey(ContentType type) throws UnsupportedOperationException {
        String id = generateUniqueId(type);

        return id == null ? null : new ContentKey(type, id);
    }

    @Override
    public Collection<BidirectionalReferenceHandler> getAllReferenceHandler() {
        Set<BidirectionalReferenceHandler> handlers = new HashSet<BidirectionalReferenceHandler>();
        for (Map<String,BidirectionalReferenceHandler> map : referenceHandlers.values()) {
            for (BidirectionalReferenceHandler handler : map.values()) {
                handlers.add(handler);
            }
        }
        return handlers;
    }
}
