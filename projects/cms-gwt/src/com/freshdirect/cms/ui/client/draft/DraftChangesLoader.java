package com.freshdirect.cms.ui.client.draft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadConfig;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.MainLayout;
import com.freshdirect.cms.ui.model.draft.GwtDraftChange;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DraftChangesLoader implements DataProxy<ListLoadResult<? extends ModelData>> {

    private List<ModelData> alreadyLoadedNodes = new ArrayList<ModelData>();
    private LoaderMode loaderMode = LoaderMode.ALL_CHANGES;

    @Override
    public void load(DataReader<ListLoadResult<? extends ModelData>> reader, Object loadConfig, final AsyncCallback<ListLoadResult<? extends ModelData>> callback) {
        final ListLoadConfig config = (ListLoadConfig) loadConfig;

        alreadyLoadedNodes.clear();

        switch (loaderMode) {
            case ALL_CHANGES:
                initiateLoadAllChanges(callback, config);
                break;
            case VALIDATION:
                initiateValidateDraft(callback, config);
                break;
            case MERGE:
                initiateMergeDraft(callback, config);
                break;
            default:
                break;
        }

    }

    private void initiateLoadAllChanges(final AsyncCallback<ListLoadResult<? extends ModelData>> callback, final ListLoadConfig config) {
        // initiate async fetch
        CmsGwt.getDraftService().loadDraftChanges(new BaseCallback<List<GwtDraftChange>>() {

            @Override
            public void onSuccess(List<GwtDraftChange> result) {
                CmsGwt.consoleLog("Loaded " + result.size() + " draft changes");
                MainLayout.getInstance().stopProgress();

                // transform gwt pojos to GXT store models
                for (GwtDraftChange cs : result) {
                    alreadyLoadedNodes.add(cs.toModelData());
                }

                Collections.reverse(alreadyLoadedNodes);
                deliverResponse(callback, config);
            }

            @Override
            public void errorOccured(Throwable error) {
                MainLayout.getInstance().stopProgress();
                callback.onFailure(error);
            }

        });
    }

    private void initiateValidateDraft(final AsyncCallback<ListLoadResult<? extends ModelData>> callback, final ListLoadConfig config) {
        CmsGwt.getDraftService().validateDraft(new BaseCallback<List<GwtDraftChange>>() {

            @Override
            public void onSuccess(List<GwtDraftChange> result) {
                CmsGwt.consoleLog("Loaded " + result.size() + " draft changes");
                MainLayout.getInstance().stopProgress();

                int failedChanges = 0;

                // transform gwt pojos to GXT store models
                for (GwtDraftChange cs : result) {
                    // count validation failures
                    failedChanges += cs.getValidationError() != null ? 1 : 0;
                    alreadyLoadedNodes.add(cs.toModelData());
                }

                // display some nice message
                if (failedChanges == 0) {
                    MessageBox.alert("Draft Verification Succeeded", "Hooray", null);
                } else {
                    final String draftNaming = failedChanges > 1 ? "drafts are" : "draft is";
                    MessageBox.alert("Draft Verification Failed", Integer.toString(failedChanges) + " " + draftNaming + " invalid", null);
                }

                deliverResponse(callback, config);
            };

            @Override
            public void errorOccured(Throwable error) {
                MainLayout.getInstance().stopProgress();

                callback.onFailure(error);
            }
        });
    }

    private void initiateMergeDraft(final AsyncCallback<ListLoadResult<? extends ModelData>> callback, final ListLoadConfig config) {
        CmsGwt.getDraftService().mergeDraft(new BaseCallback<List<GwtDraftChange>>() {

            @Override
            public void onSuccess(List<GwtDraftChange> result) {
                CmsGwt.consoleLog("Loaded " + result.size() + " draft changes");
                MainLayout.getInstance().stopProgress();

                int failedChanges = 0;

                // transform gwt pojos to GXT store models
                for (GwtDraftChange cs : result) {
                    // count validation failures
                    failedChanges += cs.getValidationError() != null ? 1 : 0;
                    alreadyLoadedNodes.add(cs.toModelData());
                }

                // display some nice message
                if (result.isEmpty() || failedChanges == 0) {
                    MessageBox.alert("Draft Merge Succeeded", "Your browser will be forced to reload GWT and return working on mainline CMS content",
                            new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(MessageBoxEvent be) {
                                    Window.Location.replace("/cms-gwt/logout.jsp");
                                }
                            });
                } else {
                    MessageBox.alert("Draft Merge Failed", Integer.toString(failedChanges) + " drafts are invalid", null);
                }

                deliverResponse(callback, config);
            };

            @Override
            public void errorOccured(Throwable error) {
                MainLayout.getInstance().stopProgress();

                callback.onFailure(error);
            }
        });
    }

    public void setLoaderMode(LoaderMode loaderMode) {
        CmsGwt.consoleDebug("Changing loader mode to " + loaderMode);
        this.loaderMode = loaderMode;
    }

    private void deliverResponse(final AsyncCallback<ListLoadResult<? extends ModelData>> callback, ListLoadConfig config) {
        callback.onSuccess(new BaseListLoadResult<ModelData>(alreadyLoadedNodes));
    }

}
