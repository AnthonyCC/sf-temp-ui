package com.freshdirect.transadmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.ZoneManagerDaoI;

public class ZoneManagerDaoOracleImpl implements ZoneManagerDaoI {
	
	private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ZoneManagerDaoOracleImpl.class);
	
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

}
