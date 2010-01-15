package com.freshdirect.cms.application.service.classgen;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class ClassGenTest extends TestCase {

    // not the nicest solution, but it effectively 1/3 the execution time in eclipse.
    static XmlContentService service;
    
    ContentKey key = new ContentKey(FDContentTypes.CATEGORY, "gro_choc_fine");


    protected XmlContentService getService(String prefix) {
        if (service == null) {
            List list = new ArrayList();
            list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

            CompositeTypeService typeService = new CompositeTypeService(list);

            service = new ClassGeneratorContentService(prefix, typeService, new FlexContentHandler(),
                    "classpath:/com/freshdirect/cms/fdstore/content/FilteredStore2.xml");
        }
        return service;
    }

    @SuppressWarnings("deprecation")
    public void testFullName() {
        ContentNodeI node = getService("fullName").getContentNode(key);

        assertNotNull("gro_choc_fine exists", node);
        assertEquals("key", key, node.getKey());
        assertEquals("FULL_NAME value", "Chocolate", node.getAttributeValue("FULL_NAME"));
        AttributeI fullNameAttr = node.getAttribute("FULL_NAME");
        assertNotNull("FULL_NAME Attribute", fullNameAttr);
        assertEquals("FULL_NAME attribute value", "Chocolate", fullNameAttr.getValue());

        assertEquals("attrib point back", node, fullNameAttr.getContentNode());
        AttributeI fullNameAttr2 = node.getAttribute("FULL_NAME");
        assertEquals("two attrib equal", fullNameAttr, fullNameAttr2);
    }
    
    public void testSubcategories() {
        ContentNodeI node = getService("subcat").getContentNode(key);
        
        Object subcategories = node.getAttributeValue("subcategories");
        assertNotNull("subcategories", subcategories);
        assertTrue("subcategories is list", subcategories instanceof List);
        List sub = (List) subcategories;
        assertEquals("subcategories is 1 element list", 1, sub.size());
        assertEquals("subcategories is 1 element list", new ContentKey(FDContentTypes.CATEGORY, "gro_candy_blkch"), sub.get(0));
    }
    
    public void testCopy() {
        ContentNodeI node = getService("copy").getContentNode(key);
        assertEquals("FULL_NAME value", "Chocolate", node.getAttributeValue("FULL_NAME"));

        ContentNodeI copy = node.copy();
        assertEquals("FULL_NAME value", "Chocolate", copy.getAttributeValue("FULL_NAME"));
        
        copy.setAttributeValue("FULL_NAME", "Diet chocolate");
        
        assertEquals("orig FULL_NAME value", "Chocolate", node.getAttributeValue("FULL_NAME"));
        assertEquals("copy FULL_NAME value", "Diet chocolate", copy.getAttributeValue("FULL_NAME"));
        
        
        
    }

}
