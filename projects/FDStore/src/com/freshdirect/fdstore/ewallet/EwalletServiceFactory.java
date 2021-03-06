package com.freshdirect.fdstore.ewallet;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.fdstore.ecomm.gateway.EwalletService;
import com.freshdirect.fdstore.ewallet.ejb.EwalletServiceHome;
import com.freshdirect.fdstore.ewallet.ejb.EwalletServiceSB;
import com.freshdirect.fdstore.ewallet.impl.MasterpassRuntimeException;
import com.freshdirect.fdstore.ewallet.impl.ejb.MasterpassServiceHome;
import com.freshdirect.fdstore.ewallet.impl.ejb.MasterpassServiceSB;
import com.freshdirect.fdstore.ewallet.impl.ejb.PayPalServiceHome;
import com.freshdirect.fdstore.ewallet.impl.ejb.PayPalServiceSB;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletServiceFactory {

	private static final Logger LOG = Logger.getLogger(EwalletServiceFactory.class);

	/**
	 * @param ewalletRequestData
	 * @return Returns the Ewallet service instance based on the Ewallet type
	 */
	public IEwallet getEwalletService(EwalletRequestData ewalletRequestData) {
		LOG.info("EwalletServiceFactory --> getEwalletService -> Entered ");
		IEwallet ewallet = null;
		if (ewalletRequestData != null && (EnumEwalletType.MP.getName().equals(ewalletRequestData.geteWalletType())
				|| EnumEwalletType.PP.getName().equals(ewalletRequestData.geteWalletType()))) {
			return new EwalletServiceFactory().getVendorService(ewalletRequestData);
		}
		LOG.info("EwalletServiceFactory --> getEwalletService -> Exit ");
		return ewallet;
	}

	/**
	 * @param ewalletRequestData
	 * @return Returns the Ewallet service instance based on the Ewallet type
	 */
	public IEwallet getVendorService(EwalletRequestData ewalletRequestData) {
		LOG.info("EwalletServiceFactory --> getVendorService(ewalletRequestData) -> Entered ");
		IEwallet ewallet = null;
		if (ewalletRequestData != null && EnumEwalletType.MP.getName().equals(ewalletRequestData.geteWalletType())) {
			ewallet = new MPVendorServiceRemoteAdapter();
		} else if (ewalletRequestData != null
				&& EnumEwalletType.PP.getName().equals(ewalletRequestData.geteWalletType())) {
			ewallet = new PPVendorServiceRemoteAdapter();
		}
		LOG.info("EwalletServiceFactory --> getVendorService(ewalletRequestData) -> Exit ");
		return ewallet;
	}

	/**
	 * @param ewalletRequestData
	 * @return Returns the Ewallet service instance based on the Ewallet type
	 */
	public IEwallet getVendorService(EnumEwalletType type) {
		LOG.info("EwalletServiceFactory --> getVendorService -> Entered ");
		IEwallet ewallet = null;
		if (EnumEwalletType.MP.equals(type)) {
			ewallet = new MPVendorServiceRemoteAdapter();
		}
		LOG.info("EwalletServiceFactory --> getVendorService -> Exit ");
		return ewallet;
	}

	/**
	 * @param ewalletRequestData
	 * @return Returns the MPVendorServiceRemoteAdapter instance based on the
	 *         Ewallet type
	 */
	public IEwallet.NotificationService getVendorNotificationService(EwalletRequestData ewalletRequestData) {
		LOG.info("EwalletServiceFactory -->  getVendorNotificationService -> Entered ");
		IEwallet.NotificationService ewallet = null;
		if (EnumEwalletType.MP.getName().equals(ewalletRequestData.geteWalletType())) {
			ewallet = new MPVendorServiceRemoteAdapter();
		}
		LOG.info("EwalletServiceFactory --> getEwalletService -> Exit ");
		return ewallet;
	}

	/**
	 * @param ewalletRequestData
	 * @return Returns the Ewallet service instance based on the Ewallet type
	 */
	public IEwallet.NotificationService getVendorNotificationService(EnumEwalletType type) {
		LOG.info("EwalletServiceFactory -->  getVendorNotificationService -> Entered ");
		IEwallet.NotificationService ewallet = null;
		if (EnumEwalletType.MP.equals(type)) {
			ewallet = new MPVendorServiceRemoteAdapter();
		}
		LOG.info("EwalletServiceFactory --> getVendorNotificationService -> Exit ");
		return ewallet;
	}

	private class MPVendorServiceRemoteAdapter implements IEwallet, IEwallet.NotificationService {

		private MasterpassServiceHome remoteHome = null;

		MPVendorServiceRemoteAdapter() {
			remoteHome = FDServiceLocator.getInstance().getMasterpassServiceHome();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.freshdirect.fdstore.ewallet.IEwallet#getToken(com.freshdirect.fdstore.
		 * ewallet.EwalletRequestData)
		 */
		@Override
		public EwalletResponseData getToken(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MasterpassServiceSB)) {
					resp = EwalletService.getInstance().getToken(ewalletRequestData);
				} else {
					MasterpassServiceSB remote = remoteHome.create();
					resp = remote.getToken(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.freshdirect.fdstore.ewallet.IEwallet#disconnect(com.freshdirect.fdstore.
		 * ewallet.EwalletRequestData)
		 */
		@Override
		public EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MasterpassServiceSB)) {
					resp = EwalletService.getInstance().disconnect(ewalletRequestData);
				} else {
					MasterpassServiceSB remote = remoteHome.create();
					resp = remote.disconnect(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.freshdirect.fdstore.ewallet.IEwallet#disconnect(com.freshdirect.fdstore.
		 * ewallet.EwalletRequestData)
		 */
		public EwalletResponseData postbackTrxns(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MasterpassServiceSB)) {
					resp = EwalletService.getInstance().postbackTrxns(ewalletRequestData);
				} else {
					MasterpassServiceSB remote = remoteHome.create();
					resp = remote.postback(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		@Override
		public EwalletResponseData preStandardCheckout(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MasterpassServiceSB)) {
					resp = EwalletService.getInstance().preStandardCheckout(ewalletRequestData);
				} else {
					MasterpassServiceSB remote = remoteHome.create();
					resp = remote.preStandardCheckout(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		@Override
		public EwalletResponseData standardCheckout(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MasterpassServiceSB)) {
					resp = EwalletService.getInstance().standardCheckout(ewalletRequestData);
				} else {
					MasterpassServiceSB remote = remoteHome.create();
					resp = remote.standardCheckout(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		@Override
		public EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws Exception {
			return null;
		}

	}

	private class PPVendorServiceRemoteAdapter implements IEwallet, IEwallet.NotificationService {

		private PayPalServiceHome remoteHome = null;

		PPVendorServiceRemoteAdapter() {
			remoteHome = FDServiceLocator.getInstance().getPayPalServiceHome();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.freshdirect.fdstore.ewallet.IEwallet#getToken(com.freshdirect.fdstore.
		 * ewallet.EwalletRequestData)
		 */
		@Override
		public EwalletResponseData getToken(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {

				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PayPalServiceSB)) {
					resp = EwalletService.getInstance().getToken(ewalletRequestData);
				} else {
					PayPalServiceSB remote = remoteHome.create();
					resp = remote.getToken(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		@Override
		public EwalletResponseData postbackTrxns(EwalletRequestData ewalletRequestData) throws Exception {
			return null;
		}
		@Override
		public EwalletResponseData disconnect(EwalletRequestData ewalletRequestData) {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PayPalServiceSB)) {
					resp = EwalletService.getInstance().disconnect(ewalletRequestData);
				} else {
					PayPalServiceSB remote = remoteHome.create();
					resp = remote.disconnect(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

		@Override
		public EwalletResponseData standardCheckout(EwalletRequestData ewalletRequestData) throws Exception {
			return null;
		}

		@Override
		public EwalletResponseData preStandardCheckout(EwalletRequestData ewalletRequestData) throws Exception {
			return null;
		}

	

		@Override
		public EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws Exception {
			EwalletResponseData resp = null;
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PayPalServiceSB)) {
					resp = EwalletService.getInstance().addPayPalWallet(ewalletRequestData);
				} else {
					PayPalServiceSB remote = remoteHome.create();
					resp = remote.addPayPalWallet(ewalletRequestData);
				}
			} catch (CreateException e) {
				throw new MasterpassRuntimeException(e);
			} catch (RemoteException e) {
				throw new MasterpassRuntimeException(e);
			}

			if (resp == null) {
				resp = new EwalletResponseData();
			}
			return resp;
		}

	}
}
