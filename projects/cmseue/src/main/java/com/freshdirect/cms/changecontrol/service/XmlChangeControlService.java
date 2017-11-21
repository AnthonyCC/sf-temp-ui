package com.freshdirect.cms.changecontrol.service;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.ContentKey;
import com.google.common.base.Optional;

@Profile({"xml", "test"})
@Service
public class XmlChangeControlService implements ContentChangeControlService {

    @Override
    public Optional<ContentChangeSetEntity> fetchChangeSet(int id) {
        return Optional.absent();
    }

    @Override
    public Set<ContentChangeSetEntity> getHistory(ContentKey key) {
        return Collections.emptySet();
    }

    @Override
    public ContentChangeSetEntity save(ContentChangeSetEntity changeSet) {
        return null;
    }

    @Override
    public Set<ContentChangeSetEntity> queryChangeSetEntities(ContentKey contentKey, String author, Date startDate, Date endDate) {
        return Collections.emptySet();
    }
}
