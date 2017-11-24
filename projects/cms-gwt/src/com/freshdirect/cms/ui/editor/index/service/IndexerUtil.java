package com.freshdirect.cms.ui.editor.index.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.lucene.domain.AttributeIndex;
import com.freshdirect.cms.lucene.domain.IndexConfiguration;
import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.lucene.domain.IndexingConstants;
import com.freshdirect.cms.ui.editor.index.domain.Dictionary;
import com.freshdirect.cms.ui.editor.index.domain.DictionaryItem;
import com.freshdirect.cms.ui.editor.index.domain.FreshDirectDictionary;
import com.freshdirect.cms.ui.editor.index.domain.SynonymDictionary;
import com.freshdirect.cms.ui.editor.search.service.SearchTermNormalizer;
import com.freshdirect.cms.ui.editor.term.service.TermNormalizer;
import com.google.common.base.Optional;

@Service
public class IndexerUtil {

    private static final String KEYWORD_DELIMITERS = ",;";
    public static final ContentKey SYNONYM_KEY = ContentKeyFactory.get("FDFolder:synonymList");
    public static final ContentKey SPELLING_SYNONYM_KEY = ContentKeyFactory.get("FDFolder:spellingSynonymList");

    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    /**
     * Collects permutations and stores them in the given synonym dictionary
     *
     */
    public void collectPermutations(SynonymDictionary permutations, ContentKey nodeKey, Map<Attribute, Object> nodeAttributesWithValues, IndexerConfiguration indexerConfiguration,

            ContentKey storeKey) {
        SearchTermNormalizer searchTermNormalizer = new SearchTermNormalizer(false);
        List<AttributeIndex> attributeIndexes = indexConfiguration.getAllIndexConfigurationsByContentType().get(nodeKey.type);
        if (attributeIndexes != null) {
            for (AttributeIndex attributeIndex : attributeIndexes) {
                List<String> values = collectValues(nodeKey, nodeAttributesWithValues, attributeIndex, !indexerConfiguration.isKeywordsDisabled(),
                        indexerConfiguration.isPrimaryHomeKeywordsEnabled(), indexerConfiguration.isRecurseParentAttributesEnabled(),
                        indexerConfiguration.getContentProviderService(), storeKey);
                if (!attributeIndex.isText() && !values.isEmpty()) {
                    permutations.addSynonyms(new HashSet<String>(values), searchTermNormalizer);
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
    public Dictionary createDictionaryForIndexing(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration indexerConfiguration, ContentKey storeKey) {

        SpellingTermNormalizer spellingTermNormalizer = new SpellingTermNormalizer(false);

        List<SynonymDictionary> synonymDictionaries = createSynonymDictionaries(SYNONYM_KEY, ContentType.Synonym, true, indexerConfiguration.isSynonymsDisabled(),
                indexerConfiguration.getContentProviderService(), spellingTermNormalizer);

        List<SynonymDictionary> spellingDictionaries = createSynonymDictionaries(SPELLING_SYNONYM_KEY, ContentType.SpellingSynonym, false,
                indexerConfiguration.isSynonymsDisabled(), indexerConfiguration.getContentProviderService(), spellingTermNormalizer);

        Dictionary dictionary = new FreshDirectDictionary(contentNodes, indexConfiguration.getAllIndexConfigurationsByContentType(), synonymDictionaries, spellingDictionaries,
                indexerConfiguration.isKeywordsDisabled(), indexerConfiguration.getContentProviderService(), storeKey, indexerConfiguration.isPrimaryHomeKeywordsEnabled(),
                indexerConfiguration.isRecurseParentAttributesEnabled());

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
    public List<SynonymDictionary> createSynonymDictionaries(ContentKey synonymKey, ContentType synonymType, boolean createNumberSynonyms, boolean synonymsDisabled,
            ContextualContentProvider storeContentSource, TermNormalizer termNormalizer) {

        List<SynonymDictionary> synonymDictionaries = new ArrayList<SynonymDictionary>();
        if (createNumberSynonyms) {
            synonymDictionaries.add(SynonymDictionary.createNumberSynonyms());
        }
        if (!synonymsDisabled) {
            synonymDictionaries.add(SynonymDictionary.createSynonyms(synonymKey, synonymType, storeContentSource, termNormalizer));
        }
        return synonymDictionaries;
    }

    public static List<String> collectValues(ContentKey nodeKey, Map<Attribute, Object> nodeAttributesWithValues, AttributeIndex index, boolean keywordsEnabled,
            boolean primaryHomeKeywordsEnabled, boolean parentRecursionEnabled, ContextualContentProvider storeContentSource, ContentKey storeKey) {
        List<String> values = new ArrayList<String>();

        String attributeName = index.getAttribute().getName();
        String relationshipAttributeName = index.getRelationshipAttributeName();
        if (!primaryHomeKeywordsEnabled && "PRIMARY_HOME".equals(attributeName) && "KEYWORDS".equals(relationshipAttributeName))
            return values;
        boolean isKeyword = relationshipAttributeName != null && relationshipAttributeName.toLowerCase().startsWith("keyword") || attributeName.toLowerCase().startsWith("keyword");
        if (!keywordsEnabled && isKeyword)
            return values;
        Object attributeValue = getAttributeValueWithName(attributeName, nodeAttributesWithValues);
        if (attributeValue == null)
            return values;

        Attribute attribute = getAttributeWithName(attributeName, nodeAttributesWithValues);

        if (attribute instanceof Relationship && relationshipAttributeName != null) {
            if (((Relationship) attribute).getCardinality().equals(RelationshipCardinality.ONE)) {
                ContentKey key = (ContentKey) attributeValue;
                Optional<Object> relNode = storeContentSource.getAttributeValue(key, attribute);
                Object relValue = relNode.orNull();
                if (relValue != null) {
                    addValues(values, relValue.toString(), isKeyword);
                }
                if (parentRecursionEnabled && index.isRecurseParent()) {
                    collectParentValues(values, key, relationshipAttributeName, isKeyword, storeContentSource, storeKey);
                }
            } else { // EnumCardinality.MANY
                @SuppressWarnings("unchecked")
                List<ContentKey> relNodes = (List<ContentKey>) attributeValue;

                for (ContentKey key : relNodes) {
                    Optional<Object> relNode = storeContentSource.getAttributeValue(key, attribute);
                    Object relValue = relNode.orNull();
                    if (relValue != null) {
                        addValues(values, relValue.toString(), isKeyword);
                    }
                }
            }
        } else {
            addValues(values, attributeValue.toString(), isKeyword);
            if (parentRecursionEnabled && index.isRecurseParent()) {
                collectParentValues(values, nodeKey, attributeName, isKeyword, storeContentSource, storeKey);
            }
        }
        return values;
    }

    private static Object getAttributeValueWithName(String attributeName, Map<Attribute, Object> attributesWithValues) {
        Object attributeValue = null;
        for (Attribute attribute : attributesWithValues.keySet()) {
            if (attribute.getName().equals(attributeName)) {
                attributeValue = attributesWithValues.get(attribute);
                break;
            }
        }
        return attributeValue;
    }

    private static Attribute getAttributeWithName(String attributeName, Map<Attribute, Object> attributesWithValues) {
        Attribute attribute = null;
        for (Attribute actualAttribute : attributesWithValues.keySet()) {
            if (actualAttribute.getName().equals(attributeName)) {
                attribute = actualAttribute;
                break;
            }
        }
        return attribute;
    }

    private static List<String> keywordize(String term) {
        StringTokenizer stringTokenizer = new StringTokenizer(term, KEYWORD_DELIMITERS);
        List<String> keywords = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken().trim();
            if (!nextToken.isEmpty()) {
                keywords.add(nextToken);
            }
        }
        return keywords;
    }

    private static void collectParentValues(List<String> values, ContentKey nodeKey, String attributeName, boolean isKeyword,
            ContextualContentProvider storeContentSource, ContentKey storeKey) {
        ContentKey key = nodeKey;
        ContentKey parentKey = null;
        while ((parentKey = getParentKey(key, storeContentSource, storeKey)) != null) {
            Map<Attribute, Object> parentNode = storeContentSource.getAllAttributesForContentKey(parentKey);
            if (parentNode != null) {
                Object parentValue = getAttributeValueWithName(attributeName, parentNode);
                if (parentValue != null) {
                    addValues(values, parentValue.toString(), isKeyword);
                }
            } else {
                break; // something wrong
            }
            key = parentKey;
        }
    }

    private static ContentKey getParentKey(ContentKey key, ContextualContentProvider storeContentSource, ContentKey storeKey) {
        ContentKey parentKey = null;
        if (!ContentType.Store.equals(key.type)) {

            if (ContentType.Product.equals(key.type)) {
                parentKey = storeContentSource.findPrimaryHomes(key).get(storeKey);
            }

            if (parentKey == null) {
                Set<ContentKey> keys = storeContentSource.getParentKeys(key);
                if (keys != null && keys.size() != 0) {
                    Iterator<ContentKey> i = keys.iterator();
                    parentKey = i.next();
                }
            }

        }
        return parentKey;
    }

    private static void addValues(List<String> values, String valueToAdd, boolean isKeyword) {
        if (valueToAdd != null && !valueToAdd.isEmpty()) {
            if (isKeyword) {
                values.addAll(keywordize(valueToAdd));
            } else {
                values.add(valueToAdd);
            }
        }
    }
}
