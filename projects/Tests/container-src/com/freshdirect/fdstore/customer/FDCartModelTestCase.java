package com.freshdirect.fdstore.customer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.FDPromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;

public class FDCartModelTestCase extends TestCase {

	private static final String ORDER_TYPE_FD="FDONLY";

	
	public void testFDCartModel() throws JspException, FDResourceException, FDInvalidConfigurationException, FDAuthenticationException, RemoteException, CreateException{
		String erpCustomerId="516479246";  // customer having 300$ credit
		String fdCustomerId="516479248";
		//String redemptionCode="USRJO2";
		
		FDIdentity identity = new FDIdentity(erpCustomerId,fdCustomerId);
		FDCartModel cart=ErpSaleModelFactory.makeFDCartModel(identity,1,ORDER_TYPE_FD);
		ArrayList cList=new ArrayList();
		cList.add(ErpSaleModelFactory.createChargeLineModel());
		cart.setCharges(cList);
		
		// adding the discount
		//addDiscount(cart,redemptionCode,identity);
		cart.addDiscount(ErpSaleModelFactory.createDiscount());
		
		// adding the applied credit		
		//FDCustomerCreditUtil.applyCustomerCredit(cart,identity);
		List creditList=new ArrayList();
		creditList.add(ErpSaleModelFactory.createAppliedCreaditModel());
		cart.setCustomerCredits(creditList);		
						
		assertEquals(cart.getSubTotal(),35.6,0.0);
			
		assertEquals(4.95,cart.getChargeAmount(EnumChargeType.DELIVERY),0.0);
										
		assertEquals(10,cart.getTotalDiscountValue(),0.0);
		
		assertEquals(30,cart.getCustomerCreditsValue(),0.0);
						
		assertEquals(2.23,cart.getTotal(),0);
		
	}
	

	
	public void testFDCartModelAppledCredit() throws FDResourceException, FDInvalidConfigurationException, JspException, FDAuthenticationException, RemoteException, CreateException{
		
		String erpCustomerId="516479246";  // customer having 300$ credit
		String fdCustomerId="516479248";
		FDIdentity identity = new FDIdentity(erpCustomerId,fdCustomerId);
		FDCartModel cart=ErpSaleModelFactory.makeFDCartModel(identity,1,ORDER_TYPE_FD);
		ArrayList cList=new ArrayList();
		cList.add(ErpSaleModelFactory.createChargeLineModel());
		cart.setCharges(cList);				
		
		// adding the applied credit		
		//FDCustomerCreditUtil.applyCustomerCredit(cart,identity);
		List creditList=new ArrayList();
		creditList.add(ErpSaleModelFactory.createAppliedCreaditModel());
		cart.setCustomerCredits(creditList);		
						
		assertEquals(cart.getSubTotal(),35.6,0.0);
		
		assertEquals(4.95,cart.getChargeAmount(EnumChargeType.DELIVERY),0.0);

		assertEquals(1.68,cart.getTaxValue(),0.0);
				
		assertEquals(0,cart.getTotalDiscountValue(),0.0);
		
		assertEquals(30,cart.getCustomerCreditsValue(),0.0);
						
		assertEquals(12.23,cart.getTotal(),0);

		
	}

	 
	 public  void addDiscount(FDCartModel tmpModel,String redemptionCode,FDIdentity identity) throws RemoteException, CreateException, FDAuthenticationException, FDResourceException
	 {
        	FDUser user =  new FDUser();
        	user.setIdentity(identity);
        	String customerId=user.getIdentity().getErpCustomerPK();
        	user.setShoppingCart(tmpModel);        	
        	PromotionI promotion = FDPromotionFactory.getInstance().getRedemptionPromotion(redemptionCode, customerId);        	    	        	        	        
        	user.setRedeemedPromotion(promotion);
        	user.updateUserState();    	 
        
	 }								
	 	
}	