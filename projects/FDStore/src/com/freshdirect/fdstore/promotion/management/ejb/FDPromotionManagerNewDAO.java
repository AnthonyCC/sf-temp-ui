package com.freshdirect.fdstore.promotion.management.ejb;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvDateModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoPaymentStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDPromotionManagerNewDAO {
	private final static Logger LOGGER = LoggerFactory.getInstance(FDPromotionManagerNewDAO.class);


	public static FDPromotionNewModel getPromotion(Connection conn,
			String promoId) throws SQLException {

		PreparedStatement ps = conn
				.prepareStatement("SELECT * FROM CUST.PROMOTION_NEW WHERE CODE = ?");

		ps.setString(1, promoId);
		ResultSet rs = ps.executeQuery();

		FDPromotionNewModel promotion = null;

		if (rs.next()) {
			promotion = loadPromotionResult(rs);
			List<String> assignedCustomerUserIds = FDPromotionManagerDAO
					.loadAssignedCustomerUserIds(conn, promotion.getId());
			if (assignedCustomerUserIds != null
					&& assignedCustomerUserIds.size() > 0) {
				promotion.setAssignedCustomerUserIds(StringUtil
						.encodeString(assignedCustomerUserIds));
			} else {
				promotion.setAssignedCustomerUserIds("");
			}
		}

		rs.close();
		ps.close();
		if (promotion != null) {
			String id = promotion.getId();

			promotion.setDcpdData(loadAssignedGroups(conn, id));
			promotion.setZipRestrictions(loadZipRestrictions(conn, id));
			promotion.setCartStrategies(loadPromoCartStrategies(conn, id));
			promotion.setCustStrategies(loadPromoCustStrategies(conn, id));
//			promotion.setPaymentStrategies(loadPromoPaymentStrategies(conn,id));
			promotion.setDlvZoneStrategies(loadPromoDlvZoneStrategies(conn, id));
			promotion.setDlvDates(loadPromoDlvDates(conn, id));
			promotion.setAttributeList(loadAttributeList(conn, id));
		}
		return promotion;
	}

	public static List<FDPromotionNewModel> getPromotions(Connection conn) throws SQLException {

		PreparedStatement ps = conn
				.prepareStatement("SELECT * FROM CUST.PROMOTION_NEW");
		ResultSet rs = ps.executeQuery();
		List<FDPromotionNewModel> promoList = new ArrayList<FDPromotionNewModel>();
		while (rs.next()) {
			FDPromotionNewModel promotion = populate(conn, rs);
			promoList.add(promotion);
		}
		rs.close();
		ps.close();
		return promoList;
	}

	private static FDPromotionNewModel loadPromotionResult(ResultSet rs)
			throws SQLException {

		String id = rs.getString("ID");
		FDPromotionNewModel promotion = new FDPromotionNewModel(new PrimaryKey(id));

		promotion.setPromotionCode(rs.getString("CODE"));
		promotion.setDescription(rs.getString("DESCRIPTION"));
		promotion.setName(rs.getString("NAME"));
		promotion.setPromotionType(rs.getString("CAMPAIGN_CODE"));
		promotion.setMaxUsage(String.valueOf(rs.getInt("MAX_USAGE")));
		promotion.setStartDate(rs.getTimestamp("START_DATE"));
		if (promotion.getStartDate() != null) {
			promotion.setStartDateStr(DateUtil.formatDate(promotion.getStartDate()));
			promotion.setStartTimeStr(DateUtil.formatTime(promotion.getStartDate()));
		}
		final Timestamp expDate = rs.getTimestamp("EXPIRATION_DATE");
		promotion.setExpirationDate(expDate);
		if (promotion.getExpirationDate() != null) {
			promotion.setExpirationDateStr(DateUtil.formatDate(promotion.getExpirationDate()));
			promotion.setExpirationTimeStr(DateUtil.formatTime(promotion.getExpirationDate()));
		}
		String redemptionCode = rs.getString("REDEMPTION_CODE");
		if (!rs.wasNull()) {
			promotion.setRedemptionCode(redemptionCode);
		} else {
			promotion.setRedemptionCode("");
		}

		double minSubtotal = rs.getDouble("MIN_SUBTOTAL");
		if (!rs.wasNull()) {
			promotion.setMinSubtotal(String.valueOf(minSubtotal));
		} else {
			promotion.setMinSubtotal("");
		}
		double maxAmount = rs.getDouble("MAX_AMOUNT");
		if (!rs.wasNull()) {
			promotion.setMaxAmount(FormatterUtil.formatToTwoDecimal(maxAmount));
		} else {
			promotion.setMaxAmount("");
		}
		double percentOff = rs.getDouble("PERCENT_OFF") * 100;
		if (!rs.wasNull()) {
			promotion.setPercentOff(String.valueOf((int) percentOff));
		} else {
			promotion.setPercentOff("");
		}
		String waiveChargeType = rs.getString("WAIVE_CHARGE_TYPE");
		if (!rs.wasNull()) {
			promotion.setWaiveChargeType(waiveChargeType);
		} else {
			promotion.setWaiveChargeType("");
		}

		// Shift status PUBLISHED to EXPIRED automatically if promo is expired
		EnumPromotionStatus status = EnumPromotionStatus.getEnum(rs.getString("status"));
		if (EnumPromotionStatus.PUBLISHED.equals(status) &&
				expDate != null && (new java.util.Date()).after(expDate) ) {
			status = EnumPromotionStatus.EXPIRED;
		}
		promotion.setStatus(status);
		promotion.setProfileOperator(rs.getString("PROFILE_OPERATOR"));
		promotion.setAudienceDesc(rs.getString("AUDIENCE_DESC"));
		promotion.setOfferDesc(rs.getString("OFFER_DESC"));
		promotion.setTerms(rs.getString("TERMS"));
		promotion.setRedeemCount(rs.getInt("REDEEM_CNT"));
		promotion.setRollingExpirationDays(rs.getInt("ROLLING_EXPIRATION_DAYS"));
		promotion.setSkuQuantity(rs.getInt("HASSKUQUANTITY"));
		promotion.setPerishable("Y".equalsIgnoreCase(rs.getString("PERISHABLEONLY"))?true:false);
		promotion.setNeedDryGoods("X".equalsIgnoreCase(rs.getString("NEEDDRYGOODS"))?true:false);
		promotion.setNeedCustomerList("Y".equalsIgnoreCase(rs.getString("NEEDCUSTOMERLIST"))?true:false);
		promotion.setCreatedBy(rs.getString("CREATED_BY"));
		promotion.setCreatedDate(rs.getTimestamp("CREATE_DATE"));
		promotion.setModifiedBy(rs.getString("MODIFIED_BY"));
		promotion.setModifiedDate(rs.getTimestamp("MODIFY_DATE"));
		promotion.setLastPublishedDate(rs.getTimestamp("PUBLISH_DATE"));
		promotion.setApplyFraud((!"X".equalsIgnoreCase(rs.getString("DONOT_APPLY_FRAUD"))));
		promotion.setRuleBased("X".equalsIgnoreCase(rs.getString("RULE_BASED"))?true:false);
		promotion.setFavoritesOnly("Y".equalsIgnoreCase(rs.getString("FAVORITES_ONLY"))?true:false);
		promotion.setCombineOffer("Y".equalsIgnoreCase(rs.getString("COMBINE_OFFER"))?true:false);
		promotion.setCategoryName(rs.getString("CATEGORY_NAME"));
		promotion.setProductName(rs.getString("PRODUCT_NAME"));
		promotion.setExtendDpDays(rs.getInt("EXTEND_DP_DAYS"));
		promotion.setSubTotalExcludeSkus(rs.getString("EXCLUDE_SKU_SUBTOTAL"));
		promotion.setOfferType(rs.getString("OFFER_TYPE"));
		promotion.setMaxItemCount(rs.getInt("MAX_ITEM_COUNT"));
		promotion.setOnHold("Y".equalsIgnoreCase(rs.getString("ON_HOLD"))?true:false);
		promotion.setGeoRestrictionType(rs.getString("GEO_RESTRICTION_TYPE"));
		promotion.setPublishes(rs.getInt("PUBLISHES"));

		return promotion;
	}

	private static TreeMap<java.util.Date,FDPromoZipRestriction> loadZipRestrictions(Connection conn,
			String promotionId) throws SQLException {
		TreeMap<java.util.Date,FDPromoZipRestriction> map = new TreeMap<java.util.Date,FDPromoZipRestriction>();
		List<java.util.Date> dateList = loadDatesByZipRestriction(conn, promotionId);
		if (!dateList.isEmpty()) {
			for (Iterator<java.util.Date> i = dateList.iterator(); i.hasNext();) {
				FDPromoZipRestriction zipRestriction = new FDPromoZipRestriction();
				java.util.Date curDate = (java.util.Date) i.next();
				zipRestriction.setStartDate(curDate);
				zipRestriction.setType(loadZipRestrictionByDate(conn,
						promotionId, (Date) curDate));

				if (zipRestriction.getType().equals("SUSPENDED")) {
					zipRestriction.setType("EXCEPT");
					zipRestriction.setZipCodes("ALL");
				} else if (zipRestriction.getType().equals("ALL")) {
					zipRestriction.setType("ONLY");
					zipRestriction.setZipCodes("ALL");
				} else {
					zipRestriction.setZipCodes(NVL.apply(StringUtil
							.encodeString(loadZipCodesByDate(conn, promotionId,
									(Date) curDate, zipRestriction.getType())),
							""));
				}
				map.put(curDate, zipRestriction);
			}
		}

		return map;
	}

	private final static String zipRestrictionDates = "SELECT START_DATE FROM CUST.PROMO_GEOGRAPHY_NEW WHERE PROMOTION_ID = ? ORDER BY START_DATE DESC";

	protected static List<java.util.Date> loadDatesByZipRestriction(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(zipRestrictionDates);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();

		List<java.util.Date> list = new ArrayList<java.util.Date>();

		while (rs.next()) {
			list.add(rs.getDate("START_DATE"));
		}
		rs.close();
		ps.close();
		return list;
	}

	protected static String loadZipRestrictionByDate(Connection conn,
			String promotionId, Date date) throws SQLException {
		String query = "SELECT CODE, SIGN FROM "
				+ " CUST.PROMO_GEOGRAPHY_DATA_NEW PGD, CUST.PROMO_GEOGRAPHY_NEW PG "
				+ " WHERE PGD.GEOGRAPHY_ID = PG.ID AND PG.START_DATE = ? AND "
				+ " PG.PROMOTION_ID = ? AND TYPE = 'Z' ORDER BY CODE DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			String restriction = "";
			ps.setDate(1, date);
			ps.setString(2, promotionId);

			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("SIGN").equals("S")
						&& rs.getString("CODE").equals("ALL")) {
					restriction = "ONLY";
				} else if (rs.getString("SIGN").equals("A")
						&& rs.getString("CODE").equals("ALL")) {
					restriction = "EXCEPT";
				} else if (rs.getString("SIGN").equals("A")
						&& restriction.equals("ONLY")) {
					return "ONLY";
				} else if (rs.getString("SIGN").equals("A")) {
					return "ONLY";
				} else if (rs.getString("SIGN").equals("S")
						&& restriction.equals("EXCEPT")) {
					return "EXCEPT";
				}
			}
			if (restriction.equals("ONLY")) {
				return "SUSPENDED";
			} else { // if(restriction.equals("EXCEPT")){
				return "ALL";
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	protected static List<String> loadZipCodesByDate(Connection conn,
			String promotionId, Date date, String restriction)
			throws SQLException {
		List<String> list = new ArrayList<String>();
		if (restriction.equals("ALL")) {
			list.add("ALL");
			return list;
		} else {
			String query = "SELECT CODE, SIGN FROM "
					+ " CUST.PROMO_GEOGRAPHY_DATA_NEW PGD, CUST.PROMO_GEOGRAPHY_NEW PG "
					+ " WHERE PGD.GEOGRAPHY_ID = PG.ID AND PG.START_DATE = TO_DATE('"
					+ date + "', 'YYYY-MM-DD') AND " + " PG.PROMOTION_ID = '"
					+ promotionId + "' AND TYPE = 'Z' AND CODE !='ALL' ";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			int counter = 0;
			while (rs.next()) {
				list.add(rs.getString("CODE"));
				counter++;
			}
			if (counter == 0) {
				list.add("");
			}
			rs.close();
			ps.close();

			return list;
		}
	}

	private final static String LOAD_PROMO_ATTR = "select id, promotion_id, promo_attr_name, attr_value,  attr_index "
			+ "from cust.PROMO_ATTR_NEW pa "
			+ "where pa.promotion_id = ? order by attr_index";

	/** @return List of assigned profile attribute list */
	protected static List<FDPromotionAttributeParam> loadAttributeList(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_ATTR);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();

		List<FDPromotionAttributeParam> attrList = new ArrayList<FDPromotionAttributeParam>();

		FDPromotionAttributeParam tmpAttr = null;

		while (rs.next()) {
			tmpAttr = new FDPromotionAttributeParam();
			tmpAttr.setId(rs.getString("id"));
			tmpAttr.setAttributeName(rs.getString("promo_attr_name"));
			tmpAttr.setDesiredValue(rs.getString("attr_value"));
			tmpAttr.setAttributeIndex(rs.getString("attr_index"));

			attrList.add(tmpAttr);

		}

		rs.close();
		ps.close();

		return attrList;
	}

	public static PrimaryKey createPromotion(Connection conn, ModelI model)
			throws SQLException {
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PrimaryKey pk = createPromotionBasic(conn, promotion);
		promotion.setPK(pk);
		updateLeftOutAttributes(conn, promotion);
		String id = pk.getId();

		storeAssignedGroups(conn, id, promotion);
		storeAttributeList(conn, id, promotion.getAttributeList());
		storeCartStrategy(conn, id, promotion);
		storeCustomerStrategy(conn, id, promotion);
		storePromoDlvZoneStrategy(conn, id, promotion);
		storePromoDlvDates(conn,id, promotion);

		if (!promotion.getZipRestrictions().isEmpty()) {
			storeGeography(conn, id, promotion.getZipRestrictions());
		}
		return new PrimaryKey(id);
	}

	public static PrimaryKey createPromotionBasic(Connection conn,
			FDPromotionNewModel promotion) throws SQLException {
		String id = SequenceGenerator.getNextId(conn, "CUST");
		// String id =
		// promotion.getPK().getId()!=null?promotion.getPK().getId():SequenceGenerator.getNextId(conn,
		// "CUST");
		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO CUST.PROMOTION_NEW"
						+ " (ID, CODE, NAME, DESCRIPTION, MAX_USAGE, START_DATE, EXPIRATION_DATE, REDEMPTION_CODE,"
						+ " CAMPAIGN_CODE, MIN_SUBTOTAL, MAX_AMOUNT, PERCENT_OFF, WAIVE_CHARGE_TYPE, ROLLING_EXPIRATION_DAYS, STATUS, OFFER_DESC, " +
								"AUDIENCE_DESC, TERMS, REDEEM_CNT, HASSKUQUANTITY, " +
								"PERISHABLEONLY, NEEDDRYGOODS, NEEDCUSTOMERLIST, " +
								"RULE_BASED, FAVORITES_ONLY, COMBINE_OFFER, " +
								"CREATED_BY, CREATE_DATE, MODIFIED_BY, MODIFY_DATE, DONOT_APPLY_FRAUD, PUBLISHES)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?)");

		int i = 1;
		ps.setString(i++, id); // 1
		i = setupPreparedStatement(ps, promotion, i);

		ps.setString(i++, promotion.getCreatedBy()); // 27
		if (promotion.getCreatedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getCreatedDate().getTime())); // 28
		} else {
			ps.setNull(i++, Types.DATE);
		}
		ps.setString(i++, promotion.getModifiedBy()); // 29
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime())); // 30
		} else {
			ps.setNull(i++, Types.DATE);
		}
		
		if(!promotion.isApplyFraud()){
			ps.setString(i++, "X"); // 31
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		
		ps.setInt(i++, promotion.getPublishes()); // 32
		
		// Execute update
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created");
		}
		ps.close();
		return new PrimaryKey(id);
	}
	
	
	public static final String GET_RESTRICTED_CUSTOMERS="select customer_id from CUST.PROMO_CUSTOMER where promotion_id=? and customer_id in ('";
	

	private static String INSERT_PROMO_DCPD_DATA = "INSERT INTO cust.promo_dcpd_data_new"
			+ " (id, promotion_id, content_type, content_id, exclude)"
			+ " VALUES(?,?,?,?, ?)";

	private static void storeAssignedGroups(Connection conn, String id,
			FDPromotionNewModel promotion) throws SQLException {
		PreparedStatement ps = null;

		removeAssignedGroups(conn, id);
		try {
			ps = conn.prepareStatement(INSERT_PROMO_DCPD_DATA);
			prepareAssignedGroupSave(conn, ps, promotion.getDcpdData(), id);
			ps.executeBatch();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	private static void prepareAssignedGroupSave(Connection conn,
			PreparedStatement ps, List<FDPromoContentModel> dataList, String promotionId) throws SQLException {

		if (dataList != null && !dataList.isEmpty()) {
			for (FDPromoContentModel promoContentModel : dataList) {
				int index = 1;
				ps.setString(index++, SequenceGenerator.getNextId(conn,"CUST"));
				ps.setString(index++, promotionId);
				ps.setString(index++, promoContentModel.getContentType().getName());
				ps.setString(index++, promoContentModel.getContentId());
				ps.setString(index++, promoContentModel.isExcluded()?"Y":"N");
				ps.addBatch();
				
			}
		}
	}

	protected static void removeAssignedGroups(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE CUST.PROMO_DCPD_DATA_NEW WHERE PROMOTION_ID = ?");
		ps.setString(1, promotionId);
		ps.executeUpdate();
		ps.close();
	}

	private static String INSERT_PROMO_ATTR = "INSERT INTO cust.promo_attr_new"
			+ " (id, promotion_id, promo_attr_name, attr_value, attr_index)"
			+ " VALUES(?,?,?,?,?)";

	private static void storeAttributeList(Connection conn, String promotionId,
			List<FDPromotionAttributeParam> attrList) throws SQLException {
		PreparedStatement ps = null;
		removeAttributeList(conn, promotionId);
		try {
			ps = conn.prepareStatement(INSERT_PROMO_ATTR);
			if (attrList != null) {
				int indexAttr = 1;
				for (FDPromotionAttributeParam tmpParam : attrList) {
					int index = 1;
					ps.setString(index++, SequenceGenerator.getNextId(conn,
							"CUST"));
					ps.setString(index++, promotionId);
					ps.setString(index++, tmpParam.getAttributeName());
					ps.setString(index++, tmpParam.getDesiredValue());
					ps.setInt(index++, indexAttr++);
					ps.addBatch();
				}
			}
			ps.executeBatch();
		} finally {
			if (ps != null)
				ps.close();
		}
	}
	
	protected static void removeAttributeList(Connection conn, String promotionId) throws SQLException {
		PreparedStatement ps =	conn.prepareStatement("DELETE CUST.PROMO_ATTR_NEW WHERE PROMOTION_ID = ?");
		ps.setString(1, promotionId);
		ps.executeUpdate();		
		ps.close();
	}

	private static int setupPreparedStatement(PreparedStatement ps,
			FDPromotionNewModel promotion, int i) throws SQLException {
		ps.setString(i++, promotion.getPromotionCode()); // 2
		ps.setString(i++, promotion.getName()); // 3
		ps.setString(i++, promotion.getDescription()); // 4
		String maxUsage = !"".equals(promotion.getMaxUsage()) ? promotion
				.getMaxUsage() : "0";
		ps.setInt(i++, Integer.parseInt(maxUsage)); // 5
		if (promotion.getStartDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion.getStartDate()
					.getTime())); // 6
		} else {
			ps.setNull(i++, Types.DATE); // 6
		}
		if (promotion.getExpirationDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getExpirationDate().getTime())); // 7
		} else {
			ps.setNull(i++, Types.DATE); // 7
		}
		if (!"".equals(promotion.getRedemptionCode())) {
			ps.setString(i++, promotion.getRedemptionCode()); // 8
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		ps.setString(i++, null !=promotion.getPromotionType()?promotion.getPromotionType():" "); // 9
		if (!"".equals(promotion.getMinSubtotal()) && null !=promotion.getMinSubtotal()) {
			ps.setDouble(i++, Double.parseDouble(promotion.getMinSubtotal())); // 10
		} else {
			ps.setNull(i++, Types.DOUBLE);
		}
		if (!"".equals(promotion.getMaxAmount())&& null !=promotion.getMaxAmount()) {
			ps.setDouble(i++, Double.parseDouble(promotion.getMaxAmount())); // 11
		} else {
			ps.setNull(i++, Types.DOUBLE);
		}
		if (!"".equals(promotion.getPercentOff())&& null !=promotion.getPercentOff()) {
			ps.setDouble(i++,
					Double.parseDouble(promotion.getPercentOff()) / 100); // 12
		} else {
			ps.setNull(i++, Types.DOUBLE);
		}
		if (!"".equals(promotion.getWaiveChargeType())) {
			ps.setString(i++, promotion.getWaiveChargeType()); // 13
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}

		if (promotion.getRollingExpirationDays() != null) {
			ps.setInt(i++, promotion.getRollingExpirationDays().intValue()); // 14
		} else {
			ps.setNull(i++, Types.INTEGER);
		}

		if (promotion.getStatus() != null) {
			ps.setString(i++, promotion.getStatus().getName()); // 15
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}

		if (promotion.getOfferDesc() != null) {
			ps.setString(i++, promotion.getOfferDesc()); // 16
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		if (promotion.getAudienceDesc() != null) {
			ps.setString(i++, promotion.getAudienceDesc()); // 17
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}

		if (promotion.getTerms() != null) {
			ps.setString(i++, promotion.getTerms()); // 18
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}

		if (promotion.getRedeemCount() != null) {
			ps.setInt(i++, promotion.getRedeemCount().intValue()); // 19
		} else {
			ps.setNull(i++, Types.INTEGER);
		}

		if (promotion.getSkuQuantity() != null) {
			ps.setInt(i++, promotion.getSkuQuantity().intValue()); // 20
		} else {
			ps.setNull(i++, Types.INTEGER);
		}

		ps.setString(i++, promotion.isPerishable() ? "Y" : "N"); // 21
		ps.setString(i++, promotion.isNeedDryGoods() ? "X" : " "); // 22
		ps.setString(i++, promotion.isNeedCustomerList() ? "Y" : "N"); // 23
		
		ps.setString(i++, promotion.isRuleBased() ? "X" : " "); // 24
		ps.setString(i++, promotion.isFavoritesOnly() ? "Y" : "N"); // 25
		ps.setString(i++, promotion.isCombineOffer() ? "Y" : "N"); //26

		return i;
	}

	protected static void storeGeography(Connection conn, String promotionId,
			TreeMap<java.util.Date, FDPromoZipRestriction> zipMap) throws SQLException {
		removeGeographyData(conn, promotionId);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(GEOGRAPHY_INSERT);
			if(null != zipMap && !zipMap.isEmpty()){
				for (Iterator<Entry<java.util.Date, FDPromoZipRestriction>> i = zipMap.entrySet().iterator(); i.hasNext();) {
					Map.Entry<java.util.Date, FDPromoZipRestriction> e = (Entry<java.util.Date, FDPromoZipRestriction>) i.next();
					java.util.Date d = (java.util.Date) e.getKey();
					String id = SequenceGenerator.getNextId(conn, "CUST");
					ps.setString(1, id);
					ps.setString(2, promotionId);
					ps.setDate(3, new Date(d.getTime()));
					if (ps.executeUpdate() == 1) {
						storeGeographyData(conn, ((FDPromoZipRestriction) e
								.getValue()), id, promotionId);
					} else {
						throw new SQLException(
								"row not created to store Geography Information");
					}
				}
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	protected static void removeGeographyData(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn
					.prepareStatement("DELETE FROM CUST.PROMO_GEOGRAPHY_DATA_NEW WHERE GEOGRAPHY_ID IN (SELECT ID FROM CUST.PROMO_GEOGRAPHY_NEW WHERE PROMOTION_ID=?)");
			ps.setString(1, promotionId);
			ps.executeUpdate();
			ps = null;
			ps = conn
					.prepareStatement("DELETE FROM CUST.PROMO_GEOGRAPHY_NEW WHERE PROMOTION_ID = ?");
			ps.setString(1, promotionId);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	private final static String GEOGRAPHY_DATA_INSERT = "INSERT INTO CUST.PROMO_GEOGRAPHY_DATA_NEW (GEOGRAPHY_ID, TYPE, CODE, SIGN) "
			+ " VALUES ( ? ,?, ?, ?)";
	private final static String GEOGRAPHY_INSERT = "INSERT INTO CUST.PROMO_GEOGRAPHY_NEW (ID, PROMOTION_ID, START_DATE) "
			+ " VALUES (?,?,?)";

	private static void storeGeographyData(Connection conn,
			FDPromoZipRestriction zipRestriction, String geographyId,
			String promotionId) throws SQLException {
		String finalZipSign = "";
		String finalDepotSign = "";
		PreparedStatement ps = conn.prepareStatement(GEOGRAPHY_DATA_INSERT);
		try {
			String[] zipList = (NVL.apply(zipRestriction.getZipCodes(), ""))
					.split("\\,");
			for (int x = 0; x < zipList.length; x++) {
				String[] a = getZipRestrictionSign(zipRestriction.getType(),
						zipRestriction.getZipCodes(), zipList[x]);
				ps.setString(1, geographyId);
				ps.setString(2, "Z");
				ps.setString(3, a[0]);
				ps.setString(4, a[1]);
				if (ps.executeUpdate() != 1) {
					throw new SQLException(
							"row not created to store Geography Data");
				}
				finalZipSign = a[2];
				finalDepotSign = a[3];
			}
			if (finalZipSign != "") {
				ps.setString(1, geographyId);
				ps.setString(2, "Z");
				ps.setString(3, "ALL");
				ps.setString(4, finalZipSign);
				if (ps.executeUpdate() != 1) {
					throw new SQLException(
							"row not created to store Geography Data");
				}
			}
			if (finalDepotSign != "") {
				ps.setString(1, geographyId);
				ps.setString(2, "D");
				ps.setString(3, "ALL");
				ps.setString(4, finalDepotSign);
				if (ps.executeUpdate() != 1) {
					throw new SQLException(
							"row not created to store Geography Data");
				}
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	private static String[] getZipRestrictionSign(String type, String zips,
			String currentZip) {
		String[] a = new String[4];
		if ("ONLY".equals(type) && "ALL".equals(zips)) {
			a[0] = "ALL";
			a[1] = "A";
			a[2] = "";
			a[3] = "A";
		} else if ("EXCEPT".equals(type) && "ALL".equals(zips)) {
			a[0] = "ALL";
			a[1] = "S";
			a[2] = "";
			a[3] = "S";
		} else if (type.equals("EXCEPT")) {
			a[0] = currentZip;
			a[1] = "S";
			a[2] = "A";
			a[3] = "A";
		} else if (type.equals("ONLY")) {
			a[0] = currentZip;
			a[1] = "A";
			a[2] = "S";
			a[3] = "S";
		}

		return a;
	}

	private static String INSERT_PROMO_CUST_STRATEGY = "INSERT INTO cust.promo_cust_strategy"
			+ " (id, promotion_id, order_range_start, order_range_end, cohort, dp_status, dp_exp_start, dp_exp_end," +
					"ordertype_home, ordertype_pickup, ordertype_corporate, payment_type, prior_echeck_use)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static String UPDATE_PROMO_CUST_STRATEGY = "UPDATE cust.promo_cust_strategy"
			+ " SET order_range_start=?,order_range_end=?, cohort=?, dp_status=?,dp_exp_start=?,dp_exp_end=?,ordertype_home=?,ordertype_pickup=?,ordertype_corporate=?,payment_type=?,prior_echeck_use=? where promotion_id = ?";
	
	private static void storeCustomerStrategy(Connection conn, String promotionId,
			FDPromotionNewModel promotion) throws SQLException {
		// Not required to remove and insert, better to update.
		// removeCustomerStrategy(conn, promotionId);
		PreparedStatement ps = null;
		try {
			
			List<FDPromoCustStrategyModel> custReqList = promotion.getCustStrategies();
			if (null!= custReqList && !custReqList.isEmpty()) {
				FDPromoCustStrategyModel model =(FDPromoCustStrategyModel)custReqList.get(0);
				int index = 1;

				if (null !=model.getId()) {
					ps = conn.prepareStatement(UPDATE_PROMO_CUST_STRATEGY);
				} else {
					ps = conn.prepareStatement(INSERT_PROMO_CUST_STRATEGY);
					ps.setString(index++, SequenceGenerator.getNextId(conn,"CUST"));
					ps.setString(index++, promotionId);
				}
				
				/*ps.setTimestamp(index++, new java.sql.Timestamp(model
						.getOrderRangeStart().getTime()));
				ps.setTimestamp(index++, new java.sql.Timestamp(model
						.getOrderRangeEnd().getTime()));*/
				if (promotion != null && model.getOrderRangeStart() != null) {
					ps.setInt(index++, model.getOrderRangeStart().intValue());					
				} else {
					ps.setNull(index++, Types.INTEGER);										
				}
				if (promotion != null && model.getOrderRangeEnd() != null) {
					ps.setInt(index++, model.getOrderRangeEnd().intValue());					
				} else {
					ps.setNull(index++, Types.INTEGER);										
				}
				/*ps.setInt(index++, model.getOrderRangeStart());
				ps.setInt(index++, model.getOrderRangeEnd());*/
	
				//Changed the 'cohort' column from VArray Type to Varchar2 type.
				/*ArrayDescriptor desc = ArrayDescriptor.createDescriptor(
						"CUST.PROMO_COHORTS", conn);
				ARRAY newArray = new ARRAY(desc, conn, model.getCohorts());
				ps.setArray(index++, newArray);*/
				String[] cohorts = model.getCohorts();
				StringBuffer cohortsString = new StringBuffer();
				if (null != cohorts && cohorts.length > 0){
					for (int i = 0; i < cohorts.length-1; i++) {
						cohortsString.append(cohorts[i]);
						cohortsString.append(",");
					}
					cohortsString.append(cohorts[cohorts.length-1]);
				}
				ps.setString(index++, cohortsString.toString());
	
				ps.setString(index++, model.getDpStatus());
				if (model.getDpExpStart() != null)
					ps.setTimestamp(index++, new java.sql.Timestamp(model
						.getDpExpStart().getTime()));
				else
					ps.setNull(index++, Types.TIMESTAMP);

				if (model.getDpExpEnd() != null)
					ps.setTimestamp(index++, new java.sql.Timestamp(model.getDpExpEnd()
						.getTime()));
				else
					ps.setNull(index++, Types.TIMESTAMP);

				ps.setString(index++, model.isOrderTypeHome() ? "X" : " ");
				ps.setString(index++, model.isOrderTypePickup() ? "X" : " ");
				ps.setString(index++, model.isOrderTypeCorporate() ? "X" : " ");
				
				EnumCardType[] paymentTypes = model.getPaymentType();
				StringBuffer paymentType = new StringBuffer();
				if (null != paymentTypes && paymentTypes.length > 0){
					for (int i = 0; i < paymentTypes.length-1; i++) {
						EnumCardType enumCardType = paymentTypes[i];
						paymentType.append(enumCardType.getFdName());
						paymentType.append(",");						
					}
					paymentType.append(paymentTypes[paymentTypes.length-1]);						
				}
				ps.setString(index++,paymentType.toString());
				ps.setString(index++, model.getPriorEcheckUse());
				if (null !=model.getId()) {
					ps.setString(index++, promotionId);
				}
	
				ps.execute();
			}
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	private static String INSERT_PROMO_CART_STRATEGY = "INSERT INTO cust.promo_cart_strategy"
			+ " (id, promotion_id, content_type, content_id)"
			+ " VALUES(?,?,?,?)";

	private static void storeCartStrategy(Connection conn, String promotionId,
			FDPromotionNewModel promotion) throws SQLException {
		removeCartStrategy(conn, promotionId);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(INSERT_PROMO_CART_STRATEGY);
			List<FDPromoContentModel> cartReqList = promotion.getCartStrategies();
			
			prepareCartStrategy(conn, promotionId, ps, cartReqList);
			
			

			ps.executeBatch();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	private static void prepareCartStrategy(Connection conn, String promotionId,
			PreparedStatement ps, List<FDPromoContentModel> cartReqList)
			throws SQLException {
		if (cartReqList != null && !cartReqList.isEmpty()) {
			for (Iterator<FDPromoContentModel> iterator = cartReqList.iterator(); iterator.hasNext();) {
				FDPromoContentModel object = iterator.next();
				int index = 1;
				
				ps.setString(index++, SequenceGenerator.getNextId(conn,"CUST"));
				ps.setString(index++, promotionId);
				ps.setString(index++, object.getContentType().getName());
				ps.setString(index++, object.getContentId());
				
				ps.addBatch();
			}			
		}
	}

	protected static void removeCustomerStrategy(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE CUST.promo_cust_strategy WHERE PROMOTION_ID = ?");
		ps.setString(1, promotionId);
		ps.executeUpdate();
		ps.close();
	}

	protected static void removeCartStrategy(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE CUST.promo_cart_strategy WHERE PROMOTION_ID = ?");
		ps.setString(1, promotionId);
		ps.executeUpdate();
		ps.close();
	}

	public static void storePromotion(Connection conn,  ModelI model) throws SQLException {
		FDPromotionNewModel promotion = storePromotionBasic(conn, model);
		
		if(!promotion.getZipRestrictions().isEmpty()){
			storeGeography(conn, promotion.getPK().getId(), promotion.getZipRestrictions());
		}
		
		storeAssignedGroups(conn, promotion.getPK().getId(), promotion);
		storeAttributeList(conn, promotion.getPK().getId(), promotion.getAttributeList());
		storeCartStrategy(conn, promotion.getPK().getId(), promotion);
		storeCustomerStrategy(conn, promotion.getPK().getId(), promotion);
	}

	public static FDPromotionNewModel storePromotionBasic(Connection conn,
			ModelI model) throws SQLException {
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.PROMOTION_NEW"
				+ " SET"
				+ " CODE = ?, NAME = ?, DESCRIPTION = ?, MAX_USAGE = ?, START_DATE = ?, EXPIRATION_DATE = ?, REDEMPTION_CODE = ?,"
				+ " CAMPAIGN_CODE = ?, MIN_SUBTOTAL = ?, MAX_AMOUNT = ?, PERCENT_OFF = ?, WAIVE_CHARGE_TYPE = ?, ROLLING_EXPIRATION_DAYS = ?, STATUS = ?, OFFER_DESC = ?, AUDIENCE_DESC = ?, TERMS = ?,REDEEM_CNT = ?,HASSKUQUANTITY = ?, PERISHABLEONLY = ? ,NEEDDRYGOODS =?,NEEDCUSTOMERLIST =?, "
				+ " RULE_BASED = ?,FAVORITES_ONLY = ?, COMBINE_OFFER = ?, MODIFIED_BY =?, MODIFY_DATE =?, DONOT_APPLY_FRAUD=?"
				+ " WHERE ID = ?");
		int i = 1;
		i = setupPreparedStatement(ps, promotion, i);
		
		ps.setString(i++, promotion.getModifiedBy());
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}
		
		if(!promotion.isApplyFraud()){
			ps.setString(i++, "X");
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		
		ps.setString(i++, promotion.getPK().getId());
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created to update Promotion");
		}
		ps.close();
		return promotion;
	}
	
	private final static String LOAD_PROMO_DCPD_DATA = "select id, promotion_id, content_type, content_id, exclude"
		+ " from cust.promo_dcpd_data_new pcpd "
		+ "where pcpd.promotion_id = ?";

	/** @return List of assigned dcpd content data */
	protected static List<FDPromoContentModel> loadAssignedGroups(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_DCPD_DATA);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();
	    List<FDPromoContentModel> list = new ArrayList<FDPromoContentModel>();
		
		while (rs.next()) {
			FDPromoContentModel contentModel = new FDPromoContentModel();
			contentModel.setId(rs.getString("id"));
			contentModel.setContentType(EnumDCPDContentType.getEnum(rs.getString("content_type")));
			contentModel.setContentId(rs.getString("content_id"));
			contentModel.setExcluded("Y".equalsIgnoreCase(rs.getString("exclude"))?true:false);
			contentModel.setPromotionId(promotionId);
			list.add(contentModel);
		}
	
		rs.close();
		ps.close();
	    
		return list;
		
	}
	
	private final static String LOAD_PROMO_CART_STRATEGY = "select id, promotion_id, content_type, content_id"
		+ " from cust.promo_cart_strategy pcs "
		+ "where pcs.promotion_id = ?";

	protected static List<FDPromoContentModel> loadPromoCartStrategies(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_CART_STRATEGY);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();
	    List<FDPromoContentModel> list = new ArrayList<FDPromoContentModel>();
		
		while (rs.next()) {
			FDPromoContentModel contentModel = new FDPromoContentModel();
			contentModel.setId(rs.getString("id"));
			contentModel.setContentType(EnumDCPDContentType.getEnum(rs.getString("content_type")));
			contentModel.setContentId(rs.getString("content_id"));
			contentModel.setPromotionId(promotionId);
			list.add(contentModel);
		}
	
		rs.close();
		ps.close();
	    
		return list;
		
	}
	
	
//	private final static String LOAD_PROMO_CUST_STRATEGY = "select id, promotion_id, ORDER_RANGE_START, ORDER_RANGE_END, COHORT, DP_STATUS, DP_EXP_START, DP_EXP_END"
//		+ " from cust.promo_cust_strategy pcs "
//		+ "where pcs.promotion_id = ?";

	private final static String LOAD_PROMO_CUST_STRATEGY = "select * "
		+ " from cust.promo_cust_strategy pcs "
		+ "where pcs.promotion_id = ?";
	protected static List<FDPromoCustStrategyModel> loadPromoCustStrategies(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_CUST_STRATEGY);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();
	    List<FDPromoCustStrategyModel> list = new ArrayList<FDPromoCustStrategyModel>();
		
		while (rs.next()) {
			FDPromoCustStrategyModel promoCustStrategyModel = new FDPromoCustStrategyModel();
			promoCustStrategyModel.setId(rs.getString("id"));
			promoCustStrategyModel.setOrderRangeStart(rs.getInt("ORDER_RANGE_START"));
			promoCustStrategyModel.setOrderRangeEnd(rs.getInt("ORDER_RANGE_END"));
			
			//Changed the 'cohort' column from VArray Type to Varchar2 type.
			/*Array array = rs.getArray("COHORT");
			if(null != array){
				String[] cohorts = (String[])array.getArray();
				promoCustStrategyModel.setCohorts(cohorts);
			}*/
			String cohorts =rs.getString("COHORT");
			if(null != cohorts && !"".equals(cohorts.trim())){
				promoCustStrategyModel.setCohorts(cohorts.split(","));
			}
			promoCustStrategyModel.setDpStatus(rs.getString("DP_STATUS"));
			promoCustStrategyModel.setDpExpStart(rs.getDate("DP_EXP_START"));
			promoCustStrategyModel.setDpExpEnd(rs.getDate("DP_EXP_END"));
			promoCustStrategyModel.setOrderTypeHome("X".equalsIgnoreCase(rs.getString("ORDERTYPE_HOME"))?true:false);
			promoCustStrategyModel.setOrderTypePickup("X".equalsIgnoreCase(rs.getString("ORDERTYPE_PICKUP"))?true:false);
			promoCustStrategyModel.setOrderTypeCorporate("X".equalsIgnoreCase(rs.getString("ORDERTYPE_CORPORATE"))?true:false);
			promoCustStrategyModel.setExcludeSameDayDlv("Y".equalsIgnoreCase(rs.getString("EXCLUDE_SAMEDAY_DELIVERY"))?true:false);
			String paymentType = rs.getString("PAYMENT_TYPE");
			if(null !=paymentType){
				String[] paymentTypes = paymentType.split(",");
				EnumCardType[] cardType = new EnumCardType[paymentTypes.length];
				for(int i=0; i<paymentTypes.length;i++){
					cardType[i] = EnumCardType.getEnum(paymentTypes[i]);
				}
				promoCustStrategyModel.setPaymentType(cardType);
			}
			
			promoCustStrategyModel.setPriorEcheckUse(rs.getString("PRIOR_ECHECK_USE"));
			promoCustStrategyModel.setPromotionId(promotionId);
			list.add(promoCustStrategyModel);
		}
	
		rs.close();
		ps.close();
	    
		return list;		
	}
	
	
	private final static String LOAD_PROMO_PAYMENT_STRATEGY = "select id, promotion_id, ORDERTYPE_HOME, ORDERTYPE_PICKUP, ORDERTYPE_CORPORATE, PAYMENT_TYPE, PRIOR_ECHECK_USE"
		+ " from cust.PROMO_PAYMENT_STRATEGY pps "
		+ "where pps.promotion_id = ?";

	protected static List<FDPromoPaymentStrategyModel> loadPromoPaymentStrategies(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_PAYMENT_STRATEGY);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();
	    List<FDPromoPaymentStrategyModel> list = new ArrayList<FDPromoPaymentStrategyModel>();
		
		while (rs.next()) {
			FDPromoPaymentStrategyModel promoPaymentStrategyModel = new FDPromoPaymentStrategyModel();
			promoPaymentStrategyModel.setId(rs.getString("id"));
			promoPaymentStrategyModel.setOrderTypeHome("X".equalsIgnoreCase(rs.getString("ORDERTYPE_HOME"))?true:false);
			promoPaymentStrategyModel.setOrderTypePickup("X".equalsIgnoreCase(rs.getString("ORDERTYPE_PICKUP"))?true:false);
			promoPaymentStrategyModel.setOrderTypeCorporate("X".equalsIgnoreCase(rs.getString("ORDERTYPE_CORPORATE"))?true:false);
			String paymentType = rs.getString("PAYMENT_TYPE");
			if(null !=paymentType){
				String[] paymentTypes = paymentType.split(",");
				EnumCardType[] cardType = new EnumCardType[paymentTypes.length];
				for(int i=0; i<paymentTypes.length;i++){
					cardType[i] = EnumCardType.getEnum(paymentTypes[i]);
				}
				promoPaymentStrategyModel.setPaymentType(cardType);
			}
			
			promoPaymentStrategyModel.setPriorEcheckUse(rs.getString("PRIOR_ECHECK_USE"));
			promoPaymentStrategyModel.setPromotionId(promotionId);
			list.add(promoPaymentStrategyModel);
		}
	
		rs.close();
		ps.close();
	    
		return list;
		
	}

	public static void createPromotions(Connection conn, List<FDPromotionNewModel> promotions)
	throws SQLException {
		if(null != promotions && !promotions.isEmpty()){
			for (Iterator<FDPromotionNewModel> iterator = promotions.iterator(); iterator.hasNext();) {
				FDPromotionNewModel promotion = iterator.next();
				createPromotion(conn, promotion);
			}
		}
	}
	
	
	private static String INSERT_PROMO_DLV_ZONE_STRATEGY = "INSERT INTO cust.promo_dlv_zone_strategy"
		+ " (id, promotion_id,dlv_days,dlv_zone)"
		+ " VALUES(?,?,?,?)";
	
	private static String INSERT_PROMO_DLV_TIMESLOT = "INSERT INTO cust.PROMO_DLV_TIMESLOT"
		+ " (id, PROMO_DLV_ZONE_ID,DAY_ID,START_TIME,END_TIME)"
		+ " VALUES(?,?,?,?,?)";

	private static void storePromoDlvZoneStrategy(Connection conn,
			String promotionId, FDPromotionNewModel promotion)
			throws SQLException {
		removePromoDlvTimeSlots(conn,promotionId);
		removeDlvZoneStrategy(conn,promotionId);
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		int index = 1;
		try {
			List<FDPromoDlvZoneStrategyModel> dlvZoneStrategies = promotion.getDlvZoneStrategies();
			if(null!= dlvZoneStrategies && !dlvZoneStrategies.isEmpty()){
				ps = conn.prepareStatement(INSERT_PROMO_DLV_ZONE_STRATEGY);
				ps1= conn.prepareStatement(INSERT_PROMO_DLV_TIMESLOT);
				for (FDPromoDlvZoneStrategyModel model : dlvZoneStrategies) {
					String promoDlvZoneId = SequenceGenerator.getNextId(conn,"CUST");
					List<FDPromoDlvTimeSlotModel>timeSlotModel = model.getDlvTimeSlots();
					if(null != timeSlotModel && !timeSlotModel.isEmpty()){
						for (FDPromoDlvTimeSlotModel promoDlvTimeSlotModel : timeSlotModel) {
							ps1.setString(1, SequenceGenerator.getNextId(conn,"CUST"));
							ps1.setString(2, promoDlvZoneId);
							ps1.setInt(3, promoDlvTimeSlotModel.getDayId());
							ps1.setString(4, promoDlvTimeSlotModel.getDlvTimeStart());
							ps1.setString(5, promoDlvTimeSlotModel.getDlvTimeEnd());
							ps1.addBatch();
						}						
					}
					
					ps.setString(index++, promoDlvZoneId);
					ps.setString(index++, promotionId);
					ps.setString(index++, model.getDlvDays());	
					ArrayDescriptor desc = ArrayDescriptor.createDescriptor("CUST.PROMO_DLV_ZONECODES", conn);
					ARRAY newArray = new ARRAY(desc, conn, model.getDlvZones());
					ps.setArray(index++, newArray);
					ps.addBatch();
				}	
				ps.executeBatch();
				ps1.executeBatch();
			}
			
		} finally {
			if (ps != null)
				ps.close();
			if (ps1 != null)
				ps1.close();
		}
	}
	
	
	protected static void removeDlvZoneStrategy(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE CUST.promo_dlv_zone_strategy WHERE PROMOTION_ID = ?");
		ps.setString(1, promotionId);
		ps.executeUpdate();
		ps.close();
	}
	
	protected static void removePromoDlvTimeSlots(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE CUST.promo_dlv_timeslot WHERE PROMO_DLV_ZONE_ID in (select id from CUST.promo_dlv_zone_strategy WHERE PROMOTION_ID = ?)");
		ps.setString(1, promotionId);
		ps.executeUpdate();
		ps.close();
	}
	
	
	private static String INSERT_PROMO_DLV_DATES = "INSERT INTO cust.PROMO_DELIVERY_DATES"
		+ " (id, promotion_id,START_DATE,END_DATE)"
		+ " VALUES(?,?,?,?)";
	
	private static void storePromoDlvDates(Connection conn,
			String promotionId, FDPromotionNewModel promotion)
			throws SQLException {
		removeDlvDates(conn,promotionId);
		PreparedStatement ps = null;		
		int index = 1;
		try {
			List<FDPromoDlvDateModel> dlvDates = promotion.getDlvDates();
			if(null!= dlvDates && !dlvDates.isEmpty()){
				ps = conn.prepareStatement(INSERT_PROMO_DLV_DATES);
				
				for (FDPromoDlvDateModel model : dlvDates) {
					String promoDlvDateId = SequenceGenerator.getNextId(conn,"CUST");
					index = 1;
					ps.setString(index++, promoDlvDateId);
					ps.setString(index++, promotionId);
					ps.setTimestamp(index++, new java.sql.Timestamp(model.getDlvDateStart().getTime()));
					ps.setTimestamp(index++, new java.sql.Timestamp(model.getDlvDateEnd().getTime()));
					
					ps.addBatch();
				}	
				ps.executeBatch();
			}
			
		} finally {
			if (ps != null)
				ps.close();
			
		}
	}
	
	protected static void removeDlvDates(Connection conn,
			String promotionId) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE CUST.PROMO_DELIVERY_DATES WHERE PROMOTION_ID = ?");
		ps.setString(1, promotionId);
		ps.executeUpdate();
		ps.close();
	}

	
	private final static String LOAD_PROMO_DLV_DATES = "select id, promotion_id, START_DATE, END_DATE"
		+ " from cust.PROMO_DELIVERY_DATES pdd "
		+ "where pdd.promotion_id = ?";

	protected static List<FDPromoDlvDateModel> loadPromoDlvDates(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_DLV_DATES);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();
	    List<FDPromoDlvDateModel> list = new ArrayList<FDPromoDlvDateModel>();		
		while (rs.next()) {
			FDPromoDlvDateModel dlvDateModel = new FDPromoDlvDateModel();
			dlvDateModel.setId(rs.getString("id"));
			try {
				dlvDateModel.setDlvDateStart(null !=rs.getTimestamp("START_DATE")? DateUtil.parseMDY(DateUtil.formatDate(rs.getTimestamp("START_DATE"))):rs.getTimestamp("START_DATE"));
				dlvDateModel.setDlvDateEnd(null !=rs.getTimestamp("END_DATE")? DateUtil.parseMDY(DateUtil.formatDate(rs.getTimestamp("END_DATE"))):rs.getTimestamp("END_DATE"));
			} catch (ParseException e) {
				
			}
			dlvDateModel.setPromoId(promotionId);
			list.add(dlvDateModel);
		}	
		rs.close();
		ps.close();
	    
		return list;		
	}
	
	
	private final static String LOAD_PROMO_DLV_ZONE_STRATEGY = "select id, promotion_id, DLV_DAYS, DLV_ZONE"
		+ " from cust.PROMO_DLV_ZONE_STRATEGY plzs "
		+ "where plzs.promotion_id = ?";
	
	private final static String LOAD_PROMO_TIME_SLOTS = "select id, PROMO_DLV_ZONE_ID, DAY_ID, START_TIME, END_TIME"
		+ " from cust.PROMO_DLV_TIMESLOT pdt "
		+ "where pdt.PROMO_DLV_ZONE_ID = ?";

	protected static List<FDPromoDlvZoneStrategyModel> loadPromoDlvZoneStrategies(Connection conn, String promotionId)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_DLV_ZONE_STRATEGY);
		ps.setString(1, promotionId);
		ResultSet rs = ps.executeQuery();
	    List<FDPromoDlvZoneStrategyModel> list = new ArrayList<FDPromoDlvZoneStrategyModel>();		
		while (rs.next()) {
			FDPromoDlvZoneStrategyModel dlvZoneStrategyModel = new FDPromoDlvZoneStrategyModel();
			dlvZoneStrategyModel.setId(rs.getString("id"));
			dlvZoneStrategyModel.setPromotionId(promotionId);
			dlvZoneStrategyModel.setDlvDays(rs.getString("DLV_DAYS"));
			Array array = rs.getArray(4);
			String[] zoneCodes = (String[])array.getArray();
			dlvZoneStrategyModel.setDlvZones(zoneCodes);
			
			PreparedStatement ps1 = conn.prepareStatement(LOAD_PROMO_TIME_SLOTS);
			ps1.setString(1, dlvZoneStrategyModel.getId());
			ResultSet rs1 = ps1.executeQuery();
			List<FDPromoDlvTimeSlotModel> timeSlotList = new ArrayList<FDPromoDlvTimeSlotModel>();
			while(rs1.next()){
				FDPromoDlvTimeSlotModel timeSlotModel = new FDPromoDlvTimeSlotModel();
				timeSlotModel.setId(rs1.getString("id"));
				timeSlotModel.setPromoDlvZoneId(dlvZoneStrategyModel.getId());
				timeSlotModel.setDayId(rs1.getInt("DAY_ID"));
				timeSlotModel.setDlvTimeStart(rs1.getString("START_TIME"));
				timeSlotModel.setDlvTimeEnd(rs1.getString("END_TIME"));
				timeSlotList.add(timeSlotModel);
			}
			dlvZoneStrategyModel.setDlvTimeSlots(timeSlotList);		
			list.add(dlvZoneStrategyModel);
		}	
		rs.close();
		ps.close();
	    
		return list;		
	}
	
	private final static String LOAD_PROMO_CREATED_USERS = "select distinct(USER_ID)"
		+ " from cust.PROMO_CHANGE pc "
		+ "where pc.ACTION_TYPE = 'CREATE' order by USER_ID";
	
	public static List<String> getPromoCreatedUsers(Connection conn) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_CREATED_USERS);
		ResultSet rs = ps.executeQuery();
		List<String> userIds = new ArrayList<String>();
		while (rs.next()) {
			String userId = rs.getString("USER_ID");
			userIds.add(userId);
		}
		return userIds;
	}
	
	private final static String LOAD_PROMO_MODIFIED_USERS = "select distinct(USER_ID)"
		+ " from cust.PROMO_CHANGE pc "
		+ "where pc.ACTION_TYPE = 'MODIFY' order by USER_ID";	
	
		
	public static List<String> getPromoModifiedUsers(Connection conn) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(LOAD_PROMO_MODIFIED_USERS);
		ResultSet rs = ps.executeQuery();
		List<String> userIds = new ArrayList<String>();
		while (rs.next()) {
			String userId = rs.getString("USER_ID");
			userIds.add(userId);
		}
		return userIds;
	}
	
	
	private static String PROMOS_4_PUBLISH = "SELECT * FROM CUST.PROMOTION_NEW WHERE " +
			"STATUS IN ('"+EnumPromotionStatus.APPROVE.getName()+"', '"+EnumPromotionStatus.CANCELLING.getName()+"')";
	
	public static List<FDPromotionNewModel> getPublishablePromos(Connection conn) throws SQLException {
		List<FDPromotionNewModel> promos = new ArrayList<FDPromotionNewModel>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(PROMOS_4_PUBLISH);
			rs = ps.executeQuery();
			while (rs.next()) {
				final FDPromotionNewModel promotion = populate(conn, rs);
				promos.add(promotion);	
			}

			
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		
		return promos;
	}

	

	/**
	 * @param conn
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static FDPromotionNewModel populate(Connection conn, ResultSet rs)
			throws SQLException {
		FDPromotionNewModel promotion = loadPromotionResult(rs);
		
		if (promotion != null) {
			PrimaryKey pk = promotion.getPK();
			promotion.setZipRestrictions(loadZipRestrictions(conn, pk
					.getId()));
			promotion.setDcpdData(loadAssignedGroups(conn, pk.getId()));
			promotion.setCartStrategies(loadPromoCartStrategies(conn, pk.getId()));
			promotion.setCustStrategies(loadPromoCustStrategies(conn, pk.getId()));
//			promotion.setPaymentStrategies(loadPromoPaymentStrategies(conn, pk.getId()));
			promotion.setDlvZoneStrategies(loadPromoDlvZoneStrategies(conn,  pk.getId()));
			promotion.setDlvDates(loadPromoDlvDates(conn,  pk.getId()));
			promotion.setAttributeList(loadAttributeList(conn,  pk.getId()));

		}
		return promotion;
	}
	
	
	public static List<FDPromoChangeModel> getPromoAuditChanges(Connection conn, String promotionId) throws SQLException{
		List<FDPromoChangeModel> auditChanges= new ArrayList<FDPromoChangeModel>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT * FROM CUST.PROMO_CHANGE WHERE PROMOTION_ID=? ORDER BY ACTION_DATE DESC");
			ps.setString(1, promotionId);
			rs = ps.executeQuery();
			while (rs.next()) {
				FDPromoChangeModel promoChangeModel = new FDPromoChangeModel();
				String changeId = rs.getString("ID");
				promoChangeModel.setId(changeId);
				promoChangeModel.setPromotionId(promotionId);
				promoChangeModel.setActionType(EnumPromoChangeType.getEnum(rs.getString("ACTION_TYPE")));
				promoChangeModel.setActionDate(rs.getTimestamp("ACTION_DATE"));
				promoChangeModel.setUserId(rs.getString("USER_ID"));
				PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM CUST.PROMO_CHANGE_DETAIL WHERE CHANGE_ID=?");
				ps1.setString(1,promoChangeModel.getId());
				ResultSet rs1 =ps1.executeQuery();
				List<FDPromoChangeDetailModel> auditChangeDetails= new ArrayList<FDPromoChangeDetailModel>();
				while(rs1.next()){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setId(rs1.getString("ID"));
					changeDetailModel.setPromoChangeId(promoChangeModel.getId());
					changeDetailModel.setChangeFieldName(rs1.getString("CHANGE_FIELD"));
					changeDetailModel.setChangeSectionId(EnumPromotionSection.getEnum(rs1.getString("SECTION_ID")));
					changeDetailModel.setChangeFieldNewValue(rs1.getString("NEW_VALUE"));
					changeDetailModel.setChangeFieldOldValue(rs1.getString("OLD_VALUE"));
					auditChangeDetails.add(changeDetailModel);
				}
				promoChangeModel.setChangeDetails(auditChangeDetails);
				auditChanges.add(promoChangeModel);
			}

			
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		
		
		return auditChanges;
	}


	/**
	 * Deletes a promotion completely
	 * 
	 * @param conn
	 * @param pk
	 * @throws SQLException
	 */
	public static void deletePromotion(Connection conn, String pk) throws SQLException {
		// Remove dependent records first
		
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_DCPD_DATA_NEW WHERE PROMOTION_ID=?", pk);
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_ATTR_NEW WHERE PROMOTION_ID=?", pk);

		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_GEOGRAPHY_DATA_NEW " +
				"WHERE GEOGRAPHY_ID IN ( " +
				"  SELECT ID FROM CUST.PROMO_GEOGRAPHY_NEW WHERE PROMOTION_ID=? " +
				")", pk);
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_GEOGRAPHY_NEW WHERE PROMOTION_ID=?", pk);
		
		// -- Strategies ---
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_CART_STRATEGY WHERE PROMOTION_ID=?", pk);
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_CUST_STRATEGY WHERE PROMOTION_ID=?", pk);
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_DLV_ZONE_STRATEGY WHERE PROMOTION_ID=?", pk);
		
		executeDeleteOperation(conn, "DELETE CUST.PROMO_DLV_TIMESLOT WHERE PROMO_DLV_ZONE_ID in " +
				"(SELECT ID FROM CUST.PROMO_DLV_ZONE_STRATEGY WHERE PROMOTION_ID = ?)", pk);
		executeDeleteOperation(conn, "DELETE CUST.PROMO_DELIVERY_DATES WHERE PROMOTION_ID = ?", pk);

		executeDeleteOperation(conn, "DELETE FROM CUST.PROMOTION_PARTICIPATION WHERE PROMOTION_ID=?", pk);
		executeDeleteOperation(conn, "DELETE FROM CUST.PROMO_CUSTOMER WHERE PROMOTION_ID=?", pk);

		executeDeleteOperation(conn, "DELETE FROM CUST.PROMOTION_NEW WHERE ID=?", pk);
	}


	/**
	 * Executes a delete operation with just one argument
	 * 
	 * @param conn Connection
	 * @param sqlString
	 * @param var1
	 * @throws SQLException
	 */
	private static void executeDeleteOperation(Connection conn, String sqlString, String ... vars) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sqlString);
			for (int k=0; k<vars.length; k++) {
				ps.setString(k+1, vars[k]);
			}
			ps.execute();
		} catch (SQLException exc) {
			LOGGER.error("Failed to execute " + sqlString, exc);
			throw exc;
		} finally {
			if (ps != null)
				ps.close();
		}
	}
	
	

	public static boolean isPromotionCodeUsed(Connection conn, String promoCode) throws SQLException {
		return findPromotion(conn, promoCode) != null;
	}


	/**
	 * Looks for promotion by promotion code and returns its PK if exists
	 * 
	 * @param conn
	 * @param promoCode
	 * @return PK of promotion
	 * @throws SQLException
	 */
	public static String findPromotion(Connection conn, String promoCode) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn
					.prepareStatement("SELECT ID FROM CUST.PROMOTION_NEW WHERE CODE=?");
			ps.setString(1, promoCode);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("ID");
			}
		} finally {
			if (ps != null)
				ps.close();
		}

		return null;
	}


	/**
	 * Method to check whether promo name is used already
	 * 
	 * @param conn
	 * @param promoName
	 * @return
	 * @throws SQLException
	 */
	public static boolean isPromotionNameUsed(Connection conn, String promoName) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn
					.prepareStatement("SELECT COUNT(ID) AS CNT_NAME FROM CUST.PROMOTION_NEW WHERE NAME=?");
			ps.setString(1, promoName);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} finally {
			if (ps != null)
				ps.close();
		}

		return false;
	}
	

	/**
	 * Publish a promotion
	 * 
	 * Replica-side action
	 * 
	 * @param conn
	 * @param promo
	 * @return
	 * @throws SQLException
	 */
	public static boolean publishPromotion(Connection conn, FDPromotionNewModel promo) throws SQLException {
		if (promo == null) {
			LOGGER.error("Promotion is NULL");
			return false;
		}
		
		final String promoCode = promo.getPromotionCode();
		if (!EnumPromotionStatus.APPROVE.equals(promo.getStatus())) {
			LOGGER.error("Only approved promotion can be published. Promotion " + promoCode + " has wrong status " + promo.getStatus().getName());
			return false;
		}

		// change status to published
		promo.setStatus(EnumPromotionStatus.LIVE);
		promo.setLastPublishedDate(Calendar.getInstance().getTime());

		// Increment number of publishes counter
		promo.setPublishes(promo.getPublishes() + 1);
		
		// Delete old instance if exists
		String promoPK = findPromotion(conn, promoCode);
		if (promoPK != null) {
			// store promotion in its new form
			promo.setPK(new PrimaryKey(promoPK));
			LOGGER.debug("Update promotion " + promoCode);
			try {
				updatePromotion(conn, promo);
			} catch (Exception e) {
				LOGGER.error("Update promotion crashed", e);
				throw new SQLException(e);
			}
		} else {
			LOGGER.debug("Save promotion " + promoCode);
			try {
				createPromotion(conn, promo);
			} catch (Exception e) {
				LOGGER.error("Store promotion crashed", e);
				throw new SQLException(e);
			}
		}
		
		return true;
	}


	/**
	 * Cancels a promotion during publish process.
	 * Replica-side action
	 * 
	 * @param conn
	 * @param promo
	 * @return
	 * @throws SQLException
	 */
	public static boolean cancelPromotion(Connection conn, FDPromotionNewModel promo) throws SQLException {
		if (promo == null) {
			LOGGER.error("Promotion is NULL");
			return false;
		}
		
		if (!EnumPromotionStatus.CANCELLING.equals(promo.getStatus())) {
			LOGGER.error("Only promotion in status 'CANCELLING' can be cancelled. Promotion " + promo.getPromotionCode() + " has wrong status " + promo.getStatus().getName());
			return false;
		}
		
		if (isPromotionCodeUsed(conn, promo.getPromotionCode())) {
			LOGGER.debug("Cancel promotion " + promo.getPromotionCode() + " first.");
			
			
			PreparedStatement ps = null;

			// switch promo status CANCELLING to CANCELLED. Modified date has to be updated to make sure website picks up the change into the cache.
			ps = conn.prepareStatement("UPDATE CUST.PROMOTION_NEW SET STATUS=?, PUBLISHES=PUBLISHES+1, PUBLISH_DATE=sysdate, , MODIFIED_BY =?, MODIFY_DATE =? WHERE CODE=?");
			ps.setString(1, EnumPromotionStatus.CANCELLED.getName());
			ps.setString(2, promo.getPromotionCode());
	        
	        ps.setString(3, promo.getModifiedBy());
			if (promo.getModifiedDate() != null) {
				ps.setTimestamp(4, new java.sql.Timestamp(promo
						.getModifiedDate().getTime()));
			} else {
				ps.setNull(4, Types.DATE);
			}	
			try {
				final int k = ps.executeUpdate();
				
				if (k != 1) {
					LOGGER.warn("Update command finished with wrong row count; 1 !=" + k);
				}
			} finally {
				if (ps != null)
					ps.close();
			}
		} else {
			LOGGER.debug("Failed to cancel non-existing promotion " + promo.getPromotionCode() + ".");
			return false;
		}

		return true;
	}

	/**
	 * Sets promo status after successful publish operaton
	 * Stage-side action
	 * 
	 * Old Status -> New Status
	 * ========================
	 * 
	 * APPROVED -> PUBLISHED
	 * CANCELLING -> CANCELLED
	 * 
	 * @param conn
	 * @param codes List of Promotion Codes
	 * @return
	 * @throws SQLException 
	 */
	public static boolean fixPromoStatusAfterPublish(Connection conn, Collection<String> codes) throws SQLException {
		if (codes == null || codes.size() == 0) {
			LOGGER.error("No promotion codes has been given");
			return false;
		}
		
		PreparedStatement ps = null;
		// get actual statuses
		Map<String,EnumPromotionStatus> statuses = getCurrentPromoStatuses(conn, codes);


		for (String promoCode : codes) {
			EnumPromotionStatus status = statuses.get(promoCode);
			
			// set state
			if (EnumPromotionStatus.APPROVE.equals(status)) {
				status = EnumPromotionStatus.PUBLISHED;
			} else if (EnumPromotionStatus.CANCELLING.equals(status)) {
				status = EnumPromotionStatus.CANCELLED;
			} else if (EnumPromotionStatus.CANCELLED.equals(status)) {
				LOGGER.error("Published promotion is already " + status.getName() + ", skipping ...");
				return true;
			} else {
				LOGGER.error("Published promotion has invalid status " + status.getName());
				return false;
			}
	
	
			// save status
			try {
				ps = conn.prepareStatement("UPDATE CUST.PROMOTION_NEW SET STATUS=?, PUBLISHES=PUBLISHES+1, PUBLISH_DATE=sysdate WHERE CODE=?");
				ps.setString(1, status.getName());
				ps.setString(2, promoCode);

				ps.executeUpdate();
			} finally {
				if (ps != null)
					ps.close();
			}

		}

		return true;
	}



	/**
	 * Log a completed publish operation
	 * 
	 * @param agent Agent who initiated the publish
	 * @param start Start date of publish
	 * @param end End date of publish
	 * @param destURL URL pointing to Replica Node
	 * @param publishResults Code to Boolean mapping containing success/failure results per promotion
	 * @param preStatuses Promo status before publish
	 * @param postStatuses (can be null)
	 * @param changeIDs (can be null)
	 */
	public static void logPublishEvent(Connection conn, String agentUserId,
			java.util.Date start, java.util.Date end, String destURL,
			Map<String, Boolean> publishResults,
			Map<String, EnumPromotionStatus> preStatuses,
			Map<String, EnumPromotionStatus> postStatuses,
			Map<String, String> changeIDs) throws SQLException {

		PreparedStatement ps = null;


		// fetch post statuses if not given yet
		final Set<String> codes = publishResults.keySet();
		if (postStatuses == null) {
			postStatuses = getCurrentPromoStatuses(conn, codes);
		}
		
		if (changeIDs == null) {
			changeIDs = getPromoChangeIDs(conn, codes);
		}
		
		// generate PK
		String pubPk = SequenceGenerator.getNextId(conn, "CUST");

		// create main log record
		try {
			ps = conn.prepareStatement("INSERT INTO CUST.PUBLISH_LOG "
					+ "(ID,AGENT_ID,START_DATE,END_DATE,DEST_URL) "
					+ "VALUES(?,?,?,?,?)");
			ps.setString(1, pubPk);
			ps.setString(2, agentUserId);
			ps.setTimestamp(3, new Timestamp(start.getTime()));
			ps.setTimestamp(4, new Timestamp(end.getTime()));
			ps.setString(5, destURL);

			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}



		// log each promo results
		for (String promoCode : codes) {
			String pubDtlPk = SequenceGenerator.getNextId(conn, "CUST");

			ps = conn
			.prepareStatement("INSERT INTO CUST.PUBLISH_LOG_DETAIL "
					+ "(ID,PUBLISH_ID,PROMOTION_CODE,CHANGE_ID,PUBLISH_STATUS,PROMO_STATUS_PRE,PROMO_STATUS_POST) "
					+ "VALUES(?,?,?,?,?,?,?)");

			try {
				ps.setString(1, pubDtlPk);
				ps.setString(2, pubPk);
				ps.setString(3, promoCode);
				ps.setString(4, changeIDs.get(promoCode));
				ps.setBoolean(5, publishResults.get(promoCode));
				ps.setString(6, preStatuses.get(promoCode).getName());
				ps.setString(7, postStatuses.get(promoCode).getName());

				ps.execute();
			} finally {
				if (ps != null)
					ps.close();
			}
		}
	}



	private static String joinStringsToInArray(Collection<String> coll) {
		if (coll.size() == 0)
			return "('')";
		
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("('");
		Iterator<String> codeIterator = coll.iterator();
		while (codeIterator.hasNext()) {
			sqlBuf.append(codeIterator.next());
			if (codeIterator.hasNext())
				sqlBuf.append("','");
		}
		sqlBuf.append("')");
		
		return sqlBuf.toString();
	}
	

	/**
	 * Returns the actual states of promotions
	 * 
	 * @param conn
	 * @param codes List of Promotion Codes
	 * @return map of code to status couples
	 * 
	 * @throws SQLException
	 */
	public static Map<String,EnumPromotionStatus> getCurrentPromoStatuses(Connection conn, Collection<String> codes) throws SQLException {
		Map<String,EnumPromotionStatus> s = new HashMap<String, EnumPromotionStatus>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT DISTINCT CODE, STATUS FROM CUST.PROMOTION_NEW WHERE CODE IN " + joinStringsToInArray(codes));
			rs = ps.executeQuery();

			while(rs.next()) {
				s.put(rs.getString("CODE"),
						EnumPromotionStatus.getEnum(rs.getString("STATUS")));
			}
		} finally {
			if (rs != null)
				rs.close();
			
			if (ps != null)
				ps.close();
		}
		
		return s;
	}


	/**
	 * Returns CHANGE_IDs of promotions
	 * 
	 * @param conn
	 * @param codes
	 * @return
	 * @throws SQLException
	 */
	public static Map<String,String> getPromoChangeIDs(Connection conn, Collection<String> codes) throws SQLException {
		Map<String,String> s = new HashMap<String, String>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("select P.CODE, X.CHANGE_ID " +
					"from CUST.PROMOTION_NEW P " +
					"left join (" +
					"  select PROMOTION_ID, max(ID) as CHANGE_ID " +
					"  from CUST.PROMO_CHANGE " +
					"  group by PROMOTION_ID" +
					") X on (P.ID = X.PROMOTION_ID) " +
					"where P.CODE in " + joinStringsToInArray(codes));
			rs = ps.executeQuery();

			while(rs.next()) {
				final String promoCode = rs.getString("CODE");
				final String changeId = rs.getString("CHANGE_ID");
				if (changeId != null) {
					s.put(promoCode, changeId );
				} else
					LOGGER.warn("Promotion " + promoCode + " has no CHANGE_ID");
			}
		} finally {
			if (rs != null)
				rs.close();
			
			if (ps != null)
				ps.close();
		}


		return s;
	}
	
	

	public static FDPromotionNewModel storePromotionOfferInfo(Connection conn,
			ModelI model) throws SQLException {
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.PROMOTION_NEW"
				+ " SET"
				+ " CODE=?, CAMPAIGN_CODE = ?, COMBINE_OFFER = ?, PERISHABLEONLY = ? ,FAVORITES_ONLY = ?, PERCENT_OFF = ?, WAIVE_CHARGE_TYPE = ?," 
				+ " CATEGORY_NAME=?, PRODUCT_NAME=?, EXTEND_DP_DAYS =?,"
				+ " MAX_AMOUNT =?, OFFER_TYPE = ?, MAX_ITEM_COUNT =?, MODIFIED_BY =?, MODIFY_DATE =?"
				+ " WHERE ID = ?");
		int i = 1;
