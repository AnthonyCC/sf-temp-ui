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
	
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData preCheckoutData(EwalletRequestData ewalletRequestData) throws Exception{
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		IEwallet ewallet = null;
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.getAllPayMethodInEwallet(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData expressCheckoutWithoutPrecheckout(EwalletRequestData ewalletRequestData) throws Exception{
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		IEwallet ewallet = null;
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.expressCheckoutWithoutPrecheckout(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	public EwalletResponseData expressCheckout(EwalletRequestData ewalletRequestData) throws Exception{
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		IEwallet ewallet = null;
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
			
			if(ewallet!=null){
				ewalletResponseData = ewallet.expressCheckout(ewalletRequestData);
			}
		}
		return ewalletResponseData;
	}
}
