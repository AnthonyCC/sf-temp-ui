package com.freshdirect.fdstore.content.test;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;

import com.freshdirect.framework.util.CartesianProduct;
import com.freshdirect.framework.util.ListConcatenation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;


import org.apache.commons.collections.Predicate;

/**
 * Builds a content matrix from CMS data.
 * 
 * The constuctor downloads the CMS content keys and the class provides iterators that can be
 * used to step through this set filtering rows and producing various columns.
 * 
 * @see RowFilter
 * @see ColumnExtractor
 * @author istvan
 *
 */
public class ContentBundle {
	
	
	public final static String SKU_SAMPLES = "SKU";
	public final static String PRODUCT_ID_SAMPLES = "PRODUCT_ID";
	
	/**
	 * A cache shared by the column extractors for each row.
	 * 
	 * The cache is clean before the commencment of extracting the columns. 
	 * @see ColumnExtractor
	 * @author istvan
	 *
	 */
	public static class RowCache {
		private Map cache = new HashMap();
		private Map sequences = new HashMap();
		
		private ContentNodeModel contentModel;
		
		/**
		 * Put an object into the cache.
		 * @param key
		 * @param o object
		 */
		public void put(String key, Object o) {
			cache.put(key, o);
		}
		
		/**
		 * Put a sequence of objects into the cache.
		 * 
		 * This sequence will be a generator in a cartesian product of rows; that is
		 * the elements of the sequence will be served individually to the column
		 * extractors' extract methods.
		 * 
		 * @param key
		 * @param sequence
		 * @see CartesianProduct
		 */
		public void putSequence(String key, List sequence) {
			sequences.put(key,sequence);
		}
	
		/**
		 * Get a value by key.
		 * @param key
		 * @return value
		 */
		public Object get(String key) {
			return cache.get(key);
		}
		
		/**
		 * Get the sequence. 
		 * @param key
		 * @return sequence
		 */
		public List getSequence(String key) {
			return (List)sequences.get(key);
		}
	
		/**
		 * Get content model of current row.
		 * @return content model of current row.
		 */
		public ContentNodeModel getContentNodeModel() {
			return contentModel;
		}
		
		/**
		 * Clear all values.
		 *
		 */
		public void purge() {
			cache.clear();
			sequences.clear();
		}
		
		public Map getSequences() { return sequences; }
	}
	
	
	private Collection contentKeys; 
	private static ContentFactory fact = ContentFactory.getInstance();
	
	
	private ContentType type;
	
	/** Constructor.
	 */
	public ContentBundle(ContentType type) {
		this(type,CmsManager.getInstance().getContentKeysByType(type));
	}
	
	public ContentBundle(ContentType type, Collection contentKeys) {
		this.type = type;
		this.contentKeys = contentKeys;
	}
	
	
	
	public List getCaches(List columnExtractors) {
		return getCaches(null,columnExtractors);
	}
	
	public List getCaches(Predicate rowFilter, List columnExtractors) {
		List caches = new ArrayList();
	
		for(Iterator i = contentKeys.iterator(); i.hasNext(); ) {
			ContentKey key = (ContentKey) i.next();
			if (!matches(key.getType(),type)) continue;
			
			RowCache cache = new RowCache();
			cache.contentModel = fact.getContentNodeByKey(key);
		
			if (rowFilter != null && !rowFilter.evaluate(cache.contentModel)) continue;
		
			boolean abortRow = false;
			for(Iterator attrI = columnExtractors.iterator(); attrI.hasNext();) {
				ColumnExtractor extractor = (ColumnExtractor)attrI.next();
			
				try {
					extractor.cacheAttribute(cache);
				} catch(AbortRowException e) {
					abortRow = true;
					break;
				}
			}
		
			if (abortRow) continue;
			
			caches.add(cache);
		}	
		return caches;
	}
	
	public int countElems(List caches) {
		int total = 0;
		for(Iterator i = caches.iterator(); i.hasNext(); ) {
			RowCache cache = (RowCache)i.next();
			
			CartesianProduct cartesianProduct = new CartesianProduct();
			Map seqs = cache.getSequences();

			for(Iterator s = seqs.entrySet().iterator(); s.hasNext(); ) {
				Map.Entry sequence = (Map.Entry)s.next();
				cartesianProduct.addSequence((List)sequence.getValue());
			}
			
			if (cartesianProduct.size() > 0) total += cartesianProduct.size();
			else ++total;
		}
		return total;
	}
	
	public Iterator cacheIterator(List caches, List columnExtractors) {
		return new CacheIterator(caches,columnExtractors);
	}
	
	private class CacheIterator implements Iterator {
		
