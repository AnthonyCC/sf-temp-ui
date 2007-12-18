/**
 * @author ekracoff
 * Created on Apr 14, 2005*/

package com.freshdirect.ocf.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.FDRuntimeException;

public class PromotionPopulator extends Action implements ActionI {
	private String promoId;
	
	//  for refer a friend module get the refer a friend prog id.
	private static String REFER_A_FRIEND_RED_CODE="TAF"; 
    
   
	 
	

	public PromotionPopulator() {
		super();
	}

	public String getPromoId() {
		return promoId;
	}

	public void setPromoId(String promoPk) {
		this.promoId = promoPk;
	}
		
	public void execute(final OcfTableI customers) {
		try {
			
			Connection conn = OcfDaoFactory.getInstance().getActionDao().currentSession().connection();
			insertNewPromoCustomers(conn, customers);			
			adjustPromoCustomers(conn, customers);
			updateReferralStatus(conn, customers);
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		}
		
	}
	
	private final static String INSERT_NEW_PROMO_CUST_QUERY = 
		"insert into cust.promo_customer(customer_id, promotion_id) " + 
			"select ?, ? from dual where not exists (select * from cust.promo_customer where customer_id = ? and promotion_id = ?)";	

	
	
	
	private void insertNewPromoCustomers(Connection conn, final OcfTableI customers) throws SQLException {
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement(INSERT_NEW_PROMO_CUST_QUERY);

			for (Iterator i = customers.getValuesByColumn("CUSTOMER_ID").iterator(); i.hasNext();) {							
				String custId = (String) i.next();				
				ps.setString(1, custId);
				ps.setString(2, promoId);
				ps.setString(3, custId);
				ps.setString(4, promoId);
				
				ps.addBatch();
			}
			ps.executeBatch();
		} finally {
			if (ps != null) ps.close();			
		}
	}
	
	
	private final static String UPDATE_REFERRAL_STATUS_QUERY = "UPDATE CUST.REF_PROG_INVITATION SET STATUS=?,MODIFIED_DATE=SYSDATE WHERE ID=?";	
	
	
	private void updateReferralStatus(Connection conn, final OcfTableI customers) throws SQLException
	{
		PreparedStatement ps = null;
						
		List redList=customers.getValuesByColumn("REDEMPTION_CODE");
		List referralList=customers.getValuesByColumn("REFERRAL_ID");
		
		
						
			if(redList==null || redList.size()==0 || referralList==null || referralList.size()==0)
				return;
			
			Iterator i =redList.iterator();
			Iterator referralIterator=referralList.iterator();
			if(i.hasNext() && referralIterator.hasNext())
			{
			     String redemprionCode=(String)i.next();
			     String referralId=(String)referralIterator.next();
			     if(REFER_A_FRIEND_RED_CODE.equalsIgnoreCase(redemprionCode))
			     {
			    	
			    		 try {
							ps = conn.prepareStatement(UPDATE_REFERRAL_STATUS_QUERY);
							 ps.setString(1,"REFERRER_PROMOTED");
							 ps.setString(2,referralId);	
							 int num=ps.executeUpdate();
							 // FDReferralManager.updateReferralStatus(referralId,EnumReferralStatus.REFERRER_PROMOTED.getName());
							 
							 if(num==0)
							 {
								 throw new SQLException("row not created");
							 }
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							throw e;
						}
			     }		     
			}					
   	      	 
	}
	
	private final static String ADJUST_PROMO_CUST_QUERY = 
		"UPDATE CUST.PROMO_CUSTOMER pc" +
		" SET" + 
		" pc.USAGE_CNT =" + 
		" (" +
		"	SELECT" +   
		"		DECODE(NVL(p2.IS_MAX_USAGE_PER_CUST, ' ')," +   
		"		'X', NVL(pc2.USAGE_CNT, 0) + 1, " +
		"		NULL)" +
		"	FROM CUST.PROMO_CUSTOMER pc2, CUST.PROMOTION p2" +
		"	WHERE pc2.promotion_id=p2.id" +
		"	AND pc2.customer_id = pc.customer_id" + 
		"	AND pc2.promotion_id = pc.promotion_id" + 
		" )," +
		" pc.EXPIRATION_DATE =" + 
		" ( " +
		"	SELECT" +   
		"		DECODE(NVL(p3.ROLLING_EXPIRATION_DAYS, 0)," +   
		"		0, p3.EXPIRATION_DATE," +
		"		TO_DATE(TO_CHAR(SYSDATE, 'DD-MON-YYYY'), 'DD-MON-YYYY') + p3.ROLLING_EXPIRATION_DAYS)" +
		"	FROM CUST.PROMO_CUSTOMER pc3, CUST.PROMOTION p3" +
		"	WHERE pc3.promotion_id=p3.id" +
		"	AND pc3.customer_id = pc.customer_id" +
		"	AND pc3.promotion_id = pc.promotion_id" + 
		" )" +
		"WHERE pc.customer_id = ? AND pc.promotion_id = ?";

	private void adjustPromoCustomers(Connection conn, final OcfTableI customers) throws SQLException {
		
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement(ADJUST_PROMO_CUST_QUERY);

			for (Iterator i = customers.getValuesByColumn("CUSTOMER_ID").iterator(); i.hasNext();) {
				String custId = (String) i.next();
				ps.setString(1, custId);
				ps.setString(2, promoId);
				
				ps.addBatch();
			}
			ps.executeBatch();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (ps != null) ps.close();			
		}

	}
	
}