package com.freshdirect.framework.event;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author knadeem Date May 3, 2005
 */
public class AsyncEventSink implements EventSinkI {
	
	private static Category LOGGER = LoggerFactory.getInstance(AsyncEventSink.class);

	private final EventSinkI sink;
	private final LinkedBlockingQueue<FDWebEvent> buffer;

	public AsyncEventSink(EventSinkI sink, int bufferSize) {
		this.sink = sink;
		this.buffer = new LinkedBlockingQueue<FDWebEvent>(bufferSize);
		//start the 
		Runnable r = new Poller();
		Thread t = new Thread(r);
		t.setDaemon(true);
		t.start();
	}

	public boolean log(FDWebEvent event) {
		return this.buffer.offer(event);
	}

	private class Poller implements Runnable {

		public void run() {
			while (true) {
				try {
					FDWebEvent event = (FDWebEvent) buffer.take();
					sink.log(event);
				} catch (RuntimeException e) {
					LOGGER.warn("Could not log event due to: ", e);
				} catch (InterruptedException e) {
					LOGGER.warn(e);
				}
			}
		}

	}

}
