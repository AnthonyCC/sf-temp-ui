package com.freshdirect.event;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.framework.event.FDRecommendationEvent;

/**
 * Aggregates recommendation events.
 * 
 * This class is a singleton.
 * 
 * This class uses an LRU (Last Recently Used, i.e. accessed) cache to aggregate 
 * {@link FDRecommendationEvent}s. It is expected, that a large number of such events
 * can be produced and thus aggregation can be advantageous. 
 * 
 * The cache is indexed by {@link FDRecommendationEvent#getContentId() content ids}. 
 * Recommendation events are aggregated over {@link FDRecommendationEvent#getContentId() content id},
 * {@link FDRecommendationEvent#getVariantId() variant id} and date (with day resolution).
 * 
 * Several events can be set to control the flushing events.
 * 
 * <ul>
 * <li>When the number of content ids exceed the {@link #getMaxEntries() maximum entries} the 
 *     recommendation events corresponding to the least recently accessed content key will be
 *     flushed to the {@link RecommendationEventLogger} or {@link ClickThroughLogger}</li>
 * <li>When the number of events (counting aggregations) not flushed exceeds 
 *     {@link #getMaxCount()}, all events will be flushed in a batch</li>
 * <li>A timer thread can be started with {@link #startTimedFlushes(int)}, which will periodically
 *     flush events in a batch</li>
 * </ul>
 * 
 * @author istvan
 *
 */
public abstract class RecommendationEventAggregator {

	private int maxEntries;
	private int maxCount;
	private int total = 0;

	protected RecommendationEventLogger getLogger() {
		return RecommendationEventLogger.getInstance();
	}
	
	protected abstract FDRecommendationEvent createInstance(String variantId, String contentId, Date date);
	
	protected abstract Class getEventClass();
	
	/**
	 * Represents a variant and a count (frequency).
	 * @author istvan
	 *
	 */
	protected static class VariantCount {
		private String variantId;
		private int count;
		
		protected VariantCount(String variantId, int count) {
			this.variantId = variantId;
			this.count = count;
		}
		
		public int getCount() {
			return count;
		}
		
		public synchronized void addToCount(int c) {
			count += c;
		}
		
		public String getVariantId() {
			return variantId;
		}
		
		/**
		 * Compares {@link VariantCount}s and {@link String}s.
		 */
		protected static Comparator stringComparator = new Comparator() {

			private String getString(Object o) {
				return o instanceof VariantCount ? ((VariantCount)o).getVariantId() : o.toString();
			}
			
			public int compare(Object o1, Object o2) {
				return getString(o1).compareTo(getString(o2));
			}
		};
		
	}
	
	
	/**
	 * Represents a day.
	 * 
	 * @author istvan
	 *
	 */
	protected static class Day {
		private int year;
		private int month; // of year
		private int day; // of month
		
		public Day(Calendar date) {
			set(date);
		}
		
