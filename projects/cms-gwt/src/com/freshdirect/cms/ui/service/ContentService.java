package com.freshdirect.cms.ui.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contentService")
public interface ContentService extends RemoteService {

    List<TreeContentNodeModel> search(String searchTerm) throws ServerException;

    List<TreeContentNodeModel> getChildren(TreeContentNodeModel loadConfig) throws ServerException;

    GwtNodeData getNodeData(String key) throws ServerException;

    GwtNodeData createNodeData(String type, String id) throws ServerException;

    List<BulkEditModel> getEditChildren(BulkEditModel loadConfig) throws ServerException;

    String generateUniqueId(String type) throws ServerException;

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

    List<GwtPublishData> getPublishHistory() throws ServerException;

    String startPublish(String comment) throws ServerException;
    
    String getPreviewUrl( String contentKey ) throws ServerException;
}
