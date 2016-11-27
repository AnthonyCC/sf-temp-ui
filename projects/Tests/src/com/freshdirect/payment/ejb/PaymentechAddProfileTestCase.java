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

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;

import junit.framework.TestCase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ GatewayLogActivity.class, ErpAffiliate.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechAddProfileTestCase extends TestCase {

	public void testAddProfileVISA() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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
		// ADD PROFILE
		CreditCard card = PaymentechTestData.getCreditCard(CreditCardType.VISA);
		Request request = GatewayAdapter
				.getAddProfileRequest(PaymentechTestData
						.parsePaymentMethod(card));

		Response response = gateway.addProfile(request);
		assertNotNull(response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertEquals(true,response.isRequestProcessed());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
		assertNotSame("",response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
	}

	public void testAddProfileMC() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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
		// ADD PROFILE
		CreditCard card = PaymentechTestData
				.getCreditCard(CreditCardType.MASTERCARD);
		Request request = GatewayAdapter
				.getAddProfileRequest(PaymentechTestData
						.parsePaymentMethod(card));

		Response response = gateway.addProfile(request);
		assertEquals(true,response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
			}

	public void testAddProfileAmex() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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
		// ADD PROFILE
		CreditCard card = PaymentechTestData.getCreditCard(CreditCardType.AMEX);
		Request request = GatewayAdapter
				.getAddProfileRequest(PaymentechTestData
						.parsePaymentMethod(card));

		Response response = gateway.addProfile(request);
		assertEquals(true,response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
		assertNotSame("",response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
	}

	public void testAddProfileDisc() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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
		// ADD PROFILE
		CreditCard card = PaymentechTestData
				.getCreditCard(CreditCardType.DISCOVER);
		Request request = GatewayAdapter
				.getAddProfileRequest(PaymentechTestData
						.parsePaymentMethod(card));

		Response response = gateway.addProfile(request);
		assertEquals(true,response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
		assertNotSame("",response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
	}

	public void testAddProfileECheck() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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

		// ADD PROFILE
		ErpPaymentMethodI card = PaymentechTestData.getECheck();
		Request request = GatewayAdapter.getAddProfileRequest(card);
		Response response = gateway.addProfile(request);
		assertEquals(true,response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
		assertNotSame("",response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
	}

	
	public void testAddProfileMasterpassCard() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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

		// ADD PROFILE
		CreditCard card = PaymentechTestData.getMasterpassCard();
		Request request = GatewayAdapter.getAddProfileRequest(PaymentechTestData.parsePaymentMethod(card));
		Response response = gateway.addProfile(request);
		assertEquals(true,response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
		assertNotSame("",response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
	}
	public void testAddProfileMasterpassCardAmex() throws ErpTransactionException {

		Gateway gateway = GatewayFactory.getGateway(GatewayType.PAYMENTECH);
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

		// ADD PROFILE
		CreditCard card = PaymentechTestData.getMasterpassCardAMEX();
		Request request = GatewayAdapter.getAddProfileRequest(PaymentechTestData.parsePaymentMethod(card));
		Response response = gateway.addProfile(request);
		assertEquals(true,response.isRequestProcessed());
		assertEquals(false, response.isError());
		assertNotNull(response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
		assertNotSame("",response.getBillingInfo().getPaymentMethod()
				.getBillingProfileID());
	}
}
