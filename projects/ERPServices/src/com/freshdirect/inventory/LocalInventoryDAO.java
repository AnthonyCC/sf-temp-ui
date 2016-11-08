package com.freshdirect.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapOrderLineI;

public class LocalInventoryDAO {
	private static final String FETCH_INVENTORY_BY_MATERIAL_PLANT = "SELECT * FROM ERPS.INVENTORY_ENTRY I WHERE I.PLANT_ID=? AND I.MATERIAL_SAP_ID ";
	
	private static final String DELETE_SALE_INVENTORY_BY_MATERIAL_DATE = "DELETE FROM CUST.SALE_MATERIAL WHERE SALE_ID = ?";
	
	private static final String COMMIT_SALE_INVENTORY_BY_MATERIAL_DATE_AND_PLANT = "INSERT INTO CUST.SALE_MATERIAL(SALE_ID, MATERIAL_SAP_ID, INSERT_TIMESTAMP, REQUESTED_DATE, QUANTITY,PLANT_ID) VALUES (?,?,?,?,?,?)";
	
	private static final String COUNT_SALE_INVENTORY_BY_MATERIAL_REQ_DATE_AND_PLANT = "SELECT SUM(QUANTITY) INVCOUNT, MATERIAL_SAP_ID FROM CUST.SALE_MATERIAL WHERE REQUESTED_DATE <= ? AND PLANT_ID=? AND MATERIAL_SAP_ID ";
	
	public SapOrderI checkLocalAvailability(Connection conn, SapOrderI sapOrder) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		    String plantId = sapOrder.getPlant();
		Date requestedDate = sapOrder.getRequestedDate();
		List<String> materialList = new ArrayList<String>();
		StringBuffer strBuf = new StringBuffer(FETCH_INVENTORY_BY_MATERIAL_PLANT);
		for (int i = 0; i < sapOrder.numberOfOrderLines(); i++) {
			SapOrderLineI orderLine = sapOrder.getOrderLine(i);
			materialList.add(orderLine.getMaterialNumber());
		}
		strBuf.append(StringUtil.formQueryString(materialList));
		strBuf.append(" ORDER BY START_DATE ASC");
		ps = conn.prepareStatement(strBuf.toString());
		ps.setString(1, sapOrder.getPlant());
		rs = ps.executeQuery();
		String materialNumber = null;
		Map<String, List<ErpInventoryEntryModel>> materialMap = new HashMap<String, List<ErpInventoryEntryModel>>();
		while(rs.next()){
			materialNumber = rs.getString("MATERIAL_SAP_ID");
			if(!materialMap.containsKey(materialNumber)){
				materialMap.put(materialNumber, new ArrayList<ErpInventoryEntryModel>());
			}
			materialMap.get(materialNumber).add(new ErpInventoryEntryModel(rs.getDate("START_DATE"), rs.getDouble("QUANTITY"), plantId));
		}
			
		strBuf = new StringBuffer(COUNT_SALE_INVENTORY_BY_MATERIAL_REQ_DATE_AND_PLANT);
		strBuf.append(StringUtil.formQueryString(materialList));
		strBuf.append(" GROUP BY MATERIAL_SAP_ID");
		ps = conn.prepareStatement(strBuf.toString());
		ps.setDate(1, new java.sql.Date(requestedDate.getTime()));
		ps.setString(2, plantId);

		rs = ps.executeQuery();
		
		Map<String, Integer> materialCountMap = new HashMap<String, Integer>();
		while(rs.next()){
			materialCountMap.put(rs.getString("MATERIAL_SAP_ID"), rs.getInt("INVCOUNT"));
		}
		
