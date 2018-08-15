package com.freshdirect.cms.relationship.comparator;

import java.util.Comparator;

import com.freshdirect.cms.persistence.entity.RelationshipEntity;

public class RelationshipEntityComparator implements Comparator<RelationshipEntity> {

    @Override
    public int compare(RelationshipEntity o1, RelationshipEntity o2) {
        String parentNode1 = o1.getRelationshipSource();
        String parentNode2 = o2.getRelationshipSource();

        int parentCompare = parentNode1.compareTo(parentNode2);

        if (parentCompare != 0) {
            return parentCompare;
        }

        String relationshipType1 = o1.getRelationshipDestinationType();
        String relationshipType2 = o2.getRelationshipDestinationType();

        int relationshipTypeCompare = relationshipType1.compareTo(relationshipType2);

        if (relationshipTypeCompare != 0) {
            return relationshipTypeCompare;
        }

        int ordinal1 = o1.getOrdinal();
        int ordinal2 = o2.getOrdinal();

        int ordinalCompare = ordinal1 - ordinal2;

        return ordinalCompare;

    }
}
