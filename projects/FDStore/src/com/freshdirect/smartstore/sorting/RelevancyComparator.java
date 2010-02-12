package com.freshdirect.smartstore.sorting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.content.FilteredSearchResults.CategoryScoreOracle;

public class RelevancyComparator extends PopularityComparator {
    
    
    
    final Map<ContentKey,Integer>	termScores = new HashMap<ContentKey,Integer>();
    final String[]                      terms;
    final String                        searchTerm;
    final String                        originalSearchTerm;
    final CategoryScoreOracle           oracle;
    CategoryNodeTree                    cnt;
    final Map<ContentKey,Integer>       predefinedScores;

    
//    Map<ContentKey,Integer> categoryScores  = new HashMap();
    
    public RelevancyComparator(boolean inverse, String searchTerm, PricingContext pricingContext,
    		CategoryScoreOracle oracle, CategoryNodeTree cnt, List<ContentNodeModel> products, String originalSearchTerm) {
        super(inverse, true, products, pricingContext);
        this.terms = StringUtils.split(searchTerm);
        this.searchTerm = searchTerm.toLowerCase();
        predefinedScores = ContentSearch.getInstance().getSearchRelevancyScores(this.searchTerm);
        this.oracle = oracle;
        this.cnt = cnt;
        this.originalSearchTerm = originalSearchTerm;
    }
    
    public int getTermScore(ProductModel product) {
        return getTermScore(product.getParentNode(), 1);
    }

    int getTermScore(ContentNodeModel model, int level) {
        if (model == null) {
            return 0;
        }
        Integer score = (Integer) termScores.get(model.getContentKey());
        int baseScore = 0;
        if (score == null) {
            baseScore = oracle.getScoreOf(model, searchTerm, terms);
            // LOG.debug("term score of '" + model.getFullName() + "' -> " + baseScore);
            termScores.put(model.getContentKey(), new Integer(baseScore));
        } else {
            baseScore = score.intValue();
        }
        return baseScore;
    }

    /**
     * compare two product model, based on their pre-defined parent category
     * score
     * 
     * @param c1
     * @param c2
     * @return
     */
    int compareByPredefinedScores(ContentNodeModel c1, ContentNodeModel c2) {
        if (predefinedScores != null) {
            Integer s1 = (Integer) predefinedScores.get(c1.getParentNode().getContentKey());
            Integer s2 = (Integer) predefinedScores.get(c2.getParentNode().getContentKey());
            if (s1 != null) {
                int i1 = s1.intValue();
                if (s2 != null) {
                    int i2 = s2.intValue();
                    if (i1 != i2) {
                        return i2 - i1;
                    }
                    // mix the two category with the same score ...
                    return compareInsideCategoryGroup(c1, c2);
                } else {
                    return -1;
                }
            } else {
                if (s2 != null) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Compares its two arguments for order. Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.
     * <p>
     */
    @Override
    public int compare(ContentNodeModel c1, ContentNodeModel c2) {
        {
            boolean h1 = isDisplayable(c1.getContentKey());
            boolean h2 = isDisplayable(c2.getContentKey());
            if (!h1 && h2) {
                return 1;
            }

            if (h1 && !h2) {
                return -1;
            }
        }
        {
            boolean n1 = originalSearchTerm.equals(c1.getFullName().toLowerCase());
            boolean n2 = originalSearchTerm.equals(c2.getFullName().toLowerCase());
            if (!n1 && n2) {
                return inverse ? -1 : 1;
            }
            if (n1 && !n2) {
                return inverse ? 1 : -1;
            }
        }

        int sc = compareContentNodes(c1, c2);
        if (inverse) {
            return -sc;
        } else {
            return sc;
        }
    }

    protected int compareCategory(ContentNodeModel c1, ContentNodeModel c2) {
        TreeElement element1 = this.cnt.getTreeElement(c1);
        TreeElement element2 = this.cnt.getTreeElement(c2);
        if (element1 != null && element2 != null) {
//            logInfo(element1);
//            logInfo(element2);
            
            if (element1.getChildCount() != element2.getChildCount()) {
                return element2.getChildCount() - element1.getChildCount();
            }
        }
        return c1.getFullName().compareTo(c2.getFullName());
    }

//    private void logInfo(TreeElement element1) {
//        if (!categoryScores.containsKey(element1.getModel().getContentKey())) {
//            int count = element1.getChildCount();
//            System.out.println("child "+count+" "+element1.getModel());
//            categoryScores.put(element1.getModel().getContentKey(), count);
//        }
//    }

    /**
     * Compare two product model.
     * 
     * @param c1
     * @param c2
     * @return
     */
    protected final int compareContentNodes(ContentNodeModel c1, ContentNodeModel c2) {
        // first compare by the predefined CMS scores ...
        int x = compareByPredefinedScores(c1, c2);
        if (x != 0) {
            return x;
        }
        // after by the magical algorithm
        int termScore1 = getTermScore(c1.getParentNode(), 1);
        int termScore2 = getTermScore(c2.getParentNode(), 1);

        if (termScore1 != termScore2) {
            return (termScore2 - termScore1);
        }
        x = compareCategory(c1.getParentNode(), c2.getParentNode());
        if (x != 0) {
            return x;
        } else {
            return compareInsideCategoryGroup(c1, c2);
        }
    }

    protected int compareInsideCategoryGroup(ContentNodeModel c1, ContentNodeModel c2) {
        return compareProductsByGlobalScore(c1, c2);
    }
}
