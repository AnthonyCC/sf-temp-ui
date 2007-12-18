package com.freshdirect.webapp.taglib.dlv;

import java.text.SimpleDateFormat;

import com.freshdirect.delivery.DlvTemplateManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class DeliveryRegionTag extends AbstractGetterTag {
	
	private String region = null;
	private String startDate = null;
		
	public String getRegion(){
		return this.region;
	}
	public void setRegion(String region){
		this.region = region;
	}
	
	public String getStartdate(){
		return this.startDate;
	}
	public void setStartdate(String startDate){
		this.startDate = startDate;
	}
	
	protected Object getResult()  throws Exception {
		DlvRegionModel regionModel = null;
		
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
		
		if(this.startDate == null || "".equals(startDate)){
			regionModel = DlvTemplateManager.getInstance().getRegion(region);
		}else{
			regionModel = DlvTemplateManager.getInstance().getRegion(region, sf.parse(startDate));
		}
		
		return regionModel;	
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.delivery.model.DlvRegionModel";
		}

	}


}