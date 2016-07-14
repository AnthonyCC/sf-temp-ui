package com.freshdirect.cms.ui.service;

import java.util.List;

import com.freshdirect.cms.ui.model.draft.GwtDraftChange;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtDraftServiceAsync {

    void loadDraftChanges(AsyncCallback<List<GwtDraftChange>> callback);

    void validateDraft(AsyncCallback<List<GwtDraftChange>> callback);

    void mergeDraft(AsyncCallback<List<GwtDraftChange>> callback);
}
