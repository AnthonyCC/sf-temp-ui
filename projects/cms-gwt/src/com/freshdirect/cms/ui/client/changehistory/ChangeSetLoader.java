package com.freshdirect.cms.ui.client.changehistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.GwtChangeDetail;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtNodeChange;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangeSetLoader implements DataProxy<PagingLoadResult<? extends ModelData>> {

    ChangeSetQueryResponse response;

    int loadedPosition;

    String lastOrder;

    List<ContentNodeModel> alreadyLoaded = new ArrayList<ContentNodeModel>();

    public ChangeSetLoader(ChangeSetQueryResponse response) {
        this.response = response;
        this.loadedPosition = 0;
        this.lastOrder = response.getQuery() != null ? response.getQuery().getSortType() : null;
        convertToRows(response);
    }

    @Override
    public void load(DataReader<PagingLoadResult<? extends ModelData>> reader, Object loadConfig, final AsyncCallback<PagingLoadResult<? extends ModelData>> callback) {
        final PagingLoadConfig config = (PagingLoadConfig) loadConfig;
        String newSortOrder = config.getSortDir().name() + '-' + config.getSortField();
        if (lastOrder != null) {
            if (!lastOrder.equals(newSortOrder)) {
                alreadyLoaded.clear();
                loadedPosition = 0;
            }
        } else {
            lastOrder = newSortOrder;
        }

        if (config.getOffset() + config.getLimit() > alreadyLoaded.size()) {
            // we have to load other objects
            if ((response.getQuery() != null) && (loadedPosition < response.getChangeCount())) {
                response.getQuery().setRange(loadedPosition, config.getLimit());
                response.getQuery().setSortType(config.getSortField());
                response.getQuery().setDirection(config.getSortDir());
                CmsGwt.getContentService().getChangeSets(response.getQuery(), new BaseCallback<ChangeSetQueryResponse>() {

                    @Override
                    public void errorOccured(Throwable error) {
                        callback.onFailure(error);
                    }

                    @Override
                    public void onSuccess(ChangeSetQueryResponse newResponse) {
                        ChangeSetLoader.this.lastOrder = newResponse.getQuery().getDirection().name() + '-' + newResponse.getQuery().getSortType();
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

        Map<String, String> catalogChangesAndValues = new HashMap<String, String>();

        for (GwtChangeSet changeSet : response.getChanges()) {
            for (GwtNodeChange nodeChange : changeSet.getNodeChanges()) {
                if (nodeChange.getChangeDetails().size() == 0) {

                    ContentNodeModel b = new ContentNodeModel(nodeChange.getType(), nodeChange.getLabel(), nodeChange.getKey());

                    b.set("changeType", nodeChange.getChangeType());

                    b.set("user", changeSet.getUserId());
                    b.set("note", changeSet.getNote());
                    b.set("date", changeSet.getModifiedDate());

                    b.set("previewLink", nodeChange.getPreviewLink());

                    alreadyLoaded.add(b);

                } else {
                    for (GwtChangeDetail detail : nodeChange.getChangeDetails()) {

                        ContentNodeModel b = new ContentNodeModel(nodeChange.getType(), nodeChange.getLabel(), nodeChange.getKey());

                        if (detail.getOldValue() != null) {
                            b.set("old", detail.getOldValue());
                        }
                        b.set("new", detail.getNewValue());
                        b.set("attribute", detail.getAttributeName());

                        if (nodeChange.getChangeType() !=null){
                            b.set("changeType", nodeChange.getChangeType());
                        }

                        b.set("user", changeSet.getUserId());
                        b.set("note", changeSet.getNote());
                        b.set("date", changeSet.getModifiedDate());

                        b.set("previewLink", nodeChange.getPreviewLink());

                        if ("catalog".equals(detail.getAttributeName())) {
                            catalogChangesAndValues.put(nodeChange.getKey(), detail.getNewValue());
                        }

                        alreadyLoaded.add(b);
                    }
                }
            }
        }

        for (ContentNodeModel contentNodeModel : alreadyLoaded) {
            if (catalogChangesAndValues.containsKey(contentNodeModel.getKey())) {
                if ("All [ALL]".equals(catalogChangesAndValues.get(contentNodeModel.getKey()))) {
                    contentNodeModel.setIconOverride("OverrideRed");
                } else if ("Corporate [CORPORATE]".equals(catalogChangesAndValues.get(contentNodeModel.getKey()))) {
                    contentNodeModel.setIconOverride("OverrideGreen");
                }
            }
        }

        loadedPosition += response.getChanges().size();
    }

}
