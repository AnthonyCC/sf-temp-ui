package com.freshdirect.athena.cache;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.freshdirect.athena.api.ICall;
import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.connection.BasePool;
import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.data.Variable;
import com.freshdirect.athena.exception.AsyncCacheException;
import com.freshdirect.athena.exception.AsyncCacheExceptionType;
import com.freshdirect.athena.util.AthenaProperties;

public class ApiCache  implements Serializable {
	
	private final Api api;
	
	private final GuavaAsyncCache<ApiKey, Data> cache;
		
	private static final Logger LOGGER = Logger.getLogger(ApiCache.class);

	public ApiCache(final Api api) {
		
		super();
		this.api = api;
		
		int frequency = api.getCache().getFrequency() != 0 ? api.getCache().getFrequency() * 1000 
																	: AthenaProperties.getDefaultCacheFrequency();
		LOGGER.debug("CACHE_"+api.getName() + " initialized with frequency "+frequency);
		
		cache = new GuavaAsyncCache<ApiKey, Data>(frequency , "CACHE_"+api.getName()) {

			protected Data loadData(ApiKey requestParam)  throws AsyncCacheException {
				
				Data result = new Data();
				try {
					
					BasePool pool = CacheManager.getInstance().getPool(api.getDatasource());
					ICall call = pool.getCall();
					if(call != null) {
						result = call.getData(api, requestParam.getParameters());
					} else {
						throw new AsyncCacheException(AsyncCacheExceptionType.LOAD_FAILED);
					}										
					
				} catch (Exception e) {	
					e.printStackTrace();
					throw new AsyncCacheException(e, AsyncCacheExceptionType.LOAD_FAILED);
				}
				return result;
			}
			
			protected Data loadDefault(ApiKey requestParam) {
								
				Data data = new Data();
				Variable variable = new Variable("nodata");
				data.addVariable(variable);
				return data;
			}
		};	
	}
	
	
	public Api getApi() {
		return api;
	}

	public GuavaAsyncCache<ApiKey, Data> getCache() {
		return cache;
	}
	
	public Data getData(ApiKey key) {
		return cache.get(key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((api == null) ? 0 : api.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiCache other = (ApiCache) obj;
		if (api == null) {
			if (other.api != null)
				return false;
		} else if (!api.equals(other.api))
			return false;
		return true;
	}

	

	
	
}
