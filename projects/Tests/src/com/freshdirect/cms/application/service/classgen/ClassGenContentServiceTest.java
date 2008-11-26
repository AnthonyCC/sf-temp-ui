package com.freshdirect.cms.application.service.classgen;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;

public class ClassGenContentServiceTest extends TestCase {

    public void testMergeOnLoad() {

        List ts = new ArrayList();
        ts.add(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef1.xml"));
        ts.add(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef2.xml"));
        ts.add(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef3.xml"));

        CompositeTypeService typeService = new CompositeTypeService(ts);

        XmlContentService service = new ClassGeneratorContentService(
                "ClassGenContentServiceTest_1",
                typeService,
                new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/TestContent1.xml,classpath:/com/freshdirect/cms/application/service/TestContent2.xml,classpath:/com/freshdirect/cms/application/service/TestContent3.xml");

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

    private final static ContentType FOO_TYPE             = ContentType.get("Foo");
    private final static ContentType BAR_TYPE             = ContentType.get("Bar");
    private final static ContentType FOO_DEFAULT_TYPE     = ContentType.get("FooDefault");
    private final static ContentType FOO_GENERATE_ID_TYPE = ContentType.get("FooGenerateId");

    private final static ContentKey  FOO_KEY              = new ContentKey(FOO_TYPE, "fooNode");
    private final static ContentKey  BAR_KEY              = new ContentKey(BAR_TYPE, "barNode");
    private final static ContentKey  FOO_DEFAULT_KEY      = new ContentKey(FOO_DEFAULT_TYPE, "fooNode");

    public void testGenerateUniqueId() {

        ContentTypeServiceI typeService;
        XmlContentService service;
        ContentNodeI node;
        ContentKey key;

        typeService = new XmlTypeService("classpath:/com/freshdirect/cms/application/service/xml/TestIdGeneration.xml");

        service = new ClassGeneratorContentService("ClassGenContentServiceTest_2", typeService, new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/TestIdGeneration.xml");

        node = service.getContentNode(FOO_DEFAULT_KEY);
        assertNotNull(node);

        // test that the generated key is really unique
        key = typeService.generateUniqueContentKey(FOO_GENERATE_ID_TYPE);
        node = service.getContentNode(key);
        assertNull(node);
    }

}
