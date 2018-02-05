package com.freshdirect.cms.draft.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.draft.domain.DraftChange;

@Service
public class DraftApplicatorService {

    @Autowired
    private DraftChangeToContentNodeApplicator draftChangeToContentNodeApplicator;

    public Map<ContentKey, Map<Attribute, Object>> applyDraftChangesToContentNodes(List<DraftChange> draftChanges, Map<ContentKey, Map<Attribute, Object>> nodes) {

        Map<ContentKey, Map<Attribute, Object>> foundContentNodes = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();

        for (DraftChange draftChange : draftChanges) {
            boolean contentNodeFound = false;
            ContentKey draftChangeKey = ContentKeyFactory.get(draftChange.getContentKey());
            Map<Attribute, Object> prevHandledNode = foundContentNodes.get(draftChangeKey);

            if (prevHandledNode != null) {
                Map<Attribute, Object> draftDecoratedNode = draftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, prevHandledNode);
                foundContentNodes.put(draftChangeKey, draftDecoratedNode);
                contentNodeFound = true;
            } else {

                for (ContentKey nodeFromMainKey : nodes.keySet()) {
                    if (draftChangeKey.equals(nodeFromMainKey)) {
                        Map<Attribute, Object> draftDecoratedNode = draftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, nodes.get(nodeFromMainKey));
                        foundContentNodes.put(draftChangeKey, draftDecoratedNode);
                        contentNodeFound = true;
                        break;
                    }
                }

                if (!contentNodeFound) {
                    Map<ContentKey, Map<Attribute, Object>> draftDecoratedNode = draftChangeToContentNodeApplicator.createContentNodeFromDraftChange(draftChange);
                    foundContentNodes.put(draftChangeKey, draftDecoratedNode.get(draftChangeKey));
                }
            }
        }
        Map<ContentKey, Map<Attribute, Object>> draftDecoratedNodes = new HashMap<ContentKey, Map<Attribute, Object>>(foundContentNodes);
        return draftDecoratedNodes;
    }
    
    public Map<Attribute, Object> applyDraftChangesToContentNode(List<DraftChange> draftChanges, Map<Attribute, Object> node) {
        for (DraftChange draftChange : draftChanges) {
            draftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, node);
        }
        return node;
    }
}
