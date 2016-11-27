package com.freshdirect.framework.util;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class RuntimeServiceUtil {

	private static final Logger LOGGER = LoggerFactory.getInstance(RuntimeServiceUtil.class);
	private static RuntimeServiceUtil instance = new RuntimeServiceUtil();

	public static RuntimeServiceUtil getInstance() {
		return instance;
	}

	private String rootDirectory;

	public RuntimeServiceUtil() {

		try {
			InitialContext ctx = new InitialContext();
			MBeanServer connection = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

			if (connection != null) {
				ObjectName rts = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
				ObjectName dc = (ObjectName) connection.getAttribute(rts,"DomainConfiguration");
				rootDirectory = (String) connection.getAttribute(dc,"RootDirectory");
			}
		} catch (Exception e) {
			LOGGER.error("RuntimeServiceUtil initialization error", e);
		}
	}

	public String getRootDirectory() {
		return rootDirectory;
	}
}