package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.NutritionDrugPanel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

// Refactored to NOT extend AbstractCache, because that cannot handle empty values,
// and so deleting a drug panel is not possible.
// This implementation simply reloads all entries periodically.
// Will be replaced with a final solution when all the new nutrition panel styles will be implemented

public class FDDrugCache {
	
	private static Category LOGGER = LoggerFactory.getInstance( FDDrugCache.class );
	private static FDDrugCache instance;

	private final ServiceLocator serviceLocator;
	
	private final Thread refresher;

	private volatile Map<String,NutritionDrugPanel> cache = new ConcurrentHashMap<String, NutritionDrugPanel>();

	private FDDrugCache() {
		try {
			this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
		} catch ( NamingException e ) {
			throw new IllegalStateException( "FDDrugCache initialization failed wih fatal error: There is no JNDI context!", e );
		}
		this.refresher = new RefreshThread( this.getClass().getName(), DateUtil.MINUTE * FDStoreProperties.getNutritionRefreshPeriod() );
		refresher.start();
	}
	
	public synchronized static FDDrugCache getInstance() {
		if ( instance == null ) {
			instance = new FDDrugCache();
		}
		return instance;
	}
	
	public void refresh() {
		Map<String, NutritionDrugPanel> data = null;
		try {
			LOGGER.info( "REFRESHING" );
			ErpNutritionSB sb = this.lookupNutritionHome().create();
			data = sb.loadDrugPanels( new Date(0L) );			
			LOGGER.info( "REFRESHED: " + data.size() );
		} catch ( RemoteException e ) {
			throw new FDRuntimeException( e );
		} catch ( CreateException e ) {
			throw new FDRuntimeException( e );
		}
		
		Map<String,NutritionDrugPanel> newCache = new ConcurrentHashMap<String, NutritionDrugPanel>();
		newCache.putAll( data );
		
		cache = newCache;
	}

	public NutritionDrugPanel getDrugPanel(String skuCode) {		
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
