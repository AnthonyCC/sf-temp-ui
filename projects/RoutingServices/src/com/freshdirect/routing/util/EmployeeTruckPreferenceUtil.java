package com.freshdirect.routing.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.routing.model.EmployeeInfo;
import com.freshdirect.routing.model.EmployeeRole;
import com.freshdirect.routing.model.EmployeeTruckPreference;
import com.freshdirect.routing.truckassignment.analysis.AbstractQueryCallback;
import com.freshdirect.routing.truckassignment.analysis.ConnectionInfo;
import com.freshdirect.routing.truckassignment.analysis.DataRetrieval;
import com.freshdirect.routing.truckassignment.analysis.QueryParam;


public class EmployeeTruckPreferenceUtil {

	private List<EmployeeInfo> employees;
	private Map<String, EmployeeInfo> employeeMap;
	private List<EmployeeRole> employeeRoles;
	
	private static final int PREF_LIST_SIZE = 5;
	
	private static final String INSERT_EMPLOYEE_TRUCK_PREFS = "INSERT INTO TRANSP.TRUCK_PREFERENCE ( KRONOS_ID, PREFERENCE_KEY, TRUCK_NUMBER ) VALUES ( ?,?,? )";

	private static final String GET_EMPLOYEE_TRUCK_PREFS = "SELECT DR.RESOURCE_ID as KRONOS_ID, D.TRUCK as TRUCK, COUNT(D.DISPATCH_ID) as COUNT"
										+" from TRANSP.DISPATCH D,  TRANSP.DISPATCH_RESOURCE DR"
										+" WHERE D.TRUCK is NOT NULL and D.DISPATCH_DATE > trunc(sysdate-180) and" 
										+" D.DISPATCH_ID=DR.DISPATCH_ID and DR.role in ('001','004') and DR.RESOURCE_ID=?"
										+" Group by DR.RESOURCE_ID,D.TRUCK "
										+" Order by RESOURCE_ID ASC, COUNT DESC";
	
	private static final String GET_KRONOS_AVTIVEINACTIVE_EMPLOYEES = "SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
										" a.HOMELABORLEVELNM7 JOB_TYPE, a.COMPANYHIREDTM HIRE_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
										" b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
										" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
										" WHERE a.SUPERVISORNUM = b.PERSONNUM(+) "+
										" AND ( a.EMPLOYMENTSTATUS='Active' or a.EMPLOYMENTSTATUS='Inactive')";
	private static final String GET_EMPLOYEE_ROLES ="SELECT er.KRONOS_ID, er.ROLE, er.SUB_ROLE "
													 +" FROM TRANSP.EMPLOYEEROLE er where er.role in ('001','004')";
	public EmployeeTruckPreferenceUtil() {
		employees = new ArrayList<EmployeeInfo>();
		employeeMap = new HashMap<String, EmployeeInfo>();
		employeeRoles = new ArrayList<EmployeeRole>();
		
	}
	
	public static void main(String[] args) throws ParseException, SQLException {
		EmployeeTruckPreferenceUtil truckPref = new EmployeeTruckPreferenceUtil();
		
		//System.setProperty(LogFactory.FACTORY_PROPERTY, "org.apache.commons.logging.impl.Log4jFactory");
		truckPref.loadActiveInactiveEmployees();
		truckPref.loadEmployeeRoles();
		truckPref.getFinalEmployees();
	}
	
	@SuppressWarnings("unchecked")
	private void getFinalEmployees() throws SQLException{
		List<EmployeeInfo> employees = new ArrayList<EmployeeInfo>(employeeRoles.size());		
		
		Iterator it = employeeRoles.iterator();
		while (it.hasNext()) {
			EmployeeRole empRole = (EmployeeRole) it.next();
			EmployeeInfo info = getTransAppActiveEmployees(this.employees, empRole.getEmployeeId());
			if (info != null){					
				employees.add(info);
				if(!employeeMap.containsKey(info.getEmployeeId())){
		    		employeeMap.put(info.getEmployeeId(), info);
		    	}
			}
		}
		
		
		for(Map.Entry<String, EmployeeInfo> employeeEntry : employeeMap.entrySet()){
			EmployeeInfo empInfo = employeeEntry.getValue();
			this.deleteTruckPreferences(empInfo.getEmployeeId());
			List<EmployeeTruckPreference> prefs = this.getEmployeeTruckPreferenceHistory(empInfo.getEmployeeId());
			
			if(prefs.size() > 0){
				if (prefs.size() > PREF_LIST_SIZE)
					prefs = prefs.subList(0, PREF_LIST_SIZE);
				Iterator<EmployeeTruckPreference> i = prefs.iterator();
				int count = 0;
				while(i.hasNext()){
					count++;
					EmployeeTruckPreference empPref = i.next();
					if(count == 1)empPref.setPrefKey("TP1");
					else if(count == 2)empPref.setPrefKey("TP2");
					else if(count == 3)empPref.setPrefKey("TP3");
					else if(count == 4)empPref.setPrefKey("TP4");
					else if(count == 5)empPref.setPrefKey("TP5");
				}
				saveEmployeeTruckPreferences(prefs);
			}		
		}
	}
	
