package com.freshdirect.cms.ui.client.publish;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PublishMessageLoader implements DataProxy<PagingLoadResult<GwtPublishMessage>> {

    ChangeSetQuery query;
    List<GwtPublishMessage> publishMessages;
    int publishMessageCount;

    String lastOrder;

    public PublishMessageLoader(ChangeSetQueryResponse response) {
        this.query = new ChangeSetQuery(response.getQuery());
        this.publishMessageCount = response.getPublishMessageCount();
        publishMessages = response.getPublishMessages() != null ? response.getPublishMessages() : new ArrayList<GwtPublishMessage>();
    }

    @Override
    public void load(DataReader<PagingLoadResult<GwtPublishMessage>> reader, Object loadConfig,
            final AsyncCallback<PagingLoadResult<GwtPublishMessage>> callback) {
        final PagingLoadConfig config = (PagingLoadConfig) loadConfig;

		query.setPublishMessageRange(config.getOffset(), config.getLimit());
		query.setPublishDirection(config.getSortDir());
		query.setPublishSortType(config.getSortField());

		CmsGwt.getContentService().getChangeSets(query,
				new BaseCallback<ChangeSetQueryResponse>() {
					@Override
					public void onSuccess(ChangeSetQueryResponse result) {
						BasePagingLoadResult<GwtPublishMessage> pbl = new BasePagingLoadResult<GwtPublishMessage>(
								result.getPublishMessages(), result.getQuery()
										.getPublishMessageStart(), result
										.getPublishMessageCount());
						callback.onSuccess(pbl);
					}

					@Override
					public void errorOccured(Throwable error) {
						callback.onFailure(error);
					}
				});

    }

}
