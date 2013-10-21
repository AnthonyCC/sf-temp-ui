package com.freshdirect.transadmin.web.model;

import org.springframework.web.multipart.MultipartFile;

public class ScheduleFileUploadCommand extends BaseCommand {
	
	
	private MultipartFile file;
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	private String processType;
	
	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	
}
