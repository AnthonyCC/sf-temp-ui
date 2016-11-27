package com.freshdirect.payment;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.BINInfoManagerHome;
import com.freshdirect.payment.ejb.BINInfoManagerSB;


public class BINCache {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(BINCache.class);

	private static final Logger LOGGER_LOADER = LoggerFactory.getInstance(Loader.class);
	
	private static final int HOUR_IN_MILLIS = 60 * 60 * 1000;

	private static Executor cacheThreadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardPolicy());

	private static BINCache INSTANCE;

	private static final Object reloadSync = new Object();

	public synchronized static BINCache getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BINCache();

		return INSTANCE;
	}

	private class Loader implements Runnable {

		@Override
		public void run() {
			LOGGER_LOADER.info("run() entry");
			// avoid doing reload in parallel
			synchronized (reloadSync) {
				LOGGER_LOADER.info("reloading BIN cache");
				try {
					@SuppressWarnings("unchecked")
					BINInfoManagerSB binInfoManagerSB = lookupBINInfoManagerHome().create();
					binInfoMap=(NavigableMap<Long, BINInfo>) binInfoManagerSB.getActiveBINs();
				} catch (FDResourceException e) {
					LOGGER_LOADER.error("failed to reload / initialize all BIN cache", e);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EJBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CreateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LOGGER_LOADER.info("BIN cache reloaded");
			}
			LOGGER_LOADER.info("run() exit");
		}
	}

	
	private NavigableMap<Long, BINInfo> binInfoMap;

	long lastRefresh;
	boolean initialized;

	
	private BINCache() {
		
		binInfoMap = new ConcurrentSkipListMap<Long, BINInfo>();
		lastRefresh = Integer.MIN_VALUE;
		initialized = false;
	}

	public boolean isDebitCard(String accountNumber, EnumCardType cardType ) {
		if( StringUtil.isEmpty(accountNumber) || cardType==null)
			return false;
		reload();
		if(!EnumCardType.VISA.equals(cardType) && !EnumCardType.MC.equals(cardType))
			return false;
		
		Long key=0l;
		try {
			key=Long.parseLong(accountNumber.substring(0, 9));
		} catch(Exception e) {
			return false;
		}
		/*if(EnumCardType.VISA.equals(cardType)) {
			return isValidBIN(key,visaBINMap);
		} else {
			return isValidBIN(key,mcBINMap);
		}*/
		return isValidBIN(key,cardType);
		
	}
	
	private boolean isValidBIN(Long key,EnumCardType cardType) {
		Map.Entry<Long,BINInfo> entry = binInfoMap.floorEntry(key);
		if (entry == null) {
			LOGGER_LOADER.debug(key+" is invalid");
		    return false;
		} else if (key <= entry.getValue().getHighRange()) {
			if(cardType.equals(entry.getValue().getCardType())) {
				LOGGER_LOADER.debug(key+" "+entry.getValue());
			    return true;
			} else {
				LOGGER_LOADER.debug(cardType+" is invalid");
				return false;
			}
		} else {
			LOGGER_LOADER.debug(key+" is invalid");
		    return false;
		}
	}

	// synchronized protects only the lastRefresh and initialized variables
	// as these variables are accessed only in this method
	public synchronized void reload() {
		long now = System.currentTimeMillis();

		if (now > (lastRefresh + HOUR_IN_MILLIS)) {
			forceReload();
			lastRefresh = now;
		}
	}

    /**
     * 
     */
    public void forceReload() {
        if (initialized) {
        	// reload cache asynchronously
        	LOGGER.info("reloading BIN cache asynchronously");
        	cacheThreadPool.execute(new Loader());
        } else {
        	initialized = true;
        	// reload cache synchronously
        	LOGGER.info("reloading BIN cache synchronously");
        	new Loader().run();
        }
    }
    
    public static BINInfoManagerHome lookupBINInfoManagerHome() throws EJBException {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			return (BINInfoManagerHome) ctx.lookup("freshdirect.payment.BINInfoManager");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOGGER.debug(ne);
			}
		}
	}
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	public static void main(String[] a) {
		System.out.println(BINCache.getInstance().isDebitCard("4207670020835933", EnumCardType.VISA));
	}
}
