package com.freshdirect.cms.listeners;

import org.apache.log4j.Logger;

import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.Executor;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadFactory;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class MediaEventQueueExecutor implements MediaEventHandlerI {
	protected static class CopyTask implements Runnable {
		String sourceUri;
		String targetUri;
		String userId;

		public CopyTask(String sourceUri, String targetUri, String userId) {
			super();
			this.sourceUri = sourceUri;
			this.targetUri = targetUri;
			this.userId = userId;
		}

		public void run() {
			synchronized (handlerSync) {
				handler.copy(sourceUri, targetUri, userId);
			}
		}
	}

	protected static class CreateTask implements Runnable {
		Media media;
		String userId;
		
		public CreateTask(Media media, String userId) {
			super();
			this.media = media;
			this.userId = userId;
		}

		public void run() {
			synchronized (handlerSync) {
				handler.create(media, userId);
			}
		}
	}

	protected static class DeleteTask implements Runnable {
		String sourceUri;
		String userId;

		public DeleteTask(String sourceUri, String userId) {
			super();
			this.sourceUri = sourceUri;
			this.userId = userId;
		}

		public void run() {
			synchronized (handlerSync) {
				handler.delete(sourceUri, userId);
			}
		}
	}
	
	protected static class MoveTask implements Runnable {
		String sourceUri;
		String targetUri;
		String userId;

		public MoveTask(String sourceUri, String targetUri, String userId) {
			super();
			this.sourceUri = sourceUri;
			this.targetUri = targetUri;
			this.userId = userId;
		}
		
		public void run() {
			synchronized (handlerSync) {
				handler.move(sourceUri, targetUri, userId);
			}
		}
	}
	
	protected static class UpdateTask implements Runnable {
		Media media;
		String userId;
		
		public UpdateTask(Media media, String userId) {
			super();
			this.media = media;
			this.userId = userId;
		}
		
		public void run() {
			synchronized (handlerSync) {
				handler.update(media, userId);
			}
		}
	}
	
	protected static class Flag {
		boolean value;
		
		public Flag(boolean value) {
			this.value = value;
		}
		
		public boolean isSet() {
			return value;
		}
		
		public void set() {
			value = true;
		}
		
		public void reset() {
			value = false;
		}
	}
	
	private static class LowerPriorityThreadFactory implements ThreadFactory {
		ThreadFactory defaultFactory = Executors.defaultThreadFactory();

		public Thread newThread(Runnable r) {
			Thread t = defaultFactory.newThread(r);
			for (int i = 1; i <= 3; i++)
				try {
					t.setPriority(t.getPriority() - 1);
					LOGGER.info("decreased priority of queue executor thread (" + i + ")");
				} catch (Exception e) {
					LOGGER.error("failed to decrease priority for queue executor thread (" + i + ")");
				}
			return t;
		}
	}

	private static Executor threadPool = new ThreadPoolExecutor(1, 1, 360,
			TimeUnit.SECONDS, new LinkedBlockingQueue(), new LowerPriorityThreadFactory(),
			new ThreadPoolExecutor.DiscardPolicy());
	
	private static Object handlerSync = new Object();
	
	private static Flag queueSync = new Flag(false);

	private static Logger LOGGER = LoggerFactory.getInstance(MediaEventQueueExecutor.class);
	
	private static MediaEventHandlerI handler;
	
	private static void init() {
		synchronized (queueSync) {
			if (handler == null) {
				try {
					handler = (MediaEventHandlerI) FDRegistry.getInstance()
							.getService(MediaEventHandlerI.class);
					queueSync.set();
				} catch (RuntimeException e) {
					LOGGER.error("cannot initialize servlet, running in DUMMY mode");
					handler = DummyMediaEventHandler.getInstance();
					queueSync.reset();
				}
			}
		}
	}
	
	public MediaEventQueueExecutor() {
		init();
	}

	public void copy(String sourceUri, String targetUri, String userId) {
		synchronized (queueSync) {
			check();
			threadPool.execute(new CopyTask(sourceUri, targetUri, userId));
		}
	}

	public void create(Media media, String userId) {
		synchronized (queueSync) {
			check();
			threadPool.execute(new CreateTask(media, userId));
		}
	}

	public void delete(String sourceUri, String userId) {
		synchronized (queueSync) {
			check();
			threadPool.execute(new DeleteTask(sourceUri, userId));
		}
	}

	public boolean isBulkload() {
		synchronized (handlerSync) {
			check();
			return handler.isBulkload();
		}
	}

	public void move(String sourceUri, String targetUri, String userId) {
		synchronized (queueSync) {
			check();
			threadPool.execute(new MoveTask(sourceUri, targetUri, userId));
		}
	}

	public void update(Media media, String userId) {
		synchronized (queueSync) {
			check();
			threadPool.execute(new UpdateTask(media, userId));
		}
	}
	
	private void check() {
		if (!queueSync.isSet())
			throw new IllegalStateException("cannot access media event handler (initialization error)");
	}
}
