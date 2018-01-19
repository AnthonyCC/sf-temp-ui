package com.freshdirect.cmsadmin.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;

public interface DraftChangeRepository extends CrudRepository<DraftChange, Long> {

    @Override
    List<DraftChange> findAll();

    List<DraftChange> deleteByDraft(Draft draft);

    /** returns the whole draft history */
    List<DraftChange> findByDraft(Draft draft);
    
    @Query(value = "select id, max_t as createdat, contentkey, attributename, value, draft_id, username " 
            + "from (select id, draft_id, username, contentkey, attributename, value, "
            + "createdat as t, max(createdat) over (partition by draft_id, contentkey, attributename) as max_t "
            + "from cms.draftchange) " 
            + "where t = max_t and draft_id = :draftId", nativeQuery = true)
    List<DraftChange> findByDraftId(@Param("draftId") long draftId);
}
