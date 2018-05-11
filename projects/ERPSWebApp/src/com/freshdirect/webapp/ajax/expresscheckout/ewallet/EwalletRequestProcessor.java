package com.freshdirect.webapp.ajax.expresscheckout.ewallet;

import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletConstants;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.EwalletServiceFactory;
import com.freshdirect.fdstore.ewallet.IEwallet;


/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletRequestProcessor {

	/**
	 * @param ewalletRequestData
	 * @return
	 */
	public EwalletResponseData processRequest(EwalletRequestData ewalletRequestData) throws Exception{
		EwalletResponseData ewalletResponseData = null;
		
		IEwallet ewallet = null;
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
		}
		
		// For Masterpass 
		if(ewallet != null && EnumEwalletType.MP.getName().equals(ewalletRequestData.geteWalletType())){
			
			if(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT.equals(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = standardCheckout(ewallet,ewalletRequestData);
			}
			if(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA.equals(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = standardCheckoutData(ewallet,ewalletRequestData);
			}
		}
		
		if(ewallet != null && EnumEwalletType.PP.getName().equals(ewalletRequestData.geteWalletType())){
			if(EwalletConstants.EWALLET_PP_START_PAIRING.equals(ewalletRequestData.geteWalletAction()) || 
					EwalletConstants.EWALLET_PP_START_CONNECTING.equalsIgnoreCase(ewalletRequestData.geteWalletAction()) ||
					EwalletConstants.GET_PP_DEVICE_DATA.equalsIgnoreCase(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = pairingEwallet(ewallet,ewalletRequestData);
			}
			if(EwalletConstants.EWALLET_PP_END_PAIRING.equals(ewalletRequestData.geteWalletAction()) || 
					EwalletConstants.EWALLET_PP_END_CONNECTING.equalsIgnoreCase(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = obtainVaultToken(ewallet,ewalletRequestData);
			}
			if(EwalletConstants.EWALLET_PP_WALLET_DISCONNECT.equals(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = disconnectPayPalAccount(ewallet,ewalletRequestData);
			}
		}
		
		return ewalletResponseData;
	}

	private EwalletResponseData pairingEwallet(IEwallet ewallet,
			EwalletRequestData ewalletRequestData) throws Exception {
		EwalletResponseData ewalletResponseData = ewallet.getToken(ewalletRequestData);
		return ewalletResponseData;
	}
	
	private EwalletResponseData obtainVaultToken(IEwallet ewallet,
			EwalletRequestData ewalletRequestData) throws Exception {
		EwalletResponseData ewalletResponseData = ewallet.addPayPalWallet(ewalletRequestData);
		return ewalletResponseData;
	}

	private EwalletResponseData disconnectPayPalAccount(IEwallet ewallet,EwalletRequestData ewalletRequestData) throws Exception{
		// get token
		EwalletResponseData ewalletResponseData = ewallet.disconnect(ewalletRequestData);
		return ewalletResponseData;
	}


	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	private EwalletResponseData standardCheckout(IEwallet ewallet,EwalletRequestData ewalletRequestData)throws Exception{
		// get Standard Checkout token
		EwalletResponseData ewalletResponseData = ewallet.preStandardCheckout(ewalletRequestData);
		return ewalletResponseData;
	}
	
	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	private EwalletResponseData standardCheckoutData(final IEwallet ewallet,final EwalletRequestData ewalletRequestData)throws Exception{
		// get Standard Checkout Data
		EwalletResponseData ewalletResponseData = ewallet.standardCheckout(ewalletRequestData);
		
		return ewalletResponseData;
	}
	
}
