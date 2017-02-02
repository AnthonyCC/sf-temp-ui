/*
 * Created on Feb 8, 2005
 *
 */
package com.freshdirect.cms.search;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
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
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.cms.search.term.ApproximationsPermuter;
import com.freshdirect.cms.search.term.SearchTermNormalizer;
import com.freshdirect.cms.search.term.SpellingTermNormalizer;
import com.freshdirect.cms.search.term.SynonymPermuter;
import com.freshdirect.cms.search.term.SynonymSearchTermNormalizerFactory;
import com.freshdirect.cms.search.term.SynonymSpellingTermNormalizerFactory;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;
import com.freshdirect.cms.search.term.TermCoderFactory;
import com.freshdirect.fdstore.FDStoreProperties;
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
public class LuceneSearchService implements ContentSearchServiceI {
	private final Category LOGGER = LoggerFactory.getInstance(LuceneSearchService.class);

	private final static FreshdirectAnalyzer ANALYZER = new FreshdirectAnalyzer();

	public final static String[] SPELLING_STOP_WORDS = { "a", "about", "above", "after", "again", "against", "all", "am", "an",
			"and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
			"but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't",
			"down", "during", "each", "few", "for", "free", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't",
			"having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how",
			"how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's",
			"me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other",
			"ought", "our", "ours ", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's",
			"should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves",
			"then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through",
			"to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were",
			"weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "with", "without", "who", "who's",
			"whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your",
			"yours", "yourself", "yourselves" };

