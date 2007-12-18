package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.enum.EnumDAOI;

public class CrmCasePriorityDAO implements EnumDAOI {

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION, PRIORITY FROM CUST.CASE_PRIORITY ORDER BY PRIORITY");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		while (rs.next()) {
			l.add(
				new CrmCasePriority(
					rs.getString("CODE"),
					rs.getString("NAME"),
					rs.getString("DESCRIPTION"),
					rs.getInt("PRIORITY")));
		}

		rs.close();
		ps.close();
		return l;
	}

}
