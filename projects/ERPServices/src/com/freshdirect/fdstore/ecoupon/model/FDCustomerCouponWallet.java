package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FDCustomerCouponWallet implements Serializable {

	private final Set<String> availableIds=new HashSet<String>();
	private final Set<String> clippedActiveIds=new HashSet<String>();
	private final Set<String> clippedRedeemedIds=new HashSet<String>();
	private final Set<String> clippedPendingIds=new HashSet<String>();
	private final Set<String> clippedExpiredIds=new HashSet<String>();
	
	private final Set<String> clippedMinQtyNotMetIds=new HashSet<String>();//Runtime
	private final Set<String> clippedFdFilteredIds = new HashSet<String>();//Runtime
	private final Set<String> clippedAppliedIds = new HashSet<String>();//Runtime
	
	private boolean couponEvaluationRequired = false;
	private boolean refreshCouponWalletRequired = false;
	/**
	 * @return the availableIds
	 */
	public Set<String> getAvailableIds() {
		return availableIds;
	}
	/**
	 * @return the clippedActiveIds
	 */
	public Set<String> getClippedActiveIds() {
		return clippedActiveIds;
	}
	/**
	 * @return the clippedRedeemedIds
	 */
	public Set<String> getClippedRedeemedIds() {
		return clippedRedeemedIds;
	}
	/**
	 * @return the clippedPendingIds
	 */
	public Set<String> getClippedPendingIds() {
		return clippedPendingIds;
	}
	/**
	 * @return the clippedExpiredIds
	 */
	public Set<String> getClippedExpiredIds() {
		return clippedExpiredIds;
	}
	/**
	 * @param availableIds the availableIds to set
	 */
	public void addAvailableIds(String[] availableIds) {
		if(null !=availableIds)
		this.availableIds.addAll(Arrays.asList(availableIds));
	}
	/**
	 * @param clippedActiveIds the clippedActiveIds to set
	 */
	public void addClippedActiveIds(String[] clippedActiveIds) {
		if(null !=clippedActiveIds)
			this.clippedActiveIds.addAll(Arrays.asList(clippedActiveIds));
	}
	/**
	 * @param clippedRedeemedIds the clippedRedeemedIds to set
	 */
	public void addClippedRedeemedIds(String[] clippedRedeemedIds) {
		if(null !=clippedRedeemedIds)
			this.clippedRedeemedIds.addAll(Arrays.asList(clippedRedeemedIds));
	}
	/**
	 * @param clippedPendingIds the clippedPendingIds to set
	 */
	public void addClippedPendingIds(String[] clippedPendingIds) {
		if(null !=clippedPendingIds)
			this.clippedPendingIds.addAll(Arrays.asList(clippedPendingIds));
	}
	/**
	 * @param clippedExpiredIds the clippedExpiredIds to set
	 */
	public void addClippedExpiredIds(String[] clippedExpiredIds) {
		if(null !=clippedExpiredIds)
			this.clippedExpiredIds.addAll(Arrays.asList(clippedExpiredIds));
	}
	
	/**
	 * @param clippedExpiredIds the clippedExpiredIds to set
	 */
	public void addClippedAppliedIds(String[] clippedAppliedIds) {
		if(null !=clippedAppliedIds)
			this.clippedAppliedIds.addAll(Arrays.asList(clippedAppliedIds));
	}
	
	
	public boolean isActive(String couponId){
		boolean isActive=false;
		if(null !=availableIds && availableIds.contains(couponId)){
			isActive=true;
		}
		return isActive;
	}
	
	public Set<String> getClippedAppliedIds() {
		return clippedAppliedIds;
	}
	public boolean isClipped(String couponId){
		boolean isClippedActive=false;
		if(null !=clippedActiveIds && clippedActiveIds.contains(couponId)){
			isClippedActive=true;
		}
		return isClippedActive;
	}
	
	public boolean isClippledActive(String couponId){
		boolean isClippedActive=false;
		if(null !=clippedActiveIds && clippedActiveIds.contains(couponId)){
			isClippedActive=true;
		}
		return isClippedActive;
	}
	
	public boolean isExpired(String couponId){
		boolean isClippedExpired=false;
		if(null !=clippedExpiredIds && clippedExpiredIds.contains(couponId)){
			isClippedExpired=true;
		}
		return isClippedExpired;
	}
	
	public boolean isRedeemed(String couponId){
		boolean isclippedRedeemed=false;
		if(null !=clippedRedeemedIds && clippedRedeemedIds.contains(couponId)){
			isclippedRedeemed=true;
		}
		return isclippedRedeemed;
	}
	
	public boolean isRedeemPending(String couponId){
		boolean isRedeemPending=false;
		if(null !=clippedPendingIds && clippedPendingIds.contains(couponId)){
			isRedeemPending=true;
		}
		return isRedeemPending;
	}
	
	public Set<String> getClippedMinQtyNotMetIds() {
		return clippedMinQtyNotMetIds;
	}
	
	public boolean isCouponEvaluationRequired() {
		return couponEvaluationRequired;
	}
	public void setCouponEvaluationRequired(boolean couponEvaluationRequired) {
		this.couponEvaluationRequired = couponEvaluationRequired;
	}
	public boolean isRefreshCouponWalletRequired() {
		return refreshCouponWalletRequired;
	}
	public void setRefreshCouponWalletRequired(boolean refreshCouponWalletRequired) {
		this.refreshCouponWalletRequired = refreshCouponWalletRequired;
	}
	public Set<String> getClippedFdFilteredIds() {
		return clippedFdFilteredIds;
	}	
	
}
