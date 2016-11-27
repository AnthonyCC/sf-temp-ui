package com.freshdirect.cms.core;

import java.util.Random;

import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.classgenerator.ClassGeneratorContentService;

public class CGContextWalkerTest extends ContextWalkerTest {

    protected XmlContentService createService(CompositeTypeService typeService) {
        return new ClassGeneratorContentService("cgContextWalkerTest"+Math.abs(new Random().nextInt(10000)),
                typeService,
                new FlexContentHandler(),
                "classpath:/com/freshdirect/cms/fdstore/content/WalkerStore.xml");
    }
}
