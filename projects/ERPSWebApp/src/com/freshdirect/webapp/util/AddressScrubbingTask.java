package com.freshdirect.webapp.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.controller.data.response.AddressScrubbingResponse;
import com.freshdirect.logistics.delivery.dto.ScrubbedAddress;

/**
 * @author Aniwesh Vatsal
 *
 */
public class AddressScrubbingTask implements Callable{

	private final String [] OUTPUT_FILE_HEADER_MAPPING = {"CUSTOMER_ID","SCRUBBED_ADDRESS","ADDRESS","APARTMENT","CITY","STATE","ZIP","DELIVERY_TYPE","Street/HighRise","Residential/Commercial","SS_SCRUBBED_ADDRESS","RDI","Address Type","Status"};
	
	private Map<String,ScrubbedAddress> addressInputMap = new HashMap<String,ScrubbedAddress>();
	
	private List<ScrubbedAddress> csvAddrList = new ArrayList<ScrubbedAddress>();
	
	@Override
	public String call() throws Exception {
		String csvReportline = "";
		List<AddressScrubbingResponse> addressScrubbingResponses = getScrubbedAddress(csvAddrList);
		csvReportline = scrubbedAddressCSVReport(addressInputMap , addressScrubbingResponses);
		return csvReportline;
	}

	/**
     * @param addressList
     * @return
     * @throws RemoteException
     */
    private List<AddressScrubbingResponse> getScrubbedAddress(List<ScrubbedAddress> addressList) throws RemoteException{
		List<AddressScrubbingResponse> addressScrubbingResponses = new ArrayList<AddressScrubbingResponse>();
		try {
			int starIndex = 0;
			while(starIndex < addressList.size()){
				int endIndex = starIndex+99;
				if( endIndex > addressList.size()){
					endIndex = addressList.size();
				}
				AddressScrubbingResponse response = FDDeliveryManager.getInstance().getScrubbedAddress(addressList.subList(starIndex, endIndex));
				addressScrubbingResponses.add(response);
				starIndex = endIndex;
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
		} catch (FDInvalidAddressException e) {
			e.printStackTrace();
		}
		
		return addressScrubbingResponses;
	}
    
    /**
	 * @param addMap
	 * @param addressScrubbingResponses
	 */
	private String scrubbedAddressCSVReport(Map<String,ScrubbedAddress> addMap , List<AddressScrubbingResponse>  addressScrubbingResponses){
		StringBuffer csvReport = new StringBuffer();
		// Add Header
		csvReport.append(addReportHeader(OUTPUT_FILE_HEADER_MAPPING));
		for(AddressScrubbingResponse addressScrubbingResponse: addressScrubbingResponses){
			List<ScrubbedAddress> scrubbedAddresses = addressScrubbingResponse.getScrubbedAddress();
			for(ScrubbedAddress scrubbedAddress : scrubbedAddresses){
				String originalRowData = "";
				String ssRowData ="";
				if(addMap.containsKey(scrubbedAddress.getId())){
					ScrubbedAddress temp = addMap.get(scrubbedAddress.getId());
					originalRowData=temp.getCustomerId()+","+temp.getScrubbedAddress()+","+temp.getAddress1()+","+temp.getApartment()+","+temp.getCity()+","+temp.getState()+","+temp.getZipCode()+","+temp.getServiceType()+","+temp.getAddressType()+","+temp.getRdi();
				}
				if(scrubbedAddress.getScrubbingResult().equalsIgnoreCase("Match")){
					ssRowData = originalRowData+","+scrubbedAddress.getSsScrubbedAddress()+","+scrubbedAddress.getServiceType()+","+scrubbedAddress.getAddressType()+","+scrubbedAddress.getScrubbingResult()+"\n";
					csvReport.append(ssRowData);
				}else { // Check for suggestions or No Match from SmartyStreets
					if(scrubbedAddress.getScrubbingResult().equalsIgnoreCase("Suggestions") && (scrubbedAddress.getSuggestions() == null || scrubbedAddress.getSuggestions().isEmpty())){
						// DPV_MATCH_CODE = S/D
						ssRowData = originalRowData+","+scrubbedAddress.getSsScrubbedAddress()+","+scrubbedAddress.getServiceType()+","+scrubbedAddress.getAddressType()+","+scrubbedAddress.getScrubbingResult()+"\n";
						csvReport.append(ssRowData);
					}
					else if(scrubbedAddress.getSuggestions() != null && !scrubbedAddress.getSuggestions().isEmpty()){
						String suggestionRow="";
						ssRowData = originalRowData+","+scrubbedAddress.getSsScrubbedAddress()+","+scrubbedAddress.getServiceType()+","+scrubbedAddress.getAddressType()+","+"Suggestions"+"\n";
						csvReport.append(ssRowData);
						for(ScrubbedAddress suggestion : scrubbedAddress.getSuggestions()){
							suggestionRow = originalRowData+","+suggestion.getSsScrubbedAddress()+","+suggestion.getServiceType()+","+suggestion.getAddressType()+","+"Suggestions"+"\n";
							csvReport.append(suggestionRow);
						}
					}else{
						csvReport.append(originalRowData+",,,,No Match\n");
					}
				}
			}
		}
		return csvReport.toString();
	}
	
	/**
	 * @param header
	 * @return
	 */
	private String addReportHeader(String[] header){
		String col="";
		for(String coulmnName : header){
			col=col+coulmnName+",";
		}
		return col.substring(0,col.length()-1)+"\n";
	}
	
	public Map<String, ScrubbedAddress> getAddressInputMap() {
		return addressInputMap;
	}

	public void setAddressInputMap(Map<String, ScrubbedAddress> addressInputMap) {
		this.addressInputMap = addressInputMap;
	}

	public List<ScrubbedAddress> getCsvAddrList() {
		return csvAddrList;
	}

	public void setCsvAddrList(List<ScrubbedAddress> csvAddrList) {
		this.csvAddrList = csvAddrList;
	}

	
}
