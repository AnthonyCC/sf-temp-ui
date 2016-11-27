package com.freshdirect.fdstore.customer;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.CaptureStrategy;
import com.sap.mw.jco.JCO.Exception;

public class CaptureStrategyTestCase extends TestCase {

	private static final String ORDER_TYPE_FD="FDONLY"; 
	private static final String ORDER_TYPE_BC="BCONLY";
	private static final String ORDER_TYPE_FD_BC="BOTH";
			
	public void testCaptureStrategyFDOnly () throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, RemoteException, FinderException, FDAuthenticationException, CreateException, ErpTransactionException {
		ErpSaleModel sale = null;						
		try
         {        	 
        	 sale=ErpSaleModelFactory.createErpSaleModel(ORDER_TYPE_FD);
        	 sale.createOrderComplete("DUMMY_SAPNUMBER1");        	 
        	 sale.addAuthorization(ErpSaleModelFactory.createErpAuthorizationModel(sale,ORDER_TYPE_FD));        	 
        	 sale.cutoff();
        	 sale.addInvoice(ErpSaleModelFactory.createErpInvoiceModel(sale));
			 Map capInfos = new CaptureStrategy(sale).getOutstandingCaptureAmounts();
			 System.out.println(" capInfos :"+capInfos);
				for(Iterator i = capInfos.keySet().iterator(); i.hasNext(); ) {
					ErpAffiliate aff = (ErpAffiliate) i.next();
					
					if(ErpAffiliate.CODE_FD.equalsIgnoreCase(aff.getCode()))
					{
						Double amount = (Double) capInfos.get(aff);
					    System.out.println(amount.doubleValue());
					    assertEquals(3.53,amount.doubleValue(),0);					    
					}				
			 }                          

		}catch(Exception e){
			throw new EJBException(e);
		}													
	}
	
	
	public void testCaptureStrategyBCOnly () throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, RemoteException, FinderException, FDAuthenticationException, CreateException, ErpTransactionException {
		ErpSaleModel sale = null;						
		try
         {        	 
        	 sale=ErpSaleModelFactory.createErpSaleModel(ORDER_TYPE_BC);
        	 sale.createOrderComplete("DUMMY_SAPNUMBER2");
        	 System.out.println("sale.getStatus()1 :"+sale.getStatus());
        	 sale.addAuthorization(ErpSaleModelFactory.createErpAuthorizationModel(sale,ORDER_TYPE_BC));
        	 System.out.println("sale.getStatus()2 :"+sale.getStatus());
        	 sale.cutoff();
        	 System.out.println("sale.getStatus()3 :"+sale.getStatus());
        	 sale.addInvoice(ErpSaleModelFactory.createErpInvoiceModel(sale));
			 Map capInfos = new CaptureStrategy(sale).getOutstandingCaptureAmounts();
			 System.out.println(" capInfos :"+capInfos);
				for(Iterator i = capInfos.keySet().iterator(); i.hasNext(); ) {
					ErpAffiliate aff = (ErpAffiliate) i.next();
					
					if(ErpAffiliate.CODE_BC.equalsIgnoreCase(aff.getCode()))
					{
						Double amount = (Double) capInfos.get(aff);
					    System.out.println(amount.doubleValue());
					    assertEquals(14.8,amount.doubleValue(),0);					    
					}				
			 }                          

		}catch(Exception e){
			throw new EJBException(e);
		}													
	}
	
	
	public void testCaptureStrategyBothFDandBC () throws FDResourceException, ErpFraudException, ErpAuthorizationException, ReservationException, DeliveryPassException, FDInvalidConfigurationException, JspException, RemoteException, FinderException, FDAuthenticationException, CreateException, ErpTransactionException {
		ErpSaleModel sale = null;						
		try
         {        	 
        	 sale=ErpSaleModelFactory.createErpSaleModel(ORDER_TYPE_FD_BC);
        	 sale.createOrderComplete("DUMMY_SAPNUMBER2");
        	 System.out.println("sale.getStatus()1 :"+sale.getStatus());
        	 sale.addAuthorization(ErpSaleModelFactory.createErpAuthorizationModel(sale,ORDER_TYPE_FD));
        	 sale.addAuthorization(ErpSaleModelFactory.createErpAuthorizationModel(sale,ORDER_TYPE_BC));
        	 System.out.println("sale.getStatus()2 :"+sale.getStatus());
        	 sale.cutoff();
        	 System.out.println("sale.getStatus()3 :"+sale.getStatus());
        	 sale.addInvoice(ErpSaleModelFactory.createErpInvoiceModel(sale));
			 Map capInfos = new CaptureStrategy(sale).getOutstandingCaptureAmounts();
			 System.out.println(" capInfos :"+capInfos);
				for(Iterator i = capInfos.keySet().iterator(); i.hasNext(); ) {
					ErpAffiliate aff = (ErpAffiliate) i.next();
					
					if(ErpAffiliate.CODE_FD.equalsIgnoreCase(aff.getCode()))
					{
						Double amount = (Double) capInfos.get(aff);
					    System.out.println(amount.doubleValue());
					    assertEquals(3.53,amount.doubleValue(),0);					    
					}
					if(ErpAffiliate.CODE_BC.equalsIgnoreCase(aff.getCode()))
					{
						Double amount = (Double) capInfos.get(aff);
					    System.out.println(amount.doubleValue());
					    assertEquals(49.85,amount.doubleValue(),0);					    
					}				

			 }                          

		}catch(Exception e){
			throw new EJBException(e);
		}													
	}

	
	
	
	
	
						
	
		

	
	
		
	 
	 
	
}	
	

