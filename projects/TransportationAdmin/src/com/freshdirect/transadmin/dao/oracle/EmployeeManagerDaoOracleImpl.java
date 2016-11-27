package com.freshdirect.transadmin.dao.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.dao.EmployeeManagerDaoI;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.KronosEmployeeInfo;

public class EmployeeManagerDaoOracleImpl implements EmployeeManagerDaoI {

	private JdbcTemplate jdbcTemplate;

	private final static Category LOGGER = LoggerFactory.getInstance(EmployeeManagerDaoOracleImpl.class);

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	private static final String GET_ALL_ACTIVE_EMPLOYEE_QRY =
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
	
	private static final String GET_KRONOS_EMPLOYEE_QRY = "select E.PERSONID from TRANSP.KRONOS_EMPLOYEE E";

	private static final String DELETE_KRONOS_EMPLOYEES = "delete from TRANSP.KRONOS_EMPLOYEE E where E.personid in ";

	private static final String INSERT_KRONOS_EMPLOYEES = "insert into TRANSP.KRONOS_EMPLOYEE E values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_KRONOS_EMPLOYEES = "update TRANSP.KRONOS_EMPLOYEE E set personnum = ?,personfullname = ?,firstnm = ?,middleinitialnm = ?," +
			"lastnm = ?,shortnm = ?,companyhiredtm = ?,ftstdhrsqty = ?,employmentstatus = ?,employmentstatusdt = ?,badgenum = ?,badgeeffectivedtm = ?," +
			"badgeexpirationdtm = ?,homelaboracctname = ?,homelaborlevelnm1 = ?,homelaborlevelnm2 = ?,homelaborlevelnm3 = ?,homelaborlevelnm4 = ?," +
			"homelaborlevelnm5 = ?,homelaborlevelnm6 = ?,homelaborlevelnm7 = ?,homelaboracctdsc = ?,homelaborleveldsc1 = ?,homelaborleveldsc2 = ?," +
			"homelaborleveldsc3 = ?,homelaborleveldsc4 = ?,homelaborleveldsc5 = ?,homelaborleveldsc6 = ?,homelaborleveldsc7 = ?,haeffectivedtm = ?," +
			"haexpirationdtm = ?,mgrsignoffthrudtm = ?,pendingsignoffsw = ?,payrollockthrudtm = ?,wageprflname = ?,grpschedname = ?,timezonename = ?," +
			"dcmdevgrpname = ?,supervisornum = ?,supervisorfullname = ?,supervisorid = ?,currpayperiodstart = ?,currpayperiodend = ?,prevpayperiodstart = ?," +
			"prevpayperiodend = ?,nextpayperiodstart = ?,nextpayperiodend = ?,laboracctid = ?,devicegroupid = ?,terminalruleid = ?,scheduleversion = ?," +
			"lasttotaltime = ?,lasttotalchngtime = ?,payruleid = ?,employeeid = ?,employmentstatid = ?,orgpathtxt = ?,orgpathdsctxt = ?,seniorityrankdate = ?," +
			"workertypenm = ? where personid = ?";

	
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
       		    		String employeeId = rs.getString("KRONOS_ID");
						String firstName = rs.getString("FIRST_NAME");
						String lastName = rs.getString("LAST_NAME");
						String middleInitial = rs.getString("MIDDLE_INITIAL");
						String shortName = rs.getString("SHORT_NAME");
						String jobType = rs.getString("JOB_TYPE");
						Date hireDate = rs.getDate("SENIORITYRANK_DATE");
						String status = rs.getString("STATUS");
						String supervisorId = rs.getString("SUP_KRONOS_ID");
						String supervisorFirstName = rs.getString("SUP_FIRST_NAME");
						String supervisorMiddleInitial = rs.getString("SUP_MIDDLE_INITIAL");
						String supervisorLastName = rs.getString("SUP_LAST_NAME");
						String supervisorShortName = rs.getString("SUP_SHORT_NAME");