//		i = setupPreparedStatement(ps, promotion, i);
		ps.setString(i++, promotion.getPromotionCode());
		ps.setString(i++, promotion.getPromotionType());
		ps.setString(i++, promotion.isCombineOffer()?"Y":"N");
		ps.setString(i++, promotion.isPerishable()?"Y":"N");
		ps.setString(i++, promotion.isFavoritesOnly()?"Y":"N");
		if (!"".equals(promotion.getPercentOff())) {
			ps.setDouble(i++,
					Double.parseDouble(promotion.getPercentOff()) / 100);
		} else {
			ps.setNull(i++, Types.DOUBLE);
		}
		if (!"".equals(promotion.getWaiveChargeType())) {
			ps.setString(i++, promotion.getWaiveChargeType());
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		ps.setString(i++, promotion.getCategoryName());
		ps.setString(i++, promotion.getProductName());
		if (promotion != null && promotion.getExtendDpDays() != null) {
			ps.setInt(i++, promotion.getExtendDpDays().intValue());					
		} else {
			ps.setNull(i++, Types.INTEGER);										
		}		
		if (!"".equals(promotion.getMaxAmount())) {
			ps.setDouble(i++, Double.parseDouble(promotion.getMaxAmount()));
		} else {
			ps.setNull(i++, Types.DOUBLE);
		}
		if (!"".equals(promotion.getOfferType())) {
			ps.setString(i++, promotion.getOfferType());
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		if (promotion != null && promotion.getMaxItemCount() != null) {
			ps.setInt(i++, promotion.getMaxItemCount().intValue());					
		} else {
			ps.setNull(i++, Types.INTEGER);										
		}
		ps.setString(i++, promotion.getModifiedBy());
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}		
			
		ps.setString(i++, promotion.getPK().getId());
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created to update Promotion");
		}
		ps.close();
		
		storeAssignedGroups(conn, promotion.getId(), promotion);
		return promotion;
	}
	
	public static FDPromotionNewModel storePromotionCartInfo(Connection conn, ModelI model) throws SQLException{
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.PROMOTION_NEW"
				+ " SET"
				+ " MIN_SUBTOTAL = ?, HASSKUQUANTITY = ?, NEEDDRYGOODS = ?,EXCLUDE_SKU_SUBTOTAL =?, MODIFIED_BY =?, MODIFY_DATE =?"
				+ " WHERE ID = ?");
		int i = 1;