		/**
		 * As {@link Date}.
		 * @return date object with hour, minute, second and millisecond truncated.
		 */
		public Date asDate() {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR,year);
			c.set(Calendar.MONTH, month);
			c.set(Calendar.DAY_OF_MONTH,day);
			c.set(Calendar.HOUR,0);
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);
			c.set(Calendar.MILLISECOND,0);
			return c.getTime();
		}
		
		/**
		 * Set from date.
		 * Extracts the "day part" of the argument.
		 * @param date to adjust to
		 */
		public void set(Calendar date) {
			year = date.get(Calendar.YEAR);
			month = date.get(Calendar.MONTH);
			day = date.get(Calendar.DAY_OF_MONTH);
		}
		
		public boolean isSameDay(Calendar date) {
			return 
				date.get(Calendar.DAY_OF_MONTH) == day &&
				date.get(Calendar.MONTH) == month &&
				date.get(Calendar.YEAR) == year;
		}
		
		public boolean isSameDay(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return isSameDay(cal);
		}
	}
	
	/**
	 * Represents an aggregation of recommendation events for the same content id.
	 * @author istvan
	 *
	 */
	protected static class ContentRecommendations {
		private Day day;
		private VariantCount[] variants;
		
		/**
		 * Constructor.
		 * @param variantIds the different {@link FDRecommendationEvent#getVariantId() variant ids} seen so far
		 */
		public ContentRecommendations(Set variantIds) {
			variants = new VariantCount[variantIds.size()];
			int k = 0;
			for(Iterator i = variantIds.iterator(); i.hasNext();) {
				variants[k++] = new VariantCount(i.next().toString(),0);
			}
			day = new Day(Calendar.getInstance());
		}
		
		/**
		 * Increments the event count for the variant.
		 * 
		 * @param variantId
		 * @param frequency
		 * @return whether the variant needed to be added
		 */
		public boolean addRecommendationEvent(String variantId, int frequency) {
			
			int p = Arrays.binarySearch(
				variants, 
				variantId, 
				VariantCount.stringComparator);
			
			if (p < 0) {
				p = -p - 1;
				VariantCount[] newVariants = new VariantCount[variants.length+1];
				System.arraycopy(variants, 0, newVariants, 0, p);
				System.arraycopy(variants, p, newVariants, p + 1, variants.length - p);
				newVariants[p] = new VariantCount(variantId,frequency);
				variants = newVariants;
				return true;
			} else {
				variants[p].addToCount(frequency);
				return false;
			}
		}
	}
	
	// variant seen so far
	private Set variantIds = new TreeSet();
	

	/**
	 * Implementation of the LRU cache. 
	 * @author istvan
	 *
	 */
	protected class LRU extends LinkedHashMap {

		private static final long serialVersionUID = 5337416969287588782L;

		/**
		 * Constructor.
		 * Access ordered {@link LinkedHashMap}.
		 */
		public LRU() {
			super(3 * maxEntries / 2 + 1, 0.75f, true);
		}

		/**
		 * Whether to remove least recently accessed entry.
		 * 
		 * If the already stored content entries exceeds the set
		 * maximum, flushes this entry and returns true.
		 * 
		 * @param e least recently accessed entry
		 * @return whether the argument is to be removed
		 */
		protected boolean removeEldestEntry(Map.Entry e) {
			if (size() > maxEntries) {
				flushEntry(e.getKey().toString(),(ContentRecommendations)e.getValue());
				return true;
			} else {
				return false;
			}
		}
	}

	private static final long serialVersionUID = -1233481545626516984L;

	/**
	 * Constructor.
	 * 
	 * @param maxEntries maximum content entries to store before flushing the least recently accessed entry 
	 * @param maxCount maximum recomendation events to aggregate before flushing all aggregated entries
	 */
	protected RecommendationEventAggregator(int maxEntries, int maxCount) {
		this.maxEntries = maxEntries;
		this.maxCount = maxCount;
	}

	/**
	 * Maximum number of content entries stored.
	 * @return maximum number of content entries
	 */
	public int getMaxEntries() {
		return maxEntries;
	}

	/**
	 * Maximum number of aggregated recommendation events to store.
	 * Once this limit is reached, all aggregated events are flushed in a batch.
	 * @return maximum aggregated recommendation events
	 */
	public int getMaxCount() {
		return maxCount;
	}

	/**
	 * Flush all events.
	 */
	public synchronized void flush() {
		flush(1);
	}
	
	/**
	 * Clear all entries without flushing.
	 */
	public synchronized void clear() {
		cache.clear();
	}
	
	
	
	/**
	 * Flush all events whose frequency is bigger or equal to the given value.
	 * @param min minimum frequency required to flush the event
	 */
	public void flush(int min) {
		Collection events = new ArrayList(total);
		synchronized(this) {
			for (Iterator i = cache.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry)i.next();
				String contentId = entry.getKey().toString();
				ContentRecommendations recommendations = (ContentRecommendations)entry.getValue();
				for(int j = 0; j< recommendations.variants.length; ++j) {
					VariantCount variantCount = recommendations.variants[j];
					if (variantCount.count == 0) continue;
					events.add(
						new RecommendationEventsAggregate(
								contentId,variantCount.variantId,recommendations.day.asDate(),variantCount.count));
					total -= variantCount.count;
					variantCount.count = 0;
				}
				recommendations.day.set(Calendar.getInstance());
			}
		}
		RecommendationEventLogger logger = getLogger();
		logger.log(getEventClass(),events);
	}

	/**
	 * Flush events corresponding to a content id.
	 * @param contentId
	 * @param recommendations
	 */
	protected synchronized void flushEntry(String contentId, ContentRecommendations recommendations) {
		
		RecommendationEventLogger logger = RecommendationEventLogger.getInstance();
	
		for(int j = 0; j< recommendations.variants.length; ++j) {
			VariantCount variantCount = recommendations.variants[j];
			if (variantCount.count == 0) continue;
			FDRecommendationEvent event = createInstance(variantCount.variantId,contentId,recommendations.day.asDate());
			logger.log(event, variantCount.count);
			total -= variantCount.count;
			variantCount.count = 0;
		}
		recommendations.day.set(Calendar.getInstance());
	}

	// the cache
	private LRU cache = new LRU();
	
	/**
	 * Register a recommendation event.
	 * 
	 * @param event 
	 */
	public synchronized void note(FDRecommendationEvent event) {
		if (total >= maxCount) flush(2);
		
		ContentRecommendations recommendations = (ContentRecommendations)cache.get(event.getContentId());
		if (recommendations == null) {
			recommendations = new ContentRecommendations(variantIds);
			cache.put(event.getContentId(), recommendations);
		}
		
		if (!recommendations.day.isSameDay(event.getTimeStamp())) {
			flushEntry(event.getContentId(),recommendations);
		}
		
		boolean newVariant = recommendations.addRecommendationEvent(event.getVariantId(),1);
		if (newVariant) variantIds.add(event.getVariantId());
		
		++total;
	}
	
	// timer frequency, 0 means no timer
	private int timeInMilliseconds = 0;
	
	// lock used in timer
	private Object lock = new Object();
	
	// timer threads' runnable
	private Runnable timerBody = new Runnable() {
		public void run() {	
			for(;;) {
				synchronized (this) {
					int sleepMilliseconds = 0;
					synchronized(lock) {
						if (timeInMilliseconds == 0) break;
						sleepMilliseconds = timeInMilliseconds;
					}
					try {
						flush();
						Thread.sleep(sleepMilliseconds);
						
					} catch (InterruptedException e) {
					}
				}
			}
		}
		
	};
	
	// the timer thread
	private Thread timerThread = new Thread(timerBody);
	
	/**
	 * Start timed event flushes.
	 * 
	 * If a timer is already running, it will not start a new one, but
	 * updates its period.
	 * 
	 * @param timeInMilliseconds period
	 * @return whether the timer was started.
	 */
	public boolean startTimedFlushes(int timeInMilliseconds) {
		synchronized (lock) {
			this.timeInMilliseconds = timeInMilliseconds;
			if (!timerThread.isAlive()) {
				timerThread.start();
				return true;
			} else return false;
		}	
	}
	
	/**
	 * Stop the currently running timer thread that flushes events.
	 * @return whether the thread was running
	 */
	public boolean stopTimedFlushes() {
		synchronized (lock) {
			this.timeInMilliseconds = 0;
			if (timerThread.isAlive()) {
				timerThread.interrupt();
				timerThread = new Thread(timerBody);
				return true;
			} else return false;
		}
	}
}
