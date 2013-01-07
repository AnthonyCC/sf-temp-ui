package com.freshdirect.fdstore.content.customerrating;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;


public class StoreFeedTask {

	private static final Logger LOGGER = LoggerFactory.getInstance(StoreFeedTask.class);

	private String FEED_FILE = "";
	private String DOWNLOAD_PATH = "";

	private List<CustomerRatingsDTO> ratedProducts = new ArrayList<CustomerRatingsDTO>();
	
	public BazaarvoiceFeedProcessResult process(){
		
		FEED_FILE = FDStoreProperties.getBazaarvoiceDownloadFeedFile();
		DOWNLOAD_PATH = FDStoreProperties.getBazaarvoiceDownloadFeedTargetPath();
		
		try {
			parseFeedFileContent();
			CustomerRatingsDAO customerRatingsDAO = new CustomerRatingsDAO();
			customerRatingsDAO.purgeFeedFileContentStorage();
			customerRatingsDAO.storeFeedFileContent(ratedProducts);
			
		} catch (FDResourceException e) {
			LOGGER.error("Storing feed failed!",e);
			return new BazaarvoiceFeedProcessResult(false, e.getMessage());
		}
		
		LOGGER.info("Feed stored.");
		return new BazaarvoiceFeedProcessResult(true, null);
	}

	private void parseFeedFileContent() throws FDResourceException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			
			Document dom = db.parse(DOWNLOAD_PATH + FEED_FILE.substring(0, FEED_FILE.length() - 3));
			NodeList products = dom.getElementsByTagName("Product");

