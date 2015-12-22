package com.freshdirect.payment.ejb;

import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.TestCase;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GatewayLogActivity.class, ErpAffiliate.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechGetProfileTestCase extends TestCase {

	public void testGetProfileVisa() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//GET_PROFILE
		Response response=gateway.getProfile(PaymentechTestData.getGetProfileRequest("65993318"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	public void testGetProfileMC() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//GET_PROFILE
		Response response=gateway.getProfile(PaymentechTestData.getGetProfileRequest("65993320"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	public void testGetProfileAmex() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//GET_PROFILE
		Response response=gateway.getProfile(PaymentechTestData.getGetProfileRequest("65993322"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	public void testGetProfileDisc() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//GET_PROFILE
		Response response=gateway.getProfile(PaymentechTestData.getGetProfileRequest("65993324"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
	
	//57523513
	public void testGetProfileEcheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//GET_PROFILE
		Response response=gateway.getProfile(PaymentechTestData.getGetProfileRequest("57523513"));
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull("0",response.getStatusCode());
		assertNotNull("Profile Request processed",response.getStatusMessage());
	}
}
