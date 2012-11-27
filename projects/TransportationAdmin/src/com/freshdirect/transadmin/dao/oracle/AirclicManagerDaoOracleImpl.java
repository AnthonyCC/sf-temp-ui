package com.freshdirect.transadmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.AirclicManagerDaoI;
import com.freshdirect.transadmin.model.EmployeeTimesheetInfo;
import com.freshdirect.transadmin.util.TransStringUtil;

public class AirclicManagerDaoOracleImpl implements AirclicManagerDaoI{
	
	private JdbcTemplate jdbcTemplate;
	
	private final static Category LOGGER = LoggerFactory.getInstance(AirclicManagerDaoOracleImpl.class);
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	/*private static String EMPLOYEES_BY_ZONE = 
			"SELECT * FROM AC_STAGETRAIN.RUNNERTRACKING@AIRCLICRW.NYC.FRESHDIRECT.COM RT  WHERE RT.RUNNERBADGEID = '661945' AND ACTION = 'Clock Out'";
	*/		
	private static String EMPLOYEES_BY_ZONE = "SELECT * FROM TRANSP.EMP_TIMESHEET ET WHERE " +
			" TO_CHAR(ET.INSERT_TIMESTAMP, 'MM/DD/YYYY') = TO_CHAR(?, 'MM/DD/YYYY') AND SUBSTR(ROUTE,2,3) = ?";
	
	@Override
	public Collection getEmployees(final Date date, final String zone) {
		final Set result=new HashSet();
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
	                PreparedStatement ps =
	                    connection.prepareStatement(EMPLOYEES_BY_ZONE);
	                ps.setDate(1, new java.sql.Date(date.getTime()));
	                ps.setString(2, zone);
	                
	                
	                return ps;
	            }  
	    };
	    
	    jdbcTemplate.query(creator,
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException{
						do{
							result.add(new EmployeeTimesheetInfo(rs.getString("RUNNERBADGEID"), rs.getString("CLOCK_IN_TIME"), rs.getString("CLOCK_OUT_TIME"),
									rs.getString("TIP"),rs.getString("BREAK")));
						}while(rs.next());
					}
				});
	    
		return result;
	}
	
	
	public Set getSignature(final Date date, final String zone, final String empId) {
		
		final Set result=new HashSet();
		final StringBuffer strBuf = new StringBuffer();
		strBuf.append(EMPLOYEES_BY_ZONE).append(" AND ET.RUNNERBADGEID = ?");
		
		PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(strBuf.toString());
                	ps.setDate(1, new java.sql.Date(date.getTime()));
                	ps.setString(2, zone);
                	ps.setString(3, empId);
                		
                return ps;
            }  
    };
    
    jdbcTemplate.query(creator,
			new RowCallbackHandler(){
				public void processRow(ResultSet rs) throws SQLException{
					do{
						result.add(rs.getBytes("signature"));
					}while(rs.next());
				}
			});
    
		return result;
	}
	
}