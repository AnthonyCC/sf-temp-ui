package com.freshdirect.smartstore.sorting;

import java.util.List;

import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FilteredSearchResults.CategoryScoreOracle;
import com.freshdirect.smartstore.scoring.Score;

public class UserRelevancyComparator extends RelevancyComparator {

    final ScriptedContentNodeComparator userComparator;

    public UserRelevancyComparator(boolean inverse, String searchTerm, String userId, CategoryScoreOracle oracle, CategoryNodeTree cnt, List<ContentNodeModel> products, String originalSearchTerm) {
        super(inverse, searchTerm, oracle, cnt, products, originalSearchTerm);
        this.userComparator = ScriptedContentNodeComparator.createUserComparator(userId);
    }

    @Override
    protected int compareInsideCategoryGroup(ContentNodeModel c1, ContentNodeModel c2) {
        int x = userComparator.compare(c1, c2);
        if (x != 0) {
            return x;
        }
        return compareProductsByGlobalScore(c1, c2);
    }
    
    
    public Score getPersonalScore(ContentNodeModel c) {
        return userComparator.getScore(c);
    }
}
