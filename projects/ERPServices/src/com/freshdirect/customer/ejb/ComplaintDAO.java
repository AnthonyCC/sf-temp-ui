/*
 * ComplaintDAO.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.EnumComplaintDlvIssueType;
import com.freshdirect.customer.ErpComplaintReason;

/** Data Access Object for department complaint reasons
 *
 * @version $Revision:9$
 * @author $Author:Mike Rose$
 */
public class ComplaintDAO implements java.io.Serializable {
	private static final long serialVersionUID = -1453186142440794088L;

	private static String reasonCodeQuery =
		"select cdc.id, cd.code as deptCode, cd.NAME as deptName, cc.name as reason, " +
		"  cc.priority as priority, cc.subject_code as subject, dlv_issue_code " +
		"from cust.complaint_dept cd, " +
		"  cust.complaint_code cc, " +
		"  cust.complaint_dept_code cdc " +
		"where cc.code=cdc.comp_code " +
		"  and cd.code=cdc.comp_dept " +
		"  and cdc.obsolete is null " +
		"order by cd.code, cdc.priority, cc.name";

	
	private static String reasonCodeQuery_noCartReq = 
		"select cdc.id, cd.code as deptCode, cd.NAME as deptName, cc.name as reason, " +
		"  cc.priority as priority, cc.subject_code as subject, dlv_issue_code " +
		"from cust.complaint_dept cd, " +
		"  ( " +
		"    select cc.* from cust.complaint_code cc " +
		"    join cust.case_subject s on(cc.subject_code=s.code) " +
		"    where subject_code is not null and (s.cartons_req<>'X' or s.cartons_req is null " +
		"		or cc.code in ('DAMBOXC','DAMBOXD','PRDDAM','DAMBOPRD','MISBOX','MISFRZ')) " +
		"  ) cc, " +
		"  cust.complaint_dept_code cdc " +
		"where cc.code=cdc.comp_code " +
		"  and cd.code=cdc.comp_dept " +
		"  and cdc.obsolete is null " +
		"order by cd.code, cdc.priority, cc.name";
	
	public Map getReasons(Connection conn, boolean excludeCartonReq) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(excludeCartonReq ? reasonCodeQuery_noCartReq : reasonCodeQuery);
		Map results = new HashMap();
		while (rs.next()) {
            ErpComplaintReason ecr = new ErpComplaintReason(rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4),
            		rs.getInt(5), rs.getString(6), EnumComplaintDlvIssueType.getEnum(rs.getString(6)) );
            List reasons = (List) results.get(ecr.getDepartmentCode());
            if (reasons == null) {
                reasons = new ArrayList();
                results.put(ecr.getDepartmentCode(), reasons);
                
            }
            reasons.add(ecr);
		}
		rs.close();
		stmt.close();
		return results;
	}
	
	/* get all complaint codes */
	private static String complaintCodeQuery = "select code, name from cust.complaint_code";
	
	public Map getComplaintCodes(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(complaintCodeQuery);
		Map results = new HashMap();
		while (rs.next()) {
            results.put(rs.getString(1), rs.getString(2));   
		}
		rs.close();
		stmt.close();
		return results;
	}


	public Collection getPendingComplaintSaleIds(Connection conn) throws SQLException {
		String sql = "SELECT SALE_ID FROM CUST.COMPLAINT WHERE STATUS = 'PEN'";

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List results = new ArrayList();
		while (rs.next()) {
			results.add( rs.getString("SALE_ID") );
		}
		rs.close();
		stmt.close();

		return results;
	}
	public void rejectMakegoodComplaint(Connection conn, String makegood_sale_id)throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.COMPLAINT SET STATUS='REJ' WHERE MAKEGOOD_SALE_ID = ? and status='APP'");
		ps.setString(1,makegood_sale_id);
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		ps = null;
	}

}