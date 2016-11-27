package com.freshdirect.dataloader.payment;


public class FileContext implements java.io.Serializable {

	private String localHost;
	
	private String remoteHost;
	
	private PaymentFileType fileType;
	
	private String key;
	
	private String userName;
	
	private String password;
	
	private boolean downloadFiles;
	
	private String paypalFileDate;

	public boolean downloadFiles() {
		return downloadFiles;
	}
	
	public void setDownloadFiles(boolean downloadFiles) {
		this.downloadFiles= downloadFiles;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public PaymentFileType getFileType() {
		return fileType;
	}

	public void setFileType(PaymentFileType fileType) {
		this.fileType = fileType;
	}
	
	
	public void setOpenSSHPrivateKey(String key) {
		this.key=key;
	}
	
	public String getOpenSSHPrivateKey() {
		return key;
	}

	public void setPayPalFileDate(String paypalFileDate) {
		this.paypalFileDate = paypalFileDate;
	}
	
	public String getPayPalFileDate() {
		return paypalFileDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (downloadFiles ? 1231 : 1237);
		result = prime * result
				+ ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result
				+ ((localHost == null) ? 0 : localHost.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((remoteHost == null) ? 0 : remoteHost.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileContext other = (FileContext) obj;
		if (downloadFiles != other.downloadFiles)
			return false;
		if (fileType != other.fileType)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (localHost == null) {
			if (other.localHost != null)
				return false;
		} else if (!localHost.equals(other.localHost))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (remoteHost == null) {
			if (other.remoteHost != null)
				return false;
		} else if (!remoteHost.equals(other.remoteHost))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileContext [localHost=" + localHost + ", remoteHost="
				+ remoteHost + ", fileType=" + fileType + ", key=" + key
				+ ", userName=" + userName + ", password=" + password
				+ ", downloadFiles=" + downloadFiles + "]";
	}

	
	
	
}
