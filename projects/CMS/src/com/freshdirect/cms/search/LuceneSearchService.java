package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
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

import org.apache.log4j.Category;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.index.FreshdirectAnalyzer;
import com.freshdirect.cms.index.IndexingConstants;
import com.freshdirect.cms.index.IndexingNotifier;
import com.freshdirect.cms.index.IndexingSubscriber;
import com.freshdirect.cms.index.configuration.IndexConfiguration;
import com.freshdirect.cms.search.configuration.SearchServiceConfiguration;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.term.ApproximationsPermuter;
import com.freshdirect.cms.search.term.SearchTermNormalizer;
import com.freshdirect.cms.search.term.SpellingTermNormalizer;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;
import com.freshdirect.framework.util.PermutationGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Implementation of {@link com.freshdirect.cms.search.ContentSearchServiceI} with the Lucene search engine. See:
 * 
 * <blockquote>
 * 
 * <pre>
 *     <a href="http://lucene.apache.org/">http://lucene.apache.org/</a>
 * </pre>
 * 
 * </blockquote>
 * 
 * Indexing behavior is configured via {@link com.freshdirect.cms.search.ContentIndex} objects.
 * 
 * @FIXME Searches are always performed on the these fields only: <code>FULL_NAME, AKA, GLANCE_NAME, KEYWORDS</code>
 */
public class LuceneSearchService implements ContentSearchServiceI, IndexingSubscriber {

    private static final Category LOGGER = LoggerFactory.getInstance(LuceneSearchService.class);

    private static final FreshdirectAnalyzer ANALYZER = new FreshdirectAnalyzer();

    private IndexConfiguration indexConfiguration = IndexConfiguration.getInstance();

    private SearchServiceConfiguration searchServiceConfiguration = SearchServiceConfiguration.getInstance();

    private Map<ContentType, List<AttributeIndex>> contentIndexes = new HashMap<ContentType, List<AttributeIndex>>();

    private String indexLocation;

    private FSDirectory indexDirectory;

    private IndexReader reader;

    private SpellingSuggestionsServiceI spellService;

    public LuceneSearchService() {
        setIndexes(indexConfiguration.getIndexConfiguration());

        this.indexLocation = searchServiceConfiguration.getCmsIndexLocation();

        IndexWriter writer = null;
        try {
            boolean exists = IndexReader.indexExists(getIndexDirectory());
            
            if (!exists) {
                LOGGER.info("Creating index at " + getIndexLocation());
                writer = new IndexWriter(getIndexDirectory(), IndexingConstants.ANALYZER, true, IndexingConstants.MAX_FIELD_LENGTH_1024);
                writer.optimize();
                LOGGER.info("Created index at " + getIndexLocation());
            }
        } catch (IOException ioe) {
            throw new CmsRuntimeException(ioe);
        } finally {
            closeIndexWriter(writer);
        }
        IndexingNotifier.getInstance().subscribe(this);
    }

    /**
     * @return path to index directory
     */
    public String getIndexLocation() {
        return indexLocation;
    }

    public synchronized FSDirectory getIndexDirectory() throws IOException {
        if (indexDirectory == null) {
            indexDirectory = FSDirectory.open(new File(indexLocation));
        }
        return indexDirectory;
    }

    /**
     * @param indexLocation
     *            path to index directory
     */
    public void setIndexLocation(String indexLocation) {
        synchronized (this) {
            if (indexDirectory != null) {
                indexDirectory.close();
                indexDirectory = null;
            }
        }
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
        ContentType cType = ContentType.get(idx.getContentType());
        List<AttributeIndex> indexes = contentIndexes.get(cType);
        if (indexes == null) {
            indexes = new ArrayList<AttributeIndex>();
            contentIndexes.put(cType, indexes);
        }
        if (idx instanceof AttributeIndex) {
            indexes.add((AttributeIndex) idx);
        }
    }

    @Override
    public Set<ContentType> getSearchableContentTypes() {
        return contentIndexes.keySet();
    }

    private IndexReader createReader(boolean readOnly) throws IOException {
        return IndexReader.open(getIndexDirectory(), readOnly);
    }

