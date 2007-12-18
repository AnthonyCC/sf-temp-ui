/**
 * @author ekracoff
 * Created on May 26, 2005*/

package com.freshdirect.ocf.core;

import java.io.Serializable;


public class EmailStatLine implements Serializable{
	private String mailingId;
	private String customerId;
	private int openCount;
	private int clickstreamCount;
	private int clickthruCount;
	private int conversionCount;
	private int attachmentCount;
	private int forwardCount;
	private int mediaCount;
	private int bounceCount;
	private int optoutCount;
	private int optinCount;
	private int abuseCount;
	private int changeAddressCount;
	private int blockedCount;
	private int restrictedCount;
	private int otherReplyCount;
	private int suppressionCount;
	
	
	public int getAbuseCount() {
		return abuseCount;
	}
	
	public void setAbuseCount(int abuseCount) {
		this.abuseCount = abuseCount;
	}
	
	public int getAttachmentCount() {
		return attachmentCount;
	}
	
	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}
	
	public int getBlockedCount() {
		return blockedCount;
	}
	
	public void setBlockedCount(int blockedCount) {
		this.blockedCount = blockedCount;
	}
	
	public int getBounceCount() {
		return bounceCount;
	}
	
	public void setBounceCount(int bounceCount) {
		this.bounceCount = bounceCount;
	}
	
	public int getChangeAddressCount() {
		return changeAddressCount;
	}
	
	public void setChangeAddressCount(int changeAddressCount) {
		this.changeAddressCount = changeAddressCount;
	}
	
	public int getClickstreamCount() {
		return clickstreamCount;
	}
	
	public void setClickstreamCount(int clickstreamCount) {
		this.clickstreamCount = clickstreamCount;
	}
	
	public int getClickthruCount() {
		return clickthruCount;
	}
	
	public void setClickthruCount(int clickthruCount) {
		this.clickthruCount = clickthruCount;
	}
	
	public int getConversionCount() {
		return conversionCount;
	}
	
	public void setConversionCount(int conversionCount) {
		this.conversionCount = conversionCount;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public int getForwardCount() {
		return forwardCount;
	}
	
	public void setForwardCount(int forwardCount) {
		this.forwardCount = forwardCount;
	}
	
	public String getMailingId() {
		return mailingId;
	}
	
	public void setMailingId(String mailingId) {
		this.mailingId = mailingId;
	}
	
	public int getMediaCount() {
		return mediaCount;
	}
	
	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}
	
	public int getOpenCount() {
		return openCount;
	}
	
	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
	
	public int getOptinCount() {
		return optinCount;
	}
	
	public void setOptinCount(int optinCount) {
		this.optinCount = optinCount;
	}
	
	public int getOptoutCount() {
		return optoutCount;
	}
	
	public void setOptoutCount(int optoutCount) {
		this.optoutCount = optoutCount;
	}
	
	public int getOtherReplyCount() {
		return otherReplyCount;
	}
	
	public void setOtherReplyCount(int otherReplyCount) {
		this.otherReplyCount = otherReplyCount;
	}
	
	public int getRestrictedCount() {
		return restrictedCount;
	}
	
	public void setRestrictedCount(int restrictedCount) {
		this.restrictedCount = restrictedCount;
	}
	
	public int getSuppressionCount() {
		return suppressionCount;
	}
	
	public void setSuppressionCount(int suppressionCount) {
		this.suppressionCount = suppressionCount;
	}
}
