package com.freshdirect.webapp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Aniwesh Vatsal
 *
 */
public class FeatureTaskThreadPool {

	private static ExecutorService threadpool =null;
	
	private static final int THREAD_POOL_SIZE = 10;
	
	/**
	 * @return ExecutorService
	 */
	public static ExecutorService getThreadPool(){
		if(threadpool == null){
			threadpool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		}
		return threadpool;
	} 
}
