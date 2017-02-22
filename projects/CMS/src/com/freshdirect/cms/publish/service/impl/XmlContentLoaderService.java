package com.freshdirect.cms.publish.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;

public class XmlContentLoaderService {

    private static final XmlContentLoaderService INSTANCE = new XmlContentLoaderService();

    public static XmlContentLoaderService getInstance() {
        return INSTANCE;
    }

    public Collection<ContentNodeI> fetchAllContentNodes(String storeDefFilePath, String storeXmlFilePath) {

        final ContentTypeServiceI typeService = new XmlTypeService(storeDefFilePath);
        final XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(), storeXmlFilePath);

        Set<ContentKey> contentKeys = service.getContentKeys(DraftContext.MAIN);
        Map<ContentKey, ContentNodeI> contentNodesInXml = service.getContentNodes(contentKeys, DraftContext.MAIN);

        return contentNodesInXml.values();
    }
}
