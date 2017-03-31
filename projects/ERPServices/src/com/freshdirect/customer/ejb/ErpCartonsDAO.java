package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.freshdirect.customer.EnumCartonShipStatus;
import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.fdstore.EnumEStoreId;
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

	public static void insert(Connection conn, List<ErpCartonInfo> cartonInfoList) throws SQLException {
		if (cartonInfoList == null) {
			return;
		}

		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.CARTON_INFO(SALE_ID, SAP_NUMBER, CARTON_NUMBER, CARTON_TYPE) " +
							"VALUES (?,?,?,?)");
			
		for ( ErpCartonInfo cartonInfo : cartonInfoList ) {
			if (!"0000000000".equalsIgnoreCase(cartonInfo.getCartonNumber())) {
				ps.setString(1, cartonInfo.getOrderNumber());
				ps.setString(2, cartonInfo.getSapNumber());
				ps.setString(3, cartonInfo.getCartonNumber());
				ps.setString(4, cartonInfo.getCartonType());
				ps.addBatch();
			}
			PreparedStatement psDetails =
				conn.prepareStatement(
						"INSERT INTO CUST.CARTON_DETAIL(" +
								"SALE_ID, CARTON_NUMBER, ORDERLINE_NUMBER, " +
								"MATERIAL_NUMBER, BARCODE, ACTUAL_QUANTITY, ACTUAL_WEIGHT, SALES_UNIT, ORDERED_QTY, CHILD_ORLN, MAT_DESC, PACK_UOM, SHIP_STAT, SUB_MATNR, SKU_CODE" +
								") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for ( ErpCartonDetails details : cartonInfo.getDetails() ) {
				psDetails.setString(1, cartonInfo.getOrderNumber());
				psDetails.setString(2, cartonInfo.getCartonNumber());
				psDetails.setString(3, details.getOrderLineNumber());
				psDetails.setString(4, details.getMaterialNumber());
				psDetails.setString(5, details.getBarcode());
				//psDetails.setDouble(6, details.getPackedQuantity());
				psDetails.setBigDecimal(6, new java.math.BigDecimal(details.getActualQuantity()));
				//psDetails.setDouble(7, details.getNetWeight());
				psDetails.setBigDecimal(7, new java.math.BigDecimal(details.getNetWeight()));
				psDetails.setString(8, details.getWeightUnit());
				psDetails.setBigDecimal(9, (details.getOrdered_quantity()!=null)?new java.math.BigDecimal(details.getOrdered_quantity()):new java.math.BigDecimal(0));
				psDetails.setString(10, details.getChildOrderLineNo());
				psDetails.setString(11, details.getMaterialDesc());
				psDetails.setString(12, details.getPacked_uom());
				psDetails.setString(13, details.getShipping_status());
				psDetails.setString(14, details.getSub_material_number());
				psDetails.setString(15, details.getSkuCode());
				psDetails.addBatch();
			}
			psDetails.executeBatch();
			
			psDetails.close();
		}

		ps.executeBatch();

		ps.close();
	}

	public static List<String> getCartonNumbers(Connection conn, PrimaryKey salePk) throws SQLException {
		List<String> cartonNumbers = new ArrayList<String>();
		PreparedStatement ps =
			conn.prepareStatement("SELECT CI.CARTON_NUMBER FROM CUST.CARTON_INFO CI WHERE CI.SALE_ID = ? AND CI.CARTON_NUMBER <> '0000000000' ORDER BY TO_NUMBER(CI.CARTON_NUMBER) ASC");
		ps.setString(1, salePk.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			cartonNumbers.add(rs.getString("CARTON_NUMBER"));
		}
		return cartonNumbers;
	}
	
	// @return List<ErpCartonInfo>
	public static List<ErpCartonInfo> getCartonInfo(Connection conn, PrimaryKey salePk, EnumEStoreId eStoreId) throws SQLException {
		List<ErpCartonInfo> cartons = new ArrayList<ErpCartonInfo>();
		long start = System.currentTimeMillis();
		PreparedStatement ps = null;
		if(EnumEStoreId.FD.getContentId().equalsIgnoreCase(eStoreId.getContentId())){
			ps = conn.prepareStatement(
					"SELECT CD.SALE_ID, CI.SAP_NUMBER, CD.CARTON_NUMBER, CI.CARTON_TYPE, " +
					"	CD.ORDERLINE_NUMBER, CD.MATERIAL_NUMBER, CD.BARCODE, CD.ACTUAL_QUANTITY, CD.ACTUAL_WEIGHT, CD.SALES_UNIT, CHILD_ORLN, MAT_DESC, PACK_UOM, SHIP_STAT, SUB_MATNR, ORDERED_QTY, SKU_CODE " +
					"FROM CUST.CARTON_INFO CI, CUST.CARTON_DETAIL CD " +
					"WHERE CI.SALE_ID(+) = CD.SALE_ID " +
					"  AND CI.CARTON_NUMBER(+) = CD.CARTON_NUMBER " +
					"  AND CD.SALE_ID = ? " +
					"ORDER BY TO_NUMBER(CD.CARTON_NUMBER) ASC, TO_NUMBER(CD.ORDERLINE_NUMBER) ASC");
		}else{
			ps = conn.prepareStatement(
				"SELECT CI.SALE_ID, CI.SAP_NUMBER, CI.CARTON_NUMBER, CI.CARTON_TYPE, " +
				"	CD.ORDERLINE_NUMBER, CD.MATERIAL_NUMBER, CD.BARCODE, CD.ACTUAL_QUANTITY, CD.ACTUAL_WEIGHT, CD.SALES_UNIT, CHILD_ORLN, MAT_DESC, PACK_UOM, SHIP_STAT, SUB_MATNR, ORDERED_QTY, SKU_CODE " +
				"FROM CUST.CARTON_INFO CI, CUST.CARTON_DETAIL CD " +
				"WHERE CI.SALE_ID = CD.SALE_ID(+) " +
				"  AND CI.CARTON_NUMBER = CD.CARTON_NUMBER(+) " +
				"  AND CI.SALE_ID = ? " +
				"ORDER BY TO_NUMBER(CI.CARTON_NUMBER) ASC, TO_NUMBER(CD.ORDERLINE_NUMBER) ASC");
		}
		ps.setString(1, salePk.getId());

		ResultSet rs = ps.executeQuery();
		/*String currentCartonNumber = "";
		ErpCartonInfo ci = null;
		
		List<ErpCartonDetails> cartonDetailList = null;
		*/
		ErpCartonInfo currentCartonInfo = null;
		Map<String, ErpCartonInfo> cartonNoToCartonInfo = new HashMap<String, ErpCartonInfo>();
		Map<String, Map<String, List<ErpCartonDetails>>> cartonNoToHeaderLineToComponent = new HashMap<String, Map<String, List<ErpCartonDetails>>>();
		Map<String, ErpCartonDetails> headerLineItemToCartonDetails = new HashMap<String, ErpCartonDetails>();
		
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
			String child_orln = rs.getString("CHILD_ORLN");
			String mat_desc =  rs.getString("MAT_DESC");
			String pack_uom =  rs.getString("PACK_UOM");
			String ship_stat =  rs.getString("SHIP_STAT");
			String sub_matnr =  rs.getString("SUB_MATNR");
			String sku_code =  rs.getString("SKU_CODE");
			double orderedQuantity = rs.getDouble("ORDERED_QTY");
			boolean short_ship = EnumCartonShipStatus.SHORTSHIP.getCode().equalsIgnoreCase(ship_stat);
			//System.err.println(cartonNumber + "-" + sku_code + " -" + orderlineNumber + "-" + child_orln);
			if("0000000000".equalsIgnoreCase(cartonNumber) && !short_ship) { // This is a header item and will have components
				headerLineItemToCartonDetails.put(orderlineNumber, new ErpCartonDetails(null, orderlineNumber, materialNumber, barCode
						, actualQuantity, actualWeight, salesUnit, sku_code, mat_desc, short_ship, child_orln, orderedQuantity, pack_uom, ship_stat, sub_matnr));
			} else {
				if(!cartonNoToCartonInfo.containsKey(cartonNumber)) {
					cartonNoToCartonInfo.put(cartonNumber, new ErpCartonInfo(saleId, sapNumber, cartonNumber, cartonType));
				}
				currentCartonInfo = cartonNoToCartonInfo.get(cartonNumber);
				if(StringUtils.isEmpty(child_orln) || "000000".equalsIgnoreCase(child_orln)) { // This is a normal line item so don't expect components
					currentCartonInfo.getDetails().add(new ErpCartonDetails(currentCartonInfo, orderlineNumber, materialNumber, barCode
							, actualQuantity, actualWeight, salesUnit, sku_code, mat_desc, short_ship, child_orln, orderedQuantity, pack_uom, ship_stat, sub_matnr));
				} else {
					if(!cartonNoToHeaderLineToComponent.containsKey(cartonNumber)) {
						cartonNoToHeaderLineToComponent.put(cartonNumber, new HashMap<String, List<ErpCartonDetails>>());
					}
					if(!cartonNoToHeaderLineToComponent.get(cartonNumber).containsKey(orderlineNumber)) {
						cartonNoToHeaderLineToComponent.get(cartonNumber).put(orderlineNumber, new ArrayList<ErpCartonDetails>());
					}
					cartonNoToHeaderLineToComponent.get(cartonNumber).get(orderlineNumber)
					.add(new ErpCartonDetails(currentCartonInfo, child_orln, materialNumber, barCode
																	, actualQuantity, actualWeight, salesUnit, sku_code, mat_desc, short_ship, child_orln, orderedQuantity, pack_uom, ship_stat, sub_matnr));
				}
			}		
			
			/*if(!cartonNumber.equals(currentCartonNumber)) {
				ci = new ErpCartonInfo(saleId, sapNumber, cartonNumber, cartonType);
				cartonDetailList = new ArrayList<ErpCartonDetails>();
				ci.setDetails(cartonDetailList);
				cartons.add(ci);
				currentCartonNumber = cartonNumber;
			}
			
			ErpCartonDetails cd = 
					new ErpCartonDetails(ci, materialNumber, orderlineNumber, barCode, orderedQuantity, actualWeight, salesUnit, 
							sku_code, mat_desc, new ArrayList<ErpCartonDetails>(), EnumCartonShipStatus.SHORTSHIP.equals(ship_stat) , parent_orln, actualQuantity, pack_uom, ship_stat, sub_matnr); 
			cartonDetailList.add(cd);*/
		}
		

		rs.close();
		ps.close();
		
		ErpCartonDetails currentCartonDetail = null;
		ErpCartonDetails tempCartonDetail = null;
		Map<String, Boolean> firstCartonWithHLine = new HashMap<String, Boolean>();
		
		for(String headerLine : headerLineItemToCartonDetails.keySet()){
			firstCartonWithHLine.put(headerLine, Boolean.FALSE);
		}
		for(Map.Entry<String, Map<String, List<ErpCartonDetails>>> cartonEntrySet : cartonNoToHeaderLineToComponent.entrySet()) {
			
			currentCartonInfo = cartonNoToCartonInfo.get(cartonEntrySet.getKey());		
			
			for(Map.Entry<String, List<ErpCartonDetails>> headerEntrySet : cartonEntrySet.getValue().entrySet()) {			
				currentCartonDetail = new ErpCartonDetails();
				tempCartonDetail = headerLineItemToCartonDetails.get(headerEntrySet.getKey());
				if(tempCartonDetail != null) {
					currentCartonDetail.setCartonInfo(tempCartonDetail.getCartonInfo());
					currentCartonDetail.setOrderLineNumber(tempCartonDetail.getOrderLineNumber());
					currentCartonDetail.setMaterialNumber(tempCartonDetail.getMaterialNumber());
					currentCartonDetail.setBarcode(tempCartonDetail.getBarcode());
					currentCartonDetail.setMaterialDesc(tempCartonDetail.getMaterialDesc());
					currentCartonDetail.setActualQuantity(tempCartonDetail.getActualQuantity());
					currentCartonDetail.setWeightUnit(tempCartonDetail.getWeightUnit());				
					currentCartonDetail.setNetWeight(tempCartonDetail.getNetWeight());	
					currentCartonDetail.setCartonInfo(currentCartonInfo); // Initial data from SAP will not have correct carton no, so updating it back
					currentCartonDetail.getComponents().addAll(headerEntrySet.getValue());
					currentCartonDetail.setShortShipped(tempCartonDetail.isShortShipped());
					currentCartonDetail.setShipping_status(tempCartonDetail.getShipping_status());
					
					if(CollectionUtils.isNotEmpty(headerEntrySet.getValue()) && !firstCartonWithHLine.get(tempCartonDetail.getOrderLineNumber())){
						 currentCartonDetail.setFirstCartonWithORLN(true);
						 firstCartonWithHLine.put(tempCartonDetail.getOrderLineNumber(), Boolean.TRUE);
					}
					
				}				
				currentCartonInfo.getDetails().add(currentCartonDetail);				
			}
			Collections.sort(currentCartonInfo.getDetails(), new CartonDetailComparator());
		}
		cartons.addAll(cartonNoToCartonInfo.values());
		Collections.sort(cartons, new CartonComparator());

		return cartons;
	}
	
	
	
	
}

	class CartonComparator implements Comparator<ErpCartonInfo> {
		public int compare(ErpCartonInfo obj1, ErpCartonInfo obj2) {
			if(obj1.getComponentOrderLine()!=null && obj2.getComponentOrderLine()!=null){
				return obj1.getComponentOrderLine().compareTo(obj2.getComponentOrderLine());
			}
			if (obj1.getCartonNumber() != null && obj2.getCartonNumber() != null) {
				return Long.valueOf(obj1.getCartonNumber()).compareTo(Long.valueOf(obj2.getCartonNumber()));
			}
			return 0;
		}
	}
	class CartonDetailComparator implements Comparator<ErpCartonDetails> {
		public int compare(ErpCartonDetails obj1, ErpCartonDetails obj2) {
			if (obj1.getOrderLineNumber() != null && obj2.getOrderLineNumber() != null) {
				return obj1.getOrderLineNumber().compareTo(obj2.getOrderLineNumber());
			}
			return 0;
		}
	}

