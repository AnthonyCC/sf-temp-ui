package com.freshdirect.transadmin.web.model;

public class FileUploadCommand extends BaseCommand {
	
	private byte[] file;
	
	private String outputFile1;
	
	private String outputFile2;
	
	private String outputFile3;
	
	private String processType;
	
	private String serviceTimeScenario;
	
	private String cutOff;

    public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	public String getServiceTimeScenario() {
		return serviceTimeScenario;
	}

	public void setServiceTimeScenario(String serviceTimeScenario) {
		this.serviceTimeScenario = serviceTimeScenario;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
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

	public String getOutputFile3() {
		return outputFile3;
	}

	public void setOutputFile3(String outputFile3) {
		this.outputFile3 = outputFile3;
	}
}