			if(products != null && products.getLength() > 0) {
				for(int i = 0 ; i < products.getLength();i++) {

					Element product = (Element)products.item(i);
					
					CustomerRatingsDTO ratedProduct = new CustomerRatingsDTO();
					
					ratedProduct.setProductId(product.getElementsByTagName("ExternalId").item(0).getTextContent());
					ratedProduct.setNumNativeQuestions(Integer.parseInt(product.getElementsByTagName("NumNativeQuestions").item(0).getTextContent()));
					ratedProduct.setNumQuestions(Integer.parseInt(product.getElementsByTagName("NumQuestions").item(0).getTextContent()));
					ratedProduct.setNumNativeAnswers(Integer.parseInt(product.getElementsByTagName("NumNativeAnswers").item(0).getTextContent()));
					ratedProduct.setNumAnswers(Integer.parseInt(product.getElementsByTagName("NumAnswers").item(0).getTextContent()));
					ratedProduct.setNumReviews(Integer.parseInt(product.getElementsByTagName("NumReviews").item(0).getTextContent()));
					ratedProduct.setNumStories(Integer.parseInt(product.getElementsByTagName("NumStories").item(0).getTextContent()));
					ratedProduct.setProductPageURL(product.getElementsByTagName("ProductPageUrl").item(0).getTextContent());
					ratedProduct.setProductReviewsURL(product.getElementsByTagName("ProductReviewsUrl").item(0).getTextContent());
					ratedProduct.setImageURL(product.getElementsByTagName("ImageUrl").item(0).getTextContent());
					
					Element reviewStatistic = (Element)product.getElementsByTagName("ReviewStatistics").item(0);
					
					ratedProduct.setAverageOverallRating((BigDecimal)getValue(BigDecimal.class, reviewStatistic, "AverageOverallRating"));
					ratedProduct.setOverallRatingRange(Integer.parseInt(reviewStatistic.getElementsByTagName("OverallRatingRange").item(0).getTextContent()));
					ratedProduct.setTotalReviewCount(Integer.parseInt(reviewStatistic.getElementsByTagName("TotalReviewCount").item(0).getTextContent()));
					ratedProduct.setRatingsOnlyReviewCount(Integer.parseInt(reviewStatistic.getElementsByTagName("RatingsOnlyReviewCount").item(0).getTextContent()));
					ratedProduct.setRecommendedCount(Integer.parseInt(reviewStatistic.getElementsByTagName("RecommendedCount").item(0).getTextContent()));
					ratedProduct.setNotRecommendedCount(Integer.parseInt(reviewStatistic.getElementsByTagName("NotRecommendedCount").item(0).getTextContent()));

					NodeList averageRatingValues = ((Element)reviewStatistic.getElementsByTagName("AverageRatingValues").item(0)).getElementsByTagName("AverageRatingValue");
					if (averageRatingValues != null && averageRatingValues.getLength() > 0) {
						for (int a = 0; a < averageRatingValues.getLength(); a++) {
							Element averageRatingValue = (Element)averageRatingValues.item(a);
							if("Quality".equals(averageRatingValue.getAttribute("id"))) {
								ratedProduct.setAverageRatingValuesQuality(new BigDecimal(averageRatingValue.getElementsByTagName("AverageRating").item(0).getTextContent()));
								ratedProduct.setAverageRatingValuesQualityRange(Integer.parseInt(averageRatingValue.getElementsByTagName("RatingRange").item(0).getTextContent()));
							} else if("Value".equals(averageRatingValue.getAttribute("id"))) {
								ratedProduct.setAverageRatingValuesValue(new BigDecimal(averageRatingValue.getElementsByTagName("AverageRating").item(0).getTextContent()));
								ratedProduct.setAverageRatingValuesValueRange(Integer.parseInt(averageRatingValue.getElementsByTagName("RatingRange").item(0).getTextContent()));
							}
						}
					}
					
					Element ratingDistributionItem = (Element)((Element)reviewStatistic.getElementsByTagName("RatingDistribution").item(0)).getElementsByTagName("RatingDistributionItem").item(0);
					
					ratedProduct.setRatingValue((Integer)getValue(Integer.class, ratingDistributionItem, "RatingValue"));
					ratedProduct.setCount((Integer)getValue(Integer.class, ratingDistributionItem, "Count"));

					Element nativeReviewStatistic = (Element)product.getElementsByTagName("NativeReviewStatistics").item(0);

					ratedProduct.setNatAverageOverallRating((BigDecimal)getValue(BigDecimal.class, nativeReviewStatistic, "AverageOverallRating"));
					ratedProduct.setNatOverallRatingRange(Integer.parseInt(nativeReviewStatistic.getElementsByTagName("OverallRatingRange").item(0).getTextContent()));
					ratedProduct.setNatTotalReviewCount(Integer.parseInt(nativeReviewStatistic.getElementsByTagName("TotalReviewCount").item(0).getTextContent()));
					ratedProduct.setNatRatingsOnlyReviewCount(Integer.parseInt(nativeReviewStatistic.getElementsByTagName("RatingsOnlyReviewCount").item(0).getTextContent()));
					ratedProduct.setNatRecommendedCount(Integer.parseInt(nativeReviewStatistic.getElementsByTagName("RecommendedCount").item(0).getTextContent()));
					ratedProduct.setNatNotRecommendedCount(Integer.parseInt(nativeReviewStatistic.getElementsByTagName("NotRecommendedCount").item(0).getTextContent()));
					
					NodeList natAverageRatingValues = ((Element)nativeReviewStatistic.getElementsByTagName("AverageRatingValues").item(0)).getElementsByTagName("AverageRatingValue");
					if (natAverageRatingValues != null && natAverageRatingValues.getLength() > 0) {
						for (int a = 0; a < natAverageRatingValues.getLength(); a++) {
							Element natAverageRatingValue = (Element)natAverageRatingValues.item(a);
							if("Quality".equals(natAverageRatingValue.getAttribute("id"))) {
								ratedProduct.setNatAverageRatingValuesQuality(new BigDecimal(natAverageRatingValue.getElementsByTagName("AverageRating").item(0).getTextContent()));
								ratedProduct.setNatAverageRatingValuesQualityRange(Integer.parseInt(natAverageRatingValue.getElementsByTagName("RatingRange").item(0).getTextContent()));
							} else if("Value".equals(natAverageRatingValue.getAttribute("id"))) {
								ratedProduct.setNatAverageRatingValuesValue(new BigDecimal(natAverageRatingValue.getElementsByTagName("AverageRating").item(0).getTextContent()));
								ratedProduct.setNatAverageRatingValuesValueRange(Integer.parseInt(natAverageRatingValue.getElementsByTagName("RatingRange").item(0).getTextContent()));
							}
						}
					}
					
					Element nativeRatingDistributionItem = (Element)((Element)nativeReviewStatistic.getElementsByTagName("RatingDistribution").item(0)).getElementsByTagName("RatingDistributionItem").item(0);
					
					ratedProduct.setNatRatingValue((Integer)getValue(Integer.class, nativeRatingDistributionItem, "RatingValue"));
					ratedProduct.setNatCount((Integer)getValue(Integer.class, nativeRatingDistributionItem, "Count"));

					ratedProduct.setExtractDate(sdf.parse(dom.getElementsByTagName("Feed").item(0).getAttributes().getNamedItem("extractDate").getNodeValue().substring(0,23)).getTime());

					NodeList reviews = ((Element)product.getElementsByTagName("Reviews").item(0)).getElementsByTagName("Review");

					if(reviews != null && reviews.getLength() > 0) {
						for(int r = 0 ; r < reviews.getLength();r++) {

							Element review = (Element)reviews.item(r);
							
							CustomerReviewsDTO customerReview = new CustomerReviewsDTO();
							
							customerReview.setBVReviewId(review.getAttribute("id"));
							customerReview.setProductId(ratedProduct.getProductId());
							customerReview.setModerationStatus(review.getElementsByTagName("ModerationStatus").item(0).getTextContent());
							customerReview.setLastModificationTime(sdf.parse(review.getElementsByTagName("LastModificationTime").item(0).getTextContent().substring(0,23)).getTime());

							Element userProfileReference = (Element)review.getElementsByTagName("UserProfileReference").item(0);

							customerReview.setExternalId(userProfileReference.getElementsByTagName("ExternalId").item(0).getTextContent());
							customerReview.setDisplayName((String)getValue(String.class, userProfileReference, "DisplayName"));
							customerReview.setAnonymous(Boolean.parseBoolean(userProfileReference.getElementsByTagName("Anonymous").item(0).getTextContent()));
							customerReview.setHyperlinkingEnabled(Boolean.parseBoolean(userProfileReference.getElementsByTagName("HyperlinkingEnabled").item(0).getTextContent()));
							
							customerReview.setRatingsOnly(Boolean.parseBoolean(review.getElementsByTagName("RatingsOnly").item(0).getTextContent()));
							customerReview.setTitle(review.getElementsByTagName("Title").item(0).getTextContent());
							customerReview.setReviewText(review.getElementsByTagName("ReviewText").item(0).getTextContent());
							customerReview.setNumComments(Integer.parseInt(review.getElementsByTagName("NumComments").item(0).getTextContent()));
							customerReview.setCampaignId(review.getElementsByTagName("CampaignId").item(0).getTextContent());
							customerReview.setRating(new BigDecimal(review.getElementsByTagName("Rating").item(0).getTextContent()));
							customerReview.setRatingRange(Integer.parseInt(review.getElementsByTagName("RatingRange").item(0).getTextContent()));
							customerReview.setRecommended((Boolean)getValue(Boolean.class, review, "Recommended"));
							customerReview.setNumFeedbacks(Integer.parseInt(review.getElementsByTagName("NumFeedbacks").item(0).getTextContent()));
							customerReview.setNumPositiveFeedbacks(Integer.parseInt(review.getElementsByTagName("NumPositiveFeedbacks").item(0).getTextContent()));
							customerReview.setNumNegativeFeedbacks(Integer.parseInt(review.getElementsByTagName("NumNegativeFeedbacks").item(0).getTextContent()));
							customerReview.setReviewerLocation((String)getValue(String.class, review, "ReviewerLocation"));
							customerReview.setIpAddress(review.getElementsByTagName("IpAddress").item(0).getTextContent());
							customerReview.setDisplayLocale(review.getElementsByTagName("DisplayLocale").item(0).getTextContent());
							customerReview.setSubmissionTime(sdf.parse(review.getElementsByTagName("SubmissionTime").item(0).getTextContent().substring(0,23)).getTime());

							Element badges = (Element)review.getElementsByTagName("Badges").item(0);
							if (badges != null) {
								Element badge = (Element)badges.getElementsByTagName("Badge").item(0);
	
								customerReview.setBadgeName(badge.getElementsByTagName("Name").item(0).getTextContent());
								customerReview.setBadgeContentType(badge.getElementsByTagName("ContentType").item(0).getTextContent());
							}
							customerReview.setProductReviewsURL(review.getElementsByTagName("ProductReviewsUrl").item(0).getTextContent());
							customerReview.setProductReviewsDLURL(review.getElementsByTagName("ProductReviewsDeepLinkedUrl").item(0).getTextContent());
							customerReview.setFeatured(Boolean.parseBoolean(review.getElementsByTagName("Featured").item(0).getTextContent()));
							customerReview.setNetPromoterScore((BigDecimal)getValue(BigDecimal.class, review, "NetPromoterScore"));
							customerReview.setNetPromoterComment((String)getValue(String.class, review, "NetPromoterComment"));
							customerReview.setAuthenticationType(review.getElementsByTagName("AuthenticationType").item(0).getTextContent());
							customerReview.setUserEmailAddress(review.getElementsByTagName("UserEmailAddress").item(0).getTextContent());
							customerReview.setPublishedEmailAlert(Boolean.parseBoolean(review.getElementsByTagName("SendEmailAlertWhenPublished").item(0).getTextContent()));
							customerReview.setCommentedEmailAlert(Boolean.parseBoolean(review.getElementsByTagName("SendEmailAlertWhenCommented").item(0).getTextContent()));
							customerReview.setOriginatingDisplayCode(Integer.parseInt(review.getElementsByTagName("OriginatingDisplayCode").item(0).getTextContent()));
							customerReview.setContentCodes((String)getValue(String.class, review, "ContentCodes"));
							customerReview.setFirstPublishTime(sdf.parse(review.getElementsByTagName("FirstPublishTime").item(0).getTextContent().substring(0,23)).getTime());
							customerReview.setLastPublishTime(sdf.parse(review.getElementsByTagName("LastPublishTime").item(0).getTextContent().substring(0,23)).getTime());

							ratedProduct.addReview(customerReview);
						}
					}
					
					ratedProducts.add(ratedProduct);
				}
			}


		}catch(ParserConfigurationException pce) {
        	throw new FDResourceException("parseFeedFileContent", pce);
		}catch(SAXException se) {
        	throw new FDResourceException("parseFeedFileContent", se);
		}catch(IOException ioe) {
        	throw new FDResourceException("parseFeedFileContent", ioe);
		} catch (DOMException de) {
        	throw new FDResourceException("parseFeedFileContent", de);
		} catch (ParseException pe) {
        	throw new FDResourceException("parseFeedFileContent", pe);
		}
	}
	
	
	//Safe generic way to get nullable elements
	private <T> Object getValue(T clazz, Element node, String nodeName) {
	
		if (clazz == String.class) {
			try {
				return node.getElementsByTagName(nodeName).item(0).getTextContent();
			} catch (Exception e) {
				return "";
			}
		
		} else if (clazz == Boolean.class) {
			try {
				return new Boolean(node.getElementsByTagName(nodeName).item(0).getTextContent());
			} catch (Exception e) {
				return new Boolean("false");
			}
		} else if (clazz == BigDecimal.class) {
			try {
				return new BigDecimal(node.getElementsByTagName(nodeName).item(0).getTextContent());
			} catch (Exception e) {
				return BigDecimal.ZERO;
			}
		} else if (clazz == Integer.class) {
			try {
				return new Integer(node.getElementsByTagName(nodeName).item(0).getTextContent());
			} catch (Exception e) {
				return new Integer(0);
			}
		}
		return null;
	}

}
