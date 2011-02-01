package com.freshdirect.transadmin.dao.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.util.TransStringUtil;

public class EmployeeManagerDaoOracleImpl implements EmployeeManagerDaoI {

	private JdbcTemplate jdbcTemplate;

	private final static Category LOGGER = LoggerFactory.getInstance(EmployeeManagerDaoOracleImpl.class);

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	private static final String GET_ALL_ACTIVE_EMPLOYEE_QRY=
		"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
		"a.HOMELABORLEVELNM7 JOB_TYPE, a.COMPANYHIREDTM HIRE_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
		"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
		" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
		"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
		"AND a.EMPLOYMENTSTATUS='Active'";

	private static final String GET_ALL_SUPERVISOR_EMPLOYEE_QRY=
		"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
		"a.HOMELABORLEVELNM7 JOB_TYPE, a.COMPANYHIREDTM HIRE_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
		"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
		" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
		"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
		"AND (a.HOMELABORLEVELNM7 = 'MANAGER' OR a.HOMELABORLEVELNM7 = 'SUPERVISOR') "+
		"AND a.EMPLOYMENTSTATUS='Active'";


	private static final String GET_ALL_TERMINATED_EMPLOYEE_QRY=
		"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
		"a.HOMELABORLEVELNM7 JOB_TYPE, a.COMPANYHIREDTM HIRE_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
		"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME, a.EMPLOYMENTSTATUSDT TERMINATION_DATE "+
		" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
		"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
		"AND a.EMPLOYMENTSTATUS='Terminated'";


