package com.freshdirect.payment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.freshdirect.enums.EnumModel;
import com.freshdirect.payment.ejb.BillingCountryDAO;
public class BillingCountryInfo extends EnumModel {


	private static final long	serialVersionUID	= -3499001811399219072L;
	
	private static Map<String, BillingCountryInfo> enums = null;	    
	private List<BillingRegionInfo> regions =null;
	private Pattern zipCheckPattern=null;
	private static List<BillingCountryInfo> countries=null;
	
	

	public BillingCountryInfo(String code, String name,List<BillingRegionInfo> regions, String zipCheckRegex ) {
		super(code, name, null);
		this.regions=regions;
		if(zipCheckRegex!=null && !"".equals(zipCheckRegex.trim()))
			this.zipCheckPattern=Pattern.compile(zipCheckRegex);
	}
    
	public static BillingCountryInfo getEnum(String code) {
		loadEnums();
		return enums.get(code);
	}

	public static Map<String, BillingCountryInfo> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<BillingCountryInfo> getEnumList() {
		loadEnums();
		return countries;
	}

	
	private static void loadEnums() {
		if (enums == null) {
			
			enums = new HashMap<String, BillingCountryInfo>();
			List<BillingCountryInfo> lst = loadEnums(BillingCountryDAO.class);
			for ( BillingCountryInfo e : lst ) {
				enums.put(e.getCode(), e);
			}
			List<BillingCountryInfo> values=new ArrayList<BillingCountryInfo>(enums.values());
			Collections.sort(values,COMPARE_BY_NAME);
			countries=values;
			
			
		}
	}

	public List<BillingRegionInfo> getRegions() {
		return regions ;
	}
	
	public static void main(String[] a) {
		BillingCountryInfo us=BillingCountryInfo.getEnum("US");
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
