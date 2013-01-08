package com.freshdirect.fdstore.content.customerrating;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerRatingsDTO implements Serializable {

	private String id;
	private String productId;
	private String productPageURL;
	private String productReviewsURL;
	private String imageURL;
	private int numNativeQuestions;
	private int numQuestions;
	private int numNativeAnswers;
	private int numAnswers;
	private int numReviews;
	private int numStories;
	private BigDecimal averageOverallRating;
	private int overallRatingRange;
	private int totalReviewCount;
	private int ratingsOnlyReviewCount;
	private int recommendedCount;
	private int notRecommendedCount;
	private BigDecimal averageRatingValuesQuality;
	private int averageRatingValuesQualityRange;
	private BigDecimal averageRatingValuesValue;
	private int averageRatingValuesValueRange;
	private int ratingValue;
	private int count;
	private BigDecimal natAverageOverallRating;
	private int natOverallRatingRange;
	private int natTotalReviewCount;
	private int natRatingsOnlyReviewCount;
	private int natRecommendedCount;
	private int natNotRecommendedCount;
	private BigDecimal natAverageRatingValuesQuality;
	private int natAverageRatingValuesQualityRange;
	private BigDecimal natAverageRatingValuesValue;
	private int natAverageRatingValuesValueRange;
	private BigDecimal natAverageRatingValues;
	private int natRatingValue;
	private int natCount;
	private long extractDate;
	private List<CustomerReviewsDTO> reviews = new ArrayList<CustomerReviewsDTO>();
	
//	public CustomerRatingsDTO() {};
	
//	public CustomerRatingsDTO(String id, String productId, BigDecimal averageOverallRating, int totalReviewCount, int ratingValue) {
//		
//		this.id = id;
//		this.productId = productId;
//		this.averageOverallRating = averageOverallRating;
//		this.totalReviewCount = totalReviewCount;
//		this.ratingValue = ratingValue;
//	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public BigDecimal getAverageOverallRating() {
		return averageOverallRating;
	}

	public void setAverageOverallRating(BigDecimal averageOverallRating) {
		this.averageOverallRating = averageOverallRating;
	}

	public int getTotalReviewCount() {
		return totalReviewCount;
	}

	public void setTotalReviewCount(int totalReviewCount) {
		this.totalReviewCount = totalReviewCount;
	}

	public int getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(int ratingValue) {
		this.ratingValue = ratingValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductPageURL() {
		return productPageURL;
	}

	public void setProductPageURL(String productPageURL) {
		this.productPageURL = productPageURL;
	}

	public String getProductReviewsURL() {
		return productReviewsURL;
	}

	public void setProductReviewsURL(String productReviewsURL) {
		this.productReviewsURL = productReviewsURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getOverallRatingRange() {
		return overallRatingRange;
	}

	public void setOverallRatingRange(int overallRatingRange) {
		this.overallRatingRange = overallRatingRange;
	}

	public int getRatingsOnlyReviewCount() {
		return ratingsOnlyReviewCount;
	}

	public void setRatingsOnlyReviewCount(int ratingsOnlyReviewCount) {
		this.ratingsOnlyReviewCount = ratingsOnlyReviewCount;
	}

	public int getRecommendedCount() {
		return recommendedCount;
	}

	public void setRecommendedCount(int recommendedCount) {
		this.recommendedCount = recommendedCount;
	}

	public int getNotRecommendedCount() {
		return notRecommendedCount;
	}

	public void setNotRecommendedCount(int notRecommendedCount) {
		this.notRecommendedCount = notRecommendedCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getNatAverageOverallRating() {
		return natAverageOverallRating;
	}

	public void setNatAverageOverallRating(BigDecimal natAverageOverallRating) {
		this.natAverageOverallRating = natAverageOverallRating;
	}

	public int getNatOverallRatingRange() {
		return natOverallRatingRange;
	}

	public void setNatOverallRatingRange(int natOverallRatingRange) {
		this.natOverallRatingRange = natOverallRatingRange;
	}

	public int getNatTotalReviewCount() {
		return natTotalReviewCount;
	}

	public void setNatTotalReviewCount(int natTotalReviewCount) {
		this.natTotalReviewCount = natTotalReviewCount;
	}

	public int getNatRatingsOnlyReviewCount() {
		return natRatingsOnlyReviewCount;
	}

	public void setNatRatingsOnlyReviewCount(int natRatingsOnlyReviewCount) {
		this.natRatingsOnlyReviewCount = natRatingsOnlyReviewCount;
	}

	public int getNatRecommendedCount() {
		return natRecommendedCount;
	}

	public void setNatRecommendedCount(int natRecommendedCount) {
		this.natRecommendedCount = natRecommendedCount;
	}

	public int getNatNotRecommendedCount() {
		return natNotRecommendedCount;
	}

	public void setNatNotRecommendedCount(int natNotRecommendedCount) {
		this.natNotRecommendedCount = natNotRecommendedCount;
	}

	public BigDecimal getNatAverageRatingValues() {
		return natAverageRatingValues;
	}

	public void setNatAverageRatingValues(BigDecimal natAverageRatingValues) {
		this.natAverageRatingValues = natAverageRatingValues;
	}

	public int getNatRatingValue() {
		return natRatingValue;
	}

	public void setNatRatingValue(int natRatingValue) {
		this.natRatingValue = natRatingValue;
	}

	public int getNatCount() {
		return natCount;
	}

	public void setNatCount(int natCount) {
		this.natCount = natCount;
	}

	public long getExtractDate() {
		return extractDate;
	}

	public void setExtractDate(long extractDate) {
		this.extractDate = extractDate;
	}

	public List<CustomerReviewsDTO> getReviews() {
		return reviews;
	}

	public void setReviews(List<CustomerReviewsDTO> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(CustomerReviewsDTO review) {
		this.reviews.add(review);
	}

	public int getNumNativeQuestions() {
		return numNativeQuestions;
	}

	public void setNumNativeQuestions(int numNativeQuestions) {
		this.numNativeQuestions = numNativeQuestions;
	}

	public int getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(int numQuestions) {
		this.numQuestions = numQuestions;
	}

	public int getNumNativeAnswers() {
		return numNativeAnswers;
	}

	public void setNumNativeAnswers(int numNativeAnswers) {
		this.numNativeAnswers = numNativeAnswers;
	}

	public int getNumAnswers() {
		return numAnswers;
	}

	public void setNumAnswers(int numAnswers) {
		this.numAnswers = numAnswers;
	}

	public int getNumReviews() {
		return numReviews;
	}

	public void setNumReviews(int numReviews) {
		this.numReviews = numReviews;
	}

	public int getNumStories() {
		return numStories;
	}

	public void setNumStories(int numStories) {
		this.numStories = numStories;
	}

	public BigDecimal getAverageRatingValuesQuality() {
		return averageRatingValuesQuality;
	}

	public void setAverageRatingValuesQuality(BigDecimal averageRatingValuesQuality) {
		this.averageRatingValuesQuality = averageRatingValuesQuality;
	}

	public int getAverageRatingValuesQualityRange() {
		return averageRatingValuesQualityRange;
	}

	public void setAverageRatingValuesQualityRange(
			int averageRatingValuesQualityRange) {
		this.averageRatingValuesQualityRange = averageRatingValuesQualityRange;
	}

	public BigDecimal getAverageRatingValuesValue() {
		return averageRatingValuesValue;
	}

	public void setAverageRatingValuesValue(BigDecimal averageRatingValuesValue) {
		this.averageRatingValuesValue = averageRatingValuesValue;
	}

	public int getAverageRatingValuesValueRange() {
		return averageRatingValuesValueRange;
	}

	public void setAverageRatingValuesValueRange(int averageRatingValuesValueRange) {
		this.averageRatingValuesValueRange = averageRatingValuesValueRange;
	}

	public BigDecimal getNatAverageRatingValuesQuality() {
		return natAverageRatingValuesQuality;
	}

	public void setNatAverageRatingValuesQuality(
			BigDecimal natAverageRatingValuesQuality) {
		this.natAverageRatingValuesQuality = natAverageRatingValuesQuality;
	}

	public int getNatAverageRatingValuesQualityRange() {
		return natAverageRatingValuesQualityRange;
	}

	public void setNatAverageRatingValuesQualityRange(
			int natAverageRatingValuesQualityRange) {
		this.natAverageRatingValuesQualityRange = natAverageRatingValuesQualityRange;
	}

	public BigDecimal getNatAverageRatingValuesValue() {
		return natAverageRatingValuesValue;
	}

	public void setNatAverageRatingValuesValue(
			BigDecimal natAverageRatingValuesValue) {
		this.natAverageRatingValuesValue = natAverageRatingValuesValue;
	}

	public int getNatAverageRatingValuesValueRange() {
		return natAverageRatingValuesValueRange;
	}

	public void setNatAverageRatingValuesValueRange(
			int natAverageRatingValuesValueRange) {
		this.natAverageRatingValuesValueRange = natAverageRatingValuesValueRange;
	}
}
