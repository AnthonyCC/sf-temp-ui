package com.freshdirect.cms.publish.service.impl;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.classgenerator.ContentNodeGenerator;
import com.freshdirect.cms.classgenerator.GeneratedNodeGeneratorFactory;
import com.freshdirect.cms.publish.service.ContentNodeGeneratorService;


public final class GeneratedContentNodeGeneratorService implements ContentNodeGeneratorService {

    private final ContentNodeGenerator generator;

    public GeneratedContentNodeGeneratorService(ContentTypeServiceI typeService) {
        this.generator = GeneratedNodeGeneratorFactory.getInstance().getNodeGenerator(typeService, ContentNodeGenerator.DEFAULT_PREFIX);
    }

    @Override
    public ContentNodeI createContentNode(ContentKey key) {
        return generator.createNode(key, DraftContext.MAIN);
    }

}
