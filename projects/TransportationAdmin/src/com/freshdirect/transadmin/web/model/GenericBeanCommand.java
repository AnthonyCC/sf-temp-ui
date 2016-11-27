package com.freshdirect.transadmin.web.model;

public class GenericBeanCommand extends BaseCommand {
	
	private byte[] file;
	
	private String fileType;
	
	private String fileHtml;

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileHtml() {
		return fileHtml;
	}

	public void setFileHtml(String fileHtml) {
		this.fileHtml = fileHtml;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
