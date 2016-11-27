package com.freshdirect.transadmin.web.util;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.GeoPoint;
import com.freshdirect.routing.model.IGeoPoint;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.model.RouteMapping;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
/**
 * @deprecated Driving Directions will be available in Airclic
 * @author tbalumuri
 *
 */
public class CoPilotUtil {
	

	public static ModelAndView gpsDrivingDirectionsHandlerEx(HttpServletRequest request, HttpServletResponse response
																, DomainManagerI domainManagerService) throws ServletException {
	return null;
	}
	
	private static String getStopOpt(int stopNo) {
		String carriageReturn = "\r\n";
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(carriageReturn).append("Start StopOpt=Stop ").append(stopNo).append(carriageReturn);
		strBuf.append("Cost=0").append(carriageReturn);
		strBuf.append("Time=0").append(carriageReturn);
		strBuf.append("Loaded=1").append(carriageReturn);
		strBuf.append("OnDuty=1").append(carriageReturn);
		strBuf.append("End StopOpt").append(carriageReturn);
		return strBuf.toString();
	}
	
	private static String getGPSMetaData() {
		String carriageReturn = "\r\n";
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("Data Version:4.5.2.1").append(carriageReturn);
		strBuf.append("Start Trip=\\Storage Card\\copilot\\NA\\North America Truck\\save\\Testtrip.trp").append(carriageReturn);
		strBuf.append("Memo=").append(carriageReturn);
		strBuf.append("DefaultTrip=").append(carriageReturn);
		strBuf.append("HasRoadSpeeds=0").append(carriageReturn);
		strBuf.append("HasStopOptions=1").append(carriageReturn);
		strBuf.append("RouteType=Quickest").append(carriageReturn);
		strBuf.append("VehicleType=Truck").append(carriageReturn);
		strBuf.append("RoundTrip=0").append(carriageReturn);
		strBuf.append("HubRouting=0").append(carriageReturn);
		strBuf.append("SeqType=0").append(carriageReturn);
		strBuf.append("FavorAvoid=1").append(carriageReturn);
		strBuf.append("DistUnits=0").append(carriageReturn);
		strBuf.append("TakeABreak=0").append(carriageReturn);
		strBuf.append("BreakTime=0").append(carriageReturn);
		strBuf.append("BreakWaitTime=0").append(carriageReturn);
		strBuf.append("BorderWaitTime=0").append(carriageReturn);
		strBuf.append("RestrTrucks=1").append(carriageReturn);
		strBuf.append("RestrTrucksOnly=0").append(carriageReturn);
		strBuf.append("TranslateAlias=0").append(carriageReturn);
		strBuf.append("StateOrder=1").append(carriageReturn);
		strBuf.append("FerryMiles=1").append(carriageReturn);
		strBuf.append("LoadedCost=13600").append(carriageReturn);
		strBuf.append("EmptyCost=13600").append(carriageReturn);
		strBuf.append("FirstStop=1").append(carriageReturn);
		strBuf.append("HazType=0").append(carriageReturn);
		strBuf.append("BordersOpen=1").append(carriageReturn);
		strBuf.append("CongestionZonesOpen=2").append(carriageReturn);
		strBuf.append("TollAvoid=0").append(carriageReturn);
		strBuf.append("NationalNetwork=0").append(carriageReturn);
		strBuf.append("53FootRouting=0").append(carriageReturn);
		strBuf.append("AccessRule=0").append(carriageReturn);
		strBuf.append("CountriesAsStates=0").append(carriageReturn);
		strBuf.append("ScenicRoute=0").append(carriageReturn);
		strBuf.append("PropaneRestricted=0").append(carriageReturn);
		strBuf.append("TollAdjust=175").append(carriageReturn);
		strBuf.append("TollType=Tolls Off").append(carriageReturn);
		strBuf.append("TollDiscounts=0").append(carriageReturn);
		strBuf.append("M2MRouting=0").append(carriageReturn);
		strBuf.append("M2MTime=Nominal").append(carriageReturn);
		strBuf.append("Length=-1").append(carriageReturn);
		strBuf.append("Width=-1").append(carriageReturn);
		strBuf.append("Height=-1").append(carriageReturn);
		strBuf.append("Weight=-1").append(carriageReturn);
		strBuf.append("WeightPerAxle=-1").append(carriageReturn);
		strBuf.append("UseHighwayOnly=0").append(carriageReturn);
		strBuf.append("TollClosed=0").append(carriageReturn);
		strBuf.append("FerryClosed=0").append(carriageReturn);
		strBuf.append("RVHeightRestr=150").append(carriageReturn);
		strBuf.append("RoadWeightAdjust=646464646464646464").append(carriageReturn);
		strBuf.append("DefAutoRoadSpeeds=0141412D1E05191E120137371E14050F190A").append(carriageReturn);
		strBuf.append("DefVanRoadSpeeds=0141412D1E05191E120137371E14050F190A").append(carriageReturn);
		strBuf.append("DefRVRoadSpeeds=013B3B291B05171B100137371E14050F190A").append(carriageReturn);
		strBuf.append("DefTruckRoadSpeeds=01323B190F050A1B05013232190F050A1705").append(carriageReturn);
		strBuf.append("DefBusRoadSpeeds=013B3B291B05171B100132321B12050E1709").append(carriageReturn);
		strBuf.append("DefTruckEditRoadSpeeds=013B3B291B05171B100132321B12050E1709").append(carriageReturn);
		strBuf.append("DefMotorcycleRoadSpeeds=0141412D1E05191E120137371E14050F190A").append(carriageReturn);
		strBuf.append("DefBicycleRoadSpeeds=0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A0A").append(carriageReturn);
		strBuf.append("DefWalkingRoadSpeeds=030303030303030303030303030303030303").append(carriageReturn);
		strBuf.append("DefCrowRoadSpeeds=030303030303030303030303030303030303").append(carriageReturn);
		strBuf.append("RptCurrency=US").append(carriageReturn);
		strBuf.append("RptConvRate=10000").append(carriageReturn);
		strBuf.append("Units=0").append(carriageReturn);
		strBuf.append("OversizePermit=0").append(carriageReturn);
		strBuf.append("NumAxles=5").append(carriageReturn);
		strBuf.append("LCV=0").append(carriageReturn);
		strBuf.append("CountryAbrev=4").append(carriageReturn);
		strBuf.append("FuelUnit=0").append(carriageReturn);
		strBuf.append("FuelCost=27100").append(carriageReturn);
		strBuf.append("MaintenenceCostLoaded=1200").append(carriageReturn);
		strBuf.append("MaintenenceCostEmpty=1200").append(carriageReturn);
		strBuf.append("CostTimeLoaded=401100").append(carriageReturn);
		strBuf.append("CostTimeEmpty=401100").append(carriageReturn);
		strBuf.append("GreenHouseGas=222000").append(carriageReturn);
		strBuf.append("MPGCostLoaded=60000").append(carriageReturn);
		strBuf.append("MPGCostEmpty=60000").append(carriageReturn);
		strBuf.append("End Trip").append(carriageReturn);
		
		return strBuf.toString();
	}
}
