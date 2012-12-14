package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

// Refactored to NOT extend AbstractCache, because that cannot handle empty values,
// and so deleting a drug panel is not possible.
// This implementation simply reloads all entries periodically.
// Will be replaced with a final solution when all the new nutrition panel styles will be implemented

public class FDNutritionPanelCache {
	
	private static Category LOGGER = LoggerFactory.getInstance( FDNutritionPanelCache.class );
	private static FDNutritionPanelCache instance;

	private final ServiceLocator serviceLocator;
	
	private final Thread refresher;

	private volatile Map<String,NutritionPanel> cache = new ConcurrentHashMap<String, NutritionPanel>();

	private FDNutritionPanelCache() {
		try {
			this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
		} catch ( NamingException e ) {
			throw new IllegalStateException( "FDNutritionPanelCache initialization failed wih fatal error: There is no JNDI context!", e );
		}
		this.refresher = new RefreshThread( this.getClass().getName(), DateUtil.MINUTE * FDStoreProperties.getNutritionRefreshPeriod() );
		refresher.start();
		
		// manual first time refresh
		refresh();
	}
	
	public synchronized static FDNutritionPanelCache getInstance() {
		if ( instance == null ) {
			instance = new FDNutritionPanelCache();
		}
		return instance;
	}
	
	public void refresh() {
		Map<String, NutritionPanel> data = null;
		try {
			LOGGER.info( "REFRESHING" );
			ErpNutritionSB sb = this.lookupNutritionHome().create();
			data = sb.loadNutritionPanels( new Date(0L) );			
			LOGGER.info( "REFRESHED: " + data.size() );
		} catch ( RemoteException e ) {
			throw new FDRuntimeException( e );
		} catch ( CreateException e ) {
			throw new FDRuntimeException( e );
		}
		
		Map<String,NutritionPanel> newCache = new ConcurrentHashMap<String, NutritionPanel>();
		newCache.putAll( data );
		
		cache = newCache;
	}

	public NutritionPanel getNutritionPanel(String skuCode) {		
		return cache.get( skuCode );
	}
	
	private ErpNutritionHome lookupNutritionHome() {
		try {
			return (ErpNutritionHome) serviceLocator.getRemoteHome("freshdirect.content.Nutrition");
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

	private final class RefreshThread extends Thread {

		private final long refreshDelay;
		
		public RefreshThread( String name, long refreshDelay ) {
			super( name );
			this.refreshDelay = refreshDelay;
		}

		@Override
		public void run() {
			while ( true ) {
				try {
					Thread.sleep( refreshDelay );
				} catch ( InterruptedException ex ) {
					// do nothing
				}
				refresh();
			}
		}
	}

}
