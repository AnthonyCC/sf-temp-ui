package com.freshdirect.mktAdmin.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

public class ReferralAdminModel implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private String description;
	private String getText;
	private String giveText;
	private String promotionId;
	private String expirationDate;
	private MultipartFile userListFile;
	private String referralFee;
	private String referralId;
	private String action;
	private boolean defaultPromo;
	private String shareHeader;
	private String shareText;
	private String giveHeader;
	private String getHeader;
	private String notes;
	private String promoDescription;
	private String fbFile;
	private String fbHeadline;
	private String fbText;
	private String twitterText;
	private String referralPageText;
	private String referralPageLegal;
	private String inviteEmailSubject;
	private String inviteEmailOfferText;
	private String inviteEmailText;
	private String inviteEmailLegal;
	private String referralCreditEmailSubject;
	private String referralCreditEmailText;
	private String userListFileHolder;
	private Collection<String> userCollection;
	private String addByUser;
	private String deleteFlag;
	private String siteAccessImageFile;
	
	public void setUserCollection(Collection<String> userCollection) {
		this.userCollection = userCollection;
	}
	public Collection<String> getUserCollection() {
		return userCollection;
	}
	public void setUserListFileHolder(String userListFileHolder) {
		this.userListFileHolder = userListFileHolder;
	}
	public String getUserListFileHolder() {
		return userListFileHolder;
	}
	public String getFbFile() {
		return fbFile;
	}
	public void setFbFile(String fbFile) {
		this.fbFile = fbFile;
	}
	public String getFbHeadline() {
		return fbHeadline;
	}
	public void setFbHeadline(String fbHeadline) {
		this.fbHeadline = fbHeadline;
	}
	public String getFbText() {
		return fbText;
	}
	public void setFbText(String fbText) {
		this.fbText = fbText;
	}
	public String getTwitterText() {
		return twitterText;
	}
	public void setTwitterText(String twitterText) {
		this.twitterText = twitterText;
	}
	public String getReferralPageText() {
		return referralPageText;
	}
	public void setReferralPageText(String referralPageText) {
		this.referralPageText = referralPageText;
	}
	public String getReferralPageLegal() {
		return referralPageLegal;
	}
	public void setReferralPageLegal(String referralPageLegal) {
		this.referralPageLegal = referralPageLegal;
	}
	public String getInviteEmailSubject() {
		return inviteEmailSubject;
	}
	public void setInviteEmailSubject(String inviteEmailSubject) {
		this.inviteEmailSubject = inviteEmailSubject;
	}
	public String getInviteEmailText() {
		return inviteEmailText;
	}
	public void setInviteEmailText(String inviteEmailText) {
		this.inviteEmailText = inviteEmailText;
	}
	public String getInviteEmailLegal() {
		return inviteEmailLegal;
	}
	public void setInviteEmailLegal(String inviteEmailLegal) {
		this.inviteEmailLegal = inviteEmailLegal;
	}
	public String getReferralCreditEmailSubject() {
		return referralCreditEmailSubject;
	}
	public void setReferralCreditEmailSubject(String referralCreditEmailSubject) {
		this.referralCreditEmailSubject = referralCreditEmailSubject;
	}
	public String getReferralCreditEmailText() {
		return referralCreditEmailText;
	}
	public void setReferralCreditEmailText(String referralCreditEmailText) {
		this.referralCreditEmailText = referralCreditEmailText;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGetText() {
		return getText;
	}
	public void setGetText(String getText) {
		this.getText = getText;
	}
	public String getGiveText() {
		return giveText;
	}
	public void setGiveText(String giveText) {
		this.giveText = giveText;
	}
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	@Override
	public String toString() {
		return "ReferralAdminModel [action=" + action + ", defaultPromo="
				+ defaultPromo + ", description=" + description
				+ ", expirationDate=" + expirationDate + ", fbFile=" + fbFile
				+ ", fbHeadline=" + fbHeadline + ", fbText=" + fbText
				+ ", getHeader=" + getHeader + ", getText=" + getText
				+ ", giveHeader=" + giveHeader + ", giveText=" + giveText
				+ ", inviteEmailLegal=" + inviteEmailLegal
				+ ", inviteEmailSubject=" + inviteEmailSubject
				+ ", inviteEmailOfferText=" + inviteEmailOfferText
				+ ", inviteEmailText=" + inviteEmailText + ", notes=" + notes
				+ ", promoDescription=" + promoDescription + ", promotionId="
				+ promotionId + ", referralCreditEmailSubject="
				+ referralCreditEmailSubject + ", referralCreditEmailText="
				+ referralCreditEmailText + ", referralFee=" + referralFee
				+ ", referralId=" + referralId + ", referralPageLegal="
				+ referralPageLegal + ", referralPageText=" + referralPageText
				+ ", shareHeader=" + shareHeader + ", shareText=" + shareText
				+ ", twitterText=" + twitterText + ", userListFile="
				+ userListFile + "]";
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setUserListFile(MultipartFile userList) {
		this.userListFile = userList;
	}
	public MultipartFile getUserListFile() {
		return userListFile;
	}
	public void setReferralFee(String referralFee) {
		this.referralFee = referralFee;
	}
	public String getReferralFee() {
		return referralFee;
	}
	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}
	public String getReferralId() {
		return referralId;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAction() {
		return action;
	}
	
	public byte[] getBytes()
    {
    	if(this.userListFile!=null) {
			try {
				return userListFile.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
    	} else
    	   return null; // need to throw application exception.    	
    }
	
	public String getFileType() {
    	String fileName=this.getName();
    	String fileType= fileName.substring(fileName.indexOf(".")+1,fileName.length());
    	return fileType;
    }
	
	public String getName(){
    	if(this.userListFile!=null) 
    		return userListFile.getOriginalFilename();
    	else
    	   return null; // need to throw application exception.
    }
	
	public boolean isEmpty() {
    	if(this.userListFile!=null) 
    		return userListFile.isEmpty();
    	else
    	   return false; // need to throw application exception.    	
    }
	public void setDefaultPromo(boolean defaultPromo) {
		this.defaultPromo = defaultPromo;
	}
	public boolean getDefaultPromo() {
		return defaultPromo;
	}
	public void setShareHeader(String shareHeader) {
		this.shareHeader = shareHeader;
	}
	public String getShareHeader() {
		return shareHeader;
	}
	public void setShareText(String shareText) {
		this.shareText = shareText;
	}
	public String getShareText() {
		return shareText;
	}
	public void setGiveHeader(String giveHeader) {
		this.giveHeader = giveHeader;
	}
	public String getGiveHeader() {
		return giveHeader;
	}
	public void setGetHeader(String getHeader) {
		this.getHeader = getHeader;
	}
	public String getGetHeader() {
		return getHeader;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getNotes() {
		return notes;
	}
	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}
	public String getPromoDescription() {
		return promoDescription;
	}
	public void setAddByUser(String addByUser) {
		this.addByUser = addByUser;
	}
	public String getAddByUser() {
		return addByUser;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setSiteAccessImageFile(String siteAccessImageFile) {
		this.siteAccessImageFile = siteAccessImageFile;
	}
	public String getSiteAccessImageFile() {
		return siteAccessImageFile;
	}
	public String getInviteEmailOfferText() {
		return inviteEmailOfferText;
	}
	public void setInviteEmailOfferText(String inviteEmailOfferText) {
		this.inviteEmailOfferText = inviteEmailOfferText;
	}
	
		

}
