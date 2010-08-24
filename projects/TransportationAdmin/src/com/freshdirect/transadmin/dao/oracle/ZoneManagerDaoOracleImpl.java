package com.freshdirect.transadmin.dao.oracle;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.ZoneManagerDaoI;
import com.freshdirect.transadmin.web.model.TimeRange;

public class ZoneManagerDaoOracleImpl implements ZoneManagerDaoI {
	
	private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ZoneManagerDaoOracleImpl.class);
	
	/* UseFull Queries
			select * from dlv.GEO_RESTRICTION_BOUNDARY gr  
    		where sdo_nn(gr.geoloc, SDO_GEOMETRY(2001, 8265, SDO_POINT_TYPE(-73.9591497, 40.7716796, NULL), NULL, NULL),'sdo_num_res=4') = 'TRUE'
	
    		select * from dlv.GEO_RESTRICTION_BOUNDARY gr  
    		where sdo_within_distance(gr.geoloc, SDO_GEOMETRY(2001, 8265, SDO_POINT_TYPE(-73.9591497, 40.7716796, NULL), NULL, NULL),'distance=.5 unit=mile') = 'TRUE'
    	
    		select distinct z.zone_code ZONE_CODE from dlv.region r, dlv.region_data rd, dlv.zone z 
            where r.id = rd.region_id 
            and rd.id = z.region_data_id 
            and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) 
            or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id))
			and sdo_nn(z.geoloc, SDO_GEOMETRY(2001, 8265, SDO_POINT_TYPE(-73.9591497, 40.7716796, NULL), NULL, NULL),'sdo_num_res=4') = 'TRUE'  
            order by z.zone_code
            
            select distinct z.zone_code ZONE_CODE from dlv.region r, dlv.region_data rd, dlv.zone z 
            where r.id = rd.region_id 
            and rd.id = z.region_data_id 
            and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) 
            or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id))
			and sdo_within_distance(z.geoloc, SDO_GEOMETRY(2001, 8265, SDO_POINT_TYPE(-73.9591497, 40.7716796, NULL), NULL, NULL),'distance=.5 unit=mile') = 'TRUE'	  
            order by z.zone_code
            
    	*/			
	
	  

	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	private static final String GET_ALL_ACTIVE_ZONECODES_QRY =
		"select distinct z.zone_code ZONE_CODE from dlv.region r, dlv.region_data rd, dlv.zone z "+
            "where r.id = rd.region_id "+
            "and rd.id = z.region_data_id "+
            "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) "+
            "or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id)) "+
            "order by z.zone_code";
	
	public Collection getActiveZoneCodes() throws DataAccessException {
		final List result = new ArrayList();		         
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_ACTIVE_ZONECODES_QRY);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {       		    		
       		    		result.add(rs.getString("ZONE_CODE"));
       		    	}  while(rs.next());
       		      }
       		   });
        return result;
	}
	
	private static final String GET_ALL_ACTIVE_ZONECODES_QRY_DATE =
		"select distinct z.zone_code ZONE_CODE from dlv.region r, dlv.region_data rd, dlv.zone z "+
            "where r.id = rd.region_id "+
            "and rd.id = z.region_data_id "+
            "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id) "+
            "or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) "+
            "order by z.zone_code";
	
	public Collection getActiveZoneCodes(final String date) throws DataAccessException {
		final List result = new ArrayList();		         
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_ACTIVE_ZONECODES_QRY_DATE);
                ps.setString(1,date);
                ps.setString(2,date);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {       		    		
       		    		result.add(rs.getString("ZONE_CODE"));
       		    	}  while(rs.next());
       		      }
       		   });
        return result;
	}	
	
	private static final String GET_ALL_WINDOWSTEERING_DISCOUNTS =
		"select p.id PID, P.MAX_AMOUNT PAMOUNT, P.OFFER_TYPE, PDZ.DLV_ZONE ZONES, PTS.DAY_ID DAY_OF_WEEK, PTS.START_TIME STIME, PTS.END_TIME ETIME " +
		"from CUST.PROMOTION_NEW p, CUST.PROMO_DLV_ZONE_STRATEGY pdz, CUST.PROMO_DLV_TIMESLOT pts " +
		"where p.campaign_code='HEADER' and p.offer_type = 'WINDOW_STEERING' and p.status='LIVE' and P.EXPIRATION_DATE > ? and to_char(?,'D') = PTS.DAY_ID " +
		"and PDZ.PROMOTION_ID = p.id and PTS.PROMO_DLV_ZONE_ID = PDZ.ID ";
	
	public Map<String, List<TimeRange>> getWindowSteeringDiscounts(final Date deliveryDate) throws DataAccessException {
		
		final Map<String, List<TimeRange>> result = new HashMap<String, List<TimeRange>>();
		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_WINDOWSTEERING_DISCOUNTS);  
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do { 
       		    		Array array = rs.getArray("ZONES");
       		    		
       		    		TimeRange capacity = new TimeRange();
       		    		capacity.setStartTime(new TimeOfDay(rs.getString("STIME")));
       		    		capacity.setEndTime(new TimeOfDay(rs.getString("ETIME")));
       		    		
       		    		if(array != null) {
       		    			String[] zoneCodes = (String[])array.getArray();
       		    			if(zoneCodes != null) {
       		    				for(String zone : zoneCodes) {
       		    					if(!result.containsKey(zone)) {
       		    						result.put(zone, new ArrayList<TimeRange>());
       		    					}
       		    					result.get(zone).add(capacity);
       		    				}
       		    			}
       		    		}
       		    	}  while(rs.next());
       		      }
       		   });
        
        return result;
	}	

}
