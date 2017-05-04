package com.freshdirect.fdstore.sitemap;


import javax.ejb.*;
import java.rmi.RemoteException;

public interface SitemapHome extends EJBHome {

    public static final String JNDI_HOME = "freshdirect.fdstore.Sitemap";

	public SitemapSB create() throws CreateException, RemoteException;
}