	private static final String GET_ALL_ACTIVE_INACTIVE_EMPLOYEE_QRY=
		"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
		"a.HOMELABORLEVELNM7 JOB_TYPE, a.COMPANYHIREDTM HIRE_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
		"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
		" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
		"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
		"AND ( a.EMPLOYMENTSTATUS='Active' or a.EMPLOYMENTSTATUS='Inactive')";

	
	public Collection getEmployees() {
		// TODO Auto-generated method stub
//		System.out.println("EmployeeManagerDaoOracleImpl :getEmployee()11 ");
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
        final List list = new ArrayList();
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_ACTIVE_EMPLOYEE_QRY);
                return ps;
            }
        };
        jdbcTemplate.query(creator,
       		  new RowCallbackHandler() {
       		      public void processRow(ResultSet rs) throws SQLException {

       		    	do {
       		    		String employeeId=rs.getString("KRONOS_ID");
       		    		String firstName=rs.getString("FIRST_NAME");
       		    		String lastName=rs.getString("LAST_NAME");
       		    		String middleInitial=rs.getString("MIDDLE_INITIAL");
       		    		String shortName=rs.getString("SHORT_NAME");
       		    		String jobType=rs.getString("JOB_TYPE");
       		    		Date hireDate=rs.getDate("HIRE_DATE");
       		    		String status=rs.getString("STATUS");
       		    		String supervisorId=rs.getString("SUP_KRONOS_ID");
       		    		String supervisorFirstName=rs.getString("SUP_FIRST_NAME");
       		    		String supervisorMiddleInitial=rs.getString("SUP_MIDDLE_INITIAL");
       		    		String supervisorLastName=rs.getString("SUP_LAST_NAME");
       		    		String supervisorShortName=rs.getString("SUP_SHORT_NAME");

       		    		EmployeeInfo model=new EmployeeInfo(
       		    		employeeId,firstName,lastName,middleInitial,shortName,jobType,hireDate,
       		    		status,supervisorId,supervisorFirstName,supervisorMiddleInitial,supervisorLastName,supervisorShortName,null
       		    		);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getEmployee list  "+list.size());
        return list;

	}

	public Collection getSupervisors() {
		// TODO Auto-generated method stub
//		System.out.println("EmployeeManagerDaoOracleImpl :GET_ALL_SUPERVISOR_EMPLOYEE_QRY()11 ");
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
        final List list = new ArrayList();
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_SUPERVISOR_EMPLOYEE_QRY);
                return ps;
            }
        };
        jdbcTemplate.query(creator,
       		  new RowCallbackHandler() {
       		      public void processRow(ResultSet rs) throws SQLException {

       		    	do {
       		    		String employeeId=rs.getString("KRONOS_ID");
       		    		String firstName=rs.getString("FIRST_NAME");
       		    		String lastName=rs.getString("LAST_NAME");
       		    		String middleInitial=rs.getString("MIDDLE_INITIAL");
       		    		String shortName=rs.getString("SHORT_NAME");
       		    		String jobType=rs.getString("JOB_TYPE");
       		    		Date hireDate=rs.getDate("HIRE_DATE");
       		    		String status=rs.getString("STATUS");
       		    		String supervisorId=rs.getString("SUP_KRONOS_ID");
       		    		String supervisorFirstName=rs.getString("SUP_FIRST_NAME");
       		    		String supervisorMiddleInitial=rs.getString("SUP_MIDDLE_INITIAL");
       		    		String supervisorLastName=rs.getString("SUP_LAST_NAME");
       		    		String supervisorShortName=rs.getString("SUP_SHORT_NAME");

       		    		EmployeeInfo model=new EmployeeInfo(
       		    		employeeId,firstName,lastName,middleInitial,shortName,jobType,hireDate,
       		    		status,supervisorId,supervisorFirstName,supervisorMiddleInitial,supervisorLastName,supervisorShortName,null
       		    		);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getSupervisor list  "+list.size());
        return list;

	}


	public Collection getTerminatedEmployees() throws DataAccessException {
		// TODO Auto-generated method stub
	//	System.out.println("EmployeeManagerDaoOracleImpl :getTerminatedEmployees()11 ");
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
        final List list = new ArrayList();
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_TERMINATED_EMPLOYEE_QRY);
                return ps;
            }
        };
        jdbcTemplate.query(creator,
       		  new RowCallbackHandler() {
       		      public void processRow(ResultSet rs) throws SQLException {

       		    	do {
       		    		String employeeId=rs.getString("KRONOS_ID");
       		    		String firstName=rs.getString("FIRST_NAME");
       		    		String lastName=rs.getString("LAST_NAME");
       		    		String middleInitial=rs.getString("MIDDLE_INITIAL");
       		    		String shortName=rs.getString("SHORT_NAME");
       		    		String jobType=rs.getString("JOB_TYPE");
       		    		Date hireDate=rs.getDate("HIRE_DATE");
       		    		String status=rs.getString("STATUS");
       		    		String supervisorId=rs.getString("SUP_KRONOS_ID");
       		    		String supervisorFirstName=rs.getString("SUP_FIRST_NAME");
       		    		String supervisorMiddleInitial=rs.getString("SUP_MIDDLE_INITIAL");
       		    		String supervisorLastName=rs.getString("SUP_LAST_NAME");
       		    		String supervisorShortName=rs.getString("SUP_SHORT_NAME");
       		    		Date terminationDate=rs.getDate("TERMINATION_DATE");
       		    		EmployeeInfo model=new EmployeeInfo(
       		    		employeeId,firstName,lastName,middleInitial,shortName,jobType,hireDate,
       		    		status,supervisorId,supervisorFirstName,supervisorMiddleInitial,supervisorLastName,supervisorShortName,terminationDate
       		    		);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getEmployee list  "+list.size());
        return list;
	}

	
	public Collection getActiveInactiveEmployees() {
		// TODO Auto-generated method stub
		//System.out.println("EmployeeManagerDaoOracleImpl :getEmployee()11 ");
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
        final List list = new ArrayList();
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement(GET_ALL_ACTIVE_INACTIVE_EMPLOYEE_QRY);
                return ps;
            }
        };
        jdbcTemplate.query(creator,
       		  new RowCallbackHandler() {
       		      public void processRow(ResultSet rs) throws SQLException {

       		    	do {
       		    		String employeeId=rs.getString("KRONOS_ID");
       		    		String firstName=rs.getString("FIRST_NAME");
       		    		String lastName=rs.getString("LAST_NAME");
       		    		String middleInitial=rs.getString("MIDDLE_INITIAL");
       		    		String shortName=rs.getString("SHORT_NAME");
       		    		String jobType=rs.getString("JOB_TYPE");
       		    		Date hireDate=rs.getDate("HIRE_DATE");
       		    		String status=rs.getString("STATUS");
       		    		String supervisorId=rs.getString("SUP_KRONOS_ID");
       		    		String supervisorFirstName=rs.getString("SUP_FIRST_NAME");
       		    		String supervisorMiddleInitial=rs.getString("SUP_MIDDLE_INITIAL");
       		    		String supervisorLastName=rs.getString("SUP_LAST_NAME");
       		    		String supervisorShortName=rs.getString("SUP_SHORT_NAME");

       		    		EmployeeInfo model=new EmployeeInfo(
       		    		employeeId,firstName,lastName,middleInitial,shortName,jobType,hireDate,
       		    		status,supervisorId,supervisorFirstName,supervisorMiddleInitial,supervisorLastName,supervisorShortName,null
       		    		);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getEmployee list  "+list.size());
        return list;

	}
	
	public void refresh(final String worktable) throws DataAccessException {
		CallableStatementCreator creator=new CallableStatementCreator(){
			public CallableStatement createCallableStatement(Connection connection) throws SQLException{
				CallableStatement cs=
						connection.prepareCall("{ call TRANSP.REFRESH_MVIEW(?) }");
				cs.setString(1, worktable);
				return cs;
			}
			
		};
		List params=new ArrayList();
		jdbcTemplate.call(creator,params);
		
	}

}
