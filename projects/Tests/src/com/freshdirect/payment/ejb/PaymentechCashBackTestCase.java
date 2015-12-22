package com.freshdirect.payment.ejb;

import static org.powermock.api.easymock.PowerMock.mockStatic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
@RunWith(PowerMockRunner.class)
@PrepareForTest( { GatewayLogActivity.class })
@PowerMockIgnore("javax.net.ssl.*")
public class PaymentechCashBackTestCase extends TestCase {
	public void testCashBackVisa() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.VISA);
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993318");
				Map<String, String> merchants = new HashMap<String, String>();
				merchants.put("VISA", "Freshdirect");
				merchants.put("ECP", "Freshdirect");
				merchants.put("AMEX", "Freshdirect");
				merchants.put("MC", "Freshdirect");
				merchants.put("DISC", "Freshdirect");
				
				Set<String> paymentechTxDivisions = new HashSet<String>();
				
				ErpAffiliate affliate = new ErpAffiliate("FD", "FreshDirect", "Freshdirect Inc", "ZT01", "ZBD1", merchants, paymentechTxDivisions);
											
				ErpCashbackModel cashbackModel = gateway.issueCashback("10270268966",
						paymentmethod,
						48.9,
						2.0,
						affliate);
				
				assertNotNull(cashbackModel);
				assertNotNull(cashbackModel.getAuthCode());
				assertNotNull(cashbackModel.getResponseCode());
				assertNotNull(cashbackModel.getSequenceNumber());
				
			}
	
	public void testCashBackMC() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.MASTERCARD);
				//57217669
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993320");
				Map<String, String> merchants = new HashMap<String, String>();
				merchants.put("VISA", "Freshdirect");
				merchants.put("ECP", "Freshdirect");
				merchants.put("AMEX", "Freshdirect");
				merchants.put("MC", "Freshdirect");
				merchants.put("DISC", "Freshdirect");
				
				Set<String> paymentechTxDivisions = new HashSet<String>();
				
				ErpAffiliate affliate = new ErpAffiliate("FD", "FreshDirect", "Freshdirect Inc", "ZT01", "ZBD1", merchants, paymentechTxDivisions);
											
				ErpCashbackModel cashbackModel = gateway.issueCashback("10270268966",
						paymentmethod,
						42.0,
						1.02,
						affliate);
				
				assertNotNull(cashbackModel);
				assertNotNull(cashbackModel.getAuthCode());
				assertNotNull(cashbackModel.getResponseCode());
				assertNotNull(cashbackModel.getSequenceNumber());
				
			}
	public void testCashBackAmex() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.AMEX);
				
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993322");
				Map<String, String> merchants = new HashMap<String, String>();
				merchants.put("VISA", "Freshdirect");
				merchants.put("ECP", "Freshdirect");
				merchants.put("AMEX", "Freshdirect");
				merchants.put("MC", "Freshdirect");
				merchants.put("DISC", "Freshdirect");
				
				Set<String> paymentechTxDivisions = new HashSet<String>();
				
				ErpAffiliate affliate = new ErpAffiliate("FD", "FreshDirect", "Freshdirect Inc", "ZT01", "ZBD1", merchants, paymentechTxDivisions);
											
				ErpCashbackModel cashbackModel = gateway.issueCashback("10270268966",
						paymentmethod,
						49.0,
						1.2,
						affliate);
				assertNotNull(cashbackModel);
				assertNotNull(cashbackModel.getResponseCode());
				assertNotNull(cashbackModel.getSequenceNumber());
				
			}
	public void testCashBackDisc() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getCreditCard(CreditCardType.DISCOVER);
				
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("65993324");
				Map<String, String> merchants = new HashMap<String, String>();
				merchants.put("VISA", "Freshdirect");
				merchants.put("ECP", "Freshdirect");
				merchants.put("AMEX", "Freshdirect");
				merchants.put("MC", "Freshdirect");
				merchants.put("DISC", "Freshdirect");
				
				Set<String> paymentechTxDivisions = new HashSet<String>();
				
				ErpAffiliate affliate = new ErpAffiliate("FD", "FreshDirect", "Freshdirect Inc", "ZT01", "ZBD1", merchants, paymentechTxDivisions);
											
				ErpCashbackModel cashbackModel = gateway.issueCashback("10270268966",
						paymentmethod,
						59.0,
						1.2,
						affliate);
				assertNotNull(cashbackModel);
				assertNotNull(cashbackModel.getAuthCode());
				assertNotNull(cashbackModel.getResponseCode());
				assertNotNull(cashbackModel.getSequenceNumber());
				
			}
	public void testCashBackEcheck() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				
				ErpPaymentMethodI paymentmethod = PaymentechTestData.getECheck();
				paymentmethod.setProfileID("57521067");
				Map<String, String> merchants = new HashMap<String, String>();
				merchants.put("VISA", "Freshdirect");
				merchants.put("ECP", "Freshdirect");
				merchants.put("AMEX", "Freshdirect");
				merchants.put("MC", "Freshdirect");
				merchants.put("DISC", "Freshdirect");
				
				Set<String> paymentechTxDivisions = new HashSet<String>();
				
				ErpAffiliate affliate = new ErpAffiliate("FD", "FreshDirect", "Freshdirect Inc", "ZT01", "ZBD1", merchants, paymentechTxDivisions);
											
				ErpCashbackModel cashbackModel = gateway.issueCashback("10270268966",
						paymentmethod,
						79.0,
						2.2,
						affliate);
				assertNotNull(cashbackModel);
				assertNotNull(cashbackModel.getAuthCode());
				assertNotNull(cashbackModel.getResponseCode());
				assertNotNull(cashbackModel.getSequenceNumber());
				
			}
	
	public void testCashBackMasterpassCard() throws ErpTransactionException {
		Gateway gateway=GatewayFactory.getGateway(GatewayType.PAYMENTECH);
		mockStatic(GatewayLogActivity.class);
		//AUTHORIZE
				CreditCard card  = PaymentechTestData.getMasterpassCard();
				
				ErpPaymentMethodI paymentmethod = PaymentechTestData.parsePaymentMethod(card);
				paymentmethod.setProfileID("58121985");
				Map<String, String> merchants = new HashMap<String, String>();
				merchants.put("VISA", "Freshdirect");
				merchants.put("ECP", "Freshdirect");
				merchants.put("AMEX", "Freshdirect");
				merchants.put("MC", "Freshdirect");
				merchants.put("DISC", "Freshdirect");
				
				Set<String> paymentechTxDivisions = new HashSet<String>();
				
				ErpAffiliate affliate = new ErpAffiliate("FD", "FreshDirect", "Freshdirect Inc", "ZT01", "ZBD1", merchants, paymentechTxDivisions);
											
				ErpCashbackModel cashbackModel = gateway.issueCashback("10270268966",
						paymentmethod,
						59.0,
						1.2,
						affliate);
								
				assertNotNull(cashbackModel);
				assertNotNull(cashbackModel.getAuthCode());
				assertNotNull(cashbackModel.getResponseCode());
				assertNotNull(cashbackModel.getSequenceNumber());
				
			}
	
}
