/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class Profiler {

	private final static int STATE_STOPPED = 0;
	private final static int STATE_RUNNING = 1;

	private int state;

	private final String transactionName;
	private long startTime;
	private int iterations;
	
	public Profiler(String transactionName) {
		if (transactionName==null) throw new NullPointerException("Transaction name can't be null");
		this.transactionName = transactionName;
		this.state = STATE_STOPPED;
	}

	public synchronized void start() {
		if (this.state!=STATE_STOPPED) throw new IllegalStateException("Profiler not in STOPPED state");
		this.iterations=0;
		this.startTime=System.currentTimeMillis();	
		this.state=STATE_RUNNING;
	}
	
	public void iterate() {
		this.iterations++;
	}
	
	public synchronized void stop() {
		if (this.state!=STATE_RUNNING) throw new IllegalStateException("Profiler not in RUNNING state");

		long duration = System.currentTimeMillis() - this.startTime;
		Category logger = LoggerFactory.getInstance( "profiler." + this.transactionName );

		if (this.iterations==0) {
			logger.info("Duration: "+duration+" milliseconds");
		} else {
			double avg = (double)duration/(double)iterations;
			logger.info(this.iterations + "iterations, Duration: "+duration+" milliseconds, Average:" + avg);
		}

		this.state=STATE_STOPPED;
	}

}