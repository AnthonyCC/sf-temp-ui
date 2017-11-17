package com.freshdirect.cms.ui.editor.index.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.lucene.domain.AttributeIndex;
import com.freshdirect.cms.lucene.domain.IndexConfiguration;
import com.freshdirect.cms.lucene.domain.IndexerConfiguration;
import com.freshdirect.cms.lucene.domain.IndexingConstants;
import com.freshdirect.cms.ui.editor.index.domain.Synonym;
import com.freshdirect.cms.ui.editor.index.domain.SynonymDictionary;
import com.freshdirect.cms.ui.editor.search.service.SearchTermNormalizer;

@Service
public class IndexDocumentCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexDocumentCreator.class);

    @Autowired
    private IndexerUtil indexerUtil;

    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    /**
     * Converts a ContentNode to a Lucene document. Creates the document and builds it up field by field.
     *
     * @param node
     *            - the content node to convert to a Lucene document
     * @param primaryHomeKeywordsEnabled
     * @param parentRecursionEnabled
     * @return a Lucene document that can be added to an index and searched or null if this node should not be indexed
     */
    public Document createDocument(List<SynonymDictionary> synonyms, ContentKey nodeKey, Map<Attribute, Object> nodeAttributesWithValues, IndexerConfiguration indexerConfiguration,
            ContentKey storeKey) {
        
        List<AttributeIndex> indexes = indexConfiguration.getAllIndexConfigurationsByContentType().get(nodeKey.type);
        if (indexes == null) {
            return null;
        }
        
        Document document = new Document();
        document.add(new Field(IndexingConstants.FIELD_CONTENT_KEY, nodeKey.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field(IndexingConstants.FIELD_CONTENT_TYPE, nodeKey.type.name(), Field.Store.NO, Field.Index.ANALYZED));
        document.add(new Field(IndexingConstants.FIELD_CONTENT_ID, nodeKey.id, Field.Store.NO, Field.Index.ANALYZED));

        if (!indexes.isEmpty()) {
            List<String> fullContent = new ArrayList<String>();
            Set<String> keywords = new HashSet<String>();

            processIndexRules(document, indexes, nodeKey, indexerConfiguration, nodeAttributesWithValues, storeKey, synonyms, fullContent, keywords);

            fullContent.addAll(keywords);
            if (!fullContent.isEmpty()) {
                String term = StringUtils.join(fullContent, " ");
                document.add(new Field(IndexingConstants.NAME_PREFIX + IndexingConstants.FULL_CONTENT, term, Field.Store.NO, Field.Index.ANALYZED));
            }
        }
        return document;
    }

    /**
     * Creates lucene document for spelling indexing. Typically it is used to create index document from dictionary items
     *
     * @param searchTerm
     * @param spellingTerm
     * @param isSynonym
     * @return
     */
    public Document createDocumentForSpelling(String searchTerm, String spellingTerm, boolean isSynonym) {
        Document doc = new Document();
        doc.add(new Field(IndexingConstants.F_SEARCH_TERM, searchTerm, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstants.F_SPELLING_TERM, spellingTerm, Field.Store.YES, Field.Index.NOT_ANALYZED));
        if (isSynonym) {
            doc.add(new Field(IndexingConstants.F_IS_SYNONYM, "true", Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        addGram(searchTerm, doc);
        if (searchTerm.length() == 1)
            doc.add(new Field("single", searchTerm, Field.Store.NO, Field.Index.NOT_ANALYZED));
        return doc;
    }

    /**
     * Creates indexing documents from contentNodes
     *
     * @param contentNodes
     *            the contentNode collection to create the indexing documents from
     * @param indexerConfiguration
     *            IndexerConfiguration to drive the document creation
     * @param indexConfiguration
     *            indexDocument creation rules (which node attributes should be indexed and how)
     * @return a list of documents which can be handled by Lucene
     */
    public List<Document> createDocumentsFromNodes(Map<ContentKey, Map<Attribute, Object>> contentNodes, IndexerConfiguration indexerConfiguration, ContentKey storeKey) {

        SearchTermNormalizer searchTermNormalizer = new SearchTermNormalizer(false);
        List<SynonymDictionary> synonyms = indexerUtil.createSynonymDictionaries(IndexerUtil.SYNONYM_KEY, ContentType.Synonym, true, indexerConfiguration.isSynonymsDisabled(),
                indexerConfiguration.getContentProviderService(), searchTermNormalizer);
        
        /* TODO: reconstruct old logic as in SynonymPermuter
        SynonymDictionary permutations = new SynonymDictionary();
        for (ContentKey nodeKey : contentNodes.keySet()) {
            indexerUtil.collectPermutations(permutations, nodeKey, contentNodes.get(nodeKey), indexerConfiguration, storeKey);
        }
        LOGGER.info("Permuted " + contentNodes.size() + " nodes");
        synonyms.add(permutations);
        */

        List<Document> documents = new ArrayList<Document>();

        for (ContentKey nodeKey : contentNodes.keySet()) {
            Document document = createDocument(synonyms, nodeKey, contentNodes.get(nodeKey), indexerConfiguration, storeKey);
            if (document != null) {
                documents.add(document);
            }
        }

        return documents;
    }

    private void addGram(String text, Document doc) {
        int len = text.length();
        int ng1 = getMinGramLength(len);
        int ng2 = getMaxGramLength(len);
        for (int ng = ng1; ng <= ng2; ng++) {
            String key = "gram" + ng;
            String end = null;
            for (int i = 0; i < len - ng + 1; i++) {
                String gram = text.substring(i, i + ng);
                doc.add(new Field(key, gram, Field.Store.NO, Field.Index.NOT_ANALYZED));
                if (i == 0) {
                    doc.add(new Field("start" + ng, gram, Field.Store.NO, Field.Index.NOT_ANALYZED));
                }
                end = gram;
            }
            if (end != null) { // may not be present if len==ng1
                doc.add(new Field("end" + ng, end, Field.Store.NO, Field.Index.NOT_ANALYZED));
            }
        }
    }

    private int getMaxGramLength(int l) {
        if (l > 5) {
            return 4;
        }
        if (l == 5) {
            return 3;
        }
        return Math.min(2, l);
    }

    private int getMinGramLength(int l) {
        if (l > 7) {
            return 3;
        }
        if (l > 5) {
            return 2;
        }
        if (l == 5) {
            return 1;
        }
        return 1;
    }

    private void processIndexRules(Document document, List<AttributeIndex> indexes, ContentKey nodeKey, IndexerConfiguration indexerConfiguration,
            Map<Attribute, Object> nodeAttributesWithValues, ContentKey storeKey, List<SynonymDictionary> synonyms, List<String> fullContent, Set<String> keywords) {
        for (AttributeIndex attributeIndex : indexes) {
            String attributeName = attributeIndex.getAttribute().getName();
            String relationshipAttributeName = attributeIndex.getRelationshipAttributeName();
            boolean isKeyword = relationshipAttributeName != null && relationshipAttributeName.toLowerCase().startsWith(IndexingConstants.KEYWORD)
                    || attributeName.toLowerCase().startsWith(IndexingConstants.KEYWORD);
            if (relationshipAttributeName != null)
                attributeName += "_" + relationshipAttributeName;

            List<String> values = IndexerUtil.collectValues(nodeKey, nodeAttributesWithValues, attributeIndex, !indexerConfiguration.isKeywordsDisabled(),
                    indexerConfiguration.isPrimaryHomeKeywordsEnabled(), indexerConfiguration.isRecurseParentAttributesEnabled(), indexerConfiguration.getContentProviderService(),
                    storeKey);

            for (String value : values) {
                addValueToDocument(document, value, synonyms, attributeIndex, isKeyword, attributeName, nodeKey, fullContent, keywords);
            }
        }
    }

    private List<String> generateSynonymValues(List<SynonymDictionary> dictionaries, String normalizedValue) {
        List<String> terms = new ArrayList<String>();
        String[] tokens = StringUtils.split(normalizedValue);
        terms.add(normalizedValue);
        if (dictionaries != null) {
            for (SynonymDictionary dictionary : dictionaries) {
                for (String token : tokens) {
                    Synonym[] synonymsForToken = dictionary.getSynonymsForPrefix(token);
                    for (Synonym synonym : synonymsForToken) {
                        if (normalizedValue.contains(synonym.getWord())) {
                            terms.addAll(synonym.getSynonymsOfWord());
                        }
                    }
                }
            }
        }
        return terms;
    }

    private void addValueToDocument(Document document, String value, List<SynonymDictionary> synonyms, AttributeIndex attributeIndex, boolean isKeyword, String attributeName,
            ContentKey nodeKey, List<String> fullContent, Set<String> keywords) {
        if (attributeIndex.isText()) {
            document.add(new Field(IndexingConstants.TEXT_PREFIX + attributeName, value, Field.Store.YES, Field.Index.ANALYZED));
        } else {
            SearchTermNormalizer searchTermNormalizer = new SearchTermNormalizer(false);
            String normalizedValue = searchTermNormalizer.convert(value);

            List<String> terms = generateSynonymValues(synonyms, normalizedValue);

            if (isKeyword) {
                keywords.addAll(terms);
            } else {
                fullContent.addAll(terms);
            }
            if (terms.size() > 32) {
                LOGGER.debug("too many permutations for " + nodeKey + "." + attributeName + "(" + terms.size() + ")");
            }

            for (String term : terms) {
                document.add(new Field(IndexingConstants.NAME_PREFIX + attributeName, term, Field.Store.YES, Field.Index.ANALYZED));
            }
        }
    }
}
