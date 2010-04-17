package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

public class FDCartLineDAO {

	private FDCartLineDAO() {
	}

	private final static String QUERY_CARTLINES =
		"SELECT ID, SKU_CODE, VERSION, QUANTITY, SALES_UNIT, CONFIGURATION, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION, VARIANT_ID, DISCOUNT_APPLIED, SAVINGS_ID "
			+ " FROM CUST.FDCARTLINE WHERE FDUSER_ID = ?";

	public static List<ErpOrderLineModel> loadCartLines(Connection conn, PrimaryKey fdUserPk) throws SQLException {

		List<ErpOrderLineModel> lst = new LinkedList<ErpOrderLineModel>();
		PreparedStatement ps = conn.prepareStatement(QUERY_CARTLINES);
		ps.setString(1, fdUserPk.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			ErpOrderLineModel line = new ErpOrderLineModel();
			FDSku sku = new FDSku(rs.getString("SKU_CODE"), rs.getInt("VERSION"));
			line.setSku(sku);
			FDConfiguration config =
				new FDConfiguration(
					rs.getDouble("QUANTITY"),
					rs.getString("SALES_UNIT"),
					convertStringToHashMap(NVL.apply(rs.getString("CONFIGURATION"), "")));
			line.setConfiguration(config);
			line.setCartlineId(rs.getString("ID"));
			line.setRecipeSourceId(rs.getString("RECIPE_SOURCE_ID"));
			line.setRequestNotification(NVL.apply(rs.getString("REQUEST_NOTIFICATION"), "").equals("X"));
			line.setVariantId(rs.getString("VARIANT_ID"));
			if(rs.getString("DISCOUNT_APPLIED")!=null && rs.getString("DISCOUNT_APPLIED").equalsIgnoreCase("X")){
			line.setDiscountFlag(true);
			line.setSavingsId(rs.getString("SAVINGS_ID"));
			}
			lst.add(line);
		}
		rs.close();
		ps.close();
		return lst;

	}

	public static void storeCartLines(Connection conn, PrimaryKey fdUserPk, List<ErpOrderLineModel> erpOrderlines) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.FDCARTLINE WHERE FDUSER_ID = ?");
		ps.setString(1, fdUserPk.getId());
		ps.executeUpdate();
		ps.close();

		ps =
			conn.prepareStatement(
				"INSERT INTO CUST.FDCARTLINE (ID, FDUSER_ID, SKU_CODE, VERSION, QUANTITY, SALES_UNIT, CONFIGURATION, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION, VARIANT_ID, DISCOUNT_APPLIED, SAVINGS_ID) values (?,?,?,?,?,?,?,?,?,?,?,?)");

		for ( ErpOrderLineModel line : erpOrderlines ) {
			ps.setString(1, line.getCartlineId());
			ps.setString(2, fdUserPk.getId());
			ps.setString(3, line.getSku().getSkuCode());
			ps.setInt(4, line.getSku().getVersion());
			ps.setDouble(5, line.getQuantity());
			ps.setString(6, line.getSalesUnit());
			ps.setString(7, convertHashMapToString(line.getOptions()));
			ps.setString(8, line.getRecipeSourceId());
			ps.setString(9, line.isRequestNotification() ? "X" : "");
			ps.setString(10, line.getVariantId());
			ps.setString(11, line.isDiscountFlag()? "X" : "");			
			ps.setString(12, line.getSavingsId());
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}

	private static HashMap<String,String> convertStringToHashMap(String configuration) {
		StringTokenizer st = new StringTokenizer(configuration, ",");
		HashMap<String,String> ret = new HashMap<String,String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken().trim();
			int idx = token.indexOf("=");
			String key = token.substring(0, idx++);
			String value = token.substring(idx, token.length());
			ret.put(key, value);
		}

		return ret;

	}

	private static String convertHashMapToString(Map<String,String> map) {
		StringBuffer ret = new StringBuffer();
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			ret.append(key);
			ret.append("=");
			ret.append(map.get(key));
			if (i.hasNext()) {
				ret.append(",");
			}
		}
		return ret.toString();
	}
}