		private Iterator cpi = null;
		private Iterator ci; 
		private List nextTuple = null;
		private RowCache cache = null;
		private List columnExtractors;
		private List labels = new ArrayList();
		
		private CacheIterator(List caches, List columnExtractors) {
			ci = caches.iterator();
			this.columnExtractors = columnExtractors; 
		}

		protected void getNextCartesianTuple() {
			nextTuple = new ArrayList(columnExtractors.size());
			
			for(;cpi.hasNext();) {
			
				List variationTuple = (List)cpi.next();
				for(int ce = 0; ce < variationTuple.size(); ++ce) {
					cache.put((String)labels.get(ce),variationTuple.get(ce));
				}
			
				try {
					nextTuple.clear();
					for(int i = 0; i <  columnExtractors.size(); ++i) {
						ColumnExtractor ae = (ColumnExtractor)columnExtractors.get(i);
						nextTuple.add(i, ae.extract(cache));
					}
					
					return;
				} catch(AbortRowException e) {
					continue;
				}
			}
			cpi = null;
			getNextTuple();
			
		}
		
		protected void getNextTuple() {
			if (cpi != null) {
				if (cpi.hasNext()) getNextCartesianTuple();
				else {
					cpi = null;
					getNextTuple();
				}
			} else { // cpi == null
				if (!ci.hasNext()) {
					nextTuple = null;
					return;
				}
				
				cache = (RowCache)ci.next();
				
				labels.clear();
				CartesianProduct seqProduct = new CartesianProduct();
				for(Iterator s = cache.getSequences().entrySet().iterator(); s.hasNext(); ) {
					Map.Entry sequence = (Map.Entry)s.next();	
					seqProduct.addSequence((List)sequence.getValue());
					labels.add((String)sequence.getKey());
				}
				
				if (seqProduct.size() > 0) {
					cpi = seqProduct.iterator();
					getNextCartesianTuple();
				} else {
					try {
						nextTuple = new ArrayList();
						for(Iterator cei = columnExtractors.iterator(); cei.hasNext(); ) {
							ColumnExtractor ae = (ColumnExtractor)cei.next();
							nextTuple.add(ae.extract(cache));
						}
					} catch(AbortRowException e) {					
						getNextTuple();
						return;
					}
				}
			}
		}
		
		public boolean hasNext() {
			if (nextTuple != null) return true;
			
			getNextTuple();
			
			return nextTuple != null;
		}

		public Object next() {
			if (nextTuple == null) {
				if (!hasNext()) throw new IndexOutOfBoundsException();
			} 
			Object result = nextTuple;
			nextTuple = null;
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
			
		}
	}
	
	
	protected static boolean matches(ContentType actualType, ContentType targetType) {
		if (targetType.equals(FDContentTypes.STORE)) {
			return 
				actualType.equals(FDContentTypes.PRODUCT) ||
				actualType.equals(FDContentTypes.CATEGORY) ||
				actualType.equals(FDContentTypes.RECIPE) ||
				actualType.equals(FDContentTypes.DEPARTMENT);
			
		} else if (actualType.equals(targetType)) return true;
		else return false;
	}
	
	/**
	 * Get content matching a row filter from a collection of content keys.
	 * @param contentKeys Collection<ContenLKey>
	 * @param rowFilter
	 * @return Collection<ContentNodeModel>
	 */
	public static List getMatchingContent(ContentType type, Collection contentKeys, Predicate rowFilter) {
		List result = new ArrayList();
		for(Iterator i = contentKeys.iterator(); i.hasNext(); ) {
			ContentKey key = (ContentKey)i.next();
			if (!matches(key.getType(),type)) continue;
			ContentNodeModel model = (ContentNodeModel)fact.getContentNodeByKey(key);
			if (!rowFilter.evaluate(model)) continue;
			result.add(key);
		}
		return result;
	}
	
	/**
	 * Get content matching a row filter from all CMS products.
	 * @param rowFilter
	 * @return Collection<ContentKey>
	 */
	public static List getMatchingContent(ContentType type, Predicate rowFilter) {
		if (FDContentTypes.STORE.equals(type)) {
			ListConcatenation all = new ListConcatenation();
			all.addList(getMatchingContent(FDContentTypes.PRODUCT, rowFilter));
			all.addList(getMatchingContent(FDContentTypes.RECIPE, rowFilter));
			all.addList(getMatchingContent(FDContentTypes.CATEGORY, rowFilter));
			all.addList(getMatchingContent(FDContentTypes.DEPARTMENT, rowFilter));
			return all;
		} else {
			return getMatchingContent(
					type,
					CmsManager.getInstance().getContentKeysByType(type), rowFilter);
		}
	}
}
