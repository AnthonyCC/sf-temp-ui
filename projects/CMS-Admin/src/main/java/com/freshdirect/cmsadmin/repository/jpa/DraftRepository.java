package com.freshdirect.cmsadmin.repository.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftStatus;

public interface DraftRepository extends CrudRepository<Draft, Long> {

    @Override
    List<Draft> findAll();

    List<Draft> findByDraftStatusNotIn(List<DraftStatus> statuses);
}
