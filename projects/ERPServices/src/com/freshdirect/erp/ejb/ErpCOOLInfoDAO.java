package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.StringUtil;

public class ErpCOOLInfoDAO implements java.io.Serializable {
	
	private static final long	serialVersionUID	= -1514506712650886097L;
	
	private static final String INSERT_COOL_INFO="INSERT INTO ERPS.COOL_INFO (ID, MAT_SAP_ID, MAT_SAP_DESC,COUNTRY1,COUNTRY2,COUNTRY3,COUNTRY4,COUNTRY5,DATE_MODIFIED,PLANT_ID) VALUES (?,?,?,?,?,?,?,?,SYSDATE,?)";
	private static final String UPDATE_COOL_INFO="UPDATE ERPS.COOL_INFO SET COUNTRY1=?,COUNTRY2=?,COUNTRY3=?,COUNTRY4=?,COUNTRY5=?, DATE_MODIFIED=SYSDATE WHERE MAT_SAP_ID=? AND PLANT_ID=?";
	private static final String SELECT_COOL_INFO_FOR_MATERIAL="SELECT COUNTRY1,COUNTRY2,COUNTRY3,COUNTRY4,COUNTRY5 FROM ERPS.COOL_INFO WHERE MAT_SAP_ID=? AND PLANT_ID=?";
	private static final String LOAD_COOL_INFO="SELECT  MAT_SAP_ID,MAT_SAP_DESC,COUNTRY1,COUNTRY2,COUNTRY3,COUNTRY4,COUNTRY5,DATE_MODIFIED,ID,PLANT_ID FROM ERPS.COOL_INFO WHERE DATE_MODIFIED > ? ORDER BY ID ";
	private static final int MAX_COUNTRY=5;
	
	private static final Map<ErpCOOLKey,ErpCOOLInfo> EMPTY_MAP=new HashMap<ErpCOOLKey,ErpCOOLInfo>();
	
	public Map<ErpCOOLKey,ErpCOOLInfo> load(Connection conn, Date since)throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<ErpCOOLKey, ErpCOOLInfo> erpCOOLInfo=null;
		try {			
			ps = conn.prepareStatement(LOAD_COOL_INFO);
			ps.setTimestamp(1, new Timestamp(since.getTime()));
			rs = ps.executeQuery();
			erpCOOLInfo= getErpCOOLInfo(rs);						
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}
		return erpCOOLInfo==null?EMPTY_MAP:erpCOOLInfo;
	}
	
	private Map<ErpCOOLKey, ErpCOOLInfo> getErpCOOLInfo(ResultSet rs) throws SQLException {
		
		Map<ErpCOOLKey, ErpCOOLInfo> erpCOOLInfo=null;
		ErpCOOLInfo info=null;
		ErpCOOLKey key=null;
		while(rs.next()) {
			if(erpCOOLInfo==null)
				erpCOOLInfo=new HashMap<ErpCOOLKey, ErpCOOLInfo>();
			info=new ErpCOOLInfo(rs.getString(1),rs.getString(2),getCountryInfo(rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)),rs.getTimestamp(8),rs.getString(10));
			key= new ErpCOOLKey(info.getSapID(),info.getPlantId());
			erpCOOLInfo.put(key, info);
		}
		return erpCOOLInfo;
	}

	private List<String> getCountryInfo(String country1, String country2, String country3, String country4, String country5) {
		List<String> countryInfo=new ArrayList<String>(3);
		if(!StringUtil.isEmpty(country1))
			countryInfo.add(country1);
		if(!StringUtil.isEmpty(country2))
			countryInfo.add(country2);
		if(!StringUtil.isEmpty(country3))
			countryInfo.add(country3);
		if(!StringUtil.isEmpty(country4))
			countryInfo.add(country4);
		if(!StringUtil.isEmpty(country5))
			countryInfo.add(country5);
		return countryInfo;
	}
	
	public void updateCOOLInfo(Connection conn,List<ErpCOOLInfo> erpCOOLInfo) throws SQLException {
		
		String matID="";
		for ( ErpCOOLInfo info : erpCOOLInfo ) {
			System.out.println(info);
			matID=info.getSapID();
			if(exists(conn, matID, info.getPlantId())) {
				update(conn,info);
			} else {
				insert(conn,info);
			}
		}		
	}
	
	private void insert(Connection conn, ErpCOOLInfo info) throws SQLException {
		PreparedStatement ps = null;
		try {			
			String id=getNextId(conn,"ERPS");
			ps = conn.prepareStatement(INSERT_COOL_INFO);
			ps.setString(1, id);
			ps.setString(2,info.getSapID());
			ps.setString(3,info.getSapDesc());
			int size=info.getCountryInfo().size();
			size=(size>MAX_COUNTRY)?MAX_COUNTRY:size;
			String country="";
			for(int i=0;i<MAX_COUNTRY;i++) {
				country=(i<size)?info.getCountryInfo().get(i).toString():"";
				ps.setString(i+4,country );
			}			
			ps.setString(9, info.getPlantId());
			ps.executeUpdate();
		} finally {
			if (ps != null) ps.close();
		}
		
	}

	private void update(Connection conn, ErpCOOLInfo info) throws SQLException {
		PreparedStatement ps = null;
		try {			
			ps = conn.prepareStatement(UPDATE_COOL_INFO);
			int size=info.getCountryInfo().size();
			size=(size>MAX_COUNTRY)?MAX_COUNTRY:size;
			String country="";
			for(int i=0;i<MAX_COUNTRY;i++) {
				country=(i<size)?info.getCountryInfo().get(i).toString():"";
				ps.setString(i+1,country );
			}			
			ps.setString(MAX_COUNTRY+1, info.getSapID());
			ps.setString(MAX_COUNTRY+2, info.getPlantId());
			ps.executeUpdate();
		} finally {
			if (ps != null) ps.close();
		}		
	}

	private boolean exists(Connection conn,String sapID, String plantId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			ps = conn.prepareStatement(SELECT_COOL_INFO_FOR_MATERIAL);
			ps.setString(1, sapID);
			ps.setString(2, plantId);
			rs = ps.executeQuery();
			return rs.next() ? true : false;					
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
		}
	}

	private String getNextId(Connection conn, String schema) throws SQLException {
		return SequenceGenerator.getNextId(conn, schema);
    }

}
