package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.AutocompleteService;
import com.freshdirect.cms.search.AutoComplete;
import com.freshdirect.cms.search.BrandNameExtractor;
import com.freshdirect.cms.search.BrandNameWordList;
import com.freshdirect.cms.search.CounterCreatorImpl;
import com.freshdirect.cms.search.LuceneSpellingSuggestionService;
import com.freshdirect.cms.search.ProductNameWordList;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.search.SimpleCounterCreator;
import com.freshdirect.cms.search.SpellingHit;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ContentSearch {
    private final static Logger        LOGGER   = LoggerFactory.getInstance(ContentSearch.class);
    
    private static ContentSearch instance = new ContentSearch();

    AutoComplete productAutocompletion = new AutoComplete(new ProductNameWordList(), new CounterCreatorImpl());
    AutoComplete brandAutocompletion = new AutoComplete(new BrandNameWordList(), new SimpleCounterCreator());
        

    
    Map<String, SearchRelevancyList> searchRelevancyMap;
    
    public static ContentSearch getInstance() {
        return instance;
    }

    public static void setInstance(ContentSearch newInstance) {
        instance = newInstance;
        LOGGER.info("ContentSearch instance replaced");
    }

    
    
    /** Maximum number of the top spelling results that should be analyzed */
    public static int MAX_SUGGESTIONS = 5;

    private SearchQuery getSearchQuery(String criteria) {
        return new SearchQuery(criteria) {

            private String[] tokens;
            private String[] rawTokens;
            private String   normalizedTerm;
            private String   searchTerm;

            public String getNormalizedTerm() {
                return normalizedTerm;
            }

            public String getSearchTerm() {
                return searchTerm;
            }

            public String[] getTokens() {
                return tokens;
            }
            
            public String[] getTokensWithBrand() {
                return rawTokens;
            }

            protected void breakUp() {
                normalizedTerm = ContentSearchUtil.normalizeTerm(getOriginalTerm());

                BrandNameExtractor brandNameExtractor = new BrandNameExtractor();

                tokens = ContentSearchUtil.tokenizeTerm(normalizedTerm);
                AutocompleteService autocompleter = productAutocompletion.initAutocompleter("product");
                StringBuffer luceneTerm;
                
                // we need to check tokens length, because search like 'v8' cause empty token array.
                if (autocompleter!=null && tokens.length > 0) {
                    luceneTerm = new StringBuffer();
                    for (int i=0;i<tokens.length;i++) {
                        tokens[i] = autocompleter.removePlural(tokens[i]);
                        luceneTerm.append(tokens[i]).append(' ');
                    }
                } else {
                    luceneTerm = new StringBuffer(normalizedTerm);
                }
                rawTokens = tokens;
                for (Iterator<CharSequence> i = brandNameExtractor.extract(getOriginalTerm()).iterator(); i.hasNext();) {
                    String canonicalBrandName = StringUtil.removeAllWhiteSpace(i.next().toString()).toLowerCase();
                    if (luceneTerm.indexOf(canonicalBrandName) == -1) {
                        luceneTerm.append(' ').append(canonicalBrandName);
                    }
                    if (tokens.length == 0) {
                        tokens = new String[] { canonicalBrandName };
                        List<String> rt = ContentSearchUtil.tokenizeTerm(normalizedTerm, " ");
                        rt.add(canonicalBrandName);
                        rawTokens = rt.toArray(new String[rt.size()]);
                        //rawTokens = tokens;
                    }
                }
                searchTerm = luceneTerm.toString();
            }

        };
    }
    


    /**
     * Perform a simple search.
     * 
     * Performs a search and filtering but does not do spell checking (unlike
     * {@link #search(String) search}).
     * 
     * If term is "", return empty results. Otherwise separate the query into
     * tokens, search for each separately, collate the results (see
     * {@link ContentSearchUtil}'s filter methods).
     * 
     * 
     * @param criteria
     *            original query
     * @return search results
     */
    @SuppressWarnings( "unchecked" )
	public SearchResults simpleSearch(String criteria, SearchQueryStemmer stemmer) {

        SearchQuery searchQuery = getSearchQuery(criteria);

        if ("".equals(searchQuery.getNormalizedTerm())) {
            return new SearchResults(Collections.<ProductModel>emptyList(), Collections.<Recipe>emptyList(), false, "");
        }

        List<SearchHit> hits = CmsManager.getInstance().search(searchQuery.getSearchTerm(), 2000);

        Map<ContentType,List<SearchHit>> hitsByType = ContentSearchUtil.mapHitsByType(hits);

        List<SearchHit> allProducts = ContentSearchUtil.resolveHits(hitsByType.get(FDContentTypes.PRODUCT));
        
        filterOutNonRelevantProducts(searchQuery, allProducts);
        
        List<SearchHit> relevantProducts = ContentSearchUtil.filterRelevantNodes(allProducts, searchQuery.getTokensWithBrand(), stemmer);

        List<SearchHit> recipes = ContentSearchUtil.filterRelevantNodes(ContentSearchUtil.resolveHits(hitsByType.get(FDContentTypes.RECIPE)), 
        		searchQuery.getTokens(), stemmer);

        List<SearchHit> filteredProducts = ContentSearchUtil.filterProductsByDisplay(relevantProducts.isEmpty() ? 
        		ContentSearchUtil.restrictToMaximumOccuringNodes(allProducts, searchQuery.getTokensWithBrand(), stemmer) : relevantProducts);

        List<SearchHit> filteredRecipes = ContentSearchUtil.filterRecipesByAvailability(recipes);

        return new SearchResults(
                (List<ProductModel>)ContentSearchUtil.collectFromSearchHits(filteredProducts), 
                (List<Recipe>)ContentSearchUtil.collectFromSearchHits(filteredRecipes), 
                !relevantProducts.isEmpty(), searchQuery.getSearchTerm(), filteredProducts, filteredRecipes);
    }

    /**
     * Filter out non relevant products, based on the search relevancy scores from the CMS. This method modifies the product list!
     * 
     * @param searchQuery
     * @param products
     */
    private void filterOutNonRelevantProducts(SearchQuery searchQuery, List<SearchHit> products) {
        Map<ContentKey,Integer> relevancyScores = getSearchRelevancyScores(searchQuery.getSearchTerm());
        if (relevancyScores!=null) { 
            // filter out negative categories
            for (Iterator<SearchHit> i = products.iterator(); i.hasNext();) {
                SearchHit hit = i.next();
                ContentNodeModel node = hit.getNode();
                Integer score = getRelevancyScore( relevancyScores, node );
                if (score!=null) {
                    if (score.intValue()<=0) {
                        i.remove();
                    }
                }
            }
        }
    }


    /**
     * Utility method for getting the relevancy score of the nearest parent node which has a valid score.
     * Returns an Integer. Value is null if no parent has a score set.  
     * 
     * @param scores	scores map
     * @param node		product node
     * @return			relevancy score
     */
    public static Integer getRelevancyScore( Map<ContentKey,Integer> scores, ContentNodeModel node ) {
    	
    	if ( node == null || scores == null )
    		return null;
    	
    	Integer score = null;
    	ContentNodeModel parent = null;    	
    	
    	while ( score == null ) {    		
    		
        	parent = node.getParentNode();
        	if ( parent == null )
        		break;
        	
        	score = scores.get( parent.getContentKey() ); 
        	node = parent;
    	}    	    	
    	
    	return score;
    }


    /**
     * Search and possibly suggest alternative.
     * 
     * The following triggers the search for alternative spellings.
     * 
     * <ul>
     * <ol>
     * The products found by {@link #simpleSearch(String) simpleSearch} were not
     * 100% relevant (ie. there were no matches that covered all query terms.
     * </ol>
     * <ol>
     * No exact categories and no exact products were found.
     * </ul>
     * 
     * Derivation of the alternative:
     * <ul>
     * <ol>
     * Retrieve a list of spelling suggestions, sorted by relevance (see
     * {@link LuceneSpellingSuggestionService#getSpellingHits}.
     * </ol>
     * <ol>
     * For earch suggestion (but at most {@link #MAX_SUGGESTIONS} many times),
     * gather the "would be" search results
     * </ol>
     * <ul>
     * <ol>
     * If there are no results, skip suggestion.
     * </ol>
     * <ol>
     * If there are some results
     * </ol>
     * <ul>
     * <ol>
     * See if the edit distance to the "whole" expression is better than that of
     * the previous suggestions, if so set this as the new candidate for the
     * suggested alternative
     * </ol>
     * <ol>
     * See if the results could indicate that the results for the suggestion add
     * new value (that is, calculate (S1 + S2 - S12)/(S1 + S2 + S1S2) where S1
     * and S2 are the number of results unique to the original and the suggested
     * queries and S1S2 is the number of elements common to both. This is
     * similar to the so called Jaccard distance. If this value is 0.80 or
     * higher, stop here and declare it "the" suggestion
     * </ul>
     * </ul> </ul>
     * 
     * @param criteria
     *            original query
     * @return search results, possibly ammended with a spelling suggestion
     */
    @SuppressWarnings( "unchecked" )
	public SearchResults search(String criteria) {

        SearchQueryStemmer stemmer = SearchQueryStemmer.Porter;
        SearchResults filteredResults = simpleSearch(criteria, stemmer);

        SearchResults.SpellingResultsDifferences diffs = null;
        String spellingSuggestion = null;
        boolean suggestionMoreRelevant = false;

        if (!filteredResults.isProductsRelevant()) {

            List<SpellingHit> spellingSuggestions = CmsManager.getInstance().suggestSpelling(criteria, 20);

            int count = 0;
            int bestDistance = criteria.length();
            for (Iterator<SpellingHit> i = spellingSuggestions.iterator(); count < MAX_SUGGESTIONS && i.hasNext(); ++count) {

                SpellingHit spellingHit = i.next();
                String suggestion = spellingHit.toString();

                SearchResults suggestionResults = simpleSearch(suggestion, stemmer);

                if (!suggestionResults.getRecipes().isEmpty() || !suggestionResults.getProducts().isEmpty()) {
                    // there are some results

                    if (spellingHit.getDistance() < bestDistance) {
                        bestDistance = spellingHit.getDistance();
                    }

                    if (!filteredResults.isProductsRelevant()) { // no original relevant products

                        Collection<? extends ContentNodeModel>[] originalHits = new Collection[] { filteredResults.getProducts(), filteredResults.getRecipes() };
                        Collection<? extends ContentNodeModel>[] suggestedHits = new Collection[] { suggestionResults.getProducts(), suggestionResults.getRecipes() };

                        diffs = ContentSearchUtil.chopSets(originalHits, suggestedHits);
                        spellingSuggestion = suggestion;
                        suggestionMoreRelevant = ((double) (diffs.getOriginal() + diffs.getSuggested() - diffs.getIntersection()) / 
                        		(double) (diffs.getOriginal() + diffs.getSuggested() + diffs.getIntersection())) > 0.80
                                && spellingHit.getDistance() == bestDistance;

                        if (suggestionMoreRelevant) {
                            break;
                        }
                    } else if (spellingSuggestion == null) {
                        spellingSuggestion = suggestion;
                    }
                }
            }
        }

        filteredResults.setSpellingSuggestion(spellingSuggestion, suggestionMoreRelevant);
        filteredResults.setSpellingResultsDifferences(diffs);

        return filteredResults;
    }

    public SearchResults searchUpc(String upc) {
    	List<ProductModel> products = new ArrayList<ProductModel>();
    	ErpFactory erpFactory = ErpFactory.getInstance();
    	try {
			Collection<ErpProductInfoModel> productInfos = erpFactory.findProductsByUPC(upc);
			for (ErpProductInfoModel productInfo : productInfos) {
				String skuCode = productInfo.getSkuCode();
				if (skuCode != null) {
					SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SKU, skuCode);
					if (sku == null)
						continue;
					ProductModel product = sku.getProductModel();
					if (product != null)
						products.add(product);
				}
			}
		} catch (FDResourceException e) {
			LOGGER.error("failed to retrieve product info for upc: " + upc, e);
		}
    	return new SearchResults(products, Collections.<Recipe>emptyList(), false , upc);
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
    public Map<ContentKey,Integer> getSearchRelevancyScores(String searchTerm) {
        synchronized(this) {
            if (this.searchRelevancyMap == null) {
                this.searchRelevancyMap = SearchRelevancyList.createFromCms();
            }
        }
        SearchRelevancyList srl = searchRelevancyMap.get(searchTerm.trim().toLowerCase());
        return srl!=null ? Collections.unmodifiableMap(srl.getCategoryScoreMap()) : null;
    }
    
    
    public void refreshRelevencyScores() {
        synchronized(this) {
            this.searchRelevancyMap = SearchRelevancyList.createFromCms();
            AutocompleteService autocompleter = productAutocompletion.initAutocompleter("product");

            if (autocompleter != null) {
            	autocompleter.clearBadSingular();
            	autocompleter.addAllBadSingular(SearchRelevancyList.getBadPluralFormsFromCms());
            }
        }
    }

    public void invalidateRelevancyScores() {
        synchronized(this) {
            this.searchRelevancyMap = null;
        }
    }
    
}