//		i = setupPreparedStatement(ps, promotion, i);
		ps.setString(i++, promotion.getMinSubtotal());
		ps.setInt(i++, promotion.getSkuQuantity());
		ps.setString(i++, promotion.isNeedDryGoods()?"X":" ");
		ps.setString(i++, promotion.getSubTotalExcludeSkus());
        ps.setString(i++, promotion.getModifiedBy());
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}				
		ps.setString(i++, promotion.getPK().getId());
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created to update Promotion");
		}
		ps.close();
		storeCartStrategy(conn, promotion.getPK().getId(), promotion);
		return promotion;
	}
	
	public static FDPromotionNewModel storePromotionPaymentInfo(Connection conn, ModelI model) throws SQLException{
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		storePromotionBasic(conn, model);
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.PROMO_CUST_STRATEGY SET PAYMENT_TYPE = ?, PRIOR_ECHECK_USE = ? WHERE ID = ?");
		int i = 1;
		List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
		if(null != custStrategies && !custStrategies.isEmpty()){
			FDPromoCustStrategyModel promoCustModel = (FDPromoCustStrategyModel)custStrategies.get(0);
			
			EnumCardType[] paymentTypes = promoCustModel.getPaymentType();
			StringBuffer paymentType = new StringBuffer();
			if(null != paymentTypes && paymentTypes.length > 0){
				for (int j = 0; j < paymentTypes.length; j++) {
					EnumCardType enumCardType = paymentTypes[j];
					if(null!=enumCardType){
						paymentType.append(enumCardType.getFdName());
						if(j != paymentTypes.length-1)
							paymentType.append(",");
					}
				}
//				paymentType.append(null !=paymentTypes[paymentTypes.length-1]?paymentTypes[paymentTypes.length-1].getFdName():"");
			}
			if(null!=promoCustModel.getId()){
				ps.setString(i++, paymentType.toString());
				ps.setString(i++, promoCustModel.getPriorEcheckUse());
				ps.setString(i++, promoCustModel.getPK().getId());
			}
			else{
				ps = conn.prepareStatement("INSERT INTO cust.PROMO_CUST_STRATEGY (id, promotion_id, payment_type,prior_echeck_use) VALUES(?,?,?,?)");
				ps.setString(i++, SequenceGenerator.getNextId(conn,"CUST"));
				ps.setString(i++, promoCustModel.getPromotionId());
				ps.setString(i++, paymentType.toString());
				ps.setString(i++, promoCustModel.getPriorEcheckUse());
			}		
			
			if (ps.executeUpdate() != 1) {
				ps.close();
				throw new SQLException("row not created to update Promotion");
			}
			ps.close();
		}
		
		return promotion;
	}
	
	private static void storePromotionAddressTypeInfo(Connection conn, ModelI model) throws SQLException{
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.PROMO_CUST_STRATEGY SET ORDERTYPE_HOME = ?, ORDERTYPE_CORPORATE = ?, ORDERTYPE_PICKUP= ?, EXCLUDE_SAMEDAY_DELIVERY=? WHERE ID = ?");
		int i = 1;
		List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
		if(null != custStrategies && !custStrategies.isEmpty()){
			FDPromoCustStrategyModel promoCustModel = (FDPromoCustStrategyModel)custStrategies.get(0);			
			if(null!=promoCustModel.getId()){
				ps.setString(i++, promoCustModel.isOrderTypeHome()?"X":"");
				ps.setString(i++, promoCustModel.isOrderTypeCorporate()?"X":"");
				ps.setString(i++, promoCustModel.isOrderTypePickup()?"X":"");
				ps.setString(i++, promoCustModel.isExcludeSameDayDlv()?"Y":"N");				
				ps.setString(i++, promoCustModel.getPK().getId());
			}
			else{
				ps = conn.prepareStatement("INSERT INTO cust.PROMO_CUST_STRATEGY (id, promotion_id, ORDERTYPE_HOME,ORDERTYPE_CORPORATE,ORDERTYPE_PICKUP,EXCLUDE_SAMEDAY_DELIVERY) VALUES(?,?,?,?,?,?)");
				ps.setString(i++, SequenceGenerator.getNextId(conn,"CUST"));
				ps.setString(i++, promoCustModel.getPromotionId());
				ps.setString(i++, promoCustModel.isOrderTypeHome()?"X":"");
				ps.setString(i++, promoCustModel.isOrderTypeCorporate()?"X":"");
				ps.setString(i++, promoCustModel.isOrderTypePickup()?"X":"");
				ps.setString(i++, promoCustModel.isExcludeSameDayDlv()?"Y":"N");	
			}		
			
			if (ps.executeUpdate() != 1) {
				ps.close();
				throw new SQLException("row not created to update Promotion");
			}
			ps.close();
		}
	}
	
	public static FDPromotionNewModel storePromotionDlvZoneInfo(Connection conn, ModelI model) throws SQLException{
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PreparedStatement ps =	conn.prepareStatement("UPDATE CUST.PROMOTION_NEW SET GEO_RESTRICTION_TYPE =?, MODIFIED_BY =?, MODIFY_DATE =?  where ID=?");
		int i=1;
		if(null != promotion.getGeoRestrictionType() && !"".equals(promotion.getGeoRestrictionType())){
			ps.setString(i++, promotion.getGeoRestrictionType());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
        ps.setString(i++, promotion.getModifiedBy());
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}	
		ps.setString(i++, promotion.getPK().getId());
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created to update Promotion");
		}
		ps.close();
		storePromotionAddressTypeInfo(conn,promotion);
		storePromoDlvDates(conn,promotion.getId(),promotion);
		storePromoDlvZoneStrategy(conn, promotion.getId(), promotion);
		storeGeography(conn, promotion.getId(), promotion.getZipRestrictions());
		
		return promotion;
	}
	

	public static FDPromotionNewModel storePromotionCustReqInfo(Connection conn, ModelI model) throws SQLException{
		FDPromotionNewModel promotion = (FDPromotionNewModel) model;
		PreparedStatement ps =	conn.prepareStatement("UPDATE CUST.PROMOTION_NEW SET PROFILE_OPERATOR =?, MODIFIED_BY =?, MODIFY_DATE =?  where ID=?");
		int i=1;
		if(!"".equals(promotion.getProfileOperator())){
			ps.setString(i++, promotion.getProfileOperator());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
        ps.setString(i++, promotion.getModifiedBy());
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}			
		ps.setString(i++, promotion.getPK().getId());
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created to update Promotion");
		}
		ps.close();
		storeCustomerStrategy(conn, promotion.getId(), promotion);
		storeAttributeList(conn, promotion.getId(), promotion.getAttributeList());		
		return promotion;
	}
	
	public static PrimaryKey savePromoChangeLog(Connection conn, FDPromoChangeModel promoChangeModel) throws SQLException{
		PreparedStatement ps =	conn.prepareStatement("INSERT INTO CUST.PROMO_CHANGE(ID, PROMOTION_ID, USER_ID, ACTION_DATE, ACTION_TYPE)"
						+ " VALUES(?,?,?,?,?)");
		int i=1;
		String id = SequenceGenerator.getNextId(conn,"CUST");
		ps.setString(i++, id);
		ps.setString(i++, promoChangeModel.getPromotionId());
		ps.setString(i++, promoChangeModel.getUserId());
		ps.setTimestamp(i++, new java.sql.Timestamp(promoChangeModel.getActionDate().getTime()));
		ps.setString(i++, promoChangeModel.getActionType().getName());
		
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not created to save Promotion Change");
		}
		ps.close();
		return new PrimaryKey(id);
	}
	
	public static void storePromoChangeDetailLog(Connection conn, List<FDPromoChangeDetailModel> promoChangeDetails) throws SQLException{
		PreparedStatement ps =	conn.prepareStatement("INSERT INTO CUST.PROMO_CHANGE_DETAIL(ID, CHANGE_ID, SECTION_ID, CHANGE_FIELD, OLD_VALUE, NEW_VALUE)"
						+ " VALUES(?,?,?,?,?,?)");
		
		try {
			for (Iterator<FDPromoChangeDetailModel> iterator = promoChangeDetails.iterator(); iterator
					.hasNext();) {
				FDPromoChangeDetailModel promoChangeDetailModel = (FDPromoChangeDetailModel) iterator.next();
			
				int i=1;
				String id = SequenceGenerator.getNextId(conn,"CUST");
				ps.setString(i++, id);
				ps.setString(i++, promoChangeDetailModel.getPromoChangeId());
				ps.setString(i++, promoChangeDetailModel.getChangeSectionId().getName());
				ps.setString(i++, promoChangeDetailModel.getChangeFieldName());
				ps.setString(i++, promoChangeDetailModel.getChangeFieldOldValue());
				ps.setString(i++, promoChangeDetailModel.getChangeFieldNewValue());		
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
		}  finally {
			if (ps != null)
				ps.close();
		}
	}
	
	
	public static void storePromoChangeLog(FDPromotionNewModel promotion,
			Connection conn, PrimaryKey pk) throws SQLException {
		FDPromotionManagerNewDAO.storeChangeLogEntries(conn, pk.getId(), promotion.getAuditChanges());
	}

	public static void storeChangeLogEntries(Connection conn, String promoPk, List<FDPromoChangeModel> changes) throws SQLException {
		if (changes == null)
			return;
		
		for (FDPromoChangeModel changeModel : changes) {
			// fix ID
			changeModel.setPromotionId(promoPk);

			final PrimaryKey changePK = FDPromotionManagerNewDAO.savePromoChangeLog(conn, changeModel);
			
			if (null != changeModel.getChangeDetails()) {
				final List<FDPromoChangeDetailModel> details = changeModel.getChangeDetails();
				
				// fix IDs
				for (FDPromoChangeDetailModel detailModel : details) {
					detailModel.setPromoChangeId(changePK.getId());					
				}
				
				// save details
				FDPromotionManagerNewDAO.storePromoChangeDetailLog(conn, details);
			}
		}
	}
	
	/**
	 * Appends detail to a master log entry
	 * @throws SQLException
	 */
	public static void appendLogDetail(Connection conn, String changeId, FDPromoChangeDetailModel promoChangeDetailModel) throws SQLException {
		PreparedStatement ps = null;
		
		
		try {
			ps = conn.prepareStatement(
				"INSERT INTO CUST.PROMO_CHANGE_DETAIL" +
				"(ID, CHANGE_ID, SECTION_ID, CHANGE_FIELD, OLD_VALUE, NEW_VALUE) " +
				"VALUES(?,?,?,?,?,?)"
			);

			int i=1;
			final String rowId = SequenceGenerator.getNextId(conn,"CUST");
			ps.setString(i++, rowId);
			ps.setString(i++, promoChangeDetailModel.getPromoChangeId());
			ps.setString(i++, promoChangeDetailModel.getChangeSectionId().getName());
			ps.setString(i++, promoChangeDetailModel.getChangeFieldName());
			ps.setString(i++, promoChangeDetailModel.getChangeFieldOldValue());
			ps.setString(i++, promoChangeDetailModel.getChangeFieldNewValue());
			
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
		}
	}


	public static boolean isRedemptionCodeExists(Connection conn, String redemptionCode) throws SQLException{
		boolean isDuplicate = false;
		PreparedStatement ps =	conn.prepareStatement("select count(*) from CUST.Promotion_new where STATUS not in('EXPIRED','CANCELLED','CANCELLING') and upper(redemption_code) = upper(?)");
		ps.setString(1, redemptionCode);
		ResultSet rs = ps.executeQuery();
		int count =0;
		if(rs.next()){
			count = rs.getInt(1);
			if(count>0){
				isDuplicate = true;
			}
		}
		return isDuplicate;
	}
	
	public static boolean isRedemptionCodeExists(Connection conn, String redemptionCode, String promotionId) throws SQLException{
		boolean isDuplicate = false;
		PreparedStatement ps =	conn.prepareStatement("select count(*) from CUST.Promotion_new where STATUS not in('EXPIRED','CANCELLED','CANCELLING') and id <> ? and upper(redemption_code) = upper(?)");
		ps.setString(1, promotionId);
		ps.setString(2, redemptionCode);
		ResultSet rs = ps.executeQuery();
		int count =0;
		if(rs.next()){
			count = rs.getInt(1);
			if(count>0){
				isDuplicate = true;
			}
		}
		return isDuplicate;
	}
	
	public static void storePromotionStatus(Connection conn, EnumPromotionStatus status, FDPromotionNewModel promotion) throws SQLException{
		PreparedStatement ps = null;
		try {			
			ps = conn.prepareStatement("Update cust.Promotion_new set Status =? , MODIFIED_BY =?, MODIFY_DATE =? where id=? ");
			ps.setString(1,status.getName());
	        ps.setString(2, promotion.getModifiedBy());
			if (promotion.getModifiedDate() != null) {
				ps.setTimestamp(3, new java.sql.Timestamp(promotion
						.getModifiedDate().getTime()));
			} else {
				ps.setNull(3, Types.DATE);
			}			
			ps.setString(4,promotion.getPK().getId());
			ps.executeUpdate();
		} finally {
		if (ps != null) ps.close();			
		}	
	}
	
	public static void storePromotionHoldStatus(Connection conn, FDPromotionNewModel promotion) throws SQLException{
		PreparedStatement ps = null;
		try {			
			ps = conn.prepareStatement("Update cust.Promotion_new set on_hold =?, MODIFIED_BY =?, MODIFY_DATE =? where id=? ");
			ps.setString(1,(promotion.isOnHold())?"Y":"N");
	        ps.setString(2, promotion.getModifiedBy());
			if (promotion.getModifiedDate() != null) {
				ps.setTimestamp(3, new java.sql.Timestamp(promotion
						.getModifiedDate().getTime()));
			} else {
				ps.setNull(3, Types.DATE);
			}			
			ps.setString(4,promotion.getId());
			ps.executeUpdate();
		} finally {
		if (ps != null) ps.close();			
		}	
	}

	public static void updatePromotion(Connection conn, FDPromotionNewModel promotion) throws SQLException {
		updatePromotionBasic(conn, promotion);
		updateLeftOutAttributes(conn, promotion);
		String id = promotion.getPK().getId();

		storeAssignedGroups(conn, id, promotion);
		storeAttributeList(conn, id, promotion.getAttributeList());
		storeCartStrategy(conn, id, promotion);
		storeCustomerStrategy(conn, id, promotion);
		storePromoDlvZoneStrategy(conn, id, promotion);
		storePromoDlvDates(conn, id, promotion);

		if (!promotion.getZipRestrictions().isEmpty()) {
			storeGeography(conn, id, promotion.getZipRestrictions());
		}
	}

	public static void updatePromotionBasic(Connection conn, FDPromotionNewModel promotion) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("UPDATE CUST.PROMOTION_NEW SET "
						+ " CODE=?, NAME=?, DESCRIPTION=?, MAX_USAGE=?, START_DATE=?, EXPIRATION_DATE=?, REDEMPTION_CODE=?,"
						+ " CAMPAIGN_CODE=?, MIN_SUBTOTAL=?, MAX_AMOUNT=?, PERCENT_OFF=?, WAIVE_CHARGE_TYPE=?,"
						+ " ROLLING_EXPIRATION_DAYS=?, STATUS=?, OFFER_DESC=?, AUDIENCE_DESC=?, TERMS=?, REDEEM_CNT=?,"
						+ " HASSKUQUANTITY=?, PERISHABLEONLY=?, NEEDDRYGOODS=?, NEEDCUSTOMERLIST=?, RULE_BASED=?,"
						+ " FAVORITES_ONLY=?, COMBINE_OFFER=?, MODIFIED_BY=?, MODIFY_DATE=?,"
						+ " DONOT_APPLY_FRAUD=?, PUBLISHES=?"
						+ " WHERE ID = ?");

		int i = 1;
		i = setupPreparedStatement(ps, promotion, i);

		ps.setString(i++, promotion.getModifiedBy());
		if (promotion.getModifiedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion
					.getModifiedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}
		
		if(!promotion.isApplyFraud()){
			ps.setString(i++, "X");
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		
		ps.setInt(i++, promotion.getPublishes());
		
		ps.setString(i++, promotion.getPK().getId());

		// Execute update
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not updated");
		}
		ps.close();
	}

	/**
	 * TODO please merge this code into a single update/insert which performs full and not partial persist operation
	 */
	public static void updateLeftOutAttributes(Connection conn, FDPromotionNewModel promotion) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.PROMOTION_NEW SET "
				+ " CATEGORY_NAME=?, PRODUCT_NAME=?, PUBLISH_DATE=?, EXTEND_DP_DAYS=?, EXCLUDE_SKU_SUBTOTAL=?, PROFILE_OPERATOR=?,"
				+ " MAX_ITEM_COUNT=?, ON_HOLD=?, OFFER_TYPE=?, GEO_RESTRICTION_TYPE=?"
				+ " WHERE ID = ?");

		int i = 1;

		if (promotion.getCategoryName() != null && promotion.getCategoryName().length() != 0)
			ps.setString(i++, promotion.getCategoryName());
		else
			ps.setNull(i++, Types.VARCHAR);
		if (promotion.getProductName() != null && promotion.getProductName().length() != 0)
			ps.setString(i++, promotion.getProductName());
		else
			ps.setNull(i++, Types.VARCHAR);
		if (promotion.getLastPublishedDate() != null) {
			ps.setTimestamp(i++, new java.sql.Timestamp(promotion.getLastPublishedDate().getTime()));
		} else {
			ps.setNull(i++, Types.DATE);
		}
		if (promotion.getExtendDpDays() != null) {
			ps.setInt(i++, promotion.getExtendDpDays().intValue());
		} else {
			ps.setNull(i++, Types.INTEGER);
		}
		if (promotion.getSubTotalExcludeSkus() != null && promotion.getSubTotalExcludeSkus().length() != 0)
			ps.setString(i++, promotion.getSubTotalExcludeSkus());
		else
			ps.setNull(i++, Types.VARCHAR);
		if (promotion.getProfileOperator() != null && promotion.getProfileOperator().length() != 0) {
			ps.setString(i++, promotion.getProfileOperator());
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		if (promotion.getMaxItemCount() != null) {
			ps.setInt(i++, promotion.getMaxItemCount().intValue());
		} else {
			ps.setNull(i++, Types.INTEGER);
		}
		ps.setString(i++, (promotion.isOnHold()) ? "Y" : "N");
		if (promotion.getOfferType() != null && promotion.getOfferType().length() != 0) {
			ps.setString(i++, promotion.getOfferType());
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}
		if (promotion.getGeoRestrictionType() != null && promotion.getGeoRestrictionType().length() != 0) {
			ps.setString(i++, promotion.getGeoRestrictionType());
		} else {
			ps.setNull(i++, Types.VARCHAR);
		}

		ps.setString(i++, promotion.getPK().getId());

		// Execute update
		if (ps.executeUpdate() != 1) {
			ps.close();
			throw new SQLException("row not updated");
		}
		ps.close();
	}

	public static boolean lookupPromotion(Connection conn, String promotionCode) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE FROM CUST.PROMOTION_NEW WHERE CODE = ?");
		ps.setString(1, promotionCode);
		ResultSet rs = ps.executeQuery();

		try {
			if (rs.next())
				return true;
			else
				return false;
		} finally {
			rs.close();
			ps.close();
		}
	}
}
