package com.freshdirect.cms.application.draft.applicator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draftchange.converter.DraftChangeToContentNodeApplicator;
import com.freshdirect.cmsadmin.domain.DraftChange;

public class DraftApplicatorService {

    private static final DraftApplicatorService INSTANCE = new DraftApplicatorService();

    public static DraftApplicatorService defaultService() {
        return INSTANCE;
    }

    private DraftApplicatorService() {
    }

    public List<ContentNodeI> applyDraftChangesToContentNodes(List<DraftChange> draftChanges, List<ContentNodeI> nodes, ContentServiceI contentService, DraftContext draftContext) {

        Map<ContentKey, ContentNodeI> foundContentNodes = new LinkedHashMap<ContentKey, ContentNodeI>();

        for (DraftChange draftChange : draftChanges) {
            boolean contentNodeFound = false;
            ContentKey draftChangeKey = ContentKey.decode(draftChange.getContentKey());
            ContentNodeI prevHandledNode = foundContentNodes.get(draftChangeKey);

            if (prevHandledNode != null) {
                ContentNodeI draftDecoratedNode = DraftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, prevHandledNode);
                foundContentNodes.put(draftChangeKey, draftDecoratedNode);
                contentNodeFound = true;
            } else {

                for (ContentNodeI nodeFromMain : nodes) {
                    if (draftChangeKey.equals(nodeFromMain.getKey())) {
                        ContentNodeI draftDecoratedNode = DraftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChange, nodeFromMain);
                        foundContentNodes.put(draftChangeKey, draftDecoratedNode);
                        contentNodeFound = true;
                        break;
                    }
                }

                if (!contentNodeFound) {
                    ContentNodeI draftDecoratedNode = DraftChangeToContentNodeApplicator.createContentNodeFromDraftChange(draftChange, contentService, draftContext);
                    foundContentNodes.put(draftChangeKey, draftDecoratedNode);
                }
            }
        }
        List<ContentNodeI> draftDecoratedNodes = new ArrayList<ContentNodeI>(foundContentNodes.values());
        return draftDecoratedNodes;
    }
}
