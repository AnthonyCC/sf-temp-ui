package com.freshdirect.cms.persistence.entity.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.persistence.entity.RelationshipEntity;

@Service
public class RelationshipToRelationshipEntityConverter {

    public RelationshipEntity convert(ContentKey source, Relationship relationship, ContentKey destination) {
        return convert(source, relationship, destination, 0);
    }

    public RelationshipEntity convert(ContentKey source, Relationship relationship, ContentKey destination, int ordinal) {
        RelationshipEntity relationshipEntity = new RelationshipEntity();
        relationshipEntity.setRelationshipSource(source.toString());
        relationshipEntity.setRelationshipDestination(destination.toString());
        relationshipEntity.setRelationshipName(relationship.getName());
        relationshipEntity.setRelationshipDestinationType(destination.type.toString());
        relationshipEntity.setOrdinal(ordinal);
        return relationshipEntity;
    }

    private RelationshipEntity createNullEntity(ContentKey source, Relationship relationship) {
        RelationshipEntity relationshipEntity = new RelationshipEntity();
        relationshipEntity.setRelationshipSource(source.toString());
        relationshipEntity.setRelationshipDestination(null);
        relationshipEntity.setRelationshipName(relationship.getName());
        relationshipEntity.setRelationshipDestinationType("Null");
        relationshipEntity.setOrdinal(0);
        return relationshipEntity;
    }

    public List<RelationshipEntity> convert(ContentKey source, Relationship relationship, List<ContentKey> destinations) {
        List<RelationshipEntity> entities = new ArrayList<RelationshipEntity>();
        int ordinal = 0;

        // [LP-226] cases
        if (destinations.isEmpty()) {
            RelationshipEntity nullRelationshipEntity = createNullEntity(source, relationship);

            entities.add(nullRelationshipEntity);
        } else {
            for (ContentKey destinationKey : destinations) {
                entities.add(convert(source, relationship, destinationKey, ordinal++));
            }
        }

        return entities;
    }
}
