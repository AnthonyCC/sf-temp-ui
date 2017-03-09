package com.freshdirect.cms.index.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.index.IndexingConstants;
import com.freshdirect.cms.index.configuration.IndexConfiguration;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.SearchUtils;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.search.SynonymDictionaryListKey;
import com.freshdirect.cms.search.spell.Dictionary;
import com.freshdirect.cms.search.spell.DictionaryItem;
import com.freshdirect.cms.search.spell.FreshDirectDictionary;
import com.freshdirect.cms.search.term.SearchTermNormalizer;
import com.freshdirect.cms.search.term.SynonymSpellingTermNormalizerFactory;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoderFactory;

public class IndexerUtil {

    private static final IndexerUtil INSTANCE = new IndexerUtil();
    public static IndexerUtil getInstance() {
        return INSTANCE;
    }

    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    private IndexerUtil() {
    }

    /**
     * Collects permutations and stores them in the given synonym dictionary
     * 
     */
    public void collectPermutations(SynonymDictionary permutations, ContentNodeI node, IndexerConfiguration indexerConfiguration) {
        List<AttributeIndex> attributeIndexes = indexConfiguration.getAllIndexConfigurationsByContentType().get(node.getKey().getType());
        if (attributeIndexes != null) {
            for (AttributeIndex attributeIndex : attributeIndexes) {
                List<Term> values = SearchUtils.collectValues(node, attributeIndex, !indexerConfiguration.isKeywordsDisabled(), indexerConfiguration.isPrimaryHomeKeywordsEnabled(),
                        indexerConfiguration.isRecurseParentAttributesEnabled(), indexerConfiguration.getStoreContentSource());
                if (!attributeIndex.isText() && !values.isEmpty()) {
                    new SearchTermNormalizer(permutations, values).getTerms();
                }
            }
        }
    }

    /**
     * Creates the dictionary from the contentNodes for indexing
     * 
     * @param contentNodes
     *            the nodes from which the Dictionary should be created
     * @param indexerConfiguration
     *            Configuration of the Indexer
     * @param indexConfigurationsByContentType
     *            indexing rules (which attributes should be indexed and how)
     * @return the dictionary which can be indexed
     */
    public Dictionary createDictionaryForIndexing(Collection<ContentNodeI> contentNodes, IndexerConfiguration indexerConfiguration) {

        SynonymSpellingTermNormalizerFactory synonymSpellingTermNormalizerFactory = new SynonymSpellingTermNormalizerFactory();

        List<SynonymDictionary> synonymDictionaries = createSynonymDictionaries(synonymSpellingTermNormalizerFactory, indexerConfiguration.getCustomSynonyms(),
                SynonymDictionaryListKey.SYNONYM, FDContentTypes.SYNONYM, true, indexerConfiguration.isSynonymsDisabled(), indexerConfiguration.getStoreContentSource());

        List<SynonymDictionary> spellingDictionaries = createSynonymDictionaries(synonymSpellingTermNormalizerFactory, indexerConfiguration.getCustomSpellingSynonyms(),
                SynonymDictionaryListKey.SPELLING_SYNONYM, FDContentTypes.SPELLING_SYNONYM, false, indexerConfiguration.isSynonymsDisabled(),
                indexerConfiguration.getStoreContentSource());

        Dictionary dictionary = new FreshDirectDictionary(contentNodes, indexConfiguration.getAllIndexConfigurationsByContentType(), synonymDictionaries, spellingDictionaries,
                indexerConfiguration.isKeywordsDisabled(), indexerConfiguration.getStoreContentSource());

        return dictionary;
    }

    /**
     * Creates the queries from a dictionary which can find the elements of the dictionary in the already existing index files
     * 
     * @param dictionary
     *            the dictionary which elements has to be identified in the index files
     * @return list of queries, or an empty list if there are no elements in the dictionary
     */
    public List<BooleanQuery> createFindQueriesForDictionary(Dictionary dictionary) {
        List<BooleanQuery> queries = new ArrayList<BooleanQuery>();
        Iterator<DictionaryItem> iter = dictionary.getWordsIterator();
        while (iter.hasNext()) {
            DictionaryItem item = iter.next();
            String searchTerm = item.getSearchTerm();
            String spellingTerm = item.getSpellingTerm();

            BooleanQuery query = new BooleanQuery();
            query.add(new TermQuery(IndexingConstants.F_WORD_TERM.createTerm(spellingTerm)), Occur.MUST);
            query.add(new TermQuery(new org.apache.lucene.index.Term(IndexingConstants.F_SEARCH_TERM, searchTerm)), Occur.MUST);
            queries.add(query);
        }
        return queries;
    }

    /**
     * Creates a list of synonymDictionaries from the parameters
     * 
     * @param termCoderFactory
     * @param customSynonyms
     * @param synonymKey
     * @param synonymType
     * @param createNumberSynonyms
     * @param synonymsDisabled
     * @param storeContentSource
     * @return
     */
    public List<SynonymDictionary> createSynonymDictionaries(TermCoderFactory termCoderFactory, List<SynonymDictionary> customSynonyms, SynonymDictionaryListKey synonymKey,
            ContentType synonymType, boolean createNumberSynonyms, boolean synonymsDisabled, StoreContentSource storeContentSource) {

        List<SynonymDictionary> synonymDictionaries = new ArrayList<SynonymDictionary>();
        if (createNumberSynonyms) {
            synonymDictionaries.add(SynonymDictionary.createNumberSynonyms());
        }
        if (!synonymsDisabled) {
            if (customSynonyms != null)
                synonymDictionaries.addAll(customSynonyms);
            else {
                synonymDictionaries.add(SynonymDictionary.createSynonymsFromSource(termCoderFactory, synonymKey, synonymType, storeContentSource));
            }
        }
        return synonymDictionaries;
    }
}
