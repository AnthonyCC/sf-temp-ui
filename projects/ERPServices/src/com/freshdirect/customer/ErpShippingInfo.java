package com.freshdirect.customer;

import java.io.Serializable;

public class ErpShippingInfo implements Serializable {

	// JNG: 1/21/05 WAVE DETAIL INFO FROM SAP
	private String waveNumber;
	private String truckNumber;
	private String stopSequence;
	private final int regularCartons;
	private final int freezerCartons;
	private final int alcoholCartons;
	
	public ErpShippingInfo(
		String truckNumber,
		String stopSequence,
		int regularCartons,
		int freezerCartons,
		int alcoholCartons) {
		this.truckNumber = truckNumber;
		this.stopSequence = stopSequence;
		this.regularCartons = regularCartons;
		this.freezerCartons = freezerCartons;
		this.alcoholCartons = alcoholCartons;
		this.waveNumber = null;
	}

	public ErpShippingInfo(
		String waveNumber,
		String truckNumber,
		String stopSequence,
		int regularCartons,
		int freezerCartons,
		int alcoholCartons) {
		this.truckNumber = truckNumber;
		this.stopSequence = stopSequence;
		this.regularCartons = regularCartons;
		this.freezerCartons = freezerCartons;
		this.alcoholCartons = alcoholCartons;
		this.waveNumber = waveNumber;
		}

	public String getWaveNumber() {
		return this.waveNumber;
	}

	public String getTruckNumber() {
		return this.truckNumber;
	}

	public String getStopSequence() {
		return this.stopSequence;
	}

	public int getRegularCartons() {
		return this.regularCartons ;
	}

	public int getFreezerCartons() {
		return this.freezerCartons;
	}

	public int getAlcoholCartons() {
		return this.alcoholCartons;
	}

	public void setWaveNumber(String waveNumber) {
		this.waveNumber = waveNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	public void setStopSequence(String stopSequence) {
		this.stopSequence = stopSequence;
	}
	
	public String toString() {
		return "ErpShippingInfo[waveNumber: "
			+ this.waveNumber
			+ " truckNumber: "
			+ this.truckNumber
			+ " stopSequence: "
			+ this.stopSequence
			+ " regularCartons: "
			+ this.regularCartons
			+ " freezerCartons: "
			+ this.freezerCartons
			+ " alcoholCartons: "
			+ this.alcoholCartons;
	}

}
