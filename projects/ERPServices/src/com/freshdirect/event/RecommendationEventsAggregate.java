package com.freshdirect.event;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents recommendation events aggregate.
 * 
 * Recommendation events are aggregated over content, recommending variant
 * and date.
 * @author istvan
 *
 */
public class RecommendationEventsAggregate implements Serializable {
	
	private static final long serialVersionUID = 8268945554741042497L;
	
	private String contentId;
	private String variantId;
	private Date date;
	private int frequency;
	private String eStoreId;
	
	public RecommendationEventsAggregate(){}
	/**
	 * Constructor.
	 * @param contentId 
	 * @param variantId
	 * @param date 
	 * @param frequency
	 */
	public RecommendationEventsAggregate(
		String contentId,
		String variantId,
		Date date,
		int frequency,
		String eStoreId) {
		
		this.contentId = contentId;
		this.variantId = variantId;
		this.date = date;
		this.frequency = frequency;
		this.eStoreId = eStoreId;
	}
	
	public String getVariantId() {
		return variantId;
	}
	
	public String getContentId() {
		return contentId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public String geteStoreId() {
		return eStoreId;
	}
	
	public String getEStoreId() {
		return eStoreId;
	}

}
