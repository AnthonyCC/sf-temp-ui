package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.framework.core.PrimaryKey;

public class ErpCartonsDAO {

	public static void delete(Connection conn, PrimaryKey salePk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CARTON_DETAIL WHERE SALE_ID = ?");
		ps.setString(1, salePk.getId());
		ps.executeUpdate();
		ps.close();

		ps = conn.prepareStatement("DELETE FROM CUST.CARTON_INFO WHERE SALE_ID = ?");
		ps.setString(1, salePk.getId());
		ps.executeUpdate();
		ps.close();
	}

	public static void insert(Connection conn, List cartonInfoList) throws SQLException {
		if (cartonInfoList == null) {
			return;
		}

		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.CARTON_INFO(SALE_ID, SAP_NUMBER, CARTON_NUMBER, CARTON_TYPE) " +
							"VALUES (?,?,?,?)");
			
		for (Iterator i = cartonInfoList.iterator(); i.hasNext();) {
			ErpCartonInfo cartonInfo = (ErpCartonInfo) i.next();
			ps.setString(1, cartonInfo.getOrderNumber());
			ps.setString(2, cartonInfo.getSapNumber());
			ps.setString(3, cartonInfo.getCartonNumber());
			ps.setString(4, cartonInfo.getCartonType());
			ps.addBatch();
			
			PreparedStatement psDetails =
				conn.prepareStatement(
						"INSERT INTO CUST.CARTON_DETAIL(" +
								"SALE_ID, CARTON_NUMBER, ORDERLINE_NUMBER, " +
								"MATERIAL_NUMBER, BARCODE, ACTUAL_QUANTITY, ACTUAL_WEIGHT, SALES_UNIT" +
								") VALUES (?,?,?,?,?,?,?,?)");
			for(Iterator j = cartonInfo.getDetails().iterator(); j.hasNext();) {
				ErpCartonDetails details = (ErpCartonDetails) j.next();
				psDetails.setString(1, cartonInfo.getOrderNumber());
				psDetails.setString(2, cartonInfo.getCartonNumber());
				psDetails.setString(3, details.getOrderLineNumber());
				psDetails.setString(4, details.getMaterialNumber());
				psDetails.setString(5, details.getBarcode());
				psDetails.setDouble(6, details.getPackedQuantity());
				psDetails.setDouble(7, details.getNetWeight());
				psDetails.setString(8, details.getWeightUnit());
				psDetails.addBatch();
			}
			psDetails.executeBatch();
			
			psDetails.close();
		}

		ps.executeBatch();

		ps.close();
	}

	// @return List<ErpCartonInfo>
	public static List getCartonInfo(Connection conn, PrimaryKey salePk) throws SQLException {
		ArrayList cartons = new ArrayList();
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT CI.SALE_ID, CI.SAP_NUMBER, CI.CARTON_NUMBER, CI.CARTON_TYPE, " +
				"	CD.ORDERLINE_NUMBER, CD.MATERIAL_NUMBER, CD.BARCODE, CD.ACTUAL_QUANTITY, CD.ACTUAL_WEIGHT, CD.SALES_UNIT " +
				"FROM CUST.CARTON_INFO CI, CUST.CARTON_DETAIL CD " +
				"WHERE CI.SALE_ID = CD.SALE_ID " +
				"  AND CI.CARTON_NUMBER = CD.CARTON_NUMBER " +
				"  AND CI.SALE_ID = ? " +
				"ORDER BY TO_NUMBER(CI.CARTON_NUMBER) ASC, TO_NUMBER(CD.ORDERLINE_NUMBER) ASC");
		ps.setString(1, salePk.getId());

		ResultSet rs = ps.executeQuery();
		String currentCartonNumber = "";
		ErpCartonInfo ci = null;
		List cartonDetailList = null;
		while (rs.next()) {
			String saleId = rs.getString("SALE_ID");
			String sapNumber = rs.getString("SAP_NUMBER");
			String cartonNumber = rs.getString("CARTON_NUMBER");
			String cartonType = rs.getString("CARTON_TYPE");
			String orderlineNumber = rs.getString("ORDERLINE_NUMBER");
			String materialNumber = rs.getString("MATERIAL_NUMBER");
			String barCode = rs.getString("BARCODE");
			double actualQuantity = rs.getDouble("ACTUAL_QUANTITY");
			double actualWeight = rs.getDouble("ACTUAL_WEIGHT");
			String salesUnit = rs.getString("SALES_UNIT");

			if(!cartonNumber.equals(currentCartonNumber)) {
				ci = new ErpCartonInfo(saleId, sapNumber, cartonNumber, cartonType);
				cartonDetailList = new ArrayList();
				ci.setDetails(cartonDetailList);
				cartons.add(ci);
				currentCartonNumber = cartonNumber;
			}
			
			ErpCartonDetails cd = new ErpCartonDetails(ci, orderlineNumber, materialNumber, barCode, actualQuantity, actualWeight, salesUnit);
			cartonDetailList.add(cd);
		}

		rs.close();
		ps.close();

		return cartons;
	}
}