    private synchronized IndexReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = createReader(true);
        }
        return this.reader;
    }

    private synchronized void closeReader() {
        if (this.reader != null) {
            try {
                this.reader.close();
                this.reader = null;
                LOGGER.info("reader is closed and set to " + this.reader);
                if (indexDirectory != null) {
                    indexDirectory.close();
                    indexDirectory = null;
                    LOGGER.info("indexDirectory is closed and set to " + this.reader);
                }
            } catch (IOException e) {
                LOGGER.error("failed to close reader", e);
            }
        } else {
            LOGGER.info("reader is already closed");
        }
    }

    @Override
    public synchronized SpellingSuggestionsServiceI getSpellService() {
        if (this.spellService == null) {
            this.spellService = new LuceneSpellingSuggestionService(indexLocation);
        }
        return this.spellService;
    }

    @Override
    public Collection<SearchHit> search(String searchTerm, boolean phrase, int maxHits) {
        try {
            Set<String> fields = new HashSet<String>();
            fields.add(IndexingConstants.FIELD_CONTENT_KEY);
            fields.add(IndexingConstants.FIELD_CONTENT_TYPE);
            fields.add(IndexingConstants.FIELD_CONTENT_ID);
            for (List<AttributeIndex> indexes : contentIndexes.values()) {
                for (AttributeIndex index : indexes) {
                    if (index.isText())
                        fields.add(IndexingConstants.TEXT_PREFIX + index.getAttributeName());
                    else if (index.getRelationshipAttributeName() != null)
                        fields.add(IndexingConstants.NAME_PREFIX + index.getAttributeName() + "_" + index.getRelationshipAttributeName());
                    else
                        fields.add(IndexingConstants.NAME_PREFIX + index.getAttributeName());
                }
            }
            if (!phrase)
                fields.add(IndexingConstants.NAME_PREFIX + IndexingConstants.FULL_CONTENT);

            return searchInternal(searchTerm, fields, maxHits, phrase, false);
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
    }

    @Override
    public Collection<SearchHit> searchFaqs(String searchTerm, boolean phrase, int maxHits) {
        try {
            List<AttributeIndex> indexes = new ArrayList<AttributeIndex>();
            if (contentIndexes.containsKey(FDContentTypes.FAQ))
                indexes.addAll(contentIndexes.get(FDContentTypes.FAQ));

            // we are adding the text fields only
            Set<String> fields = new HashSet<String>();
            for (AttributeIndex index : indexes) {
                if (index.isText())
                    fields.add(IndexingConstants.TEXT_PREFIX + index.getAttributeName());
            }

            return searchInternal(searchTerm, fields, maxHits, phrase, false);
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
    }

    @Override
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
            throw new CmsRuntimeException(e);
        }
    }

    @Override
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
            throw new CmsRuntimeException(e);
        }
    }

    private Set<String> constructSearchFieldsForIndexes(List<AttributeIndex> indexes) {
        Set<String> fields = new HashSet<String>();
        for (AttributeIndex index : indexes) {
            if (index.isText())
                fields.add(IndexingConstants.TEXT_PREFIX + index.getAttributeName());
            else if (index.getRelationshipAttributeName() != null)
                fields.add(IndexingConstants.NAME_PREFIX + index.getAttributeName() + "_" + index.getRelationshipAttributeName());
            else
                fields.add(IndexingConstants.NAME_PREFIX + index.getAttributeName());
        }
        return fields;
    }

    private Collection<SearchHit> searchInternal(String searchString, Set<String> fields, int maxHits, boolean phrase, boolean approximate)
            throws IOException, CorruptIndexException {
        int slop = phrase ? 0 : 500;
        searchString = searchString.trim();
        Term searchTerm = new Term(searchString);

        IndexSearcher searcher = new IndexSearcher(getReader());
        try {
            if (approximate) {
                if (searchTerm.getTokens().size() > 1) {
                    // start approximations
                    List<List<Term>> permutations = new ApproximationsPermuter(searchTerm).permute();
                    int i = 1;
                    for (List<Term> permutation : permutations) {
                        BooleanQuery q = new BooleanQuery();
                        for (Term queryTerm : permutation)
                            q.add(createQuery(queryTerm, fields, slop), BooleanClause.Occur.SHOULD);

                        TopDocs hits = searcher.search(q, maxHits);

                        if (hits.totalHits != 0) {
                            return extractSearchHits(maxHits, searcher, hits, i);
                        }
                        i++;
                    }
                }
                return Collections.emptyList();
            } else {
                Query query = createQuery(searchTerm, fields, slop);

                TopDocs hits = searcher.search(query, maxHits);

                return extractSearchHits(maxHits, searcher, hits, 0);
            }
        } finally {
            searcher.close();
        }
    }

    private BooleanQuery createQuery(Term searchTerm, Set<String> fields, int slop) {
        BooleanQuery query = new BooleanQuery();
        QueryParser parser = new QueryParser(Version.LUCENE_30, IndexingConstants.FIELD_CONTENT_KEY, ANALYZER);
        Term normalizedTerm = new SearchTermNormalizer(searchTerm, false, true).getTerms().get(0);
        for (String field : fields) {
            Query q;
            String queryString = field.startsWith(IndexingConstants.NAME_PREFIX) ? normalizedTerm.toString() : searchTerm.toString();
            queryString = QueryParser.escape(queryString);
            try {
                q = parser.parse(field + ":\"" + queryString + "\"~" + slop);
            } catch (ParseException e) {
                throw new CmsRuntimeException(e);
            }
            query.add(q, BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    private Collection<SearchHit> extractSearchHits(int maxHits, IndexSearcher searcher, TopDocs hits, int approximationLevel) throws CorruptIndexException, IOException {
        Set<SearchHit> h = new LinkedHashSet<SearchHit>(hits.totalHits);
        int max = Math.min(maxHits, hits.totalHits);
        for (int i = 0; i < max; i++) {
            Document doc = searcher.doc(hits.scoreDocs[i].doc);

            ContentKey key = ContentKey.getContentKey(doc.get(IndexingConstants.FIELD_CONTENT_KEY));

            h.add(new SearchHit(key, hits.scoreDocs[i].score, approximationLevel));
        }
        return h;
    }

    @Override
    public Collection<SpellingHit> suggestSpelling(String searchTerm, double threshold, int maxHits) {
        List<SpellingHit> hits = Collections.emptyList();
        TermCoder filter = new SpellingTermNormalizer(new Term(searchTerm), true);
        if (!filter.getTerms().isEmpty()) {
            List<String> words = filter.getTerms().get(0).getTokens();
            String reconstructed = filter.getTerms().get(0).toString();
            if (words.isEmpty())
                return Collections.emptyList();

            hits = suggestSpellingInternal(reconstructed, threshold, maxHits);

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

    @Override
    public Collection<SpellingHit> reconstructSpelling(String searchTerm, double threshold, int maxHits) {
        List<SpellingHit> candidates = new ArrayList<SpellingHit>();
        TermCoder filter = new SpellingTermNormalizer(new Term(searchTerm), true);
        if (!filter.getTerms().isEmpty()) {
            List<String> words = filter.getTerms().get(0).getTokens();

            List<List<SpellingHit>> particles = generateSpellingParticles(words, threshold, maxHits);

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

    @Override
    public List<List<SpellingHit>> generateSpellingParticles(List<String> searchPhrase, double threshold, int maxHits) {
        List<List<SpellingHit>> particles = new ArrayList<List<SpellingHit>>();
        List<String> remaining = new ArrayList<String>(searchPhrase);
        while (!remaining.isEmpty()) {
            particles.add(new ArrayList<SpellingHit>()); // fake candidate
            int last = particles.size() - 1;
            int bestMatchSize = 0;
            for (int i = 0; i < Math.min(remaining.size(), 3); i++) {
                int j = i + 1;
                String subTerm = Term.join(remaining.subList(0, j), Term.DEFAULT_SEPARATOR);
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

    @Override
    public List<SpellingHit> suggestSpellingInternal(String searchTerm, double threshold, int maxHits) {
        List<SpellingHit> spellingHits = getSpellService().getSpellingHits(searchTerm, maxHits);
        List<SpellingHit> results = SpellingUtils.filterBestSpellingHits(spellingHits, threshold);
        Iterator<SpellingHit> it = results.iterator();
        List<String> original = Term.tokenize(searchTerm, Term.DEFAULT_TOKENIZERS);
        while (it.hasNext()) {
            if (!SpellingUtils.checkPartialThreshold(original, Term.tokenize(it.next().getSpellingMatch(), Term.DEFAULT_TOKENIZERS), threshold, getSpellService()))
                it.remove();
        }
        return results;
    }

    @Override
    public void receiveIndexingFinishedNotification() {
        try {
            closeReader();
            getReader();
            getSpellService();
        } catch (IOException e) {
            throw new CmsRuntimeException(e);
        }
    }
    
    private void closeIndexWriter(IndexWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error("Exception while closing index writer", e);
        }
    }

}
