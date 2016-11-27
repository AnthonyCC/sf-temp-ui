package com.freshdirect.cmsadmin.repository.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;

public interface DraftChangeRepository extends CrudRepository<DraftChange, Long> {

    @Override
    List<DraftChange> findAll();

    List<DraftChange> deleteByDraft(Draft draft);

    List<DraftChange> findByDraft(Draft draft);
}
