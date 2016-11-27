package com.freshdirect.transadmin.web.model;


public class RSFileMergeCommand extends BaseCommand {
	
	private byte[] truckFile1;
	
	private byte[] orderFile1;
	
	private byte[] truckFile2;
	
	private byte[] orderFile2;
	
	private byte[] truckFile3;
	
	private byte[] orderFile3;
	
	private String orderOutputFilePath;
	
	private String truckOutputFilePath;

	public byte[] getOrderFile1() {
		return orderFile1;
	}

	public void setOrderFile1(byte[] orderFile1) {
		this.orderFile1 = orderFile1;
	}

	public byte[] getOrderFile2() {
		return orderFile2;
	}

	public void setOrderFile2(byte[] orderFile2) {
		this.orderFile2 = orderFile2;
	}

	public byte[] getOrderFile3() {
		return orderFile3;
	}

	public void setOrderFile3(byte[] orderFile3) {
		this.orderFile3 = orderFile3;
	}

	public byte[] getTruckFile1() {
		return truckFile1;
	}

	public void setTruckFile1(byte[] truckFile1) {
		this.truckFile1 = truckFile1;
	}

	public byte[] getTruckFile2() {
		return truckFile2;
	}

	public void setTruckFile2(byte[] truckFile2) {
		this.truckFile2 = truckFile2;
	}

	public byte[] getTruckFile3() {
		return truckFile3;
	}

	public void setTruckFile3(byte[] truckFile3) {
		this.truckFile3 = truckFile3;
	}

	public String getOrderOutputFilePath() {
		return orderOutputFilePath;
	}

	public void setOrderOutputFilePath(String orderOutputFilePath) {
		this.orderOutputFilePath = orderOutputFilePath;
	}

	public String getTruckOutputFilePath() {
		return truckOutputFilePath;
	}

	public void setTruckOutputFilePath(String truckOutputFilePath) {
		this.truckOutputFilePath = truckOutputFilePath;
	}

}