		for (int i = 0; i < sapOrder.numberOfOrderLines(); i++) {
			SapOrderLineI orderLine = sapOrder.getOrderLine(i);
			double requestedQty = orderLine.getQuantity();
			double cumlQty =0;
			
			List<ErpInventoryEntryModel> olInv = new ArrayList<ErpInventoryEntryModel>();
			List<ErpInventoryEntryModel> masterInv = materialMap.get(orderLine.getMaterialNumber());
			if(masterInv!=null) {
				Iterator<ErpInventoryEntryModel> it = masterInv.iterator();
				while(it.hasNext()) {
					ErpInventoryEntryModel entry = it.next();
					if(!entry.getStartDate().after(requestedDate))
						cumlQty = entry.getQuantity() + cumlQty; // Now its cumulative (As Per Pradeep's request on May15)
				}
				if(materialCountMap.containsKey(orderLine.getMaterialNumber())){
					cumlQty = cumlQty -materialCountMap.get(orderLine.getMaterialNumber());
				}

				if(cumlQty >= requestedQty) {
					olInv.add(new ErpInventoryEntryModel(requestedDate, requestedQty, plantId));
				} else {
					olInv.add(new ErpInventoryEntryModel(requestedDate, cumlQty >= 0 ? cumlQty : 0, plantId));
				}
			} else { // if the material is not in inventory table then consider it to be unlimited.
				olInv.add(new ErpInventoryEntryModel(requestedDate, requestedQty, plantId));
			}
			
			
				
			List<ErpInventoryModel> inventories = new ArrayList<ErpInventoryModel>(1);
			inventories.add(new ErpInventoryModel(orderLine.getMaterialNumber(), new Date(), olInv));
			orderLine.setInventories(inventories);
		}
		
		return sapOrder;
		}finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		
	}
	public void releaseLocalInventory(Connection conn, String saleId) throws SQLException{

		PreparedStatement ps = null;
		try {
		ps = conn.prepareStatement(DELETE_SALE_INVENTORY_BY_MATERIAL_DATE);
		ps.setString(1, saleId);
		ps.execute();
		}finally {
			if (ps != null)
				ps.close();
		}
	}
	
	public void commitLocalInventory(Connection conn, SapOrderI sapOrder) throws SQLException{

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		
		String materialNumber; double commitQuantity;
		Map<String, Double> matMap = new HashMap<String, Double>();
		for (int i = 0; i < sapOrder.numberOfOrderLines(); i++) {
			SapOrderLineI orderLine = sapOrder.getOrderLine(i);
			materialNumber = orderLine.getMaterialNumber();
			commitQuantity = orderLine.getQuantity();
			if(matMap.containsKey(materialNumber)){
				matMap.put(materialNumber, matMap.get(materialNumber)+commitQuantity);
			}else{
				matMap.put(materialNumber, commitQuantity);
			}
		}

		releaseLocalInventory(conn, sapOrder.getWebOrderNumber());
		
		ps = conn.prepareStatement(COMMIT_SALE_INVENTORY_BY_MATERIAL_DATE_AND_PLANT);
		for(String matNumber : matMap.keySet()){
			ps.setString(1, sapOrder.getWebOrderNumber());
			ps.setString(2, matNumber);
			Calendar cal = Calendar.getInstance();
			ps.setTimestamp(3, new java.sql.Timestamp(cal.getTimeInMillis()));
			ps.setDate(4, new java.sql.Date(sapOrder.getRequestedDate().getTime()));
			ps.setDouble(5, matMap.get(matNumber));
			ps.setString(6, sapOrder.getPlant());

			ps.addBatch();
		}
		ps.executeBatch();
		
		/*for(String matNumber : matMap.keySet()){
			ps = conn.prepareStatement(FETCH_INVENTORY_BY_MATERIAL);
			ps.setString(1, matNumber);
			rs = ps.executeQuery();
			List<ErpInventoryEntryModel> masterInv = new ArrayList<ErpInventoryEntryModel>();
			while(rs.next()){
				masterInv.add(new ErpInventoryEntryModel(rs.getDate("START_DATE"), rs.getDouble("QUANTITY")));
			}
			double requestedQty = matMap.get(matNumber);
			Iterator<ErpInventoryEntryModel> it = masterInv.iterator();
			List<ErpInventoryEntryModel> updatedMasterInv = new ArrayList<ErpInventoryEntryModel>();
			while(it.hasNext()){
				ErpInventoryEntryModel entry = it.next();
				if(!entry.getStartDate().after(sapOrder.getRequestedDate())){
					updatedMasterInv.add(new ErpInventoryEntryModel(entry.getStartDate(), (entry.getQuantity() - requestedQty)>=0?entry.getQuantity() - requestedQty:0));
					requestedQty = requestedQty - entry.getQuantity();
					if(requestedQty<=0) break;
				}
			}
			ps = conn.prepareStatement(UPDATE_INVENTORY_BY_MATERIAL_DATE);
			for(ErpInventoryEntryModel updatedInventory : updatedMasterInv){
				ps.setDouble(1, updatedInventory.getQuantity());
				ps.setString(2, matNumber);
				ps.setDate(3, new java.sql.Date(updatedInventory.getStartDate().getTime()));
				ps.addBatch();
			}
			ps.executeBatch();
		}*/
		
		}finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
	}
}
