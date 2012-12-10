package com.freshdirect.fdstore.content.customerrating;

import java.math.BigDecimal;

public class CustomerReviewsDTO {

	private String id;
	private String BVReviewId;
	private String productId;
	private String moderationStatus;
	private long lastModificationTime;
	private String externalId;
	private String displayName;
	private boolean anonymous;
	private boolean hyperlinkingEnabled;
	private boolean ratingsOnly;
	private String title;
	private String reviewText;
	private int numComments;
	private String campaignId;
	private BigDecimal rating;
	private int ratingRange;
	private boolean recommended;
	private int numFeedbacks;
	private int numPositiveFeedbacks;
	private int numNegativeFeedbacks;
	private String reviewerLocation;
	private String ipAddress;
	private String displayLocale;
	private long submissionTime;
	private String badgeName;
	private String badgeContentType;
	private String productReviewsURL;
	private String productReviewsDLURL;
	private boolean featured;
	private BigDecimal netPromoterScore;
	private String netPromoterComment;
	private String authenticationType;
	private String userEmailAddress;
	private boolean publishedEmailAlert;
	private boolean commentedEmailAlert;
	private int originatingDisplayCode;
	private String contentCodes;
	private long firstPublishTime;
	private long lastPublishTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getModerationStatus() {
		return moderationStatus;
	}
	public void setModerationStatus(String moderationStatus) {
		this.moderationStatus = moderationStatus;
	}
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	public boolean isHyperlinkingEnabled() {
		return hyperlinkingEnabled;
	}
	public void setHyperlinkingEnabled(boolean hyperlinkingEnabled) {
		this.hyperlinkingEnabled = hyperlinkingEnabled;
	}
	public boolean isRatingsOnly() {
		return ratingsOnly;
	}
	public void setRatingsOnly(boolean ratingsOnly) {
		this.ratingsOnly = ratingsOnly;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	public int getNumComments() {
		return numComments;
	}
	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public BigDecimal getRating() {
		return rating;
	}
	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	public int getRatingRange() {
		return ratingRange;
	}
	public void setRatingRange(int ratingRange) {
		this.ratingRange = ratingRange;
	}
	public boolean isRecommended() {
		return recommended;
	}
	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}
	public int getNumFeedbacks() {
		return numFeedbacks;
	}
	public void setNumFeedbacks(int numFeedbacks) {
		this.numFeedbacks = numFeedbacks;
	}
	public int getNumPositiveFeedbacks() {
		return numPositiveFeedbacks;
	}
	public void setNumPositiveFeedbacks(int numPositiveFeedbacks) {
		this.numPositiveFeedbacks = numPositiveFeedbacks;
	}
	public int getNumNegativeFeedbacks() {
		return numNegativeFeedbacks;
	}
	public void setNumNegativeFeedbacks(int numNegativeFeedbacks) {
		this.numNegativeFeedbacks = numNegativeFeedbacks;
	}
	public String getReviewerLocation() {
		return reviewerLocation;
	}
	public void setReviewerLocation(String reviewerLocation) {
		this.reviewerLocation = reviewerLocation;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDisplayLocale() {
		return displayLocale;
	}
	public void setDisplayLocale(String displayLocale) {
		this.displayLocale = displayLocale;
	}
	public long getSubmissionTime() {
		return submissionTime;
	}
	public void setSubmissionTime(long submissionTime) {
		this.submissionTime = submissionTime;
	}
	public String getBadgeName() {
		return badgeName;
	}
	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}
	public String getBadgeContentType() {
		return badgeContentType;
	}
	public void setBadgeContentType(String badgeContentType) {
		this.badgeContentType = badgeContentType;
	}
	public String getProductReviewsURL() {
		return productReviewsURL;
	}
	public void setProductReviewsURL(String productReviewsURL) {
		this.productReviewsURL = productReviewsURL;
	}
	public String getProductReviewsDLURL() {
		return productReviewsDLURL;
	}
	public void setProductReviewsDLURL(String productReviewsDLURL) {
		this.productReviewsDLURL = productReviewsDLURL;
	}
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}
	public String getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	public String getUserEmailAddress() {
		return userEmailAddress;
	}
	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}
	public boolean isPublishedEmailAlert() {
		return publishedEmailAlert;
	}
	public void setPublishedEmailAlert(boolean publishedEmailAlert) {
		this.publishedEmailAlert = publishedEmailAlert;
	}
	public boolean isCommentedEmailAlert() {
		return commentedEmailAlert;
	}
	public void setCommentedEmailAlert(boolean commentedEmailAlert) {
		this.commentedEmailAlert = commentedEmailAlert;
	}
	public int getOriginatingDisplayCode() {
		return originatingDisplayCode;
	}
	public void setOriginatingDisplayCode(int originatingDisplayCode) {
		this.originatingDisplayCode = originatingDisplayCode;
	}
	public void setFirstPublishTime(long firstPublishTime) {
		this.firstPublishTime = firstPublishTime;
	}
	public void setLastPublishTime(long lastPublishTime) {
		this.lastPublishTime = lastPublishTime;
	}
	public String getBVReviewId() {
		return BVReviewId;
	}
	public void setBVReviewId(String bVReviewId) {
		BVReviewId = bVReviewId;
	}
	public BigDecimal getNetPromoterScore() {
		return netPromoterScore;
	}
	public void setNetPromoterScore(BigDecimal netPromoterScore) {
		this.netPromoterScore = netPromoterScore;
	}
	public String getNetPromoterComment() {
		return netPromoterComment;
	}
	public void setNetPromoterComment(String netPromoterComment) {
		this.netPromoterComment = netPromoterComment;
	}
	public String getContentCodes() {
		return contentCodes;
	}
	public void setContentCodes(String contentCodes) {
		this.contentCodes = contentCodes;
	}
	public long getFirstPublishTime() {
		return firstPublishTime;
	}
	public long getLastPublishTime() {
		return lastPublishTime;
	}
	
}
