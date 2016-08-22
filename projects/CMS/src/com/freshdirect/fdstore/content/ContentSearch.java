package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.AutoComplete;
import com.freshdirect.cms.search.AutocompleteService;
import com.freshdirect.cms.search.BrandNameWordList;
import com.freshdirect.cms.search.CounterCreatorImpl;
import com.freshdirect.cms.search.ProductNameWordList;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.search.SimpleCounterCreator;
import com.freshdirect.cms.search.SpellingUtils;
import com.freshdirect.cms.search.spell.SpellingCandidate;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.term.ApproximationsPermuter;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ContentSearch {
	private final static Logger LOGGER = LoggerFactory.getInstance(ContentSearch.class);

	private static ContentSearch instance = new ContentSearch();

    AutoComplete productAutocompletion = new AutoComplete(new ProductNameWordList(), new CounterCreatorImpl());
    AutoComplete brandAutocompletion = new AutoComplete(new BrandNameWordList(), new SimpleCounterCreator());

	public static ContentSearch getInstance() {
		return instance;
	}

	public static void setInstance(ContentSearch newInstance) {
		instance = newInstance;
		LOGGER.info("ContentSearch instance replaced");
	}

	Map<String, SearchRelevancyList> searchRelevancyMap;

	double didYouMeanWeight = 0.0;
	double didYouMeanThreshold = 0.0;
	int didYouMeanMaxHits = 0;

	public double getDidYouMeanRatio() {
		if (didYouMeanWeight == 0.0) {
			didYouMeanWeight = FDStoreProperties.getDidYouMeanRatio();
			if (didYouMeanWeight == 0.0)
				didYouMeanWeight = 5.0;
		}
		return didYouMeanWeight;
	}

	public double getDidYouMeanThreshold() {
		if (didYouMeanThreshold == 0.0) {
			didYouMeanThreshold = FDStoreProperties.getDidYouMeanThreshold();
			if (didYouMeanThreshold == 0.0)
				didYouMeanThreshold = 0.6;
		}
		return didYouMeanThreshold;
	}

	public int getDidYouMeanMaxHits() {
		if (didYouMeanMaxHits == 0) {
			didYouMeanMaxHits = FDStoreProperties.getDidYouMeanMaxHits();
			if (didYouMeanMaxHits == 0)
				didYouMeanMaxHits = 20;
		}
		return didYouMeanMaxHits;
	}
	
	public int getSearchMaxHits() {
		return 10000;
	}
	
	/**
	 * Utility method for getting the relevancy score of the nearest parent node which has a valid score. Returns an Integer. Value
	 * is null if no parent has a score set.
	 * 
	 * @param scores
	 *            scores map
	 * @param node
	 *            product node
	 * @return relevancy score
	 */
	public static Integer getRelevancyScore(Map<ContentKey, Integer> scores, ContentNodeModel node) {

		if (node == null || scores == null)
			return null;

		Integer score = null;
		ContentNodeModel parent = null;

		while (score == null) {

			parent = node.getParentNode();
			if (parent == null)
				break;

			score = scores.get(parent.getContentKey());
			node = parent;
		}

		return score;
	}

	/**
	 * Search for a given term. If there're no results (neither product, nor recipes, nor categories) then try to suggest spelling
	 * 
	 * @param searchTerm
	 *            the term to be searched for
	 * @return the search results
	 */
    public SearchResults searchProducts(String searchTerm) {
		searchTerm = searchTerm.trim();
		boolean quoted = ContentSearchUtil.isQuoted(searchTerm);
		if (quoted)
			searchTerm = ContentSearchUtil.removeQuotes(searchTerm);

		SearchResults results = searchProductsInternal(searchTerm, quoted, false);

		suggestSpellingInternal(searchTerm, quoted, results);

		return results;
	}

	/**
	 * We skip spell checking (used for testing purposes only!)
	 * 
	 * @param searchTerm
	 * @return
	 */
	public SearchResults searchProductsNoSpell(String searchTerm) {
		searchTerm = searchTerm.trim();
		boolean exact = ContentSearchUtil.isQuoted(searchTerm);
		if (exact)
			searchTerm = ContentSearchUtil.removeQuotes(searchTerm);

		SearchResults results = searchProductsInternal(searchTerm, exact, false);

		return results;
	}

	public SearchResults searchProductsInternal(String searchTerm, boolean quoted, boolean approximate) {
		if (searchTerm.length() == 0) {
			return new SearchResults();
		}
	
		Collection<SearchHit> phraseHits = CmsManager.getInstance().searchProducts(searchTerm, true, approximate, getSearchMaxHits());
		Collection<SearchHit> nonPhraseHits;
		if (quoted) {
			nonPhraseHits = Collections.<SearchHit> emptyList();
		} else {
			nonPhraseHits = CmsManager.getInstance().searchProducts(searchTerm, false, approximate, getSearchMaxHits());

			if (approximate && !phraseHits.isEmpty() && !nonPhraseHits.isEmpty()) {
				int phraseLevel = phraseHits.iterator().next().getApproximationLevel();
				int nonPhraseLevel = nonPhraseHits.iterator().next().getApproximationLevel();
				if (phraseLevel < nonPhraseLevel)
					nonPhraseHits.clear();
				else if (nonPhraseLevel < phraseLevel)
					phraseHits.clear();
			}

			if (!phraseHits.isEmpty()) {
				Set<ContentKey> contentKeys = ContentSearchUtil.extractContentKeys(phraseHits);
				Iterator<SearchHit> it = nonPhraseHits.iterator();
				while (it.hasNext())
					if (contentKeys.contains(it.next().getContentKey()))
						it.remove();
			}
		}

		Set<FilteringSortingItem<ProductModel>> productResults = new HashSet<FilteringSortingItem<ProductModel>>();
		Set<FilteringSortingItem<Recipe>> recipeResults = new HashSet<FilteringSortingItem<Recipe>>();
		Set<FilteringSortingItem<CategoryModel>> categoryResults = new HashSet<FilteringSortingItem<CategoryModel>>();

		Set<ProductModel> alreadyAddedProducts = new HashSet<ProductModel>();

		List<ProductModel> products;
		List<Recipe> recipes;
		List<CategoryModel> categories;

		// handle exact items
		if (!phraseHits.isEmpty()) {
			products = ContentSearchUtil.filterProducts(ContentSearchUtil.filterProductHits(phraseHits));
			recipes = ContentSearchUtil.filterRecipes(ContentSearchUtil.filterRecipeHits(phraseHits));
			categories = ContentSearchUtil.filterCategories(ContentSearchUtil.filterCategoryHits(phraseHits));

			extractCategoryProducts(categories, products, alreadyAddedProducts);
	
			productResults.addAll(FilteringSortingItem.fill(products, EnumSortingValue.PHRASE, 1));
			recipeResults.addAll(FilteringSortingItem.fill(recipes, EnumSortingValue.PHRASE, 1));
			categoryResults.addAll(FilteringSortingItem.fill(categories, EnumSortingValue.PHRASE, 1));
		}

		// handle non-phrase items
		if (!quoted && !nonPhraseHits.isEmpty()) {
			products = ContentSearchUtil.filterProducts(ContentSearchUtil.filterProductHits(nonPhraseHits));
			recipes = ContentSearchUtil.filterRecipes(ContentSearchUtil.filterRecipeHits(nonPhraseHits));
			categories = ContentSearchUtil.filterCategories(ContentSearchUtil.filterCategoryHits(nonPhraseHits));

			if (productResults.isEmpty()) {
				extractCategoryProducts(categories, products, alreadyAddedProducts);
			}
	
			productResults.addAll(FilteringSortingItem.wrap(products));
			recipeResults.addAll(FilteringSortingItem.wrap(recipes));
			categoryResults.addAll(FilteringSortingItem.wrap(categories));
		}
		
		return new SearchResults(new ArrayList<FilteringSortingItem<ProductModel>>(productResults),
				new ArrayList<FilteringSortingItem<Recipe>>(recipeResults), 
				new ArrayList<FilteringSortingItem<CategoryModel>>(categoryResults), searchTerm, quoted);
	}

	private void extractCategoryProducts(List<CategoryModel> categories, List<ProductModel> products,
			Set<ProductModel> alreadyAddedProducts) {
		if (products.isEmpty() && !categories.isEmpty()) {
			for (CategoryModel category : categories) {
				for (ProductModel product : category.getAllChildProducts())
					if (ContentSearchUtil.isDisplayable(product) && !alreadyAddedProducts.contains(product)) {
						alreadyAddedProducts.add(product);
						products.add(product);
					}
			}
		}
	}

	public void suggestSpellingInternal(String searchTerm, boolean quoted, SearchResults originalResults) {
		boolean replaceOriginal = false;
		List<SpellingHit> hits = combineSpellingHitResults(searchTerm);
		
		Collection<SpellingCandidate> candidates = extractCandidatesFromSpellingHits(hits, quoted, originalResults);
		
		if (candidates.isEmpty() && originalResults.isEmpty()) {
			candidates = suggestApproximationSpellingInternal(hits,  quoted, originalResults);
			replaceOriginal = true;
		}
		
		List<String> suggestions = new ArrayList<String>();
		for (SpellingCandidate candidate : candidates)
			suggestions.add(ContentSearchUtil.quote(quoted, candidate.getPhrase()));

		if (originalResults.isEmpty() && !candidates.isEmpty()) {
			SearchResults.replaceResults(originalResults, candidates.iterator().next().getSearchResults());
		} else if (replaceOriginal && !candidates.isEmpty()) {
			SearchResults.replaceResults(originalResults, SpellingCandidate.extractSearchResults(candidates));
		}
		originalResults.setSpellingSuggestions(suggestions);
	}

	public List<SpellingHit> combineSpellingHitResults(String searchTerm) {
		Set<SpellingHit> set = new HashSet<SpellingHit>(CmsManager.getInstance().suggestSpelling(searchTerm, getDidYouMeanThreshold(), getDidYouMeanMaxHits()));
		set.addAll(CmsManager.getInstance().reconstructSpelling(searchTerm, getDidYouMeanThreshold(), getDidYouMeanMaxHits()));
		List<SpellingHit> hits = new ArrayList<SpellingHit>(set.size());
		for (SpellingHit hit : set) {
			String phrase = hit.getPhrase();
			Iterator<SpellingHit> it = hits.iterator();
			boolean add = true;
			while (it.hasNext()) {
				SpellingHit hit1 = it.next();
				String phrase1 = hit1.getPhrase();
				if (phrase1.contains(phrase) && hit1.getScore() < hit.getScore())
					it.remove();
				else if (phrase.contains(phrase1) && hit.getScore() < hit1.getScore())
					add = false;
			}
			if (add)
				hits.add(hit);
		}
		Collections.sort(hits, SpellingHit.SORT_BY_DISTANCE);
		hits = SpellingUtils.filterBestSpellingHits(hits, getDidYouMeanThreshold());
		Collections.sort(hits);
		return hits;
	}

	public Collection<SpellingCandidate> suggestApproximationSpellingInternal(Collection<SpellingHit> hits, boolean quoted, SearchResults originalResults) {
		SortedSet<SpellingCandidate> candidates = new TreeSet<SpellingCandidate>();
		if (!hits.isEmpty()) {
			// take the best (first) hit as a basis
			SpellingHit hit = hits.iterator().next();
			ApproximationsPermuter permuter = new ApproximationsPermuter(new Term(hit.getPhrase()));
			List<List<Term>> permutations = permuter.permute();
			for (List<Term> permLevel : permutations) {
				if (!candidates.isEmpty())
					break;
				List<SpellingCandidate> cans = new ArrayList<SpellingCandidate>();
				for (Term permutation : permLevel) {
					SpellingCandidate candidate = new SpellingCandidate(new SpellingHit(permutation.toString(), hit.getDistance()));
					SearchResults hitResults = searchProductsInternal(candidate.getPhrase(), quoted, false);
					if (hitResults.isEmpty())
						continue;
					candidate.setSearchResults(hitResults);
					cans.add(candidate);
				}
				if (!cans.isEmpty()) {
					if (cans.size() > 3)
						cans = cans.subList(0, 3);
					candidates.addAll(cans);
				}
			}
		}
		return candidates;
	}

	public List<SpellingCandidate> extractCandidatesFromSpellingHits(Collection<SpellingHit> hits, boolean quoted, SearchResults originalResults) {
		SortedSet<SpellingCandidate> candidates = new TreeSet<SpellingCandidate>();
		Iterator<SpellingHit> it = hits.iterator();
		SpellingHit baseHit = null;
		List<SpellingHit> sameScore = new ArrayList<SpellingHit>();
		while (it.hasNext()) {
			SpellingHit current = it.next();
			if (baseHit == null) {
				baseHit = current;
				sameScore.add(current);
			} else if (baseHit.getScore() == current.getScore()) {
				sameScore.add(current);
			} else {
				SortedSet<SpellingCandidate> subCandidates = processSubcandidates(quoted, originalResults, candidates, sameScore);
				candidates.addAll(subCandidates);
				if (candidates.size() > 2)
					break;
				sameScore.clear();
				baseHit = current;
				sameScore.add(current);
			}
		}

		SortedSet<SpellingCandidate> subCandidates = processSubcandidates(quoted, originalResults, candidates, sameScore);
		candidates.addAll(subCandidates);
		
		List<SpellingCandidate> ret = new ArrayList<SpellingCandidate>(candidates);
		while (ret.size() > 3)
			ret.remove(ret.size() - 1);
		return ret;
	}

	private SortedSet<SpellingCandidate> processSubcandidates(boolean quoted, SearchResults originalResults,
			SortedSet<SpellingCandidate> candidates, List<SpellingHit> sameScore) {
		SortedSet<SpellingCandidate> subCandidates = new TreeSet<SpellingCandidate>();
		SUBS: for (SpellingHit hit : sameScore) {
			SpellingCandidate candidate = new SpellingCandidate(hit);
			SearchResults hitResults = searchProductsInternal(hit.getPhrase(), quoted, false);
			if (hitResults.isEmpty())
				continue;
			candidate.setSearchResults(hitResults);
			for (SpellingCandidate can : candidates)
				if (candidate.getSearchResults().resultsEquals(can.getSearchResults()))
					continue SUBS;
			for (SpellingCandidate can : subCandidates)
				if (candidate.getSearchResults().resultsEquals(can.getSearchResults()))
					continue SUBS;
			if (originalResults.isEmpty() || hitResults.size() >= originalResults.size() * getDidYouMeanRatio())
				if (!hitResults.resultsEquals(originalResults))
					subCandidates.add(candidate);
		}
		return subCandidates;
	}

	public SearchResults searchUpc(String erpCustomerPK, String upc) {
		List<ProductModel> products = new ArrayList<ProductModel>();
		ErpFactory erpFactory = ErpFactory.getInstance();
		Set<String> skuCodes = new HashSet<String>();

		try {
			FDProductInfo cachedProductInfo = FDCachedFactory.getProductInfoByUpc(upc);
			if(cachedProductInfo != null && cachedProductInfo.getSkuCode() != null && cachedProductInfo.getSkuCode().length() > 0) {
				LOGGER.info("Product Found in UPCCache:"+upc+"->"+cachedProductInfo.getSkuCode());
				skuCodes.add(cachedProductInfo.getSkuCode());
			} else {
				LOGGER.info("Product Not Found in UPCCache Searching DB:"+upc);
				Collection<ErpProductInfoModel> productInfos = erpFactory.findProductsByUPC(upc);
				for (ErpProductInfoModel productInfo : productInfos) {
					if (productInfo.getSkuCode() != null) {
						skuCodes.add(productInfo.getSkuCode());
					}
				}
			}
			// If system is not able to find the skus by the material upccodes in erpsy then go search in the customer order
			// history for the last 60 days for a product with the barcode
			if (skuCodes.size() == 0 && erpCustomerPK != null && erpCustomerPK.trim().length() > 0) {
				skuCodes.addAll(erpFactory.findProductsByCustomerUPC(erpCustomerPK, upc));
			}

			for (String skuCode : skuCodes) {
				SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SKU, skuCode);
				if (sku == null)
					continue;
				ProductModel product = sku.getProductModel();
				if (product != null)
					products.add(product);
			}
		} catch (FDResourceException e) {
			LOGGER.error("failed to retrieve product info for upc: " + upc, e);
		}
		
		List<FilteringSortingItem<ProductModel>> productResults = new ArrayList<FilteringSortingItem<ProductModel>>(products.size());
		for (ProductModel product : products) {
			FilteringSortingItem<ProductModel> item = new FilteringSortingItem<ProductModel>(product);
			item.putSortingValue(EnumSortingValue.PHRASE, 1);
			productResults.add(item);
		}
		return new SearchResults(productResults, Collections.<FilteringSortingItem<Recipe>> emptyList(), Collections
				.<FilteringSortingItem<CategoryModel>> emptyList(), upc, true);
	}

	public List<ContentKey> searchFaqs(String searchTerm) {
		searchTerm = searchTerm.trim();
		if (searchTerm.length() == 0) {
			return Collections.emptyList();
		}
		boolean exact = ContentSearchUtil.isQuoted(searchTerm);
		if (exact)
			searchTerm = ContentSearchUtil.removeQuotes(searchTerm);

		Collection<SearchHit> hits = CmsManager.getInstance().searchFaqs(searchTerm, exact, getSearchMaxHits());

		List<SearchHit> faqHits = ContentSearchUtil.filterFaqHits(hits);

		List<Faq> faqs = ContentSearchUtil.filterFaqs(faqHits);

		List<ContentKey> faqKeys = new ArrayList<ContentKey>();
		for (Faq faq : faqs)
			faqKeys.add(faq.getContentKey());

		return faqKeys;
	}

	public List<ContentKey> searchRecipes(String searchTerm) {
		searchTerm = searchTerm.trim();
		if (searchTerm.length() == 0) {
			return Collections.emptyList();
		}
		boolean exact = ContentSearchUtil.isQuoted(searchTerm);
		if (exact)
			searchTerm = ContentSearchUtil.removeQuotes(searchTerm);

		Collection<SearchHit> hits = CmsManager.getInstance().searchRecipes(searchTerm, exact, getSearchMaxHits());

		List<SearchHit> recipeHits = ContentSearchUtil.filterRecipeHits(hits);

		List<Recipe> recipes = ContentSearchUtil.filterRecipes(recipeHits);

		List<ContentKey> recipeKeys = new ArrayList<ContentKey>();
		for (Recipe recipe : recipes)
			recipeKeys.add(recipe.getContentKey());

		return recipeKeys;
	}

    /**
     * Set a custom autocomplete service
     * @param autocompletion
     */    
    public List<String> getAutocompletions(String prefix) {

        AutocompleteService autocompleter = productAutocompletion.initAutocompleter("product");

        if (autocompleter!=null) {
            return autocompleter.getAutocompletions(prefix);
        }
		return Collections.emptyList();
    }

    public List<String> getAutocompletions(String prefix, AutocompleteService.Predicate predicate) {
        AutocompleteService autocompleter = productAutocompletion.initAutocompleter("product");

        if (autocompleter!=null) {
            return autocompleter.getAutocompletions(prefix);
        }
		return Collections.emptyList();
    }

    public List<String> getBrandAutocompletions(String prefix) {
        AutocompleteService autocompleter = brandAutocompletion.initAutocompleter("brand");

        if (autocompleter!=null) {
            return autocompleter.getAutocompletions(prefix);
        }
		return Collections.emptyList();
    }

	/**
	 * Return a Map<ContentKey,Integer> which contains the predefined scores for the given search term. The map can be null.
	 * 
	 * @param searchTerm
	 * @return Map<ContentKey,Integer>
	 */
	public Map<ContentKey, Integer> getSearchRelevancyScores(String searchTerm) {
		synchronized (this) {
			if (this.searchRelevancyMap == null) {
				this.searchRelevancyMap = SearchRelevancyList.createFromCms();
			}
		}
		SearchRelevancyList srl = searchRelevancyMap.get(searchTerm.trim().toLowerCase());
		return srl != null ? Collections.unmodifiableMap(srl.getCategoryScoreMap()) : null;
	}

	public void refreshRelevancyScores() {
		synchronized (this) {
			this.searchRelevancyMap = SearchRelevancyList.createFromCms();
		}
	}

	public void invalidateRelevancyScores() {
		synchronized (this) {
			this.searchRelevancyMap = null;
		}
	}

}
