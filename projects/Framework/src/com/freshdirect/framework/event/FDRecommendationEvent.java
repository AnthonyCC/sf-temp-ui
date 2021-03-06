package com.freshdirect.framework.event;

import java.util.Date;

/**
 * Recommendation event.
 * 
 * An recommendation event is characterized by a recommended content,
 * the algorithm variant which produced it and the time when
 * it happened. 
 * 
 * @author istvan
 *
 */
public abstract class FDRecommendationEvent extends FDEvent implements Cloneable {
	
	private static final long serialVersionUID = 1204785178521111739L;
	
	private String variantId;
	private String contentId;
	private Date timestamp;
	
	FDRecommendationEvent(){
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param variantId
	 * @param contentId
	 * @param timestamp
	 */
	protected FDRecommendationEvent(String variantId, String contentId, Date timestamp, String eStoreId) {
		this.variantId = variantId;
		this.contentId = contentId;
		this.timestamp = timestamp;	
		this.eStoreId = eStoreId;
	}

	public String getVariantId() {
		return variantId;
	}
	
	public String getContentId() {
		return contentId;
	}
	
	public Date getTimeStamp() {
		return timestamp;
	}
	
	
	public String toString() {
		StringBuffer buffer = new StringBuffer(variantId.length() + contentId.length() + 20);
		return buffer.
			append(contentId).
			append('.').
			append(variantId).
			append(' ').
			append(timestamp).toString();
	}
	
	public Object clone()  {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Impression event.
	 * @author istvan
	 *
	 */
	public static class Impression extends FDRecommendationEvent {
		public Impression(){}
		/**
		 * Constructor.
		 * @param variantId
		 * @param contentId
		 * @param timestamp
		 */
		public Impression(String variantId, String contentId, Date timestamp, String eStoreId) {
			super(variantId, contentId, timestamp, eStoreId);
		}

		private static final long serialVersionUID = 2286824559331191047L;
	}
	
	/**
	 * Click-through event.
	 * @author istvan
	 *
	 */
	public static class ClickThrough extends FDRecommendationEvent {
		public ClickThrough(){}
		/**
		 * Constructor.
		 * @param variantId
		 * @param contentId
		 * @param timestamp
		 */
		public ClickThrough(String variantId, String contentId, Date timestamp, String eStoreId) {
			super(variantId, contentId, timestamp, eStoreId);
		}

		private static final long serialVersionUID = 2286824559331191047L;
	}
}
