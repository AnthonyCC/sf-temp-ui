package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCaseOperation;

public class CrmCaseOperationDAO {

	public List<CrmCaseOperation> loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.CASE_OPERATION");
		ResultSet rs = ps.executeQuery();

		List<CrmCaseOperation> l = new ArrayList<CrmCaseOperation>();
		while (rs.next()) {
			String roleCode = rs.getString("ROLE");
			String subjectCode = rs.getString("CASE_SUBJECT");
			String startStateCode = rs.getString("START_CASE_STATE");
			String endStateCode = rs.getString("END_CASE_STATE");
			String actionTypeCode = rs.getString("CASEACTION_TYPE");

			CrmCaseOperation op = new CrmCaseOperation(roleCode, subjectCode, startStateCode, endStateCode, actionTypeCode);

			l.add(op);
		}

		rs.close();
		ps.close();

		return l;
	}
    
    public CrmCaseOperation getPermissableOperation(Connection conn, String role, String subject, String startState, String caseActionType) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.CASE_OPERATION WHERE CASE_SUBJECT=? AND START_CASE_STATE=? AND ROLE=? AND CASEACTION_TYPE=?");
        ps.setString(1, subject);
        ps.setString(2, startState);
        ps.setString(3, role);
        ps.setString(4, caseActionType);
		ResultSet rs = ps.executeQuery();
        
        CrmCaseOperation op = null;
        
		if (rs.next()) {
			String roleCode = rs.getString("ROLE");
			String subjectCode = rs.getString("CASE_SUBJECT");
			String startStateCode = rs.getString("START_CASE_STATE");
			String endStateCode = rs.getString("END_CASE_STATE");
			String actionTypeCode = rs.getString("CASEACTION_TYPE");
            
			op = new CrmCaseOperation(roleCode, subjectCode, startStateCode, endStateCode, actionTypeCode);            
		}
        
		rs.close();
		ps.close();
        
        return op;
        
    }

}