       		    		EmployeeInfo model = new EmployeeInfo(employeeId,
																	firstName, lastName, middleInitial, shortName,
																	jobType, hireDate, status, supervisorId,
																	supervisorFirstName, supervisorMiddleInitial,
																	supervisorLastName, supervisorShortName, null);

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
       		    		String employeeId = rs.getString("KRONOS_ID");
						String firstName = rs.getString("FIRST_NAME");
						String lastName = rs.getString("LAST_NAME");
						String middleInitial = rs.getString("MIDDLE_INITIAL");
						String shortName = rs.getString("SHORT_NAME");
						String jobType = rs.getString("JOB_TYPE");
						Date hireDate = rs.getDate("SENIORITYRANK_DATE");
						String status = rs.getString("STATUS");
						String supervisorId = rs.getString("SUP_KRONOS_ID");
						String supervisorFirstName = rs.getString("SUP_FIRST_NAME");
						String supervisorMiddleInitial = rs.getString("SUP_MIDDLE_INITIAL");
						String supervisorLastName = rs.getString("SUP_LAST_NAME");
						String supervisorShortName = rs.getString("SUP_SHORT_NAME");

       		    		EmployeeInfo model = new EmployeeInfo(employeeId,
																firstName, lastName, middleInitial, shortName,
																jobType, hireDate, status, supervisorId,
																supervisorFirstName, supervisorMiddleInitial,
																supervisorLastName, supervisorShortName, null);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getSupervisor list  "+list.size());
        return list;

	}


	public Map<String, EmployeeInfo> getTerminatedEmployees() throws DataAccessException {
		// TODO Auto-generated method stub
	//	System.out.println("EmployeeManagerDaoOracleImpl :getTerminatedEmployees()11 ");
        //final String sql="select *   from  (select a.*, rownum rnum   from  ("+VIEW_ALL_COMPETITOR_LOCATION_QRY+"  order by "+searchCriteria.getSortedByColumn()+") a   where rownum <= ? ) where rnum > ?";
        final Map empMap = new HashMap();
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
       		    		String employeeId = rs.getString("KRONOS_ID");
						String firstName = rs.getString("FIRST_NAME");
						String lastName = rs.getString("LAST_NAME");
						String middleInitial = rs.getString("MIDDLE_INITIAL");
						String shortName = rs.getString("SHORT_NAME");
						String jobType = rs.getString("JOB_TYPE");
						Date hireDate = rs.getDate("SENIORITYRANK_DATE");
						String status = rs.getString("STATUS");
						String supervisorId = rs.getString("SUP_KRONOS_ID");
						String supervisorFirstName = rs.getString("SUP_FIRST_NAME");
						String supervisorMiddleInitial = rs.getString("SUP_MIDDLE_INITIAL");
						String supervisorLastName = rs.getString("SUP_LAST_NAME");
						String supervisorShortName = rs.getString("SUP_SHORT_NAME");
						Date terminationDate = rs.getDate("TERMINATION_DATE");
						
						EmployeeInfo model = new EmployeeInfo(employeeId,
																firstName, lastName, middleInitial, shortName,
																jobType, hireDate, status, supervisorId,
																supervisorFirstName, supervisorMiddleInitial,
																supervisorLastName, supervisorShortName,
																terminationDate);

						empMap.put(employeeId, model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getTerminatedEmployee list  "+empMap.values().size());
        return empMap;
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
       		    		String employeeId = rs.getString("KRONOS_ID");
						String firstName = rs.getString("FIRST_NAME");
						String lastName = rs.getString("LAST_NAME");
						String middleInitial = rs.getString("MIDDLE_INITIAL");
						String shortName = rs.getString("SHORT_NAME");
						String jobType = rs.getString("JOB_TYPE");
						Date hireDate = rs.getDate("SENIORITYRANK_DATE");
						String status = rs.getString("STATUS");
						String supervisorId = rs.getString("SUP_KRONOS_ID");
						String supervisorFirstName = rs.getString("SUP_FIRST_NAME");
						String supervisorMiddleInitial = rs.getString("SUP_MIDDLE_INITIAL");
						String supervisorLastName = rs.getString("SUP_LAST_NAME");
						String supervisorShortName = rs.getString("SUP_SHORT_NAME");

       		    		EmployeeInfo model = new EmployeeInfo(employeeId,
																firstName, lastName, middleInitial, shortName,
																jobType, hireDate, status, supervisorId,
																supervisorFirstName, supervisorMiddleInitial,
																supervisorLastName, supervisorShortName, null);

       		    		list.add(model);
       		    	   }
       		    	   while(rs.next());
       		      }
       		  }
       	);
        LOGGER.debug("EmployeeManagerDaoOracleImpl : getActiveInactiveEmployees list  "+list.size());
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

	@Override
	public List<EmployeeInfo> getAllEmployees() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KronosEmployeeInfo> getAllKronosEmployees()
			throws DataAccessException {
		
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
       		    		KronosEmployeeInfo model = new KronosEmployeeInfo(Integer.valueOf(rs.getInt("PERSONID")));
       		    		kronosemplist.add(model);
       		    	   }while(rs.next());
       		      }
       		  }
       	);
        
        return kronosemplist;

	
	}

	@Override
	public void syncEmployees(List<KronosEmployeeInfo> inserts,
			List<KronosEmployeeInfo> updates, List<KronosEmployeeInfo> deletes) throws SQLException {

		List<List<?>> buckets = RoutingUtil.splitList(inserts, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
		for(List bucket: buckets) {
			 insertEmployees(bucket);
		}
		buckets = RoutingUtil.splitList(updates, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
		for(List bucket: buckets) {
			updateEmployees(bucket);
		}
		buckets = RoutingUtil.splitList(deletes, RoutingServicesProperties.getJDBCBatchUpdateThreshold());
		for(List bucket: buckets) {
			deleteEmployees(bucket);
		}
	}
	


	private void deleteEmployees(List<KronosEmployeeInfo> deletes) throws SQLException {
		Connection connection = null;
		
		StringBuffer deleteEmployeesQ = new StringBuffer();
		try {
		deleteEmployeesQ.append(DELETE_KRONOS_EMPLOYEES);
		if(deletes != null && deletes.size() > 0){
			deleteEmployeesQ.append(" ( ");
			for(KronosEmployeeInfo emp : deletes) {
				deleteEmployeesQ.append("'").append(emp.getPersonid()).append("'");				
				if(deletes.indexOf(emp) < deletes.size()-1) {
					deleteEmployeesQ.append(",");
				}
			}
			deleteEmployeesQ.append(")");
			this.jdbcTemplate.update(deleteEmployeesQ.toString(), new Object[] { });
			connection=this.jdbcTemplate.getDataSource().getConnection();		
		}
		
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	private void updateEmployees(List<KronosEmployeeInfo> updates) throws SQLException {
		
		
		
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_KRONOS_EMPLOYEES);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			
			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(KronosEmployeeInfo model : updates) {
				
				batchUpdater.update(new Object[]{
						model.getPersonnum(),
						model.getPersonfullname(),
						model.getFirstnm(),
						model.getMiddleinitialnm(),
						model.getLastnm(),
						model.getShortnm(),
						model.getCompanyhiredtm(),
						model.getFtstdhrsqty(),
						model.getEmploymentstatus(),
						model.getEmploymentstatusdt(),
						model.getBadgenum(),
						model.getBadgeeffectivedtm(),
						model.getBadgeexpirationdtm(),
						model.getHomelaboracctname(),
						model.getHomelaborlevelnm1(),
						model.getHomelaborlevelnm2(),
						model.getHomelaborlevelnm3(),
						model.getHomelaborlevelnm4(),
						model.getHomelaborlevelnm5(),
						model.getHomelaborlevelnm6(),
						model.getHomelaborlevelnm7(),
						model.getHomelaboracctdsc(),
						model.getHomelaborleveldsc1(),
						model.getHomelaborleveldsc2(),
						model.getHomelaborleveldsc3(),
						model.getHomelaborleveldsc4(),
						model.getHomelaborleveldsc5(),
						model.getHomelaborleveldsc6(),
						model.getHomelaborleveldsc7(),
						model.getHaeffectivedtm(),
						model.getHaexpirationdtm(),
						model.getMgrsignoffthrudtm(),
						model.getPendingsignoffsw(),
						model.getPayrollockthrudtm(),
						model.getWageprflname(),
						model.getGrpschedname(),
						model.getTimezonename(),
						model.getDcmdevgrpname(),
						model.getSupervisornum(),
						model.getSupervisorfullname(),
						model.getSupervisorid(),
						model.getCurrpayperiodstart(),
						model.getCurrpayperiodend(),
						model.getPrevpayperiodstart(),
						model.getPrevpayperiodend(),
						model.getNextpayperiodstart(),
						model.getNextpayperiodend(),
						model.getLaboracctid(),
						model.getDevicegroupid(),
						model.getTerminalruleid(),
						model.getScheduleversion(),
						model.getLasttotaltime(),
						model.getLasttotalchngtime(),
						model.getPayruleid(),
						model.getEmployeeid(),
						model.getEmploymentstatid(),
						model.getOrgpathtxt(),
						model.getOrgpathdsctxt(),
						model.getSeniorityrankdate(),
						model.getWorkertypenm(),
						model.getPersonid()
				});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	
	
	
	}

	private void insertEmployees(List<KronosEmployeeInfo> inserts) throws SQLException {
		

		Connection connection = null;
		if(inserts != null && inserts.size() > 0) {
			try{
				BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_KRONOS_EMPLOYEES);
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				
				
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
				batchUpdater.compile();
	
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(KronosEmployeeInfo model : inserts) {
					batchUpdater.update(new Object[]{
							model.getPersonid(),
							model.getPersonnum(),
							model.getPersonfullname(),
							model.getFirstnm(),
							model.getMiddleinitialnm(),
							model.getLastnm(),
							model.getShortnm(),
							model.getCompanyhiredtm(),
							model.getFtstdhrsqty(),
							model.getEmploymentstatus(),
							model.getEmploymentstatusdt(),
							model.getBadgenum(),
							model.getBadgeeffectivedtm(),
							model.getBadgeexpirationdtm(),
							model.getHomelaboracctname(),
							model.getHomelaborlevelnm1(),
							model.getHomelaborlevelnm2(),
							model.getHomelaborlevelnm3(),
							model.getHomelaborlevelnm4(),
							model.getHomelaborlevelnm5(),
							model.getHomelaborlevelnm6(),
							model.getHomelaborlevelnm7(),
							model.getHomelaboracctdsc(),
							model.getHomelaborleveldsc1(),
							model.getHomelaborleveldsc2(),
							model.getHomelaborleveldsc3(),
							model.getHomelaborleveldsc4(),
							model.getHomelaborleveldsc5(),
							model.getHomelaborleveldsc6(),
							model.getHomelaborleveldsc7(),
							model.getHaeffectivedtm(),
							model.getHaexpirationdtm(),
							model.getMgrsignoffthrudtm(),
							model.getPendingsignoffsw(),
							model.getPayrollockthrudtm(),
							model.getWageprflname(),
							model.getGrpschedname(),
							model.getTimezonename(),
							model.getDcmdevgrpname(),
							model.getSupervisornum(),
							model.getSupervisorfullname(),
							model.getSupervisorid(),
							model.getCurrpayperiodstart(),
							model.getCurrpayperiodend(),
							model.getPrevpayperiodstart(),
							model.getPrevpayperiodend(),
							model.getNextpayperiodstart(),
							model.getNextpayperiodend(),
							model.getLaboracctid(),
							model.getDevicegroupid(),
							model.getTerminalruleid(),
							model.getScheduleversion(),
							model.getLasttotaltime(),
							model.getLasttotalchngtime(),
							model.getPayruleid(),
							model.getEmployeeid(),
							model.getEmploymentstatid(),
							model.getOrgpathtxt(),
							model.getOrgpathdsctxt(),
							model.getSeniorityrankdate(),
							model.getWorkertypenm()
					});
				}			
				batchUpdater.flush();
			}finally{
				if(connection!=null) connection.close();
			}
		}
	
	}

}
