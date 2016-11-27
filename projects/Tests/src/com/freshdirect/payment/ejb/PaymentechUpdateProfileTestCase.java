package com.freshdirect.payment.ejb;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.TestCase;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GatewayLogActivity.class, ErpAffiliate.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechUpdateProfileTestCase extends TestCase {

	public void testUpdateProfileVisa() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		Response response=gateway.updateProfile(PaymentechTestData.getUpdateProfileRequest(CreditCardType.VISA,"66034988"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	public void testUpdateProfileMC() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		Response response=gateway.updateProfile(PaymentechTestData.getUpdateProfileRequest(CreditCardType.MASTERCARD,"66034990"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	public void testUpdateProfileAmex() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		Response response=gateway.updateProfile(PaymentechTestData.getUpdateProfileRequest(CreditCardType.AMEX,"66034992"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	public void testUpdateProfileDisc() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		Response response=gateway.updateProfile(PaymentechTestData.getUpdateProfileRequest(CreditCardType.DISCOVER,"66034994"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	//57523895
	public void testUpdateProfileEcheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		mockStatic(ErpAffiliate.class);
		
		Map<String, String> merchants = new HashMap<String, String>();
		merchants.put("VISA", "Freshdirect");
		merchants.put("ECP", "Freshdirect");
		merchants.put("AMEX", "Freshdirect");
		merchants.put("MC", "Freshdirect");
		merchants.put("DISC", "Freshdirect");
		
		Set<String> paymentechTxDivisions = new HashSet<String>();
		
		expect(ErpAffiliate.getPrimaryAffiliate()).andReturn(
				new ErpAffiliate("FD", "FreshDirect", "FreshDirect Inc",
						"ZT01", "ZBD1", merchants, paymentechTxDivisions));
		replay(ErpAffiliate.class);
		
		ErpPaymentMethodI card  = PaymentechTestData.getECheck();
		card.setProfileID("57523895");
		card.setAddress1("23-30 Borden Ave");
		card.setCity("Long Island City");
		card.setZipCode("11101");
		Request request = GatewayAdapter.getUpdateProfileRequest(card);
		Response response=gateway.updateProfile(request);
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	
}