package com.freshdirect.erpswebapp.tests;

import org.junit.Test;

import com.freshdirect.framework.util.*;

import junit.framework.TestCase;

public class AsyncRefreshRandomCacheTest extends TestCase {

	private AsyncDBRefreshCache<String, Object> cache;
	
	private AsyncDBRefreshCache<String, Object> buildCache(String name, int num) {
		return new AsyncDBRefreshCache<String, Object>(name, num);
	}

	@Test
	public void testReadWriteEhcache() {
		cache = buildCache("testRandomCache1", 10);
		assertEquals(cache.size(), 0);

		cache.put("key1", new Object());
		cache.put("key2", new Object());
		cache.put("key3", new Object());
		cache.put("key4", new Object());
		cache.put("key5", new Object());

		assertNotNull(cache.get("key1"));
		assertNotNull(cache.get("key2"));
		assertNotNull(cache.get("key3"));
		assertNotNull(cache.get("key4"));
		assertNotNull(cache.get("key5"));
		
		assertEquals(cache.size(), 5);
	}

	@Test
	public void testCapacity() {
		cache = buildCache("testRandomCache2", 10);
		assertEquals(cache.size(), 0);

		cache.put("k1", new Object());
		cache.put("k2", new Object());
		cache.put("k3", new Object());
		cache.put("k4", new Object());
		cache.put("k5", new Object());
		assertEquals(cache.size(), 5);

		cache.put("k6", new Object());
		cache.put("k7", new Object());
		cache.put("k8", new Object());
		cache.put("k9", new Object());
		cache.put("k10", new Object());
		assertEquals(cache.size(), 10);

		cache.put("k11", new Object());
		assertEquals(cache.size(), 10);

		cache.put("k12", new Object());
		cache.put("k13", new Object());
		assertEquals(cache.size(), 10);
	}

	@Test
	public void testRemove() {
		cache = buildCache("testRandomCache3", 10);
		assertEquals(cache.size(), 0);

		cache.put("myKey", new Object());
		cache.put("yourKey", new Object());
		assertEquals(cache.size(), 2);

		cache.remove("myKey");
		assertEquals(cache.size(), 1);
		assertNotNull(cache.get("yourKey"));
	}

	@Test
	public void testUpdate() {
		cache = buildCache("testRandomCache4", 10);
		assertEquals(cache.size(), 0);

		Object obj1 = new Object();

		cache.put("key1", obj1);
		cache.put("key2", new Object());
		assertEquals(cache.size(), 2);
		assertEquals(cache.get("key1"), obj1);

		Object obj2 = new Object();
		cache.update("key1", obj2);

		// update should change the element
		assertEquals(cache.get("key1"), obj2);

		// update should not change the cache size
		assertEquals(cache.size(), 2);
	}
}
