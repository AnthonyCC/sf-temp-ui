package com.freshdirect.transadmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.SpatialManagerDaoI;
import com.freshdirect.transadmin.web.model.SpatialBoundary;
import com.freshdirect.transadmin.web.model.SpatialPoint;

public class SpatialManagerDaoOracleImpl implements SpatialManagerDaoI {
	
private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(SpatialManagerDaoOracleImpl.class);
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	private static final String GET_GEOREST_GEOMENTRICBOUNDARY_QRY = "select gr.CODE,gr.NAME, gg.column_value from dlv.GEO_RESTRICTION_BOUNDARY gr , table(gr.geoloc.sdo_ordinates) gg where gr.CODE = ?";
	
	private static final String GET_ZONE_GEOMENTRICBOUNDARY_QRY =
						"select z.zone_code, z.name,gg.column_value	from dlv.region r, dlv.region_data rd, dlv.zone z, transp.zone  zd , table(z.geoloc.sdo_ordinates) gg	" +
						"where zd.zone_code = z.zone_code and rd.id = z.region_data_id and rd.region_id = r.id and z.zone_code = ? " +
						"and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id=r.id)";
	
		/*"select z.zone_code, z.name,gg.column_value  from dlv.region r, dlv.region_data rd, dlv.zone z, table(z.geoloc.sdo_ordinates) gg "+
            "where r.id = rd.region_id "+
            "and rd.id = z.region_data_id "+
            "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) "+
            "or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id)) "+
            "and z.zone_code = ?";*/
	
	public SpatialBoundary getGeoRestrictionBoundary(final String code) throws DataAccessException  {
		
		return getSpatialBoundary(code, GET_GEOREST_GEOMENTRICBOUNDARY_QRY);
	}
	
	public SpatialBoundary getZoneBoundary(final String code) throws DataAccessException  {
		
		return getSpatialBoundary(code, GET_ZONE_GEOMENTRICBOUNDARY_QRY);
	}
	
	private SpatialBoundary getSpatialBoundary(final String code, final String query) throws DataAccessException   {
		final SpatialBoundary result = new SpatialBoundary();
		final List geoloc = new ArrayList();
		result.setGeoloc(geoloc);
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(query);
                ps.setString(1, code);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	do {
       		    		SpatialPoint point = new SpatialPoint();
       		    		point.setX(rs.getDouble(3));
       		    		rs.next();
       		    		point.setY(rs.getDouble(3));
       		    		geoloc.add(point);
       		    		result.setCode(rs.getString(1));
       		    		result.setName(rs.getString(2));
       		    	 } while(rs.next());
       		    	
       		      }
       		   });
        System.out.println("result>"+result);
		return result;
	}
	
	
}
