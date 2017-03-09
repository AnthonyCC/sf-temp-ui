package com.freshdirect.cms.index.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.index.IndexingConstants;
import com.freshdirect.cms.index.configuration.IndexConfiguration;
import com.freshdirect.cms.index.configuration.IndexerConfiguration;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.SearchUtils;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.search.SynonymDictionaryListKey;
import com.freshdirect.cms.search.term.SearchTermNormalizer;
import com.freshdirect.cms.search.term.SynonymPermuter;
import com.freshdirect.cms.search.term.SynonymSearchTermNormalizerFactory;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;
import com.freshdirect.framework.util.log.LoggerFactory;

public class IndexDocumentCreator {

    private static final String KEYWORD = "keyword";

    private static final IndexDocumentCreator INSTANCE = new IndexDocumentCreator();

    private static final Logger LOGGER = LoggerFactory.getInstance(IndexDocumentCreator.class);

    public static IndexDocumentCreator getInstance() {
        return INSTANCE;
    }

    private final IndexerUtil indexerUtil = IndexerUtil.getInstance();
    private final IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    /**
     * Converts a ContentNode to a Lucene document. Creates the document and builds it up field by field.
     * 
     * @param node
     *            the content node to convert to a Lucene document
     * @param primaryHomeKeywordsEnabled
     * @param parentRecursionEnabled
     * @return a Lucene document that can be added to an index and searched or null if this node should not be indexed
     */
    public Document createDocument(List<SynonymDictionary> synonyms, ContentNodeI node, boolean primaryHomeKeywordsEnabled, boolean parentRecursionEnabled,
            boolean keywordsDisabled, StoreContentSource storeContentSource) {
        List<AttributeIndex> indexes = indexConfiguration.getAllIndexConfigurationsByContentType().get(node.getKey().getType());
        Document document = null;

        if (indexes != null) {
            document = new Document();

            document.add(new Field(IndexingConstants.FIELD_CONTENT_KEY, node.getKey().getEncoded(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            document.add(new Field(IndexingConstants.FIELD_CONTENT_TYPE, node.getKey().getType().getName(), Field.Store.NO, Field.Index.ANALYZED));
            document.add(new Field(IndexingConstants.FIELD_CONTENT_ID, node.getKey().getId(), Field.Store.NO, Field.Index.ANALYZED));

            List<Term> fullContent = new ArrayList<Term>();
            Set<Term> keywords = new HashSet<Term>();
            for (AttributeIndex attributeIndex : indexes) {
                String attributeName = attributeIndex.getAttributeName();
                String relationshipAttributeName = attributeIndex.getRelationshipAttributeName();
                boolean isKeyword = relationshipAttributeName != null && relationshipAttributeName.toLowerCase().startsWith(KEYWORD)
                        || attributeName.toLowerCase().startsWith(KEYWORD);
                if (relationshipAttributeName != null)
                    attributeName += "_" + relationshipAttributeName;
                List<Term> values = SearchUtils.collectValues(node, attributeIndex, !keywordsDisabled, primaryHomeKeywordsEnabled, parentRecursionEnabled, storeContentSource);

                for (Term value : values) {
                    if (attributeIndex.isText()) {
                        document.add(new Field(IndexingConstants.TEXT_PREFIX + attributeName, value.toString(), Field.Store.YES, Field.Index.ANALYZED));
                    } else {
                        TermCoder filter = new SearchTermNormalizer(value);
                        if (synonyms != null)
                            for (SynonymDictionary dictionary : synonyms)
                                filter = new SynonymPermuter(dictionary, filter);
                        // keywords may generate too many permutations
                        List<Term> terms = filter.getTerms();
                        if (isKeyword) {
                            keywords.addAll(terms);
                        } else {
                            fullContent.addAll(terms);
                        }
                        if (terms.size() > 32)
                            LOGGER.debug("too many permutations for " + node.getKey() + "." + attributeName + "(" + terms.size() + ")");
                        for (Term term : terms)
                            document.add(new Field(IndexingConstants.NAME_PREFIX + attributeName, term.toString(), Field.Store.YES, Field.Index.ANALYZED));
                    }
                }
            }
            fullContent.addAll(keywords);
            if (!fullContent.isEmpty()) {
                String term = Term.join(fullContent);
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
    public List<Document> createDocumentsFromNodes(Collection<ContentNodeI> contentNodes, IndexerConfiguration indexerConfiguration) {

        SynonymDictionary permutations = new SynonymDictionary();
        for (ContentNodeI node : contentNodes) {
            indexerUtil.collectPermutations(permutations, node, indexerConfiguration);
        }
        LOGGER.info("Permuted " + contentNodes.size() + " nodes");

        List<SynonymDictionary> synonyms = indexerUtil.createSynonymDictionaries(new SynonymSearchTermNormalizerFactory(), indexerConfiguration.getCustomSynonyms(),
                SynonymDictionaryListKey.SYNONYM, FDContentTypes.SYNONYM, false, indexerConfiguration.isSynonymsDisabled(), indexerConfiguration.getStoreContentSource());
        synonyms.add(permutations);
        permutations.addSynonyms(SynonymDictionary.createNumberSynonyms());

        List<Document> documents = new ArrayList<Document>();

        for (ContentNodeI node : contentNodes) {
            Document document = createDocument(synonyms, node, indexerConfiguration.isPrimaryHomeKeywordsEnabled(), indexerConfiguration.isRecurseParentAttributesEnabled(),
                    indexerConfiguration.isKeywordsDisabled(), indexerConfiguration.getStoreContentSource());
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
}
