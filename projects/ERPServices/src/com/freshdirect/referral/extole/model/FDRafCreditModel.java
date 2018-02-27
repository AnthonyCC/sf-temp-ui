package com.freshdirect.referral.extole.model;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.referral.extole.EnumRafTransactionStatus;

/*
 * Model class for CUST.RAF_CREDIT table
 * This class contains fields from the 
 * parsed extole rewards csv file
 * 
 * This table is used as source 
 * for referralcreditcron job
 */

public class FDRafCreditModel implements Serializable{
	
	private static final long serialVersionUID = -8399255024218460259L;

	private String id;
	
	private String advocateCustomerId;
	
	//private String status;
	
	private EnumRafTransactionStatus status;
	
	private Date creationTime;
	
	private Date modifiedTime;
	
	private String advocateFirstName;
	
	private String advocateLastName;
	
	private String advocateEmail;
	
	private String advocatePartnerUid;
	
	private String friendFirstName;
	
	private String friendLastName;
	
	private String friendEmail;
	
	private String friendPartnerUid;
	
	private String rewardType;
	
	private Date rewardDate;
	
	private String rewardSetName;
	
	private String rewardSetId;
		
	private double rewardValue;
		
	private String rewardDetail;
	
	// temp fields to store string data
	private String rewardDateString;
	
	private String rewardValueString;
	
	private String campaignId;
	
	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	private String campaignName;
	
	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the advocateCustomerId
	 */
	public String getAdvocateCustomerId() {
		return advocateCustomerId;
	}

	/**
	 * @param advocateCustomerId the advocateCustomerId to set
	 */
	public void setAdvocateCustomerId(String advocateCustomerId) {
		this.advocateCustomerId = advocateCustomerId;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the modifiedTime
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * @param modifiedTime the modifiedTime to set
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * @return the status
	 */
	public EnumRafTransactionStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EnumRafTransactionStatus status) {
		this.status = status;
	}

	/**
	 * @return the advocateFirstName
	 */
	public String getAdvocateFirstName() {
		return advocateFirstName;
	}

	/**
	 * @param advocateFirstName the advocateFirstName to set
	 */
	public void setAdvocateFirstName(String advocateFirstName) {
		this.advocateFirstName = advocateFirstName;
	}

	/**
	 * @return the advocateLastName
	 */
	public String getAdvocateLastName() {
		return advocateLastName;
	}

	/**
	 * @param advocateLastName the advocateLastName to set
	 */
	public void setAdvocateLastName(String advocateLastName) {
		this.advocateLastName = advocateLastName;
	}

	/**
	 * @return the advocateEmail
	 */
	public String getAdvocateEmail() {
		return advocateEmail;
	}

	/**
	 * @param advocateEmail the advocateEmail to set
	 */
	public void setAdvocateEmail(String advocateEmail) {
		this.advocateEmail = advocateEmail;
	}

	/**
	 * @return the advocatePartnerUid
	 */
	public String getAdvocatePartnerUid() {
		return advocatePartnerUid;
	}

	/**
	 * @param advocatePartnerUid the advocatePartnerUid to set
	 */
	public void setAdvocatePartnerUid(String advocatePartnerUid) {
		this.advocatePartnerUid = advocatePartnerUid;
	}

	/**
	 * @return the friendFirstName
	 */
	public String getFriendFirstName() {
		return friendFirstName;
	}

	/**
	 * @param friendFirstName the friendFirstName to set
	 */
	public void setFriendFirstName(String friendFirstName) {
		this.friendFirstName = friendFirstName;
	}

	/**
	 * @return the friendLastName
	 */
	public String getFriendLastName() {
		return friendLastName;
	}

	/**
	 * @param friendLastName the friendLastName to set
	 */
	public void setFriendLastName(String friendLastName) {
		this.friendLastName = friendLastName;
	}

	/**
	 * @return the friendEmail
	 */
	public String getFriendEmail() {
		return friendEmail;
	}

	/**
	 * @param friendEmail the friendEmail to set
	 */
	public void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}

	/**
	 * @return the friendPartnerUid
	 */
	public String getFriendPartnerUid() {
		return friendPartnerUid;
	}

	/**
	 * @param friendPartnerUid the friendPartnerUid to set
	 */
	public void setFriendPartnerUid(String friendPartnerUid) {
		this.friendPartnerUid = friendPartnerUid;
	}

	/**
	 * @return the rewardType
	 */
	public String getRewardType() {
		return rewardType;
	}

	/**
	 * @param rewardType the rewardType to set
	 */
	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	/**
	 * @return the rewardDate
	 */
	public Date getRewardDate() {
		return rewardDate;
	}

	/**
	 * @param rewardDate the rewardDate to set
	 */
	public void setRewardDate(Date rewardDate) {
		this.rewardDate = rewardDate;
	}

	/**
	 * @return the rewardSetName
	 */
	public String getRewardSetName() {
		return rewardSetName;
	}

	/**
	 * @param rewardSetName the rewardSetName to set
	 */
	public void setRewardSetName(String rewardSetName) {
		this.rewardSetName = rewardSetName;
	}

	public double getRewardValue() {
		return rewardValue;
	}

	public void setRewardValue(double rewardValue) {
		this.rewardValue = rewardValue;
	}

	/**
	 * @return the rewardDetail
	 */
	public String getRewardDetail() {
		return rewardDetail;
	}

	/**
	 * @param rewardDetail the rewardDetail to set
	 */
	public void setRewardDetail(String rewardDetail) {
		this.rewardDetail = rewardDetail;
	}
	  
	/**
	 * @return the rewardSetId
	 */
	public String getRewardSetId() {
		return rewardSetId;
	}

	/**
	 * @param rewardSetId the rewardSetId to set
	 */
	public void setRewardSetId(String rewardSetId) {
		this.rewardSetId = rewardSetId;
	}
	
	/**
	 * @return the rewardDateString
	 */
	public String getRewardDateString() {
		return rewardDateString;
	}

	/**
	 * @param rewardDateString the rewardDateString to set
	 */
	public void setRewardDateString(String rewardDateString) {
		this.rewardDateString = rewardDateString;
	}

	public String getRewardValueString() {
		return rewardValueString;
	}

	public void setRewardValueString(String rewardValueString) {
		this.rewardValueString = rewardValueString;
	}

	@Override
	   public String toString() {
	      return "ADVOCATE_CUSTOMER_ID="+advocateCustomerId+", ID="+id+",STATUS="+status+", CREATION_TIME="+creationTime+", MODIFIED_TIME="+modifiedTime+",ADVOCATE_FIRST_NAME="+advocateFirstName+",ADVOCATE_LAST_NAME="+advocateLastName+",ADVOCATE_EMAIL="+advocateEmail+",ADVOCATE_PARTNER_UID"+advocatePartnerUid+", FRIEND_FIRST_NAME="+friendFirstName+",FRIEND_LAST_NAME="+friendLastName+",FRIEND_EMAIL="+friendEmail+",FRIEND_PARTNER_UID="+friendPartnerUid+",REWARD_TYPE="+rewardType+",REWARD_DATE="+rewardDate+",REWARD_SET_NAME="+rewardSetName+",REWARD_SET_ID="+rewardSetId+",REWARD_VALUE="+rewardValue+",REWARD_DETAIL="+rewardDetail+"\n";	   
    }
	
}
