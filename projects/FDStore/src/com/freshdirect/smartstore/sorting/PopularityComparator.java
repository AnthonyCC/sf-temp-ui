package com.freshdirect.smartstore.sorting;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.scoring.Score;

public class PopularityComparator implements Comparator<ContentNodeModel> {

    final boolean inverse;
    final boolean hideUnavailable;
    final Set<ContentKey>     displayable;
    final ScriptedContentNodeComparator globalComparator;

    public PopularityComparator(boolean inverse, List<ContentNodeModel> products, PricingContext pricingContext) {
        this(inverse, false, products, pricingContext);
    }

    public PopularityComparator(boolean inverse, boolean hideUnavailable, List<ContentNodeModel> products, PricingContext pricingContext) {
        this.inverse = inverse;
        this.hideUnavailable = hideUnavailable;
        this.displayable = new HashSet<ContentKey>();
        this.globalComparator = ScriptedContentNodeComparator.createComparator(null, pricingContext, true);
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                ContentNodeModel c = (ContentNodeModel) products.get(i);
                if (c.isDisplayable()) {
                    displayable.add(c.getContentKey());
                }
            }
        }
    }

    /**
     * visible only for testing purposes ...
     * @param key
     * @return
     */
    public boolean isDisplayable(ContentKey key) {
        return displayable.contains(key);
    }
    
    /**
     * only for testing purposes ...
     * @param c
     * @return
     */
    public Score getGlobalScore(ContentNodeModel c) {
        return globalComparator.getScore(c);
    }

    public int compare(ContentNodeModel c1, ContentNodeModel c2) {
        if (hideUnavailable) {
            boolean h1 = isDisplayable(c1.getContentKey());
            boolean h2 = isDisplayable(c2.getContentKey());

            if (!h1 && h2) {
                return 1;
            }

            if (h1 && !h2) {
                return -1;
            }
        }

        int sc = compareProductsByGlobalScore(c1, c2);
        if (inverse) {
            sc = -sc;
        }
        return sc;
    }

    int compareProductsByGlobalScore(ContentNodeModel c1, ContentNodeModel c2) {
        int result = globalComparator.compare(c1, c2);
        if (result == 0) {
            result = c1.getFullName().compareTo(c2.getFullName());
        }
        return result;
    }
}
