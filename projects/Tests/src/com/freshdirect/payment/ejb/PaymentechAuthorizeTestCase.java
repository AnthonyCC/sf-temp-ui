package com.freshdirect.payment.ejb;

import static org.powermock.api.easymock.PowerMock.mockStatic;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest( { GatewayLogActivity.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechAuthorizeTestCase extends TestCase{

	public void testAuthoriseVisa() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.VISA);
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993318");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123451"),
						 31,
						 2.45,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
	}
	public void testAuthoriseMC() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.MASTERCARD);
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993320");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123452"),
						 42,
						 0.0,
						 Merchant.FRESHDIRECT.toString());
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
	}
	public void testAuthoriseAmex() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.AMEX);
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993322");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123453"),
						 55,
						 4.1,
						 Merchant.FRESHDIRECT.toString());
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
	}
	public void testAuthoriseDisc() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.DISCOVER);
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993324");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123454"),
						 5,
						 11.4,
						 Merchant.FRESHDIRECT.toString());
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
	}
	
	public void testAuthoriseEcheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.getECheck();
				paymentmethod.setProfileID("57521067");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123454"),
						 65,
						 11.4,
						 Merchant.FRESHDIRECT.toString());
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
	}
	public void testAuthoriseMasterpassCard() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				//CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.DISCOVER);
				//57217669
				CreditCard card = PaymentechTestData.getMasterpassCard();
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("58121985");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123454"),
						 65,
						 11.4,
						 Merchant.FRESHDIRECT.toString());
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
	}
}
