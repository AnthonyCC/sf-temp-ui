
package com.freshdirect.fdstore.sitemap;


import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;

public interface SitemapSB extends EJBObject{
	/**
	 *@deprecated Please use the SitemapController and SitemapServiceI in Storefront2.0 project.
	 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
	 *
	 *
	 */
	@Deprecated
	 public void generateSitemap() throws FDResourceException, RemoteException;

}
