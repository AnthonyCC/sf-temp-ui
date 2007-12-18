package com.freshdirect.cms.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.validation.ContentValidationException;

public class CompositeContentServiceTest extends TestCase {

	private ContentServiceI service;

	protected void setUp() throws Exception {

		XmlContentService s1 = new XmlContentService(
			new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef1.xml"),
			new FlexContentHandler(),
			"classpath:/com/freshdirect/cms/application/service/TestContent1.xml");

		XmlContentService s2 = new XmlContentService(
			new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef2.xml"),
			new FlexContentHandler(),
			"classpath:/com/freshdirect/cms/application/service/TestContent2.xml");

		XmlContentService s3 = new XmlContentService(
			new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef3.xml"),
			new FlexContentHandler(),
			"classpath:/com/freshdirect/cms/application/service/TestContent3.xml");

		s1.setName("s1");
		s2.setName("s2");
		s3.setName("s3");

		List services = new ArrayList();
		services.add(s1);
		services.add(s2);
		services.add(s3);

		service = new CompositeContentService(services);
	}

	private final static ContentType FOO_TYPE = ContentType.get("Foo");
	private final static ContentType BAR_TYPE = ContentType.get("Bar");

	private final static ContentKey FOO_KEY = new ContentKey(FOO_TYPE, "fooNode");
	private final static ContentKey BAR_KEY = new ContentKey(BAR_TYPE, "barNode");

	public void testGetAllContentKeys() {
		Set s = service.getContentKeys();
		assertEquals(2, s.size());
		assertTrue(s.contains(FOO_KEY));
		assertTrue(s.contains(BAR_KEY));
	}

	public void testContentTypeService() {
		Set types = service.getTypeService().getContentTypes();
		assertEquals(2, types.size());
		assertTrue(types.contains(FOO_TYPE));
		assertTrue(types.contains(BAR_TYPE));

		ContentTypeDefI fooDef = service.getTypeService().getContentTypeDefinition(FOO_TYPE);
		assertEquals(2, fooDef.getAttributeNames().size());
		assertNotNull(fooDef.getAttributeDef("FOO"));
		assertNotNull(fooDef.getAttributeDef("BAZ"));

		ContentTypeDefI barDef = service.getTypeService().getContentTypeDefinition(BAR_TYPE);
		assertEquals(2, barDef.getAttributeNames().size());
		assertNotNull(barDef.getAttributeDef("BAR"));
		assertNotNull(barDef.getAttributeDef("BAZ"));
	}

	public void testGetContentNode() {
		ContentNodeI fooNode = service.getContentNode(FOO_KEY);
		assertEquals(2, fooNode.getAttributes().size());
		assertEquals("fooValue", fooNode.getAttribute("FOO").getValue());
		assertEquals("bazValue", fooNode.getAttribute("BAZ").getValue());

		ContentNodeI barNode = service.getContentNode(BAR_KEY);
		assertEquals(2, barNode.getAttributes().size());
		assertEquals("barValue", barNode.getAttribute("BAR").getValue());
		assertEquals("bazValue", barNode.getAttribute("BAZ").getValue());

		assertNull(service.getContentNode(new ContentKey(FOO_TYPE, "nonexistent")));
	}

	public void testEditing() throws ContentValidationException {
		ContentKey zzzKey = new ContentKey(FOO_TYPE, "zzz");
		ContentNodeI node = service.createPrototypeContentNode(zzzKey);
		node.getAttribute("FOO").setValue("zzz_foo");
		node.getAttribute("BAZ").setValue("zzz_baz");

		//Cms.getService().storeContentNode(node);
		//ContentNodeI n = Cms.getService().getContentNode(zzzKey);

		assertEquals(2, node.getAttributes().size());
		assertEquals("zzz_foo", node.getAttribute("FOO").getValue());
		assertEquals("zzz_baz", node.getAttribute("BAZ").getValue());
	}

}