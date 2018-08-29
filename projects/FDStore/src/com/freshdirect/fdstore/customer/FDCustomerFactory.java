package com.freshdirect.fdstore.customer;

/**
 *
 * @author  knadeem
 * @version
 */
import javax.naming.*;

import org.apache.log4j.Category;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.fdstore.customer.ejb.*;
import com.freshdirect.fdstore.ecomm.gateway.CustomerIdentityService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerInfoService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerPaymentService;
import com.freshdirect.fdstore.*;

import com.freshdirect.customer.*;
import com.freshdirect.customer.ejb.*;


public class FDCustomerFactory {

	private static Category LOGGER = LoggerFactory.getInstance(FDCustomerFactory.class);

	private static FDCustomerHome fdCustomerHome = null;
	private static ErpCustomerHome erpCustomerHome = null;
	private static ErpCustomerManagerHome erpCustomerManagerHome = null;
	private static ErpCustomerInfoHome erpCustomerInfoHome = null;
	
    private FDCustomerFactory () {
    }

	public static FDCustomerModel getFDCustomer(FDIdentity identity) throws FDResourceException {
		return getFDCustomer( identity.getFDCustomerPK() );
	}

	public static FDCustomerModel getFDCustomer(String fdCustomerId) throws FDResourceException {
		try {
			
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerFactory)) {
				LOGGER.info("FDEcom FDCustomerFactory start " + fdCustomerId );
				return CustomerIdentityService.getInstance().getFDCustomer(fdCustomerId, null);
			} else {
				LOGGER.info("EJB FDCustomerFactory start " + fdCustomerId );
				if (fdCustomerHome == null) {
					lookupFDCustomerHome();
				}
				FDCustomerEB eb = fdCustomerHome.findByPrimaryKey(new PrimaryKey(fdCustomerId));
				return (FDCustomerModel) eb.getModel();
			}
		} catch (FinderException fe) {
			fdCustomerHome = null;
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			fdCustomerHome = null;
			throw new FDResourceException(re);
		}
	}

	public static FDCustomerModel getFDCustomerFromErpId(String erpCustomerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerFactory)) {
				return CustomerIdentityService.getInstance().getFDCustomer(null, erpCustomerId);
			} else {
				if (fdCustomerHome==null) {
					lookupFDCustomerHome();
				}
				FDCustomerEB eb = fdCustomerHome.findByErpCustomerId(erpCustomerId);
				return (FDCustomerModel) eb.getModel();
			}
			
		} catch(FinderException fe) {
			fdCustomerHome=null;
			throw new FDResourceException(fe);
		} catch(RemoteException re) {
			fdCustomerHome=null;
			throw new FDResourceException(re);
		}
	}

	public static String getFDCustomerIdFromErpId(String erpCustomerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerFactory)) {
				return CustomerIdentityService.getInstance().getFDCustomer(null, erpCustomerId).getPK().getId();
			} else {
				if (fdCustomerHome == null) {
					lookupFDCustomerHome();
				}
				FDCustomerEB eb = fdCustomerHome.findByErpCustomerId(erpCustomerId);
				return ((FDCustomerModel) eb.getModel()).getPK().getId();
			}
		} catch (FinderException fe) {
			fdCustomerHome = null;
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			fdCustomerHome = null;
			throw new FDResourceException(re);
		}
	}

	public static ErpCustomerModel getErpCustomer(FDIdentity identity) throws FDResourceException {
		return getErpCustomer( identity.getErpCustomerPK() );
	}

	public static ErpCustomerModel getErpCustomer(String erpCustomerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerFactory)) {
				return CustomerIdentityService.getInstance().getErpCustomer(erpCustomerId);
			}
			if (erpCustomerHome == null) {
				lookupErpCustomerHome();
			}
			ErpCustomerEB eb = erpCustomerHome.findByPrimaryKey(new PrimaryKey(erpCustomerId));
			return (ErpCustomerModel) eb.getModel();
		} catch (FinderException fe) {
			erpCustomerHome = null;
			throw new FDResourceException(fe);
		} catch (RemoteException re) {
			erpCustomerHome = null;
			throw new FDResourceException(re);
		}
	}

	public static ErpCustomerInfoModel getErpCustomerInfo(FDIdentity identity) throws FDResourceException {
		return getErpCustomerInfo( null !=identity ? identity.getErpCustomerPK():null );
	}

	@Deprecated
	public static ErpCustomerInfoModel getErpCustomerInfo(String erpCustomerId) throws FDResourceException {
		if(null !=erpCustomerId){
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerFactory)) {
					return CustomerInfoService.getInstance().getErpCustomerInfo(erpCustomerId);
				}
				if (erpCustomerInfoHome == null) {
					lookupErpCustomerInfoHome();
				}
				ErpCustomerInfoModel customerInfo = (ErpCustomerInfoModel) erpCustomerInfoHome.findByErpCustomerId(erpCustomerId).getModel();
				return customerInfo;
			} catch(FinderException fe) {
				erpCustomerInfoHome = null;
				throw new FDResourceException(fe);
			} catch(RemoteException re) {
				erpCustomerInfoHome = null;
				throw new FDResourceException(re);
			}
		}
		return null;
	}
	
	public static List<ErpCustomerCreditModel> getCustomerCreditsByErpCustId (String erpCustomerId) throws FDResourceException{
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerFactory)) {
				return CustomerPaymentService.getInstance().getCustomerCreditsByErpCustId(erpCustomerId);
			}
			if (erpCustomerManagerHome == null) {
				lookupErpCustomerManagerHome();
			}
			ErpCustomerManagerSB erpCustMgrSb =  (ErpCustomerManagerSB)erpCustomerManagerHome.create();
			return erpCustMgrSb.getCustomerCreditsByErpCustId(erpCustomerId);
		} catch(CreateException fe) {
			erpCustomerManagerHome = null;
			throw new FDResourceException(fe);
		} catch(RemoteException re) {
			erpCustomerManagerHome = null;
			throw new FDResourceException(re);
		}
		
	}
	protected static void lookupErpCustomerInfoHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			erpCustomerInfoHome = (ErpCustomerInfoHome) ctx.lookup( FDStoreProperties.getErpCustomerInfoHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}
	protected static void lookupFDCustomerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			fdCustomerHome = (FDCustomerHome) ctx.lookup( FDStoreProperties.getFDCustomerHome() );
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}

	protected static void lookupErpCustomerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			erpCustomerHome = (ErpCustomerHome) ctx.lookup( FDStoreProperties.getErpCustomerHome() );
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}
	
	protected static void lookupErpCustomerManagerHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			erpCustomerManagerHome =  (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}

}
