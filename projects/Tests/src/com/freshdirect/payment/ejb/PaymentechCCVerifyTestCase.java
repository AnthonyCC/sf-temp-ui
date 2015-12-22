package com.freshdirect.payment.ejb;

import static org.powermock.api.easymock.PowerMock.mockStatic;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GatewayLogActivity.class, ErpAffiliate.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechCCVerifyTestCase extends TestCase  {

	public void testCCverifyVisa() throws ErpTransactionException {
		
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		CreditCard card = PaymentechTestData.getCreditCard(CreditCardType.VISA);
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}
	
	public void testCCverifyMC() throws ErpTransactionException {
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		CreditCard card = PaymentechTestData
				.getCreditCard(CreditCardType.MASTERCARD);
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}

	public void testCCverifyAmex() throws ErpTransactionException {
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		PaymentechTestData testdata = new PaymentechTestData();
		CreditCard card = PaymentechTestData.getCreditCard(CreditCardType.AMEX);
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}
	public void testCCverifyDisc() throws ErpTransactionException {
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		PaymentechTestData testdata = new PaymentechTestData();
		CreditCard card = PaymentechTestData.getCreditCard(CreditCardType.DISCOVER);
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}

	public void testCCverifyMasterpassCard() throws ErpTransactionException {
		
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		CreditCard card = PaymentechTestData.getMasterpassCard();
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}
	
	public void testCCverifyMasterpassCardAMEX() throws ErpTransactionException {
		
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		CreditCard card = PaymentechTestData.getMasterpassCardAMEX();
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}
	
	public void testCCverifyMasterpassCardDISC() throws ErpTransactionException {
		
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		CreditCard card = PaymentechTestData.getMasterpassCardDISC();
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),
				PaymentechTestData.parsePaymentMethod(card));
		
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}

}
