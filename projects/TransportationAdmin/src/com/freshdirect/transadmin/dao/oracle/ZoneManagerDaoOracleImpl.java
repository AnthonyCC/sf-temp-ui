package com.freshdirect.transadmin.dao.oracle;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.transadmin.dao.ZoneManagerDaoI;
import com.freshdirect.transadmin.model.Sector;
import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.model.ZipCodeModel;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.CustomTimeOfDay;
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
		"SELECT p.id PID, "+
		       "P.MAX_AMOUNT PAMOUNT, "+
		       "P.OFFER_TYPE, "+
		       "z.DLV_ZONE ZONES, "+
		       "t.DAY_ID DAY_OF_WEEK, "+
		       "t.START_TIME STIME, "+
		       "t.END_TIME ETIME, "+
		       "t.DLV_WINDOWTYPE WINDOWTYPE, d.START_DATE "+
		  "FROM CUST.PROMOTION_NEW p, "+
		       "CUST.PROMO_DLV_ZONE_STRATEGY z, "+
		       "CUST.PROMO_DLV_TIMESLOT t, "+
		       "CUST.PROMO_DELIVERY_DATES d "+
		 "WHERE     P.ID = Z.PROMOTION_ID "+
		       "AND T.PROMO_DLV_ZONE_ID = Z.ID "+
		       "AND P.ID = D.PROMOTION_ID(+) "+
		       "AND p.campaign_code = 'HEADER' "+
		       "AND p.offer_type = 'WINDOW_STEERING' "+
		       "AND p.status = 'LIVE' "+
		       "AND P.EXPIRATION_DATE >= SYSDATE and P.EXPIRATION_DATE >= ? "+
		       "AND to_char(?,'D') = T.DAY_ID  "+
		       "AND (( d.START_DATE is NULL AND d.END_DATE is NULL) OR (d.START_DATE <= ? and d.END_DATE >= ?))";
	
	public Map<String, List<TimeRange>> getWindowSteeringDiscounts(final Date deliveryDate) throws DataAccessException {
		
		final Map<String, List<TimeRange>> result = new HashMap<String, List<TimeRange>>();
		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_WINDOWSTEERING_DISCOUNTS);  
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
                ps.setDate(3, new java.sql.Date(deliveryDate.getTime())); 
                ps.setDate(4, new java.sql.Date(deliveryDate.getTime()));
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do { 
       		    		Array array = rs.getArray("ZONES");
       		    		Array windowTypeArray = rs.getArray("WINDOWTYPE");
       		    		
       		    		TimeRange capacity = new TimeRange();
       		    		if(rs.getString("STIME") != null)
       		    			capacity.setStartTime(new CustomTimeOfDay(rs.getString("STIME")));
       		    		if(rs.getString("ETIME") != null)
       		    			capacity.setEndTime(new CustomTimeOfDay(rs.getString("ETIME")));
       		    		
       		    		if(windowTypeArray != null){
       		    			String[] windowTypeDuration = (String[]) windowTypeArray.getArray();
       		    			if(windowTypeDuration != null) {
       		    				capacity.setWindowType(windowTypeDuration);
       		    			}
       		    		}
       		    		
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
	
	private static final String GET_DEFAULT_ZONE_SUPERVISOR =
		"select day_part,supervisor_id,effective_date,zone_code from transp.zone_supervisor where effective_date=(select max(EFFECTIVE_DATE)as EFFECTIVE_DATE "+
		"from transp.zone_supervisor where zone_code = ? and day_part=? and EFFECTIVE_DATE <= TO_DATE(?, 'mm/dd/yyyy')) and zone_code=? and day_part=?";
	
	public Collection getDefaultZoneSupervisor(final String zoneCode, final String dayPart, final String date) throws DataAccessException {
		final List result = new ArrayList();		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_DEFAULT_ZONE_SUPERVISOR);
                ps.setString(1,zoneCode);
                if(dayPart!=null)ps.setString(2,dayPart);
                ps.setString(3,date);
                ps.setString(4,zoneCode);
                if(dayPart!=null)ps.setString(5,dayPart);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	ZoneSupervisor zs;
       		    	do {       		 
       		    		zs = new ZoneSupervisor(rs.getString("day_part"),rs.getString("supervisor_id"),rs.getDate("effective_date"),rs.getString("zone_code"));
       		    		result.add(zs);
       		    	}  while(rs.next());
       		      }
       		   });
        return result;
	}
	
	private static final String DELIVERABLE_ZIPS = 
		"select zipcode, home_coverage, cos_coverage,ebt_accepted from dlv.zipcode order by zipcode asc";
	
	private static final String GET_ZIPCODEINFO_NAVTECH = "select l_postcode ZIP_CODE, sdo_aggr_convexhull(MDSYS.SDOAGGRTYPE(geoloc, 0.5)) "+ 
		" FROM dlv.navtech_geocode n where l_postcode = ? or n.r_postcode = ? group by n.l_postcode ";

	//insert zip code boundary from navtech
	private static final String INSERT_ZIPCODEINFO_ZIPWORKTAB = "insert into dlv.zipcode_worktab(zipcode, geoloc) "+
    	" select bx.x, bx.y from ( select n.l_postcode x, sdo_aggr_convexhull(MDSYS.SDOAGGRTYPE(geoloc, 0.5)) y "+ 
            " FROM dlv.navtech_geocode n where l_postcode  = ? or n.r_postcode = ? group by n.l_postcode) bx where x = ?";
	
	//insert zip code boundary from zipcode_worktab
	private static final String INSERT_ZIPCODE_COVERAGE = "insert into dlv.zipcode(zipcode, geoloc) VALUES "+  
		" (?, (select geoloc FROM dlv.zipcode_worktab where zipcode = ?))";
	
	//insert zip code coverage
	private static final String UPDATE_ZIPCODE_COVERAGE = "update dlv.zipcode z set z.geoloc = (select geoloc FROM dlv.zipcode_worktab where zipcode = ?), z.home_coverage = ?, z.cos_coverage = ?,z.ebt_accepted = ? "+ 
			" where zipcode = ? ";
	
	private static final String GET_ZIPCODEINFO = "select * FROM dlv.zipcode where zipcode = ? ";
	private static final String GET_ZIPCODEINFO_WORKTAB = "select * FROM dlv.zipcode_worktab where zipcode = ? ";
	
	private static final String GET_ZIPCODE = "select * FROM dlv.zipcode where zipcode = ? ";
	
	public Set getDeliverableZipCodes() throws DataAccessException {
		final Set<ZipCodeModel> result = new HashSet<ZipCodeModel>();		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(DELIVERABLE_ZIPS);	               
	                return ps;
	            }  
	     };
	     
	     jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	ZipCodeModel model;
	       		    	do {       		 
	       		    		model = new ZipCodeModel(rs.getString("zipcode"),rs.getDouble("home_coverage"),rs.getDouble("cos_coverage"),rs.getString("ebt_accepted"));
	       		    		result.add(model);
	       		    	}  while(rs.next());
	       		      }
	       		   });
		
		return result;
	}
	

	public boolean checkNavTechZipCodeInfo(final String zipCode) throws DataAccessException {
		final Set<String> result = new HashSet<String>();		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_ZIPCODEINFO_NAVTECH);	               
	                ps.setString(1, zipCode);
	                ps.setString(2, zipCode);	             
	                return ps;
	            }  
	     };
	     
	     jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	do {       		 
	       		    		result.add(rs.getString("ZIP_CODE"));
	       		    	}  while(rs.next());
	       		      }
	       		   });
		
		return result.size() > 0 ? true : false;
	}
	
	public boolean checkZipCodeInfo(final String zipCode) throws DataAccessException {
		final Set<String> result = new HashSet<String>();		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_ZIPCODEINFO);	               
	                ps.setString(1, zipCode);	               
	                return ps;
	            }  
	     };
	     
	     jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	do {       		 
	       		    		result.add(rs.getString("ZIPCODE"));
	       		    	}  while(rs.next());
	       		      }
	       		   });
		
		return result.size() > 0 ? true : false;
	}
	
	public boolean checkWorkTabZipCodeInfo(final String zipCode) throws DataAccessException {
		final Set<String> result = new HashSet<String>();		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_ZIPCODEINFO_WORKTAB);	               
	                ps.setString(1, zipCode);	               
	                return ps;
	            }  
	     };
	     
	     jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	do {       		 
	       		    		result.add(rs.getString("ZIPCODE"));
	       		    	}  while(rs.next());
	       		      }
	       		   });
		
		return result.size() > 0 ? true : false;
	}
	

	public void addNewDeliveryZipCode(ZipCodeModel model) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_ZIPCODEINFO_ZIPWORKTAB);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			if(model != null) {
				
				batchUpdater.update(new Object[]{ model.getZipCode()
								, model.getZipCode()
								, model.getZipCode()
						
				});
			}			
			batchUpdater.flush();
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void addNewDeliveryZipCodeCoverage(ZipCodeModel model) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_ZIPCODE_COVERAGE);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			if(model != null) {
				
				batchUpdater.update(new Object[]{ model.getZipCode()
									, model.getZipCode()
						
				});
			}			
			batchUpdater.flush();
		} finally {
			if(connection != null) connection.close();
		}
	}
	
	public void updateDeliveryZipCodeCoverage(ZipCodeModel model) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_ZIPCODE_COVERAGE);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.DOUBLE));
			batchUpdater.declareParameter(new SqlParameter(Types.DOUBLE));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			if(model != null) {
				
				batchUpdater.update(new Object[]{ model.getZipCode()
								, model.getHomeCoverage()
								, model.getCosCoverage()
								, model.getEbtAccepted()
								, model.getZipCode()
								
				});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public Set<ZipCodeModel> getZipCodeInfo(final String zipCode) throws DataAccessException {
		final Set<ZipCodeModel> result = new HashSet<ZipCodeModel>();		
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(GET_ZIPCODE);
	                ps.setString(1, zipCode);
	                return ps;
	            }  
	     };
	     
	     jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {
	       		    	ZipCodeModel model;
	       		    	do {       		 
	       		    		model = new ZipCodeModel(rs.getString("zipcode"),rs.getDouble("home_coverage"),rs.getDouble("cos_coverage"),rs.getString("ebt_accepted"));
	       		    		result.add(model);
	       		    	}  while(rs.next());
	       		      }
	       		   });
		
		return result;
	}
	
	private static final String GET_SECTOR_ZIPCODEINFO = "select sz.zipcode zipcode, cs.STATE, cs.COUNTY, s.name, s.description, s.active "+
				" from dlv.zipplusfour zpf, dlv.city_state cs, transp.sector_zipcode sz, transp.sector s "+
				" where zpf.CITY_STATE_KEY = cs.CITY_STATE_KEY and sz.zipcode=zpf.zipcode and s.name = sz.sector_name ";
					
	public Map<String, SectorZipcode> getSectorZipCodeInfo(final String sectorName) throws DataAccessException {
		
		final Map<String, SectorZipcode> result = new HashMap<String, SectorZipcode>();		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_SECTOR_ZIPCODEINFO);
		if(sectorName != null && !"".equalsIgnoreCase(sectorName)){
			updateQ.append("and sz.sector_name = ? ");
		}		
		updateQ.append(" group by sz.zipcode, state, county, name, description, active order by county ");
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(updateQ.toString());	
	                if(sectorName != null && !"".equalsIgnoreCase(sectorName)) ps.setString(1, sectorName);
	                return ps;
	            }  
	     };
	     
	     jdbcTemplate.query(creator, 
	       		  new RowCallbackHandler() { 
	       		      public void processRow(ResultSet rs) throws SQLException {	       		    	
	       		    	do {       		 
	       		    		String _zipcode = rs.getString("zipcode");
	       		    		String _county = rs.getString("county");
	       		    		String _state = rs.getString("state");
	       		    		
	       		    		SectorZipcode model = new SectorZipcode();	       		    		
	       		    		Sector _nhood = new Sector();
	       		    		_nhood.setName(rs.getString("name"));
	       		    		_nhood.setDescription(rs.getString("description"));
	       		    		_nhood.setActive(rs.getString("active"));
	       		    		
	       		    		model.setZipcode(_zipcode);
	       		    		model.setCounty(_county);
	       		    		model.setState(_state);
	       		    		model.setSector(_nhood);
	       		    		result.put(model.getZipcode(), model);
	       		    	}  while(rs.next());
	       		      }
	       		   });
		
		return result;
	}

}
