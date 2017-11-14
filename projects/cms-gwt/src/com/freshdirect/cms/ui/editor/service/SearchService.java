package com.freshdirect.cms.ui.editor.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.editor.ContentKeyDisplayOrderComparator;
import com.freshdirect.cms.ui.editor.search.domain.SearchHit;
import com.freshdirect.cms.ui.editor.search.service.LuceneSearchService;

@Service
public class SearchService {

    @Autowired
    private LuceneSearchService luceneSearchService;

    @Autowired
    private LabelProviderService labelProviderService;

    @Autowired
    private TreeNodeService treeNodeService;

    public List<TreeContentNodeModel> search(String searchKeywords) {
        Collection<SearchHit> searchHits = luceneSearchService.search(searchKeywords, false, 100);
        Set<ContentKey> searchResults = new HashSet<ContentKey>();
        for (SearchHit searchHit : searchHits) {
            searchResults.add(searchHit.getContentKey());
        }

        List<ContentKey> orderedKeys = sortResults(new ArrayList<ContentKey>(searchResults));
        List<TreeContentNodeModel> results = new ArrayList<TreeContentNodeModel>();

        for (ContentKey result : orderedKeys) {
            TreeContentNodeModel nodeModel = treeNodeService.createTreeNode(result, null);
            results.add(nodeModel);
        }

        return results;
    }

    private List<ContentKey> sortResults(List<ContentKey> keys) {
        Comparator<ContentKey> displayOrderComparator = new ContentKeyDisplayOrderComparator(labelProviderService.labelsOf(keys));
        Collections.sort(keys, displayOrderComparator);
        return keys;
    }

}
