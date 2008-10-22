package com.freshdirect.transadmin.web.model;

public class MultiFileUploadCommand {
	
	private byte[] file1;
	
	private byte[] file2;
	
	private byte[] file3;
	
	private String outputFile1;
	
	private String outputFile2;
	
	private String referenceData;
	
	private String cutOff;
	
	
	public String getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}

	public byte[] getFile1() {
		return file1;
	}

	public void setFile1(byte[] file1) {
		this.file1 = file1;
	}

	public byte[] getFile2() {
		return file2;
	}

	public void setFile2(byte[] file2) {
		this.file2 = file2;
	}

	public byte[] getFile3() {
		return file3;
	}

	public void setFile3(byte[] file3) {
		this.file3 = file3;
	}

	public String getOutputFile1() {
		return outputFile1;
	}

	public void setOutputFile1(String outputFile1) {
		this.outputFile1 = outputFile1;
	}

	public String getOutputFile2() {
		return outputFile2;
	}

	public void setOutputFile2(String outputFile2) {
		this.outputFile2 = outputFile2;
	}

	public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	
}
