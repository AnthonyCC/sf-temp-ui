package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.enums.EnumDAOI;
import com.freshdirect.erp.EnumFeaturedHeaderType;

public class EnumFeaturedHeaderTypeDAO implements EnumDAOI {

	@Override
	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION FROM ERPS.PRODUCT_PROMOTION_FEAT_HEADER");
		ResultSet rs = ps.executeQuery();

		List l = new ArrayList();
		while (rs.next()) {
			String code = rs.getString("CODE");
			l.add(new EnumFeaturedHeaderType(code,rs.getString("NAME"),rs.getString("DESCRIPTION")));		}

		rs.close();
		ps.close();
		return l;
	}

}
