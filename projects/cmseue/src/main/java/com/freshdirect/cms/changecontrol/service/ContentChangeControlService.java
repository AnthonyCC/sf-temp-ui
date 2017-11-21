package com.freshdirect.cms.changecontrol.service;

import java.util.Date;
import java.util.Set;

import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.ContentKey;
import com.google.common.base.Optional;

public interface ContentChangeControlService {

    Optional<ContentChangeSetEntity> fetchChangeSet(int id);

    Set<ContentChangeSetEntity> getHistory(ContentKey key);

    ContentChangeSetEntity save(ContentChangeSetEntity changeSet);

    Set<ContentChangeSetEntity> queryChangeSetEntities(ContentKey contentKey, String author, Date startDate, Date endDate);
}
