package com.freshdirect.payment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Category;

import com.freshdirect.enums.EnumModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.BillingCountryDAO;
public class BillingCountryInfo extends EnumModel {

	private static final Category LOGGER = LoggerFactory.getInstance(BillingCountryInfo.class);
	private static final long	serialVersionUID	= -3499001811399219072L;
	
	private static Map<String, BillingCountryInfo> enums = null;	    
	private List<BillingRegionInfo> regions =null;
	private List<String> regionCodes =null;
	private Pattern zipCheckPattern=null;
	private static List<BillingCountryInfo> countries=null;
    private static long lastRefresh = 0;
    

	 private static void refresh() {
	        refresh(false);
	}
	 private synchronized static void refresh(boolean force) {
	        long t = System.currentTimeMillis();

	        if (force || ((t - lastRefresh) > (FDStoreProperties.getCountryInfoRefreshInterval()*1000*60))) {
	            loadEnums();
	            lastRefresh = t;
	            LOGGER.info("Reloaded BillingCountryInfo..");
	            
	        }
	    }

	public BillingCountryInfo(String code, String name,List<BillingRegionInfo> regions, String zipCheckRegex ) {
		super(code, name, null);
		this.regions=regions;
		if(regions!=null) {
			regionCodes=new ArrayList<String>(regions.size());
			for ( BillingRegionInfo ri : regions ) {
				regionCodes.add(ri.getCode());
			}
		}
		if(zipCheckRegex!=null && !"".equals(zipCheckRegex.trim()))
			this.zipCheckPattern=Pattern.compile(zipCheckRegex);
	}
    
	public static BillingCountryInfo getEnum(String code) {
		refresh();
		return enums.get(code);
	}

	public static Map<String, BillingCountryInfo> getEnumMap() {
		refresh();
		return enums;
	}

	public static List<BillingCountryInfo> getEnumList() {
		refresh();
		return countries;
	}

	
	private static void loadEnums() {
		
		HashMap<String, BillingCountryInfo> _enums = new HashMap<String, BillingCountryInfo>();
		List<BillingCountryInfo> lst = loadEnums(BillingCountryDAO.class);
		for ( BillingCountryInfo e : lst ) {
					_enums.put(e.getCode(), e);
		}
		enums=Collections.unmodifiableMap(_enums);
		List<BillingCountryInfo> values=new ArrayList<BillingCountryInfo>(enums.values());
		Collections.sort(values,COMPARE_BY_NAME);
		countries=Collections.unmodifiableList(values);
	
	}

	public List<BillingRegionInfo> getRegions() {
		return regions ;
	}
	
	public boolean hasRegion(String region) {
		if(regionCodes==null) 
			return false;
		return regionCodes.contains(region);
	}
	
	public BillingRegionInfo getRegion(String region) {
		if(!hasRegion(region))
			return null;
		else {
			int i=regionCodes.indexOf(region);
			return regions.get(i);
		}
	}
	public static void main(String[] a) {
		BillingCountryInfo us=BillingCountryInfo.getEnum("US");
		System.out.println(us);
		
		 us=BillingCountryInfo.getEnum("FX");
		System.out.println(us);
		us=BillingCountryInfo.getEnum(null);
		System.out.println(us);
	}

	@Override
	public String toString() {
		return "BillingCountryInfo [regions=" + "" + ", zipCheckPattern="
				+ zipCheckPattern +", code"+getCode()+", name"+getName()+ "]";
	}
	
	public Pattern getZipCheckPattern() {
		return zipCheckPattern;
	}
	 public static Comparator<BillingCountryInfo> COMPARE_BY_NAME = new Comparator<BillingCountryInfo>(){
			public int compare(BillingCountryInfo bc1, BillingCountryInfo bc2) {
			   	return bc1.getName().compareTo(bc2.getName());
			}
		};
	
}
