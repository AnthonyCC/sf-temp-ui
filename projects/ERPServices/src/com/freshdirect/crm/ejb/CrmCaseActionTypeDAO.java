package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.enums.EnumDAOI;

public class CrmCaseActionTypeDAO implements EnumDAOI{

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION FROM CUST.CASEACTION_TYPE");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		while (rs.next()) {
			l.add(new CrmCaseActionType(rs.getString("CODE"), rs.getString("NAME"), rs.getString("DESCRIPTION")));
		}

		rs.close();
		ps.close();
		return l;
	}

}
