package com.freshdirect.cms.classgenerator;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.ResourceInfoServiceI;
import com.freshdirect.cms.application.service.xml.CmsNodeHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;

public class ClassGeneratorContentService extends XmlContentService {

    ContentNodeGenerator generator;

    public ClassGeneratorContentService(ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles) {
        super(typeService);
        init(typeService, nodeHandler, resourceFiles, null, null);
    }

    public ClassGeneratorContentService(ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles,
            ResourceInfoServiceI resourceInfoService) {
        super(typeService);
        init(typeService, nodeHandler, resourceFiles, resourceInfoService, null);
    }

    public ClassGeneratorContentService(String prefix, ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles) {
        super(typeService);
        init(typeService, nodeHandler, resourceFiles, null, prefix);
    }

    public ClassGeneratorContentService(String prefix, ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles,
            ResourceInfoServiceI resourceInfoService) {
        super(typeService);
        init(typeService, nodeHandler, resourceFiles, resourceInfoService, prefix);
    }

    private void init(ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles, ResourceInfoServiceI resourceInfoService, String prefix) {
        generator = new ContentNodeGenerator(typeService, prefix);
        init(nodeHandler, resourceFiles, resourceInfoService);
    }

    public ContentNodeI createPrototypeContentNode(ContentKey key) {
        if (!getTypeService().getContentTypes().contains(key.getType())) {
            return null;
        }
        return generator.createNode(key);
    }
}
