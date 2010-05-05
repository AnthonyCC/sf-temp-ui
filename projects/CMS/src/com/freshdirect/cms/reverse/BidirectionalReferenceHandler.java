package com.freshdirect.cms.reverse;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;

public class BidirectionalReferenceHandler {

    BidirectionalRelationshipDefI relation;
    
    final Map<ContentKey, ContentKey> references = new HashMap<ContentKey, ContentKey>();
    final Map<ContentKey, ContentKey> backReferences = new HashMap<ContentKey, ContentKey>();

    public BidirectionalReferenceHandler(BidirectionalRelationshipDefI relation) {
        this.relation = relation;
    }
    
    public synchronized void addRelation(ContentKey source, ContentKey destination) {
        ContentKey oldValue = references.get(source);
        if (ContentKey.equals(oldValue, destination)) {
            return;
        }
        if (oldValue != null) {
            backReferences.remove(oldValue);
        }
        if (destination != null) {
            ContentKey oldSource = backReferences.get(destination);
            if (oldSource != null) {
                references.remove(oldSource);
            }
            references.put(source, destination);
            backReferences.put(destination, source);
        } else {
            references.remove(source);
        }
    }

    public ContentKey getReference(ContentKey source) {
        return references.get(source);
    }

    public ContentKey getInverseReference(ContentKey source) {
        return backReferences.get(source);
    }
    
    
    public String getRelationName() {
        return relation.getName();
    }
    
    public ContentType getSourceType() {
        return relation.getType();
    }
    
    public ContentType getDestinationType() {
        return relation.getOtherSide().getType();
    }
    
    
    public BidirectionalRelationshipDefI getRelation() {
        return relation;
    }
    
    public BidirectionalRelationshipDefI getInverseRelation() {
        return relation.getOtherSide();
    }
    
    @Override
    public String toString() {
        return "BidirectionalReferenceHandler ["+relation + " -> " + relation.getOtherSide()+']';
    }
    
}
