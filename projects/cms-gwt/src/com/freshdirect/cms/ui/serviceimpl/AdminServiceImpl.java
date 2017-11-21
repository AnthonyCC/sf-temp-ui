package com.freshdirect.cms.ui.serviceimpl;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.ui.editor.domain.IndexingInfo;
import com.freshdirect.cms.ui.editor.domain.IndexingStatus;
import com.freshdirect.cms.ui.editor.service.IndexingService;
import com.freshdirect.cms.ui.editor.service.PublishService;
import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.service.AdminService;

public class AdminServiceImpl extends GwtServiceBase implements AdminService {

    private static final long serialVersionUID = 1263043539819341529L;

    private IndexingService indexingService = EditorServiceLocator.indexingService();
    private PublishService publishService = EditorServiceLocator.publishService();

    public AdminServiceImpl() {
        super();
    }

    @Override
    public AdminProcStatus getBuildIndexStatus() {
        return createProcStatusWithNameForIndexing("getBuildIndexStatus()");
    }

    @Override
    public AdminProcStatus rebuildIndexes() {
        CmsServiceLocator.draftContextHolder().setDraftContext(DraftContext.MAIN);
        indexingService.indexAll();
        return createProcStatusWithNameForIndexing("rebuildIndexes()");
    }

    @Override
    public AdminProcStatus abortStuckPublishFlows() {
        publishService.abortStuckPublishes();
        return new AdminProcStatus("abortStuckPublishFlows()", "Done", false, 0L, 0L);
    }

    private AdminProcStatus createProcStatusWithNameForIndexing(String name) {
        IndexingInfo lastIndexingInfo = indexingService.getIndexingStatus();
        AdminProcStatus status = null;
        if (lastIndexingInfo == null) {
            status = new AdminProcStatus("", "", false, 0L, 0L);
        } else {
            boolean isInProgress = lastIndexingInfo.getIndexingStatus().equals(IndexingStatus.IN_PROGRESS);
            status = new AdminProcStatus(name, lastIndexingInfo.getIndexingStatus().toString(), isInProgress, lastIndexingInfo.getIndexingStarted().getTime(),
                    System.currentTimeMillis() - lastIndexingInfo.getIndexingStarted().getTime());
        }
        return status;
    }

}
