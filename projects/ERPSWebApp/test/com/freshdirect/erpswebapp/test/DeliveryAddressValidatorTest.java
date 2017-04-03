package com.freshdirect.erpswebapp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.junit.Test;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.dataloader.CSVFileParser;
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
			
			
			boolean isAddressValid = validator.validateAddressWithoutScrub(actionResult);	
			
			System.out.println(""+isAddressValid);
			System.out.println("Address1:"+validator.getScrubbedAddress().getAddress1());
			System.out.println("Address2:"+validator.getScrubbedAddress().getAddress2());
			System.out.println("ScrubbedStreet:"+validator.getScrubbedAddress().getScrubbedStreet());
			System.out.println("Apartment:"+validator.getScrubbedAddress().getApartment());
			System.out.println("BuildingId:"+validator.getScrubbedAddress().getBuildingId());
			System.out.println("City:"+validator.getScrubbedAddress().getCity());
			System.out.println("CompanyName:"+validator.getScrubbedAddress().getCompanyName());
			System.out.println("Country:"+validator.getScrubbedAddress().getCountry());
			System.out.println("Latitude:"+validator.getScrubbedAddress().getLatitude());
			System.out.println("Longitude:"+validator.getScrubbedAddress().getLongitude());
			System.out.println("State:"+validator.getScrubbedAddress().getState());
			System.out.println("ZipCode:"+validator.getScrubbedAddress().getZipCode());
			System.out.println("ServiceType:"+validator.getScrubbedAddress().getServiceType());
			
			System.out.println("SsScrubbedAddress"+validator.getScrubbedAddress().getAddressInfo().getSsScrubbedAddress());
			System.out.println("ZoneCode"+validator.getScrubbedAddress().getAddressInfo().getZoneCode());
			System.out.println("County"+validator.getScrubbedAddress().getAddressInfo().getCounty());
			
			
		}
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
