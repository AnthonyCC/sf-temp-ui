package com.freshdirect.storeapi.search.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.lucene.domain.AttributeIndex;
import com.freshdirect.cms.lucene.domain.ContentIndex;
import com.freshdirect.cms.lucene.domain.IndexConfiguration;
import com.freshdirect.cms.lucene.domain.IndexingConstants;
import com.freshdirect.cms.lucene.service.LuceneManager;
import com.freshdirect.framework.util.PermutationGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.storeapi.search.SearchHit;
import com.freshdirect.storeapi.spelling.domain.SpellingHit;
import com.freshdirect.storeapi.spelling.service.LuceneSpellingSuggestionService;
import com.freshdirect.storeapi.spelling.service.SpellingSuggestionsServiceI;
import com.freshdirect.storeapi.spelling.service.SpellingUtils;
import com.freshdirect.storeapi.term.service.TermNormalizer;

@Service
public class LuceneSearchService {

    private static final Logger LOGGER = LoggerFactory.getInstance(LuceneSearchService.class);

    private IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    private Map<ContentType, List<AttributeIndex>> contentIndexes = new HashMap<ContentType, List<AttributeIndex>>();

    @Value("${cms.index.path}")
    private String indexLocation;

    @Autowired
    private LuceneManager luceneManager;

    @Autowired
    private LuceneSpellingSuggestionService spellingSuggestionService;

    @Autowired
    private TermTokenizer termTokenizer;

