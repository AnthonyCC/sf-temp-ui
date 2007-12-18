/*
 * Class.java
 *
 * Created on October 25, 2001, 7:38 PM
 */

package com.freshdirect.fdstore.customer.ejb;

/**
 *
 * @author  knadeem
 * @version
 */
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.customer.ErpDuplicatePaymentMethodException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.RegistrationResult;

public class FDCustomerManagerTestCase extends TestCase{

	private static final String testUser = "python@test1.com";
	private static final String testPassword = "martini";


	public static void main(String[] args) {
		junit.textui.TestRunner.run(new FDCustomerManagerTestCase("testFDCustomerManager"));
	}

	public FDCustomerManagerTestCase(String testName) {
		super(testName);

	}

	protected void setUp() throws NamingException {

	}

	/** This method cleans up and closes all the resources used by this test
	 * @throws NamingException throws NamingException if there is a problem while
	 * closing the context.
	 */
	protected void tearDown() throws NamingException {

	}

	public void testFDCustomerManager() throws RemoteException, CreateException, RemoveException, FDResourceException, ErpDuplicateUserIdException, ErpFraudException, ErpDuplicatePaymentMethodException {

		ErpCustomerModel customerModel = new ErpCustomerModel();
		customerModel.setUserId(testUser);
		customerModel.setPasswordHash(testPassword);

		ErpCustomerInfoModel infoModel = new ErpCustomerInfoModel();
		infoModel.setTitle("Mr");
		infoModel.setFirstName("Python");
		infoModel.setMiddleName("Test");
		infoModel.setLastName("Martini");
		infoModel.setEmail("python@fd.com");
		infoModel.setEmailPlaintext(true);
		//set Home phone number
		PhoneNumber homePhone = new PhoneNumber("212-555-1212");
		infoModel.setHomePhone(homePhone);
		//set business phone number
		PhoneNumber busPhone = new PhoneNumber("212-555-1212", "1149");
		infoModel.setBusinessPhone(busPhone);
		//set cell phone number
		PhoneNumber cellPhone = new PhoneNumber("212-555-1212");
		infoModel.setCellPhone(cellPhone);
		//set other phone number
		PhoneNumber otherPhone = new PhoneNumber("212-555-1212");
		infoModel.setOtherPhone(otherPhone);
		//set fax number
		PhoneNumber faxNumber = new PhoneNumber("212-555-1212");
		infoModel.setFax(faxNumber);
		infoModel.setWorkDepartment("Test");

		customerModel.setCustomerInfo(infoModel);

		ErpAddressModel address = new ErpAddressModel();
		address.setAddress1("112 Fulton St");
		address.setAddress2("test");
		address.setApartment("3C");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10038");
		address.setCountry("US");

		customerModel.addShipToAddress(address);

        FDCustomerModel fdCustomer = new FDCustomerModel();
        fdCustomer.setPasswordHint("viktorz a weenie");

		RegistrationResult regResult = FDCustomerManager.register(customerModel, fdCustomer, "", false);

		FDIdentity identity = regResult.getIdentity();
		try{
			identity = FDCustomerManager.login(testUser, testPassword);
		}catch(FDAuthenticationException ae){
			ae.printStackTrace();
			fail("Login failed with FDAuthenticationException");
		}
		testAddress(identity);

		testCreditCard(identity);

		testShoppingCart(identity);


	}

	public void testAddress(FDIdentity identity) throws FDResourceException{
		ErpAddressModel address = new ErpAddressModel();
		address.setAddress1("112 Fulton St");
		address.setAddress2("test");
		address.setApartment("3C");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10038");
		address.setCountry("US");

		try {
			// !!! FIXME: no FDUserI passed
			FDCustomerManager.addShipToAddress((FDUserI)null, address);
		} catch(FDResourceException e) {
			e.printStackTrace();
		}catch(ErpDuplicateAddressException e){
			e.printStackTrace();
		}
		Collection c = FDCustomerManager.getShipToAddresses(identity);

		ErpAddressModel storedResult = (ErpAddressModel)c.iterator().next();

		assertEquals("Address check", address.getAddress1(), storedResult.getAddress1());
		assertEquals("Address check", address.getAddress2(), storedResult.getAddress2());
		assertEquals("Address check", address.getApartment(), storedResult.getApartment());
		assertEquals("Address check", address.getCity(), storedResult.getCity());
		assertEquals("Address check", address.getState(), storedResult.getState());
		assertEquals("Address check", address.getZipCode(), storedResult.getZipCode());
		assertEquals("Address check", address.getCountry(), storedResult.getCountry());

		storedResult.setAddress1("Testing testing testing");

		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setSource(EnumTransactionSource.WEBSITE);
		rec.setActivityType(EnumAccountActivityType.UPDATE_DLV_ADDRESS);
		rec.setCustomerId( identity.getErpCustomerPK() );
		rec.setInitiator("test");

		try {
			// !!! FIXME: no FDUserI passed
			FDCustomerManager.updateShipToAddress((FDUserI)null, storedResult, rec);
		} catch(FDResourceException e) {
			e.printStackTrace();
		}catch(ErpDuplicateAddressException e){
			e.printStackTrace();
		}

		Collection c2 = FDCustomerManager.getShipToAddresses(identity);

		ErpAddressModel modifiedResult = (ErpAddressModel)c2.iterator().next();

		System.out.println(modifiedResult.getAddress1());
	}

	public void testCreditCard(FDIdentity identity) throws FDResourceException, ErpDuplicatePaymentMethodException {
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

		FDCustomerManager.addPaymentMethod(identity, creditCard);

		Collection c = FDCustomerManager.getPaymentMethods(identity);

		ErpCreditCardModel storedCcd = (ErpCreditCardModel)c.iterator().next();

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

	}

	public void testShoppingCart(FDIdentity identity) throws FDResourceException {
		FDCartModel cart = new FDCartModel();

		FDCartLineModel cartLine = new FDCartLineModel("FRU00048", 123);
		cartLine.setQuantity(2.0);
		HashMap conf = new HashMap();
		conf.put("PROPERTY1", "VALUE1");
		cartLine.setConfiguration(conf);
		cartLine.setSalesUnit("lb");
		cart.addOrderLine(cartLine);

		//FDCustomerManager.storeShoppingCart(identity, cart);

		//cart = FDCustomerManager.loadShoppingCart(identity);

		cart.removeOrderLine(0);

		cartLine.setQuantity(4);
		cart.addOrderLine(cartLine);

		//FDCustomerManager.storeShoppingCart(identity, cart);
	}

}