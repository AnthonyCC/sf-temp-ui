/*
 * PaylinxTestCase.java
 *
 * Created on October 15, 2001, 1:19 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version
 */
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MD5Hasher;


public class ErpCustomerTestCase extends TestCase{

	protected Context ctx;

	protected ErpCustomerHome home;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new ErpCustomerTestCase("testErpCustomerEB"));
	}

	public ErpCustomerTestCase(String testName) {
		super(testName);

	}

	protected void setUp() throws NamingException {
		ctx = TestUtil.getInitialContext();
		home = (ErpCustomerHome) ctx.lookup("freshdirect.erp.Customer");
	}

	/** This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {
		ctx.close();
	}

	public void testErpCustomerEB() throws RemoteException, CreateException, RemoveException, FinderException{

		ErpCustomerModel customerModel = new ErpCustomerModel();
		
		final String emailAddress = "unittest" + (int)(Math.random()*1000.0) + "@freshdirect.com";
		final String password = "freshdirect"+ (int)(Math.random()*1000.0);
		
		customerModel.setUserId(emailAddress);
		customerModel.setPasswordHash(MD5Hasher.hash(password));

		//
		// setup customerInfo
		//
		ErpCustomerInfoModel infoModel = new ErpCustomerInfoModel();
		infoModel.setTitle("Mr");
		infoModel.setFirstName("Python");
		infoModel.setMiddleName("Test");
		infoModel.setLastName("Martini");
		infoModel.setEmail(emailAddress);
		infoModel.setEmailPlaintext(true);

		PhoneNumber homePhone = new PhoneNumber("212-555-1212");
		infoModel.setHomePhone(homePhone);

		PhoneNumber busPhone = new PhoneNumber("212-555-1212", "1149");
		infoModel.setBusinessPhone(busPhone);

		PhoneNumber cellPhone = new PhoneNumber("212-555-1212");
		infoModel.setCellPhone(cellPhone);

		PhoneNumber otherPhone = new PhoneNumber("212-555-1212");
		infoModel.setOtherPhone(otherPhone);

		PhoneNumber faxNumber = new PhoneNumber("212-555-1212");
		infoModel.setFax(faxNumber);

		infoModel.setWorkDepartment("Test");

		customerModel.setCustomerInfo(infoModel);


		//
		// setup ship to address
		//

		ErpAddressModel address = new ErpAddressModel();
		address.setAddress1("112 Fulton St");
		//address.setAddress2("test");
		address.setAddress2("");
		address.setApartment("3C");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10038");
		address.setCountry("US");
		address.setPhone(new PhoneNumber("212-555-1212", "1000"));


		customerModel.addShipToAddress(address);

		//
		// setup payment method
		//
		ErpCreditCardModel creditCard = new ErpCreditCardModel();
		Calendar expiration = new GregorianCalendar();
		expiration.add(Calendar.YEAR, 2);

		creditCard.setName("Python martini");
		creditCard.setAccountNumber("41111111111111111");
		creditCard.setExpirationDate(expiration.getTime());
		creditCard.setCardType(EnumCardType.getCardType("VISA"));
		creditCard.setAddress1("112 Fulton St");
		creditCard.setAddress2("test");
		creditCard.setApartment("3C");
		creditCard.setCity("New York");
		creditCard.setState("NY");
		creditCard.setZipCode("10038");
		creditCard.setCountry("US");

		customerModel.addPaymentMethod(creditCard);

		//
		// create the bean
		// 
		ErpCustomerEB eb = home.create(customerModel);

		PrimaryKey pk = (PrimaryKey) eb.getPrimaryKey();

		//
		// set sapId
		//
		eb.setSapId("000000123");

		// invalidate bean
		try {
			eb.invalidate();
			fail("Couldn't invalidate entity");
		} catch (EJBException ejbe) {
		} catch (RemoteException re) {
		}

		//
		// test findByUserIdAndPassword
		//
		eb = home.findByUserIdAndPasswordHash(emailAddress, password);

		ErpCustomerModel model = (ErpCustomerModel)eb.getModel();

		assertEquals("userId", emailAddress, model.getUserId());
		assertEquals("sapId", "000000123", model.getSapId());

		// invalidate it again so findByPrimaryKey can be tested
		try {
			eb.invalidate();
			fail("Couldn't invalidate entity");
		} catch (EJBException ejbe) {
		} catch (RemoteException re) {
		}

		//
		// test findByPrimaryKey
		//
		eb = home.findByPrimaryKey(pk);

		assertEquals("Primary key id", pk.getId(), ((PrimaryKey)eb.getPrimaryKey()).getId());
		ErpCustomerModel cust = (ErpCustomerModel)eb.getModel();


		//
		// add credits
		//
		/*
		ErpCustomerCreditModel creditModel = new ErpCustomerCreditModel();
		creditModel.setOriginalAmount(10.00);
		creditModel.setRemainingAmount(10.00);
		creditModel.setDepartment("FRUIT");
		//creditModel.setDescription("Customer not happy");
		//creditModel.setReasonCode("100");
		creditModel.setComplaintPk(new PrimaryKey("123"));
		eb.addCustomerCredit(creditModel);
		*/
		
		//
		// verify ship to address
		//
		List addresses = cust.getShipToAddresses();
		System.out.println("number of address:" +addresses.size());
		ErpAddressModel storedResult = (ErpAddressModel)addresses.get(0);

		assertEquals("address1",	address.getAddress1(),	storedResult.getAddress1());
		assertEquals("address2",	address.getAddress2(),	storedResult.getAddress2());
		assertEquals("apartment",	address.getApartment(),	storedResult.getApartment());
		assertEquals("city",		address.getCity(),		storedResult.getCity());
		assertEquals("state",		address.getState(),		storedResult.getState());
		assertEquals("zipCode",		address.getZipCode(),	storedResult.getZipCode());
		assertEquals("country",		address.getCountry(),	storedResult.getCountry());
		assertEquals("phone",		address.getPhone(),		storedResult.getPhone());

		//
		// modify ship to address
		//
		storedResult.setAddress1("Testing testing testing");
		eb.updateShipToAddress(storedResult);

		try {
			eb.invalidate();
			fail("Couldn't invalidate entity");
		} catch (EJBException ejbe) {
		} catch (RemoteException re) {
		}
		eb = home.findByPrimaryKey(pk);

		ErpCustomerModel storedCust = (ErpCustomerModel)eb.getModel();
		/*
		List storedCredits = storedCust.getCustomerCredits();
		ErpCustomerCreditModel storedCreditModel = (ErpCustomerCreditModel)storedCredits.get(0);
		*/
		addresses = cust.getShipToAddresses();
		System.out.println("number of address:" +addresses.size());
		storedResult = (ErpAddressModel)addresses.get(0);
		System.out.println(storedResult.getAddress1());

		List creditCards = cust.getPaymentMethods();
		System.out.println("number of credit card: "+creditCards.size());
		ErpCreditCardModel storedCcd = (ErpCreditCardModel)creditCards.get(0);

