package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDUserDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(FDUserDAO.class);

	private static java.util.Random rand = new java.util.Random();

	private static String newCookieId() {
		StringBuffer buff = new StringBuffer();
		for (int j = 0; j < 3; j++)
			buff.append(Long.toString(Math.abs(rand.nextLong()), 36).toUpperCase());
		return buff.toString();
	}

	private static FDUser createUser(Connection conn, String cookie, String street, String apt, String zipCode, String depotCode, EnumServiceType serviceType) throws SQLException {
		
		System.out.println("inside the create user .. creating the user");
		String id = SequenceGenerator.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.FDUSER (ID, COOKIE, ZIPCODE, DEPOT_CODE, SERVICE_TYPE, CREATED, ADDRESS1, APARTMENT,ZP_SERVICE_TYPE) values (?,?,?,?,?,SYSDATE,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, cookie);
		if (zipCode != null) {
			ps.setString(3, zipCode);
		} else {
			ps.setNull(3, Types.VARCHAR);
		}
		if (depotCode != null) {
			ps.setString(4, depotCode);
		} else {
			ps.setNull(4, Types.VARCHAR);
		}
		if(serviceType != null){
			ps.setString(5, serviceType.getName());
		} else {
			ps.setNull(5, Types.VARCHAR);
		}
		if(street != null){
			ps.setString(6, street);
		} else {
			ps.setNull(6, Types.VARCHAR);
		}
		if(apt != null){
			ps.setString(7, apt);
		} else {
			ps.setNull(7, Types.VARCHAR);
		}
		//Add for zone pricing.
		if(serviceType != null){
			ps.setString(8, serviceType.getName());
		} else {
			ps.setNull(8, Types.VARCHAR);
		}
		
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}

		ps.close();

		FDUser user = new FDUser(new PrimaryKey(id));
		user.setCookie(cookie);
		user.setZipCode(zipCode);
		user.setDepotCode(depotCode);
		// no need to create children, a new user doesn't have a cart yet
		return user;
	}

	private static List<ErpOrderLineModel> convertToErpOrderlines(List<FDCartLineI> cartlines) throws FDResourceException {

		int num = 0;
		List<ErpOrderLineModel> erpOrderlines = new ArrayList<ErpOrderLineModel>();
		for ( FDCartLineI cartline : cartlines ) {
			ErpOrderLineModel erpLines;
			try {
				erpLines = cartline.buildErpOrderLines(num);
			} catch (FDInvalidConfigurationException e) {
				LOGGER.warn("Skipping invalid cartline", e);
				continue;
			}
			erpOrderlines.add(erpLines);
			num += 1;
		}

		return erpOrderlines;
	}

	private static List<FDCartLineI> convertToCartLines(List<ErpOrderLineModel> erplines) {

		List<FDCartLineI> cartlines = new ArrayList<FDCartLineI>();
		for (int i = 0; i < erplines.size(); i++) {
			ErpOrderLineModel ol = erplines.get(i);

			FDCartLineI cartLine;
			cartLine = new FDCartLineModel(ol);

			cartlines.add(cartLine);
		}

		return cartlines;
	}

	public static FDUser createDepotUser(Connection conn, String depotCode, EnumServiceType serviceType) throws SQLException {
		FDUser user = createUser(conn, newCookieId(), null, null, null, depotCode, serviceType);
		return user;
	}

	public static FDUser createUser(Connection conn, String zipCode, EnumServiceType serviceType) throws SQLException {
		FDUser user = createUser(conn, newCookieId(), null, null, zipCode, null, serviceType);
		return user;
	}

	public static FDUser createUser(Connection conn, AddressModel address, EnumServiceType serviceType) throws SQLException {
		FDUser user = createUser(conn, newCookieId(), address.getAddress1(), address.getApartment(), address.getZipCode(), null, serviceType);
		return user;
	}
	
	private static final String LOAD_FROM_IDENTITY_QUERY =
		"SELECT fdu.ID as fduser_id, fdu.COOKIE, fdu.ADDRESS1, fdu.APARTMENT, fdu.ZIPCODE, fdu.DEPOT_CODE, fdu.SERVICE_TYPE,fdu.HPLETTER_VISITED, fdu.CAMPAIGN_VIEWED, fdc.id as fdcust_id, erpc.id as erpcust_id, fdu.ref_tracking_code, " +
		"erpc.active, ci.receive_news,fdu.last_ref_prog_id, fdu.ref_prog_invt_id, fdu.ref_trk_key_dtls, fdu.COHORT_ID, fdu.ZP_SERVICE_TYPE " +
		//",fdc.referer_customer_id, fdc.referral_prgm_id " +
		"FROM CUST.FDUSER fdu, CUST.fdcustomer fdc, CUST.customer erpc, CUST.customerinfo ci " +
		"WHERE fdu.FDCUSTOMER_ID=fdc.id and fdc.ERP_CUSTOMER_ID=erpc.ID and erpc.id=? " +
		"AND erpc.id = ci.customer_id";

	public static FDUser recognizeWithIdentity(Connection conn, FDIdentity identity) throws SQLException {
		LOGGER.debug("attempting to load FDUser from identity");
		PreparedStatement ps = conn.prepareStatement(LOAD_FROM_IDENTITY_QUERY);
		ps.setString(1, identity.getErpCustomerPK());
		ResultSet rs = ps.executeQuery();
		FDUser user = loadUserFromResultSet(rs);

		if (!user.isAnonymous()) {
			FDCartModel cart = new FDCartModel();
			cart.addOrderLines(convertToCartLines(FDCartLineDAO.loadCartLines(conn, user.getPK())));
			user.setShoppingCart(cart);
			//Load GC recipients if any. 
			List<SavedRecipientModel> recipients = SavedRecipientDAO.loadSavedRecipients(conn, user.getPK().getId());
			FDRecipientList repList = new FDRecipientList(recipients);
			user.setRecipientList(repList);
		}
		rs.close();
		ps.close();

		return user;
	}

	private static final String LOAD_FROM_EMAIL_QUERY =
		"SELECT fdu.ID as fduser_id, fdu.COOKIE, fdu.ADDRESS1, fdu.APARTMENT, fdu.ZIPCODE, fdu.DEPOT_CODE, fdu.SERVICE_TYPE,fdu.HPLETTER_VISITED, fdu.CAMPAIGN_VIEWED, fdc.id as fdcust_id, erpc.id as erpcust_id, fdu.ref_tracking_code, " +
		"erpc.active, ci.receive_news,fdu.last_ref_prog_id, fdu.ref_prog_invt_id, fdu.ref_trk_key_dtls, fdu.COHORT_ID, fdu.ZP_SERVICE_TYPE  " +
		//",fdc.referer_customer_id, fdc.referral_prgm_id " +
		"FROM CUST.FDUSER fdu, CUST.fdcustomer fdc, CUST.customer erpc, CUST.customerinfo ci " +
		"WHERE fdu.FDCUSTOMER_ID=fdc.id and fdc.ERP_CUSTOMER_ID=erpc.ID and erpc.user_id=? " +
		"AND erpc.id = ci.customer_id";

	public static FDUser recognizeWithEmail(Connection conn, String email) throws SQLException {
		LOGGER.debug("attempting to load FDUser based on user id (email)");
		PreparedStatement ps = conn.prepareStatement(LOAD_FROM_EMAIL_QUERY);
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();
		FDUser user = loadUserFromResultSet(rs);

		if (!user.isAnonymous()) {
			FDCartModel cart = new FDCartModel();
			cart.addOrderLines(convertToCartLines(FDCartLineDAO.loadCartLines(conn, user.getPK())));
			user.setShoppingCart(cart);
		}
		rs.close();
		ps.close();

		return user;
	}

	private static final String LOAD_FROM_COOKIE_QUERY =
		"SELECT fdu.ID as fduser_id, fdu.COOKIE, fdu.ADDRESS1, fdu.APARTMENT, fdu.ZIPCODE, fdu.DEPOT_CODE, fdu.SERVICE_TYPE, fdu.HPLETTER_VISITED, fdu.CAMPAIGN_VIEWED, fdc.id as fdcust_id, erpc.id as erpcust_id, fdu.ref_tracking_code, " +
		"erpc.active, ci.receive_news, fdu.last_ref_prog_id, fdu.ref_prog_invt_id, fdu.ref_trk_key_dtls, fdu.COHORT_ID, fdu.ZP_SERVICE_TYPE " +
		//",fdc.referral_link, fdc.referer_customer_id, fdc.referral_prgm_id " +
		"FROM CUST.FDUSER fdu, CUST.fdcustomer fdc, CUST.customer erpc, CUST.customerinfo ci " +
		"WHERE fdu.cookie=? and fdu.FDCUSTOMER_ID=fdc.id(+) and fdc.ERP_CUSTOMER_ID=erpc.ID(+) " +
		"AND erpc.id = ci.customer_id(+)";

	public static FDUser reconnizeWithCookie(Connection conn, String cookie) throws SQLException {
		LOGGER.debug("attempting to load FDUser from cookie");

		PreparedStatement ps = conn.prepareStatement(LOAD_FROM_COOKIE_QUERY);
		ps.setString(1, cookie);
		ResultSet rs = ps.executeQuery();
		FDUser user = loadUserFromResultSet(rs);

		if (!user.isAnonymous()) {
			FDCartModel cart = new FDCartModel();
			cart.addOrderLines(convertToCartLines(FDCartLineDAO.loadCartLines(conn, user.getPK())));
			user.setShoppingCart(cart);
			//Load GC recipients if any. 
			List<SavedRecipientModel> recipients = SavedRecipientDAO.loadSavedRecipients(conn, user.getPK().getId());
			FDRecipientList repList = new FDRecipientList(recipients);
			user.setRecipientList(repList);
		}

		rs.close();
		ps.close();

		return user;
	}

	private static FDUser loadUserFromResultSet(ResultSet rs) throws SQLException {
		FDUser user = null;
		if (rs.next()) {
			PrimaryKey pk = new PrimaryKey(rs.getString("FDUSER_ID"));
			user = new FDUser(pk);
			user.setCookie(rs.getString("COOKIE"));
			user.setZipCode(rs.getString("ZIPCODE"));
			user.setDepotCode(rs.getString("DEPOT_CODE"));
			user.setSelectedServiceType(EnumServiceType.getEnum(rs.getString("SERVICE_TYPE")));
			user.setLastRefTrackingCode(rs.getString("REF_TRACKING_CODE"));
            user.setLastRefProgramId(rs.getString("last_ref_prog_id"));
            user.setLastRefProgInvtId(rs.getString("ref_prog_invt_id"));
            user.setLastRefTrkDtls(rs.getString("ref_trk_key_dtls"));
            // for new COS customer
            user.setUserServiceType(EnumServiceType.getEnum(rs.getString("SERVICE_TYPE")));
            // for zone pricing
            //Default to service type if zp_service_type is null.
            String zpServiceType = rs.getString("ZP_SERVICE_TYPE") != null ? rs.getString("ZP_SERVICE_TYPE")  : rs.getString("SERVICE_TYPE");
            user.setZPServiceType(EnumServiceType.getEnum(zpServiceType));
            
			AddressModel addr = user.getAddress();
			if(addr != null) {
				addr.setAddress1(rs.getString("ADDRESS1"));
				addr.setApartment(rs.getString("APARTMENT"));
			}
			
			String fdcustId = rs.getString("FDCUST_ID");
			String erpcustId = rs.getString("ERPCUST_ID");
			if ((fdcustId != null) && (erpcustId != null)) {
				user.setIdentity(new FDIdentity(erpcustId, fdcustId));
			}
			user.setActive("1".equals(rs.getString("ACTIVE")));
			user.setReceiveFDEmails("X".equals(rs.getString("RECEIVE_NEWS")));
			user.setHomePageLetterVisited(NVL.apply(rs.getString("HPLETTER_VISITED"), "").equalsIgnoreCase("X")?true:false);
			user.setCampaignMsgViewed(rs.getInt("CAMPAIGN_VIEWED"));

			// Smart Store - Cohort ID
			user.setCohortName(rs.getString("COHORT_ID"));
			
			//APPDEV-1888 referral info
			//user.setReferralPrgmId(rs.getString("referral_prgm_id"));
			//user.setReferralCustomerId(rs.getString("referer_customer_id"));
		} else {
			user = new FDUser();
		}

		return user;
	}
	

	private static final String STORE_USER_SQL =
		"UPDATE CUST.FDUSER " +
		"SET COOKIE=?, ZIPCODE=?, FDCUSTOMER_ID=?, DEPOT_CODE=?, SERVICE_TYPE=?, ADDRESS1=?, APARTMENT=?, " +
		"LAST_REF_PROG_ID=?, REF_PROG_INVT_ID=?, REF_TRK_KEY_DTLS=?, HPLETTER_VISITED=?, CAMPAIGN_VIEWED=?, COHORT_ID=?, ZP_SERVICE_TYPE=? " + 
		"WHERE ID=?";


	public static void storeUser(Connection conn, FDUser user) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(STORE_USER_SQL);
		int index = 1;
		ps.setString(index++, user.getCookie());
		if (user.getZipCode() != null) {
			ps.setString(index++, user.getZipCode());
		} else {
			ps.setNull(index++, Types.VARCHAR);
		}
		FDIdentity identity = user.getIdentity();
		if ((identity != null) && (identity.getFDCustomerPK() != null)) {
			ps.setString(index++, identity.getFDCustomerPK());
		} else {
			ps.setNull(index++, Types.VARCHAR);
		}
		if (user.getDepotCode() != null) {
			ps.setString(index++, user.getDepotCode());
		} else {
			ps.setNull(index++, Types.VARCHAR);
		}
		if(user.getSelectedServiceType() == null){
			ps.setNull(index++, Types.VARCHAR);
		}else{
			ps.setString(index++, user.getSelectedServiceType().getName());
		}
		AddressModel addr = user.getAddress();
		if(addr != null) {
			if(addr.getAddress1() == null) {
				ps.setNull(index++, Types.VARCHAR);			
			} else {
				ps.setString(index++, addr.getAddress1());
			}
			if(addr.getApartment() == null) {
				ps.setNull(index++, Types.VARCHAR);			
			} else {
				ps.setString(index++, addr.getApartment());
			}
		} else {
			ps.setNull(index++, Types.VARCHAR);
			ps.setNull(index++, Types.VARCHAR);		
		}

		if(user.getLastRefProgId()!=null)			
		    ps.setString(index++, user.getLastRefProgId());
		else
			ps.setNull(index++, Types.VARCHAR);
		
		if(user.getLastRefProgInvtId()!=null)			
		    ps.setString(index++, user.getLastRefProgInvtId());
		else
			ps.setNull(index++, Types.VARCHAR);

		if(user.getLastRefTrkDtls()!=null)			
		    ps.setString(index++, user.getLastRefTrkDtls());
		else
			ps.setNull(index++, Types.VARCHAR);

		if(user.isHomePageLetterVisited())
		{
			ps.setString(index++, "X" );	
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}

		ps.setInt(index++, user.getCampaignMsgViewed());
		
		// Smart Store - Cohort ID
		if (user.getCohortName() != null)
			ps.setString(index++, user.getCohortName());
		else
			ps.setNull(index++, Types.VARCHAR);
		
		if(user.getZPServiceType() == null){
			ps.setNull(index++, Types.VARCHAR);
		}else{
			ps.setString(index++, user.getZPServiceType().getName());
		}		
		
		// where id = ...
		ps.setString(index++, user.getPK().getId());
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();

		// store children

		ps = conn.prepareStatement("DELETE FROM CUST.FDCARTLINE WHERE FDUSER_ID = ?");
		ps.setString(1, user.getPK().getId());
		ps.executeUpdate();
		ps.close();

		List<ErpOrderLineModel> erpOrderLine;
		try {
			erpOrderLine = convertToErpOrderlines(user.getShoppingCart().getOrderLines());
		} catch (FDResourceException e) {
			// !!! fix exception handling
			throw new FDRuntimeException(e);
		}
		
		FDCartLineDAO.storeCartLines(conn, user.getPK(), erpOrderLine);
		//Store GC Recipient List if any.
		if(user.getRecipientList() != null){
			SavedRecipientDAO.storeSavedRecipients(conn, user.getPK().getId(), user.getRecipientList().getRecipients());
		}
	}
	
	
	/**
	 * [APPREQ-369] Store Cohort ID for the given user
	 * 
	 * @param conn
	 * @param user
	 * @throws SQLException
	 */
	public static void storeCohortName(Connection conn, FDUser user) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.FDUSER SET COHORT_ID=? WHERE ID=?");

		if (user.getCohortName() != null)
			ps.setString(1, user.getCohortName());
		else
			ps.setNull(1, Types.VARCHAR);

		ps.setString(2, user.getPK().getId());

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
	}
	
	public static void storeMobilePreferences(Connection conn, String customerId, String mobileNumber, String textOffers, String textDelivery) {
		PreparedStatement ps = null;
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		
		try {
			ps = conn.prepareStatement("update CUST.CUSTOMERINFO set mobile_number=?, offers_notification=?,delivery_notification=? where customer_id=?");				
			ps.setString(1, phone.getPhone());
			ps.setString(2, "Y".equals(textOffers)?"Y":"N");
			ps.setString(3, "Y".equals(textDelivery)?"Y":"N");
			ps.setString(4, customerId);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error updating mobile preferences", e);
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (Exception e1) {}
		}
	}
	
	public static void storeGoGreenPreferences(Connection conn, String customerId, String goGreen) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update CUST.CUSTOMERINFO set go_green=? where customer_id=?");				
			ps.setString(1, "Y".equals(goGreen)?"Y":"N");
			ps.setString(2, customerId);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error updating mobile preferences", e);
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (Exception e1) {}
		}
	}
	
	public static void storeMobilePreferencesNoThanks(Connection conn, String customerId) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update CUST.CUSTOMERINFO set mobile_preference_flag='Y' where customer_id=?");				
			ps.setString(1, customerId);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error updating mobile preferences", e);
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (Exception e1) {}
		}
	}
	
	public static void storeAllMobilePreferences(Connection conn, String customerId, String mobileNumber, String textOffers, String textDelivery, String goGreen, String phone, String ext, boolean isCorpUser) {
		PreparedStatement ps = null;
		PhoneNumber mphone = new PhoneNumber(mobileNumber);
		PhoneNumber bphone = new PhoneNumber(phone, ext);
		
		try {
			if(isCorpUser)
				ps = conn.prepareStatement("update CUST.CUSTOMERINFO set mobile_number=?, offers_notification=?,delivery_notification=?, go_green=?,business_phone=?,business_ext=?,mobile_preference_flag='U'  where customer_id=?");
			else
				ps = conn.prepareStatement("update CUST.CUSTOMERINFO set mobile_number=?, offers_notification=?,delivery_notification=?, go_green=?, home_phone = ?, home_ext = ?,mobile_preference_flag='U' where customer_id=?");
			ps.setString(1, mphone.getPhone());
			ps.setString(2, "Y".equals(textOffers)?"Y":"N");
			ps.setString(3, "Y".equals(textDelivery)?"Y":"N");
			ps.setString(4, "Y".equals(goGreen)?"Y":"N");
			ps.setString(5, bphone.getPhone());
			ps.setString(6, bphone.getExtension());
			ps.setString(7, customerId);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error updating mobile preferences", e);
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (Exception e1) {}
		}
	}
}
