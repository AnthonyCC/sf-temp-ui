package com.freshdirect.cms.meta;

import java.util.Collections;
import java.util.Set;

import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumCardinality;

public class BidirectionalReferenceDef extends RelationshipDef implements BidirectionalRelationshipDefI {

    BidirectionalRelationshipDefI otherSide;
    boolean writable;
    final ContentType type;
    
    public BidirectionalReferenceDef (RelationshipDef original, ContentType otherType, String otherName, String otherLabel) {
        this(original.getSourceType(), original.getName(), original.getLabel(), original.isReadOnly(), true, otherType, otherName, otherLabel);
    }

    public BidirectionalReferenceDef (ContentType type, String name, String label, boolean readOnly, boolean writable, ContentType otherType, String otherName, String otherLabel) {
        super(type, name, label, false, false, false, readOnly, EnumCardinality.ONE);
        this.type = type;
        this.writable = writable;
        this.otherSide = new BidirectionalReferenceDef (otherType, otherName, otherLabel, this);
    }
    
    BidirectionalReferenceDef (ContentType type, String name, String otherLabel, BidirectionalReferenceDef otherSide) {
        super(type, name, otherLabel, false, false, false, true, EnumCardinality.ONE);
        this.otherSide = otherSide;
        this.writable = !otherSide.isWritableSide();
        this.type = type;
    }
    
    @Override
    public BidirectionalRelationshipDefI getOtherSide() {
        return otherSide;
    }
    
    @Override
    public boolean isWritableSide() {
        return writable;
    }
    
    @Override
    public ContentType getType() {
        return type;
    }
    
    @Override
    public Set<ContentType> getContentTypes() {
        return Collections.singleton(otherSide.getType());
    }

}
