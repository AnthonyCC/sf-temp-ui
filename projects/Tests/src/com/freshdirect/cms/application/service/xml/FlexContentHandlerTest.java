package com.freshdirect.cms.application.service.xml;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;

public class FlexContentHandlerTest extends TestCase {

	protected ContentServiceI service;

	protected void setUp() throws Exception {
		service = new XmlContentService(
			new XmlTypeService("classpath:/com/freshdirect/cms/application/service/xml/TestDef1.xml"),
			new FlexContentHandler(),
			"classpath:/com/freshdirect/cms/application/service/xml/TestContent1.xml");
	}

	public void testGetContentNode() {
		Set allKeys = service.getContentKeys();
		assertEquals(5, allKeys.size());
		assertTrue(allKeys.contains(key("A")));
		assertTrue(allKeys.contains(key("B")));
		assertTrue(allKeys.contains(key("C")));
		assertTrue(allKeys.contains(key("D")));
		assertTrue(allKeys.contains(key("flex_0")));

		ContentNodeI fooA = service.getContentNode(key("A"));
		assertEquals(3, fooA.getAttributes().size());
		assertEquals("aaa", fooA.getAttributeValue("label"));
		assertNull(fooA.getAttributeValue("children"));

		ContentNodeI fooB = service.getContentNode(key("B"));
		assertEquals(3, fooB.getAttributes().size());
		assertEquals("bbb", fooB.getAttributeValue("label"));
		List l = (List) fooB.getAttributeValue("children");
		assertEquals(2, l.size());
		assertEquals(key("A"), l.get(0));
		assertEquals(key("B"), l.get(1));

		ContentNodeI fooC = service.getContentNode(key("C"));
		assertEquals(3, fooC.getAttributes().size());
		assertEquals("ccc", fooC.getAttributeValue("label"));
		l = (List) fooC.getAttributeValue("children");
		assertEquals(4, l.size());
		assertEquals(key("A"), l.get(0));
		assertEquals(key("D"), l.get(1));
		assertEquals(key("flex_0"), l.get(2));
		assertEquals(key("B"), l.get(3));

		ContentNodeI fooFlex0 = service.getContentNode(key("flex_0"));
		assertEquals(3, fooFlex0.getAttributes().size());
		assertEquals("xxx", fooFlex0.getAttributeValue("label"));
		assertNull(fooFlex0.getAttributeValue("children"));
	}

	public void testDate() {
		ContentNodeI fooD = service.getContentNode(key("D"));
		assertEquals(3, fooD.getAttributes().size());
		assertEquals("an element with a date attribute", fooD.getAttributeValue("label"));

		Calendar date = new GregorianCalendar(2006, 0, 25);
		assertEquals(fooD.getAttributeValue("date"), date.getTime());
	}

	public void testBadDate() {

		boolean gotException = false;
		try {
			service = new XmlContentService(
				new XmlTypeService("classpath:/com/freshdirect/cms/application/service/xml/TestDef1.xml"),
				new FlexContentHandler(),
				"classpath:/com/freshdirect/cms/application/service/xml/TestContentBadDate.xml");

			// the above will already throw an exception, because of the bad date.
		} catch (RuntimeException e) {
			gotException = true;
		}
		assertTrue(gotException);
	}

	private final static com.freshdirect.cms.ContentType FOO_TYPE = ContentType.get("Foo");

	private static ContentKey key(String id) {
		return new ContentKey(FOO_TYPE, id);
	}

}