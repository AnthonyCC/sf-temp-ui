package com.freshdirect.cms.application.service.xml;

import junit.framework.TestCase;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;

public class BackReferenceTest extends TestCase {

    ContentTypeServiceI typeService;
    XmlContentService service;
    ContentType fooType = ContentType.get("Foo");
    ContentType barType = ContentType.get("Bar");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        typeService = new XmlTypeService("classpath:/com/freshdirect/cms/application/service/xml/TestDefinition.xml");


    }

    public void testLink() throws InvalidContentKeyException {
        service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/xml/References.xml");

        assertEquals(TestUtils.toSet(new ContentType[] { fooType, barType }), typeService.getContentTypes());

        ContentKey barKey = ContentKey.create(barType, "barNode");
        ContentNodeI barNode = service.getContentNode(barKey);
        
        assertNotNull("barNode", barNode);
        assertNotNull("barNode.foo", barNode.getAttributeValue("foo"));
        assertNotNull("barNode.foo attribute", barNode.getAttribute("foo"));
        
        ContentKey fooKey = ContentKey.create(fooType, "fooNode1");
        assertEquals("barNode.foo = fooNode1", fooKey, barNode.getAttributeValue("foo"));
        assertEquals("barNode.foo = fooNode1", fooKey, barNode.getAttribute("foo").getValue());
        
        ContentNodeI fooNode = service.getContentNode(fooKey);
        assertNotNull("fooNode", fooNode);
        assertNotNull("fooNode.bar attribute", fooNode.getAttribute("bar"));
        assertNotNull("fooNode.bar", fooNode.getAttributeValue("bar"));
        assertEquals("fooNode.bar == barNode", barKey, fooNode.getAttributeValue("bar"));
        assertEquals("fooNode.bar == barNode", barKey, fooNode.getAttribute("bar").getValue());
        
        barNode.setAttributeValue("foo", null);
        
        assertNull("barNode.foo is null now", barNode.getAttributeValue("foo"));
        
        assertNull("fooNode.bar is null now", fooNode.getAttributeValue("bar"));
        
        barNode.setAttributeValue("foo", fooKey);

        assertEquals("barNode.foo = fooNode1 again", fooKey, barNode.getAttributeValue("foo"));
        assertEquals("fooNode.bar == barNode again", barKey, fooNode.getAttributeValue("bar"));
    }
    
    
    public void testGeneratedClassLink() throws InvalidContentKeyException {
        
        service = new ClassGeneratorContentService("backRefs", typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/xml/References.xml");
        
        assertEquals(TestUtils.toSet(new ContentType[] { fooType, barType }), typeService.getContentTypes());

        ContentKey barKey = ContentKey.create(barType, "barNode");
        ContentNodeI barNode = service.getContentNode(barKey);
        
        assertNotNull("barNode", barNode);
        assertNotNull("barNode.foo", barNode.getAttributeValue("foo"));
        ContentKey fooKey = ContentKey.create(fooType, "fooNode1");
        
        assertEquals("barNode.foo = fooNode1", fooKey, barNode.getAttributeValue("foo"));
        
        ContentNodeI fooNode = service.getContentNode(fooKey);
        assertNotNull("fooNode", fooNode);
        assertNotNull("fooNode.bar", fooNode.getAttributeValue("bar"));
        assertEquals("fooNode.bar == barNode", barKey, fooNode.getAttributeValue("bar"));
        
        barNode.setAttributeValue("foo", null);
        
        assertNull("barNode.foo is null now", barNode.getAttributeValue("foo"));
        
        assertNull("fooNode.bar is null now", fooNode.getAttributeValue("bar"));
        
        barNode.setAttributeValue("foo", fooKey);

        assertEquals("barNode.foo = fooNode1 again", fooKey, barNode.getAttributeValue("foo"));
        assertEquals("fooNode.bar == barNode again", barKey, fooNode.getAttributeValue("bar"));
    }
    
    public void testCopied() throws InvalidContentKeyException {
        service = new ClassGeneratorContentService("backRefCopies", typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/xml/References.xml");
        
        ContentKey barKey = ContentKey.create(barType, "barNode");
        ContentNodeI barNode = service.getContentNode(barKey);
        
        assertNotNull("barNode", barNode);
        assertNotNull("barNode.foo", barNode.getAttributeValue("foo"));
        ContentKey fooKey = ContentKey.create(fooType, "fooNode1");
        
        assertEquals("barNode.foo = fooNode1", fooKey, barNode.getAttributeValue("foo"));

        
        ContentNodeI barNodeCopiedInstance = barNode.copy();

        assertEquals("copied barNode.foo = fooNode1", fooKey, barNodeCopiedInstance.getAttributeValue("foo"));

        
        barNode.setAttributeValue("foo", null);
        
        
        assertNull("barNode.foo is null now", barNode.getAttributeValue("foo"));
        
        assertEquals("copied barNode.foo is still fooNode1", fooKey, barNodeCopiedInstance.getAttributeValue("foo"));
        
        ContentKey fooKey2 = ContentKey.create(fooType, "fooNode2");

        barNodeCopiedInstance.setAttributeValue("foo", fooKey2);

        assertEquals("copied barNode.foo is now fooNode2", fooKey2, barNodeCopiedInstance.getAttributeValue("foo"));
        
        assertNull("barNode.foo is null 2", barNode.getAttributeValue("foo"));
    }
    
    
    public void testOtherReference() throws InvalidContentKeyException {
        service = new ClassGeneratorContentService("backRefCopies2", typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/application/service/xml/References.xml");

        ContentNodeI barNode2 = service.getContentNode(ContentKey.create(barType, "barNode2"));
        ContentNodeI barNode3 = service.getContentNode(ContentKey.create(barType, "barNode3"));
        final ContentKey foo3 = ContentKey.create(fooType, "fooNode3");
        
        assertNotNull("barNode2.foo", barNode2.getAttributeValue("foo"));
        assertEquals("barNode2.foo", foo3, barNode2.getAttributeValue("foo"));
        assertNull("barNode3.foo", barNode3.getAttributeValue("foo"));

        barNode3.setAttributeValue("foo", foo3);
        assertEquals("barNode3.foo now correctly set to foo3", foo3, barNode3.getAttributeValue("foo"));
        assertNull("barNode2.foo is set to null", barNode2.getAttributeValue("foo"));
        
    }
    
}
