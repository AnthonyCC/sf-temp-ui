package com.freshdirect.framework.monitor;

import java.util.Hashtable;

import javax.management.ObjectName;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class UriStats implements UriStatsMBean {

	private static final Category LOGGER = LoggerFactory.getInstance(UriStats.class);

	String uri;

	private int hitCount;
	private long maxExecTime;
	private long minExecTime;
	private double avgExecTime;

	private ObjectName objectName;
	
	public UriStats(String uri, ObjectName parentName)  {
		this.uri = uri; 
		Hashtable t = new Hashtable(parentName.getKeyPropertyList());
		t.put("URI", uri);
		try {
			objectName = new ObjectName(JMXUtil.FRESHDIRECT_DOMAIN, t);
		} catch (Exception e) {
			LOGGER.warn("Could not instantiate UriMonitor for URI: "+uri, e);
			throw new RuntimeException("Could not instantiate UriMonitor for URI: "+uri, e);
		}
	}
	
	public void clearStats() {
		synchronized (this) {
			hitCount = 0;
			maxExecTime = 0;
			minExecTime = 0;
			avgExecTime = 0;
		}
	}

	public double getAvgExecTime() {
		return avgExecTime;
	}

	public int getHitCount() {
		return hitCount;
	}

	public long getMaxExecTime() {
		return maxExecTime;
	}

	public long getMinExecTime() {
		return minExecTime;
	}

	public String getURI() {
		return uri;
	}

	public void updateStats(long startTime, long endTime) {
		long executionTime = endTime - startTime;
		synchronized (this) {
			int oldHitCount = hitCount;
			hitCount ++;  // Add to hitCount
			maxExecTime = Math.max(maxExecTime, executionTime);
			if (hitCount == 1) { 
				minExecTime = executionTime;
			} else {
				minExecTime = Math.min(minExecTime, executionTime);
			}
			avgExecTime = ((avgExecTime * oldHitCount) + executionTime) / (double) hitCount;
			LOGGER.debug(uri+": hit:"+hitCount+", max:"+maxExecTime + ", min:"+minExecTime + ", avg:"+avgExecTime + ", curr:"+executionTime);
		}		
	}

	public ObjectName getObjectName() {
		return objectName;
	}
	
}
