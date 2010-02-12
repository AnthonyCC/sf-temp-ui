package com.freshdirect.framework.util;

import junit.framework.TestCase;;

public class BalkingExpiringReferenceTestCase extends TestCase {
	private static class Message {
		private String message;
		
		public Message(String message) {
			super();
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	public void testBalkingExpiringReference() {
		final Message message = new Message("hello");
		
		ExpiringReference ref = new BalkingExpiringReference(800) {
			protected Object load() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
				return message.getMessage();
			}
		};
		
		assertNull(ref.get());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		assertNull(ref.get());
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
		}
		message.setMessage("ciao");
		assertEquals("hello", (String) ref.get());
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
		}
		assertEquals("hello", (String) ref.get());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		assertEquals("ciao", (String) ref.get());
	}
}
