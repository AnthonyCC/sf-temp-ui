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
import com.freshdirect.cms.ui.model.NavigableRelationInfo;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contentService")
public interface ContentService extends RemoteService {

    List<TreeContentNodeModel> search(String searchTerm) throws ServerException;

    List<TreeContentNodeModel> getChildren(TreeContentNodeModel loadConfig) throws ServerException;

    GwtNodeData loadNodeData(String key) throws ServerException;

    GwtNodeData createNodeData(String type, String id) throws ServerException;

    String generateUniqueId(String type) throws ServerException;

    String getGoogleMapsApiKey() throws ServerException;
    
    /**
     * 
     * @param models
     * @return the changeset ID
     */
    GwtSaveResponse save(Collection<GwtContentNode> models) throws ServerException;

    GwtUser getUser() throws ServerException;

    ChangeSetQueryResponse getChangeSets(ChangeSetQuery query) throws ServerException;

    /**
     * Return a map of domains -> domainValues
     * 
     * @param domains
     * @return
     */
    Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains) throws ServerException;

    GwtPublishData getPublishData(ChangeSetQuery publishId) throws ServerException;
    
    List<GwtPublishData> getPublishHistory(PagingLoadConfig config) throws ServerException;

    String startPublish(String comment) throws ServerException;
    
    String getPreviewUrl( String contentKey ) throws ServerException;
    
    ProductConfigParams getProductConfigParams( String skuKey ) throws ServerException;
    
    
    NavigableRelationInfo getNavigableRelations(String contentType);
    
}
