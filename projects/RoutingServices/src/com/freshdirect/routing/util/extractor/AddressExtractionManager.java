package com.freshdirect.routing.util.extractor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.ejb.DlvManagerDAO;
import com.freshdirect.delivery.ejb.GeographyDAO;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.routing.util.extractor.model.ExtractAddressModel;

public class AddressExtractionManager extends BaseExtractor {
	
	public void collectAddressByOrderDate(String destinationFile, String date) throws Exception {
		
		Connection connection = null;
		try {
		
			connection = getConnection();		
			List addressByOrderDays = DataExtractorDAO.getAddressByOrderDate
														(connection, date);		
			serializeData(addressByOrderDays, destinationFile);		
		} finally {
			if (connection != null)
				connection.close();			
		}
	}
	
	public void collectBuildingFromAddress(String sourceFile, String destinationFile) throws Exception {
		
		List buildingFromAddress = (List)deSerialize(sourceFile);
		List buildings = new ArrayList();
		Map streetZip = new HashMap();
		Iterator iterator = buildingFromAddress.iterator();
		ExtractAddressModel address = null;
		String scrubbedStreet = null;
		
		while (iterator.hasNext()) {
			address = (ExtractAddressModel) iterator.next();
			scrubbedStreet = RoutingUtil.standardizeStreetAddress(
					trimNoNull(address.getAddress1()), trimNoNull(address.getAddress2()));
			if(!streetZip.containsKey(scrubbedStreet+"$"+address.getZip())) {
				address.setScrubbedAddress(scrubbedStreet);
				buildings.add(address);
				streetZip.put(scrubbedStreet+"$"+address.getZip(), null);
			}
		}
		serializeData(buildings, destinationFile);
	}
	
	public void compareCollectLowQualityBuildings(String sourceFile, String destinationFile1, String destinationFile2) throws Exception {
		
		Connection connection = null;
		StringBuffer strBuf = new StringBuffer();
		try {
						
			connection = getConnection();
					
			List addressWithOrders = (List)deSerialize(sourceFile);
			List lowQualityBuildings = DataExtractorDAO.getLowQualityBuildings(connection);
			
			System.out.println("addressWithOrders :" + addressWithOrders.size());
			Iterator tmpIterator = 	addressWithOrders.iterator();
			Map scrubbedStAddress = new HashMap();
			ExtractAddressModel addressWithOrder = null;
			ExtractAddressModel refAddressWithOrder = null;
			
			while(tmpIterator.hasNext()) {
				addressWithOrder = (ExtractAddressModel)tmpIterator.next();
				scrubbedStAddress.put(addressWithOrder.getScrubbedAddress()+"$"+addressWithOrder.getZip(), addressWithOrder);
			}
			
			tmpIterator = 	lowQualityBuildings.iterator();
			
			List hasOrderList = new ArrayList();
			List hasNoOrderList = new ArrayList();
			String srubbedAddress = null;
			
			while(tmpIterator.hasNext()) {
				addressWithOrder = (ExtractAddressModel)tmpIterator.next();
				srubbedAddress = addressWithOrder.getAddress1()+"$"+addressWithOrder.getZip();
				refAddressWithOrder = (ExtractAddressModel)scrubbedStAddress.get(srubbedAddress);
				if(scrubbedStAddress.containsKey(srubbedAddress)) {
					addressWithOrder.setRefId(refAddressWithOrder.getRefId());
					hasOrderList.add(addressWithOrder);
				} else {
					hasNoOrderList.add(addressWithOrder);
					if(strBuf.length() != 0) {
						strBuf.append(",");
					}
					strBuf.append("'"+addressWithOrder.getId()+"'");
				}
			}			
			
			System.out.println("HAS ORDER LIST:" + hasOrderList.size());
			System.out.println("HAS NO ORDER LIST:" + hasNoOrderList.size());
			System.out.println("(" + strBuf.toString() + ")");
			
			serializeData(hasNoOrderList, destinationFile1);
			serializeData(addressWithOrder, destinationFile2);
		} finally {			
			connection.close();
		}
	}
	
