package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class AirclicCartonScanDetails implements Serializable,
		Comparator<AirclicCartonScanDetails> {

	private Date scanDate;
	private String action;
	private String cartonStatus;
	private String employee;
	private String route;
	private String stop;
	private String nextel;
	private String userId;
	private String deliveredTo;
	private String returnReason;
	private String motDriverName;

	public Date getScanDate() {
		return scanDate;
	}

	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCartonStatus() {
		return cartonStatus;
	}

	public void setCartonStatus(String cartonStatus) {
		this.cartonStatus = cartonStatus;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getNextel() {
		return nextel;
	}

	public void setNextel(String nextel) {
		this.nextel = nextel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeliveredTo() {
		return deliveredTo;
	}

	public void setDeliveredTo(String deliveredTo) {
		this.deliveredTo = deliveredTo;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getMotDriverName() {
		return motDriverName;
	}

	public void setMotDriverName(String motDriverName) {
		this.motDriverName = motDriverName;
	}

	public String getScanTime() {
		if (this.scanDate != null) {
			return new SimpleDateFormat("MM/dd/yyyy hh:mm aaa")
					.format(this.scanDate);
		}
		return "";
	}

	public AirclicCartonScanDetails(Date scanDate, String action,
			String cartonStatus, String employee, String route, String stop,
			String nextel, String userId, String deliveredTo,
			String returnReason, String motDriverName) {
		super();
		this.scanDate = scanDate;
		this.action = action;
		this.cartonStatus = cartonStatus;
		this.employee = employee;
		this.route = route;
		this.stop = stop;
		this.nextel = nextel;
		this.userId = userId;
		this.deliveredTo = deliveredTo;
		this.returnReason = returnReason;
		this.motDriverName = motDriverName;
	}

	@Override
	public String toString() {
		return "AirclicCartonScanDetails [scanDate=" + scanDate + ", action="
				+ action + ", cartonStatus=" + cartonStatus + ", employee="
				+ employee + ", route=" + route + ", stop=" + stop
				+ ", nextel=" + nextel + ", userId=" + userId + "]";
	}

	@Override
	public int compare(AirclicCartonScanDetails o1, AirclicCartonScanDetails o2) {
		// TODO Auto-generated method stub
		return ((AirclicCartonScanDetails) o1).getScanDate().compareTo(
				((AirclicCartonScanDetails) o2).getScanDate());
	}

}
