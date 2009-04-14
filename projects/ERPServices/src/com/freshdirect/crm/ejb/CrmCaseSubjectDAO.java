package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.enum.EnumDAOI;

public class CrmCaseSubjectDAO implements EnumDAOI {

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION, OBSOLETE, CASE_QUEUE, CASE_PRIORITY, CARTONS_REQ FROM CUST.CASE_SUBJECT");
		ResultSet rs = ps.executeQuery();
		List l = new ArrayList();
		while (rs.next()) {
			String code = rs.getString("CODE");
			String name = rs.getString("NAME");
			String desc = rs.getString("DESCRIPTION");
			boolean obsolete = "X".equals(rs.getString("OBSOLETE"));
			String queueCode = rs.getString("CASE_QUEUE");
			String priorityCode = rs.getString("CASE_PRIORITY");
			boolean isCartonsRequired = "X".equals(rs.getString("CARTONS_REQ"));

			CrmCaseSubject subject = new CrmCaseSubject(queueCode, code, name, desc, obsolete, priorityCode, isCartonsRequired);

			l.add(subject);
		}

		rs.close();
		ps.close();
		
		return l;
	}

}
