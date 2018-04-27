package com.freshdirect.mobileapi.ewallet;

import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.EwalletServiceFactory;
import com.freshdirect.fdstore.ewallet.IEwallet;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletMobileRequestProcessor {

	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception 
	 */
	public EwalletResponseData checkout(EwalletRequestData ewalletRequestData) throws Exception{
		
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.getToken(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData standardCheckout(EwalletRequestData ewalletRequestData) throws Exception{
		
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.preStandardCheckout(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	
	/**
	 * Generate everytime new Client Token
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData generateClientToken(EwalletRequestData ewalletRequestData) throws Exception{
		
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.getToken(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	/**
	 * Method is used to whether PayPal a/c is paired or not. If yes then return the PayPal paymentMethod details otherwise return the Client Token
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData isPayPalPaired(EwalletRequestData ewalletRequestData) throws Exception{
		
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.getToken(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	

	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData disconnectWallet(EwalletRequestData ewalletRequestData) throws Exception{
		
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.disconnect(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws Exception{
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			if(ewallet!=null){
				ewalletResponseData = ewallet.addPayPalWallet(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData standardCheckoutData(EwalletRequestData ewalletRequestData) throws Exception{
		
		IEwallet ewallet = null;
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.standardCheckout(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData checkoutData(EwalletRequestData ewalletRequestData) throws Exception{
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		IEwallet ewallet = null;
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.checkout(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	
}