/*
		assertEquals("Customer Credit Check", creditModel.getOriginalAmount(), storedCreditModel.getOriginalAmount(), 0.001);
		assertEquals("Customer Credit Check", creditModel.getRemainingAmount(), storedCreditModel.getRemainingAmount(), 0.001);
		assertEquals("Customer Credit Check", creditModel.getDepartment(), storedCreditModel.getDepartment());
		//assertEquals("Customer Credit Check", creditModel.getDescription(), storedCreditModel.getDescription());
		//assertEquals("Customer Credit Check", creditModel.getReasonCode(), storedCreditModel.getReasonCode());
*/

		assertEquals("Credit Card Check", creditCard.getName(), storedCcd.getName());
		assertEquals("Credit Card Check", creditCard.getAccountNumber(), storedCcd.getAccountNumber());
		//assertEquals("Credit Card Check", creditCard.getExpirationDate(), storedCcd.getExpirationDate());
		assertEquals("Credit Card Check", creditCard.getAddress1(), storedCcd.getAddress1());
		assertEquals("Credit Card Check", creditCard.getAddress2(), storedCcd.getAddress2());
		assertEquals("Credit Card Check", creditCard.getApartment(), storedCcd.getApartment());
		assertEquals("Credit Card Check", creditCard.getCity(), storedCcd.getCity());
		assertEquals("Credit Card Check", creditCard.getState(), storedCcd.getState());
		assertEquals("Credit Card Check", creditCard.getZipCode(), storedCcd.getZipCode());
		assertEquals("Credit Card Check", creditCard.getCountry(), storedCcd.getCountry());

		ErpCustomerInfoModel storedInfo = cust.getCustomerInfo();

		assertEquals("Customer info check", infoModel.getTitle(), storedInfo.getTitle());
		assertEquals("Customer info check", infoModel.getFirstName(), storedInfo.getFirstName());
		assertEquals("Customer info check", infoModel.getMiddleName(), storedInfo.getMiddleName());
		assertEquals("Customer info check", infoModel.getLastName(), storedInfo.getLastName());
		assertEquals("Customer info check", infoModel.getEmail(), storedInfo.getEmail());
		assertEquals("Customer info check", infoModel.getHomePhone().getPhone(), storedInfo.getHomePhone().getPhone());
		assertEquals("Customer info check", infoModel.getBusinessPhone().getPhone(), storedInfo.getBusinessPhone().getPhone());
		assertEquals("Customer info check", infoModel.getFax().getPhone(), storedInfo.getFax().getPhone());
		assertEquals("Customer info check", infoModel.getOtherPhone().getPhone(), storedInfo.getOtherPhone().getPhone());
		assertEquals("Customer info check", infoModel.getCellPhone().getPhone(), storedInfo.getCellPhone().getPhone());
		assertEquals("Customer info check", infoModel.getWorkDepartment(), storedInfo.getWorkDepartment());

		//
		// test remove
		//
		eb.remove();
		try {
			eb = home.findByPrimaryKey(pk);
			fail("Removed entity found");
		} catch (FinderException ex) {}

	}

}
