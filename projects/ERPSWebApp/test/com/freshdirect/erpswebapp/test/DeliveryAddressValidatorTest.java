package com.freshdirect.erpswebapp.test;

import java.util.List;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Test;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.fdlogistics.services.ILogisticsService;
import com.freshdirect.fdlogistics.services.impl.FDLogisticsService;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;

public class DeliveryAddressValidatorTest {
	
	@SuppressWarnings("deprecation")
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
		
		AddressCSVFileParser parser = new AddressCSVFileParser();
		
		parser.parseFile("/Users/aneela/csv/testAddress.csv");		
		
		List<AddressModel> addressModelList = parser.getAddressModelList();
		//Map<String, ExpectedAddressResult> expectedAddressResultMap = parser.getExpectedAddressResultMap();
		
		for(AddressModel addressModel : addressModelList) {
		
			DeliveryAddressValidator validator = new DeliveryAddressValidator(addressModel);
			
			
			
			ActionResult actionResult = new ActionResult();
			
			
			boolean isAddressValid = validator.validateAddress(actionResult);	
			

			System.out.println("isAddressValid:"+isAddressValid);
			
			System.out.println("ActionResult : " + actionResult.toString());						
			
			System.out.println("Scrubbed Address : " + validator.getScrubbedAddress().toString());
		   
			
		}
	}
	
	
	@Test
	public void testValidateAddressWithScrubAddress() throws FDResourceException {		
		
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
		
		AddressCSVFileParser parser = new AddressCSVFileParser();
		
		parser.parseFile("/Users/aneela/csv/testAddress1.csv");		
		
		List<AddressModel> addressModelList = parser.getAddressModelList();
		//Map<String, ExpectedAddressResult> expectedAddressResultMap = parser.getExpectedAddressResultMap();
		
		for(AddressModel addressModel : addressModelList) {
		
			DeliveryAddressValidator validator = new DeliveryAddressValidator(addressModel);
			
			
			
			ActionResult actionResult = new ActionResult();
			
			
			boolean isAddressValid = validator.validateAddress(actionResult);
			
			System.out.println("isAddressValid:"+isAddressValid);
			
			System.out.println("ActionResult : " + actionResult.toString());						
			
			System.out.println("Scrubbed Address : " + validator.getScrubbedAddress().toString());
		   
			
		}
	}
	
/*	@Test
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
*/

}
