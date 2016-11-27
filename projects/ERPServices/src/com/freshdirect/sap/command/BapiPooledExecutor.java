package com.freshdirect.sap.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.bapi.BapiFunctionI;

public class BapiPooledExecutor {

	private final static Category LOGGER = LoggerFactory.getInstance(BapiPooledExecutor.class);

	private final Semaphore threadSemaphore;
	private final int threadsPerRequest;

	private int requestCounter = 0;

	public BapiPooledExecutor() {
		this(15, 3);
	}

	public BapiPooledExecutor(int maxThreads, int threadsPerRequest) {
		this.threadSemaphore = new Semaphore(maxThreads);
		this.threadsPerRequest = threadsPerRequest;
	}

	public void execute(BapiFunctionI[] bapis, long timeout) {

		if (bapis.length==0) {
			return;
		}
		
		String name = "BapiRequest[" + (requestCounter++) + "]";
		TaskGroup taskGroup = new TaskGroup(name, threadSemaphore, threadsPerRequest, bapis);

		try {
			synchronized (taskGroup) {
				taskGroup.execute();
				taskGroup.wait(timeout);
			}

		} catch (InterruptedException e) {
			LOGGER.warn(name + ": Execution interrupted", e);

		}

	}

	private static class TaskGroup {

		private final Semaphore threadSemaphore;
		private final String name;
		private final int threadsPerRequest;
		private final BapiFunctionI[] bapis;
		private final List tasks;

		public TaskGroup(String name, Semaphore threadSemaphore, int threadsPerRequest, BapiFunctionI[] bapis) {
			this.name = name;
			this.threadSemaphore = threadSemaphore;
			this.threadsPerRequest = threadsPerRequest;
			this.bapis = bapis;

			this.tasks = new ArrayList(bapis.length);
			for (int i = 0; i < bapis.length; i++) {
				this.tasks.add(bapis[i]);
			}

		}

		public void execute() {

			int numThreads = Math.min(threadsPerRequest, tasks.size());

			for (int i = 0; i < numThreads; i++) {
				Thread t = new BapiExecutionThread(name + "[" + i + "]");
				t.start();
			}

		}

		private synchronized void notifyIfFinished() {
			for (int i = 0; i < bapis.length; i++) {
				if (!bapis[i].isFinished()) {
					return;
				}
			}
			TaskGroup.this.notifyAll();
		}

		private class BapiExecutionThread extends Thread {

			public BapiExecutionThread(String name) {
				super(name);
			}

			public void run() {
				if (!threadSemaphore.attemptAcquire()) {
					LOGGER.warn("Threads exhausted");
					return;
				}
				try {

					while (true) {
						BapiFunctionI bapi;
						synchronized (TaskGroup.this) {
							if (tasks.size() == 0) {
								// nothing left to do
								LOGGER.debug("notify");
								TaskGroup.this.notifyIfFinished();
								break;
							}
							bapi = (BapiFunctionI) tasks.remove(0);
						}

						try {
							LOGGER.debug("execute");
							bapi.execute();
							LOGGER.debug("done");

						} catch (Exception ex) {
							LOGGER.warn("Error executing BAPI", ex);
						}
					}

				} finally {
					threadSemaphore.release();
				}
			}
		}
	}

	private static class Semaphore {
		private int count;

		public Semaphore(int max) {
			this.count = max;
		}

		public synchronized boolean attemptAcquire() {
			if (this.count > 0) {
				count--;
				return true;
			}
			return false;
		}

		public synchronized void release() {
			count++;
		}

	}

}
