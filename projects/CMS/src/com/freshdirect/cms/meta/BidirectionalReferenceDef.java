package com.freshdirect.cms.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.BackReferenceDefI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;

public class BidirectionalReferenceDef extends RelationshipDef implements BidirectionalRelationshipDefI {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class ReverseSide implements BackReferenceDefI {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        ContentType sourceType;
        String name;
        String label;
        
        public ReverseSide(ContentType otherType, String name, String label) {
            this.sourceType = otherType;
            this.name = name;
            this.label = label;
        }
        
        @Override
        public EnumAttributeType getAttributeType() {
            return EnumAttributeType.RELATIONSHIP;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public boolean isInheritable() {
            return false;
        }
        

        @Override
        public ContentType getSourceType() {
            return ReverseSide.this.sourceType;
        }
        
        @Override
        public EnumCardinality getCardinality() {
            return EnumCardinality.ONE;
        }

        @Override
        public boolean isCardinalityOne() {
            return getCardinality() == EnumCardinality.ONE;
        }

        @Override
        public Object getEmptyValue() {
            return null;
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        public Set<ContentType> getContentTypes() {
            return Collections.singleton(BidirectionalReferenceDef.this.getSourceType());
        }

        @Override
        public boolean isNavigable() {
            return false;
        }
        
        @Override
        public boolean isCalculated() {
            return true;
        }
        
        @Override
        public RelationshipDefI getMainRelationship() {
            return BidirectionalReferenceDef.this;
        }
    }
    
    List<RelationshipDefI> otherSide;

    public BidirectionalReferenceDef (RelationshipDefI rel) {
        this(rel.getSourceType(), rel.getName(), rel.getLabel(), rel.isReadOnly());
    }
    
    public BidirectionalReferenceDef (ContentType type, String name, String label, boolean readOnly) {
        super(type, name, label, false, false, false, readOnly, EnumCardinality.ONE);
        this.otherSide = new ArrayList<RelationshipDefI> ();
    }
    
    public void addOtherSide(ContentType otherType, String otherName, String otherLabel) {
        otherSide.add(new ReverseSide(otherType, otherName, otherLabel));
    }
    
    @Override
    public Collection<RelationshipDefI> getOtherSide() {
        return otherSide;
    }
    
    @Override
    public Set<ContentType> getContentTypes() {
        Set<ContentType> types = new HashSet<ContentType>();
        for (RelationshipDefI r : otherSide) {
            types.add(r.getSourceType());
        }
        return types;
    }

    public RelationshipDefI getOtherSide(ContentType type) {
        for (RelationshipDefI r : otherSide) {
            if (r.getSourceType().equals(type)) {
                return r;
            }
        }
        return null;
    }
}
