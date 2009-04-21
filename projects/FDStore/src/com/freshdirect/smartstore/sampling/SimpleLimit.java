/**
 * 
 */
package com.freshdirect.smartstore.sampling;

import java.util.List;


/**
 * Defines the highest limit of either topN or topP
 *
 * @author unknown
 */
public final class SimpleLimit implements ContentSampler.ConsiderationLimit {
    private final double topP;
    private final int    topN;

    public SimpleLimit(double topP, int topN) {
        this.topP = topP;
        this.topN = topN;
    }

    public int max(List rankedItems) {
        return Math.max((int) ((topP * rankedItems.size()) / 100.0), topN);
    }

    public String toString() {
        return "topN:" + topN + ",percent:" + topP;
    }
}