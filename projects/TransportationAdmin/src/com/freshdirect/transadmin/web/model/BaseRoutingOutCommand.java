package com.freshdirect.transadmin.web.model;

public class BaseRoutingOutCommand  extends BaseCommand {
		
	private byte[] truckRoutingFile;
	
	private byte[] depotRoutingFile;
	
	private byte[] depotTruckScheduleFile;
	
	private String routingZones;
	
	private String routingDepotZones;
	
	private String cutOff;
	
	private String force;
	
	private String orderOutputFilePath;
	
	private String truckOutputFilePath;
	
	private String cutoffReportFilePath;
	
	private String truckRoutingSessionDesc;
	
	private String depotRoutingSessionDesc;

	public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	public String getCutoffReportFilePath() {
		return cutoffReportFilePath;
	}

	public void setCutoffReportFilePath(String cutoffReportFilePath) {
		this.cutoffReportFilePath = cutoffReportFilePath;
	}

	public byte[] getDepotRoutingFile() {
		return depotRoutingFile;
	}

	public void setDepotRoutingFile(byte[] depotRoutingFile) {
		this.depotRoutingFile = depotRoutingFile;
	}

	public String getForce() {
		return force;
	}

	public void setForce(String force) {
		this.force = force;
	}

	public String getOrderOutputFilePath() {
		return orderOutputFilePath;
	}

	public void setOrderOutputFilePath(String orderOutputFilePath) {
		this.orderOutputFilePath = orderOutputFilePath;
	}

	public String getRoutingDepotZones() {
		return routingDepotZones;
	}

	public void setRoutingDepotZones(String routingDepotZones) {
		this.routingDepotZones = routingDepotZones;
	}

	public String getRoutingZones() {
		return routingZones;
	}

	public void setRoutingZones(String routingZones) {
		this.routingZones = routingZones;
	}

	public String getTruckOutputFilePath() {
		return truckOutputFilePath;
	}

	public void setTruckOutputFilePath(String truckOutputFilePath) {
		this.truckOutputFilePath = truckOutputFilePath;
	}

	public byte[] getDepotTruckScheduleFile() {
		return depotTruckScheduleFile;
	}

	public void setDepotTruckScheduleFile(byte[] depotTruckScheduleFile) {
		this.depotTruckScheduleFile = depotTruckScheduleFile;
	}

	public byte[] getTruckRoutingFile() {
		return truckRoutingFile;
	}

	public void setTruckRoutingFile(byte[] truckRoutingFile) {
		this.truckRoutingFile = truckRoutingFile;
	}

	public String getDepotRoutingSessionDesc() {
		return depotRoutingSessionDesc;
	}

	public void setDepotRoutingSessionDesc(String depotRoutingSessionDesc) {
		this.depotRoutingSessionDesc = depotRoutingSessionDesc;
	}

	public String getTruckRoutingSessionDesc() {
		return truckRoutingSessionDesc;
	}

	public void setTruckRoutingSessionDesc(String truckRoutingSessionDesc) {
		this.truckRoutingSessionDesc = truckRoutingSessionDesc;
	}

	
		
}
