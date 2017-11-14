package com.freshdirect.cms.ui.editor.publish.feed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishEntity;

public interface FeedPublishEntityRepository extends CrudRepository<FeedPublishEntity, Long> {

    @Query("Select distinct feedPublish from FeedPublishEntity feedPublish left JOIN FETCH feedPublish.messages")
    @Override
    List<FeedPublishEntity> findAll();

    @Query("Select feedPublish from FeedPublishEntity feedPublish left JOIN FETCH feedPublish.messages where feedPublish.id = ?1")
    FeedPublishEntity findById(Long id);

    @Query("Select feedPublish from FeedPublishEntity feedPublish left JOIN FETCH feedPublish.messages where feedPublish.creationDate = (select max(feedPublish.creationDate) from FeedPublishEntity feedPublish)")
    FeedPublishEntity findFirstByOrderByCreationDateDesc();

    @Query("Select distinct feedPublish from FeedPublishEntity feedPublish ORDER BY feedPublish.creationDate desc")
    List<FeedPublishEntity> findAllByOrderByCreationDateDesc();
}
