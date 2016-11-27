package com.freshdirect.cmsadmin.business;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftStatus;
import com.freshdirect.cmsadmin.repository.jpa.DraftRepository;

@Service
public class DraftService {

    @Autowired
    private DraftRepository repository;

    public List<Draft> loadAllDrafts() {
        return repository.findAll();
    }

    public List<Draft> loadNotDroppedOrMergedDrafts() {
        return repository.findByDraftStatusNotIn(Arrays.asList(DraftStatus.DROPPED, DraftStatus.MERGED));
    }

    @Transactional
    public void createDraft(Draft draft) {
        repository.save(draft);
    }

    @Transactional
    public void updateDraftStatus(Draft draft, DraftStatus draftStatus) {
        draft.setDraftStatus(draftStatus);
        repository.save(draft);
    }

}
