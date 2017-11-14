package com.freshdirect.cms.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.persistence.entity.RelationshipEntity;

public interface RelationshipEntityRepository extends CrudRepository<RelationshipEntity, String> {

    @Override
    List<RelationshipEntity> findAll();

    List<RelationshipEntity> findByRelationshipSource(String relationshipSource);

    List<RelationshipEntity> findByRelationshipSourceIn(List<String> relationshipSources);

    List<RelationshipEntity> findByRelationshipDestination(String relationshipDestination);

    List<RelationshipEntity> findByRelationshipSourceAndRelationshipNameIn(String relationshipSource, List<String> relationshipNames);
}
