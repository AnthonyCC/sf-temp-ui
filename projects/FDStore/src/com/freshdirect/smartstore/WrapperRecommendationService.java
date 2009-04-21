package com.freshdirect.smartstore;

import java.util.List;

public abstract class WrapperRecommendationService implements RecommendationService {

    protected RecommendationService internal;

    public WrapperRecommendationService(RecommendationService internal) {
        this.internal = internal;
    }
    
    public String getDescription() {
        return "Wrapper["+this.getClass().getName()+"]:"+internal.getDescription();
    }

    public Variant getVariant() {
        return internal.getVariant();
    }
    
    public void setInternalRecommendationService(RecommendationService internal) {
        this.internal = internal;
    }

    public abstract List recommendNodes(SessionInput input);

}
