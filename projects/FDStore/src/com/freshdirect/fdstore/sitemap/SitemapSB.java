
package com.freshdirect.fdstore.sitemap;


import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;

public interface SitemapSB extends EJBObject{

	 public void generateSitemap() throws FDResourceException, RemoteException;

}
