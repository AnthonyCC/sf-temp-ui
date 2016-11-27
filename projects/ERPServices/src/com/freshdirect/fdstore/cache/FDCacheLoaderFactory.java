package com.freshdirect.fdstore.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.constructs.CacheDecoratorFactory;
import net.sf.ehcache.loader.CacheLoader;
import net.sf.ehcache.loader.CacheLoaderFactory;

public class FDCacheLoaderFactory extends CacheLoaderFactory {
	
	public class CMSLoaderAdapter implements CacheLoader{
		
		@Override
		public CacheLoader clone(Ehcache arg0)
				throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void dispose() throws CacheException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Status getStatus() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void init() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object load(Object arg0) throws CacheException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object load(Object arg0, Object arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map loadAll(Collection arg0) {
			return null;
		}

		@Override
		public Map loadAll(Collection arg0, Object arg1) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	CacheLoader webPageCacheLoader = null;
	
	
	@Override
	public CacheLoader createCacheLoader(Ehcache cache, Properties properties) {
		
		if(cache.getName().equals("cmsPageCache")){
			webPageCacheLoader = new CacheLoader(){

				@Override
				public CacheLoader clone(Ehcache arg0)
						throws CloneNotSupportedException {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void dispose() throws CacheException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Status getStatus() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void init() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public Object load(Object object) throws CacheException {
					
					return null;
				}

				@Override
				public Object load(Object arg0, Object arg1) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Map loadAll(Collection arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Map loadAll(Collection arg0, Object arg1) {
					// TODO Auto-generated method stub
					return null;
				}
				
			};
			
			
			return webPageCacheLoader;
		}
		return null;
	}

	
}