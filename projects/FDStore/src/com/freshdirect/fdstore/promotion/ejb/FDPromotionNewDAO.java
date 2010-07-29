package com.freshdirect.fdstore.promotion.ejb;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.promotion.ActiveInactiveStrategy;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.AudienceStrategy;
import com.freshdirect.fdstore.promotion.CartStrategy;
import com.freshdirect.fdstore.promotion.CompositeStrategy;
import com.freshdirect.fdstore.promotion.CustomerStrategy;
import com.freshdirect.fdstore.promotion.DCPDLineItemStrategy;
import com.freshdirect.fdstore.promotion.DateRangeStrategy;
import com.freshdirect.fdstore.promotion.DlvZoneStrategy;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumOrderType;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.FraudStrategy;
import com.freshdirect.fdstore.promotion.GeographyStrategy;
import com.freshdirect.fdstore.promotion.HeaderDiscountApplicator;
import com.freshdirect.fdstore.promotion.HeaderDiscountRule;
import com.freshdirect.fdstore.promotion.LimitedUseStrategy;
import com.freshdirect.fdstore.promotion.LineItemDiscountApplicator;
import com.freshdirect.fdstore.promotion.MaxLineItemCountStrategy;
import com.freshdirect.fdstore.promotion.MaxRedemptionStrategy;
import com.freshdirect.fdstore.promotion.OrderTypeStrategy;
import com.freshdirect.fdstore.promotion.PercentOffApplicator;
import com.freshdirect.fdstore.promotion.PerishableLineItemStrategy;
import com.freshdirect.fdstore.promotion.ProfileAttributeStrategy;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromoVariantModelImpl;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionDlvDate;
import com.freshdirect.fdstore.promotion.PromotionDlvTimeSlot;
import com.freshdirect.fdstore.promotion.PromotionGeography;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.PromotionStrategyI;
import com.freshdirect.fdstore.promotion.RecommendationStrategy;
import com.freshdirect.fdstore.promotion.RecommendedLineItemStrategy;
import com.freshdirect.fdstore.promotion.RedemptionCodeStrategy;
import com.freshdirect.fdstore.promotion.RuleBasedPromotionStrategy;
import com.freshdirect.fdstore.promotion.SampleLineApplicator;
import com.freshdirect.fdstore.promotion.SampleStrategy;
import com.freshdirect.fdstore.promotion.WaiveChargeApplicator;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDPromotionNewDAO {
	private final static Logger LOGGER = LoggerFactory.getInstance(FDPromotionNewDAO.class);

	/**
	 * Loads all active automatic promotions(redemption_code is null) which were not expired in last 7 days.
	 * 
	 * @param promoStrategyMap Map of String(promotionCode) -> PromotionStrategy
	 * @return List of Promotion
	 */
	private final static String getAllAutomaticPromotions = "SELECT * FROM CUST.PROMOTION_NEW p where p.status STATUSES and " +
	 							"(p.expiration_date > (sysdate-7) or p.expiration_date is null) and p.redemption_code is null order by p.modify_date desc";
	public static List<PromotionI> loadAllAutomaticPromotions(Connection conn) throws SQLException {
		final String query = getAllAutomaticPromotions.replace("STATUSES", getStatusReplacementString());
		LOGGER.debug("Query is "+query);

		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		List<PromotionI> promotions =  loadPromotions(conn, rs, null);
		rs.close();
		ps.close();
		return promotions;
	}

	private static String getStatusReplacementString() {
		
		String[] statuses = FDStoreProperties.getPromoValidRTStatuses().split(",");
		StringBuffer buf = new StringBuffer();
		if(statuses.length > 1) {
			buf.append("in (");	
			for (int i = 0; i < statuses.length; i++) {
	 			buf.append("'").append(statuses[i].trim()).append("'");
				if(i < statuses.length - 1) 
					buf.append(",");
			}
			buf.append(")");	
		}else {
 			buf.append("='").append(statuses[0].trim()).append("'");
		}
		return buf.toString();
	}
	
	private final static String getModifiedOnlyPromotions = "SELECT * FROM CUST.PROMOTION_NEW where modify_date > ? order by modify_date desc";
	
	public static List<PromotionI> loadModifiedOnlyPromotions(Connection conn, Date lastModified) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(getModifiedOnlyPromotions);
		ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		ResultSet rs = ps.executeQuery();
		List<PromotionI> promotions = loadPromotions(conn, rs, lastModified);
		rs.close();
		ps.close();
		return promotions;
	}
	
	private static List<PromotionI> loadPromotions(Connection conn, ResultSet rs, Date paramLastModified) throws SQLException {
		Map<PrimaryKey,GeographyStrategy> geoStrategies = loadGeographyStrategies(conn, paramLastModified);
		//List audiencePromoCodes = getAudienceBasedPromotionCodes(conn, paramLastModified);
		Map<PrimaryKey, CustomerStrategy> customerStrategies = loadCustomerStrategies(conn, paramLastModified);
		Map<PrimaryKey,PromotionStrategyI> profileStrategies = loadAllProfiles(conn, paramLastModified);
		Map<PrimaryKey, DCPDLineItemStrategy> dcpdData = loadDCPDData(conn, paramLastModified);
		Map<PrimaryKey, CartStrategy> cartStrategyData = loadCartStrategies(conn, paramLastModified);
		Map<String,DlvZoneStrategy> dlvZoneStrategies = loadDlvZoneStrategies(conn,paramLastModified);
		List<PromotionI> promos = new ArrayList<PromotionI>();
		while (rs.next()) {
			PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
			String promoCode = rs.getString("CODE");
			String name = rs.getString("NAME");
			String description = rs.getString("DESCRIPTION");
			EnumPromotionType promoType = EnumPromotionType.getEnum(rs.getString("CAMPAIGN_CODE"));
			if (promoType == null)
				continue;
		
			Timestamp lastModified = rs.getTimestamp("MODIFY_DATE");	
			Promotion promo = new Promotion(pk, promoType, promoCode, name, description, lastModified);
			EnumOfferType offerType = EnumOfferType.getEnum(rs.getString("OFFER_TYPE"));
			promo.setOfferType(offerType);
			if("Y".equalsIgnoreCase(rs.getString("NEEDCUSTOMERLIST"))){
				//This is customer restricted Promotion. Create Audience Strategy.
				int rollingExpirationDays = rs.getInt("ROLLING_EXPIRATION_DAYS");
				//APPDEV-659 - Made isMaxUsagePerCustomer obsolete.
				AudienceStrategy aStrategy = new AudienceStrategy(false, rollingExpirationDays);
				promo.addStrategy(aStrategy);
			}
			String excludeSkusFromSubTotal = rs.getString("EXCLUDE_SKU_SUBTOTAL");
			if(excludeSkusFromSubTotal != null && excludeSkusFromSubTotal.length() > 0){
				promo.setExcludeSkusFromSubTotal(excludeSkusFromSubTotal);
			}
			String status = rs.getString("STATUS");
			boolean onHold = "Y".equalsIgnoreCase(rs.getString("ON_HOLD"));
			promo.addStrategy(new ActiveInactiveStrategy(FDStoreProperties.getPromoValidRTStatuses().contains(status) && !onHold));
			
			promo.addStrategy(new LimitedUseStrategy(rs.getInt("MAX_USAGE")));
			int redeemCnt = rs.getInt("REDEEM_CNT");
			if(!rs.wasNull() && redeemCnt > 0)
				promo.addStrategy(new MaxRedemptionStrategy(redeemCnt));
			
			if("X".equalsIgnoreCase(rs.getString("RULE_BASED"))) {
				promo.addStrategy(new RuleBasedPromotionStrategy());
			}

			promo.addStrategy(new DateRangeStrategy(rs.getTimestamp("START_DATE"), rs.getTimestamp("EXPIRATION_DATE")));
			/*if (custStrategies.get(promo.getPK()) != null) {
				promo.addStrategy((AssignedCustomerStrategy) custStrategies.get(promo.getPK()));
			}*/

			String redemptionCode = rs.getString("REDEMPTION_CODE");
			if (!rs.wasNull()) {
				promo.addStrategy(new RedemptionCodeStrategy(redemptionCode));
			}
			
			if(!"X".equals(rs.getString("DONOT_APPLY_FRAUD"))){
				promo.addStrategy(new FraudStrategy());
			}
			//TODO This needs to replaced by Customer strategy. 	
			//decorateOrderTypestrategy(rs, promo);

			PromotionStrategyI geoStrategy = geoStrategies.get(pk);
			if (geoStrategy != null) {
				promo.addStrategy(geoStrategy);
			}

			//TODO This needs to replaced by Cart strategy 	
			decorateSampleStrategy(rs, promo);
			
			//Load the profile strategy
			PromotionStrategyI profStrategy = profileStrategies.get(pk);
			if (profStrategy != null) {
				promo.addStrategy(profStrategy);
			}
			
			if(promoType.getName().equals(EnumPromotionType.LINE_ITEM.getName())){
				//boolean recItemsOnly = "X".equalsIgnoreCase(rs.getString("RECOMMENDED_ITEMS_ONLY"));
				boolean favoritesOnly = "X".equalsIgnoreCase(rs.getString("FAVORITES_ONLY"));
				if(favoritesOnly) {
					promo.addStrategy(new RecommendationStrategy());
				}
				//promo.addLineItemStrategy(new RecommendedItemStrategy());
			}
			if("Y".equalsIgnoreCase(rs.getString("COMBINE_OFFER"))){				
				promo.setCombineOffer(true);		
			}
				
			//Add Customer Strategy
			PromotionStrategyI custStrategy = customerStrategies.get(pk);
			if (custStrategy != null) {
				promo.addStrategy(custStrategy);
			}

			
			PromotionStrategyI cartStrategyI = cartStrategyData.get(pk);
			if(null != cartStrategyI){
				CartStrategy cartStrategy = (CartStrategy)cartStrategyI;
				cartStrategy.setNeedDryGoods(("X".equalsIgnoreCase(rs.getString("NEEDDRYGOODS"))|| "X".equalsIgnoreCase(rs.getString("NEEDDRYGOODS")))?true:false);
				cartStrategy.setMinSkuQuantity(rs.getInt("HASSKUQUANTITY"));
				promo.addStrategy(cartStrategy);				
			}
			
			PromotionApplicatorI applicator = loadApplicator(rs);	
			PromotionStrategyI dlvZoneStrategyI = dlvZoneStrategies.get(pk.getId());
			//Set the zone strategy if applicable.
			if(null != applicator && null != dlvZoneStrategyI){
				DlvZoneStrategy dlvZoneStrategy = (DlvZoneStrategy)dlvZoneStrategyI;
				if((null !=dlvZoneStrategy.getDlvDates() && !dlvZoneStrategy.getDlvDates().isEmpty()) || null != dlvZoneStrategy.getDlvZoneId()){			
					applicator.setZoneStrategy(dlvZoneStrategy);
				}
			}
			if(null != applicator && applicator instanceof LineItemDiscountApplicator){
				/*
				 * If the promotion is a Line item Discount Promotion, add the corresponding 
				 * DCPD line item strategy if present.
				 */
				DCPDLineItemStrategy strategy = dcpdData.get(pk);
				if(strategy != null) {
					((LineItemDiscountApplicator) applicator).addLineItemStrategy(strategy);
				}
			} 

			if (applicator != null) {
				promo.setApplicator(applicator);
			}

			promos.add(promo);
		}
		return promos;
	}
	
	private final static String getAllActiveAutomaticPromotionCodes = "SELECT CODE, MODIFY_DATE FROM CUST.PROMOTION_NEW p where p.status STATUSES and " +
		"(p.expiration_date > (sysdate-7) or p.expiration_date is null) and p.redemption_code is null";
	
	/**
	 * This method returns all active automatic promotion codes along
	 * with their last modified timestamp.
	 * TODO Later the Promotion codes has to be replaced with Promotion IDs when
	 * new data model changes for AI is implemented.
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,Timestamp> getAllAutomaticPromotionCodes(Connection conn) throws SQLException {
		Map<String,Timestamp> promoCodes = new HashMap<String,Timestamp>();
		//TODO later the where clause need to be changed to point to ID column instead of Code. 
		String query = getAllActiveAutomaticPromotionCodes.replace("STATUSES", getStatusReplacementString());
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String promoCode = rs.getString("CODE");
			Timestamp lastModified = rs.getTimestamp("MODIFY_DATE");
			promoCodes.put(promoCode, lastModified);
		}

		rs.close();
		ps.close();

		return promoCodes;
	}
	
	/**
	 * Load Promotion for a given Promotion PK.
	 * @param conn
	 * @param promoId - The current value passed is Promotion CODE. Later when new data model
	 * for AI is implemented, value passed will be promotion ID.
	 * @return
	 * @throws SQLException
	 */
	public static PromotionI loadPromotion(Connection conn, String promoId) throws SQLException {
		Promotion promo = null;
		//TODO later the where clause need to be changed to point to ID column instead of Code. 
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.PROMOTION_NEW where code = ?");
		ps.setString(1, promoId);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			promo = constructPromotionFromResultSet(conn, rs);
		}

		rs.close();
		ps.close();

		return promo;
	}

	/**
	 * Load a redemption promotion
	 * @param conn
	 * @param redemptionCode
	 * @return redemption promoId - The current value returned is Promotion CODE. Later when new data model
	 * for AI is implemented, value returned will be promotion ID.
	 * @throws SQLException
	 */
	public static String getRedemptionPromotionId(Connection conn, String redemptionCode) throws SQLException {
		String promoId = null;
		//TODO later the select query need to be changed to return ID column instead of Code. 
		PreparedStatement ps = conn.prepareStatement("SELECT code FROM CUST.PROMOTION_NEW where UPPER(redemption_code) = UPPER(?) and " +
													"(expiration_date > (sysdate-1) or expiration_date is null)");
		ps.setString(1, redemptionCode);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			promoId = rs.getString("CODE");
		}

		rs.close();
		ps.close();

		return promoId;
	}
	
	/**
	 * Load a redemption promotion
	 * @param conn
	 * @param redemptionCode
	 * @return redemption promoId - The current value returned is Promotion CODE. Later when new data model
	 * for AI is implemented, value returned will be promotion ID.
	 * @throws SQLException
	 */
	public static Integer getRedemptions(Connection conn, String promoId) throws SQLException {
		
		//TODO later the select query need to be changed to return ID column instead of Code. 
		PreparedStatement ps = conn.prepareStatement("SELECT COUNT(sale_id) redemptions FROM CUST.PROMOTION_PARTICIPATION WHERE " +
													 "PROMOTION_ID = (SELECT ID FROM CUST.PROMOTION_NEW WHERE CODE = ?)");
		ps.setString(1, promoId);
		ResultSet rs = ps.executeQuery();
		int redemptions = 0;
		if (rs.next()) {
			redemptions = rs.getInt("redemptions");
		}

		rs.close();
		ps.close();

		return redemptions;
	}
	
	/**
	 * @param conn
	 * @param promoPK
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static Promotion constructPromotionFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		Promotion promo;
		String promoId = rs.getString("ID");
		String promoCode = rs.getString("CODE");
		String name = rs.getString("NAME");
		String description = rs.getString("DESCRIPTION");
		EnumPromotionType promoType = EnumPromotionType.getEnum(rs.getString("CAMPAIGN_CODE"));


		Timestamp lastModified = rs.getTimestamp("MODIFY_DATE");	
		promo = new Promotion(new PrimaryKey(promoId), promoType, promoCode, name, description, lastModified);
		EnumOfferType offerType = EnumOfferType.getEnum(rs.getString("OFFER_TYPE"));
		promo.setOfferType(offerType);		
		if("Y".equalsIgnoreCase(rs.getString("NEEDCUSTOMERLIST"))){
			//This is customer restricted Promotion. Create Audience Strategy.
			int rollingExpirationDays = rs.getInt("ROLLING_EXPIRATION_DAYS");
			//APPDEV-659 - Made isMaxUsagePerCustomer obsolete.
			AudienceStrategy aStrategy = new AudienceStrategy(false, rollingExpirationDays);
			promo.addStrategy(aStrategy);
		}

		String status = rs.getString("STATUS");
		boolean onHold = "Y".equalsIgnoreCase(rs.getString("ON_HOLD"));
		promo.addStrategy(new ActiveInactiveStrategy(FDStoreProperties.getPromoValidRTStatuses().contains(status) && !onHold));
		
		promo.addStrategy(new LimitedUseStrategy(rs.getInt("MAX_USAGE")));
		int redeemCnt = rs.getInt("REDEEM_CNT");
		if(!rs.wasNull() && redeemCnt > 0)
			promo.addStrategy(new MaxRedemptionStrategy(redeemCnt));
		
		if("X".equalsIgnoreCase(rs.getString("RULE_BASED"))) {
			promo.addStrategy(new RuleBasedPromotionStrategy());
		}

		promo.addStrategy(new DateRangeStrategy(rs.getTimestamp("START_DATE"), rs.getTimestamp("EXPIRATION_DATE")));
		/*if (custStrategies.get(promo.getPK()) != null) {
			promo.addStrategy((AssignedCustomerStrategy) custStrategies.get(promo.getPK()));
		}*/

		String redemptionCode = rs.getString("REDEMPTION_CODE");
		if (!rs.wasNull()) {
			promo.addStrategy(new RedemptionCodeStrategy(redemptionCode));
		}
		
		if(!"X".equals(rs.getString("DONOT_APPLY_FRAUD"))){
			promo.addStrategy(new FraudStrategy());
		}
		//TODO This needs to replaced by Customer strategy 	
		//decorateOrderTypestrategy(rs, promo);
		
		GeographyStrategy geoStrategy = loadGeographyStrategy(conn, promoId);
		if (geoStrategy != null) {
			promo.addStrategy(geoStrategy);
		}
		
		//TODO This needs to replaced by Cart strategy. 	
		decorateSampleStrategy(rs, promo);
		
		PromotionStrategyI profStrategy = loadProfiles(conn, promoId);
		if(profStrategy != null){
			promo.addStrategy(profStrategy);	
		}
		
		if(promoType.getName().equals(EnumPromotionType.LINE_ITEM.getName())){
			boolean favoritesOnly = "Y".equalsIgnoreCase(rs.getString("FAVORITES_ONLY"));
			if(favoritesOnly) {
				promo.addStrategy(new RecommendationStrategy());
			}
		}
		if("Y".equalsIgnoreCase(rs.getString("COMBINE_OFFER"))){				
			   promo.setCombineOffer(true);		
		}		
		//Add Customer Strategy
		PromotionStrategyI custStrategy = loadCustomerStrategy(conn, promoId);
		if (custStrategy != null) {
			promo.addStrategy(custStrategy);
		}
		

		
		PromotionStrategyI cartStrategyI = loadCartStrategy(conn,promoId);
		if(null != cartStrategyI){
			CartStrategy cartStrategy = (CartStrategy)cartStrategyI;
			cartStrategy.setNeedDryGoods(("X".equalsIgnoreCase(rs.getString("NEEDDRYGOODS"))|| "X".equalsIgnoreCase(rs.getString("NEEDDRYGOODS")))?true:false);
			cartStrategy.setMinSkuQuantity(rs.getInt("HASSKUQUANTITY"));
			promo.addStrategy(cartStrategy);				
		}
		
		PromotionApplicatorI applicator = loadApplicator(rs);	
		//Set the zone strategy if applicable.
		PromotionStrategyI dlvZoneStrategyI = loadDlvZoneStrategy(conn, promoId);
		if(applicator != null && null != dlvZoneStrategyI){
			DlvZoneStrategy dlvZoneStrategy = (DlvZoneStrategy)dlvZoneStrategyI;
			if((null !=dlvZoneStrategy.getDlvDates() && !dlvZoneStrategy.getDlvDates().isEmpty()) || null != dlvZoneStrategy.getDlvZoneId()){			
				applicator.setZoneStrategy(dlvZoneStrategy);
			}
		}		
				
		if(applicator != null && applicator instanceof LineItemDiscountApplicator){
			/*
			 * If the promotion is a Line item Discount Promotion, add the corresponding 
			 * DCPD line item strategy if present.
			 */
			DCPDLineItemStrategy strategy = loadDCPDData(conn, promoId);
			if(strategy != null) {
				((LineItemDiscountApplicator) applicator).addLineItemStrategy(strategy);
			}
		} 
		if (applicator != null) {
			promo.setApplicator(applicator);
		}
		

		return promo;
	}
	
	private static void decorateOrderTypestrategy(ResultSet rs, Promotion promo) throws SQLException {
		Set<EnumOrderType> orderTypes = new HashSet<EnumOrderType>();

		if ("X".equals(rs.getString("ORDERTYPE_HOME"))) {
			orderTypes.add(EnumOrderType.HOME);
		}

		if ("X".equals(rs.getString("ORDERTYPE_DEPOT"))) {
			orderTypes.add(EnumOrderType.DEPOT);
		}
		if ("X".equals(rs.getString("ORDERTYPE_PICKUP"))) {
			orderTypes.add(EnumOrderType.PICKUP);
		}

		if ("X".equals(rs.getString("ORDERTYPE_CORPORATE"))) {
			orderTypes.add(EnumOrderType.CORPORATE);
		}

		if (!orderTypes.isEmpty()) {
			promo.addStrategy(new OrderTypeStrategy(orderTypes));
		}
	}

	private static void decorateSampleStrategy(ResultSet rs, Promotion promo) throws SQLException {
		
		SampleStrategy s = new SampleStrategy();

		boolean valid = true;
		
		//TODO to be modified based on new rules
		/*
		boolean valid = false;

		boolean needDryGoods = "X".equals(rs.getString("NEEDDRYGOODS"));
		if (!rs.wasNull()) {
			valid = true;
			s.setNeedDryGoods(needDryGoods);
		}

		int orderCount = rs.getInt("ORDERCOUNT");
		if (!rs.wasNull()) {
			valid = true;
			s.setOrderCount(new Integer(orderCount));
		}

		String needItemsFrom = rs.getString("NEEDITEMSFROM");
		if (!rs.wasNull()) {
			valid = true;
			s.setNeedItemsFrom(decodeStrings(needItemsFrom));
		}

		String excludeSkuPrefix = rs.getString("EXCLUDESKUPREFIX");
		if (!rs.wasNull()) {
			valid = true;
			s.setExcludeSkuPrefix(excludeSkuPrefix);
		}

		String needBrands = rs.getString("NEEDBRANDS");
		if (!rs.wasNull()) {
			valid = true;
			s.setNeedBrands(decodeStrings(needBrands));
		}

		String excludeBrands = rs.getString("EXCLUDEBRANDS");
		if (!rs.wasNull()) {
			valid = true;
			s.setExcludeBrands(decodeStrings(excludeBrands));
		}
	*/
		if (valid) {
			promo.addStrategy(s);
		}
		
	}

	private static String[] decodeStrings(String string) {
		StringTokenizer st = new StringTokenizer(string, ",");
		String[] strings = new String[st.countTokens()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = st.nextToken().trim();
		}
		return strings;
	}

	private static PromotionApplicatorI loadApplicator(ResultSet rs) throws SQLException {

		//
		// header discount applicator
		//
		boolean wasNull = false;

		double minSubtotal = rs.getDouble("min_subtotal");
		wasNull |= rs.wasNull();

		double maxAmount = rs.getDouble("max_amount");
		wasNull |= rs.wasNull();



		
		if (!wasNull) {
			return new HeaderDiscountApplicator(new HeaderDiscountRule(minSubtotal, maxAmount));
		}

		
		//
		// percent-off applicator
		//
		wasNull = false;
		double percentOff = rs.getDouble("percent_off");		
		
		wasNull |= rs.wasNull();
		if (!wasNull && "HEADER".equals(rs.getString("CAMPAIGN_CODE"))) {
			
			return new PercentOffApplicator(minSubtotal, percentOff);
		}
		
		if("LINE_ITEM".equals(rs.getString("CAMPAIGN_CODE"))){
			LineItemDiscountApplicator applicator = new LineItemDiscountApplicator(minSubtotal, percentOff);
			boolean recItemsOnly = "X".equalsIgnoreCase(rs.getString("FAVORITES_ONLY"));
			applicator.setFavoritesOnly(recItemsOnly);
			if(recItemsOnly){
				applicator.addLineItemStrategy(new RecommendedLineItemStrategy());
			}
			int maxItemCount = rs.getInt("MAX_ITEM_COUNT");
			if(!rs.wasNull() && maxItemCount > 0){
				applicator.addLineItemStrategy(new MaxLineItemCountStrategy(maxItemCount));
			}
			boolean perishableOnly = "Y".equalsIgnoreCase(rs.getString("PERISHABLEONLY"));
			if(perishableOnly){
				applicator.addLineItemStrategy(new PerishableLineItemStrategy());
			}
			return applicator;
		}

		//
		// waive-charge applicator
		//
		wasNull = false;
		String waiveChargeType = rs.getString("waive_charge_type");
		wasNull |= rs.wasNull();
		if (!wasNull) {
			return new WaiveChargeApplicator(minSubtotal, EnumChargeType.getEnum(waiveChargeType));
		}

		//	
		// sample item applicator
		//
		wasNull = false;
		String categoryName = rs.getString("category_name");
		wasNull |= rs.wasNull();
		String productName = rs.getString("product_name");
		wasNull |= rs.wasNull();
		if (!wasNull) {
			return new SampleLineApplicator(new ProductReference(categoryName, productName), minSubtotal);
		}


		//
		// Extend delivery pass applicator
		//
		wasNull = false;
		int extendDPDays = rs.getInt("extend_dp_days");
		wasNull |= rs.wasNull();
		if (!wasNull) {
			return new ExtendDeliveryPassApplicator(extendDPDays, minSubtotal);
		}
		return null;
	}

	/** @return Map of promotionPK -> GeographyStrategy */
	private final static String getPromoGeographyData = "select pg.id, pg.promotion_id, pg.start_date from cust.promo_geography_new pg, " +
									"(SELECT ID FROM CUST.PROMOTION_NEW where status STATUSES and (expiration_date > (sysdate-7) " +
									"or expiration_date is null) and redemption_code is null) p where p.ID = pg.PROMOTION_ID";

	private final static String getModifiedOnlyPromoGeographyData = "select pg.id, pg.promotion_id, pg.start_date from cust.promo_geography_new pg, " +
	"(SELECT ID FROM CUST.PROMOTION_NEW where modify_date > ? ) p where p.ID = pg.PROMOTION_ID";
	
	protected static Map<PrimaryKey,GeographyStrategy> loadGeographyStrategies(Connection conn, Date lastModified) throws SQLException {
		PreparedStatement ps;
		
		if(lastModified != null){
			ps = conn.prepareStatement(getModifiedOnlyPromoGeographyData);	
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		}else {
			final String query = getPromoGeographyData.replace("STATUSES", getStatusReplacementString());
			ps = conn.prepareStatement(query);
		}
		
		ResultSet rs = ps.executeQuery();

		// promotionPK -> GeographyStrategy 
		Map<PrimaryKey,GeographyStrategy> strategies = new HashMap<PrimaryKey,GeographyStrategy>();

		// geographyPK -> PromotionGeography		
		Map<PrimaryKey,PromotionGeography> geographies = new HashMap<PrimaryKey,PromotionGeography>();

		while (rs.next()) {

			PrimaryKey promoPK = new PrimaryKey(rs.getString("PROMOTION_ID"));
			PrimaryKey geoPK = new PrimaryKey(rs.getString("ID"));
			PromotionGeography geo = new PromotionGeography(geoPK, rs.getDate("START_DATE"));

			GeographyStrategy strategy = strategies.get(promoPK);
			if (strategy == null) {
				strategy = new GeographyStrategy();
				strategies.put(promoPK, strategy);
			}
			strategy.addGeography(geo);

			geographies.put(geoPK, geo);
		}

		loadGeographyData(conn, geographies, null);

		rs.close();
		ps.close();

		return strategies;
	}
	
	protected static GeographyStrategy loadGeographyStrategy(Connection conn, String promoPK) throws SQLException {
		GeographyStrategy strategy = null;
		// geographyPK -> PromotionGeography		
		Map<PrimaryKey,PromotionGeography> geographies = new HashMap<PrimaryKey,PromotionGeography>();
		
		PreparedStatement ps = conn.prepareStatement("select id, start_date from cust.promo_geography_new where promotion_id = ?");
		ps.setString(1, promoPK);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			//PrimaryKey promoPK = new PrimaryKey(rs.getString("PROMOTION_ID"));
			PrimaryKey geoPK = new PrimaryKey(rs.getString("ID"));
			PromotionGeography geo = new PromotionGeography(geoPK, rs.getDate("START_DATE"));

			if (strategy == null) {
				strategy = new GeographyStrategy();
			}
			strategy.addGeography(geo);
			geographies.put(geoPK, geo);
		}

		loadGeographyData(conn, geographies, promoPK);

		rs.close();
		ps.close();

		return strategy;
	}

	private final static String GEO_CODE_ALL = "ALL";

	private static class GeoTuple {
		public final PrimaryKey geoPK;
		public final boolean add;
		public final boolean zip;
		public final String code;

		public GeoTuple(ResultSet rs) throws SQLException {
			geoPK = new PrimaryKey(rs.getString("geography_id"));
			add = "A".equals(rs.getString("sign"));
			zip = "Z".equals(rs.getString("type"));
			code = rs.getString("code");
		}
	}

	private static void loadGeographyData(Connection conn, Map<PrimaryKey,PromotionGeography> geographies, String promoPK) throws SQLException {
		PreparedStatement ps = null;
		if(promoPK == null){
			ps = conn.prepareStatement("select geography_id, type, code, sign from cust.promo_geography_data_new");	
		} else{
			ps = conn.prepareStatement("select geography_id, type, code, sign from cust.promo_geography_data_new " +
										"where GEOGRAPHY_ID IN (select ID from cust.promo_geography_new where " +
										"promotion_id = ?)");
			ps.setString(1, promoPK);
		}
		
		ResultSet rs = ps.executeQuery();

		List<GeoTuple> tuples = new ArrayList<GeoTuple>();

		while (rs.next()) {
			tuples.add(new GeoTuple(rs));
		}

		// deal with GEO_CODE_ALL entries
		for (ListIterator<GeoTuple> i = tuples.listIterator(); i.hasNext();) {
			GeoTuple t = i.next();
			if (GEO_CODE_ALL.equals(t.code)) {
				PromotionGeography geo = geographies.get(t.geoPK);
				if(geo != null){
					/*
					 * geo object can be null if there is no matching geoPk found in Map geographies. This can happen
					 * due to the fact from now on we don't load Promo_Geography records for expired promotions.
					 */
					if (t.zip) {
						geo.setAllowAllZipCodes(t.add);
					} else {
						geo.setAllowAllDepotCodes(t.add);
					}
				}
				i.remove();
			}
		}

		// deal with exclusions
		for (GeoTuple t : tuples) {
			PromotionGeography geo = geographies.get(t.geoPK);
			if(geo != null){
				/*
				 * geo object can be null if there is no matching geoPk found in Map geographies. This can happen
				 * due to the fact from now on we don't load Promo_Geography records for expired promotions.
				 */
				if (t.zip && t.add != geo.isAllowAllZipCodes()) {
					geo.excludeZipCode(t.code);
				} else if (t.add != geo.isAllowAllDepotCodes()) {
					geo.excludeDepotCode(t.code);
				}
			}
		}

		rs.close();
		ps.close();

	}

	
	private final static String GET_DCPD_DATA = "select pg.promotion_id, pg.content_type, pg.content_id, exclude from cust.PROMO_DCPD_DATA_NEW pg, " +
	"(SELECT ID FROM CUST.PROMOTION_NEW where status STATUSES and (expiration_date > (sysdate-7) " +
	"or expiration_date is null) and redemption_code is null) p where p.ID = pg.PROMOTION_ID";

	private final static String GET_MODIFIED_ONLY_DCPD_DATA = "select pg.id, pg.promotion_id, pg.content_type, pg.content_id, exclude from cust.PROMO_DCPD_DATA_NEW pg, " +
	"(SELECT ID  FROM CUST.PROMOTION_NEW where modify_date > ? ) p where p.ID = pg.PROMOTION_ID";
	
	protected static Map<PrimaryKey, DCPDLineItemStrategy> loadDCPDData(Connection conn, Date lastModified) throws SQLException {
		PreparedStatement ps;
		if(lastModified != null){
			ps = conn.prepareStatement(GET_MODIFIED_ONLY_DCPD_DATA);	
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		}else {
			final String query = GET_DCPD_DATA.replace("STATUSES", getStatusReplacementString());		
			ps = conn.prepareStatement(query);
		}
		//Map PromoPK --> DCPDLineItemStrategy
		Map<PrimaryKey, DCPDLineItemStrategy> dcpdDataMap = new HashMap<PrimaryKey, DCPDLineItemStrategy>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String promoId = rs.getString("PROMOTION_ID");
			PrimaryKey promoPK = new PrimaryKey(promoId);
			DCPDLineItemStrategy strategy = dcpdDataMap.get(promoPK);
			if( strategy == null){
				strategy = new DCPDLineItemStrategy();
			}
			String contentType =  rs.getString("CONTENT_TYPE");
			String contentId = rs.getString("CONTENT_ID");
			if(contentType.equalsIgnoreCase("Sku")){
				if(strategy.getSkus().isEmpty()) {
					//First time exclude is set.
					strategy.setExcludeSkus("X".equalsIgnoreCase(rs.getString("EXCLUDE")));
				}
				strategy.addSku(contentId);
			}
			else if(contentType.equalsIgnoreCase("Brand")){
				if(strategy.getBrands().isEmpty()) {
					//First time exclude is set.
					strategy.setExcludeBrands("X".equalsIgnoreCase(rs.getString("EXCLUDE")));
				}
				strategy.addBrand(contentId);
			} else {
				//Content Type is department,category or recipe.
				strategy.addContent(contentType, contentId);
			}
			dcpdDataMap.put(promoPK, strategy);				
		}
		rs.close();
		ps.close();
		
		return dcpdDataMap;
	}
	
	protected static DCPDLineItemStrategy loadDCPDData(Connection conn, String promoId) throws SQLException {
		DCPDLineItemStrategy strategy = null;
		PreparedStatement ps = conn.prepareStatement("select pg.content_type, pg.content_id, exclude from cust.PROMO_DCPD_DATA_NEW pg, " +
								"cust.promotion_new p where p.ID = pg.PROMOTION_ID and pg.promotion_id = ?");
		ps.setString(1, promoId);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			if( strategy == null){
				strategy = new DCPDLineItemStrategy();
			}
			String contentType =  rs.getString("CONTENT_TYPE");
			String contentId = rs.getString("CONTENT_ID");
			if(contentType.equalsIgnoreCase("Sku")){
				if(strategy.getSkus().isEmpty()) {
					//First time exclude is set.
					strategy.setExcludeSkus("X".equalsIgnoreCase(rs.getString("EXCLUDE")));
				}
				strategy.addSku(contentId);
			}
			else if(contentType.equalsIgnoreCase("Brand")){
				if(strategy.getBrands().isEmpty()) {
					//First time exclude is set.
					strategy.setExcludeBrands("X".equalsIgnoreCase(rs.getString("EXCLUDE")));
				}
				strategy.addBrand(contentId);
			} else {
				//Content Type is department,category or recipe.
				strategy.addContent(contentType, contentId);
			}
		}
		rs.close();
		ps.close();
		
		return strategy;
	}

	/** @return Map of promotionPK -> ProfileAttributeStrategy */
	private final static String GET_PROMO_PROFILE_ATTR = "select pa.promotion_id, pa.promo_attr_name, pa.attr_value, p.profile_operator from cust.promo_attr_new pa, " +
									"(SELECT ID, PROFILE_OPERATOR FROM CUST.PROMOTION_NEW where status STATUSES and (expiration_date > (sysdate-7) " +
									"or expiration_date is null) and redemption_code is null) p where p.ID = pa.PROMOTION_ID";

	private final static String GET_MODIFIED_ONLY_PROMO_PROFILE = "select pa.promotion_id, pa.promo_attr_name, pa.attr_value, p.profile_operator from cust.promo_attr_new pa, " +
	"(SELECT ID, PROFILE_OPERATOR FROM CUST.PROMOTION_NEW where modify_date > ? ) p where p.ID = pa.PROMOTION_ID";
	
	protected static Map<PrimaryKey,PromotionStrategyI> loadAllProfiles(Connection conn, Date lastModified) throws SQLException {
		PreparedStatement ps;
		if(lastModified != null){
			ps = conn.prepareStatement(GET_MODIFIED_ONLY_PROMO_PROFILE);	
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		}else {
			final String query = GET_PROMO_PROFILE_ATTR.replace("STATUSES", getStatusReplacementString());				
			ps = conn.prepareStatement(query);
		}
		//Map PromoPK --> CategoryDiscountApplicator
		Map<PrimaryKey,PromotionStrategyI> strategies = new HashMap<PrimaryKey,PromotionStrategyI>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String promoId = rs.getString("PROMOTION_ID");				
			PrimaryKey promoPK = new PrimaryKey(promoId);
			String attrName =  rs.getString("PROMO_ATTR_NAME");
			String attrValue = rs.getString("ATTR_VALUE");
			String operator = rs.getString("PROFILE_OPERATOR");
			if(operator == null || operator.length() == 0){
				//this promotion has a single profile attribute. Create a ProfileAttributeStrategy
				ProfileAttributeStrategy strategy = new ProfileAttributeStrategy();
				strategy.setAttributeName(attrName);
				strategy.setDesiredValue(attrValue);
				strategies.put(promoPK, strategy);
			}else {
				//It has more than one profile attribute. Go For compositestrategy in this case.
				CompositeStrategy compStrategy = (CompositeStrategy)strategies.get(promoPK);	
				if(compStrategy == null){
					compStrategy = new CompositeStrategy(operator.equals("AND") ? CompositeStrategy.AND : CompositeStrategy.OR);
				}
				ProfileAttributeStrategy profileStrategy = new ProfileAttributeStrategy();
				profileStrategy.setAttributeName(attrName);
				profileStrategy.setDesiredValue(attrValue);
				compStrategy.addStrategy(profileStrategy);
				strategies.put(promoPK, compStrategy);
			}
		}

		rs.close();
		ps.close();

		return strategies;
	}
	
	protected static PromotionStrategyI loadProfiles(Connection conn, String promoId) throws SQLException {
		PromotionStrategyI strategy = null;
		PreparedStatement ps = conn.prepareStatement("select pa.promo_attr_name, pa.attr_value, p.profile_operator from cust.promo_attr_new pa, cust.promotion_new p " +
														"where p.ID = pa.PROMOTION_ID and pa.promotion_id = ?");
		ps.setString(1, promoId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String attrName =  rs.getString("PROMO_ATTR_NAME");
			String attrValue = rs.getString("ATTR_VALUE");
			String operator = rs.getString("PROFILE_OPERATOR");
			if(operator == null || operator.length() == 0){
				//this promotion has a single profile attribute. Create a ProfileAttributeStrategy
				strategy = new ProfileAttributeStrategy();
				((ProfileAttributeStrategy)strategy).setAttributeName(attrName);
				((ProfileAttributeStrategy)strategy).setDesiredValue(attrValue);
			}else {
				//It has more than one profile attribute. Go For compositestrategy in this case.
				if(strategy == null){
					strategy= new CompositeStrategy(operator.equals("AND") ? CompositeStrategy.AND : CompositeStrategy.OR);
				}
				ProfileAttributeStrategy profileStrategy = new ProfileAttributeStrategy();
				profileStrategy.setAttributeName(attrName);
				profileStrategy.setDesiredValue(attrValue);
				((CompositeStrategy)strategy).addStrategy(profileStrategy);
			}
		}
		rs.close();
		ps.close();

		return strategy;
	}
	
	/** @return Map of promotionPK -> CustomerStrategy */
	private final static String GET_CUST_PROMO_STRATEGY = "select cs.promotion_id, cs.cohort, cs.dp_exp_end,cs.dp_exp_start, cs.dp_status, cs.order_range_end, " +
														   "cs.order_range_start,cs.payment_type,cs.prior_echeck_use, ordertype_home, ordertype_pickup, ordertype_corporate " +
														   "from cust.promo_cust_strategy cs, " +
														   "(SELECT ID FROM CUST.PROMOTION_NEW where status STATUSES and (expiration_date > (sysdate-7) " +
														   "or expiration_date is null) and redemption_code is null) p where p.ID = cs.PROMOTION_ID";

	private final static String GET_MODIFIED_CUST_PROMO_STRATEGY  =  "select cs.promotion_id, cs.cohort, cs.dp_exp_end,cs.dp_exp_start, cs.dp_status, cs.order_range_end, " +
																	 "cs.order_range_start,cs.payment_type,cs.prior_echeck_use, ordertype_home, ordertype_pickup, ordertype_corporate " +
																	 "from cust.promo_cust_strategy cs, " +
																	 "(SELECT ID FROM CUST.PROMOTION_NEW where modify_date > ? ) p where p.ID = cs.PROMOTION_ID";
	
	protected static Map<PrimaryKey, CustomerStrategy> loadCustomerStrategies(Connection conn, Date lastModified) throws SQLException {
		PreparedStatement ps;
		if(lastModified != null){
			ps = conn.prepareStatement(GET_MODIFIED_CUST_PROMO_STRATEGY);	
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		}else {
			final String query = GET_CUST_PROMO_STRATEGY.replace("STATUSES", getStatusReplacementString());		
			ps = conn.prepareStatement(query);
		}
		//Map PromoPK --> CategoryDiscountApplicator
		Map<PrimaryKey, CustomerStrategy> strategies = new HashMap<PrimaryKey, CustomerStrategy>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			CustomerStrategy strategy = new CustomerStrategy();
			String promoId = rs.getString("PROMOTION_ID");				
			PrimaryKey promoPK = new PrimaryKey(promoId);
			String cohorts = rs.getString("COHORT");
			if(cohorts != null && cohorts.length() > 0){
				strategy.setCohorts(cohorts);
			}
			String status = rs.getString("dp_status");
			if(status != null){
				strategy.setDPStatus(EnumDlvPassStatus.getEnum(status));
				Date dpStartDate = rs.getDate("dp_exp_start");
				if(dpStartDate != null){
					strategy.setDPStartDate(dpStartDate);
				}
				Date dpEndDate = rs.getDate("dp_exp_end");
				
				if(dpEndDate != null){
					strategy.setDPEndDate(dpEndDate);
				}
			}
			int orderStartRange = rs.getInt("order_range_start");
			strategy.setOrderStartRange(orderStartRange);
			int orderEndRange = rs.getInt("order_range_end");
			strategy.setOrderEndRange(orderEndRange);
			
			String paymentTypes = rs.getString("payment_type");
			if(paymentTypes != null && paymentTypes.length() > 0)
				strategy.setPaymentTypes(paymentTypes);
			int priorEcheckUse = rs.getInt("prior_echeck_use");
			strategy.setPriorEcheckUse(priorEcheckUse);
			Set<EnumOrderType> orderTypes = new HashSet<EnumOrderType>();

			if ("X".equals(NVL.apply(rs.getString("ORDERTYPE_HOME"),""))) {
				orderTypes.add(EnumOrderType.HOME);
			}
			if ("X".equals(NVL.apply(rs.getString("ORDERTYPE_PICKUP"),""))) {
				orderTypes.add(EnumOrderType.PICKUP);
			}

			if ("X".equals(NVL.apply(rs.getString("ORDERTYPE_CORPORATE"),""))) {
				orderTypes.add(EnumOrderType.CORPORATE);
			}			
			strategy.setAllowedOrderTypes(orderTypes);
			strategies.put(promoPK, strategy);
		}

		rs.close();
		ps.close();

		return strategies;
	}
	
	protected static PromotionStrategyI loadCustomerStrategy(Connection conn, String promoId) throws SQLException {
		CustomerStrategy strategy = null;
		PreparedStatement ps = conn.prepareStatement("select cs.promotion_id, cs.cohort, cs.dp_exp_end,cs.dp_exp_start, cs.dp_status, cs.order_range_end, " +
													 "cs.order_range_start,cs.payment_type,cs.prior_echeck_use, ordertype_home, ordertype_pickup, ordertype_corporate " +
													 "from cust.promo_cust_strategy cs, cust.promotion_new p " +
													 "where p.ID = cs.PROMOTION_ID and cs.promotion_id = ?");
		ps.setString(1, promoId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			strategy = new CustomerStrategy();
			String cohorts = rs.getString("COHORT");
			if(cohorts != null && cohorts.length() > 0){
				strategy.setCohorts(cohorts);
			}
			String status = rs.getString("dp_status");
			if(status != null){
				strategy.setDPStatus(EnumDlvPassStatus.getEnum(status));
				Date dpStartDate = rs.getDate("dp_exp_start");
				if(dpStartDate != null){
					strategy.setDPStartDate(dpStartDate);
				}
				Date dpEndDate = rs.getDate("dp_exp_end");
				
				if(dpEndDate != null){
					strategy.setDPEndDate(dpEndDate);
				}
			}
			int orderStartRange = rs.getInt("order_range_start");
			strategy.setOrderStartRange(orderStartRange);
			int orderEndRange = rs.getInt("order_range_end");
			strategy.setOrderEndRange(orderEndRange);
			
			String paymentTypes = rs.getString("payment_type");
			if(paymentTypes != null && paymentTypes.length() > 0)
				strategy.setPaymentTypes(paymentTypes);
			
			int priorEcheckUse = rs.getInt("prior_echeck_use");
			strategy.setPriorEcheckUse(priorEcheckUse);
			Set<EnumOrderType> orderTypes = new HashSet<EnumOrderType>();

			if ("X".equals(rs.getString("ORDERTYPE_HOME"))) {
				orderTypes.add(EnumOrderType.HOME);
			}

			/*if ("X".equals(rs.getString("ORDERTYPE_DEPOT"))) {
				orderTypes.add(EnumOrderType.DEPOT);
			}*/
			
			if ("X".equals(rs.getString("ORDERTYPE_PICKUP"))) {
				orderTypes.add(EnumOrderType.PICKUP);
			}

			if ("X".equals(rs.getString("ORDERTYPE_CORPORATE"))) {
				orderTypes.add(EnumOrderType.CORPORATE);
			}			
			
			strategy.setAllowedOrderTypes(orderTypes);			
		}

		rs.close();
		ps.close();

		return strategy;
	}
	
	
	/**
	 * Query to retreive all the Audience-Based promotions for a given customer.
	 * TODO Replace the Promotion Code with the Promotion ID when new AI database model
	 * is implemented.
	 */
	private final static String getABPromotionsForCustomer = "select p.code, pc.usage_cnt, pc.expiration_date "
		+ "from cust.promotion_new p, cust.promo_customer pc "
		+ "where p.id=pc.promotion_id and (p.expiration_date > (sysdate-7) or p.expiration_date is null) and pc.customer_id = ?";
	
	/** @return Map of promotionPK -> AssignedCustomerStrategy */
	public static Map<String,AssignedCustomerParam> loadAssignedCustomerParams(Connection conn, String erpCustomerId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(getABPromotionsForCustomer);
		ps.setString(1, erpCustomerId);
		ResultSet rs = ps.executeQuery();
		// promotionPK -> FDPromoAudience 
		Map<String,AssignedCustomerParam> audiencePromoDtls = new HashMap<String,AssignedCustomerParam>();

		while (rs.next()) {
			String promoId = rs.getString("CODE");
			java.util.Date expirationDate = rs.getDate("EXPIRATION_DATE");
			int usageCnt = rs.getInt("USAGE_CNT");
			AssignedCustomerParam assignedCustParam = new AssignedCustomerParam(new Integer(usageCnt), expirationDate);
			audiencePromoDtls.put(promoId, assignedCustParam);
		}

		rs.close();
		ps.close();
		return audiencePromoDtls;
	}
	
	private final static String GET_ALL_ACTIVE_PROMO_VARIANTS = "select vp.VARIANT_ID, vp.PROMO_CODE, vp.PROMO_PRIORITY, v.FEATURE, vp.VARIANT_PRIORITY from cust.PROMO_VARIANTS vp, " +
			"cust.SS_VARIANTS v, cust.PROMOTION_NEW p where p.CODE = vp.PROMO_CODE and v.ID = vp.VARIANT_ID and p.status STATUSES and (p.expiration_date > (sysdate-7) " +
			"or p.expiration_date is null) and p.FAVORITES_ONLY='X'";
	
	public static List<PromoVariantModel> loadAllActivePromoVariants(Connection conn, List<EnumSiteFeature> smartSavingFeatures) throws SQLException {
		StringBuffer preparedStmtQry = new StringBuffer( GET_ALL_ACTIVE_PROMO_VARIANTS.replace("STATUSES", getStatusReplacementString()) ); 
		StringBuffer buffer = new StringBuffer();
		if(smartSavingFeatures != null && smartSavingFeatures.size() > 0) {
			buffer.append(" AND ").append("V.FEATURE IN ").append("(");
			for(Iterator<EnumSiteFeature> it = smartSavingFeatures.iterator(); it.hasNext();){
				EnumSiteFeature siteFeature = it.next();
				buffer.append("\'").append(siteFeature.getName()).append("\'");
				if (it.hasNext()){
					buffer.append(",");
				}else{
					buffer.append(")");
				}
			}
		}
		
		if(buffer.length() > 0){
			preparedStmtQry.append(buffer);
		}
		preparedStmtQry.append(" order by vp.VARIANT_ID desc");
		PreparedStatement ps = conn.prepareStatement(preparedStmtQry.toString());
		ResultSet rs = ps.executeQuery();
		List<PromoVariantModel> promoVariants =  constructPromoVariants(rs);
		rs.close();
		ps.close();
		return promoVariants;
	}
	
	private static List<PromoVariantModel> constructPromoVariants(ResultSet rs) throws SQLException {
		List<PromoVariantModel> promoVariants = new ArrayList<PromoVariantModel>();
		while(rs.next()){
			String variantId = rs.getString("VARIANT_ID");
			String promoCode = rs.getString("PROMO_CODE");
			int priority = rs.getInt("PROMO_PRIORITY");
			String featureId = rs.getString("FEATURE");
			int featurePriority = rs.getInt("VARIANT_PRIORITY");
			PromoVariantModel promoVariant = new PromoVariantModelImpl(variantId, promoCode, priority, EnumSiteFeature.getEnum(featureId),featurePriority);
			promoVariants.add(promoVariant);
		}
		return promoVariants;
	}
	
	private final static String GET_ALL_PROMOTION_IDS = "SELECT ID FROM CUST.PROMOTION_NEW where status STATUSES and (expiration_date > (sysdate-7) " +
	"or expiration_date is null) and redemption_code is null ";
    private final static String GET_ALL_PROMOTION_IDS_BY_MODIFY_DATE = "SELECT ID FROM CUST.PROMOTION_NEW where modify_date > ? ";
    
	protected static Map<String, DlvZoneStrategy> loadDlvZoneStrategies(Connection conn, Date lastModified) throws SQLException {
		Map<String, DlvZoneStrategy> dlvZoneStrategies = new HashMap<String, DlvZoneStrategy>();
		PreparedStatement ps;
		if(lastModified != null){
			ps = conn.prepareStatement(GET_ALL_PROMOTION_IDS_BY_MODIFY_DATE);	
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		}else {
			final String query = GET_ALL_PROMOTION_IDS.replace("STATUSES", getStatusReplacementString());	
			ps = conn.prepareStatement(query);
		}
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String promoPK = rs.getString("ID");
			DlvZoneStrategy dlvZoneStrategy = new DlvZoneStrategy();
			loadDlvDates(conn, promoPK, dlvZoneStrategy);
			loadDlvZones(conn, promoPK, dlvZoneStrategy);
			dlvZoneStrategies.put(promoPK, dlvZoneStrategy);
		}
		rs.close();
		ps.close();
		return dlvZoneStrategies;
	}
	
	private final static String GET_PROMO_DLV_DATES = " select * from CUST.PROMO_DELIVERY_DATES where PROMOTION_ID = ?";
	private final static String GET_PROMO_DLV_ZONES = " select * from CUST.PROMO_DLV_ZONE_STRATEGY where PROMOTION_ID = ?";
	private final static String GET_PROMO_DLV_TIMESLOTS = " select * from CUST.PROMO_DLV_TIMESLOT where PROMO_DLV_ZONE_ID = ?";
	
	protected static DlvZoneStrategy loadDlvZoneStrategy(Connection conn, String promoPK) throws SQLException {
		DlvZoneStrategy dlvZoneStrategy = new DlvZoneStrategy();
		loadDlvDates(conn, promoPK, dlvZoneStrategy);
		loadDlvZones(conn, promoPK, dlvZoneStrategy);
		return dlvZoneStrategy;
	}

	private static void loadDlvZones(Connection conn, String promoPK,
			DlvZoneStrategy dlvZoneStrategy)
			throws SQLException {
		Map<Integer,List<PromotionDlvTimeSlot>> dlvTimeSlots = new HashMap<Integer,List<PromotionDlvTimeSlot>>();
		PreparedStatement ps;
		ResultSet rs;
		ps = conn.prepareStatement(GET_PROMO_DLV_ZONES);
		ps.setString(1, promoPK);
		rs = ps.executeQuery();
		if(rs.next()) {
			String dlvZoneId =rs.getString("id");
			dlvZoneStrategy.setDlvZoneId(dlvZoneId);
			dlvZoneStrategy.setDlvDays(rs.getString("DLV_DAYS"));
			Array array = rs.getArray(4);
			String[] zoneCodes = (String[])array.getArray();
			dlvZoneStrategy.setDlvZones(Arrays.asList(zoneCodes));;			
		}
		rs.close();
		ps.close();
		if(null !=dlvZoneStrategy.getDlvZoneId() && !"".equals(dlvZoneStrategy.getDlvZoneId().trim())){
			ps = conn.prepareStatement(GET_PROMO_DLV_TIMESLOTS);
			ps.setString(1, dlvZoneStrategy.getDlvZoneId());
			rs = ps.executeQuery();
			while (rs.next()) {
				Integer dayId = rs.getInt("DAY_ID");
				PromotionDlvTimeSlot dlvTimeSlot = new PromotionDlvTimeSlot(dayId,rs.getString("START_TIME"),rs.getString("END_TIME"));
				List<PromotionDlvTimeSlot> dlvTimeSlotList = dlvTimeSlots.get(dayId);
				if(null == dlvTimeSlotList){
					dlvTimeSlotList = new ArrayList<PromotionDlvTimeSlot>();
				}
				dlvTimeSlotList.add(dlvTimeSlot);
				dlvTimeSlots.put(dayId, dlvTimeSlotList);			
			}
			rs.close();
			ps.close();
		}
		dlvZoneStrategy.setDlvTimeSlots(dlvTimeSlots);
	}

	private static void loadDlvDates(Connection conn, String promoPK,
			DlvZoneStrategy dlvZoneStrategy) throws SQLException {
		List<PromotionDlvDate> dlvDates = new ArrayList<PromotionDlvDate>();
		PreparedStatement ps = conn.prepareStatement(GET_PROMO_DLV_DATES);
		ps.setString(1, promoPK);
		ResultSet rs = ps.executeQuery();	
		while (rs.next()) {
			PrimaryKey pk  = new PrimaryKey(rs.getString("ID"));
			Date startDate = rs.getDate("START_DATE");
			Date endDate = rs.getDate("END_DATE");
			PromotionDlvDate dlvDate = new PromotionDlvDate(pk,startDate,endDate);
			dlvDates.add(dlvDate);
		}
		rs.close();
		ps.close();
		dlvZoneStrategy.setDlvDates(dlvDates);
	}
	private static CartStrategy loadCartStrategy(Connection conn, String promoPK) throws SQLException{
		CartStrategy cartStrategy = new CartStrategy();
		PreparedStatement ps = conn.prepareStatement("select id, promotion_id, content_type, content_id from cust.promo_cart_strategy pcs where pcs.promotion_id = ? ");
		ps.setString(1, promoPK);
		ResultSet rs = ps.executeQuery();
	    Set<String> set = null;//List list =new ArrayList<FDPromoContentModel>();
	    Map<EnumDCPDContentType, Set<String>> dcpdData = new HashMap<EnumDCPDContentType, Set<String>>();
		
		while (rs.next()) {
			EnumDCPDContentType contentType = EnumDCPDContentType.getEnum(rs.getString("content_type"));
			set = dcpdData.get(EnumDCPDContentType.getEnum(rs.getString("content_type")));
			if(null == set){
				set = new HashSet<String>();
			}
			/*FDPromoContentModel contentModel = new FDPromoContentModel();
			contentModel.setId(rs.getString("id"));
			contentModel.setContentType(EnumDCPDContentType.getEnum(rs.getString("content_type")));			
			contentModel.setContentId(rs.getString("content_id"));
			contentModel.setPromotionId(promoPK);*/
			if(EnumDCPDContentType.DEPARTMENT.equals(contentType) || EnumDCPDContentType.CATEGORY.equals(contentType)){
				cartStrategy.addContent(contentType.getName(), rs.getString("content_id"));
			}
			set.add(rs.getString("content_id"));
			dcpdData.put(EnumDCPDContentType.getEnum(rs.getString("content_type")),set);
		}
		cartStrategy.setDcpdData(dcpdData);		
		
		return cartStrategy;
	}
	
	private final static String GET_CART_STRATEGY_DATA = "select pcs.id, pcs.promotion_id, pcs.content_type, pcs.content_id from cust.PROMO_CART_STRATEGY pcs, " +
	"(SELECT ID FROM CUST.PROMOTION_NEW where status STATUSES and (expiration_date > (sysdate-7) " +
	"or expiration_date is null) and redemption_code is null) p where p.ID = pcs.PROMOTION_ID";

	private final static String GET_MODIFIED_ONLY_CART_STRATEGY_DATA = "select pcs.id, pcs.promotion_id, pcs.content_type, pcs.content_id from cust.PROMO_CART_STRATEGY pcs, " +
	"(SELECT ID  FROM CUST.PROMOTION_NEW where modify_date > ? ) p where p.ID = pcs.PROMOTION_ID";
	
	private static Map<PrimaryKey, CartStrategy> loadCartStrategies(Connection conn, Date lastModified) throws SQLException{
//		CartStrategy cartStrategy = new CartStrategy();
		
		PreparedStatement ps = null;
		if(lastModified != null){
			ps = conn.prepareStatement(GET_MODIFIED_ONLY_CART_STRATEGY_DATA);
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
		}else {
			final String query = GET_CART_STRATEGY_DATA.replace("STATUSES", getStatusReplacementString());		
			ps = conn.prepareStatement(query);	
		}
		ResultSet rs = ps.executeQuery();
	    Set<String> set = null;//List list =new ArrayList<FDPromoContentModel>();
	    Map<PrimaryKey, CartStrategy> cartStrategyDataMap = new HashMap<PrimaryKey, CartStrategy>();
		
		while (rs.next()) {
			String promoId = rs.getString("PROMOTION_ID");
			PrimaryKey promoPK = new PrimaryKey(promoId);
			EnumDCPDContentType contentType =EnumDCPDContentType.getEnum(rs.getString("content_type"));
			CartStrategy strategy = cartStrategyDataMap.get(promoPK);
			if( strategy == null){
				strategy = new CartStrategy();
			}
			set = strategy.getDcpdData().get(EnumDCPDContentType.getEnum(rs.getString("content_type")));
			if(null == set){
				set = new HashSet<String>();
			}
			/*FDPromoContentModel contentModel = new FDPromoContentModel();
			contentModel.setId(rs.getString("id"));
			contentModel.setContentType(EnumDCPDContentType.getEnum(rs.getString("content_type")));			
			contentModel.setContentId(rs.getString("content_id"));
			contentModel.setPromotionId(promoPK);*/
			if(EnumDCPDContentType.DEPARTMENT.equals(contentType) || EnumDCPDContentType.CATEGORY.equals(contentType)){
				strategy.addContent(contentType.getName(), rs.getString("content_id"));
			}
			set.add(rs.getString("content_id"));
			strategy.getDcpdData().put(EnumDCPDContentType.getEnum(rs.getString("content_type")),set);
			cartStrategyDataMap.put(promoPK, strategy);
		}	
		
		return cartStrategyDataMap;
	}
	
}
