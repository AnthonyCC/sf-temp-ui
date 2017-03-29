package com.freshdirect.webapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ejb.EJBException;

import org.junit.Test;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.fdlogistics.services.ILogisticsService;
import com.freshdirect.fdlogistics.services.impl.FDLogisticsService;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import mockit.Mock;
import mockit.MockUp;

public class DeliveryAddressValidatorTest {
	
	@Test
	public void testValidateAddressWithoutScrub() throws FDResourceException {		
		
		new MockUp<FDDeliveryManager>() {
			@Mock
			void $init() {
			}
		};
		
		new MockUp<LogisticsServiceLocator>() {
			
			@Mock
			void $init() {
							}
			
			@Mock
			public ILogisticsService getLogisticsService() {
				return new FDLogisticsService();
			}
		};
		
		
		AddressModel addressModel = new AddressModel();
		addressModel.setAddress1("281 N HIGHLAND AVE");
		addressModel.setAddress2("");
		addressModel.setCity("Lansdowne");
		addressModel.setState("PA");
		addressModel.setZipCode("19050");
		addressModel.setCountry("US");
		
		DeliveryAddressValidator validator = new DeliveryAddressValidator(addressModel);
		
		
		
		ActionResult actionResult = new ActionResult();
		
		
		boolean isAddressValid = validator.validateAddressWithoutScrub(actionResult);	
		
		assertTrue(isAddressValid);
		assertTrue(actionResult.isSuccess());
		assertNotNull(validator.getScrubbedAddress());
		
		assertNotNull(validator.getScrubbedAddress().getScrubbedStreet());
		assertNotNull(validator.getServiceResult().getAvailableServices());
		assertEquals("281 N HIGHLAND AVE", validator.getScrubbedAddress().getScrubbedStreet());
		assertEquals(-75.274421, validator.getScrubbedAddress().getLongitude(),new Double(0.1));
		assertEquals(39.948805, validator.getScrubbedAddress().getLatitude(),new Double(0.1));
	}
	
	@Test
	public void testValidateAddressWithScrub() throws FDResourceException {		
		
		new MockUp<FDDeliveryManager>() {
			@Mock
			void $init() {
			}
		};
		
		new MockUp<LogisticsServiceLocator>() {
			
			@Mock
			void $init() {
				
			}
			
			@Mock
			public ILogisticsService getLogisticsService() {
				return new FDLogisticsService();
			}
		};
		
		AddressModel addressModel = new AddressModel();
		addressModel.setAddress1("281 N HIGHLAND AVE");
		addressModel.setAddress2("");
		addressModel.setCity("Lansdowne");
		addressModel.setState("PA");
		addressModel.setZipCode("19050");
		addressModel.setCountry("US");
		
		DeliveryAddressValidator validator = new DeliveryAddressValidator(addressModel);
		
		ActionResult actionResult = new ActionResult();
		
		boolean isAddressValid = validator.validateScrubbedAddress(actionResult, "GEOCODE_OK");
		
		assertTrue(isAddressValid);
		assertTrue(actionResult.isSuccess());
		assertNotNull(validator.getScrubbedAddress());
		
		assertNotNull(validator.getScrubbedAddress().getScrubbedStreet());
		assertNotNull(validator.getServiceResult().getAvailableServices());
		assertEquals("281 N HIGHLAND AVE", validator.getScrubbedAddress().getScrubbedStreet());
		assertEquals(-75.274421, validator.getScrubbedAddress().getLongitude(),new Double(0.1));
		assertEquals(39.948805, validator.getScrubbedAddress().getLatitude(),new Double(0.1));
	}


}
