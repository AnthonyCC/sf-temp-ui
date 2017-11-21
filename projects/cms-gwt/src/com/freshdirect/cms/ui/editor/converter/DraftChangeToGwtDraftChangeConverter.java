package com.freshdirect.cms.ui.editor.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.ui.model.draft.GwtDraftChange;

public class DraftChangeToGwtDraftChangeConverter {

    private static final DraftChangeToGwtDraftChangeConverter INSTANCE = new DraftChangeToGwtDraftChangeConverter();

    public static DraftChangeToGwtDraftChangeConverter getInstance() {
        return INSTANCE;
    }

    private DraftChangeToGwtDraftChangeConverter() {
    }

    public GwtDraftChange convert(DraftChange draftChange) {
        GwtDraftChange gwtDraftChange = new GwtDraftChange();
        gwtDraftChange.setAttributeName(draftChange.getAttributeName());
        gwtDraftChange.setChangedValue(draftChange.getValue());
        gwtDraftChange.setContentKey(draftChange.getContentKey());
        gwtDraftChange.setDraftId(draftChange.getDraft().getId());
        gwtDraftChange.setCreatedAt(new Date(draftChange.getCreatedAt()));
        gwtDraftChange.setUserName(draftChange.getUserName());

        return gwtDraftChange;
    }

    public List<GwtDraftChange> convert(List<DraftChange> draftChanges) {
        List<GwtDraftChange> gwtDraftChanges = new ArrayList<GwtDraftChange>();
        for (DraftChange draftChange : draftChanges) {
            gwtDraftChanges.add(convert(draftChange));
        }
        return gwtDraftChanges;
    }
}
