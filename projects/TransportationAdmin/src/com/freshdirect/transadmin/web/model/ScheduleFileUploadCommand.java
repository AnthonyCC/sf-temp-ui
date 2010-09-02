package com.freshdirect.transadmin.web.model;

import java.io.File;

public class ScheduleFileUploadCommand extends BaseCommand {
	
	private byte[] file;
	
	//private File file;
	
	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	/*public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
*/
	private String processType;
	
	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	
}
