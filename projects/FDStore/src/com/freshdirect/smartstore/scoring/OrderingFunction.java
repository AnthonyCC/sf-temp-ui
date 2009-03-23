package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.sampling.RankedContent;

public class OrderingFunction {
    // if multiple number is the returning statement, we can't interpret as a probability.
    TreeSet scores = new TreeSet();

    public void addScore(ContentNodeModel contentNode, double[] score) {
        Score sc = new Score(contentNode, score);
        scores.add(sc);
    }
    
    public List getRankedContents() {
        int max = scores.size();
        List rankedContents = new ArrayList(max);
        int i = 0;
        for (Iterator iter = scores.iterator(); iter.hasNext();) {
            Score sc = (Score) iter.next();
            rankedContents.add(new RankedContent.Single(max + 1 - i, sc.getNode()));
            i++;
        }
        return rankedContents;
    }
    
}