	private void saveEmployeeTruckPreferences(final List<EmployeeTruckPreference> dataList) throws SQLException{
		Connection connection = null;
		ConnectionInfo conInfo = ConnectionInfo.DWDEV;
		if(dataList != null && dataList.size() > 0) {
			try{
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(conInfo.getDataSource(),INSERT_EMPLOYEE_TRUCK_PREFS);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
				batchUpdater.compile();
				
				connection = conInfo.getNewConnection();
				
				for(EmployeeTruckPreference model : dataList) {
					batchUpdater.update(new Object[]{ model.getEmployeeId()
												, model.getPrefKey()
												, model.getTruckNumber()
											});
				}			
				batchUpdater.flush();
			}finally{
				if(connection!=null) connection.close();
			}
		}
	}
	
	private List<EmployeeTruckPreference> getEmployeeTruckPreferenceHistory(final String empId) throws SQLException{
		
		final List<EmployeeTruckPreference> truckPrefs = new ArrayList<EmployeeTruckPreference>();
		
		DataRetrieval retrieval = new DataRetrieval(ConnectionInfo.PROD);
		retrieval.setFetchSize(1000);
		retrieval.setQuery(GET_EMPLOYEE_TRUCK_PREFS, new QueryParam[] {new QueryParam(Types.VARCHAR, empId)});
		retrieval.executeQuery(new AbstractQueryCallback() {
			@Override
			protected void rowImpl(ResultSet rs) throws SQLException {
					String employeeId = rs.getString("KRONOS_ID");
		    		String truck  = rs.getString("TRUCK");
		    		int count  = rs.getInt("COUNT");
		    		
		    		EmployeeTruckPreference model = new EmployeeTruckPreference(employeeId,truck,count);
		    		truckPrefs.add(model);
			}				
		});		
		return truckPrefs;
	}
	
	private void deleteTruckPreferences(String empId) throws SQLException{
		
		Connection con = null;
		ConnectionInfo conInfo = ConnectionInfo.DWDEV;
		try{
			con = conInfo.getNewConnection();
			Statement st = con.createStatement();
	        st.executeUpdate("DELETE FROM TRANSP.TRUCK_PREFERENCE TP WHERE TP.KRONOS_ID="+empId);
	    
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(con != null)con.close();
		}
	}
	
	private EmployeeInfo getTransAppActiveEmployees(Collection activeEmployees, String empId) {
		List transpActiveEmployees = (List) activeEmployees;
		for (int i = 0; i < transpActiveEmployees.size(); i++) {
			EmployeeInfo info = (EmployeeInfo) transpActiveEmployees.get(i);
			if (info.getEmployeeId().equalsIgnoreCase(empId))
				return info;
		}
		return null;
	}
	
	private void loadActiveInactiveEmployees() throws ParseException, SQLException {
			
			DataRetrieval retrieval = new DataRetrieval(ConnectionInfo.DWDEV);
			retrieval.setFetchSize(1000);
			retrieval.setQuery(GET_KRONOS_AVTIVEINACTIVE_EMPLOYEES,new QueryParam[] {});
			retrieval.executeQuery(new AbstractQueryCallback() {
				@Override
				protected void rowImpl(ResultSet rs) throws SQLException {
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

   		    		EmployeeInfo model = new EmployeeInfo(
   		    								employeeId,firstName,lastName,middleInitial,shortName,jobType,hireDate,
   		    								status,supervisorId,supervisorFirstName,supervisorMiddleInitial,supervisorLastName,supervisorShortName,null
   		    								);
   		    		employees.add(model);
   		    		
				}				
			});
	}
	
	private void loadEmployeeRoles()throws ParseException, SQLException {
		
		DataRetrieval retrieval = new DataRetrieval(ConnectionInfo.DWDEV);
		retrieval.setFetchSize(5000);
		retrieval.setQuery(GET_EMPLOYEE_ROLES, new QueryParam[] {});
		retrieval.executeQuery(new AbstractQueryCallback() {
			@Override
			protected void rowImpl(ResultSet rs) throws SQLException {
				    String employeeId = rs.getString("KRONOS_ID");
		    		String role = rs.getString("ROLE");
		    		String subRole = rs.getString("SUB_ROLE");
		    		EmployeeRole model = new EmployeeRole(employeeId,role,subRole);
		    		employeeRoles.add(model);		    		
			}				
		});

	}
	
}
