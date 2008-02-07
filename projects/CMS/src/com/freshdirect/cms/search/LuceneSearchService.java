/*
 * Created on Feb 8, 2005
 *
 */
package com.freshdirect.cms.search;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Category;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.webdav.lib.methods.RebindMethod;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Implementation of {@link com.freshdirect.cms.search.ContentSearchServiceI}
 * with the Lucene search engine. See:
 * 
 * <blockquote><pre>
 *     <a href="http://lucene.apache.org/">http://lucene.apache.org/</a>
 * </pre></blockquote> 
 * 
 * Indexing behavior is configured via {@link com.freshdirect.cms.search.ContentIndex}
 * objects.
 * 
 * @FIXME Searches are always performed on the these fields only:
 * 	<code>FULL_NAME, AKA, GLANCE_NAME, KEYWORDS</code>
 */
public class LuceneSearchService implements ContentSearchServiceI {
	

	private final static Set stopWords = new HashSet(Arrays.asList(new String[] {"and","or","of","from","with"}));
	
	private final Category LOGGER = LoggerFactory.getInstance(LuceneSearchService.class);
	
	/** Field name to store content key as "ContentType:contentId" */
	public final static String FIELD_CONTENT_KEY = "_contentKey_";
	
	/** Field name to store content type */
	private final static String FIELD_CONTENT_TYPE = "_contentType_";
	
	/** Field name to store content id */
	private final static String FIELD_CONTENT_ID = "_contentId_";
	
	/** Field name to store the full name, untokenized */
	private final static String FULL_NAME_INDEX = "_fullname_";

	/** Suffix for fields that store the suffixed version of an attribute */
	private final static String STEMMED_SUFFIX = "_stemmed";

	/** Indexes to check. If one fails, reindex */
	private final static String[] CHECK_INDEXES = {
		FIELD_CONTENT_KEY, FULL_NAME_INDEX, "FULL_NAME", "AKA"
	};

	private final static StemmingAnalyzer STEMMER = new StemmingAnalyzer();

	/** Map of ContentType -> List of AttributeIndex */
	private Map contentIndexes = new HashMap();
	
	
	private String indexLocation = null;

	private IndexReader reader;
	
	private LuceneSpellingSuggestionService spellService;

	/**
	 * @return path to index directory
	 */
	public String getIndexLocation() {
		return indexLocation;
	}

	/**
	 * @param indexLocation path to index directory
	 */
	public void setIndexLocation(String indexLocation) {
		this.indexLocation = indexLocation;
	}

	/**
	 * Set content indexing rules.
	 * 
	 * @param descrs Collection of {@link ContentIndex}
	 */
	public void setIndexes(Collection descrs) {
		for (Iterator i = descrs.iterator(); i.hasNext();) {
			ContentIndex idx = (ContentIndex) i.next();
			this.addIndex(idx);
		}
	}

