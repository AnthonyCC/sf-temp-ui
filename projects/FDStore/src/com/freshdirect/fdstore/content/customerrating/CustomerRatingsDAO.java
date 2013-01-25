package com.freshdirect.fdstore.content.customerrating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.DataSourceLocator;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerRatingsDAO {

	private static final Logger LOGGER = LoggerFactory.getInstance(CustomerRatingsDAO.class);
	
	private String getNextId(Connection conn) throws SQLException {
		return SequenceGenerator.getNextId(conn, "MIS");
	}
	
	public void purgeFeedFileContentStorage() throws FDResourceException {
		String productSql = "delete from MIS.BV_PRODUCT_RATINGS";
		String reviewSql = "delete from MIS.BV_PRODUCT_REVIEWS";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(reviewSql);
			ps.execute();
			ps = connection.prepareStatement(productSql);
			ps.execute();
			
		} catch (SQLException e) { 
			LOGGER.error("Purging feed storage failed!",e);
        	throw new FDResourceException("purgeFeedFileContentStorage", e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();		
				}
			} catch (SQLException e) {
				LOGGER.error("Purging feed storage failed!",e);
	        	throw new FDResourceException("purgeFeedFileContentStorage", e);
			}		
		}
	}
	
	public void storeFeedFileContent(List<CustomerRatingsDTO> ratedProducts) throws FDResourceException {
		String ratingsSql = "insert into MIS.BV_PRODUCT_RATINGS " +
				"(ID, PRODUCT_ID, PRODUCT_PAGE_URL, PRODUCT_REVIEWS_URL, IMAGE_URL, NUM_NATIVE_QUESTIONS, NUM_QUESTIONS, NUM_NATIVE_ANSWERS, NUM_ANSWERS, " +
				"NUM_REVIEWS, NUM_STORIES, AVERAGE_OVERALL_RATING, OVERALL_RATING_RANGE, TOTAL_REVIEW_COUNT, RATINGS_ONLY_REVIEW_COUNT, RECOMMENDED_COUNT, " +
				"NOT_RECOMMENDED_COUNT, AVERAGE_RATING_QUALITY, AVERAGE_RATING_QUALITY_RANGE, AVERAGE_RATING_VALUE, AVERAGE_RATING_VALUE_RANGE, RATING_VALUE, " +
				"COUNT, NAT_AVERAGE_OVERALL_RATING, NAT_OVERALL_RATING_RANGE, NAT_TOTAL_REVIEW_COUNT, NAT_RATINGS_ONLY_REVIEW_COUNT, NAT_RECOMMENDED_COUNT, " +
				"NAT_NOT_RECOMMENDED_COUNT, NAT_AVERAGE_RATING_QUALITY, NAT_AVERAGE_RATING_QUALITY_R, NAT_AVERAGE_RATING_VALUE, NAT_AVERAGE_RATING_VALUE_RANGE, " +
				"NAT_RATING_VALUE, NAT_COUNT, EXTRACT_DATE) " +
				"values " +
				"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String reviewsSql = "insert into MIS.BV_PRODUCT_REVIEWS " +
				"(ID, PRODUCT_ID, BV_REVIEW_ID, MODERATION_STATUS, LAST_MODIFICATION_TIME, EXTERNAL_ID, DISPLAY_NAME, ANONYMUS, HYPERLINKING_ENABLED, RATINGS_ONLY, " +
				"TITLE, REVIEW_TEXT, NUM_COMMENTS, CAMPAIGN_ID, RATING, RATING_RANGE, RECOMMENDED, NUM_FEEDBACKS, NUM_POSITIVE_FEEDBACKS, NUM_NEGATIVE_FEEDBACKS, " +
				"REVIEWER_LOCATION, IP_ADDRESS, DISPLAY_LOCALE, SUBMISSION_TIME, BADGE_NAME, BADGE_CONTENT_TYPE, PRODUCT_REVIEWS_URL, PRODUCT_REVIEWS_DL_URL, FEATURED, " +
				"NET_PROMOTER_SCORE, NET_PROMOTER_COMMENT, AUTHENTICATION_TYPE, USER_EMAIL_ADDRESS, PUBLISHED_EMAIL_ALERT, COMMENTED_EMAIL_ALERT, ORIGINATING_DISPLAY_CODE, " +
				"CONTENT_CODES, FIRST_PUBLISH_TIME, LAST_PUBLISH_TIME)" +
				"values " +
				"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = getConnection();
			ps = connection.prepareStatement(ratingsSql);
			 
			final int batchSize = 1000;
			int count = 0;
			 
			for (CustomerRatingsDTO ratedProduct: ratedProducts) {
			 
				ps.setString(1, getNextId(connection));
				ps.setString(2, ratedProduct.getProductId());
				ps.setString(3, ratedProduct.getProductPageURL());
				ps.setString(4, ratedProduct.getProductReviewsURL());
				ps.setString(5, ratedProduct.getImageURL());
				ps.setInt(6, ratedProduct.getNumNativeQuestions());
				ps.setInt(7, ratedProduct.getNumQuestions());
				ps.setInt(8, ratedProduct.getNumNativeAnswers());
				ps.setInt(9, ratedProduct.getNumAnswers());
				ps.setInt(10, ratedProduct.getNumReviews());
				ps.setInt(11, ratedProduct.getNumStories());
				ps.setBigDecimal(12, ratedProduct.getAverageOverallRating());
				ps.setInt(13, ratedProduct.getOverallRatingRange());
				ps.setInt(14, ratedProduct.getTotalReviewCount());
				ps.setInt(15, ratedProduct.getRatingsOnlyReviewCount());
				ps.setInt(16, ratedProduct.getRecommendedCount());
				ps.setInt(17, ratedProduct.getNotRecommendedCount());
				ps.setBigDecimal(18, ratedProduct.getAverageRatingValuesQuality());
				ps.setInt(19, ratedProduct.getAverageRatingValuesQualityRange());
				ps.setBigDecimal(20, ratedProduct.getAverageRatingValuesValue());
				ps.setInt(21, ratedProduct.getAverageRatingValuesValueRange());
				ps.setInt(22, ratedProduct.getRatingValue());
				ps.setInt(23, ratedProduct.getCount());
				ps.setBigDecimal(24, ratedProduct.getNatAverageOverallRating());
				ps.setInt(25, ratedProduct.getNatOverallRatingRange());
				ps.setInt(26, ratedProduct.getNatTotalReviewCount());
				ps.setInt(27, ratedProduct.getNatRatingsOnlyReviewCount());
				ps.setInt(28, ratedProduct.getNatRecommendedCount());
				ps.setInt(29, ratedProduct.getNatNotRecommendedCount());
				ps.setBigDecimal(30, ratedProduct.getNatAverageRatingValuesQuality());
				ps.setInt(31, ratedProduct.getNatAverageRatingValuesQualityRange());
				ps.setBigDecimal(32, ratedProduct.getNatAverageRatingValuesValue());
				ps.setInt(33, ratedProduct.getNatAverageRatingValuesValueRange());
				ps.setInt(34, ratedProduct.getNatRatingValue());
				ps.setInt(35, ratedProduct.getNatCount());
				ps.setTimestamp(36, new java.sql.Timestamp(ratedProduct.getExtractDate()));

			    ps.addBatch();
			     
			    if(++count % batchSize == 0) {
			        ps.executeBatch();
			    }
			}
			ps.executeBatch();

			ps = connection.prepareStatement(reviewsSql);
			 
			count = 0;
			 
			for (CustomerRatingsDTO ratedProduct: ratedProducts) {
				for (CustomerReviewsDTO customerReview: ratedProduct.getReviews()) {
			 
					ps.setString(1, getNextId(connection));
					ps.setString(2, ratedProduct.getProductId());
					ps.setString(3, customerReview.getBVReviewId());
					ps.setString(4, customerReview.getModerationStatus());
					ps.setTimestamp(5, new java.sql.Timestamp(customerReview.getLastModificationTime()));
					ps.setString(6, customerReview.getExternalId());
					ps.setString(7, customerReview.getDisplayName());
					ps.setBoolean(8, customerReview.isAnonymous());
					ps.setBoolean(9, customerReview.isHyperlinkingEnabled());
					ps.setBoolean(10, customerReview.isRatingsOnly());
					ps.setString(11, customerReview.getTitle());
					ps.setString(12, customerReview.getReviewText());
					ps.setInt(13, customerReview.getNumComments());
					ps.setString(14, customerReview.getCampaignId());
					ps.setBigDecimal(15, customerReview.getRating());
					ps.setInt(16, customerReview.getRatingRange());
					ps.setBoolean(17, customerReview.isRecommended());
					ps.setInt(18, customerReview.getNumFeedbacks());
					ps.setInt(19, customerReview.getNumPositiveFeedbacks());
					ps.setInt(20, customerReview.getNumNegativeFeedbacks());
					ps.setString(21, customerReview.getReviewerLocation());
					ps.setString(22, customerReview.getIpAddress());
					ps.setString(23, customerReview.getDisplayLocale());
					ps.setTimestamp(24, new java.sql.Timestamp(customerReview.getSubmissionTime()));
					ps.setString(25, customerReview.getBadgeName());
					ps.setString(26, customerReview.getBadgeContentType());
					ps.setString(27, customerReview.getProductReviewsURL());
					ps.setString(28, customerReview.getProductReviewsDLURL());
					ps.setBoolean(29, customerReview.isFeatured());
					ps.setBigDecimal(30, customerReview.getNetPromoterScore());
					ps.setString(31, customerReview.getNetPromoterComment());
					ps.setString(32, customerReview.getAuthenticationType());
					ps.setString(33, customerReview.getUserEmailAddress());
					ps.setBoolean(34, customerReview.isPublishedEmailAlert());
					ps.setBoolean(35, customerReview.isCommentedEmailAlert());
					ps.setString(36, customerReview.getOriginatingDisplayCode());
					ps.setString(37, customerReview.getContentCodes());
					ps.setTimestamp(38, new java.sql.Timestamp(customerReview.getFirstPublishTime()));
					ps.setTimestamp(39, new java.sql.Timestamp(customerReview.getLastPublishTime()));
	
					ps.addBatch();
				     
				    if(++count % batchSize == 0) {
				        ps.executeBatch();
				    }
				}
			}
			ps.executeBatch();

		} catch (SQLException e) { 
			LOGGER.error("Storing feed failed!",e);
        	throw new FDResourceException("storeFeedFileContent", e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();		
				}
			} catch (SQLException e) {
				LOGGER.error("Storing feed failed!",e);
	        	throw new FDResourceException("storeFeedFileContent", e);
			}		
		}
		
	}
	
	public Map<String,CustomerRatingsDTO> getCustomerRatings() throws FDResourceException {
		String sql = "select * from MIS.BV_PRODUCT_RATINGS";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,CustomerRatingsDTO> ratedProducts = new HashMap<String,CustomerRatingsDTO>();
		
		try {
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery(); 
			 
			while (rs.next()) {
				
				CustomerRatingsDTO ratedProduct = new CustomerRatingsDTO();
				ratedProduct.setId(rs.getString("id"));
				String productId = rs.getString("product_id");
				ratedProduct.setProductId(productId);
				ratedProduct.setAverageOverallRating(rs.getBigDecimal("average_overall_rating"));
				ratedProduct.setTotalReviewCount(rs.getInt("total_review_count"));
				ratedProduct.setRatingValue(rs.getInt("rating_value"));
			    
				if (productId!=null){
					ratedProducts.put(productId, ratedProduct);
				}
				
			}
		} catch (SQLException e) { 
			LOGGER.error("Getting rated products failed!",e);
        	throw new FDResourceException("getCustomerRatings", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();		
				}
			} catch (SQLException e) {
				LOGGER.error("Getting rated products failed!!",e);
	        	throw new FDResourceException("getCustomerRatings", e);
			}		
		}
		return ratedProducts;
		
	}
	
	public long getTimestamp() throws FDResourceException {
		
		long timestamp = -1;
		
		String sql = "select extract_date from MIS.BV_PRODUCT_RATINGS where rownum = 1";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery(); 
			 
			if (rs.next()) {
				timestamp = rs.getTimestamp("extract_date").getTime();
			}
		} catch (SQLException e) { 
			LOGGER.error("Getting ratings timestamp failed!",e);
        	throw new FDResourceException("getTimeStamp", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();		
				}
			} catch (SQLException e) {
				LOGGER.error("Getting ratings timestamp failed!",e);
	        	throw new FDResourceException("getTimeStamp", e);
			}		
		}
		return timestamp;

	}
	
	public Connection getConnection() throws SQLException {
   		return DataSourceLocator.getConnectionByDatasource("fddatasource");
    }
	
}
