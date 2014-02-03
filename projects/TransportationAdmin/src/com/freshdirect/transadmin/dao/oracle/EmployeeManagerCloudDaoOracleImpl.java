package com.freshdirect.transadmin.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.KronosEmployeeInfo;

public class EmployeeManagerCloudDaoOracleImpl implements EmployeeManagerDaoI {

	private JdbcTemplate jdbcTemplate;

	private final static Category LOGGER = LoggerFactory.getInstance(EmployeeManagerCloudDaoOracleImpl.class);

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

/*	private static final String GET_ALL_ACTIVE_EMPLOYEE_QRY =
			"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
			"a.HOMELABORLEVELNM7 JOB_TYPE, a.SENIORITYRANKDATE SENIORITYRANK_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
			"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
			" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
			"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
			"AND a.EMPLOYMENTSTATUS='Active'";

		private static final String GET_ALL_SUPERVISOR_EMPLOYEE_QRY =
			"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
			"a.HOMELABORLEVELNM7 JOB_TYPE, a.SENIORITYRANKDATE SENIORITYRANK_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
			"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
			" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
			"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
			"AND (a.HOMELABORLEVELNM7 = 'MANAGER' OR a.HOMELABORLEVELNM7 = 'SUPERVISOR') "+
			"AND a.EMPLOYMENTSTATUS='Active'";


		private static final String GET_ALL_TERMINATED_EMPLOYEE_QRY =
			"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
			"a.HOMELABORLEVELNM7 JOB_TYPE, a.SENIORITYRANKDATE SENIORITYRANK_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
			"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME, a.EMPLOYMENTSTATUSDT TERMINATION_DATE "+
			" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
			"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
			"AND a.EMPLOYMENTSTATUS='Terminated'";


		private static final String GET_ALL_ACTIVE_INACTIVE_EMPLOYEE_QRY =
			"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
			"a.HOMELABORLEVELNM7 JOB_TYPE, a.SENIORITYRANKDATE SENIORITYRANK_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
			"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
			" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
			"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
			"AND ( a.EMPLOYMENTSTATUS='Active' or a.EMPLOYMENTSTATUS='Inactive')";
*/
		
	private static final String GET_ALL_EMPLOYEE_QRY =
	"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
	"a.HOMELABORLEVELNM7 JOB_TYPE, a.SENIORITYRANKDATE SENIORITYRANK_DATE, a.EMPLOYMENTSTATUS STATUS, a.SUPERVISORNUM SUP_KRONOS_ID, a.EMPLOYMENTSTATUSDT TERMINATION_DATE "+
	"from dbo.combhomeacct HA, dbo.laboracct LA1, dbo.vp_employeev42 a "+
	"where LA1.LABORACCTID = HA.LABORACCTID "+
	"and HA.employeeid = a.employeeid "+
	"and LA1.LABORLEV5NM in ('10001', '10002', '10003', '10004', '10005', '10006', '10007', '10008', '10009', '10012', '10101', '10102', '10103', '10104', '10105','10014','10015','10016','10017','10018','10020') "+
	"and getdate() between ha.effectivedtm and ha.expirationdtm";
	
	private static final String GET_KRONOS_EMPLOYEE_QRY =
	"SELECT a.* " +
	"from dbo.combhomeacct HA, dbo.laboracct LA1, dbo.vp_employeev42 a "+
	"where LA1.LABORACCTID = HA.LABORACCTID "+
	"and HA.employeeid = a.employeeid "+
	"and LA1.LABORLEV5NM in ('10001', '10002', '10003', '10004', '10005', '10006', '10007', '10008', '10009', '10012', '10101', '10102', '10103', '10104', '10105','10014','10015','10016','10017','10018','10020') "+
	"and getdate() between ha.effectivedtm and ha.expirationdtm";
	
