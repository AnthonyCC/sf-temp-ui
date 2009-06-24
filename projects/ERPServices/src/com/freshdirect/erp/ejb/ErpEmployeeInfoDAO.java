package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.erp.ErpEmployeeInfo;

public class ErpEmployeeInfoDAO implements java.io.Serializable {
	
	private static final String GET_ALL_ACTIVE_EMPLOYEE_QRY=
		"SELECT a.PERSONNUM KRONOS_ID, a.FIRSTNM FIRST_NAME, a.MIDDLEINITIALNM MIDDLE_INITIAL, a.LASTNM LAST_NAME, a.SHORTNM SHORT_NAME, "+
		"a.HOMELABORLEVELNM7 JOB_TYPE, a.COMPANYHIREDTM HIRE_DATE, a.EMPLOYMENTSTATUS STATUS, b.PERSONNUM SUP_KRONOS_ID, b.FIRSTNM SUP_FIRST_NAME, "+
		"b.MIDDLEINITIALNM SUP_MIDDLE_INITIAL, b.LASTNM SUP_LAST_NAME, b.SHORTNM SUP_SHORT_NAME "+
		" FROM transp.KRONOS_EMPLOYEE a, transp.KRONOS_EMPLOYEE b "+
		"WHERE a.SUPERVISORNUM = b.PERSONNUM(+) ";
	
	public List getEmployees(Connection conn)throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List employees = new ArrayList();
		try {			
			ps = conn.prepareStatement(GET_ALL_ACTIVE_EMPLOYEE_QRY);
			
			rs = ps.executeQuery();
			ErpEmployeeInfo info = null;
			
			while(rs.next()) {
				
	    		info = new ErpEmployeeInfo(
	    				rs.getString("KRONOS_ID"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME")
	    				,rs.getString("MIDDLE_INITIAL"),rs.getString("SHORT_NAME"),rs.getString("JOB_TYPE")
	    				,rs.getDate("HIRE_DATE"), rs.getString("STATUS")
	    				,rs.getString("SUP_KRONOS_ID") 
	    				,rs.getString("SUP_FIRST_NAME")
	    				,rs.getString("SUP_MIDDLE_INITIAL")
	    				,rs.getString("SUP_LAST_NAME"),rs.getString("SUP_SHORT_NAME"),null);
	    		employees.add(info);
			}						
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}
		 return employees;
	}
	
	
}
