package com.freshdirect.transadmin.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.PunchInfoDaoI;
import com.freshdirect.transadmin.dao.oracle.EmployeeManagerDaoOracleImpl;
import com.freshdirect.transadmin.model.PunchInfo;

public class PunchInfoDaoImpl implements PunchInfoDaoI {

	private JdbcTemplate jdbcTemplate;

	private final static Category LOGGER = LoggerFactory.getInstance(EmployeeManagerDaoOracleImpl.class);

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	public Collection getPunchInfo(final String date) throws DataAccessException {
		
		 final List list = new ArrayList();
	        PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                   // connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM dbo.VP_TIMESHTPUNCHV42 where eventdate=( ?) ");
	                	// connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM dbo.FDDW_TIMESHTPUNCHV42 where eventdate=( ?) "); 
	                	 connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM TRANSP.PUNCHINFO where eventdate=( ?) and PAYCODENAME is  null");
	                ps.setString(1, date);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
	       		  new RowCallbackHandler() {
	       		      public void processRow(ResultSet rs) throws SQLException {

	       		    	while(rs.next()) {
	       		    		String employeeId=rs.getString("PERSONNUM");
	       		    		Date eventDate=rs.getDate("EVENTDATE");
	       		    		Date startDTM=rs.getTimestamp("STARTDTM");
	       		    		Date endDTM=rs.getTimestamp("ENDDTM");
	       		    		Date inPunchDTM=rs.getTimestamp("INPUNCHDTM");
	       		    		Date outPunchDTM=rs.getTimestamp("OUTPUNCHDTM");
	       		    		list.add(new PunchInfo(eventDate,employeeId,startDTM,endDTM,inPunchDTM,outPunchDTM));
	       		    	}
	       		    	  
	       		      }
	       		  }
	       	);
	        LOGGER.debug("PunchInfoDaoImpl.getPunchInfo() Total punched:"+list.size());
	        return list;
	}
	
	public Collection getScheduleInfo(final String date) throws DataAccessException {
		
		 final List list = new ArrayList(); 
	        PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                   // connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM dbo.VP_TIMESHTPUNCHV42 where eventdate=( ?) ");
	                	// connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM dbo.FDDW_TIMESHTPUNCHV42 where eventdate=( ?) "); 
	                	 connection.prepareStatement("SELECT PERSONNUM,trunc(shiftstartdate) shiftstartdate ,shiftstarttime,shiftendtime FROM TRANSP.SCHEDULEINFO where trunc(shiftstartdate)=to_date(?,'dd-mm-yyyy') " +
	                	 		" and homelaborlevelname5 in ('10004','10005','10006','10007','10008','10009')");
	                ps.setString(1, date);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
	       		  new RowCallbackHandler() {
	       		      public void processRow(ResultSet rs) throws SQLException {

	       		    	while(rs.next()) {
	       		    		String employeeId=rs.getString("PERSONNUM");
	       		    		Date eventDate=rs.getDate("shiftstartdate");
	       		    		Date startDTM=rs.getTimestamp("shiftstarttime");
	       		    		Date endDTM=rs.getTimestamp("shiftendtime");	       		    		
	       		    		list.add(new PunchInfo(eventDate,employeeId,startDTM,endDTM,null,null));
	       		    	}
	       		    	  
	       		      }
	       		  }
	       	);
	        LOGGER.debug("PunchInfoDaoImpl.getPunchInfo() Total punched:"+list.size());
	        return list;
	}
	
	public Collection getPunchInfoPayCode(final String date) throws DataAccessException {
		
		 final List list = new ArrayList();
	        PreparedStatementCreator creator=new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps =
	                   // connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM dbo.VP_TIMESHTPUNCHV42 where eventdate=( ?) ");
	                	// connection.prepareStatement("SELECT PERSONNUM,EVENTDATE,STARTDTM,ENDDTM,INPUNCHDTM,OUTPUNCHDTM FROM dbo.FDDW_TIMESHTPUNCHV42 where eventdate=( ?) "); 
	                	 connection.prepareStatement("SELECT personnum,trunc(shiftstartdate) ,paycodename FROM TRANSP.SCHEDULEINFO where trunc(shiftstartdate)=to_date(?,'dd-mm-yyyy') and PAYCODENAME in ('PTO','advPTO','PERSONAL','SICK','VACATION','WORKCOMP')" +
	                	 		" and homelaborlevelname5 in ('10004','10005','10006','10007','10008','10009')");
	                ps.setString(1, date);
	                return ps;
	            }
	        };
	        jdbcTemplate.query(creator,
	       		  new RowCallbackHandler() {
	       		      public void processRow(ResultSet rs) throws SQLException {

	       		    	while(rs.next()) {
	       		    		String employeeId=rs.getString("PERSONNUM");
	       		    		Date eventDate=rs.getDate("shiftstartdate");	  
	       		    		String paycode=rs.getString("PAYCODENAME");
	       		    		PunchInfo p=new PunchInfo(eventDate,employeeId,null,null,null,null);
	       		    		p.setPaycode(paycode);
	       		    		list.add(p);
	       		    	}
	       		    	  
	       		      }
	       		  }
	       	);
	        LOGGER.debug("PunchInfoDaoImpl.getPunchInfoPayCode() Total paycode:"+list.size());
	        return list;
	}
	
}

