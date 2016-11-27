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
public class PaymentechDeleteProfileTestCase extends TestCase {

	public void testDeleteProfileSuccess() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//DELETE_PROFILE
		Response response=gateway.deleteProfile(PaymentechTestData.getDeleteProfileRequest("57162851"));
		assertNotNull(response.getStatusCode());
		assertEquals("9581",response.getStatusCode());
		assertNotNull(response.getStatusMessage());
	}
	
	public void testDeleteProfileSuccessEcheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//DELETE_PROFILE
		Response response=gateway.deleteProfile(PaymentechTestData.getDeleteProfileRequest("57524125"));
		assertNotNull(response.getStatusCode());
		assertEquals("9581",response.getStatusCode());
		assertNotNull(response.getStatusMessage());
	}
}
