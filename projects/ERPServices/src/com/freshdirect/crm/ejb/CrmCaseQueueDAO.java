package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.enums.EnumDAOI;

public class CrmCaseQueueDAO implements EnumDAOI {

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION, OBSOLETE FROM CUST.CASE_QUEUE");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		
		while (rs.next()) {
			String code = rs.getString("CODE");
			String name = rs.getString("NAME");
			String desc = rs.getString("DESCRIPTION");
			boolean obsolete = "X".equals(rs.getString("OBSOLETE"));

			CrmCaseQueue queue = new CrmCaseQueue(code, name, desc, obsolete);

			l.add(queue);
		}

		rs.close();
		ps.close();

		return l;
	}

}
