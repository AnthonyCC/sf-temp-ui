package com.freshdirect.cms.ui.editor.publish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;

public interface StorePublishRepository extends CrudRepository<StorePublish, Long> {

    @Override
    @Query("from StorePublish publish left JOIN FETCH publish.messages where publish.id = ?1")
    StorePublish findOne(Long id);

    List<StorePublish> findAllByOrderByTimestampDesc();

    @Query("from StorePublish publish left JOIN FETCH publish.messages where publish.timestamp = (select max(publish.timestamp) from StorePublish publish where status = com.freshdirect.cms.ui.editor.publish.domain.StorePublishStatus.COMPLETE)")
    StorePublish getMostRecentPublish();

    @Query("from StorePublish where timestamp = (select max(publish.timestamp) from StorePublish publish)")
    StorePublish getLastPublish();
}
