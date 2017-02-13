package com.freshdirect.cms.application.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.applicator.DraftApplicatorService;
import com.freshdirect.cms.application.draft.extractor.DraftChangeExtractorService;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cmsadmin.domain.DraftChange;

public class DraftHandlingContentService implements ContentServiceI {

    private final ContentServiceI contentService;
    private final DraftService draftService;
    private final DraftApplicatorService draftApplicatorService;
    private final DraftChangeExtractorService draftChangeExtractorService;

    public DraftHandlingContentService(ContentServiceI rootService) {
        contentService = rootService;
        draftService = DraftService.defaultService();
        draftApplicatorService = DraftApplicatorService.defaultService();
        draftChangeExtractorService = DraftChangeExtractorService.defaultService();
    }

    @Override
    public String getName() {
        return DraftHandlingContentService.class.getSimpleName();
    }

    @Override
    public ContentTypeServiceI getTypeService() {
        return contentService.getTypeService();
    }

    @Override
    public Set<ContentKey> getContentKeys(DraftContext draftContext) {
        if (DraftContext.MAIN == draftContext) {
            return contentService.getContentKeys(draftContext);
        }

        final Set<ContentKey> keys = new HashSet<ContentKey>();
        keys.addAll(contentService.getContentKeys(draftContext));
        keys.addAll(draftService.getAllChangedContentKeys(draftContext.getDraftId()));
        return keys;
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
        if (DraftContext.MAIN == draftContext) {
            return contentService.getContentKeysByType(type, draftContext);
        }

        final Set<ContentKey> keys = new HashSet<ContentKey>();

        keys.addAll(contentService.getContentKeysByType(type, draftContext));

        final Set<ContentKey> draftKeys = draftService.getAllChangedContentKeys(draftContext.getDraftId());
        for (final ContentKey draftKey : draftKeys) {
            if (type.equals(draftKey.getType())) {
                keys.add(draftKey);
            }
        }

        return keys;
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
        if (DraftContext.MAIN == draftContext) {
            return contentService.getParentKeys(key, draftContext);
        }

        // collect all draft changes for the given draft and extract nodes
        List<DraftChange> draftChanges = draftService.getDraftChanges(draftContext.getDraftId());

        List<ContentNodeI> nodes = constructDraftNodes(draftChanges, draftContext);

        // build parent map - acquire existing ones first
        Set<ContentKey> parentKeys = contentService.getParentKeys(key, DraftContext.MAIN);

        // build parent index from draft nodes
        Map<ContentKey, Set<ContentKey>> draftParentIndex = ContentNodeUtil.getParentIndex(nodes);
        if (draftParentIndex.containsKey(key)) {
            parentKeys = clearUpParentKeysOnDraft(key, parentKeys, draftParentIndex.get(key), nodes);
            parentKeys.addAll(draftParentIndex.get(key));
        }
        
        return parentKeys;
    }
    

    private Set<ContentKey> clearUpParentKeysOnDraft(ContentKey keyOfQuestion, Set<ContentKey> parentKeysFromMain, Set<ContentKey> parentKeysOnDraft, List<ContentNodeI> draftNodes){
        Set<ContentKey> parentKeys = new HashSet<ContentKey>();
        Set<ContentKey> changedParents = new HashSet<ContentKey>(parentKeysFromMain);
        changedParents.retainAll(collectKeysOfNodes(draftNodes)); // resulting those parents which changed on draft
        
        for(ContentKey key : changedParents){
            ContentNodeI contentNode = getNodeByKey(draftNodes, key);
            //check if the changed 'parent' is still a parent of the node
            if(contentNode.getChildKeys().contains(keyOfQuestion)){
                parentKeys.add(key);
            }
        }
        
        //we need the parent keys from MAIN which are not changed on draft - they are still parents of the node
        for(ContentKey key : parentKeysFromMain){
            if(!changedParents.contains(key)){
                parentKeys.add(key);
            }
        }
        
        return parentKeys;
    }
    
    private ContentNodeI getNodeByKey(List<ContentNodeI> nodes, ContentKey key){
        ContentNodeI contentNode = null;
        for(ContentNodeI node : nodes){
            if(node.getKey().equals(key)){
                contentNode = node;
                break;
            }
        }
        return contentNode;
    }
    
    private Set<ContentKey> collectKeysOfNodes(List<ContentNodeI> nodes){
        Set<ContentKey> contentKeys = new HashSet<ContentKey>();
        for(ContentNodeI node : nodes){
            contentKeys.add(node.getKey());
        }
        return contentKeys;
    }                                                  

