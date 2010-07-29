package com.freshdirect.mktAdmin.model;

import java.io.Serializable;
import java.util.Collection;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;

public class RestrictionSearchBean implements Serializable {
	
	private FDPromotionNewModel promotion=null;
	private String serachKey="";
	private long searchCount=100;
	private Collection promotionList=null;	
	private long startIndex=0;
	private long endIndex=0;
	
	public static final long DEFAULT_START_COUNT=0;
	public static final long DEFAULT_END_COUNT=100;
	
	public RestrictionSearchBean(){	
	}
	
	public FDPromotionNewModel getPromotion() {
		return promotion;
	}
	public void setPromotion(FDPromotionNewModel promotion) {
		this.promotion = promotion;
	}
	public Collection getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(Collection promotionList) {
		this.promotionList = promotionList;
	}
	public long getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(long searchCount) {
		this.searchCount = searchCount;
	}
	public String getSerachKey() {
		return serachKey;
	}
	public void setSerachKey(String serachContent) {
		this.serachKey = serachContent;
	}

	public long getEndIndex() {
		if(endIndex==0) return DEFAULT_END_COUNT;
		else return endIndex;
	}

	public void setEndIndex(long endIndex) {
		this.endIndex = endIndex;
	}

	public long getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	} 

}
