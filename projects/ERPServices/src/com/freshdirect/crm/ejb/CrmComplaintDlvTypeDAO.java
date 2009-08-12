package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumComplaintDlvIssueType;
import com.freshdirect.enums.EnumDAOI;

public class CrmComplaintDlvTypeDAO implements EnumDAOI {
	private static final long serialVersionUID = -1953028802257852580L;

	public List<EnumComplaintDlvIssueType> loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION FROM CUST.COMPLAINT_DLV_TYPE");
		ResultSet rs = ps.executeQuery();

		List<EnumComplaintDlvIssueType> l = new ArrayList<EnumComplaintDlvIssueType>();
		while (rs.next()) {
			l.add(new EnumComplaintDlvIssueType(rs.getString("CODE"), rs.getString("NAME"), rs.getString("DESCRIPTION")));
		}

		rs.close();
		ps.close();
		
		return l;
	}
}
