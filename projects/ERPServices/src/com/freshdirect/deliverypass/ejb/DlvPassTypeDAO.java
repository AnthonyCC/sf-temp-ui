package com.freshdirect.deliverypass.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.enums.EnumDAOI;

public class DlvPassTypeDAO implements EnumDAOI{
	
	private static final long	serialVersionUID	= 5894229300804868385L;

	public List<DeliveryPassType> loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT SKU_CODE, NAME, NO_OF_DLVS, DURATION, IS_UNLIMITED,PROFILE_VAL, IS_AUTORENEW_DP, IS_FREETRIAL, RESTRICT_FREETRIAL,AUTORENEWAL_SKU_CODE FROM CUST.DLV_PASS_TYPE");
		ResultSet rs = ps.executeQuery();

		List<DeliveryPassType> l = new ArrayList<DeliveryPassType>();
		while (rs.next()) {
			String skuCode= rs.getString("SKU_CODE");
			String name = rs.getString("NAME");
			int noOfDlvs = rs.getInt("NO_OF_DLVS");
			int duration = rs.getInt("DURATION");
			String profileValue=rs.getString("PROFILE_VAL");
			if(profileValue==null)
				profileValue="";
			boolean unlimited = ("Y".equals(rs.getString("IS_UNLIMITED"))) ? true : false;
			boolean isAutoRenewDP = ("Y".equals(rs.getString("IS_AUTORENEW_DP"))) ? true : false;
			boolean isFreeTrial = ("Y".equals(rs.getString("IS_FREETRIAL"))) ? true : false;
			boolean restrictFreeTrial = ("Y".equals(rs.getString("RESTRICT_FREETRIAL"))) ? true : false;
			String autoRenewalSKU=rs.getString("AUTORENEWAL_SKU_CODE");
			if((isAutoRenewDP)&& ((autoRenewalSKU==null)||("".equals(autoRenewalSKU)))) {
				autoRenewalSKU=skuCode;
			}
			l.add(new DeliveryPassType(skuCode, name, noOfDlvs, duration,unlimited, profileValue.trim(),isAutoRenewDP,isFreeTrial,restrictFreeTrial,autoRenewalSKU));
		}

		rs.close();
		ps.close();
		
		return l;
	}
	
}
