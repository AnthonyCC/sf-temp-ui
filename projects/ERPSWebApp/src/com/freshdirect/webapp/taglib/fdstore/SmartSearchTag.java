package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.http.HttpHeaders;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.ContentSearchUtil;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSearchPage;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.SortIntValueComparator;
import com.freshdirect.fdstore.content.SortLongValueComparator;
import com.freshdirect.fdstore.content.SortValueComparator;
import com.freshdirect.fdstore.content.util.SmartSearchUtils;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.util.ProductPagerNavigator;
import com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.search.SearchService;
import com.freshdirect.webapp.util.RequestUtil;

/**
 * @author zsombor, csongor
 * 
 */
public class SmartSearchTag extends AbstractProductPagerTag {
	private static final long serialVersionUID = 3093054384959548572L;

	public static class TagEI extends TagExtraInfo {
		@Override
        public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { new VariableInfo(data.getAttributeString("id"), SmartSearchTag.class.getName(), true,
					VariableInfo.NESTED) };
		}
	}

	private List<Recipe> recipes = Collections.emptyList();
	private int noOfRecipes;
	private SortedMap<DomainValue, Set<Recipe>> classificationsMap;
	private Collection<String> spellingSuggestions;
	private String searchTerm;

	public SmartSearchTag() {
		super();
	}

	/**
	 * This is the mock constructor
	 * 
	 * @param nav
	 * @param customerId
	 */
	public SmartSearchTag(ProductPagerNavigator nav, String customerId) {
		super(nav, customerId);
	}

	@Override
	protected SearchResults getResults() {
		FDUserI user = getFDUser();
		String userId = user != null && user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : null;

		if (nav.getUpc() != null) {
			searchTerm = nav.getUpc();
			return ContentSearch.getInstance().searchUpc(userId, nav.getUpc());
		} else {
			searchTerm = nav.getSearchTerm();
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            return SearchService.getInstance().searchProducts(nav.getSearchTerm(), request.getCookies(), user, RequestUtil.getFullRequestUrl(request),
                    request.getHeader(HttpHeaders.REFERER));
		}
	}

	@Override
	protected Comparator<FilteringSortingItem<ProductModel>> getProductSorter(List<FilteringSortingItem<ProductModel>> products, SearchSortType sortBy, boolean ascending) {
		ComparatorChain<FilteringSortingItem<ProductModel>> comparator;
		switch (sortBy) {
			case DEFAULT:
				return null;
			case BY_NAME:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				break;
			case BY_PRICE:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(ProductModel.GENERIC_PRICE_COMPARATOR));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_POPULARITY:
				comparator = ComparatorChain.create(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createGlobalComparator(getUserId(), getPricingContext())));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_SALE:
				SmartSearchUtils.collectSaleInfo(products, getPricingContext());
				comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.DEAL));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_RELEVANCY:
				String suggestedTerm = getProcessedResults().getSuggestedTerm();
				if (suggestedTerm == null)
					suggestedTerm = searchTerm;
				if(FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdintegrationblackhole2016, ((HttpServletRequest) pageContext.getRequest()).getCookies(), getFDUser())){
		            //for unbxd search results the relevancy is the order of in which the products are returned
		            for(int i = products.size(); i > 0; i--){
		                products.get(products.size() - i).putSortingValue(EnumSortingValue.TERM_SCORE, i);
		            }
		        } else {
		            // if there's only one DYM then we display products for that DYM
		            // but for those products we have to use the suggested term to produce the following scores
		            SmartSearchUtils.collectOriginalTermInfo(products, suggestedTerm);
		            SmartSearchUtils.collectRelevancyCategoryScores(products, suggestedTerm);
		            SmartSearchUtils.collectTermScores(products, suggestedTerm);
		        }
				comparator = ComparatorChain.create(new SortValueComparator<ProductModel>(EnumSortingValue.PHRASE));
				comparator.chain(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createUserComparator(getUserId(), getPricingContext())));
				comparator.chain(new SortIntValueComparator<ProductModel>(EnumSortingValue.ORIGINAL_TERM));
				comparator.chain(new SortValueComparator<ProductModel>(EnumSortingValue.CATEGORY_RELEVANCY));
				comparator.chain(new SortLongValueComparator<ProductModel>(EnumSortingValue.TERM_SCORE));
				comparator.chain(FilteringSortingItem.wrap(ScriptedContentNodeComparator.createGlobalComparator(getUserId(), getPricingContext())));
				if (!ascending)
					comparator = ComparatorChain.reverseOrder(comparator);
				comparator.chain(FilteringSortingItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			default:
				return null;
		}
		SmartSearchUtils.collectAvailabilityInfo(products, getPricingContext());
		comparator.prepend(new SortValueComparator<ProductModel>(EnumSortingValue.AVAILABILITY));
		return comparator;
	}
	
	@Override
	protected void postProcess(SearchResults results) {

		if (FDStoreProperties.isFavouritesTopNumberFilterSwitchedOn() && nav.getSortBy().equals(SearchSortType.BY_RELEVANCY)) {
			List<FilteringSortingItem<ProductModel>> products = FilteringComparatorUtil.reOrganizeFavourites(results.getProducts(), getUserId(), getPricingContext());
			results = new SearchResults(products, results.getRecipes(), results.getCategories(), searchTerm, ContentSearchUtil.isQuoted(searchTerm));
		}

		
		if (nav.isFromDym())
			results.setSpellingSuggestions(Collections.<String>emptyList());

		noOfRecipes = results.getRecipes().size();
		
		// build classifications tree (map)
		buildRecipeClassificationsTree(results);

		// filter recipes by classification
		filterRecipesByClassification(results);

		// prepare recipes list
		createRecipeList(results);

		// sort recipes
		sortRecipes();
		
		spellingSuggestions = results.getSpellingSuggestions();
	}

	private void buildRecipeClassificationsTree(SearchResults results) {
		classificationsMap = new TreeMap<DomainValue, Set<Recipe>>(DomainValue.SORT_BY_LABEL);
		RecipeSearchPage recipeSearchPage = RecipeSearchPage.getDefault();
		List<Domain> classificationDomains = recipeSearchPage.getFilterByDomains();
		for (FilteringSortingItem<Recipe> item : results.getRecipes()) {
			List<DomainValue> classifications = item.getModel().getClassifications();
			for (DomainValue classification : classifications)
				if (classificationDomains.contains(classification.getDomain())) {
					Set<Recipe> recipes = classificationsMap.get(classification);
					if (recipes == null)
						classificationsMap.put(classification, recipes = new TreeSet<Recipe>(Recipe.SORT_BY_NAME));
					recipes.add(item.getModel());
				}
		}
	}

	private void filterRecipesByClassification(SearchResults results) {
		if (results.getRecipes().isEmpty())
			return;
		
		if (nav.isProductsFiltered()) {
			results.emptyRecipes();
			return;
		}

		String classificationId = nav.getRecipeFilter();
		if (classificationId != null) {
			Iterator<FilteringSortingItem<Recipe>> it = results.getRecipes().iterator();
			OUTER: while (it.hasNext()) {
				FilteringSortingItem<Recipe> item = it.next();
				List<DomainValue> classifications = item.getModel().getClassifications();
				for (DomainValue classification : classifications)
					if (classification.getContentKey().getId().equals(classificationId))
						continue OUTER;
				it.remove();
			}
		}
	}

	public void createRecipeList(SearchResults results) {
		List<FilteringSortingItem<Recipe>> items = results.getRecipes();
		recipes = new ArrayList<Recipe>(items.size());
		for (FilteringSortingItem<Recipe> item : items)
			recipes.add(item.getModel());
	}

	public void sortRecipes() {
		Collections.sort(recipes, Recipe.SORT_BY_NAME);
		if (!nav.isSortOrderingAscending())
			Collections.reverse(recipes);
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public int getNoOfRecipes() {
		return noOfRecipes;
	}

	public boolean hasRecipes() {
		return !recipes.isEmpty();
	}

	public boolean isShowOnlyRecipes() {
		return !hasProducts() && hasRecipes();
	}

	public SortedMap<DomainValue, Set<Recipe>> getRecipeClassificationsMap() {
		return classificationsMap;
	}

	public boolean hasNoResults() {
		return !hasProducts() && !hasRecipes();
	}

	public Collection<String> getSpellingSuggestions() {
		return spellingSuggestions;
	}
}
