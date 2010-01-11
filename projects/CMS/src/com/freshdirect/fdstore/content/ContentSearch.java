package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.AutocompleteService;
import com.freshdirect.cms.search.BrandNameExtractor;
import com.freshdirect.cms.search.LuceneSpellingSuggestionService;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.search.SpellingHit;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ContentSearch {
    private final static Logger        LOGGER   = LoggerFactory.getInstance(ContentSearch.class);
    
    private static ContentSearch instance = new ContentSearch();

    AutocompleteService autocompletion;
    Thread autocompleteUpdater;
    

    boolean disableAutocompleter = false;
    
    Map<String, SearchRelevancyList> searchRelevancyMap;
    
    public static ContentSearch getInstance() {
        return instance;
    }

    public static void setInstance(ContentSearch newInstance) {
        instance = newInstance;
        LOGGER.info("ContentSearch instance replaced");
    }

    public void setDisableAutocompleter(boolean disableAutocompleter) {
        this.disableAutocompleter = disableAutocompleter;
    }
    
    public boolean isDisableAutocompleter() {
        return disableAutocompleter;
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
                AutocompleteService autocompleter = initAutocompleter();
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
                for (Iterator i = brandNameExtractor.extract(getOriginalTerm()).iterator(); i.hasNext();) {
                    String canonicalBrandName = StringUtil.removeAllWhiteSpace(i.next().toString()).toLowerCase();
                    if (luceneTerm.indexOf(canonicalBrandName) == -1) {
                        luceneTerm.append(' ').append(canonicalBrandName);
                    }
                    if (tokens.length == 0) {
                        tokens = new String[] { canonicalBrandName };
                        List<String> rt = ContentSearchUtil.tokenizeTerm(normalizedTerm, " ");
                        rt.add(canonicalBrandName);
                        rawTokens = (String[]) rt.toArray(new String[rt.size()]);
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
    public SearchResults simpleSearch(String criteria, SearchQueryStemmer stemmer) {

        SearchQuery searchQuery = getSearchQuery(criteria);

        if ("".equals(searchQuery.getNormalizedTerm())) {
            return new SearchResults(Collections.EMPTY_LIST, Collections.EMPTY_LIST, false, "");
        }

        List hits = CmsManager.getInstance().search(searchQuery.getSearchTerm(), 2000);

        Map hitsByType = ContentSearchUtil.mapHitsByType(hits);

        List allProducts = ContentSearchUtil.resolveHits((List) hitsByType.get(FDContentTypes.PRODUCT));
        
        filterOutNonRelevantProducts(searchQuery, allProducts);
        
        List relevantProducts = ContentSearchUtil.filterRelevantNodes(allProducts, searchQuery.getTokensWithBrand(), stemmer);

        List recipes = ContentSearchUtil.filterRelevantNodes(ContentSearchUtil.resolveHits((List) hitsByType.get(FDContentTypes.RECIPE)), searchQuery
                .getTokens(), stemmer);

        List filteredProducts = ContentSearchUtil.filterProductsByDisplay(relevantProducts.isEmpty() ? ContentSearchUtil.restrictToMaximumOccuringNodes(
                allProducts, searchQuery.getTokensWithBrand(), stemmer) : relevantProducts);

        List filteredRecipes = ContentSearchUtil.filterRecipesByAvailability(recipes);

        return new SearchResults(
                ContentSearchUtil.collectFromSearchHits(filteredProducts), 
                ContentSearchUtil.collectFromSearchHits(filteredRecipes), 
                !relevantProducts.isEmpty(), searchQuery.getSearchTerm());
    }

    /**
     * Filter out non relevant products, based on the search relevancy scores from the CMS. This method modifies the product list!
     * 
     * @param searchQuery
     * @param products
     */
    private void filterOutNonRelevantProducts(SearchQuery searchQuery, List products) {
        Map relevancyScores = getSearchRelevancyScores(searchQuery.getSearchTerm());
        if (relevancyScores!=null) { 
            // filter out negative categories
            for (Iterator i = products.iterator(); i.hasNext();) {
                SearchHit hit = (SearchHit) i.next();
                ContentNodeModel node = hit.getNode();
                Integer score = (Integer) relevancyScores.get(node.getParentNode().getContentKey());
                if (score!=null) {
                    if (score.intValue()<=0) {
                        i.remove();
                    }
                }
            }
        }
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
    public SearchResults search(String criteria) {

        SearchQueryStemmer stemmer = SearchQueryStemmer.Porter;
        SearchResults filteredResults = simpleSearch(criteria, stemmer);

        SearchResults.SpellingResultsDifferences diffs = null;
        String spellingSuggestion = null;
        boolean suggestionMoreRelevant = false;

        if (!filteredResults.isProductsRelevant()) {

            List spellingSuggestions = CmsManager.getInstance().suggestSpelling(criteria, 20);

            int count = 0;
            int bestDistance = criteria.length();
            for (Iterator i = spellingSuggestions.iterator(); count < MAX_SUGGESTIONS && i.hasNext(); ++count) {

                SpellingHit spellingHit = (SpellingHit) i.next();
                String suggestion = spellingHit.toString();

                SearchResults suggestionResults = simpleSearch(suggestion, stemmer);

                if (!suggestionResults.getRecipes().isEmpty() || !suggestionResults.getProducts().isEmpty()) {
                    // there are some results

                    if (spellingHit.getDistance() < bestDistance) {
                        bestDistance = spellingHit.getDistance();
                    }

                    if (!filteredResults.isProductsRelevant()) { // no original
                                                                 // relevant
                                                                 // products

                        Collection[] originalHits = new Collection[] { filteredResults.getProducts(), filteredResults.getRecipes() };
                        Collection[] suggestedHits = new Collection[] { suggestionResults.getProducts(), suggestionResults.getRecipes() };

                        diffs = ContentSearchUtil.chopSets(originalHits, suggestedHits);
                        spellingSuggestion = suggestion;
                        suggestionMoreRelevant = ((double) (diffs.getOriginal() + diffs.getSuggested() - diffs.getIntersection()) / (double) (diffs
                                .getOriginal()
                                + diffs.getSuggested() + diffs.getIntersection())) > 0.80
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



    public AutocompleteService createAutocompleteService() {
        LOGGER.info("createAutocompleteService");
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
        LOGGER.info("contentKeysByType loaded :"+contentKeysByType.size());
        
        List<String> words = new ArrayList<String>(contentKeysByType.size());
        
        ContentFactory contentFactory = ContentFactory.getInstance();
        for (Iterator<ContentKey> keyIterator = contentKeysByType.iterator();keyIterator.hasNext();) {
            ContentKey key = keyIterator.next();
            ContentNodeModel nodeModel = contentFactory.getContentNodeByKey(key);
            if (nodeModel instanceof ProductModel) {
                ProductModel pm = (ProductModel) nodeModel;
                //if (pm.isDisplayableBasedOnCms()) {
                if (pm.isDisplayable()) {
                    words.add(pm.getFullName());
                }
            }
        }
        LOGGER.info("product names extracted:"+words.size());
        
        AutocompleteService a = new AutocompleteService(words);
        a.addAllBadSingular(SearchRelevancyList.getBadPluralFormsFromCms());
        return a;     
    }

    
    /**
     * Set a custom autocomplete service
     * @param autocompletion
     */
    public void setAutocompleteService(AutocompleteService autocompletion) {
        this.autocompletion = autocompletion;
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getAutocompletions(String prefix) {
        initAutocompleter();
        if (this.autocompletion!=null) {
            return this.autocompletion.getAutocompletions(prefix);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    

    @SuppressWarnings("unchecked")
    public List<String> getAutocompletions(String prefix, AutocompleteService.Predicate predicate) {
        initAutocompleter();
        if (this.autocompletion!=null) {
            return this.autocompletion.getAutocompletions(prefix, predicate);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
    

    private AutocompleteService initAutocompleter() {
        synchronized(this) {
            if (this.autocompletion == null) {
                if (autocompleteUpdater == null && disableAutocompleter == false) {
                    autocompleteUpdater = new Thread(new Runnable() {
                        public void run() {
                            //while(true) {
                                long time = System.currentTimeMillis();
                                LOGGER.info("autocomplete re-calculation started");
                                ContentSearch.this.autocompletion = createAutocompleteService();
                                time = System.currentTimeMillis() - time; 
                                LOGGER.info("autocomplete re-calculation finished, elapsed time : "+time+" ms");
                                /*try {
                                    Thread.sleep(24*60*60*1000);
                                } catch (InterruptedException e) {
                                    LOGGER.info("interrupted:"+e.getMessage(), e);
                                }*/
                            //}
                        }
                    },"autocompleteUpdater");
                    autocompleteUpdater.setDaemon(true);
                    autocompleteUpdater.start();
                }
                return null;
            } else {
                return autocompletion;
            }
        }
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
            autocompletion.clearBadSingular();
            autocompletion.addAllBadSingular(SearchRelevancyList.getBadPluralFormsFromCms());
        }
    }

    public void invalidateRelevancyScores() {
        synchronized(this) {
            this.searchRelevancyMap = null;
        }
    }
    
}
