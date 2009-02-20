/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.Map;

import com.freshdirect.smartstore.fdstore.ScoreProvider;

public abstract class ProductStatisticUserProviderAspect extends ScoreProvider {
    
    public ProductStatisticUserProviderAspect() {
        super(false);
    }
    /**
     * 
     * @return Map<ContentKey,Float>
     */
    public abstract Map getUserProductScores(String userId);
    
}