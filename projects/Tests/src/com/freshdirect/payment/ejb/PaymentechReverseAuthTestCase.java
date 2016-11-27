package com.freshdirect.payment.ejb;

import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.TestCase;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GatewayLogActivity.class, ErpAffiliate.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechReverseAuthTestCase extends TestCase{
	public void testReverseAuthoriseVisa() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.VISA);
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993318");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123451"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				Request request = GatewayAdapter.getReverseAuthRequest(paymentmethod, authModel1);
				Response response=gateway.reverseAuthorize(request);
				assertEquals(false, response.isError());
				assertEquals(true,response.isRequestProcessed());
				assertNotNull("0",response.getStatusCode());
				assertNotNull("Approved",response.getStatusMessage());
				
				
	}
	
	public void testReverseAuthoriseEcheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.getECheck();
				paymentmethod.setProfileID("57521067");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123451"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				Request request = GatewayAdapter.getReverseAuthRequest(paymentmethod, authModel1);
				Response response=gateway.reverseAuthorize(request);
				assertEquals(false, response.isError());
				assertEquals(false,response.isRequestProcessed());
				assertNotNull("9811",response.getStatusCode());
				assertNotNull("Online reversals are not allowed for cardtype [EC]",response.getStatusMessage());
				
				
	}
	
	public void testReverseAuthoriseMasterpassCard() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getMasterpassCard();
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("58121985");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123451"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				Request request = GatewayAdapter.getReverseAuthRequest(paymentmethod, authModel1);
				Response response=gateway.reverseAuthorize(request);
				assertEquals(false, response.isError());
				assertEquals(true,response.isRequestProcessed());
				assertNotNull("0",response.getStatusCode());
				assertNotNull("Approved",response.getStatusMessage());
				
				
	}


}
