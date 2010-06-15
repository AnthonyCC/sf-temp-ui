package com.freshdirect.cms.ui.serviceimpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeComparator;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.SearchRelevancyList;
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
import com.freshdirect.cms.ui.model.changeset.Comparators;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtNodeChange;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.GwtSecurityException;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.cms.ui.translator.TranslatorFromGwt;
import com.freshdirect.cms.ui.translator.TranslatorToGwt;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@RemoteServiceRelativePath("/contentService")
public class ContentServiceImpl extends RemoteServiceServlet implements ContentService {

    static final Logger LOG = LoggerFactory.getInstance(ContentServiceImpl.class);
    
    private static final long     serialVersionUID = 1893834571301941106L;

    private final static int      MAX_HITS         = 120;
    
    private static final String[] ROOTKEYS         = { "Store:FreshDirect", "MediaFolder:/", "CmsFolder:forms", "CmsQueryFolder:queries",
            "CmsQuery:orphans", "FDFolder:recipes", "FDFolder:ymals", "FDFolder:starterLists",
            "FDFolder:synonymList", SearchRelevancyList.SEARCH_RELEVANCY_KEY, SearchRelevancyList.WORD_STEMMING_EXCEPTION,"FDFolder:FAQ" };    

    @Override
    public void init() throws ServletException {
        super.init();
        CmsManager.getInstance();
    }
    
    
    public String getGoogleMapsApiKey() {
    	return FDStoreProperties.getGoogleMapsAPIKey();
    }

    
    
    // ====================== User ====================== 
    
    
    public GwtUser getUser() {
        HttpServletRequest request = getThreadLocalRequest();
        return getUserFromRequest(request);
    }

    /**
     * Return a GWT User instance from a request
     * 
     * @param request
     * @return
     */
    protected static GwtUser getUserFromRequest(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        
        // TODO : eliminate this check, user principal is null, if it's running in hosted mode OR it isn't authenticated.
        if (userPrincipal == null) {
            return new GwtUser("cms-teszt-user", true, true);
        } else {
            return new GwtUser(userPrincipal.getName(), request.isUserInRole("cms_editor"), request.isUserInRole("cms_admin"));
        }
    }
    
    /**
     * Return a GWT User instance from a request
     * 
     * @param request
     * @return
     */
    protected static CmsUser getCmsUserFromRequest(HttpServletRequest request) {
        GwtUser user = getUserFromRequest(request);
        return new CmsUser(user.getName(), user.isAllowedToWrite(), user.isAdmin());
    }
    
    
    
    
    
    
    // ====================== Content tree ====================== 
    
    
    public List<TreeContentNodeModel> search( String searchTerm ) {
		List<SearchHit> hits = (List<SearchHit>)CmsManager.getInstance().search( searchTerm, MAX_HITS );
		List<ContentNodeI> resultNodes = new ArrayList<ContentNodeI>( hits.size() );
		List<TreeContentNodeModel> result = new ArrayList<TreeContentNodeModel>( hits.size() );
		
		for ( SearchHit hit : hits ) {
			resultNodes.add( hit.getContentKey().getContentNode() );
		}
		Collections.sort( resultNodes, ContentNodeComparator.DEFAULT );
		
		for ( ContentNodeI node : resultNodes ) {
			if ( node == null )
				continue;
			result.add( TranslatorToGwt.toTreeContentNodeModel( node ) );
		}

		return result;
	}

