package com.freshdirect.dataloader.geocodeloader;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.geocodeloader.ejb.GeoCodeLoaderHome;
import com.freshdirect.dataloader.geocodeloader.ejb.GeoCodeLoaderSB;
import com.freshdirect.framework.util.log.LoggerFactory;

public class GeoCodeLoaderThread implements Runnable {

	private int threadCount=0; 
	private int startIndex=0; 
	private int endIndex=0;
	
	private static final Category LOGGER = LoggerFactory.getInstance(GeoCodeLoaderThread.class);
	
	public GeoCodeLoaderThread(int threadCount,int startIndex,int endIndex){
		this.threadCount=threadCount;
		this.startIndex=startIndex;
		this.endIndex=endIndex;
	}
	
	public void run(){
		// TODO Auto-generated method stub
		Context ctx = null;
		try {
			ctx = getInitialContext();
			LOGGER.debug("Thread Number :"+this.threadCount+" is getting executed");			
			GeoCodeLoaderHome home=getGeoCodeLoaderHome(ctx);
			GeoCodeLoaderSB sb = home.create();						
			sb.geoCodeDeliveryInfo(this.startIndex,this.endIndex);				
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn(e.getMessage());			
		}
		finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
			}
		}		
	}
	
	public GeoCodeLoaderHome getGeoCodeLoaderHome(Context ctx)
	{
		
		GeoCodeLoaderHome home=null;
		try{
					
			home = (GeoCodeLoaderHome) ctx.lookup("freshdirect.dataloader.GeoCodeLoader");
		}	
		catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn(e.getMessage());
		} 
			
		return home;	
	}

	 public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}	
}
