package com.freshdirect.fdstore.cms.ejb;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CMSManagerDAO {
	
	private static final Logger LOG = Logger.getLogger(CMSManagerDAO.class);
	
	public static void publishFeed(Connection conn,String feedId, String storeId, String feedData) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO ERPS.FEED VALUES (?,?,SYSDATE,?)");
		ps.setString(1,feedId);
		InputStream inputStream = new ByteArrayInputStream(feedData.getBytes());
		ps.setBlob(2,inputStream);
		ps.setString(3, storeId);
		ps.execute();
	}
	
	public static String getFeedContent(Connection connection, String storeName){
		StringBuilder response = new StringBuilder();
		BufferedReader reader = null;
		InputStream stream = null;
		try{
			PreparedStatement statement = connection.prepareStatement("select * from erps.feed where cro_mod_datetime = (select max(cro_mod_datetime) from erps.feed where storeid = ?) and storeid = ?");
			statement.setString(1, StringUtils.defaultString(storeName, "FDX"));
			statement.setString(2, StringUtils.defaultString(storeName, "FDX"));
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				Blob blob = resultSet.getBlob("DATA");
				stream = blob.getBinaryStream();
				reader = new BufferedReader(new InputStreamReader(stream));
				String str = null;
				while((str = reader.readLine()) != null){
					response.append(str);
				}
			}
		} catch(SQLException e){
			LOG.error(e);
		} catch (IOException e){
			LOG.error(e);
		} finally {
			IOUtils.closeQuietly(stream);
			IOUtils.closeQuietly(reader);
			
		}
		return response.toString();
	}
}