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
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.validation.ContentValidationException;

public class CompositeContentServiceTest extends TestCase {

    protected ContentServiceI service;

    protected void setUp() throws Exception {
        service = createService();
    }

    public ContentServiceI createService() {
        XmlContentService s1 = new XmlContentService(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef1.xml"),
                new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/TestContent1.xml");

        XmlContentService s2 = new XmlContentService(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef2.xml"),
                new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/TestContent2.xml");

        XmlContentService s3 = new XmlContentService(new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef3.xml"),
                new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/TestContent3.xml");

        s1.setName("s1");
        s2.setName("s2");
        s3.setName("s3");

        List services = new ArrayList();
        services.add(s1);
        services.add(s2);
        services.add(s3);

        return new CompositeContentService(services);
    }

    private final static ContentType FOO_TYPE = ContentType.get("Foo");
    private final static ContentType BAR_TYPE = ContentType.get("Bar");
    private final static ContentType VOODOO_TYPE = ContentType.get("VooDoo");

    private final static ContentKey  FOO_KEY  = ContentKey.getContentKey(FOO_TYPE, "fooNode");
    protected final static ContentKey  BAR_KEY  = ContentKey.getContentKey(BAR_TYPE, "barNode");

    public void testGetAllContentKeys() {
        Set s = service.getContentKeys(DraftContext.MAIN);
        assertEquals(2, s.size());
        assertTrue(s.contains(FOO_KEY));
        assertTrue(s.contains(BAR_KEY));
    }

    public void testContentTypeService() {
        Set types = service.getTypeService().getContentTypes();
        assertEquals(3, types.size());
        assertTrue(types.contains(FOO_TYPE));
        assertTrue(types.contains(BAR_TYPE));
        assertTrue(types.contains(VOODOO_TYPE));

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
        ContentNodeI fooNode = service.getContentNode(FOO_KEY, DraftContext.MAIN);
        assertEquals(2, fooNode.getAttributes().size());
        assertEquals("fooValue", fooNode.getAttributeValue("FOO"));
        assertEquals("bazValue", fooNode.getAttributeValue("BAZ"));

        ContentNodeI barNode = service.getContentNode(BAR_KEY, DraftContext.MAIN);
        assertEquals(2, barNode.getAttributes().size());
        assertEquals("barValue", barNode.getAttributeValue("BAR"));
        assertEquals("bazValue", barNode.getAttributeValue("BAZ"));

        assertNull(service.getContentNode(ContentKey.getContentKey(FOO_TYPE, "nonexistent"), DraftContext.MAIN));
    }

    public void testEditing() throws ContentValidationException {
        ContentKey zzzKey = ContentKey.getContentKey(FOO_TYPE, "zzz");
        ContentNodeI node = service.createPrototypeContentNode(zzzKey, DraftContext.MAIN);
        node.getAttribute("FOO").setValue("zzz_foo");
        node.getAttribute("BAZ").setValue("zzz_baz");

        // Cms.getService().storeContentNode(node);
        // ContentNodeI n = Cms.getService().getContentNode(zzzKey);

        assertEquals(2, node.getAttributes().size());
        assertEquals("zzz_foo", node.getAttributeValue("FOO"));
        assertEquals("zzz_baz", node.getAttributeValue("BAZ"));
    }

    public void testEditing2() throws ContentValidationException {
        ContentKey zzzKey = ContentKey.getContentKey(FOO_TYPE, "zzz2");
        ContentNodeI node = service.createPrototypeContentNode(zzzKey, DraftContext.MAIN);
        node.setAttributeValue("FOO", "zzz_foo");
        node.setAttributeValue("BAZ", "zzz_baz");

        // Cms.getService().storeContentNode(node);
        // ContentNodeI n = Cms.getService().getContentNode(zzzKey);

        assertEquals(2, node.getAttributes().size());
        assertEquals("zzz_foo", node.getAttributeValue("FOO"));
        assertEquals("zzz_baz", node.getAttributeValue("BAZ"));
    }
    
    
}