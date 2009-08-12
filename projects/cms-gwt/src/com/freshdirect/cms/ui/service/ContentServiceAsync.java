package com.freshdirect.cms.ui.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtPublishData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContentServiceAsync {

	void getChildren( ContentNodeModel loadConfig, AsyncCallback<List<ContentNodeModel>> callback );

	void search( String searchTerm, AsyncCallback<List<ContentNodeModel>> callback );

	void getNodeData( String key, AsyncCallback<GwtNodeData> callback );
	
	void getEditChildren( BulkEditModel loadConfig, AsyncCallback<List<BulkEditModel>> callback );

	void save( Collection<GwtContentNode> models, AsyncCallback<GwtSaveResponse> callback );

	void getUser( AsyncCallback<GwtUser> user );
	
    void generateUniqueId(String type, AsyncCallback<String> callback);
    
    void createNodeData(String type, String id, AsyncCallback<GwtNodeData> callback);
    
    void getChangeSets(ChangeSetQuery query, AsyncCallback<ChangeSetQueryResponse> callback);
    
    void getDomainValues(List<ContentNodeModel> domains, AsyncCallback<Map<String, List<ContentNodeModel>>> callback);

    void getPublishHistory(AsyncCallback<List<GwtPublishData>> callback);
    
    void startPublish(String comment, AsyncCallback<String> callback);
    
}
