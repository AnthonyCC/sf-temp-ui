package com.freshdirect.dataloader.promotion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.fdstore.promotion.pxp.PromoPublisher;
import com.freshdirect.framework.util.log.LoggerFactory;


public class WSPromoCancellationCron {
	private static Category	LOGGER	= LoggerFactory.getInstance( WSPromoCancellationCron.class );
	
	protected Connection getConnection() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		//		PRD DB URL: db1.nyc2.freshdirect.com:1521:DBSTO01
		//		INT DB URL: db1.dev.nyc1.freshdirect.com:1521:DBINT01
		String connectString = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = nyc1dbcl01-vip01.nyc1.freshdirect.com)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = nyc1dbcl01-vip02.nyc1.freshdirect.com)(PORT = 1521))(LOAD_BALANCE = yes)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = devint)(FAILOVER_MODE =(TYPE = SELECT)(METHOD = BASIC)(RETRIES = 180)(DELAY = 5))))";
		String connectUser = "fdstore_prda";
		String connectPass = "fdstore_prda";
		if(FDStoreProperties.isWSPromotionProductionMode()) {
			connectString = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=nyc2stdb01-vip.nyc2.freshdirect.com)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=nyc2stdb02-vip.nyc2.freshdirect.com)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=dbsto_prod)(failover_mode=(type=session)(method=basic)(retries=20))))";
			connectUser = "appdev";
			connectPass = "readn0wrt";
		}
		Connection c = 
			DriverManager.getConnection(connectString, connectUser,connectPass);
		return c;
	}
	private static final String GET_ALL_ACTIVE_PROMOTIONS = "select P.ID, P.CODE, P.REDEEM_CNT, P.MAX_AMOUNT, T.DAY_ID, "+
			"(SELECT count(s.id) from cust.sale s, cust.salesaction sa, cust.PROMOTION_PARTICIPATION pa "+
			"where S.ID = SA.SALE_ID "+
			"and S.CUSTOMER_ID = SA.CUSTOMER_ID "+
			"and S.CROMOD_DATE = SA.ACTION_DATE "+
			"and SA.ACTION_TYPE IN ('CRO','MOD') "+
			"and S.STATUS <> 'CAN' "+
			"and S.ID = PA.SALE_ID "+
			"and SA.REQUESTED_DATE >= P.CREATE_DATE "+
			"and PA.PROMOTION_ID = P.ID and to_char(SA.REQUESTED_DATE,'D') = T.DAY_ID) RCOUNT "+ 
			"FROM cust.promotion_new p, cust.PROMO_DLV_ZONE_STRATEGY z, "+
			"cust.PROMO_DLV_TIMESLOT t "+
			"where P.ID = Z.PROMOTION_ID "+
			"and Z.ID = T.PROMO_DLV_ZONE_ID "+
			"and P.STATUS = 'LIVE' "+
			"and  P.START_DATE <= ? "+
			"and  P.EXPIRATION_DATE >= ?";
	
	private List<WSPromotionInfo> getAllActiveWSPromotions(Date effectiveDate) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WSPromotionInfo> wspromotions = new ArrayList<WSPromotionInfo>();
		try {
			//Get All LIVE WS promotions for current day.
			conn = this.getConnection();
			ps = conn.prepareStatement(GET_ALL_ACTIVE_PROMOTIONS);
			ps.setTimestamp(1, new Timestamp(effectiveDate.getTime()));
			ps.setTimestamp(2, new Timestamp(effectiveDate.getTime()));
			rs = ps.executeQuery();
			while(rs.next()) {
				WSPromotionInfo promo = new WSPromotionInfo();
				promo.setPromotionCode(rs.getString("CODE"));
				promo.setRedeemCount(rs.getInt("REDEEM_CNT"));
				promo.setDiscount(rs.getDouble("MAX_AMOUNT"));
				promo.setDayofweek(rs.getInt("DAY_ID"));
				promo.setRedemptions(rs.getInt("RCOUNT"));
				wspromotions.add(promo);
			}

		}catch(SQLException se) {
			
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return wspromotions;
	}
	private Set<String> getWSPromotionstoCancel() throws FDResourceException {
		Set<String> cancelPromoCodes = new HashSet<String>();
		try {
			Date today = new Date();
			List<WSPromotionInfo> wspromotions = getAllActiveWSPromotions(today);
			if(wspromotions == null || wspromotions.size() == 0){
				return Collections.emptySet();
			}
			//Load dayofweek limits from Database.
			Map<Integer,Double> dowLimits = FDPromotionNewManager.getDOWLimits();
	
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			for(int i = 1 ; i <= 7; i++) {
				//check for next 7 days delivery horizon
				cal.add(Calendar.DATE, 1);
				double totalDiscount = 0;
				Set<String> temp = new HashSet<String>();
				int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
				Iterator<WSPromotionInfo> it = wspromotions.iterator();
				while(it.hasNext()){
					WSPromotionInfo promo = it.next();
					if(promo.getDayofweek() == dayofweek) {
						temp.add(promo.getPromotionCode());
						totalDiscount += (promo.getDiscount() * promo.getRedemptions());
					}
				}
				Double dowLimit = dowLimits.get(new Integer(dayofweek));
				if(dowLimit == null) continue;
				BigDecimal limit = new BigDecimal(dowLimit.doubleValue()); 
				BigDecimal total = new BigDecimal(totalDiscount);
				if(total.compareTo(limit) >= 0){
					//limit reached. add all temp codes to main set.
					cancelPromoCodes.addAll(temp);
				}
			}
			//Now check for individual promotion redemption limit.
			Iterator<WSPromotionInfo> it = wspromotions.iterator();
			while(it.hasNext()){
				WSPromotionInfo promo = it.next();
				if(!cancelPromoCodes.contains(promo.getPromotionCode())){
					//Check for redemption limit.
					if(promo.getRedemptions() >= promo.getRedeemCount()) {
						//Limit reached.
						cancelPromoCodes.add(promo.getPromotionCode());
					}
				}
			}
		} catch(SQLException se) {
			throw new FDResourceException(se);
		}
		return cancelPromoCodes;
	}
	private List<FDPromotionNewModel> cancelPromotions(Set<String> cancelPromoCodes) throws FDResourceException, FinderException{
		List<FDPromotionNewModel> cancelledPromotions = new ArrayList<FDPromotionNewModel>();
		
		if(cancelPromoCodes.size() > 0 ){
			Iterator<String> it = cancelPromoCodes.iterator();
			while(it.hasNext()) {
				String promoCode = it.next();
				FDPromotionNewModel promotion = FDPromotionNewManager.loadPromotion(promoCode);
				if(promotion == null){
					throw new FDResourceException("Unable to locate Windows Steering Promotion. Please contact AppSupport.");
				}
				promotion.setAuditChanges(FDPromotionNewManager.loadPromoAuditChanges(promotion.getId()));
				promotion.setStatus(EnumPromotionStatus.CANCELLING);
				promotion.setModifiedBy("System");
				promotion.setModifiedDate(new Date());						
				FDPromoChangeModel changeModel = new FDPromoChangeModel();
				List<FDPromoChangeModel> promoChanges = new ArrayList<FDPromoChangeModel>();
				changeModel.setPromotionId(promotion.getId());
				changeModel.setActionDate(new Date());
				changeModel.setActionType(EnumPromoChangeType.CANCEL);
				changeModel.setUserId("System");
				promoChanges.add(changeModel);
				promotion.setAuditChanges(promoChanges);
				
				FDPromotionNewManager.storePromotionStatus(promotion,EnumPromotionStatus.CANCELLING);
				FDPromotionNewModel promo = FDPromotionNewManager.loadPromotion(promoCode);
				cancelledPromotions.add(promo);
			}
		}
		return cancelledPromotions;
	}
	public void publishPromotions(List<FDPromotionNewModel> ppList) {
		CrmAgentModel agent = new CrmAgentModel();
		PromoPublisher publisher = new PromoPublisher();
		publisher.setPromoList(ppList);
		publisher.setAgent(agent);
		final boolean result = publisher.doPublish();
		if (!result) {
			//Log Error
			LOGGER.error("Published failed. Check Server Log.");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		WSPromoCancellationCron cron = new WSPromoCancellationCron();
		Set<String> cancelPromoCodes = cron.getWSPromotionstoCancel();
		List<FDPromotionNewModel> cancelledPromotions = cron.cancelPromotions(cancelPromoCodes);
		cron.publishPromotions(cancelledPromotions);
	}

}
