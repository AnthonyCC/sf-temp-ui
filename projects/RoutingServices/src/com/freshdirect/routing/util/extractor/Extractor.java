package com.freshdirect.routing.util.extractor;

import java.util.List;

public class Extractor {
	
	public static void main(String a[]) throws Exception {
		AddressExtractionManager addressManager = new AddressExtractionManager();
		//addressManager.collectAddressByOrderDate("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\ADDRESS_BYORDER_SEP012006.ser"
		//												, "01-SEP-2006");
		//addressManager.collectBuildingFromAddress("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\ADDRESS_BYORDER_SEP012006.ser"
		//												, "C:\\UPSData\\ADDRESSBYORDER\\BUILDING\\BUILDING_BYORDER_SEP012006.ser");
		
		//addressManager.compareCollectLowQualityBuildings("C:\\UPSData\\ADDRESSBYORDER\\BUILDING\\BUILDING_BYORDER_SEP012006.ser"
		//		, "C:\\UPSData\\ADDRESSBYORDER\\BUILDINGNOORDER\\BUILDING_NOORDER_SEP012006.ser"
		//		, "C:\\UPSData\\ADDRESSBYORDER\\BUILDINGNOORDER\\BUILDING_WITHORDER_SEP012006.ser");
		
		//addressManager.splitFile("C:\\UPSData\\ADDRESSBYORDER\\BUILDING\\BUILDING_BYORDER_SEP012006.ser"
			//						, "C:\\UPSData\\ADDRESSBYORDER\\BUILDING\\ZONEMISMATCH\\", 5000, "BUILDING_BYORDER_SEP012006_");
		
		//addressManager.checkZoneMismatch("C:\\UPSData\\ADDRESSBYORDER\\BUILDING\\ZONEMISMATCH\\BUILDING_BYORDER_SEP012006_16.ser"
					//						, "C:\\UPSData\\ADDRESSBYORDER\\BUILDING\\ZONEMISMATCH\\BUILDING_BYORDER_SEP012006_16.csv");
		//List tmpList = (List)addressManager.deSerialize("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\LOCATION_BYORDER_SEP012006_ASSIGNED.ser");
		//System.out.println("tmpList >"+tmpList.size());
		//addressManager.collectLocationsFromAddress("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\ADDRESS_BYORDER_SEP012006.ser",
				//"C:\\UPSData\\ADDRESSBYORDER\\MASTER\\LOCATION_BYORDER_SEP012006.sql");
		//addressManager.convertFile("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\LOCATION_BYORDER_SEP012006.csv"
							//,"C:\\UPSData\\ADDRESSBYORDER\\MASTER\\LOCATION_BYORDER_SEP012006_NULLFORMATTED.csv");
		//addressManager.collectLocationsFromAddressEx("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\ADDRESS_BYORDER_SEP012006.ser",
				//"C:\\UPSData\\ADDRESSBYORDER\\MASTER\\LOCATION_BYORDER_SEP012006_ASSIGNEDEXT.ser");
		addressManager.saveLocations("C:\\UPSData\\ADDRESSBYORDER\\MASTER\\LOCATION_BYORDER_SEP012006_ASSIGNEDEXT.ser");
		
	}
}
