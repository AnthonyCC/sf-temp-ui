package com.freshdirect.delivery.map.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import com.freshdirect.delivery.map.Layer;
import com.freshdirect.delivery.map.Polygon;
import com.freshdirect.framework.core.SessionBeanSupport;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

public class DlvMapperSessionBean extends SessionBeanSupport{
	
	private static final Category LOGGER = LoggerFactory.getInstance(DlvMapperSessionBean.class);
	
	private final static int X_SCALE = 10000;
	private final static int Y_SCALE = -10000;
	
	public List getMapLayersForRegion() {
		Connection conn = null;
		
		List layers = new ArrayList();
		try{
			conn = this.getConnection();
			layers.add( this.queryZips(conn) );
			layers.add( this.queryZones(conn) );
			
			return layers;
			
		}catch(SQLException se){
			throw new EJBException(se);
		}finally{
			try{
				if(conn != null){
					conn.close();
					conn = null;
				}
			}catch(SQLException se){
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}
	}
	
	private static String zipCoordQuery = "select zipcode, gg.column_value from dlv.zipcode z , table(z.geoloc.sdo_ordinates) gg"; 
	
	private Layer queryZips(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(zipCoordQuery);
			rs = ps.executeQuery();

			Layer polys = this.transform(rs, "zip");
			
			return polys;
		}catch(SQLException se){
			throw se;
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(SQLException se){
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}
	}
	
	private static String zoneCoordQuery = "SELECT z.NAME, gg.column_value FROM dlv.region r, dlv.region_data rd, dlv.zone z, table(z.geoloc.sdo_ordinates) gg "
		+"where r.id = rd.region_id and z.region_data_id = rd.id and r.home_delivery is not null "  
		+"AND rd.START_DATE = (select max(start_date) from dlv.region_data rd1 where rd1.region_id = r.id and rd1.start_date <= sysdate ) ";
	
	private Layer queryZones(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(zoneCoordQuery);
			rs = ps.executeQuery();

			Layer polys = this.transform(rs, "zone");
			return polys;
		}catch(SQLException se){
			throw se;
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps != null){
					ps.close();
					ps = null;
				}
			}catch(SQLException se){
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}
	}

	private Layer transform(ResultSet rs, String klazz) throws SQLException {
		String zip = "";
		List polygons = new ArrayList(); 

		List coords = null; 
		while (rs.next()) {
			String currentZip = rs.getString(1);
			if (!zip.equals( currentZip )) {
				if (coords!=null) {
					polygons.add( new Polygon(zip, coords) );	
				}
				zip = currentZip; 
				coords = new ArrayList();
			}
		
			int[] c = new int[2];
			c[0] = (int)Math.round( rs.getDouble(2) * X_SCALE );
			rs.next();
			c[1] = (int)Math.round( rs.getDouble(2) * Y_SCALE );
		
			coords.add(c);
		}

		polygons.add( new Polygon(zip, coords) );	
	
		return new Layer(polygons, klazz);
	}

}