    public List<TreeContentNodeModel> getChildren(TreeContentNodeModel parentNode) throws ServerException {
        try {
            ArrayList<TreeContentNodeModel> children = new ArrayList<TreeContentNodeModel>();

            if (parentNode == null) {
                for (int i = 0; i < ROOTKEYS.length; i++) {
                    ContentNodeI cn = ContentKey.decode(ROOTKEYS[i]).getContentNode();
                    if (cn != null) {
                        children.add( TranslatorToGwt.toTreeContentNodeModel(cn) );
                    }
                }
                return children;
            }

            ContentNodeI root = CmsManager.getInstance().getContentNode(ContentKey.decode(parentNode.getKey()));
            if (root != null) {
                List<ContentKey> childKeys = new ArrayList<ContentKey>((Collection<ContentKey>)root.getChildKeys());
                TreeSet<ContentNodeI> nodes = getOrderedNodes(childKeys);
                for (ContentNodeI childNode : nodes) {
                    children.add( TranslatorToGwt.toTreeContentNodeModel(childNode,parentNode) );
                }
            }            
            return children;
            
        } catch (Throwable e) {
            LOG.error("runtime exception for "+parentNode, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

	private TreeSet<ContentNodeI> getOrderedNodes(Collection<ContentKey> childKeys) {
        TreeSet<ContentNodeI> nodes = new TreeSet<ContentNodeI>(ContentNodeComparator.DEFAULT);
        for (ContentKey key : childKeys) {
        	ContentNodeI node = key.getContentNode();
        	if ( node != null )
        		nodes.add( node );
        }
        return nodes;
    }

	

	
    // ====================== Node data ====================== 
	
	
    public GwtNodeData loadNodeData( String nodeKey ) throws ServerException {
        try {
            ContentNodeI node;
            node = ContentKey.decode(nodeKey).getContentNode();
            GwtNodeData gwtNode = TranslatorToGwt.gwtNodeData( node, !getUser().isAllowedToWrite() );
            return gwtNode;
            
        } catch (IllegalArgumentException e) {
            LOG.error("IllegalArgumentException for "+nodeKey, e);
            throw new ServerException("Invalid content key : " + e.getMessage());
        } catch (Throwable e) {
            LOG.error("Runtime Exception : " + e.getMessage(), e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    public GwtNodeData createNodeData(String type, String id) throws ServerException {
        if (!getUser().isAllowedToWrite()) {
            throw new ServerException("Error : Creating new node as read-only.");
        }

        return TranslatorToGwt.gwtNodeDataSkeleton(type, id);
    }

    
    
    
    
    // ====================== Save ====================== 	
   
    
    public GwtSaveResponse save(Collection<GwtContentNode> nodes) throws ServerException {
		GwtUser user = getUser();
		List<ContentNodeI> contentNodes = new ArrayList<ContentNodeI>(nodes.size());
		for (GwtContentNode gwtNode : nodes) {
			ContentNodeI node = TranslatorFromGwt.getContentNodeI(gwtNode);

			for (String attrName : gwtNode.getChangedValueKeys()) {
			    // FIXME : we have to check for the return value 
				node.setAttributeValue(attrName, TranslatorFromGwt.getServerValue(gwtNode.getAttributeValue(attrName)));
			}

			contentNodes.add(node);
		}
		return saveNodes(user, contentNodes);
	}

    public static GwtSaveResponse saveNodes(GwtUser user, List<ContentNodeI> nodes) throws ServerException {
		try {
			CmsUser cmsUser = new CmsUser(user.getName(), user.isAllowedToWrite(), user.isAdmin());
			CmsRequestI request = new CmsRequest(cmsUser);
			for (ContentNodeI node : nodes) {
				request.addNode(node);
			}
			CmsResponseI responseI = CmsManager.getInstance().handle(request);
			String id = null;
			GwtChangeSet gsc = null;
			if (responseI != null && responseI.getChangeSetId() != null) {
				id = responseI.getChangeSetId().getId();
				gsc = TranslatorToGwt.getGwtChangeSet(getChangeLogService().getChangeSet(responseI.getChangeSetId()), new ChangeSetQuery());
			}
			return new GwtSaveResponse(id, gsc);

		} catch (ContentValidationException v) {
			List<ContentValidationMessage> messages = (List<ContentValidationMessage>) v.getDelegate().getValidationMessages();
			List<GwtValidationError> errors = new ArrayList<GwtValidationError>();
			for (ContentValidationMessage msg : messages) {
				errors.add(new GwtValidationError(msg.getContentKey().getEncoded(), msg.getAttribute(), msg.getMessage()));
			}
			return new GwtSaveResponse(errors);
		} catch (Throwable e) {
			LOG.error("RuntimeException saving  " + nodes, e);
			throw TranslatorToGwt.wrap(e);
		}
	}


    
    
    // ====================== Changesets & Publish ======================    
    
    
    private PublishServiceI getPublishService() {
        return (PublishServiceI) FDRegistry.getInstance().getService(PublishServiceI.class);
    }
    private static ChangeLogServiceI getChangeLogService() {
        return (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);
    }

    
    public ChangeSetQueryResponse getChangeSets(ChangeSetQuery query) throws ServerException {
        try { 
            ChangeLogServiceI chgService = getChangeLogService();
            ChangeSetQueryResponse response = null;
            
            if ( query.isChangeSetQuery() ) {
				ContentKey key;
				try {
					key = ContentKey.decode( query.getContentKey() );
				} catch ( IllegalArgumentException ex ) {
					key = null;
				}
				List<ChangeSet> list = (List<ChangeSet>)chgService.getChangeSets( key, query.getUser(), query.getStartDate(), query.getEndDate() );
				response = createResponse( TranslatorToGwt.getGwtChangeSets( list , query ), query, null );
				response.setLabel( "Query result : " + response.getChangeCount() + " changes in " + response.getChangeSetCount() + " changesets." );		
            	
            } else if ( query.getPublishId() != null ) {
				response = getChangeSetsByPublishId( query, chgService );
				response.setLabel( "Changes in publish #" + query.getPublishId() );				
				
			} else if ( query.getContentKey() != null ) {
				response = createResponse( TranslatorToGwt.getGwtChangeSets( (List<ChangeSet>)chgService.getChangeHistory( ContentKey.decode( query.getContentKey() ) ), query ), query, null );
				response.setLabel( "History of " + ContentKey.decode( query.getContentKey() ).getContentNode().getLabel() );
				
			} 
			
			return response;
        } catch (Throwable e) {
            LOG.error("RuntimeException saving  "+query, e);
            throw TranslatorToGwt.wrap(e);
        }    	
    }
    
    protected ChangeSetQueryResponse getChangeSetsByPublishId( ChangeSetQuery query, ChangeLogServiceI chgService ) {
 
    		// FIXME  seeing publish history and publish-allowed (= allowed-to-write) is not the same!
//            GwtUser user = getUser();
//            if (!user.isPublishAllowed()) {
//                throw new GwtSecurityException("User "+user.getName()+" is not allowed see publish history!");
//            }
            
            if ( query.getPublishId() == null || chgService == null ) {
            	return null;
            }
            
            // Query by publish ID
            
            PublishServiceI service = getPublishService();
            Date prevTimestamp ;
            Date timestamp ;
            Publish publish;
            if ("latest".equalsIgnoreCase(query.getPublishId())) {
                timestamp = new Date();
                publish = service.getMostRecentPublish();
                prevTimestamp = publish.getTimestamp();
            } else {
                publish = service.getPublish(query.getPublishId());
                Publish prevPublish = service.getPreviousPublish(publish);
            
                // for unknown reason, sometimes during publish, prevPublish become null, fortunately 
                prevTimestamp = prevPublish != null ? prevPublish.getTimestamp() : null;
                timestamp = publish.getTimestamp();
            }
            if (query.isPublishInfoQuery()) {
                LOG.info("publish info:"+query.getPublishId() + " -> "+ publish.getId()+':'+publish.getStatus().getName()+" ("+publish.getDescription()+')');
                return new ChangeSetQueryResponse(publish.getStatus().getName(),
                        publish.getTimestamp(), 
                        System.currentTimeMillis() - publish.getTimestamp().getTime(), 
                        TranslatorToGwt.getPublishMessages(publish, query), 
                        getLastInfo(publish));
            } else {
            
	            LOG.info("collecting changes from " + prevTimestamp + " to " + timestamp + ", query:" + query);
	            List<ChangeSet> result = prevTimestamp != null ? (List<ChangeSet>) chgService.getChangesBetween(prevTimestamp, timestamp)
	                    : new ArrayList<ChangeSet>();
	            LOG.info("returning " + result.size() + " changeset" + ", query:" + query);
	            
	            // hack, to not fail with thousands of changesets ...
	            return createResponse(TranslatorToGwt.getGwtChangeSets(result, query), query, publish);
            }
    }
    
    
    private ChangeSetQueryResponse createResponse(List<GwtChangeSet> changeHistory, ChangeSetQuery query, Publish publish) {
        int changeCount = 0;
        for (GwtChangeSet gcs : changeHistory) {
            changeCount += gcs.length();
        }

        if (query.getDirection() != null && query.getDirection()!=SortDir.NONE) {
            if ("user".equals(query.getSortType())) {
                if (query.getDirection()==SortDir.ASC) {
                    Collections.sort(changeHistory, Comparators.USER_COMPARATOR);
                } else {
                    Collections.sort(changeHistory, Comparators.USER_COMPARATOR_INV);
                }
            } if ("date".equals(query.getSortType())) {
                if (query.getDirection()==SortDir.ASC) {
                    Collections.sort(changeHistory, Comparators.DATE_COMPARATOR);
                } else {
                    Collections.sort(changeHistory, Comparators.DATE_COMPARATOR_INV);
                }
            }
        } else  {
            Collections.sort(changeHistory, Comparators.DATE_COMPARATOR);
        }

        int limit = query.getLimit() <= 0 ? 1000 : Math.min(query.getLimit(), 1000);
        int end = Math.min(query.getStart() + limit, changeHistory.size());
        
        List<GwtChangeSet> clientChanges = null;
        
        if (end == changeHistory.size() && query.getStart() == 0) {
            clientChanges = changeHistory;
        } else {
            clientChanges = new ArrayList<GwtChangeSet>(changeHistory.subList(query.getStart(), end)); 
        }
        List<GwtPublishMessage> publishMessages = null;
        int publishMessageCount = 0;
        
        if (publish != null) {
            publishMessageCount = publish.getMessages().size();
            publishMessages = TranslatorToGwt.getPublishMessages(publish, query);            
        }
        
        return new ChangeSetQueryResponse(clientChanges, changeHistory.size(), changeCount, query, publishMessages, publishMessageCount);
    }

    private String getLastInfo(Publish publish) {
        String lastMessage = "running.";
        Date lastDate = null;
        for (PublishMessage m : publish.getMessages()) {
            if (m.getSeverity() == PublishMessage.INFO) {
                if (lastDate==null || lastDate.before(m.getTimestamp())) {
                    if (m.getMessage()!=null && m.getMessage().trim().length()>0) {
                        lastMessage = m.getMessage();
                        lastDate = m.getTimestamp();
                    }
                }
            }
        }
        return lastMessage;
    }

    /**
     * 
     * @param comment
     * @return
     * @throws ServerException 
     */
    public String startPublish(String comment) throws ServerException {
        try {
	        GwtUser user = getUser();
	        if (!user.isPublishAllowed()) {
	            throw new GwtSecurityException("User "+user.getName()+" is not allowed to publish!");
	        }
                LOG.info("start publish called by " + user.getName() + " : '" + comment + "'");
	        Publish recentPublish = getPublishService().getMostRecentNotCompletedPublish();
                LOG.info("most recent publish : " + recentPublish + ", status : " + (recentPublish != null ? recentPublish.getStatus() : null)
                        + ", id : "+ (recentPublish != null ? recentPublish.getId() : null));
                
                if (recentPublish != null && EnumPublishStatus.PROGRESS.equals(recentPublish.getStatus())) {
                    LOG.info("publish in progress ...: "+recentPublish.getId());
                    return recentPublish.getId();
                }
	        
	        Date date = new Date();
	        
	        Publish publish = new Publish();
	        publish.setTimestamp(date);
	        publish.setUserId(user.getName());
	        publish.setStatus(EnumPublishStatus.PROGRESS);
	        publish.setDescription(comment);
	        publish.setLastModified(date);
	        LOG.info("starting new publish by " + user.getName() + ", with comment : '" + comment + "'");
	        return getPublishService().doPublish(publish);
        } catch (Throwable e) {
            LOG.error("RuntimeException for startPublish ", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

	public GwtPublishData getPublishData(ChangeSetQuery query) throws ServerException {
		try {
			GwtPublishData publishData;
			ChangeSetQueryResponse r = getChangeSets(query);						
			ArrayList<GwtChangeSet> changes = new ArrayList<GwtChangeSet>(r.getChanges());
			
			Publish publish;			
			PublishServiceI service = getPublishService();
			
            if ("latest".equalsIgnoreCase(query.getPublishId())) {
                publish = service.getMostRecentPublish();
            } else {
                publish = service.getPublish(query.getPublishId());
            }                      
            
            publishData = TranslatorToGwt.getPublishData(publish);
            publishData.setId(query.getPublishId());
            publishData.setFullyLoaded(true);            
			int changeCount = 0;
			for (GwtChangeSet change : changes) {				
				
				for (GwtNodeChange nodechange : change.getNodeChanges()) {
					int length = nodechange.length();
					publishData.addContributor(change.getUserId(), length);
					publishData.addType(nodechange.getType(), length);		
					changeCount += length;
				}
			}
			
			publishData.setChangeCount(changeCount);
			
			for (PublishMessage message : publish.getMessages() ){
				switch (message.getSeverity()) {
					// Failure
					case 0:
					// Error
					case 1:
					// Warning
					case 2: publishData.addMessage(String.valueOf(message.getSeverity()));
				}				
			}
			
			Collections.sort(publishData.getContributors());
			Collections.sort(publishData.getTypes());
			Collections.sort(publishData.getMessages());
						
			return publishData;			
			
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
	}
    
    @SuppressWarnings("unchecked")
    public List<GwtPublishData> getPublishHistory(PagingLoadConfig config) throws ServerException {
        try {
            PublishServiceI ps = getPublishService();
            List<Publish> listPublishes = ps.getPublishHistory();
            List<GwtPublishData> result = new ArrayList<GwtPublishData>(listPublishes.size());
            for (Publish p : listPublishes) {
                result.add(TranslatorToGwt.getPublishData(p));
            }
    		int start = config.getOffset();
    		int limit = listPublishes.size();
    		if (config.getLimit() > 0) {
    			limit = Math.min(start + config.getLimit(), limit);
    		}
            return new ArrayList(result.subList(start, limit));
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    
    
    
    // ====================== Other stuff ======================     
    

    @Override
    public String generateUniqueId(String type) {
        return CmsManager.getInstance().getTypeService().generateUniqueId(ContentType.get(type));
    }

    @Override
    public Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains) throws ServerException {
        try {
        	return TranslatorToGwt.getDomainValues( domains );
        } catch (Throwable e) {
            LOG.error("RuntimeException for getDomainValues "+domains, e);
            throw TranslatorToGwt.wrap(e);
        }
    }
    
    public String getPreviewUrl( String contentKey ) throws ServerException {
        try {
        	return PreviewLinkProvider.getLink( ContentKey.decode( contentKey ) );
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPreviewUrl", e);
            throw TranslatorToGwt.wrap(e);
        }    	
    }
    
    public ProductConfigParams getProductConfigParams( String skuKey ) throws ServerException { 
    	try {
    		return TranslatorToGwt.getProductConfigParams( ContentKey.decode( skuKey ) );
    	} catch ( Throwable e ) {
            LOG.error("RuntimeException in getProductConfigParams", e);
            throw TranslatorToGwt.wrap(e);    		
    	}
    }

    
    @Override
    public NavigableRelationInfo getNavigableRelations(String contentType) {
        ContentType type = ContentType.get(contentType);

        NavigableRelationInfo nr = new NavigableRelationInfo();
        nr.setContentType(contentType);
        Set<? extends ContentTypeDefI> typeDefinitions = CmsManager.getInstance().getTypeService().getContentTypeDefinitions();
        for (ContentTypeDefI def : typeDefinitions) {
            
            String attrName = getNavigableAttributeName(type, def);
            if (attrName != null) {
                nr.addNavigableAttribute(def.getType().getName(), attrName);
            }
        }
        return nr;
    }

    /**
     * @param type
     * @param nr
     * @param def
     * @param navRels
     * @return 
     */
    private String getNavigableAttributeName(ContentType type, ContentTypeDefI def) {
        Collection<RelationshipDefI> navRels = ContentTypeUtil.getNavigableRelationshipDefs(def);
        for (RelationshipDefI relDef : navRels) {
            if (relDef.getContentTypes().contains(type)) {
                if (EnumCardinality.MANY.equals(relDef.getCardinality())) {
                    return relDef.getName();
                }
            }
        }
        return null;
    }
    
}

