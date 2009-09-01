package com.freshdirect.cms.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.GwtChangeDetail;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtContentNodeChange;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangeSetLoader implements DataProxy<PagingLoadResult<? extends ModelData>> {

    ChangeSetQueryResponse response;

    int loadedPosition;

    List<ContentNodeModel> alreadyLoaded = new ArrayList<ContentNodeModel>();

    public ChangeSetLoader(ChangeSetQueryResponse response) {
        this.response = response;
        this.loadedPosition = 0;
        convertToRows(response);
    }

    @Override
    public void load(DataReader<PagingLoadResult<? extends ModelData>> reader, Object loadConfig,
            final AsyncCallback<PagingLoadResult<? extends ModelData>> callback) {
        final PagingLoadConfig config = (PagingLoadConfig) loadConfig;

        if (config.getOffset() + config.getLimit() > alreadyLoaded.size()) {
            // we have to load other objects
            if ((response.getQuery() != null) && (loadedPosition < response.getChangeCount())) {
                response.getQuery().setRange(loadedPosition, config.getLimit());
                CmsGwt.getContentService().getChangeSets(response.getQuery(), new BaseCallback<ChangeSetQueryResponse>() {
                    @Override
                    public void errorOccured(Throwable error) {
                        callback.onFailure(error);
                    }

                    @Override
                    public void onSuccess(ChangeSetQueryResponse newResponse) {
                        convertToRows(newResponse);
                        deliverResponse(callback, config);
                    }
                });
                return;
            }
        }
        deliverResponse(callback, config);

    }

    private void deliverResponse(final AsyncCallback<PagingLoadResult<? extends ModelData>> callback, PagingLoadConfig config) {
        int fromIndex = config.getOffset();
        int toIndex = Math.min(config.getLimit() + config.getOffset(), alreadyLoaded.size());

        // no List.subList in gwt :(
        List<BaseModelData> result = new ArrayList<BaseModelData>(config.getLimit());
        for (int i = fromIndex; i < toIndex; i++) {
            result.add(alreadyLoaded.get(i));
        }
        BasePagingLoadResult<BaseModelData> plr = new BasePagingLoadResult<BaseModelData>(result, fromIndex, response.getChangeCount());
        callback.onSuccess(plr);
    }

    void convertToRows(ChangeSetQueryResponse response) {

        for (GwtChangeSet changeSet : response.getChanges()) {
            for (GwtContentNodeChange nodeChange : changeSet.getNodeChanges()) {
                if (nodeChange.getChangeDetails().size() == 0) {
                    ContentNodeModel b = new ContentNodeModel(nodeChange.getContentType(), nodeChange.getLabel(), nodeChange.getContentKey() );

                    b.set("changeType", nodeChange.getChangeType());

                    b.set("user", changeSet.getUserId());
                    b.set("note", changeSet.getNote());
                    b.set("date", changeSet.getModifiedDate());
                    alreadyLoaded.add(b);

                } else {
                    for (GwtChangeDetail detail : nodeChange.getChangeDetails()) {
                        ContentNodeModel b = new ContentNodeModel(nodeChange.getContentType(), nodeChange.getLabel(), nodeChange.getContentKey());
                        b.set("old", detail.getOldValue());
                        b.set("new", detail.getNewValue());
                        b.set("attribute", detail.getAttributeName());

                        b.set("changeType", nodeChange.getChangeType());

                        b.set("user", changeSet.getUserId());
                        b.set("note", changeSet.getNote());
                        b.set("date", changeSet.getModifiedDate());

                        alreadyLoaded.add(b);
                    }
                }
            }
        }
        loadedPosition += response.getChanges().size();
    }

}
