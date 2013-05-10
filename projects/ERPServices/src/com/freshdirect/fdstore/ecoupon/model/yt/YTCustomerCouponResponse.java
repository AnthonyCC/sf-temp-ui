package com.freshdirect.fdstore.ecoupon.model.yt;


public class YTCustomerCouponResponse extends YTCouponResponse {

	private String[] available_ids;
	private String[] clipped_active_ids;
	private String[] clipped_redeemed_ids;
	private String[] clipped_pending_ids;
	private String[] clipped_expired_ids;
	/**
	 * @return the available_Ids
	 */
	public String[] getAvailable_ids() {
		return available_ids;
	}
	/**
	 * @param available_Ids the available_Ids to set
	 */
	public void setAvailable_ids(String[] available_ids) {
		this.available_ids = available_ids;
	}
	
	/**
	 * @return the clipped_redeemed_ids
	 */
	public String[] getClipped_redeemed_ids() {
		return clipped_redeemed_ids;
	}
	/**
	 * @param clipped_redeemed_ids the clipped_redeemed_ids to set
	 */
	public void setClipped_redeemed_ids(String[] clipped_redeemed_ids) {
		this.clipped_redeemed_ids = clipped_redeemed_ids;
	}
	/**
	 * @return the clipped_pending_ids
	 */
	public String[] getClipped_pending_ids() {
		return clipped_pending_ids;
	}
	/**
	 * @param clipped_pending_ids the clipped_pending_ids to set
	 */
	public void setClipped_pending_ids(String[] clipped_pending_ids) {
		this.clipped_pending_ids = clipped_pending_ids;
	}
	/**
	 * @return the clipped_expired_ids
	 */
	public String[] getClipped_expired_ids() {
		return clipped_expired_ids;
	}
	/**
	 * @param clipped_expired_ids the clipped_expired_ids to set
	 */
	public void setClipped_expired_ids(String[] clipped_expired_ids) {
		this.clipped_expired_ids = clipped_expired_ids;
	}
	/**
	 * @return the clipped_active_ids
	 */
	public String[] getClipped_active_ids() {
		return clipped_active_ids;
	}
	/**
	 * @param clipped_active_ids the clipped_active_ids to set
	 */
	public void setClipped_active_ids(String[] clipped_active_ids) {
		this.clipped_active_ids = clipped_active_ids;
	}
	
	
}