    @PostConstruct
    private void initService() {
        setIndexes(indexConfiguration.getIndexConfiguration());
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

    public Collection<SearchHit> searchFaqs(String searchTerm, boolean phrase, int maxHits) {
        try {
            List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
            if (contentIndexes.containsKey(FDContentTypes.FAQ))
                indexes.addAll(contentIndexes.get(FDContentTypes.FAQ));

            // we are adding the text fields only
            Set<String> fields = new HashSet<String>();
            for (AttributeIndex index : indexes) {
                if (index.isText())
                    fields.add(IndexingConstants.TEXT_PREFIX + index.getAttribute().getName());
            }

            return searchInternal(searchTerm, fields, maxHits, phrase, false);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while searching faqs under {0} searchTerm {1} phrase {2} maxHits {3}", indexLocation, searchTerm, phrase, maxHits), e);
            throw new RuntimeException(e);
        }
    }

    public Collection<SearchHit> searchRecipes(String searchTerm, boolean phrase, int maxHits) {
        try {
            Set<String> fields;
            if (phrase) {
                List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
                if (contentIndexes.containsKey(FDContentTypes.RECIPE))
                    indexes.addAll(contentIndexes.get(FDContentTypes.RECIPE));
                fields = constructSearchFieldsForIndexes(indexes);
            } else {
                // we know we search only for logical AND of words
                // Recipe has only NAME type fields
                // FULL_CONTENT contains the concatenation of all NAME type
                // fields
                fields = Collections.singleton(IndexingConstants.NAME_PREFIX + IndexingConstants.FULL_CONTENT);
            }

            return searchInternal(searchTerm, fields, maxHits, phrase, false);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while searching recipes under {0} searchTerm {1} phrase {2} maxHits {3}", indexLocation, searchTerm, phrase, maxHits), e);
            throw new RuntimeException(e);
        }
    }

    public Collection<SearchHit> searchProducts(String searchTerm, boolean phrase, boolean approximate, int maxHits) {
        try {
            Set<String> fields;
            if (phrase) {
                List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
                if (contentIndexes.containsKey(FDContentTypes.PRODUCT))
                    indexes.addAll(contentIndexes.get(FDContentTypes.PRODUCT));
                if (contentIndexes.containsKey(FDContentTypes.RECIPE))
                    indexes.addAll(contentIndexes.get(FDContentTypes.RECIPE));
                if (contentIndexes.containsKey(FDContentTypes.CATEGORY))
                    indexes.addAll(contentIndexes.get(FDContentTypes.CATEGORY));
                fields = constructSearchFieldsForIndexes(indexes);
            } else {
                // we know we search only for logical AND of words
                // Recipe, Category, and Product have only NAME type fields
                // FULL_CONTENT contains the concatenation of all NAME type
                // fields
                fields = Collections.singleton(IndexingConstants.NAME_PREFIX + IndexingConstants.FULL_CONTENT);
            }

            return searchInternal(searchTerm, fields, maxHits, phrase, approximate);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Exception while searching products under {0} searchTerm {1} phrase {2} maxHits {3} approximate {4}", indexLocation, searchTerm,
                    phrase, maxHits, approximate), e);
            throw new RuntimeException(e);
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
        QueryParser parser = new QueryParser(Version.LUCENE_30, IndexingConstants.FIELD_CONTENT_KEY, IndexingConstants.ANALYZER);
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

    public Collection<SpellingHit> suggestSpelling(String searchTerm, double threshold, int maxHits) {
        List<SpellingHit> hits = Collections.emptyList();
        TermNormalizer normalizer = new SpellingTermNormalizer(true);
        String normalizedSearchTerm = normalizer.convert(searchTerm);
        if (!normalizedSearchTerm.isEmpty()) {
            String[] words = normalizedSearchTerm.split(" ");
            if (words.length == 0)
                return Collections.emptyList();

            hits = suggestSpellingInternal(normalizedSearchTerm, threshold, maxHits);

            // remove exact suggestions
            Iterator<SpellingHit> it = hits.iterator();
            while (it.hasNext()) {
                SpellingHit hit = it.next();
                if (hit.getDistance() == 0)
                    it.remove();
            }
        }
        return hits;
    }

    public Collection<SpellingHit> reconstructSpelling(String searchTerm, double threshold, int maxHits) {
        List<SpellingHit> candidates = new ArrayList<SpellingHit>();
        TermNormalizer normalizer = new SpellingTermNormalizer(true);
        String normalizedSearchTerm = normalizer.convert(searchTerm);
        if (!normalizedSearchTerm.isEmpty()) {
            String[] words = normalizedSearchTerm.split(" ");
            List<String> searchWords = new ArrayList<String>();
            for (String word : words) {
                searchWords.add(word);
            }

            List<List<SpellingHit>> particles = generateSpellingParticles(searchWords, threshold, maxHits);

            // filter the permutations by retaining the exact and first two
            // levels of spelling hits
            for (int i = 0; i < particles.size(); i++) {
                List<SpellingHit> particle = particles.get(i);
                Collections.sort(particle, SpellingHit.SORT_BY_DISTANCE);
                particle = SpellingUtils.filterBestSpellingHits(particle, threshold);
                particles.set(i, particle);
            }
            // permuting spelling hit parts candidates
            for (PermutationGenerator pg = new PermutationGenerator(particles, 1000); pg.hasMoreStep(); pg.step()) {
                List<SpellingHit> candidate = new ArrayList<SpellingHit>(particles.size());
                for (int i = 0; i < particles.size(); i++)
                    candidate.add(particles.get(i).get(pg.get(i)));
                candidates.add(SpellingHit.join(candidate));
            }
            // sorting using the least score first principle
            Collections.sort(candidates);
            // return up to maxHits candidates
            while (candidates.size() > maxHits)
                candidates.remove(candidates.size() - 1);
        }
        return candidates;
    }

    public List<List<SpellingHit>> generateSpellingParticles(List<String> searchPhrase, double threshold, int maxHits) {
        List<List<SpellingHit>> particles = new ArrayList<List<SpellingHit>>();
        List<String> remaining = new ArrayList<String>(searchPhrase);
        while (!remaining.isEmpty()) {
            particles.add(new ArrayList<SpellingHit>()); // fake candidate
            int last = particles.size() - 1;
            int bestMatchSize = 0;
            for (int i = 0; i < Math.min(remaining.size(), 3); i++) {
                int j = i + 1;
                String subTerm = StringUtils.join(remaining.subList(0, j), " ");
                List<SpellingHit> parts = suggestSpellingInternal(subTerm, threshold, maxHits);
                if (!parts.isEmpty() && SpellingHit.bestScore(parts) >= SpellingHit.bestScore(particles.get(last))) {
                    particles.set(last, parts);
                    bestMatchSize = j;
                } else {
                    break;
                }
            }
            if (bestMatchSize == 0) {
                // we haven't found any proper match therefore returning the
                // original word with zero distance
                // it'll always be filtered by product search but approximations
                // will still be given
                List<SpellingHit> particle = new ArrayList<SpellingHit>(1);
                particle.add(new SpellingHit(remaining.get(0), 0));
                particles.set(last, particle);
                remaining.remove(0);
            } else
                for (int i = 0; i < bestMatchSize; i++)
                    remaining.remove(0);
        }
        return particles;
    }

    public List<SpellingHit> suggestSpellingInternal(String searchTerm, double threshold, int maxHits) {
        List<SpellingHit> spellingHits = getSpellService().getSpellingHits(indexLocation, searchTerm, maxHits);
        List<SpellingHit> results = SpellingUtils.filterBestSpellingHits(spellingHits, threshold);
        Iterator<SpellingHit> it = results.iterator();

        List<String> original = termTokenizer.tokenize(searchTerm, TermTokenizer.DEFAULT_TOKENIZERS);

        while (it.hasNext()) {
            if (!SpellingUtils.checkPartialThreshold(original, termTokenizer.tokenize(it.next().getSpellingMatch(), TermTokenizer.DEFAULT_TOKENIZERS), threshold,
                    getSpellService().getStringDistance()))
                it.remove();
        }
        return results;
    }

    private SpellingSuggestionsServiceI getSpellService() {
        return spellingSuggestionService;
    }

    private Set<String> constructSearchFieldsForIndexes(List<AttributeIndex> indexes) {
        Set<String> fields = new HashSet<String>();
        for (AttributeIndex index : indexes) {
            if (index.isText())
                fields.add(IndexingConstants.TEXT_PREFIX + index.getAttribute().getName());
            else if (index.getRelationshipAttributeName() != null)
                fields.add(IndexingConstants.NAME_PREFIX + index.getAttribute().getName() + "_" + index.getRelationshipAttributeName());
            else
                fields.add(IndexingConstants.NAME_PREFIX + index.getAttribute().getName());
        }
        return fields;
    }
}
