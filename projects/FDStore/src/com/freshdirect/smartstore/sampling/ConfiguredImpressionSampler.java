/**
 * 
 */
package com.freshdirect.smartstore.sampling;

import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;


public class ConfiguredImpressionSampler implements ImpressionSampler {
    private ContentSampler.ConsiderationLimit cl;
    private ListSampler listSampler;

    public ConfiguredImpressionSampler(ContentSampler.ConsiderationLimit cl, ListSampler listSampler) {
        this.cl = cl;
        this.listSampler = listSampler;
    }

    public List<RankedContent.Single> sample(List<RankedContent> sortedRankedContent, Set<ContentKey> reserved, int k) {
        return ContentSampler.drawWithoutReplacement(sortedRankedContent, reserved, cl, k, listSampler);
    }

    public String toString() {
        return "limit:" + cl + ",list:" + listSampler;
    }
    
    public boolean isDeterministic() {
    	return listSampler.isDeterministic();
    }
}
