package com.freshdirect.transadmin.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.AssetAttributeId;
import com.freshdirect.transadmin.model.AssetType;

public class AssetCSVParser extends CSVFileParser implements IParser {
	
	SynchronousParserClient client;
	Asset record;
	HashMap<String, Integer> fieldMap = new HashMap<String, Integer>();
	
	private static DateFormat MONTH_DATE_YEAR_FORMATER = new SimpleDateFormat("MM/dd/yyyy");
	private static DateFormat YEAR_MONTH_DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");
	
	public AssetCSVParser() {
		super();
		fieldMap.put("Asset Residual", new Integer(6));
		fieldMap.put("Body Length", new Integer(7));
		fieldMap.put("Body Make", new Integer(8));
		fieldMap.put("Body Serial#", new Integer(9));
		fieldMap.put("Body Year", new Integer(10));
		fieldMap.put("Body/Reefer Residual", new Integer(11));
		fieldMap.put("Chassis Engine Type", new Integer(12));
		fieldMap.put("Chassis Make", new Integer(13));
		fieldMap.put("Chassis Model", new Integer(14));
		fieldMap.put("Chassis Residual", new Integer(15));
		fieldMap.put("Chassis Year", new Integer(16));
		fieldMap.put("Contract-Type", new Integer(17));
		fieldMap.put("Decommission Date", new Integer(18));
		fieldMap.put("Domicile", new Integer(19));
		fieldMap.put("Exp - HUT", new Integer(20));
		fieldMap.put("Exp - IFTA", new Integer(21));
		fieldMap.put("Exp - Inspect", new Integer(22));
		fieldMap.put("Exp - Reg", new Integer(23));
		fieldMap.put("GVW", new Integer(24));
		fieldMap.put("Height", new Integer(25));
		fieldMap.put("Lease(Current) End", new Integer(26));
		fieldMap.put("Lease(Current) Start", new Integer(27));
		fieldMap.put("License Plate (Current)", new Integer(28));
		fieldMap.put("License Plate State", new Integer(29));
		fieldMap.put("Mile Cost", new Integer(30));
		fieldMap.put("Monthly Lease Cost", new Integer(31));
		fieldMap.put("Monthly Mileage Included", new Integer(32));
		fieldMap.put("Owner (Body/Reefer)", new Integer(33));
		fieldMap.put("Owner (Chassis)", new Integer(34));
		fieldMap.put("Reefer Make", new Integer(35));
		fieldMap.put("Reefer Model", new Integer(36));
		fieldMap.put("Reefer Rate", new Integer(37));
		fieldMap.put("Reefer Type", new Integer(38));
		fieldMap.put("Reefer Unit#", new Integer(39));
		fieldMap.put("Reefer Year", new Integer(40));
		fieldMap.put("Schedule A (Body)", new Integer(41));
		fieldMap.put("Schedule A (Chassis)", new Integer(42));
		fieldMap.put("Schedule A (Reefer)", new Integer(43));
		fieldMap.put("Shelving", new Integer(44));
		fieldMap.put("Vendor - Reefer", new Integer(45));
		fieldMap.put("Vendor - Truck", new Integer(46));
		fieldMap.put("Vendor Number#", new Integer(47));
		fieldMap.put("VIN #", new Integer(48));
	}
	
	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    protected void makeObjects(String[] recordLine) throws BadDataException {
		
		record = new Asset();
		try {			
			record.setCurrentAssetNo(trim(recordLine[0]));
			record.setAssetType(new AssetType(trim(recordLine[1]).toUpperCase(), null));
			record.setAssetNo(trim(recordLine[2]));
			record.setAssetDescription(trim(recordLine[3]));
			record.setBarcode(trim(recordLine[4]));			
			record.setAssetStatus(EnumAssetStatus.getEnumByDesc(trim(recordLine[5])));			
			
			for(Map.Entry<String, Integer> fieldEntry : fieldMap.entrySet()) {
				AssetAttribute assetAttribute = new AssetAttribute(new AssetAttributeId(
						null, fieldEntry.getKey()),null);
				assetAttribute.setAttributeValue(isEmpty(recordLine[fieldEntry.getValue().intValue()]) ? "UNKNOWN" : trim(recordLine[fieldEntry.getValue().intValue()]));
				assetAttribute.setAttributeMatch("U");
				record.getAssetAttributes().add(assetAttribute);
			}
			
		} catch(Exception e){
		
		}
		record.setSource(recordLine);		
		getClient().accept(record);
	}
	
	private String trimToLength(String str) {
		return (str != null && str.length()>10 ? str.substring(str.length()-10) : str);
	}

	private String removeLeadingZeros(String str) {
		return str != null ? str.replaceFirst("^0+(?!$)", "") : null;
	}

	private static Date parseDate(String dateStr) {
		if (!isEmpty(dateStr)) {
			try {
				return MONTH_DATE_YEAR_FORMATER.parse(dateStr);
			} catch (ParseException px) {
				try {
					return YEAR_MONTH_DATE_FORMATER.parse(dateStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static boolean isEmpty(String str) {
		return str == null || (str != null && str.length() == 0)
				|| "null".equalsIgnoreCase(str);
	}

	private static String trim(String str) {
		return str != null ? str.trim() : null;
	}
	
	private static double parseDouble(String str) {
		return str != null && !isEmpty(str) ? Double.parseDouble(str
				.replaceAll("[^\\d-]+", "")) : 0.0;
	}

	private static int parseInt(String str) {
		return str != null && !isEmpty(str) ? Integer.parseInt(str.replaceAll(
				"[^\\d-]+", "")) : 0;
	}
 }
