package com.freshdirect.athena.connection;

import java.util.HashMap;
import java.util.Properties;

import com.freshdirect.athena.api.ICall;
import com.freshdirect.athena.api.JCOCall;
import com.freshdirect.athena.config.Datasource;
import com.freshdirect.athena.exception.PoolException;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

public class JCOPool extends BasePool {

	JCoRepository repository = null;
	
	CustomDestinationDataProvider jcoProvider = null;
	
	public JCOPool(Datasource dataSource)
	{
		super(dataSource);
	}
	
	@Override
	public void init() throws PoolException
	{
		Properties defaults = new Properties();
		defaults.put(DestinationDataProvider.JCO_POOL_CAPACITY, "10");
		defaults.put("sap.jco.dump", "critical");
		
		defaults.put("jco.client.trace", "false");
		defaults.put("jco.client.client", this.getDataSource().getClient());
		
		defaults.put("jco.client.user", this.getDataSource().getUser());
		defaults.put("jco.client.passwd", this.getDataSource().getPassword());
		
		defaults.put("jco.client.sysnr", this.getDataSource().getSystemno() );
		defaults.put("jco.client.ashost", this.getDataSource().getUrl());
		
		jcoProvider = new CustomDestinationDataProvider();

		// Register the provider with the JCo environment
		try
		{
			if (!Environment.isDestinationDataProviderRegistered())
			{
				com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(jcoProvider);
			}
		}
		catch (final IllegalStateException providerAlreadyRegisteredException)
		{
			throw new Error(providerAlreadyRegisteredException);
		}

		jcoProvider.changeProperties(this.getDataSource().getName(), defaults);
		
		try {
			if (JCoDestinationManager.getDestination(this.getDataSource().getName()) != null)
			{
				this.repository = JCoDestinationManager.getDestination(this.getDataSource().getName()).getRepository();
			}
		} catch (JCoException e) {
			throw new PoolException(e);
		}
	}

	@Override
	public void complete() throws PoolException
	{
		if (Environment.isDestinationDataProviderRegistered())
		{
			com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(jcoProvider);
		}
	}


	@Override
	public ICall getCall() throws PoolException {
		// TODO Auto-generated method stub
		return new JCOCall(repository);
	}
	
	static class CustomDestinationDataProvider implements DestinationDataProvider
	{
		private DestinationDataEventListener eL;
		private final HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();

		public Properties getDestinationProperties(final String destinationName)
		{
			try
			{
				//read the destination from DB
				final Properties p = secureDBStorage.get(destinationName);

				if (p != null)
				{
					if (p.isEmpty())
					{
						throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION,
								"destination configuration is incorrect", null);
					}

					return p;
				}

				return null;
			}
			catch (final RuntimeException re)
			{
				throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
			}
		}

		/*
		 * An implementation supporting events, to retain the eventListener instance provided by the JCo runtime. This
		 * listener instance shall be used to notify the JCo runtime about all changes in destination configurations.
		 */
		public void setDestinationDataEventListener(final DestinationDataEventListener eventListener)
		{
			this.eL = eventListener;
		}

		public boolean supportsEvents()
		{
			return true;
		}

		// implementation that saves the properties in a very secure way
		void changeProperties(final String destName, final Properties properties)
		{
			synchronized (secureDBStorage)
			{

				if (!Environment.isDestinationDataProviderRegistered())
				{
					Environment.registerDestinationDataProvider(this);
				}
				if (properties == null)
				{
					if (secureDBStorage.remove(destName) != null && eL != null)
					{
						eL.deleted(destName);
					}
				}
				else
				{
					secureDBStorage.put(destName, properties);
					if (eL != null)
					{
						eL.updated(destName);
					}
				}
			}
		}
	}

}
