package com.freshdirect.cms.ui.editor.search.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.lucene.domain.AttributeIndex;
import com.freshdirect.cms.lucene.domain.ContentIndex;
import com.freshdirect.cms.lucene.domain.FreshdirectAnalyzer;
import com.freshdirect.cms.lucene.domain.IndexConfiguration;
import com.freshdirect.cms.lucene.domain.IndexingConstants;
import com.freshdirect.cms.lucene.service.LuceneManager;
import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.editor.search.domain.SearchHit;

/**
 * Search implementation, using the Lucene engine.
 */
@Service
public class LuceneSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneSearchService.class);

    private static final FreshdirectAnalyzer ANALYZER = IndexingConstants.ANALYZER;

    private IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    @Autowired
    private PropertyResolverService propertyResolverService;
    // private SearchServiceConfiguration searchServiceConfiguration = SearchServiceConfiguration.getInstance();

    private Map<ContentType, List<AttributeIndex>> contentIndexes = new HashMap<ContentType, List<AttributeIndex>>();

    private String indexLocation;

    @Autowired
    private LuceneManager luceneManager;

    @PostConstruct
    private void initService() {
        setIndexes(indexConfiguration.getIndexConfiguration());
        setIndexLocation(propertyResolverService.getCmsIndexPath());
        luceneManager.createDefaultIndex(indexLocation);
    }

    public void setIndexLocation(String indexLocation) {
        this.indexLocation = indexLocation;
    }

    /**
     * Set content indexing rules.
     *
     * @param descrs
     *            Collection of {@link ContentIndex}
     */
    public void setIndexes(Collection<ContentIndex> descrs) {
        for (ContentIndex idx : descrs) {
            this.addIndex(idx);
        }
    }

    /**
     * adds a single attribute to be added to the content indexing rules
     */
    private void addIndex(ContentIndex idx) {
        ContentType cType = idx.getContentType();
        List<AttributeIndex> indexes = contentIndexes.get(cType);
        if (indexes == null) {
            indexes = new ArrayList<AttributeIndex>();
            contentIndexes.put(cType, indexes);
        }
        if (idx instanceof AttributeIndex) {
            indexes.add((AttributeIndex) idx);
        }
    }

    public Set<ContentType> getSearchableContentTypes() {
        return contentIndexes.keySet();
    }

    public Collection<SearchHit> search(String searchTerm, boolean phrase, int maxHits) {
        try {
            Set<String> fields = new HashSet<String>();
            fields.add(IndexingConstants.FIELD_CONTENT_KEY);
            fields.add(IndexingConstants.FIELD_CONTENT_TYPE);
            fields.add(IndexingConstants.FIELD_CONTENT_ID);
            for (List<AttributeIndex> indexes : contentIndexes.values()) {
                for (AttributeIndex index : indexes) {
                    if (index.isText())
                        fields.add(IndexingConstants.TEXT_PREFIX + index.getAttribute().getName());
                    else if (index.getRelationshipAttributeName() != null)
                        fields.add(IndexingConstants.NAME_PREFIX + index.getAttribute().getName() + "_" + index.getRelationshipAttributeName());
                    else
                        fields.add(IndexingConstants.NAME_PREFIX + index.getAttribute().getName());
                }
            }
            if (!phrase)
                fields.add(IndexingConstants.NAME_PREFIX + IndexingConstants.FULL_CONTENT);

            return searchInternal(searchTerm, fields, maxHits, phrase, false);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while searching under {0} searchTerm {1} phrase {2} maxHits {3}", indexLocation, searchTerm, phrase, maxHits), e);
            throw new RuntimeException(e);
        }
    }

    private Collection<SearchHit> searchInternal(String searchString, Set<String> fields, int maxHits, boolean phrase, boolean approximate)
            throws IOException, CorruptIndexException {
        int slop = phrase ? 0 : 500;
        searchString = searchString.trim();
        List<String> searchTerm = new ArrayList<String>();
        for (String token : searchString.split(" ")) {
            searchTerm.add(token);
        }

        if (approximate) {
            if (searchTerm.size() > 1) {
                // start approximations
                List<List<List<String>>> permutations = new ApproximationsPermuter(searchTerm).permute();
                int i = 1;
                for (List<List<String>> permutation : permutations) {
                    BooleanQuery q = new BooleanQuery();
                    for (List<String> queryTerm : permutation)
                        q.add(createQuery(queryTerm, fields, slop), BooleanClause.Occur.SHOULD);

                    TopDocs hits = luceneManager.search(indexLocation, q, maxHits);

                    if (hits.totalHits != 0) {
                        return extractSearchHits(maxHits, hits, i);
                    }
                    i++;
                }
            }
            return Collections.emptyList();
        } else {
            Query query = createQuery(searchTerm, fields, slop);
            TopDocs hits = luceneManager.search(indexLocation, query, maxHits);
            return extractSearchHits(maxHits, hits, 0);
        }
    }

    private BooleanQuery createQuery(List<String> searchTerm, Set<String> fields, int slop) {
        BooleanQuery query = new BooleanQuery();
        QueryParser parser = new QueryParser(Version.LUCENE_30, IndexingConstants.FIELD_CONTENT_KEY, ANALYZER);
        String searchWords = StringUtils.join(searchTerm, " ");
        String normalizedTerm = new SearchTermNormalizer(true).convert(searchWords);
        for (String field : fields) {
            Query q;
            String queryString = field.startsWith(IndexingConstants.NAME_PREFIX) ? normalizedTerm : searchWords;
            queryString = QueryParser.escape(queryString);
            try {
                q = parser.parse(field + ":\"" + queryString + "\"~" + slop);
            } catch (ParseException e) {
                LOGGER.error(MessageFormat.format("Exception while creating query searchTerm {0} fields {1} slop {2}", searchTerm, fields, slop), e);
                throw new RuntimeException(e);
            }
            query.add(q, BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    private Collection<SearchHit> extractSearchHits(int maxHits, TopDocs hits, int approximationLevel) {
        List<Document> hitDocuments = luceneManager.convertSearchHits(indexLocation, hits, maxHits);
        Set<SearchHit> searchHits = new LinkedHashSet<SearchHit>(hitDocuments.size());
        for (int i = 0; i < hitDocuments.size(); i++) {
            ContentKey key = ContentKeyFactory.get(hitDocuments.get(i).get(IndexingConstants.FIELD_CONTENT_KEY));
            searchHits.add(new SearchHit(key, hits.scoreDocs[i].score, approximationLevel));
        }
        return searchHits;
    }

}
