package com.freshdirect.fdstore.request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class FDProductRequestDAO {

	public static void storeRequest(Connection conn, List request) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PRODUCT_REQ(ID,CUSTOMER_ID,DEPT,CATEGORY,SUB_CATEGORY,PRODUCT_NAME,STATUS,CREATE_DATE) VALUES(?, ?, ?, ?, ?, ?, 'NEW', SYSDATE)");
		
		FDProductRequest prodReq=null;
		for(int i=0;i<request.size();i++) {
			prodReq=(FDProductRequest)request.get(i);
			ps.setString(1, prodReq.getId());
			ps.setString(2, prodReq.getCustomerId());
			setField(ps,3,prodReq.getDept());
			setField(ps,4,prodReq.getCategory());
			setField(ps,5,prodReq.getSubCategory());
			setField(ps,6,prodReq.getProductName());
			ps.addBatch();
		}
		ps.executeBatch();
		ps.close();
	}
	
	private static void setField(PreparedStatement ps, int index, String value) throws SQLException {
		
		if(value==null) {
			ps.setNull(index, Types.VARCHAR);
		} else {
			ps.setString(index, value);
		}
	}

}
