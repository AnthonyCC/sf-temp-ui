package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmDepartment;
import com.freshdirect.enums.EnumDAOI;

public class CrmDepartmentDAO implements EnumDAOI {

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION, OBSOLETE FROM CUST.DEPARTMENT");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		while (rs.next()) {
			l.add(
				new CrmDepartment(
					rs.getString("CODE"),
					rs.getString("NAME"),
					rs.getString("DESCRIPTION"),
					"X".equals(rs.getString("OBSOLETE"))));
		}

		rs.close();
		ps.close();
		return l;
	}

}
