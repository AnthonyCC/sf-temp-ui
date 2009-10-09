/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.smartstore.fdstore.ScoreProvider;

public abstract class ProductStatisticUserProviderAspect extends ScoreProvider {

    public ProductStatisticUserProviderAspect() {
        super(false);
    }

    public ProductStatisticUserProviderAspect addPersonalizedFactors(String... names) {
        for (int i = 0; i < names.length; i++) {
            personalizedIndexes.put(names[i], i);
        }
        return this;
    }

    public ProductStatisticUserProviderAspect addGlobalFactors(String... names) {
        for (int i = 0; i < names.length; i++) {
            globalIndexes.put(names[i], i);
        }
        return this;
    }
    
    /**
     * 
     * @return Map<ContentKey,Float>
     */
    // public abstract Map getUserProductScores(String userId);

    @Override
    public synchronized List acquireFactors(Collection names) {
        return new ArrayList(names);
    }
}