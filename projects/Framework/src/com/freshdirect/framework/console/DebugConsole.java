package com.freshdirect.framework.console;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.management.ObjectName;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import bsh.NameSpace;

import com.freshdirect.framework.util.JMXUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class DebugConsole extends ApplicationLifecycleListener implements DebugConsoleMBean {


	private static final Category LOGGER = LoggerFactory.getInstance(DebugConsole.class);

	private boolean isStarted;
	private DebugConsoleSessiond sessiond;
	ConcurrentHashMap users;
	
	ObjectName objName;
	
	public DebugConsole() {
		super();
		users = new ConcurrentHashMap();
	}

	public void postStart(ApplicationLifecycleEvent evt) {
		synchronized (this) {
			if (objName == null) {
				objName = JMXUtil.registerMBean(this, "DebugConsole", "BeanShell");
				if (objName == null) {
					LOGGER.warn("Unable to register DebugConsole MBean");
					return ;
				}
				LOGGER.info("Registered DebugConsole MBean");
			}
		}
	}

	public boolean isConsoleStarted() {
		return isStarted;
	}

	public void startConsole(int port) {
		try {
			sessiond = new DebugConsoleSessiond(this, NameSpace.JAVACODE, port);
			sessiond.start();
			isStarted = true;
		} catch (IOException e) {
			LOGGER.warn("Unable to start DebugConsole",e);
		}
	}

	public void stopConsole() {
		sessiond.exitSession();
		try {
			sessiond.join();
		} catch (InterruptedException e) {
		}
		isStarted = false;
	}

	public void defineUser(String username, String password) {
		if (username == null || password == null || username.trim().equals("") || password.trim().equals("")) {
			LOGGER.warn("Empty username or password not allowed");
			return; 
		}
		if (password.length() < 5) {
			LOGGER.warn("Password should be at least 5 characters long");
		}
		users.put(username, password);
		
	}

	public String[] getUsers() {
		Set keys = new HashSet(users.keySet());
		return (String[]) keys.toArray(new String[keys.size()]);
	}

	public String getPassword(String login) {
		return (String) users.get(login);
	}

}
