
/*
 * RegisterCustomerTestCase.java
 * JUnit based test
 *
 * Created on May 15, 2002, 6:12 PM
 */

package com.freshdirect.load;

import junit.framework.*;

import com.freshdirect.customer.*;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.fdstore.*;

/**
 *
 * @author mrose
 */
public class RegisterCustomerTestCase extends TestCase {
    
    public RegisterCustomerTestCase(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(RegisterCustomerTestCase.class); 
        return suite;
    }
    
    public void testCustomerRegistration() {
        
        String rnd = String.valueOf((long) (10000000000L * Math.random()));
        
        ErpCustomerInfoModel erpCustInfo = new ErpCustomerInfoModel();
        erpCustInfo.setTitle("Mr.");
        erpCustInfo.setFirstName("Testy");
        erpCustInfo.setMiddleName("Testo");
        erpCustInfo.setLastName("TestTest");
        erpCustInfo.setHomePhone(new PhoneNumber("212 212 2121"));
        erpCustInfo.setCellPhone(new PhoneNumber("917 917 9171"));
        erpCustInfo.setBusinessPhone(new PhoneNumber("666 666 6666"));
        erpCustInfo.setOtherPhone(new PhoneNumber("777 777 7777"));
        erpCustInfo.setFax(new PhoneNumber("999 999 9999"));
        erpCustInfo.setEmail("test" + rnd + "@freshdirect.com");
        erpCustInfo.setAlternateEmail("test" + rnd + "2@freshdirect.com");
        erpCustInfo.setEmailPlaintext(false);
        erpCustInfo.setReceiveNewsletter(true);
        erpCustInfo.setWorkDepartment("party planning");
        
        ErpCustomerModel erpCust = new ErpCustomerModel();
        erpCust.setCustomerInfo(erpCustInfo);
        erpCust.setUserId(erpCustInfo.getEmail());
        erpCust.setPasswordHash("password");
        erpCust.setActive(true);
        ErpAddressModel shipTo = new ErpAddressModel();
        shipTo.setFirstName(erpCustInfo.getFirstName());
        shipTo.setLastName(erpCustInfo.getLastName());
        shipTo.setAddress1("280 E 2nd St");
        shipTo.setApartment("5A");
        shipTo.setCity("New York");
        shipTo.setState("NY");
        shipTo.setZipCode("10009");
        shipTo.setCountry("US");
        shipTo.setPhone(erpCustInfo.getHomePhone());
        erpCust.addShipToAddress(shipTo);
        ErpCreditCardModel erpCC = new ErpCreditCardModel();
        erpCC.setName(erpCustInfo.getFirstName() + " " + erpCustInfo.getLastName());
        erpCC.setAccountNumber("4111111111111111");
        erpCC.setCardType(EnumCardType.VISA);
        erpCC.setExpirationDate(new java.util.Date(System.currentTimeMillis() + 365*24*60*60*1000));
        erpCC.setAddress1(shipTo.getAddress1());
        erpCC.setAddress2(shipTo.getAddress2());
        erpCC.setApartment(shipTo.getApartment());
        erpCC.setCity(shipTo.getCity());
        erpCC.setState(shipTo.getState());
        erpCC.setZipCode(shipTo.getZipCode());
        erpCC.setCountry(shipTo.getCountry());
        erpCust.addPaymentMethod(erpCC);
        
        FDCustomerModel fdCust = new FDCustomerModel();
        fdCust.setPasswordHint("who cares?");
        
        try {
            
            FDUser user = FDCustomerManager.createNewUser(shipTo);
			FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.SYSTEM, user.getIdentity(), "RegisterCustomerTestCase", "");
            FDCustomerManager.register(actionInfo, erpCust, fdCust, user.getCookie(), false, true, null);
            
        } catch (FDResourceException fdre) {
            fdre.printStackTrace();
            fail(fdre.getMessage());
        } catch (ErpDuplicateUserIdException eduie) {
            fail(eduie.getMessage());
        }
        
    }
    
    
}
