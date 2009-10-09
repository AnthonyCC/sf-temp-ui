package com.freshdirect.cms.ui.serviceimpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeComparator;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
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
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.core.PrimaryKey;
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
            "FDFolder:synonymList", SearchRelevancyList.SEARCH_RELEVANCY_KEY, SearchRelevancyList.WORD_STEMMING_EXCEPTION };
    
    public List<ContentNodeModel> search(String searchTerm) {
        List<SearchHit> hits = (List<SearchHit>)CmsManager.getInstance().search(searchTerm, MAX_HITS);
        List<ContentNodeModel> result = new ArrayList<ContentNodeModel>(hits.size());
        for (SearchHit hit : hits) {
            ContentKey key = hit.getContentKey();
            ContentNodeI node = CmsManager.getInstance().getContentNode(key);
            if ( node == null )
            	continue;
            ContentNodeModel n = TranslatorToGwt.getContentNodeModel(node);
            result.add(n);
        }
        Collections.sort( result );
        
        return result;
    }

    public List<ContentNodeModel> getChildren(ContentNodeModel loadConfig) throws ServerException {
        try {
            ArrayList<ContentNodeModel> children = new ArrayList<ContentNodeModel>();

            if (loadConfig == null) {
                for (int i = 0; i < ROOTKEYS.length; i++) {
                    ContentNodeI cn = ContentKey.decode(ROOTKEYS[i]).getContentNode();
                    if (cn != null) {
                        ContentNodeModel n = TranslatorToGwt.getContentNodeModel(cn);
                        children.add(n);
                    }
                }
                return children;
            }

            ContentNodeI root = CmsManager.getInstance().getContentNode(ContentKey.decode(loadConfig.getKey()));
            if (root != null) {
                List<ContentKey> childKeys = new ArrayList<ContentKey>((Collection<ContentKey>)root.getChildKeys());
                TreeSet<ContentNodeI> nodes = getOrderedNodes(childKeys);
                for (ContentNodeI childNode : nodes) {
                    ContentNodeModel child = TranslatorToGwt.getContentNodeModel(childNode);
                    children.add(child);
                }
            }            
            return children;
            
        } catch (Throwable e) {
            LOG.error("runtime exception for "+loadConfig, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @SuppressWarnings("unchecked")
	private TreeSet<ContentNodeI> getOrderedNodes(Collection<ContentKey> childKeys) {
        TreeSet<ContentNodeI> nodes = new TreeSet<ContentNodeI>(ContentNodeComparator.DEFAULT);
        for (ContentKey key : childKeys) {
            nodes.add(key.getContentNode());
        }
        return nodes;
    }

    public GwtNodeData getNodeData(String nodeKey) throws ServerException {
        try {
            ContentNodeI node;
            node = ContentKey.decode(nodeKey).getContentNode();            
            return TranslatorToGwt.gwtNodeData( node, !getUser().isAllowedToWrite() );
            
        } catch (IllegalArgumentException e) {
            LOG.error("IllegalArgumentException for "+nodeKey, e);
            throw new RuntimeException("Invalid content key : " + e.getMessage());
        } catch (Throwable e) {
            LOG.error("Runtime Exception : " + e.getMessage(), e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    public GwtNodeData createNodeData(String type, String id) {
        if (!getUser().isAllowedToWrite()) {
            throw new RuntimeException("Error : Creating new node as read-only.");
        }

        return TranslatorToGwt.gwtNodeDataSkeleton(type, id);
    }

    public List<BulkEditModel> getEditChildren(BulkEditModel loadConfig) {
        ArrayList<BulkEditModel> children = new ArrayList<BulkEditModel>();

        if (loadConfig == null) {
            for (int i = 0; i < ROOTKEYS.length; i++) {
                ContentNodeI cn = ContentKey.decode(ROOTKEYS[i]).getContentNode();
                if (cn != null) {
                    BulkEditModel n = TranslatorToGwt.getBulkModel(cn);
                    children.add(n);
                }
            }

            return children;
        }

        ContentNodeI root = ContentKey.decode(loadConfig.getKey()).getContentNode();
        if (root == null) {
            return children;
        }

        List<ContentKey> childKeys = new ArrayList<ContentKey>((Collection<ContentKey>)root.getChildKeys());
        TreeSet<ContentNodeI> nodes = getOrderedNodes(childKeys);
        for (ContentNodeI childNode : nodes) {
            BulkEditModel child = TranslatorToGwt.getBulkModel(childNode);
            children.add(child);
        }

        return children;
    }

    
    @SuppressWarnings("unchecked")
	public GwtSaveResponse save(Collection<GwtContentNode> nodes) throws ServerException {
        try {
            GwtUser user = getUser();
            CmsUser cmsUser = new CmsUser(user.getName(), user.isAllowedToWrite(), user.isAdmin());
            CmsRequestI request = new CmsRequest(cmsUser);
            for (GwtContentNode gwtNode : nodes) {
                ContentNodeI node = TranslatorFromGwt.getContentNodeI(gwtNode);
                
                for (String attrName : gwtNode.getChangedValueKeys()) {
                    node.setAttributeValue(attrName, TranslatorFromGwt.getServerValue(gwtNode.getAttributeValue(attrName)));
                }
                request.addNode(node);
            }
            CmsResponseI responseI = CmsManager.getInstance().handle(request);
            String id = null;
            GwtChangeSet gsc = null;
            if (responseI != null && responseI.getChangeSetId() != null) {
                id = responseI.getChangeSetId().getId();
                gsc = TranslatorToGwt.getGwtChangeSet(getChangeLogService().getChangeSet(responseI.getChangeSetId()));
            }
            return new GwtSaveResponse(id, gsc);
            
        } catch (ContentValidationException v) {
            List<ContentValidationMessage> messages = (List<ContentValidationMessage>)v.getDelegate().getValidationMessages();
            List<GwtValidationError> errors = new ArrayList<GwtValidationError>();
            for (ContentValidationMessage msg : messages) {
                errors.add(new GwtValidationError(msg.getContentKey().getEncoded(), msg.getAttribute(), msg.getMessage()));
            }
            return new GwtSaveResponse(errors);
        } catch (Throwable e) {
            LOG.error("RuntimeException saving  "+nodes, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ChangeSetQueryResponse getChangeSets(ChangeSetQuery query) throws ServerException {
        try {
            GwtUser user = getUser();
            if (!user.isPublishAllowed()) {
                throw new GwtSecurityException("User "+user.getName()+" is not allowed see publish history!");
            }
            ChangeLogServiceI chgService = getChangeLogService();
            if (query.getById() != null) {
                return createResponse(TranslatorToGwt.getGwtChangeSets(chgService.getChangeSet(new PrimaryKey(query.getById()))), query, null);
            }
            if (query.getByKey() != null) {
                return createResponse(TranslatorToGwt.getGwtChangeSets((List<ChangeSet>) chgService.getChangeHistory(ContentKey.decode(query.getByKey()))), query, null);
            }
            if (query.getPublishId() != null) {
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
                
                    prevTimestamp = prevPublish.getTimestamp();
                    timestamp = publish.getTimestamp();
                }
                if (query.isPublishInfoQuery()) {
                    LOG.info("publish info:"+query.getPublishId() + " -> "+ publish.getId()+':'+publish.getStatus().getName()+" ("+publish.getDescription()+')');
                    return new ChangeSetQueryResponse(publish.getStatus().getName(),
                            publish.getTimestamp(), 
                            System.currentTimeMillis() - publish.getTimestamp().getTime(), 
                            TranslatorToGwt.getPublishMessages(publish, query.getPublishMessageStart(), query.getPublishMessageEnd()), 
                            getLastInfo(publish));
                }
                
                LOG.info("collecting changes from " + prevTimestamp + " to " + timestamp + ", query:" + query);
                List<ChangeSet> result = (List<ChangeSet>) chgService.getChangesBetween(prevTimestamp, timestamp);
                LOG.info("returning " + result.size() + " changeset" + ", query:" + query);
                
                // hack, to not fail with thousands of changesets ...
                return createResponse(TranslatorToGwt.getGwtChangeSets(result), query, publish);
            }
            return null;
        } catch (Throwable e) {
            LOG.error("RuntimeException saving  "+query, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    private String getLastInfo(Publish publish) {
        String lastMessage = "running.";
        //FIXME lastDate is always null, whats the point?
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
	        Publish recentPublish = getPublishService().getMostRecentNotCompletedPublish();
	        if (recentPublish!=null && EnumPublishStatus.PROGRESS.equals(recentPublish.getStatus())) {
	            return recentPublish.getId();
	        }
	        
	        Date date = new Date();
	        
	        Publish publish = new Publish();
	        publish.setTimestamp(date);
	        publish.setUserId(user.getName());
	        publish.setStatus(EnumPublishStatus.PROGRESS);
	        publish.setDescription(comment);
	        publish.setLastModified(date);
	        
	        return getPublishService().doPublish(publish);
        } catch (Throwable e) {
            LOG.error("RuntimeException for startPublish ", e);
            throw TranslatorToGwt.wrap(e);
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
                    Collections.sort(changeHistory, GwtChangeSet.USER_COMPARATOR);
                } else {
                    Collections.sort(changeHistory, GwtChangeSet.USER_COMPARATOR_INV);
                }
            } if ("date".equals(query.getSortType())) {
                if (query.getDirection()==SortDir.ASC) {
                    Collections.sort(changeHistory, GwtChangeSet.DATE_COMPARATOR);
                } else {
                    Collections.sort(changeHistory, GwtChangeSet.DATE_COMPARATOR_INV);
                }
            }
        } else  {
            Collections.sort(changeHistory, GwtChangeSet.DATE_COMPARATOR);
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
            publishMessages = TranslatorToGwt.getPublishMessages(publish, query
                    .getPublishMessageStart(), query.getPublishMessageEnd());            
        }
        
        return new ChangeSetQueryResponse(clientChanges, changeHistory.size(), changeCount, query, publishMessages, publishMessageCount);
    }


    private ChangeLogServiceI getChangeLogService() {
        return (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);
    }

    @Override
    public String generateUniqueId(String type) {
        return CmsManager.getInstance().getTypeService().generateUniqueId(ContentType.get(type));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains) throws ServerException {
        try {
            Map<String, List<ContentNodeModel>> result = new HashMap<String, List<ContentNodeModel>>();
            for (ContentNodeModel domain : domains) {
                ContentKey key = (ContentKey) TranslatorFromGwt.getServerValue(domain);
                ContentNodeI node = key.getContentNode();
                List<ContentKey> domainValues = (List<ContentKey>) node.getAttribute("domainValues").getValue();
                
                List<ContentNodeModel> tempList = new ArrayList<ContentNodeModel>();
                for (ContentKey domainValue : domainValues) {
                    tempList.add(TranslatorToGwt.getContentNodeModel(domainValue.getContentNode()));
                }
                
                result.put(domain.getKey(), tempList);
                
            }
            return result;
        } catch (Throwable e) {
            LOG.error("RuntimeException for getDomainValues "+domains, e);
            throw TranslatorToGwt.wrap(e);
        }
    }
    
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
    public static GwtUser getUserFromRequest(HttpServletRequest request) {
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
    public static CmsUser getCmsUserFromRequest(HttpServletRequest request) {
        GwtUser user = getUserFromRequest(request);
        return new CmsUser(user.getName(), user.isAllowedToWrite(), user.isAdmin());
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
    public List<GwtPublishData> getPublishHistory() throws ServerException {
        try {
            PublishServiceI ps = getPublishService();
            List<Publish> listPublishes = ps.getPublishHistory();
            List<GwtPublishData> result = new ArrayList<GwtPublishData>(listPublishes.size());
            for (Publish p : listPublishes) {
                result.add(TranslatorToGwt.getPublishData(p));
            }
            return result;
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    private PublishServiceI getPublishService() {
        return (PublishServiceI) FDRegistry.getInstance().getService(PublishServiceI.class);
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        CmsManager.getInstance();
    }
    
    public String getPreviewUrl( String contentKey ) throws ServerException {
        try {
        	return PreviewLinkProvider.getLink( ContentKey.decode( contentKey ) );
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPreviewUrl", e);
            throw TranslatorToGwt.wrap(e);
        }    	
    }
}
