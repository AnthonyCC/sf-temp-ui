package com.freshdirect.mktAdmin.model;

import java.io.Serializable; 
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class RestrictionListAppendBean implements Serializable{

	private String customerIds=null;
	private String promotionCode=null;
	
	public RestrictionListAppendBean(){}
	
	public String getCustomerIds() {
		return customerIds;
	}
	public void setCustomerIds(String customerIds) {
		this.customerIds = customerIds;
	}
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	
	public String[] getDecodedCustomerIds(){
		return decodeStrings(this.customerIds);
	}
	
	public static String[] decodeStrings(String string) {
		StringTokenizer st = new StringTokenizer(string, ",");
		String[] strings = new String[st.countTokens()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = st.nextToken().trim();
		}
		return strings;
	}

	public String encodeString(String strArray[]) {
		String str = "";
		if (strArray != null && strArray.length > 0) {
			for (int i=0; i < strArray.length; i++) {
				if (!"".equals(str)) {
					str += ",";
				}
				str += strArray[i].trim();
			}
		}
		return (!"".equals(str)) ? str : null;
	}

	public String encodeString(List list) {
		String str = "";
		
		if (list != null && list.size() > 0) {
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				if (!"".equals(str)) {
					str += ",";
				}
				str += ((String)iter.next()).trim();
			}
		}
		return (!"".equals(str)) ? str : null;
	}

	
	
}
