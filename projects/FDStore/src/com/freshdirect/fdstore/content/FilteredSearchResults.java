/**
 * 
 */
package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.util.RecipesUtil;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;

/**
 * @author zsombor
 * 
 */
public class FilteredSearchResults extends SearchResults implements Serializable {

    final static Logger LOG = Logger.getLogger(FilteredSearchResults.class);

    static class PopularityComparator implements Comparator {

        final boolean inverse;
        final boolean hideUnavailable;
        final Set     displayable;

        PopularityComparator(boolean inverse, List products) {
            this(inverse, false, products);
        }

        PopularityComparator(boolean inverse, boolean hideUnavailable, List products) {
            this.inverse = inverse;
            this.hideUnavailable = hideUnavailable;
            this.displayable = new HashSet();
            if (products != null) {
                for (int i = 0; i < products.size(); i++) {
                    ContentNodeModel c = (ContentNodeModel) products.get(i);
                    if (c.isDisplayable()) {
                        displayable.add(c.getContentKey());
                    }
                }
            }
        }

        boolean isDisplayable(ContentKey key) {
            return displayable.contains(key);
        }

        public int compare(Object o1, Object o2) {
            ContentNodeModel c1 = (ContentNodeModel) o1;
            ContentNodeModel c2 = (ContentNodeModel) o2;

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
            float globalScore1 = ProductStatisticsProvider.getInstance().getGlobalProductScore(c1.getContentKey());
            float globalScore2 = ProductStatisticsProvider.getInstance().getGlobalProductScore(c2.getContentKey());
            // -1 means that no score found, so it should modify our ranking
            int result = Float.compare(globalScore2, globalScore1);
            if (result == 0) {
                result = c1.getFullName().compareTo(c2.getFullName());
            }
            return result;
        }
    }

    static class RelevancyComparator extends PopularityComparator {
        final Map                 termScores = new HashMap();
        final String[]            terms;
        final String              searchTerm;
        final String              originalSearchTerm;
        final CategoryScoreOracle oracle;
        CategoryNodeTree          cnt;
        final Map                 predefinedScores;

        RelevancyComparator(boolean inverse, String searchTerm, CategoryScoreOracle oracle, CategoryNodeTree cnt, List products, String originalSearchTerm) {
            super(inverse, true, products);
            this.terms = StringUtils.split(searchTerm);
            this.searchTerm = searchTerm.toLowerCase();
            predefinedScores = ContentSearch.getInstance().getSearchRelevancyScores(this.searchTerm);
            this.oracle = oracle;
            this.cnt = cnt;
            this.originalSearchTerm = originalSearchTerm;
        }

