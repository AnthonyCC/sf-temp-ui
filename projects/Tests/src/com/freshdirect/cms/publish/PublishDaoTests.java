package com.freshdirect.cms.publish;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.ObjectDeletedException;

import com.freshdirect.cms.CmsDaoTestCaseSupport;

public class PublishDaoTests extends CmsDaoTestCaseSupport {
	
	public PublishDaoTests(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CMS";
	}

	protected String[] getAffectedTables() {
		final String[] tables = { "CMS.Publish", "CMS.PublishMessages" };
		
		return tables;
	}	

	/**
	 *  Test simple creation and persistance of a Publish object.
	 */
	public void testPublishCreate() {
		PublishDao	publishDao = getPublishDao();
		Publish		publish    = new Publish();
		
		publish.setDescription("a test publish");
		publish.setLastModified(new Date());
		publish.setPath("/tmp/publish01");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(new Date());
		publish.setUserId("user01");
		
		publishDao.savePublish(publish);
		publishDao.currentSession().flush();
		
		Publish 		ppublish = publishDao.getPublish(publish.getId());
		
		assertEquals(publish, ppublish);
	}
	
	/**
	 *  Test simple publish delete
	 */
	public void testPublishDelete() {
		PublishDao	publishDao = getPublishDao();
		Publish		publish    = new Publish();
		Publish		ppublish;
		boolean		gotException;
		
		publish.setDescription("a test publish");
		publish.setLastModified(new Date());
		publish.setPath("/tmp/publish01");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(new Date());
		publish.setUserId("user01");
		
		publishDao.savePublish(publish);
		publishDao.currentSession().flush();
		
		ppublish = publishDao.getPublish(publish.getId());
		assertNotNull(ppublish);
		
		publishDao.deletePublish(publish);
		
		gotException = false;
		try {
			ppublish = publishDao.getPublish(publish.getId());
		} catch (ObjectDeletedException e) {
			gotException = true;
		}
		assertTrue(gotException);
	}
	
	/**
	 *  Test retrieving all publishes
	 */
	public void testAllPublishes() {
		PublishDao	publishDao = getPublishDao();
		Calendar		time       = GregorianCalendar.getInstance();
		Publish		publish;
		Publish		ppublish;
		String		id1, id2;
		List			publishes;
		
		publish = new Publish();
		publish.setDescription("a test publish");
		publish.setLastModified(time.getTime());
		publish.setPath("/tmp/publish01");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(time.getTime());
		publish.setUserId("user01");
		
		publishDao.savePublish(publish);
		id1 = publish.getId();

		// insert another one, with a date for yesterday
		time.add(Calendar.DATE, -1);
		publish = new Publish();
		publish.setDescription("a test publish, a bit earlier");
		publish.setLastModified(time.getTime());
		publish.setPath("/tmp/publish02");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(time.getTime());
		publish.setUserId("user02");
		
		publishDao.savePublish(publish);
		id2 = publish.getId();
		
		publishDao.currentSession().flush();
		
		// check the most recent publish
		publishes = publishDao.getAllPublishes();
		assertNotNull(publishes);
		assertEquals(publishes.size(), 2);
		
		// NOTE: the order in the retured list is not guaranteed
		ppublish = (Publish) publishes.get(0);
		assertEquals(ppublish.getId(), id1);
		ppublish = (Publish) publishes.get(1);
		assertEquals(ppublish.getId(), id2);
	}

	/**
	 *  Test retrieving all publishes
	 */
	public void testAllPublishesOrderred() {
		PublishDao	publishDao = getPublishDao();
		Calendar		time       = GregorianCalendar.getInstance();
		Publish		publish;
		Publish		ppublish;
		String		id1, id2;
		List			publishes;
		
		publish = new Publish();
		publish.setDescription("a test publish");
		publish.setLastModified(time.getTime());
		publish.setPath("/tmp/publish01");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(time.getTime());
		publish.setUserId("user01");
		
		publishDao.savePublish(publish);
		id1 = publish.getId();

		// insert another one, with a date for yesterday
		time.add(Calendar.DATE, -1);
		publish = new Publish();
		publish.setDescription("a test publish, a bit earlier");
		publish.setLastModified(time.getTime());
		publish.setPath("/tmp/publish02");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(time.getTime());
		publish.setUserId("user02");
		
		publishDao.savePublish(publish);
		id2 = publish.getId();
		
		publishDao.currentSession().flush();
		
		// check the most recent publish
		publishes = publishDao.getAllPublishesOrdered("timestamp");
		assertNotNull(publishes);
		assertEquals(publishes.size(), 2);

		// check if they are returned in the right order
		ppublish = (Publish) publishes.get(1);
		assertEquals(ppublish.getId(), id1);
		ppublish = (Publish) publishes.get(0);
		assertEquals(ppublish.getId(), id2);
	}

