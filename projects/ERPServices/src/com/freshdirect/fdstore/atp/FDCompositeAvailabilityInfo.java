package com.freshdirect.fdstore.atp;

import java.util.Map;


public class FDCompositeAvailabilityInfo extends FDAvailabilityInfo {

	private static final long	serialVersionUID	= -4275539848281241852L;
	
	private Map<String,FDAvailabilityInfo> componentInfo;

	/**
	 * @param available
	 * @param componentInfo Map of component key -> FDAvailabilityInfo
	 */
	public FDCompositeAvailabilityInfo(boolean available, Map<String,FDAvailabilityInfo> componentInfo) {
		super(available);
		this.componentInfo = componentInfo;
	}

	public Map<String,FDAvailabilityInfo> getComponentInfo() {
		return this.componentInfo;
	}
	
	/*public String getComponentDesc() {
		if(this.componentInfo.isEmpty()){
			return "";
		}else{
			String desc = "Some of the options you selected are not available: ";
			for(Iterator i = this.componentInfo.values().iterator(); i.hasNext(); ){
				String d = (String) i.next();
				if(desc.indexOf(d) < 0){
					desc += d +", ";
				}
			}
			desc = desc.substring(0, desc.lastIndexOf(","));
			return desc;
		}
	}*/
}
