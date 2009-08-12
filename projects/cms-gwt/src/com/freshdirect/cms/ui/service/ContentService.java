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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath( "contentService" )
public interface ContentService extends RemoteService {

	List<ContentNodeModel> search( String searchTerm );

	List<ContentNodeModel> getChildren( ContentNodeModel loadConfig );

    GwtNodeData getNodeData( String key );

    GwtNodeData createNodeData(String type, String id);
    
	List<BulkEditModel> getEditChildren( BulkEditModel loadConfig );

	String generateUniqueId( String type );

	/**
	 * 
	 * @param models
	 * @return the changeset ID
	 */
	GwtSaveResponse save( Collection<GwtContentNode> models );

	GwtUser getUser();
	
	ChangeSetQueryResponse getChangeSets(ChangeSetQuery query);
	
	/**
	 * Return a map of domains -> domainValues
	 * 
	 * @param domains
	 * @return
	 */
	Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains);
	
	List<GwtPublishData> getPublishHistory();

    String startPublish(String comment);
}