        int getTermScore(ContentNodeModel model, int level) {
            if (model == null) {
                return 0;
            }
            Integer score = (Integer) termScores.get(model.getContentKey());
            int baseScore = 0;
            if (score == null) {
                baseScore = oracle.getScoreOf(model, searchTerm, terms);
                LOG.debug("term score of '" + model.getFullName() + "' -> " + baseScore);
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
        public int compare(Object o1, Object o2) {
            ContentNodeModel c1 = (ContentNodeModel) o1;
            ContentNodeModel c2 = (ContentNodeModel) o2;

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
                    return 1;
                }
                if (n1 && !n2) {
                    return -1;
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
                if (element1.getChildCount() != element2.getChildCount()) {
                    return element2.getChildCount() - element1.getChildCount();
                }
            }
            return c1.getFullName().compareTo(c2.getFullName());
        }

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

    static class UserRelevancyComparator extends RelevancyComparator {

        final Map userProductScores;

        UserRelevancyComparator(boolean inverse, String searchTerm, Map userProductScores, CategoryScoreOracle oracle, CategoryNodeTree cnt, List products, String originalSearchTerm) {
            super(inverse, searchTerm, oracle, cnt, products, originalSearchTerm);
            this.userProductScores = userProductScores;
        }

        protected int compareInsideCategoryGroup(ContentNodeModel c1, ContentNodeModel c2) {
            int x = userScore(c1, c2);
            if (x != 0) {
                return x;
            }
            return compareProductsByGlobalScore(c1, c2);
        }

        private int userScore(ContentNodeModel c1, ContentNodeModel c2) {
            Float score1 = (Float) userProductScores.get(c1.getContentKey());
            Float score2 = (Float) userProductScores.get(c2.getContentKey());
            if (score1 != null) {
                if (score2 != null) {
                    return Float.compare(score2.floatValue(), score1.floatValue());
                } else {
                    return -1;
                }
            } else {
                if (score2 != null) {
                    return 1;
                }
            }
            return 0;
        }
    }

    static class InverseComparator implements Comparator {
        Comparator inner;

        public InverseComparator(Comparator inner) {
            this.inner = inner;
        }

        public int compare(Object o1, Object o2) {
            return inner.compare(o2, o1);
        }
    }

    static class RecipeNameComparator implements Comparator {
        boolean reverse = false;

        public RecipeNameComparator(boolean reverse) {
            this.reverse = reverse;
        }

        public int compare(Object o1, Object o2) {
            Recipe r1 = (Recipe) o1;
            Recipe r2 = (Recipe) o2;

            return reverse ? r2.getName().compareTo(r1.getName()) : r1.getName().compareTo(r2.getName());
        }
    }

    public interface CategoryScoreOracle {
        public int getScoreOf(ContentNodeModel model, String fullSearchTerm, String[] searchTerms);
    }

    /**
     * This score oracle gives 10 points for every mention of the search terms
     * in every parent categories, cumulative.
     * 
     * @author zsombor
     * 
     */
    static class DefaultCategoryScoreOracle implements CategoryScoreOracle {

        public int getScoreOf(ContentNodeModel model, String fullSearchTerm, String[] terms) {
            String name = model.getFullName();
            int baseScore = 0;
            if (name != null) {
                name = name.toLowerCase();
                for (int i = 0; i < terms.length; i++) {
                    int position = name.indexOf(terms[i]);
                    if (position != -1 && (position == 0 || !Character.isLetterOrDigit(name.charAt(position - 1)))) {
                        baseScore += 10;
                    }
                }
                if (model.getParentNode() != null) {
                    baseScore += getScoreOf(model.getParentNode(), fullSearchTerm, terms);
                }
            }
            return baseScore;
        }
    }

    /**
     * This score oracle gives 100-(10*depth) points for every mention of the
     * search terms in every parent categories, cumulative.
     * 
     * @author zsombor
     * 
     */
    public static class HierarchicalScoreOracle implements FilteredSearchResults.CategoryScoreOracle {
        final static int[]     DEPTH_SCORES = { 10000, 1000, 100, 10, 1 };
        final CategoryNodeTree tree;

        public HierarchicalScoreOracle(CategoryNodeTree tree) {
            this.tree = tree;
            this.tree.initDepthFields();
        }

        public int getScoreOf(ContentNodeModel model, String fullSearchTerm, String[] terms) {
            TreeElement element = tree.getTreeElement(model);
            if (element != null) {
                String name = model.getFullName();
                int baseScore = 0;
                int depthScore = element.getDepth() < DEPTH_SCORES.length ? DEPTH_SCORES[element.getDepth()] : 0;
                if (model instanceof ProductModel) {
                    depthScore = DEPTH_SCORES[0];
                }
                if (name != null) {
                    name = name.toLowerCase();
                    if (name.equals(fullSearchTerm)) {
                        // full search term match -> maximum boost :)
                        baseScore += DEPTH_SCORES[0];
                    }
                    int nameLength = name.length();
                    int position = name.indexOf(fullSearchTerm);
                    if (position != -1 && (position == 0 || !Character.isLetterOrDigit(name.charAt(position - 1)))) {
                        baseScore += (depthScore * fullSearchTerm.length() / nameLength);
                    } else {
                        for (int i = 0; i < terms.length; i++) {
                            position = name.indexOf(terms[i]);
                            if (position != -1 && (position == 0 || !Character.isLetterOrDigit(name.charAt(position - 1)))) {
                                baseScore += (depthScore * terms[i].length() / nameLength) / 2;
                            }
                        }
                    }

                    if (model instanceof CategoryModel) {
                        String keywords = model.getKeywords();
                        if (keywords != null && keywords.length() > 0) {
                            String[] keywordArray = StringUtils.split(keywords.toLowerCase(), ',');
                            for (int i = 0; i < keywordArray.length; i++) {
                                if (keywordArray[i].trim().equals(fullSearchTerm)) {
                                    baseScore += DEPTH_SCORES[0];
                                }
                            }
                            // boolean x =
                            // (keywords.indexOf(fullSearchTerm)!=-1);
                        }
                    }
                }
                ContentNodeModel parentNode = model.getParentNode();
                if (parentNode != null) {
                    int parentScore = getScoreOf(parentNode, fullSearchTerm, terms);
                    baseScore += parentScore;
                    /*
                     * if (parentScore>0) { return parentScore; }
                     */
                }
                return baseScore;
            }
            return 0;
        }
    }


    /**
     * Sale Sort Comparator
     *   based on Popularity comparator
     * [APPREQ-234]
     * 
     * @author segabor
     */
    public static class SaleComparator extends PopularityComparator {
        SaleComparator(boolean inverse, boolean hideUnavailable, List products) {
            super(inverse, hideUnavailable, products);
        }

        public SaleComparator(boolean inverse, List products) {
            super(inverse, products);
        }

        public int compare(Object o1, Object o2) {
            ProductModel c1 = (ProductModel) o1;
            ProductModel c2 = (ProductModel) o2;

            try {
                // Stage 0 -- sort out null cases
                //
                if (c1.getDefaultSku() == null) {
                    if (c2.getDefaultSku() == null) {
                        return 0;
                    } else {
                        // sku1 == null, sku2 != null -> 1
                        return inverse ? -1 : 1;
                    }
                } else if (c2.getDefaultSku() == null) {
                    // sku1 != null, sku2 == null -> -1
                    return inverse ? 1 : -1;
                }

                FDProductInfo i1 = c1.getDefaultSku().getProductInfo();
                FDProductInfo i2 = c2.getDefaultSku().getProductInfo();

                // Stage 1 -- Compare deal percentages
                //
                if (i1.isDeal()) {
                    if (i2.isDeal()) {
                        int p1 = i1.getDealPercentage();
                        int p2 = i2.getDealPercentage();

                        // greater percentage is better
                        int sc = p1 > p2 ? -1 : (p1 < p2 ? 1 : 0);

                        // same prices -> sort by popularity
                        if (sc == 0)
                            return super.compare(o1, o2);
                        return inverse ? -sc : sc;
                    } else {
                        return inverse ? 1 : -1;
                    }
                } else if (i2.isDeal()) {
                    return inverse ? -1 : 1;
                }

                // Stage 2 -- tiered pricing
                //
                Pricing prc1 = c1.getDefaultSku().getProduct().getPricing();
                Pricing prc2 = c2.getDefaultSku().getProduct().getPricing();
                double defp1 = i1.getBasePrice();
                double defp2 = i2.getBasePrice();

                if (prc1.hasScales()) {
                    if (prc2.hasScales()) {
                        // find best scaled price (if any)
                        double mp1 = prc1.getMinPrice();
                        double mp2 = prc2.getMinPrice();

                        if (mp1 != Double.NaN && mp2 != Double.NaN) {
                            int sc = Double.compare(mp1, mp2);

                            // same prices -> sort by popularity
                            if (sc == 0)
                                return super.compare(o1, o2);
                            return inverse ? -sc : sc;
                        } else {
                            int sc = (mp1 == Double.NaN ? 1 : -1);
                            return inverse ? -sc : sc;
                        }
                    } else {
                        return inverse ? 1 : -1;
                    }
                } else if (prc2.hasScales()) {
                    return inverse ? -1 : 1;
                }

                // Stage 3 -- base prices
                //
                if (i1.getBasePrice() != i2.getBasePrice()) {

                    // cheaper price is better
                    int sc = Double.compare(defp1, defp2);
                    if (sc == 0)
                        return super.compare(o1, o2);
                    return inverse ? -sc : sc;
                }

                // Stage 4 -- equal prices --> compare by popularity
                //
                return super.compare(o1, o2);
            } catch (FDSkuNotFoundException noSkuExc) {
                return 0;
            } catch (FDResourceException se) {
                return 0;
            }
        }

    }
    
    
    public final static int        NATURAL_SORT      = -1;                        // don't
                                                                                   // sort
    public final static int        BY_NAME           = 0;
    public final static int        BY_PRICE          = 1;
    public final static int        BY_RELEVANCY      = 2;
    public final static int        BY_POPULARITY     = 3;
	public static final int        BY_SALE           = 5;

    /*private final static Comparator POPULARITY                      = new PopularityComparator(false, false);
    private final static Comparator POPULARITY_INVERSE              = new PopularityComparator(true, false);*/
/*    private final static Comparator POPULARITY_HIDE_UNAVAIL         = new PopularityComparator(false, true);
    private final static Comparator POPULARITY_HIDE_UNAVAIL_INVERSE = new PopularityComparator(true, true);*/

    /**
	 * 
	 */
    private static final long      serialVersionUID  = 1L;

    String                         customerId;
    int                            start             = 0;
    


    // number of items per page; 0 value means show all products (do not page)
    int                            pageSize          = 30;
    List                           postFilteredProducts;                          // List<FDProductSelectionI>

    
    Map                            userProductScores = null;

    Comparator                     currentComparator;

    CategoryScoreOracle            scoreOracle;
    CategoryNodeTree               nodeTree;

    String                         recipeFilter;
    List                           _filteredRecipes;
    boolean                        reversed = false; // reversed sort of recipes
    protected String originalSearchTerm;
    
    /**
     *
     * @param searchTerm the modified search term (blueberries -> blueberry)
     * @param original
     * @param customerId
     *            the customer id
     * @param originalSearchTerm the original search term (blueberries)
     */
    public FilteredSearchResults(String searchTerm, SearchResults original, String customerId, String originalSearchTerm) {
        super(original.getProducts(), original.getRecipes(), original.isProductsRelevant(), searchTerm);
        setSpellingSuggestion(original.getSpellingSuggestion(), original.isSuggestionMoreRelevant());
        this.customerId = customerId;
        this.originalSearchTerm = originalSearchTerm;
    }

    public FilteredSearchResults(String searchTerm, SearchResults original, String customerId) {
        this(searchTerm, original, customerId, searchTerm);
    }

    public void sortProductsBy(Integer code, boolean inverse) {
        List products = getProducts();
        currentComparator = getComparator(code, inverse, products);
        Collections.sort(products, currentComparator);
    }

    public void setScoreOracle(CategoryScoreOracle scoreOracle) {
        this.scoreOracle = scoreOracle;
    }

    public void setNodeTree(CategoryNodeTree nodeTree) {
        this.nodeTree = nodeTree;
    }

    public void setRecipeFilter(String rcpFilter, boolean reversed) {
        this.recipeFilter = rcpFilter;
        this.reversed = reversed;
        this._filteredRecipes = null; // clear filtered recipes cache
    }

    public Comparator getCurrentComparator() {
        return currentComparator;
    }

    private Comparator getComparator(Integer code, boolean inverse, List products) {
        if (code != null) {
            int sort = code.intValue();
            return getComparator(sort, inverse, products);
        } else {
            return getComparator(BY_RELEVANCY, inverse, products);
        }
    }

    private Comparator getComparator(int sort, boolean inverse, List products) {
        Comparator c = null;
        switch (sort) {
            case BY_NAME:
                c = ContentNodeModel.FULL_NAME_WITH_ID_COMPARATOR;
                if (inverse) {
                    c = new InverseComparator(c);
                }
                break;
            case BY_PRICE:
                c = inverse ? ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE : ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR;
                break;
            case BY_POPULARITY:
                c = new PopularityComparator(inverse, products);
                break;
            case BY_SALE:
            	c = new SaleComparator(inverse, products);
            	break;
            case BY_RELEVANCY:
//                return (customerId != null) ? new RelevancyComparator(customerId) : POPULARITY;
            default:
                CategoryScoreOracle sc = scoreOracle != null ? scoreOracle : new DefaultCategoryScoreOracle();
                c = (customerId != null) ? new UserRelevancyComparator(inverse, searchTerm, getUserProductScores(), sc, nodeTree, products, originalSearchTerm) : new RelevancyComparator(inverse, searchTerm,
                        sc, nodeTree, products, originalSearchTerm);
        }
        return c;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public Map getUserProductScores() {
        if (userProductScores == null) {
            userProductScores = ProductStatisticsProvider.getInstance().getUserProductScores(customerId);
        }
        return userProductScores;
    }

    // number of items per page
    public int getPageSize() {
        return pageSize > 0 ? pageSize : this.postFilteredProducts.size();
    }

    // Must list all result
    public boolean isAll() {
        return pageSize == 0;
    }

    // count of pages
    public int getPageCount() {
        int size = postFilteredProducts.size();
        return pageSize == 0 ? 1 : (size / pageSize) + (size % pageSize == 0 ? 0 : 1);
    }

    public final int getProductsSize() {
        return postFilteredProducts.size();
    }

    public int getCurrentPage() {
        return pageSize == 0 ? 0 : start / pageSize;
    }

    /**
     * recalculate the filtered product list.
     * 
     * @param filter
     */
    public void setFilter(ProductFilterI filter) {
        if (filter != null) {
            try {
                this.postFilteredProducts = filter.apply(getProducts());
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            }
        } else {
            this.postFilteredProducts = getProducts();
        }
    }

    public void setFilteredProducts(List postFilteredProducts) {
        this.postFilteredProducts = postFilteredProducts;
    }

    public List getFilteredProducts() {
        return postFilteredProducts;
    }

    public List getFilteredProductsListPage() {
        return postFilteredProducts.subList(getStart(), Math.min(getStart() + getPageSize(), postFilteredProducts.size()));
    }

    public List getFilteredRecipes() {
        if (_filteredRecipes == null) {
            if (recipeFilter != null) {
                // filter recipes
                //
                _filteredRecipes = new ArrayList();
                for (Iterator it = getRecipes().iterator(); it.hasNext();) {
                    Recipe r = (Recipe) it.next();

                    for (Iterator j = r.getClassifications().iterator(); j.hasNext();) {
                        DomainValue dv = (DomainValue) j.next();

                        if (recipeFilter.equalsIgnoreCase(dv.getContentKey().getId())) {
                            _filteredRecipes.add(r);
                            break;
                        }
                    }
                }
                Collections.sort(_filteredRecipes, new RecipeNameComparator(reversed));
            } else {
                _filteredRecipes = getRecipes();
                Collections.sort(_filteredRecipes, new RecipeNameComparator(reversed));
            }
        }

        return _filteredRecipes;
    }

    // Returns Map<DomainValue,Integer> of classification-number of recipes
    // couples
    public Map getRecipeCounts() {
        Map stat = new HashMap();
        List recipes = getRecipes();

        for (Iterator it = getRecipeClassifications().iterator(); it.hasNext();) {
            DomainValue dv = (DomainValue) it.next();
            int k = 0;

            for (Iterator it1 = recipes.iterator(); it1.hasNext();) {
                Recipe r = (Recipe) it1.next();

                if (r.getClassifications().contains(dv)) {
                    ++k;
                }
            }
            stat.put(dv, new Integer(k));
        }
        return stat;
    }

    public List getRecipeClassifications() {
        RecipeSearchPage searchPage = RecipeSearchPage.getDefault();
        if (searchPage == null) {
            LOG.warn("RecipeSearchPage.getDefault() is null!");
            return Collections.EMPTY_LIST;
        }
        return RecipesUtil.collectClassifications(java.util.Collections.EMPTY_SET, new HashSet(searchPage.getFilterByDomains()), getRecipes());
    }
}
