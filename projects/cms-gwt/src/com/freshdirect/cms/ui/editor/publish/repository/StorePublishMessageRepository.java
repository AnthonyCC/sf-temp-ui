package com.freshdirect.cms.ui.editor.publish.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessageId;

public interface StorePublishMessageRepository extends CrudRepository<StorePublishMessage, StorePublishMessageId> {

    @Query("from StorePublishMessage where publishId = ?1 and sortOrder = (select max(sortOrder) from StorePublishMessage where publishId = ?1)")
    StorePublishMessage findLastMessageForPublish(Long publishId);

}
