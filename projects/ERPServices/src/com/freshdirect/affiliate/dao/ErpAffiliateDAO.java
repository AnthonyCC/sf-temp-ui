package com.freshdirect.affiliate.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.enums.EnumDAOI;
import com.freshdirect.framework.util.DaoUtil;

public class ErpAffiliateDAO implements EnumDAOI {

	public List loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = null;		
		ResultSet rs = null;	
		List l = new ArrayList();
		try {
			ps = conn.prepareStatement("SELECT CODE, NAME, DESCRIPTION, COND_TAX, COND_DEPOSIT, PAYMENTECH_DIVISION FROM CUST.AFFILIATE");
			rs = ps.executeQuery();
			while (rs.next()) {
				String affiliateCode = rs.getString("CODE");
				l.add(
					new ErpAffiliate(
						affiliateCode,
						rs.getString("NAME"),
						rs.getString("DESCRIPTION"),
						rs.getString("COND_TAX"),
						rs.getString("COND_DEPOSIT"),
						this.loadMerchants(affiliateCode, conn),
						this.getDivisions(rs.getString("PAYMENTECH_DIVISION"))));
			}
			
		} finally {
			DaoUtil.close(rs, ps);
		}
		return l;
	}
	
	private Set getDivisions(String divisions){
		Set s = new HashSet();
		if (divisions != null) {
			String[] tokens = divisions.split("\\,", -2);
			for (int i = 0; i < tokens.length; i++) {
				s.add(tokens[i]);
			}
		}
		return s;
	}
	
	private static final String MERCHANT_QUERY = "SELECT PAYMENT_TYPE, MERCHANT FROM CUST.AFFILIATE_MERCHANT WHERE AFFILIATE_CODE = ?";
	
	private Map<EnumCardType, String> loadMerchants(String affiliateCode, Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<EnumCardType, String> m;
		try {
			ps = conn.prepareStatement(MERCHANT_QUERY);
			ps.setString(1, affiliateCode);
			rs = ps.executeQuery();
			m = new HashMap<EnumCardType, String>();
			while (rs.next()) {
				m.put(EnumCardType.getEnum(rs.getString("PAYMENT_TYPE")), rs.getString("MERCHANT"));
			} 
		} finally {
			DaoUtil.close(rs, ps);
		}
		return m;
	}

}
