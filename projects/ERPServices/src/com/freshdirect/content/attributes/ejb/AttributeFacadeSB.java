/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttributeCollection;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface AttributeFacadeSB extends EJBObject {
	
	public Map loadAttributes(Date lastModified) throws AttributeException, RemoteException;

	public FlatAttributeCollection getAttributes(String[] rootIds) throws AttributeException, RemoteException;
	
	public void storeAttributes(FlatAttributeCollection attributes, String user, String sapId) throws AttributeException, RemoteException;

}

