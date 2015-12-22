package com.freshdirect.payment.ejb;

import static org.powermock.api.easymock.PowerMock.mockStatic;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
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
public class PaymentechCaptureTestCase extends TestCase{
	
	public void testCaptureVisa() throws ErpTransactionException {
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
				
				//ErpCaptureModel capture = null;
				String orderNumber =authModel1.getGatewayOrderID();
				ErpCaptureModel capture =gateway.capture(authModel1, paymentmethod, authModel1.getAmount(),  authModel1.getTax(), orderNumber);
				assertNotNull(capture.getAuthCode());
				assertNotNull(capture.getSequenceNumber());
				assertNotNull(capture.getResponseCode());
				assertNotNull(capture.getDescription());
				
			}
	
	public void testCaptureMC() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.MASTERCARD);
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993320");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("123452"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				//ErpCaptureModel capture = null;
				String orderNumber =authModel1.getGatewayOrderID();
				ErpCaptureModel capture =gateway.capture(authModel1, paymentmethod, authModel1.getAmount(),  authModel1.getTax(), orderNumber);
				assertNotNull(capture.getAuthCode());
				assertNotNull(capture.getSequenceNumber());
				assertNotNull(capture.getResponseCode());
				assertNotNull(capture.getDescription());
				
			}
	
	public void testCaptureAmex() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.AMEX);
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993322");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("1234523"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				//ErpCaptureModel capture = null;
				String orderNumber =authModel1.getGatewayOrderID();
				ErpCaptureModel capture =gateway.capture(authModel1, paymentmethod, authModel1.getAmount(),  authModel1.getTax(), orderNumber);
				assertNotNull(capture.getAuthCode());
				assertNotNull(capture.getSequenceNumber());
				assertNotNull(capture.getResponseCode());
				assertNotNull(capture.getDescription());
				
			}
	public void testCaptureDisc() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.DISCOVER);
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993324");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("1234524"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				//ErpCaptureModel capture = null;
				String orderNumber =authModel1.getGatewayOrderID();
				ErpCaptureModel capture =gateway.capture(authModel1, paymentmethod, authModel1.getAmount(),  authModel1.getTax(), orderNumber);
				assertNotNull(capture.getAuthCode());
				assertNotNull(capture.getSequenceNumber());
				assertNotNull(capture.getResponseCode());
				assertNotNull(capture.getDescription());
				
			}
		public void testCaptureECheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				//CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.DISCOVER);
				//57521067
				ErpPaymentMethodI paymentmethod = PaymentechTestData.getECheck();
				paymentmethod.setProfileID("57521067");
				ErpAuthorizationModel authModel1 = gateway.authorize(
						paymentmethod,
						 PaymentechTestData.getSaleID("1234524"),
						 145,
						 1,
						 Merchant.FRESHDIRECT.toString());
				
				assertNotNull(authModel1);
				assertEquals(true, authModel1.isApproved());
				assertNotNull(authModel1.getAuthCode());
				assertNotNull(authModel1.getResponseCode());
				assertNotNull(authModel1.getDescription());
				assertNotNull(authModel1.getProcResponseCode());
				
				//ErpCaptureModel capture = null;
				String orderNumber =authModel1.getGatewayOrderID();
				ErpCaptureModel capture =gateway.capture(authModel1, paymentmethod, authModel1.getAmount(),  authModel1.getTax(), orderNumber);
				assertNotNull(capture.getAuthCode());
				assertNotNull(capture.getSequenceNumber());
				assertNotNull(capture.getResponseCode());
				assertNotNull(capture.getDescription());
				
			}
		
		public void testCaptureMasterpassCard() throws ErpTransactionException {
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
					
					//ErpCaptureModel capture = null;
					String orderNumber =authModel1.getGatewayOrderID();
					ErpCaptureModel capture =gateway.capture(authModel1, paymentmethod, authModel1.getAmount(),  authModel1.getTax(), orderNumber);
					assertNotNull(capture.getAuthCode());
					assertNotNull(capture.getSequenceNumber());
					assertNotNull(capture.getResponseCode());
					assertNotNull(capture.getDescription());
					
				}

	
}
