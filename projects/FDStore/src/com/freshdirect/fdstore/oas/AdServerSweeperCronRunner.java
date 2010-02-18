package com.freshdirect.fdstore.oas;


import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AdServerSweeperCronRunner {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Start AdServerSweeperCronRunner..");		
		Connection conn = null;
		try {
			conn = getConnection(args);
			if(null != args && args.length>5)
				updateOASdatabase(conn, args[5]);
			else
				updateOASdatabase(conn, null);
			System.out.println("Stop AdServerSweeperCronRunner..");
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in AdServerSweeperCronRunner..");
		} catch (NamingException e) {
			e.printStackTrace();
			System.out.println("Error in AdServerSweeperCronRunner..");
		} finally {
			try {
				if(null !=conn)
					conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				System.out.println("Error in AdServerSweeperCronRunner..");
			}
		}
		
	} 

	
	private static void updateOASdatabase(Connection conn, String campaigns) throws SQLException{
		
		System.out.println("Inside updateOASdatabase() method.");		
		PreparedStatement  ps = null;
		if(null != campaigns && campaigns.trim().length()>0){
			ps = conn.prepareStatement("select c.CampaignKey, c.SearchTerm from Campaign c where c.CampaignKey in("+campaigns+")");			
		}else{
			ps = conn.prepareStatement("select c.CampaignKey, c.SearchTerm from Campaign c,Campaign_Creative cc where c.CampaignKey = cc.CampaignKey and cc.CreativeKey in(Select Distinct(CreativeKey) From Creative c1, CreativeUpdate_Zone cu where c1.DisplayFlag ='Yes' and instr(c1.extraHtml, Concat('productId=', cu.ProductId, '&')) and cu.ZONETYPE<>'M' and cu.ZONETYPE<>'')");
		}
		ResultSet rs = ps.executeQuery();
		PreparedStatement  ucps = conn.prepareStatement("update Campaign c set c.SearchTerm = ? where c.CampaignKey=?");
		PreparedStatement iczps = conn.prepareStatement("insert into CampaignZoneTerm(CampaignKey,ZoneSearchTerm,TimeStamp) values(?,?,?)");
		Date date = new Date();		
		while(rs.next()){
			int campaignKey = rs.getInt(1);
			String oldsearchTerm = rs.getString(2);
			prepareZonePricingSearchTerm(conn, ucps, iczps, date, campaignKey,
					oldsearchTerm);
			
		}
		int[] result = ucps.executeBatch();
		System.out.println("Updated "+result.length+" Campaign records.");
		ucps.close();
		
		result = iczps.executeBatch();
		System.out.println("Inserted "+result.length+" CampaignZoneTerm records.");
		iczps.close();
		System.out.println("Completed updateOASdatabase() method.");
	}


	private static void prepareZonePricingSearchTerm(Connection conn,
			PreparedStatement ucps, PreparedStatement iczps, Date date,
			int campaignKey, String oldsearchTerm) throws SQLException {
		String searchTerm ="";
		if(null != searchTerm && !searchTerm.trim().equals("")){
			searchTerm = searchTerm +"AND((zonelevel!?)OR((zonelevel=true)";				
		}else{
			searchTerm = "((zonelevel!?)OR((zonelevel=true)";
		}
//				searchTerm = searchTerm +"&& (zonelevel=null ||(zonelevel=true ";
		{
			PreparedStatement ps1 = conn.prepareStatement("select cuz.ZoneId from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.ExtraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.ZONETYPE<>'M' and cuz.price is not null and cuz.price<>'' and c1.LinkText<>'' and c1.LinkText <> cuz.Price)and cr.CreativeKey = CC.creativeKey and CC.CampaignKey =?;");
			ps1.setInt(1, campaignKey);
			ResultSet rs1 = ps1.executeQuery();
			searchTerm = searchTerm+"AND(";
			
			searchTerm = searchTerm+"((zid?)";	
			while(rs1.next()){
				if(rs1.isFirst())
					searchTerm = searchTerm+"AND(";	
											
				searchTerm = searchTerm + "(zid!="+rs1.getString(1)+")";	
				if(!rs1.isLast())
					searchTerm = searchTerm+"AND";
				if(rs1.isLast())
					searchTerm = searchTerm+")";
			}
			PreparedStatement ps2 = conn.prepareStatement("select cuz.zoneId from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cuz.ZoneId<>'' and cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.ExtraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.price is not null and cuz.price<>'' and (c1.LinkText = cuz.Price or c1.LinkText=''))	and cr.CreativeKey = CC.CreativeKey and CC.CampaignKey =?;");
			ps2.setInt(1, campaignKey);
			ResultSet rs2 = ps2.executeQuery();
//			searchTerm = searchTerm+"AND(";	
			
			while(rs2.next()){
				if(rs2.isFirst())
					searchTerm = searchTerm+"AND(";
				searchTerm = searchTerm + "(zid="+rs2.getString(1)+")";
				if(!rs2.isLast())
					searchTerm = searchTerm+"OR";					
				if(rs2.isLast())
					searchTerm = searchTerm +")";
			}
			searchTerm = searchTerm+")";	
			PreparedStatement ps3 = conn.prepareStatement("select count(cuz.zoneId) from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.ExtraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.price is not null and cuz.price<>'' and cuz.ZONETYPE='S')	and cr.CreativeKey = CC.CreativeKey and CC.CampaignKey =?;");
			ps3.setInt(1, campaignKey);
			ResultSet rs3 = ps3.executeQuery();
			if(rs3.next() && rs3.getInt(1)>0){
				searchTerm = searchTerm +"OR((zid!?)AND(szid?)";
				ps3 = conn.prepareStatement("select cuz.zoneId from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cuz.ZoneId<>'' and cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.ExtraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.price is not null and cuz.price<>'' and c1.LinkText <>'' and c1.LinkText <> cuz.Price and cuz.ZONETYPE='S')	and cr.CreativeKey = CC.CreativeKey and CC.CampaignKey =?;");
				ps3.setInt(1, campaignKey);
				rs3 = ps3.executeQuery();
				while(rs3.next()){
					searchTerm = searchTerm+"AND";
					searchTerm = searchTerm + "(szid!="+rs3.getString(1)+")";							
				}
//					searchTerm = searchTerm +"&& ";
				PreparedStatement ps4 = conn.prepareStatement("select cuz.zoneId from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cuz.ZoneId<>'' and cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.extraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.price is not null and cuz.price<>'' and (c1.LinkText = cuz.Price or c1.LinkText='') and cuz.ZONETYPE='S')	and cr.CreativeKey = CC.creativeKey and CC.CampaignKey =?;");
				ps4.setInt(1, campaignKey);
				ResultSet rs4= ps4.executeQuery();
				while(rs4.next()){
					if(rs4.isFirst())	
						searchTerm = searchTerm+"AND(";
					if(!rs4.isFirst()){
						searchTerm = searchTerm +"OR";
					}
					searchTerm = searchTerm+"(szid="+rs4.getString(1)+")";
					if(rs4.isLast())
						searchTerm = searchTerm+")";
				}
				searchTerm = searchTerm+")";
			}
			searchTerm = searchTerm +"OR((zid!?)AND(szid!?)AND(";
			PreparedStatement ps5 = conn.prepareStatement("select cuz.zoneId from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cuz.ZoneId<>'' and cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.extraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.price is not null and cuz.price<>'' and (c1.LinkText = cuz.Price or c1.LinkText='') and cuz.ZONETYPE='M')	and cr.CreativeKey = CC.creativeKey and CC.CampaignKey =?;");
			ps5.setInt(1, campaignKey);
			ResultSet rs5= ps5.executeQuery();			
			if(rs5.next()){				
				/*if(rs5.isFirst()){
					searchTerm = searchTerm +"OR((zid!?)AND(szid!?)AND(";
					isMasterZonePriceMatched = true;
				}
				if(!rs5.isFirst()){
					searchTerm = searchTerm +"OR";
				}*/
				searchTerm = searchTerm +"(mzid="+rs5.getString(1)+")";
				/*if(rs5.isLast())
					searchTerm = searchTerm+"))";*/
			}else{
				ps5 = conn.prepareStatement("select cuz.zoneId from Campaign_Creative CC, Creative cr, CreativeUpdate_Zone cuz where cuz.ZoneId<>'' and cr.CreativeKey in(Select Distinct(CreativeKey) From Creative c1 where instr(c1.extraHtml, Concat('productId=', cuz.ProductId, '&')) and cuz.price is not null and cuz.price<>'' and c1.LinkText <> cuz.Price and cuz.ZONETYPE='M')	and cr.CreativeKey = CC.creativeKey and CC.CampaignKey =?;");
				ps5.setInt(1, campaignKey);
				rs5= ps5.executeQuery();
				if(rs5.next()){
					searchTerm = searchTerm +"(mzid!="+rs5.getString(1)+")";	
				}
			}
			searchTerm = searchTerm+"))";
			
			searchTerm = searchTerm +")";
		}
		searchTerm = searchTerm +"))";
		
		//update each campaign with the updated searchterm
		PreparedStatement sczps = conn.prepareStatement("select ZoneSearchTerm from CampaignZoneTerm where CampaignKey=? and TimeStamp = (select max(TimeStamp) from CampaignZoneTerm where CampaignKey=?)");
		sczps.setInt(1, campaignKey);
		sczps.setInt(2, campaignKey);
		ResultSet rscz = sczps.executeQuery();
		String zoneSearchTerm ="";
		if(rscz.next()){
			zoneSearchTerm = rscz.getString(1);
		}
		if(null !=zoneSearchTerm && !zoneSearchTerm.equals(""))
			oldsearchTerm = oldsearchTerm.replace(zoneSearchTerm, "");
		ucps.setString(1, oldsearchTerm+searchTerm);
		ucps.setInt(2, campaignKey);
		ucps.addBatch();
		
		iczps.setInt(1, campaignKey);
		iczps.setString(2,searchTerm);
		iczps.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
		iczps.addBatch();
	}


	private static Connection getConnection(String[] args) throws SQLException, NamingException {

		/*Context ctx = new InitialContext();
		if (ctx == null)
			throw new NamingException("No Context found");

		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AdServer");
		Connection conn = null;
		if (ds != null) {
		conn = ds.getConnection();
	}*/

		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			if(null !=args && args.length>4){
				System.out.println("Arguments Size:"+args.length);
				conn = DriverManager.getConnection("jdbc:mysql://"+args[0]+":"+args[1]+"/"+args[2],args[3], args[4]);
			}else{
				System.out.println("Arguments Size:0");
				conn = DriverManager.getConnection("jdbc:mysql://nyc1stam01.nyc1.freshdirect.com:3306/OAS","fdadmin", "fd8848admin");
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return conn;
	}
}