	/**
	 *  Test retrieving the most recent publish
	 */
	public void testGetMostRecentPublish() {
		PublishDao	publishDao = getPublishDao();
		Calendar		time       = GregorianCalendar.getInstance();
		Publish		publish;
		Publish		ppublish;
		String		id;
		
		publish = new Publish();
		publish.setDescription("a test publish");
		publish.setLastModified(time.getTime());
		publish.setPath("/tmp/publish01");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(time.getTime());
		publish.setUserId("user01");
		
		publishDao.savePublish(publish);
		id = publish.getId();

		// insert another one, with a date for yesterday
		time.add(Calendar.DATE, -1);
		publish = new Publish();
		publish.setDescription("a test publish, a bit earlier");
		publish.setLastModified(time.getTime());
		publish.setPath("/tmp/publish02");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(time.getTime());
		publish.setUserId("user02");
		
		publishDao.savePublish(publish);
		
		publishDao.currentSession().flush();
		
		// check the most recent publish
		ppublish = publishDao.getMostRecentPublish();
		assertNotNull(ppublish);
		// excpect the first inserted to be the most recent
		assertEquals(ppublish.getId(), id);
	}
	
	/**
	 *  A simple test to add and retrieve publish messages.
	 */
	public void testPublishMessages() {
		PublishDao	publishDao = getPublishDao();
		Publish		publish    = new Publish();
		
		publish.setDescription("a test publish");
		publish.setLastModified(new Date());
		publish.setPath("/tmp/publish01");
		publish.setStatus(EnumPublishStatus.COMPLETE);
		publish.setTimestamp(new Date());
		publish.setUserId("user01");
		
		publishDao.savePublish(publish);
		publishDao.currentSession().flush();
		
		List			    messages;
		PublishMessage	message;
		
		messages = publish.getMessages();
		
		messages.add(new PublishMessage(PublishMessage.INFO, "an info message"));
		messages.add(new PublishMessage(PublishMessage.WARNING, "a warning message"));
		
		publish.setMessages(messages);
		
		publishDao.savePublish(publish);
		publishDao.currentSession().flush();
		
		
		Publish 		ppublish = publishDao.getPublish(publish.getId());
		
		assertEquals(publish, ppublish);
		
		messages = ppublish.getMessages();
		assertEquals(messages.size(), 2);
		
		message = (PublishMessage) messages.get(0);
		assertEquals(message.getSeverity(), PublishMessage.INFO);
		assertEquals(message.getMessage(), "an info message");
		
		message = (PublishMessage) messages.get(1);
		assertEquals(message.getSeverity(), PublishMessage.WARNING);
		assertEquals(message.getMessage(), "a warning message");
	}

	/**
	 *  Test the getPreviousPublish function
	 */
	public void testPreviousPublish() {
		PublishDao	publishDao = getPublishDao();
		Calendar		time       = GregorianCalendar.getInstance();
		Publish		publish1, publish2, publish3;
		Publish		ppublish;
		
		publish1 = new Publish();
		publish1.setDescription("a test publish");
		publish1.setLastModified(time.getTime());
		publish1.setPath("/tmp/publish01");
		publish1.setStatus(EnumPublishStatus.COMPLETE);
		publish1.setTimestamp(time.getTime());
		publish1.setUserId("user01");
		
		publishDao.savePublish(publish1);

		// insert another one, with a date for tomorrow
		time.add(Calendar.DATE, 1);
		publish2 = new Publish();
		publish2.setDescription("a test publish, a bit earlier");
		publish2.setLastModified(time.getTime());
		publish2.setPath("/tmp/publish02");
		publish2.setStatus(EnumPublishStatus.COMPLETE);
		publish2.setTimestamp(time.getTime());
		publish2.setUserId("user02");
		
		publishDao.savePublish(publish2);
		
		// insert another one, with a date for the day after tomorrow
		time.add(Calendar.DATE, 2);
		publish3 = new Publish();
		publish3.setDescription("a test publish, a bit earlier");
		publish3.setLastModified(time.getTime());
		publish3.setPath("/tmp/publish03");
		publish3.setStatus(EnumPublishStatus.COMPLETE);
		publish3.setTimestamp(time.getTime());
		publish3.setUserId("user03");
		
		publishDao.savePublish(publish3);
		
		publishDao.currentSession().flush();
		
		// check for the previous publish
		ppublish = publishDao.getPreviousPublish(publish3);
		assertNotNull(ppublish);
		assertEquals(ppublish.getId(), publish2.getId());

		// check for the publish previous to the first one, must be null
		ppublish = publishDao.getPreviousPublish(publish1);
		assertNull(ppublish);
	}
	
}
