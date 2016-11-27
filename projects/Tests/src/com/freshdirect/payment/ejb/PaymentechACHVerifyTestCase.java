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
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest( { GatewayLogActivity.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechACHVerifyTestCase extends TestCase  {
	
	
	public void testACHVerify() throws ErpTransactionException {
		
		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		// CC_VERIFY
		ErpPaymentMethodI card = PaymentechTestData.getECheck();
		ErpAuthorizationModel authModel = gateway.verify(
				Merchant.FRESHDIRECT.toString(),card);
		
		assertNotNull(authModel);
		assertEquals(true, authModel.isApproved());
		assertNotNull(authModel.getAuthCode());
		assertNotNull(authModel.getResponseCode());
		assertNotNull(authModel.getDescription());
		assertNotNull(authModel.getProcResponseCode());
	}
	

}
