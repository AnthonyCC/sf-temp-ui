package com.freshdirect.cms.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.persistence.entity.ContentNodeEntity;

public interface ContentNodeEntityRepository extends CrudRepository<ContentNodeEntity, String> {

    @Override
    List<ContentNodeEntity> findAll();

    ContentNodeEntity findByContentKey(String contentKey);

    List<ContentNodeEntity> findByContentKeyIn(List<String> contentKeyList);

    List<ContentNodeEntity> findByContentType(String contentType);

    List<ContentNodeEntity> findByContentTypeIn(List<String> contentTypes);

    @Query(value = "select * from contentnode c where c.id in (select parent_contentnode_id from cms_navtree where child_contentnode_id=?1)", nativeQuery = true)
    List<ContentNodeEntity> findParentsByContentKey(String contentKey);
}