	public List<EmployeeInfo> getAllEmployees() {
		// TODO Auto-generated method stub
//		System.out.println("EmployeeManagerDaoOracleImpl :getEmployee()11 ");
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
        final List<EmployeeInfo> list = new ArrayList<EmployeeInfo>();
        final Map<String, EmployeeInfo> empMap = new HashMap<String, EmployeeInfo>();
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_EMPLOYEE_QRY);
                return ps;
            }
        };
        jdbcTemplate.query(creator,
       		  new RowCallbackHandler() {
       		      public void processRow(ResultSet rs) throws SQLException {

       		    	do {
       		    		String employeeId = rs.getString("KRONOS_ID");
						String firstName = rs.getString("FIRST_NAME");
						String lastName = rs.getString("LAST_NAME");
						String middleInitial = rs.getString("MIDDLE_INITIAL");
						String shortName = rs.getString("SHORT_NAME");
						String jobType = rs.getString("JOB_TYPE");
						Date hireDate = rs.getDate("SENIORITYRANK_DATE");
						String status = rs.getString("STATUS");
						String supervisorId = rs.getString("SUP_KRONOS_ID");
						Date terminationDate = rs.getDate("TERMINATION_DATE");
						/*String supervisorFirstName = rs.getString("SUP_FIRST_NAME");
						String supervisorMiddleInitial = rs.getString("SUP_MIDDLE_INITIAL");
						String supervisorLastName = rs.getString("SUP_LAST_NAME");
						String supervisorShortName = rs.getString("SUP_SHORT_NAME");*/

       		    		EmployeeInfo model = new EmployeeInfo(employeeId,
																	firstName, lastName, middleInitial, shortName,
																	jobType, hireDate, status, supervisorId,/*
																	supervisorFirstName, supervisorMiddleInitial,
																	supervisorLastName, supervisorShortName,*/ terminationDate);
       		    		empMap.put(employeeId, model);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        
        for(EmployeeInfo e1 : list){
        	if(!"Terminated".equals(e1.getStatus()))
        		e1.setTerminationDate(null);
        	
        	if(e1.getSupervisorId()!=null && empMap.containsKey(e1.getSupervisorId())){
        		if(empMap.get(e1.getSupervisorId())!=null){
        			e1.setSupervisorFirstName(empMap.get(e1.getSupervisorId()).getFirstName());
        			e1.setSupervisorLastName(empMap.get(e1.getSupervisorId()).getLastName());
        			e1.setSupervisorMiddleInitial(empMap.get(e1.getSupervisorId()).getMiddleInitial());
        			e1.setSupervisorShortName(empMap.get(e1.getSupervisorId()).getSupervisorShortName());
        		}
        	}
        	
        }
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getAllEmployees list  "+list.size());
        return list;

	}
	
	public Collection getEmployees() {
		List<EmployeeInfo> list = getAllEmployees();
		Iterator<EmployeeInfo> itr = list.iterator();
		while(itr.hasNext()){
			EmployeeInfo e = itr.next();
			if(!"Active".equals(e.getStatus()))
				itr.remove();
		}
		LOGGER.debug("EmployeeManagerCloudDaoOracleImpl : getEmployees list  "+list.size());
		return list;
		
	}

	public Collection getSupervisors() {
		List<EmployeeInfo> list = getAllEmployees();
		Iterator<EmployeeInfo> itr = list.iterator();
		while(itr.hasNext()){
			EmployeeInfo e = itr.next();
			if(!("Active".equals(e.getStatus()) && ("MANAGER".equals(e.getJobType()) || "SUPERVISOR".equals(e.getJobType()))))
				itr.remove();
		}
        LOGGER.debug("EmployeeManagerCloudDaoOracleImpl : getSupervisor list  "+list.size());
        return list;

	}


	public Map<String, EmployeeInfo> getTerminatedEmployees() throws DataAccessException {
		
        Map<String, EmployeeInfo> empMap = new HashMap<String, EmployeeInfo>();
        
		List<EmployeeInfo> list = getAllEmployees();
		Iterator<EmployeeInfo> itr = list.iterator();
		while(itr.hasNext()){
			EmployeeInfo e = itr.next();
			if(!"Terminated".equals(e.getStatus()))
				itr.remove();
		}
		for(EmployeeInfo e : list){
			empMap.put(e.getEmployeeId(), e);
		}
		LOGGER.debug("EmployeeManagerCloudDaoOracleImpl : getTerminatedEmployee list  "+empMap .values().size());
        return empMap;
	}

	
	public Collection getActiveInactiveEmployees() {

		List<EmployeeInfo> list = getAllEmployees();
		Iterator<EmployeeInfo> itr = list.iterator();
		while(itr.hasNext()){
			EmployeeInfo e = itr.next();
			if(!("Inactive".equals(e.getStatus()) || "Active".equals(e.getStatus())))
				itr.remove();
		}
		
        LOGGER.debug("EmployeeManagerCloudDaoOracleImpl : getActiveInactiveEmployees list  "+list.size());
        return list;

        
	}

	@Override
	public void refresh(String worktable) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}
	

	
	public List<KronosEmployeeInfo> getAllKronosEmployees() {
     final List<KronosEmployeeInfo> kronosemplist = new ArrayList<KronosEmployeeInfo>();
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_KRONOS_EMPLOYEE_QRY);
                return ps;
            }
        };
        jdbcTemplate.query(creator,
       		  new RowCallbackHandler() {
       		      public void processRow(ResultSet rs) throws SQLException {

       		    	do {
       		    		KronosEmployeeInfo model = KronosEmployeeInfo.build(rs);
       		    		kronosemplist.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        
        return kronosemplist;

	}

	@Override
	public void syncEmployees(List<KronosEmployeeInfo> inserts,
			List<KronosEmployeeInfo> updates, List<KronosEmployeeInfo> deletes)
			throws SQLException {
		
		return;
	}
	
}
