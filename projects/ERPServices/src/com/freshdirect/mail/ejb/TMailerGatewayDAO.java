package com.freshdirect.mail.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.DaoUtil;

public class TMailerGatewayDAO {


	public static final String GET_TRAN_EMAIL_STATUS_SQL="select STATUS from cust.TRANS_EMAIL_MASTER where id=?";
	
	public static String getTransactionEmailStatus(Connection conn,String modelId) throws SQLException{
		String status=null;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try
		{
   	       ps = conn.prepareStatement(GET_TRAN_EMAIL_STATUS_SQL);   	          	         	      
   	       ps.setString(1, modelId);
   	       
   	       rs=ps.executeQuery();
   	          	       
   	       if(rs.next()) status=rs.getString("STATUS");
   	       
		}catch(SQLException e){
	      	 throw e;
	    } finally {
	    	DaoUtil.close(rs);
	    	DaoUtil.close(ps);
	    }

		return status;
	}
	
	
				
	
	public static final String UPDATE_TRANS_EMAIL_MASTER=" UPDATE CUST.TRANS_EMAIL_MASTER SET STATUS=?,ERROR_TYPE=?,ERROR_DESC=?,CROMOD_DATE=SYSDATE WHERE ID=? "; 
	
	public static void updateTransactionEmailInfoStatus(Connection conn,String modelId,String status,String errorType, String errorDesc) throws SQLException {
		PreparedStatement ps = null;
		try
		{
   	       ps = conn.prepareStatement(UPDATE_TRANS_EMAIL_MASTER);   	       
   	       ps.setString(1, status);
   	       if(errorType!=null && errorType.trim().length()>0)
   	            ps.setString(2, errorType);
   	       else
   	    	ps.setNull(2, Types.NULL);
   	       
   	       if(errorDesc!=null && errorDesc.trim().length()>0)
  	            ps.setString(3, errorDesc);
  	       else
  	    	ps.setNull(3, Types.NULL);
   	        ps.setString(4, modelId);
   	       
   	       int count=ps.executeUpdate();
   	          	       
   	       if(count==0) throw new SQLException("could npt update email Info");
   	       
		}catch(SQLException e){
	      	 throw e;
	    } finally {
	    	DaoUtil.close(ps);
	    }
	       
		    
	}
	
	

	public static final String INSERT_TRANS_EMAIL_FAIURE=" INSERT INTO  CUST.TRANS_EMAIL_ERROR_DETAILS (  ID,  TRANS_EMAIL_ID,  TARGET_PROG_ID, TRANS_TYPE ,STATUS ,  ERROR_TYPE,  ERROR_DESC ,  DATE_CREATED) "+ 
														 " VALUES (CUST.SYSTEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE) "; 
	
	public static void insertTransactionEmailFailureInfo(Connection conn,TEmailI mail,String status,String errorType,String errorDesc) throws SQLException {
		PreparedStatement ps =null;
		try
		{
   	       ps = conn.prepareStatement(INSERT_TRANS_EMAIL_FAIURE);   	       
   	       ps.setString(1, mail.getId());
   	       ps.setString(2, mail.getTargetProgId());
   	       ps.setString(3, mail.getEmailTransactionType());
   	       ps.setString(4, status);
   	       if(errorType!=null && errorType.trim().length()>0)
   	            ps.setString(5, errorType);
   	       else
   	    	ps.setNull(5, Types.NULL);
   	       
   	       if(errorDesc!=null && errorDesc.trim().length()>0)
  	            ps.setString(6, errorDesc);
  	       else
  	    	ps.setNull(6, Types.NULL);
   	        
   	       
   	       int count=ps.executeUpdate();
   	          	       
   	       if(count==0) throw new SQLException("could not insert email Info");
   	       
		}catch(SQLException e){
	      	 throw e;
	    } finally {
	    	DaoUtil.close(ps);
	    }
	       
		    
	}
	
	
	public static final String RESET_TRAN_EMAIL_SQL="update cust.TRANS_EMAIL_MASTER set status='FLD' where status in ('NEW','PRC') and cromod_date>sysdate-1/24 "; 
	
	public static void resetTransactionalEmailInfo(Connection conn) throws SQLException{
	    int count=0;
	    PreparedStatement ps = null;
	    try
		{
   	       ps = conn.prepareStatement(RESET_TRAN_EMAIL_SQL);   	          	          	       
   	        count=ps.executeUpdate();   	          	     
   	          	          	       
		}catch(SQLException e){
	      	 throw e;
	    }finally{
	    	DaoUtil.close(ps);
	    }
	       		   	
	}
	
	public static final String SELECT_FAILED_EMAILS_SQL="select count(*) count from cust.TRANS_EMAIL_MASTER where status='FAILED'";
	
	public static int getFailedTransactionalEmailCount(Connection conn) throws SQLException  {
	    int count=0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
   	       ps = conn.prepareStatement(SELECT_FAILED_EMAILS_SQL);   	          	       
   	        
   	       rs=ps.executeQuery();
   	       
   	       if(rs.next())  count=rs.getInt("count");
   	          	       
   	       
		}catch(SQLException e){
	      	 throw e;
	    } finally{
	    	DaoUtil.close(rs);
	    	DaoUtil.close(ps);
	    }
	       
		return count;
	}
	
	
	
	
}
