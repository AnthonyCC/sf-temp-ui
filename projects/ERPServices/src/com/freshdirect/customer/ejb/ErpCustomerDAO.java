/**
 * 
 */
package com.freshdirect.customer.ejb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;

/**
 * @author ksriram
 *
 */
public class ErpCustomerDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7975703282135574908L;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ErpCustomerDAO.class);
	
	public static boolean isCustomerActive(Connection conn, String erpCustomerId) throws SQLException {
		boolean isCustomerActive = false;
		PreparedStatement ps = null; 
        ResultSet rs = null;
	        
	    try {
	    	if(null != erpCustomerId){
				ps = conn.prepareStatement("SELECT ACTIVE FROM CUST.CUSTOMER WHERE ID = ? ");
				ps.setString(1, erpCustomerId);
				rs = ps.executeQuery();
				if (rs.next()) {
					String active = rs.getString("ACTIVE");
					isCustomerActive = "1".equals(active);//1 is active, 0 is inactive.
				}
	    	}
		} finally {
			DaoUtil.close(rs, ps);
		}
		return isCustomerActive;
	}
	
	/**
	 * 
	 * @param conn
	 * @param erpCustomerId
	 * @return
	 * @throws SQLException
	 */
	public static List<ErpCustomerAlertModel> getCustomerAlertsByErpCustId(Connection conn, String erpCustomerId) throws SQLException{
		List<ErpCustomerAlertModel> customerAlertList = new java.util.LinkedList<ErpCustomerAlertModel>();
		PreparedStatement ps = null;		
		ResultSet rs = null;
		if(null != erpCustomerId && !"".equals(erpCustomerId.trim())){
			try {
				ps = conn.prepareStatement(	"SELECT ID, CUSTOMER_ID, ALERT_TYPE, CREATE_DATE, CREATE_USER_ID, NOTE FROM CUST.CUSTOMERALERT WHERE CUSTOMER_ID=?");
				ps.setString(1, erpCustomerId);
				rs = ps.executeQuery();
				while (rs.next()) {
					ErpCustomerAlertModel customerAlert = new ErpCustomerAlertModel();
					customerAlert.setCustomerId(rs.getString("CUSTOMER_ID"));
					customerAlert.setAlertType(rs.getString("ALERT_TYPE"));
					customerAlert.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					customerAlert.setCreateUserId(rs.getString("CREATE_USER_ID"));
					customerAlert.setNote(rs.getString("NOTE"));
					customerAlert.setPK(new PrimaryKey(rs.getString("ID")));
					customerAlertList.add(customerAlert);	
				}				
			} finally {
				DaoUtil.close(rs, ps);;
			}
		}
		return customerAlertList;
	}
	
	/**
	 * To fetch the list of customer's credits by customer id.
	 * @param conn
	 * @param erpCustomerId
	 * @return List<ErpCustomerCreditModel>
	 * @throws SQLException
	 */
	public static List<ErpCustomerCreditModel> getCustomerCreditsByErpCustId (Connection conn, String erpCustomerId) throws SQLException {
		List<ErpCustomerCreditModel> customerCredits = new ArrayList<ErpCustomerCreditModel>();
		PreparedStatement ps = null;		
		ResultSet rs = null;
		if(null != erpCustomerId && !"".equals(erpCustomerId.trim())){
			try {
				ps = conn.prepareStatement(	"SELECT ID, COMPLAINT_ID, AMOUNT, ORIGINAL_AMOUNT, DEPARTMENT, CREATE_DATE, AFFILIATE FROM CUST.CUSTOMERCREDIT WHERE CUSTOMER_ID=?");
				ps.setString(1, erpCustomerId);
				rs = ps.executeQuery();
				while (rs.next()) {
					ErpCustomerCreditModel customerCredit = new ErpCustomerCreditModel();
					customerCredit.setId(rs.getString("ID"));
					customerCredit.setComplaintPk(new PrimaryKey(rs.getString("COMPLAINT_ID")));
					customerCredit.setRemainingAmount(rs.getDouble("AMOUNT"));
					customerCredit.setOriginalAmount(rs.getDouble("ORIGINAL_AMOUNT"));
					customerCredit.setDepartment( rs.getString("DEPARTMENT"));
					customerCredit.setCreateDate(rs.getTimestamp("CREATE_DATE"));
					ErpAffiliate a = ErpAffiliate.getEnum(rs.getString("AFFILIATE"));
					customerCredit.setAffiliate(a == null ? ErpAffiliate.getEnum(ErpAffiliate.CODE_FD) : a);
					customerCredits.add(customerCredit);	
				}							
			} finally {
				DaoUtil.close(rs, ps);
			}
		}
		return customerCredits;
	}
}
