package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.enum.EnumDAOI;

public class CrmCaseOriginDAO implements EnumDAOI {

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.CASE_ORIGIN");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		while (rs.next()) {
			String code = rs.getString("CODE");
			String name = rs.getString("NAME");
			String desc = rs.getString("DESCRIPTION");
			boolean obsolete = "X".equals(rs.getString("OBSOLETE"));

			l.add(new CrmCaseOrigin(code, name, desc, obsolete));
		}

		rs.close();
		ps.close();
		return l;
	}

}
