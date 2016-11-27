package com.freshdirect.cmsadmin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.cmsadmin.repository.jpa.DraftChangeRepository;

@Service
public class DraftChangeService {

    @Autowired
    private DraftChangeRepository repository;

    public List<DraftChange> loadAllDraftChanges() {
        return repository.findAll();
    }

    public List<DraftChange> loadAllChangesByDraft(Draft draft) {
        return repository.findByDraft(draft);
    }

    @Transactional
    public void createDraftChange(List<DraftChange> draftChanges) {
        repository.save(draftChanges);
    }

    @Transactional
    public void deleteDraftChange(DraftChange draftChange) {
        repository.delete(draftChange);
    }

    @Transactional
    public void deleteDraftChangesByDraft(Draft draft) {
        repository.deleteByDraft(draft);
    }
}
