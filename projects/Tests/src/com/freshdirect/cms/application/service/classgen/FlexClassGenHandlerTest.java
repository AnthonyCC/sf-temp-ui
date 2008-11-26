package com.freshdirect.cms.application.service.classgen;

import java.util.Random;

import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.FlexContentHandlerTest;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;

public class FlexClassGenHandlerTest extends FlexContentHandlerTest {

    protected void setUp() throws Exception {
        service = new ClassGeneratorContentService("flex_" + Math.abs(new Random().nextInt(10000)), new XmlTypeService(
                "classpath:/com/freshdirect/cms/application/service/xml/TestDef1.xml"), new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/xml/TestContent1.xml");
    }

}