    @Override
    public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
        if (DraftContext.MAIN == draftContext) {
            return contentService.getContentNode(key, draftContext);
        }

        // collect all draft changes for the given draft and extract nodes
        List<DraftChange> draftChanges = draftService.getDraftChanges(draftContext.getDraftId());

        List<ContentNodeI> nodes = constructDraftNodes(draftChanges, draftContext);
        for (final ContentNodeI node : nodes) {
            // look for draft node first
            if (key.equals(node.getKey())) {
                return node;
            }
        }
        
        // fall back to upstream service
        return contentService.getContentNode(key, DraftContext.MAIN);
    }

    @Override
    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
        if (DraftContext.MAIN == draftContext) {
            return contentService.getContentNodes(keys, draftContext);
        }

        // collect all draft changes for the given draft and extract nodes
        List<DraftChange> draftChanges = draftService.getDraftChanges(draftContext.getDraftId());
        
        List<ContentNodeI> nodes = constructDraftNodes(draftChanges, draftContext);

        // transform list of nodes to map of (key, node) couples
        final Map<ContentKey, ContentNodeI> nodeMap = new HashMap<ContentKey, ContentNodeI>( keys.size() );
        for (final ContentNodeI node : nodes) {
            nodeMap.put(node.getKey(), node);
        }

        // find out which keys are non-drafts
        final Set<ContentKey> nonDraftKeys = new HashSet<ContentKey>();
        nonDraftKeys.addAll(keys);
        nonDraftKeys.removeAll(nodeMap.keySet());
        
        Map<ContentKey, ContentNodeI> nonDraftNodeMap = contentService.getContentNodes(nonDraftKeys, DraftContext.MAIN);
        // complement result with original nodes if any
        nodeMap.putAll(nonDraftNodeMap);

        return nodeMap;
    }

    @Override
    public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
        return contentService.getContentNode(key, draftContext);
    }

    /**
     * Does the same as {@link AbstractContentService#queryContentNodes(ContentType, Predicate, DraftContext)}
     */
    @Override
    public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type, Predicate criteria, DraftContext draftContext) {
        Set<ContentKey> keys = this.getContentKeysByType(type, draftContext);
        Map<ContentKey, ContentNodeI> nodes = this.getContentNodes(keys, draftContext);
        CollectionUtils.filter(nodes.values(), criteria);
        return nodes;
    }

    @Override
    public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
        return contentService.createPrototypeContentNode(key, draftContext);
    }

    @Override
    public CmsResponseI handle(CmsRequestI request) {
        if (DraftContext.MAIN == request.getDraftContext()) {
            // stay on course
            return contentService.handle(request);
        }
        
        List<DraftChange> draftChanges = draftChangeExtractorService.extractChangesFromRequest(request);
        if (!draftChanges.isEmpty()) {
            draftService.saveDraftChange(draftChanges);
        }
        
        return new CmsResponse();
    }

    // collect all draft changes for the given draft and extract nodes
    private Set<ContentKey> extractContentKeys(Collection<DraftChange> draftChanges) {
        final Set<ContentKey> draftKeys = new HashSet<ContentKey>();

        // map Collection<DraftChange> -> Set<ContentKey>
        for (final DraftChange change : draftChanges) {
            draftKeys.add(ContentKey.getContentKey(change.getContentKey()));
        }

        return draftKeys;
    }


    /**
     * Load existing nodes and apply draft changes to them
     * 
     * @param draftChanges
     * @param draftContext
     * @return
     */
    private List<ContentNodeI> constructDraftNodes(List<DraftChange> draftChanges, DraftContext draftContext) {
        Set<ContentKey> draftKeys = extractContentKeys(draftChanges);

        // fetch original nodes if any
        final Map<ContentKey, ContentNodeI> mainContentNodes = contentService.getContentNodes(draftKeys, DraftContext.MAIN);
        List<ContentNodeI> clones = new ArrayList<ContentNodeI>(draftKeys.size());
        for (final ContentKey aKey : draftKeys) {
            if (mainContentNodes.keySet().contains(aKey)) {
                clones.add(mainContentNodes.get(aKey).copy());
            } else {
                clones.add(createPrototypeContentNode(aKey, draftContext));
            }
        }

        // apply draft changes
        draftApplicatorService.applyDraftChangesToContentNodes(draftChanges, clones, contentService, draftContext);

        return clones;
    }
}