	public static boolean isSpellingStopWord(String word) {
		//Configured in CMS.
		if(word != null){
			Set<ContentKey> stopwordsKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.STOPWORDS);
			if(stopwordsKeys != null){
		    	for(final ContentKey key:stopwordsKeys){
		    		ContentNodeI contentNode = CmsManager.getInstance().getContentNode(key);
		    		if(word.equals((String)contentNode.getAttributeValue("word"))){
		    			return true;
		    		}
		    	}
	    	}
		}
		//Default hardcoded checking.
		for (String s : SPELLING_STOP_WORDS)
			if (s.equals(word))
				return true;
		return false;
	}

	private Map<ContentType, List<AttributeIndex>> contentIndexes = new HashMap<ContentType, List<AttributeIndex>>();

	private String indexLocation = null;

	private FSDirectory indexDirectory = null;

	private IndexReader reader;

	SpellingSuggestionsServiceI spellService;

	private List<SynonymDictionary> customSynonyms;

	private List<SynonymDictionary> customSpellingSynonyms;

	private boolean synonymsDisabled;

	private boolean keywordsDisabled;

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
	public Set<ContentType> getIndexedTypes() {
		return contentIndexes.keySet();
	}

	@Override
	public List<AttributeIndex> getIndexesForType(ContentType type) {
		return contentIndexes.get(type);
	}

	private IndexReader createReader(boolean readOnly) throws IOException {
		return IndexReader.open(getIndexDirectory(), readOnly);
	}

	public void setSynonyms(List<SynonymDictionary> customSynonyms) {
		this.customSynonyms = customSynonyms;
	}

	public void setSpellingSynonyms(List<SynonymDictionary> customSpellingSynonyms) {
		this.customSpellingSynonyms = customSpellingSynonyms;
	}

	@Override
	public synchronized void index(Collection<ContentNodeI> contentNodes, boolean rebuild) {
		try {
			// closing spell service before we bother the index
			if (spellService != null)
				spellService.close();
			spellService = null;

			// delete old documents
			if (!rebuild) {
				IndexReader localReader = createReader(false);
				int count = 0;
				for (Iterator<ContentNodeI> i = contentNodes.iterator(); i.hasNext();) {
					ContentNodeI node = i.next();
					// CHANGED count += reader.delete(new Term(FIELD_CONTENT_KEY, node.getKey().getEncoded()));
					count += localReader.deleteDocuments(new org.apache.lucene.index.Term(FIELD_CONTENT_KEY, node.getKey()
							.getEncoded()));

				}
				LOGGER.debug("Deleted " + count + " content nodes");
				localReader.close();
			}

			int count = 0;

			boolean primaryHomeKeywordsEnabled = FDStoreProperties.isPrimaryHomeKeywordsEnabled();
			boolean recurseParentAttributesEnabled = FDStoreProperties.isSearchRecurseParentAttributesEnabled();
			SynonymDictionary permutations = new SynonymDictionary();
			for (ContentNodeI node : contentNodes) {
				collectPermutations(permutations, node, primaryHomeKeywordsEnabled, recurseParentAttributesEnabled);

				count++;
				if (count % 1000 == 0) {
					LOGGER.info("Permuted " + count + " of " + contentNodes.size() + " so far");
				}
			}
			LOGGER.info("Permuted " + contentNodes.size() + " nodes");

			// index new documents
			IndexWriter writer = new IndexWriter(getIndexDirectory(), getAnalyzer(), false, new MaxFieldLength(1024));

			if (rebuild) {
				writer.deleteAll();
				writer.commit();
			}

			count = 0;
			List<SynonymDictionary> synonyms = new ArrayList<SynonymDictionary>();
			synonyms.add(permutations);
			LOGGER.info("Synonyms " + (synonymsDisabled ? "disabled" : "enabled"));
			LOGGER.info("Keywords " + (keywordsDisabled ? "disabled" : "enabled"));
			permutations.addSynonyms(SynonymDictionary.createNumberSynonyms());
			if (!synonymsDisabled) {
				if (customSynonyms != null)
					synonyms.addAll(customSynonyms);
				else {
					synonyms.add(SynonymDictionary.createFromCms(new SynonymSearchTermNormalizerFactory()));
				}
			}

			for (ContentNodeI node : contentNodes) {
				Document doc = createDocument(synonyms, node, primaryHomeKeywordsEnabled, recurseParentAttributesEnabled);
				if (doc != null)
					writer.addDocument(doc);

				count++;
				if (count % 1000 == 0) {
					LOGGER.info("Indexed " + count + " of " + contentNodes.size() + " so far");
				}

				if (count % 10000 == 0)
					System.gc();
			}

			writer.close();

			// replace old reader
			IndexReader oldReader = this.reader;
			this.reader = createReader(true);
			if (oldReader != null) {
				oldReader.close();
			}

			// reinitializing the spell service
			getSpellService();
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}

	@Override
	public synchronized void indexSpelling(Collection<ContentNodeI> contentNodes) {
		List<SynonymDictionary> synonymDictionaries = new ArrayList<SynonymDictionary>();
		synonymDictionaries.add(SynonymDictionary.createNumberSynonyms());
		List<SynonymDictionary> spellingDictionaries = new ArrayList<SynonymDictionary>();
		if (!synonymsDisabled) {
			TermCoderFactory factory = new SynonymSpellingTermNormalizerFactory();
			if (customSynonyms != null)
				synonymDictionaries.addAll(customSynonyms);
			else {
				synonymDictionaries.add(SynonymDictionary.createFromCms(factory));
			}
			if (customSpellingSynonyms != null)
				spellingDictionaries.addAll(customSpellingSynonyms);
			else {
				spellingDictionaries.add(SynonymDictionary.createSpellingFromCms(factory));
			}
		}
		spellService.indexTerms(contentNodes, synonymDictionaries, spellingDictionaries, keywordsDisabled);
	}

	public synchronized void optimize() {
		try {
			LOGGER.debug("Starting optimization process");

			// index new documents
			IndexWriter writer = new IndexWriter(getIndexDirectory(), getAnalyzer(), false, new MaxFieldLength(1024));

			writer.optimize();
			writer.close();

			// replace old reader
			IndexReader localReader = this.reader;
			this.reader = createReader(false);
			if (localReader != null) {
				localReader.close();
			}

			LOGGER.debug("Finished optimization process");
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}

	/**
	 * Collects permutations and stores them in a separate synonym dictionary
	 * 
	 * @param node
	 *            the content node to convert to a Lucene document
	 * @param primaryHomeKeywordsEnabled 
	 * @param recurseParentAttributesEnabled 
	 * @return a Lucene document that can be added to an index and searched or null if this node should not be indexed
	 */
	private void collectPermutations(SynonymDictionary permutations, ContentNodeI node, boolean primaryHomeKeywordsEnabled,
			boolean recurseParentAttributesEnabled) {
		List<AttributeIndex> indexes = contentIndexes.get(node.getKey().getType());
		if (indexes == null) {
			return;
		}

		for (AttributeIndex attrIndex : indexes) {
			List<Term> values = SearchUtils.collectValues(node, attrIndex, !keywordsDisabled, primaryHomeKeywordsEnabled,
					recurseParentAttributesEnabled);

			if (!attrIndex.isText() && !values.isEmpty()) {
				new SearchTermNormalizer(permutations, values).getTerms();
			}
		}
	}

	/**
	 * Converts a ContentNode to a Lucene document. Creates the document and builds it up field by field.
	 * 
	 * @param node
	 *            the content node to convert to a Lucene document
	 * @param primaryHomeKeywordsEnabled 
	 * @param parentRecursionEnabled 
	 * @return a Lucene document that can be added to an index and searched or null if this node should not be indexed
	 */
	private Document createDocument(List<SynonymDictionary> synonyms, ContentNodeI node, boolean primaryHomeKeywordsEnabled, boolean parentRecursionEnabled) {
		List<AttributeIndex> indexes = contentIndexes.get(node.getKey().getType());
		if (indexes == null) {
			return null;
		}

		Document document = new Document();

		document.add(new Field(FIELD_CONTENT_KEY, node.getKey().getEncoded(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		document.add(new Field(FIELD_CONTENT_TYPE, node.getKey().getType().getName(), Field.Store.NO, Field.Index.ANALYZED));
		document.add(new Field(FIELD_CONTENT_ID, node.getKey().getId(), Field.Store.NO, Field.Index.ANALYZED));

		List<Term> fullContent = new ArrayList<Term>();
		Set<Term> keywords = new HashSet<Term>();
		for (AttributeIndex attrIndex : indexes) {
			String attributeName = attrIndex.getAttributeName();
			String relationshipAttributeName = attrIndex.getRelationshipAttributeName();
			boolean isKeyword = relationshipAttributeName != null && relationshipAttributeName.toLowerCase().startsWith("keyword")
					|| attributeName.toLowerCase().startsWith("keyword");
			if (relationshipAttributeName != null)
				attributeName += "_" + relationshipAttributeName;
			List<Term> values = SearchUtils.collectValues(node, attrIndex, !keywordsDisabled, primaryHomeKeywordsEnabled, parentRecursionEnabled);

			for (Term value : values) {
				if (attrIndex.isText()) {
					document.add(new Field(TEXT_PREFIX + attributeName, value.toString(), Field.Store.YES, Field.Index.ANALYZED));
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
						LOGGER.info("too many permutations for " + node.getKey() + "." + attributeName + "(" + terms.size() + ")");
					for (Term term : terms)
						document.add(new Field(NAME_PREFIX + attributeName, term.toString(), Field.Store.YES, Field.Index.ANALYZED));
				}
			}
		}
		fullContent.addAll(keywords);
		if (!fullContent.isEmpty()) {
			String term = Term.join(fullContent);
			document.add(new Field(NAME_PREFIX + FULL_CONTENT, term, Field.Store.NO, Field.Index.ANALYZED));
		}

		return document;
	}

	public void initialize() {
		try {
			boolean exists = IndexReader.indexExists(getIndexDirectory());

			if (!exists) {
				LOGGER.info("Creating index at " + getIndexLocation());
				IndexWriter writer = new IndexWriter(getIndexDirectory(), getAnalyzer(), true, new MaxFieldLength(1024));
				writer.optimize();
				writer.close();
				LOGGER.info("Created index at " + getIndexLocation());
			}
		} catch (IOException ioe) {
			throw new CmsRuntimeException(ioe);
		}
	}

	@Override
	public synchronized IndexReader getReader() throws IOException {
		if (this.reader == null) {
			this.reader = createReader(true);
		}
		return this.reader;
	}

	@Override
	public synchronized void closeReader() {
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
		if (this.spellService == null)
			this.spellService = new LuceneSpellingSuggestionService(indexLocation, contentIndexes);
		return this.spellService;
	}

	@Override
	public Collection<SearchHit> search(String searchTerm, boolean phrase, int maxHits) {
		try {
			Set<String> fields = new HashSet<String>();
			fields.add(FIELD_CONTENT_KEY);
			fields.add(FIELD_CONTENT_TYPE);
			fields.add(FIELD_CONTENT_ID);
			for (List<AttributeIndex> indexes : contentIndexes.values()) {
				for (AttributeIndex index : indexes) {
					if (index.isText())
						fields.add(TEXT_PREFIX + index.getAttributeName());
					else if (index.getRelationshipAttributeName() != null)
						fields.add(NAME_PREFIX + index.getAttributeName() + "_" + index.getRelationshipAttributeName());
					else
						fields.add(NAME_PREFIX + index.getAttributeName());
				}
			}
			if (!phrase)
				fields.add(NAME_PREFIX + FULL_CONTENT);

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
					fields.add(TEXT_PREFIX + index.getAttributeName());
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
				// FULL_CONTENT contains the concatenation of all NAME type fields
				fields = Collections.singleton(NAME_PREFIX + FULL_CONTENT);
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
				// FULL_CONTENT contains the concatenation of all NAME type fields
				fields = Collections.singleton(NAME_PREFIX + FULL_CONTENT);
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
				fields.add(TEXT_PREFIX + index.getAttributeName());
			else if (index.getRelationshipAttributeName() != null)
				fields.add(NAME_PREFIX + index.getAttributeName() + "_" + index.getRelationshipAttributeName());
			else
				fields.add(NAME_PREFIX + index.getAttributeName());
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
		QueryParser parser = new QueryParser(Version.LUCENE_30, FIELD_CONTENT_KEY, getAnalyzer());
		Term normalizedTerm = new SearchTermNormalizer(searchTerm, false, true).getTerms().get(0);
		for (String field : fields) {
			Query q;
			String queryString = field.startsWith(NAME_PREFIX) ? normalizedTerm.toString() : searchTerm.toString();
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

	private Collection<SearchHit> extractSearchHits(int maxHits, IndexSearcher searcher, TopDocs hits, int approximationLevel)
			throws CorruptIndexException, IOException {
		Set<SearchHit> h = new LinkedHashSet<SearchHit>(hits.totalHits);
		int max = Math.min(maxHits, hits.totalHits);
		for (int i = 0; i < max; i++) {
			Document doc = searcher.doc(hits.scoreDocs[i].doc);

			ContentKey key = ContentKey.getContentKey(doc.get(FIELD_CONTENT_KEY));

			h.add(new SearchHit(key, hits.scoreDocs[i].score, approximationLevel));
		}
		return h;
	}

	public static class FreshdirectAnalyzer extends Analyzer {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

		public FreshdirectAnalyzer() {
			super();
		}

		@Override
		public TokenStream tokenStream(String field, Reader reader) {
			if (field.startsWith(NAME_PREFIX)) {
				WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
				TokenStream stemmer = new SnowballFilter(tokenizer, "English");
				return stemmer;
			} else if (field.startsWith(TEXT_PREFIX)) {
				return new SnowballFilter(analyzer.tokenStream(field, reader), "English");
			} else if (field.startsWith("__")) {
				return new org.apache.lucene.analysis.KeywordTokenizer(reader);
			} else {
				WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
				return tokenizer;
			}
		}
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

			// filter the permutations by retaining the exact and first two levels of spelling hits
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
				// we haven't found any proper match therefore returning the original word with zero distance
				// it'll always be filtered by product search but approximations will still be given
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
			if (!SpellingUtils.checkPartialThreshold(original, Term.tokenize(it.next().getSpellingMatch(), Term.DEFAULT_TOKENIZERS),
					threshold, getSpellService()))
				it.remove();
		}
		return results;
	}

	public Analyzer getAnalyzer() {
		return ANALYZER;
	}

	public void setSynonymsDisabled(boolean synonymsDisabled) {
		this.synonymsDisabled = synonymsDisabled;
	}

	public boolean isSynonymsDisabled() {
		return synonymsDisabled;
	}

	public void setKeywordsDisabled(boolean keywordsDisabled) {
		this.keywordsDisabled = keywordsDisabled;
	}

	public boolean isKeywordsDisabled() {
		return keywordsDisabled;
	}
}