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

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeComparator;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.model.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtPublishData;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.GwtValidationError;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.GwtSecurityException;
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
    
    static class KeyInfo {
        int    start = -1;
        int    end   = -1;
        String type;
        String id;

        String originalKey;

        public KeyInfo(String originalKey) {
            this.originalKey = originalKey;
            String[] segments = originalKey.split(":");
            if ("page".equals(segments[0])) {
                start = Integer.parseInt(segments[1]);
                end = Integer.parseInt(segments[2]);
                type = segments[3];
                id = segments[4];
            } else {
                type = segments[0];
                id = segments[1];
            }
        }
        
        public String getType() {
            return type;
        }
        
        public String getId() {
            return id;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public ContentKey getContentKey() throws InvalidContentKeyException {
            return new ContentKey(ContentType.get(type), id);
        }

        public String getPageKey(int nstart, int nend) {
            return "page:" + nstart + ":" + nend + ":" + type + ":" + id;
        }

        public boolean isPaging() {
            return start >= 0;
        }

    }

    private static final long     serialVersionUID = 1893834571301941106L;

    private final static int      MAX_HITS         = 120;
    
//    private final static int      MAX_CHILD_KEYS   = 100;

    private static final String[] ROOTKEYS         = { "Store:FreshDirect", "MediaFolder:/", "CmsFolder:forms", "CmsQueryFolder:queries",
            "CmsQuery:orphans", "FDFolder:recipes", "FDFolder:ymals", "FDFolder:starterLists",
            "FDFolder:synonymList", "FDFolder:searchRelevancyList" };

    public String getLabel(String nodeKey) {
        return ContentKey.decode(nodeKey).getContentNode().getLabel();
    }

    @SuppressWarnings("unchecked")
	public List<ContentNodeModel> search(String searchTerm) {
        List<SearchHit> hits = (List<SearchHit>)CmsManager.getInstance().search(searchTerm, MAX_HITS);
        List<ContentNodeModel> result = new ArrayList<ContentNodeModel>(hits.size());
        for (SearchHit hit : hits) {
            ContentKey key = hit.getContentKey();
            ContentNodeI node = CmsManager.getInstance().getContentNode(key);
            ContentNodeModel n = TranslatorToGwt.getContentNodeModel(node);
            result.add(n);
        }
        
        Collections.sort( result );
        
        return result;
    }

    @SuppressWarnings("unchecked")
	public List<ContentNodeModel> getChildren(ContentNodeModel loadConfig) {
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

            KeyInfo keyInfo = new KeyInfo(loadConfig.getKey());
            ContentNodeI root = CmsManager.getInstance().getContentNode(keyInfo.getContentKey());
            if (root != null) {
                List<ContentKey> childKeys = new ArrayList<ContentKey>((Collection<ContentKey>)root.getChildKeys());
                TreeSet<ContentNodeI> nodes = getOrderedNodes(childKeys);
                for (ContentNodeI childNode : nodes) {
                    ContentNodeModel child = TranslatorToGwt.getContentNodeModel(childNode);
                    children.add(child);
                }
            }

            
            // old method with paging hack
            
//            KeyInfo keyInfo = new KeyInfo(loadConfig.getKey());
//            ContentNodeI root = CmsManager.getInstance().getContentNode(keyInfo.getContentKey());
//            if (root != null) {
//                List<ContentKey> childKeys = new ArrayList<ContentKey>((Collection<ContentKey>)root.getChildKeys());
//                if (childKeys.size() >= MAX_CHILD_KEYS) {
//                    // paging ...
//                    if (!keyInfo.isPaging()) {
//                        // return 'virtual' nodes ...
//                        for (int i = 0; i < childKeys.size(); i += MAX_CHILD_KEYS) {
//                            int endPos = Math.min(i + MAX_CHILD_KEYS, childKeys.size());
//                            ContentNodeModel child = new ContentNodeModel(keyInfo.getType(), root.getLabel() + "[" + i + ',' + endPos + ']', keyInfo.getPageKey(i, endPos));
//                            children.add(child);
//                        }
//                    } else {
//                        // return paged result, without sorting ...
//                        for (int i = keyInfo.getStart(); i < keyInfo.getEnd(); i++) {
//                            ContentKey key = childKeys.get(i);
//                            ContentNodeModel child = TranslatorToGwt.getContentNodeModel(key.getContentNode());
//                            children.add(child);
//                        }
//                    }
//                } else {
//                    TreeSet<ContentNodeI> nodes = getOrderedNodes(childKeys);
//                    for (ContentNodeI childNode : nodes) {
//                        ContentNodeModel child = TranslatorToGwt.getContentNodeModel(childNode);
//                        children.add(child);
//                    }
//                }
//            }

            return children;
        } catch (RuntimeException e) {
            LOG.error("runtime exception for "+loadConfig, e);
            return new ArrayList<ContentNodeModel>();
        } catch (InvalidContentKeyException e) {
            LOG.error("InvalidContentKeyException for "+loadConfig, e);
            return new ArrayList<ContentNodeModel>();
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

    public GwtNodeData getNodeData(String nodeKey) {
        try {
            ContentNodeI node;
            node = new KeyInfo(nodeKey).getContentKey().getContentNode();
            
            return TranslatorToGwt.gwtNodeData( node, !getUser().isAllowedToWrite() );
            
        } catch (InvalidContentKeyException e) {
            LOG.error("InvalidContentKeyException for "+nodeKey, e);
            throw new RuntimeException("Invalid content key : " + e.getMessage());
        }
    }

    public GwtNodeData createNodeData(String type, String id) {

    	if ( !getUser().isAllowedToWrite() ) {
    		throw new RuntimeException("Error : Creating new node as read-only.");
    	}
    	
    	return TranslatorToGwt.gwtNodeDataSkeleton( type, id );
    }

    @SuppressWarnings("unchecked")
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
	public GwtSaveResponse save(Collection<GwtContentNode> nodes) {
        try {
            GwtUser user = getUser();
            CmsUser cmsUser = new CmsUser(user.getName(), user.isAllowedToWrite(), user.isAdmin());
            CmsRequestI request = new CmsRequest(cmsUser);
            for (GwtContentNode gwtNode : nodes) {
                ContentNodeI node = TranslatorFromGwt.getContentNodeI(gwtNode);
                
                for (String attrName : gwtNode.getChangedValueKeys()) {
                    AttributeI attribute = node.getAttribute(attrName);
                    if (attribute != null) {
                        attribute.setValue(TranslatorFromGwt.getServerValue(gwtNode.getAttributeValue(attrName)));
                    }
                }
                request.addNode(node);
            }
            CmsResponseI responseI = CmsManager.getInstance().handle(request);
            String id = responseI != null && responseI.getChangeSetId() != null ? responseI.getChangeSetId().getId() : null;
            GwtChangeSet gsc = null;
            if (id != null) {
                gsc = TranslatorToGwt.getGwtChangeSet( getChangeLogService().getChangeSet(responseI.getChangeSetId()));
            }
            return new GwtSaveResponse(id, gsc);
        } catch (ContentValidationException v) {
            List<ContentValidationMessage> messages = (List<ContentValidationMessage>)v.getDelegate().getValidationMessages();
            List<GwtValidationError> errors = new ArrayList<GwtValidationError>();
            for (ContentValidationMessage msg : messages) {
                errors.add(new GwtValidationError(msg.getContentKey().getEncoded(), msg.getAttribute(), msg.getMessage()));
            }
            return new GwtSaveResponse(errors);
        } catch (RuntimeException e) {
            LOG.error("RuntimeException saving  "+nodes, e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ChangeSetQueryResponse getChangeSets(ChangeSetQuery query) {
        try {
            GwtUser user = getUser();
            if (!user.isAdmin()) {
                throw new GwtSecurityException("User "+user.getName()+" is not allowed see publish history!");
            }
            ChangeLogServiceI chgService = getChangeLogService();
            if (query.getById() != null) {
                return createResponse(TranslatorToGwt.getGwtChangeSets(chgService.getChangeSet(new PrimaryKey(query.getById()))), query);
            }
            if (query.getByKey() != null) {
                return createResponse(TranslatorToGwt.getGwtChangeSets((List<ChangeSet>) chgService.getChangeHistory(ContentKey.decode(query.getByKey()))), query);
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
                    return new ChangeSetQueryResponse(publish.getStatus().getName(),
                            publish.getTimestamp(), 
                            System.currentTimeMillis() - publish.getTimestamp().getTime());
                }
                
                LOG.info("collecting changes from " + prevTimestamp + " to " + timestamp + ", query:" + query);
                List<ChangeSet> result = (List<ChangeSet>) chgService.getChangesBetween(prevTimestamp, timestamp);
                LOG.info("returning " + result.size() + " changeset" + ", query:" + query);
                
                // hack, to not fail with thousands of changesets ...
                return createResponse(TranslatorToGwt.getGwtChangeSets(result), query);
            }
            return null;
        } catch (RuntimeException e) {
            LOG.error("RuntimeException saving  "+query, e);
            throw e;
        }
    }

    /**
     * 
     * @param comment
     * @return
     */
    public String startPublish(String comment) {
        GwtUser user = getUser();
        if (!user.isAdmin()) {
            throw new GwtSecurityException("User "+user.getName()+" is not allowed to publish!");
        }
        Date date = new Date();
        
        Publish publish = new Publish();
        publish.setTimestamp(date);
        publish.setUserId(user.getName());
        publish.setStatus(EnumPublishStatus.PROGRESS);
        publish.setDescription(comment);
        publish.setLastModified(date);
        
        return getPublishService().doPublish(publish);
    }
    

    private ChangeSetQueryResponse createResponse(List<GwtChangeSet> changeHistory, ChangeSetQuery query) {
        int changeCount = 0;
        for (GwtChangeSet gcs : changeHistory) {
            changeCount += gcs.length();
        }

        int limit = query.getLimit() <= 0 ? 1000 : Math.min(query.getLimit(), 1000);
        int end = Math.min(query.getStart() + limit, changeHistory.size());
        if (end == changeHistory.size() && query.getStart() == 0) {
            return new ChangeSetQueryResponse(changeHistory, changeHistory.size(), changeCount, query);
        }
        return new ChangeSetQueryResponse(new ArrayList<GwtChangeSet>(changeHistory.subList(query.getStart(), end)), changeHistory.size(), changeCount, query);
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
    public Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains) {
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
        } catch (RuntimeException e) {
            LOG.error("RuntimeException for getDomainValues "+domains, e);
            throw e;
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
        Principal userPrincipal = request.getUserPrincipal();
        // TODO : eliminate this check, user principal is null, if it's running in hosted mode OR it isn't authenticated.
        if (userPrincipal == null) {
            return new CmsUser("cms-teszt-user", true, true);
        } else {
            return new CmsUser(userPrincipal.getName(), request.isUserInRole("cms_editor"), request.isUserInRole("cms_admin"));
        }
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
    public List<GwtPublishData> getPublishHistory() {
        try {
            PublishServiceI ps = getPublishService();
            List<Publish> listPublishes = ps.getPublishHistory();
            List<GwtPublishData> result = new ArrayList<GwtPublishData>(listPublishes.size());
            for (Publish p : listPublishes) {
                result.add(TranslatorToGwt.getPublishData(p));
            }
            return result;
        } catch (RuntimeException e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw e;
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
}
