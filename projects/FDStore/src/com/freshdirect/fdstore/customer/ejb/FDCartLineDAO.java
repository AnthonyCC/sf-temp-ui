package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.freshdirect.customer.ErpClientCode;
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
	
	private final static String QUERY_CARTLINE_CLIENTCODES =
		"SELECT CLIENT_CODE, QUANTITY, CARTLINE_ID FROM CUST.FDCARTLINE_CLIENTCODE WHERE FDUSER_ID = ? ORDER BY CARTLINE_ID, ORDINAL";

	public static List<ErpOrderLineModel> loadCartLines(Connection conn, PrimaryKey fdUserPk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_CARTLINE_CLIENTCODES);
		ps.setString(1, fdUserPk.getId());
		ResultSet rs = ps.executeQuery();
		Map<String,List<ErpClientCode>> clientCodes = new HashMap<String, List<ErpClientCode>>();
		while (rs.next()) {
			ErpClientCode cc = new ErpClientCode();
			cc.setClientCode(rs.getString("CLIENT_CODE"));
			cc.setQuantity(rs.getInt("QUANTITY"));
			String cartLine = rs.getString("CARTLINE_ID");
			if (!clientCodes.containsKey(cartLine))
				clientCodes.put(cartLine, new ArrayList<ErpClientCode>());
			clientCodes.get(cartLine).add(cc);
		}
		rs.close();
		ps.close();

		List<ErpOrderLineModel> lst = new LinkedList<ErpOrderLineModel>();
		ps = conn.prepareStatement(QUERY_CARTLINES);
		ps.setString(1, fdUserPk.getId());
		rs = ps.executeQuery();
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
		
		for (ErpOrderLineModel item : lst)
			if (clientCodes.containsKey(item.getCartlineId()))
				item.getClientCodes().addAll(clientCodes.get(item.getCartlineId()));
		return lst;

	}

	public static void storeCartLines(Connection conn, PrimaryKey fdUserPk, List<ErpOrderLineModel> erpOrderlines) throws SQLException {
		Map<String,List<ErpClientCode>> clientCodes = new HashMap<String, List<ErpClientCode>>();
		for (ErpOrderLineModel item : erpOrderlines) {
			// basic error resolution
			if (item.getCartlineId() == null)
				continue;
			
			ArrayList<ErpClientCode> ccList = new ArrayList<ErpClientCode>();
			ccList.addAll(item.getClientCodes());

			// we'll overwrite possible duplicates
			clientCodes.put(item.getCartlineId(), ccList);
		}
		
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.FDCARTLINE_CLIENTCODE WHERE FDUSER_ID = ?");
		ps.setString(1, fdUserPk.getId());
		ps.executeUpdate();
		ps.close();

		ps = conn.prepareStatement("DELETE FROM CUST.FDCARTLINE WHERE FDUSER_ID = ?");
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

		ps =
			conn.prepareStatement(
				"INSERT INTO CUST.FDCARTLINE_CLIENTCODE (CLIENT_CODE, QUANTITY, ORDINAL, FDUSER_ID, CARTLINE_ID) values (?, ?, ?, ?, ?)");

		for (Map.Entry<String, List<ErpClientCode>> cartItem : clientCodes.entrySet()) {
			for (int i = 0; i < cartItem.getValue().size(); i++) {
				ErpClientCode ccItem = cartItem.getValue().get(i);
				ps.setString(1, ccItem.getClientCode());
				ps.setInt(2, ccItem.getQuantity());
				ps.setInt(3, i);
				ps.setString(4, fdUserPk.getId());
				ps.setString(5, cartItem.getKey());
				ps.addBatch();
			}
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