	public void checkZoneMismatch(String sourceFile, String destinationFile) throws Exception {
		
		Connection conn = null;
		
		GeographyDAO dao = new GeographyDAO();
		
		GeographyServiceProxy proxy = new GeographyServiceProxy();
		List buildingFromAddress = (List)deSerialize(sourceFile);
		//List buildings = new ArrayList();
		StringBuffer results = new StringBuffer();
		EnumDeliveryStatus statusCorp1 = null;
		EnumDeliveryStatus statusCorp2 = null;
		EnumDeliveryStatus statusHome1 = null;
		EnumDeliveryStatus statusHome2 = null;
		boolean hasCorpMismatch = false;
		boolean hasHomeMismatch = false;
		
		Iterator iterator = buildingFromAddress.iterator();
		ExtractAddressModel addressEx = null;
		IBuildingModel buildingModel = null;
		AddressModel address = null;
		int intCount = 0;
		int intStageCount = 0;
		results.append("SCRUBBED ST").append(",").append("ZIPCODE").append(",")
					.append("HOME MISMATCH").append(",").append("CORP MISMATCH")
					.append(",").append("STORE LAT").append(",").append("STORE LONG")
					.append(",").append("LOCDB LAT").append(",").append("LOCDB LONG")
					.append("\n");
		try {
			while (iterator.hasNext()) {
				addressEx = (ExtractAddressModel) iterator.next();
				buildingModel = proxy.getBuildingLocation(addressEx.getScrubbedAddress(), addressEx.getZip());
				if(buildingModel != null && buildingModel.getBuildingId() != null) {
					address = this.getFDAddress(addressEx);
					if(address != null && RoutingUtil.isGeocodeAcceptable
											(buildingModel.getGeographicLocation().getConfidence(), buildingModel.getGeographicLocation().getQuality())) {
						
						if(conn == null || intCount > 2500) {
							intCount = 0;
							if(conn != null)
								conn.close();
							
							conn = getConnection();
						}
						if(intStageCount == 1000) {
							System.out.println("STAGE >>"+intStageCount);
							intStageCount = 0;
						}
						intCount++;
						intStageCount++;
						AddressInfo info = address.getAddressInfo() == null ? new AddressInfo() : address.getAddressInfo();
						info.setLatitude(Double.parseDouble(buildingModel.getGeographicLocation().getLatitude()));
						info.setLongitude(Double.parseDouble(buildingModel.getGeographicLocation().getLongitude()));				
						address.setAddressInfo(info);
						
						statusCorp1 = getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.CORPORATE));				
						statusHome1 = getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.HOME));
						
						dao.geocodeAddress(address,true, false, conn);
						statusHome2 = getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.HOME));
						statusCorp2 = getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.CORPORATE));
						
						hasCorpMismatch = false;
						hasHomeMismatch = false;
						if(("Donot Deliver".equalsIgnoreCase(statusCorp1.getName()) 
								&& "Deliver".equalsIgnoreCase(statusCorp2.getName()))) {
							hasCorpMismatch = true;
						}
						if("Donot Deliver".equalsIgnoreCase(statusHome1.getName())	
								&& "Deliver".equalsIgnoreCase(statusHome2.getName())) {
							hasHomeMismatch = true;
						}
						if(hasCorpMismatch || hasHomeMismatch)
							results.append(buildingModel.getSrubbedStreet()).append(",").append(buildingModel.getZipCode()).append(",")
							.append(hasHomeMismatch).append(",").append(hasCorpMismatch)
							.append(",").append(address.getLatitude()).append(",").append(address.getLongitude())
							.append(",").append(buildingModel.getGeographicLocation().getLatitude())
												.append(",").append(buildingModel.getGeographicLocation().getLongitude())
							.append("\n");
					}
				}
			}
		} finally {
			writeFile(results.toString(), destinationFile);
			if(conn != null)
				conn.close();
		}
		
	}
	
	public void collectLocationsFromAddressEx(String sourceFile, String destinationFile) throws Exception {
		
		Connection conn = null;
		try {
			List buildingFromAddress = (List)deSerialize(sourceFile);
			List dataList = new ArrayList();
			Map streetZip = new HashMap();
			Iterator iterator = buildingFromAddress.iterator();
			ExtractAddressModel address = null;
			ExtractAddressModel addressExt = null;
			String scrubbedStreet = null;
			String apt = null;
			int intCount = 0;
			int intTotalCount = 0;
			String buildingId = null;
			
			while (iterator.hasNext()) {
				if(conn == null || intCount > 2500) {
					intCount = 0;
					if(conn != null)
						conn.close();
					
					conn = getConnection();
					System.out.println("STAGE COMPLETE - "+intTotalCount);
				}
				intCount++;
				intTotalCount++;
				address = (ExtractAddressModel) iterator.next();
				scrubbedStreet = RoutingUtil.standardizeStreetAddress(
						trimNoNull(address.getAddress1()), trimNoNull(address.getAddress2()));
				apt = trimNoNull(address.getApartment()).toUpperCase();
				buildingId = DataExtractorDAO.getBuildingId(conn, scrubbedStreet, address.getZip().trim() );
				if(buildingId != null) {
					if(!streetZip.containsKey(buildingId+"$"+apt)) {
						
						streetZip.put(buildingId+"$"+apt, null);
						
						address.setApartment(apt);
						address.setRefAddressId(buildingId);
						
						addressExt = new ExtractAddressModel();
						
						addressExt.setAddress1(address.getAddress1());
						addressExt.setAddress2(address.getAddress2());
						addressExt.setApartment(apt);
						addressExt.setCity(address.getCity());
						addressExt.setState(address.getState());
						addressExt.setZip(address.getZip().trim());
						addressExt.setCountry(address.getCountry());
						addressExt.setScrubbedAddress(scrubbedStreet);
						addressExt.setRefAddressId(buildingId);
					
						
						dataList.add(addressExt);
							
					}
				}
			}
			this.serializeData(dataList, destinationFile);
		} finally {
			if(conn != null)
				conn.close();
		}
	}

	public void collectLocationsFromAddress(String sourceFile, String destinationFile) throws Exception {
		
		Connection conn = null;
		try {
			List buildingFromAddress = (List)deSerialize(sourceFile);
			StringBuffer strBuf = new StringBuffer();
			Map streetZip = new HashMap();
			Iterator iterator = buildingFromAddress.iterator();
			ExtractAddressModel address = null;
			String scrubbedStreet = null;
			String apt = null;
			int intCount = 0;
			int intTotalCount = 0;
			String buildingId = null;
			
			while (iterator.hasNext()) {
				if(conn == null || intCount > 2500) {
					intCount = 0;
					if(conn != null)
						conn.close();
					
					conn = getConnection();
					System.out.println("STAGE COMPLETE - "+intTotalCount);
				}
				intCount++;
				intTotalCount++;
				address = (ExtractAddressModel) iterator.next();
				scrubbedStreet = RoutingUtil.standardizeStreetAddress(
						trimNoNull(address.getAddress1()), trimNoNull(address.getAddress2()));
				apt = trimNoNull(address.getApartment()).toUpperCase();
				buildingId = DataExtractorDAO.getBuildingId(conn, scrubbedStreet, address.getZip().trim() );
				if(buildingId != null) {
					if(!streetZip.containsKey(buildingId+"$"+apt)) {
						
						streetZip.put(buildingId+"$"+apt, null);
						strBuf.append("INSERT INTO DLV.STAGGING_DELIVERY_LOCATION VALUES(")
							.append("STAGGING_DELIVERY_LOCATION_SEQ.nextval")
							.append(", '").append(buildingId).append("'")
							.append(", '").append(apt).append("'")
							.append(", NULL);\n");
							
					}
				}
			}
			this.writeFile(strBuf.toString(), destinationFile);
		} finally {
			if(conn != null)
				conn.close();
		}
	}
	
	public void saveLocations(String sourceFile) throws Exception {
		
		List locations = (List)deSerialize(sourceFile);		
		List addresses = new ArrayList();
		Iterator iterator = locations.iterator();
			
		while (iterator.hasNext()) {
			addresses.add(iterator.next());
			if(addresses.size() == 50000) {
				DataExtractorDAO.insertLocations(addresses, getDataSource(true, "fddatasource"));
				addresses  = new ArrayList();
				System.out.println("Stage Complete");
			}
		}
		if(addresses.size() > 0) {
			DataExtractorDAO.insertLocations(addresses, getDataSource(true, "fddatasource"));
			System.out.println("End Stage Complete");
		}
	}
	
	public void convertFile(String sourceFile, String destinationFile) throws Exception {
		DataInputStream in = null;
		StringBuffer strBuf = new StringBuffer();
		String strLine = null;
		try {
			FileInputStream fstream = new FileInputStream(sourceFile);
			
		    in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    StringTokenizer st;
		    int intCount = 0;
		    while ((strLine = br.readLine()) != null)   {	    	
		      if(strLine.trim().length() > 0) {
		    	  st = new StringTokenizer(strLine, ",");
		    	  intCount++;
		    	  st.nextToken();
		    	  strBuf.append("'").append(intCount).append("','");
		    	  strBuf.append(st.nextToken()).append("',");
		    	  strBuf.append(decode(st.nextToken())).append(",");
		    	  strBuf.append(decode(st.nextToken())).append("\n");
		      }
		    }
		    
		} catch(Exception e) {
			System.out.println(strLine);
			throw e;
		} finally {
			in.close();
		}
		this.writeFile(strBuf.toString(), destinationFile);
	}
	
	private String decode(String str) {
		if("NULL".equalsIgnoreCase(str)) {
			return "NULL";
		}
		return "'"+str+"'";
	}
}
