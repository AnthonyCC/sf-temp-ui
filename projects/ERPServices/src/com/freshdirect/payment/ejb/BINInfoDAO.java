package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.BINInfo;

public class BINInfoDAO {
	private static final DateFormat DATE_FORMATTER =new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat TIME_FORMATTER =new SimpleDateFormat("MM/dd/yyyy hh:MM a");
	
	private static Category LOGGER = LoggerFactory.getInstance( BINInfoDAO.class );
	
	public static String createHistoryData(Connection conn) throws SQLException	{
		PreparedStatement ps = null;
	       try {    	
	    	    ps = conn.prepareStatement("INSERT INTO CUST.BIN_HISTORY (VERSION, DATE_CREATED,STATUS) VALUES (?,SYSDATE,?)");
	    	    String version = SequenceGenerator.getNextId(conn, "CUST", "BIN_HISTORY_SEQ");
			    ps.setLong(1, Long.parseLong(version));
			    //ps.setTimestamp(2, timestamp);
			    ps.setString(2," ");
			    int rowsaffected = ps.executeUpdate();
			    if (rowsaffected != 1) {
			        throw new SQLException("Unable to create new version.  Couldn't update BIN_HISTORY table.");
			    }		    		    
			  //  ps.close();
			    return version;
	       }catch(SQLException e){
	      	 throw e;
		} finally {
			DaoUtil.close(ps);
		}
	}
	
	public static void storeBINInfo(Connection conn, List<List<BINInfo>> binInfosList) throws SQLException{
		PreparedStatement ps =null;
		//Date currentTime =new Date();
		
		String version =createHistoryData(conn);		
		ps =conn.prepareStatement(
					"INSERT INTO CUST.BIN_INFO "
					+ "(ID,RECORD_SEQ,VERSION,LOW_RANGE,HIGH_RANGE,CARD_TYPE) "
					+ "VALUES(?,?,?,?,?,?)");
		try {
			for (Iterator<List<BINInfo>> it = binInfosList.iterator(); it.hasNext();) {
				
				List<BINInfo> binInfos=it.next();
				for (Iterator<BINInfo> iterator = binInfos.iterator(); iterator.hasNext();) {
					BINInfo binInfo = (BINInfo) iterator.next();
					int i=1;
					
					ps.setString(i++, SequenceGenerator.getNextId(conn, "CUST", "BIN_INFO_SEQ"));
					ps.setString(i++, String.valueOf(binInfo.getSequence()));
					ps.setLong(i++, Long.parseLong(version));
					ps.setString(i++, String.valueOf(binInfo.getLowRange()));
					ps.setString(i++, String.valueOf(binInfo.getHighRange()));
					ps.setString(i++, binInfo.getCardType().getPaymentechCode());
					ps.addBatch();
	
				}
			}
			ps.executeBatch();
		//	ps.close();
			
		} finally {
			DaoUtil.close(ps);
		}		
	}
	
	
	public static NavigableMap<Long,BINInfo> getLatestBINInfo(Connection conn) throws SQLException{
		NavigableMap<Long,BINInfo> binInfoMap= new ConcurrentSkipListMap<Long,BINInfo>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps1 =null;
		try {
			ps = conn.prepareStatement("SELECT * FROM  CUST.BIN_INFO BI WHERE  BI.VERSION=(SELECT MAX(VERSION) "
					+ " FROM CUST.BIN_HISTORY) and BI.CARD_TYPE='VI' ");
			rs = ps.executeQuery();
			loadBINInfosFromResultSet(binInfoMap, rs);
			rs.close();
			ps.close();

			ps1 = conn.prepareStatement("SELECT * FROM  CUST.BIN_INFO BI WHERE  BI.VERSION=(SELECT MAX(VERSION) "
					+ " FROM CUST.BIN_HISTORY) and BI.CARD_TYPE='MC' ");
			rs1 = ps1.executeQuery();
			loadBINInfosFromResultSet(binInfoMap, rs1);
		} finally {
			DaoUtil.close(ps1);
			DaoUtil.close(rs1);
			DaoUtil.close(ps);
			DaoUtil.close(rs);
		}
		return binInfoMap;
	}

	private static void loadBINInfosFromResultSet(NavigableMap<Long,BINInfo> binInfoMap,
			ResultSet rs) throws SQLException {
		BINInfo binInfo = null;
		while (rs.next()) {
			
				binInfo = loadBINInfo(rs);
				binInfoMap.put(binInfo.getLowRange(), binInfo);
			}
	}
	
	private static BINInfo loadBINInfo(ResultSet rs)
			throws SQLException {
		
		String id=rs.getString("ID");
		Long sequence=Long.parseLong(rs.getString("RECORD_SEQ"));
		Long lowRange=Long.parseLong(rs.getString("LOW_RANGE"));
		Long highRange=Long.parseLong(rs.getString("HIGH_RANGE"));
		EnumCardType cardType=EnumCardType.getByPaymentechCode(rs.getString("CARD_TYPE"));
		return new BINInfo(id,lowRange,highRange,sequence,cardType);
	}
	
	
	
	public static int getMaxBINVersion(Connection conn) throws SQLException {  	
		String QUERY = "select max(version) from CUST.BIN_HISTORY";		       
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERY);			
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);			
			}
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
		}		
		return 0;
	}
	
}

