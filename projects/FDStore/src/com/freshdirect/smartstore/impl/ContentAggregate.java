package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * Represents a set of content keys.
 * 
 * @author istvan
 */
public class ContentAggregate {
	
	private static class ContentScorePair {
		private ContentKey key;
		private float score;
		
		private ContentScorePair(ContentKey key, float score) {
			this.key = key;
			this.score = score;
			
		}
	}
	
	private static final String ORPHAN_LABEL = "_";

	// label
	private String label;
	
	// List<ContentScorePair>
	private List contentList = new ArrayList();
	
	// total score
	private float totalScore = 0.0f;
	
	/**
	 * Constructor.
	 * @param label aggregation label
	 */
	public ContentAggregate(String label) {
		this.label = label;
	}
	
	/**
	 * Get score of the aggregates.
	 * @return total score of content aggregated
	 */
	public float getScore() {
		return totalScore;
	}
	
	/**
	 * Add a content key.
	 * 
	 * It is assumed that {@link #getAggregateLabel(ContentKey) getAggregateLabel}(key)
	 * is the same is {@link #getLabel()}.
	 * 
	 * @param key content to be added
	 * @param keyScore score associated with key
	 */
	public void addContent(ContentKey key, float keyScore) {
		ContentScorePair cp = new ContentScorePair(key,keyScore);
		contentList.add(cp);
		totalScore += keyScore;
	}
	
	/**
	 * Get label of aggregates.
	 * @return label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Remove aggregates.
	 * 
	 * In more elements requested than the population, the entire
	 * population is returned.
	 *
	 * @param R random stream
	 * @param n number of elements to remove
	 * @return list of aggregates removed
	 */
	public List take(Random R, int n) {
		
		List result = null;
		if (n >= contentList.size()) {
			result = contentList;
			totalScore = 0;
			contentList = new ArrayList();	
		} else {
			result = new ArrayList(n);
			Collections.shuffle(contentList,R);
			for(int i = 0; i< n; ++i) {
				ContentScorePair cp = (ContentScorePair)contentList.remove(contentList.size()-1);
				totalScore -= cp.score;
				result.add(cp.key);
			}
		}
		
		return result;
	}
	
	/**
	 * Get a content key randomly and remove it from
	 * @param R
	 * @return
	 */
	public ContentKey take(Random R) {
		if (contentList.size() == 0) throw new NoSuchElementException("Content Aggregate Empty");
		ContentScorePair cp = (ContentScorePair)contentList.remove(R.nextInt(contentList.size()));
		totalScore -= cp.score;
		
		return cp.key;	
	}
	
	public Iterator keys() {
		return new Iterator() {
			
			private Iterator i = contentList.iterator();
			private ContentScorePair cp = null;

			public boolean hasNext() {
				return i.hasNext();
			}

			public Object next() {
				cp = (ContentScorePair)i.next();
				return cp.key;
			}

			public void remove() {
				if (cp == null) throw new IllegalStateException();
				totalScore -= cp.score;
				cp = null;
				i.remove();
			}		
		};
	}
	
	
	
	/**
	 * Get the highest aggregation label for the key.
	 * @param key
	 * @return label
	 */
	public static String getAggregateLabel(ContentKey key) {
		ContentNodeModel model =ContentFactory.getInstance().getContentNodeByKey(key);
		if (model == null) {
			return ORPHAN_LABEL;
		} else if (model instanceof SkuModel) {
			return getAggregateLabel((ProductModel)model.getParentNode());
		} else if (model instanceof ProductModel) {
			return getAggregateLabel((ProductModel)model);
		} else if (model instanceof CategoryModel) {
			return getAggregateLabel((CategoryModel)model);
		} else return key.getId();
	}
	
	/**
	 * Get the highest aggregation label for the key.
	 * @param key
	 * @return label
	 */
	public static String getAggregateLabel(ProductModel product) {
		if (product == null) return ORPHAN_LABEL; // in case argument is a getParent of a sku
		String label = getAggregateLabel((CategoryModel)product.getParentNode());
		return label == null ? product.getContentKey().getId() : label;
	}
	
	/**
	 * Get the highest aggregation label for the key.
	 * @param key
	 * @return label
	 */
	public static String getAggregateLabel(CategoryModel model) {
		String label = ORPHAN_LABEL;

		while(model != null) {
			if (model.isDYFAggregated()) label = model.getContentKey().getId();
			if (model.getParentNode() instanceof CategoryModel) {
				model = (CategoryModel) model.getParentNode();
			} else break;
		}	
		return label;
	}
}
