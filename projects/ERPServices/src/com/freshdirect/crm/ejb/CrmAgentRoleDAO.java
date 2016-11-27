package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.enums.EnumDAOI;

public class CrmAgentRoleDAO implements EnumDAOI{
	
	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION, LDAP_ROLE_NAME FROM CUST.ROLE");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		while (rs.next()) {
			l.add(new CrmAgentRole(rs.getString("CODE"), rs.getString("NAME"), rs.getString("DESCRIPTION"),rs.getString("LDAP_ROLE_NAME")));
		}

		rs.close();
		ps.close();
		
		return l;
	}
	
}
