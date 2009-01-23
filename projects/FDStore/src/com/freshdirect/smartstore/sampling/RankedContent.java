package com.freshdirect.smartstore.sampling;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.freshdirect.cms.ContentKey;

/**
 * Wrapper class for ranked content.
 * 
 * This class represents either one particular content key ({@link Single}) or
 * a group of content keys ({@link Aggregate}) that need to be counted
 * with a common rank (Category Aggregation).
 * 
 * @author istvan
 *
 */
public interface RankedContent {
	
	/**
	 * @return score associated with content(s)
	 */
	public double getScore();
	
	/**
	 * @return number of content keys associated with this instance
	 */
	public int getCount();
	
	/**
	 * @return id used in referring to this instance
	 */
	public abstract String getId();
	
	/**
	 * One particular content key.
	 * @author istvan
	 *
	 */
	public static class Single implements RankedContent {
		
		private ContentKey id;
		
		private double score;
		
		public Single(ContentKey id, double score) {
			this.id = id;
			this.score = score;
			
		}
		
		public double getScore() {
			return score;
		}
		
		public int getCount() { return 1; }
	
		public String getId() {
			return id.getId();
		}
		
		public ContentKey getContentKey() {
			return id;
		}
		
		public String toString() {
			return id.toString() + ' ' + score;
		}
	};
	
	/**
	 * Content key aggregation.
	 * 
	 * This items are aggregated at the category level, and thus
	 * are treated the same from the perspective of sampling.
	 * 
	 * @author istvan
	 *
	 */
	public static class Aggregate implements RankedContent {
		
		private static Random R = new Random();
		
		private double totalScore = 0;
		
		// List<Single> 
		private List items = new LinkedList(); 
		private String id;
		
		public Aggregate(String id) {
			this.id = id;
		}
		
		public void add(Single o) {
			items.add(o);
			totalScore += o.getScore();
		}
		
		/**
		 * Remove one content key and adjust group score.
		 * 
		 * @return content removed
		 */
		public Single take() {
			Single r = (Single)items.remove(R.nextInt(items.size()));
			//Single r = items.remove(0);
			totalScore -= r.getScore();
			return r;
		}
		
		/**
		 * Remove the content by its name
		 * @param item name of content to be removed
		 */
		public void remove(String item) {
			for(Iterator i = items.iterator(); i.hasNext();) {
				Single stored = (Single)i.next();
				if (item.equals(stored.getId())) {
					totalScore -= stored.getScore();
					break;
				}
			}
		}
		
		/**
		 * Get the id of teh group.
		 */
		public String getId() {
			return id;
		}
		
		/**
		 * 
		 * @return List<Single>
		 */
		public List getItems() {
			return items;
		}
		
		public double getScore() {
			return totalScore;
		}
		
		public int getCount() {
			return items.size();
		}
		
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			for(Iterator i = items.iterator(); i.hasNext(); ) {
				RankedContent.Single item = (RankedContent.Single)i.next();	
				buffer.append(buffer.length() > 0? ',' : '[');
				buffer.append(item);
			}
			return buffer.append("] ").append(getScore()).toString();
		}
	};	
}