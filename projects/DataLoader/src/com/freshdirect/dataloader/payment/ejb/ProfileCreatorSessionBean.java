package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.dataloader.payment.ProfileCreatorOutput;
import com.freshdirect.dataloader.payment.ProfileCreatorOutputDetail;
import com.freshdirect.dataloader.payment.dao.ProfileCreatorDAO;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.impl.GatewayFactory;

public class ProfileCreatorSessionBean extends SessionBeanSupport {
	
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	private final static Category LOGGER = LoggerFactory.getInstance(ProfileCreatorSessionBean.class);
	
	public void createProfiles(String batchId) throws SQLException, FDResourceException   {
		
		FDCustomerManagerSB sb = this.getFDCustomerManagerSB();
		FDIdentity identity=null;
		Collection<ErpPaymentMethodI> paymentMethods=null;
		Connection conn = getConnection();
		List<String> customers = ProfileCreatorDAO.getCustomersByBatchId(conn, batchId);
		LOGGER.info( "processing profile creation for batch id: "+batchId+" Total no of customers: " + customers.size());
		String paymentMethodId="";
		String profileId="";
		List<ProfileCreatorOutput> outputList = new ArrayList<ProfileCreatorOutput>();
		Date insertTimestamp = new Date();
		for(String customer:customers) {
				
			ProfileCreatorOutput output = null;
			LOGGER.info("Processing customerID : "+customer+" for profile creation.");
			identity=getFDIdentity(customer);
			if(conn.isClosed())
				conn=getConnection();
			boolean hasProfile  = 
					ProfileCreatorDAO.getCustomerProfile(conn, identity.getFDCustomerPK(), "siteFeature.Paymentech");
			
			if(!hasProfile){
			
			try {
				LOGGER.info("Attempting to load payment methods for customerID : "+customer);
				paymentMethods=sb.getPaymentMethods(identity);
			}catch(Exception e) {
				e.printStackTrace();
				output = new ProfileCreatorOutput(customer, insertTimestamp, false, "FAILED to load payment methods for customerID : "+customer+" with exception :"+e.toString());
				paymentMethods = Collections.emptyList();
			}
			LOGGER.info("CustomerID : "+customer+" has "+paymentMethods!=null?paymentMethods.size():0 +" payment methods in their account.");
			
			
			boolean error = false;
			List<ProfileCreatorOutputDetail> outputDtlList = new ArrayList<ProfileCreatorOutputDetail>();
			
			for(ErpPaymentMethodI paymentMethod: paymentMethods) {
				
				paymentMethodId=paymentMethod.getPK().getId();
				profileId=paymentMethod.getProfileID();
				try {
					if(( EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())||
					     EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())
					    )&& 
					    (StringUtil.isEmpty(profileId))
					   ){
						LOGGER.info("Attempting to add profile for paymentMethodID :"+paymentMethodId+" of Type :"+paymentMethod.getPaymentMethodType());
						Request request=GatewayAdapter.getAddProfileRequest(paymentMethod);
						Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
						Response response=gateway.addProfile(request);
						if(response!=null&& response.isRequestProcessed()) {
							if(response.getBillingInfo()!=null && response.getBillingInfo().getPaymentMethod()!=null) {
								profileId=response.getBillingInfo().getPaymentMethod().getBillingProfileID();
								outputDtlList.add(new ProfileCreatorOutputDetail(customer, insertTimestamp, paymentMethodId, profileId, "SUCCESS", null));
								LOGGER.info("Profile addition SUCCESS for paymentMethodID :"+paymentMethodId+" with ProfileID :"+profileId);
							} else {
								outputDtlList.add(new ProfileCreatorOutputDetail(customer, insertTimestamp, paymentMethodId, null, "FAILURE", response.getResponseCode()+"-"+response.getStatusMessage()));
								LOGGER.info("Profile addition FAILURE for paymentMethodID :"+paymentMethodId);
								error = true;
							}
						} else {
							LOGGER.info("Profile addition FAILURE for paymentMethodID :"+paymentMethodId);
							outputDtlList.add(new ProfileCreatorOutputDetail(customer, insertTimestamp, paymentMethodId, null, "FAILURE", "response is null or request not processed by gateway"));
							error = true;
						}
					}else{
						outputDtlList.add(new ProfileCreatorOutputDetail(customer, insertTimestamp, paymentMethodId, null, "SKIPPED", null));
					}
				} catch(Exception e) {
					e.printStackTrace();
					LOGGER.info("ERROR is processing paymentMethodID :"+paymentMethodId);
					LOGGER.info("Profile addition FAILURE for paymentMethodID :"+paymentMethodId);
					error = true;
				}
			}
			if(!error) {				//Add these profileIds to database.
				output = new ProfileCreatorOutput(customer, insertTimestamp, true, null);
			}else if(output==null){
				output = new ProfileCreatorOutput(customer, insertTimestamp, false, null);
			}
			output.setDetails(outputDtlList);
			
					UserTransaction utx = null;
					//conn =null;
					int rowsAffected =0;
					try {
						utx = this.getSessionContext().getUserTransaction();
						utx.begin();
						if(conn==null)
							conn =getConnection();
	         	    	PreparedStatement ps = conn.prepareStatement("UPDATE CUST.PAYMENTMETHOD PM set PM.PROFILE_ID=?,PM.account_num_masked='XXXXXXXXXXXX'||substr( PM.ACCOUNT_NUMBER,LENGTH(PM.account_number)-3,4) WHERE PM.ID=?");
	         	    	for(ProfileCreatorOutputDetail detail : outputDtlList){
	         	    		if(detail.getProfileId()!=null){
	         	    		    ps.setString(1,detail.getProfileId());
	         	    		    ps.setString(2, detail.getPaymentMethodId());
	         	    		    ps.addBatch();
	         	    		    rowsAffected++;
	         	    		}
	         	    	   }
	         	    	if(rowsAffected>0) {
	         	    		ps.executeBatch();
	         	    		ps.close();
	         	    	}
	         	    	
	         	    	if(!error){ 
		         		   ps = conn.prepareStatement("INSERT INTO CUST.PROFILE(CUSTOMER_ID, PROFILE_TYPE, PROFILE_NAME, PROFILE_VALUE, PRIORITY) VALUES (?,?,?,?,?)");
		         		   ps.setString(1,  identity.getFDCustomerPK());
		         		   ps.setString(2, "S");
		         		   ps.setString(3, "siteFeature.Paymentech");
		         		   ps.setString(4,"true");
		         		   ps.setInt(5,-1); 
		         		   ps.execute();
		         		   ps.close();
	         		   }
	         	    	
	         	    	store(conn, output);
	         	    	
	         	    	utx.commit();
	         		 
				  } catch (Exception e) {
					LOGGER.warn("Exception occured", e);
					if (utx != null) {
						try {
							utx.rollback();
						} catch (SystemException se) {
							LOGGER.warn("Error while trying to rollback transaction", se);
						}
					}
				   } /*finally {
					   if(conn!=null) {
							try {
								conn.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					   }
				   }*/
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	   }
	}
	private void store(Connection conn, ProfileCreatorOutput output) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO MIS.PAYMENT_MIGRATION_OUTPUT(CUSTOMER_ID, INSERT_TIMESTAMP, PROFILE_CREATED) VALUES(?,?,?)");
		ps.setString(1, output.getCustomerId());
		ps.setTimestamp(2, new java.sql.Timestamp(output.getInsertTimestamp().getTime()));
		ps.setString(3, output.isProfileCreated()?"X":"");
		ps.execute();
		ps.close();
		
		ps = conn.prepareStatement("INSERT INTO MIS.PAYMENT_MIGRATION_OUTPUT_DTL(CUSTOMER_ID, INSERT_TIMESTAMP, PAYMENTMETHOD_ID, STATUS, EXCEPTION_MSG) VALUES(?,?,?,?,?)");
		
		for(ProfileCreatorOutputDetail detail: output.getDetails()){
		ps.setString(1, detail.getCustomerId());
		ps.setTimestamp(2, new java.sql.Timestamp(detail.getInsertTimestamp().getTime()));
		ps.setString(3, detail.getPaymentMethodId());
		ps.setString(4, detail.getStatus());
		ps.setString(5, detail.getExceptionMsg());
		ps.addBatch();
		}
		if(output.getDetails().size()>0) { ps.executeBatch();ps.close();}
		
	}
	private FDIdentity getFDIdentity(String erpCustomerID) throws FDResourceException {
		return new FDIdentity(erpCustomerID,FDCustomerFactory.getFDCustomerIdFromErpId(erpCustomerID));
	}
	private FDCustomerManagerSB getFDCustomerManagerSB() {
		try {
			FDCustomerManagerHome home = (FDCustomerManagerHome) LOCATOR.getRemoteHome(
				"java:comp/env/ejb/FDCustomerManager");
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}
}
