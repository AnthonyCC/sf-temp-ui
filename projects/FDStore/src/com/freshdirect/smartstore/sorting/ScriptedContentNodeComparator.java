/**
 * 
 */
package com.freshdirect.smartstore.sorting;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.scoring.DataAccess;
import com.freshdirect.smartstore.scoring.Score;
import com.freshdirect.smartstore.scoring.ScoringAlgorithm;
import com.freshdirect.smartstore.service.SearchScoringRegistry;

class ScriptedContentNodeComparator implements Comparator<ContentNodeModel> {
    final DataAccess dataAccess;
    final String userId;
    final ScoringAlgorithm algorithm;

    private String[] variables;
    private Map<ContentKey, Score> cache = new HashMap<ContentKey, Score>();

    ScriptedContentNodeComparator(DataAccess dataAccess, String userId, ScoringAlgorithm algorithm) {
        super();
        this.dataAccess = dataAccess;
        this.userId = userId;
        this.algorithm = algorithm;
        this.variables = algorithm.getVariableNames();
    }

    Score getScore(ContentNodeModel contentNode) {
        Score score = cache.get(contentNode.getContentKey());
        if (score == null) {
            double[] vars = dataAccess.getVariables(userId, contentNode, variables);
            score = new Score(contentNode, algorithm.getScores(vars));
            cache.put(contentNode.getContentKey(), score);
        }
        return score;
    }
    
    @Override
    public int compare(ContentNodeModel o1, ContentNodeModel o2) {
        Score s1 = getScore(o1);
        Score s2 = getScore(o2);
        return s1.compareTo(s2);
    }
    
    /**
     * Return a global popularity based comparator, where the scoring functions comes from FDStoreProperties.
     *  
     */
    public static ScriptedContentNodeComparator createGlobalComparator() {
       return new ScriptedContentNodeComparator(ScoreProvider.getInstance(), null, SearchScoringRegistry.getInstance().getGlobalScoringAlgorithm());
    }

    /**
     * Return an user specific comparator based on a custom scoring functions, which is defined by FDStoreProperties.
     * @param userId
     * @return
     */
    public static ScriptedContentNodeComparator createUserComparator(String userId) {
        return new ScriptedContentNodeComparator(ScoreProvider.getInstance(), userId, SearchScoringRegistry.getInstance().getUserScoringAlgorithm());
    }    

}