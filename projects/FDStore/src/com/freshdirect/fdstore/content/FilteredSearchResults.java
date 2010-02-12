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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.RecipesUtil;
import com.freshdirect.smartstore.scoring.Score;
import com.freshdirect.smartstore.sorting.PopularityComparator;
import com.freshdirect.smartstore.sorting.RelevancyComparator;
import com.freshdirect.smartstore.sorting.SaleComparator;
import com.freshdirect.smartstore.sorting.UserRelevancyComparator;

/**
 * @author zsombor
 * 
 */
public class FilteredSearchResults extends SearchResults implements Serializable {

    final static Logger LOG = Logger.getLogger(FilteredSearchResults.class);




    static class InverseComparator implements Comparator<ContentNodeModel> {
        Comparator<ContentNodeModel> inner;

        public InverseComparator(Comparator<ContentNodeModel> inner) {
            this.inner = inner;
        }

        public int compare(ContentNodeModel o1, ContentNodeModel o2) {
            return inner.compare(o2, o1);
        }
    }

    static class RecipeNameComparator implements Comparator<Recipe> {
        boolean reverse = false;

        public RecipeNameComparator(boolean reverse) {
            this.reverse = reverse;
        }

        public int compare(Recipe r1, Recipe r2) {
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


    
    



	private static final long      serialVersionUID  = 1L;

    String                         customerId;
    PricingContext                 pricingContext;
    int                            start             = 0;
    


    // number of items per page; 0 value means show all products (do not page)
    int                            pageSize          = 30;
    List                           postFilteredProducts;                          // List<FDProductSelectionI>

    
    Map<ContentKey,Float>			userProductScores = null;

    Comparator<ContentNodeModel>	currentComparator;

    CategoryScoreOracle            scoreOracle;
    CategoryNodeTree               nodeTree;

    String                         recipeFilter;
    List<Recipe>					_filteredRecipes;
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
    public FilteredSearchResults(String searchTerm, SearchResults original, String customerId, String originalSearchTerm,  PricingContext pCtx) {
    	//Convert Products to ProductModelPricingAdapter for Zone Pricing.
        super(convertToPricingAdapter(original.getProducts(), pCtx), original.getRecipes(), original.isProductsRelevant(), searchTerm);
        setSpellingSuggestion(original.getSpellingSuggestion(), original.isSuggestionMoreRelevant());
        this.customerId = customerId;
        this.originalSearchTerm = originalSearchTerm;
        this.pricingContext = pCtx;
    }

    public FilteredSearchResults(String searchTerm, SearchResults original, String customerId, PricingContext pCtx) {
        this(searchTerm, original, customerId, searchTerm, pCtx);
    }

	private static List convertToPricingAdapter(List products, PricingContext pCtx){
		List adapters = new ArrayList<ProductModel>(products.size());
		ProductPricingFactory factory = ProductPricingFactory.getInstance();
		for(Iterator<ProductModel> it = products.iterator(); it.hasNext();){
			adapters.add(factory.getPricingAdapter(((ProductModel) it.next()), pCtx));
		}
		return adapters;
	}
	
    public void sortProductsBy(SearchSortType code, boolean inverse) {
        List<ContentNodeModel> products = getProducts();
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

    public Comparator<ContentNodeModel> getCurrentComparator() {
        return currentComparator;
    }

    @SuppressWarnings("unchecked")
	private Comparator<ContentNodeModel> getComparator(SearchSortType sort, boolean inverse, List<ContentNodeModel> products) {
        Comparator<ContentNodeModel> c = null;
        
        if (sort == null)
        	sort = SearchSortType.BY_RELEVANCY;

        switch (sort) {
            case BY_NAME:
                c = (Comparator<ContentNodeModel>) ContentNodeModel.FULL_NAME_WITH_ID_COMPARATOR;
                if (inverse) {
                    c = new InverseComparator(c);
                }
                break;
            case BY_PRICE:
                c = inverse ? (Comparator<ContentNodeModel>) ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE : (Comparator<ContentNodeModel>) ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR;
                break;
            case BY_POPULARITY:
                c = new PopularityComparator(inverse, products, pricingContext);
                break;
            case BY_SALE:
            	c = new SaleComparator(inverse, products, pricingContext);
            	break;
            case BY_RELEVANCY:
            default:
                CategoryScoreOracle sc = scoreOracle != null ? scoreOracle : new DefaultCategoryScoreOracle();
                c = (customerId != null) ? new UserRelevancyComparator(inverse, searchTerm, customerId, pricingContext, sc, nodeTree, products, originalSearchTerm) : new RelevancyComparator(inverse, searchTerm,
                        pricingContext, sc, nodeTree, products, originalSearchTerm);
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

    public List<Recipe> getFilteredRecipes() {
        if (_filteredRecipes == null) {
            if (recipeFilter != null) {
                // filter recipes
                //
                _filteredRecipes = new ArrayList<Recipe>();
                
                for (Recipe r : getRecipes()) {
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
    public Map<DomainValue,Integer> getRecipeCounts() {
        Map<DomainValue,Integer> stat = new HashMap<DomainValue,Integer>();
        List<Recipe> recipes = getRecipes();

        for (Iterator it = getRecipeClassifications().iterator(); it.hasNext();) {
            DomainValue dv = (DomainValue) it.next();
            int k = 0;

            for (Recipe r : recipes) {
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
    

    /**
     * Just for testing ...
     * @param model
     * @return
     */
    public int getCategoryScore(ProductModel model) {
        if (currentComparator instanceof RelevancyComparator) {
            return ((RelevancyComparator)currentComparator).getTermScore(model);
        }
        return -1;  
    }

    /**
     * Just for testing ...
     * @param model
     * @return
     */
    public boolean isDisplayable(ProductModel model) {
        if (currentComparator instanceof PopularityComparator) {
            return ((PopularityComparator) currentComparator).isDisplayable(model.getContentKey());
        }
        return true;
    }
    
    public Score getGlobalScore(ProductModel model) {
        if (currentComparator instanceof PopularityComparator) {
            return ((PopularityComparator)currentComparator).getGlobalScore(model);
        }
        return null;
    }
    
    public Score getPersonalScore(ProductModel model) {
        if (currentComparator instanceof UserRelevancyComparator) {
            return ((UserRelevancyComparator)currentComparator).getPersonalScore(model);
        }
        return null;
    }
    
    
}
