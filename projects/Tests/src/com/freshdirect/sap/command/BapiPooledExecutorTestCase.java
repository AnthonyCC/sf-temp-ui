package com.freshdirect.sap.command;

import junit.framework.TestCase;

import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiFunctionI;
import com.freshdirect.sap.bapi.BapiInfo;

public class BapiPooledExecutorTestCase extends TestCase {

	public BapiPooledExecutorTestCase(String arg0) {
		super(arg0);
	}

	public void testParallel() {
		BapiFunctionI[] bapis = {new DummyBapi(500), new DummyBapi(500), new DummyBapi(500)};

		BapiPooledExecutor pool = new BapiPooledExecutor(15, 3);

		pool.execute(bapis, 800);
		assertTrue(bapis[0].isFinished());
		assertTrue(bapis[1].isFinished());
		assertTrue(bapis[2].isFinished());
	}

	public void testTimeout() {
		BapiFunctionI[] bapis = {new DummyBapi(500), new DummyBapi(500), new DummyBapi(500)};

		BapiPooledExecutor pool = new BapiPooledExecutor(15, 1);

		pool.execute(bapis, 1200);
		assertTrue(bapis[0].isFinished());
		assertTrue(bapis[1].isFinished());
		assertTrue(!bapis[2].isFinished());
	}

	public void testDelayOnNoneBug() {
		BapiFunctionI[] bapis = {};

		BapiPooledExecutor pool = new BapiPooledExecutor(15, 1);

		long t = System.currentTimeMillis();
		pool.execute(bapis, 5000);
		t = System.currentTimeMillis() - t;
		assertTrue(t < 500);
	}

	private static class DummyBapi implements BapiFunctionI {

		private final long duration;

		private boolean finished = false;

		public DummyBapi(long duration) {
			this.duration = duration;
		}

		public void execute() throws BapiException {
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
			}
			this.finished = true;
		}

		public BapiInfo[] getInfos() {
			return new BapiInfo[0];
		}

		public boolean isFinished() {
			return this.finished;
		}

	}

}