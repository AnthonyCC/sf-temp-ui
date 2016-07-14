package com.freshdirect.cms.ui.service;

import java.util.List;

import com.freshdirect.cms.ui.model.draft.GwtDraftChange;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath( "draftService" )
public interface GwtDraftService extends RemoteService {
    List<GwtDraftChange> loadDraftChanges() throws ServerException;
    List<GwtDraftChange> validateDraft() throws ServerException;
    List<GwtDraftChange> mergeDraft() throws ServerException;
}
