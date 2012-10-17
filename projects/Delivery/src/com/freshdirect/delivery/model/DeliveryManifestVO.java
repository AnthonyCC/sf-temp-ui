package com.freshdirect.delivery.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.freshdirect.routing.model.IBuildingOperationDetails;
import com.freshdirect.routing.model.IDeliveryModel;

public class DeliveryManifestVO implements Serializable{
	
	public static DateFormat dateFormatwithTime = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
	public static DateFormat serverTimeFormat = new SimpleDateFormat("hh:mm aaa");
	
	private IDeliveryModel deliveryInfo;

	private String firstName;
	private String lastName;
	private int cartonCnt;

	private String deliveryInstructions;

	private int stopNo;
	private Date stopArrivalTime;
	private Date stopDepartureTime;
	
	private String lastAirclicMsg;

	public DeliveryManifestVO() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstName() {
		return firstName;
	}

	public IDeliveryModel getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(IDeliveryModel deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getCartonCnt() {
		return cartonCnt;
	}

	public void setCartonCnt(int cartonCnt) {
		this.cartonCnt = cartonCnt;
	}

	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}

	public void setDeliveryInstructions(String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}

	public Date getStopArrivalTime() {
		return stopArrivalTime;
	}

	public void setStopArrivalTime(Date stopArrivalTime) {
		this.stopArrivalTime = stopArrivalTime;
	}

	public Date getStopDepartureTime() {
		return stopDepartureTime;
	}

	public void setStopDepartureTime(Date stopDepartureTime) {
		this.stopDepartureTime = stopDepartureTime;
	}

	public int getStopNo() {
		return stopNo;
	}

	public void setStopNo(int stopNo) {
		this.stopNo = stopNo;
	}

	public String getLastAirclicMsg() {
		return lastAirclicMsg;
	}

	public void setLastAirclicMsg(String lastAirclicMsg) {
		this.lastAirclicMsg = lastAirclicMsg;
	}

	public String getWindowTime() {
		if (this.getDeliveryInfo() != null) {
			Calendar baseDate = Calendar.getInstance();	
			baseDate.setTime(this.getDeliveryInfo().getDeliveryEndTime());
			baseDate.add(Calendar.MINUTE, -1);
			
			return serverTimeFormat.format(this.getDeliveryInfo().getDeliveryStartTime())+ " - "+ serverTimeFormat.format(baseDate.getTime());
		}
		return "";
	}

	public String getAddress() {
		StringBuffer buf = new StringBuffer();
		if (this.getDeliveryInfo() != null) {
			buf.append(this.getDeliveryInfo().getDeliveryLocation()
					.getBuilding().getSrubbedStreet() + " ");
			if (this.getDeliveryInfo().getDeliveryLocation()
					.getApartmentNumber() != null) {
				buf.append(" Apt: "
						+ this.getDeliveryInfo().getDeliveryLocation()
								.getApartmentNumber() + " ");
			}
			
			buf.append(this.getDeliveryInfo().getDeliveryLocation()
					.getBuilding().getCity()
					+ ", "
					+ this.getDeliveryInfo().getDeliveryLocation()
							.getBuilding().getState()
					+ ", "
					+ this.getDeliveryInfo().getDeliveryLocation()
							.getBuilding().getCountry()
					+ ", "
					+ this.getDeliveryInfo().getDeliveryLocation()
							.getBuilding().getZipCode());
		}

		return buf.toString();
	}

	public String getServiceAddress() {
		StringBuffer buf = new StringBuffer();
		if (this.getDeliveryInfo() != null) {
			buf.append(this.getDeliveryInfo().getDeliveryLocation()
					.getBuilding().getSvcScrubbedStreet());
		
			buf.append(this.getDeliveryInfo().getDeliveryLocation()
					.getBuilding().getSvcCity()
					+ ", "
					+ this.getDeliveryInfo().getDeliveryLocation()
							.getBuilding().getSvcState()
					+ ", "
					+ this.getDeliveryInfo().getDeliveryLocation()
							.getBuilding().getCountry()
					+ ", "
					+ this.getDeliveryInfo().getDeliveryLocation()
							.getBuilding().getSvcZip());
		}

		return buf.toString();
	}

	public String getBuildingType() {

		if (this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null) {

			if (this.getDeliveryInfo().getDeliveryLocation().getBuilding().isHouse()) {
				return "House";
			}

			if (this.getDeliveryInfo().getDeliveryLocation().getBuilding().isSvcEnt()) {
				return "Service Entrance";
			}

			if (this.getDeliveryInfo().getDeliveryLocation().getBuilding().isWalkup()) {
				return "Walkup";
			}

			if (this.getDeliveryInfo().getDeliveryLocation().getBuilding().isDoorman()) {
				return "Doorman";
			}

			if (this.getDeliveryInfo().getDeliveryLocation().getBuilding().isElevator()) {
				return "Elevator";
			}

			if (this.getDeliveryInfo().getDeliveryLocation().getBuilding().isFreightElevator()) {
				return "Freight Elevator";
			}
		}
		return null;
	}

	public String getServiceHours() {
		if (this.getServiceEntranceOpenTime() != null && this.getServiceEntranceCloseTime() != null) {
			return serverTimeFormat.format(this.getServiceEntranceOpenTime()) + " - " + serverTimeFormat.format(this.getServiceEntranceCloseTime());
		}
		return "";
	}
	
	public Date getServiceEntranceCloseTime() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails() != null) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails().size() > 0) {
				IBuildingOperationDetails detail = this.getDeliveryInfo().getDeliveryLocation()
														.getBuilding().getOperationDetails().iterator().next();
				return detail.getServiceEndHour();
			}
		}
		return null;
	}

	public Date getServiceEntranceOpenTime() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null 
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails() != null) {
			if(this.getDeliveryInfo().getDeliveryLocation().getBuilding().getOperationDetails().size() > 0) {
				IBuildingOperationDetails detail = this.getDeliveryInfo().getDeliveryLocation()
														.getBuilding().getOperationDetails().iterator().next();
				return detail.getServiceStartHour();
			}
		}
		return null;
	}
	
	public String getCrossStreet() {
		if(this.getDeliveryInfo() != null
				&& this.getDeliveryInfo().getDeliveryLocation() != null
				 	&& this.getDeliveryInfo().getDeliveryLocation().getBuilding() != null ) {
			return this.getDeliveryInfo().getDeliveryLocation().getBuilding().getCrossStreet();
		}
		return null;
	}

}
