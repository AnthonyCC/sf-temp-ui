package com.freshdirect.fdstore.iplocator;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IpLocatorResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2865722762559277682L;
	
	@JsonProperty("Version")
	private String version;
	
	@JsonProperty("TransmissionReference")
	private String transmissionReference;
	
	@JsonProperty("TransmissionResults")
	private String transmissionResults;
	
	@JsonProperty("Records")
	private List<IpLocatorRecord> records;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTransmissionReference() {
		return transmissionReference;
	}

	public void setTransmissionReference(String transmissionReference) {
		this.transmissionReference = transmissionReference;
	}

	public String getTransmissionResults() {
		return transmissionResults;
	}

	public void setTransmissionResults(String transmissionResults) {
		this.transmissionResults = transmissionResults;
	}

	public List<IpLocatorRecord> getRecords() {
		return records;
	}

	public void setRecords(List<IpLocatorRecord> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "IpLocatorResponse [version=" + version
				+ ", transmissionReference=" + transmissionReference
				+ ", transmissionResults=" + transmissionResults + ", records="
				+ records + "]";
	}
	
	
}

