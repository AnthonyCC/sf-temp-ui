package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;

public abstract class BaseContentKeyRecommendationService extends AbstractRecommendationService {
    /**
     * Recommend a list of {@link ContentKey}s.
     * 
     * 
     * @param input
     *            session information.
     * @param max
     *            the maximum number of recommendations to be produced
     * @return a List<{@link ContentKey}> of recommendations, expected to
     *         be sorted by relevance
     */
    abstract public List recommend(SessionInput input);

    public BaseContentKeyRecommendationService(Variant variant) {
        super(variant);
    }

    public List recommendNodes(Trigger trigger, SessionInput input) {
        List contentKeys = recommend(input);
        // remove duplicates and turn content keys into content nodes
        Set seen = new HashSet();

        List contentModels = new ArrayList(contentKeys.size());
        for (Iterator i = contentKeys.iterator(); i.hasNext();) {
            ContentKey contentKey = (ContentKey) i.next();

            // get product
            ProductModel prdModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(contentKey);

            if (seen.contains(prdModel.getContentKey()))
                continue;
            seen.add(prdModel.getContentKey());

            contentModels.add(prdModel);
        }

        return contentModels;
    }

}
