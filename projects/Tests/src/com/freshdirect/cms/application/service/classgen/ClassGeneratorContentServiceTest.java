package com.freshdirect.cms.application.service.classgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.CompositeContentService;
import com.freshdirect.cms.application.service.CompositeContentServiceTest;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;

public class ClassGeneratorContentServiceTest extends CompositeContentServiceTest {

    public ContentServiceI createService() {
        Random rnd = new Random();
        XmlContentService s1 = new ClassGeneratorContentService("t1_"+Math.abs(rnd.nextInt(10000)),
                new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef1.xml"),
                new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/TestContent1.xml");

        XmlContentService s2 = new ClassGeneratorContentService("t2_"+Math.abs(rnd.nextInt(10000)),
                new XmlTypeService("classpath:/com/freshdirect/cms/application/service/TestDef2.xml"),
                new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/application/service/TestContent2.xml");

        XmlContentService s3 = new ClassGeneratorContentService("t3_"+Math.abs(rnd.nextInt(10000)),
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

        return new CompositeContentService(services);
    }
}
