package com.freshdirect.athena.config;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Cache implements Serializable {
	
	@Element(required=false)
	private int maxsize;
	
	@Element(required=false)
	private int frequency;

	public int getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(int maxsize) {
		this.maxsize = maxsize;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frequency;
		result = prime * result + maxsize;
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
		Cache other = (Cache) obj;
		if (frequency != other.frequency)
			return false;
		if (maxsize != other.maxsize)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[maxsize=" + maxsize + ", frequency=" + frequency + "]";
	}
	
	
}
