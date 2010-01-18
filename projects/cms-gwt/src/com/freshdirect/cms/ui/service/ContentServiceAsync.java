package com.freshdirect.cms.ui.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContentServiceAsync {

	void getChildren( TreeContentNodeModel loadConfig, AsyncCallback<List<TreeContentNodeModel>> callback );

	void search( String searchTerm, AsyncCallback<List<TreeContentNodeModel>> callback );

	void loadNodeData( String key, String context, AsyncCallback<GwtNodeData> callback );
	
	void save( Collection<GwtContentNode> models, AsyncCallback<GwtSaveResponse> callback );

	void getUser( AsyncCallback<GwtUser> user );
	
    void generateUniqueId(String type, AsyncCallback<String> callback);
    
    void createNodeData(String type, String id, AsyncCallback<GwtNodeData> callback);
    
    void getChangeSets(ChangeSetQuery query, AsyncCallback<ChangeSetQueryResponse> callback);
    
    void getDomainValues(List<ContentNodeModel> domains, AsyncCallback<Map<String, List<ContentNodeModel>>> callback);

    void getPublishHistory(PagingLoadConfig config, AsyncCallback<List<GwtPublishData>> callback);
    
    void startPublish(String comment, AsyncCallback<String> callback);
    
    void getPreviewUrl( String contentKey, AsyncCallback<String> callback );
    
    void getProductConfigParams( String skuKey, AsyncCallback<ProductConfigParams> callback ); 

	void getPublishData(ChangeSetQuery publishId, AsyncCallback<GwtPublishData> callback);
}
