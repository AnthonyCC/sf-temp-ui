package com.freshdirect.telargo.test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetWithStateDto;
import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetWithStateDto;
import org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.CurrentStatusAssetsFilter;

import com.freshdirect.telargo.model.FDAssetInfo;
import com.freshdirect.telargo.proxy.stub.coreservices.CoreService;
import com.freshdirect.telargo.service.TelargoServiceLocator;

public class TelargoCoreService {
	
	private static final SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String TELARGO_CORESERVICE_PROVIDERURL = "https://host.eu.tfc.telargo.com/GeneralServices/CoreService";
	private static final String TELARGO_CORESERVICE_USERNAME = "FDWSaccess";
	private static final String TELARGO_CORESERVICE_PASSWORD = "FD2011access!";
	
	public static void main(String args[]) throws Exception {
		
		//System.setProperty("javax.net.ssl.trustStore","C:\\bea10\\jrockit_160_05\\bin\\clientTelargo");  
		//System.setProperty("javax.net.ssl.trustStorePassword", "changeit");  
		
		new TelargoCoreService().getAssetsWithLastState();			
	}
	
	public List<FDAssetInfo> getAssetsWithLastState() throws Exception {
		
		CoreService service = TelargoServiceLocator.getInstance(TELARGO_CORESERVICE_PROVIDERURL
										, TELARGO_CORESERVICE_USERNAME, TELARGO_CORESERVICE_PASSWORD).getTelargoCoreService();		
		
		CurrentStatusAssetsFilter filter = new CurrentStatusAssetsFilter();
		//filter.setRegistrationNumber("FD-TelargoTest");
		
		ArrayOfAssetWithStateDto assetStateArray = service.getAssetsWithLastState(filter);
		List<FDAssetInfo> assetInfos = new ArrayList<FDAssetInfo>();
		
		if(assetStateArray != null) {
			AssetWithStateDto[] assetStateDtos = assetStateArray.getAssetWithStateDto();
			if(assetStateDtos != null) {
				for(AssetWithStateDto assetDTO : assetStateDtos) {
					assetInfos.add(new FDAssetInfo(assetDTO));					
				}
			} else {
				System.out.println("------------ Empty AssetWithStateDto ------------------");
			}
		} else {
			System.out.println("------------ Empty ArrayOfAssetWithStateDto ------------------");
		}
		System.out.println("Total Assets:"+assetInfos.size());
		for(FDAssetInfo assetInfo : assetInfos) {			
			System.out.println(assetInfo);
		}
		return assetInfos;
	}
	
	public static Calendar parseTimeStamp(String timestamp) throws Exception {

		Date d = sdf.parse(timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.setTimeZone(TimeZone.getDefault());
		return cal;
	}
	
	public static String toBeanString(Object src) {
	    String lineSeaparator = System.getProperty("line.separator");
	    StringBuffer buffer = new StringBuffer(lineSeaparator);
	    buffer.append("|------------------------|");
	    /*buffer.append("|---------");
	    buffer.append(this.getClass());
	    buffer.append("---------|");*/
	    buffer.append(lineSeaparator);
	    Method[] methods = src.getClass().getMethods();
	    if(methods != null && methods.length >0){
	        Method method = null;
	        for(int i =0; i< methods.length; i++){
	            method = methods[i];
	            if(method.getName().startsWith("get")
	            && !method.getName().startsWith("getClass")){
	            buffer.append
	                           (method.getName().replaceAll("get",""));
	            buffer.append(" = ");
	            Object[] params=null;
	            try {
	                buffer.append(method.invoke(src, params));
	            } catch (Exception e) {
	                buffer.append("  ");
	            }
	            buffer.append(lineSeaparator);
	            }
	        }
	    }
	    buffer.append("|------------------------|");
	    buffer.append(lineSeaparator);
	    return buffer.toString();
	}
	
}
