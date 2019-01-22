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
	/**
	 *@deprecated Please use the AttributeFacadeController and AttributeFacadeServiceI in Storefront2.0 project.
	 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
	 */
	@Deprecated
	public Map loadAttributes(Date lastModified) throws AttributeException, RemoteException;
	
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public FlatAttributeCollection getAttributes(String[] rootIds) throws AttributeException, RemoteException;
	
	/**
	 *@deprecated This method is moved to backoffice project.
	 * SVN location :: https://appdevsvn.nj01/appdev/backoffice/trunk
	 */
	@Deprecated
	public void storeAttributes(FlatAttributeCollection attributes, String user, String sapId) throws AttributeException, RemoteException;

}

