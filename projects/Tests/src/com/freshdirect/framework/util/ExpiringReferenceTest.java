package com.freshdirect.framework.util;

import junit.framework.TestCase;

public class ExpiringReferenceTest extends TestCase {

	public void testExpiration() throws InterruptedException {

		ExpiringReference ref = new ExpiringReference(500) {

			private int c = 0;

			protected Object load() {
				return new Integer(c++);
			}

		};

		assertEquals(new Integer(0), ref.get());
		assertEquals(new Integer(0), ref.get());

		Thread.sleep(1000);
		assertEquals(new Integer(1), ref.get());
		assertEquals(new Integer(1), ref.get());

	}

}
