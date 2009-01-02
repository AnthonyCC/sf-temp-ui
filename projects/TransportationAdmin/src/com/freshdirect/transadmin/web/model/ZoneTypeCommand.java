package com.freshdirect.transadmin.web.model;

public class ZoneTypeCommand extends BaseCommand {

	private String zoneTypeId;
	
	private String name;
	
	private String description;
	
	private Integer driverMax;
	
	private Integer driverReq;
	
	private Integer helperMax;
	
	private Integer helperReq;
	
	private Integer runnerMax;
	
	private Integer runnerReq;
	
	private String driverCode;
	
	private String helperCode;
	
	private String runnerCode;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDriverMax() {
		return driverMax;
	}

	public void setDriverMax(Integer driverMax) {
		this.driverMax = driverMax;
	}

	public Integer getDriverReq() {
		return driverReq;
	}

	public void setDriverReq(Integer driverReq) {
		this.driverReq = driverReq;
	}

	public Integer getHelperMax() {
		return helperMax;
	}

	public void setHelperMax(Integer helperMax) {
		this.helperMax = helperMax;
	}

	public Integer getHelperReq() {
		return helperReq;
	}

	public void setHelperReq(Integer helperReq) {
		this.helperReq = helperReq;
	}

	public Integer getRunnerMax() {
		return runnerMax;
	}

	public void setRunnerMax(Integer runnerMax) {
		this.runnerMax = runnerMax;
	}

	public Integer getRunnerReq() {
		return runnerReq;
	}

	public void setRunnerReq(Integer runnerReq) {
		this.runnerReq = runnerReq;
	}

	public String getZoneTypeId() {
		return zoneTypeId;
	}

	public void setZoneTypeId(String zoneTypeId) {
		this.zoneTypeId = zoneTypeId;
	}

	public String getDriverCode() {
		return driverCode;
	}

	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}

	public String getHelperCode() {
		return helperCode;
	}

	public void setHelperCode(String helperCode) {
		this.helperCode = helperCode;
	}

	public String getRunnerCode() {
		return runnerCode;
	}

	public void setRunnerCode(String runnerCode) {
		this.runnerCode = runnerCode;
	}
	
	public Tooltip getNameEx() {
		return new Tooltip(this.getName(), "Test tool tip");
	}
	
	class Tooltip implements IToolTip {
		
		Object value = null;
		String toolTip = null;
		
		Tooltip(Object value, String tooltip) {
			this.value = value;
			this.toolTip = tooltip;
		}

		

		public Object getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}



		public String getToolTip() {
			return toolTip;
		}



		public void setToolTip(String toolTip) {
			this.toolTip = toolTip;
		}
		
		public String toString() {
			return getValue().toString();
		}
	}
		
}


