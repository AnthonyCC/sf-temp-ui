package com.freshdirect.cms.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.persistence.entity.AttributeEntity;

public interface AttributeEntityRepository extends CrudRepository<AttributeEntity, String> {

    @Override
    List<AttributeEntity> findAll();

    List<AttributeEntity> findByContentKey(String contentKey);

    AttributeEntity findByContentKeyAndName(String contentKey, String name);

    List<AttributeEntity> findByContentKeyAndNameIn(String contentKey, List<String> names);

    List<AttributeEntity> findByContentKeyIn(List<String> contentKeys);

    void deleteByContentKeyAndNameIn(String contentKey, List<String> names);

    void deleteByContentKeyAndName(String contentKey, String name);
}
