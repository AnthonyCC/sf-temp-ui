package com.freshdirect.cms.filter;

import junit.framework.TestCase;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.FilterResource;

public class FilterResourceTest extends TestCase {
	private static abstract class TestResource<T> extends FilterResource<T> {
		/**
		 * Access counter
		 */
		private int counter = 0;
		
		/**
		 * Resource provider counter
		 */
		private int obtainCounter = 0;
		
		public int getCounter() {
			return counter;
		}
		
		public int getObtainCounter() {
			return obtainCounter;
		}
		
		@Override
		public T getResource() throws FDResourceException {
			obtainCounter++;
			return super.getResource();
		}



		@Override
		protected T obtainResource() throws FDResourceException,
				FDSkuNotFoundException {
			counter++;
			
			return getTestResource();
		}
		
		protected abstract T getTestResource();
	}

	public void testRetrieveMissingResource() {
		TestResource<Object> tr = new TestResource<Object>() {
			@Override
			protected Object getTestResource() {
				return null;
			}
		};

		assertEquals(0, tr.getCounter());
		assertEquals(0, tr.getObtainCounter());
		
		// first try
		try {
			tr.getResource();
			
			fail("Must throw exception!");
		} catch (FDResourceException e) {
			assertEquals(1, tr.getCounter());
			assertEquals(1, tr.getObtainCounter());
		}

		// second try - test marker
		try {
			tr.getResource();

			fail("Must throw exception!");
		} catch (FDResourceException e) {
			assertEquals(1, tr.getCounter());
			assertEquals(2, tr.getObtainCounter());
		}

		// third try - test marker
		try {
			tr.getResource();

			fail("Must throw exception!");
		} catch (FDResourceException e) {
			assertEquals(1, tr.getCounter());
			assertEquals(3, tr.getObtainCounter());
		}
	}

	/**
	 * Test retrieving 'good' resource
	 */
	public void testRetrieveResource() {
		final Object testObject = new Object();
		
		TestResource<Object> tr = new TestResource<Object>() {
			@Override
			protected Object getTestResource() {
				return testObject;
			}
		};

		assertEquals(0, tr.getCounter());
		assertEquals(0, tr.getObtainCounter());
		
		// first try
		try {
			Object obj = tr.getResource();
			
			assertEquals(testObject, obj);
			assertEquals(1, tr.getCounter());
			assertEquals(1, tr.getObtainCounter());
		} catch (FDResourceException e) {
			fail("Must not end here!");
		}

		// second try - test marker
		try {
			Object obj = tr.getResource();

			assertEquals(testObject, obj);
			assertEquals(1, tr.getCounter());
			assertEquals(2, tr.getObtainCounter());
		} catch (FDResourceException e) {
			fail("Must not end here!");
		}

		// third try - test marker
		try {
			Object obj = tr.getResource();

			assertEquals(testObject, obj);
			assertEquals(1, tr.getCounter());
			assertEquals(3, tr.getObtainCounter());
		} catch (FDResourceException e) {
			fail("Must not end here!");
		}
	}
}
