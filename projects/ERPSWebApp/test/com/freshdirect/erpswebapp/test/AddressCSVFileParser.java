package com.freshdirect.erpswebapp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.CSVFileParser;


public class AddressCSVFileParser extends CSVFileParser{
	
	List<AddressModel> addressModelList;
	
	Map<String, ExpectedAddressResult> expectedAddressResultMap;
	
	public AddressCSVFileParser() {
		super();
		addressModelList = new ArrayList<AddressModel>();
		//expectedAddressResultMap = new HashMap<String, ExpectedAddressResult>();
	}
	
	
	
	@Override
	protected void makeObjects(String[] line) throws BadDataException {
		
		AddressModel addressModel = new AddressModel();
		
		addressModel.setAddress1(line[0]);
		addressModel.setAddress2(line[1]);
		addressModel.setCity(line[2]);
		addressModel.setState(line[3]);
		addressModel.setZipCode(line[4]);
		addressModel.setCountry(line[5]);
		
//		ExpectedAddressResult expectedAddressResult = new ExpectedAddressResult();
//		expectedAddressResult.setScrubbedStreet(line[6]);
//		expectedAddressResult.setLongitude(Double.parseDouble(line[7]));
//		expectedAddressResult.setLatitude(Double.parseDouble(line[8]));
		
		//expectedAddressResultMap.put(addressModel.getAddress1(), expectedAddressResult);
		
		addressModelList.add(addressModel);
	}

	public List<AddressModel> getAddressModelList() {
		return addressModelList;
	}

	public void setAddressModelList(List<AddressModel> addressModelList) {
		this.addressModelList = addressModelList;
	}


	public Map<String, ExpectedAddressResult> getExpectedAddressResultMap() {
		return expectedAddressResultMap;
	}



	public void setExpectedAddressResultMap(
			Map<String, ExpectedAddressResult> expectedAddressResultMap) {
		this.expectedAddressResultMap = expectedAddressResultMap;
	}
	
}
