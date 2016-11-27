package com.freshdirect.fdstore.customer;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.AuthorizationInfo;
import com.freshdirect.payment.AuthorizationStrategy;
import com.sap.mw.jco.JCO.Exception;

public class AuthorizationStrategyTestCase extends TestCase {

	private static final String ORDER_TYPE_FD="FDONLY"; 
	private static final String ORDER_TYPE_BC="BCONLY";
	private static final String ORDER_TYPE_FD_BC="BOTH";
	

	public void testAuthorizeFDOnly () throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, RemoteException, FinderException, FDAuthenticationException, CreateException {
		ErpSaleModel sale = null;						
		AuthorizationInfo info=null;
		try
         {        	 
        	 sale=ErpSaleModelFactory.createErpSaleModel(ORDER_TYPE_FD);        	         	
			 List authInfos = new AuthorizationStrategy(sale).getOutstandingAuthorizations();
				for(Iterator i = authInfos.iterator(); i.hasNext(); ) {
					info = (AuthorizationInfo) i.next();	
					if(ErpAffiliate.CODE_FD.equalsIgnoreCase(info.getAffiliate().getCode()))
					{
					    System.out.println(info.getAmount());
					    assertEquals(12.43,info.getAmount(),0);					    
					}				
			}                          

		}catch(Exception e){
			throw new EJBException(e);
		}													
	}
	
	
	
	public void testAuthorizeBCOnly () throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, RemoteException, FinderException, FDAuthenticationException, CreateException {
		ErpSaleModel sale = null;						
		AuthorizationInfo info=null;
         try
         {
        	 
        	 sale=ErpSaleModelFactory.createErpSaleModel(ORDER_TYPE_BC);
        	         	 
			 List authInfos = new AuthorizationStrategy(sale).getOutstandingAuthorizations();
				for(Iterator i = authInfos.iterator(); i.hasNext(); ) {
					info = (AuthorizationInfo) i.next();	
					if(ErpAffiliate.CODE_BC.equalsIgnoreCase(info.getAffiliate().getCode()))
					{
					    System.out.println(info.getAmount());
					    // sum of 11.5*4 + 3.85(tax) - 5.05(discount) - 30(discount)
					    assertEquals(14.8,info.getAmount(),0);
					    
					}				
			}                          

		}catch(Exception e){
			throw new EJBException(e);
		}													
	}
	
	

	
	public void testAuthorizeFDandBC () throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, RemoteException, FinderException, FDAuthenticationException, CreateException {
		ErpSaleModel sale = null;						
		AuthorizationInfo info=null;
         try
         {
        	 
        	 sale=ErpSaleModelFactory.createErpSaleModel(ORDER_TYPE_FD_BC);
        	         	 
			 List authInfos = new AuthorizationStrategy(sale).getOutstandingAuthorizations();
				for(Iterator i = authInfos.iterator(); i.hasNext(); ) {
					info = (AuthorizationInfo) i.next();	
					if(ErpAffiliate.CODE_FD.equalsIgnoreCase(info.getAffiliate().getCode()))
					{
					    System.out.println(info.getAmount());
					    // sum of 11.5*4 + 3.85(tax) + 4.95(delivery charge) - 10(discount) - 30(discount)
					    assertEquals(12.43,info.getAmount(),0);
					    
					}
					if(ErpAffiliate.CODE_BC.equalsIgnoreCase(info.getAffiliate().getCode()))
					{
					    System.out.println(info.getAmount());
					    // sum of 11.5*4 + 3.85(tax)  - 10(discount) - 30(discount)
					    assertEquals(49.85,info.getAmount(),0);
					    
					}				

			}                          

		}catch(Exception e){
			throw new EJBException(e);
		}													
	}

	

	
	
	
	
	
						
	
		

	
	
		
	 	 
	
}	




	

