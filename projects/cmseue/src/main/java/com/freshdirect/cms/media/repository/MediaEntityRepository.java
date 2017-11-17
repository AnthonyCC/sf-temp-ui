package com.freshdirect.cms.media.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.media.entity.MediaEntity;

public interface MediaEntityRepository extends CrudRepository<MediaEntity, String> {

    @Override
    List<MediaEntity> findAll();

    MediaEntity findByUri(String uri);

    List<MediaEntity> findByType(String type);

    MediaEntity findByTypeAndId(String type, String id);

    List<MediaEntity> findByTypeInAndIdIn(List<String> tpye, List<String> id);

    List<MediaEntity> findByUriStartsWith(String uriPrefix);

    @Query(value = "select * from media m where m.uri like ?1 and instr(m.uri, '/', LENGTH(?1)) = 0 and m.uri != '/'", nativeQuery = true)
    List<MediaEntity> findByBasePath(String basePath);

    List<MediaEntity> findByLastModifiedGreaterThan(Date timestamp);
}