	/**
	 * adds a single attribute to be added to the content indexing rules
	 */
	private void addIndex(ContentIndex idx) {
		ContentType cType = ContentType.get(idx.getContentType());
		List indexes = (List) contentIndexes.get(cType);
		if (indexes == null) {
			indexes = new ArrayList();
			contentIndexes.put(cType, indexes);
		}
		if (idx instanceof AttributeIndex) {
			indexes.add(idx);
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.search.ContentSearchServiceI#getIndexedTypes()
	 */
	public Set getIndexedTypes() {
		return contentIndexes.keySet();
	}

	private IndexReader createReader() throws IOException {
		return IndexReader.open(getIndexLocation());
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.search.ContentSearchServiceI#index(java.util.Collection)
	 */
	public synchronized void index(Collection contentNodes) {
		try {
			// delete old documents
			IndexReader localReader = createReader();
			int count = 0;
			for (Iterator i = contentNodes.iterator(); i.hasNext();) {
				ContentNodeI node = (ContentNodeI) i.next();
				// CHANGED count += reader.delete(new Term(FIELD_CONTENT_KEY, node.getKey().getEncoded()));
				count += localReader.deleteDocuments(new Term(FIELD_CONTENT_KEY, node.getKey().getEncoded()));
				

			}
			LOGGER.debug("Deleted " + count + " content nodes");
			localReader.close();

			// index new documents
			IndexWriter writer = new IndexWriter(getIndexLocation(), STEMMER, false);
			count = 0;
			for (Iterator i = contentNodes.iterator(); i.hasNext();) {
				ContentNodeI node = (ContentNodeI) i.next();
				indexNode(node, writer);

				count++;
				if (count % 100 == 0) {
					LOGGER.debug("Indexed " + count + " of " + contentNodes.size()	);
				}
			}

			writer.optimize();
			writer.close();
			
			

			// replace old reader
			localReader = this.reader;
			this.reader = createReader();
			if (localReader != null) {
				localReader.close();
			}
			
			spellService.indexWords(reader);
			
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
	}

	private void indexNode(ContentNodeI node, IndexWriter writer) throws IOException {

		Document doc = createDocument(node);
		if (doc == null)
			return;

		writer.addDocument(doc);
	}

	/**
	 * Converts a ContentNode to a Lucene document.  Creates the document and builds
	 * it up field by field.  
	 * 
	 * @param node the content node to convert to a Lucene document
	 * @return a Lucene document that can be added to an index and searched
	 * 		or null if this node should not be indexed
	 */
	private Document createDocument(ContentNodeI node) {
		
		BrandNameExtractor brandNameExtractor = new BrandNameExtractor();

		List indexes = (List) contentIndexes.get(node.getKey().getType());
		if (indexes == null) {
			return null;
		}

		Document doc = new Document();

		//CHANGED doc.add(Field.Keyword(FIELD_CONTENT_KEY, node.getKey().getEncoded()));
		doc.add(new Field(FIELD_CONTENT_KEY, node.getKey().getEncoded(),Field.Store.YES,Field.Index.UN_TOKENIZED));

		//CHANGED doc.add(Field.UnStored(FIELD_CONTENT_TYPE, node.getKey().getType().getName()));
		doc.add(new Field(FIELD_CONTENT_TYPE,node.getKey().getType().getName(),Field.Store.NO,Field.Index.TOKENIZED));
		//CHANGED doc.add(Field.UnStored(FIELD_CONTENT_ID, node.getKey().getId()));
		doc.add(new Field(FIELD_CONTENT_ID, node.getKey().getId(),Field.Store.NO,Field.Index.TOKENIZED));

		for (Iterator i = indexes.iterator(); i.hasNext();) {
			AttributeIndex ad = (AttributeIndex) i.next();

			Object atrValue = node.getAttribute(ad.getAttributeName()).getValue();
			
			if (atrValue != null) {
				
				// create an UN_TOKENIZED index field for full name
				// if it has 2-3 terms
				if (ad.getAttributeName().equals("FULL_NAME")) {
					
					List tokens = new ArrayList();
					for(StringTokenizer tokenizer = new StringTokenizer(atrValue.toString().trim().toLowerCase()," \t&,")
						;tokenizer.hasMoreTokens(); tokens.add(tokenizer.nextToken()));
					
					if (tokens.size() > 1) {
						for(int p = 0; p< tokens.size() - 1; ++p) {
							if (stopWords.contains(tokens.get(p))) continue; 
							StringBuffer comp = new StringBuffer((String)tokens.get(p)).append(' ').append(tokens.get(tokens.size()-1));
							//System.out.println("COMPOUND WORD: " + comp);
							doc.add(new Field(FULL_NAME_INDEX,comp.toString(),Field.Store.NO,Field.Index.UN_TOKENIZED));
						}
						
						if (tokens.size() > 2 && tokens.size() < 4) {
							StringBuffer buff = new StringBuffer();
							for(int p=0; p< tokens.size(); ++p) {
								if (buff.length() > 0) buff.append(' ');
								buff.append(tokens.get(p));
							}
							//System.out.println("TRI COMPOUND WORD: "+ buff.toString());
							doc.add(new Field(FULL_NAME_INDEX,buff.toString(),Field.Store.NO,Field.Index.UN_TOKENIZED));
						}
					}
				}
				
				//
				// TODO: handle attributes whose value doesn't
				// have a convenient, indexable toString() value
				// the AttributeIndex should eventually
				// contain this info...
				//
				String value = atrValue.toString();
				// CHANGED doc.add(Field.Text(ad.getAttributeName(), value));
				doc.add(new Field(ad.getAttributeName(), value, Field.Store.YES, Field.Index.TOKENIZED));
				// CHANGED doc.add(Field.Text(ad.getAttributeName() + STEMMED_SUFFIX, value));
				doc.add(new Field(ad.getAttributeName() + STEMMED_SUFFIX, value, Field.Store.YES, Field.Index.TOKENIZED));
				
				// extract potential brand names and add them to the search
				List brandNames = brandNameExtractor.extract(value);
				for(Iterator bni = brandNames.iterator(); bni.hasNext(); ) {
					String canonicalBrandName = StringUtil.removeAllWhiteSpace(bni.next().toString());
					doc.add(new Field(ad.getAttributeName(), canonicalBrandName, Field.Store.YES, Field.Index.UN_TOKENIZED));
				}
			}

		}
		

		return doc;
	}

	public void initialize() {
		try {
			boolean exists = IndexReader.indexExists(getIndexLocation());
			boolean indexGood = true;
			if (exists) {
				Collection indexedFields = getReader().getFieldNames(IndexReader.FieldOption.INDEXED);
				for(int i=0; i< CHECK_INDEXES.length; ++i) {
					if (!indexedFields.contains(CHECK_INDEXES[i])) {
						LOGGER.debug("REQUIRED INDEX: " + CHECK_INDEXES[i] + " DOES NOT EXIST");
						indexGood = false;
						// DO NOT BREAK, IT IS GOOD TO KNOW ALL INDEXES THAT FAIL :)
					}
				}
				
				if (!indexGood) {
					
					LOGGER.debug("REBUILDING INDEX");
					Set keys = new HashSet();
					for (Iterator i = getIndexedTypes().iterator(); i.hasNext();) {
						ContentType type = (ContentType) i.next();
						keys.addAll(CmsManager.getInstance().getContentKeysByType(type));
					}

					Map nodes = CmsManager.getInstance().getContentNodes(keys);
					index(nodes.values());
					spellService = null;
				}
			}
			
			if (!exists) {
				LOGGER.info("Creating index at " + getIndexLocation());
				IndexWriter writer = new IndexWriter(getIndexLocation(), STEMMER, false);
				writer.optimize();
				writer.close();
			}
			
			
			if (spellService == null) {
				spellService = new LuceneSpellingSuggestionService(
					getIndexLocation(),
					Arrays.asList(new String[] {FULL_NAME_INDEX, "FULL_NAME", "AKA"}),
					getReader());
			}
			
		} catch (IOException ioe) {
			throw new CmsRuntimeException(ioe);
		}
	}

	private IndexReader getReader() throws IOException {
		if (this.reader == null) {
			this.reader = createReader();
		}
		return this.reader;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.search.ContentSearchServiceI#search(java.lang.String, int)
	 */
	public List search(String query, int maxHits) {

		try {

			IndexSearcher searcher = new IndexSearcher(getReader());

			// changed interface
			Query q = MultiFieldQueryParser.parse(
					new String[] {
							query, // FIELD_CONTENT_ID
							query, // FULL_NAME
							query, // AKA
							query, // GLANCE_NAME
							query, // KEYWORDS
							query, // keywords
							query, // name
							query, // name + STEMMED_SUFFIX
							query, // FULL_NAME + STEMMED_SUFFIX
							query, // title
							query  // title + STEMMED_SUFFIX
					},
					new String[] {
							FIELD_CONTENT_ID,
							"FULL_NAME",
							"AKA",
							"GLANCE_NAME",
							"KEYWORDS",
							"keywords",
							"name",
							"name" + STEMMED_SUFFIX,
							"FULL_NAME" + STEMMED_SUFFIX,
							"title",
							"title" + STEMMED_SUFFIX
					}
			    , STEMMER);

			Hits hits = searcher.search(q);

			if (hits.length() == 0) {
				return Collections.EMPTY_LIST;
			}

			List h = new ArrayList(hits.length());
			int max = Math.min(maxHits, hits.length());
			for (int i = 0; i < max; i++) {
				Document doc = hits.doc(i);
				
				ContentKey key = ContentKey.decode(doc.get(FIELD_CONTENT_KEY));
				h.add(new SearchHit(key, hits.score(i)));
			}
			return h;

		} catch (ParseException e) {
			throw new CmsRuntimeException("Invalid search query " + query, e);
		} catch (IOException e) {
			throw new CmsRuntimeException(e);
		}
		
	}

	private static class StemmingAnalyzer extends StandardAnalyzer {
		
		public TokenStream tokenStream(String fieldName, Reader reader) {
			TokenStream ts = super.tokenStream(fieldName, reader);
			ts = new ISOLatin1AccentFilter(ts);
			if (fieldName.endsWith(STEMMED_SUFFIX)) {
				// added "lower case" as this is suggested in the Lucene API docs
				return new PorterStemFilter(new LowerCaseFilter(ts));
			}
			return ts;
		}

	}
	
	/**
	 * Accept edit distance, If it is less or equal to a quarter of the longer string's length + 1 (and even
	 * less for short words)
	 */
	private static LuceneSpellingSuggestionService.AcceptableEditDistanceFilter quarterPlusOne = 
		new LuceneSpellingSuggestionService.AcceptableEditDistanceFilter() {

			public boolean accept(String q, String s, int d) {
				int max = Math.max(q.length(), s.length());
				
				switch(max) {
					case 1:
						return d == 0;
					case 2:
					case 3:
					case 4:
						return d <= 1;
					default:
						return d <= max/4 + 1;
				}
			}
	};
	
	/**
	 * Suggest a unique list of sorted spelling suggestions.
	 * @param query original query
	 * @param maxHits to retrieve for each term
	 */
	public List suggestSpelling(String query, int maxHits) {
		return LuceneSpellingSuggestionService.getUniqueQueries(
				spellService.getSpellingHits(query, maxHits, quarterPlusOne));
	}
	
	  
}