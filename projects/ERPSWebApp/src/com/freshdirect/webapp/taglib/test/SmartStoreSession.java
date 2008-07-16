package com.freshdirect.webapp.taglib.test;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;

public class SmartStoreSession implements javax.servlet.http.HttpSession {
	
	private HttpSession session;
	private FDUser looser;

	public SmartStoreSession(String erpPk) throws FDResourceException, FDAuthenticationException {
		this(erpPk, null);
	}

	public SmartStoreSession(String erpPk, javax.servlet.http.HttpSession session) throws FDResourceException, FDAuthenticationException {
		FDIdentity identity = new FDIdentity(erpPk);
		
		this.session = session;
		looser = FDCustomerManager.recognize(identity);
		looser.setIdentity(identity);
	}

	public Object getAttribute(String arg0) {
		if (arg0.equals("fd.user")) return looser;
		else if (session != null)
			return session.getAttribute(arg0);
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getAttributeNames();
		return null;
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getCreationTime();
		return 0;
	}

	public String getId() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getId();
		return null;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getLastAccessedTime();
		return 0;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getMaxInactiveInterval();
		return 0;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getServletContext();
		return null;
	}

	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getSessionContext();
		return null;
	}

	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getValue(arg0);
		return null;
	}

	public String[] getValueNames() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.getValueNames();
		return null;
	}

	public void invalidate() {
		// TODO Auto-generated method stub
		if (session != null)
			invalidate();
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		if (session != null)
			return session.isNew();
		return false;
	}

	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (session != null)
			session.putValue(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		if (session != null)
			session.removeAttribute(arg0);
	}

	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		if (session != null)
			session.removeValue(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (session != null)
			session.setAttribute(arg0, arg1);
	}

	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		if (session != null)
			session.setMaxInactiveInterval(arg0);
	}
	
}
