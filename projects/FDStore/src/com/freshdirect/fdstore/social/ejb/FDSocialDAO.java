package com.freshdirect.fdstore.social.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;


public class FDSocialDAO {
	
	private static Category LOGGER = LoggerFactory.getInstance(FDSocialDAO.class);
	
	public static String getUserIdForUserToken(Connection con, String userToken) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String userId = "";

		String sql = "SELECT USER_ID FROM CUST.CUST_SOCIAL_LINK WHERE USER_TOKEN=?";
	
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1,userToken.trim());
			rs = ps.executeQuery();
		
			while(rs.next()) {
				userId = rs.getString(1);
				
			}
			
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		} finally {

			try {

				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {

				LOGGER.error(e.getMessage());
			}

		}

		return userId;

	}
	
	public static List<String> getConnectedProvidersByUserId(String userId,Connection con)
	{
		List<String> providers = new ArrayList<String>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT PROVIDER FROM CUST.CUST_SOCIAL_LINK WHERE USER_ID=?";
	
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1,userId);
			rs = ps.executeQuery();
		
			while(rs.next()) {
				
				providers.add(rs.getString("PROVIDER"));
				
				
			}
			
			//LOGGER.info(providers);
			
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		} finally {

			try {

				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {

				LOGGER.error(e.getMessage());
			}

		}

		return providers;
	}
	
	
	public static boolean isUserEmailAlreadyExist(Connection con,String email)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isEmailExist = false;

		String sql = "SELECT COUNT(*) FROM CUST.CUSTOMER WHERE USER_ID=?";
	
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1,email.trim());
			rs = ps.executeQuery();
		
			while(rs.next()) {
				if(rs.getInt(1) > 0)
					isEmailExist = true;
				
			}
			
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		} finally {

			try {

				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {

				LOGGER.error(e.getMessage());
			}

		}

		
		return isEmailExist;
	}
	
	public static void linkUserTokenToUserId(Connection con,String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified)
	{
		String sql="INSERT INTO CUST.CUST_SOCIAL_LINK (USER_ID,USER_TOKEN,IDENTITY_TOKEN,PROVIDER,DISPLAY_NAME,PREFERRED_USER_NAME,EMAIL,EMAIL_VERIFIED) VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement ps = null;
		
		try {
			//LOGGER.info("inside link user");
			
			ps = con.prepareStatement(sql);
			ps.setString(1,userId.trim());
			ps.setString(2,userToken.trim());
			ps.setString(3,identityToken.trim());
			ps.setString(4,provider.trim());
			ps.setString(5,displayName.trim());
			ps.setString(6,preferredUserName);
			ps.setString(7,email.trim());
			ps.setString(8,emailVerified.trim());
			ps.executeUpdate();
		
			//LOGGER.info("inside link user");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {

			try {

				if (ps != null)
					ps.close();
			} catch (SQLException e) {

				LOGGER.error(e.getMessage());
			}

		}
	}
	
	public static void unLinkUserTokenFromUserId(Connection con,String userId,String userToken)
	{
		String sql="DELETE FROM CUST.CUST_SOCIAL_LINK WHERE USER_ID=? AND USER_TOKEN=?";
		PreparedStatement ps = null;
		
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1,userId.trim());
			ps.setString(2,userToken.trim());
			ps.executeUpdate();
			
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		} finally {

			try {

				if (ps != null)
					ps.close();
			} catch (SQLException e) {

				LOGGER.error(e.getMessage());
			}

		}

	}
	
	public static boolean isSocialLoginOnlyUser(String id,Connection con)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isSocialLoginOnly = false;

		String sql = "SELECT COUNT(*) FROM CUST.CUSTOMER WHERE ID=? AND SOCIAL_LOGIN_ONLY=?";
	
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1,id);
			ps.setString(2,"1");
			rs = ps.executeQuery();
		
			while(rs.next()) {
				if(rs.getInt(1) > 0)
					isSocialLoginOnly = true;
				
			}
			
			
			
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		} finally {

			try {

				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {

				LOGGER.error(e.getMessage());
			}
			

		}
	
		return isSocialLoginOnly;
	}
}
