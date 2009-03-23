package com.freshdirect.smartstore.scoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.content.ContentNodeModel;

/**
 * Implement sorting functionality which support zeroing out all non-maximum values.
 * It is used in the following scoring function: 'Recency:top,GlobalPopularity' which causes the top recent product sorted to the first position, 
 * and the rest sorted by GlobalPopularity.
 * 
 * @author zsombor
 *
 */
public class TopLimitOrderingFunction extends OrderingFunction {
    
    int position;
    ContentNodeModel maximum;
    double maxValue = Double.NEGATIVE_INFINITY;
    List nodes = new ArrayList();
    
    public TopLimitOrderingFunction() {
        position  = 0;
    }
    
    public TopLimitOrderingFunction(int pos) {
        this.position = pos;
    }
    
    public void addScore(ContentNodeModel contentNode, double[] score) {
        nodes.add(new Score(contentNode, score));
        if (score[position]>maxValue) {
            maximum = contentNode;
            maxValue = score[position];
        }
    }
    
    public List getRankedContents() {
        for (Iterator iter=this.nodes.iterator();iter.hasNext();) {
            Score sc = (Score) iter.next();
            if (sc.node!=maximum) {
                sc.values[position] = 0;
            }
            scores.add(sc);
        }
        return super.getRankedContents();
    }
    

}
