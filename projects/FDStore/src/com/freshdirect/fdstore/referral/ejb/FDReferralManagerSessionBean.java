/*
 * Created on Jun 10, 2005
 *
 */
package com.freshdirect.fdstore.referral.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.EnumReferralStatus;
import com.freshdirect.fdstore.referral.ReferralCampaign;
import com.freshdirect.fdstore.referral.ReferralChannel;
import com.freshdirect.fdstore.referral.ReferralHistory;
import com.freshdirect.fdstore.referral.ReferralObjective;
import com.freshdirect.fdstore.referral.ReferralPartner;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel;
import com.freshdirect.fdstore.referral.ReferralSearchCriteria;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author jng
 *
 */
public class FDReferralManagerSessionBean extends SessionBeanSupport  {

	private final static Category LOGGER = LoggerFactory.getInstance(FDReferralManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();
		
	public List loadAllReferralPrograms() throws FDResourceException,	RemoteException	{	
		   Connection conn =null;
		   List list=null;
		try {
			conn = this.getConnection();
			list=FDReferralManagerDAO.loadAllReferralPrograms(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
				
		return list;		
	}
	
    public List loadAllReferralChannels() throws FDResourceException,	RemoteException  {
		   Connection conn =null;
		   List list=null;
		try {
			conn = this.getConnection();
			list=FDReferralManagerDAO.loadAllReferralChannels(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}				
		return list;
    }
    
	public  List loadAllReferralpartners() throws FDResourceException,	RemoteException{
		   Connection conn =null;
		   List list=null;
		try {
			conn = this.getConnection();
			list=FDReferralManagerDAO.loadAllReferralPartners(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}			
		return list;
	}
	
	public  List loadAllReferralObjective() throws FDResourceException,	RemoteException	{
		   Connection conn =null;
		   List list=null;
		try {
			conn = this.getConnection();
			list=FDReferralManagerDAO.loadAllReferralObjective(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}				
		return list;
	
	}
	
	
	public  List loadAllReferralCampaigns() throws FDResourceException,	RemoteException{
		   Connection conn =null;
		   List list=null;
		try {
			conn = this.getConnection();
			list=FDReferralManagerDAO.loadAllReferralCampaigns(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}				
		return list;

	}
		
	
     /* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralChannel(com.freshdirect.fdstore.referral.ReferralChannel)
	 */
    public ReferralChannel createReferralChannel(ReferralChannel channel)  throws FDResourceException,  RemoteException {		
		Connection conn =null;
		PrimaryKey key=null;
		try {
			conn = this.getConnection();
			key=FDReferralManagerDAO.createReferralChannel(conn,channel);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
				
		return channel;
	}
     
     /* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralPartner(com.freshdirect.fdstore.referral.ReferralPartner)
	 */
    public ReferralPartner createReferralPartner(ReferralPartner partner)  throws FDResourceException, RemoteException {
 		
 		Connection conn =null;
 		PrimaryKey key=null;
 		try {
 			conn = this.getConnection();
 			key=FDReferralManagerDAO.createReferralpartner(conn,partner);
 		} catch (SQLException e) {
 			this.getSessionContext().setRollbackOnly();
 			throw new FDResourceException(e);
 		} finally {
 			try {
 				if (conn != null) conn.close();
 			} catch (SQLException e) {
 				LOGGER.warn("Exception while trying to close connection: " + e);
 			}
 		} 			
 		return partner;
 	}
 
     
     /* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralObjective(com.freshdirect.fdstore.referral.ReferralObjective)
	 */
    public ReferralObjective createReferralObjective(ReferralObjective objective)  throws FDResourceException,  RemoteException {
  		
  		Connection conn =null;
  		PrimaryKey key=null;
  		try {
  			conn = this.getConnection();
  			key=FDReferralManagerDAO.createReferralObjective(conn,objective);
  		} catch (SQLException e) {
  			this.getSessionContext().setRollbackOnly();
  			throw new FDResourceException(e);
  		} finally {
  			try {
  				if (conn != null) conn.close();
  			} catch (SQLException e) {
  				LOGGER.warn("Exception while trying to close connection: " + e);
  			}
  		}  				
  		return objective;
  	}
     

     /* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralCampaign(com.freshdirect.fdstore.referral.ReferralCampaign)
	 */
    public ReferralCampaign createReferralCampaign(ReferralCampaign campaign)  throws FDResourceException, RemoteException {
   		
   		Connection conn =null;
   		PrimaryKey key=null;
   		try {
   			conn = this.getConnection();
   			if(campaign.getObjective()!=null && campaign.getObjective().getPK()==null){
   				  key=FDReferralManagerDAO.createReferralObjective(conn,campaign.getObjective());
   				  campaign.getObjective().setPK(key);
   			}
   				
   			key=FDReferralManagerDAO.createReferralCampaign(conn,campaign);
   			
   		} catch (SQLException e) {
   			this.getSessionContext().setRollbackOnly();
   			throw new FDResourceException(e);
   		} finally {
   			try {
   				if (conn != null) conn.close();
   			} catch (SQLException e) {
   				LOGGER.warn("Exception while trying to close connection: " + e);
   			}
   		}  				
   		return campaign;
   	}


     /* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralProgram(com.freshdirect.fdstore.referral.ReferralProgram)
	 */
    public ReferralProgram createReferralProgram(ReferralProgram program)  throws FDResourceException, RemoteException {
    		
    		Connection conn =null;
    		PrimaryKey key=null;
    		try {
    			conn = this.getConnection();
    			if(program.getChannel() !=null && program.getChannel().getPK()==null){
    				createReferralChannel(program.getChannel());
    			}
    			if(program.getPartner() !=null && program.getPartner().getPK()==null){
    				createReferralPartner(program.getPartner());
    			}
    			if(program.getCampaign() !=null && program.getCampaign().getPK()==null){
    				createReferralCampaign(program.getCampaign());
    			}
    				
    			key=FDReferralManagerDAO.createReferralProgram(conn,program);
    			
    		} catch (SQLException e) {
    			this.getSessionContext().setRollbackOnly();
    			throw new FDResourceException(e);
    		} finally {
    			try {
    				if (conn != null) conn.close();
    			} catch (SQLException e) {
    				LOGGER.warn("Exception while trying to close connection: " + e);
    			}
    		}  				
    		return program;
    	}

     
     /* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralHistory(com.freshdirect.fdstore.referral.ReferralHistory)
	 */
    public ReferralHistory createReferralHistory(ReferralHistory history)  throws FDResourceException, RemoteException {
 		
 		Connection conn =null;
 		PrimaryKey key=null;
 		try {
 			conn = this.getConnection(); 			 			 		 				
 			key=FDReferralManagerDAO.createReferralHistory(conn,history);
 			
 		} catch (SQLException e) {
 			this.getSessionContext().setRollbackOnly();
 			throw new FDResourceException(e);
 		} finally {
 			try {
 				if (conn != null) conn.close();
 			} catch (SQLException e) {
 				LOGGER.warn("Exception while trying to close connection: " + e);
 			}
 		}  				
 		return history;
 	}
     
     
	
          			
	
	
	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#createReferralInvitee(com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel, com.freshdirect.fdstore.customer.FDUser)
	 */
	public ReferralProgramInvitaionModel createReferralInvitee(ReferralProgramInvitaionModel referral, FDUserI user)  throws FDResourceException,  RemoteException {
		Connection conn = null;
		PrimaryKey key=null; 
		try {
			LOGGER.debug("Creating the ReferralInvitee****"+referral);
			Date today = new Date();
			referral.setReferralCreatedDate(today);
			
			if(referral.getReferralProgramId() == null) {
				ReferralProgram  referralProgram = loadLastestActiveReferralProgram();
				if(referralProgram!=null && referralProgram.getPK()!=null )
				{
				    	referral.setReferralProgramId(referralProgram.getPK().getId());
				    	LOGGER.debug("setting the Referral Program Id ****"+referralProgram.getPK().getId());
				}
			}			
			conn = this.getConnection();
			LOGGER.debug("getting the connection con ****"+conn);													 			
			EnumReferralStatus status = EnumReferralStatus.REFERRED;  // defaulted to email sent
			if (isReferrerRestricted(conn, user)) {
				status = EnumReferralStatus.RESTRICTED;
			} else 	if (!isReferrerEligible(conn, user)) {
				status = EnumReferralStatus.INELIGIBLE;				
			} else 	if (isMaxedOutReferrals(conn, user)) {
				status = EnumReferralStatus.MAXED_REFERRALS;				
			} else if (isCustomer(referral.getReferrelEmailAddress())) {
				status = EnumReferralStatus.REFERRAL_ALREADY_CUST;				
			}
			referral.setStatus(status);
			LOGGER.debug("setting the referral status ****"+status);
			key=FDReferralManagerDAO.createReferral(conn, referral);
							
		} catch (SQLException e) {
			e.printStackTrace();
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} 		
		finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return referral;
	}
	
	
	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#storeReferral(com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel, com.freshdirect.fdstore.customer.FDUser)
	 */
	public void storeReferral(ReferralProgramInvitaionModel referral, FDUser user)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.storeReferral(conn, referral);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralFromPK(java.lang.String)
	 */
	public ReferralProgramInvitaionModel loadReferralFromPK(String referralId)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralFromPK(conn, new PrimaryKey(referralId));
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralsFromReferralProgramId(java.lang.String)
	 */
	public List loadReferralsFromReferralProgramId(String referralProgramId)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralsFromReferralProgramId(conn, referralProgramId);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralsFromReferrerCustomerId(java.lang.String)
	 */
	public List loadReferralsFromReferrerCustomerId(String referrerCustomerId)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralsFromReferrerCustomerId(conn, referrerCustomerId, null);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralsFromReferralEmailAddress(java.lang.String)
	 */
	public List loadReferralsFromReferralEmailAddress(String referralEmailAddress)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralsFromReferralEmailAddress(conn, referralEmailAddress);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralReportFromReferrerCustomerId(java.lang.String)
	 */
	public List loadReferralReportFromReferrerCustomerId(String referrerCustomerId)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralReportFromReferrerCustomerId(conn, referrerCustomerId);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralReportFromReferralCustomerId(java.lang.String)
	 */
	public List loadReferralReportFromReferralCustomerId(String referralCustomerId)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralReportFromReferralCustomerId(conn, referralCustomerId);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferrerNameFromReferralCustomerId(java.lang.String)
	 */
	public String loadReferrerNameFromReferralCustomerId(String referralCustomerId)  throws FDResourceException, RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferrerNameFromReferralCustomerId(conn, referralCustomerId);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadReferralProgramFromPK(java.lang.String)
	 */
	public ReferralProgram loadReferralProgramFromPK(String referralProgramId) throws FDResourceException,RemoteException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadReferralProgramFromPK(conn, new PrimaryKey(referralProgramId));
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.referral.ejb.FDReferralManagerTMPSB#loadLastestActiveReferralProgram()
	 */
	public ReferralProgram loadLastestActiveReferralProgram() throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return FDReferralManagerDAO.loadLatestActiveReferralProgram(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}
	
	private boolean isReferrerRestricted(Connection conn, FDUserI user) throws FDResourceException, SQLException {		
		if (user != null && user.isReferrerRestricted()) {
			return true;
		}
		return false;
	}
		
	private boolean isReferrerEligible(Connection conn, FDUserI user) throws FDResourceException, SQLException { 
		if (user != null && user.isReferrerEligible()) {
			return true;
		}
		return false;
	}

	private boolean isMaxedOutReferrals(Connection conn, FDUserI user) throws SQLException {
		int numDaysMaxReferrals = Integer.parseInt(FDStoreProperties.getNumDaysMaxReferrals());
		Date startDate = null;
		if (numDaysMaxReferrals > 0) {
			startDate = new Date();
			startDate =  DateUtil.addDays(DateUtil.truncate(startDate), (numDaysMaxReferrals-1)*-1);
		}
		List referralList = FDReferralManagerDAO.loadReferralsFromReferrerCustomerId(conn, user.getIdentity().getErpCustomerPK(), startDate);
		if (referralList != null) {
			// check to make sure referrer didn't reach maximum number of referrals 
			int maxReferrals = Integer.parseInt(FDStoreProperties.getMaxReferrals());
			if (referralList.size() >= maxReferrals) {
				return true;
			}
		}
		return false;
	}

	private boolean isCustomer(String userId) throws FDResourceException {
		try {
			ErpCustomerEB eb;
			try {
				eb = this.getErpCustomerHome().findByUserId(userId);
			} catch (ObjectNotFoundException ex) {
				return false;
			}
			if (getOrderCount(eb.getPK().getId()) < 1) {
				return false;
			}
			return true;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (FinderException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	private int getOrderCount(String customerId)  throws FDResourceException  {
		ErpCustomerManagerSB sb;
		try {
			sb= this.getErpCustomerManagerHome().create();
			return sb.getValidOrderCount(new PrimaryKey(customerId));
		} catch (CreateException ex) {
			throw new FDResourceException(ex) ;
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}		
	}
	
	protected String getResourceCacheKey() {
		    return "com.freshdirect.fdstore.referral.ejb.FDReferralManagerHome";
	}
	
	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer", ErpCustomerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager", ErpCustomerManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	


	

	public void updateReferralStatus(String referralId,  String status) throws FDResourceException,  RemoteException {
		// TODO Auto-generated method stub		
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.updateReferralStatus(conn, referralId,status);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}
				
	}

	public void updateReferralProgram(ReferralProgram refProgram) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.updateReferralProgram(conn, refProgram);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	public void updateReferralChannel(ReferralChannel channel) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.updateReferralChannel(conn, channel);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}

		
	}

	public void updateReferralCampaign(ReferralCampaign campaign) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.updateReferralCampaign(conn, campaign);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	public void updateReferralPartner(ReferralPartner partner) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.updateReferralPartner(conn, partner);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	
	
	public void updateReferralObjective(ReferralObjective objective) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.updateReferralObjective(conn, objective);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}
	
	public void removeReferralProgram(String refProgramId[]) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.deleteReferralProgram(conn,refProgramId);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	
	
	public void removeReferralChannel(String channelIds[]) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.deleteReferralChannel(conn, channelIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	
	public void removeReferralCampaign(String campaignIds[]) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.deleteReferralCampaign(conn, campaignIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	
	public void removeReferralPartner(String partnerIds[]) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.deleteReferralPartner(conn, partnerIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	
	public void removeReferralObjective(String objectiveIds[]) throws FDResourceException, RemoteException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = this.getConnection();
			FDReferralManagerDAO.deleteReferralObjective(conn, objectiveIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
	}

	
    public  ReferralChannel getReferralChannleModel(String refChaId) throws FDResourceException, RemoteException{
    	Connection conn = null;
    	ReferralChannel channel=null;
		try {
			conn = this.getConnection();
			channel = FDReferralManagerDAO.loadReferralChannelFromPK(conn, new PrimaryKey(refChaId));
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return channel;
    }
	
	public ReferralCampaign getReferralCampaigneModel(String refCampId) throws FDResourceException,	RemoteException {
    	Connection conn = null;
    	ReferralCampaign campaign=null;
		try {
			conn = this.getConnection();
			campaign = FDReferralManagerDAO.loadReferralCampaignFromPK(conn, new PrimaryKey(refCampId));
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return campaign;
	}
	
	public  ReferralObjective getReferralObjectiveModel(String refObjId) throws FDResourceException, RemoteException{
    	Connection conn = null;
    	ReferralObjective objective=null;
		try {
			conn = this.getConnection();
			objective = FDReferralManagerDAO.loadReferralObjectiveFromPK(conn, new PrimaryKey(refObjId));
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return objective;
	}
	
	public ReferralPartner getReferralPartnerModel(String refPartId) throws FDResourceException,RemoteException {
    	Connection conn = null;
    	ReferralPartner partner=null;
		try {
			conn = this.getConnection();
			partner = FDReferralManagerDAO.loadReferralpartnerFromPK(conn, new PrimaryKey(refPartId));
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return partner;
	}
	
	public ReferralProgram getReferralProgramModel(String refProgId) throws FDResourceException, RemoteException {
    	Connection conn = null;
    	ReferralProgram program=null;
		try {
			conn = this.getConnection();
			program = FDReferralManagerDAO.loadReferralProgramFromPK(conn, new PrimaryKey(refProgId));
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return program;
	}		
	
	
	public List getReferralProgarmforRefChannel(String refChaIds[]) throws FDResourceException, RemoteException {
		Connection conn = null;
    	List collection=null;
		try {
			conn = this.getConnection();
			collection = FDReferralManagerDAO.loadReferralProgramForChannel(conn, refChaIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return collection;
	}

	public List getReferralProgarmforRefPartner(String refPartnerIds[]) throws FDResourceException, RemoteException	{
		Connection conn = null;
    	List collection=null;
		try {
			conn = this.getConnection();
			collection = FDReferralManagerDAO.loadReferralProgramForPartner(conn, refPartnerIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return collection;
	}

	public List getReferralProgarmforRefCampaign(String refCampaignIds[]) throws FDResourceException,	RemoteException {
		Connection conn = null;
    	List collection=null;
		try {
			conn = this.getConnection();
			collection = FDReferralManagerDAO.loadReferralProgramForCampaign(conn, refCampaignIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return collection;
	}
	
	
	
	
	public  List getReferralCampaignforRefObjective(String refObjIds[]) throws FDResourceException,	RemoteException {
		
		Connection conn = null;
    	List collection=null;
		try {
			conn = this.getConnection();
			collection = FDReferralManagerDAO.loadReferralCampaignForRefObjective(conn, refObjIds);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Exception while trying to close connection: " + e);
			}
		}		
		return collection;		
	}
	
	 public boolean isReferralProgramNameExist(String refPrgName) throws FDResourceException, RemoteException {
	    	
		    Connection conn = null;
	    	boolean isExists=false;
			try {
				conn = this.getConnection();
				int count = FDReferralManagerDAO.getTotalCountOfRefProgram(conn, refPrgName);
				if(count>0)
					isExists=true;
			} catch (SQLException e) {
				this.getSessionContext().setRollbackOnly();
				throw new FDResourceException(e);
			} finally {
				try {
					if (conn != null) conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Exception while trying to close connection: " + e);
				}
			}		
			return isExists;
	    }
	    
	    public boolean isReferralChannelNameAndTypeExist(String name,String type)  throws FDResourceException,	RemoteException {
	        Connection conn = null;
	    	boolean isExists=false;
			try {
				conn = this.getConnection();
				int count = FDReferralManagerDAO.getTotalCountOfRefChannel(conn, name,type);
				if(count>0)
					isExists=true;
			} catch (SQLException e) {
				this.getSessionContext().setRollbackOnly();
				throw new FDResourceException(e);
			} finally {
				try {
					if (conn != null) conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Exception while trying to close connection: " + e);
				}
			}		
			return isExists;
	    }

	    public boolean isReferralObjectiveNameExist(String refObjName) throws FDResourceException,	RemoteException {
	    	    Connection conn = null;
		    	boolean isExists=false;
				try {
					conn = this.getConnection();
					int count = FDReferralManagerDAO.getTotalCountOfRefObjective(conn, refObjName);
					if(count>0)
						isExists=true;
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return isExists;
	    }

	    
	    public boolean isReferralCampaignNameExist(String refCampName) throws FDResourceException,	RemoteException {
	    	
	    	    Connection conn = null;
		    	boolean isExists=false;
				try {
					conn = this.getConnection();
					int count = FDReferralManagerDAO.getTotalCountOfRefCampaign(conn, refCampName);
					if(count>0)
						isExists=true;
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return isExists;
	    }

	    public boolean isReferralPartnerNameExist(String refPartName) throws FDResourceException, RemoteException {
	    	    Connection conn = null;
		    	boolean isExists=false;
				try {
					conn = this.getConnection();
					int count = FDReferralManagerDAO.getTotalCountOfRefPartner(conn, refPartName);
					if(count>0)
						isExists=true;
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return isExists;
	    }

		 public List getReferralPrograms(ReferralSearchCriteria criteria) throws FDResourceException, RemoteException{
				Connection conn = null;
		    	List collection=null;
				try {
					conn = this.getConnection();
					if(criteria.getStartIndex()==0 || criteria.getTotalRcdSize()==0){
						criteria.setTotalRcdSize(FDReferralManagerDAO.getTotalReferralProgramCount(conn));
					}
					collection = FDReferralManagerDAO.getReferralPrograms(conn,criteria);
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return collection;
		 }
		  
		 public List getReferralChannels(ReferralSearchCriteria criteria) throws FDResourceException, RemoteException{
				Connection conn = null;
		    	List collection=null;
				try {
					conn = this.getConnection();
					if(criteria.getStartIndex()==0 || criteria.getTotalRcdSize()==0){
						criteria.setTotalRcdSize(FDReferralManagerDAO.getTotalReferralChannelCount(conn));
					}
					collection = FDReferralManagerDAO.getReferralChannel(conn,criteria);
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return collection;

		 }
		  
		 public List getReferralCampaigns(ReferralSearchCriteria criteria)throws FDResourceException, RemoteException{
				Connection conn = null;
		    	List collection=null;
				try {
					conn = this.getConnection();
					if(criteria.getStartIndex()==0 || criteria.getTotalRcdSize()==0){
						criteria.setTotalRcdSize(FDReferralManagerDAO.getTotalReferralCampaignCount(conn));
					}
					collection = FDReferralManagerDAO.getReferralCampaign(conn,criteria);
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return collection;
		 }
		  
		 public List getReferralPartners(ReferralSearchCriteria criteria)throws FDResourceException, RemoteException{
				Connection conn = null;
		    	List collection=null;
				try {
					conn = this.getConnection();
					if(criteria.getStartIndex()==0 || criteria.getTotalRcdSize()==0){
						criteria.setTotalRcdSize(FDReferralManagerDAO.getTotalReferralPartnerCount(conn));
					}
					collection = FDReferralManagerDAO.getReferralPartner(conn,criteria);
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return collection;
		 }
		 
		 public List getReferralObjective(ReferralSearchCriteria criteria)throws FDResourceException, RemoteException{
				Connection conn = null;
		    	List collection=null;
				try {
					conn = this.getConnection();
					if(criteria.getStartIndex()==0 || criteria.getTotalRcdSize()==0){
						criteria.setTotalRcdSize(FDReferralManagerDAO.getTotalReferralObjectiveCount(conn));
					}
					collection = FDReferralManagerDAO.getReferralObjective(conn,criteria);
				} catch (SQLException e) {
					this.getSessionContext().setRollbackOnly();
					throw new FDResourceException(e);
				} finally {
					try {
						if (conn != null) conn.close();
					} catch (SQLException e) {
						LOGGER.warn("Exception while trying to close connection: " + e);
					}
				}		
				return collection;

		 }
	    
}
